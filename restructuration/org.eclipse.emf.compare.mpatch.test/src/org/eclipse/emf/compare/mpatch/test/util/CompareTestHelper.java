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
package org.eclipse.emf.compare.mpatch.test.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchResolver;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util.TransformationLauncher;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;

/**
 * A helper class for loading models and creating {@link MPatchModel}s.
 * 
 * No test logic in here!
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class CompareTestHelper {

	/**
	 * Loads the file from the uri first from the test plugin in the workspace, and if that fails from the test plugin.
	 * Use the given resource set to load the models; this gives the caller the freedom to define the resourceset of the
	 * models.
	 */
	public static List<EObject> loadModel(String uriString, ResourceSet resourceSet) {
		// is uri already valid?
		URI uri = URI.createURI(uriString);
		if (!URIConverter.INSTANCE.exists(uri, null)) {

			// try workspace
			uri = URI.createURI(TestConstants.PREFIX_WORKSPACE + uriString);
			if (!URIConverter.INSTANCE.exists(uri, null)) {

				// try plugins
				uri = URI.createURI(TestConstants.PREFIX_PLUGIN + uriString);
				assertTrue("Cannot find file: " + uriString, URIConverter.INSTANCE.exists(uri, null));
			}
		}
		final Resource resource = resourceSet.getResource(uri, true);
		return resource.getContents();
	}

	/**
	 * Compare two versions of a model with EMF Compare.
	 * 
	 * @param leftUri
	 *            The URI of the changed version.
	 * @param rightUri
	 *            The URI of the unchanged version.
	 * @return The calculated emfdiff.
	 */
	public static ComparisonSnapshot getEmfdiffFromEmfCompare(String leftUri, String rightUri) {
		// Loading models
		ResourceSet resourceSet = new ResourceSetImpl();
		final EObject leftModel = loadModel(leftUri, resourceSet).get(0);
		final EObject rightModel = loadModel(rightUri, resourceSet).get(0);

		// delegate call
		return getEmfdiffFromEmfCompare(leftModel, rightModel);
	}

	/**
	 * Compare two versions of a model with EMF Compare.
	 * 
	 * @param leftModel
	 *            The changed version.
	 * @param rightModel
	 *            The unchanged version.
	 * @return The calculated emfdiff.
	 */
	public static ComparisonSnapshot getEmfdiffFromEmfCompare(EObject leftModel, EObject rightModel) {
		ComparisonResourceSnapshot emfdiff = CommonUtils.createEmfdiff(leftModel, rightModel, true);
		checkToString(emfdiff);
		return emfdiff;
	}

	/**
	 * An emfdiff2mpatch transformation throws an exception if toString() of an object fails. (That is/was the case for
	 * UpdateReference and UpdateAttribute sometimes!) So lets explicitely check that here!
	 */
	private static void checkToString(EObject eObject) {
		TreeIterator<EObject> iterator = eObject.eAllContents();
		while (iterator.hasNext()) {
			EObject next = iterator.next();
			try {
				next.toString();
			} catch (Exception e) {
				fail("toString() failed on: " + next);
			}
		}
	}

	/**
	 * Create an {@link MPatchModel}.
	 * 
	 * @param leftUri
	 *            Changed version of the test model.
	 * @param rightUri
	 *            Unchanged version of the test model.
	 * @param symrefCreator
	 *            The symbolic reference creator.
	 * @param descriptorCreator
	 *            The model descriptor creator.
	 * @return An {@link MPatchModel} created with the given creators.
	 */
	public static MPatchModel getMPatchFromUris(String leftUri, String rightUri,
			ISymbolicReferenceCreator symrefCreator, IModelDescriptorCreator descriptorCreator) {
		try {
			final ComparisonSnapshot emfdiff = getEmfdiffFromEmfCompare(leftUri, rightUri);
			final MPatchModel mpatch = TransformationLauncher
					.transform(emfdiff, null, symrefCreator, descriptorCreator);

			final String diagnostic = validateMPatch(mpatch);
			assertNull("Transformation with symrefCreator: " + symrefCreator.getLabel() + " and descriptorCreator: "
					+ descriptorCreator.getLabel() + " failed: " + diagnostic, diagnostic);

			return mpatch;
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Use EMF validator to check whether the diff is correct in terms of the constraints in its meta model.
	 * 
	 * @param maptch
	 *            MPatch.
	 * @return A string containing all wrong elements or <code>null</code>, if the diff is valid.
	 */
	public static String validateMPatch(MPatchModel maptch) {
		// we assume that we got exactly _one_ output model here
		final Diagnostic emfDiagnostic = Diagnostician.INSTANCE.validate(maptch);

		// perform emf validation
		if (emfDiagnostic.getSeverity() == Diagnostic.ERROR || emfDiagnostic.getSeverity() == Diagnostic.WARNING) {
			String message = emfDiagnostic.getMessage() + "\n";

			for (Diagnostic childDiagnostic : emfDiagnostic.getChildren()) {
				switch (childDiagnostic.getSeverity()) {
				case Diagnostic.ERROR:
				case Diagnostic.WARNING:
					message += "\t" + childDiagnostic.getMessage() + "\n";
				}
			}
			return "Transformation result does not validate successfully:\n" + message;
		}
		return null;
	}

	/**
	 * Resolve the MPatch to the model.
	 * 
	 * @param mpatch
	 *            An MPatch.
	 * @param applyModel
	 *            A model for which the MPatch should be resolved.
	 * @param info
	 *            Some info String for assert messages.
	 * @return The resolution of symbolic references.
	 */
	public static ResolvedSymbolicReferences resolveReferences(MPatchModel mpatch, EObject applyModel, String info) {
		final ResolvedSymbolicReferences resolved = MPatchResolver.resolveSymbolicReferences(mpatch, applyModel,
				ResolvedSymbolicReferences.RESOLVE_UNCHANGED, true);
		final List<IndepChange> invalidResolution = MPatchValidator.validateResolutions(resolved, true);
		assertTrue("The following changes did not resolve correctly (" + info + "): " + invalidResolution,
				invalidResolution.isEmpty());
		return resolved;
	}

}
