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
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.IAccessorFactory.Registry;
import org.eclipse.jface.util.SafeRunnable;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEDiffNode extends AbstractEDiffContainer implements ICompareInput {

	/**
	 * 
	 */
	private final ListenerList fListener;

	/**
	 * @param adapterFactory
	 */
	public AbstractEDiffNode(AdapterFactory adapterFactory) {
		super(adapterFactory);
		fListener = new ListenerList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#addCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void addCompareInputChangeListener(ICompareInputChangeListener listener) {
		fListener.add(listener);
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#removeCompareInputChangeListener(org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener)
	 */
	public void removeCompareInputChangeListener(ICompareInputChangeListener listener) {
		fListener.remove(listener);
	}

	/**
	 * Sends out notification that a change has occurred on the <code>ICompareInput</code>.
	 */
	protected void fireChange() {
		if (fListener != null) {
			final Object[] listeners = fListener.getListeners();
			for (int i = 0; i < listeners.length; i++) {
				final ICompareInputChangeListener listener = (ICompareInputChangeListener)listeners[i];
				SafeRunnable runnable = new SafeRunnable() {
					public void run() throws Exception {
						listener.compareInputChanged(AbstractEDiffNode.this);
					}
				};
				SafeRunner.run(runnable);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#copy(boolean)
	 */
	public void copy(boolean leftToRight) {

	}

	protected IAccessorFactory getAccessorFactoryForTarget() {
		Registry factoryRegistry = EMFCompareIDEUIPlugin.getDefault().getAccessorFactoryRegistry();
		return factoryRegistry.getHighestRankingFactory(getTarget());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
		if (accessorFactory != null) {
			return accessorFactory.createAncestor(getAdapterFactory(), getTarget());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
		if (accessorFactory != null) {
			return accessorFactory.createLeft(getAdapterFactory(), getTarget());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
		if (accessorFactory != null) {
			return accessorFactory.createRight(getAdapterFactory(), getTarget());
		}
		return null;
	}
}
