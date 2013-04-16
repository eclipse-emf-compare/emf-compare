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
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.impl;

import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.internal.spec.EObjectUtil;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.utils.DiffUtil;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MergeViewerItem extends AdapterImpl implements IMergeViewerItem {

	private final Object fLeft;

	private final Object fRight;

	private final Object fAncestor;

	private final Diff fDiff;

	private final Comparison fComparison;

	private final MergeViewerSide fSide;

	private final AdapterFactory fAdapterFactory;

	public MergeViewerItem(Comparison comparison, Diff diff, Object left, Object right, Object ancestor,
			MergeViewerSide side, AdapterFactory adapterFactory) {
		fLeft = left;
		fRight = right;
		fAncestor = ancestor;
		fDiff = diff;
		fSide = side;
		fAdapterFactory = adapterFactory;
		fComparison = comparison;
	}

	/**
	 * @param comparison
	 * @param diff
	 * @param match
	 * @param side
	 * @param adapterFactory
	 */
	public MergeViewerItem(Comparison comparison, Diff diff, Match match, MergeViewerSide side,
			AdapterFactory adapterFactory) {
		this(comparison, diff, match.getLeft(), match.getRight(), match.getOrigin(), side, adapterFactory);
	}

	/**
	 * @return
	 */
	public final Diff getDiff() {
		return fDiff;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getAncestor()
	 */
	public final Object getAncestor() {
		return fAncestor;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getLeft()
	 */
	public final Object getLeft() {
		return fLeft;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getRight()
	 */
	public final Object getRight() {
		return fRight;
	}

	/**
	 * @return the fSide
	 */
	public final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.ide.ui.internal.contentmergeviewer.IMergeViewerItem#getSideValue(org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide)
	 */
	public final Object getSideValue(MergeViewerSide side) {
		switch (side) {
			case LEFT:
				return fLeft;
			case RIGHT:
				return fRight;
			case ANCESTOR:
				return fAncestor;
			default:
				throw new IllegalStateException(); // happy compiler :)
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem#getParent()
	 */
	public IMergeViewerItem.Container getParent() {
		IMergeViewerItem.Container ret = null;

		Object sideValue = getBestSideValue();
		ITreeItemContentProvider treeItemContentProvider = (ITreeItemContentProvider)fAdapterFactory.adapt(
				sideValue, ITreeItemContentProvider.class);

		Object parent = treeItemContentProvider != null ? treeItemContentProvider.getParent(sideValue) : null;
		if (parent instanceof EObject) {
			ret = createBasicContainer((EObject)parent);
		}

		return ret;
	}

	public IMergeViewerItem cloneAsOpposite() {
		return new MergeViewerItem(getComparison(), getDiff(), getLeft(), getRight(), getAncestor(),
				getSide(), getAdapterFactory());
	}

	protected final Object getBestSideValue() {
		Object sideValue;
		if (fSide != MergeViewerSide.ANCESTOR) {
			sideValue = getSideValue(fSide);
			if (sideValue == null) {
				sideValue = getSideValue(fSide.opposite());
				if (sideValue == null) {
					sideValue = getSideValue(MergeViewerSide.ANCESTOR);
				}
			}
		} else {
			sideValue = getSideValue(MergeViewerSide.ANCESTOR);
			if (sideValue == null) {
				sideValue = getSideValue(MergeViewerSide.LEFT);
				if (sideValue == null) {
					sideValue = getSideValue(MergeViewerSide.RIGHT);
				}
			}
		}
		return sideValue;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.item.IMergeViewerItem#isInsertionPoint()
	 */
	public boolean isInsertionPoint() {
		return getSideValue(getSide()) == null && getDiff() != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String className = this.getClass().getName();
		int start = className.lastIndexOf('.');
		return Objects.toStringHelper(className.substring(start + 1)).add("ancestor",
				EObjectUtil.getLabel((EObject)getAncestor())).add("left",
				EObjectUtil.getLabel((EObject)getLeft())).add("right",
				EObjectUtil.getLabel((EObject)getRight())).add("side", getSide()).add("diff", getDiff())
				.toString();
	}

	/**
	 * @return the fComparison
	 */
	protected final Comparison getComparison() {
		return fComparison;
	}

	/**
	 * @return the fAdapterFactory
	 */
	protected final AdapterFactory getAdapterFactory() {
		return fAdapterFactory;
	}

	protected final IMergeViewerItem.Container createBasicContainer(EObject eObject) {
		IMergeViewerItem.Container ret = null;
		Match parentMatch = fComparison.getMatch(eObject);
		if (parentMatch == null) {
			return null;
		}
		Object expectedValue = MergeViewerUtil.getEObject(parentMatch, fSide);
		if (expectedValue != null) {
			Iterable<ReferenceChange> containmentReferenceChanges = filter(filter(filter(parentMatch
					.getDifferences(), ReferenceChange.class),
					EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE), EMFComparePredicates
					.valueIs(expectedValue));

			if (size(containmentReferenceChanges) > 1) {
				throw new IllegalStateException("Should not have more than one ReferenceChange on each Match"); //$NON-NLS-1$
			} else {
				ReferenceChange referenceChange = getFirst(containmentReferenceChanges, null);
				ret = new MergeViewerItem.Container(fComparison, referenceChange, parentMatch, fSide,
						fAdapterFactory);
			}
		} else {
			expectedValue = MergeViewerUtil.getEObject(parentMatch, fSide.opposite());
			if (expectedValue == null) {
				expectedValue = MergeViewerUtil.getEObject(parentMatch, MergeViewerSide.ANCESTOR);
			}

			if (expectedValue != null) {
				Iterable<ReferenceChange> containmentReferenceChanges = filter(filter(filter(parentMatch
						.getDifferences(), ReferenceChange.class),
						EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE), EMFComparePredicates
						.valueIs(expectedValue));
				if (!isEmpty(containmentReferenceChanges)) {
					if (size(containmentReferenceChanges) > 1) {
						throw new IllegalStateException(
								"Should not have more than one ReferenceChange on each Match"); //$NON-NLS-1$
					} else {
						ReferenceChange referenceChange = containmentReferenceChanges.iterator().next();
						ret = createInsertionPoint(referenceChange, fSide, fAdapterFactory);
					}
				}
			}
		}
		return ret;
	}

	protected final List<IMergeViewerItem> createInsertionPoints(Comparison comparison,
			EStructuralFeature eStructuralFeature, final List<? extends IMergeViewerItem> values,
			List<ReferenceChange> differences) {
		final List<IMergeViewerItem> ret = newArrayList(values);
		final List<Object> sideContent = ReferenceUtil.getAsList((EObject)getSideValue(getSide()),
				eStructuralFeature);
		final List<Object> oppositeContent = ReferenceUtil.getAsList((EObject)getSideValue(getSide()
				.opposite()), eStructuralFeature);

		for (Diff diff : Lists.reverse(differences)) {
			EObject value = (EObject)MergeViewerUtil.getDiffValue(diff);
			Match match = getComparison().getMatch(value);

			boolean b1 = diff.getSource() == DifferenceSource.LEFT && diff.getKind() == DifferenceKind.DELETE
					&& getSide() == MergeViewerSide.LEFT;
			boolean b2 = diff.getSource() == DifferenceSource.LEFT && diff.getKind() == DifferenceKind.ADD
					&& getSide() == MergeViewerSide.RIGHT;
			boolean b3 = diff.getSource() == DifferenceSource.RIGHT && diff.getKind() == DifferenceKind.ADD
					&& getSide() == MergeViewerSide.LEFT;
			boolean b4 = diff.getSource() == DifferenceSource.RIGHT
					&& diff.getKind() == DifferenceKind.DELETE && getSide() == MergeViewerSide.RIGHT;

			if (b1 || b2 || b3 || b4) {
				IMergeViewerItem.Container insertionPoint = new MergeViewerItem.Container(getComparison(),
						diff, match.getLeft(), match.getRight(), match.getOrigin(), getSide(),
						getAdapterFactory());

				final int insertionIndex;
				if (match.getLeft() == null && match.getRight() == null) {
					insertionIndex = ReferenceUtil.getAsList((EObject)getSideValue(MergeViewerSide.ANCESTOR),
							eStructuralFeature).indexOf(value);
				} else {
					insertionIndex = Math.min(DiffUtil.findInsertionIndex(comparison, oppositeContent,
							sideContent, value), ret.size());
				}

				// offset the insertion by the number of previous insertion points in the list
				int realIndex = 0;
				for (int index = 0; index < insertionIndex; realIndex++) {
					if (!ret.get(realIndex).isInsertionPoint()) {
						index++;
					}
				}

				ret.add(realIndex, insertionPoint);
			}
		}
		return ret;
	}

	private IMergeViewerItem.Container createInsertionPoint(Diff diff, MergeViewerSide side,
			AdapterFactory adapterFactory) {
		Object left = MergeViewerUtil.getValueFromDiff(diff, MergeViewerSide.LEFT);
		Object right = MergeViewerUtil.getValueFromDiff(diff, MergeViewerSide.RIGHT);

		IMergeViewerItem.Container insertionPoint = null;
		if (left == null && right == null) {
			// Do not display anything
		} else {
			final boolean leftEmptyBox = side == MergeViewerSide.LEFT
					&& (left == null || !MergeViewerUtil.getValues(diff, side).contains(left));
			final boolean rightEmptyBox = side == MergeViewerSide.RIGHT
					&& (right == null || !MergeViewerUtil.getValues(diff, side).contains(right));
			if (leftEmptyBox || rightEmptyBox) {
				Object ancestor = MergeViewerUtil.getValues(diff, MergeViewerSide.ANCESTOR);

				insertionPoint = new MergeViewerItem.Container(getComparison(), diff, left, right, ancestor,
						side, adapterFactory);
			}
		}

		return insertionPoint;
	}

	protected final List<IMergeViewerItem> createMergeViewerItemFrom(Collection<?> values) {
		List<IMergeViewerItem> ret = newArrayListWithCapacity(values.size());
		for (Object value : values) {
			IMergeViewerItem valueToAdd = createMergeViewerItemFrom((EObject)value);
			ret.add(valueToAdd);
		}
		return ret;
	}

	private IMergeViewerItem createMergeViewerItemFrom(EObject eObject) {
		Match match = getComparison().getMatch(eObject);

		ReferenceChange referenceChange = getFirst(filter(filter(getComparison().getDifferences(eObject),
				ReferenceChange.class), EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE), null);
		if (match != null) {
			return new MergeViewerItem.Container(getComparison(), referenceChange, match, getSide(),
					getAdapterFactory());
		} else {
			switch (getSide()) {
				case LEFT:
					return new MergeViewerItem.Container(getComparison(), referenceChange, eObject, null,
							null, getSide(), getAdapterFactory());
				case RIGHT:
					return new MergeViewerItem.Container(getComparison(), referenceChange, null, eObject,
							null, getSide(), getAdapterFactory());
				case ANCESTOR:
					return new MergeViewerItem.Container(getComparison(), referenceChange, null, null,
							eObject, getSide(), getAdapterFactory());
				default:
					throw new IllegalStateException();
			}
		}
	}

	public static class Container extends MergeViewerItem implements IMergeViewerItem.Container {

		/**
		 * 
		 */
		private static final IMergeViewerItem[] NO_ITEMS_ARR = new IMergeViewerItem[0];

		/**
		 * @param comparison
		 * @param diff
		 * @param left
		 * @param right
		 * @param ancestor
		 */
		public Container(Comparison comparison, Diff diff, Object left, Object right, Object ancestor,
				MergeViewerSide side, AdapterFactory adapterFactory) {
			super(comparison, diff, left, right, ancestor, side, adapterFactory);
		}

		/**
		 * @param fComparison
		 * @param referenceChange
		 * @param parentMatch
		 * @param fSide
		 * @param fAdapterFactory
		 */
		public Container(Comparison comparison, Diff diff, Match match, MergeViewerSide side,
				AdapterFactory adapterFactory) {
			super(comparison, diff, match, side, adapterFactory);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.MergeViewerItem.item.impl.AbstractMergeViewerItem#getParent()
		 */
		@Override
		public IMergeViewerItem.Container getParent() {
			IMergeViewerItem.Container ret = null;

			Object sideValue = getBestSideValue();
			ITreeItemContentProvider treeItemContentProvider = (ITreeItemContentProvider)getAdapterFactory()
					.adapt(sideValue, ITreeItemContentProvider.class);

			Object parent = treeItemContentProvider != null ? treeItemContentProvider.getParent(sideValue)
					: null;
			if (parent instanceof EObject) {
				ret = createBasicContainer((EObject)parent);
			}

			return ret;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem.Container#hasChildren()
		 */
		public boolean hasChildren() {
			return getChildren().length > 0;
		}

		@Override
		public IMergeViewerItem.Container cloneAsOpposite() {
			return new MergeViewerItem.Container(getComparison(), getDiff(), getLeft(), getRight(),
					getAncestor(), getSide(), getAdapterFactory());
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem.Container#getChildren()
		 */
		public IMergeViewerItem[] getChildren() {
			Object sideValue = getSideValue(getSide());
			EObject bestSideValue = (EObject)getBestSideValue();

			final Collection<? extends EStructuralFeature> childrenFeatures = getChildrenFeatures(bestSideValue);

			Match match = getComparison().getMatch(bestSideValue);
			final ImmutableList<ReferenceChange> differences;
			if (match != null) {
				differences = ImmutableList.copyOf(filter(filter(match.getDifferences(),
						ReferenceChange.class), and(EMFComparePredicates.CONTAINMENT_REFERENCE_CHANGE,
						EMFComparePredicates.hasState(DifferenceState.UNRESOLVED))));
			} else {
				differences = ImmutableList.of();
			}

			List<IMergeViewerItem> ret = newArrayList();
			for (EStructuralFeature eStructuralFeature : childrenFeatures) {
				List<Object> list = ReferenceUtil.getAsList((EObject)sideValue, eStructuralFeature);
				List<IMergeViewerItem> mergeViewerItem = createMergeViewerItemFrom(list);
				if (getSide() != MergeViewerSide.ANCESTOR) {
					List<ReferenceChange> differencesOnFeature = ImmutableList.copyOf(filter(differences,
							EMFComparePredicates.onFeature(eStructuralFeature.getName())));
					ret.addAll(createInsertionPoints(getComparison(), eStructuralFeature, mergeViewerItem,
							differencesOnFeature));
				} else {
					ret.addAll(mergeViewerItem);
				}
			}

			return ret.toArray(NO_ITEMS_ARR);
		}

		/**
		 * Returns the list of children features to display within the UI.
		 * <p>
		 * Default implementation loops through all existing children and retain the order of appearance of
		 * containing feature. Bad performance, but is giving the best visual result.
		 * 
		 * @param object
		 * @return
		 */
		protected Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
			Collection<? extends EStructuralFeature> ret = getChildrenFeaturesFromItemProviderAdapter(object);

			if (ret == null) {
				ret = getChildrenFeaturesFromEClass(object);
			}

			return ret;
		}

		protected Collection<? extends EStructuralFeature> getChildrenFeaturesFromEClass(Object object) {
			ImmutableSet.Builder<EStructuralFeature> features = ImmutableSet.builder();
			for (EStructuralFeature feature : ((EObject)object).eClass().getEAllContainments()) {
				features.add(feature);
			}
			return features.build();
		}

		@SuppressWarnings("unchecked")
		protected Collection<? extends EStructuralFeature> getChildrenFeaturesFromItemProviderAdapter(
				Object object) {
			Collection<? extends EStructuralFeature> ret = null;

			Object treeItemContentProvider = getAdapterFactory()
					.adapt(object, ITreeItemContentProvider.class);

			if (treeItemContentProvider instanceof ItemProviderAdapter) {
				ItemProviderAdapter itemProviderAdapter = (ItemProviderAdapter)treeItemContentProvider;
				Method method;
				try {
					method = itemProviderAdapter.getClass().getMethod("getChildrenFeatures", Object.class); //$NON-NLS-1$
					method.setAccessible(true);
					ret = (Collection<? extends EStructuralFeature>)method
							.invoke(itemProviderAdapter, object);
				} catch (SecurityException e) {
				} catch (NoSuchMethodException e) {
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}

			return ret;
		}

	}
}
