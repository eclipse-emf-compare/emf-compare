package org.eclipse.emf.compare.example.library.library.presentation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.compare.diff.merge.service.MergeService;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.editor.ModelCompareEditorInput;
import org.eclipse.emf.compare.ui.services.CompareServices;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class WorkspaceSync {

	protected ResourceSet ancestor = new ResourceSetImpl();

	protected ResourceSet editor = new ResourceSetImpl();

	/**
	 * The editor notify us that its state is synchronized with the Workspace state.
	 * 
	 * @param set
	 *            the models loaded in the editor.
	 */
	public void synchronizationCheckpoint(ResourceSet set) {
		editor = set;
		ancestor = new ResourceSetImpl();
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		for (Resource editorResource : set.getResources()) {
			Resource ancestorResource = ancestor.createResource(editorResource.getURI());
			for (EObject root : editorResource.getContents()) {
				ancestorResource.getContents().add(copier.copy(root));
			}
		}

	}

	protected void importChangesFromWorkspace(Collection<Resource> changedResources)
			throws InterruptedException, PartInitException, InvocationTargetException {

		/*
		 * Let's retrieve the model elements delta from the workspace versions
		 */

		ComparisonResourceSetSnapshot snap = compareModels();

		List<DiffElement> deltas = getAllDiffs(snap);

		if (hasConflicts(deltas)) {
			if (MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					"Conflict with workspace", //$NON-NLS-1$
					"Your changes are conflicting with others from the workspace, do you want to see them ?")) { //$NON-NLS-1$
				openComparisonEditor(snap);
			}

		} else {
			for (DiffElement delta : deltas) {
				if (delta.isRemote()) {
					MergeService.merge(delta, false);
				} else {
					MergeService.merge(delta, true);
				}
			}
		}

	}

	protected ComparisonResourceSetSnapshot compareModels() throws InterruptedException {
		ResourceSet workspaceVersions = new ResourceSetImpl();
		for (Resource futureRes : editor.getResources()) {
			workspaceVersions.getResource(futureRes.getURI(), true);
		}

		MatchResourceSet match = MatchService.doResourceSetMatch(workspaceVersions, editor, ancestor,
				Collections.EMPTY_MAP);
		DiffResourceSet diff = DiffService.doDiff(match, true);
		ComparisonResourceSetSnapshot snap = DiffFactory.eINSTANCE.createComparisonResourceSetSnapshot();
		snap.setDiffResourceSet(diff);
		snap.setMatchResourceSet(match);
		return snap;
	}

	private List<DiffElement> getAllDiffs(ComparisonResourceSetSnapshot snap) {
		List<DiffElement> result = new ArrayList<DiffElement>();
		for (DiffModel diff : snap.getDiffResourceSet().getDiffModels()) {
			result.addAll(diff.getDifferences());
		}
		return result;
	}

	protected void openComparisonEditor(ComparisonResourceSetSnapshot snap) throws InterruptedException,
			PartInitException, InvocationTargetException {
		ModelCompareEditorInput input = new ModelCompareEditorInput(snap);
		CompareServices.openEditor(input, Collections.EMPTY_LIST);
	}

	private boolean hasConflicts(List<DiffElement> deltas) {
		for (DiffElement diffElement : deltas) {
			if (diffElement.isConflicting()) {
				return true;
			}
		}
		return false;

	}

}
