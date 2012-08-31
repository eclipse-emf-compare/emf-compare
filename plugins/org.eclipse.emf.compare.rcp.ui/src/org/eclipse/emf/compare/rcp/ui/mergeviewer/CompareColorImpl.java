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
package org.eclipse.emf.compare.rcp.ui.mergeviewer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import org.eclipse.emf.compare.Diff;
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
 * Default implementation that use a cache to store created Color and that is listening to a preference store
 * for color configuration.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CompareColorImpl implements RemovalListener<RGB, Color>, ICompareColor {

	/**
	 * 
	 */
	private static final int MAX_CACHE_SIZE = 16;

	/**
	 * 
	 */
	private static final int MED_RGB_COMPONENT = 128;

	/**
	 * 
	 */
	private static final int MAX_RGB_COMPONENT = 255;

	/**
	 * 
	 */
	private static final double INTERPOLATION_SCALE_1 = 0.6;

	/**
	 * 
	 */
	private static final double INTERPOLATION_SCALE_2 = 0.97;

	public static final String INCOMING_COLOR = "INCOMING_COLOR"; //$NON-NLS-1$

	public static final String OUTGOING_COLOR = "OUTGOING_COLOR"; //$NON-NLS-1$

	public static final String CONFLICTING_COLOR = "CONFLICTING_COLOR"; //$NON-NLS-1$

	public static final String RESOLVED_COLOR = "RESOLVED_COLOR"; //$NON-NLS-1$

	private RGB fIncomingSelected;

	private RGB fIncoming;

	private RGB fIncomingFill;

	private RGB fConflictSelected;

	private RGB fConflict;

	private RGB fConflictFill;

	private RGB fOutgoingSelected;

	private RGB fOutgoing;

	private RGB fOutgoingFill;

	private RGB fResolved;

	private final Cache<RGB, Color> fColors;

	private final IPreferenceStore fPreferenceStore;

	private final IPropertyChangeListener fPreferenceChangeListener;

	private final boolean fLeftIsLocal;

	private Display fDisplay;

	public CompareColorImpl(Display display, boolean leftIsLocal, IPreferenceStore preferenceStore) {
		this.fDisplay = display;
		this.fPreferenceStore = preferenceStore;
		this.fColors = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_SIZE).removalListener(this).build(
				new CacheLoader<RGB, Color>() {
					@Override
					public Color load(RGB rgb) throws Exception {
						return new Color(fDisplay, rgb);
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

		fLeftIsLocal = leftIsLocal;
		updateColors();
	}

	public void onRemoval(RemovalNotification<RGB, Color> notification) {
		Color color = notification.getValue();
		if (!color.isDisposed()) {
			color.dispose();
		}
	}

	protected final void handlePropertyChangeEvent(PropertyChangeEvent event) {
		String key = event.getProperty();

		if (key.equals(INCOMING_COLOR) || key.equals(OUTGOING_COLOR) || key.equals(CONFLICTING_COLOR)
				|| key.equals(RESOLVED_COLOR)) {
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.ICompareColor#getFillColor(org.eclipse.emf.compare.Diff,
	 *      boolean, boolean, boolean)
	 */
	public Color getFillColor(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		return getColor(getFillRGB(diff, isThreeWay, isIgnoreAncestor, selected));
	}

	private RGB getFillRGB(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		RGB selectedFill = getBackground();
		if (isThreeWay && !isIgnoreAncestor) {
			boolean requiredConflictForWayOfMerge = false;

			if (diff.getConflict() == null && !requiredConflictForWayOfMerge) {
				switch (diff.getSource()) {
					case RIGHT:
						if (fLeftIsLocal) {
							return selected ? selectedFill : fIncomingFill;
						}
						return selected ? selectedFill : fOutgoingFill;
					case LEFT:
						if (fLeftIsLocal) {
							return selected ? selectedFill : fOutgoingFill;
						}
						return selected ? selectedFill : fIncomingFill;
				}
			} else {
				return selected ? selectedFill : fConflictFill;
			}
			return selected ? selectedFill : fConflictFill;
		}
		return selected ? selectedFill : fOutgoingFill;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.ICompareColor#getStrokeColor(org.eclipse.emf.compare.Diff,
	 *      boolean, boolean, boolean)
	 */
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
		return fDisplay.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
	}

	private void updateColors() {
		ColorRegistry registry = JFaceResources.getColorRegistry();

		RGB bg = getBackground();
		fIncomingSelected = registry.getRGB(INCOMING_COLOR);
		if (fIncomingSelected == null) {
			fIncomingSelected = new RGB(0, 0, MAX_RGB_COMPONENT); // BLUE
		}
		fIncoming = interpolate(fIncomingSelected, bg, INTERPOLATION_SCALE_1);
		fIncomingFill = interpolate(fIncomingSelected, bg, INTERPOLATION_SCALE_2);

		fOutgoingSelected = registry.getRGB(OUTGOING_COLOR);
		if (fOutgoingSelected == null) {
			fOutgoingSelected = new RGB(0, 0, 0); // BLACK
		}
		fOutgoing = interpolate(fOutgoingSelected, bg, INTERPOLATION_SCALE_1);
		fOutgoingFill = interpolate(fOutgoingSelected, bg, INTERPOLATION_SCALE_2);

		fConflictSelected = registry.getRGB(CONFLICTING_COLOR);
		if (fConflictSelected == null) {
			fConflictSelected = new RGB(MAX_RGB_COMPONENT, 0, 0); // RED
		}
		fConflict = interpolate(fConflictSelected, bg, INTERPOLATION_SCALE_1);
		fConflictFill = interpolate(fConflictSelected, bg, INTERPOLATION_SCALE_2);

		fResolved = registry.getRGB(RESOLVED_COLOR);
		if (fResolved == null) {
			fResolved = new RGB(0, MAX_RGB_COMPONENT, 0); // GREEN
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
		return new RGB(MED_RGB_COMPONENT, MED_RGB_COMPONENT, MED_RGB_COMPONENT); // a gray
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.util.ICompareColor#dispose()
	 */
	public void dispose() {
		fColors.invalidateAll();
		fPreferenceStore.removePropertyChangeListener(fPreferenceChangeListener);
	}
}
