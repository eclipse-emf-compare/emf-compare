/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.emf.compare.diagram.diagramdiff.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffFactory;
import org.eclipse.emf.compare.diagram.diagramdiff.DiagramdiffPackage;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.provider.ModelElementChangeLeftTargetItemProvider;
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
 * This is the item provider adapter for a {@link org.eclipse.emf.compare.diagram.diagramdiff.DiagramModelElementChangeLeftTarget} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DiagramModelElementChangeLeftTargetItemProvider
	extends ModelElementChangeLeftTargetItemProvider
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
	public DiagramModelElementChangeLeftTargetItemProvider(AdapterFactory adapterFactory) {
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

			addSemanticDiffPropertyDescriptor(object);
			addHideElementsPropertyDescriptor(object);
			addIsCollapsedPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Semantic Diff feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSemanticDiffPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_DiagramModelElementChange_semanticDiff_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_DiagramModelElementChange_semanticDiff_feature", "_UI_DiagramModelElementChange_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DiagramdiffPackage.Literals.DIAGRAM_MODEL_ELEMENT_CHANGE__SEMANTIC_DIFF,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
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
				 getString("_UI_AbstractDiffExtension_hideElements_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_hideElements_feature", "_UI_AbstractDiffExtension_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
				 getString("_UI_AbstractDiffExtension_isCollapsed_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractDiffExtension_isCollapsed_feature", "_UI_AbstractDiffExtension_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns DiagramModelElementChangeLeftTarget.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return super.getImage(object);
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		return super.getText(object);
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

		switch (notification.getFeatureID(DiagramModelElementChangeLeftTarget.class)) {
			case DiagramdiffPackage.DIAGRAM_MODEL_ELEMENT_CHANGE_LEFT_TARGET__IS_COLLAPSED:
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
				 DiagramdiffFactory.eINSTANCE.createDiagramShowElement()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 DiagramdiffFactory.eINSTANCE.createDiagramHideElement()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 DiagramdiffFactory.eINSTANCE.createDiagramMoveNode()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 DiagramdiffFactory.eINSTANCE.createDiagramEdgeChange()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 DiagramdiffFactory.eINSTANCE.createDiagramLabelChange()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 DiagramdiffFactory.eINSTANCE.createDiagramModelElementChangeLeftTarget()));

		newChildDescriptors.add
			(createChildParameter
				(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				 DiagramdiffFactory.eINSTANCE.createDiagramModelElementChangeRightTarget()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return DiagramdiffEditPlugin.INSTANCE;
	}

}
