package org.eclipse.emf.compare.tests.unit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.ComparePackage;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.Equivalence;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.util.CompareAdapterFactory;
import org.junit.Test;

/*
 * TODO This is but a skeleton for the tests of CompareAdapterFactory.
 * Set as "generated NOT" and override each test if you overrode the default generated
 * behavior.
 */
/**
 * Tests the behavior of the {@link CompareAdapterFactory generated adapter factory} for package compare.
 * 
 * @generated
 */
public class CompareAdapterFactoryTest {
	/**
	 * Ensures that creating adapters for {@link Comparison} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateComparisonAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createComparisonAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createComparison()));
	}

	/**
	 * Ensures that creating adapters for {@link MatchResource} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateMatchResourceAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createMatchResourceAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createMatchResource()));
	}

	/**
	 * Ensures that creating adapters for {@link Match} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateMatchAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createMatchAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createMatch()));
	}

	/**
	 * Ensures that creating adapters for {@link Diff} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateDiffAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createDiffAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createDiff()));
	}

	/**
	 * Ensures that creating adapters for {@link ResourceAttachmentChange} can be done through the
	 * AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateResourceAttachmentChangeAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createResourceAttachmentChangeAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createResourceAttachmentChange()));
	}

	/**
	 * Ensures that creating adapters for {@link ReferenceChange} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateReferenceChangeAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createReferenceChangeAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createReferenceChange()));
	}

	/**
	 * Ensures that creating adapters for {@link AttributeChange} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateAttributeChangeAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createAttributeChangeAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createAttributeChange()));
	}

	/**
	 * Ensures that creating adapters for {@link Conflict} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateConflictAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createConflictAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createConflict()));
	}

	/**
	 * Ensures that creating adapters for {@link Equivalence} can be done through the AdapterFactory.
	 * 
	 * @generated
	 */
	@Test
	public void testCreateEquivalenceAdapter() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertNull(adapterFactory.createEquivalenceAdapter());
		assertNull(adapterFactory.createAdapter(CompareFactory.eINSTANCE.createEquivalence()));
	}

	/**
	 * Ensures that the AdapterFactory knows all classes of package compare.
	 * 
	 * @generated
	 */
	@Test
	public void testIsFactoryForType() {
		CompareAdapterFactory adapterFactory = new CompareAdapterFactory();
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createComparison()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createMatchResource()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createMatch()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createDiff()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createResourceAttachmentChange()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createReferenceChange()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createAttributeChange()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createConflict()));
		assertTrue(adapterFactory.isFactoryForType(CompareFactory.eINSTANCE.createEquivalence()));
		assertTrue(adapterFactory.isFactoryForType(ComparePackage.eINSTANCE));
		org.eclipse.emf.ecore.EClass eClass = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEClass();
		assertFalse(adapterFactory.isFactoryForType(eClass));
		assertFalse(adapterFactory.isFactoryForType(new Object()));
	}
}
