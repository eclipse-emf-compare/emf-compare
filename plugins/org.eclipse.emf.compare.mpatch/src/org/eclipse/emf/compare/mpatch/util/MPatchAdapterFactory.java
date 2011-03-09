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
 * $Id: MPatchAdapterFactory.java,v 1.1 2010/09/10 15:23:08 cbrun Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.util;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepElementChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.compare.mpatch.MPatchPackage
 * @generated
 */
public class MPatchAdapterFactory extends AdapterFactoryImpl {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2010, 2011 Technical University of Denmark.\r\nAll rights reserved. This program and the accompanying materials \r\nare made available under the terms of the Eclipse Public License v1.0 \r\nwhich accompanies this distribution, and is available at \r\nhttp://www.eclipse.org/legal/epl-v10.html \r\n\r\nContributors:\r\n   Patrick Koenemann, DTU Informatics - initial API and implementation";
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MPatchPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MPatchAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MPatchPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MPatchSwitch<Adapter> modelSwitch =
		new MPatchSwitch<Adapter>() {
			@Override
			public Adapter caseMPatchModel(MPatchModel object) {
				return createMPatchModelAdapter();
			}
			@Override
			public Adapter caseIndepChange(IndepChange object) {
				return createIndepChangeAdapter();
			}
			@Override
			public Adapter caseChangeGroup(ChangeGroup object) {
				return createChangeGroupAdapter();
			}
			@Override
			public Adapter caseIndepElementChange(IndepElementChange object) {
				return createIndepElementChangeAdapter();
			}
			@Override
			public Adapter caseIndepAddRemElementChange(IndepAddRemElementChange object) {
				return createIndepAddRemElementChangeAdapter();
			}
			@Override
			public Adapter caseIndepAddElementChange(IndepAddElementChange object) {
				return createIndepAddElementChangeAdapter();
			}
			@Override
			public Adapter caseIndepRemoveElementChange(IndepRemoveElementChange object) {
				return createIndepRemoveElementChangeAdapter();
			}
			@Override
			public Adapter caseIndepAttributeChange(IndepAttributeChange object) {
				return createIndepAttributeChangeAdapter();
			}
			@Override
			public Adapter caseIndepAddRemAttributeChange(IndepAddRemAttributeChange object) {
				return createIndepAddRemAttributeChangeAdapter();
			}
			@Override
			public Adapter caseIndepMoveElementChange(IndepMoveElementChange object) {
				return createIndepMoveElementChangeAdapter();
			}
			@Override
			public Adapter caseIndepAddAttributeChange(IndepAddAttributeChange object) {
				return createIndepAddAttributeChangeAdapter();
			}
			@Override
			public Adapter caseIndepRemoveAttributeChange(IndepRemoveAttributeChange object) {
				return createIndepRemoveAttributeChangeAdapter();
			}
			@Override
			public Adapter caseIndepUpdateAttributeChange(IndepUpdateAttributeChange object) {
				return createIndepUpdateAttributeChangeAdapter();
			}
			@Override
			public Adapter caseIndepReferenceChange(IndepReferenceChange object) {
				return createIndepReferenceChangeAdapter();
			}
			@Override
			public Adapter caseIndepAddRemReferenceChange(IndepAddRemReferenceChange object) {
				return createIndepAddRemReferenceChangeAdapter();
			}
			@Override
			public Adapter caseIndepAddReferenceChange(IndepAddReferenceChange object) {
				return createIndepAddReferenceChangeAdapter();
			}
			@Override
			public Adapter caseIndepRemoveReferenceChange(IndepRemoveReferenceChange object) {
				return createIndepRemoveReferenceChangeAdapter();
			}
			@Override
			public Adapter caseIndepUpdateReferenceChange(IndepUpdateReferenceChange object) {
				return createIndepUpdateReferenceChangeAdapter();
			}
			@Override
			public Adapter caseIElementReference(IElementReference object) {
				return createIElementReferenceAdapter();
			}
			@Override
			public Adapter caseIModelDescriptor(IModelDescriptor object) {
				return createIModelDescriptorAdapter();
			}
			@Override
			public Adapter caseElementReferenceToEObjectMap(Map.Entry<IElementReference, EList<EObject>> object) {
				return createElementReferenceToEObjectMapAdapter();
			}
			@Override
			public Adapter caseEObjectToIModelDescriptorMap(Map.Entry<EObject, IModelDescriptor> object) {
				return createEObjectToIModelDescriptorMapAdapter();
			}
			@Override
			public Adapter caseUnknownChange(UnknownChange object) {
				return createUnknownChangeAdapter();
			}
			@Override
			public Adapter caseModelDescriptorReference(ModelDescriptorReference object) {
				return createModelDescriptorReferenceAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.MPatchModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.MPatchModel
	 * @generated
	 */
	public Adapter createMPatchModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepChange <em>Indep Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepChange
	 * @generated
	 */
	public Adapter createIndepChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.ChangeGroup <em>Change Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.ChangeGroup
	 * @generated
	 */
	public Adapter createChangeGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepElementChange <em>Indep Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepElementChange
	 * @generated
	 */
	public Adapter createIndepElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAddRemElementChange <em>Indep Add Rem Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemElementChange
	 * @generated
	 */
	public Adapter createIndepAddRemElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAddElementChange <em>Indep Add Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddElementChange
	 * @generated
	 */
	public Adapter createIndepAddElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepRemoveElementChange <em>Indep Remove Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepRemoveElementChange
	 * @generated
	 */
	public Adapter createIndepRemoveElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAttributeChange <em>Indep Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAttributeChange
	 * @generated
	 */
	public Adapter createIndepAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange <em>Indep Add Rem Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange
	 * @generated
	 */
	public Adapter createIndepAddRemAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepMoveElementChange <em>Indep Move Element Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepMoveElementChange
	 * @generated
	 */
	public Adapter createIndepMoveElementChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAddAttributeChange <em>Indep Add Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddAttributeChange
	 * @generated
	 */
	public Adapter createIndepAddAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange <em>Indep Remove Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange
	 * @generated
	 */
	public Adapter createIndepRemoveAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange <em>Indep Update Attribute Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange
	 * @generated
	 */
	public Adapter createIndepUpdateAttributeChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepReferenceChange <em>Indep Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepReferenceChange
	 * @generated
	 */
	public Adapter createIndepReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange <em>Indep Add Rem Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange
	 * @generated
	 */
	public Adapter createIndepAddRemReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepAddReferenceChange <em>Indep Add Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepAddReferenceChange
	 * @generated
	 */
	public Adapter createIndepAddReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange <em>Indep Remove Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange
	 * @generated
	 */
	public Adapter createIndepRemoveReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange <em>Indep Update Reference Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange
	 * @generated
	 */
	public Adapter createIndepUpdateReferenceChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IElementReference <em>IElement Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IElementReference
	 * @generated
	 */
	public Adapter createIElementReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.IModelDescriptor <em>IModel Descriptor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.IModelDescriptor
	 * @generated
	 */
	public Adapter createIModelDescriptorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Element Reference To EObject Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createElementReferenceToEObjectMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EObject To IModel Descriptor Map</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEObjectToIModelDescriptorMapAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.UnknownChange <em>Unknown Change</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.UnknownChange
	 * @generated
	 */
	public Adapter createUnknownChangeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.compare.mpatch.ModelDescriptorReference <em>Model Descriptor Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.compare.mpatch.ModelDescriptorReference
	 * @generated
	 */
	public Adapter createModelDescriptorReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MPatchAdapterFactory
