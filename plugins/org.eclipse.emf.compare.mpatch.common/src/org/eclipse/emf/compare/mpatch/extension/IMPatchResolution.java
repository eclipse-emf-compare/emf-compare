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
package org.eclipse.emf.compare.mpatch.extension;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.swt.widgets.Composite;


/**
 * Implementations of this interface refine a given resolution of symbolic references (
 * {@link ResolvedSymbolicReferences}).
 * 
 * The interactive way of refining the resolution is GUI-based and uses the
 * {@link IMPatchResolution#refineResolution(ResolvedSymbolicReferences, IMPatchResolutionHost)} and
 * {@link IMPatchResolution#buildResolutionGUI(Composite, AdapterFactory)}. The idea is to first call
 * <code>buildResolutionGUI</code> in order to initialize the GUI. Then <code>refineResolution</code> will do the actual
 * refinement. {@link IMPatchResolutionHost} is a callback interface to notify the refinement.
 * 
 * The automated way of refining the resolution uses
 * {@link IMPatchResolution#refineResolution(ResolvedSymbolicReferences)}.
 * 
 * Implementations can and should use <code>org.eclipse.emf.compare.mpatch.apply.util.DiffValidator</code> to validate the
 * refinement.
 * 
 * The refinement is expected to be performed as follows:<br>
 * <ul>
 * <li>If an {@link IndepChange} is be ignored, it must be removed from
 * {@link ResolvedSymbolicReferences#getResolutionByChange()}.
 * <li>If the set of resolved model elements for a particular symbolic reference is refined, the collections in
 * {@link ResolvedSymbolicReferences#getResolutionByChange()} must be updated.
 * </ul>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public interface IMPatchResolution {

	/**
	 * Extension ID.
	 */
	String EXTENSION_ID = "org.eclipse.emf.compare.mpatch.resolve";

	/**
	 * The automated way of refining the resolution of symbolic references. This should not include user interaction and
	 * is not GUI-based. Thus, it is a synchronous call.
	 * 
	 * @param mapping
	 *            A resolution of symbolic references.
	 */
	void refineResolution(final ResolvedSymbolicReferences mapping);

	/**
	 * Build the GUI for a user-interactive way of refining the resolution of symbolic references.
	 * 
	 * @param container
	 *            The composite on which the GUI can be created.
	 * @param adapterFactory
	 *            An adapter factory for creating nicer labels for model elements.
	 */
	void buildResolutionGUI(final Composite container, final AdapterFactory adapterFactory);

	/**
	 * The user-interactive way of refining the resolution of symbolic references. This call usually succeeds one or
	 * multiple times after {@link IMPatchResolution#buildResolutionGUI(Composite, AdapterFactory)} was called. This is an
	 * asynchronous call.
	 * 
	 * @param mapping
	 *            A resolution of symbolic references.
	 * @param host
	 *            A callback object for notification of the refinement.
	 */
	void refineResolution(final ResolvedSymbolicReferences mapping, final IMPatchResolutionHost host);

	/**
	 * The label.
	 * 
	 * @return A human-readable and unique label for this extension.
	 */
	String getLabel();
}
