/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.postprocessor;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.AbstractItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemUtil;
import org.eclipse.emf.compare.rcp.internal.extension.impl.WrapperItemDescriptor;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.scope.IComparisonScope;

/**
 * IPostProcessor.Descriptor.Registry implementation based on wrapping a {@link IItemRegistry}.
 * 
 * @see IItemRegistry
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class PostProcessorRegistryImpl implements IPostProcessor.Descriptor.Registry<String> {

	/** EMPTY_STRING. */
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	/** {@link IItemRegistry} of descriptor of {@link IItemDescriptor}. */
	private final IItemRegistry<IPostProcessor.Descriptor> baseRegisty;

	/**
	 * Constructor.
	 * 
	 * @param baseRegisty
	 *            {@link IItemDescriptor} filled with {@link IItemDescriptor} of
	 *            {@link IPostProcessor.Descriptor}.
	 */
	public PostProcessorRegistryImpl(IItemRegistry<IPostProcessor.Descriptor> baseRegisty) {
		super();
		this.baseRegisty = baseRegisty;
	}

	/**
	 * {@inheritDoc}
	 */
	public IPostProcessor.Descriptor put(String key, IPostProcessor.Descriptor descriptor) {
		WrapperItemDescriptor<IPostProcessor.Descriptor> newDescriptor = new WrapperItemDescriptor<IPostProcessor.Descriptor>(
				EMPTY_STRING, EMPTY_STRING, descriptor.getOrdinal(), descriptor.getInstanceClassName(),
				descriptor);
		IItemDescriptor<IPostProcessor.Descriptor> oldDescriptor = baseRegisty.add(newDescriptor);
		if (oldDescriptor != null) {
			return oldDescriptor.getItem();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		baseRegisty.clear();

	}

	/**
	 * {@inheritDoc}
	 */
	public List<IPostProcessor.Descriptor> getDescriptors() {
		List<IItemDescriptor<Descriptor>> itemDescriptors = baseRegisty.getItemDescriptors();
		Collections.sort(itemDescriptors);

		Collection<IItemDescriptor<IPostProcessor.Descriptor>> activeDescriptor = Collections2
				.filter(itemDescriptors, not(in(getDisabledEngines())));

		Collection<IPostProcessor.Descriptor> descriptors = Collections2.transform(activeDescriptor,
				AbstractItemDescriptor.<IPostProcessor.Descriptor> getItemFunction());
		return Lists.newArrayList(descriptors);
	}

	/**
	 * Return a collection of disabled {@link IItemDescriptor<IPostProcessor.Descriptor>}.
	 * 
	 * @return Collection<IItemDescriptor<IPostProcessor.Descriptor>>
	 */
	private Collection<IItemDescriptor<IPostProcessor.Descriptor>> getDisabledEngines() {
		List<IItemDescriptor<IPostProcessor.Descriptor>> result = ItemUtil.getItemsDescriptor(baseRegisty,
				EMFComparePreferences.DISABLED_POST_PROCESSOR,
				EMFCompareRCPPlugin.getDefault().getEMFComparePreferences());
		if (result == null) {
			result = Collections.emptyList();
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public Descriptor remove(String key) {
		IItemDescriptor<IPostProcessor.Descriptor> oldDescriptor = baseRegisty.remove(key);
		if (oldDescriptor != null) {
			return oldDescriptor.getItem();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor.Registry#getPostProcessors(org.eclipse.emf.compare.scope.IComparisonScope)
	 */
	public List<IPostProcessor> getPostProcessors(IComparisonScope scope) {
		final ImmutableList.Builder<IPostProcessor> processors = ImmutableList.builder();
		for (IPostProcessor.Descriptor factory : getDescriptors()) {
			Pattern nsURIPattern = factory.getNsURI();
			if (nsURIPattern != null) {
				for (String nsURI : scope.getNsURIs()) {
					if (nsURIPattern.matcher(nsURI).matches()) {
						processors.add(factory.getPostProcessor());
						break;
					}
				}
			}
			// Should probably use two loops here to prioritize NsURI matching
			Pattern resourceURIPattern = factory.getResourceURI();
			if (resourceURIPattern != null) {
				for (String resourceURI : scope.getResourceURIs()) {
					if (resourceURIPattern.matcher(resourceURI).matches()) {
						processors.add(factory.getPostProcessor());
						break;
					}
				}
			}
		}
		return processors.build();
	}

}
