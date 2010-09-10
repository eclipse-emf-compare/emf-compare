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

import java.util.Collection;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Extension for creating symbolic references for arbitrary model elements ({@link EObject}s).
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public interface ISymbolicReferenceCreator {

	/**
	 * The id of the extension point this interface is used in.
	 */
	String EXTENSION_ID = "org.eclipse.emf.compare.mpatch.symbolicreference";

	/**
	 * Create a symbolic reference for the context.
	 * 
	 * The resulting reference <b>must not contain direct references to <code>self</code> or any other model</b>!
	 * 
	 * @param self
	 *            The context, an arbitrary {@link EObject}.
	 * @return A symbolic reference to the context as an {@link IElementReference}.
	 */
	IElementReference toSymbolicReference(EObject self);

	/**
	 * In order to determine references to external resources, the resources of the actual model(s) are set here.
	 * 
	 * In other words, symbolic references to external model elements must consider the external location, i.e. the
	 * {@link URI}!
	 * 
	 * @param resources
	 *            The resources of the model(s) which contain(s) the elements for which the mpatch is created.
	 */
	void setNonExternalResources(Collection<Resource> resources);

	/**
	 * A String representation of the {@link URI} for the given {@link EObject}.
	 * 
	 * @param self
	 *            Any {@link EObject}.
	 * @return A String representation of the {@link URI} for the given {@link EObject}.
	 */
	String getUriString(EObject self);

	/**
	 * The label for this creator.<br>
	 * 
	 * @return <b>Note: the label must be <i>unique</i> over all labels of all extensions!</b>
	 */
	String getLabel();

}
