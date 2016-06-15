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
package org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
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
 * {@link org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.ACliche2} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class ACliche2ItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public ACliche2ItemProvider(AdapterFactory adapterFactory) {
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

			addSingleValuedAttributePropertyDescriptor(object);
			addManyValuedAttributePropertyDescriptor(object);
			addSingleValuedReferencePropertyDescriptor(object);
			addManyValuedReferencePropertyDescriptor(object);
			addBase_ClassPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Single Valued Attribute feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addSingleValuedAttributePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ACliche2_singleValuedAttribute_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_ACliche2_singleValuedAttribute_feature", //$NON-NLS-1$ //$NON-NLS-2$
						"_UI_ACliche2_type"), //$NON-NLS-1$
				UML2CompareTestProfilePackage.Literals.ACLICHE2__SINGLE_VALUED_ATTRIBUTE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Many Valued Attribute feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addManyValuedAttributePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ACliche2_manyValuedAttribute_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_ACliche2_manyValuedAttribute_feature", //$NON-NLS-1$ //$NON-NLS-2$
						"_UI_ACliche2_type"), //$NON-NLS-1$
				UML2CompareTestProfilePackage.Literals.ACLICHE2__MANY_VALUED_ATTRIBUTE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Single Valued Reference feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addSingleValuedReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ACliche2_singleValuedReference_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_ACliche2_singleValuedReference_feature", //$NON-NLS-1$ //$NON-NLS-2$
						"_UI_ACliche2_type"), //$NON-NLS-1$
				UML2CompareTestProfilePackage.Literals.ACLICHE2__SINGLE_VALUED_REFERENCE, true, false, true,
				null, null, null));
	}

	/**
	 * This adds a property descriptor for the Many Valued Reference feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addManyValuedReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ACliche2_manyValuedReference_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_ACliche2_manyValuedReference_feature", //$NON-NLS-1$ //$NON-NLS-2$
						"_UI_ACliche2_type"), //$NON-NLS-1$
				UML2CompareTestProfilePackage.Literals.ACLICHE2__MANY_VALUED_REFERENCE, true, false, true,
				null, null, null));
	}

	/**
	 * This adds a property descriptor for the Base Class feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addBase_ClassPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ACliche2_base_Class_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_ACliche2_base_Class_feature", //$NON-NLS-1$ //$NON-NLS-2$
						"_UI_ACliche2_type"), //$NON-NLS-1$
				UML2CompareTestProfilePackage.Literals.ACLICHE2__BASE_CLASS, true, false, true, null, null,
				null));
	}

	/**
	 * This returns ACliche2.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated not
	 */
	@Override
	public Object getImage(Object object) {
		// Custom icon for test on item providers
		return overlayImage(object, getResourceLocator().getImage("full/obj16/eclipse_kepler")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ACliche2)object).getSingleValuedAttribute();
		return label == null || label.length() == 0 ? getString("_UI_ACliche2_type") : //$NON-NLS-1$
				getString("_UI_ACliche2_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

		switch (notification.getFeatureID(ACliche2.class)) {
			case UML2CompareTestProfilePackage.ACLICHE2__SINGLE_VALUED_ATTRIBUTE:
			case UML2CompareTestProfilePackage.ACLICHE2__MANY_VALUED_ATTRIBUTE:
				fireNotifyChanged(
						new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
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

	/**
	 * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return UML2CompareTestProfileEditPlugin.INSTANCE;
	}

}
