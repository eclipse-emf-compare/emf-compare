/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ReferenceChangeLeftTargetItemProvider extends ReferenceChangeItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("hiding")
	public ReferenceChangeLeftTargetItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns ReferenceChangeLeftTarget.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		final ReferenceChangeLeftTarget operation = (ReferenceChangeLeftTarget)object;

		final Object labelImage = AdapterUtils.getItemProviderImage(operation.getLeftTarget());
		final Object operationImage;
		if (operation.isRemote()) {
			operationImage = getResourceLocator().getImage("full/obj16/RemoteRemoveReferenceValue"); //$NON-NLS-1$
		} else {
			operationImage = getResourceLocator().getImage("full/obj16/AddReferenceValue"); //$NON-NLS-1$
		}

		if (labelImage != null) {
			final List<Object> images = new ArrayList<Object>(2);
			images.add(labelImage);
			images.add(operationImage);
			return new ComposedImage(images);
		}
		return operationImage;
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

			addLeftTargetPropertyDescriptor(object);
			addRightTargetPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Left Target feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addLeftTargetPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_ReferenceChangeLeftTarget_leftTarget_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_ReferenceChangeLeftTarget_leftTarget_feature", "_UI_ReferenceChangeLeftTarget_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						DiffPackage.Literals.REFERENCE_CHANGE_LEFT_TARGET__LEFT_TARGET, true, false, true,
						null, null, null));
	}

	/**
	 * This adds a property descriptor for the Right Target feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addRightTargetPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_ReferenceChangeLeftTarget_rightTarget_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_ReferenceChangeLeftTarget_rightTarget_feature", "_UI_ReferenceChangeLeftTarget_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						DiffPackage.Literals.REFERENCE_CHANGE_LEFT_TARGET__RIGHT_TARGET, true, false, true,
						null, null, null));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		final ReferenceChangeLeftTarget operation = (ReferenceChangeLeftTarget)object;

		final String valueLabel = AdapterUtils.getItemProviderText(operation.getLeftTarget());
		final String referenceLabel = AdapterUtils.getItemProviderText(operation.getReference());
		final String elementLabel = AdapterUtils.getItemProviderText(operation.getRightElement());

		if (operation.isRemote())
			return getString("_UI_RemoteRemoveReferenceValue_type", new Object[] {valueLabel, referenceLabel, //$NON-NLS-1$
					elementLabel, });
		return getString("_UI_AddReferenceValue_type", new Object[] {valueLabel, referenceLabel, //$NON-NLS-1$
				elementLabel, });
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

}
