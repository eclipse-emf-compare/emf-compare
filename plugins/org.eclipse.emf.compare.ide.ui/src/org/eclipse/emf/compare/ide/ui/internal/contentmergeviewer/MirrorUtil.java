/*******************************************************************************
 * Copyright (c) 2017 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Martin Fleck - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import java.lang.reflect.Field;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;

/**
 * A utility class to handle the common functionality of mirroring the content merge viewer sides, i.e.,
 * swapping the left and right viewer. The mirroring functionality was introduced in Eclipse Compare v3.7 and
 * therefore needs to be handled reflectively to avoid backwards compatibility issues.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
public class MirrorUtil {
	/** Mirrored: Compare configuration property key. */
	public static final String CONFIG_MIRRORED = "MIRRORED"; //$NON-NLS-1$

	/** Mirrored: Compare preference store key. */
	public static final String PREF_MIRRORED = "org.eclipse.compare.Swapped"; //$NON-NLS-1$

	/** Mirrored: Name of the 'Switch Left and Right' action field in {@link ContentMergeViewer}. */
	public static final String SWITCH_LEFT_RIGHT_ACTION = "fSwitchLeftAndRight"; //$NON-NLS-1$

	/**
	 * Reflectively returns the action for switching the left and right side of the viewer. The returned
	 * action may be null, if it is not available or cannot be retrieved.
	 * 
	 * @param contentMergeViewer
	 *            content merge viewer
	 * @return switch left and right action or null
	 */
	public static Action getAction(ContentMergeViewer contentMergeViewer) {
		if (contentMergeViewer == null) {
			return null;
		}

		try {
			Field declaredField = ContentMergeViewer.class.getDeclaredField(SWITCH_LEFT_RIGHT_ACTION);
			declaredField.setAccessible(true);
			Object action = declaredField.get(contentMergeViewer);
			if (action != null && action instanceof Action) {
				return (Action)action;
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
				| IllegalAccessException e) {
			// ignore as the action is not available on all platforms
		}
		return null;
	}

	/**
	 * Removes the action for switching the left and right side of the viewer from the toolbar, if it is
	 * available.
	 * 
	 * @param contentMergeViewer
	 *            content merge viewer
	 * @param toolBarManager
	 *            tool bar manager of the content merge viewer
	 * @return the removed action or null, if the action was not available
	 * @see #getAction(ContentMergeViewer)
	 */
	public static Action removeFromToolBar(ContentMergeViewer contentMergeViewer,
			ToolBarManager toolBarManager) {
		// switch left and right view action, may be null -> remove as it destroys the view
		Action switchLeftAndRightAction = getAction(contentMergeViewer);
		if (switchLeftAndRightAction != null && toolBarManager != null) {
			IContributionItem[] items = toolBarManager.getItems();
			for (IContributionItem iContributionItem : items) {
				if (iContributionItem instanceof ActionContributionItem) {
					IAction action = ((ActionContributionItem)iContributionItem).getAction();
					if (switchLeftAndRightAction == action) {
						toolBarManager.remove(iContributionItem);
					}
				}
			}
		}
		return switchLeftAndRightAction;
	}

	/**
	 * Evaluates whether the left and right side of the viewer should be mirrored.
	 * 
	 * @param compareConfiguration
	 *            compare configuration holding the mirrored property
	 * @return true if the left and right side of the viewer should be mirrored. Default is false.
	 */
	public static boolean isMirrored(CompareConfiguration compareConfiguration) {
		if (compareConfiguration == null) {
			return false;
		}

		boolean isMirrored = false;
		Object value = compareConfiguration.getProperty(CONFIG_MIRRORED);
		if (value instanceof Boolean) {
			isMirrored = ((Boolean)value).booleanValue();
		}
		return isMirrored;
	}

	/**
	 * Sets the mirrored property for the given compare configuration.
	 * 
	 * @param compareConfiguration
	 *            compare configuration holding the mirrored property
	 * @param mirrored
	 *            true if the left and right side of the viewer should be mirrored, false otherwise.
	 */
	public static void setMirrored(EMFCompareConfiguration compareConfiguration, boolean mirrored) {
		compareConfiguration.setProperty(CONFIG_MIRRORED, Boolean.valueOf(mirrored));
	}

	/**
	 * Returns true if the given property is used to store the mirrored value in the compare configuration,
	 * false otherwise.
	 * 
	 * @param configProperty
	 *            compare configuration property
	 * @return true if the given property is the mirrored property, false otherwise
	 */
	public static boolean isMirroredProperty(String configProperty) {
		return CONFIG_MIRRORED.equals(configProperty);
	}

	/**
	 * Returns true if the given property is used to store the mirrored value in the preference store, false
	 * otherwise.
	 * 
	 * @param preferenceProperty
	 *            preference property
	 * @return true if the given property is the mirrored property, false otherwise
	 */
	public static boolean isMirroredPreference(String preferenceProperty) {
		return PREF_MIRRORED.equals(preferenceProperty);
	}

}
