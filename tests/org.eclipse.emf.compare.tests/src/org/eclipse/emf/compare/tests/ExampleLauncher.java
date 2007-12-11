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
import java.util.Calendar;
import java.util.Collections;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

/**
 * This application will try and launch an headless model comparison.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public final class ExampleLauncher {
	/**
	 * This class doesn't need to be instantiated.
	 */
	private ExampleLauncher() {
		// prevents instantiation
	}

	/**
	 * Launcher of this application.
	 * 
	 * @param args
	 *            Arguments of the launch.
	 */
	public static void main(String[] args) {
		if (args.length == 2 && new File(args[0]).canRead() && new File(args[1]).canRead()) {
			// Creates the resourceSet where we'll load the models
			final ResourceSet resourceSet = new ResourceSetImpl();
			try {
				// Loads the two models passed as arguments
				final EObject model1 = ModelUtils.load(new File(args[0]), resourceSet);
				final EObject model2 = ModelUtils.load(new File(args[1]), resourceSet);

				// Creates the match then the diff model for those two models
				final MatchModel match = MatchService.doMatch(model1, model2, new NullProgressMonitor(), Collections.<String, Object> emptyMap());
				final DiffModel diff = DiffService.doDiff(match, false);

				// Prints the results
				try {
					System.out.println(ModelUtils.serialize(match));
					System.out.println(ModelUtils.serialize(diff));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// System.out.println("saving diff as \"result.diff\"");
				// ModelUtils.save(diff, "result.diff");
				// System.out.println("saving match as \"result.match\"");
				// ModelUtils.save(match, "result.match");

				// Serializes the result as "result.emfdiff" in the directory this class has been called from.
				System.out.println("saving emfdiff as \"result.emfdiff\""); //$NON-NLS-1$
				final ModelInputSnapshot snapshot = DiffFactory.eINSTANCE.createModelInputSnapshot();
				snapshot.setDate(Calendar.getInstance().getTime());
				snapshot.setMatch(match);
				snapshot.setDiff(diff);
				ModelUtils.save(snapshot, "result.emfdiff"); //$NON-NLS-1$
			} catch (IOException e) {
				// shouldn't be thrown
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("usage : Launcher <Model1> <Model2>"); //$NON-NLS-1$
		}
	}
}
