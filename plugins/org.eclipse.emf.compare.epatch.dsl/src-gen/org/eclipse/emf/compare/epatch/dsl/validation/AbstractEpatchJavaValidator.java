package org.eclipse.emf.compare.epatch.dsl.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.compare.epatch.EpatchPackage;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;
import org.eclipse.xtext.validation.ComposedChecks;

@ComposedChecks(validators = {org.eclipse.xtext.validation.ImportUriValidator.class})
public class AbstractEpatchJavaValidator extends AbstractDeclarativeValidator {

	@Override
	protected List<? extends EPackage> getEPackages() {
		List<EPackage> result = new ArrayList<EPackage>();
		result.add(EpatchPackage.eINSTANCE);
		return result;
	}

}
