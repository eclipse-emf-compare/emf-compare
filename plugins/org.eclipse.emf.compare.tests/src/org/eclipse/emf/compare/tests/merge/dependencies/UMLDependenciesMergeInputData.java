package org.eclipse.emf.compare.tests.merge.dependencies;

import java.io.IOException;

import org.eclipse.emf.compare.tests.merge.data.AbstractInputData;
import org.eclipse.emf.ecore.EObject;

public class UMLDependenciesMergeInputData extends AbstractInputData {

	public EObject getOriginalModelElementChangeLeft() throws IOException {
		return loadFromClassloader("modelElementChangeLeft/original/model.uml"); //$NON-NLS-1$
	}

	public EObject getModifiedModelElementChangeLeft() throws IOException {
		return loadFromClassloader("modelElementChangeLeft/modified/model.uml"); //$NON-NLS-1$
	}

	public EObject getOriginalReferenceChangeLeftRightTarget() throws IOException {
		return loadFromClassloader("referenceChangeLeftRightTarget/original/model.uml"); //$NON-NLS-1$
	}

	public EObject getModifiedReferenceChangeLeftRightTarget() throws IOException {
		return loadFromClassloader("referenceChangeLeftRightTarget/modified/model.uml"); //$NON-NLS-1$
	}

	public EObject getOriginalReferenceChangeLeftTarget() throws IOException {
		return loadFromClassloader("referenceChangeLeftTarget/original/model.uml"); //$NON-NLS-1$
	}

	public EObject getModifiedReferenceChangeLeftTarget() throws IOException {
		return loadFromClassloader("referenceChangeLeftTarget/modified/model.uml"); //$NON-NLS-1$
	}

	public EObject getOriginalUpdateReferenceLeft() throws IOException {
		return loadFromClassloader("updateReferenceLeft/original/model.uml"); //$NON-NLS-1$
	}

	public EObject getModifiedUpdateReferenceLeft() throws IOException {
		return loadFromClassloader("updateReferenceLeft/modified/model.uml"); //$NON-NLS-1$
	}

	public EObject getOriginalUpdateReferenceLeftAndRight() throws IOException {
		return loadFromClassloader("updateReferenceLeftAndRight/original/model.uml"); //$NON-NLS-1$
	}

	public EObject getModifiedUpdateReferenceLeftAndRight() throws IOException {
		return loadFromClassloader("updateReferenceLeftAndRight/modified/model.uml"); //$NON-NLS-1$
	}

}
