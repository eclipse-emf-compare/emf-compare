/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.ConflictKind.REAL;
import static org.eclipse.emf.compare.DifferenceState.DISCARDED;
import static org.eclipse.emf.compare.DifferenceState.MERGED;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.canBeConsideredAsPseudoConflicting;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasDirectOrIndirectConflict;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.merge.IMergeData;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedImage;

/**
 * Utility class to externalize the retrieval of difference overlay.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class OverlayImageProvider {

	/** The base name of the change (and move) kind of diff overlay. */
	private static final String CHG_OV = "chg_ov"; //$NON-NLS-1$

	/** The base name of the deletion kind of diff overlay. */
	private static final String DEL_OV = "del_ov"; //$NON-NLS-1$

	/** The base name of the add kind of diff overlay. */
	private static final String ADD_OV = "add_ov"; //$NON-NLS-1$

	/** The base name of the merge to right diff overlay. */
	private static final String MERGED_TO_RIGHT_OV = "merged_right_ov"; //$NON-NLS-1$

	/** The base name of the merge to left diff overlay. */
	private static final String MERGED_TO_LEFT_OV = "merged_left_ov"; //$NON-NLS-1$

	/** The base name of the rejected diff overlay. */
	private static final String REJECTED_OV = "removed_ov"; //$NON-NLS-1$

	/** The base name of the accepted diff overlay. */
	private static final String ACCEPTED_OV = "merged_ov"; //$NON-NLS-1$

	/** The resource locator to use to retrieve the images. */
	private final ResourceLocator fResourceLocator;

	/**
	 * Creates a new instance with the given resource locator.
	 * 
	 * @param resourceLocator
	 *            the resource locator to use to retrieve images
	 */
	public OverlayImageProvider(ResourceLocator resourceLocator) {
		this.fResourceLocator = resourceLocator;
	}

	/**
	 * Returns a composed image with the image of the given diff the appropriate overlay.
	 * 
	 * @param diff
	 *            the diff for which we have to find an overlay.
	 * @param imageToCompose
	 *            the image of the diff to use as base.
	 * @return a composed image with the image of the given diff the appropriate overlay.
	 */
	public Object getComposedImage(Diff diff, Object imageToCompose) {
		String overlay = getImageOverlay(diff);
		return getComposedImage(imageToCompose, overlay);
	}

	/**
	 * Returns a composed image with the image of the given match the appropriate overlay.
	 * 
	 * @param match
	 *            the match for which we have to find an overlay.
	 * @param imageToCompose
	 *            the image of the match to use as base.
	 * @return a composed image with the image of the given match the appropriate overlay.
	 */
	public Object getComposedImage(Match match, Object imageToCompose) {
		String overlay = getImageOverlay();
		return getComposedImage(imageToCompose, overlay);
	}

	/**
	 * Returns a composed image with the image of the given object and the appropriate overlay.
	 * 
	 * @param object
	 *            the object for which we have to find an overlay.
	 * @param imageToCompose
	 *            the image of the match to use as base.
	 * @return a composed image with the image of the given object and the appropriate overlay.
	 * @since 4.0
	 */
	public Object getComposedImage(Object object, Object imageToCompose) {
		String overlay = "full/ovr16/match_ov"; //$NON-NLS-1$
		return getComposedImage(imageToCompose, overlay);
	}

	/**
	 * Returns a composed image with the image of the given match the overlay at the given path.
	 * 
	 * @param imageToCompose
	 *            the image to use as base.
	 * @param overlayPath
	 *            the path to the overlay.
	 * @return a composed image with the image of the given match the overlay at the given path.
	 */
	private Object getComposedImage(Object imageToCompose, String overlayPath) {
		Collection<Object> images = newArrayList();
		images.add(imageToCompose);
		if (overlayPath != null) {
			Object image = fResourceLocator.getImage(overlayPath);
			images.add(image);
		}
		return new ComposedImageExtension(images);
	}

	/**
	 * Returns the path to the image overlay for the given {@code diff}.
	 * 
	 * @param diff
	 *            the diff we have to find an image for.
	 * @return the path to the image overlay for the given {@code diff}.
	 */
	private String getImageOverlay(Diff diff) {
		final Comparison comparison = ComparisonUtil.getComparison(diff);
		String path = "full/ovr16/"; //$NON-NLS-1$

		if (diff.getState() == MERGED) {
			path += getMergedOverlay(diff);
		} else if (diff.getState() == DISCARDED) {
			path += getDiscardedOverlay(diff);
		} else if (comparison.isThreeWay()) {
			path += getThreeWayOverlay(diff);
		} else {
			path += getTwoWayOverlay(diff);
		}
		return path;
	}

	/**
	 * Returns the overlay path for the given unmerged diff.
	 * 
	 * @param diff
	 *            the diff we have to find an image for.
	 * @return the overlay path for the given unmerged diff.
	 */
	private String getThreeWayOverlay(final Diff diff) {
		DifferenceKind diffKind = diff.getKind();
		DifferenceSource source = diff.getSource();
		StringBuilder path = new StringBuilder();
		if (hasDirectOrIndirectConflict(REAL).apply(diff)) {
			// The diff or one of its refining diffs are in a pseudo conflict
			path.append("conf"); //$NON-NLS-1$
			if (source == DifferenceSource.RIGHT) {
				path.append("r_"); //$NON-NLS-1$
			}
		} else if (canBeConsideredAsPseudoConflicting().apply(diff)) {
			// If the diff is not a refined diff and is in a pseudo conflict
			// Or if the diff is a refined diff that is not in a direct pseudo conflict, but all its
			// refining diffs are in pseudo conflicts
			path.append("pconf"); //$NON-NLS-1$
			if (source == DifferenceSource.RIGHT) {
				path.append("r_"); //$NON-NLS-1$
			}
		} else {
			switch (source) {
				case LEFT:
					path.append("r_out"); //$NON-NLS-1$
					break;
				case RIGHT:
					path.append("r_in"); //$NON-NLS-1$
					break;
				default:
					throw new IllegalArgumentException();
			}
		}
		switch (diffKind) {
			case ADD:
				path.append(ADD_OV);
				break;
			case DELETE:
				path.append(DEL_OV);
				break;
			case CHANGE:
				// fallthrough
			case MOVE:
				path.append(CHG_OV);
				break;
			default:
				throw new IllegalArgumentException();
		}
		return path.toString();
	}

	/**
	 * Returns the overlay for a diff that's in a 'DISCARDED' state.
	 * 
	 * @param diff
	 *            The diff
	 * @return The overlay path to use.
	 */
	// @CHECKSTYLE:OFF
	private String getDiscardedOverlay(Diff diff) {
		DifferenceSource source = diff.getSource();
		Comparison comp = ComparisonUtil.getComparison(diff);
		IMergeData mergeData = (IMergeData)EcoreUtil.getExistingAdapter(comp, IMergeData.class);
		if (mergeData != null) {
			switch (source) {
				case LEFT:
					if (mergeData.isLeftEditable() && mergeData.isRightEditable()) {
						return MERGED_TO_LEFT_OV;
					} else {
						return REJECTED_OV;
					}
				case RIGHT:
					if (mergeData.isLeftEditable() && mergeData.isRightEditable()) {
						return MERGED_TO_RIGHT_OV;
					} else {
						return REJECTED_OV;
					}
				default:
					throw new IllegalArgumentException();
			}
		}
		// Cannot determine direction
		return REJECTED_OV;
	}
	// @CHECKSTYLE:ON

	/**
	 * Returns the overlay for a diff that's in a 'MERGED' state.
	 * 
	 * @param diff
	 *            The diff
	 * @return The overlay path to use.
	 */
	// @CHECKSTYLE:OFF
	private String getMergedOverlay(Diff diff) {
		DifferenceSource source = diff.getSource();
		Comparison comp = ComparisonUtil.getComparison(diff);
		IMergeData mergeData = (IMergeData)EcoreUtil.getExistingAdapter(comp, IMergeData.class);
		if (mergeData != null) {
			switch (source) {
				case LEFT:
					if (mergeData.isLeftEditable() && mergeData.isRightEditable()) {
						return MERGED_TO_RIGHT_OV;
					} else {
						return ACCEPTED_OV;
					}
				case RIGHT:
					if (mergeData.isLeftEditable() && mergeData.isRightEditable()) {
						return MERGED_TO_LEFT_OV;
					} else {
						return ACCEPTED_OV;
					}
				default:
					throw new IllegalArgumentException();
			}
		}
		// Cannot determine direction
		return ACCEPTED_OV;
	}
	// @CHECKSTYLE:ON

	/**
	 * Returns the overlay path for the given unmerged diff.
	 * 
	 * @param diff
	 *            the diff we have to find an image for.
	 * @return the overlay path for the given unmerged diff.
	 */
	private String getTwoWayOverlay(Diff diff) {
		final String path;
		final DifferenceKind diffKind = diff.getKind();
		switch (diffKind) {
			case ADD:
				path = ADD_OV;
				break;
			case DELETE:
				path = DEL_OV;
				break;
			case CHANGE:
				// fallthrough
			case MOVE:
				path = CHG_OV;
				break;
			default:
				throw new IllegalStateException();
		}
		return path;
	}

	/**
	 * Returns the overlay path for the given match.
	 * 
	 * @return the overlay path for the given match.
	 */
	private String getImageOverlay() {
		return "full/ovr16/match_ov"; //$NON-NLS-1$
	}

	/**
	 * Extended {@link ComposedImage} that positionned the overlay properly for EMF Compare.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class ComposedImageExtension extends ComposedImage {

		/** The offset of the overlays. */
		private static final int X_OFFSET = 10;

		/**
		 * Creates a new instance with the given image list. The second image will be draw at the offset
		 * (X_OFFSET, 2).
		 * 
		 * @param images
		 *            the images to composed.
		 */
		ComposedImageExtension(Collection<?> images) {
			super(images);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.provider.ComposedImage#getDrawPoints(org.eclipse.emf.edit.provider.ComposedImage.Size)
		 */
		@Override
		public List<Point> getDrawPoints(Size size) {
			List<ComposedImage.Point> result = super.getDrawPoints(size);
			if (result.size() > 1) {
				result.get(1).x = X_OFFSET;
				result.get(1).y = 2;
			}
			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.edit.provider.ComposedImage#getSize(java.util.Collection)
		 */
		@Override
		public Size getSize(Collection<? extends Size> sizes) {
			this.imageSizes = newArrayList(sizes);
			List<Point> drawPoints = getDrawPoints(null);

			Size result = new Size();
			for (int i = 0; i < sizes.size(); i++) {
				Size size = this.imageSizes.get(i);
				Point point = drawPoints.get(i);

				result.width = Math.max(result.width, size.width + Math.abs(point.x));
				result.height = Math.max(result.height, size.height + Math.abs(point.y));
			}
			return result;
		}
	}
}
