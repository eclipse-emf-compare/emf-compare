/*******************************************************************************
 * Copyright (c) EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
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
}
