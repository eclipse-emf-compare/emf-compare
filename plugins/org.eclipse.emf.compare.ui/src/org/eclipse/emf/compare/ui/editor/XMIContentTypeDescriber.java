/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;

/**
 * This class will try and determine if a given file is an xmi file. Assumes UTF-8 encoding and that an xml
 * file starts with <tt>&quot;&lt;?xml &quot;</tt>. If these conditions are met, we seek the declaration of
 * the xmi:version of the file.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class XMIContentTypeDescriber implements ITextContentDescriber {
	/** This is the expected prefix of an XML file. */
	private static final String XML_PREFIX = "<?xml "; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.content.IContentDescriber#describe(java.io.InputStream,
	 *      org.eclipse.core.runtime.content.IContentDescription)
	 */
	public int describe(InputStream input, IContentDescription description) throws IOException {
		input.reset();
		int result = VALID;

		final byte[] xmlPrefixBytes = XML_PREFIX.getBytes("UTF-8"); //$NON-NLS-1$
		final byte[] prefix = new byte[xmlPrefixBytes.length];

		// We cannot determine content type if there is less bytes than the expected xml prefix
		if (input.read(prefix) < prefix.length)
			result = INDETERMINATE;
		else
			// Checks if file has an xml declaration
			for (int i = 0; i < prefix.length; i++)
				if (prefix[i] != xmlPrefixBytes[i])
					result = INVALID;

		// FIXME now need to look for xmi:version. char by char reading until 'x' is reached then tries to getBytes("xmi:version")?

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.runtime.content.ITextContentDescriber#describe(java.io.Reader,
	 *      org.eclipse.core.runtime.content.IContentDescription)
	 */
	// TODO readability is poor with do {} while; is there a way to avoid this?
	public int describe(Reader input, IContentDescription description) throws IOException {
		final BufferedReader reader = new BufferedReader(input);
		String line = reader.readLine();
		int result = VALID;

		// EOS reached, returns now to avoid potential NPEs
		if (line == null)
			result = INDETERMINATE;
		else if (!line.startsWith(XML_PREFIX))
			// The first line of the stream should be our prefix
			result = INVALID;
		else
			do {
				line = reader.readLine();
				if (line == null)
					// xml declaration should be followed by at least one line containing xmi:version
					// declaration
					return INVALID;
				else if (line.contains("xmi:version")) //$NON-NLS-1$
					/*
					 * breaking while now. no need for further testing since UI is waiting and we now know
					 * this is an XMI file.
					 */
					break;
				else if (line.endsWith(">")) //$NON-NLS-1$
					result = INVALID;
			} while (!line.endsWith(">")); //$NON-NLS-1$

		return result;
	}

	public QualifiedName[] getSupportedOptions() {
		return null;
	}
}
