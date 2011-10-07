/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.ICompareNavigator;
import org.eclipse.compare.internal.CompareEditor;
import org.eclipse.compare.internal.CompareEditorInputNavigator;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.viewer.group.IDifferenceGroupingFacility.UIDifferenceGroup;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;

/**
 * This class aims to provide services about querying elements visible in the current
 * {@link ModelStructureMergeViewer}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * @since 1.3
 */
public final class ModelStructureMergeViewerService {

	/**
	 * Constructor.
	 */
	private ModelStructureMergeViewerService() {

	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare editor.
	 * 
	 * @param compareEditor
	 *            The EMF Compare editor.
	 * @return all visible {@link DiffElement}s from the given compare editor.
	 */
	public static List<DiffElement> getVisibleDiffElements(CompareEditor compareEditor) {
		final IEditorInput editorInput = compareEditor.getEditorInput();
		if (editorInput instanceof CompareEditorInput) {
			final CompareEditorInput compareEditorInput = (CompareEditorInput)editorInput;
			final CompareConfiguration compareConfiguration = compareEditorInput.getCompareConfiguration();
			final ICompareNavigator compareNavigator = compareEditorInput.getNavigator();
			if (compareNavigator instanceof CompareEditorInputNavigator)
				return getVisibleDiffElements(compareConfiguration, compareNavigator);
		}
		return Collections.emptyList();
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare configuration and compare navigator.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration.
	 * @param compareNavigator
	 *            The compare navigator.
	 * @return all visible {@link DiffElement}s from the given compare configuration and compare navigator.
	 */
	public static List<DiffElement> getVisibleDiffElements(final CompareConfiguration compareConfiguration,
			final ICompareNavigator compareNavigator) {
		final CompareEditorInputNavigator compareEditorInputNavigator = (CompareEditorInputNavigator)compareNavigator;
		final Object[] panes = compareEditorInputNavigator.getPanes();
		for (Object object : panes) {
			if (object instanceof CompareViewerSwitchingPane) {
				final CompareViewerSwitchingPane compareViewerSwitchingPane = (CompareViewerSwitchingPane)object;
				final Control control = compareViewerSwitchingPane.getContent();
				if (control instanceof Composite) {
					return getvisibleDiffElements((ICompareInput)compareViewerSwitchingPane.getViewer()
							.getInput(), (Composite)control, compareConfiguration);
				}
			}
		}
		return Collections.emptyList();
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare in and compare configuration in the
	 * given viewer.
	 * 
	 * @param input
	 *            The compare input of the editor.
	 * @param parent
	 *            The parent composite.
	 * @param configuration
	 *            The compare configuration.
	 * @return Returns all visible {@link DiffElement}s from the given compare in and compare configuration in
	 *         the given viewer
	 */
	public static List<DiffElement> getvisibleDiffElements(ICompareInput input, Composite parent,
			CompareConfiguration configuration) {
		final List<DiffElement> ret = new ArrayList<DiffElement>();

		final ContentViewer contentViewer = (ContentViewer)CompareUI.findStructureViewer(null, input, parent,
				configuration);
		final ITreeContentProvider contentProvider = (ITreeContentProvider)contentViewer.getContentProvider();

		final Object[] elements = contentProvider.getElements(contentViewer.getInput());
		for (Object object : elements) {
			if (object instanceof DiffElement) {
				ret.add((DiffElement)object);
			}
			ret.addAll(allChildren(object, contentProvider));
		}
		return ret;
	}

	/**
	 * Returns all children from given root element using the given {@link ITreeContentProvider}.
	 * 
	 * @param element
	 *            The root element.
	 * @param contentProvider
	 *            The content provider.
	 * @return all children from given root element using the given {@link ITreeContentProvider}.
	 */
	private static List<DiffElement> allChildren(Object element, ITreeContentProvider contentProvider) {
		final List<DiffElement> ret = new ArrayList<DiffElement>();
		final Object[] children = contentProvider.getChildren(element);
		for (Object object : children) {
			if (object instanceof DiffElement) {
				ret.add((DiffElement)object);
			}
			ret.addAll(allChildren(element, contentProvider));
		}
		return ret;
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare editor grouped by selected groups.
	 * 
	 * @param compareEditor
	 *            The Compare Editor.
	 * @return all visible {@link DiffElement}s from the given compare editor grouped by selected groups.
	 */
	public static Map<UIDifferenceGroup, List<DiffElement>> getGroupedDiffElements(CompareEditor compareEditor) {
		final IEditorInput editorInput = compareEditor.getEditorInput();
		if (editorInput instanceof CompareEditorInput) {
			final CompareEditorInput compareEditorInput = (CompareEditorInput)editorInput;
			final CompareConfiguration compareConfiguration = compareEditorInput.getCompareConfiguration();
			final ICompareNavigator compareNavigator = compareEditorInput.getNavigator();
			if (compareNavigator instanceof CompareEditorInputNavigator)
				return getGroupedDiffElements(compareConfiguration, compareNavigator);
		}
		return Collections.emptyMap();
	}

	/**
	 * Returns all visible {@link DiffElement}s from the given compare configuration and compare navigator
	 * grouped by selected groups.
	 * 
	 * @param compareConfiguration
	 *            The compare configuration.
	 * @param compareNavigator
	 *            The compare navigator.
	 * @return all visible {@link DiffElement}s from the given compare configuration and compare navigator
	 *         grouped by selected groups.
	 */
	public static Map<UIDifferenceGroup, List<DiffElement>> getGroupedDiffElements(
			final CompareConfiguration compareConfiguration, final ICompareNavigator compareNavigator) {
		final CompareEditorInputNavigator compareEditorInputNavigator = (CompareEditorInputNavigator)compareNavigator;
		final Object[] panes = compareEditorInputNavigator.getPanes();
		for (Object object : panes) {
			if (object instanceof CompareViewerSwitchingPane) {
				final CompareViewerSwitchingPane compareViewerSwitchingPane = (CompareViewerSwitchingPane)object;
				final Control control = compareViewerSwitchingPane.getContent();
				if (control instanceof Composite) {
					return getGroupedDiffElements((ICompareInput)compareViewerSwitchingPane.getViewer()
							.getInput(), (Composite)control, compareConfiguration);
				}
			}
		}
		return Collections.emptyMap();
	}

	/**
	 * Returns all visible elements from the given compare in and compare configuration in the given viewer
	 * grouped by selected groups.
	 * 
	 * @param input
	 *            The compare input of the editor.
	 * @param parent
	 *            The parent composite.
	 * @param configuration
	 *            The compare configuration.
	 * @return all visible elements from the given compare in and compare configuration in the given viewer
	 *         grouped by selected groups.
	 */
	public static Map<UIDifferenceGroup, List<DiffElement>> getGroupedDiffElements(ICompareInput input,
			Composite parent, CompareConfiguration configuration) {
		final Map<UIDifferenceGroup, List<DiffElement>> ret = new HashMap<UIDifferenceGroup, List<DiffElement>>();

		final ContentViewer contentViewer = (ContentViewer)CompareUI.findStructureViewer(null, input, parent,
				configuration);
		final ITreeContentProvider contentProvider = (ITreeContentProvider)contentViewer.getContentProvider();

		final Object[] elements = contentProvider.getElements(contentViewer.getInput());
		for (Object object : elements) {
			if (object instanceof UIDifferenceGroup) {
				final UIDifferenceGroup diffGroup = (UIDifferenceGroup)object;
				ret.put(diffGroup, allChildren(object, contentProvider));
			}

		}

		return ret;
	}

}
