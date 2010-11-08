/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.util.QvtlibHelper;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * This implementation creates {@link ElementSetReference}s as symbolic references. That is, an {@link OclCondition} is
 * created that contains an expression over all attributes of that element.
 * 
 * Example: An instance of an abstract {@link EClass} called <i>Person</i> results in the following condition:<br>
 * <code>self.abstract = true and self.interface = false and self.name = 'Person' and instanceTypeName = ''</code>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class ElementSetReferenceCreator extends AbstractReferenceCreator {

	/**
	 * All these types are treated as primitive types during symbolic reference creation.
	 */
	static final Collection<String> PRIMITIVE_TYPES = Arrays.asList(new String[] { "java.lang.String", "int",
			"java.lang.Integer", "boolean", "java.lang.Boolean", "long", "java.lang.Long", "byte", "java.lang.Byte",
			"char", "java.lang.Character", "short", "java.lang.Short", });

	/**
	 * Create a symbolic reference for the context.<br>
	 * For all elements, this default implementation creates an {@link ElementSetReference} with a default OCL
	 * expression which is used to identify the element.
	 * 
	 * @param self
	 *            The context, an arbitrary {@link EObject}.
	 * @return A symbolic reference to the context as an {@link IElementReference}.
	 */
	@Override
	public IElementReference createSymbolicReference(EObject self) {

		// create symbolic reference object for any given object
		return create(self, 1);
	}

	public String getLabel() {
		return "Condition-based";
	}

	// //////////////////////////// HERE STARTS THE SYMREF CREATION //////////////////////////////

	/**
	 * Create a symbolic reference which contains an ocl condition which holds the criteria for the referenced model
	 * element given in the parameter.
	 * 
	 * @param self
	 *            The element which should be referenced.
	 * @param contextDepth
	 *            The depth for which context references should be created. If <code>0</code>, then no context reference
	 *            is created.
	 * @return A symbolic reference to the model element.
	 */
	public ElementSetReference create(EObject self, int contextDepth) {
		final ElementSetReference ref = SymrefsFactory.eINSTANCE.createElementSetReference();
		ref.setType(self.eClass());
		ref.setUriReference(QvtlibHelper.getUriString(self));
		ref.setLabel(QvtlibHelper.getLabel(self));
		ref.getConditions().add(createOclCondition(self));

		// the context can of course only be set if the parent exists
		if (contextDepth > 0 && self.eContainer() != null) {
			ref.setContext(create(self.eContainer(), contextDepth - 1));
		}

		return ref;
	}

	/**
	 * This creates an {@link OclCondition} which describes the given object.<br>
	 * Subclasses may override or extend the condition.
	 * 
	 * @param self
	 *            the context
	 * @return an {@link OclCondition} which sufficiently describes the context
	 */
	protected OclCondition createOclCondition(EObject self) {
		final EClass eclass = self.eClass();

		// build ocl condition for all attributes as a string
		String expr = ""; // "self.oclIsTypeOf(" + eclass.getName() + ")";
		for (final EAttribute attribute : eclass.getEAllAttributes()) {
			if (isRelevantAttribute(attribute)) {
				expr += (expr.length() > 0 ? " and " : "") + eAttributeToCondition(attribute, self.eGet(attribute));
			}
		}

		// create ocl condition object
		final OclCondition condition = SymrefsFactory.eINSTANCE.createOclCondition();
		condition.setExpression(expr);

		return condition;
	}

	/**
	 * This defines which attributes are used for building the OCL condition. The default implementation uses only the
	 * primitive types contained in {@link ElementSetReferenceCreator#PRIMITIVE_TYPES}.
	 * 
	 * Furthermore, all derived attributes are ignored. This is due to an issue with UML models.
	 * 
	 * Subclasses may override this to refine the set of types.
	 * 
	 * @param eAttribute
	 *            An {@link EAttribute} in the transformation.
	 * @return <code>true</code>, if the attribute should be part of the condition; <code>false</code> otherwise.
	 */
	protected boolean isRelevantAttribute(EAttribute eAttribute) {
		return PRIMITIVE_TYPES.contains(eAttribute.getEType().getInstanceClassName()) && !eAttribute.isDerived();
	}

	/**
	 * Create an OCL condition from the given {@link Object} of type {@link EAttribute}.<br>
	 * Subclasses may use information from the <code>eAttribute</code> to refine the condition.
	 * 
	 * Example:<br>
	 * A String attribute called <code>foo</code> containing the value <code>"bar"</code> results in the condition <code>self.foo = 'bar'</code>
	 * .
	 * 
	 * @param eAttribute
	 *            The type of the object.
	 * @param obj
	 *            The object for which the condition should be created.
	 * @return An ocl condition which describes this object.
	 */
	protected String eAttributeToCondition(EAttribute eAttribute, Object obj) {
		final String prefix = ""; // "self.";
		if (obj instanceof List<?>) {
			// recursive call for lists :-)
			return prefix + eAttribute.getName() + "->asSequence() = " + listAttributeToString((List<?>) obj);

		} else if (obj == null) {
			// FIXME: unfortunately, the OCL implementation does not work with
			// OclVoid as null! workaround: create set and check if it is empty :o)
			return prefix + eAttribute.getName() + "->asSet()->isEmpty()";

		} else {
			// handle primitive types
			return prefix + eAttribute.getName() + " = " + primitiveAttributeToString(obj);
		}
	}

	/**
	 * If attributes have a cardinality &gt;1, it needs to be compared using a collection type.
	 * 
	 * Example:<br>
	 * A list containing <code>"foo"</code> and <code>"bar"</code> is returned as <code>Sequence{'foo','bar'}</code>.
	 * 
	 * @param list
	 *            A list of objects.
	 * @return An ocl sequence containing the given objects.
	 */
	protected String listAttributeToString(List<?> list) {
		String result = "";
		for (final Object o : (List<?>) list) {
			if (o instanceof List<?>) {
				result += (result.length() == 0 ? "" : ",") + listAttributeToString((List<?>) o);
			} else {
				result += (result.length() == 0 ? "" : ",") + primitiveAttributeToString(o);
			}
		}
		return "Sequence{" + result + "}";
	}

	/**
	 * Return a String representation of the given object <code>obj</code>.<br>
	 * Subclasses may refine this conversion.
	 * 
	 * @param obj
	 *            The object to convert.
	 * @return A valid ocl string representation of the value.
	 */
	protected String primitiveAttributeToString(Object obj) {
		if (obj == null) {
			return "OclVoid"; // this does not work yet, unfortunately!
		} else if (obj instanceof String) {
			return "'" + (String) obj + "'";
		} else if (obj instanceof Integer) {
			return obj.toString();
		} else if (obj instanceof Boolean) {
			return (Boolean) obj ? "true" : "false";
		} else if (obj instanceof EObject) {
			return "<EObject is not yet supported as attribute type!>";
		} else {
			return "<Type not supported: " + obj.getClass().getName() + ">";
		}
	}

}
