/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer;

import com.google.common.collect.ForwardingObject;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.ICompareInputChangeListener;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.TypeConstants;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class ForwardingCompareInput extends ForwardingObject implements ICompareInput {

	private final ICompareInput fDelegate;

	private final ListenerList listeners;

	protected ForwardingCompareInput(ICompareInput delegate) {
		fDelegate = getRootCompareInput(delegate);
		fDelegate.addCompareInputChangeListener(new ICompareInputChangeListener() {
			public void compareInputChanged(ICompareInput source) {
				fireChange();
			}
		});
		listeners = new ListenerList(ListenerList.IDENTITY);
	}

	/**
	 * @param delegate
	 * @return
	 */
	private static ICompareInput getRootCompareInput(ICompareInput compareInput) {
		if (compareInput instanceof ForwardingCompareInput) {
			return getRootCompareInput(((ForwardingCompareInput)compareInput).delegate());
		} else {
			return compareInput;
		}
	}

	@Override
	public final ICompareInput delegate() {
		return fDelegate;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getName()
	 */
	public String getName() {
		return delegate().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getImage()
	 */
	public Image getImage() {
		return delegate().getImage();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getKind()
	 */
	public int getKind() {
		return delegate().getKind();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getAncestor()
	 */
	public ITypedElement getAncestor() {
		ITypedElement typedElement = delegate().getAncestor();
		if (typedElement != null) {
			return createForwardingTypedElement(typedElement);
		} else {
			return null;
		}
	}

	protected ForwardingTypedElement createForwardingTypedElement(ITypedElement typedElement) {
		return new ForwardingTypedElement(typedElement);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getLeft()
	 */
	public ITypedElement getLeft() {
		ITypedElement typedElement = delegate().getLeft();
		if (typedElement != null) {
			return createForwardingTypedElement(typedElement);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.structuremergeviewer.ICompareInput#getRight()
	 */
	public ITypedElement getRight() {
		ITypedElement typedElement = delegate().getRight();
		if (typedElement != null) {
			return createForwardingTypedElement(typedElement);
		} else {
			return null;
		}
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
						listener.compareInputChanged(ForwardingCompareInput.this);
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
		delegate().copy(leftToRight);
	}

	public static class ForwardingTypedElement extends ForwardingObject implements ITypedElement {
	
		private final ITypedElement delegate;
	
		public ForwardingTypedElement(ITypedElement delegate) {
			this.delegate = delegate;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.ITypedElement#getName()
		 */
		public String getName() {
			return "__" + delegate().getName() + "__"; //$NON-NLS-1$//$NON-NLS-2$
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.ITypedElement#getImage()
		 */
		public Image getImage() {
			return delegate().getImage();
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.compare.ITypedElement#getType()
		 */
		public String getType() {
			return TypeConstants.TYPE_FALLBACK_TEXT;
		}
	
		/**
		 * {@inheritDoc}
		 * 
		 * @see com.google.common.collect.ForwardingObject#delegate()
		 */
		@Override
		protected ITypedElement delegate() {
			return delegate;
		}
	
	}

}
