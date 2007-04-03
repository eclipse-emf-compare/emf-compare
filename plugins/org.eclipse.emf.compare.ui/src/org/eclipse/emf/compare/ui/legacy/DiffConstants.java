/*******************************************************************************
 * Copyright (c) 2006, Intalio Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Intalio Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.legacy;

/**
 * This interface defines constants for defining diff types.
 * 
 * @author <a href="http://www.intalio.com">&copy; Intalio, Inc.</a>
 * 
 */
public interface DiffConstants {
	/**
	 * Constant for attribute missing.
	 */
	public static final int ATT_MISSING = 0;

	/**
	 * Constant for attribute added.
	 */
	public static final int ATT_ADDED = 1;

	/**
	 * Constant for attribute value changed.
	 */
	public static final int ATT_VALUE_CHANGED = 2;

	/**
	 * Constant for element missing.
	 */
	public static final int ELT_MISSING = 3;

	/**
	 * Constant for element added.
	 */
	public static final int ELT_ADDED = 4;

	/**
	 * Constant for element order changed.
	 */
	public static final int ELT_ORDER_CHANGED = 5;

	/**
	 * Constant for element renamed.
	 * 
	 * @see RefactoringDiffImpl
	 */
	public static final int ELT_RENAMED = 6;

	// Types for ITypedElement
	public static final String DIFF_TYPE = "Difference";

	public static final String DELTA_TYPE = "Delta";

	public static final String ELEMENT_TYPE = "UMLElement";

	/*
	 * This is copied from the Differencer class, will help for representing
	 * Deltas and Diffs in DiffTreeviewer
	 */
	// The kind of differences.
	/**
	 * Difference constant (value 0) indicating no difference.
	 */
	public static final int NO_CHANGE = 0;

	/**
	 * Difference constant (value 1) indicating one side was added.
	 */
	public static final int ADDITION = 1;

	/**
	 * Difference constant (value 2) indicating one side was removed.
	 */
	public static final int DELETION = 2;

	/**
	 * Difference constant (value 3) indicating side changed.
	 */
	public static final int CHANGE = 3;

	/**
	 * Bit mask (value 3) for extracting the kind of difference.
	 */
	public static final int CHANGE_TYPE_MASK = 3;

	// The direction of a three-way change.
	/**
	 * Three-way change constant (value 4) indicating a change on left side.
	 */
	public static final int LEFT = 4;

	/**
	 * Three-way change constant (value 8) indicating a change on right side.
	 */
	public static final int RIGHT = 8;

	/**
	 * Three-way change constant (value 12) indicating a change on left and
	 * right sides.
	 */
	public static final int CONFLICTING = 12;

	/**
	 * Bit mask (value 12) for extracting the direction of a three-way change.
	 */
	public static final int DIRECTION_MASK = 12;

	/**
	 * Constant (value 16) indicating a change on left and right side (with
	 * respect to ancestor) but left and right are identical.
	 */
	public static final int PSEUDO_CONFLICT = 16;

	public static final int RESOLVED = 32;

	// added for UI purposes - send the right element to the tree
	public static final int ANCESTOR = CONFLICTING;
}
