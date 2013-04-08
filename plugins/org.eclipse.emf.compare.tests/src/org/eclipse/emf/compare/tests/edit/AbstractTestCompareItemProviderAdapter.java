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
package org.eclipse.emf.compare.tests.edit;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.find;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.provider.CompareItemProviderAdapterFactory;
import org.eclipse.emf.compare.provider.spec.CompareItemProviderAdapterFactorySpec;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.edit.data.ResourceScopeProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.junit.Before;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class AbstractTestCompareItemProviderAdapter {

	protected CompareItemProviderAdapterFactory compareItemProviderAdapterFactory;

	@Before
	public void before() throws IOException {
		compareItemProviderAdapterFactory = new CompareItemProviderAdapterFactorySpec();
	}

	/**
	 * @return the comparison
	 * @throws IOException
	 */
	protected static Comparison getComparison(ResourceScopeProvider scopeProvider) throws IOException {
		IComparisonScope scope = EMFCompare.createDefaultScope(scopeProvider.getLeft(), scopeProvider
				.getRight(), scopeProvider.getOrigin());
		return EMFCompare.builder().build().compare(scope);
	}

	protected Match getMatchWithFeatureValue(Collection<?> c, final String featureName, final Object value) {
		Iterable<Match> matches = filter(c, Match.class);
		Predicate<Match> predicate = hasFeatureValue(featureName, value);
		return find(matches, predicate);
	}

	protected ReferenceChange getReferenceChangeWithFeatureValue(Collection<?> c, final String featureName,
			final Object value) {
		Iterable<ReferenceChange> matches = filter(c, ReferenceChange.class);
		Predicate<ReferenceChange> predicate = new Predicate<ReferenceChange>() {
			public boolean apply(ReferenceChange referenceChange) {
				EObject referenceChangeValue = referenceChange.getValue();
				if (referenceChangeValue != null) {
					return Objects.equal(eGet(referenceChangeValue, featureName), value);
				}
				return false;
			}
		};
		return find(matches, predicate);
	}

	protected Predicate<Match> hasFeatureValue(final String featureName, final Object value) {
		Predicate<Match> predicate = new Predicate<Match>() {
			public boolean apply(Match match) {
				final boolean ret;
				final EObject left = match.getLeft();
				final EObject right = match.getRight();
				final EObject origin = match.getOrigin();
				if (left != null) {
					ret = Objects.equal(value, eGet(left, featureName));
				} else if (right != null) {
					ret = Objects.equal(value, eGet(right, featureName));
				} else if (origin != null) {
					ret = Objects.equal(value, eGet(origin, featureName));
				} else {
					ret = false;
				}
				return ret;
			}
		};
		return predicate;
	}

	protected Object eGet(EObject eObject, String featureName) {
		EStructuralFeature eStructuralFeature = eObject.eClass().getEStructuralFeature(featureName);
		return eObject.eGet(eStructuralFeature);
	}

	protected List<Notifier> eAllChildren(Notifier notifier) {
		List<Notifier> ret = newArrayList();
		ITreeItemContentProvider contentProvider = adaptAsITreeItemContentProvider(notifier);
		Iterable<Notifier> children = filter(contentProvider.getChildren(notifier), Notifier.class);
		for (Notifier child : children) {
			ret.add(child);
			ret.addAll(eAllChildren(child));
		}
		return ret;
	}

	protected ITreeItemContentProvider adaptAsITreeItemContentProvider(Notifier notifier) {
		ITreeItemContentProvider contentProvider = (ITreeItemContentProvider)compareItemProviderAdapterFactory
				.adapt(notifier, ITreeItemContentProvider.class);
		return contentProvider;
	}
}
