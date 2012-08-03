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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;

import com.google.common.base.Predicate;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.DifferenceState;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ImageProvider {

	private final boolean fLeftIsLocal;

	/**
	 * 
	 */
	public ImageProvider(boolean leftIsLocal) {
		fLeftIsLocal = leftIsLocal;
	}

	public ImageDescriptor getImageDescriptorOverlay(Diff diff) {
		final DifferenceSource source = diff.getSource();
		final Match match = diff.getMatch();
		final Conflict conflict = diff.getConflict();
		final DifferenceKind diffKind = diff.getKind();
		final Comparison c = match.getComparison();
		String path = "/icons/full/ovr16/";

		if (diff.getState() == DifferenceState.MERGED) {
			path += "merged_ov.gif";
		} else if (diff.getState() == DifferenceState.DISCARDED) {
			path += "removed_ov.gif";
		} else if (c.isThreeWay()) {
			String filext = ".gif";
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
				case MOVE:
					path += "chg_ov";
					break;
			}
			path += filext;
		} else {
			path += getPathForTwoWayDiff(diffKind);
		}
		return EMFCompareIDEUIPlugin.getDefault().getImageDescriptor(path);
	}

	private String getPathForTwoWayDiff(final DifferenceKind diffKind) {
		final String path;
		switch (diffKind) {
			case ADD:
				if (fLeftIsLocal) {
					path = "add_ov.gif";
				} else {
					path = "del_ov.gif";
				}
				break;
			case DELETE:
				if (fLeftIsLocal) {
					path = "del_ov.gif";
				} else {
					path = "add_ov.gif";
				}
				break;
			case CHANGE:
			case MOVE:
				path = "chg_ov.gif";
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

	public ImageDescriptor getImageDescriptorOverlay(Match match) {
		String path = null;
		final EObject ancestor = match.getOrigin();
		final EObject left = match.getLeft();
		final EObject right = match.getRight();

		final Iterable<Diff> differences = match.getAllDifferences();

		if (match.getComparison().isThreeWay()) {
			Iterable<Diff> conflictualDiffs = filter(differences, CONFLICTUAL_DIFF);

			if (ancestor == null) {
				if (left == null) {
					if (right != null) {
						if (fLeftIsLocal) {
							path = "r_inadd_ov.gif";
						} else {
							path = "inadd_ov.gif";
						}
					}
				} else {
					if (right == null) {
						if (fLeftIsLocal) {
							path = "r_outadd_ov.gif";
						} else {
							path = "outadd_ov.gif";
						}
					} else {
						if (!isEmpty(conflictualDiffs)) {
							path = "confadd_ov.png";
							if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
								// path |= Differencer.PSEUDO_CONFLICT;
							}
						}
					}
				}
			} else {
				if (left == null) {
					if (right == null) {
						// path = Differencer.CONFLICTING | Differencer.DELETION |
						// Differencer.PSEUDO_CONFLICT;
					} else {
						if (isEmpty(conflictualDiffs)) {
							if (fLeftIsLocal) {
								path = "r_outdel_ov.gif";
							} else {
								path = "outdel_ov.gif";
							}
						} else {
							if (!isEmpty(conflictualDiffs)) {
								path = "confdel_ov.png";
								if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
									// path |= Differencer.PSEUDO_CONFLICT;
								}
							}
						}
					}
				} else {
					if (right == null) {
						if (isEmpty(conflictualDiffs)) {
							if (fLeftIsLocal) {
								path = "r_indel_ov.gif";
							} else {
								path = "indel_ov.gif";
							}
						} else {
							if (!isEmpty(conflictualDiffs)) {
								path = "confchg_ov.png";
								if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
									// path |= Differencer.PSEUDO_CONFLICT;
								}
							}
						}
					} else {
						boolean ay = isEmpty(filter(differences, LEFT_DIFF));
						boolean am = isEmpty(filter(differences, RIGHT_DIFF));

						if (isEmpty(differences)) {
							// empty
						} else if (ay && !am) {
							if (fLeftIsLocal) {
								path = "r_inchg_ov.gif";
							} else {
								path = "inchg_ov.gif";
							}
						} else if (!ay && am) {
							if (fLeftIsLocal) {
								path = "r_outchg_ov.gif";
							} else {
								path = "outchg_ov.gif";
							}
						} else {
							if (!isEmpty(conflictualDiffs)) {
								path = "confchg_ov.png";
								if (all(conflictualDiffs, PSEUDO_CONFLICT)) {
									// path |= Differencer.PSEUDO_CONFLICT;
								}
							}
						}
					}
				}
			}
		} else { // two way compare ignores ancestor
			if (left == null) {
				if (right != null) {
					if (fLeftIsLocal) {
						path = "add_ov.gif";
					} else {
						path = "del_ov.gif";
					}
				}
			} else {
				if (right == null) {
					if (fLeftIsLocal) {
						path = "del_ov.gif";
					} else {
						path = "add_ov.gif";
					}
				} else {
					if (!isEmpty(differences)) {
						path = "chg_ov.gif";
					}
				}
			}
		}

		ImageDescriptor ret = null;
		if (path != null) {
			ret = EMFCompareIDEUIPlugin.getDefault().getImageDescriptor("/icons/full/ovr16/" + path);
		}
		return ret;
	}

	private static final Predicate<Diff> CONFLICTUAL_DIFF = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getConflict() != null;
		}
	};

	private static final Predicate<Diff> REAL_CONFLICT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getConflict().getKind() == ConflictKind.REAL;
		}
	};

	private static final Predicate<Diff> PSEUDO_CONFLICT = new Predicate<Diff>() {
		public boolean apply(Diff input) {
			return input != null && input.getConflict().getKind() == ConflictKind.REAL;
		}
	};

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
}
