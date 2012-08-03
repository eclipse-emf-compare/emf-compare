/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.actions.group;

import static com.google.common.collect.Iterables.toArray;

import com.google.common.collect.Iterables;

import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffNode;
import org.eclipse.swt.graphics.Image;

/**
 * This will be used by the EMF Compare UI to wrap a DifferenceGroup as an Eclipse Diff node so that it is
 * understood by the eclipse compare UI.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DifferenceGroupNode extends AbstractEDiffNode {
	/** These nodes are set just under the Comparison, this allows us to remember of our parent. */
	private final Comparison parent;

	/** The actual DifferenceGroup we are wrapping. */
	private final DifferenceGroup group;

	/**
	 * Instantiates our node given its parent comparison, the adapter factory it was created through (as we
	 * are already in the midst of an adaptation chain), and the DifferenceGroup that we need to wrap.
	 * 
	 * @param comparison
	 *            The comparison which difference we're grouping.
	 * @param factory
	 *            The adapter factory we are to use to adapt our Diff objects to IDiffElements.
	 * @param group
	 *            The group wrapped within this instance.
	 */
	public DifferenceGroupNode(Comparison comparison, AdapterFactory factory, DifferenceGroup group) {
		super(factory);
		this.parent = comparison;
		this.group = group;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffContainer#getChildren()
	 */
	@Override
	public IDiffElement[] getChildren() {
		final Iterable<? extends Diff> diffs = group.getDifferences();
		final Iterable<IDiffElement> children = adapt(diffs, getAdapterFactory(), IDiffElement.class);
		return toArray(children, IDiffElement.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getImage()
	 */
	@Override
	public Image getImage() {
		final Image groupImage = group.getImage();
		if (groupImage != null) {
			return groupImage;
		}
		return EMFCompareIDEUIPlugin.getDefault().getImage("icons/full/toolb16/group.gif"); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getName()
	 */
	@Override
	public String getName() {
		return group.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffElement#getParent()
	 */
	@Override
	public IDiffContainer getParent() {
		return (IDiffContainer)getAdapterFactory().adapt(parent, IDiffContainer.class);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.AbstractEDiffContainer#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return !Iterables.isEmpty(group.getDifferences());
	}
}
