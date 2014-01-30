/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.Diff} object. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 * 
 * @generated
 */
@SuppressWarnings("all")
// generated code : suppressing warnings
public class DiffItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider, IItemFontProvider {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2012 Obeo.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n\r\nContributors:\r\n    Obeo - initial API and implementation"; //$NON-NLS-1$

	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public DiffItemProvider(AdapterFactory adapterFactory) {
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

			addRequiresPropertyDescriptor(object);
			addRequiredByPropertyDescriptor(object);
			addImpliesPropertyDescriptor(object);
			addImpliedByPropertyDescriptor(object);
			addRefinesPropertyDescriptor(object);
			addRefinedByPropertyDescriptor(object);
			addPrimeRefiningPropertyDescriptor(object);
			addKindPropertyDescriptor(object);
			addSourcePropertyDescriptor(object);
			addStatePropertyDescriptor(object);
			addEquivalencePropertyDescriptor(object);
			addConflictPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Requires feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRequiresPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_requires_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_requires_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__REQUIRES, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Required By feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addRequiredByPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_requiredBy_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_requiredBy_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__REQUIRED_BY, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Implies feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @since 4.0
	 */
	protected void addImpliesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(), getString("_UI_Diff_implies_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Diff_implies_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ComparePackage.Literals.DIFF__IMPLIES, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Implied By feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 * @since 4.0
	 */
	protected void addImpliedByPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_impliedBy_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_impliedBy_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__IMPLIED_BY, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Refines feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRefinesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(), getString("_UI_Diff_refines_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Diff_refines_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ComparePackage.Literals.DIFF__REFINES, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Refined By feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addRefinedByPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_refinedBy_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_refinedBy_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__REFINED_BY, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Prime Refining feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 * @since 4.0
	 */
	protected void addPrimeRefiningPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_primeRefining_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_primeRefining_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__PRIME_REFINING, false, false, false, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Kind feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addKindPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Diff_kind_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Diff_kind_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ComparePackage.Literals.DIFF__KIND, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Source feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addSourcePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Diff_source_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Diff_source_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ComparePackage.Literals.DIFF__SOURCE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the State feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addStatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_Diff_state_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", "_UI_Diff_state_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ComparePackage.Literals.DIFF__STATE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Equivalence feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addEquivalencePropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_equivalence_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_equivalence_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__EQUIVALENCE, true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Conflict feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addConflictPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
						.getRootAdapterFactory(),
						getResourceLocator(),
						getString("_UI_Diff_conflict_feature"), //$NON-NLS-1$
						getString(
								"_UI_PropertyDescriptor_description", "_UI_Diff_conflict_feature", "_UI_Diff_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ComparePackage.Literals.DIFF__CONFLICT, true, false, true, null, null, null));
	}

	/**
	 * This returns Diff.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Diff")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		DifferenceKind labelValue = ((Diff)object).getKind();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ? getString("_UI_Diff_type") : //$NON-NLS-1$
				getString("_UI_Diff_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

		switch (notification.getFeatureID(Diff.class)) {
			case ComparePackage.DIFF__KIND:
			case ComparePackage.DIFF__SOURCE:
			case ComparePackage.DIFF__STATE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false,
						true));
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
		return EMFCompareEditPlugin.INSTANCE;
	}

}
