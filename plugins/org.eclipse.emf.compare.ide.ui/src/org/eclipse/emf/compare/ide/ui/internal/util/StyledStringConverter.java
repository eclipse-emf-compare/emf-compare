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
package org.eclipse.emf.compare.ide.ui.internal.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style.UnderLineStyle;
import org.eclipse.emf.edit.ui.provider.ExtendedColorRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class StyledStringConverter {

	private final Font defaultFont;

	private final Color defaultForeground;

	private final Color defaultBackground;

	public StyledStringConverter(Font defaultFont, Color defaultForeground, Color defaultBackground) {
		this.defaultFont = defaultFont;
		this.defaultForeground = defaultForeground;
		this.defaultBackground = defaultBackground;
	}

	public StyledString toJFaceStyledString(IComposedStyledString composedStyledString) {
		StyledString ret = new StyledString();
		for (IStyledString styledString : composedStyledString) {
			Style style = styledString.getStyle();
			String str = styledString.getString();
			if (style == IStyledString.Style.NO_STYLE) {
				ret.append(str);
			} else if (style == IStyledString.Style.COUNTER_STYLER) {
				ret.append(str, StyledString.COUNTER_STYLER);
			} else if (style == IStyledString.Style.DECORATIONS_STYLER) {
				ret.append(str, StyledString.DECORATIONS_STYLER);
			} else if (style == IStyledString.Style.QUALIFIER_STYLER) {
				ret.append(str, StyledString.QUALIFIER_STYLER);
			} else {
				ret.append(str, toJFaceStyle(style));
			}
		}
		return ret;
	}

	/**
	 * @param style
	 * @return
	 */
	private Styler toJFaceStyle(final Style style) {
		return new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				textStyle.font = getFont(style.getFont());

				textStyle.background = getColor(style.getBackgoundColor());
				textStyle.foreground = getColor(style.getForegroundColor());

				textStyle.strikeout = style.isStrikedout();
				textStyle.strikeoutColor = getColor(style.getStrikeoutColor());

				textStyle.borderColor = getColor(style.getBorderColor());
				switch (style.getBorderStyle()) {
					case SOLID:
						textStyle.borderStyle = SWT.BORDER_SOLID;
						break;
					case DOT:
						textStyle.borderStyle = SWT.BORDER_DOT;
						break;
					case DASH:
						textStyle.borderStyle = SWT.BORDER_DASH;
						break;
					case NONE:
						textStyle.borderStyle = SWT.NONE;
						break;
				}

				if (style.getUnderlineStyle() != UnderLineStyle.NONE) {
					textStyle.underline = true;
					textStyle.underlineColor = getColor(style.getUnderlineColor());
					switch (style.getUnderlineStyle()) {
						case SINGLE:
							textStyle.underlineStyle = SWT.UNDERLINE_SINGLE;
							break;
						case DOUBLE:
							textStyle.underlineStyle = SWT.UNDERLINE_DOUBLE;
							break;
						case ERROR:
							textStyle.underlineStyle = SWT.UNDERLINE_ERROR;
							break;
						case LINK:
							textStyle.underlineStyle = SWT.UNDERLINE_LINK;
							break;
						case SQUIGGLE:
							textStyle.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
							break;
					}
				}
			}
		};
	}

	private Color getColor(URI colorURI) {
		return ExtendedColorRegistry.INSTANCE.getColor(defaultForeground, defaultBackground, colorURI);
	}

	private Font getFont(URI fontURI) {
		return ExtendedFontRegistry.INSTANCE.getFont(defaultFont, fontURI);
	}
}
