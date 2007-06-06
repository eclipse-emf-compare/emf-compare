package org.eclipse.emf.compare.ui.legacy.structuremergeviewer;

import java.io.IOException;

import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.generic.DiffMaker;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Structure viewer used by the
 * {@link org.eclipse.emf.compare.ui.legacy.editor.ModelCompareEditorInput model compare editor}. 
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class ModelStructureContentProvider implements ITreeContentProvider {
	private DiffModel diffInput;

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(final Object parentElement) {
		Object[] children = null;
		if (parentElement instanceof EObject) {
			children = ((EObject)parentElement).eContents().toArray();
		}
		return children;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(final Object element) {
		Object parent = null;
		if (element instanceof EObject) {
			parent = ((EObject)element).eContainer();
		}
		return parent;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(final Object element) {
		boolean hasChildren = false;
		if (element instanceof EObject) {
			hasChildren = !((EObject)element).eContents().isEmpty();
		}
		return hasChildren;
	}

	/**
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(final Object inputElement) {
		Object[] elements = null;
		if (inputElement instanceof DiffModel) {
			elements = ((DiffModel)inputElement).getOwnedElements().toArray();
		} else {
			elements = diffInput.getOwnedElements().toArray();
		}
		return elements;
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// Nothing to dispose here.
	}

	/**
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		((TreeViewer) viewer).getTree().clearAll(true);
		if (newInput instanceof ICompareInput) {
			ITypedElement left = ((ICompareInput)newInput).getLeft();
			ITypedElement right = ((ICompareInput)newInput).getRight();
			
			if (left instanceof IStreamContentAccessor
					&& right instanceof IStreamContentAccessor) {
				try {
					final EObject leftModel = ModelUtils.load(((IStreamContentAccessor)left).getContents(), left.getName());
					final EObject rightModel = ModelUtils.load(((IStreamContentAccessor)right).getContents(), right.getName());
					
					final MatchModel match = new MatchService().doMatch(
							leftModel, rightModel, new NullProgressMonitor());
					final DiffModel diff = new DiffMaker().doDiff(match);
					
					diffInput = diff;
				} catch (InterruptedException e) {
					EMFComparePlugin.getDefault().log(e.getMessage(), true);
				} catch (IOException e) {
					EMFComparePlugin.getDefault().log(e.getMessage(), true);
				} catch (CoreException e) {
					EMFComparePlugin.getDefault().log(e.getMessage(), true);
				}
			}
		} else if (newInput instanceof DiffModel) {
			diffInput = (DiffModel)newInput;
		}
	}
}
