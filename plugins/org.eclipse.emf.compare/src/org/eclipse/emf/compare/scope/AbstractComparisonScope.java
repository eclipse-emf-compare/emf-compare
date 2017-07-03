/*******************************************************************************
 * Copyright (c) 2012, 2016 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - introduce getAllInvolvedResourceURIs and adapter
 *******************************************************************************/
package org.eclipse.emf.compare.scope;

import com.google.common.collect.Sets;

import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.utils.IDiagnosable;

/**
 * This implementation of {@link IComparisonScope} can be sub-classed in order to avoid re-implementing some
 * of the methods imposed by this interface.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public abstract class AbstractComparisonScope extends AdapterImpl implements IComparisonScope2, IDiagnosable {
	/** The left root of this comparison. */
	protected Notifier left;

	/** The right root of this comparison. */
	protected Notifier right;

	/** The common ancestor of {@link #left} and {@link #right}. May be <code>null</code>. */
	protected Notifier origin;

	/** The namespace uris detected in the comparison. */
	protected Set<String> nsURIs;

	/** The resource uris detected in the comparison. */
	protected Set<String> resourceURIs;

	/** The diagnostic of the notifiers. */
	protected Diagnostic diagnostic;

	/**
	 * The resources URIs representing the files that have been selected to be in the scope of the comparison.
	 */
	protected Set<URI> allInvolvedResourceURIs;

	/**
	 * This will instantiate a scope with left, right and origin Notifiers defined.
	 * 
	 * @param left
	 *            The left root of this comparison.
	 * @param right
	 *            The right root of this comparison.
	 * @param origin
	 *            The common ancestor of <code>left</code> and <code>right</code>. May be <code>null</code>.
	 */
	public AbstractComparisonScope(Notifier left, Notifier right, Notifier origin) {
		this.left = left;
		this.right = right;
		this.origin = origin;
		this.resourceURIs = Sets.newHashSet();
		this.nsURIs = Sets.newHashSet();
		this.allInvolvedResourceURIs = Sets.newHashSet();
		this.diagnostic = new BasicDiagnostic(Diagnostic.OK, EMFCompare.DIAGNOSTIC_SOURCE, 0, null,
				new Object[] {this, });
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getLeft()
	 */
	public Notifier getLeft() {
		return left;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getRight()
	 */
	public Notifier getRight() {
		return right;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getOrigin()
	 */
	public Notifier getOrigin() {
		return origin;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getNsURIs()
	 */
	public Set<String> getNsURIs() {
		return nsURIs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope#getResourceURIs()
	 */
	public Set<String> getResourceURIs() {
		return resourceURIs;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope.Internal#getDiagnostic()
	 */
	public Diagnostic getDiagnostic() {
		return diagnostic;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope.Internal#setDiagnostic(org.eclipse.emf.common.util.Diagnostic)
	 */
	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.scope.IComparisonScope2#getAllInvolvedResourceURIs()
	 */
	public Set<URI> getAllInvolvedResourceURIs() {
		return allInvolvedResourceURIs;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		if (type == IComparisonScope2.class || type == IComparisonScope.class) {
			return true;
		}
		return super.isAdapterForType(type);
	}
}
