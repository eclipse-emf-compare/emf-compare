package org.eclipse.emf.compare.uml2.tests.profiles.data;

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

public class ProfileInputData extends AbstractInputData {

	private final static String EX_UML_RESOURCES_LOCATION = "jar:file:/D:/Dev/Ericsson/eclipseJuno3/plugins/org.eclipse.uml2.uml.resources_4.0.0.v20120604-0919.jar";

	private final static String EX_TEST_PROFILE_LOCATION = "jar:file:/D:/Dev/Ericsson/eclipseJuno3/dropins/plugins/org.eclipse.emf.compare.uml2.profiles.tests_1.0.0.201208091553.jar";

	private final static String UML_RESOURCES_JAR_LOCATION_ENV = "UML_RESOURCES_JAR_LOCATION";

	private final static String TEST_PROFILE_JAR_LOCATION_ENV = "TEST_PROFILE_JAR_LOCATION";

	private String umlResourcesJarLocation;

	private String testProfileJarLocation;

	public ProfileInputData() {
		umlResourcesJarLocation = System.getenv(UML_RESOURCES_JAR_LOCATION_ENV);
		testProfileJarLocation = System.getenv(TEST_PROFILE_JAR_LOCATION_ENV);
	}

	public Resource getA1Left() throws IOException {
		return loadFromClassloader("a1/left.uml"); //$NON-NLS-1$
	}

	public Resource getA1Right() throws IOException {
		return loadFromClassloader("a1/right.uml"); //$NON-NLS-1$
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

	// @Override
	// protected Resource loadFromClassloader(String string) throws IOException {
	// final URL fileURL = getClass().getResource(string);
	// final InputStream str = fileURL.openStream();
	// final URI uri = URI.createURI(fileURL.toString());
	//
	// ResourceSet resourceSet = new ResourceSetImpl();
	// UMLResourcesUtil.init(resourceSet);
	//
	// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml",
	// new UMLResourceFactoryImpl());
	// resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
	// resourceSet.getPackageRegistry().put(UML2CompareTestProfilePackage.eNS_URI,
	// UML2CompareTestProfilePackage.eINSTANCE);
	//
	// Map uriMap = resourceSet.getURIConverter().getURIMap();
	//
	// URI uri2 = URI.createURI("platform:/plugin/org.eclipse.uml2.uml.resources/");
	// uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), uri2.appendSegment("libraries")
	// .appendSegment(""));
	// uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), uri2.appendSegment("metamodels")
	// .appendSegment(""));
	// uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), uri2.appendSegment("profiles").appendSegment(
	// ""));
	//
	// // URI uri3 = URI.createURI("platform:/plugin/org.eclipse.emf.compare.uml2.profiles.tests/");
	// URI uri3 = URI
	// .createURI("jar:file:/D:/Dev/Ericsson/eclipseJuno3/dropins/plugins/org.eclipse.emf.compare.uml2.profiles.tests_1.0.0.201208091553.jar!/profile/");
	// uriMap.put(URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/"), uri3);
	//
	// Resource res = resourceSet.createResource(uri);
	// res.load(str, Collections.emptyMap());
	// str.close();
	//
	// EcoreUtil.resolveAll(resourceSet);
	// return res;
	// }

}
