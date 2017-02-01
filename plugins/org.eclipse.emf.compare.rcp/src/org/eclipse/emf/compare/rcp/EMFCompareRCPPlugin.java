/*******************************************************************************
 * Copyright (c) 2013, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 483798
 *     Mathias Schaefer - preferences refactoring
 *******************************************************************************/
package org.eclipse.emf.compare.rcp;

import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.EMFC_APPENDER_NAME;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_BACKUP_COUNT_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_BACKUP_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILENAME_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILE_MAX_SIZE_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_FILE_SIZE_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_LEVEL_DEFAULT;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_LEVEL_KEY;
import static org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences.LOG_PATTERN;

import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptor;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactoryDescriptorRegistryImpl;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.provider.EMFCompareEditPlugin;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.graph.IGraphConsumer;
import org.eclipse.emf.compare.rcp.internal.EMFCompareRCPMessages;
import org.eclipse.emf.compare.rcp.internal.adapterfactory.AdapterFactoryDescriptorRegistryListener;
import org.eclipse.emf.compare.rcp.internal.extension.IItemRegistry;
import org.eclipse.emf.compare.rcp.internal.extension.impl.DescriptorRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.extension.impl.ItemRegistry;
import org.eclipse.emf.compare.rcp.internal.match.MatchEngineFactoryRegistryListener;
import org.eclipse.emf.compare.rcp.internal.match.MatchEngineFactoryRegistryWrapper;
import org.eclipse.emf.compare.rcp.internal.match.WeightProviderDescriptorRegistryListener;
import org.eclipse.emf.compare.rcp.internal.merger.MergerExtensionRegistryListener;
import org.eclipse.emf.compare.rcp.internal.policy.LoadOnDemandPolicyRegistryImpl;
import org.eclipse.emf.compare.rcp.internal.policy.LoadOnDemandPolicyRegistryListener;
import org.eclipse.emf.compare.rcp.internal.postprocessor.PostProcessorFactoryRegistryListener;
import org.eclipse.emf.compare.rcp.internal.postprocessor.PostProcessorRegistryImpl;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.rcp.policy.ILoadOnDemandPolicy;
import org.eclipse.emf.compare.req.IReqEngine;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the EMF Compare RCP plugin.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareRCPPlugin extends Plugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "org.eclipse.emf.compare.rcp"; //$NON-NLS-1$

	/** The id of the post processor extension point. */
	public static final String POST_PROCESSOR_PPID = "postProcessor"; //$NON-NLS-1$

	/** The id of the diff engine extension point. */
	public static final String DIFF_ENGINE_PPID = "diffEngine"; //$NON-NLS-1$

	/** The id of the equi engine extension point. */
	public static final String EQUI_ENGINE_PPID = "equiEngine"; //$NON-NLS-1$

	/** The id of the req engine extension point. */
	public static final String REQ_ENGINE_PPID = "reqEngine"; //$NON-NLS-1$

	/** The id of the conflict engine extension point. */
	public static final String CONFLICT_DETECTOR_PPID = "conflictsDetector"; //$NON-NLS-1$

	/** The id of the weight provider extension point. */
	public static final String WEIGHT_PROVIDER_PPID = "weightProvider"; //$NON-NLS-1$

	/** The id of the load on demand policy extension point. */
	public static final String LOAD_ON_DEMAND_POLICY_PPID = "loadOnDemandPolicy"; //$NON-NLS-1$

	/** The id of the merger extension point. */
	public static final String MERGER_PPID = "merger"; //$NON-NLS-1$

	/** The id of the match extension point. */
	public static final String MATCH_ENGINE_PPID = "matchEngine"; //$NON-NLS-1$

	/** The id of the adapter factory extension point. */
	public static final String FACTORY_PPID = "adapterFactory"; //$NON-NLS-1$

	/**
	 * Log4j logger to use throughout EMFCompare for logging purposes.
	 */
	private static final Logger LOGGER = Logger.getLogger("org.eclipse.emf.compare"); //$NON-NLS-1$

	/** Number of bytes in a MiB. */
	private static final int MEGABYTE = 1024 * 1024;

	/** This plugin is a singleton, so it's quite ok to keep the plugin in a static field. */
	private static EMFCompareRCPPlugin plugin;

	/** The registry that will hold references to all mergers. */
	private IMerger.Registry mergerRegistry;

	/** The registry that will hold references to all differences engines. */
	private ItemRegistry<IDiffEngine> diffEngineRegistry;

	/** The registry that will hold references to all equivalences engines. */
	private ItemRegistry<IEquiEngine> equiEngineRegistry;

	/** The registry that will hold references to all requirements engines. */
	private ItemRegistry<IReqEngine> reqEngineRegistry;

	/** The registry that will hold references to all conflicts detector. */
	private ItemRegistry<IConflictDetector> conflictDetectorRegistry;

	/** The registry that will hold references to all weight providers. */
	private WeightProvider.Descriptor.Registry weightProviderRegistry;

	/** The registry listener that will be used to react to merger changes. */
	private AbstractRegistryEventListener mergerRegistryListener;

	/** The registry that will hold references to all {@link ILoadOnDemandPolicy}. **/
	private ILoadOnDemandPolicy.Registry loadOnDemandRegistry;

	/** The registry listener that will be used to react to load on demand policy changes. */
	private AbstractRegistryEventListener loadOnDemandRegistryListener;

	/** The registry that will hold references to all post processors. */
	private IPostProcessor.Descriptor.Registry<String> postProcessorDescriptorsRegistry;

	/** The registry that will hold reference to all post processors descriptors. */
	private IItemRegistry<IPostProcessor.Descriptor> postProcessorItemDescriptorsRegistry;

	/** The registry listener that will be used to react to post processor changes. */
	private AbstractRegistryEventListener postProcessorFactoryRegistryListener;

	/** The registry that will hold references to all match engine factories. */
	private IItemRegistry<IMatchEngine.Factory> matchEngineFactoryRegistry;

	/** The registry that will hold references to all match engine factories. */
	private MatchEngineFactoryRegistryWrapper matchEngineFactoryRegistryWrapped;

	/** The registry listener that will be used to react to match engine changes. */
	private MatchEngineFactoryRegistryListener matchEngineFactoryRegistryListener;

	/** Backing multimap that will hold references to all adapter factory descriptors. */
	private Multimap<Collection<?>, RankedAdapterFactoryDescriptor> adapterFactoryRegistryBackingMultimap;

	/** Adapter factory registry that references all the EMF Compare adapter factories descriptors. */
	private RankedAdapterFactoryDescriptorRegistryImpl rankedAdapterFactoryRegistry;

	/** The registry listener that will be used to react to adapter factory descriptor changes. */
	private AbstractRegistryEventListener adapterFactoryRegistryListener;

	/** The registry listener that will be used to react to diff engine changes. */
	private DescriptorRegistryEventListener<IDiffEngine> diffEngineListener;

	/** The registry listener that will be used to react to equivalences engine changes. */
	private DescriptorRegistryEventListener<IEquiEngine> equiEngineListener;

	/** The registry listener that will be used to react to requirements engine changes. */
	private DescriptorRegistryEventListener<IReqEngine> reqEngineListener;

	/** The registry listener that will be used to react to conflict detector changes. */
	private DescriptorRegistryEventListener<IConflictDetector> conflictDetectorListener;

	/** The registry listener that will be used to react to weight provider changes. */
	private WeightProviderDescriptorRegistryListener weightProviderListener;

	/** Will listen to preference changes and update log4j configuration accordingly. */
	private LoggingPreferenceChangeListener preferenceChangeListener;

	/**
	 * Keep all resources graphs identified by their id.
	 */
	private Map<String, IGraphView<URI>> graphsById = new HashMap<String, IGraphView<URI>>();

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		EMFCompareRCPPlugin.plugin = this;

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		setUpAdapterFactoryRegistry(registry);

		setUpWeightProviderRegistry(registry);

		setUpMatchEngineFactoryRegistry(registry);

		setUpMergerRegistry(registry);

		setUpPostProcessorRegisty(registry);

		setUpLoadOnDemandRegistry(registry);

		setUpDiffEngineRegistry(registry);

		setUpEquiEngineRegistry(registry);

		setUpReqEngineRegistry(registry);

		setUpConflictDetectorRegistry(registry);

		initLogging();
	}

	/**
	 * Set the Adapter Factory Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpAdapterFactoryRegistry(final IExtensionRegistry registry) {
		adapterFactoryRegistryBackingMultimap = Multimaps.synchronizedListMultimap(
				ArrayListMultimap.<Collection<?>, RankedAdapterFactoryDescriptor> create());
		adapterFactoryRegistryListener = new AdapterFactoryDescriptorRegistryListener(
				EMFCompareEditPlugin.PLUGIN_ID, FACTORY_PPID, getLog(),
				adapterFactoryRegistryBackingMultimap);
		registry.addListener(adapterFactoryRegistryListener,
				EMFCompareEditPlugin.PLUGIN_ID + '.' + FACTORY_PPID);
		adapterFactoryRegistryListener.readRegistry(registry);
		rankedAdapterFactoryRegistry = new RankedAdapterFactoryDescriptorRegistryImpl(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE,
				Multimaps.unmodifiableMultimap(adapterFactoryRegistryBackingMultimap));
	}

	/**
	 * Set the Match Engine Factory Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpMatchEngineFactoryRegistry(final IExtensionRegistry registry) {
		matchEngineFactoryRegistry = new ItemRegistry<IMatchEngine.Factory>();
		matchEngineFactoryRegistryListener = new MatchEngineFactoryRegistryListener(PLUGIN_ID,
				MATCH_ENGINE_PPID, getLog(), matchEngineFactoryRegistry);
		matchEngineFactoryRegistryListener.readRegistry(registry);
		matchEngineFactoryRegistryWrapped = new MatchEngineFactoryRegistryWrapper(matchEngineFactoryRegistry);
	}

	/**
	 * Set the Merger Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpMergerRegistry(final IExtensionRegistry registry) {
		mergerRegistry = new IMerger.RegistryImpl();
		mergerRegistryListener = new MergerExtensionRegistryListener(PLUGIN_ID, MERGER_PPID, getLog(),
				mergerRegistry);
		registry.addListener(mergerRegistryListener, PLUGIN_ID + '.' + MERGER_PPID);
		mergerRegistryListener.readRegistry(registry);
	}

	/**
	 * Set the Post Processor Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpPostProcessorRegisty(final IExtensionRegistry registry) {
		postProcessorItemDescriptorsRegistry = new ItemRegistry<IPostProcessor.Descriptor>();

		postProcessorFactoryRegistryListener = new PostProcessorFactoryRegistryListener(PLUGIN_ID,
				POST_PROCESSOR_PPID, getLog(), postProcessorItemDescriptorsRegistry);
		registry.addListener(postProcessorFactoryRegistryListener, PLUGIN_ID + '.' + POST_PROCESSOR_PPID);
		postProcessorFactoryRegistryListener.readRegistry(registry);
		postProcessorDescriptorsRegistry = new PostProcessorRegistryImpl(
				postProcessorItemDescriptorsRegistry);
	}

	/**
	 * Set the Load On Demand Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpLoadOnDemandRegistry(final IExtensionRegistry registry) {
		loadOnDemandRegistry = new LoadOnDemandPolicyRegistryImpl();
		loadOnDemandRegistryListener = new LoadOnDemandPolicyRegistryListener(loadOnDemandRegistry, PLUGIN_ID,
				LOAD_ON_DEMAND_POLICY_PPID, getLog());
		registry.addListener(loadOnDemandRegistryListener, PLUGIN_ID + '.' + LOAD_ON_DEMAND_POLICY_PPID);
		loadOnDemandRegistryListener.readRegistry(registry);
	}

	/**
	 * Set the Differences Engine Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpDiffEngineRegistry(final IExtensionRegistry registry) {
		diffEngineRegistry = new ItemRegistry<IDiffEngine>();
		diffEngineListener = new DescriptorRegistryEventListener<IDiffEngine>(PLUGIN_ID, DIFF_ENGINE_PPID,
				getLog(), diffEngineRegistry);
		registry.addListener(diffEngineListener);
		diffEngineListener.readRegistry(registry);
	}

	/**
	 * Set the Equivalences Engine Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpEquiEngineRegistry(final IExtensionRegistry registry) {
		equiEngineRegistry = new ItemRegistry<IEquiEngine>();
		equiEngineListener = new DescriptorRegistryEventListener<IEquiEngine>(PLUGIN_ID, EQUI_ENGINE_PPID,
				getLog(), equiEngineRegistry);
		registry.addListener(equiEngineListener);
		equiEngineListener.readRegistry(registry);
	}

	/**
	 * Set the Requirements Engine Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpReqEngineRegistry(final IExtensionRegistry registry) {
		reqEngineRegistry = new ItemRegistry<IReqEngine>();
		reqEngineListener = new DescriptorRegistryEventListener<IReqEngine>(PLUGIN_ID, REQ_ENGINE_PPID,
				getLog(), reqEngineRegistry);
		registry.addListener(reqEngineListener);
		reqEngineListener.readRegistry(registry);
	}

	/**
	 * Set the Conflicts Detector Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpConflictDetectorRegistry(final IExtensionRegistry registry) {
		conflictDetectorRegistry = new ItemRegistry<IConflictDetector>();
		conflictDetectorListener = new DescriptorRegistryEventListener<IConflictDetector>(PLUGIN_ID,
				CONFLICT_DETECTOR_PPID, getLog(), conflictDetectorRegistry);
		registry.addListener(conflictDetectorListener);
		conflictDetectorListener.readRegistry(registry);
	}

	/**
	 * Set the Weight Provider Registry.
	 * 
	 * @param registry
	 *            {@link IExtensionRegistry} to listen in order to fill the registry
	 */
	private void setUpWeightProviderRegistry(final IExtensionRegistry registry) {
		weightProviderRegistry = new WeightProviderDescriptorRegistryImpl();
		weightProviderListener = new WeightProviderDescriptorRegistryListener(PLUGIN_ID, WEIGHT_PROVIDER_PPID,
				getLog(), weightProviderRegistry);
		registry.addListener(weightProviderListener);
		weightProviderListener.readRegistry(registry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (preferenceChangeListener != null) {
			InstanceScope.INSTANCE.getNode(EMFCompareRCPPlugin.PLUGIN_ID)
					.removePreferenceChangeListener(preferenceChangeListener);
		}

		final IExtensionRegistry registry = Platform.getExtensionRegistry();

		discardConflictDetectorRegistry(registry);

		discardReqEngineRegistry(registry);

		discardEquiEngineRegistry(registry);

		discardDiffEngineRegistry(registry);

		discardLoadOnDemandeRegistry(registry);

		discardPostProcessorfactoryRegistry(registry);

		discardMergerRegistry(registry);

		discardMatchEngineRegistry(registry);

		discardWeightProviderRegistry(registry);

		discardAdapterFactoryRegistry(registry);

		super.stop(bundleContext);
	}

	/**
	 * Discard Requirement Engine Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardReqEngineRegistry(final IExtensionRegistry registry) {
		registry.removeListener(reqEngineListener);
		reqEngineListener = null;
		reqEngineRegistry = null;
	}

	/**
	 * Discard Conflict Detector Engine Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardConflictDetectorRegistry(final IExtensionRegistry registry) {
		registry.removeListener(conflictDetectorListener);
		conflictDetectorListener = null;
		conflictDetectorRegistry = null;
	}

	/**
	 * Discard Weight Provider Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardWeightProviderRegistry(final IExtensionRegistry registry) {
		registry.removeListener(weightProviderListener);
		weightProviderListener = null;
		weightProviderRegistry = null;
	}

	/**
	 * Discard Equivalence Engine Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardEquiEngineRegistry(final IExtensionRegistry registry) {
		registry.removeListener(equiEngineListener);
		equiEngineListener = null;
		equiEngineRegistry = null;
	}

	/**
	 * Discard Difference Engine Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardDiffEngineRegistry(final IExtensionRegistry registry) {
		registry.removeListener(diffEngineListener);
		diffEngineListener = null;
		diffEngineRegistry = null;
	}

	/**
	 * Discard Adapter Factory Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardAdapterFactoryRegistry(final IExtensionRegistry registry) {
		rankedAdapterFactoryRegistry = null;
		registry.removeListener(adapterFactoryRegistryListener);
		adapterFactoryRegistryListener = null;
		adapterFactoryRegistryBackingMultimap = null;
	}

	/**
	 * Discard Match Engine Factory Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardMatchEngineRegistry(final IExtensionRegistry registry) {
		registry.removeListener(matchEngineFactoryRegistryListener);
		matchEngineFactoryRegistryListener = null;
		matchEngineFactoryRegistry = null;
		matchEngineFactoryRegistryWrapped = null;
	}

	/**
	 * Discard Merger Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardMergerRegistry(final IExtensionRegistry registry) {
		registry.removeListener(mergerRegistryListener);
		mergerRegistryListener = null;
		mergerRegistry = null;
	}

	/**
	 * Discard Post Processor Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardPostProcessorfactoryRegistry(final IExtensionRegistry registry) {
		registry.removeListener(postProcessorFactoryRegistryListener);
		postProcessorFactoryRegistryListener = null;
		postProcessorDescriptorsRegistry = null;
		postProcessorItemDescriptorsRegistry = null;
	}

	/**
	 * Discard Load On Demand Registry.
	 * 
	 * @param registry
	 *            IExtensionRegistry to remove listener
	 */
	private void discardLoadOnDemandeRegistry(final IExtensionRegistry registry) {
		registry.removeListener(loadOnDemandRegistryListener);
		loadOnDemandRegistryListener = null;
		loadOnDemandRegistry = null;
	}

	/**
	 * Returns the adapter factory descriptor registry to which extension will be registered.
	 * 
	 * @return the the adapter factory descriptor registry to which extension will be registered
	 * @since 3.0
	 */
	public RankedAdapterFactoryDescriptor.Registry getAdapterFactoryRegistry() {
		return rankedAdapterFactoryRegistry;
	}

	/**
	 * Returns a new instance of EMF Compare adapter factory descriptor registry to which extension will be
	 * registered. It filters available adapter factories using preferences.
	 * 
	 * @return the the adapter factory descriptor registry to which extension will be registered
	 * @since 2.3
	 * @deprecated Use {@link #createFilteredAdapterFactoryRegistry(Map)} to take the context into
	 *             consideration. Typically, you would provide the comparison as a context map by passing
	 *             <code>ImmutableMap.of(IContextTester.CTX_COMPARISON, comparison)</code> as an argument. If
	 *             no comparison context is available, use an empty context.
	 */
	@Deprecated
	public RankedAdapterFactoryDescriptor.Registry createFilteredAdapterFactoryRegistry() {
		return createFilteredAdapterFactoryRegistry(Maps.newLinkedHashMap());
	}

	/**
	 * Returns a new instance of EMF Compare adapter factory descriptor registry to which extension will be
	 * registered. It filters available adapter factories using preferences.
	 * 
	 * @param context
	 *            context for the adapter factories. This context cannot be null but may be empty.
	 * @return the adapter factory descriptor registry to which extension will be registered
	 * @since 2.5
	 */
	public RankedAdapterFactoryDescriptor.Registry createFilteredAdapterFactoryRegistry(
			Map<Object, Object> context) {
		final List<String> disabledAdapterFactories = EMFComparePreferences
				.getDisabledAdapterFactoryDescriptorIds();
		// Filters disabled adapter factories
		Multimap<Collection<?>, RankedAdapterFactoryDescriptor> filteredBackingMultimap = ImmutableMultimap
				.copyOf(Multimaps.filterValues(adapterFactoryRegistryBackingMultimap,
						new Predicate<RankedAdapterFactoryDescriptor>() {
							public boolean apply(RankedAdapterFactoryDescriptor input) {
								return !disabledAdapterFactories.contains(input.getId());
							}
						}));

		return new RankedAdapterFactoryDescriptorRegistryImpl(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE, filteredBackingMultimap, context);
	}

	/**
	 * Returns the merger registry to which extension will be registered.
	 * 
	 * @return the merger registry to which extension will be registered
	 * @since 3.0
	 */
	public IMerger.Registry getMergerRegistry() {
		return mergerRegistry;
	}

	/**
	 * Returns the post processor registry to which extension will be registered.
	 * 
	 * @return the post processor registry to which extension will be registered
	 */
	public IPostProcessor.Descriptor.Registry<String> getPostProcessorRegistry() {
		return postProcessorDescriptorsRegistry;
	}

	/**
	 * Get the {@link IItemRegistry} of {@link IPostProcessor.Descriptor}.
	 * 
	 * @return {@link IItemRegistry} of {@link IPostProcessor.Descriptor}.
	 * @since 2.2.0
	 */
	public IItemRegistry<IPostProcessor.Descriptor> getPostProcessorDescriptorRegistry() {
		return postProcessorItemDescriptorsRegistry;
	}

	/**
	 * Returns the registry of load on demand policies.
	 * 
	 * @return the registry of load on demand policies.
	 */
	public ILoadOnDemandPolicy.Registry getLoadOnDemandPolicyRegistry() {
		return loadOnDemandRegistry;
	}

	/**
	 * Returns the registry of Differences engines.
	 * 
	 * @return the registry of Differences engines
	 */
	public IItemRegistry<IDiffEngine> getDiffEngineDescriptorRegistry() {
		return diffEngineRegistry;
	}

	/**
	 * Returns the registry of Equivalences engines.
	 * 
	 * @return the registry of Equivalences engines
	 */
	public IItemRegistry<IEquiEngine> getEquiEngineDescriptorRegistry() {
		return equiEngineRegistry;
	}

	/**
	 * Returns the registry of Requirements engines.
	 * 
	 * @return the registry of Requirements engines
	 */
	public IItemRegistry<IReqEngine> getReqEngineDescriptorRegistry() {
		return reqEngineRegistry;
	}

	/**
	 * Returns the registry of Conflict detector.
	 * 
	 * @return the registry of Conflict detector
	 */
	public IItemRegistry<IConflictDetector> getConflictDetectorDescriptorRegistry() {
		return conflictDetectorRegistry;
	}

	/**
	 * Returns the registry of weight providers.
	 * 
	 * @return the registry of weight providers
	 */
	public WeightProvider.Descriptor.Registry getWeightProviderRegistry() {
		return weightProviderRegistry;
	}

	/**
	 * Returns the match engine factory registry to which extension will be registered.
	 * 
	 * @return the match engine factory registry to which extension will be registered
	 * @since 3.0
	 */
	public IMatchEngine.Factory.Registry getMatchEngineFactoryRegistry() {
		return matchEngineFactoryRegistryWrapped;
	}

	/**
	 * Returns the match engine factory registry to which extension will be registered.
	 * 
	 * @return the match engine factory registry to which extension will be registered
	 * @since 3.0
	 */
	public IItemRegistry<IMatchEngine.Factory> getMatchEngineFactoryDescriptorRegistry() {
		return matchEngineFactoryRegistry;
	}

	/**
	 * Log the given message with the given severity to the logger of this plugin.
	 * 
	 * @param severity
	 *            the severity of the message.
	 * @param message
	 *            the message to log.
	 */
	public void log(int severity, String message) {
		getLog().log(new Status(severity, PLUGIN_ID, message));
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static EMFCompareRCPPlugin getDefault() {
		return plugin;
	}

	/**
	 * Initializes log4j by reading the preferences.
	 */
	private void initLogging() {
		LOGGER.setLevel(Level.toLevel(Platform.getPreferencesService()
				.getString(EMFCompareRCPPlugin.PLUGIN_ID, LOG_LEVEL_KEY, LOG_LEVEL_DEFAULT, null)));
		if (!Level.OFF.equals(LOGGER.getLevel())) {
			RollingFileAppender appender = (RollingFileAppender)LOGGER.getAppender(EMFC_APPENDER_NAME);
			String logFileName = Platform.getPreferencesService().getString(EMFCompareRCPPlugin.PLUGIN_ID,
					LOG_FILENAME_KEY, "", null); //$NON-NLS-1$
			if (logFileName.length() > 0) {
				if (appender == null) {
					try {
						createLogAppender(logFileName);
					} catch (IOException e) {
						// Invalidate file name
						Platform.getPreferencesService().getString(EMFCompareRCPPlugin.PLUGIN_ID,
								LOG_FILENAME_KEY, "", null); //$NON-NLS-1$
					}
				} else {
					appender.setMaxBackupIndex(Platform.getPreferencesService().getInt(
							EMFCompareRCPPlugin.PLUGIN_ID, LOG_BACKUP_COUNT_KEY, LOG_BACKUP_DEFAULT, null));
					appender.setMaximumFileSize(
							(Platform.getPreferencesService().getInt(EMFCompareRCPPlugin.PLUGIN_ID,
									LOG_FILE_MAX_SIZE_KEY, LOG_FILE_SIZE_DEFAULT, null)) * MEGABYTE);
				}
			}
		}
		preferenceChangeListener = new LoggingPreferenceChangeListener();
		InstanceScope.INSTANCE.getNode(EMFCompareRCPPlugin.PLUGIN_ID)
				.addPreferenceChangeListener(preferenceChangeListener);
	}

	/**
	 * Creates the RollingFileAppender used to log with log4j.
	 * 
	 * @param newFileName
	 *            Path opf the log file
	 * @throws IOException
	 *             If an IO problem occurs, like the given path does not represent a file, or it cannot be
	 *             written;
	 */
	private void createLogAppender(String newFileName) throws IOException {
		RollingFileAppender appender;
		appender = new RollingFileAppender(new PatternLayout(LOG_PATTERN), newFileName, true);
		LOGGER.removeAllAppenders(); // We don't want to log elsewhere
		LOGGER.addAppender(appender);
		appender.setMaxBackupIndex(Platform.getPreferencesService().getInt(EMFCompareRCPPlugin.PLUGIN_ID,
				LOG_BACKUP_COUNT_KEY, LOG_BACKUP_DEFAULT, null));
		appender.setMaximumFileSize((Platform.getPreferencesService().getInt(EMFCompareRCPPlugin.PLUGIN_ID,
				LOG_FILE_MAX_SIZE_KEY, LOG_FILE_SIZE_DEFAULT, null)) * MEGABYTE);
	}

	/**
	 * Listens to logging preference changes to maintain the log4j configuration up-to-date.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class LoggingPreferenceChangeListener implements IPreferenceChangeListener {

		/**
		 * The path of the logging file.
		 */
		private String path;

		/**
		 * The level of the logger.
		 */
		private Level loggingLevel;

		/**
		 * Updates the log4j configuration when logging preferences change.
		 * 
		 * @param event
		 *            the preference change event.
		 */
		public void preferenceChange(PreferenceChangeEvent event) {
			final IPreferencesService prefs = Platform.getPreferencesService();

			path = prefs.getString(EMFCompareRCPPlugin.PLUGIN_ID, LOG_FILENAME_KEY, "", null); //$NON-NLS-1$
			if (LOG_LEVEL_KEY.equals(event.getKey())) {
				loggingLevel = Level.toLevel((String)event.getNewValue());
				LOGGER.setLevel(loggingLevel);
			} else if (LOG_FILENAME_KEY.equals(event.getKey())) {
				path = (String)event.getNewValue();
			} else if (LOG_BACKUP_COUNT_KEY.equals(event.getKey())) {
				RollingFileAppender appender = (RollingFileAppender)LOGGER.getAppender(EMFC_APPENDER_NAME);
				if (appender != null) {
					appender.setMaxBackupIndex(Integer.parseInt((String)event.getNewValue()));
				}
			} else if (LOG_FILE_MAX_SIZE_KEY.equals(event.getKey())) {
				RollingFileAppender appender = (RollingFileAppender)LOGGER.getAppender(EMFC_APPENDER_NAME);
				if (appender != null) {
					appender.setMaximumFileSize(Integer.parseInt((String)event.getNewValue()) * MEGABYTE);
				}
			}
			if (loggingLevel != null && !Level.OFF.equals(loggingLevel) && path != null
					&& path.length() > 0) {
				initFile();
			}
		}

		/**
		 * Create the logging file.
		 */
		private void initFile() {
			RollingFileAppender appender = (RollingFileAppender)LOGGER.getAppender(EMFC_APPENDER_NAME);
			if (appender == null) {
				try {
					EMFCompareRCPPlugin.getDefault().createLogAppender(path);
				} catch (IOException e) {
					// Force the value to be harmless
					InstanceScope.INSTANCE.getNode(EMFCompareRCPPlugin.PLUGIN_ID).put(LOG_FILENAME_KEY, ""); //$NON-NLS-1$
					getDefault().log(IStatus.ERROR,
							EMFCompareRCPMessages.getString("logging.appender.error", path, e.getMessage())); //$NON-NLS-1$
				}
			} else {
				appender.setFile(path);
			}
		}

	}

	/**
	 * This method creates a new Graph of URI, passes it to the given consumer and then keeps track of the
	 * given graph to be able to provide a read only view of it on demand.
	 * 
	 * @param consumer
	 *            An instance of graph consumer, for instance the ThreadedModelResolver instance.
	 * @throws IllegalArgumentException
	 *             if the consumer uses an ID that is already registered.
	 * @since 2.4
	 */
	public void register(IGraphConsumer consumer) {
		consumer.setGraph(new Graph<URI>());
		String id = consumer.getId();
		if (graphsById.containsKey(id)) {
			throw new IllegalArgumentException(EMFCompareRCPMessages.getString("duplicate.graph.id.msg", id)); //$NON-NLS-1$
		}
		graphsById.put(id, consumer.getGraphView());
	}

	/**
	 * Return the graph view associated with the given id, or null if it does not exist.
	 * 
	 * @param id
	 *            The id of the graph
	 * @return The graph view registered for the given ID, which can be null.
	 * @since 2.4
	 */
	public IGraphView<URI> getGraphView(String id) {
		return graphsById.get(id);
	}

}
