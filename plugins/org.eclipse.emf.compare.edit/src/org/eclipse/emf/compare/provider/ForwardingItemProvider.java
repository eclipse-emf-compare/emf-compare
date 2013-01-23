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
package org.eclipse.emf.compare.provider;

import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ForwardingItemProvider implements Adapter.Internal, IChangeNotifier, IDisposable, CreateChildCommand.Helper, ResourceLocator, IEditingDomainItemProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider, IItemFontProvider {

	private ItemProviderAdapter fDelegate;

	/**
	 * @param adapterFactory
	 */
	public ForwardingItemProvider(ItemProviderAdapter delegate) {
		this.fDelegate = delegate;
	}

	/**
	 * @return the fDelegate
	 */
	protected ItemProviderAdapter delegate() {
		return fDelegate;
	}

	/**
	 * 
	 */
	public AdapterFactory getAdapterFactory() {
		return delegate().getAdapterFactory();
	}

	protected AdapterFactory getRootAdapterFactory() {
		if (delegate().getAdapterFactory() instanceof ComposeableAdapterFactory) {
			return ((ComposeableAdapterFactory)delegate().getAdapterFactory()).getRootAdapterFactory();
		}

		return delegate().getAdapterFactory();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		delegate().notifyChanged(notification);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return delegate().getTarget();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		delegate().setTarget(newTarget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return delegate().isAdapterForType(type);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemPropertySource#getPropertyDescriptors(java.lang.Object)
	 */
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		return delegate().getPropertyDescriptors(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemPropertySource#getPropertyDescriptor(java.lang.Object,
	 *      java.lang.Object)
	 */
	public IItemPropertyDescriptor getPropertyDescriptor(Object object, Object propertyID) {
		return delegate().getPropertyDescriptor(object, propertyID);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemPropertySource#getEditableValue(java.lang.Object)
	 */
	public Object getEditableValue(Object object) {
		return delegate().getEditableValue(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemLabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object object) {
		return delegate().getText(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemLabelProvider#getImage(java.lang.Object)
	 */
	public Object getImage(Object object) {
		return delegate().getImage(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ITreeItemContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object object) {
		return delegate().hasChildren(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IStructuredItemContentProvider#getElements(java.lang.Object)
	 */
	public Collection<?> getElements(Object object) {
		return delegate().getElements(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getChildren(java.lang.Object)
	 */
	public Collection<?> getChildren(Object object) {
		return delegate().getChildren(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object object) {
		return delegate().getParent(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#getNewChildDescriptors(java.lang.Object,
	 *      org.eclipse.emf.edit.domain.EditingDomain, java.lang.Object)
	 */
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) {
		return delegate().getNewChildDescriptors(object, editingDomain, sibling);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IEditingDomainItemProvider#createCommand(java.lang.Object,
	 *      org.eclipse.emf.edit.domain.EditingDomain, java.lang.Class,
	 *      org.eclipse.emf.edit.command.CommandParameter)
	 */
	public Command createCommand(Object object, EditingDomain editingDomain,
			Class<? extends Command> commandClass, CommandParameter commandParameter) {
		return delegate().createCommand(object, editingDomain, commandClass, commandParameter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.util.ResourceLocator#getBaseURL()
	 */
	public URL getBaseURL() {
		return delegate().getBaseURL();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.util.ResourceLocator#getImage(java.lang.String)
	 */
	public Object getImage(String key) {
		return delegate().getImage(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.util.ResourceLocator#getString(java.lang.String)
	 */
	public String getString(String key) {
		return delegate().getString(key);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.util.ResourceLocator#getString(java.lang.String, boolean)
	 */
	public String getString(String key, boolean translate) {
		return delegate().getString(key, translate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.util.ResourceLocator#getString(java.lang.String, java.lang.Object[])
	 */
	public String getString(String key, Object[] substitutions) {
		return delegate().getString(key, substitutions);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.util.ResourceLocator#getString(java.lang.String, java.lang.Object[],
	 *      boolean)
	 */
	public String getString(String key, Object[] substitutions, boolean translate) {
		return delegate().getString(key, substitutions, translate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.CreateChildCommand.Helper#getCreateChildResult(java.lang.Object)
	 */
	public Collection<?> getCreateChildResult(Object child) {
		return delegate().getCreateChildResult(child);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.CreateChildCommand.Helper#getCreateChildText(java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.util.Collection)
	 */
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		return delegate().getCreateChildText(owner, feature, child, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.CreateChildCommand.Helper#getCreateChildDescription(java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.util.Collection)
	 */
	public String getCreateChildDescription(Object owner, Object feature, Object child,
			Collection<?> selection) {
		return delegate().getCreateChildDescription(owner, feature, child, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.CreateChildCommand.Helper#getCreateChildToolTipText(java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.util.Collection)
	 */
	public String getCreateChildToolTipText(Object owner, Object feature, Object child,
			Collection<?> selection) {
		return delegate().getCreateChildToolTipText(owner, feature, child, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.command.CreateChildCommand.Helper#getCreateChildImage(java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.util.Collection)
	 */
	public Object getCreateChildImage(Object owner, Object feature, Object child, Collection<?> selection) {
		return delegate().getCreateChildImage(owner, feature, child, selection);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IDisposable#dispose()
	 */
	public void dispose() {
		delegate().dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#fireNotifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void fireNotifyChanged(Notification notification) {
		delegate().fireNotifyChanged(notification);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#addListener(org.eclipse.emf.edit.provider.INotifyChangedListener)
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		delegate().addListener(notifyChangedListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IChangeNotifier#removeListener(org.eclipse.emf.edit.provider.INotifyChangedListener)
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		delegate().removeListener(notifyChangedListener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter.Internal#unsetTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void unsetTarget(Notifier oldTarget) {
		delegate().unsetTarget(oldTarget);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemFontProvider#getFont(java.lang.Object)
	 */
	public Object getFont(Object object) {
		return delegate().getFont(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemColorProvider#getForeground(java.lang.Object)
	 */
	public Object getForeground(Object object) {
		return delegate().getForeground(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IItemColorProvider#getBackground(java.lang.Object)
	 */
	public Object getBackground(Object object) {
		return delegate().getBackground(object);
	}

}
