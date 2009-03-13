/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.parsetree.reconstr.ITokenSerializer;

/**
 * used to register components to be used within the IDE.
 * 
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchRuntimeModule extends AbstractEpatchRuntimeModule {
	@Override
	public Class<? extends ITokenSerializer> bindITokenSerializer() {
		return EpatchFormatter.class;
	}

	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return EpatchValueConverter.class;
	}
}
