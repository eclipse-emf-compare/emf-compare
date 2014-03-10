/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.extension.impl.WrapperItemDescriptor;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.ComparisonType;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.osgi.service.prefs.Preferences;

/**
 * Implementation of the {@link IDifferenceGroupProvider.Descriptor.Registry}. This implementation allow user
 * to override ranking of each group using preferences.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 4.0
 */
public class DifferenceGroupRegistryImpl implements IDifferenceGroupProvider.Descriptor.Registry {

	/** A map that associates the class name to theirs {@link IDifferenceGroupProvider.Descriptor}s. */
	private final IItemRegistry<IDifferenceGroupProvider.Descriptor> registry;

	/** Preference key for two way comparison group ranking. */
	private final String twoWayPreferenceKey;

	/** Preference key for three way comparison group ranking. */
	private final String threeWayPrefenceKey;

	/** Preferences holding ranked descriptors. */
	private Preferences preferences;

	/**
	 * Constructs the registry.
	 * 
	 * @param twoWaygroupProviderDescriptors
	 */
	public DifferenceGroupRegistryImpl(IItemRegistry<IDifferenceGroupProvider.Descriptor> registry,
			Preferences preferences, String twoWayPreferenceKey, String threeWayPrefenceKey) {
		this.registry = registry;
		this.twoWayPreferenceKey = twoWayPreferenceKey;
		this.threeWayPrefenceKey = threeWayPrefenceKey;
		this.preferences = preferences;
	}

	/**
	 * Return an ordered list of IItemDescriptor<IDifferenceGroupProvider.Descriptor>.
	 * 
	 * @return Ordered list of IItemDescriptor<IDifferenceGroupProvider.Descriptor>.
	 */
	private List<IItemDescriptor<IDifferenceGroupProvider.Descriptor>> getOrderedGroupProviderDescriptors(
			final Comparison comparison) {
		final List<IItemDescriptor<Descriptor>> items;
		if (comparison.isThreeWay()) {
			items = ItemUtil.getOrderedItems(getDefaultThreeWayDescriptors(), registry, threeWayPrefenceKey,
					preferences);
		} else {
			items = ItemUtil.getOrderedItems(getDefaultTwoWayDescriptors(), registry, twoWayPreferenceKey,
					preferences);
		}
		return items;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#getGroupProviders(IComparisonScope,
	 *      Comparison)
	 */
	public List<IDifferenceGroupProvider.Descriptor> getGroupProviders(IComparisonScope scope,
			Comparison comparison) {
		if (comparison != null) {
			List<IDifferenceGroupProvider.Descriptor> providers = newArrayList();
			List<IItemDescriptor<Descriptor>> groupProviderDescriptors = getOrderedGroupProviderDescriptors(comparison);
			ListIterator<IItemDescriptor<Descriptor>> groupIterator = groupProviderDescriptors.listIterator();
			while (groupIterator.hasNext()) {
				IItemDescriptor<Descriptor> desc = groupIterator.next();
				IDifferenceGroupProvider.Descriptor groupProviderDescriptor = desc.getItem();
				IDifferenceGroupProvider gp = groupProviderDescriptor.createGroupProvider();
				if (gp != null && isGroupProviderActivable(gp, scope, comparison)) {
					providers.add(groupProviderDescriptor);
				}
			}
			return ImmutableList.copyOf(providers);
		}
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#getDefaultGroupProvider(org.eclipse.emf.compare.scope.IComparisonScope,
	 *      org.eclipse.emf.compare.Comparison)
	 */
	public IDifferenceGroupProvider.Descriptor getDefaultGroupProvider(IComparisonScope scope,
			Comparison comparison) {
		List<IDifferenceGroupProvider.Descriptor> descriptors = getGroupProviders(scope, comparison);
		if (!descriptors.isEmpty()) {
			return descriptors.get(0);
		}
		return null;
	}

	/**
	 * Checks if the given IDifferenceGroupProvider is activable based on the scope and comparison objects.
	 * 
	 * @param dgp
	 *            the given IDifferenceGroupProvider.
	 * @param scope
	 *            The scope on which the group provider will be applied.
	 * @param comparison
	 *            The comparison which is to be displayed in the structural view.
	 * @return true, if it is activable, false otherwise.
	 */
	static final boolean isGroupProviderActivable(final IDifferenceGroupProvider dgp,
			final IComparisonScope scope, final Comparison comparison) {
		return dgp.isEnabled(scope, comparison);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#add
	 *      (org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor)
	 */
	public IDifferenceGroupProvider.Descriptor add(IDifferenceGroupProvider.Descriptor providerDescriptor,
			String className) {
		Preconditions.checkNotNull(providerDescriptor);
		IDifferenceGroupProvider groupProvider = providerDescriptor.createGroupProvider();
		WrapperItemDescriptor<IDifferenceGroupProvider.Descriptor> descriptor = new WrapperItemDescriptor<IDifferenceGroupProvider.Descriptor>(
				providerDescriptor.getLabel(), providerDescriptor.getDescription(), providerDescriptor
						.getRank(), groupProvider.getClass().getName(), providerDescriptor);

		IItemDescriptor<Descriptor> oldValue = registry.add(descriptor);
		if (oldValue != null) {
			return oldValue.getItem();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#remove(java.lang.String)
	 */
	public IDifferenceGroupProvider.Descriptor remove(String className) {
		IItemDescriptor<IDifferenceGroupProvider.Descriptor> oldValue = registry.remove(className);
		if (oldValue != null) {
			return oldValue.getItem();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor.Registry#clear()
	 */
	public void clear() {
		registry.clear();
	}

	/**
	 * Get an ordered list of default descriptors for a three way comparison.
	 * 
	 * @return
	 */
	private List<IItemDescriptor<Descriptor>> getDefaultTwoWayDescriptors() {
		Iterable<IItemDescriptor<Descriptor>> threeWayGroups = Iterables.filter(
				registry.getItemDescriptors(), new Predicate<IItemDescriptor<Descriptor>>() {

					public boolean apply(IItemDescriptor<Descriptor> descriptor) {
						if (descriptor == null) {
							return false;
						}
						ComparisonType type = descriptor.getItem().getType();
						return type == ComparisonType.BOTH || type == ComparisonType.TWO_WAY;
					}
				});
		List<IItemDescriptor<Descriptor>> result = Lists.newArrayList(threeWayGroups);
		Collections.sort(result, Collections.reverseOrder());
		return result;
	}

	/**
	 * Get an ordered list of default descriptors for a three way comparison.
	 * 
	 * @return
	 */
	private List<IItemDescriptor<Descriptor>> getDefaultThreeWayDescriptors() {
		Iterable<IItemDescriptor<Descriptor>> twoWayComparison = Iterables.filter(registry
				.getItemDescriptors(), new Predicate<IItemDescriptor<Descriptor>>() {

			public boolean apply(IItemDescriptor<Descriptor> descriptor) {
				if (descriptor == null) {
					return false;
				}
				ComparisonType type = descriptor.getItem().getType();
				return type == ComparisonType.BOTH || type == ComparisonType.THREE_WAY;
			}
		});
		List<IItemDescriptor<Descriptor>> result = Lists.newArrayList(twoWayComparison);
		Collections.sort(result, Collections.reverseOrder());
		return result;
	}

}
