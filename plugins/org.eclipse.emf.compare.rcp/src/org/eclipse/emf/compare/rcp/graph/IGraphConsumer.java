/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.graph;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.graph.IGraph;
import org.eclipse.emf.compare.graph.IGraphView;

/**
 * Graph consumer, that maintains the state of a graph created by a third party.
 * 
 * @author <a href="mailto:mathieu.cartaud@obeo.fr">Mathieu Cartaud</a>
 * @since 2.4
 */
public interface IGraphConsumer {

	/**
	 * Set the graph to be used by this consumer.
	 * 
	 * @param graph
	 *            The graph to use
	 */
	void setGraph(IGraph<URI> graph);

	/**
	 * Return the ID used to identify this specific consumer.
	 * 
	 * @return the consumer id
	 */
	String getId();

	/**
	 * Provide a read-only view of the graph used by this consumer.
	 * 
	 * @return A read-only view of the graph used by this consumer.
	 */
	IGraphView<URI> getGraphView();
}
