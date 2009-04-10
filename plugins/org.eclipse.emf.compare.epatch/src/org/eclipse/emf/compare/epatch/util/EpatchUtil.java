/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.compare.epatch.Assignment;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.CreatedObject;
import org.eclipse.emf.compare.epatch.ListAssignment;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.ObjectRef;
import org.eclipse.emf.compare.epatch.SingleAssignment;

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

	public static List<CreatedObject> getAllLeftValues(NamedObject obj) {
		return getAllValues(obj, new ArrayList<CreatedObject>(), true);
	}

	private static List<CreatedObject> getAllValues(NamedObject obj, List<CreatedObject> list, boolean isLeft) {
		boolean left = isLeft || obj instanceof CreatedObject;
		for (Assignment a : obj.getAssignments())
			if (a instanceof ListAssignment) {
				ListAssignment la = (ListAssignment)a;
				for (AssignmentValue v : left ? la.getLeftValues() : la.getRightValues())
					if (v.getNewObject() != null) {
						list.add(v.getNewObject());
						getAllValues(v.getNewObject(), list, isLeft);
					}
			} else if (a instanceof SingleAssignment) {
				SingleAssignment si = (SingleAssignment)a;
				AssignmentValue v = left ? si.getLeftValue() : si.getRightValue();
				if (v != null && v.getNewObject() != null) {
					list.add(v.getNewObject());
					getAllValues(v.getNewObject(), list, isLeft);
				}
			}
		return list;
	}

	public static List<CreatedObject> getAllRightValues(NamedObject obj) {
		return getAllValues(obj, new ArrayList<CreatedObject>(), false);
	}
}
