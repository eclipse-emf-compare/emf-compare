/*******************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.logical;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;

/**
 * Registry managing the model inclusion testers dependency registered through extension point
 * <code>org.eclipse.emf.compare.ide.modelInclusionTester</code>.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 3.4.2
 */
public class ModelInclusionTesterRegistry {

	/** Keeps track of the registered testers. */
	private Map<String, IModelInclusionTester> registeredTesters = new LinkedHashMap<String, IModelInclusionTester>();

	/**
	 * Returns the model inclusion testers of this registry.
	 * 
	 * @return The model inclusion testers.
	 */
	public Collection<IModelInclusionTester> getModelInclusionTesters() {
		return registeredTesters.values();
	}

	/**
	 * Adds a the specified <code>modelInclusionTester</code>.
	 * 
	 * @param key
	 *            The key to store it with. Must not be null.
	 * @param modelInclusionTester
	 *            The model inclusion tester. Must not be null.
	 */
	public void add(String key, IModelInclusionTester modelInclusionTester) {
		registeredTesters.put(checkNotNull(key), checkNotNull(modelInclusionTester));
	}

	/**
	 * Removes the model inclusion tester with the specified <code>key</code>.
	 * 
	 * @param key
	 *            The key of the model inclusion tester to be removed.
	 * @return <code>true</code> if the tester was removed, <code>false</code> if there was no tester with the
	 *         specified <code>key</code>.
	 */
	public boolean remove(String key) {
		return registeredTesters.remove(key) != null;
	}

	/**
	 * Clears the registered {@link IModelInclusionTester model inclusion testers}.
	 * <p>
	 * This method is mainly intended to be used for unit testing.
	 * </p>
	 */
	public void clear() {
		registeredTesters.clear();
	}

	/**
	 * Specifies whether any of the registered {@link IModelInclusionTester model inclusion testers} includes
	 * the specified <code>file</code>.
	 * 
	 * @param file
	 *            The file to test.
	 * @return <code>true</code> if any of the testers includes the specified <code>file</code>,
	 *         <code>false</code> otherwise.
	 */
	public boolean anyTesterIncludes(IFile file) {
		for (IModelInclusionTester tester : registeredTesters.values()) {
			if (tester.shouldInclude(file)) {
				return true;
			}
		}
		return false;
	}

}
