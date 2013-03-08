/**
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * Item provider for node changes.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class NodeChangeItemProviderSpec extends ForwardingDiagramDiffItemProvider {

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            The origin item provider adapter.
	 */
	public NodeChangeItemProviderSpec(ItemProviderAdapter delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.extensions.provider.spec.ForwardingDiagramDiffItemProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Collection<?> superChildren = super.getChildren(object);
		List<? super Object> ret = newArrayList(superChildren);

		NodeChange nodeChange = (NodeChange)object;
		if (nodeChange.getKind() == DifferenceKind.ADD || nodeChange.getKind() == DifferenceKind.DELETE) {
			Comparison comparison = nodeChange.getMatch().getComparison();
			Match matchValue = comparison.getMatch(nodeChange.getView());
			ret.addAll(matchValue.getDifferences());
			ret.addAll(matchValue.getSubmatches());
		}

		return ImmutableList.copyOf(ret/* filter(ret, instanceOf(DiagramDiff.class)) */);
	}

}
