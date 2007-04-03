/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy.contentmergeviewer;

import java.io.ByteArrayInputStream;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.ui.legacy.ModelCompareInput;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public class ModelCompareContentProvider implements IMergeViewerContentProvider {

	private CompareConfiguration cc;

	public ModelCompareContentProvider(final CompareConfiguration config) {
		this.cc = config;
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getAncestorContent(java.lang.Object)
	 */
	public Object getAncestorContent(final Object input) {
		if (input == null) {
			return null;
		}

		//		
		// if (!(input instanceof UMLCompareInput))
		// throw new IllegalStateException("Invalid input");
		//
		// return ((UMLCompareInput)input).getEngine().getAncestor();
		// TODOCBR handle 3way diff
		return null;
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getAncestorImage(java.lang.Object)
	 */
	public Image getAncestorImage(final Object input) {
		if (input == null) {
			return null;
		}

		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}

		return this.cc.getAncestorImage(input);
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getAncestorLabel(java.lang.Object)
	 */
	public String getAncestorLabel(final Object input) {
		if (input == null) {
			return null;
		}
		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}
		if (((ModelCompareInput) input).getAncestor() == null) {
			return null;
		}

		return this.cc.getAncestorLabel(input);
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getLeftContent(java.lang.Object)
	 */
	public Object getLeftContent(final Object input) {

		if (input == null) {
			return null;
		}
		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}

		return ((Match2Elements) ((ModelCompareInput) input).getDelta()
				.getMatchedElements().get(0)).getLeftElement();
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getLeftImage(java.lang.Object)
	 */
	public Image getLeftImage(final Object input) {
		if (input == null) {
			return null;
		}

		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}

		return this.cc.getLeftImage(input);
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getLeftLabel(java.lang.Object)
	 */
	public String getLeftLabel(final Object input) {
		if (input == null) {
			return null;
		}

		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}

		if (((ModelCompareInput) input).getLeft() == null) {
			return null;
		}

		return this.cc.getLeftLabel(input);
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getRightContent(java.lang.Object)
	 */
	public Object getRightContent(final Object input) {

		if (input == null) {
			return null;
		}

		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}
		return ((Match2Elements) ((ModelCompareInput) input).getDelta()
				.getMatchedElements().get(0)).getRightElement();
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getRightImage(java.lang.Object)
	 */
	public Image getRightImage(final Object input) {
		if (input == null) {
			return null;
		}

		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}

		return this.cc.getRightImage(input);
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#getRightLabel(java.lang.Object)
	 */
	public String getRightLabel(final Object input) {
		if (input == null) {
			return null;
		}

		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}
		if (((ModelCompareInput) input).getRight() == null) {
			return null;
		}
		return this.cc.getRightLabel(input);
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#isLeftEditable(java.lang.Object)
	 */
	public boolean isLeftEditable(final Object input) {
		if (input == null) {
			return false;
		}
		return ((ModelCompareInput) input).getLeftStorage() != null;
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#isRightEditable(java.lang.Object)
	 */
	public boolean isRightEditable(final Object input) {
		if (input == null) {
			return false;
		}

		return ((ModelCompareInput) input).getRightStorage() != null;
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#saveLeftContent(java.lang.Object,
	 *      byte[])
	 */
	public void saveLeftContent(final Object input, final byte[] bytes) {
		if (input == null) {
			return;
		}

		final Object storage = ((ModelCompareInput) input).getLeftStorage();
		if (storage instanceof IFile) {
			try {
				((IFile) storage).setContents(new ByteArrayInputStream(bytes),
						true, true, new NullProgressMonitor());
			} catch (final CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}
	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#saveRightContent(java.lang.Object,
	 *      byte[])
	 */
	public void saveRightContent(final Object input, final byte[] bytes) {
		if (input == null) {
			return;
		}

		final Object storage = ((ModelCompareInput) input).getRightStorage();
		if (storage instanceof IFile) {
			try {
				((IFile) storage).setContents(new ByteArrayInputStream(bytes),
						true, true, new NullProgressMonitor());
			} catch (final CoreException e) {
				EMFComparePlugin.getDefault().log(e, false);
			}
		}

	}

	/**
	 * @see org.eclipse.compare.contentmergeviewer.IMergeViewerContentProvider#showAncestor(java.lang.Object)
	 */
	public boolean showAncestor(final Object input) {
		if (input == null) {
			return false;
		}
		if (!(input instanceof ModelCompareInput)) {
			throw new IllegalStateException("Invalid input");
		}
		// TODOCBR handle 3way comparison
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

	}

}
