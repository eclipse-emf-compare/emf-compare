package org.eclipse.emf.compare.uml2.tests;

import java.net.URL;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.uml2.uml.resources.ResourcesPlugin;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractUMLProfileTest extends AbstractUMLTest {

	@BeforeClass
	public static void addProfilePathmap() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			final URL UMLJarredFileLocation = ResourcesPlugin.class.getResource("ResourcesPlugin.class");
			String UMLJarPath = UMLJarredFileLocation.toString();
			UMLJarPath = UMLJarPath.substring(0, UMLJarPath.indexOf('!'));
			URIConverter.URI_MAP.put(URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/"), URI
					.createURI(getProfilePath()));
		}
	}

	@AfterClass
	public static void resetProfilePathmap() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			URIConverter.URI_MAP.remove(URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/"));
		}
	}

	protected static String getProfilePath() {
		final String thisNamespace = "org.eclipse.emf.compare.uml2.tests";
		final URL thisClassLocation = AbstractUMLProfileTest.class.getResource(AbstractUMLProfileTest.class
				.getSimpleName()
				+ ".class");
		String profilePath = thisClassLocation.toString();
		profilePath = profilePath.substring(0, profilePath.indexOf(thisNamespace) + thisNamespace.length());
		profilePath += "/model/";
		return profilePath;
	}
}
