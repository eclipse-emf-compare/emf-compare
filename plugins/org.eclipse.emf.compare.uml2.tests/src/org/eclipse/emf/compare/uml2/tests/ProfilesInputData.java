package org.eclipse.emf.compare.uml2.tests;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class ProfilesInputData extends AbstractInputData {

	private final static String UML_RESOURCES_JAR_LOCATION_ENV = "UML_RESOURCES_JAR_LOCATION";

	private final static String TEST_PROFILE_JAR_LOCATION_ENV = "TEST_PROFILE_JAR_LOCATION";

	private String umlResourcesJarLocation;

	private String testProfileJarLocation;

	public ProfilesInputData() {
		umlResourcesJarLocation = System.getenv(UML_RESOURCES_JAR_LOCATION_ENV);
		testProfileJarLocation = System.getenv(TEST_PROFILE_JAR_LOCATION_ENV);
	}

	@Override
	protected Resource loadFromClassloader(String string) throws IOException {

		final URL fileURL = getClass().getResource(string);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		ResourceSet resourceSet = new ResourceSetImpl();

		if (!EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE) {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
					new EcoreResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

			Assert.assertTrue("Environment variables (jar:file:/D:/xxx.jar): "
					+ UML_RESOURCES_JAR_LOCATION_ENV + " and " + TEST_PROFILE_JAR_LOCATION_ENV
					+ " have to be defined.", umlResourcesJarLocation != null
					&& testProfileJarLocation != null);

			UMLResourcesUtil.init(resourceSet);

			final Map uriMap = resourceSet.getURIConverter().getURIMap();
			uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), URI.createURI(umlResourcesJarLocation
					+ "!/libraries/"));
			uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), URI.createURI(umlResourcesJarLocation
					+ "!/metamodels/"));
			uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), URI.createURI(umlResourcesJarLocation
					+ "!/profiles/"));
			uriMap.put(URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/"), URI
					.createURI(testProfileJarLocation + "!/profile/"));

		}

		Resource resource = resourceSet.createResource(uri);

		resource.load(str, Collections.emptyMap());
		str.close();

		return resource;
	}

}
