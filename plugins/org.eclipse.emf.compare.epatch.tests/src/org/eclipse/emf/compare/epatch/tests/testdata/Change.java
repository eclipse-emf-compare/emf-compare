/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.tests.testdata;

import java.util.ArrayList;

import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface Change {
	public static class Util {
		public static ArrayList<Object[]> getAllChanges() {
			ArrayList<Object[]> r = new ArrayList<Object[]>();
			for (Change c : ListChanges.values())
				r.add(new Object[] {c});
			for (Change c : SingleChanges.values())
				r.add(new Object[] {c});
			for (Change c : ObjectChanges.values())
				r.add(new Object[] {c});
			// for (Change c : BookChanges.values())
			// r.add(new Object[] { c });
			return r;
		}

		public static ArrayList<Change> getAllChanges2() {
			ArrayList<Change> r = new ArrayList<Change>();
			for (Change c : ListChanges.values())
				r.add(c);
			for (Change c : SingleChanges.values())
				r.add(c);
			for (Change c : ObjectChanges.values())
				r.add(c);
			// for (Change c : BookChanges.values())
			// r.add(c);
			return r;
		}
	}

	public final static Class<?>[] ALL_CHANGES = new Class<?>[] {SingleChanges.class, ListChanges.class,
			ObjectChanges.class,
	/* BookChanges.class */};

	public void apply(ResourceSet rs);

	public String asPatch();

	public String getL2RDocu();

	public String getR2LDocu();

	public ResourceSet getResourceSet();
}
