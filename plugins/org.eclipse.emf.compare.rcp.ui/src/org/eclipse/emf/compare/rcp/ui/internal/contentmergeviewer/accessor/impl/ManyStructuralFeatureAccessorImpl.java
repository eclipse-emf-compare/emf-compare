/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.utils.DiffUtil;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl.MergeViewerItem;
import org.eclipse.emf.compare.utils.IEqualityHelper;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ManyStructuralFeatureAccessorImpl extends AbstractStructuralFeatureAccessor {

	/**
	 * @param diff
	 * @param side
	 */
	public ManyStructuralFeatureAccessorImpl(AdapterFactory adapterFactory, Diff diff, MergeViewerSide side) {
		super(adapterFactory, diff, side);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl.AbstractStructuralFeatureAccessor.ui.internal.contentmergeviewer.provider.BasicStructuralFeatureAccessorImpl#getItems()
	 */
	public ImmutableList<? extends IMergeViewerItem> getItems() {
		List<? extends IMergeViewerItem> ret;
		List<?> list = getFeatureValues(getSide());
		ret = createMergeViewerItemFrom(list);

		if (getSide() != MergeViewerSide.ANCESTOR) {
			ret = createInsertionPoints(ret);
		}

		return ImmutableList.copyOf(ret);
	}

	private List<? extends IMergeViewerItem> createMergeViewerItemFrom(List<?> values) {
		List<IMergeViewerItem> ret = newArrayListWithCapacity(values.size());
		for (Object value : values) {
			IMergeViewerItem valueToAdd = createMergeViewerItemFrom(value);
			ret.add(valueToAdd);
		}
		return ret;
	}

	private IMergeViewerItem createMergeViewerItemFrom(Object object) {
		Diff diff = getDiffWithValue(object);
		Object left = matchingValue(object, MergeViewerSide.LEFT);
		Object right = matchingValue(object, MergeViewerSide.RIGHT);
		Object ancestor = matchingValue(object, MergeViewerSide.ANCESTOR);
		final boolean leftEmptyBox = !getFeatureValues(MergeViewerSide.LEFT).contains(left);
		final boolean rightEmptyBox = !getFeatureValues(MergeViewerSide.RIGHT).contains(right);
		if (leftEmptyBox || rightEmptyBox) {
			if (leftEmptyBox) {
				left = null;
			}
			if (rightEmptyBox) {
				right = null;
			}
		}
		return new MergeViewerItem(getComparison(), diff, left, right, ancestor, getSide(),
				getRootAdapterFactory());
	}

	private List<? extends IMergeViewerItem> createInsertionPoints(
			final List<? extends IMergeViewerItem> values) {
		List<IMergeViewerItem> ret = newArrayList(values);
		for (Diff diff : getDifferences().reverse()) {
			boolean rightToLeft = (getSide() == MergeViewerSide.LEFT);
			Object left = getValueFromDiff(diff, MergeViewerSide.LEFT);
			Object right = getValueFromDiff(diff, MergeViewerSide.RIGHT);

			if (left == null && right == null) {
				// Do not display anything
			} else {
				final boolean leftEmptyBox = getSide() == MergeViewerSide.LEFT
						&& (left == null || !getFeatureValues(getSide()).contains(left));
				final boolean rightEmptyBox = getSide() == MergeViewerSide.RIGHT
						&& (right == null || !getFeatureValues(getSide()).contains(right));
				if (leftEmptyBox || rightEmptyBox) {
					Object ancestor = getValueFromDiff(diff, MergeViewerSide.ANCESTOR);
					if (leftEmptyBox) {
						left = null;
					}
					if (rightEmptyBox) {
						right = null;
					}
					IMergeViewerItem insertionPoint = new MergeViewerItem(getComparison(), diff, left, right,
							ancestor, getSide(), getRootAdapterFactory());

					final int insertionIndex = Math.min(findInsertionIndex(diff, rightToLeft), ret.size());
					List<IMergeViewerItem> subList = ret.subList(0, insertionIndex);
					final int nbInsertionPointBefore = size(filter(subList,
							IMergeViewerItem.IS_INSERTION_POINT));

					int index = Math.min(insertionIndex + nbInsertionPointBefore, ret.size());
					ret.add(index, insertionPoint);
				}
			}
		}
		return ret;
	}

	protected int findInsertionIndex(Diff diff, boolean rightToLeft) {
		return DiffUtil.findInsertionIndex(getComparison(), diff, rightToLeft);
	}

	/**
	 * 
	 */
	private Diff getDiffWithValue(Object value) {
		Diff ret = null;
		for (Diff diff : getDifferences()) {
			Object valueOfDiff = getValueFromDiff(diff, getSide());
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
	protected Object getValueFromDiff(final Diff diff, MergeViewerSide side) {
		Object diffValue = getDiffValue(diff);
		Object ret = matchingValue(diffValue, side);
		return ret;
	}

	private Object matchingValue(Object object, MergeViewerSide side) {
		final Object ret;
		if (object instanceof EObject) {
			final Match matchOfValue = getComparison().getMatch((EObject)object);
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
				ret = matchingValue(object, getFeatureValues(side));
			}
		} else {
			ret = matchingValue(object, getFeatureValues(side));
		}
		return ret;
	}

	private Object matchingValue(Object value, List<?> in) {
		Object ret = null;
		IEqualityHelper equalityHelper = getComparison().getEqualityHelper();
		Iterator<?> valuesIterator = in.iterator();
		while (valuesIterator.hasNext() && ret == null) {
			Object object = valuesIterator.next();
			if (equalityHelper.matchingValues(object, value)) {
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
	protected List<?> getFeatureValues(MergeViewerSide side) {
		final EObject eObject = getEObject(side);
		return ReferenceUtil.getAsList(eObject, getStructuralFeature());
	}

	/**
	 * Returns either {@link ReferenceChange#getValue()} or {@link AttributeChange#getValue()} depending on
	 * the runtime type of the give {@code diff} or null otherwise.
	 * 
	 * @param diff
	 * @return
	 */
	protected Object getDiffValue(Diff diff) {
		final Object ret;
		if (diff instanceof ReferenceChange) {
			ret = ((ReferenceChange)diff).getValue();
		} else if (diff instanceof AttributeChange) {
			ret = ((AttributeChange)diff).getValue();
		} else if (diff.getPrimeRefining() instanceof ReferenceChange) {
			ret = ((ReferenceChange)diff.getPrimeRefining()).getValue();
		} else {
			ret = null;
		}
		return ret;
	}
}
