/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *     Philip Langer - filter diagrams and refactorings
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.provider;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.toArray;
import static com.google.common.collect.Iterables.transform;
import static java.util.Arrays.asList;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.facet.PapyrusFacetContentProviderWrapperAdapterFactory;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.item.MergeViewerItemConverter;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.tree.provider.TreeContentMergeViewerItemContentProvider;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.ICompareAccessor;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.StructureMergeViewerFilter;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.StructureMergeViewerGrouper;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.item.IMergeViewerItem;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * The content provider for the {@link PyprusTreeContentMergeViewer}.
 * <p>
 * Note that this content provider filters {@link Diagram diagrams}, as they shouldn't show up in the content
 * merge viewer after double-clicking a UML model element or a diff of a model element.
 * </p>
 * 
 * @author Stefan Dirix
 */
@SuppressWarnings("restriction")
public class PapyrusTreeContentMergeViewerItemContentProvider extends TreeContentMergeViewerItemContentProvider {

	/** Filters Diagram elements for inputs of type MergeViewerItem, Match, and Object. */
	private static final Predicate<Object> DIAGRAM_FILTER = new Predicate<Object>() {
		public boolean apply(Object input) {
			if (input instanceof IMergeViewerItem) {
				final IMergeViewerItem item = (IMergeViewerItem)input;
				return containsNoDiagrams(asSet(item.getLeft(), item.getRight(), item.getAncestor()));
			} else if (input instanceof Match) {
				final Match match = (Match)input;
				return containsNoDiagrams(asSet(match.getLeft(), match.getRight(), match.getOrigin()));
			} else if (input instanceof Diagram) {
				return true;
			}
			return false;
		}
	};

	/**
	 * The Compare Configuration.
	 */
	private EMFCompareConfiguration compareConfiguration;

	/**
	 * The Converter to replace the 'standard' {@link IMergeViewerItem}s with the specialized ones of Papyrus.
	 */
	private MergeViewerItemConverter converter;

	/**
	 * The {@link AdapterFactory} for the {@link IMergeViewerItem}s.
	 */
	private AdapterFactory itemAdapterFactory;

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            the {@link AdapterFactory}.
	 * @param compareConfiguration
	 *            the {@link EMFCompareConfiguration}.
	 * @param groupProvider
	 *            the {@link IDifferenceGroupProvider}.
	 * @param predicate
	 *            the {@link Predicate}.
	 */
	public PapyrusTreeContentMergeViewerItemContentProvider(AdapterFactory adapterFactory,
			EMFCompareConfiguration compareConfiguration, IDifferenceGroupProvider groupProvider,
			Predicate<? super EObject> predicate) {
		super(adapterFactory, groupProvider, predicate);
		this.adapterFactory = adapterFactory;
		this.compareConfiguration = compareConfiguration;
		this.itemAdapterFactory = new PapyrusFacetContentProviderWrapperAdapterFactory();
		this.converter = new MergeViewerItemConverter(
				new ComposedAdapterFactory(new AdapterFactory[] {itemAdapterFactory, adapterFactory, }),
				compareConfiguration.getComparison());
	}

	@Override
	public Object[] getElements(Object object) {
		if (object instanceof ICompareAccessor) {
			final ICompareAccessor accessor = (ICompareAccessor)object;
			return toArray(convert(filtered(accessor.getItems())), Object.class);
		}
		return super.getElements(object);
	}

	/**
	 * {@inheritDoc} Note that {@link Diagram diagrams} will be filtered.
	 */
	@Override
	public Object[] getChildren(Object object) {
		return toArray(filter(asList(super.getChildren(object)), DIAGRAM_FILTER), Object.class);
	}

	/**
	 * Convert {@link IMergeViewerItem} s to the specialized ones of Papyrus.
	 * 
	 * @param items
	 *            The {@link IMergeViewerItem}s which shall be converted.
	 * @return An {@link Iterable} over the converted {@link IMergeViewerItem}s.
	 */
	private Iterable<? extends IMergeViewerItem> convert(Iterable<? extends IMergeViewerItem> items) {
		return transform(items, new Function<IMergeViewerItem, IMergeViewerItem>() {
			public IMergeViewerItem apply(IMergeViewerItem input) {
				return converter.convert(input);
			}
		});
	}

	/**
	 * Skips {@link IMergeViewerItem}s whose diffs shall not be displayed.
	 * 
	 * @param items
	 *            The {@link IMergeViewerItem}s to filter.
	 * @return An {@link Iterable} over the {@link IMergeViewerItem}s which shall be displayed.
	 */
	private Iterable<? extends IMergeViewerItem> filtered(Iterable<? extends IMergeViewerItem> items) {
		final StructureMergeViewerGrouper grouper = compareConfiguration.getStructureMergeViewerGrouper();
		final StructureMergeViewerFilter filter = compareConfiguration.getStructureMergeViewerFilter();
		final IDifferenceGroupProvider provider = grouper.getProvider();
		final Predicate<? super EObject> aggregatedPredicate = filter.getAggregatedPredicate();
		return filter(items, new Predicate<IMergeViewerItem>() {
			public boolean apply(IMergeViewerItem item) {
				final Match match = getMatch(item.getLeft(), item.getRight(), item.getAncestor());
				if (match == null) {
					// no match, but we can still show the element in the PCMV
					return true;
				}
				if (!DIAGRAM_FILTER.apply(match)) {
					return false;
				}
				return any(match.getAllDifferences(), new Predicate<Diff>() {
					public boolean apply(Diff diff) {
						return MergeViewerUtil.isVisibleInMergeViewer(diff, provider, aggregatedPredicate);
					}
				});
			}
		});
	}

	/**
	 * Determines the {@link Match} for the given object triple.
	 * 
	 * @param left
	 *            the left object.
	 * @param right
	 *            the right object.
	 * @param origin
	 *            the origin object.
	 * @return The determined {@link Match}, {@code null} otherwise.
	 */
	private Match getMatch(Object left, Object right, Object origin) {
		Match match = getMatch(left);
		if (match != null) {
			return match;
		}
		match = getMatch(right);
		if (match != null) {
			return match;
		}
		return getMatch(origin);
	}

	/**
	 * Determines the {@link Match} for the given object.
	 * 
	 * @param object
	 *            The object for which the match is to be determined.
	 * @return The determined {@link Match}, {@code null} otherwise.
	 */
	private Match getMatch(Object object) {
		if (object == null) {
			return null;
		}
		for (Match match : compareConfiguration.getComparison().getMatches()) {
			if (object == match.getLeft() || object == match.getRight() || object == match.getOrigin()) {
				return match;
			}
		}
		return null;
	}

	/**
	 * Creates a null-safe set of the specified objects.
	 * 
	 * @param objects
	 *            To create set of.
	 * @return The created set. Null values are ignored.
	 */
	private static Set<Object> asSet(Object... objects) {
		Builder<Object> builder = ImmutableSet.builder();
		for (Object object : objects) {
			if (object != null) {
				builder.add(object);
			}
		}
		return builder.build();
	}

	/**
	 * Specifies whether the given objects contain <em>no</em> instances of Diagrams.
	 * 
	 * @param objects
	 *            The objects to check.
	 * @return <code>true</code> if no diagram is contained, <code>false</code> otherwise.
	 */
	private static boolean containsNoDiagrams(Set<Object> objects) {
		return !any(objects, instanceOf(Diagram.class));
	}

	@Override
	public void dispose() {
		if (IDisposable.class.isInstance(itemAdapterFactory)) {
			IDisposable.class.cast(itemAdapterFactory).dispose();
		}
	}
}
