package org.eclipse.emf.compare.uml2.tests;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.uml2.uml.UMLPlugin;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.ResourcesPlugin;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

@SuppressWarnings("nls")
public class ProfilesInputData extends AbstractInputData {

	/** Store the set of the resource sets of the input data. */
	private Set<ResourceSet> sets = new LinkedHashSet<ResourceSet>();

	private static boolean doOnce;

	public Set<ResourceSet> getSets() {
		return sets;
	}

	@Override
	protected Resource loadFromClassLoader(String string) throws IOException {

		final URL fileURL = getClass().getResource(string);
		final InputStream str = fileURL.openStream();
		final URI uri = URI.createURI(fileURL.toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		sets.add(resourceSet);

		if (!EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE) {
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
					new EcoreResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
			resourceSet.getPackageRegistry().put(UML2CompareTestProfilePackage.eNS_URI,
					UML2CompareTestProfilePackage.eINSTANCE);

			UMLResourcesUtil.init(resourceSet);

			Map<String, URI> ePackageNsURIToProfileLocationMap = UMLPlugin
					.getEPackageNsURIToProfileLocationMap();

			ePackageNsURIToProfileLocationMap
					.put(UML2CompareTestProfilePackage.eNS_URI,
							URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw")); //$NON-NLS-1$

			final URL UMLJarredFileLocation = ResourcesPlugin.class.getResource("ResourcesPlugin.class");
			String UMLJarPath = UMLJarredFileLocation.toString();
			UMLJarPath = UMLJarPath.substring(0, UMLJarPath.indexOf('!'));

			final String thisNamespace = "org.eclipse.emf.compare.uml2.tests";
			final URL thisClassLocation = this.getClass().getResource(
					this.getClass().getSimpleName() + ".class");
			String staticProfilePath = thisClassLocation.toString();
			staticProfilePath = staticProfilePath.substring(0, staticProfilePath.indexOf(thisNamespace)
					+ thisNamespace.length());
			staticProfilePath += "/model/";

			if (!doOnce) {
				doOnce = true;
				System.out.println("Path to UML jar : " + UMLJarPath);
				debugPathResolution(UMLJarPath + "!/profiles/Ecore.profile.uml");
				System.out.println("Path to test profile : " + staticProfilePath
						+ "/uml2.compare.testprofile.profile.uml");
				debugPathResolution(staticProfilePath + "/uml2.compare.testprofile.profile.uml");
			}

			final Map<URI, URI> uriMap = resourceSet.getURIConverter().getURIMap();
			uriMap.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), URI.createURI(UMLJarPath
					+ "!/libraries/"));
			uriMap.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), URI.createURI(UMLJarPath
					+ "!/metamodels/"));
			uriMap.put(URI.createURI(UMLResource.PROFILES_PATHMAP), URI.createURI(UMLJarPath + "!/profiles/"));
			uriMap.put(URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/"), URI
					.createURI(staticProfilePath));

		}

		Resource resource = resourceSet.createResource(uri);

		resource.load(str, Collections.emptyMap());
		str.close();

		return resource;
	}

	private void debugPathResolution(String path) {
		System.out.println("Trying to resolve \"" + path + "\" on file system.");
		try {
			new URL(path).openConnection();
			System.out.println("resolution success!");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
