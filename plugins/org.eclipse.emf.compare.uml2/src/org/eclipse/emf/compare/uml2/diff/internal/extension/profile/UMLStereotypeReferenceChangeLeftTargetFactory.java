package org.eclipse.emf.compare.uml2.diff.internal.extension.profile;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeLeftTarget;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeReferenceChangeLeftTargetFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeReferenceChangeLeftTargetFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof ReferenceChangeLeftTarget) {
			EObject left = ((ReferenceChangeLeftTarget)input).getLeftElement();
			EObject right = ((ReferenceChangeLeftTarget)input).getRightElement();
			EObject leftBase = UMLUtil.getBaseElement(left);
			EObject rightBase = UMLUtil.getBaseElement(right);
			return leftBase != null && rightBase != null;
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ReferenceChangeLeftTarget referenceChangeLeftTarget = (ReferenceChangeLeftTarget)input;
		EObject leftElement = referenceChangeLeftTarget.getLeftElement();
		EObject rightElement = referenceChangeLeftTarget.getRightElement();
		EObject leftBase = UMLUtil.getBaseElement(leftElement);
		EObject rightBase = UMLUtil.getBaseElement(rightElement);

		UMLStereotypeReferenceChangeLeftTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeReferenceChangeLeftTarget();

		ret.setStereotype(UMLUtil.getStereotype(leftElement));
		ret.setRemote(input.isRemote());

		ret.setReference(referenceChangeLeftTarget.getReference());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		ret.setLeftTarget(referenceChangeLeftTarget.getLeftTarget());

		ret.getHideElements().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input) {
		EObject right = ((ReferenceChangeLeftTarget)input).getRightElement();
		EObject rightBase = UMLUtil.getBaseElement(right);

		DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		return findOrCreateDiffGroup(rootDiffGroup, rightBase);
	}
}
