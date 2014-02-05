/*******************************************************************************
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.ui.internal.mergeviewer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor;
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
 * @since 4.0
 */
public class CompareColorImpl implements RemovalListener<RGB, Color>, ICompareColor {

	/** INCOMING_COLOR. */
	public static final String INCOMING_COLOR = "INCOMING_COLOR"; //$NON-NLS-1$

	/** OUTGOING_COLOR. */
	public static final String OUTGOING_COLOR = "OUTGOING_COLOR"; //$NON-NLS-1$

	/** CONFLICTING_COLOR. */
	public static final String CONFLICTING_COLOR = "CONFLICTING_COLOR"; //$NON-NLS-1$

	/** RESOLVED_COLOR. */
	public static final String RESOLVED_COLOR = "RESOLVED_COLOR"; //$NON-NLS-1$

	/** MAX_CACHE_SIZE. */
	private static final int MAX_CACHE_SIZE = 16;

	/** MED_RGB_COMPONENT. */
	private static final int MED_RGB_COMPONENT = 128;

	/** MAX_RGB_COMPONENT. */
	private static final int MAX_RGB_COMPONENT = 255;

	/** INTERPOLATION_SCALE_1. */
	private static final double INTERPOLATION_SCALE_1 = 0.6;

	/** INTERPOLATION_SCALE_2. */
	private static final double INTERPOLATION_SCALE_2 = 0.97;

	/** RGB of incoming selected diff. */
	private RGB fIncomingSelected;

	/** RGB of incoming diffs. */
	private RGB fIncoming;

	/** RGB of incoming fill. */
	private RGB fIncomingFill;

	/** RGB of selected conflict. */
	private RGB fConflictSelected;

	/** RGB of conflicts. */
	private RGB fConflict;

	/** RGB of conflicts fill. */
	private RGB fConflictFill;

	/** RGB of outgoing selected diff. */
	private RGB fOutgoingSelected;

	/** RGB of outgoing diffs. */
	private RGB fOutgoing;

	/** RGB of outgoing fill. */
	private RGB fOutgoingFill;

	/** RGB of resolved diffs. */
	private RGB fResolved;

	/** Cache of loaded colors. */
	private final LoadingCache<RGB, Color> fColors;

	/** The {@link IPreferenceStore} used to store colors preferences. */
	private final IPreferenceStore fPreferenceStore;

	/** A property change listener to listen colors changes. */
	private final IPropertyChangeListener fPreferenceChangeListener;

	/** To know if the left model used in comparison is a local model or not. */
	private final boolean fLeftIsLocal;

	/** The SWT display. */
	private Display fDisplay;

	/**
	 * Default constructor.
	 * 
	 * @param display
	 *            the SWT display.
	 * @param leftIsLocal
	 *            to know if the left model used in comparison is a local model or not.
	 * @param preferenceStore
	 *            the {@link IPreferenceStore} used to store colors preferences.
	 */
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

	/**
	 * {@inheritDoc}.
	 */
	public void onRemoval(RemovalNotification<RGB, Color> notification) {
		Color color = notification.getValue();
		if (!color.isDisposed()) {
			color.dispose();
		}
	}

	/**
	 * Handle a change of a property.
	 * 
	 * @param event
	 *            the event representing the change.
	 */
	protected final void handlePropertyChangeEvent(PropertyChangeEvent event) {
		String key = event.getProperty();

		if (key.equals(INCOMING_COLOR) || key.equals(OUTGOING_COLOR) || key.equals(CONFLICTING_COLOR)
				|| key.equals(RESOLVED_COLOR)) {
			updateColors();
		}
	}

	/**
	 * Get the color from the given RGB.
	 * 
	 * @param rgb
	 *            the given RGB.
	 * @return the color from the given RGB.
	 */
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
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor#getFillColor(org.eclipse.emf.compare.Diff,
	 *      boolean, boolean, boolean)
	 */
	public Color getFillColor(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		return getColor(getFillRGB(diff, isThreeWay, isIgnoreAncestor, selected));
	}

	/**
	 * Get the RGB fill color according to the given diff, his comparison kind, the ancestor should be ignore
	 * or not, and is diff selected or not.
	 * 
	 * @param diff
	 *            the given Diff.
	 * @param isThreeWay
	 *            is the comparison a 3-way or 2-way.
	 * @param isIgnoreAncestor
	 *            is the ancestor should be ignore.
	 * @param selected
	 *            is the diff selected.
	 * @return the appropriate RGB color.
	 */
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
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.ide.ui.internal.contentmergeviewer.util.ICompareColor#getStrokeColor(org.eclipse.emf.compare.Diff,
	 *      boolean, boolean, boolean)
	 */
	public Color getStrokeColor(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		return getColor(getStrokeRGB(diff, isThreeWay, isIgnoreAncestor, selected));
	}

	/**
	 * Get the RGB stroke color according to the given diff, his comparison kind, the ancestor should be
	 * ignore or not, and is diff selected or not.
	 * 
	 * @param diff
	 *            the given Diff.
	 * @param isThreeWay
	 *            is the comparison a 3-way or 2-way.
	 * @param isIgnoreAncestor
	 *            is the ancestor should be ignore.
	 * @param selected
	 *            is the diff selected.
	 * @return the appropriate RGB color.
	 */
	private RGB getStrokeRGB(Diff diff, boolean isThreeWay, boolean isIgnoreAncestor, boolean selected) {
		if (isThreeWay && !isIgnoreAncestor) {
			boolean requiredConflictForWayOfMerge = false;

			if (diff != null && diff.getConflict() == null && !requiredConflictForWayOfMerge) {
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

	/**
	 * Get the RGB background color.
	 * 
	 * @return the RGB background color.
	 */
	private RGB getBackground() {
		return fDisplay.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB();
	}

	/**
	 * Update colors.
	 */
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

	/**
	 * Interpolate RGB color from the given colors.
	 * 
	 * @param fg
	 *            foreground color.
	 * @param bg
	 *            background color.
	 * @param scale
	 *            the scale to use.
	 * @return the interpolated RGB color.
	 */
	private static RGB interpolate(RGB fg, RGB bg, double scale) {
		final RGB rgb;
		if (fg != null && bg != null) {
			rgb = new RGB((int)((1.0 - scale) * fg.red + scale * bg.red),
					(int)((1.0 - scale) * fg.green + scale * bg.green), (int)((1.0 - scale) * fg.blue + scale
							* bg.blue));
		} else if (fg != null) {
			rgb = fg;
		} else if (bg != null) {
			rgb = bg;
		} else {
			rgb = new RGB(MED_RGB_COMPONENT, MED_RGB_COMPONENT, MED_RGB_COMPONENT); // a gray
		}
		return rgb;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.mergeviewer.ICompareColor#dispose()
	 */
	public void dispose() {
		fColors.invalidateAll();
		fPreferenceStore.removePropertyChangeListener(fPreferenceChangeListener);
	}
}
