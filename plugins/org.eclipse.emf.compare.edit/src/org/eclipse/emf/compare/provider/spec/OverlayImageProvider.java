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
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.hasConflict;

import com.google.common.base.Predicate;

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

	private final boolean fLeftIsLocal;

	private final ResourceLocator fResourceLocator;

	/**
	 * 
	 */
	public OverlayImageProvider(ResourceLocator resourceLocator, boolean leftIsLocal) {
		this.fResourceLocator = resourceLocator;
		this.fLeftIsLocal = leftIsLocal;
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

	private String getImageOverlay(Diff diff) {
		final DifferenceSource source = diff.getSource();
		final Match match = diff.getMatch();
		final Conflict conflict = diff.getConflict();
		final DifferenceKind diffKind = diff.getKind();
		final Comparison c = match.getComparison();
		String path = "full/ovr16/";

		if (diff.getState() == DifferenceState.MERGED) {
			path += "merged_ov";
		} else if (diff.getState() == DifferenceState.DISCARDED) {
			path += "removed_ov";
		} else if (c.isThreeWay()) {
			String filext = "";
			if (conflict != null) {
				if (conflict.getKind() == ConflictKind.REAL) {
					filext = ".png";
					path += "conf";
					path += getConflictWay(source);
				}
				// if (conflict.getKind() == ConflictKind.PSEUDO) {
				// path += "pconf";
				// }
			} else {
				switch (source) {
					case LEFT:
						if (fLeftIsLocal) {
							path += "r_out";
						} else {
							path += "out";
						}
						break;
					case RIGHT:
						if (fLeftIsLocal) {
							path += "r_in";
						} else {
							path += "in";
						}
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
			path += filext;
		} else {
			path += getPathForTwoWayDiff(diffKind);
		}
		return path;
	}

	private String getPathForTwoWayDiff(final DifferenceKind diffKind) {
		final String path;
		switch (diffKind) {
			case ADD:
				if (fLeftIsLocal) {
					path = "add_ov";
				} else {
					path = "del_ov";
				}
				break;
			case DELETE:
				if (fLeftIsLocal) {
					path = "del_ov";
				} else {
					path = "add_ov";
				}
				break;
			case CHANGE:
				// fallthrough
			case MOVE:
				path = "chg_ov";
				break;
			default:
				path = "";
				break;
		}
		return path;
	}

	private String getConflictWay(final DifferenceSource source) {
		final String path;
		if (source == DifferenceSource.LEFT && !fLeftIsLocal) {
			path = "r_";
		} else if (source == DifferenceSource.RIGHT && fLeftIsLocal) {
			path = "r_";
		} else {
			path = "";
		}
		return path;
	}

	private String getImageOverlay(Match match) {
		String path = null;
		final EObject ancestor = match.getOrigin();
		final EObject left = match.getLeft();
		final EObject right = match.getRight();

		final Iterable<Diff> differences = match.getAllDifferences();

		if (match.getComparison().isThreeWay()) {
			boolean hasConflicts = any(differences, hasConflict(ConflictKind.REAL, ConflictKind.PSEUDO));

			if (ancestor == null) {
				if (left == null) {
					if (right != null) {
						if (fLeftIsLocal) {
							path = "r_inadd_ov";
						} else {
							path = "inadd_ov";
						}
					}
				} else if (right == null) {
					if (fLeftIsLocal) {
						path = "r_outadd_ov";
					} else {
						path = "outadd_ov";
					}
				} else if (hasConflicts && any(differences, hasConflict(ConflictKind.REAL))) {
					path = "confadd_ov.png";
				}
			} else if (left == null) {
				if (right == null) {
					// path = Differencer.CONFLICTING | Differencer.DELETION |
					// Differencer.PSEUDO_CONFLICT;
				} else if (!hasConflicts) {
					if (fLeftIsLocal) {
						path = "r_outdel_ov";
					} else {
						path = "outdel_ov";
					}
				} else if (any(differences, hasConflict(ConflictKind.REAL))) {
					path = "confdel_ov.png";
				}
			} else if (right == null) {
				if (!hasConflicts) {
					if (fLeftIsLocal) {
						path = "r_indel_ov";
					} else {
						path = "indel_ov";
					}
				} else if (any(differences, hasConflict(ConflictKind.REAL))) {
					path = "confchg_ov.png";
				}
			} else {
				boolean ay = isEmpty(filter(differences, LEFT_DIFF));
				boolean am = isEmpty(filter(differences, RIGHT_DIFF));

				if (isEmpty(differences)) {
					// empty
				} else if (ay && !am) {
					if (fLeftIsLocal) {
						path = "r_inchg_ov";
					} else {
						path = "inchg_ov";
					}
				} else if (!ay && am) {
					if (fLeftIsLocal) {
						path = "r_outchg_ov";
					} else {
						path = "outchg_ov";
					}
				} else {
					if (hasConflicts && any(differences, hasConflict(ConflictKind.REAL))) {
						path = "confchg_ov.png";
					} else {
						path = "r_inoutchg_ov.gif";
					}
				}
			}
		} else if (left == null) {
			if (right != null) {
				if (fLeftIsLocal) {
					path = "add_ov";
				} else {
					path = "del_ov";
				}
			}
		} else if (right == null) {
			if (fLeftIsLocal) {
				path = "del_ov";
			} else {
				path = "add_ov";
			}
		} else if (!isEmpty(differences)) {
			path = "chg_ov";
		}

		String ret = null;
		if (path != null) {
			ret = "full/ovr16/" + path;
		}
		return ret;
	}

	private static final Predicate<Diff> LEFT_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getSource() == DifferenceSource.LEFT;
		}
	};

	private static final Predicate<Diff> RIGHT_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getSource() == DifferenceSource.RIGHT;
		}
	};

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
