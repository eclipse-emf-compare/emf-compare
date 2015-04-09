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
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.postprocessor;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceLocationChange;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.CompareUIPapyrusMessages;
import org.eclipse.papyrus.uml.tools.model.UmlModel;

/**
 * Treatment that adds equivalences (bi-directional "requires") between equivalent papyrus resource renames.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 2.4
 */
public class AddEquivalencesBetweenPapyrusRenames {

	/** UML files extension. */
	private static final String UML_EXTENSION = "." + UmlModel.UML_FILE_EXTENSION; //$NON-NLS-1$

	/** The comparison. */
	private final Comparison comparison;

	/** The monitor. */
	private final Monitor monitor;

	/** Index used to easily find changes. */
	private final Multimap<String, ResourceLocationChange> changesByTrimmedURI = LinkedHashMultimap.create(
			10, 4);

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison, must no be {@code null}.
	 * @param monitor
	 *            The monitor, must no be {@code null}.
	 */
	public AddEquivalencesBetweenPapyrusRenames(Comparison comparison, Monitor monitor) {
		this.comparison = checkNotNull(comparison);
		this.monitor = checkNotNull(monitor);
	}

	/** Executes this treatment. */
	public void run() {
		monitor.subTask(CompareUIPapyrusMessages.getString("AddEquivalencesBetweenPapyrusRenames.TaskLabel")); //$NON-NLS-1$
		indexLocationChanges();
		for (ResourceLocationChange resourceLocationChange : changesByTrimmedURI.values()) {
			final String baseLocation = resourceLocationChange.getBaseLocation();
			if (baseLocation.endsWith(UML_EXTENSION)) {
				addEquivalences(resourceLocationChange);
			}
		}
	}

	/** Index the location changes in the model. */
	private void indexLocationChanges() {
		for (MatchResource matchResource : comparison.getMatchedResources()) {
			for (ResourceLocationChange change : matchResource.getLocationChanges()) {
				changesByTrimmedURI.put(getIndexKey(change), change);
			}
		}
	}

	/**
	 * Adds equivalences to relevant changes.
	 * 
	 * @param umlLocationChange
	 *            The change
	 */
	private void addEquivalences(ResourceLocationChange umlLocationChange) {
		for (ResourceLocationChange relatedChange : changesByTrimmedURI.get(getIndexKey(umlLocationChange))) {
			if (relatedChange != umlLocationChange
					&& relatedChange.getSource() == umlLocationChange.getSource()) {
				umlLocationChange.getRequires().add(relatedChange);
				relatedChange.getRequires().add(umlLocationChange);
			}
		}
	}

	/**
	 * Provides the key that should identify the given change in the index.
	 * 
	 * @param change
	 *            The change
	 * @return the key that should identify the given change in the index.
	 */
	private String getIndexKey(ResourceLocationChange change) {
		String uri = change.getBaseLocation();
		int dot = uri.lastIndexOf("."); //$NON-NLS-1$
		if (dot >= 0) {
			return uri.substring(0, dot);
		}
		return uri;
	}
}
