/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer;

import static org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.CompareConfigurationExtension.getBoolean;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.IEMFCompareConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareColor implements RemovalListener<RGB, Color> {

	private RGB SELECTED_INCOMING;

	private RGB INCOMING;

	private RGB INCOMING_FILL;

	private RGB INCOMING_TEXT_FILL;

	private RGB SELECTED_CONFLICT;

	private RGB CONFLICT;

	private RGB CONFLICT_FILL;

	private RGB CONFLICT_TEXT_FILL;

	private RGB SELECTED_OUTGOING;

	private RGB OUTGOING;

	private RGB OUTGOING_FILL;

	private RGB OUTGOING_TEXT_FILL;

	private RGB RESOLVED;

	private final Cache<RGB, Color> fColors;

	private final ContentMergeViewer fContentMergeViewer;

	private final IPreferenceStore fPreferenceStore;

	private final IPropertyChangeListener fPreferenceChangeListener;

	private final boolean fLeftIsLocal;

	public EMFCompareColor(ContentMergeViewer contentMergeViewer, IPreferenceStore preferenceStore,
			CompareConfiguration compareConfiguration) {
		this.fContentMergeViewer = contentMergeViewer;
		this.fPreferenceStore = preferenceStore;
		this.fColors = CacheBuilder.newBuilder().maximumSize(16).removalListener(this).build(
				new CacheLoader<RGB, Color>() {
					@Override
					public Color load(RGB rgb) throws Exception {
						return new Color(getDisplay(), rgb);
					}
				});
		this.fPreferenceChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				handlePropertyChangeEvent(event);
			}
		};
		if (fPreferenceStore != null) {
			fPreferenceStore.addPropertyChangeListener(fPreferenceChangeListener);
		}
		fLeftIsLocal = getBoolean(compareConfiguration, "LEFT_IS_LOCAL", false); //$NON-NLS-1$
		updateColors();
	}

	private Display getDisplay() {
		return fContentMergeViewer.getControl().getDisplay();
	}

	public void onRemoval(RemovalNotification<RGB, Color> notification) {
		Color color = notification.getValue();
		if (!color.isDisposed()) {
			color.dispose();
		}
	}

	private void handlePropertyChangeEvent(PropertyChangeEvent event) {
		String key = event.getProperty();

		if (key.equals(IEMFCompareConstants.INCOMING_COLOR)
				|| key.equals(IEMFCompareConstants.OUTGOING_COLOR)
				|| key.equals(IEMFCompareConstants.CONFLICTING_COLOR)
				|| key.equals(IEMFCompareConstants.RESOLVED_COLOR)) {
			updateColors();
		}
	}

	private Color getColor(RGB rgb) {
		if (rgb == null) {
			return null;
		}
		Color c = fColors.getUnchecked(rgb);
		return c;
	}

	public Color getFillColor(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		return getColor(getFillRGB(diff, isThreeWay, isIgnoreAncestor, selected));
	}

	private RGB getFillRGB(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		RGB selected_fill = getBackground();
		if (isThreeWay && !isIgnoreAncestor) {
			if (diff.getConflict() == null) {
				switch (diff.getSource()) {
					case RIGHT:
						if (fLeftIsLocal) {
							return selected ? selected_fill : INCOMING_FILL;
						}
						return selected ? selected_fill : OUTGOING_FILL;
					case LEFT:
						if (fLeftIsLocal) {
							return selected ? selected_fill : OUTGOING_FILL;
						}
						return selected ? selected_fill : INCOMING_FILL;
				}
			} else {
				return selected ? selected_fill : CONFLICT_FILL;
			}
			return selected ? selected_fill : CONFLICT_FILL;
		}
		return selected ? selected_fill : OUTGOING_FILL;
	}

	public Color getStrokeColor(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		return getColor(getStrokeRGB(diff, isThreeWay, isIgnoreAncestor, selected));
	}

	private RGB getStrokeRGB(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		if (isThreeWay && !isIgnoreAncestor) {
			if (diff.getConflict() == null) {
				switch (diff.getSource()) {
					case RIGHT:
						if (fLeftIsLocal) {
							return selected ? SELECTED_INCOMING : INCOMING;
						}
						return selected ? SELECTED_OUTGOING : OUTGOING;
					case LEFT:
						if (fLeftIsLocal) {
							return selected ? SELECTED_OUTGOING : OUTGOING;
						}
						return selected ? SELECTED_INCOMING : INCOMING;
				}
			} else {
				return selected ? SELECTED_CONFLICT : CONFLICT;
			}
			return selected ? SELECTED_CONFLICT : CONFLICT;
		}
		return selected ? SELECTED_OUTGOING : OUTGOING;
	}

	private RGB getBackground() {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
	}

	private void updateColors() {
		ColorRegistry registry = JFaceResources.getColorRegistry();

		RGB bg = getBackground();
		SELECTED_INCOMING = registry.getRGB(IEMFCompareConstants.INCOMING_COLOR);
		if (SELECTED_INCOMING == null) {
			SELECTED_INCOMING = new RGB(0, 0, 255); // BLUE
		}
		INCOMING = interpolate(SELECTED_INCOMING, bg, 0.6);
		INCOMING_FILL = interpolate(SELECTED_INCOMING, bg, 0.97);
		INCOMING_TEXT_FILL = interpolate(SELECTED_INCOMING, bg, 0.85);

		SELECTED_OUTGOING = registry.getRGB(IEMFCompareConstants.OUTGOING_COLOR);
		if (SELECTED_OUTGOING == null) {
			SELECTED_OUTGOING = new RGB(0, 0, 0); // BLACK
		}
		OUTGOING = interpolate(SELECTED_OUTGOING, bg, 0.6);
		OUTGOING_FILL = interpolate(SELECTED_OUTGOING, bg, 0.97);
		OUTGOING_TEXT_FILL = interpolate(SELECTED_OUTGOING, bg, 0.85);

		SELECTED_CONFLICT = registry.getRGB(IEMFCompareConstants.CONFLICTING_COLOR);
		if (SELECTED_CONFLICT == null) {
			SELECTED_CONFLICT = new RGB(255, 0, 0); // RED
		}
		CONFLICT = interpolate(SELECTED_CONFLICT, bg, 0.6);
		CONFLICT_FILL = interpolate(SELECTED_CONFLICT, bg, 0.97);
		CONFLICT_TEXT_FILL = interpolate(SELECTED_CONFLICT, bg, 0.85);

		RESOLVED = registry.getRGB(IEMFCompareConstants.RESOLVED_COLOR);
		if (RESOLVED == null) {
			RESOLVED = new RGB(0, 255, 0); // GREEN
		}
	}

	private static RGB interpolate(RGB fg, RGB bg, double scale) {
		if (fg != null && bg != null) {
			return new RGB((int)((1.0 - scale) * fg.red + scale * bg.red),
					(int)((1.0 - scale) * fg.green + scale * bg.green), (int)((1.0 - scale) * fg.blue + scale
							* bg.blue));
		}
		if (fg != null) {
			return fg;
		}
		if (bg != null) {
			return bg;
		}
		return new RGB(128, 128, 128); // a gray
	}

	public void dispose() {
		fColors.invalidateAll();
	}
}
