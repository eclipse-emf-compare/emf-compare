package org.eclipse.emf.compare.sysml.sysmldiff.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffPackage;
import org.eclipse.emf.compare.diff.provider.AttributeChangeRightTargetItemProvider;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffFactory;
import org.eclipse.emf.compare.sysml.sysmldiff.SysMLdiffPackage;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffPackage;
import org.eclipse.emf.compare.util.AdapterUtils;
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
 * This is the item provider adapter for a
 * {@link org.eclipse.emf.compare.sysml.sysmldiff.SysMLStereotypePropertyChangeRightTarget} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class SysMLStereotypePropertyChangeRightTargetItemProvider extends AttributeChangeRightTargetItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public SysMLStereotypePropertyChangeRightTargetItemProvider(AdapterFactory adapterFactory) {
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

			addHideElementsPropertyDescriptor(object);
			addIsCollapsedPropertyDescriptor(object);
			addStereotypePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Hide Elements feature. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addHideElementsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_AbstractDiffExtension_hideElements_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", //$NON-NLS-1$
						"_UI_AbstractDiffExtension_hideElements_feature", "_UI_AbstractDiffExtension_type"), //$NON-NLS-1$ //$NON-NLS-2$
				DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__HIDE_ELEMENTS, true, false, true, null, null,
				null));
	}

	/**
	 * This adds a property descriptor for the Is Collapsed feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addIsCollapsedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_AbstractDiffExtension_isCollapsed_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", //$NON-NLS-1$
						"_UI_AbstractDiffExtension_isCollapsed_feature", "_UI_AbstractDiffExtension_type"), //$NON-NLS-1$ //$NON-NLS-2$
				DiffPackage.Literals.ABSTRACT_DIFF_EXTENSION__IS_COLLAPSED, true, false, false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Stereotype feature. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected void addStereotypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				getResourceLocator(),
				getString("_UI_UMLStereotypePropertyChange_stereotype_feature"), //$NON-NLS-1$
				getString("_UI_PropertyDescriptor_description", //$NON-NLS-1$
						"_UI_UMLStereotypePropertyChange_stereotype_feature", //$NON-NLS-1$
						"_UI_UMLStereotypePropertyChange_type"), //$NON-NLS-1$
				UML2DiffPackage.Literals.UML_STEREOTYPE_PROPERTY_CHANGE__STEREOTYPE, true, false, true, null,
				null, null));
	}

	/**
	 * This returns SysMLStereotypePropertyChangeRightTarget.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated not
	 */
	@Override
	public Object getImage(Object object) {
		return super.getImage(object);
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated not
	 */
	@Override
	public String getText(Object object) {
		final AttributeChangeRightTarget operation = (AttributeChangeRightTarget)object;

		final String attributeLabel = AdapterUtils.getItemProviderText(operation.getAttribute());
		final String elementLabel = AdapterUtils.getItemProviderText(operation.getLeftElement());

		if (operation.isRemote())
			return getString(
					"_UI_RemoteAddUMLStereotypeAttribute_type", new Object[] {operation.getRightTarget(), //$NON-NLS-1$
							attributeLabel, elementLabel,});
		return getString("_UI_RemoveUMLStereotypeAttribute_type", new Object[] {operation.getRightTarget(), //$NON-NLS-1$
				attributeLabel, elementLabel,});
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

		switch (notification.getFeatureID(SysMLStereotypePropertyChangeRightTarget.class)) {
			case SysMLdiffPackage.SYS_ML_STEREOTYPE_PROPERTY_CHANGE_RIGHT_TARGET__IS_COLLAPSED:
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

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypePropertyChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypePropertyChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypeReferenceChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypeReferenceChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypeReferenceOrderChange()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypeUpdateAttribute()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				SysMLdiffFactory.eINSTANCE.createSysMLStereotypeUpdateReference()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLAssociationChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLAssociationChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLAssociationBranchChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLAssociationBranchChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLDependencyBranchChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLDependencyBranchChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLGeneralizationSetChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLGeneralizationSetChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLDependencyChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLDependencyChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLExtendChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLExtendChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLExecutionSpecificationChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLExecutionSpecificationChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLDestructionEventChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLDestructionEventChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLIntervalConstraintChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLIntervalConstraintChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLMessageChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLMessageChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeAttributeChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeAttributeChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeUpdateAttribute()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeApplicationAddition()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeApplicationRemoval()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeReferenceChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeReferenceChangeRightTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeUpdateReference()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLStereotypeReferenceOrderChange()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLProfileApplicationAddition()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLProfileApplicationRemoval()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLElementChangeLeftTarget()));

		newChildDescriptors.add(createChildParameter(DiffPackage.Literals.DIFF_ELEMENT__SUB_DIFF_ELEMENTS,
				UML2DiffFactory.eINSTANCE.createUMLElementChangeRightTarget()));
	}

	/**
	 * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SysMLdiffEditPlugin.INSTANCE;
	}

}
