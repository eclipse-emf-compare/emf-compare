/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.hash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A structural feature and its normalized, immutable values. The owning object's identity is not part of
 * equality; the structural feature itself still distinguishes features with equal names.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class FeatureValue {

	/** Combines the structural feature and its normalized values in the cached hash. */
	private static final int HASH_PRIME = 31;

	/** Containment or stored structural feature. */
	private final EStructuralFeature feature;

	/** Normalized exact feature values. */
	private final List<Object> values;

	/** Cached exact hash of the immutable values. */
	private final int hashCode;

	/**
	 * Copies the supplied list before caching its exact hash. Elements, including nested lists, must
	 * already be immutable normalized snapshots; this constructor does not copy or normalize them.
	 *
	 * @param feature
	 *            the structural feature
	 * @param values
	 *            the normalized feature values
	 */
	public FeatureValue(EStructuralFeature feature, List<?> values) {
		this.feature = feature;
		this.values = Collections.unmodifiableList(new ArrayList<>(values));
		// Nested immutable snapshots are indexed repeatedly; compute their exact hash once.
		this.hashCode = HASH_PRIME * Objects.hashCode(feature) + Objects.hashCode(this.values);
	}

	/**
	 * Copies normalized feature values for use by a custom {@link IContentHasher}. Values, including nested
	 * lists, must already be immutable snapshots rather than live model objects. Feature identity
	 * distinguishes structural features with equal names.
	 *
	 * @param feature
	 *            the structural feature
	 * @param values
	 *            the normalized feature values
	 * @return an immutable feature snapshot
	 */
	public static FeatureValue of(EStructuralFeature feature, List<?> values) {
		return new FeatureValue(feature, values);
	}

	/**
	 * Captures an attribute scalar without retaining mutable datatype instances. Strings and null already
	 * are stable; other values use their datatype's serialized form, paired with their Java type so an
	 * EJavaObject attribute can distinguish, for example, an Integer from a Long. Custom datatypes must
	 * supply a stable, lossless factory conversion. Reference values are normalized by the hasher instead.
	 *
	 * @param feature
	 *            the effective feature holding the scalar
	 * @param value
	 *            the scalar or default value to capture
	 * @return an immutable scalar key, or the unchanged value for a non-attribute feature
	 */
	static Object captureScalarValue(EStructuralFeature feature, Object value) {
		if (value == null || value instanceof String || !(feature instanceof EAttribute)) {
			return value;
		}
		String serialized = EcoreUtil.convertToString(((EAttribute)feature).getEAttributeType(), value);
		return Collections.unmodifiableList(Arrays.asList(value.getClass(), serialized));
	}

	/**
	 * Checks whether this feature provides evidence beyond a name or default value. The matcher uses this to
	 * avoid treating similarly numbered names alone as evidence of a rename. Null values and empty lists
	 * provide no such evidence.
	 *
	 * @return whether the feature provides discriminating content
	 */
	public boolean isDiscriminating() {
		if ("name".equals(feature.getName())) { //$NON-NLS-1$
			return false;
		}
		Object defaultValue = feature.getDefaultValue();
		Object capturedDefaultValue = captureScalarValue(feature, defaultValue);
		for (Object value : values) {
			if (value == null || Objects.equals(value, defaultValue)
					|| Objects.equals(value, capturedDefaultValue)) {
				continue;
			}
			if (value instanceof List<?> && ((List<?>)value).isEmpty()) {
				continue;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof FeatureValue)) {
			return false;
		}
		FeatureValue other = (FeatureValue)object;
		if (hashCode != other.hashCode || !Objects.equals(feature, other.feature)
				|| values.size() != other.values.size()) {
			return false;
		}
		// Scalar snapshots dominate; indexed comparison avoids allocating two list iterators.
		for (int i = 0; i < values.size(); i++) {
			if (!Objects.equals(values.get(i), other.values.get(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return "FeatureValue[feature=" + feature + ", values=" + values + ']'; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
