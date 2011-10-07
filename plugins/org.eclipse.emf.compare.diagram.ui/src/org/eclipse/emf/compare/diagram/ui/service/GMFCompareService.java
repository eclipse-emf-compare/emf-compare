/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.diagram.ui.mergeviewer.EditingDomainUtils;
import org.eclipse.emf.compare.diagram.ui.viewmodel.NotationDiffCreator;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Provide services related to diagrams handling.
 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
 */
public final class GMFCompareService {
	
	/**
	 * Structure to embed all the resources referenced by a difference model.
	 * @author Cedric Notot <a href="mailto:cedric.notot@obeo.fr">cedric.notot@obeo.fr</a>
	 */
	public static class ComparedResourceSets {	
		
		/** Left resources.*/
		private List<Resource> left = new ArrayList<Resource>();
		
		/** right resources.*/
		private List<Resource> right = new ArrayList<Resource>();
		
		/** ancestor resources.*/
		private List<Resource> ancestor = new ArrayList<Resource>();
		
		
		/**
		 * Returns the left.
		 *
		 * @return The left.
		 */
		public List<Resource> getLeft() {
			return left;
		}


		/**
		 * Sets the value of left to left.
		 *
		 * @param pLeft The left to set.
		 */
		public void setLeft(List<Resource> pLeft) {
			this.left = pLeft;
		}


		/**
		 * Returns the right.
		 *
		 * @return The right.
		 */
		public List<Resource> getRight() {
			return right;
		}


		/**
		 * Sets the value of right to right.
		 *
		 * @param pRight The right to set.
		 */
		public void setRight(List<Resource> pRight) {
			this.right = pRight;
		}


		/**
		 * Returns the ancestor.
		 *
		 * @return The ancestor.
		 */
		public List<Resource> getAncestor() {
			return ancestor;
		}


		/**
		 * Sets the value of ancestor to ancestor.
		 *
		 * @param pAncestor The ancestor to set.
		 */
		public void setAncestor(List<Resource> pAncestor) {
			this.ancestor = pAncestor;
		}


		/**
		 * Append the resources from a {@link ComparedResourceSets} in itself.
		 * @param resources 
		 * @return The appended {@link ComparedResourceSets}
		 */
		public ComparedResourceSets append(ComparedResourceSets resources) {
			left.addAll(resources.left);
			right.addAll(resources.right);
			ancestor.addAll(resources.ancestor);
			return this;
		}
	}
	
	/**
	 * Constructor.
	 */
	private GMFCompareService() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Layer differences, through decorators, on the GMFResources referenced by the specified difference model.
	 * @param diffModel 
	 * @return The decorated GMFResources.
	 * @throws IOException 
	 */
	public static ComparedResourceSets layerDifferences(final DiffResourceSet diffModel) throws IOException {
		final ComparedResourceSets comparedResourceSets = new ComparedResourceSets();
		createEAnnotations(diffModel);
		for (DiffModel diff : diffModel.getDiffModels()) {
			final GMFResource left = getGMFResource(diff, MatchSide.LEFT);
			if (left != null)
				comparedResourceSets.left.add(left);
			final GMFResource right = getGMFResource(diff, MatchSide.RIGHT);
			if (right != null)
				comparedResourceSets.right.add(right);
			final GMFResource ancestor = getGMFResource(diff, MatchSide.ANCESTOR);
			if (ancestor != null)
				comparedResourceSets.ancestor.add(left);
		}
		return comparedResourceSets;
	}
	
	/**
	 * Get the {@link GMFResource} from the difference model and the expected side.
	 * @param diff 
	 * @param side 
	 * @return GMFResource
	 */
	private static GMFResource getGMFResource(DiffModel diff, MatchSide side) {
		final EObject root = getFirstRoot(diff, side);
		if (root != null && root.eResource() instanceof GMFResource) {
			return (GMFResource)root.eResource();
		}
		return null;
	}
	
	/** Get the root model object in the resource referenced by the difference model, from the specified side.
	 * @param diff 
	 * @param side 
	 * @return The model object root.
	 */
	private static EObject getFirstRoot(DiffModel diff, MatchSide side) {
		List<EObject> roots = null;
		switch(side) {
			case LEFT:
				roots = diff.getLeftRoots();
				break;
			case RIGHT:
				roots = diff.getRightRoots();
				break;
			case ANCESTOR:
				roots = diff.getAncestorRoots();
				break;
			default:
		}
		if (roots != null && !roots.isEmpty()) {
			return roots.get(0);
		}
		return null;
	}
	
	/**
	 * Layer differences, through decorators, on the {@link input} GMFResources to compare.
	 * @param input 
	 * @param options Options for the match processing.
	 * @return The decorated GMFResources.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static ComparedResourceSets layerDifferences(ComparedResourceSets input, final Map<String, Object> options) throws InterruptedException, IOException {
				
		final List<Resource> left = input.left;
		final List<Resource> right = input.right;
		final List<Resource> ancestor = input.ancestor;
		
		final ComparedResourceSets result = new ComparedResourceSets();
		
		for (int i = 0; i < left.size(); i++) {
			DiffResourceSet diffModel = null;
			if (ancestor.size() == 0) {
				final MatchResourceSet matchModel = MatchService.doResourceSetMatch(left.get(i).getResourceSet(),
						right.get(i).getResourceSet(), options);			
				diffModel = DiffService.doDiff(matchModel, false);
			} else {
				final MatchResourceSet matchModel = MatchService.doResourceSetMatch(left.get(i).getResourceSet(),
						right.get(i).getResourceSet(), ancestor.get(i).getResourceSet(), options);			
				diffModel = DiffService.doDiff(matchModel, true);
			}
			result.append(layerDifferences(diffModel));
		}
		return result;
	}
	
	/**
	 * Create the annotations from a difference resource set model.
	 * @param diffModel The Difference ResourceSet Model.
	 */
	private static void createEAnnotations(final DiffResourceSet diffModel) {
		final NotationDiffCreator gmfModelCreator = new NotationDiffCreator();
		gmfModelCreator.setInput(new ModelCompareInput(null, diffModel));
		
		createEAnnotations(diffModel, MatchSide.LEFT, gmfModelCreator);
		createEAnnotations(diffModel, MatchSide.RIGHT, gmfModelCreator);
		

	}
	
	/**
	 * Create the decorators on the GMFResources from the differences resource set model and the side to handle.
	 * @param diffResourceSet 
	 * @param side 
	 * @param gmfModelCreator 
	 */
	private static void createEAnnotations(final DiffResourceSet diffResourceSet, final MatchSide side, final NotationDiffCreator gmfModelCreator) {
		final EObject root = getDiagramRoot(diffResourceSet, side);
		if (root != null) {
			final TransactionalEditingDomain ted = EditingDomainUtils.getOrCreateEditingDomain(root);
			final RecordingCommand command = new RecordingCommand(ted) {
				@Override
				protected void doExecute() {
					gmfModelCreator.addEAnnotations(side);
				}
			};
			ted.getCommandStack().execute(command);
			ted.dispose();
		}
	}
	
	/**
	 * Get the root diagram object from the difference resource set model and the expected side.
	 * @param diffResourceSet 
	 * @param side 
	 * @return The root.
	 */
	private static EObject getDiagramRoot(DiffResourceSet diffResourceSet, MatchSide side) {
		EObject ret = null;
		final EList<DiffModel> diffModels = diffResourceSet.getDiffModels();
		for (DiffModel diffModel : diffModels) {
			ret = getFirstRoot(diffModel, side);
			if (ret instanceof Diagram) {
				return ret;
			}
		}
		return ret;
	}
	
}
