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
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
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

	private ImmutableSet<IResource> resources;

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

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SynchronizationModel) {
			final SynchronizationModel other = (SynchronizationModel)obj;
			return Objects.equal(leftTraversal, other.leftTraversal)
					&& Objects.equal(rightTraversal, other.rightTraversal)
					&& Objects.equal(originTraversal, other.originTraversal);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[] {leftTraversal, rightTraversal, originTraversal, });
	}

	/**
	 * Returns the set of resources this synchronization model spans. The returned set may contain resources
	 * that do not exist locally.
	 * 
	 * @return The set of resources this synchronization model spans.
	 * @since 4.0
	 */
	public Set<IResource> getResources() {
		if (resources == null) {
			final Set<IResource> leftResources = collectResources(getLeftTraversal());
			final Set<IResource> rightResources = collectResources(getRightTraversal());
			final Set<IResource> originResources = collectResources(getOriginTraversal());
			resources = ImmutableSet.<IResource> builder().addAll(leftResources).addAll(rightResources)
					.addAll(originResources).build();
		}
		return resources;
	}

	/**
	 * Collect the set of IResources the given storage traversal spans.
	 * 
	 * @param traversal
	 *            The traversal from which to collect IResources. Might be <code>null</code>, in which case an
	 *            empty set will be returned.
	 * @return The converted traversal.
	 */
	private static Set<IResource> collectResources(StorageTraversal aTraversal) {
		final Set<IResource> resources = new LinkedHashSet<IResource>();
		if (aTraversal == null) {
			return resources;
		}

		for (IStorage storage : aTraversal.getStorages()) {
			if (storage instanceof IFile) {
				resources.add((IFile)storage);
			} else {
				/*
				 * Use a file handle. Since files can be both local and remote, they might not even exist in
				 * the current workspace. It will be the responsibility of the user to either get the remote
				 * or local content. The traversal itself only tells "all" potential resources linked to the
				 * current.
				 */
				resources.add(ResourcesPlugin.getWorkspace().getRoot().getFile(storage.getFullPath()));
			}
		}
		return resources;
	}
}
