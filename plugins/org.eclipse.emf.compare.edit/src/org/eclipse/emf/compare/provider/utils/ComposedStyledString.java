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

import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.provider.utils.IStyledString.IComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString.Style;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ComposedStyledString implements IComposedStyledString {

	private final List<IStyledString> content;

	public ComposedStyledString() {
		content = newArrayList();
	}

	public ComposedStyledString(String text) {
		this(text, Style.NO_STYLE);
	}

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
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<IStyledString> iterator() {
		return content.iterator();
	}

	private static final class StyledString implements IStyledString {
		private final String str;

		private final Style style;

		StyledString(String str, Style style) {
			this.str = str;
			this.style = style;
		}

		/**
		 * @return the str
		 */
		public String getString() {
			return str;
		}

		/**
		 * @return the style
		 */
		public Style getStyle() {
			return style;
		}
	}
}
