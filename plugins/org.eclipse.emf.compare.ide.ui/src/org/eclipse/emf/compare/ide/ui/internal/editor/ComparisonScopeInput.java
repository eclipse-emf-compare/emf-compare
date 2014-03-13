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
package org.eclipse.emf.compare.ide.ui.internal.editor;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.AccessorAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.impl.TypedNotifier;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComparisonScopeInput implements ICompareInput {

	private final int kind;

	private final IComparisonScope scope;

	private final AdapterFactory adapterFactory;

	private final ListenerList listeners;

	public ComparisonScopeInput(IComparisonScope scope, AdapterFactory adapterFactory) {
		this.scope = scope;
		this.adapterFactory = adapterFactory;

		if (scope.getOrigin() == null) {
			this.kind = Differencer.CHANGE;
		} else {
			this.kind = Differencer.CONFLICTING;
		}

		listeners = new ListenerList(ListenerList.IDENTITY);
	}

	/**
	 * @return the scope
	 */
	public IComparisonScope getComparisonScope() {
		return scope;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getName()
	 */
	public String getName() {
		return getMainElement().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getImage()
	 */
	public Image getImage() {
		return getMainElement().getImage();
	}

	/**
	 * Return the main non-null element that identifies this input. By default, the left is returned if
	 * non-null. If the left is null, the right is returned. If both the left and right are null the ancestor
	 * is returned.
	 * 
	 * @return the main non-null element that identifies this input
	 */
	private ITypedElement getMainElement() {
		if (getLeft() != null) {
			return getLeft();
		}
		if (getRight() != null) {
			return getRight();
		}
		return getAncestor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getKind()
	 */
	public int getKind() {
		return kind;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		if (scope.getOrigin() == null) {
			return null;
		}
		return AccessorAdapter.adapt(new TypedNotifier(adapterFactory, scope.getOrigin()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		if (scope.getLeft() == null) {
			return null;
		}
		return AccessorAdapter.adapt(new TypedNotifier(adapterFactory, scope.getLeft()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		if (scope.getRight() == null) {
			return null;
		}
		return AccessorAdapter.adapt(new TypedNotifier(adapterFactory, scope.getRight()));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#addCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void addCompareInputChangeListener(ICompareInputChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#removeCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire a compare input change event. This method must be called from the UI thread.
	 */
	protected void fireChange() {
		if (!listeners.isEmpty()) {
			Object[] allListeners = listeners.getListeners();
			for (int i = 0; i < allListeners.length; i++) {
				final ICompareInputChangeListener listener = (ICompareInputChangeListener)allListeners[i];
				SafeRunner.run(new ISafeRunnable() {
					public void run() throws Exception {
						listener.compareInputChanged(ComparisonScopeInput.this);
					}

					public void handleException(Throwable exception) {
						// Logged by the safe runner
					}
				});
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#copy(boolean)
	 */
	public void copy(boolean leftToRight) {
		throw new UnsupportedOperationException(ComparisonScopeInput.class.getName() + "#copy(boolean)"); //$NON-NLS-1$
	}
}
