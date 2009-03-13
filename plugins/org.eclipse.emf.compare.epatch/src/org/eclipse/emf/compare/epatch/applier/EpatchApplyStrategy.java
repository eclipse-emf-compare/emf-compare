/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.applier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.CreatedObject;
import org.eclipse.emf.compare.epatch.ListAssignment;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectRef;
import org.eclipse.emf.compare.epatch.SingleAssignment;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface EpatchApplyStrategy {
	public static class EpatchLeftToRightStrategy implements EpatchApplyStrategy {
		protected EpatchLeftToRightStrategy() {
		}

		public String getInputFragment(ObjectRef obj) {
			return obj.getLeftFrag();
		}

		public NamedResource getInputResource(ObjectRef obj) {
			return obj.getLeftRes();
		}

		public String getInputURI(NamedResource res) {
			return res.getLeftUri();
		}

		public EList<AssignmentValue> getInputValues(ListAssignment si) {
			return si.getLeftValues();
		}

		public CreatedObject getOutputRoot(NamedResource res) {
			return res.getRightRoot();
		}

		public String getOutputURI(NamedResource res) {
			return res.getRightUri();
		}

		public AssignmentValue getOutputValue(SingleAssignment si) {
			return si.getRightValue();
		}

		public EList<AssignmentValue> getOutputValues(ListAssignment si) {
			return si.getRightValues();
		}
	}

	public static class EpatchRightToLeftStrategy implements EpatchApplyStrategy {
		protected EpatchRightToLeftStrategy() {
		}

		public String getInputFragment(ObjectRef obj) {
			return obj.getRightFrag() != null && obj.getRightRes() != null ? obj.getRightFrag() : obj
					.getLeftFrag();
		}

		public NamedResource getInputResource(ObjectRef obj) {
			return obj.getRightFrag() != null && obj.getRightRes() != null ? obj.getRightRes() : obj
					.getLeftRes();
		}

		public String getInputURI(NamedResource res) {
			return res.getRightUri();
		}

		public EList<AssignmentValue> getInputValues(ListAssignment si) {
			return si.getRightValues();
		}

		public CreatedObject getOutputRoot(NamedResource res) {
			return res.getLeftRoot();
		}

		public String getOutputURI(NamedResource res) {
			return res.getLeftUri();
		}

		public AssignmentValue getOutputValue(SingleAssignment si) {
			return si.getLeftValue();
		}

		public EList<AssignmentValue> getOutputValues(ListAssignment si) {
			return si.getLeftValues();
		}
	}

	public static class Util {
		private final static EpatchApplyStrategy LEFT_TO_RIGHT = new EpatchLeftToRightStrategy();

		private final static EpatchApplyStrategy RIGHT_TO_LEFT = new EpatchRightToLeftStrategy();

		public final static EpatchApplyStrategy get(ApplyStrategy strategy) {
			switch (strategy) {
				case LEFT_TO_RIGHT:
					return LEFT_TO_RIGHT;
				case RIGHT_TO_LEFT:
					return RIGHT_TO_LEFT;
				default:
					throw new RuntimeException();
			}
		}
	}

	public String getInputFragment(ObjectRef obj);

	public NamedResource getInputResource(ObjectRef obj);

	public String getInputURI(NamedResource res);

	public EList<AssignmentValue> getInputValues(ListAssignment si);

	public CreatedObject getOutputRoot(NamedResource res);

	public String getOutputURI(NamedResource res);

	public AssignmentValue getOutputValue(SingleAssignment si);

	public EList<AssignmentValue> getOutputValues(ListAssignment si);
}
