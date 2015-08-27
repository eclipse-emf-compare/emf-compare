/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.match.data;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

@SuppressWarnings("nls")
public class MatchInputData extends AbstractInputData {

	public Resource getProxyMatchingA1Left() throws IOException {
		return loadFromClassLoader("proxy/a1/left.nodes", new ResourceSetImpl());
	}

	public Resource getProxyMatchingA1Right() throws IOException {
		return loadFromClassLoader("proxy/a1/right.nodes", new ResourceSetImpl());
	}

	public Resource getProxyMatchingA2Left() throws IOException {
		return loadFromClassLoader("proxy/a2/left.nodes", new ResourceSetImpl());
	}

	public Resource getProxyMatchingA2Right() throws IOException {
		return loadFromClassLoader("proxy/a2/right.nodes", new ResourceSetImpl());
	}

	public List<Resource> getRootIDTwoWayA1Left() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a1/left.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a1/left2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA1Right() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a1/right.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a1/right2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA2Left() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a2/left.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a2/left2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA2Right() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a2/right.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a2/right2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA3Left() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a3/left.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a3/left2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA3Right() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a3/right.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a3/right2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA4Left() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a4/left.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a4/left2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA4Right() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a4/right.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a4/right2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA5Left() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a5/left.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a5/left2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA5Right() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a5/right.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a5/right2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA6Left() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a6/left.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a6/left2.nodes"));
		return result;
	}

	public List<Resource> getRootIDTwoWayA6Right() throws IOException {
		List<Resource> result = Lists.newLinkedList();
		result.add(loadFromClassLoader("rootid/twoway/a6/right.nodes"));
		result.add(loadFromClassLoader("rootid/twoway/a6/right2.nodes"));
		return result;
	}

	public Resource getSetIDAttributeLeft() throws IOException {
		return loadFromClassLoader("setidattribute/left.nodes");
	}

	public Resource getSetIDAttributeRight() throws IOException {
		return loadFromClassLoader("setidattribute/right.nodes");
	}

	public Resource getXMIIDPriorityA1Left() throws IOException {
		return loadFromClassLoader("xmiidpriority/a1/left.nodes");
	}

	public Resource getXMIIDPriorityA1Right() throws IOException {
		return loadFromClassLoader("xmiidpriority/a1/right.nodes");
	}

	public Resource getXMIIDPriorityA2Left() throws IOException {
		return loadFromClassLoader("xmiidpriority/a2/left.nodes");
	}

	public Resource getXMIIDPriorityA2Right() throws IOException {
		return loadFromClassLoader("xmiidpriority/a2/right.nodes");
	}
}
