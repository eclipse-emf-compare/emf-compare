/*******************************************************************************
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChange;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * Basic implementation of an {@link IMerger}. Clients can extend this class instead of implementing IMerger
 * to avoid reimplementing all methods.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class DefaultMerger implements IMerger {
	/** {@link DiffElement} to be merged by this merger. */
	protected DiffElement diff;

	/** Keeps a reference on the left resource for this merger. */
	protected Resource leftResource;

	/** Keeps a reference on the right resource for this merger. */
	protected Resource rightResource;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger#applyInOrigin()
	 */
	public void applyInOrigin() {
		handleMutuallyDerivedReferences();
		removeFromContainer(diff);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger#canApplyInOrigin()
	 */
	public boolean canApplyInOrigin() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger#canUndoInTarget()
	 */
	public boolean canUndoInTarget() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger#setDiffElement(org.eclipse.emf.compare.diff.metamodel.DiffElement)
	 */
	public void setDiffElement(DiffElement element) {
		diff = element;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMerger#undoInTarget()
	 */
	public void undoInTarget() {
		handleMutuallyDerivedReferences();
		removeFromContainer(diff);
	}

	/**
	 * Removes the given {@link DiffGroup} from its container if it was its last child, also calls for the
	 * same cleanup operation on its hierarchy.
	 * 
	 * @param diffGroup
	 *            {@link DiffGroup} we want to cleanup.
	 */
	protected void cleanDiffGroup(DiffGroup diffGroup) {
		if (diffGroup != null && diffGroup.getSubchanges() == 0) {
			final EObject parent = diffGroup.eContainer();
			if (parent instanceof DiffGroup) {
				EcoreUtil.remove(diffGroup);
				cleanDiffGroup((DiffGroup)parent);
			}
		}
	}

	/**
	 * Creates a copy of the given EObject as would {@link EcoreUtil#copy(EObject)} would, except we use
	 * specific handling for unmatched references.
	 * 
	 * @param eObject
	 *            The object to copy.
	 * @return the copied object.
	 */
	protected EObject copy(EObject eObject) {
		final EMFCompareEObjectCopier copier = MergeService.getCopier(diff);
		final EObject result = copier.copy(eObject);
		copier.copyReferences();
		copier.copyXMIIDs();
		return result;
	}

	/**
	 * Returns the left resource.
	 * 
	 * @return The left resource.
	 */
	protected Resource findLeftResource() {
		if (leftResource == null) {
			final MatchModel match = ((ComparisonResourceSnapshot)EcoreUtil.getRootContainer(diff))
					.getMatch();
			final Iterator<MatchElement> matchIterator = match.getMatchedElements().iterator();
			while (matchIterator.hasNext()) {
				final Match2Elements element = (Match2Elements)matchIterator.next();
				if (element.getLeftElement() != null) {
					leftResource = element.getLeftElement().eResource();
				}
			}
		}
		return leftResource;
	}

	/**
	 * Returns the right resource.
	 * 
	 * @return The right resource.
	 */
	protected Resource findRightResource() {
		if (rightResource == null) {
			final MatchModel match = ((ComparisonResourceSnapshot)EcoreUtil.getRootContainer(diff))
					.getMatch();
			final Iterator<MatchElement> matchIterator = match.getMatchedElements().iterator();
			while (matchIterator.hasNext()) {
				final Match2Elements element = (Match2Elements)matchIterator.next();
				if (element.getRightElement() != null) {
					rightResource = element.getRightElement().eResource();
				}
			}
		}
		return rightResource;
	}

	/**
	 * Returns the {@link DiffModel} containing the {@link DiffElement} this merger is intended to merge.
	 * 
	 * @return The {@link DiffModel} containing the {@link DiffElement} this merger is intended to merge.
	 */
	protected DiffModel getDiffModel() {
		EObject container = diff.eContainer();
		while (container != null) {
			if (container instanceof DiffModel)
				return (DiffModel)container;
			container = container.eContainer();
		}
		return null;
	}

	/**
	 * Returns the XMI ID of the given {@link EObject} or <code>null</code> if it cannot be resolved.
	 * 
	 * @param object
	 *            Object which we seek the XMI ID of.
	 * @return <code>object</code>'s XMI ID, <code>null</code> if not applicable.
	 */
	protected String getXMIID(EObject object) {
		String objectID = null;
		if (object != null && object.eResource() instanceof XMIResource) {
			objectID = ((XMIResource)object.eResource()).getID(object);
		}
		return objectID;
	}

	/**
	 * Removes all references to the given {@link EObject} from the {@link DiffModel}.
	 * 
	 * @param deletedObject
	 *            Object to remove all references to.
	 */
	protected void removeDanglingReferences(EObject deletedObject) {
		EObject root = EcoreUtil.getRootContainer(deletedObject);
		if (root instanceof ComparisonResourceSnapshot) {
			root = ((ComparisonResourceSnapshot)root).getDiff();
		}
		if (root != null) {
			final EcoreUtil.CrossReferencer referencer = new EcoreUtil.CrossReferencer(root.eResource()) {
				private static final long serialVersionUID = 616050158241084372L;

				// initializer for this anonymous class
				{
					crossReference();
				}

				@Override
				protected boolean crossReference(EObject eObject, EReference eReference,
						EObject crossReferencedEObject) {
					if (eReference.isChangeable() && !eReference.isDerived())
						return crossReferencedEObject.eResource() == null;
					return false;
				}
			};
			final Iterator<Map.Entry<EObject, Collection<EStructuralFeature.Setting>>> i = referencer
					.entrySet().iterator();
			while (i.hasNext()) {
				final Map.Entry<EObject, Collection<EStructuralFeature.Setting>> entry = i.next();
				final Iterator<EStructuralFeature.Setting> j = entry.getValue().iterator();
				while (j.hasNext()) {
					EcoreUtil.remove(j.next(), entry.getKey());
				}
			}
		}
	}

	/**
	 * Removes a {@link DiffElement} from its {@link DiffGroup}.
	 * 
	 * @param diffElement
	 *            {@link DiffElement} to remove from its container.
	 */
	protected void removeFromContainer(DiffElement diffElement) {
		final EObject parent = diffElement.eContainer();
		EcoreUtil.remove(diffElement);
		removeDanglingReferences(parent);

		// If diff was contained by a ConflictingDiffElement, we call back this on it
		if (parent instanceof ConflictingDiffElement) {
			removeFromContainer((DiffElement)parent);
		}

		// if diff was in a diffGroup and it was the last one, we also remove the diffgroup
		if (parent instanceof DiffGroup) {
			cleanDiffGroup((DiffGroup)parent);
		}
	}

	/**
	 * Sets the XMI ID of the given {@link EObject} if it belongs in an {@link XMIResource}.
	 * 
	 * @param object
	 *            Object we want to set the XMI ID of.
	 * @param id
	 *            XMI ID to give to <code>object</code>.
	 */
	protected void setXMIID(EObject object, String id) {
		if (object != null && object.eResource() instanceof XMIResource) {
			((XMIResource)object.eResource()).setID(object, id);
		}
	}

	/**
	 * Mutually derived references need specific handling : merging one will implicitely merge the other and
	 * there are no way to tell such references apart.
	 * <p>
	 * Currently known references raising such issues :
	 * <table>
	 * <tr>
	 * <td>{@link EcorePackage#ECLASS__ESUPER_TYPES}</td>
	 * <td>{@link EcorePackage#ECLASS__EGENERIC_SUPER_TYPES}</td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	private void handleMutuallyDerivedReferences() {
		DiffElement toRemove = null;
		if (diff instanceof ReferenceChange) {
			final EReference reference = ((ReferenceChange)diff).getReference();
			switch (reference.getFeatureID()) {
				case EcorePackage.ECLASS__ESUPER_TYPES:
					final EObject referenceType;
					if (diff instanceof ReferenceChangeLeftTarget) {
						referenceType = ((ReferenceChangeLeftTarget)diff).getRightTarget();
					} else {
						referenceType = ((ReferenceChangeRightTarget)diff).getLeftTarget();
					}
					for (final DiffElement siblingDiff : ((DiffGroup)diff.eContainer()).getSubDiffElements()) {
						if (siblingDiff instanceof ModelElementChangeLeftTarget) {
							if (((ModelElementChangeLeftTarget)siblingDiff).getLeftElement() instanceof EGenericType
									&& ((EGenericType)((ModelElementChangeLeftTarget)siblingDiff)
											.getLeftElement()).getEClassifier() == referenceType) {
								toRemove = siblingDiff;
								break;
							}
						} else if (siblingDiff instanceof ModelElementChangeRightTarget) {
							if (((ModelElementChangeRightTarget)siblingDiff).getRightElement() instanceof EGenericType
									&& ((EGenericType)((ModelElementChangeRightTarget)siblingDiff)
											.getRightElement()).getEClassifier() == referenceType) {
								toRemove = siblingDiff;
								break;
							}
						}
					}
					break;
				default:
			}
		} else if (diff instanceof ModelElementChangeLeftTarget
				&& ((ModelElementChangeLeftTarget)diff).getLeftElement() instanceof EGenericType) {
			final ModelElementChangeLeftTarget theDiff = (ModelElementChangeLeftTarget)diff;
			final EClassifier referenceType = ((EGenericType)theDiff.getLeftElement()).getEClassifier();
			for (final DiffElement siblingDiff : ((DiffGroup)diff.eContainer()).getSubDiffElements()) {
				if (siblingDiff instanceof ReferenceChangeLeftTarget
						&& ((ReferenceChangeLeftTarget)siblingDiff).getReference().getFeatureID() == EcorePackage.ECLASS__ESUPER_TYPES) {
					if (((ReferenceChangeLeftTarget)siblingDiff).getRightTarget() == referenceType) {
						toRemove = siblingDiff;
						break;
					}
				}
			}
		} else if (diff instanceof ModelElementChangeRightTarget
				&& ((ModelElementChangeRightTarget)diff).getRightElement() instanceof EGenericType) {
			final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget)diff;
			final EClassifier referenceType = ((EGenericType)theDiff.getRightElement()).getEClassifier();
			for (final DiffElement siblingDiff : ((DiffGroup)diff.eContainer()).getSubDiffElements()) {
				if (siblingDiff instanceof ReferenceChangeRightTarget
						&& ((ReferenceChangeRightTarget)siblingDiff).getReference().getFeatureID() == EcorePackage.ECLASS__ESUPER_TYPES) {
					if (((ReferenceChangeRightTarget)siblingDiff).getLeftTarget() == referenceType) {
						toRemove = siblingDiff;
						break;
					}
				}
			}
		}
		if (toRemove != null) {
			removeFromContainer(toRemove);
		}
	}

}
