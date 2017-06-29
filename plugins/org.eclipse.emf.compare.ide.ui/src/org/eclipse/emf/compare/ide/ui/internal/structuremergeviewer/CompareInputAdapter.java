/*******************************************************************************
 * Copyright (c) 2013, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - integrated model update strategy (bug 457839)
 *     Martin Fleck - bug 518983
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.accessor.AccessorAdapter;
import org.eclipse.emf.compare.internal.utils.ComparisonUtil;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.factory.IAccessorFactory;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.IModelUpdateStrategy;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.IModelUpdateStrategyProvider;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.SingleValuedAttributeModelUpdateStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class CompareInputAdapter extends AdapterImpl implements ICompareInput, IDisposable, IAdaptable {

	private static final ITypedElement NULL_ELEMENT = AccessorAdapter.adapt(null);

	/**
	 * Store the listeners for notifications.
	 */
	private final ListenerList fListener;

	/**
	 * The {@link AdapterFactory} used to implement {@link #getName()} and {@link #getImage()}.
	 */
	private final AdapterFactory fAdapterFactory;

	/** The item delegator to use to retrieve item */
	private final ExtendedAdapterFactoryItemDelegator itemDelegator;

	/** A {@link IDeferredWorkbenchAdapter} to which this compareInput can adapt to. */
	private IDeferredWorkbenchAdapter deferredWorkbenchAdapter;

	/** A {@link IModelUpdateStrategyProvider} providing the strategy for updating the underlying model. */
	private IModelUpdateStrategyProvider modelUpdateStrategyProvider;

	/**
	 * The cached value for {@link #getAncestor()}.
	 */
	private ITypedElement ancestor = NULL_ELEMENT;

	/**
	 * The cached value for {@link #getLeft()}.
	 */
	private ITypedElement left = NULL_ELEMENT;

	/**
	 * The cached value for (@link {@link #getRight()}.
	 */
	private ITypedElement right = NULL_ELEMENT;

	/**
	 * Simple constructor storing the given {@link AdapterFactory}.
	 * 
	 * @param adapterFactory
	 *            the factory.
	 */
	public CompareInputAdapter(AdapterFactory adapterFactory) {
		fAdapterFactory = adapterFactory;
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
		fListener = new ListenerList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(java.lang.Object)
	 */
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
	 * Gets the root factory if this local adapter factory is composed, otherwise just the local one.
	 */
	protected final AdapterFactory getRootAdapterFactory() {
		if (fAdapterFactory instanceof ComposeableAdapterFactory) {
			return ((ComposeableAdapterFactory)fAdapterFactory).getRootAdapterFactory();
		}

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
						listener.compareInputChanged(CompareInputAdapter.this);
					}
				};
				SafeRunner.run(runnable);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public EObject getComparisonObject() {
		// target can be null when getLeft/Right/Ancestor is requested after StructureMergeViewer is disposed.
		if (getTarget() != null) {
			return ((TreeNode)getTarget()).getData();
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#copy(boolean)
	 */
	public void copy(boolean leftToRight) {

	}

	/**
	 * Returns the appropriate {@link IAccessorFactory} from the accessor factory registry.
	 * 
	 * @return the appropriate {@link IAccessorFactory}.
	 */
	protected IAccessorFactory getAccessorFactoryForTarget() {
		IAccessorFactory.Registry factoryRegistry = EMFCompareRCPUIPlugin.getDefault()
				.getAccessorFactoryRegistry();
		return factoryRegistry.getHighestRankingFactory(getComparisonObject());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		Object imageObject = itemDelegator.getImage(getComparisonObject());
		return ExtendedImageRegistry.getInstance().getImage(imageObject);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getKind()
	 */
	public int getKind() {
		Notifier notifier = getComparisonObject();
		boolean isThreeWay = isThreeWay(notifier);
		if (isThreeWay) {
			return Differencer.CONFLICTING;
		}
		return Differencer.NO_CHANGE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getName()
	 */
	public String getName() {
		return itemDelegator.getText(getComparisonObject());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		if (ancestor == NULL_ELEMENT) {
			Notifier notifier = getComparisonObject();
			boolean isThreeWay = isThreeWay(notifier);
			if (isThreeWay) {
				IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
				if (accessorFactory != null) {
					org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement typedElement = accessorFactory
							.createAncestor(getAdapterFactory(), getComparisonObject());
					if (typedElement != null) {
						ancestor = AccessorAdapter.adapt(typedElement);
					} else {
						ancestor = null;
					}
				} else {
					ancestor = null;
				}
			} else {
				ancestor = null;
			}
		}
		return ancestor;
	}

	protected boolean isThreeWay(Notifier notifier) {
		final boolean isThreeWay;
		if (notifier instanceof Diff) {
			if (((Diff)notifier).eContainer() instanceof MatchResource) {
				isThreeWay = ((MatchResource)((Diff)notifier).eContainer()).getComparison().isThreeWay();
			} else {
				isThreeWay = ComparisonUtil.getComparison((Diff)notifier).isThreeWay();
			}
		} else if (notifier instanceof Match) {
			isThreeWay = ((Match)notifier).getComparison().isThreeWay();
		} else if (notifier instanceof Conflict) {
			isThreeWay = true;
		} else if (notifier instanceof MatchResource) {
			isThreeWay = ((MatchResource)notifier).getComparison().isThreeWay();
		} else if (notifier instanceof Comparison) {
			isThreeWay = ((Comparison)notifier).isThreeWay();
		} else {
			isThreeWay = false;
		}
		return isThreeWay;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		if (left == NULL_ELEMENT) {
			IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
			if (accessorFactory != null) {
				org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement typedElement = accessorFactory
						.createLeft(getAdapterFactory(), getComparisonObject());
				if (typedElement != null) {
					left = AccessorAdapter.adapt(typedElement);
				} else {
					left = null;
				}
			} else {
				left = null;
			}
		}
		return left;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		if (right == NULL_ELEMENT) {
			IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
			if (accessorFactory != null) {
				org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement typedElement = accessorFactory
						.createRight(getAdapterFactory(), getComparisonObject());
				if (typedElement != null) {
					right = AccessorAdapter.adapt(typedElement);
				} else {
					right = null;
				}
			} else {
				right = null;
			}
		}
		return right;
	}

	/**
	 * This will remove this adapter from all its the targets and dispose any remaining children wrappers in
	 * the children store.
	 */
	public void dispose() {
		Notifier oldTarget = target;
		target = null;

		if (oldTarget != null) {
			oldTarget.eAdapters().remove(this);
		}
		deferredWorkbenchAdapter = null;
		modelUpdateStrategyProvider = null;
	}

	/**
	 * Set a {@link IDeferredWorkbenchAdapter} for this.
	 * 
	 * @param deferredWorkbenchAdapter
	 */
	public void setDeferredAdapter(IDeferredWorkbenchAdapter deferredWorkbenchAdapter) {
		this.deferredWorkbenchAdapter = deferredWorkbenchAdapter;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see IAdaptable#getAdapter(Class)
	 */
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		if (adapter == IDeferredWorkbenchAdapter.class) {
			return deferredWorkbenchAdapter;
		}
		return null;
	}

	/**
	 * Returns the {@link IModelUpdateStrategy} to be used by content mergers for this compare input.
	 * 
	 * @return The {@link IModelUpdateStrategy} to be used.
	 */
	public IModelUpdateStrategy getModelUpdateStrategy() {
		if (modelUpdateStrategyProvider == null) {
			modelUpdateStrategyProvider = createModelUpdateStrategyProvider();
		}
		return modelUpdateStrategyProvider.getModelUpdateStrategy();
	}

	/**
	 * Creates a {@link IModelUpdateStrategyProvider}.
	 * <p>
	 * Checks if the {@link #getAccessorFactoryForTarget() accessor factory for the target} is a
	 * {@link IModelUpdateStrategyProvider} and, if yes, returns this accessor factory as
	 * {@link IModelUpdateStrategyProvider}. If it is not a model update strategy provider, it will default to
	 * one that always returns the tolerant {@link SingleValuedAttributeModelUpdateStrategy}.
	 * </p>
	 * 
	 * @return The created {@link IModelUpdateStrategyProvider}.
	 */
	private IModelUpdateStrategyProvider createModelUpdateStrategyProvider() {
		final IModelUpdateStrategyProvider ret;
		IAccessorFactory accessorFactory = getAccessorFactoryForTarget();
		if (accessorFactory instanceof IModelUpdateStrategyProvider) {
			ret = (IModelUpdateStrategyProvider)accessorFactory;
		} else {
			ret = new IModelUpdateStrategyProvider() {
				public IModelUpdateStrategy getModelUpdateStrategy() {
					return new SingleValuedAttributeModelUpdateStrategy();
				}
			};
		}
		return ret;
	}
}
