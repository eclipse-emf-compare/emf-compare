package org.eclipse.emf.compare.uml2.tests;

import java.util.Map;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.uml2.profile.test.uml2comparetestprofile.UML2CompareTestProfilePackage;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.uml2.uml.UMLPlugin;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class AbstractStaticProfileTest extends AbstractUMLProfileTest {

	@BeforeClass
	public static void fillRegistriesForStatic() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			EPackage.Registry.INSTANCE.put(UML2CompareTestProfilePackage.eNS_URI,
					UML2CompareTestProfilePackage.eINSTANCE); // registers
			// against
			// EPackage.Registry
			// It is required to link the EPackage to the UML package of the UML Profile
			Map<String, URI> ePackageNsURIToProfileLocationMap = UMLPlugin
					.getEPackageNsURIToProfileLocationMap();
			ePackageNsURIToProfileLocationMap
					.put(UML2CompareTestProfilePackage.eNS_URI,
							URI.createURI("pathmap://UML_COMPARE_TESTS_PROFILE/uml2.compare.testprofile.profile.uml#_hZFTgIwkEeC_FYHMbTTxXw")); //$NON-NLS-1$
		}
	}

	@AfterClass
	public static void resetRegistriesForStatic() {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			UMLPlugin.getEPackageNsURIToProfileLocationMap().remove(UML2CompareTestProfilePackage.eNS_URI);
			EPackage.Registry.INSTANCE.remove(UML2CompareTestProfilePackage.eNS_URI);
		}
	}
}
