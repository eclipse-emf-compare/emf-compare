/*******************************************************************************
 * Copyright (c) 2012-2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;

import java.util.Set;

import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.ColorChangeEvent;
import org.eclipse.emf.compare.rcp.ui.internal.mergeviewer.CompareColorImpl;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.themes.ITheme;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareColor extends CompareColorImpl {

	private final IPropertyChangeListener fThemeChangeListener;

	private final EventBus eventBus;

	private final ITheme theme;

	/** Ids of all main colors handle by EMFCompareColor */
	private static final Set<String> COLOR_IDS = ImmutableSet.of(UNMERGEABLE_DIFF_COLOR_THEME_KEY,
			UNMERGEABLE_DIFF_COLOR_THEME_KEY, INCOMING_CHANGE_COLOR_THEME_KEY, REQUIRED_DIFF_COLOR_THEME_KEY,
			CONFLICTING_CHANGE_COLOR_THEME_KEY, OUTGOING_CHANGE_COLOR_THEME_KEY);

	public EMFCompareColor(Display display, boolean leftIsLocal, ITheme theme, EventBus eventBus) {
		super(display, leftIsLocal, theme != null ? theme.getColorRegistry() : JFaceResources
				.getColorRegistry());
		this.eventBus = eventBus;
		this.theme = theme;
		this.fThemeChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				handlePropertyChangeEvent(event);
			}
		};

		if (theme != null) {
			theme.addPropertyChangeListener(fThemeChangeListener);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor#dispose()
	 */
	@Override
	public void dispose() {
		if (theme != null) {
			theme.removePropertyChangeListener(fThemeChangeListener);
		}
		super.dispose();
	}

	/**
	 * Update color from a PropertyChangeEvent
	 * 
	 * @param event
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		String propertyKey = event.getProperty();

		if (COLOR_IDS.contains(propertyKey)) {
			updateColors();
			// This event bus may not have been set
			if (eventBus != null) {
				ColorChangeEvent colorEvent = new ColorChangeEvent(propertyKey);
				eventBus.post(colorEvent);
			}
		}

	}
}
