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
package org.eclipse.emf.compare.mpatch.symrefs.util;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.ocl.EnvironmentFactory;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironment;
import org.eclipse.ocl.ecore.SendSignalAction;


class RegexEnvironment extends EcoreEnvironment {
    private EOperation regexMatch;
    private EOperation contains;
    private EOperation containsIgnoreCase;
    private EOperation checkSimilarity;
    
    final static String REGEX_MATCH_OPERATION_NAME = "regexMatch";
    final static String CONTAINS_OPERATION_NAME = "contains";
    final static String CONTAINS_IGNORE_CASE_OPERATION_NAME = "containsIgnoreCase";
	final static String CHECK_SIMILARITY_OPERATION_NAME = "checkSimilarity";
    
    // this constructor is used to initialize the root environment
    RegexEnvironment(EPackage.Registry registry) {
        super(registry);
        
        defineCustomOperations();
    }
    
    // this constructor is used to initialize child environments
    RegexEnvironment(RegexEnvironment parent) {
        super(parent);
        
        // get the parent's custom operations
        regexMatch = parent.regexMatch;
        contains = parent.contains;
        containsIgnoreCase = parent.containsIgnoreCase;
        checkSimilarity = parent.checkSimilarity;
    }
	
	// override this to provide visibility of the inherited protected method
    @Override
    protected void setFactory(
            EnvironmentFactory<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> factory) {
        super.setFactory(factory);
    }
    
    // use the AbstractEnvironment's mechanism for defining "additional operations"
    // to add our custom operation to OCL's String primitive type
	private void defineCustomOperations() {
        // pattern-matching operation
        regexMatch = EcoreFactory.eINSTANCE.createEOperation();
        regexMatch.setName(REGEX_MATCH_OPERATION_NAME);
        regexMatch.setEType(getOCLStandardLibrary().getString());
        EParameter parm = EcoreFactory.eINSTANCE.createEParameter();
        parm.setName("pattern");
        parm.setEType(getOCLStandardLibrary().getString());
        regexMatch.getEParameters().add(parm);
        
        // contains operation
        contains = EcoreFactory.eINSTANCE.createEOperation();
        contains.setName(CONTAINS_OPERATION_NAME);
        contains.setEType(getOCLStandardLibrary().getBoolean());
        parm = EcoreFactory.eINSTANCE.createEParameter();
        parm.setName("str");
        parm.setEType(getOCLStandardLibrary().getString());
        contains.getEParameters().add(parm);
        
        // contains ignore case operation
        containsIgnoreCase = EcoreFactory.eINSTANCE.createEOperation();
        containsIgnoreCase.setName(CONTAINS_IGNORE_CASE_OPERATION_NAME);
        containsIgnoreCase.setEType(getOCLStandardLibrary().getBoolean());
        parm = EcoreFactory.eINSTANCE.createEParameter();
        parm.setName("str");
        parm.setEType(getOCLStandardLibrary().getString());
        containsIgnoreCase.getEParameters().add(parm);
        
        // is similar operation
        checkSimilarity = EcoreFactory.eINSTANCE.createEOperation();
        checkSimilarity.setName(CHECK_SIMILARITY_OPERATION_NAME);
        checkSimilarity.setEType(getOCLStandardLibrary().getBoolean());
        parm = EcoreFactory.eINSTANCE.createEParameter();
        parm.setName("other");
        parm.setEType(getOCLStandardLibrary().getString());
        checkSimilarity.getEParameters().add(parm);
        parm = EcoreFactory.eINSTANCE.createEParameter();
        parm.setName("similarity");
        parm.setEType(getOCLStandardLibrary().getReal());
        checkSimilarity.getEParameters().add(parm);
        
        // annotate it so that we will recognize it in the evaluation environment
        EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
        annotation.setSource("RegexEnvironment");
        regexMatch.getEAnnotations().add(annotation);
        annotation = EcoreFactory.eINSTANCE.createEAnnotation();
        annotation.setSource("RegexEnvironment");
        contains.getEAnnotations().add(annotation);
        annotation = EcoreFactory.eINSTANCE.createEAnnotation();
        annotation.setSource("RegexEnvironment");
        containsIgnoreCase.getEAnnotations().add(annotation);
        annotation = EcoreFactory.eINSTANCE.createEAnnotation();
        annotation.setSource("RegexEnvironment");
        checkSimilarity.getEAnnotations().add(annotation);
        
        // define it as an additional operation on OCL String
        addHelperOperation(getOCLStandardLibrary().getString(), regexMatch);
        addHelperOperation(getOCLStandardLibrary().getString(), contains);
        addHelperOperation(getOCLStandardLibrary().getString(), containsIgnoreCase);
        addHelperOperation(getOCLStandardLibrary().getString(), checkSimilarity);
    }
}
