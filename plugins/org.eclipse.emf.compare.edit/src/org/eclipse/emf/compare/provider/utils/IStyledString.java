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
package org.eclipse.emf.compare.provider.utils;

import org.eclipse.emf.common.util.URI;

/**
 * Utility class that mimics the JFace's StyledString.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public interface IStyledString {

	/**
	 * Returns the Java string for this styled string.
	 * 
	 * @return the Java string for this styled string.
	 */
	String getString();

	/**
	 * Returns the style associated with this string.
	 * 
	 * @return the style associated with this string.
	 */
	Style getStyle();

	/**
	 * An iterable of {@link IStyledString}. It is appendable.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	interface IComposedStyledString extends Iterable<IStyledString> {

		/**
		 * Append the given string with no style.
		 * 
		 * @param str
		 *            the string to append.
		 * @return this instance to let you chain the calls.
		 */
		IComposedStyledString append(String str);

		/**
		 * Append the given string with the given style.
		 * 
		 * @param str
		 *            the string to append.
		 * @param style
		 *            the style of the appended string.
		 * @return this instance to let you chain the calls.
		 */
		IComposedStyledString append(String str, Style style);

		/**
		 * Appends the given composed styled string to this.
		 * 
		 * @param composedStyledString
		 *            the styled string to append.
		 * @return this instance.
		 * @since 4.0
		 */
		IComposedStyledString append(IComposedStyledString composedStyledString);

		/**
		 * Returns the Java string for this composed styled string.
		 * 
		 * @return the Java string for this composed styled string.
		 */
		String getString();
	}

	/**
	 * A style class for {@link IStyledString}.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static final class Style {

		/** Instance that represent a default unstyled string. */
		public static final Style NO_STYLE = new Style(null, null, null, false, null, null, null, null, null);

		/** Constant that will eventually map to org.eclipse.jface.viewers.StyledString.QUALIFIER_STYLER. */
		public static final Style QUALIFIER_STYLER = new Style(null, null, null, false, null, null, null,
				null, null);

		/** Constant that will eventually map to org.eclipse.jface.viewers.StyledString.COUNTER_STYLER. */
		public static final Style COUNTER_STYLER = new Style(null, null, null, false, null, null, null, null,
				null);

		/** Constant that will eventually map to org.eclipse.jface.viewers.StyledString.DECORATIONS_STYLER. */
		public static final Style DECORATIONS_STYLER = new Style(null, null, null, false, null, null, null,
				null, null);

		/** The font URI. */
		private final URI font;

		/** The background color URI. */
		private final URI backgroundColor;

		/** The foreground color URI. */
		private final URI foregroundColor;

		/** Is the string strikedout. */
		private final boolean isStrikedout;

		/** The strikeout color URI. */
		private final URI strikeoutColor;

		/** The style of the underline. */
		private final UnderLineStyle underlineStyle;

		/** The underline color URI. */
		private final URI underlineColor;

		/** The border style. */
		private final BorderStyle borderStyle;

		/** The border color URI. */
		private final URI borderColor;

		/**
		 * Creates a new instance with the given values for each component of this style.
		 * 
		 * @param font
		 *            the font
		 * @param backgroundColor
		 *            the background color
		 * @param foregroundColor
		 *            the foreground color
		 * @param isStrikedout
		 *            whether to strikedout the text
		 * @param strikeoutColor
		 *            the color of the strikedout
		 * @param underlineStyle
		 *            the style of the underline
		 * @param underlineColor
		 *            the color of the underline
		 * @param borderStyle
		 *            the style of the border
		 * @param borderColor
		 *            the color of the border
		 */
		// CHECKSTYLE:OFF private method
		private Style(URI font, URI backgroundColor, URI foregroundColor, boolean isStrikedout,
				URI strikeoutColor, UnderLineStyle underlineStyle, URI underlineColor,
				BorderStyle borderStyle, URI borderColor) {
			// CHECKSTYLE:ON
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

		/**
		 * The possible styles of the underline. These are those supported by JFace 3.8. Other versions or
		 * other widgets toolkit may not support all of these or may support more than these.
		 * 
		 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
		 */
		public static enum UnderLineStyle {
			/** No underline. */
			NONE,
			/** Single line. */
			SINGLE,
			/** Double line. */
			DOUBLE,
			/** Squiggle line. */
			SQUIGGLE,
			/** Error line (often dash line). */
			ERROR,
			/** Hyperlink. */
			LINK;
		}

		/**
		 * The possible styles of the border. These are those supported by JFace 3.8. Other versions or other
		 * widgets toolkit may not support all of these or may support more than these.
		 * 
		 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
		 */
		public static enum BorderStyle {
			/** No border. */
			NONE,
			/** Solid thin border. */
			SOLID,
			/** Dot thin border. */
			DOT,
			/** Dash thin border. */
			DASH;
		}

		/**
		 * Returns a new builder for the style.
		 * 
		 * @return a new builder for the style.
		 */
		public static StyleBuilder builder() {
			return new StyleBuilder();
		}

		/**
		 * A {@link Style} builder.
		 * 
		 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
		 */
		public static final class StyleBuilder {

			/** The black color URI constant. */
			private static final URI BLACK = URI.createURI("color://rgb/0/0/0"); //$NON-NLS-1$

			/** The font. */
			private URI font;

			/** The background color. */
			private URI backgroundColor;

			/** The foreground color. */
			private URI foregroundColor;

			/** Is the text strikedout. */
			private boolean isStrikedout;

			/** The strikedout color. */
			private URI strikeoutColor;

			/** The underline style. */
			private UnderLineStyle underlineStyle = UnderLineStyle.NONE;

			/** The underline color. */
			private URI underlineColor;

			/** The border style. */
			private BorderStyle borderStyle = BorderStyle.NONE;

			/** The border color. */
			private URI borderColor;

			/**
			 * Set the font.
			 * 
			 * @param pFont
			 *            the font to set.
			 * @return this.
			 */
			public StyleBuilder setFont(URI pFont) {
				this.font = pFont;
				return this;
			}

			/**
			 * Set the background color.
			 * 
			 * @param pBackgroundColor
			 *            the backgroundColor to set
			 * @return this.
			 */
			public StyleBuilder setBackgroundColor(URI pBackgroundColor) {
				this.backgroundColor = pBackgroundColor;
				return this;
			}

			/**
			 * Set the foreground color.
			 * 
			 * @param pForegroundColor
			 *            the forregroundColor to set
			 * @return this
			 */
			public StyleBuilder setForegroundColor(URI pForegroundColor) {
				this.foregroundColor = pForegroundColor;
				return this;
			}

			/**
			 * Set the strikedout.
			 * 
			 * @param pIsStrikedout
			 *            the isStrikedout to set
			 * @return this.
			 */
			public StyleBuilder setStrikedout(boolean pIsStrikedout) {
				this.isStrikedout = pIsStrikedout;
				if (strikeoutColor == null) {
					strikeoutColor = BLACK;
				}
				return this;
			}

			/**
			 * Set the strikeout color.
			 * 
			 * @param pStrikeoutColor
			 *            the strikeoutColor to set
			 * @return this.
			 */
			public StyleBuilder setStrikeoutColor(URI pStrikeoutColor) {
				this.strikeoutColor = pStrikeoutColor;
				isStrikedout = true;
				return this;
			}

			/**
			 * Set the border color.
			 * 
			 * @param pBorderColor
			 *            the borderColor to set
			 * @return this.
			 */
			public StyleBuilder setBorderColor(URI pBorderColor) {
				this.borderColor = pBorderColor;
				if (borderStyle == BorderStyle.NONE) {
					borderStyle = BorderStyle.SOLID;
				}
				return this;
			}

			/**
			 * Set the border color.
			 * 
			 * @param pBorderStyle
			 *            the borderStyle to set
			 * @return this.
			 */
			public StyleBuilder setBorderStyle(BorderStyle pBorderStyle) {
				this.borderStyle = pBorderStyle;
				if (borderColor == null) {
					borderColor = BLACK;
				}
				return this;
			}

			/**
			 * Set the underline color.
			 * 
			 * @param pUnderlineColor
			 *            the underlineColor to set
			 * @return this.
			 */
			public StyleBuilder setUnderlineColor(URI pUnderlineColor) {
				this.underlineColor = pUnderlineColor;
				if (underlineStyle == UnderLineStyle.NONE) {
					underlineStyle = UnderLineStyle.SINGLE;
				}
				return this;
			}

			/**
			 * Set the underline style.
			 * 
			 * @param pUnderlineStyle
			 *            the underlineStyle to set
			 * @return this.
			 */
			public StyleBuilder setUnderlineStyle(UnderLineStyle pUnderlineStyle) {
				this.underlineStyle = pUnderlineStyle;
				if (pUnderlineStyle == null) {
					underlineColor = BLACK;
				}
				return this;
			}

			/**
			 * Builds and returns a new Style instance regarding the values that have been given beforehands.
			 * 
			 * @return a new Style instance regarding the values that have been given beforehands.
			 */
			public Style build() {
				return new Style(font, backgroundColor, foregroundColor, isStrikedout, strikeoutColor,
						underlineStyle, underlineColor, borderStyle, borderColor);
			}
		}
	}
}
