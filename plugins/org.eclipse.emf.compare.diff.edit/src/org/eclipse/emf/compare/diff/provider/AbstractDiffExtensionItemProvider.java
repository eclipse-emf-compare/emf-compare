/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a
 * {@link org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
public class AbstractDiffExtensionItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("hiding")
	public AbstractDiffExtensionItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addHideElementsPropertyDescriptor(object);
			addIsCollapsedPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DiffEditPlugin.INSTANCE;
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		AbstractDiffExtension abstractDiffExtension = (AbstractDiffExtension)object;
		return getString("_UI_AbstractDiffExtension_type") + " " + abstractDiffExtension.isIsCollapsed(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached children and
	 * by creating a viewer notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(AbstractDiffExtension.class)) {
			case DiffPackage.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false,
						true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds a property descriptor for the Hide Elements feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unused")
	protected void addHideElementsPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_AbstractDiffExtension_hideElements_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_hideElements_feature", "_UI_AbstractDiffExtension_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS, true, false, true, null,
						null, null));
	}

	/**
	 * This adds a property descriptor for the Is Collapsed feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unused")
	protected void addIsCollapsedPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_AbstractDiffExtension_isCollapsed_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_isCollapsed_feature", "_UI_AbstractDiffExtension_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED, true, false, false,
						ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children that can be
	 * created under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

}
