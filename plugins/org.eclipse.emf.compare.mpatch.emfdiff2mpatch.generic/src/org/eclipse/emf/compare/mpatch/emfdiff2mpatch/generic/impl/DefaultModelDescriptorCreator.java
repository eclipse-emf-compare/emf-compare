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

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorFactory;
import org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.util.QvtlibHelper;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The default model descriptor creator.
 * 
 * For a given model element, it creates a {@link IModelDescriptor} which describes that particular model element. That
 * is, the model descriptor can re-build the model element. For cross-references to other model elements it uses the
 * given {@link ISymbolicReferenceCreator}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class DefaultModelDescriptorCreator implements IModelDescriptorCreator {

	/**
	 * {@inheritDoc}
	 */
	public IModelDescriptor toModelDescriptor(EObject self, boolean serializable,
			ISymbolicReferenceCreator symbolicReferenceCreator) {

		// create model descriptor
		final EMFModelDescriptor descriptor = DescriptorFactory.eINSTANCE.createEMFModelDescriptor();
		descriptor.setType(self.eClass());
		descriptor.setDescriptorUri(symbolicReferenceCreator.getUriString(self));
		descriptor.setSelfReference(symbolicReferenceCreator.toSymbolicReference(self));

		// store containments, attributes, and references
		descriptor.getSubDescriptors().putAll(extractSubElements(self, serializable, symbolicReferenceCreator));
		descriptor.getAttributes().putAll(extractAttributes(self, serializable));
		descriptor.getReferences().putAll(extractReferences(self, serializable, symbolicReferenceCreator));

		return descriptor;
	}

	/**
	 * Create a map containing all contained elements of the given {@link EObject}.
	 * 
	 * Note that if <code>serializable</code> is set, this does not include unchangeable, derived, and transient
	 * containments!
	 * 
	 * @param obj
	 *            The {@link EObject} for which the containments should be extracted.
	 * @param serializable
	 *            If set to <code>true</code>, all containments are considered. Otherwise only changeable, non-derived,
	 *            and non-transient containments are considered.
	 * @param symbolicReferenceCreator
	 *            The {@link ISymbolicReferenceCreator} used to create symbolic references.
	 * @return A map containing all relevant containments as {@link EMFModelDescriptor}s.
	 */
	@SuppressWarnings("unchecked")
	// @SuppressWarnings because of the cast to EList<EObject>
	protected EMap<EReference, EList<EMFModelDescriptor>> extractSubElements(EObject obj, boolean serializable,
			ISymbolicReferenceCreator symbolicReferenceCreator) {
		final EMap<EReference, EList<EMFModelDescriptor>> result = new BasicEMap<EReference, EList<EMFModelDescriptor>>();

		// iterate over all references and store symbolic references to them.
		for (final EReference containment : obj.eClass().getEAllContainments()) {
			if (evaluateSerializable(serializable, containment)) {

				// distinguish between one and many containments
				final EList<EMFModelDescriptor> containments = new BasicEList<EMFModelDescriptor>();
				if (containment.isMany()) {
					for (final EObject listObj : (EList<EObject>) obj.eGet(containment)) {
						containments.add((EMFModelDescriptor) toModelDescriptor(listObj, serializable,
								symbolicReferenceCreator));
					}
				} else if (obj.eGet(containment) != null) {
					containments.add((EMFModelDescriptor) toModelDescriptor((EObject) obj.eGet(containment),
							serializable, symbolicReferenceCreator));
				}

				// only add non-empty entires
				if (!containments.isEmpty()) {
					result.put(containment, containments);
				}
			}
		}
		return result;
	}

	/**
	 * Create a map containing symbolic references ({@link IElementReference}s) to all referenced objects.
	 * 
	 * Note that if <code>serializable</code> is set, this does not include unchangeable, derived, and transient
	 * references!
	 * 
	 * @param obj
	 *            The {@link EObject} for which the references should be extracted.
	 * @param serializable
	 *            If set to <code>true</code>, all references are considered. Otherwise only changeable, non-derived,
	 *            and non-transient references are considered.
	 * @param symbolicReferenceCreator
	 *            The {@link ISymbolicReferenceCreator} used to create the symbolic references.
	 * @return A map containing all relevant references as {@link IElementReference}s.
	 */
	@SuppressWarnings("unchecked")
	// @SuppressWarnings because of cast to EList<EObject>
	protected EMap<EReference, EList<IElementReference>> extractReferences(EObject obj, boolean serializable,
			ISymbolicReferenceCreator symbolicReferenceCreator) {
		final EMap<EReference, EList<IElementReference>> result = new BasicEMap<EReference, EList<IElementReference>>();

		// iterate over all references and store symbolic references to them.
		for (final EReference reference : obj.eClass().getEAllReferences()) {
			if (!reference.isContainment() && !reference.isContainer() && evaluateSerializable(serializable, reference)) {

				// distinguish between one and many references
				final EList<IElementReference> references = new BasicEList<IElementReference>();
				if (reference.isMany()) {
					for (final EObject listObj : (EList<EObject>) obj.eGet(reference)) {
						references.add(symbolicReferenceCreator.toSymbolicReference(listObj));
					}
				} else if (obj.eGet(reference) != null) {
					references.add(symbolicReferenceCreator.toSymbolicReference((EObject) obj.eGet(reference)));
				}

				// only add non-empty entires
				if (!references.isEmpty()) {
					result.put(reference, references);
				}
			}
		}
		return result;
	}

	/**
	 * Create a map containing all attributes of the given {@link EObject}.
	 * 
	 * Note that if <code>serializable</code> is set, this does not include unchangeable, derived, and transient
	 * attributes!
	 * 
	 * @param obj
	 *            The {@link EObject} for which the attributes should be extracted.
	 * @param serializable
	 *            If set to <code>true</code>, all attributes are considered. Otherwise only changeable, non-derived,
	 *            and non-transient attributes are considered.
	 * @return A map containing all relevant attribute values.
	 */
	protected EMap<EAttribute, EList<Object>> extractAttributes(EObject obj, boolean serializable) {
		final EMap<EAttribute, EList<Object>> result = new BasicEMap<EAttribute, EList<Object>>();

		// iterate over all attributes and store a clone of them.
		for (final EAttribute attribute : obj.eClass().getEAllAttributes()) {
			if (evaluateSerializable(serializable, attribute)) {

				// distinguish between one and many attributes
				final EList<Object> values = new BasicEList<Object>();
				if (attribute.isMany()) {
					for (final Object listObj : (EList<?>) obj.eGet(attribute)) {
						values.add(deepCloneRequired(attribute) ? clone(listObj, attribute.getEAttributeType())
								: listObj);
					}
				} else if (obj.eGet(attribute) != null && !obj.eGet(attribute).equals(attribute.getDefaultValue())) {
					values.add(deepCloneRequired(attribute) ? clone(obj.eGet(attribute), attribute.getEAttributeType())
							: obj.eGet(attribute));
				}

				// only add non-empty entires
				if (!values.isEmpty()) {
					result.put(attribute, values);
				}
			}
		}
		return result;
	}

	/**
	 * Determine whether this feature should be considered according to the serializable parameter and the properties
	 * transient, derived, and changeable.
	 * 
	 * @param serializable
	 *            If set to <code>true</code>, all containments are considered. Otherwise only changeable, non-derived,
	 *            and non-transient containments are considered.
	 * @param feature
	 *            The considered attribute or reference.
	 * @return <code>true</code>, if the feature should be considered, <code>false</code> otherwise.
	 */
	protected boolean evaluateSerializable(boolean serializable, EStructuralFeature feature) {
		if (!serializable)
			return true;

		return MPatchUtil.isRelevantFeature(feature);
	}

	/**
	 * Specifies whether a deep clone of a particular attribute is required or not. The default implementation requires
	 * a deep clone of any data type other than {@link String}, {@link Integer}, and {@link Boolean}.
	 * 
	 * @param eAttribute
	 *            The according {@link EAttribute}.
	 * @return <code>true</code>, if a deep clone is required, <code>false</code> otherwise.
	 */
	protected boolean deepCloneRequired(EAttribute eAttribute) {
		return !String.class.isAssignableFrom(eAttribute.getEAttributeType().getInstanceClass())
				&& !Integer.class.isAssignableFrom(eAttribute.getEAttributeType().getInstanceClass())
				&& !Boolean.class.isAssignableFrom(eAttribute.getEAttributeType().getInstanceClass())
				&& !Enum.class.isAssignableFrom(eAttribute.getEAttributeType().getInstanceClass());
	}

	/**
	 * Create a deep clone from a given java {@link Object}. This default implementation uses
	 * {@link QvtlibHelper#clone(Object)} to serialize and deserialize the object. Subclasses may provide a different
	 * way of cloning objects.
	 * 
	 * @param copyObject
	 *            The object to clone.
	 * @param eDataType
	 *            The data type of the object to clone.
	 * @return A clone of the given object.
	 */
	public Object clone(Object copyObject, EDataType eDataType) {
		return QvtlibHelper.clone(copyObject, eDataType);
	}

	public String getLabel() {
		return "Default";
	}
}
