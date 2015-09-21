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
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.filter;

import com.google.common.base.Predicate;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal.CompareUIPapyrusMessages;
import org.eclipse.papyrus.infra.core.resource.sasheditor.DiModel;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.uml.tools.model.UmlModel;

/**
 * Treatment that adds equivalences (bi-directional "requires") between equivalent papyrus resource renames.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @since 2.5
 */
public class AddEquivalencesBetweenPapyrusResourceLocationChanges {

	/** UML files extension. */
	private static final String UML_EXTENSION = '.' + UmlModel.UML_FILE_EXTENSION;

	/** UML files extension. */
	private static final String NOTATION_EXTENSION = '.' + NotationModel.NOTATION_FILE_EXTENSION;

	/** UML files extension. */
	private static final String DI_EXTENSION = '.' + DiModel.DI_FILE_EXTENSION;

	/** The comparison. */
	private final Comparison comparison;

	/** The monitor. */
	private final Monitor monitor;

	/** Index used to easily find move changes. */
	private final Multimap<String, ResourceAttachmentChange> moveChangesByTrimmedURI = LinkedHashMultimap
			.create(10, 4);

	/** Index used to easily find other changes. */
	private final Multimap<String, ResourceAttachmentChange> changesByTrimmedURI = LinkedHashMultimap.create(
			10, 4);

	/**
	 * Constructor.
	 * 
	 * @param comparison
	 *            The comparison, must no be {@code null}.
	 * @param monitor
	 *            The monitor, must no be {@code null}.
	 */
	public AddEquivalencesBetweenPapyrusResourceLocationChanges(Comparison comparison, Monitor monitor) {
		this.comparison = checkNotNull(comparison);
		this.monitor = checkNotNull(monitor);
	}

	/** Executes this treatment. */
	public void run() {
		monitor.subTask(CompareUIPapyrusMessages.getString("AddEquivalencesBetweenPapyrusRenames.TaskLabel")); //$NON-NLS-1$
		indexAttachmentChanges();
		for (ResourceAttachmentChange resourceAttachmentChange : moveChangesByTrimmedURI.values()) {
			final String resourceURI = resourceAttachmentChange.getResourceURI();
			if (resourceURI.endsWith(UML_EXTENSION)) {
				addEquivalences(resourceAttachmentChange, moveChangesByTrimmedURI);
			}
		}
		for (ResourceAttachmentChange resourceAttachmentChange : changesByTrimmedURI.values()) {
			final String resourceURI = resourceAttachmentChange.getResourceURI();
			if (resourceURI.endsWith(UML_EXTENSION)) {
				addEquivalences(resourceAttachmentChange, moveChangesByTrimmedURI);
				addEquivalences(resourceAttachmentChange, changesByTrimmedURI);
			}
		}

	}

	/** Index the attachment changes in the model. */
	private void indexAttachmentChanges() {
		Predicate<? super Diff> isKindNotNull = new Predicate<Diff>() {
			public boolean apply(Diff input) {
				return input != null;
			}
		};

		for (Diff change : filter(comparison.getDifferences(), and(
				instanceOf(ResourceAttachmentChange.class), isKindNotNull))) {
			ResourceAttachmentChange attachmentChange = (ResourceAttachmentChange)change;
			switch (attachmentChange.getKind()) {
				case ADD:
					// Voluntary pass-through
				case DELETE:
					changesByTrimmedURI.put(getIndexKey(attachmentChange), attachmentChange);
					break;
				case MOVE:
					moveChangesByTrimmedURI.put(getIndexKey(attachmentChange), attachmentChange);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Add bi-directional implications between the given UML change and the related changes (notation, di)
	 * found in the map for the given change.
	 * 
	 * @param umlAttachmentChange
	 *            The change
	 * @param attachmentChangeByKey
	 *            The map of attachment changes by keys
	 */
	private void addEquivalences(ResourceAttachmentChange umlAttachmentChange,
			Multimap<String, ResourceAttachmentChange> attachmentChangeByKey) {
		for (ResourceAttachmentChange relatedChange : attachmentChangeByKey
				.get(getIndexKey(umlAttachmentChange))) {
			if (relatedChange != umlAttachmentChange
					&& relatedChange.getSource() == umlAttachmentChange.getSource()) {
				umlAttachmentChange.getRequires().add(relatedChange);
				relatedChange.getRequires().add(umlAttachmentChange);
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
	private String getIndexKey(ResourceAttachmentChange change) {
		String uri = change.getResourceURI();
		int dot = uri.lastIndexOf("."); //$NON-NLS-1$
		if (dot >= 0) {
			return uri.substring(0, dot);
		}
		return uri;
	}
}
