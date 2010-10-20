/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.transform.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.common.util.CommonUtils;
import org.eclipse.emf.compare.mpatch.symrefs.Condition;
import org.eclipse.emf.compare.mpatch.symrefs.ElementSetReference;
import org.eclipse.emf.compare.mpatch.symrefs.OclCondition;
import org.eclipse.emf.compare.mpatch.symrefs.SymrefsFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A helper class for merging changes.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class MergeChangesGeneralizer {

	/**
	 * Merge the given changes and create a single generalized change.
	 * 
	 * Please note: This method assumes that all given changes have the same structure, i.e.
	 * {@link MergeChangesCompare#generalizable(IndepChange, IndepChange)} returned true for all of them!
	 * 
	 * @param changes
	 *            A list of changes that should be merged.
	 * @return A generalized change merged from all given changes.
	 */
	static IndepChange generalizeChanges(List<IndepChange> changes) {
		if (changes == null || changes.size() < 2)
			return null; // nothing to do here...

		// final IndepChange generalizedChange;
		// if (MPatchPackage.Literals.INDEP_ADD_ATTRIBUTE_CHANGE.isInstance(changes.get(0))) {
		// generalizedChange = generalizeAddAttributeChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_REMOVE_ATTRIBUTE_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeRemoveAttributeChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_UPDATE_ATTRIBUTE_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeUpdateAttributeChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_ADD_ELEMENT_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeAddElementChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_REMOVE_ELEMENT_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeRemoveElementChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_MOVE_ELEMENT_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeMoveElementChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_ADD_REFERENCE_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeAddReferenceChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_REMOVE_REFERENCE_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeRemoveReferenceChanges(changes);
		// } else if (MPatchPackage.Literals.INDEP_UPDATE_REFERENCE_CHANGE.equals(changes.get(0))) {
		// generalizedChange = generalizeUpdateReferenceChanges(changes);
		// } else {
		// generalizedChange = null;
		// }
		// return generalizedChange;
		// }
		//
		// private static IndepChange generalizeAddAttributeChanges(List<IndepChange> list) {

		/*
		 * We can assume that all changes equal except for their corresponding elements! So lets create a copy of one of
		 * them as the generalized change :-)
		 * 
		 * This works because we don't have any bidirectional references in our mpatch metamodel except for
		 * dependencies. And we are taking care of dependencies later.
		 */
		final IndepChange generalizedChange = (IndepChange) EcoreUtil.copy(changes.get(0));

		// merge the corresponding elements and resulting reference, if available
		mergeCorrespondingElement(generalizedChange, changes,
				MPatchPackage.Literals.INDEP_CHANGE__CORRESPONDING_ELEMENT);
		if (changes.get(0).getResultingElement() != null) {
			mergeCorrespondingElement(generalizedChange, changes,
					MPatchPackage.Literals.INDEP_CHANGE__RESULTING_ELEMENT);
		}

		// we need to adjust the self reference of model descriptors, too!
		if (MPatchPackage.Literals.INDEP_ADD_REM_ELEMENT_CHANGE.isInstance(generalizedChange)) {
			mergeSelfReferences((IndepAddRemElementChange) generalizedChange, changes);
		}

		return generalizedChange;
	}

	/**
	 * Merge the self reference of added / removed element changes.
	 * 
	 * @param generalizedChange
	 *            The generalized change describing an added / removed element for all <code>changes</code>.
	 * @param changes
	 *            A list of changes from which <code>generalizedChange</code> was created.
	 */
	private static void mergeSelfReferences(IndepAddRemElementChange generalizedChange, List<IndepChange> changes) {
		// collect all self symbolic references that are matter of the merge
		final List<IElementReference> unmergedSelfSymrefs = new ArrayList<IElementReference>();
		for (IndepChange change : changes) {
			unmergedSelfSymrefs.add(((IndepAddRemElementChange) change).getSubModel().getSelfReference());
		}

		// do the merge and update the self symbolic reference!
		final IElementReference mergedSelfSymref = mergeSymbolicReferences(unmergedSelfSymrefs);
		((IndepAddRemElementChange) generalizedChange).getSubModel().setSelfReference(mergedSelfSymref);
	}

	/**
	 * Merge the symbolic references.
	 * 
	 * @param generalizedChange
	 *            The generalized change that does not yet have a merged symbolic reference for the given symbolic
	 *            reference.
	 * @param changes
	 *            The original changes.
	 * @param feature
	 *            The feature that contains the symbolic reference to merge.
	 */
	private static void mergeCorrespondingElement(IndepChange generalizedChange, List<IndepChange> changes,
			EReference feature) {
		if (!MPatchPackage.Literals.IELEMENT_REFERENCE.equals(feature.getEType()))
			throw new IllegalArgumentException("The given feature does not contain symbolic references: "
					+ feature.getName());

		// collect all symbolic references that are matter of the merge
		final List<IElementReference> unmergedSymrefs = new ArrayList<IElementReference>();
		for (IndepChange change : changes) {
			unmergedSymrefs.add((IElementReference) change.eGet(feature));
		}

		// do the merge and update the corresponding element symbolic reference!
		final IElementReference mergedSymref = mergeSymbolicReferences(unmergedSymrefs);
		generalizedChange.eSet(feature, mergedSymref);
	}

	/**
	 * Merge a set of symbolic references into a single symbolic reference. This is the magical call for generalizing
	 * model changes :-D
	 * 
	 * <b>Please note:</b><br>
	 * At the moment, only {@link ElementSetReference}s are allowed that contain exactly <b>one</b> {@link OclCondition}
	 * ! All other input will be ignored!
	 * 
	 * See {@link #mergeConditions(List)} for details.
	 * 
	 * @param unmergedSymrefs
	 *            A set of symbolic references that are about to being merged into a single generalized symbolic
	 *            reference.
	 * @return A generalized symbolic reference.
	 */
	private static IElementReference mergeSymbolicReferences(List<IElementReference> unmergedSymrefs) {

		int upperBound = 0;

		// merge context first, if it exists and calculate new upper bound
		final List<IElementReference> contexts = new ArrayList<IElementReference>();
		final List<String> labels = new ArrayList<String>();
		final List<String> uris = new ArrayList<String>();
		for (IElementReference symref : unmergedSymrefs) {
			final IElementReference context = ((ElementSetReference) symref).getContext();
			if (context != null)
				contexts.add(context);
			upperBound = upperBound == -1 || symref.getUpperBound() == -1 ? -1 : upperBound + symref.getUpperBound();
			labels.add(symref.getLabel());
			uris.add(symref.getUriReference());
		}
		if (!contexts.isEmpty() && contexts.size() != unmergedSymrefs.size()) {
			throw new IllegalArgumentException("Either all or no symbolic reference must have a context!");
		}

		// build new condition
		final List<String> conditions = collectConditions(unmergedSymrefs);
		final OclCondition condition = SymrefsFactory.eINSTANCE.createOclCondition();
		final String mergedExpression = mergeConditions(conditions);
		condition.setExpression(mergedExpression);

		// create resulting symbolic reference
		final ElementSetReference mergedSymbolicReference = SymrefsFactory.eINSTANCE.createElementSetReference();
		mergedSymbolicReference.setUpperBound(upperBound);
		mergedSymbolicReference.getConditions().add(condition);
		mergedSymbolicReference.setType(unmergedSymrefs.get(0).getType());
		mergedSymbolicReference.setLabel(createMergedLabel(labels));
		mergedSymbolicReference.setUriReference("Merged: " + CommonUtils.join(uris, ", "));
		if (!contexts.isEmpty()) {
			final IElementReference context = mergeSymbolicReferences(contexts);
			mergedSymbolicReference.setContext(context);
		}

		return mergedSymbolicReference;
	}

	private static String createMergedLabel(List<String> labels) {
		if (labels == null || labels.isEmpty())
			return "";
		final HashSet<String> lab = new HashSet<String>(labels); // eliminate duplicates ;-)
		return "Merged: " + CommonUtils.join(new ArrayList<String>(lab), ", ");
	}

	/**
	 * Merge all conditions into a new, combined condition. All conditions must have the form:<br>
	 * <code>[boolean expression] and [boolean expression] and ...</code>
	 * 
	 * In the resulting condition only those boolean expression remain that occur in <i>all</i> conditions.
	 * 
	 * @param conditions
	 *            A collection of ocl conditions.
	 * @return A merged condition.
	 */
	private static String mergeConditions(List<String> conditions) {
		final Map<String, Integer> expressions = new LinkedHashMap<String, Integer>();
		final int max = conditions.size();

		// count all expressions
		for (String condition : conditions) {
			for (String expression : condition.split("and")) {
				final String trimmedExpression = expression.trim();
				if (trimmedExpression.length() > 0) {
					final Integer counter = expressions.get(trimmedExpression);
					if (counter == null)
						expressions.put(trimmedExpression, 1);
					else
						expressions.put(trimmedExpression, counter.intValue() + 1);
				}
			}
		}

		// filter common expressions
		final List<String> commonExpressions = new ArrayList<String>();
		for (String expression : expressions.keySet()) {
			if (expressions.get(expression).intValue() == max)
				commonExpressions.add(expression);
		}

		// build final expression
		final String expression;
		if (!commonExpressions.isEmpty()) {
			expression = CommonUtils.join(commonExpressions, " and ");
		} else {
			expression = "true";
		}

		// TODO: check whether the expression is syntactically correct
		return expression;
	}

	/**
	 * Collect all string expressions of all ocl conditions.
	 * 
	 * Please note that only {@link ElementSetReference}s and {@link OclCondition}s and that only one condition per
	 * symbolic reference are allowed!
	 * 
	 * @param symrefs
	 *            A collection of symbolic references.
	 * @return All string expressions.
	 */
	private static List<String> collectConditions(List<IElementReference> symrefs) {
		// collect all ocl conditions
		final List<String> conditions = new ArrayList<String>();
		for (IElementReference symref : symrefs) {
			if (symref instanceof ElementSetReference) {
				ElementSetReference setSymref = (ElementSetReference) symref;
				if (setSymref.getConditions().size() == 1) {
					final Condition condition = setSymref.getConditions().get(0);
					if (condition instanceof OclCondition) {
						conditions.add(((OclCondition) condition).getExpression());
					} else {
						throw new IllegalArgumentException("Only OCL conditions are supported at the moment, not: "
								+ condition);
					}
				} else {
					throw new IllegalArgumentException(
							"Symbolic references must contain one condition only, but here, "
									+ setSymref.getConditions().size() + " are found: " + setSymref);
				}
			} else {
				throw new IllegalArgumentException(
						"Symbolic references must be of type ElementSetReference! But found: " + symref);
			}
		}
		if (conditions.size() != symrefs.size())
			throw new IllegalArgumentException(
					"Number of collected conditions and number of unmerged symbolic references differ! Aborting...");
		return conditions;
	}

}
