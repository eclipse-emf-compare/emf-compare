/*******************************************************************************
 * Copyright (c) 2014, 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.extension.impl;

import com.google.common.base.Preconditions;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.conflict.IConflictDetector;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.equi.IEquiEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.IEMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.rcp.internal.preferences.EMFComparePreferences;
import org.eclipse.emf.compare.req.IReqEngine;

/**
 * Engine provider. This class provides engines using registered engines and user preferences.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class EMFCompareBuilderConfigurator implements IEMFCompareBuilderConfigurator {

	/** {@link IEclipsePreferences} holding engines information. */
	private final IEclipsePreferences enginePreferences;

	/** Match engine factory registry. */
	private final IMatchEngine.Factory.Registry matchEngineFactoryRegistry;

	/** Post Processor registry. */
	private final IPostProcessor.Descriptor.Registry<String> postProcessorRegistry;

	/**
	 * Constructor.
	 * 
	 * @param enginePreferences
	 *            {@link EMFCompareBuilderConfigurator#enginePreferences}
	 * @param matchEngineFactoryRegistry
	 *            {@link IMatchEngine.Factory.Registry} that holds Match Engine factories.
	 * @param postProcessorRegistry
	 *            {@link IPostProcessor.Descriptor.Registry} that holds post processors.
	 */
	public EMFCompareBuilderConfigurator(IEclipsePreferences enginePreferences,
			IMatchEngine.Factory.Registry matchEngineFactoryRegistry,
			IPostProcessor.Descriptor.Registry<String> postProcessorRegistry) {
		Preconditions.checkNotNull(enginePreferences);
		this.enginePreferences = enginePreferences;
		this.matchEngineFactoryRegistry = matchEngineFactoryRegistry;
		this.postProcessorRegistry = postProcessorRegistry;
	}

	/**
	 * Get EMFCompareBuilderConfigurator with EMF Compare default values. Get the default preference store,
	 * the default {@link IMatchEngine.Factory.Registry} and the default
	 * {@link IPostProcessor.Descriptor.Registry}.
	 * 
	 * @return Default EMFCompareBuilderConfigurator;
	 */
	public static EMFCompareBuilderConfigurator createDefault() {
		return new EMFCompareBuilderConfigurator(EMFCompareRCPPlugin.getDefault().getEMFComparePreferences(),
				EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryRegistry(), EMFCompareRCPPlugin
						.getDefault().getPostProcessorRegistry());
	}

	/**
	 * Get the Diff engine.
	 * 
	 * @return {@link IDiffEngine} that has been set by the user (by preferences) or the most ranked one.This
	 *         method use eclipse default preference store to retrieve the engine
	 */
	private IDiffEngine getDiffEngine() {
		return ItemUtil.getItem(EMFCompareRCPPlugin.getDefault().getDiffEngineDescriptorRegistry(),
				EMFComparePreferences.DIFF_ENGINES, enginePreferences);
	}

	/**
	 * Get the Equivalences engine.
	 * 
	 * @return {@link IEquiEngine} that has been set by the user (by preferences) or the most ranked one.This
	 *         method use eclipse default preference store to retrieve the engine
	 */
	private IEquiEngine getEquiEngine() {
		return ItemUtil.getItem(EMFCompareRCPPlugin.getDefault().getEquiEngineDescriptorRegistry(),
				EMFComparePreferences.EQUI_ENGINES, enginePreferences);
	}

	/**
	 * Get the Requirements engine.
	 * 
	 * @return {@link IReqEngine} that has been set by the user (by preferences) or the most ranked one.This
	 *         method use eclipse default preference store to retrieve the engine
	 */
	private IReqEngine getReqEngine() {
		return ItemUtil.getItem(EMFCompareRCPPlugin.getDefault().getReqEngineDescriptorRegistry(),
				EMFComparePreferences.REQ_ENGINES, enginePreferences);
	}

	/**
	 * Get the Conflict Detector.
	 * 
	 * @return {@link IConflictDetector} that has been set by the user (by preferences) or the most ranked
	 *         one.This method use eclipse default preference store to retrieve the engine
	 */
	private IConflictDetector getConflictDetector() {
		return ItemUtil.getItem(EMFCompareRCPPlugin.getDefault().getConflictDetectorDescriptorRegistry(),
				EMFComparePreferences.CONFLICTS_DETECTOR, enginePreferences);
	}

	/**
	 * {@inheritDoc}
	 */
	public void configure(Builder builder) {
		if (matchEngineFactoryRegistry != null) {
			builder.setMatchEngineFactoryRegistry(matchEngineFactoryRegistry);
		}
		if (postProcessorRegistry != null) {
			builder.setPostProcessorRegistry(postProcessorRegistry);
		}

		IDiffEngine diffEngine = getDiffEngine();
		if (diffEngine != null) {
			builder.setDiffEngine(diffEngine);
		}
		IConflictDetector conflictDetector = getConflictDetector();
		if (conflictDetector != null) {
			builder.setConflictDetector(conflictDetector);
		}
		IReqEngine reqEngine = getReqEngine();
		if (reqEngine != null) {
			builder.setRequirementEngine(reqEngine);
		}
		IEquiEngine equiEngine = getEquiEngine();
		if (equiEngine != null) {
			builder.setEquivalenceEngine(equiEngine);
		}
	}

}
