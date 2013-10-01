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

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.provider.ExtendedAdapterFactoryItemDelegator;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroup;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.tree.TreeNode;
import org.eclipse.emf.edit.tree.provider.TreeNodeItemProvider;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeNodeItemProviderSpec extends TreeNodeItemProvider implements IItemStyledLabelProvider, IItemColorProvider, IItemFontProvider {

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
		itemDelegator = new ExtendedAdapterFactoryItemDelegator(getRootAdapterFactory());
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
		if (superParent == null) {
			GroupItemProviderAdapter groupItemProvider = (GroupItemProviderAdapter)EcoreUtil
					.getExistingAdapter(treeNode, GroupItemProviderAdapter.class);
			return groupItemProvider;
		} else if (object instanceof GroupItemProviderAdapter) {
			parent = ((GroupItemProviderAdapter)object).getParent(null);
		} else {
			parent = superParent;
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
			IDifferenceGroupProvider groupProvider = (IDifferenceGroupProvider)EcoreUtil.getExistingAdapter(
					treeNode, IDifferenceGroupProvider.class);
			Comparison comparison = (Comparison)data;
			Collection<? extends IDifferenceGroup> groups = groupProvider.getGroups(comparison);
			if (groups.size() > 1) {
				List<GroupItemProviderAdapter> children = newArrayList();
				for (IDifferenceGroup differenceGroup : groups) {
					GroupItemProviderAdapter adapter = new GroupItemProviderAdapter(adapterFactory, treeNode,
							differenceGroup);
					children.add(adapter);
				}
				return children;
			} else {
				// do not display group if there is only one.
				return groups.iterator().next().getChildren();
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
		return ((ExtendedAdapterFactoryItemDelegator)itemDelegator).getStyledText(treeNode.getData());
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getFont(java.lang.Object)
	 */
	@Override
	public Object getFont(Object object) {
		TreeNode treeNode = ((TreeNode)object);
		return itemDelegator.getFont(treeNode.getData());
	}
}
