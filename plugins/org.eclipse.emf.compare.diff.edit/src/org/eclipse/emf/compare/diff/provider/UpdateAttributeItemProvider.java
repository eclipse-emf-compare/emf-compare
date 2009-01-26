/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.diff.metamodel.UpdateAttribute} object.
 * <!-- begin-user-doc --> <!-- end-user-doc -->
 * @generated
 */
public class UpdateAttributeItemProvider extends AttributeChangeItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("hiding")
	public UpdateAttributeItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns UpdateAttribute.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		final UpdateAttribute updateAttribute = (UpdateAttribute)object;
		Object labelImage = AdapterUtils.getItemProviderImage(updateAttribute.getAttribute());

		if (labelImage != null) {
			final List<Object> images = new ArrayList<Object>(2);
			images.add(labelImage);
			images.add(getResourceLocator().getImage("full/obj16/UpdateAttribute")); //$NON-NLS-1$
			labelImage = new ComposedImage(images);
		} else {
			labelImage = getResourceLocator().getImage("full/obj16/UpdateAttribute"); //$NON-NLS-1$
		}

		return labelImage;
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

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		final UpdateAttribute updateOp = (UpdateAttribute)object;

		final String attributeLabel = AdapterUtils.getItemProviderText(updateOp.getAttribute());
		final String elementLabel = AdapterUtils.getItemProviderText(updateOp.getLeftElement());
		final Object leftValue = updateOp.getLeftElement().eGet(updateOp.getAttribute());
		final Object rightValue = updateOp.getRightElement().eGet(updateOp.getAttribute());

		if (updateOp.isConflicting())
			return getString("_UI_UpdateAttribute_conflicting", new Object[] {attributeLabel, rightValue, //$NON-NLS-1$
					leftValue, });
		return getString("_UI_UpdateAttribute_type", new Object[] {attributeLabel, elementLabel, rightValue, //$NON-NLS-1$
				leftValue, });
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
