/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.merge.BatchMerger;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/** Checks the published core without an Eclipse application or Tycho classpath. */
class MavenConsumerSmokeTest {
	public static void main(String[] args) throws Exception {
		XMIResourceImpl left = new XMIResourceImpl(URI.createURI("model.ecore"));
		EClass model = EcoreFactory.eINSTANCE.createEClass();
		model.setName("Example");
		left.getContents().add(model);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		left.save(bytes, Collections.emptyMap());

		XMIResourceImpl right = new XMIResourceImpl(left.getURI());
		right.load(new ByteArrayInputStream(bytes.toByteArray()), Collections.emptyMap());
		((EClass)right.getContents().get(0)).setAbstract(true);

		Comparison comparison = EMFCompare.builder().build()
				.compare(new DefaultComparisonScope(left, right, null));
		assert comparison.getDifferences().size() == 1 : "Expected one change";
		new BatchMerger(IMerger.RegistryImpl.createStandaloneInstance())
				.copyAllRightToLeft(comparison.getDifferences(), new BasicMonitor());
		assert model.isAbstract() : "The change was not merged";
		assert EMFCompare.builder().build().compare(new DefaultComparisonScope(left, right, null))
				.getDifferences().isEmpty() : "Models still differ after merge";
		System.out.println("Maven consumer comparison and merge passed.");
	}
}
