/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Moritz Eysholdt <Moritz@Eysholdt.de> - initial API and implementation

 *******************************************************************************/
package org.eclipse.emf.compare.ui;

import org.eclipse.ltk.core.refactoring.Change;

/**
 * This interface needs to be implemented by objects that want to provide a {@link ModelCompareInput}. For
 * example, this is supposed to be used for classes based on {@link Change}, that want to be visualized via
 * EMF Compare.
 * 
 * @author Moritz Eysholdt
 */
public interface IModelCompareInputProvider {

	/**
	 * @return Returns the {@link ModelCompareInput} for the object.
	 */
	public ModelCompareInput getModelCompareInput();
}
