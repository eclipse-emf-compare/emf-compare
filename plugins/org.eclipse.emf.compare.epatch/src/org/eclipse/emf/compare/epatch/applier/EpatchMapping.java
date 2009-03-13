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
import java.util.Set;

import org.eclipse.emf.compare.epatch.Import;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface EpatchMapping {
	public interface EpatchMappingEntry {
		public EObject getDst();

		public NamedObject getPtc();

		public EObject getSrc();
	}

	public ApplyStrategy getStrategy();

	public Set<EpatchMappingEntry> getAllEntries();

	public EpatchMappingEntry getByDst(EObject dst);

	public EpatchMappingEntry getByPtc(NamedObject ptc);

	public EpatchMappingEntry getBySrc(EObject src);

	public Map<NamedResource, Resource> getDstResources();

	public Map<EObject, EObject> getDstToSrcMap();

	public Map<Import, Resource> getImportedResources();

	public Map<NamedResource, Resource> getSrcResources();

	public Map<EObject, EObject> getSrcToDstMap();

	public void put(EObject src, EObject dst, NamedObject ptc);

}
