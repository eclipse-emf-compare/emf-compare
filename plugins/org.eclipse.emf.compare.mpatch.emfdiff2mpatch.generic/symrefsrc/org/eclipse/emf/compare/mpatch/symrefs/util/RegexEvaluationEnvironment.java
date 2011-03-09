/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.symrefs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ecore.EcoreEvaluationEnvironment;

class RegexEvaluationEnvironment extends EcoreEvaluationEnvironment {
	RegexEvaluationEnvironment() {
		super();
	}

	RegexEvaluationEnvironment(
			EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> parent) {
		super(parent);
	}

	@Override
	public Object callOperation(EOperation operation, int opcode, Object source, Object[] args) {
		if (operation.getEAnnotation("RegexEnvironment") == null) {
			// not one of our custom operations
			return super.callOperation(operation, opcode, source, args);
		}

		if (RegexEnvironment.REGEX_MATCH_OPERATION_NAME.equals(operation.getName())) {
			Pattern pattern = Pattern.compile((String)args[0]);
			Matcher matcher = pattern.matcher((String)source);
			return matcher.matches() ? matcher.group() : null;

		} else if (RegexEnvironment.CONTAINS_OPERATION_NAME.equals(operation.getName())) {
			if (source == null) {
				return args[0] == null;
			}
			return ((String)source).contains((String)args[0]);

		} else if (RegexEnvironment.CONTAINS_IGNORE_CASE_OPERATION_NAME.equals(operation.getName())) {
			if (source == null) {
				return args[0] == null;
			}
			return ((String)source).toLowerCase().contains(((String)args[0]).toLowerCase());

		} else if (RegexEnvironment.CHECK_SIMILARITY_OPERATION_NAME.equals(operation.getName())) {
			if (source == null) {
				source = "";
			}
			final String target = args[0] == null ? "" : (String)args[0];
			final Double similarity = (Double)args[1];
			return checkSimilarity((String)source, target, similarity);

		} else {

			throw new UnsupportedOperationException("Unknown operation: " + operation.getName()); // unknown
																									// operation
		}
	}

	/**
	 * Calculate the similarity <code>s</code> of two strings <code>source</code> and <code>target</code> and
	 * compare it against the given threshold.<br>
	 * <br>
	 * If <code>1 = s</code>, then the two strings are equal.<br>
	 * If <code>1 &gt; s &gt; 0.5</code>, then one of the strings is a substring of the other.<br>
	 * If <code>0.5 &gt; s &gt; 0</code>, then they are a little bit similar.<br>
	 * If <code>s = 0</code>, then they are completely different.<br>
	 * In general, the higher the value, the more similar they are.<br>
	 * <br>
	 * Examples:
	 * <ul>
	 * <li>"id", "id" --&gt; 1
	 * <li>"id", "mid" --&gt; 0.85
	 * <li>"Id", "width" --&gt; 0.68
	 * <li>"Id", "middleman" --&gt; 0.59
	 * <li>"data", "customerdata" --&gt; 0.75
	 * <li>"data", "DATA" --&gt; 0.9
	 * <li>"data", "data2" --&gt; 0.88
	 * <li>"data", "date" --&gt; 0.38
	 * <li>"id", "wired" --&gt; 0.2
	 * <li>"person", "season" --&gt; 0.34
	 * </ul>
	 * A threshold of <code>0.7</code> is a recommended value for most cases to consider two Strings of being
	 * similar.
	 * 
	 * @return Whether the similarity satisfies the threshold.
	 */
	private static Boolean checkSimilarity(String source, String target, Double threshold) {
		final String a; // shorter string
		final String b; // longer string
		if (source.length() < target.length()) {
			a = source;
			b = target;
		} else {
			a = target;
			b = source;
		}

		final double al = a.length();
		final double bl = b.length();
		final double similarity;
		if (a.equals(b)) { // equal
			similarity = 1;
		} else if (b.contains(a)) { // containment?
			similarity = 0.5 + 1.25 * al / (bl + 2 * al);
		} else if (b.toLowerCase().equals(a.toLowerCase())) { // equal ignoring case
			similarity = 0.9;
		} else if (b.toLowerCase().contains(a.toLowerCase())) { // containment ignoring case
			if (threshold > 0.9) {
				return false; // we can get at most 0.9 here!
			}
			similarity = 0.4 + 1.25 * al / (bl + 2 * al);
		} else {
			if (threshold > 0.5) {
				return false; // we can get at most 0.5 here!
			}
			final int distance = LevenshteinDistance.calculateDistance(a, b); // else
			similarity = 0.5 - distance / 2d / bl;
		}
		// System.out.println(a + " , " + b + " --> " + similarity);
		return similarity >= threshold;
	}

	// private static void print(String a, String b) {
	// System.out.println(a + " , " + b + " --> " + calculateSimilarity(a, b));
	// }
	//
	// public static void main(String[] args) {
	// print("id", "id");
	// print("id", "mid");
	// print("Id", "width");
	// print("Id", "middleman");
	// print("data", "customerdata");
	// print("data", "DATA");
	// print("data", "data2");
	// print("data", "date");
	// print("id", "wired");
	// print("person", "season");
	// }
}
