package org.eclipse.emf.compare.ide.utils.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.compare.ide.internal.utils.NotifyingParserPool;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Test;

@SuppressWarnings("restriction")
public class Bug471045Test {

	@Test
	public void test() throws IOException {
		InputStream stream = getClass().getResourceAsStream("data/bug471045.ecore"); //$NON-NLS-1$
		try {
			Resource r = new XMIResourceImpl();
			NotifyingParserPool parserPool = new NotifyingParserPool(true);
			Map<Object, Object> loadOptions = new HashMap<Object, Object>();
			loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, parserPool);
			r.load(stream, loadOptions);
			// Prior to fix, this caused a BasicIndexOutOfBoundsException
		} finally {
			stream.close();
		}
	}

}
