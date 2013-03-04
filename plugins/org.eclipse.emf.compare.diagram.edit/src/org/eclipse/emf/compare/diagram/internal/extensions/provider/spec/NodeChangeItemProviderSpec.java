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

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.NodeChange;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

public class NodeChangeItemProviderSpec extends ForwardingDiagramDiffItemProvider {

	public NodeChangeItemProviderSpec(ItemProviderAdapter delegate) {
		super(delegate);
	}

	@Override
	public Collection<?> getChildren(Object object) {
		Collection<?> superChildren = super.getChildren(object);
		List<? super Object> ret = newArrayList(superChildren);

		NodeChange nodeChange = (NodeChange)object;
		if (nodeChange.getKind() == DifferenceKind.ADD || nodeChange.getKind() == DifferenceKind.DELETE) {
			ret.addAll(nodeChange.getMatch().getComparison().getMatch(nodeChange.getView()).getDifferences());
		}

		return ImmutableList.copyOf(filter(ret, instanceOf(DiagramDiff.class)));
	}

}
