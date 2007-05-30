package org.eclipse.emf.compare.tests.unit;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.diff.DiffFactory;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.tests.util.EMFCompareTestCase;
import org.eclipse.emf.compare.ui.legacy.AdapterUtils;
import org.eclipse.emf.ecore.EObject;

/**
 * Test the finding of the EMF adapterFactory
 * 
 * @author Cedric Brun  cedric.brun@obeo.fr 
 * 
 */
public class TestFindAdapterFactory extends EMFCompareTestCase {

	/**
	 * Check the util finding the adapter factory
	 * 
	 * @throws Exception
	 */
	public void testFindAdapterFactory() throws Exception {
		String uri = "http://www.eclipse.org/emf/compare/diff/1.0"; //$NON-NLS-1$
		AdapterUtils util = new AdapterUtils();
		AdapterFactory factory = util.findAdapterFactory(uri);
		assertNotNull(factory);
	}
	/**
	 * Find the adapter factory from an loaded file
	 */
	public void testFindAdapterFactoryFromFile() throws Exception{
		EObject model = load(pluginFile("/data/result.diff")); //$NON-NLS-1$
		AdapterUtils util = new AdapterUtils();		
		AdapterFactory factory = util.findAdapterFactory(model);
		assertNotNull(factory);
	}
	
	
	/**
	 * Find the adapter factory from an eobject
	 */
	public void testFindAdapterFactoryFromEObject() throws Exception{
		DiffModel model = DiffFactory.eINSTANCE.createDiffModel();
		AdapterUtils util = new AdapterUtils();		
		AdapterFactory factory = util.findAdapterFactory(model);
		assertNotNull(factory);
	}
}
