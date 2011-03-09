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
package org.eclipse.emf.compare.mpatch.extension;

/**
 * Implementation of this interface provide the main functionality of applying an {@link MPatchModel} to an emf
 * model.<br>
 * <br>
 * The main process is explained in the following example and implemented by {@link ApplyWizard}:
 * <ol>
 * <li>A file <code>diff.mpatch</code> contains an mpatch for models of type 'mymodel'.
 * <li>An example model <code>test.mymodel</code> contains an instance of that type of models.
 * <li>The wizard takes both files as input and tries to resolve all symbolic references from the
 * <code>diff.mpatch</code> in <code>test.mymodel</code>.
 * <li>Then it asks the user to store the results in an EMF Compare based diff, e.g. <code>diff.emfdiff</code>.
 * <li>Afterwards it asks the user to store the resulting model in a new file, e.g. <code>test2.mymodel</code>.
 * <li>In the end, it opens the EMF Compare GUI to revise the application of the diff.
 * </ol>
 * 
 * The creation of the changed model in step 5 is covered by this interface.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public interface IMPatchApplication {

	/**
	 * Extension ID.
	 */
	String EXTENSION_ID = "org.eclipse.emf.compare.mpatch.apply";

	/**
	 * Apply a diff to a given model, having the symbolic references resolved in the given binding.
	 * 
	 * <b>Important note:</b> The implementation must fill {@link ResolvedSymbolicReferences#resolutionApplied()}!
	 * 
	 * <i>Please note that {@link ResolvedSymbolicReferences#direction()} defines the direction of difference
	 * application:</i>
	 * <ul>
	 * <li>{@link ResolvedSymbolicReferences#RESOLVE_UNCHANGED}: apply the changes from unchanged to changed, i.e. the
	 * 'forward' way.
	 * <li>{@link ResolvedSymbolicReferences#RESOLVE_CHANGED}: apply the changes from changed to unchanged, i.e. the
	 * 'backward' way. Thus, an {@link IndepAddElementChange} will be treated as an {@link IndepRemoveElementChange},
	 * etc.</li>
	 * </ul>
	 * 
	 * @param resolvedElements
	 *            A resolution of symbolic references, also referring to the model to which the differences should be
	 *            applied and the mpatch.
	 * @param storeBinding
	 *            If set to true, {@link ResolvedSymbolicReferences#getMPatchModelBinding()} will be filled with
	 *            {@link ChangeBinding}s which link the applied differences to the particular model elements.
	 * 
	 * @return Summary of what was successfully and what was not successfully applied.
	 */
	MPatchApplicationResult applyMPatch(final ResolvedSymbolicReferences resolvedElements,
			final boolean storeBinding);

	/**
	 * The label.
	 * 
	 * @return A label for this difference applier.
	 */
	String getLabel();

}
