/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.provider;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.provider.TreeNodeItemProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeNodeItemProviderSpec extends TreeNodeItemProvider implements IItemStyledLabelProvider, IItemColorProvider {

	/***/
	private final Map<IDifferenceGroup, GroupItemProviderAdapter> fGroupAdapters = Maps.newHashMap();

	/**
	 * This constructs an instance from a factory.
	 * 
	 * @param adapterFactory
	 *            the given factory
	 */
	public TreeNodeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.tree.provider.TreeNodeItemProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object object) {
		Object parent = null;
		TreeNode treeNode = (TreeNode)object;
		TreeNode superParent = (TreeNode)super.getParent(object);
		if (superParent != null && superParent.getData() instanceof Comparison) {
			EObject parentData = superParent.getData();
			GroupItemProviderAdapter comparisonGroupItemProvider = (GroupItemProviderAdapter)adapterFactory
					.adapt(parentData, IEditingDomainItemProvider.class);
			Collection<?> children = comparisonGroupItemProvider.getChildren(parentData);
			if (children.size() > 1) {
				for (Notifier child : filter(children, Notifier.class)) {
					IDifferenceGroup parentGroup = (IDifferenceGroup)EcoreUtil.getAdapter(child.eAdapters(),
							IDifferenceGroup.class);
					IDifferenceGroup group = (IDifferenceGroup)EcoreUtil.getAdapter(treeNode.getData()
							.eAdapters(), IDifferenceGroup.class);
					if (parentGroup == group) {
						parent = child;
					}
				}
			} else {
				parent = superParent;
			}
		}
		return parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		TreeNode treeNode = (TreeNode)object;
		EObject data = treeNode.getData();
		if (data instanceof Comparison) {
			IDifferenceGroupProvider groupProvider = (IDifferenceGroupProvider)EcoreUtil.getAdapter(treeNode
					.eAdapters(), IDifferenceGroupProvider.class);
			Comparison comparison = (Comparison)data;
			Collection<? extends IDifferenceGroup> groups = groupProvider.getGroups(comparison);
			if (groups.size() > 1) {
				List<GroupItemProviderAdapter> children = newArrayList();
				for (IDifferenceGroup differenceGroup : groups) {
					if (!fGroupAdapters.containsKey(differenceGroup)) {
						fGroupAdapters.put(differenceGroup, new GroupItemProviderAdapter(adapterFactory,
								comparison, differenceGroup));
					}
					children.add(fGroupAdapters.get(differenceGroup));
				}
				return children;
			} else {
				return groups.iterator().next().getGroupTree();
			}
		} else {
			return super.getChildren(object);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IComposedStyledString getStyledText(Object object) {
		TreeNode treeNode = (TreeNode)object;
		EObject data = treeNode.getData();
		return (IComposedStyledString)AdapterFactoryUtil.getStyledText(getRootAdapterFactory(), data);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getBackground(java.lang.Object)
	 */
	@Override
	public Object getBackground(Object object) {
		TreeNode treeNode = ((TreeNode)object);
		return itemDelegator.getBackground(treeNode.getData());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		TreeNode treeNode = ((TreeNode)object);
		return itemDelegator.getForeground(treeNode.getData());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getBackground(java.lang.Object, int)
	 */
	@Override
	public Object getBackground(Object object, int columnIndex) {
		TreeNode treeNode = ((TreeNode)object);
		return itemDelegator.getBackground(treeNode.getData(), columnIndex);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object, int)
	 */
	@Override
	public Object getForeground(Object object, int columnIndex) {
		TreeNode treeNode = ((TreeNode)object);
		return itemDelegator.getForeground(treeNode.getData(), columnIndex);
	}
}
