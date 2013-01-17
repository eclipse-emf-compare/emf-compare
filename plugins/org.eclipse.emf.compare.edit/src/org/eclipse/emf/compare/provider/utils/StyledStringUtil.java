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
package org.eclipse.emf.compare.provider.utils;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

/**
 * This utility class holds methods that will be used by the item providers.
 * 
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public final class StyledStringUtil {
	/**
	 * Utility classes don't need a default constructor.
	 */
	private StyledStringUtil() {
		// Hides default constructor
	}

	private static FontData FONT_DATA = Display.getCurrent().getSystemFont().getFontData()[0];

	private static String DEFAULT_FONT_NAME = FONT_DATA.getName();

	static {
		JFaceResources.getColorRegistry().put("BLUE", new RGB(18, 120, 143)); //$NON-NLS-1$
		JFaceResources.getColorRegistry().put("RED", new RGB(143, 14, 55)); //$NON-NLS-1$
		JFaceResources.getColorRegistry().put("GRAY_BLUE", new RGB(93, 102, 150)); //$NON-NLS-1$
		JFaceResources.getColorRegistry().put("PURPLE", new RGB(150, 79, 138)); //$NON-NLS-1$
		JFaceResources.getColorRegistry().put("DARK_GRAY", new RGB(77, 82, 72)); //$NON-NLS-1$
	}

	public static enum FontStyle {
		NONE, BOLD, ITALIC
	}

	public static enum Color {

		WHITE(SWT.COLOR_WHITE), BLACK(SWT.COLOR_BLACK), GREEN(SWT.COLOR_GREEN), GRAY(SWT.COLOR_GRAY), YELLOW(
				SWT.COLOR_YELLOW), DARK_YELLOW(SWT.COLOR_DARK_YELLOW), DARK_GRAY(1004), BLUE(1000), RED(1001), GRAY_BLUE(
				1002), PURPLE(1003);

		private final int value;

		private Color(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public static StyledString StyledString(final String text) {
		return new StyledString(text);
	}

	public static StyledString StyledString(final String text, final Styler styler) {
		return new StyledString(text, styler);
	}

	public static StyledString StyledString(final String text, final Color fgColor) {
		return StyledString(text, fgColor, FontStyle.NONE);
	}

	public static StyledString StyledString(final String text, final FontStyle fontStyle) {
		return StyledString(text, Color.BLACK, fontStyle);
	}

	public static StyledString StyledString(final String text, final Color fgColor, final FontStyle fontStyle) {
		final Styler styler = new Styler() {

			@Override
			public void applyStyles(TextStyle textStyle) {
				switch (fontStyle) {
					case BOLD:
						textStyle.font = JFaceResources.getFontRegistry().getBold(DEFAULT_FONT_NAME);
						break;
					case ITALIC:
						textStyle.font = JFaceResources.getFontRegistry().getItalic(DEFAULT_FONT_NAME);
						break;
					default:
						break;
				}

				org.eclipse.swt.graphics.Color color = JFaceResources.getColorRegistry().get(fgColor.name());
				if (color != null) {
					textStyle.foreground = color;
				} else {
					textStyle.foreground = Display.getCurrent().getSystemColor(fgColor.getValue());
				}
			}
		};

		return new StyledString(text, styler);
	}

}
