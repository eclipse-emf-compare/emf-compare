/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.internal.DiffReferenceUtil;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.metamodel.ResourceDependencyChange;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMIResource;

/**
 * Defines here a new copier to alter the way references are copied when an EObject is. References
 * corresponding to unmatched object need specific handling.
 * <p>
 * <b>This map's content should be cleared when all differences of {@link #diffModel} are merged.</b>
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 0.8
 */
public class EMFCompareEObjectCopier extends org.eclipse.emf.ecore.util.EcoreUtil.Copier {
	/** This class' serial version UID. */
	private static final long serialVersionUID = 2701874812215174395L;

	/** If there are any ResourceDependencyChanges in the diffModel, they'll be cached in this. */
	private final List<ResourceDependencyChange> dependencyChanges = new ArrayList<ResourceDependencyChange>();

	/* (non-javadoc) defined transient since not serializable. */
	/**
	 * The DiffModel on which differences this copier will be used. Note that this could be <code>null</code>
	 * if {@link #diffResourceSet} is not.
	 */
	private final transient DiffModel diffModel;

	/* (non-javadoc) defined transient since not serializable. */
	/** The Diff Resource Set on which differences this copier will be used. */
	private final transient DiffResourceSet diffResourceSet;

	/**
	 * Creates a Copier given the DiffModel it will be used for.
	 * <p>
	 * <b>Note</b> that this should never be used if the given <code>diff</code> is contained in a
	 * DiffResourceSet.
	 * </p>
	 * 
	 * @param diff
	 *            The DiffModel which elements will be merged using this copier.
	 */
	public EMFCompareEObjectCopier(DiffModel diff) {
		super();
		diffModel = diff;
		if (diffModel.eContainer() instanceof DiffResourceSet) {
			diffResourceSet = (DiffResourceSet)diffModel.eContainer();
			for (final EObject child : diffModel.eContainer().eContents()) {
				if (child instanceof ResourceDependencyChange) {
					dependencyChanges.add((ResourceDependencyChange)child);
				}
			}
		} else {
			diffResourceSet = null;
		}
	}

	/**
	 * Creates a Copier given the DiffResourceSet it will be used for.
	 * 
	 * @param diff
	 *            The Diff Resource Set which elements will be merged using this copier.
	 * @since 1.3
	 */
	public EMFCompareEObjectCopier(DiffResourceSet diff) {
		super();
		diffModel = null;
		diffResourceSet = diff;
		for (final EObject child : diffResourceSet.eContents()) {
			if (child instanceof ResourceDependencyChange) {
				dependencyChanges.add((ResourceDependencyChange)child);
			}
		}
	}

	/**
	 * Adds the given <code>newValue</code> to the given <code>list</code> at the given <code>index</code>. If
	 * the value cannot be inserted at said index, we'll attach an adapter to it in order to remember its
	 * "expected" position. The list will be reordered later on.
	 * 
	 * @param collection
	 *            The collection to which we are to add a value.
	 * @param newValue
	 *            The value that we need to add to that collection.
	 * @param index
	 *            Index at which to insert the value.
	 */
	private static void addAtIndex(Collection<EObject> collection, EObject newValue, int index) {
		if (collection instanceof InternalEList<?>) {
			final InternalEList<? super EObject> internalEList = (InternalEList<? super EObject>)collection;
			final int listSize = internalEList.size();
			if (index > -1 && index < listSize) {
				internalEList.addUnique(index, newValue);
			} else {
				internalEList.addUnique(newValue);
			}
			attachRealPositionEAdapter(newValue, index);
			reorderList(internalEList);
		} else if (collection instanceof List<?>) {
			final List<? super EObject> list = (List<? super EObject>)collection;
			final int listSize = list.size();
			if (index > -1 && index < listSize) {
				list.add(index, newValue);
			} else {
				list.add(newValue);
			}
			attachRealPositionEAdapter(newValue, index);
			reorderList(list);
		} else {
			collection.add(newValue);
		}
	}

	/**
	 * If we could not merge a given object at its expected position in a list, we'll attach an Adapter to it
	 * in order to "remember" that "expected" position. That will allow us to reorder the list later on if
	 * need be.
	 * 
	 * @param object
	 *            The object on which to attach an Adapter.
	 * @param expectedPosition
	 *            The expected position of <code>object</code> in its list.
	 */
	private static void attachRealPositionEAdapter(Object object, int expectedPosition) {
		if (object instanceof EObject) {
			((EObject)object).eAdapters().add(new PostionAdapter(expectedPosition));
		}
	}

