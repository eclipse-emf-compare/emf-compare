/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.generic.DiffMaker;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.statistic.DifferencesServices;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * This application will try and launch an headless model comparison.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class ExampleLauncher {
	private ExampleLauncher() {
		// prevents instantiation
	}

	/**
	 * Loads a model from an {@link org.eclipse.emf.common.util.URI URI} in a given {@link ResourceSet}.
	 * 
	 * @param file
	 *            {@link java.io.File File} containing the model to be loaded.
	 * @param resourceSet
	 *            The {@link ResourceSet} to load the model in.
	 * @return The model loaded from the file.
	 * @throws IOException
	 *             If the given file does not exist.
	 */
	@SuppressWarnings("unchecked")
	public static EObject load(File file, ResourceSet resourceSet) throws IOException {
		final URI modelURI = URI.createFileURI(file.getPath());
		EObject result = null;

		String fileExtension = modelURI.fileExtension();
		if (fileExtension == null || fileExtension.length() == 0) {
			fileExtension = Resource.Factory.Registry.DEFAULT_EXTENSION;
		}

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Object resourceFactory = reg.getExtensionToFactoryMap().get(fileExtension);
		if (resourceFactory != null) {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(fileExtension, resourceFactory);
		} else {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(fileExtension, new XMIResourceFactoryImpl());
		}

		final Resource modelResource = resourceSet.createResource(modelURI);
		modelResource.load(Collections.EMPTY_MAP);
		if (modelResource.getContents().size() > 0)
			result = (EObject)modelResource.getContents().get(0);
		return result;
	}

	/**
	 * Launcher of this application.
	 * 
	 * @param args
	 *            Arguments of the launch.
	 */
	public static void main(String[] args) {
		if (args.length == 2 && new File(args[0]).canRead() && new File(args[1]).canRead()) {
			final ResourceSet resourceSet = new ResourceSetImpl();
			try {
				final EObject model1 = load(new File(args[0]), resourceSet);
				final EObject model2 = load(new File(args[1]), resourceSet);
				final MatchModel match = new DifferencesServices().modelMatch(model1, model2, new NullProgressMonitor());
				final DiffModel diff = new DiffMaker().doDiff(match);
				
				try {
					System.out.println(ModelUtils.serialize(match));
					System.out.println(ModelUtils.serialize(diff));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				// cannot be thrown
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("usage : Launcher <Model1> <Model2>"); //$NON-NLS-1$
		}
	}
}
