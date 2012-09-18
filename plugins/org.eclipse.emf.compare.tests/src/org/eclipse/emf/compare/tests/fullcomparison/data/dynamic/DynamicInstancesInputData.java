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
package org.eclipse.emf.compare.tests.fullcomparison.data.dynamic;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class DynamicInstancesInputData extends AbstractInputData {

	private EPackage pak;

	private EClass testElement;

	private EClass container;

	private EObject containerInstance;

	public DynamicInstancesInputData() {
		pak = EcoreFactory.eINSTANCE.createEPackage();
		pak.setNsURI("testPackageNSURI");
		pak.setName("testPackage");
		pak.setNsPrefix("testPackagePrefix");
		testElement = EcoreFactory.eINSTANCE.createEClass();
		pak.getEClassifiers().add(testElement);
		testElement.setName("TestElement");
		container = EcoreFactory.eINSTANCE.createEClass();
		pak.getEClassifiers().add(container);
		container.setName("Container");
		EReference content = EcoreFactory.eINSTANCE.createEReference();
		content.setContainment(true);
		content.setEType(testElement);
		content.setName("content");
		content.setUpperBound(-1);
		container.getEStructuralFeatures().add(content);
		EAttribute name = EcoreFactory.eINSTANCE.createEAttribute();
		name.setEType(EcorePackage.eINSTANCE.getEString());
		name.setName("name");
		testElement.getEStructuralFeatures().add(name);

		containerInstance = EcoreUtil.create(container);
		EObject element1 = EcoreUtil.create(testElement);
		element1.eSet(testElement.getEStructuralFeature("name"), "some element");
		EObject element2 = EcoreUtil.create(testElement);
		element2.eSet(testElement.getEStructuralFeature("name"), "some other element");
		((Collection<EObject>)containerInstance.eGet(container.getEStructuralFeature("content")))
				.add(element1);
		((Collection<EObject>)containerInstance.eGet(container.getEStructuralFeature("content")))
				.add(element2);

	}

	public Resource getCompareLeft() throws IOException {
		Resource res = new XMIResourceImpl(URI.createURI("http://model.xmi", true));
		res.getContents().add(containerInstance);
		return res;
	}

	public Resource getCompareRight() throws IOException {
		EObject copy = EcoreUtil.copy(containerInstance);
		Resource res = new XMIResourceImpl(URI.createURI("http://modelv2.xmi", true));
		res.getContents().add(copy);
		EObject element3 = EcoreUtil.create(testElement);
		element3.eSet(testElement.getEStructuralFeature("name"), "added element");
		((Collection<EObject>)copy.eGet(container.getEStructuralFeature("content"))).add(element3);
		return res;
	}

}