	/**
	 * Initially copied from {@link org.eclipse.emf.ecore.resource.impl.ResourceImpl#getEObject(List<String>)}
	 * .
	 * 
	 * @param container
	 *            The container in which we need to find an EObject.
	 * @param uriFragmentPath
	 *            Segments of the URI we need an EObject for.
	 * @return The EObject contained in <code>container</code> that lies at <code>uriFragmentPath</code>.
	 * @since 1.3
	 */
	private static EObject getEObject(EObject container, List<String> uriFragmentPath) {
		final int size = uriFragmentPath.size();
		EObject eObject = container;
		for (int i = 1; i < size && eObject != null; ++i) {
			eObject = ((InternalEObject)eObject).eObjectForURIFragmentSegment(uriFragmentPath.get(i));
		}

		return eObject;
	}

	/**
	 * Initially copied from {@link org.eclipse.emf.ecore.resource.impl.ResourceImpl#getEObject(String)}.
	 * 
	 * @param container
	 *            The container in which we need to find an EObject.
	 * @param fragment
	 *            The fragment we need an EObject for.
	 * @return The EObject contained in <code>container</code> that lies at <code>uriFragment</code>.
	 * @since 1.3
	 */
	private static EObject getEObject(EObject container, String fragment) {
		String uriFragment = fragment;
		final int length = uriFragment.length();
		if (length > 0) {
			if (uriFragment.charAt(0) == '/') {
				final ArrayList<String> uriFragmentPath = new ArrayList<String>(4);
				int start = 1;
				for (int i = 1; i < length; ++i) {
					if (uriFragment.charAt(i) == '/') {
						if (start == i) {
							uriFragmentPath.add(""); //$NON-NLS-1$							
						} else {
							uriFragmentPath.add(uriFragment.substring(start, i));
						}
						start = i + 1;
					}
				}
				uriFragmentPath.add(uriFragment.substring(start));
				return getEObject(container, uriFragmentPath);
			} else if (uriFragment.charAt(length - 1) == '?') {
				final int index = uriFragment.lastIndexOf('?', length - 2);
				if (index > 0) {
					uriFragment = uriFragment.substring(0, index);
				}
			}
		}

		return getEObjectByID(container, uriFragment);
	}

	/**
	 * Initially copied from {@link org.eclipse.emf.ecore.resource.impl.ResourceImpl#getEObjectByID(String)}.
	 * 
	 * @param container
	 *            The container in which we need to find an EObject.
	 * @param id
	 *            The id we need an EObject for.
	 * @return The EObject contained in <code>container</code> that corresponds to ID <code>id</code>.
	 * @since 1.3
	 */
	private static EObject getEObjectByID(EObject container, String id) {
		EObject result = null;

		final TreeIterator<EObject> iterator = EcoreUtil.getAllProperContents(container, false);
		while (result == null && iterator.hasNext()) {
			final EObject eObject = iterator.next();
			final String eObjectId = EcoreUtil.getID(eObject);
			if (eObjectId != null) {
				if (eObjectId.equals(id)) {
					result = eObject;
				}
			}
		}

		return result;
	}

	/**
	 * Moves the Object located at index <code>currentIndex</code> from list <code>list</code> to index
	 * <code>expectedIndex</code>.
	 * 
	 * @param list
	 *            The list from which we need an object moved.
	 * @param currentIndex
	 *            The current index of the Object that is to be moved.
	 * @param expectedIndex
	 *            The index at which to move the Object.
	 * @param <T>
	 *            type of the list's elements.
	 */
	private static <T> void movetoIndex(List<T> list, int currentIndex, int expectedIndex) {
		final int size = list.size();
		if (size <= 1 || currentIndex < 0 || currentIndex >= size) {
			return;
		}

		if (expectedIndex != -1 && expectedIndex != currentIndex && expectedIndex <= size - 1) {
			if (list instanceof InternalEList<?>) {
				((InternalEList<T>)list).move(expectedIndex, currentIndex);
			} else {
				list.add(expectedIndex, list.remove(currentIndex));
			}
		}
	}

