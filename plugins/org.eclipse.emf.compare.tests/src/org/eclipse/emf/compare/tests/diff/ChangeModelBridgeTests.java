package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompareConfiguration;
import org.eclipse.emf.compare.EMFCompareMessages;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.diff.IDiffEngine;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.scope.DefaultComparisonScope;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Before;
import org.junit.Test;

public class ChangeModelBridgeTests {

	ChangeModelBridge bridge;

	EObject v1;

	EObject v2;

	ChangeRecorder recorder;

	@Before
	public void setUp() {
		bridge = new ChangeModelBridge();
		v1 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		new XMIResourceImpl(URI.createURI("http://ecore.ecore")).getContents().add(v1);
		v2 = EcoreUtil.copy(EcorePackage.eINSTANCE);
		new XMIResourceImpl(URI.createURI("http://ecore.ecore")).getContents().add(v2);
		recorder = new ChangeRecorder();
		recorder.setRecordingTransientFeatures(false);
		recorder.beginRecording(Lists.newArrayList(v2));
	}

	private String serialize(EObject root) throws IOException {
		if (root == null) {
			throw new NullPointerException(EMFCompareMessages.getString("ModelUtils.NullSaveRoot")); //$NON-NLS-1$
		}

		// Copies the root to avoid modifying it
		final EObject copyRoot = EcoreUtil.copy(root);
		attachResource(URI.createFileURI("resource.xml"), copyRoot); //$NON-NLS-1$
		final StringWriter writer = new StringWriter();
		final Map<String, String> options = Maps.newHashMap();
		options.put(XMLResource.OPTION_ENCODING, "utf-8");
		// Should not throw ClassCast since uri calls for an xml resource
		((XMLResource)copyRoot.eResource()).save(writer, options);
		final String result = writer.toString();
		writer.flush();
		return result;
	}

	public static Resource attachResource(URI resourceURI, EObject root) {
		if (root == null) {
			throw new NullPointerException(EMFCompareMessages.getString("ModelUtils.NullRoot")); //$NON-NLS-1$
		}

		final Resource newResource = new XMIResourceImpl(resourceURI);
		newResource.getContents().add(root);
		return newResource;
	}

	@Test
	public void rename() throws Exception {
		((EPackage)v2).setName("new name");
		assertEMFCompareFindTheDifferences();
	}

	@Test
	public void reorder() throws Exception {
		((EPackage)v2).getEClassifiers().move(3, 4);
		assertEMFCompareFindTheDifferences();
	}

	@Test
	public void remove() throws Exception {
		EcoreUtil.remove(((EPackage)v2).getEClassifiers().get(0));
		assertEMFCompareFindTheDifferences();
	}

	@Test
	public void delete() throws Exception {
		EcoreUtil.delete(((EPackage)v2).getEClassifiers().get(0));
		assertEMFCompareFindTheDifferences();
	}

	@Test
	public void addNew() throws Exception {
		EClassifier newClas = EcoreFactory.eINSTANCE.createEClass();
		newClas.setName("Added class");
		((EPackage)v2).getEClassifiers().add(newClas);
		assertEMFCompareFindTheDifferences();
	}

	protected void assertEMFCompareFindTheDifferences() throws IOException {
		final IMatchEngine matchEngine = new DefaultMatchEngine();
		Comparison comparison = matchEngine.match(new DefaultComparisonScope(v1, v2, null),
				EMFCompareConfiguration.builder().build());

		// TODO allow extension of the default diff engine
		final IDiffEngine diffEngine = new DefaultDiffEngine(bridge);
		diffEngine.diff(comparison);

		ChangeDescription inferedChanges = bridge.getChanges();
		ChangeDescription capturedChanges = recorder.summarize();
		capturedChanges.applyAndReverse();
		assertEquals(serialize(capturedChanges), serialize(inferedChanges));
	}
}
