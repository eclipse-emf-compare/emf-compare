/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject;

import com.google.common.base.Function;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * A function computing an URI Fragment. This implementation is based on
 * org.eclipse.emf.ecore.impl.BasicEObjectImpl.eURIFragmentSegment(EStructuralFeature, EObject) no ID or
 * specific code for a given EObject instance will be used, and that's on purpose, in the case of UML models
 * for instance the specialization of the eURIFragment() method leads to a massive loss of performance for no
 * added value in our current use case (matching references).
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class EUriFragmentFunction implements Function<EObject, String> {

	/**
	 * {@inheritDoc}
	 */
	public String apply(EObject input) {
		EObject container = input.eContainer();
		EStructuralFeature feat = input.eContainingFeature();
		if (container instanceof BasicEObjectImpl) {
			// String frag = ((BasicEObjectImpl)container).eURIFragmentSegment(feat, input);
			return eURIFragmentSegment(container, feat, input);
		}
		return null;
	}

	// CHECKSTYLE:OFF
	// TODO Comment the checkstyle suppression. Was that copy/pasted from somewhere?
	public String eURIFragmentSegment(EObject container, EStructuralFeature eStructuralFeature,
			EObject eObject) {
		if (eStructuralFeature == null) {
			for (@SuppressWarnings("unchecked")
			EContentsEList.FeatureIterator<EObject> crossReferences = (EContentsEList.FeatureIterator<EObject>)((InternalEList<?>)container
					.eCrossReferences()).basicIterator(); crossReferences.hasNext();) {
				EObject crossReference = crossReferences.next();
				if (crossReference == eObject) {
					eStructuralFeature = crossReferences.feature();
				}
			}
		}

		StringBuilder result = new StringBuilder();
		result.append('@');
		result.append(eStructuralFeature.getName());

		if (eStructuralFeature instanceof EAttribute) {
			FeatureMap featureMap = (FeatureMap)container.eGet(eStructuralFeature, false);
			for (int i = 0, size = featureMap.size(); i < size; ++i) {
				if (featureMap.getValue(i) == eObject) {
					EStructuralFeature entryFeature = featureMap.getEStructuralFeature(i);
					if (entryFeature instanceof EReference && ((EReference)entryFeature).isContainment()) {
						result.append('.');
						result.append(i);
						return result.toString();
					}
				}
			}
			result.append(".-1");
		} else if (eStructuralFeature.isMany()) {
			EList<EAttribute> eKeys = ((EReference)eStructuralFeature).getEKeys();
			if (eKeys.isEmpty()) {
				EList<?> eList = (EList<?>)container.eGet(eStructuralFeature, false);
				int index = eList.indexOf(eObject);
				result.append('.');
				result.append(index);
			} else {
				EAttribute[] eAttributes = (EAttribute[])((BasicEList<?>)eKeys).data();
				result.append('[');
				for (int i = 0, size = eAttributes.length; i < size; ++i) {
					EAttribute eAttribute = eAttributes[i];
					if (eAttribute == null) {
						break;
					} else {
						if (i != 0) {
							result.append(',');
						}
						result.append(eAttribute.getName());
						result.append('=');
						EDataType eDataType = eAttribute.getEAttributeType();
						EFactory eFactory = eDataType.getEPackage().getEFactoryInstance();
						if (eAttribute.isMany()) {
							List<?> values = (List<?>)eObject.eGet(eAttribute);
							result.append('[');
							if (!values.isEmpty()) {
								Iterator<?> j = values.iterator();
								eEncodeValue(result, eFactory, eDataType, j.next());
								while (j.hasNext()) {
									result.append(',');
									eEncodeValue(result, eFactory, eDataType, j.next());
								}
							}
							result.append(']');
						} else {
							eEncodeValue(result, eFactory, eDataType, eObject.eGet(eAttribute));
						}
					}
				}
				result.append(']');
			}
		}

		return result.toString();
	}

	private void eEncodeValue(StringBuilder result, EFactory eFactory, EDataType eDataType, Object value) {
		String stringValue = eFactory.convertToString(eDataType, value);
		if (stringValue == null) {
			result.append("null");
		} else {
			int length = stringValue.length();
			result.ensureCapacity(result.length() + length + 2);
			result.append('\'');
			for (int i = 0; i < length; ++i) {
				char character = stringValue.charAt(i);
				if (character < ESCAPE.length) {
					String escape = ESCAPE[character];
					if (escape != null) {
						result.append(escape);
						continue;
					}
				}
				result.append(character);
			}
			result.append('\'');
		}
	}

	/**
	 * Returns the encoded value or the original, if no encoding was needed.
	 * 
	 * @param value
	 *            the value to be encoded.
	 * @return the encoded value or the original, if no encoding was needed.
	 */
	private String eEncodeValue(String value) {
		int length = value.length();
		StringBuilder result = null;
		for (int i = 0; i < length; ++i) {
			char character = value.charAt(i);
			if (character < ESCAPE.length) {
				String escape = ESCAPE[character];
				if (escape != null) {
					if (result == null) {
						result = new StringBuilder(length + 2);
						result.append(value, 0, i);
					}
					result.append(escape);
					continue;
				}
			}
			if (result != null) {
				result.append(character);
			}
		}
		return result == null ? value : result.toString();
	}

	private final String[] ESCAPE = {"%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07", "%08", "%09",
			"%0A", "%0B", "%0C", "%0D", "%0E", "%0F", "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
			"%18", "%19", "%1A", "%1B", "%1C", "%1D", "%1E", "%1F", "%20", null, "%22", "%23", null, "%25",
			"%26", "%27", null, null, null, null, "%2C", null, null, "%2F", null, null, null, null, null,
			null, null, null, null, null, "%3A", null, "%3C", null, "%3E", null, };
	// CHECKSTYLE:ON
}
