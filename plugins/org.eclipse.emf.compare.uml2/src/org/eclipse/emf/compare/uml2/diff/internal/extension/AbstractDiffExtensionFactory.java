/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff.internal.extension;

import java.util.Iterator;

import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.uml2.diff.DiffOfSwitch;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.ecore.EObject;

public abstract class AbstractDiffExtensionFactory implements IDiffExtensionFactory {

	private UML2DiffEngine fEngine;
	
	public AbstractDiffExtensionFactory(UML2DiffEngine engine) {
		fEngine = engine;
	}
	
	public UML2DiffEngine getEngine() {
		return fEngine;
	}
	
	public DiffElement getParentDiff(DiffElement input) {
		return (DiffElement) input.eContainer();
	}
	
	DiffElement findOrCreateDiffElementFor(DiffGroup from, EObject left, EObject right) {
		DiffElement ret = null;
		
		if(left == null && right == null) {
			if(!from.getSubDiffElements().isEmpty()) {
				ret = from.getSubDiffElements().get(0);
			} else {
				ret = DiffFactory.eINSTANCE.createDiffGroup();
				from.getSubDiffElements().add(ret);
			}
		} else {
			DiffElement element = findDiffElementFor(from, left, right);
			if(element != null) {
				ret = element;
			} else {
				DiffElement diffParent = findOrCreateDiffElementFor(from, eContainerIfNotNull(left), eContainerIfNotNull(right));
				DiffGroup retGroup = DiffFactory.eINSTANCE.createDiffGroup();
				retGroup.setRightParent(right);
				diffParent.getSubDiffElements().add(retGroup);
				
				ret = retGroup;
			}
		}
		
		return ret;
	}

	private EObject eContainerIfNotNull(EObject eObject) {
		return eObject == null ? null : eObject.eContainer();
	}
	
	DiffElement findDiffElementFor(DiffGroup from, EObject left, EObject right) {
		if(left == null && right == null) {
			return null;
		}
		final Iterator<EObject> it = from.eAllContents();
		while(it.hasNext()) {
			final DiffElement element = (DiffElement)it.next();
			getEngine();
			DiffOfSwitch diffOfSwitch = new DiffOfSwitch(left, right);
			Boolean doSwitch = diffOfSwitch.doSwitch(element);
			if(doSwitch != null && doSwitch.booleanValue()) {
				return element;
			}
		}
		return null;
	}
}
