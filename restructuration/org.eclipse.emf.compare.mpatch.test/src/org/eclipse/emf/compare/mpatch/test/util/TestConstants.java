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
package org.eclipse.emf.compare.mpatch.test.util;

import java.util.Collection;

import org.eclipse.emf.compare.mpatch.common.util.ExtensionManager;
import org.eclipse.emf.compare.mpatch.extension.IMPatchApplication;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;


/**
 * Test constants:
 * <ul>
 * <li>Location of (all) test case files
 * <li>URIs for all test case files
 * <li>Singleton instance of {@link IMPatchApplication}
 * <li>List of {@link ISymbolicReferenceCreator}s for testing
 * <li>List of {@link IModelDescriptorCreator}s for testing
 * </ul>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 *
 */
public class TestConstants {

	/** Prefix for workspace test files. */
	public static final String PREFIX_WORKSPACE = "platform:/resource/org.eclipse.emf.compare.mpatch.test/tests/";

	/** Prefix for plugin test files. */
	public static final String PREFIX_PLUGIN = "platform:/plugin/org.eclipse.emf.compare.mpatch.test/tests/";

	/** Unchanged version of a simple test model containing just one changed attribute. */
	public static final String SIMPLE_URI1 = "simple/unchanged.eachonce";

	/** Changed version of a simple test model containing just one changed attribute. */
	public static final String SIMPLE_URI2 = "simple/changed.eachonce";

	/** Unchanged version of a more complex test model covering all kinds of changes. */
	public static final String EACHONCE_URI1 = "eachonce/unchanged.eachonce";

	/** Changed version of a more complex test model covering all kinds of changes. */
	public static final String EACHONCE_URI2 = "eachonce/changed.eachonce";

	/** Unchanged version of a test model covering different dependecies between changes. */
	public static final String DEPENDENCY_URI1 = "dependencies/unchanged.eachonce";

	/** Changed version of a test model covering different dependecies between changes. */
	public static final String DEPENDENCY_URI2 = "dependencies/changed.eachonce";

	/** Unchanged version of a model containing a dependency cycle. */
	public static final String DEP_CYCLE_URI1 = "dep_cycle/unchanged.ecore";

	/** Changed version of a model containing a dependency cycle. */
	public static final String DEP_CYCLE_URI2 = "dep_cycle/changed.ecore";

	/** A simple emfdiff file containing a conflicting diff element. */
	public static final String CONFLICT_EMFDIFF_URI = "conflicts/conflict.emfdiff";

	/** Unchanged version of a simple ecore model which is the basis for a performance test. */
	public static final String PERFORMANCE_URI1 = "performance/unchanged.ecore";

	/** Changed version of a simple ecore model which is the basis for a performance test. */
	public static final String PERFORMANCE_URI2 = "performance/changed.ecore";

	/**
	 * Unchanged version of an ecore model with focus on datatypes and references to datatypes outside the current
	 * resource.
	 */
	public static final String ECORE_URI1 = "ecore/unchanged.ecore";

	/**
	 * Unchanged version of an ecore model with focus on datatypes and references to datatypes outside the current
	 * resource.
	 */
	public static final String ECORE_URI2 = "ecore/changed.ecore";

	/** Unchanged version of a uml model covering all but attribute add/del changes. */
	public static final String UML_URI1 = "uml/unchanged.uml";

	/** Changed version of a uml model covering all but attribute add/del changes. */
	public static final String UML_URI2 = "uml/changed.uml";

	/** Unchanged version of a uml model without an association. */
	public static final String UML_ASSOC_URI1 = "uml/ref_unchanged.uml";

	/** Changed version of a uml model having an added association. */
	public static final String UML_ASSOC_URI2 = "uml/ref_changed.uml";

	/** Unchanged version of model for applying a change multiple times. */
	public static final String MULTI_REF_URI1 = "multiref/multi_unchanged.eachonce";
	
	/** Changed version of model for applying a change multiple times. */
	public static final String MULTI_REF_URI2 = "multiref/multi_changed.eachonce";
	
	/** The differences describing the generalized adding of a references. */
	public static final String MULTI_REF_DIFF_URI = "multiref/multiref.mpatch";
	
	/** Unchanged version of model for internal reference test. */
	public static final String INTERNAL_REF_URI1 = "internal_refs/unchanged.ecore";

