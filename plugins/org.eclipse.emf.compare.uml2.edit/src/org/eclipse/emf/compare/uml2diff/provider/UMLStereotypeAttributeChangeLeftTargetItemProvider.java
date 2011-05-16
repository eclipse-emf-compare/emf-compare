/**
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.uml2diff.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.compare.diff.metamodel.DiffPackage;

import org.eclipse.emf.compare.diff.provider.AttributeChangeLeftTargetItemProvider;

import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.uml2diff.UMLStereotypeAttributeChangeLeftTarget} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UMLStereotypeAttributeChangeLeftTargetItemProvider
	extends AttributeChangeLeftTargetItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UMLStereotypeAttributeChangeLeftTargetItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addHideElementsPropertyDescriptor(object);
			addIsCollapsedPropertyDescriptor(object);
			addStereotypeApplicationsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Hide Elements feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHideElementsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractDiffExtension_hideElements_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_hideElements_feature", "_UI_AbstractDiffExtension_type"),
				 DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Is Collapsed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIsCollapsedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractDiffExtension_isCollapsed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_isCollapsed_feature", "_UI_AbstractDiffExtension_type"),
				 DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Stereotype Applications feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStereotypeApplicationsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UMLStereotypePropertyChange_stereotypeApplications_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UMLStereotypePropertyChange_stereotypeApplications_feature", "_UI_UMLStereotypePropertyChange_type"),
				 UML2DiffPackage.Literals.UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE_APPLICATIONS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns UMLStereotypeAttributeChangeLeftTarget.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/UMLStereotypeAttributeChangeLeftTarget"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		UMLStereotypeAttributeChangeLeftTarget umlStereotypeAttributeChangeLeftTarget = (UMLStereotypeAttributeChangeLeftTarget)object;
		return getString("_UI_UMLStereotypeAttributeChangeLeftTarget_type") + " " + umlStereotypeAttributeChangeLeftTarget.isConflicting();
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(UMLStereotypeAttributeChangeLeftTarget.class)) {
			case UML2DiffPackage.UML_STEREOTYPE_ATTRIBUTE_CHANGE_LEFT_TARGET__IS_COLLAPSED:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLAbstractionChangeLeftTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLAbstractionChangeRightTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLAssociationChangeLeftTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLAssociationChangeRightTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLStereotypeAttributeChangeLeftTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLStereotypeAttributeChangeRightTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLStereotypeUpdateAttribute()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLStereotypeApplicationAddition()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 UML2DiffFactory.eINSTANCE.createUMLStereotypeApplicationRemoval()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return UML2DiffEditPlugin.INSTANCE;
	}

}
