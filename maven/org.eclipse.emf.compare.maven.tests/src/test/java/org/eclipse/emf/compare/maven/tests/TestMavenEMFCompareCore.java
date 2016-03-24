/*******************************************************************************
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.maven.tests;

import java.util.regex.Pattern;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.WeightProvider;
import org.eclipse.emf.compare.match.eobject.WeightProviderDescriptorRegistryImpl;
import org.eclipse.emf.compare.match.eobject.internal.WeightProviderDescriptorImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Test;

import junit.framework.Assert;

public class TestMavenEMFCompareCore {

	@Test
	public void test1() {
		Resource left = new XMIResourceImpl(URI.createFileURI("left"));
		EClass o1 = EcoreFactory.eINSTANCE.createEClass();
		o1.setName("LeftEobject");
		left.getContents().add(o1);
		Resource right = new XMIResourceImpl(URI.createFileURI("right"));
		EClass o2 = EcoreFactory.eINSTANCE.createEClass();
		o2.setName("RightEobject");
		right.getContents().add(o2);
		DefaultComparisonScope scope = new DefaultComparisonScope(left, right, null);
		IMatchEngine.Factory.Registry registry = MatchEngineFactoryRegistryImpl.createStandaloneInstance();
		WeightProvider.Descriptor.Registry weightProviderRegistry = new WeightProviderDescriptorRegistryImpl();
		MyWeightProvider weightProvider = new MyWeightProvider();
		WeightProviderDescriptorImpl descriptor = new WeightProviderDescriptorImpl(weightProvider, 100,
				Pattern.compile(".*"));
		weightProviderRegistry.put(weightProvider.getClass().getName(), descriptor);
		final MatchEngineFactoryImpl matchEngineFactory = new MatchEngineFactoryImpl(UseIdentifiers.NEVER, weightProviderRegistry);
		matchEngineFactory.setRanking(20); // default engine ranking is 10, must be higher to override.
		registry.add(matchEngineFactory);

		Comparison comparison = EMFCompare.builder().setMatchEngineFactoryRegistry(registry).build().compare(scope);
		Assert.assertNotNull(comparison);
	}
}
