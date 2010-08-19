package org.eclipse.emf.compare.tests.merge;

import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class TestContainmentRemove extends TestCase {

	private EObject leftModel;

	private EObject expectedModel;

	private EObject rightModel;

	@Override
	public void setUp() throws Exception {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage p = factory.createEPackage();
		p.setName("TestPackage1");
		EClass class0 = factory.createEClass();
		class0.setName("Class0");

		leftModel = EcoreUtil.copy(p);
		URI leftURI = URI.createURI("leftmodel.ecore");
		ModelUtils.attachResource(leftURI, leftModel);

		expectedModel = EcoreUtil.copy(leftModel);
		URI expectedURI = URI.createURI("expectedmodel.ecore");
		ModelUtils.attachResource(expectedURI, expectedModel);

		p.getEClassifiers().add(class0);
		rightModel = EcoreUtil.copy(p);
		URI rightURI = URI.createURI("rightmodel.ecore");
		ModelUtils.attachResource(rightURI, rightModel);

	}

	public void testClassifierOrder() throws Exception {
		Map<String, Object> options = Collections.emptyMap();

		MatchModel match = MatchService.doMatch(leftModel, rightModel, options);
		System.out.println("Match model:");
		System.out.println(ModelUtils.serialize(match));

		DiffModel diff = DiffService.doDiff(match);
		EList<DiffElement> differences = diff.getDifferences();

		System.out.println("Diffs: ");
		for (DiffElement diffElement : differences) {
			System.out.println(diffElement);
		}

		EPackage leftPackage = (EPackage)leftModel;
		EPackage rightPackage = (EPackage)rightModel;

		assertNotNull("Left eFactoryInstance is not null ", leftPackage.getEFactoryInstance());
		assertNotNull("Right eFactoryInstance is not null ", rightPackage.getEFactoryInstance());

		MergeService.merge(differences, true);

		assertNotNull("Left eFactoryInstance is not null ", leftPackage.getEFactoryInstance());
		assertNotNull("Right eFactoryInstance is not null ", rightPackage.getEFactoryInstance());

		System.out.println("Expected :\n" + ModelUtils.serialize(expectedModel));
		System.out.println("Actual   :\n" + ModelUtils.serialize(rightModel));

		boolean mergeOK = EcoreUtil.equals(expectedModel, rightModel);

		if (false == mergeOK) {
			fail(" Merge failed ");
		}
	}
}
