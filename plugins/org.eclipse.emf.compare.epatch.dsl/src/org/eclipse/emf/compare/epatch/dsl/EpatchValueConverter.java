/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;
import org.eclipse.xtext.parsetree.AbstractNode;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchValueConverter extends DefaultTerminalConverters {
	@ValueConverter(rule = "FRAGMENT")
	public IValueConverter<String> FRAGMENT() {
		return new AbstractNullSafeConverter<String>() {

			@Override
			protected String internalToString(String arg0) {
				return "#" + arg0.toString();
			}

			@Override
			protected String internalToValue(String string, AbstractNode node) {
				if (string.length() > 0)
					return string.substring(1);
				return "";
			}
		};
	}
}
