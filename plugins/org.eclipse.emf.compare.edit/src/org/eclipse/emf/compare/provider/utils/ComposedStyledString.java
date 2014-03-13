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

import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;

/**
 * Composed styled string implementation backed by an {@link java.util.ArrayList}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComposedStyledString implements IComposedStyledString {

	/** the backing list. */
	private final List<IStyledString> content;

	/** Creates a new empty instance. */
	public ComposedStyledString() {
		content = newArrayList();
	}

	/**
	 * Creates a new instance with the given text without style.
	 * 
	 * @param text
	 *            the text.
	 */
	public ComposedStyledString(String text) {
		this(text, Style.NO_STYLE);
	}

	/**
	 * Creates a new instance with the given text and the given style.
	 * 
	 * @param text
	 *            the text.
	 * @param style
	 *            the style of the text.
	 */
	public ComposedStyledString(String text, Style style) {
		this();
		append(text, style);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString#getString()
	 */
	public String getString() {
		StringBuilder sb = new StringBuilder();
		for (IStyledString styledString : this) {
			sb.append(styledString.getString());
		}
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.util.IComposedStyledString#append(java.lang.String)
	 */
	public IComposedStyledString append(String str) {
		content.add(new StyledString(str, IStyledString.Style.NO_STYLE));
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.ui.internal.util.IComposedStyledString#append(java.lang.String,
	 *      org.eclipse.emf.compare.ide.ui.internal.util.IComposedStyledString.Style)
	 */
	public IComposedStyledString append(String str, Style style) {
		content.add(new StyledString(str, style));
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString#append(org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString)
	 * @since 4.0
	 */
	public IComposedStyledString append(IComposedStyledString composedStyledString) {
		for (IStyledString styledString : composedStyledString) {
			content.add(styledString);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<IStyledString> iterator() {
		return content.iterator();
	}

	/**
	 * Private implementation of {@link IStyledString} backed by the tuple String and Style.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class StyledString implements IStyledString {
		/** The string. */
		private final String str;

		/** The style. */
		private final Style style;

		/**
		 * Creates a new styled string.
		 * 
		 * @param str
		 *            the text value
		 * @param style
		 *            the style.
		 */
		StyledString(String str, Style style) {
			this.str = str;
			this.style = style;
		}

		/**
		 * Returns the string value.
		 * 
		 * @return the string value.
		 */
		public String getString() {
			return str;
		}

		/**
		 * Returns the style.
		 * 
		 * @return the style.
		 */
		public Style getStyle() {
			return style;
		}
	}
}