	/**
	 * Reorders the given list if it contains EObjects associated with a PositionAdapter which are not located
	 * at their expected positions.
	 * 
	 * @param list
	 *            The list that is to be reordered.
	 * @param <T>
	 *            type of the list's elements.
	 */
	private static <T> void reorderList(List<T> list) {
		final int size = list.size();
		if (size <= 1) {
			return;
		}

		final List<?> copy = Collections.unmodifiableList(list);
		for (int i = 0; i < size; i++) {
			final Object current = copy.get(i);
			if (current instanceof EObject) {
				int expectedIndex = -1;
				final Iterator<Adapter> adapters = ((EObject)current).eAdapters().iterator();
				while (expectedIndex == -1 && adapters.hasNext()) {
					final Adapter adapter = adapters.next();
					if (adapter instanceof PostionAdapter) {
						expectedIndex = ((PostionAdapter)adapter).getExpectedIndex();
					}
				}
				if (expectedIndex != -1 && expectedIndex != i && expectedIndex <= size - 1) {
					if (list instanceof InternalEList<?>) {
						((InternalEList<T>)list).move(expectedIndex, i);
					} else {
						list.add(expectedIndex, list.remove(i));
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.EcoreUtil$Copier#copyReferences()
	 */
	@Override
	public void copyReferences() {
		final Set<Map.Entry<EObject, EObject>> entrySetCopy = new HashSet<Map.Entry<EObject, EObject>>(
				entrySet());
		for (final Map.Entry<EObject, EObject> entry : entrySetCopy) {
			final EObject eObject = entry.getKey();
			final EObject copyEObject = entry.getValue();
			for (EStructuralFeature feature : DiffReferenceUtil.getCopiableReferences(eObject)) {
				if (DiffReferenceUtil.isSimpleReference(feature)) {
					copyReference((EReference)feature, eObject, copyEObject);
				} else if (DiffReferenceUtil.isFeatureMap(feature)) {
					copyFeatureMap(eObject, feature);
				}
			}
		}
	}

	/**
	 * This will copy the given <tt>value</tt> to the reference <tt>targetReference</tt> of <tt>target</tt>.
	 * 
	 * @param targetReference
	 *            The reference to add a value to.
	 * @param target
	 *            The object to copy to.
	 * @param value
	 *            The value that is to be copied.
	 * @return The copied value.
	 * @deprecated Use {@link #copyReferenceValue(EReference, EObject, EObject, int)} instead
	 */
	@Deprecated
	public EObject copyReferenceValue(EReference targetReference, EObject target, EObject value) {
		return copyReferenceValue(targetReference, target, value, -1);
	}

	/**
	 * This will copy the given <tt>value</tt> to the reference <tt>targetReference</tt> of <tt>target</tt>.
	 * 
	 * @param targetReference
	 *            The reference to add a value to.
	 * @param target
	 *            The object to copy to.
	 * @param value
	 *            The value that is to be copied.
	 * @param matchedValue
	 *            Matched value of <tt>value</tt> if it is known. Will behave like
	 *            {@link #copyReferenceValue(EReference, EObject, EObject)} if <code>null</code>.
	 * @return The copied value.
	 * @deprecated use {@link #copyReferenceValue(EReference, EObject, EObject, int)} instead
	 */
	@Deprecated
	public EObject copyReferenceValue(EReference targetReference, EObject target, EObject value,
			EObject matchedValue) {
		return copyReferenceValue(targetReference, target, value, matchedValue, -1);
	}

	/**
	 * This will copy the given <tt>value</tt> to the reference <tt>targetReference</tt> of <tt>target</tt>.
	 * 
	 * @param targetReference
	 *            The reference to add a value to.
	 * @param target
	 *            The object to copy to.
	 * @param value
	 *            The value that is to be copied.
	 * @param matchedValue
	 *            Matched value of <tt>value</tt> if it is known. Will behave like
	 *            {@link #copyReferenceValue(EReference, EObject, EObject)} if <code>null</code>.
	 * @param index
	 *            an optional index in case the target is a List (-1 is a good default, the value will be
	 *            appended to the list)
	 * @return The copied value.
	 */
	@SuppressWarnings("unchecked")
	public EObject copyReferenceValue(EReference targetReference, EObject target, EObject value,
			EObject matchedValue, int index) {
		EObject actualValue = value;
		if (value == null && matchedValue != null) {
			handleLinkedResourceDependencyChange(matchedValue);
			actualValue = get(matchedValue);
		}
		if (matchedValue != null) {
			put(actualValue, matchedValue);

			final Object referenceValue = target.eGet(targetReference);
			if (referenceValue instanceof Collection<?>) {
				addAtIndex((Collection<EObject>)referenceValue, matchedValue, index);
			} else {
				target.eSet(targetReference, matchedValue);
			}
			return matchedValue;
		}
		return copyReferenceValue(targetReference, target, actualValue, index);
	}

	/**
	 * This will copy the given <tt>value</tt> to the reference <tt>targetReference</tt> of <tt>target</tt>.
	 * 
	 * @param targetReference
	 *            The reference to add a value to.
	 * @param target
	 *            The object to copy to.
	 * @param value
	 *            The value that is to be copied.
	 * @param index
	 *            An optional index in case the target is a List. -1 can be used to either append to the end
	 *            of the list, or copy the value of a single-valued reference (
	 *            <code>targetReference.isMany() == false</code>).
	 * @return The copied value.
	 * @since 1.3
	 */
	@SuppressWarnings("unchecked")
	public EObject copyReferenceValue(EReference targetReference, EObject target, EObject value, int index) {
		final EObject copy;
		final EObject targetValue = get(value);
		if (targetValue != null) {
			copy = targetValue;
		} else {
			if (value.eResource() == null || value.eResource().getURI().isPlatformPlugin()) {
				// We can't copy that object
				copy = value;
			} else {
				copy = copy(value);
			}
		}

		final Object referenceValue = target.eGet(targetReference);
		if (referenceValue instanceof List && targetReference.isMany()) {
			if (copy.eIsProxy() && copy instanceof InternalEObject) {
				// only add if the element is not already there.
				final URI proxURI = ((InternalEObject)copy).eProxyURI();
				boolean found = false;
				final Iterator<EObject> it = ((List<EObject>)referenceValue).iterator();
				while (!found && it.hasNext()) {
					final EObject obj = it.next();
					if (obj instanceof InternalEObject) {
						found = proxURI.equals(((InternalEObject)obj).eProxyURI());
					}
				}
				if (!found) {
					final List<EObject> targetList = (List<EObject>)referenceValue;
					addAtIndex(targetList, copy, index);
				}
			} else {
				final List<EObject> targetList = (List<EObject>)referenceValue;
				final int currentIndex = targetList.indexOf(copy);
				if (currentIndex == -1) {
					addAtIndex(targetList, copy, index);
				} else {
					// The order could be wrong in case of eOpposites
					movetoIndex(targetList, currentIndex, index);
				}
			}
		} else {
			if (copy.eIsProxy() && copy instanceof InternalEObject) {
				// only change value if the URI changes
				final URI proxURI = ((InternalEObject)copy).eProxyURI();
				if (referenceValue instanceof InternalEObject) {
					if (!proxURI.equals(((InternalEObject)referenceValue).eProxyURI())) {
						target.eSet(targetReference, copy);
					}
				}
			} else {
				target.eSet(targetReference, copy);
			}
		}
		return copy;
	}

	/**
	 * Ensures the original and copied objects all share the same XMI ID.
	 */
	public void copyXMIIDs() {
		for (final Map.Entry<EObject, EObject> entry : entrySet()) {
			final EObject original = entry.getKey();
			final EObject copy = entry.getValue();
			if (original.eResource() instanceof XMIResource && copy.eResource() instanceof XMIResource) {
				final XMIResource originResource = (XMIResource)original.eResource();
				final XMIResource copyResource = (XMIResource)copy.eResource();
				if (originResource.getID(original) != null) {
					copyResource.setID(copy, originResource.getID(original));
					final TreeIterator<EObject> originalIterator = original.eAllContents();
					final TreeIterator<EObject> copyIterator = copy.eAllContents();
					while (originalIterator.hasNext()) {
						final EObject nextOriginalChild = originalIterator.next();
						final EObject nextCopyChild = copyIterator.next();
						copyResource.setID(nextCopyChild, originResource.getID(nextOriginalChild));
					}
				}
			}
		}
	}

	/**
	 * Returns the DiffModel associated with this copier if any.
	 * 
	 * @return The DiffModel associated with this copier. Could be <code>null</code>.
	 */
	public DiffModel getDiffModel() {
		return diffModel;
	}

	/**
	 * Returns the DiffResourceSet associated with this copier if any.
	 * 
	 * @return The DiffResourceSet associated with this copier. Could be <code>null</code>.
	 * @since 1.3
	 */
	public DiffResourceSet getDiffResourceSet() {
		return diffResourceSet;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.ecore.util.EcoreUtil$Copier#copyReference(org.eclipse.emf.ecore.EReference,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void copyReference(EReference eReference, EObject eObject, EObject copyEObject) {
		// No use trying to copy the reference if it isn't set in the origin
		if (!eObject.eIsSet(eReference)) {
			return;
		}
		final Object referencedEObject = eObject.eGet(eReference, resolveProxies);
		if (eReference == EcorePackage.eINSTANCE.getEPackage_EFactoryInstance()) {
			// Let the super do its work.
			super.copyReference(eReference, eObject, copyEObject);
		} else if (eReference.isMany()) {
			final List<?> referencedObjectsList = (List<?>)referencedEObject;
			if (referencedObjectsList == null) {
				copyEObject.eSet(getTarget(eReference), null);
			} else if (referencedObjectsList.size() == 0) {
				copyEObject.eSet(getTarget(eReference), referencedObjectsList);
			} else {
				for (final Object referencedEObj : referencedObjectsList) {
					final Object copyReferencedEObject = get(referencedEObj);
					if (copyReferencedEObject != null) {
						// The referenced object has been copied via this Copier
						((List<Object>)copyEObject.eGet(getTarget(eReference))).add(copyReferencedEObject);
					} else if (referencedEObj instanceof EObject) {
						// referenced object lies in another resource, simply reference it
						final Object copyReferencedObject = findReferencedObjectCopy((EObject)referencedEObj);
						((List<Object>)copyEObject.eGet(getTarget(eReference))).add(copyReferencedObject);
					} else {
						((List<Object>)copyEObject.eGet(getTarget(eReference))).add(referencedEObj);
					}
				}
			}
		} else {
			if (referencedEObject == null) {
				copyEObject.eSet(getTarget(eReference), null);
			} else {
				final Object copyReferencedEObject = get(referencedEObject);
				if (copyReferencedEObject != null) {
					// The referenced object has been copied via this Copier
					copyEObject.eSet(getTarget(eReference), copyReferencedEObject);
				} else if (referencedEObject instanceof EObject) {
					final Object copyReferencedObject = findReferencedObjectCopy((EObject)referencedEObject);
					copyEObject.eSet(getTarget(eReference), copyReferencedObject);
				} else {
					((List<Object>)copyEObject.eGet(getTarget(eReference))).add(referencedEObject);
				}
			}
		}
	}

	/**
	 * We couldn't find a copy of <em>referencedObject</em>. We still need to find its matched object in the
	 * target resource in order not to simply reference the "old" resource from a copied object.
	 * 
	 * @param referencedEObject
	 *            object referenced from <em>eObject</em> that needs to be copied or found in the target
	 *            resource.
	 * @return Copy of the referenced object, located in the target resource if we could find it.
	 * @since 1.3
	 */
	protected Object findReferencedObjectCopy(EObject referencedEObject) {
		Object copyReferencedObject = referencedEObject;
		if (referencedEObject.eResource() == null) {
			return findReferencedObjectCopyNullResource(referencedEObject);
		}

		// Is the referencedObject in either left or right?
		final Resource referencedResource = referencedEObject.eResource();
		final String uriFragment = referencedEObject.eResource().getURIFragment(referencedEObject);
		Resource leftResource = null;
		Resource rightResource = null;

		if (diffResourceSet != null) {
			final Iterator<DiffModel> diffModels = diffResourceSet.getDiffModels().iterator();
			while (diffModels.hasNext() && leftResource == null && rightResource == null) {
				final DiffModel aDiffModel = diffModels.next();
				DiffModel referencedDiffModel = null;
				if (!aDiffModel.getLeftRoots().isEmpty()
						&& aDiffModel.getLeftRoots().get(0).eResource() != null) {
					final Resource resource = aDiffModel.getLeftRoots().get(0).eResource();
					if (referencedEObject.eResource() == resource) {
						referencedDiffModel = aDiffModel;
					}
				}
				if (referencedDiffModel == null) {
					if (!aDiffModel.getRightRoots().isEmpty()
							&& aDiffModel.getRightRoots().get(0).eResource() != null) {
						final Resource resource = aDiffModel.getRightRoots().get(0).eResource();
						if (referencedEObject.eResource() == resource) {
							referencedDiffModel = aDiffModel;
						}
					}
				}
				if (referencedDiffModel != null) {
					leftResource = referencedDiffModel.getLeftRoots().get(0).eResource();
					rightResource = referencedDiffModel.getRightRoots().get(0).eResource();
				}
			}
		} else if (diffModel != null) {
			if (!diffModel.getLeftRoots().isEmpty() && diffModel.getLeftRoots().get(0).eResource() != null) {
				leftResource = diffModel.getLeftRoots().get(0).eResource();
			}
			if (!diffModel.getRightRoots().isEmpty() && diffModel.getRightRoots().get(0).eResource() != null) {
				rightResource = diffModel.getRightRoots().get(0).eResource();
			}
		}

		if (referencedResource == leftResource && rightResource != null) {
			/*
			 * FIXME we should be using the MatchModel, but can't access it. let's hope the referenced object
			 * has already been copied
			 */
			copyReferencedObject = rightResource.getEObject(uriFragment);
			if (copyReferencedObject == null) {
				// FIXME can we find the referenced object without the match model?
			}
		} else if (referencedResource == rightResource && leftResource != null) {
			/*
			 * FIXME we should be using the MatchModel, but can't access it. let's hope the referenced object
			 * has already been copied
			 */
			copyReferencedObject = leftResource.getEObject(uriFragment);
			if (copyReferencedObject == null) {
				// FIXME can we find the referenced object without the match model?
			}
		} else {
			// Reference lies in another resource. Simply return it as is.
		}

		return copyReferencedObject;
	}

	/**
	 * We couldn't find a copy of <em>referencedObject</em>. We still need to find its matched object in the
	 * target resource in order not to simply reference the "old" resource from a copied object.
	 * <p>
	 * This will only be called after we've made sure that the referenced Object is not attached to a
	 * resource. We thus can only look within its containment tree.
	 * </p>
	 * <p>
	 * Take good not that this treatment will be extremely costly and should be avoided whenever possible.
	 * </p>
	 * 
	 * @param referencedObject
	 *            object referenced from <em>eObject</em> that needs to be copied or found in the target
	 *            containment tree.
	 * @return Copy of the referenced object, located in the target containment tree if we could find it.
	 * @since 1.3
	 */
	protected Object findReferencedObjectCopyNullResource(EObject referencedObject) {
		Object copyReferencedObject = referencedObject;

		final EObject rootContainer = EcoreUtil.getRootContainer(referencedObject);

		EObject leftRoot = null;
		EObject rightRoot = null;

		if (diffResourceSet != null) {
			final Iterator<DiffModel> diffModels = diffResourceSet.getDiffModels().iterator();
			while (diffModels.hasNext() && leftRoot == null && rightRoot == null) {
				final DiffModel aDiffModel = diffModels.next();
				DiffModel referencedDiffModel = null;
				int rootIndex = 0;

				for (int i = 0; i < aDiffModel.getLeftRoots().size(); i++) {
					if (rootContainer == aDiffModel.getLeftRoots().get(i)) {
						referencedDiffModel = aDiffModel;
						rootIndex = i;
					}
				}
				if (referencedDiffModel == null) {
					for (int i = 0; i < aDiffModel.getRightRoots().size(); i++) {
						if (rootContainer == aDiffModel.getRightRoots().get(i)) {
							referencedDiffModel = aDiffModel;
							rootIndex = i;
						}
					}
				}
				if (referencedDiffModel != null) {
					if (referencedDiffModel.getLeftRoots().size() >= rootIndex) {
						leftRoot = referencedDiffModel.getLeftRoots().get(rootIndex);
					}
					if (referencedDiffModel.getRightRoots().size() >= rootIndex) {
						rightRoot = referencedDiffModel.getRightRoots().get(rootIndex);
					}
				}
			}
		} else if (diffModel != null) {
			int rootIndex = -1;
			for (int i = 0; i < diffModel.getLeftRoots().size(); i++) {
				if (rootContainer == diffModel.getLeftRoots().get(i)) {
					rootIndex = i;
				}
			}
			for (int i = 0; i < diffModel.getRightRoots().size(); i++) {
				if (rootContainer == diffModel.getRightRoots().get(i)) {
					rootIndex = i;
				}
			}
			if (diffModel.getLeftRoots().size() >= rootIndex) {
				leftRoot = diffModel.getLeftRoots().get(rootIndex);
			}
			if (diffModel.getRightRoots().size() >= rootIndex) {
				rightRoot = diffModel.getRightRoots().get(rootIndex);
			}
		}

		// Trims the starting '#//' out
		final String uriFragment = EcoreUtil.getURI(referencedObject).toString().substring(2);
		if (rootContainer == leftRoot && rightRoot != null) {
			/*
			 * FIXME we should be using the MatchModel, but can't access it. let's hope the referenced object
			 * has already been copied
			 */
			copyReferencedObject = getEObject(rightRoot, uriFragment);
			if (copyReferencedObject == null) {
				// FIXME can we find the referenced object without the match model?
			}
		} else if (rootContainer == rightRoot && leftRoot != null) {
			/*
			 * FIXME we should be using the MatchModel, but can't access it. let's hope the referenced object
			 * has already been copied
			 */
			copyReferencedObject = getEObject(leftRoot, uriFragment);
			if (copyReferencedObject == null) {
				// FIXME can we find the referenced object without the match model?
			}
		} else {
			// Reference lies in another resource. Simply copy it
			copyReferencedObject = referencedObject;
		}

		return copyReferencedObject;
	}

	/**
	 * Copies the feature as a FeatureMap.
	 * 
	 * @param eObject
	 *            The EObject from which the feature is copied
	 * @param eStructuralFeature
	 *            The feature which needs to be copied.
	 */
	private void copyFeatureMap(EObject eObject, EStructuralFeature eStructuralFeature) {
		final FeatureMap featureMap = (FeatureMap)eObject.eGet(eStructuralFeature);
		final FeatureMap copyFeatureMap = (FeatureMap)get(eObject).eGet(getTarget(eStructuralFeature));
		int copyFeatureMapSize = copyFeatureMap.size();
		for (int k = 0; k < featureMap.size(); ++k) {
			final EStructuralFeature feature = featureMap.getEStructuralFeature(k);
			if (feature instanceof EReference) {
				final Object referencedEObject = featureMap.getValue(k);
				Object copyReferencedEObject = get(referencedEObject);
				if (copyReferencedEObject == null && referencedEObject != null) {
					final EReference reference = (EReference)feature;
					if (!useOriginalReferences || reference.isContainment()
							|| reference.getEOpposite() != null) {
						continue;
					}
					copyReferencedEObject = referencedEObject;
				}
				// If we can't add it, it must already be in the list so find it and move it
				// to the end.
				//
				if (!copyFeatureMap.add(feature, copyReferencedEObject)) {
					for (int l = 0; l < copyFeatureMapSize; ++l) {
						if (copyFeatureMap.getEStructuralFeature(l) == feature
								&& copyFeatureMap.getValue(l) == copyReferencedEObject) {
							copyFeatureMap.move(copyFeatureMap.size() - 1, l);
							--copyFeatureMapSize;
							break;
						}
					}
				}
			} else {
				copyFeatureMap.add(featureMap.get(k));
			}
		}
	}

	/**
	 * This will be called when merging reference changes in order to remove linked dependency changes.
	 * 
	 * @param element
	 *            The element that is being merged.
	 */
	private void handleLinkedResourceDependencyChange(EObject element) {
		for (final ResourceDependencyChange dependencyChange : new ArrayList<ResourceDependencyChange>(
				dependencyChanges)) {
			final Resource resource = dependencyChange.getRoots().get(0).eResource();
			if (resource == element.eResource() && dependencyChange.eContainer() != null) {
				// There are no explicit mergers for resource addition/removal.
				EcoreUtil.remove(dependencyChange);
				dependencyChanges.remove(dependencyChange);
				// map the element to itself : we wish a direct link to the same resource.
				put(element, element);
				break;
			}
		}
	}

	/**
	 * This adapter will be used to remember the accurate position of an EObject in its target list.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class PostionAdapter extends AdapterImpl {
		/** The index at which we expect to find this object. */
		private int expectedIndex;

		/**
		 * Creates our adapter.
		 * 
		 * @param index
		 *            The index at which we expect to find this object.
		 */
		public PostionAdapter(int index) {
			this.expectedIndex = index;
		}

		/**
		 * Returns the index at which we expect to find this object.
		 * 
		 * @return The index at which we expect to find this object.
		 */
		public int getExpectedIndex() {
			return expectedIndex;
		}
	}
}
