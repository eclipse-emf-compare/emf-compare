package org.eclipse.emf.compare.tests.merge;

import java.util.Collections;
import java.util.List;
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
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class AttributeOrderTest extends TestCase {
	private EObject leftModel;

	private EObject expectedModel;

	private EObject rightModel;

	@Override
	public void setUp() throws Exception {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
				Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

		// We create a dynamic instance with attribute "att1" whose type is
		// EString and multiplicity = -1. By default it is unique, I think
		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage p = factory.createEPackage();
		p.setName("TestPackage1");

		EClass class0 = factory.createEClass();
		class0.setName("Class0");
		p.getEClassifiers().add(class0);

		EAttribute att = factory.createEAttribute();
		att.setName("att1");
		att.setEType(EcorePackage.eINSTANCE.getEString());
		att.setLowerBound(0);
		att.setUpperBound(-1);
		class0.getEStructuralFeatures().add(att);

		EObject instance = p.getEFactoryInstance().create(class0);
		List<String> attVal = (List<String>)instance.eGet(att);
		attVal.add("item1");
		attVal.add("item2");
		attVal.add("item3");

		leftModel = EcoreUtil.copy(instance);
		URI leftURI = URI.createURI("leftmodel.xmi");
		ModelUtils.attachResource(leftURI, leftModel);

		expectedModel = EcoreUtil.copy(leftModel);
		URI expectedURI = URI.createURI("expectedmodel.xmi");
		ModelUtils.attachResource(expectedURI, expectedModel);

		attVal.remove("item2");
		rightModel = EcoreUtil.copy(instance);
		URI rightURI = URI.createURI("rightmodel.xmi");
		ModelUtils.attachResource(rightURI, rightModel);
	}

	public void testAttributeOrder() throws Exception {
		Map<String, Object> options = Collections.emptyMap();
		MatchModel match = MatchService.doMatch(leftModel, rightModel, options);
		DiffModel diff = DiffService.doDiff(match);
		EList<DiffElement> differences = diff.getDifferences();
		MergeService.merge(differences, true);

		System.out.println("Expected :\n" + ModelUtils.serialize(expectedModel));
		System.out.println("Actual   :\n" + ModelUtils.serialize(rightModel));

		boolean mergeOK = EcoreUtil.equals(expectedModel, rightModel);

		if (false == mergeOK) {
			fail(" Merge failed ");
		}
	}
}
