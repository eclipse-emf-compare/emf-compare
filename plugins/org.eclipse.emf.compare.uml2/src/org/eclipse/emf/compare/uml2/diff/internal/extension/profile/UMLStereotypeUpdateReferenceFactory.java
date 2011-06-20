package org.eclipse.emf.compare.uml2.diff.internal.extension.profile;

import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.AbstractDiffExtensionFactory;
import org.eclipse.emf.compare.uml2diff.UML2DiffFactory;
import org.eclipse.emf.compare.uml2diff.UMLStereotypeUpdateReference;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.util.UMLUtil;

public class UMLStereotypeUpdateReferenceFactory extends AbstractDiffExtensionFactory {

	public UMLStereotypeUpdateReferenceFactory(UML2DiffEngine engine,
			EcoreUtil.CrossReferencer crossReferencer) {
		super(engine, crossReferencer);
	}

	public boolean handles(DiffElement input) {
		if (input instanceof UpdateReference) {
			EObject left = ((UpdateReference)input).getLeftElement();
			EObject right = ((UpdateReference)input).getRightElement();
			EObject leftBase = UMLUtil.getBaseElement(left);
			EObject rightBase = UMLUtil.getBaseElement(right);
			return leftBase != null && rightBase != null;
		}
		return false;
	}

	public AbstractDiffExtension create(DiffElement input) {
		UpdateReference updateReference = (UpdateReference)input;
		EObject leftElement = updateReference.getLeftElement();
		EObject rightElement = updateReference.getRightElement();
		EObject leftBase = UMLUtil.getBaseElement(leftElement);
		EObject rightBase = UMLUtil.getBaseElement(rightElement);

		UMLStereotypeUpdateReference ret = UML2DiffFactory.eINSTANCE.createUMLStereotypeUpdateReference();

		ret.setStereotype(UMLUtil.getStereotype(rightElement));
		ret.setRemote(input.isRemote());

		ret.setReference(updateReference.getReference());
		ret.setLeftElement(leftBase);
		ret.setRightElement(rightBase);
		ret.setRightTarget(updateReference.getRightTarget());
		ret.setLeftTarget(updateReference.getLeftTarget());

		ret.getHideElements().add(input);

		return ret;
	}

	@Override
	public DiffElement getParentDiff(DiffElement input) {
		EObject right = ((UpdateReference)input).getRightElement();
		EObject rightBase = UMLUtil.getBaseElement(right);

		DiffModel rootDiffGroup = (DiffModel)EcoreUtil.getRootContainer(input);

		return findOrCreateDiffGroup(rootDiffGroup, rightBase);
	}
}
