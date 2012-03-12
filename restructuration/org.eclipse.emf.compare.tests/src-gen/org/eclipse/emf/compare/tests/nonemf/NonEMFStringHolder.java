/**
 * Copyright (c) 2009, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Stephen McCants - Initial API and implementation
 */
package org.eclipse.emf.compare.tests.nonemf;

/**
 * This is a Non-EMF object that we will use with our models.
 * 
 * @author smccants
 */
public class NonEMFStringHolder {
	private String string;

	public void setString(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NonEMFStringHolder) {
			final NonEMFStringHolder other = (NonEMFStringHolder)obj;
			return other.string == string || other.string != null && other.string.equals(string);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (string != null)
			return string.hashCode();
		return super.hashCode();
	}
}
