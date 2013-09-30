/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.Disposable;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.tree.util.TreeAdapterFactory;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeCompareInputAdapterFactory extends TreeAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 */
	protected final IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IDisposable}.
	 */
	protected Disposable disposable = new Disposable();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 */
	protected final Collection<Object> supportedTypes = newArrayList();

	/**
	 * Creates an {@link ComposeableAdapterFactory} with the following supported types:
	 * <ul>
	 * <li>{@link ICompareInput}</li>.
	 * </ul>
	 * 
	 * @param grouper
	 *            This will be used by the comparison adapter to group differences together.
	 */
	public TreeCompareInputAdapterFactory() {
		supportedTypes.add(ICompareInput.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.AdapterFactory#isFactoryForType(java.lang.Object)
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * 
	 * @param notifier
	 *            the notifier to adapt
	 * @param type
	 *            unused
	 * @return a previously existing associated adapter, a new associated adapter if possible, or
	 *         <code>null</code> otherwise.
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterFactoryImpl#adapt(java.lang.Object, java.lang.Object)
	 */
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
	 * 
	 * @return the root adapter factory that contains this factory.
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		ComposeableAdapterFactory ret = this;
		if (parentAdapterFactory != null) {
			ret = parentAdapterFactory.getRootAdapterFactory();
		}
		return ret;
	}

	/**
	 * This sets the direct parent adapter factory into which this factory is composed.
	 * 
	 * @param parentAdapterFactory
	 *            the direct parent adapter factory into which this factory is composed
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.tree.util.TreeAdapterFactory#createTreeNodeAdapter()
	 */
	@Override
	public Adapter createTreeNodeAdapter() {
		return new TreeNodeCompareInput(this);
	}

	/**
	 * This records adapters for subsequent disposable.
	 */
	@Override
	public Adapter adaptNew(Notifier object, Object type) {
		Adapter result = super.adaptNew(object, type);
		disposable.add(result);
		return result;
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

	/**
	 * This disposes all the disposables.
	 */
	public void dispose() {
		disposable.dispose();
	}
}
