/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.applier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public abstract class AbstractEpatchMapping implements EpatchMapping {
	public class EpatchMappingEntryImpl implements EpatchMappingEntry {
		private EObject dst;

		private NamedObject ptc;

		private EObject src;

		public EpatchMappingEntryImpl(EObject src, EObject dst, NamedObject ptc) {
			super();
			this.src = src;
			this.dst = dst;
			this.ptc = ptc;
		}

		public EObject getDst() {
			return dst;
		}

		public NamedObject getPtc() {
			return ptc;
		}

		public EObject getSrc() {
			return src;
		}

		@Override
		public String toString() {
			StringBuffer b = new StringBuffer();
			b.append("src:");
			b.append(src == null ? "null" : src.eClass().getName() + "@"
					+ Integer.toHexString(src.hashCode()));
			b.append(" dst:");
			b.append(dst == null ? "null" : dst.eClass().getName() + "@"
					+ Integer.toHexString(dst.hashCode()));
			b.append(" ptc:");
			b.append(ptc == null ? "null" : ptc.eClass().getName() + "@"
					+ Integer.toHexString(ptc.hashCode()));
			return b.toString();
		}

	}

	private Map<EObject, EpatchMappingEntry> dst = new HashMap<EObject, EpatchMappingEntry>();

	private Map<NamedObject, EpatchMappingEntry> ptc = new HashMap<NamedObject, EpatchMappingEntry>();

	private Map<EObject, EpatchMappingEntry> src = new HashMap<EObject, EpatchMappingEntry>();

	public Set<EpatchMappingEntry> getAllEntries() {
		Set<EpatchMappingEntry> entries = new HashSet<EpatchMappingEntry>(src.values());
		entries.addAll(dst.values());
		entries.addAll(ptc.values());
		return entries;
	}

	public EpatchMappingEntry getByDst(EObject dst) {
		return this.dst.get(dst);
	}

	public EpatchMappingEntry getByPtc(NamedObject ptc) {
		return this.ptc.get(ptc);
	}

	public EpatchMappingEntry getBySrc(EObject src) {
		return this.src.get(src);
	}

	public Map<EObject, EObject> getDstToSrcMap() {
		Map<EObject, EObject> map = new HashMap<EObject, EObject>();
		for (Entry<EObject, EpatchMappingEntry> e : dst.entrySet())
			map.put(e.getKey(), e.getValue().getSrc());
		return map;
	}

	public Map<EObject, EObject> getSrcToDstMap() {
		Map<EObject, EObject> map = new HashMap<EObject, EObject>();
		for (Entry<EObject, EpatchMappingEntry> e : src.entrySet())
			map.put(e.getKey(), e.getValue().getDst());
		return map;
	}

	protected EpatchMappingEntry createEPatchMappingEntry(EObject src, EObject dst, NamedObject ptc) {
		return new EpatchMappingEntryImpl(src, dst, ptc);
	}

	public void put(EObject src, EObject dst, NamedObject ptc) {
		EpatchMappingEntry e = createEPatchMappingEntry(src, dst, ptc);
		if (src != null)
			this.src.put(src, e);
		if (dst != null)
			this.dst.put(dst, e);
		if (ptc != null)
			this.ptc.put(ptc, e);
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer(getClass().getName());
		b.append(" {\n");
		for (EpatchMappingEntry e : getAllEntries()) {
			b.append("  ");
			b.append(e.toString());
			b.append("\n");
		}
		b.append("}");
		return b.toString();
	}
}
