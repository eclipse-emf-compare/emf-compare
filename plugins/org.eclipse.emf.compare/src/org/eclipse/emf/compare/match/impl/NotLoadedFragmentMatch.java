/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.impl;

import com.google.common.collect.ImmutableList;

import java.util.Collection;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.internal.spec.MatchSpec;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * The not loaded fragment match.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 * @since 3.2
 */
public class NotLoadedFragmentMatch extends MatchSpec {

	/** The children of the not loaded fragment associated to this match. */
	private ImmutableList<Match> children;

	/** The name of the not loaded fragment associated to this match. */
	private String name;

	/**
	 * Constructor.
	 */
	public NotLoadedFragmentMatch() {
		super();
		this.children = ImmutableList.of();
		this.name = ""; //$NON-NLS-1$
	}

	/**
	 * Constructor.
	 * 
	 * @param child
	 *            the child of this not loaded fragment match.
	 */
	public NotLoadedFragmentMatch(Match child) {
		super();
		this.children = ImmutableList.of(child);
		this.name = ""; //$NON-NLS-1$
	}

	/**
	 * Constructor.
	 * 
	 * @param children
	 *            the children of this not loaded fragment match.
	 */
	public NotLoadedFragmentMatch(Collection<Match> children) {
		super();
		this.children = ImmutableList.copyOf(children);
		this.name = ""; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.impl.MatchImpl#getComparison()
	 */
	@Override
	public Comparison getComparison() {
		Comparison ret = null;

		if (!children.isEmpty()) {
			Match firstChild = getFirstMatchChild();
			EObject eContainer = firstChild.eContainer();
			while (!(eContainer instanceof Comparison) && eContainer != null) {
				eContainer = eContainer.eContainer();
			}

			if (eContainer != null) {
				ret = (Comparison)eContainer;
			}
		}

		return ret;
	}

	/**
	 * Get children.
	 * 
	 * @return the children.
	 */
	public Collection<Match> getChildren() {
		return children;
	}

	/**
	 * Get name.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name.
	 * 
	 * @param name
	 *            the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get resource.
	 * 
	 * @return the resource.
	 */
	public Resource getResource() {
		final Resource resource;
		final Match match = getFirstMatchChild();
		if (match != null && match.getLeft() != null) {
			resource = match.getLeft().eResource();
		} else if (match != null && match.getRight() != null) {
			resource = match.getRight().eResource();
		} else if (match != null && match.getOrigin() != null) {
			resource = match.getOrigin().eResource();
		} else {
			resource = null;
		}
		return resource;
	}

	/**
	 * Get the first match child.
	 * 
	 * @return the first match child.
	 */
	public Match getFirstMatchChild() {
		Match match;
		if (!children.isEmpty()) {
			match = children.iterator().next();
			if (match instanceof NotLoadedFragmentMatch) {
				match = ((NotLoadedFragmentMatch)match).getFirstMatchChild();
			}
		} else {
			match = null;
		}
		return match;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj.equals(children);
	}

	@Override
	public int hashCode() {
		return children.hashCode();
	}
}
