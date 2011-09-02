/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Andreas Mayer - bug 356097
 *******************************************************************************/
package org.eclipse.emf.compare.diff.provider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.diff.metamodel.UpdateReference}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class UpdateReferenceItemProvider extends ReferenceChangeItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("hiding")
	public UpdateReferenceItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns UpdateReference.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		final UpdateReference updateReference = (UpdateReference)object;

		final Object operationImage;
		if (updateReference.isRemote()) {
			operationImage = getResourceLocator().getImage("full/obj16/RemoteUpdateUniqueReferenceValue"); //$NON-NLS-1$
		} else {
			operationImage = getResourceLocator().getImage("full/obj16/UpdateReference"); //$NON-NLS-1$
		}

		EObject value = getLeftValue(updateReference);
		if (value == null) {
			value = getRightValue(updateReference);
		}

		if (value != null) {
			final Object labelImage = AdapterUtils.getItemProviderImage(value);
			if (labelImage != null) {
				final List<Object> images = Arrays.asList(labelImage, operationImage);
				return new ComposedImage(images);
			}
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
						getString("_UI_UpdateReference_leftTarget_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_UpdateReference_leftTarget_feature", "_UI_UpdateReference_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						DiffPackage.Literals.UPDATE_REFERENCE__LEFT_TARGET, true, false, true, null, null,
						null));
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
						getString("_UI_UpdateReference_rightTarget_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_UpdateReference_rightTarget_feature", "_UI_UpdateReference_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						DiffPackage.Literals.UPDATE_REFERENCE__RIGHT_TARGET, true, false, true, null, null,
						null));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		final UpdateReference operation = (UpdateReference)object;

		final String elementLabel = AdapterUtils.getItemProviderText(operation.getLeftElement());
		final String referenceLabel = AdapterUtils.getItemProviderText(operation.getReference());
		final String leftValueLabel = AdapterUtils.getItemProviderText(getLeftValue(operation));
		final String rightValueLabel = AdapterUtils.getItemProviderText(getRightValue(operation));

		final String diffLabel;
		if (operation.isRemote()) {
			diffLabel = getString("_UI_RemoteUpdateReference_type", //$NON-NLS-1$ 
					new Object[] {referenceLabel, elementLabel, leftValueLabel, rightValueLabel, });
		} else {
			if (operation.isConflicting()) {
				diffLabel = getString("_UI_UpdateReference_conflicting", //$NON-NLS-1$
						new Object[] {referenceLabel, elementLabel, rightValueLabel, leftValueLabel, });
			} else {
				diffLabel = getString("_UI_UpdateReference_type", //$NON-NLS-1$
						new Object[] {referenceLabel, elementLabel, rightValueLabel, leftValueLabel, });
			}
		}

		return diffLabel;
	}

	/**
	 * Returns the value of the given operation's target reference for the left element.
	 * 
	 * @param operation
	 *            The update reference operation for which we need target information.
	 * @return The value of the given operation's target reference for the left element.
	 */
	private EObject getLeftValue(UpdateReference operation) {
		final EReference reference = operation.getReference();
		return (EObject)operation.getLeftElement().eGet(reference);
	}

	/**
	 * Returns the value of the given operation's target reference for the right element.
	 * 
	 * @param operation
	 *            The update reference operation for which we need target information.
	 * @return The value of the given operation's target reference for the right element.
	 */
	private EObject getRightValue(UpdateReference operation) {
		final EReference reference = operation.getReference();
		return (EObject)operation.getRightElement().eGet(reference);
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
		super.notifyChanged(notification);
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
