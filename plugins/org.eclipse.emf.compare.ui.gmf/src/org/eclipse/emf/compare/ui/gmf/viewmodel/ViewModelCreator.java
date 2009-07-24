/*******************************************************************************
 * Copyright (c) 2009 Tobias Jaehnel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Tobias Jaehnel - Bug#241385
 *******************************************************************************/

package org.eclipse.emf.compare.ui.gmf.viewmodel;

import java.util.Collections;
import java.util.HashMap;

import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Singleton class Provides functions for creating a ViewModel (Which is actually a notation model) from two
 * notation models.
 * 
 * @author Tobias Jaehnel <tjaehnel@gmail.com>
 */
public class ViewModelCreator {
	private static HashMap<Object, ViewModelCreator> creators = new HashMap<Object, ViewModelCreator>();

	Diagram left;
	Diagram right;
	Diagram merged;
	/**
	 * Returns the left.
	 *
	 * @return The left.
	 */
	public Diagram getLeft() {
		return left;
	}

	/**
	 * Returns the right.
	 *
	 * @return The right.
	 */
	public Diagram getRight() {
		return right;
	}

	/**
	 * Returns the merged.
	 *
	 * @return The merged.
	 */
	public Diagram getMerged() {
		return merged;
	}

	/**
	 * ctor - take left and right diagram and perform merge and annotation
	 * @param left
	 * @param right
	 */
	private ViewModelCreator(Diagram left, Diagram right) {
		this.right = right;
		this.left = left;
		createViewModel();
	}

	/**
	 * Create a ViewModelCreator using the given left and right diagrams
	 * note: The given diagrams will be modified 
	 * 
	 * @param id unique identifier (e.g. compare configuration)
	 * @param left
	 * @param right
	 * @return
	 */
	public static ViewModelCreator createCreator(Object id, Diagram left, Diagram right) {
		ViewModelCreator creator = new ViewModelCreator(left, right);
		creators.put(id, creator);
		return creator;
	}
	/**
	 * Get the creator with the given id or create one
	 * 
	 * @param id
	 * @param left
	 * @param right
	 * @return
	 */
	public static ViewModelCreator getCreator(Object id, Diagram left, Diagram right) {
		ViewModelCreator creator = getCreator(id);
		if(creator == null)
			creator = createCreator(id, left, right);
		return creator;
	}
	/**
	 * Return the creator with the given id or null
	 * @param id
	 * @return
	 */
	public static ViewModelCreator getCreator(Object id) {
		return creators.get(id);
	}
	
	/**
	 * Create the merged model and annotate left, right and merged model
	 * 
	 */
	public void createViewModel() {
		try {
			MatchModel match = MatchService.doMatch(left.getElement(), right.getElement(), 
					Collections.<String, Object> emptyMap());
			DiffModel diff = DiffService.doDiff(match, false);
			NotationDiffMergeVisitor.removeNotationDiffElements(diff);
			NotationDiffMergeVisitor visitor = new NotationDiffMergeVisitor();
			merged = left;
			left = visitor.doMergeAndAnnotate(diff, match, merged, right, merged.getElement(), right.getElement());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
