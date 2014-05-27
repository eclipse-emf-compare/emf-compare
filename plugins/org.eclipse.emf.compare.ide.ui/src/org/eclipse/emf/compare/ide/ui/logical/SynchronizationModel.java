/*******************************************************************************
 * Copyright (c) 2011, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.logical;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import org.eclipse.core.resources.IStorage;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.utils.IDiagnosable;

/**
 * This class acts as a simple DTO that allows us to store the three traversals corresponding to the three
 * sides of a comparison while we build its scope.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @since 4.0
 */
@Beta
public final class SynchronizationModel implements IDiagnosable {
	/** The traversal corresponding to the left side. */
	private final StorageTraversal leftTraversal;

	/** The traversal corresponding to the right side. */
	private final StorageTraversal rightTraversal;

	/** The traversal corresponding to the common ancestor of both other side. */
	private final StorageTraversal originTraversal;

	/** The diagnostic that may have been issued for this synchronization model */
	private Diagnostic diagnostic;

	/**
	 * Constructs our logical model given the three traversal for our sides.
	 * 
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal corresponding to the common ancestor of both other side. Can be
	 *            <code>null</code>.
	 */
	public SynchronizationModel(StorageTraversal leftTraversal, StorageTraversal rightTraversal,
			StorageTraversal originTraversal) {
		this(leftTraversal, rightTraversal, originTraversal, new BasicDiagnostic(
				EMFCompareIDEUIPlugin.PLUGIN_ID, 0, null, new Object[] {leftTraversal, rightTraversal,
						originTraversal }));
	}

	/**
	 * Constructs our logical model given the three traversal for our sides.
	 * 
	 * @param leftTraversal
	 *            The traversal corresponding to the left side.
	 * @param rightTraversal
	 *            The traversal corresponding to the right side.
	 * @param originTraversal
	 *            The traversal corresponding to the common ancestor of both other side. Can be
	 *            <code>null</code>.
	 * @param diagnostic
	 *            The diagnostic that have gathered during the computation of the traversals.
	 */
	public SynchronizationModel(StorageTraversal leftTraversal, StorageTraversal rightTraversal,
			StorageTraversal originTraversal, Diagnostic diagnostic) {
		this.diagnostic = Preconditions.checkNotNull(diagnostic);
		if (leftTraversal == null) {
			this.leftTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.leftTraversal = leftTraversal;
		}

		if (rightTraversal == null) {
			this.rightTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.rightTraversal = rightTraversal;
		}

		if (originTraversal == null) {
			this.originTraversal = new StorageTraversal(Sets.<IStorage> newHashSet());
		} else {
			this.originTraversal = originTraversal;
		}
	}

	/**
	 * Returns the left traversal of this model.
	 * 
	 * @return The left traversal of this model.
	 */
	public StorageTraversal getLeftTraversal() {
		return leftTraversal;
	}

	/**
	 * Returns the right traversal of this model.
	 * 
	 * @return The right traversal of this model.
	 */
	public StorageTraversal getRightTraversal() {
		return rightTraversal;
	}

	/**
	 * Returns the origin traversal of this model, if any.
	 * 
	 * @return The origin traversal of this model, <code>null</code> if none.
	 */
	public StorageTraversal getOriginTraversal() {
		return originTraversal;
	}

	public Diagnostic getDiagnostic() {
		BasicDiagnostic ret = new BasicDiagnostic(EMFCompareIDEUIPlugin.PLUGIN_ID, 0, EMFCompareIDEUIMessages
				.getString("SynchronizationModel.diagnosticMesg"), new Object[] {this, }); //$NON-NLS-1$
		ret.merge(this.diagnostic);

		ret.add(leftTraversal.getDiagnostic());
		ret.add(originTraversal.getDiagnostic());
		ret.add(rightTraversal.getDiagnostic());
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.utils.IDiagnosable#setDiagnostic(org.eclipse.emf.common.util.Diagnostic)
	 */
	public void setDiagnostic(Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}
}
