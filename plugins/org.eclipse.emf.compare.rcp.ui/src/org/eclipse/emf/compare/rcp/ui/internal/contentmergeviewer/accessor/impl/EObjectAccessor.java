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
package org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.impl;

import com.google.common.collect.Maps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.IEObjectAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.impl.TypeConstants;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * A {@link ITypedElement} that can be used as input of TreeContentMergeViewer. It is implementing
 * {@link IStreamContentAccessor} to be able to compare XMI serialization of wrapped {@link EObject}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EObjectAccessor implements IEObjectAccessor {

	/**
	 * The wrapped {@link EObject}.
	 */
	// private final EObject fEObject;

	/**
	 * The adapter factory that will be used to get the name and the image of the wrapped EObject.
	 */
	private final AdapterFactory fAdapterFactory;

	private final Match fOwnerMatch;

	private final MergeViewerSide fSide;

	/**
	 * Creates a new object wrapping the given <code>eObject</code>.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to get the image from.
	 * @param eObject
	 *            the {@link EObject} to wrap.
	 */
	public EObjectAccessor(AdapterFactory adapterFactory, Match match, MergeViewerSide side) {
		fAdapterFactory = adapterFactory;
		fOwnerMatch = match;
		fSide = side;
	}

	/**
	 * @return the fSide
	 */
	protected final MergeViewerSide getSide() {
		return fSide;
	}

	protected EObject getEObject(MergeViewerSide side) {
		final EObject eObject;
		switch (side) {
			case ANCESTOR:
				eObject = fOwnerMatch.getOrigin();
				break;
			case LEFT:
				eObject = fOwnerMatch.getLeft();
				break;
			case RIGHT:
				eObject = fOwnerMatch.getRight();
				break;
			default:
				throw new IllegalStateException();
		}
		return eObject;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.accessor.compare.ITypedElement#getName()
	 */
	public String getName() {
		return getEObject(getSide()).eClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.wrapper.accessor.compare.ITypedElement#getImage()
	 */
	public Image getImage() {
		Adapter adapter = fAdapterFactory.adapt(getEObject(), IItemLabelProvider.class);
		if (adapter instanceof IItemLabelProvider) {
			Object image = ((IItemLabelProvider)adapter).getImage(getEObject());
			return ExtendedImageRegistry.getInstance().getImage(image);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.provider.IEObjectAccessor#getEObject()
	 */
	public EObject getEObject() {
		return getEObject(getSide());
	}

	public InputStream getContents() throws CoreException {
		XMIResourceImpl r = new XMIResourceImpl(URI.createURI("dummy.xmi")); //$NON-NLS-1$

		final ProperContentCopier copier = new ProperContentCopier();
		final EObject copy = copier.copy(getEObject());
		copier.copyReferences();

		r.getContents().add(copy);
		StringWriter sw = new StringWriter();
		try {
			r.save(sw, Maps.newHashMap());
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, EMFCompareRCPUIPlugin.PLUGIN_ID,
					e.getMessage(), e));
		}
		// Assume that the platform locale is appropriate.
		return new ByteArrayInputStream(sw.toString().getBytes());
	}

	private class ProperContentCopier extends EcoreUtil.Copier {
		/** Generated SUID. */
		private static final long serialVersionUID = -5458049632291531717L;

		public ProperContentCopier() {
			this.resolveProxies = false;
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Implementation mostly copy/pasted from EcoreUtil.Copier#copyContainment(EReference, EObject,
		 * EObject). We're only making sure not to resolve any proxy to another resource.
		 * </p>
		 * 
		 * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#copyContainment(org.eclipse.emf.ecore.EReference,
		 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
		 */
		@Override
		protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject) {
			if (!eObject.eIsSet(eReference)) {
				return;
			}

			if (eReference.isMany()) {
				final Iterator<EObject> source = new BoundProperContentIterator(eObject, eReference);
				@SuppressWarnings("unchecked")
				List<EObject> target = (List<EObject>)copyEObject.eGet(getTarget(eReference), resolveProxies);
				if (!source.hasNext()) {
					target.clear();
				} else {
					while (source.hasNext()) {
						final EObject next = source.next();
						target.add(copy(next));
					}
				}
			} else {
				/*
				 * TODO untested yet as this is a rare case. EMF ignors the "resolve" argument of eGet for
				 * multi-valued containment features. Will it honor it for unique features?
				 */
				EObject childEObject = (EObject)eObject.eGet(eReference, resolveProxies);
				if (childEObject == null) {
					copyEObject.eSet(getTarget(eReference), null);
				} else {
					copyEObject.eSet(getTarget(eReference), copy(childEObject));
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * <p>
		 * Implementation mostly copy/pasted from EcoreUtil.Copier#copyContainment(EReference, EObject,
		 * EObject). We're only making sure not to resolve any proxy to another resource.
		 * </p>
		 * 
		 * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#copyReference(org.eclipse.emf.ecore.EReference,
		 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
		 */
		@Override
		protected void copyReference(EReference eReference, EObject eObject, EObject copyEObject) {
			if (!eObject.eIsSet(eReference)) {
				return;
			}

			if (eReference.isMany()) {
				final Iterator<EObject> source = new BoundProperContentIterator(eObject, eReference);
				@SuppressWarnings("unchecked")
				InternalEList<EObject> target = (InternalEList<EObject>)copyEObject.eGet(
						getTarget(eReference), false);
				if (!source.hasNext()) {
					target.clear();
				} else {
					boolean isBidirectional = eReference.getEOpposite() != null;
					int index = 0;
					while (source.hasNext()) {
						final EObject next = source.next();
						final EObject copyReferencedEObject = get(next);

						if (copyReferencedEObject == null) {
							if (useOriginalReferences && !isBidirectional) {
								target.addUnique(index, next);
								++index;
							}
						} else {
							if (isBidirectional) {
								int position = target.indexOf(copyReferencedEObject);
								if (position == -1) {
									target.addUnique(index, copyReferencedEObject);
								} else if (index != position) {
									target.move(index, copyReferencedEObject);
								}
							} else {
								target.addUnique(index, copyReferencedEObject);
							}
							++index;
						}
					}
				}
			} else {
				/*
				 * TODO untested yet as this is a rare case. EMF ignors the "resolve" argument of eGet for
				 * multi-valued containment features. Will it honor it for unique features?
				 */
				final Object referencedEObject = eObject.eGet(eReference, resolveProxies);
				if (referencedEObject == null) {
					copyEObject.eSet(getTarget(eReference), null);
				} else {
					final Object copyReferencedEObject = get(referencedEObject);
					if (copyReferencedEObject == null) {
						if (useOriginalReferences && eReference.getEOpposite() == null) {
							copyEObject.eSet(getTarget(eReference), referencedEObject);
						}
					} else {
						copyEObject.eSet(getTarget(eReference), copyReferencedEObject);
					}
				}
			}
		}
	}

	private final class BoundProperContentIterator extends EcoreUtil.ProperContentIterator<EObject> {
		public BoundProperContentIterator(EObject eObject, EReference eReference) {
			super(eObject, false);
			// Calling super-constructor since we need to, but we'll override what it just did
			@SuppressWarnings("unchecked")
			final List<EObject> contents = (List<EObject>)ReferenceUtil.safeEGet(eObject, eReference);
			if (contents instanceof InternalEList<?>) {
				this.iterator = ((InternalEList<EObject>)contents).basicIterator();
			} else {
				this.iterator = contents.iterator();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE__EMATCH;
	}
}
