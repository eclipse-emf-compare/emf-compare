/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.merge;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.compare.tests.nodes.NodeSingleValueAttribute;
import org.eclipse.emf.compare.tests.nodes.impl.NodesFactoryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * A three-way merge scenario consisting of a root {@link Node} containing a {@link NodeSingleValueAttribute},
 * whereas the contents of the String attribute is subject to concurrent changes.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class ThreeWayAttributeMergeScenario {

	private static final String ROOT_NODE_NAME = "RootNode"; //$NON-NLS-1$

	private static final String NODE_NAME = "Node"; //$NON-NLS-1$

	private final NodesFactoryImpl nodesFactory = new NodesFactoryImpl();

	private final Resource.Factory resourceFactory = new XMIResourceFactoryImpl();

	private final Resource originResource;

	private final Resource leftResource;

	private final Resource rightResource;

	public ThreeWayAttributeMergeScenario(String originValue, String leftValue, String rightValue)
			throws IOException {
		originResource = createNodeWithAttributeResource("origin", originValue); //$NON-NLS-1$
		leftResource = createNodeWithAttributeResource("left", leftValue); //$NON-NLS-1$
		rightResource = createNodeWithAttributeResource("right", rightValue); //$NON-NLS-1$
	}

	public Resource createNodeWithAttributeResource(String fileName, String attributeValue) {
		URI fakeUri = URI.createFileURI(fileName + ".nodes"); //$NON-NLS-1$
		Resource resource = resourceFactory.createResource(fakeUri);

		Node rootNode = nodesFactory.createNode();
		rootNode.setName(ROOT_NODE_NAME);

		Node node = createSingleValueAttributeNode(attributeValue);
		node.setName(NODE_NAME);

		rootNode.getContainmentRef1().add(node);
		resource.getContents().add(rootNode);

		return resource;
	}

	private Node createSingleValueAttributeNode(String attributeValue) {
		NodeSingleValueAttribute node = nodesFactory.createNodeSingleValueAttribute();
		node.setSingleValuedAttribute(attributeValue);
		return node;
	}

	public Resource getOriginResource() {
		return originResource;
	}

	public Resource getLeftResource() {
		return leftResource;
	}

	public Resource getRightResource() {
		return rightResource;
	}

	public String getLeftAttributeValue() {
		return getAttributeValue(leftResource);
	}

	public String getRightAttributeValue() {
		return getAttributeValue(rightResource);
	}

	private String getAttributeValue(final Resource resource) {
		Node rootNode = (Node)resource.getContents().get(0);
		final Node childNode = rootNode.getContainmentRef1().get(0);
		NodeSingleValueAttribute attNode = (NodeSingleValueAttribute)childNode;
		return attNode.getSingleValuedAttribute();
	}

}
