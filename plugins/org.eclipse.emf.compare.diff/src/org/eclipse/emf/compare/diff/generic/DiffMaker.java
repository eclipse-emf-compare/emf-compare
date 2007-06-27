/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.api.DiffEngine;
import org.eclipse.emf.compare.diff.metamodel.AddModelElement;
import org.eclipse.emf.compare.diff.metamodel.AddReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.UnMatchElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This class is useful when one wants to determine a diff from a matching model.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class DiffMaker implements DiffEngine {
	/**
	 * This hashmap is useful to find the Match from any EObject instance.
	 */
	private HashMap<EObject, Match2Elements> eObjectToMatch = new HashMap<EObject, Match2Elements>();

	/**
	 * Return a diffmodel created using the match model. This implementation is a generic and simple one.
	 * 
	 * @param match
	 *            The matching model
	 * @return The corresponding diff model
	 * @throws FactoryException
	 */
	@SuppressWarnings("unchecked")
	public DiffModel doDiff(MatchModel match) {
		updateEObjectToMatch(match);
		final DiffModel result = DiffFactory.eINSTANCE.createDiffModel();
		// we have to browse the model and create the corresponding operations
		final Match2Elements matchRoot = (Match2Elements)match.getMatchedElements().get(0);
		final Resource leftModel = matchRoot.getLeftElement().eResource();
		final Resource rightModel = matchRoot.getRightElement().eResource();

		// creating the root modelchange
		final DiffGroup diffRoot = DiffFactory.eINSTANCE.createDiffGroup();

		// browsing the match model
		doDiffDelegate(diffRoot, matchRoot);
		// iterate over the unmached elements end determine if they have been
		// added or removed.
		final Iterator unMatched = match.getUnMatchedElements().iterator();
		while (unMatched.hasNext()) {
			final UnMatchElement unMatchElement = (UnMatchElement)unMatched.next();
			if (unMatchElement.getElement().eResource() == leftModel) {
				// add remove model element
				final RemoveModelElement operation = DiffFactory.eINSTANCE.createRemoveModelElement();
				operation.setLeftElement(unMatchElement.getElement());
				operation.setRightParent(getMatchedEObject(unMatchElement.getElement().eContainer()));
				addInContainerPackage(diffRoot, operation, unMatchElement.getElement().eContainer());
			}
			if (unMatchElement.getElement().eResource() == rightModel) {
				// add remove model element
				final AddModelElement operation = DiffFactory.eINSTANCE.createAddModelElement();
				final EObject addedElement = unMatchElement.getElement();
				operation.setRightElement(addedElement);
				final EObject targetParent = getMatchedEObject(addedElement.eContainer());

				operation.setLeftParent(targetParent);
				addInContainerPackage(diffRoot, operation, targetParent);
			}
		}
		
		result.getOwnedElements().add(diffRoot);
		// FIXME call diff extensions.
		return result;
	}

	/**
	 * Fill the <code>eObjectToMatch</code> hashmap to retrieve matchings from left or right EObject.
	 */
	private void updateEObjectToMatch(MatchModel match) {
		final Iterator rootElemIt = match.getMatchedElements().iterator();
		while (rootElemIt.hasNext()) {
			final Match2Elements matchRoot = (Match2Elements)rootElemIt.next();
			eObjectToMatch.put(matchRoot.getLeftElement(), matchRoot);
			eObjectToMatch.put(matchRoot.getRightElement(), matchRoot);
			final TreeIterator matchElemIt = matchRoot.eAllContents();
			while (matchElemIt.hasNext()) {
				final Match2Elements matchElem = (Match2Elements)matchElemIt.next();
				eObjectToMatch.put(matchElem.getLeftElement(), matchElem);
				eObjectToMatch.put(matchElem.getRightElement(), matchElem);
			}
		}

	}

	/**
	 * Return the matched EObject from the one given.
	 * 
	 * @param from
	 *            The original EObject.
	 * @return The matched EObject.
	 */
	private EObject getMatchedEObject(EObject from) {
		EObject matchedEObject = null;
		final Match2Elements matchElem = eObjectToMatch.get(from);
		if (matchElem != null && from.equals(matchElem.getLeftElement()))
			matchedEObject = matchElem.getRightElement();
		else if (matchElem != null)
			matchedEObject = matchElem.getLeftElement();
		return matchedEObject;
	}

	private void doDiffDelegate(DiffGroup root, Match2Elements match) {
		DiffGroup current = DiffFactory.eINSTANCE.createDiffGroup();
		current.setLeftParent(match.getLeftElement());
		try {
			checkAttributesUpdates(current, match);
			checkReferencesUpdates(current, match);
			checkForMove(current, match);
		} catch (FactoryException e) {
			EMFComparePlugin.getDefault().log(e, false);
		}
		// we need to build this list to avoid concurrent modifications
		final List<DiffElement> shouldAddToList = new ArrayList<DiffElement>();
		// we really have changes
		if (current.getSubDiffElements().size() > 0) {
			final Iterator it2 = current.getSubDiffElements().iterator();
			while (it2.hasNext()) {
				final Object eObj = it2.next();
				if (!(eObj instanceof DiffGroup)) {
					shouldAddToList.add((DiffElement)eObj);
				}
			}
			for (DiffElement diff : shouldAddToList) {
				addInContainerPackage(root, diff, current.getLeftParent());
			}
		} else {
			current = root;
		}
		// taking care of our childs
		final Iterator it = match.getSubMatchElements().iterator();
		while (it.hasNext()) {
			final Match2Elements element = (Match2Elements)it.next();
			doDiffDelegate(root, element);
		}

	}

	/**
	 * Looks for an already created diff group in order to add the operation, if none exists, create one where
	 * the operation belongs to.
	 */
	@SuppressWarnings("unchecked")
	private void addInContainerPackage(DiffGroup root, DiffElement operation, EObject targetParent) {
		if (targetParent == null) {
			root.getSubDiffElements().add(operation);
			return;
		}
		final DiffGroup targetGroup = findExistingGroup(root, targetParent);
		if (targetGroup == null) {
			// we have to create the group
			buildHierarchyGroup(targetParent, root).getSubDiffElements().add(operation);
		} else {
			targetGroup.getSubDiffElements().add(operation);
		}
	}

	@SuppressWarnings("unchecked")
	private DiffGroup buildHierarchyGroup(EObject targetParent, DiffGroup root) {
		// if targetElement has a parent, we call buildgroup on it, else we add the current group to the root
		DiffGroup curGroup = DiffFactory.eINSTANCE.createDiffGroup();
		curGroup.setLeftParent(targetParent);
		final DiffGroup targetGroup = findExistingGroup(root, targetParent);
		if (targetGroup != null)
			curGroup = targetGroup;
		if (targetParent.eContainer() == null) {
			root.getSubDiffElements().add(curGroup);
			return curGroup;
		}
		buildHierarchyGroup(targetParent.eContainer(), root).getSubDiffElements().add(curGroup);
		return curGroup;
	}

	private DiffGroup findExistingGroup(DiffGroup root, EObject targetParent) {
		final TreeIterator it = root.eAllContents();
		while (it.hasNext()) {
			final EObject obj = (EObject)it.next();
			if (obj instanceof DiffGroup) {
				if (((DiffGroup)obj).getLeftParent() == targetParent) {
					return (DiffGroup)obj;
				}
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private void checkForMove(DiffGroup root, Match2Elements matchElement) {
		if (matchElement.getLeftElement().eContainer() != null
				&& matchElement.getRightElement().eContainer() != null
				&& getMatchedEObject(matchElement.getLeftElement().eContainer()) != matchElement
						.getRightElement().eContainer()) {
			final MoveModelElement operation = DiffFactory.eINSTANCE.createMoveModelElement();
			operation.setRightElement(matchElement.getRightElement());
			operation.setLeftElement(matchElement.getLeftElement());
			operation.setLeftParent(matchElement.getLeftElement().eContainer());
			operation.setRightParent(matchElement.getRightElement().eContainer());
			root.getSubDiffElements().add(operation);
		}
	}

	@SuppressWarnings("unchecked")
	private void checkAttributesUpdates(DiffGroup root, Match2Elements mapping) throws FactoryException {
		final EObject eClass = mapping.getLeftElement().eClass();

		List eclassAttributes = new LinkedList();
		if (eClass instanceof EClass)
			eclassAttributes = ((EClass)eClass).getEAllAttributes();
		// for each feature, compare the value
		final Iterator it = eclassAttributes.iterator();
		while (it.hasNext()) {
			final EAttribute next = (EAttribute)it.next();
			if (!next.isDerived()) {
				final String attributeName = next.getName();
				if (EFactory.eGet(mapping.getLeftElement(), attributeName) != null
						&& !EFactory.eGet(mapping.getLeftElement(), attributeName).equals(
								EFactory.eGet(mapping.getRightElement(), attributeName))) {
					final UpdateAttribute operation = DiffFactory.eINSTANCE.createUpdateAttribute();
					operation.setRightElement(mapping.getRightElement());
					operation.setLeftElement(mapping.getLeftElement());
					operation.setAttribute(next);
					root.getSubDiffElements().add(operation);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void checkReferencesUpdates(DiffGroup root, Match2Elements mapping) throws FactoryException {
		// for each reference, compare the targets
		final Iterator it = mapping.getLeftElement().eClass().getEAllReferences().iterator();
		while (it.hasNext()) {
			final EReference next = (EReference)it.next();
			final String referenceName = next.getName();
			if (!next.isContainment() && !next.isDerived() && !next.isTransient()) {
				final List leftElementReferences = EFactory.eGetAsList(mapping.getLeftElement(),
						referenceName);
				final List rightElementReferences = EFactory.eGetAsList(mapping.getRightElement(),
						referenceName);

				final List<EObject> deletedReferences = new ArrayList<EObject>();
				final List<EObject> addedReferences = new ArrayList<EObject>();
				if (leftElementReferences != null)
					deletedReferences.addAll(leftElementReferences);
				if (rightElementReferences != null)
					addedReferences.addAll(rightElementReferences);

				final List<EObject> matchedOldReferences = getMatchedReferences(deletedReferences);
				final List<EObject> matchedNewReferences = getMatchedReferences(addedReferences);

				// "Added" references are the references from the left element that can't be mapped
				addedReferences.removeAll(matchedOldReferences);
				// "deleted" references are the references from the right element that can't be mapped
				deletedReferences.removeAll(matchedNewReferences);

				// Double check for objects defined in a different model and thus not matched
				// We'll use a new list to keep track of theses elements !avoid concurrent modification!
				final List<EObject> remoteMatchedElements = new ArrayList<EObject>();
				for (EObject deleted : deletedReferences) {
					if (addedReferences.contains(deleted)) {
						remoteMatchedElements.add(deleted);
					}
				}
				addedReferences.removeAll(remoteMatchedElements);
				deletedReferences.removeAll(remoteMatchedElements);

				// REFERENCES UPDATES
				if (!next.isMany() && addedReferences.size() > 0 && deletedReferences.size() > 0) {
					/*
					 * If neither the left nor the right target are proxies, or if their target URIs are
					 * distinct, this is a reference update. Otherwise, we are here because we haven't been
					 * able to resolve the proxy.
					 */
					if (!addedReferences.get(0).eIsProxy()
							|| !deletedReferences.get(0).eIsProxy()
							|| !EcoreUtil.getURI(addedReferences.get(0)).equals(
									EcoreUtil.getURI(deletedReferences.get(0)))) {
						root.getSubDiffElements().add(
								createUpdatedReferencesOperation(mapping, next, addedReferences,
										deletedReferences));
					}
				} else {
					// REFERENCES ADD
					if (addedReferences.size() > 0) {
						root.getSubDiffElements().add(
								createNewReferencesOperation(mapping, next, addedReferences));
					}
					// REFERENCES DEL
					if (deletedReferences.size() > 0) {
						root.getSubDiffElements().add(
								createRemovedReferencesOperation(mapping, next, deletedReferences));
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private UpdateUniqueReferenceValue createUpdatedReferencesOperation(Match2Elements mapping,
			EReference newReference, List<EObject> deletedReferences, List<EObject> addedReferences) {
		final UpdateUniqueReferenceValue operation = DiffFactory.eINSTANCE.createUpdateUniqueReferenceValue();
		operation.setLeftElement(mapping.getLeftElement());
		operation.setRightElement(mapping.getRightElement());
		operation.setReference(newReference);

		EObject leftTarget = getMatchedEObject(addedReferences.get(0));
		EObject rightTarget = getMatchedEObject(deletedReferences.get(0));
		// checks if target are defined remotely
		if (leftTarget == null)
			leftTarget = addedReferences.get(0);
		if (rightTarget == null)
			rightTarget = deletedReferences.get(0);

		operation.getLeftTarget().add(leftTarget);
		operation.getRightTarget().add(rightTarget);

		return operation;
	}

	@SuppressWarnings("unchecked")
	private AddReferenceValue createNewReferencesOperation(Match2Elements mapping, EReference newReference,
			List<EObject> addedReferences) {
		final AddReferenceValue operation = DiffFactory.eINSTANCE.createAddReferenceValue();
		operation.setLeftElement(mapping.getLeftElement());
		operation.setRightElement(mapping.getRightElement());
		operation.setReference(newReference);
		for (final Iterator<EObject> addedReferenceIterator = addedReferences.iterator(); addedReferenceIterator
				.hasNext(); ) {
			final EObject eobj = addedReferenceIterator.next();
			operation.getRightAddedTarget().add(eobj);
			if (getMatchedEObject(eobj) != null)
				operation.getLeftAddedTarget().add(getMatchedEObject(eobj));
		}
		return operation;
	}

	@SuppressWarnings("unchecked")
	private RemoveReferenceValue createRemovedReferencesOperation(Match2Elements mapping,
			EReference removedReference, List<EObject> deletedReferences) {
		final RemoveReferenceValue deloperation = DiffFactory.eINSTANCE.createRemoveReferenceValue();
		deloperation.setRightElement(mapping.getRightElement());
		deloperation.setLeftElement(mapping.getLeftElement());
		deloperation.setReference(removedReference);
		for (final Iterator<EObject> deletedReferenceIterator = deletedReferences.iterator(); deletedReferenceIterator
				.hasNext(); ) {
			final EObject eobj = deletedReferenceIterator.next();
			deloperation.getLeftRemovedTarget().add(eobj);
			if ((getMatchedEObject(eobj)) != null)
				deloperation.getRightRemovedTarget().add(getMatchedEObject(eobj));
		}
		return deloperation;
	}

	/**
	 * Returns the list of references from the given list that can be matched.
	 * 
	 * @param references
	 *            {@link List} of the references to match.
	 * @return The list of references from the given list that can be matched.
	 */
	private List<EObject> getMatchedReferences(List<EObject> references) {
		final List<EObject> matchedReferences = new ArrayList<EObject>();
		for (final Iterator refIterator = references.iterator(); refIterator.hasNext(); ) {
			final Object currentReference = refIterator.next();
			if (currentReference != null) {
				final EObject currentMapped = getMatchedEObject((EObject)currentReference);
				if (currentMapped != null)
					matchedReferences.add(currentMapped);
			}
		}
		return matchedReferences;
	}
}
