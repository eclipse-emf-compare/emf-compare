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
package org.eclipse.emf.compare.diff.api;

import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.merge.api.AbstractMerger;

/**
 * A Diff Extension may be used to define higher level diff operations.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public interface DiffExtension {
	/*
	 * (non-javadoc)
	 * TODOCBR check this
	 * laurent : je crée ces constantes ici pour refactorer
	 * org.eclipse.emf.compare.diff.service.DiffExtensionDescriptor#getPriorityValue(String)
	 * pour la validité checkstyle. par ailleurs je remplace la priorité par défaut
	 * renvoyée par cette méthode par "PRIORITY_NORMAL" soit "3" au lieu du "0" que tu
	 * renvoyais précédemment.
	 * note : il faudrait une super classe commune a 
	 * org.eclipse.emf.compare.diff.service.DiffExtensionDescriptor,
	 * org.eclipse.emf.compare.diff.service.EngineDescriptor et
	 * org.eclipse.emf.compare.merge.service.FactoryDescriptor
	 * pour eviter les redondances de code (getpriorityvalue, hashcode, compareto, equals).
	 */
	/** Integer (value 1) representing the lowest priority for a {@link DiffExtension}. */
	int PRIORITY_LOWEST = 1;
	/** Integer (value 2) representing low priority for a {@link DiffExtension}. */
	int PRIORITY_LOW = 2;
	/** Integer (value 3) representing normal priority for a {@link DiffExtension}. */
	int PRIORITY_NORMAL = 3;
	/** Integer (value 4) representing high priority for a {@link DiffExtension}. */
	int PRIORITY_HIGH = 4;
	/** Integer (value 5) representing the highest priority for a {@link DiffExtension}. */
	int PRIORITY_HIGHEST = 5;
	
	/**
	 * TODOCBR comment.
	 * 
	 * @param diff
	 *            TODO comment
	 */
	void visit(DiffModel diff);

	/**
	 * TODOCBR comment.
	 * 
	 * @param element
	 *            TODO comment
	 * @return TODO comment
	 */
	AbstractMerger providesMerger(DiffElement element);
}