	/** Changed version of model for internal reference test. */
	public static final String INTERNAL_REF_URI2 = "internal_refs/changed.ecore";

	/** Model to which the internal referenced diffs are applied. */
	public static final String INTERNAL_REF_URI3 = "internal_refs/apply.ecore";

	/** Unchanged version of model for library example test. */
	public static final String LIBRARY_URI1 = "platform:/plugin/org.eclipse.emf.compare.mpatch.example/library/library.ecore";

	/** Changed version of model for library example test. */
	public static final String LIBRARY_URI2 = "platform:/plugin/org.eclipse.emf.compare.mpatch.example/library/library_karl.ecore";

	/** Model version for application of mpatch for library example test. */
	public static final String LIBRARY_URI3 = "platform:/plugin/org.eclipse.emf.compare.mpatch.example/library/library_eve.ecore";

	/** Unchanged version of EMF model for merging changes. */
	public static final String MERGE_EMF_URI1 = "merge/unchanged.ecore";

	/** Changed version of EMF model for merging changes. */
	public static final String MERGE_EMF_URI2 = "merge/changed.ecore";

	/** Unchanged version of eachonce model for merging changes. */
	public static final String MERGE_EACHONCE_URI1 = "merge/unchanged.eachonce";

	/** Changed version of eachonce model for merging changes. */
	public static final String MERGE_EACHONCE_URI2 = "merge/changed.eachonce";

	/** Unchanged version of eachonce model for individual add element change. */
	public static final String INDIVIDUAL_ADD_REM_ELEMENT_URI1 = "individual/add_rem_element1.eachonce";
	/** Changed version of eachonce model for individual add element change. */
	public static final String INDIVIDUAL_ADD_REM_ELEMENT_URI2 = "individual/add_rem_element2.eachonce";
	/** Unchanged version of eachonce model for individual move element change. */
	public static final String INDIVIDUAL_MOVE_ELEMENT_URI1 = "individual/move_element1.eachonce";
	/** Changed version of eachonce model for individual move element change. */
	public static final String INDIVIDUAL_MOVE_ELEMENT_URI2 = "individual/move_element2.eachonce";
	/** Unchanged version of eachonce model for individual add attribute change. */
	public static final String INDIVIDUAL_ADD_REM_ATTRIBUTE_URI1 = "individual/add_rem_attribute1.eachonce";
	/** Changed version of eachonce model for individual add attribute change. */
	public static final String INDIVIDUAL_ADD_REM_ATTRIBUTE_URI2 = "individual/add_rem_attribute2.eachonce";
	/** Unchanged version of eachonce model for individual update attribute change. */
	public static final String INDIVIDUAL_UPDATE_ATTRIBUTE_URI1 = "individual/update_attribute1.eachonce";
	/** Changed version of eachonce model for individual update attribute change. */
	public static final String INDIVIDUAL_UPDATE_ATTRIBUTE_URI2 = "individual/update_attribute2.eachonce";
	/** Unchanged version of eachonce model for individual add reference change. */
	public static final String INDIVIDUAL_ADD_REM_REFERENCE_URI1 = "individual/add_rem_reference1.eachonce";
	/** Changed version of eachonce model for individual add reference change. */
	public static final String INDIVIDUAL_ADD_REM_REFERENCE_URI2 = "individual/add_rem_reference2.eachonce";
	/** Unchanged version of eachonce model for individual update reference change. */
	public static final String INDIVIDUAL_UPDATE_REFERENCE_URI1 = "individual/update_reference1.eachonce";
	/** Changed version of eachonce model for individual update reference change. */
	public static final String INDIVIDUAL_UPDATE_REFERENCE_URI2 = "individual/update_reference2.eachonce";
	
	/** The diff applier used in the tests. */
	public static IMPatchApplication DIFF_APPLIER = ExtensionManager.getSelectedApplication();

	/** Symbolic reference creators. */
	public static Collection<ISymbolicReferenceCreator> SYM_REF_CREATORS = ExtensionManager.getAllSymbolicReferenceCreators().values();

	/** Model descriptor creators. */
	public static Collection<IModelDescriptorCreator> MODEL_DESCRIPTOR_CREATORS = ExtensionManager.getAllModelDescriptorCreators().values();
}
