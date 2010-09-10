/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: EMFModelDescriptorItemProvider.java,v 1.1 2010/09/10 15:32:24 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.mpatch.EditPlugin;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorFactory;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage;
import org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EMFModelDescriptorItemProvider
	extends ItemProviderAdapter
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
	public EMFModelDescriptorItemProvider(AdapterFactory adapterFactory) {
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

			addCrossReferencesPropertyDescriptor(object);
			addAllCrossReferencesPropertyDescriptor(object);
			addAllSelfReferencesPropertyDescriptor(object);
			addSubModelDescriptorsPropertyDescriptor(object);
			addDescriptorUrisPropertyDescriptor(object);
			addTypePropertyDescriptor(object);
			addDescriptorUriPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Cross References feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCrossReferencesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IModelDescriptor_crossReferences_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IModelDescriptor_crossReferences_feature", "_UI_IModelDescriptor_type"),
				 MPatchPackage.Literals.IMODEL_DESCRIPTOR__CROSS_REFERENCES,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the All Cross References feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAllCrossReferencesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IModelDescriptor_allCrossReferences_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IModelDescriptor_allCrossReferences_feature", "_UI_IModelDescriptor_type"),
				 MPatchPackage.Literals.IMODEL_DESCRIPTOR__ALL_CROSS_REFERENCES,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Descriptor Uris feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptorUrisPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IModelDescriptor_descriptorUris_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IModelDescriptor_descriptorUris_feature", "_UI_IModelDescriptor_type"),
				 MPatchPackage.Literals.IMODEL_DESCRIPTOR__DESCRIPTOR_URIS,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the All Self References feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAllSelfReferencesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IModelDescriptor_allSelfReferences_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IModelDescriptor_allSelfReferences_feature", "_UI_IModelDescriptor_type"),
				 MPatchPackage.Literals.IMODEL_DESCRIPTOR__ALL_SELF_REFERENCES,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sub Model Descriptors feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSubModelDescriptorsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IModelDescriptor_subModelDescriptors_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IModelDescriptor_subModelDescriptors_feature", "_UI_IModelDescriptor_type"),
				 MPatchPackage.Literals.IMODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_IModelDescriptor_type_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_IModelDescriptor_type_feature", "_UI_IModelDescriptor_type"),
				 MPatchPackage.Literals.IMODEL_DESCRIPTOR__TYPE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Descriptor Uri feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptorUriPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EMFModelDescriptor_descriptorUri_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EMFModelDescriptor_descriptorUri_feature", "_UI_EMFModelDescriptor_type"),
				 DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(MPatchPackage.Literals.IMODEL_DESCRIPTOR__SELF_REFERENCE);
			childrenFeatures.add(DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__ATTRIBUTES);
			childrenFeatures.add(DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS);
			childrenFeatures.add(DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__REFERENCES);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns EMFModelDescriptor.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ModelDescriptor"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		EMFModelDescriptor descriptor = (EMFModelDescriptor) object;
		String info = "contains " + descriptor.getAllCrossReferences().size() + " cross-reference" + (descriptor.getAllCrossReferences().size() == 1 ? "" : "s");
		return getString("_UI_EMFModelDescriptor_type") + " : " + 
				descriptor.getType().getName() + " (" + info + ")";
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

		switch (notification.getFeatureID(EMFModelDescriptor.class)) {
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URIS:
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE:
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES:
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS:
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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
				(MPatchPackage.Literals.IMODEL_DESCRIPTOR__SELF_REFERENCE,
				 MPatchFactory.eINSTANCE.createModelDescriptorReference()));

		newChildDescriptors.add
			(createChildParameter
				(DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__ATTRIBUTES,
				 DescriptorFactory.eINSTANCE.create(DescriptorPackage.Literals.EATTRIBUTE_TO_OBJECT_MAP)));

		newChildDescriptors.add
			(createChildParameter
				(DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS,
				 DescriptorFactory.eINSTANCE.create(DescriptorPackage.Literals.EREFERENCE_TO_DESCRIPTOR_MAP)));

		newChildDescriptors.add
			(createChildParameter
				(DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR__REFERENCES,
				 DescriptorFactory.eINSTANCE.create(DescriptorPackage.Literals.EREFERENCE_TO_ELEMENT_REFERENCE_MAP)));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return EditPlugin.INSTANCE;
	}

}
