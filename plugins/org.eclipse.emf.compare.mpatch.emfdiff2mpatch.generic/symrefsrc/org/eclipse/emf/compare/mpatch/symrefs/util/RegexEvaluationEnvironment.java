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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ecore.EcoreEvaluationEnvironment;

class RegexEvaluationEnvironment extends EcoreEvaluationEnvironment {
	RegexEvaluationEnvironment() {
		super();
	}

	RegexEvaluationEnvironment(
			EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> parent) {
		super(parent);
	}

	public Object callOperation(EOperation operation, int opcode, Object source, Object[] args) {
		if (operation.getEAnnotation("RegexEnvironment") == null) {
			// not one of our custom operations
			return super.callOperation(operation, opcode, source, args);
		}

		if ("regexMatch".equals(operation.getName())) {
			Pattern pattern = Pattern.compile((String) args[0]);
			Matcher matcher = pattern.matcher((String) source);
			return matcher.matches() ? matcher.group() : null;

		} else if ("contains".equals(operation.getName())) {
			if (source == null)
				return args[0] == null;
			return ((String) source).contains((String) args[0]);

		} else if ("containsIgnoreCase".equals(operation.getName())) {
			if (source == null)
				return args[0] == null;
			return ((String) source).toLowerCase().contains(((String) args[0]).toLowerCase());

		} else {

			throw new UnsupportedOperationException("Unknown operation: " + operation.getName()); // unknown operation
		}
	}
}
