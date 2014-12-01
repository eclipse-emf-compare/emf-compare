/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *     Alexandra Buzila - test data for bug 446252
 *     Stefan Dirix - test data for bug 453749
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("nls")
public class TwoWayMergeInputData extends AbstractInputData {
	public Resource getMoveToDifferentContainmentFeatureRTLLeft() throws IOException {
		return loadFromClassLoader("twoway/movedifferentcontainmentfeature/rtl/left.nodes");
	}

	public Resource getMoveToDifferentContainmentFeatureRTLRight() throws IOException {
		return loadFromClassLoader("twoway/movedifferentcontainmentfeature/rtl/right.nodes");
	}

	public Resource getMoveToDifferentContainmentFeatureL2RLeft() throws IOException {
		return loadFromClassLoader("twoway/movedifferentcontainmentfeature/ltr/left.nodes");
	}

	public Resource getMoveToDifferentContainmentFeatureL2RRight() throws IOException {
		return loadFromClassLoader("twoway/movedifferentcontainmentfeature/ltr/right.nodes");
	}

	public Resource getOppositeReferenceChangeWithoutMatchingOrignalContainerL2RLeft() throws IOException {
		return loadFromClassLoader("twoway/oppositereferencechangewithoutmatchingorignalcontainer/ltr/left.nodes");
	}

	public Resource getOppositeReferenceChangeWithoutMatchingOrignalContainerL2RRight() throws IOException {
		return loadFromClassLoader("twoway/oppositereferencechangewithoutmatchingorignalcontainer/ltr/right.nodes");
	}

	public Resource getOppositeReferenceChangeWithAddAndDeleteOnMultivaluedSideLeft() throws IOException {
		return loadFromClassLoader("twoway/oppositereferencechangewithaddanddeleteonmultivaluedside/rtl/left.nodes");
	}

	public Resource getOppositeReferenceChangeWithAddAndDeleteOnMultivaluedSideRight() throws IOException {
		return loadFromClassLoader("twoway/oppositereferencechangewithaddanddeleteonmultivaluedside/rtl/right.nodes");
	}

	public Resource getMoveFromSingleValueReferenceToMultiValueReferenceR2LLeft() throws IOException {
		return loadFromClassLoader("twoway/movefromsinglevaluereferencetomultivaluereference/rtl/left.nodes");
	}

	public Resource getMoveFromSingleValueReferenceToMultiValueReferenceR2LRight() throws IOException {
		return loadFromClassLoader("twoway/movefromsinglevaluereferencetomultivaluereference/rtl/right.nodes");
	}

	public Resource getMoveToNewContainerInADifferentOrderR2LLeft() throws IOException {
		return loadFromClassLoader("twoway/movetonewcontainerinadifferentorder/rtl/left.nodes");
	}

	public Resource getMoveToNewContainerInADifferentOrderR2LRight() throws IOException {
		return loadFromClassLoader("twoway/movetonewcontainerinadifferentorder/rtl/right.nodes");
	}

	public Resource getManyToManyReferenceChangesR2LLeft() throws IOException {
		return loadFromClassLoader("twoway/manytomanyreferencechanges/rtl/left.nodes");
	}

	public Resource getManyToManyReferenceChangesR2LRight() throws IOException {
		return loadFromClassLoader("twoway/manytomanyreferencechanges/rtl/right.nodes");
	}

	public Resource getMoveToFeatureMapL2RLeft() throws IOException {
		return loadFromClassLoader("twoway/movetofeaturemap/ltr/left.nodes");
	}

	public Resource getMoveToFeatureMapL2RRight() throws IOException {
		return loadFromClassLoader("twoway/movetofeaturemap/ltr/right.nodes");
	}

	public Resource getDeleteFeatureMapNonContainmentsL2RLeft() throws IOException {
		return loadFromClassLoader("twoway/deletefeaturemapnoncontainments/ltr/left.nodes");
	}

	public Resource getDeleteFeatureMapNonContainmentsL2RRight() throws IOException {
		return loadFromClassLoader("twoway/deletefeaturemapnoncontainments/ltr/right.nodes");
	}
}
