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

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.List;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.IMergeViewerItem;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.provider.EcoreEditPlugin;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class BasicStructuralFeatureAccessorImpl implements IStructuralFeatureAccessor, ITypedElement {

	private final Diff fDiff;

	private final MergeViewerSide fSide;

	private final Match fOwnerMatch;

	private final EStructuralFeature fStructuralFeature;

	private final ImmutableList<Diff> fDifferences;

	public BasicStructuralFeatureAccessorImpl(Diff diff, MergeViewerSide side) {
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		if (fStructuralFeature instanceof EAttribute) {
			return ExtendedImageRegistry.getInstance().getImage(
					EcoreEditPlugin.getPlugin().getImage("full/obj16/EAttribute")); //$NON-NLS-1$
		} else {
			return ExtendedImageRegistry.getInstance().getImage(
					EcoreEditPlugin.getPlugin().getImage("full/obj16/EReference")); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getType()
	 */
	public String getType() {
		return ContentMergeViewerConstants.REFERENCE_CHANGE_NODE_TYPE;
	}

	public EObject getEObject(MergeViewerSide side) {
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
		return eObject;
	}

	/**
	 * @return the fStructuralFeature
	 */
	public EStructuralFeature getStructuralFeature() {
		return fStructuralFeature;
	}

	/**
	 * @return the fSide
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	/**
	 * @return the fDifferences
	 */
	protected final ImmutableList<Diff> getDifferences() {
		return fDifferences;
	}

	private static Predicate<Diff> diffWithThisFeature(final EStructuralFeature thisFeature) {
		return new Predicate<Diff>() {
			public boolean apply(Diff input) {
				final EStructuralFeature feature = getDiffFeature(input);
				return feature == thisFeature;
			}
		};
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
}
