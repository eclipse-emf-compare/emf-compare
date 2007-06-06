package org.eclipse.emf.compare.tests.unit;

import java.io.IOException;

import junit.framework.AssertionFailedError;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.tests.util.EMFCompareTestCase;
import org.eclipse.emf.compare.ui.legacy.AdapterUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;

/**
 * Test the finding of the EMF adapterFactory.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class TestFindAdapterFactory extends EMFCompareTestCase {
	/**
	 * Check the util finding the adapter factory.
	 * 
	 * @throws AssertionFailedError
	 * 			Thrown if the adapterFactory hasn't been found.
	 */
	public void testFindAdapterFactory() throws AssertionFailedError {
		final String uri = "http://www.eclipse.org/emf/compare/diff/1.0"; //$NON-NLS-1$
		final AdapterUtils util = new AdapterUtils();
		final AdapterFactory factory = util.findAdapterFactory(uri);
		assertNotNull(factory);
	}
	
	/**
	 * Find the adapter factory from an loaded file.
	 * 
	 * @throws AssertionFailedError
	 * 			Thrown if the adapterFactory hasn't been found.
	 * @throws IOException
	 * 			If the file does not exist.
	 */
	public void testFindAdapterFactoryFromFile() throws AssertionFailedError, IOException {
		final EObject model = ModelUtils.load(pluginFile("/data/result.diff")); //$NON-NLS-1$
		final AdapterUtils util = new AdapterUtils();		
		final AdapterFactory factory = util.findAdapterFactory(model);
		assertNotNull(factory);
	}
	
	
	/**
	 * Find the adapter factory from an eobject.
	 * 
	 * @throws AssertionFailedError
	 * 			Thrown if the adapterFactory hasn't been found.
	 */
	public void testFindAdapterFactoryFromEObject() throws AssertionFailedError {
		final DiffModel model = DiffFactory.eINSTANCE.createDiffModel();
		final AdapterUtils util = new AdapterUtils();		
		final AdapterFactory factory = util.findAdapterFactory(model);
		assertNotNull(factory);
	}
}
