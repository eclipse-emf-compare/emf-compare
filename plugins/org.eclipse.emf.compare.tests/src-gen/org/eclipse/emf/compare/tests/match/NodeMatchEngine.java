/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests.match;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.tests.nodes.Node;
import org.eclipse.emf.ecore.EObject;


/**
 * Matches using UUIDs for nodes
 * @author smccants
 *
 */
public class NodeMatchEngine extends GenericMatchEngine
{
	@Override
	protected boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException
	{
		if(obj1 instanceof Node && obj2 instanceof Node)
		{
			Node node1 = (Node) obj1;
			Node node2 = (Node) obj2;
			return node1.getUuid().equals(node2.getUuid());
		}
		else
			return super.isSimilar(obj1, obj2);
	}
}
