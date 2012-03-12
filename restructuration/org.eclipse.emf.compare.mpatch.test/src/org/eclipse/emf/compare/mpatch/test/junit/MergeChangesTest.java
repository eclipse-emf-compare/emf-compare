/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.test.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.test.util.CommonTestOperations;
import org.eclipse.emf.compare.mpatch.test.util.CompareTestHelper;
import org.eclipse.emf.compare.mpatch.test.util.TestConstants;
import org.eclipse.emf.compare.mpatch.transform.util.GeneralizeTransformation;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the merging of changes. Each change type can be merged, thus, 9 test methods. After applying the merged changes
 * to the original unchanged model again, the result must again be the changed model!
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class MergeChangesTest {

	private static final String CONDITION_BASED_SYMREF_CREATOR = "Condition-based";

	/** The merged MPatch for EMF models. */
	private static Map<String, MPatchModel> mergedMPatchesEMF = new HashMap<String, MPatchModel>();

	/** The merged MPatch for eachonce models. */
	private static Map<String, MPatchModel> mergedMPatchesEachonce = new HashMap<String, MPatchModel>();

	/**
	 * Build the MPatch and merge changes before the actual tests are performed.
	 */
	@BeforeClass
	public static void prepare() {
		for (ISymbolicReferenceCreator symrefCreator : TestConstants.SYM_REF_CREATORS) {
			if (!symrefCreator.getLabel().equals(CONDITION_BASED_SYMREF_CREATOR))
				continue;
			for (IModelDescriptorCreator descriptorCreator : TestConstants.MODEL_DESCRIPTOR_CREATORS) {
				final String info = "Symref-creator: " + symrefCreator + "; descriptor-creator: " + descriptorCreator;

				// prepare MPatches
				final MPatchModel emfMPatch = prepareMPatch(symrefCreator, descriptorCreator,
						TestConstants.MERGE_EMF_URI1, TestConstants.MERGE_EMF_URI2, info);
				final MPatchModel eachonceMPatch = prepareMPatch(symrefCreator, descriptorCreator,
						TestConstants.MERGE_EACHONCE_URI1, TestConstants.MERGE_EACHONCE_URI2, info);

				// storing them
				mergedMPatchesEMF.put(info, emfMPatch);
				mergedMPatchesEachonce.put(info, eachonceMPatch);
			}
		}
	}

	/**
	 * Create an MPatch for the two given uris and perform the merge transformation.
	 * 
	 * @param symrefCreator
	 *            The symref creator to use.
	 * @param descriptorCreator
	 *            The model descriptor creator to use.
	 * @param uri1
	 *            The UNCHANGED version of a model.
	 * @param uri2
	 *            The CHANGED version of a model.
	 * @param info
	 *            Additional information for the assert messages.
	 * @return The MPatch that was already merged.
	 */
	private static MPatchModel prepareMPatch(ISymbolicReferenceCreator symrefCreator,
			IModelDescriptorCreator descriptorCreator, String uri1, String uri2, String info) {

		// creating mpatches
		final MPatchModel mPatch = CompareTestHelper.getMPatchFromUris(uri2, uri1, symrefCreator, descriptorCreator);

		// checking them
		assertNotNull("Could not create MPatch (" + info + ") for " + uri1 + " and " + uri2, mPatch);

		// performing common transformations
		CommonTestOperations.doTransformations(mPatch, null, info);

		// merging!!!
		GeneralizeTransformation.mergeChanges(mPatch);
		return mPatch;
	}

	/**
	 * Test whether added elements can be merged.
	 */
	@Test
	public void testMergedMPatchApplication() {
		checkMPatchApplication(mergedMPatchesEMF, TestConstants.MERGE_EMF_URI1, TestConstants.MERGE_EMF_URI2, "EMF-model");
		checkMPatchApplication(mergedMPatchesEachonce, TestConstants.MERGE_EACHONCE_URI1, TestConstants.MERGE_EACHONCE_URI2, "Eachonce-model");
	}

	private void checkMPatchApplication(Map<String, MPatchModel> mPatches, String uri1, String uri2, String moreInfo) {
		for (String info : mPatches.keySet()) {
			final MPatchModel mPatch = mPatches.get(info);
			info += "; " + moreInfo;
		
			// load models
			final EObject model = CompareTestHelper.loadModel(uri1, new ResourceSetImpl()).get(0);
			final EObject expectedModel = CompareTestHelper.loadModel(uri2, new ResourceSetImpl()).get(0);
			
			final ResolvedSymbolicReferences resolved = CommonTestOperations.resolveAndValidate(mPatch, model, null, info);
			final EObject newModel = CommonTestOperations.applyMPatchToModel(mPatch, model, resolved, null, info);
			CommonTestOperations.compareChangedAndUnchangedModels(model, newModel, null, info);
			CommonTestOperations.checkAppliedModel(model, expectedModel, info);
			CommonTestOperations.checkBinding(resolved.getMPatchModelBinding(), info);
		}
	}
	
	/**
	 * Test whether added element changes were successfully merged.
	 */
	@Test
	public void testAddedElementMerge() {
		checkNumberOfChanges(mergedMPatchesEMF, MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE, 1);
	}

	/**
	 * Test whether removed element changes were successfully merged.
	 */
	@Test
	public void testRemovedElementMerge() {
		checkNumberOfChanges(mergedMPatchesEMF, MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE, 1);
	}

	/**
	 * Test whether moved element changes were successfully merged.
	 */
	@Test
	public void testMovedElementMerge() {
		// we got 2 merges here in the test model!
		checkNumberOfChanges(mergedMPatchesEMF, MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE, 2);
	}

	/**
	 * Test whether add attribute changes were successfully merged.
	 */
	@Test
	public void testAddAttributeMerge() {
		checkNumberOfChanges(mergedMPatchesEachonce, MPatchPackage.Literals.INDEP_ADD_ATTRIBUTE_CHANGE, 1);
	}

	/**
	 * Test whether remove attribute changes were successfully merged.
	 */
	@Test
	public void testRemoveAttributeMerge() {
		checkNumberOfChanges(mergedMPatchesEachonce, MPatchPackage.Literals.INDEP_REMOVE_ATTRIBUTE_CHANGE, 1);
	}

	/**
	 * Test whether update attribute changes were successfully merged.
	 */
	@Test
	public void testUpdateAttributeMerge() {
		checkNumberOfChanges(mergedMPatchesEachonce, MPatchPackage.Literals.INDEP_UPDATE_ATTRIBUTE_CHANGE, 1);
	}

	/**
	 * Test whether add reference changes were successfully merged.
	 */
	@Test
	public void testAddReferenceMerge() {
		checkNumberOfChanges(mergedMPatchesEachonce, MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE, 1);
	}

	/**
	 * Test whether remove reference changes were successfully merged.
	 */
	@Test
	public void testRemoveReferenceMerge() {
		checkNumberOfChanges(mergedMPatchesEachonce, MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE, 1);
	}

	/**
	 * Test whether update reference changes were successfully merged.
	 */
	@Test
	public void testUpdateReferenceMerge() {
		// we got 3 merges here in the test model!
		checkNumberOfChanges(mergedMPatchesEachonce, MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE, 3);
	}

	/**
	 * Check the number of changes in all given mPatches.
	 * 
	 * @param mPatches
	 *            The MPatches to check.
	 * @param changeType
	 *            The type that should be checked.
	 * @param count
	 *            The expected number of changes of the specified type.
	 */
	private void checkNumberOfChanges(Map<String, MPatchModel> mPatches, EClass changeType, int count) {
		for (String info : mPatches.keySet()) {
			final MPatchModel mPatch = mPatches.get(info);
			final List<EObject> addElementChanges = ExtEcoreUtils.collectTypedElements(mPatch.getChanges(), Collections
					.singleton(changeType), true);

			// remove those whose upper bound of corresponding reference is still 1.
			for (int i = addElementChanges.size() - 1; i >= 0; i--) {
				final IndepChange change = (IndepChange) addElementChanges.get(i);
				if (change.getCorrespondingElement().getUpperBound() == 1)
					addElementChanges.remove(i);
			}

			// we just require one merged change here!
			assertEquals("Wrong number of merged changes: " + changeType.getName() + "! (" + info + ")", count,
					addElementChanges.size());
		}
	}
}
