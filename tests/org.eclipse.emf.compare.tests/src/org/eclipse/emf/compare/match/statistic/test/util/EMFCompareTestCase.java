package org.eclipse.emf.compare.match.statistic.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * TestCase with usefull utility methods
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class EMFCompareTestCase extends TestCase {
	/**
	 * Assert file contents are strictly equals
	 * 
	 * @param file1
	 * @param file2
	 */
	public void assertFileContentsEqual(final File file1, final File file2) {
		assertTrue("File contents are not equals for " + file1.getName()
				+ " and " + file2.getName(), TestUtils.readFile(file1, false)
				.equals(TestUtils.readFile(file2, false)));
	}

	/**
	 * Save a model in a file
	 * 
	 * @param root
	 * @param outputFile
	 * @throws IOException
	 */
	public void save(final EObject root, final File outputFile)
			throws IOException {
		final FileOutputStream out = new FileOutputStream(outputFile);

		final XMIResourceImpl resource = new XMIResourceImpl();
		resource.getContents().add(root);
		resource.save(out, Collections.EMPTY_MAP);

	}

	/**
	 * @param path
	 * @return a file bundled inside a plugin knowing it's path
	 */
	public File pluginFile(final String path) {
		return new File(TestUtils.getPluginDirectory() + path);
	}

	/**
	 * Compare two dirs
	 * 
	 * @param dir1
	 * @param dir2
	 * @param fileNameSuffixExpected
	 */
	public void compareDirs(final File dir1, final File dir2,
			final String fileNameSuffixExpected) {
		if (!dir1.isDirectory() || !dir2.isDirectory()) {
			throw new RuntimeException("dir1 and dir2 are not folders! "); //$NON-NLS-1$
		}

		// saving files from dir2 in a hashmap
		final HashMap files2 = new HashMap();
		for (int i = 0; i < dir2.listFiles().length; i++) {
			files2.put(dir2.listFiles()[i].getName(), dir2.listFiles()[i]);
		}

		for (int i = 0; i < dir1.listFiles().length; i++) {
			final File file1 = dir1.listFiles()[i];
			if (files2.containsKey(file1.getName() + fileNameSuffixExpected)) {
				assertFileContentsEqual(file1, (File) files2.get(file1
						.getName()
						+ fileNameSuffixExpected));
			} else {
				throw new RuntimeException(file1.getName()
						+ fileNameSuffixExpected + " is missing for comparison"); //$NON-NLS-1$
			}

		}

	}

	/**
	 * load a model from an IFile
	 * 
	 * @param file
	 * @return loaded model
	 */
	public EObject load(final IFile file) {
		EObject result = null;
		final URI modelURI = URI.createURI(file.getFullPath().toString());
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		final Resource modelResource = resourceSet.getResource(modelURI, true);
		// EcoreUtil.resolveAll(resourceSet);
		result = (EObject) ((modelResource.getContents().size() > 0) ? modelResource
				.getContents().get(0)
				: null);
		return result;
	}

	/**
	 * Load a model from a java.io.File
	 * 
	 * @param file
	 * @return the model
	 */
	public static EObject load(final File file) {
		try {
			final FileInputStream in = new FileInputStream(file);
			try {
				return load(in);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}

	}

	/**
	 * Load a model from an InputStream
	 * 
	 * @param in
	 * @return the model
	 */
	public static EObject load(final InputStream in) {
		final XMIResourceImpl resource = new XMIResourceImpl();

		try {
			resource.load(in, Collections.EMPTY_MAP);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		final EObject result = (EObject) ((resource.getContents().size() > 0) ? resource
				.getContents().get(0)
				: null);
		return result;
	}

	/**
	 * Save a model 
	 * @param root
	 * @param path
	 * @throws IOException
	 */
	public void save(final EObject root, final String path) throws IOException {
		final URI modelURI = URI.createURI(path);
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		final Resource newModelResource = resourceSet.createResource(modelURI);
		newModelResource.getContents().add(root);
		final Map options = new HashMap();
		options.put(XMLResource.OPTION_ENCODING, System
				.getProperty("file.encoding"));
		newModelResource.save(options);

	}

}
