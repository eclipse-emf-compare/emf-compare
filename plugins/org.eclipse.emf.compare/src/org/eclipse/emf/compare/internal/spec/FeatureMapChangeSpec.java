/*******************************************************************************
 * Copyright (c) 2014, 2015 EclipseSource Munich GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.internal.spec;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.impl.FeatureMapChangeImpl;
import org.eclipse.emf.compare.utils.Objects;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;

/**
 * This specialization of the {@link FeatureMapChangeImpl} class allows us to define the derived features and
 * operations implementations.
 * 
 * @author <a href="mailto:planger@eclipsesource.com">Philip Langer</a>
 */
public class FeatureMapChangeSpec extends FeatureMapChangeImpl {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#discard()
	 */
	@Override
	public void discard() {
		setState(DifferenceState.DISCARDED);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		// @formatter:off
		return Objects.toStringHelper(this).add("map", getQNameOfFeature(getAttribute()))
				.add("changedKey", getQNameOfFeature(getChangedKey())).add("value", getValueAsString())
				.add("parentMatch", getMatch().toString()).add("match of value", getValueMatch())
				.add("kind", getKind()).add("source", getSource()).add("state", getState()).toString();
		// @formatter:on
	}

	/**
	 * Returns qualified name of the given {@code feature} in the form of &lt;class&gt;.&lt;featureName&gt;.
	 * 
	 * @param feature
	 *            The feature to get the qualified name of.
	 * @return The qualified name of the given feature name.
	 */
	private String getQNameOfFeature(EStructuralFeature feature) {
		if (feature != null) {
			return feature.getEContainingClass().getName() + "." + feature.getName(); //$NON-NLS-1$
		} else {
			return null;
		}

	}

	/**
	 * Returns the feature map entry affected by this feature map change.
	 * 
	 * @return The changed feature map entry.
	 */
	private FeatureMap.Entry getChangedEntry() {
		if (getValue() != null && getValue() instanceof FeatureMap.Entry) {
			return (FeatureMap.Entry)getValue();
		} else {
			return null;
		}
	}

	/**
	 * Returns the value of the feature map entry changed by this feature map change.
	 * 
	 * @return The value of the changed feature map entry.
	 */
	private Object getChangedValue() {
		Object changedValue = null;
		final Entry changedEntry = getChangedEntry();
		if (changedEntry != null) {
			changedValue = changedEntry.getValue();
		}
		return changedValue;
	}

	/**
	 * Returns the match of the value of the feature map entry changed by this diff.
	 * <p>
	 * Note that there is only a match, if the affected value is an EObject.
	 * </p>
	 * 
	 * @return The match of the value or <code>null</code>, if the value is not an EObject or there is no
	 *         match.
	 */
	private Match getValueMatch() {
		Match valueMatch = null;
		final Object changedValue = getChangedValue();
		if (changedValue != null && changedValue instanceof EObject) {
			valueMatch = getMatch().getComparison().getMatch((EObject)changedValue);
		}
		return valueMatch;
	}

	/**
	 * Returns the feature key affected by this feature map change.
	 * 
	 * @return The feature that is affected by this feature map change.
	 */
	private EStructuralFeature getChangedKey() {
		EStructuralFeature changedKey = null;
		final Entry changedEntry = getChangedEntry();
		if (changedEntry != null) {
			changedKey = changedEntry.getEStructuralFeature();
		}
		return changedKey;
	}

	/**
	 * Returns a textual representation of the value affected by this diff.
	 * 
	 * @return The affected value represented as a string.
	 */
	private String getValueAsString() {
		String valueAsString = "null"; //$NON-NLS-1$
		final Object changedValue = getChangedValue();
		if (changedValue != null) {
			if (changedValue instanceof EObject) {
				valueAsString = EObjectUtil.getLabel((EObject)changedValue);
			} else {
				valueAsString = changedValue.toString();
			}
		}
		return valueAsString;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#basicGetMatch()
	 */
	@Override
	public Match basicGetMatch() {
		if (eContainer() instanceof Match) {
			return (Match)eContainer();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.DiffImpl#setMatch(org.eclipse.emf.compare.Match)
	 */
	@Override
	public void setMatch(Match newMatch) {
		Match oldMatch = basicGetMatch();
		if (newMatch != null) {
			EList<Diff> differences = newMatch.getDifferences();
			differences.add(this);
			eNotify(new ENotificationImpl(this, Notification.SET, ComparePackage.DIFF__MATCH, oldMatch,
					newMatch));
		} else if (eContainer() instanceof Match) {
			EList<Diff> differences = ((Match)eContainer()).getDifferences();
			differences.remove(this);
			eNotify(new ENotificationImpl(this, Notification.UNSET, ComparePackage.DIFF__MATCH, oldMatch,
					newMatch));

		}
	}

}
