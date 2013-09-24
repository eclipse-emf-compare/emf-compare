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
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.merge.IDiffMergeData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedImage;

/**
 * Utility class to externalize the retrieval of difference overlay.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class OverlayImageProvider {

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
		String overlay = getImageOverlay(match);
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
	// Nothing here has to be externalized
	@SuppressWarnings("nls")
	private String getImageOverlay(Diff diff) {
		final Match match = diff.getMatch();
		final Comparison comparison = match.getComparison();
		String path = "full/ovr16/";

		if (diff.getState() == DifferenceState.MERGED) {
			path += getMergedOverlay(diff);
		} else if (diff.getState() == DifferenceState.DISCARDED) {
			path += REJECTED_OV;
		} else if (comparison.isThreeWay()) {
			path += getThreeWayOverlay(diff);
		} else {
			final DifferenceKind diffKind = diff.getKind();
			switch (diffKind) {
				case ADD:
					path += "add_ov";
					break;
				case DELETE:
					path += "del_ov";
					break;
				case CHANGE:
					// fallthrough
				case MOVE:
					path += "chg_ov";
					break;
				default:
					break;
			}
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
	// Nothing here has to be externalized
	@SuppressWarnings("nls")
	private String getThreeWayOverlay(final Diff diff) {
		final Conflict conflict = diff.getConflict();
		final DifferenceKind diffKind = diff.getKind();
		final DifferenceSource source = diff.getSource();
		String path = "";
		if (conflict != null) {
			if (conflict.getKind() == ConflictKind.PSEUDO) {
				path += "p";
			}
			path += "conf";
			if (source == DifferenceSource.RIGHT) {
				path += "r_";
			}
		} else {
			switch (source) {
				case LEFT:
					path += "r_out";
					break;
				case RIGHT:
					path += "r_in";
					break;
				default:
					// Cannot happen ... for now
					break;
			}
		}

		switch (diffKind) {
			case ADD:
				path += "add_ov";
				break;
			case DELETE:
				path += "del_ov";
				break;
			case CHANGE:
				// fallthrough
			case MOVE:
				path += "chg_ov";
				break;
			default:
				// Cannot happen ... for now
				break;
		}
		return path;
	}

	/**
	 * Return the merged overlay path for the given diff.
	 * 
	 * @param diff
	 *            the diff we have to find an image for.
	 * @return the merged overlay for the given diff.
	 */
	private String getMergedOverlay(Diff diff) {
		final String path;
		Adapter adapter = EcoreUtil.getExistingAdapter(diff, IDiffMergeData.class);
		if (adapter != null) {
			IDiffMergeData mergeData = (IDiffMergeData)adapter;
			if (!mergeData.isLeftEditable() || !mergeData.isRightEditable()) {
				if (mergeData.mergedTo() == diff.getSource()) {
					path = REJECTED_OV;
				} else {
					path = ACCEPTED_OV;
				}
			} else if (mergeData.isLeftEditable() && mergeData.isRightEditable()) {
				if (mergeData.hasBeenMergedToLeft()) {
					path = MERGED_TO_LEFT_OV;
				} else {
					path = MERGED_TO_RIGHT_OV;
				}
			} else {
				path = ACCEPTED_OV;
			}
		} else {
			path = ACCEPTED_OV;
		}
		return path;
	}

	/**
	 * Returns the overlay path for the given match.
	 * 
	 * @param match
	 *            the match we have to find an image for.
	 * @return the overlay path for the given match.
	 */
	// Nothing here has to be externalized
	@SuppressWarnings("nls")
	private String getImageOverlay(Match match) {
		return "full/ovr16/match_ov";
	}

	/**
	 * Extended {@link ComposedImage} that positionned the overlay properly for EMF Compare.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private final class ComposedImageExtension extends ComposedImage {

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
