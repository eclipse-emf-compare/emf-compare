package org.eclipse.emf.compare.uml2.diff.internal.extension.profile;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeReferenceChangeRightTarget;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeReferenceChangeRightTargetFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeReferenceChangeRightTargetFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof ReferenceChangeRightTarget) {
			EObject left = ((ReferenceChangeRightTarget)input).getLeftElement();
			EObject right = ((ReferenceChangeRightTarget)input).getRightElement();
			EObject leftBase = UMLUtil.getBaseElement(left);
			EObject rightBase = UMLUtil.getBaseElement(right);
			return leftBase != null && rightBase != null;
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input) {
		ReferenceChangeRightTarget referenceChangeRightTarget = (ReferenceChangeRightTarget)input;
		EObject leftElement = referenceChangeRightTarget.getLeftElement();
		EObject rightElement = referenceChangeRightTarget.getRightElement();
		EObject leftBase = UMLUtil.getBaseElement(leftElement);
		EObject rightBase = UMLUtil.getBaseElement(rightElement);

		UMLStereotypeReferenceChangeRightTarget ret = UML2DiffFactory.eINSTANCE
				.createUMLStereotypeReferenceChangeRightTarget();

		ret.setStereotype(UMLUtil.getStereotype(rightElement));
		ret.setRemote(input.isRemote());

		ret.setReference(referenceChangeRightTarget.getReference());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		ret.setRightTarget(referenceChangeRightTarget.getRightTarget());

		ret.getHideElements().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input) {
		EObject right = ((ReferenceChangeRightTarget)input).getRightElement();
		EObject rightBase = UMLUtil.getBaseElement(right);

		DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		return findOrCreateDiffGroup(rootDiffGroup, rightBase);
	}
}
