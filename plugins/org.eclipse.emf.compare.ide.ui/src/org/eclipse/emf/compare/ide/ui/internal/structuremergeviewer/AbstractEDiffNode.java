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
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.AccessorAdapter;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.factory.IAccessorFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class AbstractEDiffNode extends AdapterImpl implements ICompareInput {

	/**
	 * 
	 */
	private final ListenerList fListener;

	/**
	 * The {@link AdapterFactory} used to implement {@link #getName()} and {@link #getImage()}.
	 */
	private final AdapterFactory fAdapterFactory;

	/**
	 * Simple constructor storing the given {@link AdapterFactory}.
	 * 
	 * @param adapterFactory
	 *            the factory.
	 */
	public AbstractEDiffNode(AdapterFactory adapterFactory) {
		fAdapterFactory = adapterFactory;
		fListener = new ListenerList();
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == fAdapterFactory;
	}

	/**
	 * Final accessor to the {@link AdapterFactory} for sub classses.
	 * 
	 * @return the wrapped {@link AdapterFactory}.
	 */
	protected final AdapterFactory getAdapterFactory() {
		return fAdapterFactory;
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
		IAccessorFactory.Registry factoryRegistry = EMFCompareRCPUIPlugin.getDefault()
				.getAccessorFactoryRegistry();
		return factoryRegistry.getHighestRankingFactory(getTarget());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		Image ret = null;
		Adapter adapter = getAdapterFactory().adapt(target, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			Object imageObject = ((IItemLabelProvider)adapter).getImage(target);
			ret = ExtendedImageRegistry.getInstance().getImage(imageObject);
		}
		return ret;
	}

	public int getKind() {
		return Differencer.NO_CHANGE;
	}

	public String getName() {
		String ret = null;
		Adapter adapter = getAdapterFactory().adapt(target, IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			ret = ((IItemLabelProvider)adapter).getText(target);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		final ITypedElement ret;
		Notifier notifier = getTarget();
		boolean isThreeWay = false;
		if (notifier instanceof Diff) {
			isThreeWay = ((Diff)notifier).getMatch().getComparison().isThreeWay();
		} else if (notifier instanceof Match) {
			isThreeWay = ((Match)notifier).getComparison().isThreeWay();
		} else if (notifier instanceof Conflict) {
			isThreeWay = true;
		} else if (notifier instanceof MatchResource) {
			isThreeWay = ((MatchResource)notifier).getComparison().isThreeWay();
		}
		if (isThreeWay) {
			IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
			if (accessorFactory != null) {
				org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement typedElement = accessorFactory
						.createAncestor(getAdapterFactory(), getTarget());
				if (typedElement != null) {
					ret = AccessorAdapter.adapt(typedElement);
				} else {
					ret = null;
				}
			} else {
				ret = null;
			}
		} else {
			ret = null;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		final ITypedElement ret;
		IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
		if (accessorFactory != null) {
			org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement typedElement = accessorFactory
					.createLeft(getAdapterFactory(), getTarget());
			if (typedElement != null) {
				ret = AccessorAdapter.adapt(typedElement);
			} else {
				ret = null;
			}
		} else {
			ret = null;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		final ITypedElement ret;
		IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
		if (accessorFactory != null) {
			org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement typedElement = accessorFactory
					.createRight(getAdapterFactory(), getTarget());
			if (typedElement != null) {
				ret = AccessorAdapter.adapt(typedElement);
			} else {
				ret = null;
			}
		} else {
			ret = null;
		}
		return ret;
	}
}
