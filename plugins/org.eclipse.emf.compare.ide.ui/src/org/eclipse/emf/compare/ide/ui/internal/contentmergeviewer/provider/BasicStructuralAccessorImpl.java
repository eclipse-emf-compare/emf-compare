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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.InsertionPoint;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.MatchedObject;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EqualityHelper;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class BasicStructuralAccessorImpl implements IStructuralFeatureAccessor {

	private final Diff fDiff;

	private final MergeViewerSide fSide;

	private final Match fOwnerMatch;

	private final EStructuralFeature fStructuralFeature;

	private final ImmutableList<Diff> fDifferences;

	public BasicStructuralAccessorImpl(Diff diff, MergeViewerSide side) {
		fDiff = diff;
		fSide = side;
		fOwnerMatch = diff.getMatch();
		fStructuralFeature = getDiffFeature(diff);

		List<Diff> siblingDifferences = fOwnerMatch.getDifferences();
		final Predicate<Diff> diffWithThisFeature = diffWithThisFeature(fStructuralFeature);
		fDifferences = ImmutableList.copyOf(filter(siblingDifferences, diffWithThisFeature));
	}

	public Comparison getComparison() {
		return fOwnerMatch.getComparison();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		List<?> list = getFeatureValues(fSide);
		List<? extends IMergeViewerItem> ret = createMergeViewerItemFrom(list);
		if (fSide != MergeViewerSide.ANCESTOR) {
			ret = createInsertionPoints(ret);
		}
		return ImmutableList.copyOf(ret);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IStructuralFeatureAccessor#getInitialItem()
	 */
	public IMergeViewerItem getInitialItem() {
		IMergeViewerItem ret = null;
		ImmutableList<? extends IMergeViewerItem> items = getItems();
		for (IMergeViewerItem item : items) {
			Diff diff = item.getDiff();
			if (diff == fDiff) {
				ret = item;
			}
		}
		return ret;
	}

	private List<? extends IMergeViewerItem> createInsertionPoints(
			final List<? extends IMergeViewerItem> values) {
		List<IMergeViewerItem> ret = newArrayList(values);
		for (Diff diff : fDifferences.reverse()) {
			boolean rightToLeft = (fSide == MergeViewerSide.LEFT);
			Object value = getValueFromDiff(diff, fSide);

			if (value == null || !getFeatureValues(fSide).contains(value)) {
				Object left = getValueFromDiff(diff, MergeViewerSide.LEFT);
				Object right = getValueFromDiff(diff, MergeViewerSide.RIGHT);
				Object ancestor = getValueFromDiff(diff, MergeViewerSide.ANCESTOR);
				InsertionPoint insertionPoint = new InsertionPoint(diff, left, right, ancestor);

				final int insertionIndex;
				final int nbInsertionPointBefore;

				if (featureIsMany(diff)) {
					insertionIndex = Math.min(
							DiffUtil.findInsertionIndex(getComparison(), diff, rightToLeft), ret.size());
					List<IMergeViewerItem> subList = ret.subList(0, insertionIndex);
					nbInsertionPointBefore = size(filter(subList, InsertionPoint.class));
				} else {
					insertionIndex = 0;
					nbInsertionPointBefore = 0;
				}

				int index = Math.min(insertionIndex + nbInsertionPointBefore, ret.size());
				ret.add(index, insertionPoint);
			}
		}
		return ret;
	}

	private List<? extends IMergeViewerItem> createMergeViewerItemFrom(List<?> values) {
		List<MatchedObject> ret = newArrayListWithCapacity(values.size());
		for (Object value : values) {
			final MatchedObject valueToAdd;
			if (value instanceof EObject) {
				valueToAdd = createMergeViewerItemFrom((EObject)value);
			} else {
				valueToAdd = createMergeViewerItemFrom(value);
			}
			ret.add(valueToAdd);
		}
		return ret;
	}

	private MatchedObject createMergeViewerItemFrom(EObject eObject) {
		final MatchedObject ret;
		final Comparison comparison = getComparison();
		Match match = comparison.getMatch(eObject);
		if (match == null) {
			// do want to call createMergeViewerItemFrom(Object) and NOT createMergeViewerItemFrom(EObject)
			ret = createMergeViewerItemFrom((Object)eObject);
		} else {
			Diff diff = getDiffWithValue(eObject, fSide);
			ret = new MatchedObject(diff, match);
		}
		return ret;
	}

	private MatchedObject createMergeViewerItemFrom(Object object) {
		Diff diff = getDiffWithValue(object, fSide);
		Object left = getMatchingValueFromSide(object, MergeViewerSide.LEFT);
		Object right = getMatchingValueFromSide(object, MergeViewerSide.RIGHT);
		Object ancestor = getMatchingValueFromSide(object, MergeViewerSide.ANCESTOR);
		return new MatchedObject(diff, left, right, ancestor);
	}

	private Object getMatchingValueFromSide(Object o, MergeViewerSide side) {
		Object ret = null;
		Comparison comparison = getComparison();
		EqualityHelper equalityHelper = comparison.getConfiguration().getEqualityHelper();
		Iterator<?> valuesIterator = getFeatureValues(side).iterator();
		while (valuesIterator.hasNext() && ret == null) {
			Object value = valuesIterator.next();
			if (equalityHelper.matchingValues(comparison, o, value)) {
				ret = value;
			}
		}
		return ret;
	}

	/**
	 * 
	 */
	private Diff getDiffWithValue(Object value, MergeViewerSide side) {
		Diff ret = null;
		for (Diff diff : fDifferences) {
			Object valueOfDiff = getValueFromDiff(diff, side);
			if (valueOfDiff == value) {
				ret = diff;
				break;
			}
		}
		return ret;
	}

	/**
	 * @param diff
	 * @param side
	 * @return
	 */
	private Object getValueFromDiff(final Diff diff, MergeViewerSide side) {
		Object ret = null;
		Object diffValue = getDiffValue(diff);
		if (diffValue instanceof EObject) {
			final Match matchOfValue = getComparison().getMatch((EObject)diffValue);
			if (matchOfValue != null) {
				switch (side) {
					case ANCESTOR:
						ret = matchOfValue.getOrigin();
						break;
					case LEFT:
						ret = matchOfValue.getLeft();
						break;
					case RIGHT:
						ret = matchOfValue.getRight();
						break;
					default:
						throw new IllegalStateException();
				}
			} else {
				ret = matchingValue(diffValue, side);
			}
		} else {
			ret = matchingValue(diffValue, side);
		}

		if (fStructuralFeature instanceof EAttribute) {
			EDataType eAttributeType = ((EAttribute)fStructuralFeature).getEAttributeType();
			ret = EcoreUtil.convertToString(eAttributeType, ret);
		}

		return ret;
	}

	private Object matchingValue(Object value, MergeViewerSide side) {
		Object ret = null;
		EqualityHelper equalityHelper = getComparison().getConfiguration().getEqualityHelper();
		Iterator<?> valuesIterator = getFeatureValues(side).iterator();
		while (valuesIterator.hasNext() && ret == null) {
			Object object = valuesIterator.next();
			if (equalityHelper.matchingValues(getComparison(), object, value)) {
				ret = object;
			}
		}
		return ret;
	}

	/**
	 * Returns the values of the current feature on the given side.
	 * 
	 * @param side
	 * @return
	 */
	private List<?> getFeatureValues(MergeViewerSide side) {
		final EObject eObject;
		switch (side) {
			case ANCESTOR:
				eObject = fOwnerMatch.getOrigin();
				break;
			case LEFT:
				eObject = fOwnerMatch.getLeft();
				break;
			case RIGHT:
				eObject = fOwnerMatch.getRight();
				break;
			default:
				throw new IllegalStateException();
		}

		return getAsList(eObject, fStructuralFeature);
	}

	private static Predicate<Diff> diffWithThisFeature(final EStructuralFeature thisFeature) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final EStructuralFeature feature = getDiffFeature(input);
				return feature == thisFeature;
			}
		};
	}

	private static boolean featureIsMany(Diff diff) {
		final EStructuralFeature eStructuralFeature;
		if (diff instanceof ReferenceChange) {
			eStructuralFeature = ((ReferenceChange)diff).getReference();
		} else {
			eStructuralFeature = ((AttributeChange)diff).getAttribute();
		}
		return eStructuralFeature.isMany();
	}

	/**
	 * This utility simply allows us to retrieve the value of a given feature as a List.
	 * 
	 * @param object
	 *            The object for which feature we need a value.
	 * @param feature
	 *            The actual feature of which we need the value.
	 * @return The value of the given <code>feature</code> for the given <code>object</code> as a list. An
	 *         empty list if this object has no value for that feature or if the object is <code>null</code>.
	 */
	private static List<?> getAsList(EObject object, EStructuralFeature feature) {
		final List<?> asList;
		if (object != null) {
			Object value = object.eGet(feature, false);
			if (value instanceof InternalEList<?>) {
				// EMF ignores the "resolve" flag for containment lists...
				asList = newArrayList(((InternalEList<?>)value).basicList());
			} else if (value instanceof List) {
				asList = newArrayList((List<?>)value);
			} else if (value instanceof Iterable) {
				asList = newArrayList((Iterable<?>)value);
			} else if (value != null) {
				asList = newArrayList(value);
			} else {
				asList = newArrayList();
			}
		} else {
			asList = newArrayList();
		}
		return asList;
	}

	/**
	 * Returns either {@link ReferenceChange#getReference()} or {@link AttributeChange#getAttribute()}
	 * depending on the runtime type of the give {@code diff} or null otherwise.
	 * 
	 * @param siblingDiff
	 * @return
	 */
	private static EStructuralFeature getDiffFeature(Diff siblingDiff) {
		final EStructuralFeature feature;
		if (siblingDiff instanceof ReferenceChange) {
			feature = ((ReferenceChange)siblingDiff).getReference();
		} else if (siblingDiff instanceof AttributeChange) {
			feature = ((AttributeChange)siblingDiff).getAttribute();
		} else {
			feature = null;
		}
		return feature;
	}

	/**
	 * Returns either {@link ReferenceChange#getValue()} or {@link AttributeChange#getValue()} depending on
	 * the runtime type of the give {@code diff} or null otherwise.
	 * 
	 * @param diff
	 * @return
	 */
	private static Object getDiffValue(Diff diff) {
		final Object ret;
		if (diff instanceof ReferenceChange) {
			ret = ((ReferenceChange)diff).getValue();
		} else if (diff instanceof AttributeChange) {
			ret = ((AttributeChange)diff).getValue();
		} else {
			ret = null;
		}
		return ret;
	}
}
