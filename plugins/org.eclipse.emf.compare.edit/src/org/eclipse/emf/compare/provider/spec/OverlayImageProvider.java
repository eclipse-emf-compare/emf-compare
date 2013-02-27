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

import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedImage;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class OverlayImageProvider {

	private final ResourceLocator fResourceLocator;

	/**
	 * 
	 */
	public OverlayImageProvider(ResourceLocator resourceLocator) {
		this.fResourceLocator = resourceLocator;
	}

	public Object getComposedImage(Diff diff, Object imageToCompose) {
		String overlay = getImageOverlay(diff);
		return getComposedImage(imageToCompose, overlay);
	}

	public Object getComposedImage(Match match, Object imageToCompose) {
		String overlay = getImageOverlay(match);
		return getComposedImage(imageToCompose, overlay);
	}

	private Object getComposedImage(Object imageToCompose, String overlay) {
		Collection<Object> images = newArrayList();
		images.add(imageToCompose);
		if (overlay != null) {
			Object image = fResourceLocator.getImage(overlay);
			images.add(image);
		}
		return new ComposedImageExtension(images);
	}

	// Nothing here has to be externalized
	@SuppressWarnings("nls")
	private String getImageOverlay(Diff diff) {
		final DifferenceSource source = diff.getSource();
		final Match match = diff.getMatch();
		final Conflict conflict = diff.getConflict();
		final DifferenceKind diffKind = diff.getKind();
		final Comparison comparison = match.getComparison();
		String path = "full/ovr16/";

		if (diff.getState() == DifferenceState.MERGED) {
			path += "merged_ov";
		} else if (diff.getState() == DifferenceState.DISCARDED) {
			path += "removed_ov";
		} else if (comparison.isThreeWay()) {
			// "png" needs explicit declaration, "gif" does not
			String extension = "";
			if (conflict != null && conflict.getKind() == ConflictKind.REAL) {
				extension = ".png";
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
			path += extension;
		} else {
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

	// Nothing here has to be externalized
	@SuppressWarnings("nls")
	private String getImageOverlay(Match match) {
		String path = null;
		final EObject ancestor = match.getOrigin();
		final EObject left = match.getLeft();
		final EObject right = match.getRight();

		final Iterable<Diff> differences = match.getAllDifferences();

		if (match.getComparison().isThreeWay()) {
			if (any(differences, hasConflict(ConflictKind.REAL))) {
				path = "confinoutchg_ov.png";
			} else if (ancestor == null) {
				if (right == null) {
					path = "r_outadd_ov";
				} else if (left == null) {
					path = "r_inadd_ov";
				} else {
					// pseudo conflict addition
					// TODO we filter this by default, what to do if the filter is off?
				}
			} else if (left == null) {
				if (right != null) {
					path = "r_outdel_ov";
				} else {
					// pseudo conflict deletion
					// TODO we filter this by default, what to do if the filter is off?
				}
			} else if (right == null) {
				path = "r_indel_ov";
			} else {
				boolean hasLeftDiffs = any(differences, fromSide(DifferenceSource.LEFT));
				boolean hasRightDiffs = any(differences, fromSide(DifferenceSource.RIGHT));

				if (hasLeftDiffs && hasRightDiffs) {
					path = "r_inoutchg_ov";
				} else if (hasLeftDiffs) {
					path = "r_outchg_ov";
				} else if (hasRightDiffs) {
					path = "r_inchg_ov";
				}
			}
		} else {
			if (left == null) {
				path = "del_ov";
			} else if (right == null) {
				path = "add_ov";
			} else if (!isEmpty(differences)) {
				path = "chg_ov";
			}
		}

		String ret = null;
		if (path != null) {
			ret = "full/ovr16/" + path;
		}
		return ret;
	}

	private final class ComposedImageExtension extends ComposedImage {

		/**
		 * @param images
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
				result.get(1).x = 12;
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
		public Size getSize(Collection<? extends Size> imageSizes) {
			this.imageSizes = newArrayList(imageSizes);
			List<Point> drawPoints = getDrawPoints(null);

			Size result = new Size();
			for (int i = 0; i < imageSizes.size(); i++) {
				Size size = this.imageSizes.get(i);
				Point point = drawPoints.get(i);

				result.width = Math.max(result.width, size.width + Math.abs(point.x));
				result.height = Math.max(result.height, size.height + Math.abs(point.y));
			}
			return result;
		}
	}
}
