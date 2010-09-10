/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.test.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.text.Format;
import java.util.List;

import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.TransformationLauncher;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.PerformanceTimes;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Test;


/**
 * This test case is about performance: by setting some parameters, the number of differences and the size of the model
 * can be varied.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class PerformanceTest {

	/** The smallest test case including this many classes (and 6 times as many changes), e.g. 50. */
	private static final int lowerBound = 2;

	/** The biggest test case including this many classes (and 6 times as many changes), e.g. 100. */
	private static final int upperBound = 100;

	/** The step with which the test size is incremented in each iteration, e.g. 10. */
	private static final int step = 49;

	/** Just some legend for the test results printed to <code>System.out</code>. */
	private static final String header = "============================================================================================================\n"
			+ "Legend of the performance test results:\n"
			+ "   Diff's         - The number of differences in this test run\n"
			+ "   Build          - Time for building test models\n"
			+ "   Emfdiff        - Time for comparing these model with EMF Compare\n"
			+ "   MPatch         - Time for QVTO transformation to create mpatch\n"
			+ "   Groups         - Time for creating groups in mpatch\n"
			+ "   Deps           - Time for creating dependency graph in mpatch\n"
			+ "   Refs           - Time for creating inernal symbolic references\n"
			+ "   Resolve        - Time for resolving symbolic references for target model\n"
			+ "   Copy           - Time for copying the target model\n"
			+ "   Apply          - Time for applying mpatch to target model\n"
			+ "   Changes        - Time for comparing unchanged with changed model with EMF Compare\n"
			+ "   Check          - Time for checking whether all diffs have been applied successfully\n"
			+ "   Total          - Total time for this test run\n"
			+ "   Transformation - Parameters for the transformation, i.e. symref descriptor creators\n"
			+ "All times are given in ms (miliseconds).\n\n"
			+ " Diff's | Built | Emfdiff | MPatch | Groups | Deps | Refs | Resolve | Copy |  Apply | Changes | Check |  Total | Transformation\n"
			+ "--------+-------+---------+--------+--------+------+------+---------+------+--------+---------+-------+--------+----------------";

	/**
	 * Run the performance test with the parameters given in <code>lowerBound</code>, <code>upperBound</code>, and
	 * <code>step</code>.
	 */
	@Test
	public void testPerformance() {
		System.out.println(header);

		/*
		 *  test for the performance documentation in the paper:
		 */
		// System.out.println(prettyPrint(100, testPerformance(100, ExtensionManager.getAllSymbolicReferenceCreators()
		// .get("ID-based"), TestConstants.modelDescriptorCreators.iterator().next()), "id performance test"));
		// if("".isEmpty())return;

		// iterate from 50 to 500 classes in the model to find out the limit
		for (int i = lowerBound; i <= upperBound; i += step)
			for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS)
				for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS)
					System.out.println(prettyPrint(i * 6, testPerformance(i, symrefCreator, descriptorCreator),
							"descriptor: " + descriptorCreator.getLabel() + ", symref: " + symrefCreator.getLabel()));

	}

	private static String prettyPrint(int differences, PerformanceTimes times, String transformation) {
		return intToFixedWidthString(differences, 7) + " |" // Differences
				+ intToFixedWidthString(times.getBuilt(), 6) + " |" // Built
				+ intToFixedWidthString(times.getEmfdiff(), 8) + " |" // Emfdiff
				+ intToFixedWidthString(times.getMPatch(), 7) + " |" // MPatch
				+ intToFixedWidthString(times.getGroups(), 7) + " |" // Groups
				+ intToFixedWidthString(times.getDeps(), 5) + " |" // Deps
				+ intToFixedWidthString(times.getRefs(), 5) + " |" // Refs
				+ intToFixedWidthString(times.getResolve(), 8) + " |" // Resolve
				+ intToFixedWidthString(times.getCopy(), 5) + " |" // Copy
				+ intToFixedWidthString(times.getApply(), 7) + " |" // Apply
				+ intToFixedWidthString(times.getChanges(), 8) + " |" // Changes
				+ intToFixedWidthString(times.getCheck(), 6) + " |" // Check
				+ intToFixedWidthString(times.getTotal(), 7) + " | " // Total
				+ transformation; // details about the transformation
	}

	private static String intToFixedWidthString(long i, int strlen) {
		StringBuffer result = new StringBuffer();
		String str = String.valueOf(i);
		for (int j = 0; j < strlen - str.length(); j++)
			result.append(" ");
		result.append(str);
		return result.toString();
	}

	private static PerformanceTimes testPerformance(int size, ISymbolicReferenceCreator symrefCreator,
			IModelDescriptorCreator descriptorCreator) {

		final PerformanceTimes times = new PerformanceTimes(); // start timing
		final String info = "symrefCreator: " + symrefCreator.getLabel() + " and descriptorCreator: "
				+ descriptorCreator.getLabel();

		// build performance model!
		ResourceSet resourceSet = new ResourceSetImpl();
		EObject rightModel = buildUnchangedModel(TestConstants.PERFORMANCE_URI1, resourceSet, size);
		EObject leftModel = buildChangedModel(TestConstants.PERFORMANCE_URI2, resourceSet, size);
		times.setBuilt(); // models are built

//		 try { // store models without doing anything, e.g. to see test models
//		 rightModel.eResource().save(null);
//		 leftModel.eResource().save(null);
//		 return times;
//		 } catch (Exception e) {
//		 fail("Could not save models: " + e.getMessage());
//		 }

		// prepare models
		final List<ComparisonSnapshot> inModels = CompareTestHelper.getInModelsFromEmfCompare(leftModel, rightModel);
		times.setEmfdiff(); // emfdiff is built
		List<EObject> outModels;
		try {
			outModels = TransformationLauncher.transform(inModels, null, symrefCreator, descriptorCreator);
		} catch (Exception e) {
			fail("transformation failed (" + info + "): " + e.getMessage());
			return null;
		}
		final MPatchModel mpatch = (MPatchModel) outModels.get(0);
		times.setMPatch(); // mpatch is built
		assertNotNull("Preceeding transformation failed! " + info, mpatch);

		// get new resource to not conflict with original models!
		resourceSet = new ResourceSetImpl();
		resourceSet.getResources().add(rightModel.eResource());
		resourceSet.getResources().add(leftModel.eResource());

		// apply differences and validate the result against the leftModel!
		CommonTestOperations.createAndApplyMPatch(mpatch, rightModel, leftModel, times, info);

		return times;
	}

	/*
	 * BELOW: METHODS FOR BUILDING THE PERFORMANCE TEST MODEL
	 */

	private static Format fourFormat = new java.text.DecimalFormat("000");

	private static EObject buildChangedModel(String uriString, ResourceSet resourceSet, int size) {
		// get package of interest
		final EPackage pack = (EPackage) CompareTestHelper.loadModel(uriString, resourceSet).get(0);
		EPackage newPlace = null;
		for (EPackage p : pack.getESubpackages())
			if ("newPlace".equals(p.getName()))
				newPlace = p;
		assertNotNull("Cannot find package 'newPlace'", newPlace);

		// get some classifiers we need later
		EClass superType = (EClass) pack.getEClassifier("SuperType");
		EClassifier oldReferenceTarget = pack.getEClassifier("OldReferenceTarget");
		EClassifier newReferenceTarget = pack.getEClassifier("NewReferenceTarget");
		assertNotNull("Cannot find classifier 'SuperType'", superType);
		assertNotNull("Cannot find classifier 'OldReferenceTarget'", oldReferenceTarget);
		assertNotNull("Cannot find classifier 'NewReferenceTarget'", newReferenceTarget);

		// now lets create the test data
		for (int i = 0; i < size; i++) {

			// create a class
			EClass c = EcoreFactory.eINSTANCE.createEClass();
			c.setName("Class" + fourFormat.format(i));
			c.getESuperTypes().add(superType);

			// create an attribute
			EAttribute a = EcoreFactory.eINSTANCE.createEAttribute();
			a.setName("attribute");
			a.setEType(EcorePackage.Literals.ESTRING);

			// create a reference to newReferenceTarget
			EReference r = EcoreFactory.eINSTANCE.createEReference();
			r.setName("reference");
			r.setEType(newReferenceTarget);
			r.setUpperBound(-1);

			// add everthing properly
			c.getEStructuralFeatures().add(a);
			c.getEStructuralFeatures().add(r);
			newPlace.getEClassifiers().add(c);
		}
		return pack;
	}

	private static EObject buildUnchangedModel(String uriString, ResourceSet resourceSet, int size) {
		// get package of interest
		final EPackage pack = (EPackage) CompareTestHelper.loadModel(uriString, resourceSet).get(0);
		EPackage oldPlace = null;
		for (EPackage p : pack.getESubpackages())
			if ("oldPlace".equals(p.getName()))
				oldPlace = p;
		assertNotNull("Cannot find package 'oldPlace'", oldPlace);

		// get some classifiers we need later
		EClassifier superType = pack.getEClassifier("SuperType");
		EClassifier oldReferenceTarget = pack.getEClassifier("OldReferenceTarget");
		EClassifier newReferenceTarget = pack.getEClassifier("NewReferenceTarget");
		assertNotNull("Cannot find classifier 'SuperType'", superType);
		assertNotNull("Cannot find classifier 'OldReferenceTarget'", oldReferenceTarget);
		assertNotNull("Cannot find classifier 'NewReferenceTarget'", newReferenceTarget);

		// now lets create the test data
		for (int i = 0; i < size; i++) {

			// create a class
			EClass c = EcoreFactory.eINSTANCE.createEClass();
			c.setName("Class" + fourFormat.format(i));

			// create an operation with two parameters for the class
			EOperation o = EcoreFactory.eINSTANCE.createEOperation();
			o.setName("operation");
			o.setEType(newReferenceTarget);
			EParameter p1 = EcoreFactory.eINSTANCE.createEParameter();
			p1.setName("parameter1");
			p1.setEType(superType);
			EParameter p2 = EcoreFactory.eINSTANCE.createEParameter();
			p2.setName("parameter2");
			p2.setEType(superType);

			// create a reference to oldReferenceTarget
			EReference r = EcoreFactory.eINSTANCE.createEReference();
			r.setName("reference");
			r.setEType(oldReferenceTarget);

			// add everthing properly
			o.getEParameters().add(p1);
			o.getEParameters().add(p2);
			c.getEOperations().add(o);
			c.getEStructuralFeatures().add(r);
			oldPlace.getEClassifiers().add(c);
		}
		return pack;
	}

}
