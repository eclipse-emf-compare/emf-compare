/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
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
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement} object. <!-- begin-user-doc --> <!--
 * end-user-doc -->
 * 
 * @generated
 */
@SuppressWarnings("nls")
public class ConflictingDiffElementItemProvider extends DiffElementItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ConflictingDiffElementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addLeftParentPropertyDescriptor(object);
			addRightParentPropertyDescriptor(object);
			addLeftDiffPropertyDescriptor(object);
			addRightDiffPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Left Parent feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	protected void addLeftParentPropertyDescriptor(@SuppressWarnings("unused")
	Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ConflictingDiffElement_leftParent_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ConflictingDiffElement_leftParent_feature", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"_UI_ConflictingDiffElement_type"), DiffPackage.Literals.CONFLICTING_DIFF_ELEMENT__LEFT_PARENT, true, false, true, null, null, null)); //$NON-NLS-1$
	}

	/**
	 * This adds a property descriptor for the Right Parent feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	protected void addRightParentPropertyDescriptor(@SuppressWarnings("unused")
	Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ConflictingDiffElement_rightParent_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ConflictingDiffElement_rightParent_feature", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"_UI_ConflictingDiffElement_type"), DiffPackage.Literals.CONFLICTING_DIFF_ELEMENT__RIGHT_PARENT, true, false, true, null, null, null)); //$NON-NLS-1$
	}

	/**
	 * This adds a property descriptor for the Left Diff feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	protected void addLeftDiffPropertyDescriptor(@SuppressWarnings("unused")
	Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ConflictingDiffElement_leftDiff_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ConflictingDiffElement_leftDiff_feature", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"_UI_ConflictingDiffElement_type"), DiffPackage.Literals.CONFLICTING_DIFF_ELEMENT__LEFT_DIFF, true, false, true, null, null, null)); //$NON-NLS-1$
	}

	/**
	 * This adds a property descriptor for the Right Diff feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	protected void addRightDiffPropertyDescriptor(@SuppressWarnings("unused")
	Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ConflictingDiffElement_rightDiff_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ConflictingDiffElement_rightDiff_feature", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"_UI_ConflictingDiffElement_type"), DiffPackage.Literals.CONFLICTING_DIFF_ELEMENT__RIGHT_DIFF, true, false, true, null, null, null)); //$NON-NLS-1$
	}

	/**
	 * This returns ConflictingDiffElement.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ConflictingDiffElement")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		final ConflictingDiffElement conflictingDiffElement = (ConflictingDiffElement)object;
		try {
			return getString("_UI_ConflictingDiffElement_type", new Object[] {NameSimilarity.findName(conflictingDiffElement.getLeftParent()), //$NON-NLS-1$
					NameSimilarity.findName(conflictingDiffElement.getRightParent())});
		} catch (FactoryException e) {
			return getString("_UI_ConflictingDiffElement_type"); //$NON-NLS-1$
		}
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of the children that can be created under
	 * this object. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DiffEditPlugin.INSTANCE;
	}

}
