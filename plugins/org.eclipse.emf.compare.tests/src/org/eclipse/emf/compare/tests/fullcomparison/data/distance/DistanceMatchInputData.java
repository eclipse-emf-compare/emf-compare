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
package org.eclipse.emf.compare.tests.fullcomparison.data.distance;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

public class DistanceMatchInputData extends AbstractInputData {

	public Resource getCompareLeft() throws IOException {
		return loadFromClassLoader("compareLeft.ecore");
	}

	public Resource getCompareRight() throws IOException {
		return loadFromClassLoader("compareRight.ecore");
	}

	public Resource getNominalUMLLeft() throws IOException {
		return loadFromClassLoader("agencyLeft.uml");
	}

	public Resource getNominalUMLRight() throws IOException {
		return loadFromClassLoader("agencyRight.uml");
	}

	public Resource getNominalUMLOrigin() throws IOException {
		return loadFromClassLoader("agencyOrigin.uml");
	}

	public Resource getVerySmallLeft() throws IOException {
		return loadFromClassLoader("verySmallLeft.ecore");
	}

	public Resource getVerySmallRight() throws IOException {
		return loadFromClassLoader("verySmallRight.ecore");
	}

	public Resource getPackageAddDeleteLeft() throws IOException {
		return loadFromClassLoader("package-add-delete.ecore");
	}

	public Resource getPackageAddDeleteRight() throws IOException {
		return loadFromClassLoader("package-add-deletev2.ecore");
	}

	public Resource getAbstractAndSuperLeft() throws IOException {
		return loadFromClassLoader("abstract-and-super.ecore");
	}

	public Resource getAbstractAndSuperRight() throws IOException {
		return loadFromClassLoader("abstract-and-superv2.ecore");
	}

	public Resource getFeatureUpdateDeleteLeft() throws IOException {
		return loadFromClassLoader("feature-update-delete.ecore");
	}

	public Resource getFeatureUpdateDeleteRight() throws IOException {
		return loadFromClassLoader("feature-update-deletev2.ecore");
	}

	public Resource getPackageAddRemoveNoRenameLeft() throws IOException {
		return loadFromClassLoader("add-remove-norename.ecore");
	}

	public Resource getPackageAddRemoveNoRenameRight() throws IOException {
		return loadFromClassLoader("add-remove-norenamev2.ecore");
	}

}
