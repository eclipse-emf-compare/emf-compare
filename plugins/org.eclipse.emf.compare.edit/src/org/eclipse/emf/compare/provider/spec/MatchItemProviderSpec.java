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
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.transform;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.provider.MatchItemProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Specialized {@link MatchItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchItemProviderSpec extends MatchItemProvider {

	private static final Predicate<ReferenceChange> CONTAINMENT_REFERENCE_CHANGE = new Predicate<ReferenceChange>() {
		public boolean apply(ReferenceChange input) {
			return input.getReference().isContainment();
		}
	};

	private static final Function<ReferenceChange, EObject> VALUE = new Function<ReferenceChange, EObject>() {
		public EObject apply(ReferenceChange input) {
			return input.getValue();
		}
	};

	/**
	 * Constructor calling super {@link #MatchItemProvider(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		Match match = (Match)object;
		Object ret = AdapterFactoryUtil.getImage(getRootAdapterFactory(), match.getLeft());

		if (ret == null) {
			ret = AdapterFactoryUtil.getImage(getRootAdapterFactory(), match.getRight());
		}

		if (ret == null) {
			ret = AdapterFactoryUtil.getImage(getRootAdapterFactory(), match.getOrigin());
		}

		if (ret == null) {
			ret = super.getImage(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		Match match = (Match)object;
		String ret = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getLeft());

		if (ret == null) {
			ret = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getRight());
		}

		if (ret == null) {
			ret = AdapterFactoryUtil.getText(getRootAdapterFactory(), match.getOrigin());
		}

		if (ret == null) {
			ret = super.getText(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Match match = (Match)object;
		Iterable<?> filteredChildren = getChildrenIterable(match);
		return ImmutableList.copyOf(filteredChildren);
	}

	Iterable<?> getChildrenIterable(Match match) {
		ImmutableSet<EObject> containementDifferenceValues = containmentReferencesValues(match);

		@SuppressWarnings("unchecked")
		Predicate<Object> childrenFilter = not(or(matchOfContainmentDiff(containementDifferenceValues),
				matchWithNoChildren(), emptyMatch(), PSEUDO_CONFLICT_DIFF, REFINED_DIFF,
				PSEUDO_DELETE_CONFLICT));

		Iterable<?> filteredChildren = filter(super.getChildren(match), childrenFilter);
		return filteredChildren;
	}

	private ImmutableSet<EObject> containmentReferencesValues(Match match) {
		EList<Diff> differences = match.getDifferences();
		Iterable<ReferenceChange> containmentReferenceChanges = filter(filter(differences,
				ReferenceChange.class), CONTAINMENT_REFERENCE_CHANGE);
		ImmutableSet<EObject> containementDifferenceValues = ImmutableSet.copyOf(transform(
				containmentReferenceChanges, VALUE));
		return containementDifferenceValues;
	}

	private static Predicate<? super Object> matchOfContainmentDiff(
			final ImmutableSet<EObject> containementDifferenceValues) {
		return new Predicate<Object>() {
			public boolean apply(Object input) {
				boolean ret = false;
				if (input instanceof Match) {
					Match match = (Match)input;
					if (containementDifferenceValues.contains(match.getLeft())
							|| containementDifferenceValues.contains(match.getRight())
							|| containementDifferenceValues.contains(match.getOrigin())) {
						ret = true;
					}
				}
				return ret;
			}
		};
	}

	private Predicate<? super Object> matchWithNoChildren() {
		return new Predicate<Object>() {
			public boolean apply(Object input) {
				boolean ret = false;
				if (input instanceof Match) {
					Match match = (Match)input;
					ret = Iterables.isEmpty(MatchItemProviderSpec.this.getChildrenIterable(match));
				}
				return ret;
			}
		};
	}

	private static Predicate<? super Object> emptyMatch() {
		return new Predicate<Object>() {
			public boolean apply(Object input) {
				if (input instanceof Match) {
					final Match match = ((Match)input);
					return match.getLeft() == null && match.getRight() == null && match.getOrigin() == null;
				}
				return false;
			}
		};
	}

	static final Predicate<? super Object> REFINED_DIFF = new Predicate<Object>() {
		public boolean apply(Object input) {
			boolean ret = false;
			if (input instanceof Diff) {
				Diff diff = (Diff)input;
				ret = !diff.getRefines().isEmpty();
			}
			return ret;
		}
	};

	private static final Predicate<? super Object> PSEUDO_CONFLICT_DIFF = new Predicate<Object>() {
		public boolean apply(Object input) {
			boolean ret = false;
			if (input instanceof Diff) {
				Diff diff = (Diff)input;
				Conflict conflict = diff.getConflict();
				if (conflict != null && conflict.getKind() == ConflictKind.PSEUDO) {
					ret = true;
				}
			}
			return ret;
		}
	};

	private static final Predicate<? super Object> PSEUDO_DELETE_CONFLICT = new Predicate<Object>() {
		public boolean apply(Object input) {
			boolean ret = false;
			if (input instanceof ReferenceChange) {
				ReferenceChange referenceChange = (ReferenceChange)input;
				Conflict conflict = referenceChange.getConflict();
				if (conflict != null) {
					EReference eReference = referenceChange.getReference();
					EObject value = referenceChange.getValue();
					Iterable<ReferenceChange> conflictualReferenceChanges = filter(conflict.getDifferences(),
							ReferenceChange.class);
					for (ReferenceChange conflictualReferenceChange : conflictualReferenceChanges) {
						if (conflictualReferenceChange != referenceChange
								&& conflictualReferenceChange.getReference() == eReference
								&& conflictualReferenceChange.getValue() == value) {
							ret = true;
						}
					}
				}
			}
			return ret;
		}
	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		Match match = (Match)object;
		return !isEmpty(getChildrenIterable(match));
	}
}
