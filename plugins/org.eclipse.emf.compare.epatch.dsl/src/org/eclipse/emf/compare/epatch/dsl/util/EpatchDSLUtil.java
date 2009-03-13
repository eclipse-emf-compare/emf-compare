/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.dsl.EpatchRuntimeModule;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.parsetree.reconstr.SerializerUtil;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Guice;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchDSLUtil {
	public static Epatch loadEpatch(String file) {
		XtextResourceSet rs = new XtextResourceSet();
		Resource r = rs.getResource(URI.createURI(file), true);
		return (Epatch)r.getContents().get(0);
	}

	public static Epatch parseEpatch(String resURI, String patch) {
		XtextResourceSet rs = new XtextResourceSet();
		Resource r = rs.createResource(URI.createURI(resURI));
		ByteArrayInputStream in = new ByteArrayInputStream(patch.getBytes());
		try {
			r.load(in, Collections.emptyMap());
			for (Diagnostic d : r.getErrors())
				System.out.println("Error:" + d);
			for (Diagnostic d : r.getWarnings())
				System.out.println("Warning:" + d);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return (Epatch)r.getContents().get(0);
	}

	public static Epatch loadEpatch(Class<?> ctx, String file) {
		file = "/" + ctx.getPackage().getName().replace('.', '/') + "/" + file;
		XtextResourceSet rs = new XtextResourceSet();
		Resource r = rs.createResource(URI.createURI(file));
		InputStream in = ctx.getResourceAsStream(file);
		if (in == null)
			throw new RuntimeException("File not found:" + file);
		try {
			r.load(in, Collections.emptyMap());
			for (Diagnostic d : r.getErrors())
				System.out.println("Error:" + d);
			for (Diagnostic d : r.getWarnings())
				System.out.println("Warning:" + d);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return (Epatch)r.getContents().get(0);
	}

	public static String serializeEpatch(Epatch patch) {
		return epatchSerializer.serialize(patch);
	}

	private static SerializerUtil epatchSerializer = Guice.createInjector(new EpatchRuntimeModule())
			.getInstance(SerializerUtil.class);

	public static String serializeEpatchUnformatted(Epatch patch) {
		return serializeEpatch(patch);
		// Injector i = ServiceRegistry.getInjector(EpatchStandaloneSetup
		// .getServiceScope());
		// ITokenSerializer ts = new WhitespacePreservingTokenSerializer();
		// IParseTreeConstructor ptc = new EpatchParseTreeConstructor();
		// i.injectMembers(ts);
		// i.injectMembers(ptc);
		// ByteArrayOutputStream out = new ByteArrayOutputStream();
		// try {
		// ts.serialize(ptc.serialize(patch), out);
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// }
		// return out.toString();
	}
}
