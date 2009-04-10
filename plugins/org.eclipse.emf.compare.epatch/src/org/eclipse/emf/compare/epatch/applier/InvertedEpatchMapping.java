/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.applier;

import java.util.Map;

import org.eclipse.emf.compare.epatch.ModelImport;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class InvertedEpatchMapping extends AbstractEpatchMapping {

	protected EpatchMapping mapping;

	public InvertedEpatchMapping(EpatchMapping mapping) {
		super();
		this.mapping = mapping;
		for (EpatchMappingEntry e : mapping.getAllEntries())
			put(e.getDst(), e.getSrc(), e.getPtc());
	}

	public Map<NamedResource, Resource> getDstResources() {
		return mapping.getSrcResources();
	}

	public Map<ModelImport, Resource> getImportedResources() {
		return mapping.getImportedResources();
	}

	public Map<NamedResource, Resource> getSrcResources() {
		return mapping.getDstResources();
	}

	public ApplyStrategy getStrategy() {
		return mapping.getStrategy().inverse();
	}

}
