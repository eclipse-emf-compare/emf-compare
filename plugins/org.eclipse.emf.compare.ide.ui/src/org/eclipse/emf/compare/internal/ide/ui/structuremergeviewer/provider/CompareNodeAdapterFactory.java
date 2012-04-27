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
package org.eclipse.emf.compare.internal.ide.ui.structuremergeviewer.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.util.CompareAdapterFactory;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

/**
 * Adapter factory that creates structures for Compare framework.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareNodeAdapterFactory extends CompareAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {

	protected ComposedAdapterFactory parentAdapterFactory;

	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	public CompareNodeAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ComparePackage.eINSTANCE;
		}
		supportedTypes.add(IDiffElement.class);
		supportedTypes.add(IDiffContainer.class);
		supportedTypes.add(ITypedElement.class);
		supportedTypes.add(ICompareInput.class);
	}

	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createComparisonAdapter()
	 */
	@Override
	public Adapter createComparisonAdapter() {
		return new ComparisonNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createMatchResourceAdapter()
	 */
	@Override
	public Adapter createMatchResourceAdapter() {
		return new MatchResourceNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createMatchAdapter()
	 */
	@Override
	public Adapter createMatchAdapter() {
		return new MatchNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createDiffAdapter()
	 */
	@Override
	public Adapter createDiffAdapter() {
		return new DiffNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createResourceAttachmentChangeAdapter()
	 */
	@Override
	public Adapter createResourceAttachmentChangeAdapter() {
		return new ResourceAttachmentChangeNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createReferenceChangeAdapter()
	 */
	@Override
	public Adapter createReferenceChangeAdapter() {
		return new ReferenceChangeNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createAttributeChangeAdapter()
	 */
	@Override
	public Adapter createAttributeChangeAdapter() {
		return new AttributeChangeNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createConflictAdapter()
	 */
	@Override
	public Adapter createConflictAdapter() {
		return new ConflictNode(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.util.CompareAdapterFactory#createEquivalenceAdapter()
	 */
	@Override
	public Adapter createEquivalenceAdapter() {
		return new EquivalenceNode(this);
	}

	/**
	 * This adds a listener.
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	public void dispose() {
	}

}
