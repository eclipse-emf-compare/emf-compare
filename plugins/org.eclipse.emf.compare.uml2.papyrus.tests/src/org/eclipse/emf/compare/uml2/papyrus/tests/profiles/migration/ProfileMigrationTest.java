/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.papyrus.tests.profiles.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diff.DefaultDiffEngine;
import org.eclipse.emf.compare.ide.ui.tests.framework.RuntimeTestRunner;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.Compare;
import org.eclipse.emf.compare.ide.ui.tests.framework.annotations.DiffEngines;
import org.eclipse.emf.compare.ide.ui.tests.framework.internal.CompareTestSupport;
import org.eclipse.emf.compare.uml2.internal.ProfileApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.internal.StereotypeAttributeChange;
import org.eclipse.emf.compare.uml2.papyrus.internal.hook.ProfileMigrationHook;
import org.eclipse.emf.compare.uml2.papyrus.internal.hook.migration.ProfileMigrationDiagnostic;
import org.eclipse.emf.compare.uml2.papyrus.internal.hook.migration.ProfileNamespaceURIPatternAPI;
import org.eclipse.emf.compare.utils.EMFComparePredicates;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.papyrus.sysml.blocks.Block;
import org.eclipse.papyrus.sysml.modelelements.ViewPoint;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.util.UMLUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * <p>
 * Tests whether the {@link ProfileMigrationHook profile migration} correctly migrates the models if their
 * profile applications can not be found.
 * </p>
 * <p>
 * The basic migrated, example structure is a SysML model with two stereotype applications on the same UML
 * class element: One Block application from the package Blocks with the feature
 * {@link Block#isEncapsulated()} set to true and one ViewPoint application from the package ModelElements
 * with the feature {@link ViewPoint#getPurpose()} set to 'This is just for testing.'.
 * </p>
 * <ul>
 * <li>Block definition: http://www.eclipse.org/papyrus/0.7.0/SysML/Blocks</li>
 * <li>ViewPoint definition: http://www.eclipse.org/papyrus/0.7.0/SysML/ModelElements</li>
 * </ul>
 * <p>
 * Artificial older profile versions of the model can not be found and therefore trigger the migration to an
 * existing (newer) profile version, i.e., the versions as specified above. Since the migration is built on
 * top of the Papyrus model repair mechanism, we need to ensure that packages are recognized correctly. This
 * mainly implies that there needs to be a version number at the end of the URI. The older versions are
 * therefore:
 * </p>
 * <ul>
 * <li>Block package definition: http://www.eclipse.org/papyrus/0.6.0/SysML/Blocks/1</li>
 * <li>ViewPoint package definition: http://www.eclipse.org/papyrus/0.6.0/SysML/ModelElements/1</li>
 * </ul>
 * <p>
 * If the number at the end would not be given, both packages would be assumed to be the same and some
 * stereotypes would be deleted.
 * </p>
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings("nls")
@RunWith(RuntimeTestRunner.class)
@DiffEngines({DefaultDiffEngine.class })
public class ProfileMigrationTest {

	/**
	 * Default value for the purpose feature of the Viewpoint stereotype.
	 */
	protected static final String VIEWPOINT_PURPOSE = "This is just for testing.";

	/**
	 * Changed value for the purpose feature of the Viewpoint stereotype.
	 */
	protected static final String VIEWPOINT_PURPOSE_CHANGE = "This is just for enhanced testing.";

	/**
	 * Changed value for the purpose feature of the Viewpoint stereotype, different from
	 * {@link #VIEWPOINT_PURPOSE_CHANGE}.
	 */
	protected static final String VIEWPOINT_PURPOSE_OTHER_CHANGE = "This is just for very enhanced testing.";

	/**
	 * Default value for the isEncapsulated feature of the Block stereotype.
	 */
	protected static final boolean BLOCK_ISENCAPSULATED = true;

	private static Object symlPattern = ProfileNamespaceURIPatternAPI
			.createPattern("^http://www\\.eclipse\\.org/papyrus/([^/]+)/SysML/[^/]+(.*)$");

	@BeforeClass
	public static void initialize() {
		ProfileNamespaceURIPatternAPI.registerPattern(symlPattern);
	}

	@AfterClass
	public static void tearDown() {
		ProfileNamespaceURIPatternAPI.unregisterPattern(symlPattern);
	}

	/*
	 * ============================ Helper Methods ============================
	 */

	/**
	 * A predicate that matches if the given {@link EObject} is an {@link AnyType} and has the given class
	 * name.
	 * 
	 * @param className
	 *            class name of the anytype object
	 * @return true if object is anytype and its class has the given name
	 */
	protected Predicate<EObject> anyType(final String className) {
		return Predicates.and(Predicates.instanceOf(AnyType.class), new Predicate<EObject>() {
			public boolean apply(final EObject object) {
				return ((AnyType)object).eClass().getName().equals(className);
			}
		});
	}

	/**
	 * Returns the provided object if it is not null and throws an error otherwise.
	 * 
	 * @param object
	 *            object to test
	 * @return object
	 */
	protected <T> T checkNotNull(final T object) {
		return checkNotNull("Unexpected null value.", object);
	}

	/**
	 * Returns the provided object if it is not null and throws an error with the given message otherwise.
	 * 
	 * @param errorMessage
	 *            message to be used in the error
	 * @param object
	 *            object to test
	 * @return object
	 */
	protected <T> T checkNotNull(final String errorMessage, final T object) {
		assertNotNull(errorMessage, object);
		return object;
	}

	/**
	 * Returns all profile migration warnings of the given resource.
	 * 
	 * @param resource
	 * @return profile migration warnings
	 */
	protected Iterable<ProfileMigrationDiagnostic> getMigrationWarnings(final Resource resource) {
		return Iterables.filter(resource.getWarnings(), ProfileMigrationDiagnostic.class);
	}

	/**
	 * Returns all profile migration errors of the given resource.
	 * 
	 * @param resource
	 * @return profile migration errors
	 */
	protected Iterable<ProfileMigrationDiagnostic> getMigrationErrors(final Resource resource) {
		return Iterables.filter(resource.getErrors(), ProfileMigrationDiagnostic.class);
	}

	/**
	 * Returns the root of this resource if it is not null. Throws an error otherwise.
	 * 
	 * @param resource
	 * @return root of the resource
	 */
	protected EObject getRoot(final Resource resource) {
		// let it fail if resource is null
		return checkNotNull("Root null.", resource.getContents().get(0));
	}

	/**
	 * Asserts that the migrated resources contains the specified number of successful and failed package
	 * migrations.
	 * 
	 * @param resource
	 * @param successfulPackageMigrations
	 * @param failedPackageMigrations
	 */
	protected void assertPartialMigration(final Resource resource, final int successfulPackageMigrations,
			final int failedPackageMigrations) {
		assertMigrationSuccess(resource, successfulPackageMigrations);
		assertMigrationFails(resource, failedPackageMigrations);
	}

	/**
	 * Asserts that the migrated resource contains the specified number of successful package migrations.
	 * 
	 * @param resource
	 * @param successfulPackageMigrations
	 */
	protected void assertMigrationSuccess(final Resource resource, final int successfulPackageMigrations) {
		// migration warnings: successful migrations
		assertEquals(successfulPackageMigrations, Iterables.size(getMigrationWarnings(resource)));
	}

	/**
	 * Asserts that the migrated resource contains the specified number of failed package migrations.
	 * 
	 * @param resource
	 * @param successfulPackageMigrations
	 */
	protected void assertMigrationFails(final Resource resource, final int failedPackageMigrations) {
		// migration errors: failed migrations
		assertEquals(failedPackageMigrations, Iterables.size(getMigrationErrors(resource)));
	}

	/**
	 * Asserts that the resource has not been migrated, i.e., it has no migration warnings and errors.
	 * 
	 * @param resource
	 * @param successfulPackageMigrations
	 */
	protected void assertNoMigration(final Resource resource) {
		assertPartialMigration(resource, 0, 0);
	}

	/**
	 * Asserts that the migrated resources has been completely migrated, i.e., it has two migration warnings
	 * and no errors
	 * 
	 * @param resource
	 */
	protected void assertMigration(final Resource resource) {
		assertPartialMigration(resource, 2, 0);
	}

	/**
	 * Returns the only UML class in the given resource. If no class is found or multiple classes are found,
	 * an error is thrown.
	 * 
	 * @param resource
	 * @return the single UML class
	 */
	protected Class getSingleUMLClass(final Resource resource) {
		final EObject root = getRoot(resource);
		final Iterable<Class> umlClasses = Iterables.filter(root.eContents(), Class.class);
		final Class umlClass = Iterables.getOnlyElement(umlClasses);
		return umlClass;
	}

	/**
	 * Asserts that there is the correct number of differences expected from the change of an attribute in the
	 * {@link ViewPoint} stereotype.
	 * 
	 * @param comparison
	 *            comparison containing the differences
	 * @param resource
	 * @param changeOnBothSides
	 *            true if changes are expected on two sides
	 */
	protected void assertViewPointAttributeChange(final Comparison comparison, final Resource resource,
			final boolean changeOnBothSides) {
		final int expectedDifferences = changeOnBothSides ? 2 : 1;

		final Class singleUMLClass = getSingleUMLClass(resource);
		final ViewPoint viewPoint = UMLUtil.getStereotypeApplication(singleUMLClass, ViewPoint.class);

		final Match classMatch = comparison.getMatch(singleUMLClass);
		final EList<Diff> classDifferences = classMatch.getDifferences();

		final Match viewPointMatch = comparison.getMatch(viewPoint);
		final EList<Diff> viewPointDifferences = viewPointMatch.getDifferences();

		assertEquals(expectedDifferences, viewPointDifferences.size());
		assertTrue(Iterables.all(viewPointDifferences, Predicates.instanceOf(AttributeChange.class)));
		assertTrue(Iterables.all(viewPointDifferences, EMFComparePredicates.ofKind(DifferenceKind.CHANGE)));
		assertEquals(expectedDifferences, classDifferences.size());
		assertTrue(Iterables.all(classDifferences, Predicates.instanceOf(StereotypeAttributeChange.class)));
		assertTrue(Iterables.all(classDifferences, EMFComparePredicates.ofKind(DifferenceKind.CHANGE)));
	}

	/**
	 * Asserts that there is the correct number of differences expected from the addition of a stereotype.
	 * 
	 * @param comparison
	 *            comparison containing the differences
	 * @param resource
	 *            migrated resource
	 * @param missingStereotypeClass
	 *            added stereotype Application
	 * @param changeOnBothSides
	 *            true if changes are expected on two sides
	 */
	protected void assertStereotypeAddition(final Comparison comparison, final Resource resource,
			final java.lang.Class<? extends EObject> missingStereotypeClass,
			final boolean changeOnBothSides) {
		final int expectedDifferences = changeOnBothSides ? 2 : 1;

		final Class singleUMLClass = getSingleUMLClass(resource);

		final Match classMatch = comparison.getMatch(singleUMLClass);
		final EList<Diff> classDifferences = classMatch.getDifferences();

		final EObject stereotypeApplication = UMLUtil.getStereotypeApplication(singleUMLClass,
				missingStereotypeClass);

		final Match stereotypeMatch = comparison.getMatch(stereotypeApplication);
		final EList<Diff> stereotypeDifferences = stereotypeMatch.getDifferences();

		assertEquals(expectedDifferences * 2, stereotypeDifferences.size());
		final Iterable<Diff> resourceAttachmentChanges = Iterables.filter(stereotypeDifferences,
				Predicates.instanceOf(ResourceAttachmentChange.class));
		assertEquals(expectedDifferences, Iterables.size(resourceAttachmentChanges));
		assertTrue(Iterables.all(resourceAttachmentChanges, EMFComparePredicates.ofKind(DifferenceKind.ADD)));
		final Iterable<Diff> referenceChanges = Iterables.filter(stereotypeDifferences,
				Predicates.instanceOf(ReferenceChange.class));
		assertEquals(expectedDifferences, Iterables.size(referenceChanges));
		assertTrue(Iterables.all(referenceChanges, EMFComparePredicates.ofKind(DifferenceKind.CHANGE)));

		assertEquals(expectedDifferences, classDifferences.size());
		assertTrue(Iterables.all(classDifferences, Predicates.instanceOf(StereotypeApplicationChange.class)));
		assertTrue(Iterables.all(classDifferences, EMFComparePredicates.ofKind(DifferenceKind.ADD)));
	}

	/**
	 * Asserts that there is the correct number of differences expected from the deletion of a profile
	 * application.
	 * 
	 * @param comparison
	 *            comparison containing the differences
	 * @param resource
	 *            migrated resource
	 * @param changeOnBothSides
	 *            true if changes are expected on two sides
	 */
	protected void assertProfileApplicationDeletion(final Comparison comparison, final Resource resource,
			final boolean changeOnBothSides) {
		final int expectedDifferences = changeOnBothSides ? 2 : 1;

		final EObject root = getRoot(resource);
		final Match rootMatch = comparison.getMatch(root);
		final EList<Diff> rootDifferences = rootMatch.getDifferences();

		final Iterable<Diff> profileApplicationChanges = Iterables.filter(rootDifferences,
				Predicates.instanceOf(ProfileApplicationChange.class));

		assertEquals(expectedDifferences, Iterables.size(profileApplicationChanges));
		assertTrue(
				Iterables.all(profileApplicationChanges, EMFComparePredicates.ofKind(DifferenceKind.DELETE)));
	}

	/*
	 * ========================= Partial Migration Assertions =========================
	 */

	/**
	 * Asserts that the single UML class of this resource has the specified number of migrated stereotype
	 * applications.
	 * 
	 * @param resource
	 *            migrated resource
	 * @param migratedStereotypeApplications
	 *            number of migrated stereotype applications
	 * @return the single UML class
	 * @see #getSingleUMLClass(Resource)
	 */
	protected Class assertPartiallyMigratedUmlClass(final Resource resource,
			final int migratedStereotypeApplications) {
		final Class umlClass = getSingleUMLClass(resource);
		assertEquals("Expected " + migratedStereotypeApplications + " stereotype applications.",
				migratedStereotypeApplications, umlClass.getStereotypeApplications().size());
		return umlClass;
	}

	/**
	 * Asserts that part of the resource has been migrated.
	 * 
	 * @param resource
	 *            migrated resource
	 * @param migratedBlock
	 *            whether the {@link Block} stereotype was migrated or not
	 * @param blockIsEncapsulated
	 *            expected value of the {@link Block#isEncapsulated()} feature
	 * @param migratedViewpoint
	 *            whether the {@link ViewPoint} stereotype was migrated or not
	 * @param viewPointPurpose
	 *            expected value of the {@link ViewPoint#getPurpose()} feature
	 */
	protected void assertPartiallyMigratedStructure(final Resource resource, final boolean migratedBlock,
			final boolean blockIsEncapsulated, final boolean migratedViewpoint,
			final String viewPointPurpose) {
		int migratedStereotypeApplications = 0;
		if (migratedBlock) {
			migratedStereotypeApplications++;
		}
		if (migratedViewpoint) {
			migratedStereotypeApplications++;
		}
		Class umlClass = assertPartiallyMigratedUmlClass(resource, migratedStereotypeApplications);
		if (migratedBlock) {
			assertMigratedBlock(umlClass, blockIsEncapsulated);
		} else {
			assertUnmigratedBlock(resource, blockIsEncapsulated);
		}
		if (migratedViewpoint) {
			assertMigratedViewPoint(umlClass, viewPointPurpose);
		} else {
			assertUnmigratedViewpoint(resource, viewPointPurpose);
		}
	}

	/*
	 * ========================= Migration Assertions =========================
	 */

	/**
	 * Asserts that the given UML class has a migrated {@link Block} stereotype applied that has the given
	 * encapsulated value.
	 * 
	 * @param umlClass
	 *            UML class to check
	 * @param isEncapsulated
	 *            expected value of the {@link Block#isEncapsulated()} feature
	 * @return Block stereotype applied on the UML class
	 */
	@SuppressWarnings("boxing")
	protected Block assertMigratedBlock(final Class umlClass, final boolean isEncapsulated) {
		final Block block = UMLUtil.getStereotypeApplication(umlClass, Block.class);
		assertNotNull("Block got deleted.", block);
		assertEquals("Wrong value in Block.", isEncapsulated, block.isEncapsulated());
		return block;
	}

	/**
	 * Asserts that the given UML class has a migrated {@link ViewPoint} stereotype applied that has the given
	 * encapsulated value.
	 * 
	 * @param umlClass
	 *            UML class to check
	 * @param purpose
	 *            expected value of the {@link ViewPoint#getPurpose()} feature
	 * @return ViewPoint stereotype applied on the UML class
	 */
	protected ViewPoint assertMigratedViewPoint(final Class umlClass, final String purpose) {
		final ViewPoint viewPoint = UMLUtil.getStereotypeApplication(umlClass, ViewPoint.class);
		assertNotNull("ViewPoint got deleted.", viewPoint);
		assertEquals("Wrong value in ViewPoint.", purpose, viewPoint.getPurpose());
		return viewPoint;
	}

	/**
	 * Asserts the completely migrated structure with two migrated packages (Block and Viewpoint) and the
	 * resulting two stereotype applications. Furthermore, asserts that the stereotypes have the provided
	 * values.
	 * 
	 * @param resource
	 * @param blockIsEncapsulated
	 *            expected value of the {@link Block#isEncapsulated()} feature
	 * @param viewPointPurpose
	 *            expected value of the {@link ViewPoint#getPurpose()} feature
	 */
	protected void assertMigratedStructure(final Resource resource, final boolean blockIsEncapsulated,
			final String viewPointPurpose) {
		assertPartiallyMigratedStructure(resource, true, blockIsEncapsulated, true, viewPointPurpose);
	}

	/*
	 * ======================= No Migration Assertions ========================
	 */

	/**
	 * Asserts that the {@link ViewPoint} stereotype has not been migrated by checking that it is available as
	 * an {@link AnyType}.
	 * 
	 * @param resource
	 * @param purpose
	 *            expected value of the {@link ViewPoint#getPurpose()} feature
	 * @return the {@link AnyType} representing the {@link ViewPoint} stereotype application
	 */
	protected AnyType assertUnmigratedViewpoint(final Resource resource, final String purpose) {
		final Iterable<EObject> viewPoints = Iterables.filter(resource.getContents(), anyType("ViewPoint"));
		final AnyType viewPoint = (AnyType)Iterables.getOnlyElement(viewPoints);
		boolean correctPropertyValue = false;
		for (final Entry entry : viewPoint.getAnyAttribute()) {
			if (entry.getEStructuralFeature().getName().equals("purpose")
					&& entry.getValue().toString().equals(purpose)) {
				correctPropertyValue = true;
				break;
			}
		}
		assertTrue("Wrong value in ViewPoint.", correctPropertyValue);
		return viewPoint;
	}

	/**
	 * Asserts that the {@link Block} stereotype has not been migrated by checking that it is available as an
	 * {@link AnyType}.
	 * 
	 * @param resource
	 * @param isEncapsulated
	 *            expected value of the {@link Block#isEncapsulated()} feature
	 * @return the {@link AnyType} representing the {@link Block} stereotype application
	 */
	protected AnyType assertUnmigratedBlock(final Resource resource, final boolean isEncapsulated) {
		final Iterable<EObject> viewPoints = Iterables.filter(resource.getContents(), anyType("Block"));
		final AnyType block = (AnyType)Iterables.getOnlyElement(viewPoints);
		boolean correctPropertyValue = false;
		for (final Entry entry : block.getAnyAttribute()) {
			if (entry.getEStructuralFeature().getName().equals("isEncapsulated")
					&& Boolean.parseBoolean(entry.getValue().toString()) == isEncapsulated) {
				correctPropertyValue = true;
				break;
			}
		}
		assertTrue("Wrong value in Block.", correctPropertyValue);
		return block;
	}

	/**
	 * Asserts that the resource has not been migrated and contains {@link AnyType} for the missing stereotype
	 * applications.
	 * 
	 * @param resource
	 * @param blockIsEncapsulated
	 *            expected value of the {@link Block#isEncapsulated()} feature
	 * @param viewPointPurpose
	 *            expected value of the {@link ViewPoint#getPurpose()} feature
	 */
	protected void assertUnmigratedStructure(final Resource resource, final boolean blockIsEncapsulated,
			final String viewPointPurpose) {
		assertPartiallyMigratedStructure(resource, false, blockIsEncapsulated, false, viewPointPurpose);
	}

	/*
	 * ============================== Test Cases ==============================
	 */

	/**
	 * All models follow the same, migrated structure. Therefore, no migration is executed.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/nodiff/left.uml", right = "data/sysml/nodiff/right.uml", ancestor = "data/sysml/nodiff/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testNoDifference(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertNoMigration(originResource);
		assertNoMigration(leftResource);
		assertNoMigration(rightResource);

		// test structure of models (all models have same structure)
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// there are no conflicts and no differences since only the version changed
		assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
		assertTrue("Unexpected differences: " + differences, differences.isEmpty());
	}

	/**
	 * Tests whether the profile migration correctly migrates the origin model even if the left and the right
	 * model are identical. There are no changes in the features.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/version/abb/left.uml", right = "data/sysml/version/abb/right.uml", ancestor = "data/sysml/version/abb/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testVersionChangeABB(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource);
		assertNoMigration(rightResource);

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts and no differences since only the version changed
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			assertTrue("Unexpected differences: " + differences, differences.isEmpty());
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the right and the origin model. There are no
	 * changes in the features.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/version/aba/left.uml", right = "data/sysml/version/aba/right.uml", ancestor = "data/sysml/version/aba/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testVersionChangeABA(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource);
		assertMigration(rightResource); // success: Block and ViewPoint package, no fails

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts and no differences since only the version changed
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			assertTrue("Unexpected differences: " + differences, differences.isEmpty());
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the right and the origin model and feature
	 * changes are detected. In the left resource, the feature {@link ViewPoint#getPurpose()} has changed and
	 * the difference should be detected, but no conflict should be produced. This test is equivalent to
	 * {@link #testAttributeChangeAAB(Comparison, CompareTestSupport)} with reversed left and right sides.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/attribute/change/aba/left.uml", right = "data/sysml/attribute/change/aba/right.uml", ancestor = "data/sysml/attribute/change/aba/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testAttributeChangeABA(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource);
		assertMigration(rightResource); // success: Block and ViewPoint package, no fails

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE_CHANGE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			// 2 differences related to viewPointChange: Attribute change refining a StereotypeAttributeChange
			assertEquals(2, differences.size());
			assertViewPointAttributeChange(comparison, originResource, false);
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the left and the origin model and feature
	 * changes are detected. In the right resource the feature {@link ViewPoint#getPurpose()} has changed and
	 * the difference should be detected, but no conflict should be produced. This test is equivalent to
	 * {@link #testAttributeChangeABA(Comparison, CompareTestSupport)} with reversed left and right sides.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/attribute/change/aab/left.uml", right = "data/sysml/attribute/change/aab/right.uml", ancestor = "data/sysml/attribute/change/aab/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testAttributeChangeAAB(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertMigration(leftResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(rightResource);

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE_CHANGE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			// 2 differences related to viewPointChange: Attribute change refining a StereotypeAttributeChange
			assertEquals(2, differences.size());
			assertViewPointAttributeChange(comparison, originResource, false);
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the origin model and feature changes are
	 * detected. The {@link ViewPoint#getPurpose()} feature has changed in the left and right resource to the
	 * same value. So, differences should be detected, but no REAL conflict should be produced.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/attribute/change/abb/left.uml", right = "data/sysml/attribute/change/abb/right.uml", ancestor = "data/sysml/attribute/change/abb/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testAttributeChangeABB(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(rightResource);

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE_CHANGE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE_CHANGE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// 1 PSEUDO conflict: ViewPoint::purpose changed on both sides to equal value
			assertEquals((conflicts.size() - 1) + " unexpected conflicts", 1, conflicts.size());
			assertTrue(Iterables.all(conflicts,
					EMFComparePredicates.containsConflictOfTypes(ConflictKind.PSEUDO)));

			// 2 differences on both sides related to viewPointChange: Attribute change refining a
			// StereotypeAttributeChange
			assertEquals(4, differences.size());
			assertViewPointAttributeChange(comparison, originResource, true);
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the origin model and feature changes are
	 * detected. The {@link ViewPoint#getPurpose()} feature has changed in the left and right resource to
	 * different values. So, differences should be detected and a REAL conflict should be produced.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/attribute/change/abc/left.uml", right = "data/sysml/attribute/change/abc/right.uml", ancestor = "data/sysml/attribute/change/abc/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testAttributeChangeABC(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(rightResource);

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE_CHANGE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE_OTHER_CHANGE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {

			// 1 REAL conflict: ViewPoint::purpose changed on both sides to equal value
			assertEquals((conflicts.size() - 1) + " unexpected conflicts", 1, conflicts.size());
			assertTrue(Iterables.all(conflicts,
					EMFComparePredicates.containsConflictOfTypes(ConflictKind.REAL)));

			// 2 differences on both sides related to viewPointChange: Attribute change refining a
			// StereotypeAttributeChange
			assertEquals(4, differences.size());
			assertViewPointAttributeChange(comparison, originResource, true);
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the origin and right model and unknown features
	 * are removed as expected. The origin and the right model have a String attribute "oldAttribute" with the
	 * same value. This feature does not exist in the migrated version of the profile and should therefore be
	 * deleted. Since the profile migration happens before the comparison, this deletion is not part of the
	 * result. Therefore, all models are considered the same and no differences are detected.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/attribute/unmigrated/aba/left.uml", right = "data/sysml/attribute/unmigrated/aba/right.uml", ancestor = "data/sysml/attribute/unmigrated/aba/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testUnmigratedAttributeABA(final Comparison comparison, final CompareTestSupport support) {
		// origin and right have oldAttribute="This is not available in the new version." in ViewPoint
		// stereotype application, but this attribute is not in new version and gets lost
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource);
		assertMigration(rightResource); // success: Block and ViewPoint package, no fails

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts and differences
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			assertTrue("Unexpected differences: " + differences, differences.isEmpty());
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests whether the profile migration correctly migrates the origin and right model and unknown features
	 * are created with their default value as expected. In this setup, the origin and the right model have no
	 * value for the {@link ViewPoint#getPurpose()} feature. Therefore the comparison should take the default
	 * value of this feature detect the change from the default value to the value in the left model.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/attribute/defaultval/aba/left.uml", right = "data/sysml/attribute/defaultval/aba/right.uml", ancestor = "data/sysml/attribute/defaultval/aba/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testAttributeDefaultValueABA(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		final Class singleUMLClass = getSingleUMLClass(originResource);
		final ViewPoint viewPoint = UMLUtil.getStereotypeApplication(singleUMLClass, ViewPoint.class);

		assertNotNull("Viewpoint expected.", viewPoint);

		final String purposeDefaultValue = viewPoint.eClass().getEStructuralFeature("purpose")
				.getDefaultValueLiteral();

		// test migration
		assertMigration(originResource); // success: Block and ViewPoint package, no fails
		assertNoMigration(leftResource);
		assertMigration(rightResource); // success: Block and ViewPoint package, no fails

		// test structure of models
		assertMigratedStructure(originResource, BLOCK_ISENCAPSULATED, purposeDefaultValue);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, purposeDefaultValue);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			// 2 differences related to viewPointChange: Attribute change refining a StereotypeAttributeChange
			assertEquals(2, differences.size());
			assertViewPointAttributeChange(comparison, originResource, false);
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests the behavior if the a wrong profile definition is selected which does not contain the missing
	 * stereotype. As a result, the unmigratable stereotypes are deleted by the Papyrus model repair
	 * mechanism. For this example we use the following definitions for the origin model:
	 * <ul>
	 * <li>Block definition: http://www.eclipse.org/uml2/5.0.0/UML/WrongProfile/Standard/1</li>
	 * </ul>
	 * This definition is close enough to the UML Standard Profile with package URI
	 * http://www.eclipse.org/uml2/5.0.0/UML/Profile/Standard to automatically migrate the missing Block
	 * stereotype. As the UML Standard Profile does not contain a definition for Block, the stereotype is
	 * deleted since the correct Block class can not be determined. We therefore expect the comparison to
	 * yield an error since the profile definition changed and we can not handle it accordingly.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/uri/wrongmigrated/abb/left.uml", right = "data/sysml/uri/wrongmigrated/abb/right.uml", ancestor = "data/sysml/uri/wrongmigrated/abb/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testWrongMigratedPackageURIABB(final Comparison comparison,
			final CompareTestSupport support) {
		if (ProfileNamespaceURIPatternAPI.isAvailable()) {
			// With the ProfileNamespaceURIPatternAPI, we don't perform string matching and therefore do not
			// return a wrong profile. Instead, no profile is returned, leaving the Block stereotype intact.
			return;
		}
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration

		assertMigration(originResource); // success: ViewPoint package, wrong success: Block package
		assertNoMigration(leftResource);
		assertNoMigration(rightResource);

		// test structure of models

		final Class umlClass = assertPartiallyMigratedUmlClass(originResource, 1);
		assertMigratedViewPoint(umlClass, VIEWPOINT_PURPOSE);

		// Block was migrated to a wrong package, because the URI was not helpful enough
		final Block blockApplication = UMLUtil.getStereotypeApplication(umlClass, Block.class);
		assertNull("Block should have been deleted.", blockApplication);
		// wrong migration -> deletion, so also no AnyType present
		assertFalse(Iterables.tryFind(originResource.getContents(), Predicates.instanceOf(AnyType.class))
				.isPresent());

		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// there are no conflicts and no differences since the comparison was not completed
			assertEquals(Diagnostic.ERROR, comparison.getDiagnostic().getSeverity());
			assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
			assertTrue("Unexpected differences: " + differences, differences.isEmpty());
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests the behavior if the profile definitions can not be migrated, but a wrong profile definition is
	 * selected instead. As a result, the unmigratable stereotypes are deleted by the Papyrus model repair
	 * mechanism. For this example we use the following definitions for the origin model:
	 * <ul>
	 * <li>Block definition: http://www.eclipse.org/fake/0/Lang/Fakes/1</li>
	 * </ul>
	 * Since we cannot find a proper profile containing such a package, the unresolved {@link AnyType}s remain
	 * in the resources. We expect the comparison to yield an error since the profile definition changed and
	 * we can not handle it accordingly.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/uri/notmigrated/abb/left.uml", right = "data/sysml/uri/notmigrated/abb/right.uml", ancestor = "data/sysml/uri/notmigrated/abb/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testNotMigratedPackageURIABB(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertPartialMigration(originResource, 1, 1); // could not migrate block
		assertNoMigration(leftResource);
		assertNoMigration(rightResource);

		// test structure of models
		assertPartiallyMigratedStructure(originResource, false, BLOCK_ISENCAPSULATED, true,
				VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// there are no conflicts and no differences since the comparison was not completed
		assertEquals(Diagnostic.ERROR, comparison.getDiagnostic().getSeverity());
		assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
		assertTrue("Unexpected differences: " + differences, differences.isEmpty());
	}

	/**
	 * Tests the behavior if the profile definitions can not be migrated, but a wrong profile definition is
	 * selected instead. As a result, the unmigratable stereotypes are deleted by the Papyrus model repair
	 * mechanism. For this example we use the following definitions for the left and origin model:
	 * <ul>
	 * <li>Block definition: http://www.eclipse.org/fake/0/Lang/Fakes/1</li>
	 * </ul>
	 * Since we cannot find a proper profile containing such a package, the unresolved {@link AnyType}s remain
	 * in the resources. We expect the comparison to yield an error since the profile definition changed and
	 * we can not handle it accordingly.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/uri/notmigrated/aab/left.uml", right = "data/sysml/uri/notmigrated/aab/right.uml", ancestor = "data/sysml/uri/notmigrated/aab/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testNotMigratedPackageURIAAB(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertPartialMigration(originResource, 1, 1);
		assertPartialMigration(leftResource, 1, 1);
		assertNoMigration(rightResource);

		// test structure of models
		assertPartiallyMigratedStructure(originResource, false, BLOCK_ISENCAPSULATED, true,
				VIEWPOINT_PURPOSE);
		assertPartiallyMigratedStructure(leftResource, false, BLOCK_ISENCAPSULATED, true, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// there are no conflicts and no differences since the comparison was not completed
		assertEquals(Diagnostic.ERROR, comparison.getDiagnostic().getSeverity());
		assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
		assertTrue("Unexpected differences: " + differences, differences.isEmpty());
	}

	/**
	 * Tests the behavior if the profile definitions follow not the expected URI scheme. As a result,
	 * stereotype applications are grouped incorrectly and deleted by the Papyrus model repair mechanism. For
	 * this example we use the following definitions for the origin model:
	 * <ul>
	 * <li>Block definition: http://www.eclipse.org/papyrus/0.6.0/SysML/Blocks</li>
	 * <li>ViewPoint definition: http://www.eclipse.org/papyrus/0.6.0/SysML/ModelElements</li>
	 * </ul>
	 * As a result, both definitions are assumed to be the same and the we only migrate stereotypes of the
	 * first found package. The remaining stereotypes can not be migrated with this package (as the definition
	 * would be in another package) and are deleted automatically together with their profile application. We
	 * therefore expect one stereotype to be added to the left and right model and therefore get differences
	 * and PSEUDO conflicts in the comparison.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/uri/wrongformat/abb/left.uml", right = "data/sysml/uri/wrongformat/abb/right.uml", ancestor = "data/sysml/uri/wrongformat/abb/origin.uml", resourceSetHooks = ProfileMigrationHook.class)
	public void testWrongFormatPackageURIsABB(final Comparison comparison, final CompareTestSupport support) {
		if (ProfileNamespaceURIPatternAPI.isAvailable()) {
			// With the ProfileNamespaceURIPatternAPI, we don't perform string matching and therefore do not
			// return a profile for URIs we cannot match. Instead, no profile is returned, resulting in an
			// unsuccessful migration and not a wrong correct one.
			return;
		}
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertMigration(originResource); // success: ViewPoint package, wrong success: Block package
		assertNoMigration(leftResource);
		assertNoMigration(rightResource);

		// test structure of models
		final Class umlClass = assertPartiallyMigratedUmlClass(originResource, 1);
		// deletion is non-deterministic, sometimes Block is migrated, sometimes ViewPoint
		final ViewPoint viewPointApplication = UMLUtil.getStereotypeApplication(umlClass, ViewPoint.class);
		final Block blockApplication = UMLUtil.getStereotypeApplication(umlClass, Block.class);
		java.lang.Class<? extends EObject> missingStereotypeClass = null;
		if (viewPointApplication == null) {
			// Block got migrated
			missingStereotypeClass = ViewPoint.class;
			assertNull("ViewPoint should have been deleted.", viewPointApplication);
			assertMigratedBlock(umlClass, BLOCK_ISENCAPSULATED);
		} else {
			// ViewPoint got migrated
			missingStereotypeClass = Block.class;
			assertNull("Block should have been deleted.", blockApplication);
			assertMigratedViewPoint(umlClass, VIEWPOINT_PURPOSE);
		}
		// no anytypes left as the stereotype got deleted and not only "not migrated"
		assertFalse(Iterables.tryFind(originResource.getContents(), Predicates.instanceOf(AnyType.class))
				.isPresent());

		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// test differences and conflicts
		if (!PapyrusMigrationUtil.isLuna()) {
			// 4 PSEUDO conflict: stereotype application (2)
			assertEquals((conflicts.size() - 2) + " unexpected conflicts", 2, conflicts.size());
			assertTrue(Iterables.all(conflicts,
					EMFComparePredicates.containsConflictOfTypes(ConflictKind.PSEUDO)));

			// 2 differences on both sides related to stereotype removal: ResourceAttachmentChange and
			// ReferenceChange refining a StereotypeApplicationChange
			assertEquals(6, differences.size());
			assertStereotypeAddition(comparison, rightResource, missingStereotypeClass, true); // both sides
		} else {
			// In the model repair mechanism of Luna, IDs were not preserved so we detect a lot of differences
			// and PSEUDO conflicts through additions and removals as the elements are not matched as expected
		}
	}

	/**
	 * Tests the behavior without the profile migration capabilities, i.e., without the
	 * {@link ProfileMigrationHook}. We expect the comparison to yield an error since the profile definition
	 * changed and we can not handle it accordingly.
	 * 
	 * @param comparison
	 *            comparison object between the models
	 * @param support
	 *            exposed by the testing framework
	 */
	@Compare(left = "data/sysml/version/aba/left.uml", right = "data/sysml/version/aba/right.uml", ancestor = "data/sysml/version/aba/origin.uml")
	public void testErrorWithoutMigration(final Comparison comparison, final CompareTestSupport support) {
		final List<Diff> differences = comparison.getDifferences();
		final EList<Conflict> conflicts = comparison.getConflicts();

		// does not use ProfileMigrationHook
		final Resource originResource = support.getAncestorResource();
		final Resource leftResource = support.getLeftResource();
		final Resource rightResource = support.getRightResource();

		// test migration
		assertNoMigration(originResource);
		assertNoMigration(leftResource);
		assertNoMigration(rightResource);

		// test structure of models
		assertUnmigratedStructure(originResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertMigratedStructure(leftResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);
		assertUnmigratedStructure(rightResource, BLOCK_ISENCAPSULATED, VIEWPOINT_PURPOSE);

		// there are no conflicts and no differences since the comparison was not completed
		assertEquals(Diagnostic.ERROR, comparison.getDiagnostic().getSeverity());
		assertTrue("Unexpected conflicts: " + conflicts, conflicts.isEmpty());
		assertTrue("Unexpected differences: " + differences, differences.isEmpty());
	}
}
