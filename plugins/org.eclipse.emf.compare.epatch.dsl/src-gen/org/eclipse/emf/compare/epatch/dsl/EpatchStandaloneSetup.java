package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.ISetup;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IResourceFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Generated from StandaloneSetup.xpt!
 */
public class EpatchStandaloneSetup implements ISetup {

	public static void doSetup() {
		new EpatchStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	public Injector createInjectorAndDoEMFRegistration() {
		org.eclipse.xtext.common.TerminalsStandaloneSetup.doSetup();

		Injector injector = createInjector();
		register(injector);
		return injector;
	}

	public Injector createInjector() {
		return Guice.createInjector(new org.eclipse.emf.compare.epatch.dsl.EpatchRuntimeModule());
	}

	public void register(Injector injector) {
		if (!EPackage.Registry.INSTANCE.containsKey("http://www.eclipse.org/emf/compare/epatch/0.1")) {
			EPackage.Registry.INSTANCE.put("http://www.eclipse.org/emf/compare/epatch/0.1",
					org.eclipse.emf.compare.epatch.EpatchPackage.eINSTANCE);
		}
		// TODO registration of EValidators should be added here, too

		org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector
				.getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("epatch", resourceFactory);

	}
}
