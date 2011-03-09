/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: ChangeGroupItemProvider.java,v 1.1 2010/09/10 15:28:06 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.MPatchFactory;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;


/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.mpatch.ChangeGroup} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangeGroupItemProvider
	extends IndepChangeItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";

	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeGroupItemProvider(AdapterFactory adapterFactory) {
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

		}
		return itemPropertyDescriptors;
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
			childrenFeatures.add(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES);
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
	 * This returns ChangeGroup.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ChangeGroup"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		final ChangeGroup group = (ChangeGroup) object;
		return MPatchUtil.toSummaryString(MPatchUtil.collectChanges(group.getSubChanges()));
			
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

		switch (notification.getFeatureID(ChangeGroup.class)) {
			case MPatchPackage.CHANGE_GROUP__SUB_CHANGES:
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
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createChangeGroup()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepAddElementChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepRemoveElementChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepMoveElementChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepAddAttributeChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepRemoveAttributeChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepUpdateAttributeChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepAddReferenceChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepRemoveReferenceChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createIndepUpdateReferenceChange()));

		newChildDescriptors.add
			(createChildParameter
				(MPatchPackage.Literals.CHANGE_GROUP__SUB_CHANGES,
				 MPatchFactory.eINSTANCE.createUnknownChange()));
	}

}
