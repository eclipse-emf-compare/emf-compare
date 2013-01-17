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

import org.eclipse.emf.common.util.URI;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IStyledString {

	String getString();

	Style getStyle();

	interface IComposedStyledString extends Iterable<IStyledString> {

		IComposedStyledString append(String str);

		IComposedStyledString append(String str, Style style);

		String getString();
	}

	public static final class Style {

		public static final Style NO_STYLE = new Style(null, null, null, false, null, null, null, null, null);

		public static final Style QUALIFIER_STYLER = new Style(null, null, null, false, null, null, null,
				null, null);

		public static final Style COUNTER_STYLER = new Style(null, null, null, false, null, null, null, null,
				null);

		public static final Style DECORATIONS_STYLER = new Style(null, null, null, false, null, null, null,
				null, null);

		private final URI font;

		private final URI backgroundColor;

		private final URI foregroundColor;

		private final boolean isStrikedout;

		private final URI strikeoutColor;

		private final UnderLineStyle underlineStyle;

		private final URI underlineColor;

		private final BorderStyle borderStyle;

		private final URI borderColor;

		private Style(URI font, URI backgroundColor, URI foregroundColor, boolean isStrikedout,
				URI strikeoutColor, UnderLineStyle underlineStyle, URI underlineColor,
				BorderStyle borderStyle, URI borderColor) {
			this.font = font;
			this.backgroundColor = backgroundColor;
			this.foregroundColor = foregroundColor;
			this.isStrikedout = isStrikedout;
			this.strikeoutColor = strikeoutColor;
			this.underlineStyle = underlineStyle;
			this.underlineColor = underlineColor;
			this.borderStyle = borderStyle;
			this.borderColor = borderColor;
		}

		public URI getFont() {
			return font;
		}

		public URI getBackgoundColor() {
			return backgroundColor;
		}

		public URI getForegroundColor() {
			return foregroundColor;
		}

		public boolean isStrikedout() {
			return isStrikedout;
		}

		public URI getStrikeoutColor() {
			return strikeoutColor;
		}

		public UnderLineStyle getUnderlineStyle() {
			return underlineStyle;
		}

		public URI getUnderlineColor() {
			return underlineColor;
		}

		public BorderStyle getBorderStyle() {
			return borderStyle;
		}

		public URI getBorderColor() {
			return borderColor;
		}

		public static enum UnderLineStyle {
			NONE, SINGLE, DOUBLE, SQUIGGLE, ERROR, LINK;
		}

		public static enum BorderStyle {
			NONE, SOLID, DOT, DASH;
		}

		public static StyleBuilder builder() {
			return new StyleBuilder();
		}

		public static final class StyleBuilder {
			private static final URI BLACK = URI.createURI("color://rgb/0/0/0"); //$NON-NLS-1$

			private URI font;

			private URI backgroundColor;

			private URI foregroundColor;

			private boolean isStrikedout;

			private URI strikeoutColor;

			private UnderLineStyle underlineStyle = UnderLineStyle.NONE;

			private URI underlineColor;

			private BorderStyle borderStyle = BorderStyle.NONE;

			private URI borderColor;

			/**
			 * @param font
			 *            the font to set
			 * @return
			 */
			public StyleBuilder setFont(URI font) {
				this.font = font;
				return this;
			}

			/**
			 * @param backgroundColor
			 *            the backgroundColor to set
			 * @return
			 */
			public StyleBuilder setBackgroundColor(URI backgroundColor) {
				this.backgroundColor = backgroundColor;
				return this;
			}

			/**
			 * @param foregroundColor
			 *            the forregroundColor to set
			 * @return
			 */
			public StyleBuilder setForegroundColor(URI foregroundColor) {
				this.foregroundColor = foregroundColor;
				return this;
			}

			/**
			 * @param isStrikedout
			 *            the isStrikedout to set
			 * @return
			 */
			public StyleBuilder setStrikedout(boolean isStrikedout) {
				this.isStrikedout = isStrikedout;
				if (strikeoutColor == null) {
					strikeoutColor = BLACK;
				}
				return this;
			}

			/**
			 * @param strikeoutColor
			 *            the strikeoutColor to set
			 * @return
			 */
			public StyleBuilder setStrikeoutColor(URI strikeoutColor) {
				this.strikeoutColor = strikeoutColor;
				isStrikedout = true;
				return this;
			}

			/**
			 * @param borderColor
			 *            the borderColor to set
			 * @return
			 */
			public StyleBuilder setBorderColor(URI borderColor) {
				this.borderColor = borderColor;
				if (borderStyle == BorderStyle.NONE) {
					borderStyle = BorderStyle.SOLID;
				}
				return this;
			}

			/**
			 * @param borderStyle
			 *            the borderStyle to set
			 * @return
			 */
			public StyleBuilder setBorderStyle(BorderStyle borderStyle) {
				this.borderStyle = borderStyle;
				if (borderColor == null) {
					borderColor = BLACK;
				}
				return this;
			}

			/**
			 * @param underlineColor
			 *            the underlineColor to set
			 * @return
			 */
			public StyleBuilder setUnderlineColor(URI underlineColor) {
				this.underlineColor = underlineColor;
				if (underlineStyle == UnderLineStyle.NONE) {
					underlineStyle = UnderLineStyle.SINGLE;
				}
				return this;
			}

			/**
			 * @param underlineStyle
			 *            the underlineStyle to set
			 * @return
			 */
			public StyleBuilder setUnderlineStyle(UnderLineStyle underlineStyle) {
				this.underlineStyle = underlineStyle;
				if (underlineStyle == null) {
					underlineColor = BLACK;
				}
				return this;
			}

			public Style build() {
				return new Style(font, backgroundColor, foregroundColor, isStrikedout, strikeoutColor,
						underlineStyle, underlineColor, borderStyle, borderColor);
			}
		}
	}
}
