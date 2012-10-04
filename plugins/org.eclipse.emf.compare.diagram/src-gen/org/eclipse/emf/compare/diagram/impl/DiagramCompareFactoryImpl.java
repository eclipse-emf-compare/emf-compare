/**
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.impl;

import org.eclipse.emf.compare.diagram.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DiagramCompareFactoryImpl extends EFactoryImpl implements DiagramCompareFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DiagramCompareFactory init() {
		try {
			DiagramCompareFactory theDiagramCompareFactory = (DiagramCompareFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/emf/compare/diagram/2.0"); 
			if (theDiagramCompareFactory != null) {
				return theDiagramCompareFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DiagramCompareFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramCompareFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DiagramComparePackage.SHOW: return createShow();
			case DiagramComparePackage.HIDE: return createHide();
			case DiagramComparePackage.NODE_CHANGE: return createNodeChange();
			case DiagramComparePackage.EDGE_CHANGE: return createEdgeChange();
			case DiagramComparePackage.LABEL_CHANGE: return createLabelChange();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Show createShow() {
		ShowImpl show = new ShowImpl();
		return show;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Hide createHide() {
		HideImpl hide = new HideImpl();
		return hide;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeChange createNodeChange() {
		NodeChangeImpl nodeChange = new NodeChangeImpl();
		return nodeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdgeChange createEdgeChange() {
		EdgeChangeImpl edgeChange = new EdgeChangeImpl();
		return edgeChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LabelChange createLabelChange() {
		LabelChangeImpl labelChange = new LabelChangeImpl();
		return labelChange;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiagramComparePackage getDiagramComparePackage() {
		return (DiagramComparePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DiagramComparePackage getPackage() {
		return DiagramComparePackage.eINSTANCE;
	}

} //DiagramCompareFactoryImpl
