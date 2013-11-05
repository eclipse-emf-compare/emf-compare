/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.AbstractDifferenceGroupProvider;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.TreePackage;

/**
 * Instances of this class will be used by EMF Compare in order to provide difference grouping facilities to
 * the structural differences view.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 3.0
 */
public interface IDifferenceGroupProvider extends Adapter {

	public static IDifferenceGroupProvider EMPTY = new AbstractDifferenceGroupProvider() {

		public void setLabel(String label) {
			// do nothing
		}

		public void setDefaultSelected(boolean defaultSelected) {
			// do nothing;
		}

		public boolean isEnabled(IComparisonScope scope, Comparison comparison) {
			return true;
		}

		public String getLabel() {
			return IDifferenceGroupProvider.class.getName() + ".EMPTY"; //$NON-NLS-1$
		}

		public Collection<? extends IDifferenceGroup> getGroups(Comparison comparison) {
			return ImmutableList.of();
		}

		public boolean defaultSelected() {
			return true;
		}

		public void dispose() {
		}
	};

	/**
	 * This will be called internally by the grouping actions in order to determine how the differences should
	 * be grouped in the structural view.
	 * 
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view. By default, its containment
	 *            tree will be displayed.
	 * @return The collection of difference groups that are to be displayed in the structural viewer. An empty
	 *         group will not be displayed at all. If {@code null}, we'll fall back to the default behavior.
	 */
	Collection<? extends IDifferenceGroup> getGroups(Comparison comparison);

	/**
	 * A human-readable label for this group. This will be displayed in the EMF Compare UI.
	 * 
	 * @return The label for this group.
	 */
	String getLabel();

	/**
	 * Set the label for this group. This will be displayed in the EMF Compare UI.
	 * 
	 * @param label
	 *            A human-readable label for this group.
	 */
	void setLabel(String label);

	/**
	 * Returns the initial activation state that the group should have.
	 * 
	 * @return The initial activation state that the group should have.
	 */
	boolean defaultSelected();

	/**
	 * Set the initial activation state that the group should have.
	 * 
	 * @param defaultSelected
	 *            The initial activation state that the group should have (true if the group should be active
	 *            by default).
	 */
	void setDefaultSelected(boolean defaultSelected);

	/**
	 * Returns the activation condition based on the scope and comparison objects.
	 * 
	 * @param scope
	 *            The scope on which the group provider will be applied.
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view.
	 * @return The activation condition based on the scope and comparison objects.
	 */
	boolean isEnabled(IComparisonScope scope, Comparison comparison);

	/**
	 * Dispose this difference group provider.
	 */
	void dispose();

	/**
	 * A descriptor that can create adifference group provider. They are used as the values in a
	 * {@link IDifferenceGroupProvider.Descriptor.Registry registry}.
	 * 
	 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
	 */
	interface Descriptor {

		/**
		 * Creates a group provider.
		 * 
		 * @return the newly created group provider.
		 */
		IDifferenceGroupProvider createGroupProvider();

		/**
		 * A registry of {@link IDifferenceGroupProvider}.
		 */
		interface Registry {

			/**
			 * Returns the list of {@link IDifferenceGroupProvider} contained in the registry.
			 * 
			 * @param scope
			 *            The scope on which the group providers will be applied.
			 * @param comparison
			 *            The comparison which is to be displayed in the structural view.
			 * @return The list of {@link IDifferenceGroupProvider} contained in the registry.
			 */
			Collection<IDifferenceGroupProvider> getGroupProviders(IComparisonScope scope,
					Comparison comparison);

			IDifferenceGroupProvider getDefaultGroupProviders(IComparisonScope scope, Comparison comparison);

			/**
			 * Add to the registry the given {@link IDifferenceGroupProvider}.
			 * 
			 * @param provider
			 *            The given {@link IDifferenceGroupProvider}.
			 * @return The previous value associated with the class name of the given
			 *         {@link IDifferenceGroupProvider}, or null if there was no entry in the registry for the
			 *         class name.
			 */
			IDifferenceGroupProvider.Descriptor add(IDifferenceGroupProvider.Descriptor provider,
					String className);

			/**
			 * Remove from the registry the {@link IDifferenceGroupProvider} designated by the given
			 * {@link String} .
			 * 
			 * @param className
			 *            The given {@link String} representing a {@link IDifferenceGroupProvider}.
			 * @return The {@link IDifferenceGroupProvider} designated by the given {@link String}.
			 */
			IDifferenceGroupProvider.Descriptor remove(String className);

			/**
			 * Clear the registry.
			 */
			void clear();
		}

	}

	/**
	 * Returns all {@link TreeNode}s that are wrapping the given {@code eObject}. It internally use a cross
	 * reference adapter.
	 * 
	 * @param eObject
	 *            the object from which we want inverse reference.
	 * @return all {@link TreeNode}s targeting the given {@code eObject} through
	 *         {@link TreePackage.Literals#TREE_NODE__DATA}.
	 */
	List<TreeNode> getTreeNodes(EObject eObject);

}
