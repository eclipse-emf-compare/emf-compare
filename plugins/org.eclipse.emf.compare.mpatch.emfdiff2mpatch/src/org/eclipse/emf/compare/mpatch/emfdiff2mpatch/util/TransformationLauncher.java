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
package org.eclipse.emf.compare.mpatch.emfdiff2mpatch.util;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.Emfdiff2mpatchActivator;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.impl.Emfdiff2Mpatch;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.lib.MPatchLibraryComponents;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ExecutionStackTraceElement;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.WriterLog;

/**
 * Public API for transforming and emfdiff into an {@link MPatchModel}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public final class TransformationLauncher {

	/** The qvto transformation specification file. */
	private static final String TRANSFORMATION_SPECIFICATION = "/transforms/emfdiff2mpatch.qvto";

	/**
	 * Start a transformation from an emfdiff to mpatch.
	 * 
	 * The EMF Compare diff must conform to the diff meta model with the URI
	 * <code>http://www.eclipse.org/emf/compare/diff/1.1</code>, i.e. an object or a set of objects of type
	 * {@link ComparisonSnapshot} are required.
	 * 
	 * The result is an mpatch conforming to the meta model with the URI
	 * <code>http://www.eclipse.org/emf/compare/mpatch/1.0</code>, i.e. an object of type {@link MPatchModel} is
	 * returned.
	 * 
	 * @param emfdiff
	 *            An EMF Compare diff comparison snapshot.
	 * @param output
	 *            The console output of the qvto transformation engine (ignored if <code>null</code>).
	 * @param symbolicReferenceCreator
	 *            The symbolic reference creator which will be used in the transformation.
	 * @param modelDescriptorCreator
	 *            The model descriptor creator which will be used in the transformation.
	 * @return An mpatch.
	 * @throws Exception
	 *             If the transformation was not successful.
	 */
	public static MPatchModel transform(ComparisonSnapshot emfdiff, StringBuffer output,
			ISymbolicReferenceCreator symbolicReferenceCreator, IModelDescriptorCreator modelDescriptorCreator)
			throws Exception {

		// set the library components prior to transformation
		MPatchLibraryComponents.setModelDescriptorCreator(modelDescriptorCreator);
		MPatchLibraryComponents.setSymbolicReferenceCreator(symbolicReferenceCreator);

		// set resources of the models to compare for the symref creator so it knows what are external elements
		final Set<Resource> modelResources = getModelResourcesFromEmfdiff((ComparisonSnapshot) emfdiff);
		symbolicReferenceCreator.setNonExternalResources(modelResources);

		/*
		 * Because of the critical dependency to qvto we should rather use a transformation that is coded in pure Java
		 * :-/
		 */
		//return transformQVTo(emfdiff, output);
		return transformJava(emfdiff, output);
	}

	/**
	 * Realization of the transformation in pure Java + EMF.
	 */
	public static MPatchModel transformJava(ComparisonSnapshot emfdiff, StringBuffer output) {
		return new Emfdiff2Mpatch().transform(emfdiff, output);
	}

	/**
	 * Realization of the transformation in QVT Operational Mappings.
	 */
	public static MPatchModel transformQVTo(ComparisonSnapshot emfdiff, StringBuffer output) throws Exception {
		// get the qvto transformation helper
		final URI transformationSpecification = URI.createPlatformPluginURI(Emfdiff2mpatchActivator.PLUGIN_ID
				+ TRANSFORMATION_SPECIFICATION, true);
		final TransformationExecutor transformationExecuter = new TransformationExecutor(transformationSpecification);

		// prepare input and output
		final ModelExtent inputModels = new BasicModelExtent(Collections.singletonList(emfdiff));
		final ModelExtent outputModels = new BasicModelExtent();
		final ExecutionContextImpl context = new ExecutionContextImpl();

		// set our own logger to catch the log output!
		final StringWriter log = new StringWriter();
		context.setLog(new WriterLog(log));

		// perform transformation
		final ExecutionDiagnostic diagnostic = transformationExecuter.execute(context, inputModels, outputModels);

		// analyze results
		if (diagnostic.getSeverity() == Diagnostic.ERROR || diagnostic.getCode() == ExecutionDiagnostic.FATAL_ASSERTION) {
			String message = "";
			for (final ExecutionStackTraceElement element : diagnostic.getStackTrace()) {
				message += element.getModuleName() + "." + element.getOperationName() + " (" + element.getUnitName()
						+ ":" + element.getLineNumber() + ")\n";
			}
			if (message.length() > 0) {
				message = "\nTransformation trace:\n" + message;
			}
			throw new Exception("Transformation was not successful: " + diagnostic.getMessage() + message + "\n"
					+ log.toString(), diagnostic.getException());
		}

		if (output != null) {
			output.append(diagnostic.getMessage());
			output.append(log.toString());
		}

		final List<EObject> contents = outputModels.getContents();
		if (contents != null && contents.size() == 1)
			return (MPatchModel) contents.get(0);
		throw new Exception("QVTo Transformation did not produce one single valid output model, but: " + contents);
	}

	/**
	 * Helper method to extract resource links from emfdiff.
	 * 
	 * @param snapshot
	 *            An emfdiff.
	 * @return The set of resources referenced by this emfdiff.
	 */
	private static Set<Resource> getModelResourcesFromEmfdiff(ComparisonSnapshot snapshot) {
		final Set<Resource> modelResources = new HashSet<Resource>();
		if (snapshot instanceof ComparisonResourceSnapshot) {
			final ComparisonResourceSnapshot resourceSnapshot = (ComparisonResourceSnapshot) snapshot;
			addModelResourcesFromDiffModel(resourceSnapshot.getDiff(), modelResources);
		} else if (snapshot instanceof ComparisonResourceSetSnapshot) {
			final ComparisonResourceSetSnapshot resourceSetSnapshot = (ComparisonResourceSetSnapshot) snapshot;
			for (DiffModel diffModel : resourceSetSnapshot.getDiffResourceSet().getDiffModels()) {
				addModelResourcesFromDiffModel(diffModel, modelResources);
			}
		}
		return modelResources;
	}

	/**
	 * Helper method to add resources of a {@link DiffModel} to an accumulator.
	 * 
	 * @param diff
	 *            A diffmodel.
	 * @param modelResources
	 *            Accumulator.
	 */
	private static void addModelResourcesFromDiffModel(DiffModel diff, final Set<Resource> modelResources) {
		for (EObject leftInModel : diff.getLeftRoots()) {
			if (leftInModel.eResource() != null)
				modelResources.add(leftInModel.eResource());
		}
		for (EObject rightInModel : diff.getRightRoots()) {
			if (rightInModel.eResource() != null)
				modelResources.add(rightInModel.eResource());
		}
	}

}
