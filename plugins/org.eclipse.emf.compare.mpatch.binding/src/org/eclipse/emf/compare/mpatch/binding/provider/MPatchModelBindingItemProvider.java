/**
 *  Copyright (c) 2010, 2011 Technical University of Denmark.
 *  All rights reserved. This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html 
 *  
 *  Contributors:
 *     Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: MPatchModelBindingItemProvider.java,v 1.1 2010/09/10 15:27:17 cbrun Exp $
 */
package org.eclipse.emf.compare.mpatch.binding.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.mpatch.binding.BindingFactory;
import org.eclipse.emf.compare.mpatch.binding.BindingPackage;
import org.eclipse.emf.compare.mpatch.binding.BindingPlugin;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MPatchModelBindingItemProvider
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
	public MPatchModelBindingItemProvider(AdapterFactory adapterFactory) {
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

			addNotesPropertyDescriptor(object);
			addModelPropertyDescriptor(object);
			addMPatchModelPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Notes feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NoteElement_notes_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NoteElement_notes_feature", "_UI_NoteElement_type"),
				 BindingPackage.Literals.NOTE_ELEMENT__NOTES,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MPatchModelBinding_model_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MPatchModelBinding_model_feature", "_UI_MPatchModelBinding_type"),
				 BindingPackage.Literals.MPATCH_MODEL_BINDING__MODEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the MPatch Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMPatchModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MPatchModelBinding_mPatchModel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MPatchModelBinding_mPatchModel_feature", "_UI_MPatchModelBinding_type"),
				 BindingPackage.Literals.MPATCH_MODEL_BINDING__MPATCH_MODEL,
				 true,
				 false,
				 true,
				 null,
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
			childrenFeatures.add(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS);
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
	 * This returns MPatchModelBinding.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/MPatchModelBinding"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_MPatchModelBinding_type");
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

		switch (notification.getFeatureID(MPatchModelBinding.class)) {
			case BindingPackage.MPATCH_MODEL_BINDING__ALL_NOTES:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case BindingPackage.MPATCH_MODEL_BINDING__CHANGE_BINDINGS:
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
				(BindingPackage.Literals.NOTE_CONTAINER__ALL_NOTES,
				 BindingFactory.eINSTANCE.createNote()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createAttributeChangeBinding()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createAddElementChangeBinding()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createMoveElementChangeBinding()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createAddReferenceChangeBinding()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createUpdateReferenceChangeBinding()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createRemoveElementChangeBinding()));

		newChildDescriptors.add
			(createChildParameter
				(BindingPackage.Literals.MPATCH_MODEL_BINDING__CHANGE_BINDINGS,
				 BindingFactory.eINSTANCE.createRemoveReferenceChangeBinding()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return BindingPlugin.INSTANCE;
	}

}
