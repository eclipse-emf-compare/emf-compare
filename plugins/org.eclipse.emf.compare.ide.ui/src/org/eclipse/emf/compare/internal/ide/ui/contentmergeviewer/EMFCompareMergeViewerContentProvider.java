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
package org.eclipse.emf.compare.internal.ide.ui.contentmergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareMergeViewerContentProvider implements IMergeViewerContentProvider {

	public static final char ANCESTOR_CONTRIBUTOR = 'A';

	public static final char RIGHT_CONTRIBUTOR = 'R';

	public static final char LEFT_CONTRIBUTOR = 'L';

	private CompareConfiguration fCompareConfiguration;

	private String fAncestorError;

	private String fLeftError;

	private String fRightError;

	public EMFCompareMergeViewerContentProvider(CompareConfiguration cc) {
		fCompareConfiguration = cc;
	}

	private boolean hasError() {
		return fAncestorError != null || fLeftError != null || fRightError != null;
	}

	public void dispose() {
		// empty default implementation
	}

	public void inputChanged(Viewer v, Object o1, Object o2) {
		// we are not interested since we have no state
	}

	// ---- ancestor

	public void setAncestorError(String errorMessage) {
		fAncestorError = errorMessage;
	}

	public String getAncestorLabel(Object element) {
		if (fAncestorError != null) {
			return fAncestorError;
		}
		return fCompareConfiguration.getAncestorLabel(element);
	}

	public Image getAncestorImage(Object element) {
		if (fAncestorError != null) {
			return null;
		}
		return fCompareConfiguration.getAncestorImage(element);
	}

	public Object getAncestorContent(Object element) {
		if (element instanceof ICompareInput) {
			ICompareInput compareInput = (ICompareInput)element;
			Object ret = compareInput.getAncestor();
			// if no ancestor and element is a diff, try to reach the ancestor of parent, recursively
			if (ret == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)compareInput).getParent();
				ret = getAncestorContent(parent);
			}
			return ret;
		}
		return null;
	}

	public boolean showAncestor(Object element) {
		if (element instanceof ICompareInput) {
			return true; // fix for #45239: Show ancestor for incoming and outgoing changes
		}
		// return (((ICompareInput)element).getKind() & Differencer.DIRECTION_MASK) ==
		// Differencer.CONFLICTING;
		return false;
	}

	// ---- left

	public void setLeftError(String errorMessage) {
		fLeftError = errorMessage;
	}

	public String getLeftLabel(Object element) {
		if (fLeftError != null) {
			return fLeftError;
		}
		return fCompareConfiguration.getLeftLabel(element);
	}

	public Image getLeftImage(Object element) {
		if (fLeftError != null) {
			return null;
		}
		return fCompareConfiguration.getLeftImage(element);
	}

	public Object getLeftContent(Object element) {
		if (element instanceof ICompareInput) {
			ICompareInput compareInput = (ICompareInput)element;
			Object ret = compareInput.getLeft();
			// if no left and element is a diff, try to reach the left of parent, recursively
			if (ret == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)compareInput).getParent();
				ret = getLeftContent(parent);
			}
			return ret;
		}
		return null;
	}

	public boolean isLeftEditable(Object element) {
		if (hasError()) {
			return false;
		}
		if (element instanceof ICompareInput) {
			Object left = ((ICompareInput)element).getLeft();
			if (left == null && element instanceof IDiffElement) {
				IDiffElement parent = ((IDiffElement)element).getParent();
				if (parent instanceof ICompareInput) {
					left = ((ICompareInput)parent).getLeft();
				}
			}
			if (left instanceof IEditableContent) {
				return ((IEditableContent)left).isEditable();
			}
		}
		return false;
	}

	public void saveLeftContent(Object element, byte[] bytes) {
		if (element instanceof ICompareInput) {
			ICompareInput node = (ICompareInput)element;
			if (bytes != null) {
				ITypedElement left = node.getLeft();
				// #9869: problem if left is null (because no resource exists yet) nothing is done!
				if (left == null) {
					node.copy(false);
					left = node.getLeft();
				}
				if (left instanceof IEditableContent) {
					((IEditableContent)left).setContent(bytes);
				}

			} else {
				node.copy(false);
			}
		}
	}

	// ---- right

	public void setRightError(String errorMessage) {
		fRightError = errorMessage;
	}

	public String getRightLabel(Object element) {
		if (fRightError != null) {
			return fRightError;
		}
		return fCompareConfiguration.getRightLabel(element);
	}

	public Image getRightImage(Object element) {
		if (fRightError != null) {
			return null;
		}
		return fCompareConfiguration.getRightImage(element);
	}

	public Object getRightContent(Object element) {
		if (element instanceof ICompareInput) {
			ICompareInput compareInput = (ICompareInput)element;
			Object ret = compareInput.getRight();
			// if no right and element is a diff, try to reach the right of parent, recursively
			if (ret == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)compareInput).getParent();
				ret = getRightContent(parent);
			}
			return ret;
		}
		return null;
	}

	public boolean isRightEditable(Object element) {
		if (hasError()) {
			return false;
		}
		if (element instanceof ICompareInput) {
			Object right = ((ICompareInput)element).getRight();
			if (right == null && element instanceof IDiffElement) {
				IDiffContainer parent = ((IDiffElement)element).getParent();
				if (parent instanceof ICompareInput) {
					right = ((ICompareInput)parent).getRight();
				}
			}
			if (right instanceof IEditableContent) {
				return ((IEditableContent)right).isEditable();
			}
		}
		return false;
	}

	public void saveRightContent(Object element, byte[] bytes) {
		if (element instanceof ICompareInput) {
			ICompareInput node = (ICompareInput)element;
			if (bytes != null) {
				ITypedElement right = node.getRight();
				// #9869: problem if right is null (because no resource exists yet) nothing is done!
				if (right == null) {
					node.copy(true);
					right = node.getRight();
				}
				if (right instanceof IEditableContent) {
					((IEditableContent)right).setContent(bytes);
				}

			} else {
				node.copy(true);
			}
		}
	}

}
