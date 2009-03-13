/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.util;

import java.util.Comparator;

import org.eclipse.emf.compare.epatch.Assignment;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.ObjectRef;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchUtil {
	public static class AssSorter implements Comparator<Assignment> {
		public int compare(Assignment o1, Assignment o2) {
			return o1.getFeature().compareToIgnoreCase(o2.getFeature());
		}
	}

	public static class AssValSorter implements Comparator<AssignmentValue> {
		private int down, up;

		public AssValSorter(boolean asc) {
			up = asc ? -1 : 1;
			down = asc ? 1 : -1;
		}

		public int compare(AssignmentValue o1, AssignmentValue o2) {
			return o1.getIndex() < o2.getIndex() ? up : o1.getIndex() == o2.getIndex() ? 0 : down;
		}
	}

	public static class ObjSorter implements Comparator<NamedObject> {
		public int compare(NamedObject o1, NamedObject o2) {
			int r = 0;
			if (o1.getName() != null && o2.getName() != null)
				r = o1.getName().compareToIgnoreCase(o2.getName());
			else if (o1.getName() == null != (o2.getName() == null))
				return o1.getName() != null ? 1 : -1;
			if (r != 0)
				return r;

			if (o1 instanceof ObjectRef && o2 instanceof ObjectRef) {
				ObjectRef r1 = (ObjectRef)o1;
				ObjectRef r2 = (ObjectRef)o2;
				if (r1.getLeftRes() != null && r2.getLeftRes() != null) {
					r = r1.getLeftRes().getName().compareTo(r2.getLeftRes().getName());
					return r != 0 ? r : r1.getLeftFrag().compareTo(r2.getLeftFrag());
				}
			}
			return 0;
		}
	}

	public final static AssSorter ASS_SORTER = new AssSorter();

	public static AssValSorter ASS_VAL_SORTER_ASC = new AssValSorter(true);

	public static AssValSorter ASS_VAL_SORTER_DESC = new AssValSorter(false);

	public static ObjSorter NAMED_OBJECT_SORTER = new ObjSorter();

	// public static void removeMigrationInfo(Epatch patch) {
	// for (TreeIterator<EObject> i = patch.eAllContents(); i.hasNext();) {
	// EObject o = i.next();
	// if (o instanceof Migration || o instanceof JavaImport
	// || o instanceof ExtensionImport) {
	// i.prune();
	// EcoreUtil.remove(o);
	// }
	// }
	// }

}
