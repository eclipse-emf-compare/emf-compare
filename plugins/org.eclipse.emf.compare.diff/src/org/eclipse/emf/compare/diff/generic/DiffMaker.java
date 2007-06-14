/*  
 * Copyright (c) 2006, Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diff.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.AddModelElement;
import org.eclipse.emf.compare.diff.AddReferenceValue;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.DiffGroup;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.MoveModelElement;
import org.eclipse.emf.compare.diff.RemoveModelElement;
import org.eclipse.emf.compare.diff.RemoveReferenceValue;
import org.eclipse.emf.compare.diff.UpdateAttribute;
import org.eclipse.emf.compare.diff.api.DiffEngine;
import org.eclipse.emf.compare.match.Match2Elements;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.UnMatchElement;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class is usefull when one want's to determine a diff from a matching
 * model
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class DiffMaker implements DiffEngine {
	/**
	 * This hasmap is usefull to find the Match from any EObject instance
	 */
	private HashMap EObjectToMatch = new HashMap();

	/**
	 * Fill the EObjectToMatch hashmap to retrieve matchings from left or right
	 * EObject
	 * 
	 */
	private void updateEObjectToMatch(MatchModel match) {
		Iterator rootElemIt = match.getMatchedElements().iterator();
		while (rootElemIt.hasNext()) {
			Match2Elements matchRoot = (Match2Elements) rootElemIt.next();
			EObjectToMatch.put(matchRoot.getLeftElement(), matchRoot);
			EObjectToMatch.put(matchRoot.getRightElement(), matchRoot);
			TreeIterator matchElemIt = matchRoot.eAllContents();
			while (matchElemIt.hasNext()) {
				Match2Elements matchElem = (Match2Elements) matchElemIt.next();
				EObjectToMatch.put(matchElem.getLeftElement(), matchElem);
				EObjectToMatch.put(matchElem.getRightElement(), matchElem);
			}
		}

	}

	/**
	 * Return the matched EObject from the one given.
	 * 
	 * @param from
	 *            the original EObject
	 * @return the matched EObject
	 */
	private EObject getMatchedEObject(EObject from) {
		Match2Elements matchElem = (Match2Elements) EObjectToMatch.get(from);
		if (matchElem == null)
			return null;
		if (from == matchElem.getLeftElement())
			return matchElem.getRightElement();
		return matchElem.getLeftElement();

	}

	// private Map leftEContainerToUnMatch = new HashMap();

	/**
	 * Return a diffmodel created using the match model. This implementation is
	 * a generic and simple one.
	 * 
	 * @param match
	 *            the matching model
	 * @return the corresponding diff model
	 * @throws FactoryException
	 */
	public DiffModel doDiff(MatchModel match) {
		updateEObjectToMatch(match);
		DiffModel result = DiffFactory.eINSTANCE.createDiffModel();
		// we have to visit browse the model and create the corresponding
		// operations
		Resource leftModel = ((Match2Elements) match.getMatchedElements()
				.get(0)).getLeftElement().eResource();
		Resource rightModel = ((Match2Elements) match.getMatchedElements().get(
				0)).getRightElement().eResource();

		// creating the root modelchange
		DiffGroup root = DiffFactory.eINSTANCE.createDiffGroup();
		
		// browsing the match model
		doDiffDelegate(root, (Match2Elements) match.getMatchedElements().get(0));
		// iterate over the unmached elements end determine if they has been
		// added or removed.
		Iterator unMatched = match.getUnMatchedElements().iterator();
		while (unMatched.hasNext()) {
			UnMatchElement unMatchElement = (UnMatchElement) unMatched.next();
			if (unMatchElement.getElement().eResource() == leftModel) {
				// add remove model element
				RemoveModelElement operation = DiffFactory.eINSTANCE
						.createRemoveModelElement();
				operation.setLeftElement(unMatchElement.getElement());
				operation.setRightParent(getMatchedEObject(unMatchElement
						.getElement().eContainer()));
				addInContainerPackage(root, operation, unMatchElement
						.getElement().eContainer());
				// root.getSubDiffElements().add(operation);
			}
			if (unMatchElement.getElement().eResource() == rightModel) {
				// add remove model element
				AddModelElement operation = DiffFactory.eINSTANCE
						.createAddModelElement();
				operation.setRightElement(unMatchElement.getElement());
				EObject addedElement = unMatchElement.getElement();
				EObject parent = addedElement.eContainer();
				EObject targetParent = getMatchedEObject(parent);

				operation.setLeftParent(targetParent);
				addInContainerPackage(root, operation, targetParent);
				// root.getSubDiffElements().add(operation);
			}

		}
		
		if (root.getSubDiffElements().size() == 0)
			result.getOwnedElements().add(root);
		else
			result.getOwnedElements().add(root.getSubDiffElements().get(0));
		return result;// FIXME Do something
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
		Collection shouldAddToList = new ArrayList();
		// we really have changes
		if (current.getSubDiffElements().size() > 0) {
			Iterator it2 = current.getSubDiffElements().iterator();
			while (it2.hasNext()) {
				Object eObj = it2.next();
				if (!(eObj instanceof DiffGroup)) {
					shouldAddToList.add(eObj);
				}
			}
			// root.getSubDiffElements().add(current);
			Iterator opIt = shouldAddToList.iterator();
			while (opIt.hasNext()) {
				addInContainerPackage(root, (DiffElement) opIt.next(), current
						.getLeftParent());
			}
		} else {
			current = root;
		}
		// taking care of our childs
		Iterator it = match.getSubMatchElements().iterator();
		while (it.hasNext()) {
			Match2Elements element = (Match2Elements)it.next();
			doDiffDelegate(root, element);
		}

	}

	/**
	 * Look for an already created diff group in order to add the operation, if
	 * none exists, create one in the right place
	 */
	private void addInContainerPackage(DiffGroup root, DiffElement operation,
			EObject targetParent) {
		if (targetParent == null) {
			root.getSubDiffElements().add(operation);
			return;
		}
		DiffGroup targetGroup = findExistingGroup(root, targetParent);
		if (targetGroup == null) {
			// we have to create the group
			buildHierarchyGroup(targetParent, root).getSubDiffElements().add(
					operation);
			// TODOCBR : reconstruct the whole hiearchy starting from the root
			// or branch if a group already exists
			// root.getSubDiffElements().add(targetGroup);
		} else {
			targetGroup.getSubDiffElements().add(operation);
		}

	}

	private DiffGroup buildHierarchyGroup(EObject targetParent, DiffGroup root) {
		// si j'ai un père, je relance buildGroup sur lui et je m'ajoute dans
		// mon père, sinon
		DiffGroup curGroup = DiffFactory.eINSTANCE.createDiffGroup();
		curGroup.setLeftParent(targetParent);
		DiffGroup targetGroup = findExistingGroup(root, targetParent);
		if (targetGroup != null)
			curGroup = targetGroup;
		if (targetParent.eContainer() == null) {
			root.getSubDiffElements().add(curGroup);
			return curGroup;
		} else {
			buildHierarchyGroup(targetParent.eContainer(), root)
					.getSubDiffElements().add(curGroup);

		}
		return curGroup;
	}

	private DiffGroup findExistingGroup(DiffGroup root, EObject targetParent) {
		TreeIterator it = root.eAllContents();
		while (it.hasNext()) {
			EObject obj = (EObject) it.next();
			if (obj instanceof DiffGroup)
				if (((DiffGroup) obj).getLeftParent() == targetParent) {
					return (DiffGroup) obj;
				}

		}

		return null;
	}

	private void checkForMove(DiffGroup root, Match2Elements matchElement) {
		// TODOCBR check for moves in diffMaker
		if (matchElement.getLeftElement().eContainer() != null
				&& matchElement.getRightElement().eContainer() != null)
			if (getMatchedEObject(matchElement.getLeftElement().eContainer()) != matchElement
					.getRightElement().eContainer())
			// if
			// (!ETools.getURI(matchElement.getLeftElement()).equals(ETools.getURI(matchElement.getRightElement())))
			{
				MoveModelElement operation = DiffFactory.eINSTANCE
						.createMoveModelElement();
				operation.setRightElement(matchElement.getRightElement());
				operation.setLeftElement(matchElement.getLeftElement());
				operation.setLeftParent(matchElement.getLeftElement()
						.eContainer());
				operation.setRightParent(matchElement.getRightElement()
						.eContainer());
				root.getSubDiffElements().add(operation);

			}
	}

	/**
	 * Check wether the attributes values have changed or not
	 * 
	 * @param root
	 * @param mapping
	 * @throws FactoryException
	 */
	private void checkAttributesUpdates(DiffGroup root, Match2Elements mapping)
			throws FactoryException {

		EObject eclass = mapping.getLeftElement().eClass();

		List eclassAttributes = new LinkedList();
		if (eclass instanceof EClass)
			eclassAttributes = ((EClass) eclass).getEAllAttributes();
		// for each feature, compare the value
		Iterator it = eclassAttributes.iterator();
		while (it.hasNext()) {
			EAttribute next = (EAttribute) it.next();
			if (!next.isDerived()) {
				String attributeName = next.getName();
				if (EFactory.eGet(mapping.getLeftElement(), attributeName) != null)
					if (!EFactory.eGet(mapping.getLeftElement(), attributeName)
							.equals(
									EFactory.eGet(mapping.getRightElement(),
											attributeName))) {
						UpdateAttribute operation = DiffFactory.eINSTANCE
								.createUpdateAttribute();
						operation.setRightElement(mapping.getRightElement());
						operation.setLeftElement(mapping.getLeftElement());
						operation.setAttribute(next);
						root.getSubDiffElements().add(operation);
					}
			}
		}
	}

	/**
	 * Check wether the references values have changed or not
	 * 
	 * @param root
	 * @param mapping
	 * @throws FactoryException
	 */
	private void checkReferencesUpdates(DiffGroup root, Match2Elements mapping)
			throws FactoryException {
		// for each reference, compare the targets
		boolean break_process = false;
		Iterator it = mapping.getLeftElement().eClass().getEAllReferences().iterator();
		while (it.hasNext()) {
			EReference next = (EReference) it.next();
			String referenceName = next.getName();
			if (!next.isContainment() && !next.isDerived()
					&& !next.isTransient()) {
				List leftElementReferences = EFactory.eGetAsList(mapping
						.getLeftElement(), referenceName);
				List rightElementReferences = EFactory.eGetAsList(mapping
						.getRightElement(), referenceName);
				
				List<EObject> oldReferences = new ArrayList<EObject>();
				List<EObject> newReferences = new ArrayList<EObject>();
				List mappedOldReferences = new ArrayList();
				List mappedNewReferences = new ArrayList();
				
				if (leftElementReferences != null)
					oldReferences.addAll(leftElementReferences);
				if (rightElementReferences != null)
					newReferences.addAll(rightElementReferences);
				
				// For each of the old reference
				// if the linked element is not linked using the new references
				// then a reference has been added
				Iterator oldRef = oldReferences.iterator();
				while (oldRef.hasNext()) {
					Object curRef = oldRef.next();
					if (curRef != null) {
						EObject curMapping = getMatchedEObject((EObject) curRef);
						if (curMapping == null) {
							break_process = true;
						}
						mappedOldReferences.add(curMapping);
					}
				}
				Iterator newRef = newReferences.iterator();
				while (newRef.hasNext()) {
					Object curRef = newRef.next();
					if (curRef != null) {
						EObject curMapping = getMatchedEObject((EObject) curRef);
						if (curMapping == null) {
							break_process = true;
						}
						mappedNewReferences.add(curMapping);
					}
				}
				// new References is now added references
				newReferences.removeAll(mappedOldReferences);
				// old References is now removed references
				oldReferences.removeAll(mappedNewReferences);
				if (newReferences.size() + oldReferences.size() != 0) {
					AddReferenceValue operation = DiffFactory.eINSTANCE
							.createAddReferenceValue();
					operation.setLeftElement(mapping.getLeftElement());
					operation.setRightElement(mapping.getRightElement());
					operation.setReference(next);
					newRef = newReferences.iterator();
					while (newRef.hasNext()) {
						Object eobj = newRef.next();
						operation.getRightAddedTarget().add((eobj));
						if ((getMatchedEObject((EObject) eobj)) != null)
							operation.getLeftAddedTarget().add(
									(getMatchedEObject((EObject) eobj)));
						// EFactory.eAdd(operation, "referencesOrigins",
						// );
					}
					if (newReferences.size() > 0 && !break_process)
						root.getSubDiffElements().add(operation);
					// Remove references
					RemoveReferenceValue deloperation = DiffFactory.eINSTANCE
							.createRemoveReferenceValue();
					deloperation.setRightElement(mapping.getRightElement());
					deloperation.setLeftElement(mapping.getLeftElement());
					deloperation.setReference(next);
					oldRef = oldReferences.iterator();
					while (oldRef.hasNext()) {
						Object eobj = oldRef.next();
						// TODOCBR check that and fix
						if ((getMatchedEObject((EObject) eobj)) != null) {
							deloperation.getLeftRemovedTarget().add((eobj));
							deloperation.getRightRemovedTarget().add(
									getMatchedEObject((EObject) eobj));
						}
						// EFactory.eAdd(deloperation, "referencesTargets",
						// ((Match2Elements)
						// getMatchedEObject((EObject)eobj)).getRightElement());
						// EFactory.eAdd(deloperation, "referencesOrigins",
						// eobj);
					}
					if (oldReferences.size() > 0 && !break_process)
						root.getSubDiffElements().add(deloperation);

				}
			}
		}

	}

}

