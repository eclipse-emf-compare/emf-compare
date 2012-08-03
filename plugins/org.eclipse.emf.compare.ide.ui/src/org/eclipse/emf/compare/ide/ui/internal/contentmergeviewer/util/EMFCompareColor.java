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
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util;

import static org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.CompareConfigurationExtension.getBoolean;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.contentmergeviewer.ContentMergeViewer;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareConstants;
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

	private RGB fIncomingSelected;

	private RGB fIncoming;

	private RGB fIncomingFill;

	private RGB fIncomingTextFill;

	private RGB fConflictSelected;

	private RGB fConflict;

	private RGB fConflictFill;

	private RGB fConflictTextFill;

	private RGB fOutgoingSelected;

	private RGB fOutgoing;

	private RGB fOutgoingFill;

	private RGB fOutgoingTextFill;

	private RGB fResolved;

	private final Cache<RGB, Color> fColors;

	private final ContentMergeViewer fContentMergeViewer;

	private final IPreferenceStore fPreferenceStore;

	private final IPropertyChangeListener fPreferenceChangeListener;

	private final IPropertyChangeListener fCompareConfigurationChangeListener;

	private final boolean fLeftIsLocal;

	private boolean fMergeTipRightToLeft;

	private final CompareConfiguration fCompareConfiguration;

	public EMFCompareColor(ContentMergeViewer contentMergeViewer, IPreferenceStore preferenceStore,
			CompareConfiguration compareConfiguration) {
		this.fContentMergeViewer = contentMergeViewer;
		this.fPreferenceStore = preferenceStore;
		this.fCompareConfiguration = compareConfiguration;
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

		this.fCompareConfigurationChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				handlePropertyChangeEvent(event);
			}
		};

		if (fPreferenceStore != null) {
			fPreferenceStore.addPropertyChangeListener(fPreferenceChangeListener);
		}

		compareConfiguration.addPropertyChangeListener(fCompareConfigurationChangeListener);

		fMergeTipRightToLeft = getBoolean(compareConfiguration, EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT,
				EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT_DEFAULT);
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

		if (key.equals(EMFCompareConstants.INCOMING_COLOR)
				|| key.equals(EMFCompareConstants.OUTGOING_COLOR)
				|| key.equals(EMFCompareConstants.CONFLICTING_COLOR)
				|| key.equals(EMFCompareConstants.RESOLVED_COLOR)) {
			updateColors();
		}

		if (key.equals(EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT)) {
			fMergeTipRightToLeft = getBoolean(fCompareConfiguration,
					EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT,
					EMFCompareConstants.MERGE_TIP_RIGHT_TO_LEFT_DEFAULT);
			// fContentMergeViewer.getControl().redraw();
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
			boolean requiredConflictForWayOfMerge = false;

			if (diff.getConflict() == null && !requiredConflictForWayOfMerge) {
				switch (diff.getSource()) {
					case RIGHT:
						if (fLeftIsLocal) {
							return selected ? selected_fill : fIncomingFill;
						}
						return selected ? selected_fill : fOutgoingFill;
					case LEFT:
						if (fLeftIsLocal) {
							return selected ? selected_fill : fOutgoingFill;
						}
						return selected ? selected_fill : fIncomingFill;
				}
			} else {
				return selected ? selected_fill : fConflictFill;
			}
			return selected ? selected_fill : fConflictFill;
		}
		return selected ? selected_fill : fOutgoingFill;
	}

	public Color getStrokeColor(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		return getColor(getStrokeRGB(diff, isThreeWay, isIgnoreAncestor, selected));
	}

	private RGB getStrokeRGB(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		if (isThreeWay && !isIgnoreAncestor) {
			boolean requiredConflictForWayOfMerge = false;

			if (diff.getConflict() == null && !requiredConflictForWayOfMerge) {
				switch (diff.getSource()) {
					case RIGHT:
						if (fLeftIsLocal) {
							return selected ? fIncomingSelected : fIncoming;
						}
						return selected ? fOutgoingSelected : fOutgoing;
					case LEFT:
						if (fLeftIsLocal) {
							return selected ? fOutgoingSelected : fOutgoing;
						}
						return selected ? fIncomingSelected : fIncoming;
				}
			} else {
				return selected ? fConflictSelected : fConflict;
			}
			return selected ? fConflictSelected : fConflict;
		}
		return selected ? fOutgoingSelected : fOutgoing;
	}

	private RGB getBackground() {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
	}

	private void updateColors() {
		ColorRegistry registry = JFaceResources.getColorRegistry();

		RGB bg = getBackground();
		fIncomingSelected = registry.getRGB(EMFCompareConstants.INCOMING_COLOR);
		if (fIncomingSelected == null) {
			fIncomingSelected = new RGB(0, 0, 255); // BLUE
		}
		fIncoming = interpolate(fIncomingSelected, bg, 0.6);
		fIncomingFill = interpolate(fIncomingSelected, bg, 0.97);
		fIncomingTextFill = interpolate(fIncomingSelected, bg, 0.85);

		fOutgoingSelected = registry.getRGB(EMFCompareConstants.OUTGOING_COLOR);
		if (fOutgoingSelected == null) {
			fOutgoingSelected = new RGB(0, 0, 0); // BLACK
		}
		fOutgoing = interpolate(fOutgoingSelected, bg, 0.6);
		fOutgoingFill = interpolate(fOutgoingSelected, bg, 0.97);
		fOutgoingTextFill = interpolate(fOutgoingSelected, bg, 0.85);

		fConflictSelected = registry.getRGB(EMFCompareConstants.CONFLICTING_COLOR);
		if (fConflictSelected == null) {
			fConflictSelected = new RGB(255, 0, 0); // RED
		}
		fConflict = interpolate(fConflictSelected, bg, 0.6);
		fConflictFill = interpolate(fConflictSelected, bg, 0.97);
		fConflictTextFill = interpolate(fConflictSelected, bg, 0.85);

		fResolved = registry.getRGB(EMFCompareConstants.RESOLVED_COLOR);
		if (fResolved == null) {
			fResolved = new RGB(0, 255, 0); // GREEN
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
		fPreferenceStore.removePropertyChangeListener(fPreferenceChangeListener);
		fCompareConfiguration.removePropertyChangeListener(fCompareConfigurationChangeListener);
	}
}
