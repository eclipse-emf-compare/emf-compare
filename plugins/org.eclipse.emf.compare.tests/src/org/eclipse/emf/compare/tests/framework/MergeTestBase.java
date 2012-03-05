/**
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.framework;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * This test class provides the basic tests that should be made for any model comparison use case :
 * <table>
 * <tr>
 * <td>Two-way comparisons</td>
 * <td>
 * <ul>
 * <li>Comparing the two notifiers as EObjects, along with their content, making sure the two are contained
 * within a resource.</li>
 * <li>Comparing the two notifiers as EObject, along with their content, making sure the two are <b>not</b>
 * contained within a resource.</li>
 * <li>Comparing the two notifiers through their Resource, along with all of the resource's contents.</li>
 * <li>Comparing the two notifiers through their ResourceSet, along with all of the resource set's contents.</li>
 * </ul>
 * </td>
 * </tr>
 * <tr>
 * <td>Three-way comparisons, local changes</td>
 * <td>
 * <ul>
 * <li>Comparing the two notifiers as EObjects, along with their content, making sure the two are contained
 * within a resource. The "right" notifier is considered "common ancestor" so that all detected changes are
 * local.</li>
 * <li>Comparing the two notifiers as EObject, along with their content, making sure the two are <b>not</b>
 * contained within a resource. The "right" notifier is considered "common ancestor" so that all detected
 * changes are local.</li>
 * <li>Comparing the two notifiers through their Resource, along with all of the resource's contents. The
 * "right" notifier is considered "common ancestor" so that all detected changes are local.</li>
 * <li>Comparing the two notifiers through their ResourceSet, along with all of the resource set's contents.
 * The "right" notifier is considered "common ancestor" so that all detected changes are local.</li>
 * </ul>
 * </td>
 * </tr>
 * <tr>
 * <td>Three-way comparisons, remote changes</td>
 * <td>
 * <ul>
 * <li>Comparing the two notifiers as EObjects, along with their content, making sure the two are contained
 * within a resource. The "left" notifier is considered "common ancestor" so that all detected changes are
 * local.</li>
 * <li>Comparing the two notifiers as EObject, along with their content, making sure the two are <b>not</b>
 * contained within a resource. The "left" notifier is considered "common ancestor" so that all detected
 * changes are local.</li>
 * <li>Comparing the two notifiers through their Resource, along with all of the resource's contents. The
 * "left" notifier is considered "common ancestor" so that all detected changes are local.</li>
 * <li>Comparing the two notifiers through their ResourceSet, along with all of the resource set's contents.
 * The "left" notifier is considered "common ancestor" so that all detected changes are local.</li>
 * </ul>
 * </td>
 * </tr>
 * </table>
 * All of these tests should be executed twice : one for each "direction" of the merge. First merging from the
 * left to the right, then merging from the right to the left.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class MergeTestBase {
	/**
	 * Tests the comparison and merge of the two given Resource-contained EObjects.
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsResourceContainedEObject(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft(), couple.getRight());
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject while making sure that they are not contained
	 * in a resource.
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsOrphanEObject(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Resource leftRes = couple.getLeft().eResource();
		Resource rightRes = couple.getRight().eResource();

		EcoreUtil.resolveAll(couple.getLeft());
		EcoreUtil.resolveAll(couple.getRight());

		couple.getLeft().eResource().getContents().clear();
		couple.getRight().eResource().getContents().clear();

		Assert.assertNull(couple.getLeft().eResource());
		Assert.assertNull(couple.getRight().eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft(), couple.getRight());
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);

		leftRes.getContents().add(couple.getLeft());
		rightRes.getContents().add(couple.getRight());
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject through their respective resources.
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsWithResource(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft().eResource(), couple
				.getRight().eResource());
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject through their respective resource sets.
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsWithResourceSet(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		final ResourceSet leftResourceSet = new ResourceSetImpl();
		leftResourceSet.getResources().add(couple.getLeft().eResource());
		final ResourceSet rightResourceSet = new ResourceSetImpl();
		rightResourceSet.getResources().add(couple.getRight().eResource());

		final List<DiffElement> differences = detectDifferences(leftResourceSet, rightResourceSet);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given Resource-contained EObjects.
	 * <p>
	 * The "right" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "local" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsResourceContainedEObject3WayLocalChanges(EObjectCouple couple,
			Boolean mergeLeftToRight) throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		// We need here to detect and merge "local" changes. We'll use the "right" as ancestor
		EObject ancestor = createAncestor(couple.getRight());
		Assert.assertNotNull(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft(), couple.getRight(), ancestor);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject while making sure that they are not contained
	 * in a resource.
	 * <p>
	 * The "right" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "local" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsOrphanEObject3WayLocalChanges(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Resource leftRes = couple.getLeft().eResource();
		Resource rightRes = couple.getRight().eResource();

		EcoreUtil.resolveAll(couple.getLeft());
		EcoreUtil.resolveAll(couple.getRight());

		couple.getLeft().eResource().getContents().clear();
		couple.getRight().eResource().getContents().clear();

		// We need here to detect and merge "local" changes. We'll use the "right" as ancestor
		EObject ancestor = EcoreUtil.copy(couple.getRight());

		Assert.assertNull(couple.getLeft().eResource());
		Assert.assertNull(couple.getRight().eResource());
		Assert.assertNull(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft(), couple.getRight(), ancestor);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);

		leftRes.getContents().add(couple.getLeft());
		rightRes.getContents().add(couple.getRight());
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject through their respective resources.
	 * <p>
	 * The "right" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "local" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsWithResource3WayLocalChanges(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		// We need here to detect and merge "local" changes. We'll use the "right" as ancestor
		EObject ancestor = createAncestor(couple.getRight());
		Assert.assertNotNull(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft().eResource(), couple
				.getRight().eResource(), ancestor.eResource());
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject through their respective resource sets.
	 * <p>
	 * The "right" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "local" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsWithResourceSet3WayLocalChanges(EObjectCouple couple,
			Boolean mergeLeftToRight) throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		// We need here to detect and merge "local" changes. We'll use the "right" as ancestor
		EObject ancestor = createAncestor(couple.getRight());
		Assert.assertNotNull(ancestor.eResource());

		final ResourceSet leftResourceSet = new ResourceSetImpl();
		leftResourceSet.getResources().add(couple.getLeft().eResource());
		final ResourceSet rightResourceSet = new ResourceSetImpl();
		rightResourceSet.getResources().add(couple.getRight().eResource());
		final ResourceSet ancestorResourceSet = new ResourceSetImpl();
		ancestorResourceSet.getResources().add(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(leftResourceSet, rightResourceSet,
				ancestorResourceSet);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given Resource-contained EObjects.
	 * <p>
	 * The "left" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "remote" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsResourceContainedEObject3WayRemoteChanges(EObjectCouple couple,
			Boolean mergeLeftToRight) throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		// We need here to detect and merge "remote" changes. We'll use the "left" as ancestor
		EObject ancestor = createAncestor(couple.getLeft());
		Assert.assertNotNull(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft(), couple.getRight(), ancestor);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject while making sure that they are not contained
	 * in a resource.
	 * <p>
	 * The "left" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "remote" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsOrphanEObject3WayRemoteChanges(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Resource leftRes = couple.getLeft().eResource();
		Resource rightRes = couple.getRight().eResource();

		EcoreUtil.resolveAll(couple.getLeft());
		EcoreUtil.resolveAll(couple.getRight());

		couple.getLeft().eResource().getContents().clear();
		couple.getRight().eResource().getContents().clear();

		// We need here to detect and merge "remote" changes. We'll use the "left" as ancestor
		EObject ancestor = EcoreUtil.copy(couple.getLeft());

		Assert.assertNull(couple.getLeft().eResource());
		Assert.assertNull(couple.getRight().eResource());
		Assert.assertNull(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft(), couple.getRight(), ancestor);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);

		leftRes.getContents().add(couple.getLeft());
		rightRes.getContents().add(couple.getRight());
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject through their respective resources.
	 * <p>
	 * The "left" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "remote" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsWithResource3WayRemoteChanges(EObjectCouple couple, Boolean mergeLeftToRight)
			throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		// We need here to detect and merge "remote" changes. We'll use the "left" as ancestor
		EObject ancestor = createAncestor(couple.getLeft());
		Assert.assertNotNull(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(couple.getLeft().eResource(), couple
				.getRight().eResource(), ancestor.eResource());
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Tests the comparison and merge of the two given EObject through their respective resource sets.
	 * <p>
	 * The "left" EObject will be considered as common ancestor of the two so that we can perform a three-way
	 * comparison while only being able to detect "remote" changes.
	 * </p>
	 * 
	 * @param couple
	 *            The EObjects to compare.
	 * @param mergeLeftToRight
	 *            Indicates whether the differences will be merged from left to right, or from right to left.
	 */
	@MergeTest
	public void testMergeAllDiffsWithResourceSet3WayRemoteChanges(EObjectCouple couple,
			Boolean mergeLeftToRight) throws Exception {
		Assert.assertNotNull(couple.getLeft().eResource());
		Assert.assertNotNull(couple.getRight().eResource());

		// We need here to detect and merge "remote" changes. We'll use the "left" as ancestor
		EObject ancestor = createAncestor(couple.getLeft());
		Assert.assertNotNull(ancestor.eResource());

		final ResourceSet leftResourceSet = new ResourceSetImpl();
		leftResourceSet.getResources().add(couple.getLeft().eResource());
		final ResourceSet rightResourceSet = new ResourceSetImpl();
		rightResourceSet.getResources().add(couple.getRight().eResource());
		final ResourceSet ancestorResourceSet = new ResourceSetImpl();
		ancestorResourceSet.getResources().add(ancestor.eResource());

		final List<DiffElement> differences = detectDifferences(leftResourceSet, rightResourceSet,
				ancestorResourceSet);
		checkDifferences(couple.getLeft(), couple.getRight(), differences);

		merge(mergeLeftToRight.booleanValue(), differences);
		checkResult(couple.getLeft(), couple.getRight(), mergeLeftToRight.booleanValue());
	}

	/**
	 * Creates a new resource as a copy of the given EObject's, then return the copy of EObject.
	 * 
	 * @param eObject
	 *            The EObject which should be used as a reference to create an ancestor.
	 * @return The newly created EObject, contained within its own resource.
	 */
	protected EObject createAncestor(EObject reference) {
		Resource referenceRes = reference.eResource();
		Resource ancestorRes = null;
		try {
			Constructor<?>[] constructors = referenceRes.getClass().getConstructors();
			for (int i = 0; i < constructors.length && ancestorRes == null; i++) {
				Constructor<?> current = constructors[i];
				if (current.getParameterTypes().length == 0) {
					ancestorRes = (Resource)current.newInstance();
					ancestorRes.setURI(referenceRes.getURI());
				} else if (current.getParameterTypes().length == 1
						&& current.getParameterTypes()[0] == URI.class) {
					ancestorRes = (Resource)current.newInstance(referenceRes.getURI());
				}
			}
			if (ancestorRes == null) {
				Assert.fail("Cannot create an instance of " + referenceRes.getClass().getName()); //$NON-NLS-1$
			}
		} catch (InstantiationException e) {
			Assert.fail("Couldn't copy '" + referenceRes.getURI() + "' resource"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IllegalAccessException e) {
			Assert.fail("Couldn't copy '" + referenceRes.getURI() + "' resource"); //$NON-NLS-1$//$NON-NLS-2$
		} catch (InvocationTargetException e) {
			Assert.fail("Couldn't copy '" + referenceRes.getURI() + "' resource"); //$NON-NLS-1$//$NON-NLS-2$
		}

		// Happy compiler
		assert ancestorRes != null;

		EObject ancestor = EcoreUtil.copy(reference);
		ancestorRes.getContents().add(ancestor);

		return ancestor;
	}

	/**
	 * Checks the differences for these notifiers. The default only expects that there is at least one
	 * difference.
	 * 
	 * @param left
	 *            The Notifier which has been compared as the "left".
	 * @param right
	 *            The Notifier which has been compared as the "right".
	 * @param differences
	 *            The list of differences detected on the given notifiers.
	 */
	@SuppressWarnings("unused")
	protected void checkDifferences(Notifier left, Notifier right, List<DiffElement> differences) {
		Assert.assertFalse(differences.isEmpty());
	}

	/**
	 * Does the actual job of calling EMF Compare on the given notifiers.
	 * 
	 * @param left
	 *            The Notifier which will be compared as the "left".
	 * @param right
	 *            The Notifier which will be compared as the "right".
	 * @return The list of differences detected by EMF Compare on these Notifiers.
	 * @throws InterruptedException
	 *             Thrown if the comparison process has been interrupted.
	 */
	protected List<DiffElement> detectDifferences(Notifier left, Notifier right) throws InterruptedException {
		Map<String, Object> options = Collections.emptyMap();

		List<DiffElement> differences = new ArrayList<DiffElement>();
		if (left instanceof EObject) {
			final MatchModel match = MatchService.doMatch((EObject)left, (EObject)right, options);
			final DiffModel diff = DiffService.doDiff(match);
			differences = diff.getDifferences();
		} else if (left instanceof Resource) {
			final MatchModel match = MatchService.doResourceMatch((Resource)left, (Resource)right, options);
			final DiffModel diff = DiffService.doDiff(match);
			differences = diff.getDifferences();
		} else if (left instanceof ResourceSet) {
			final MatchResourceSet match = MatchService.doResourceSetMatch((ResourceSet)left,
					(ResourceSet)right, options);
			final DiffResourceSet diff = DiffService.doDiff(match);
			for (DiffModel diffModel : diff.getDiffModels()) {
				differences.addAll(diffModel.getDifferences());
			}
		} else {
			Assert.fail();
			// Unreachable
		}

		return differences;
	}

	/**
	 * Does the actual job of calling EMF Compare on the given notifiers.
	 * 
	 * @param left
	 *            The Notifier which will be compared as the "left".
	 * @param right
	 *            The Notifier which will be compared as the "right".
	 * @param ancestor
	 *            The notifier which should be used as common ancestor of the two others.
	 * @return The list of differences detected by EMF Compare on these Notifiers.
	 * @throws InterruptedException
	 *             Thrown if the comparison process has been interrupted.
	 */
	protected List<DiffElement> detectDifferences(Notifier left, Notifier right, Notifier ancestor)
			throws InterruptedException {
		Map<String, Object> options = Collections.emptyMap();

		List<DiffElement> differences = new ArrayList<DiffElement>();
		if (left instanceof EObject) {
			final MatchModel match = MatchService.doMatch((EObject)left, (EObject)right, (EObject)ancestor,
					options);
			final DiffModel diff = DiffService.doDiff(match);
			differences = diff.getDifferences();
		} else if (left instanceof Resource) {
			final MatchModel match = MatchService.doResourceMatch((Resource)left, (Resource)right,
					(Resource)ancestor, options);
			final DiffModel diff = DiffService.doDiff(match);
			differences = diff.getDifferences();
		} else if (left instanceof ResourceSet) {
			final MatchResourceSet match = MatchService.doResourceSetMatch((ResourceSet)left,
					(ResourceSet)right, (ResourceSet)ancestor, options);
			final DiffResourceSet diff = DiffService.doDiff(match);
			for (DiffModel diffModel : diff.getDiffModels()) {
				differences.addAll(diffModel.getDifferences());
			}
		} else {
			Assert.fail();
			// Unreachable
		}

		return differences;
	}

	/**
	 * Calls EMF Compare in order to merge the given differences in the given direction.
	 * 
	 * @param mergeLeftToRight
	 *            Whether to merge all diffs from the left model to the right, or the contrary.
	 * @param differences
	 *            The list of differences that EMF Compare is to merge.
	 */
	protected void merge(boolean mergeLeftToRight, List<DiffElement> differences) {
		MergeService.merge(differences, mergeLeftToRight);
	}

	/**
	 * Checks that the result of the merge yielded the accurate result. The default only expects that the
	 * merge made the two Notifiers equal (XMI-wise) and that comparing these two notifiers again yields no
	 * difference.
	 * 
	 * @param left
	 *            The Notifier which has been compared as the "left".
	 * @param right
	 *            The Notifier which has been compared as the "right".
	 * @param mergeLeftToRight
	 *            Whether we merged all diffs from the left model to the right, or the contrary.
	 * @throws IOException
	 *             Thrown if we cannot serialize the result as XMI.
	 * @throws InterruptedException
	 *             Thrown if the double-check through EMF Compare has been cancelled somehow.
	 */
	protected void checkResult(Notifier left, Notifier right, boolean mergeLeftToRight) throws IOException,
			InterruptedException {
		final String leftAsString = toString(left);
		final String rightAsString = toString(right);

		if (!mergeLeftToRight) {
			// expected : both notifiers equal, right being the reference
			Assert.assertEquals(rightAsString, leftAsString);
		} else {
			// expected : both notifiers equal, left being the reference
			Assert.assertEquals(leftAsString, rightAsString);
		}
	}

	/**
	 * Tries and serialize the given notifier in its XMI form. In the case of a resource set, serializes all
	 * resources in a single String.
	 * 
	 * @param notifier
	 *            The notifier to try and serialize.
	 * @return The serialized form of the given notifier.
	 * @throws IOException
	 *             Thrown if we cannot serialize this notifier into its XMI form.
	 */
	protected String toString(Notifier notifier) throws IOException {
		final String toString;
		if (notifier instanceof EObject) {
			toString = ModelUtils.serialize((EObject)notifier);
		} else if (notifier instanceof Resource) {
			// No check for content.isEmpty : there should be at least one root, or the test failed anyway
			toString = ModelUtils.serialize(((Resource)notifier).getContents().get(0));
		} else if (notifier instanceof ResourceSet) {
			StringBuilder buffer = new StringBuilder();
			for (Resource res : ((ResourceSet)notifier).getResources()) {
				buffer.append(toString(res));
			}
			toString = buffer.toString();
		} else {
			Assert.fail();
			// Unreachable
			toString = ""; //$NON-NLS-1$
		}
		return toString;
	}
}
