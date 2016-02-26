/*****************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Philip Langer (EclipseSource) - Initial API and implementation
 *****************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.papyrus.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

/**
 * This hook initializes the ElementTypeSetConfigurationRegistry before a Papyrus resource is loaded.
 * <p>
 * The class
 * {@link org.eclipse.papyrus.infra.elementtypesconfigurations.registries.ElementTypeSetConfigurationRegistry.ElementTypeSetConfigurationRegistry
 * ElementTypeSetConfigurationRegistry} is not available on Luna, so we have to use reflection to be backwards
 * compatible with Luna.
 * </p>
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 * @since 2.5
 */
public class ElementTypeSetConfigurationRegistryInitializingHook extends AbstractPapyrusResourceSetHook {

	private static final String BUNDLE_ID = "org.eclipse.papyrus.infra.elementtypesconfigurations"; //$NON-NLS-1$

	private static final String GET_INSTANCE = "getInstance"; //$NON-NLS-1$

	private static final String ELEMENTTYPESETCONFIGREG_CLASS_NAME = "org.eclipse.papyrus.infra.elementtypesconfigurations.registries.ElementTypeSetConfigurationRegistry"; //$NON-NLS-1$

	@Override
	public void preLoadingHook(ResourceSet resourceSet, Collection<? extends URI> uris) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					// ElementTypeSetConfigurationRegistry is not available on Luna, so we have to use
					// reflection to be backwards compatible with Luna
					Bundle bundle = Platform.getBundle(BUNDLE_ID);
					Class<?> registryClass = bundle.loadClass(ELEMENTTYPESETCONFIGREG_CLASS_NAME);
					Method getInstanceMethod = registryClass.getDeclaredMethod(GET_INSTANCE);
					getInstanceMethod.invoke(null);
				} catch (ClassNotFoundException e) {
					logException(e);
				} catch (NoSuchMethodException e) {
					logException(e);
				} catch (SecurityException e) {
					logException(e);
				} catch (IllegalAccessException e) {
					logException(e);
				} catch (IllegalArgumentException e) {
					logException(e);
				} catch (InvocationTargetException e) {
					logException(e);
				} catch (NullPointerException e) {
					logException(new RuntimeException(
							"Papyrus Element TypeSet Configuration Registry could not be found in bundle " //$NON-NLS-1$
									+ BUNDLE_ID + ", class " + ELEMENTTYPESETCONFIGREG_CLASS_NAME //$NON-NLS-1$
									+ ", method " + GET_INSTANCE, e)); //$NON-NLS-1$
				}
			}
		});
	}

	private void logException(Exception e) {
		CompareDiagramIDEUIPapyrusPlugin.getDefault().getLog().log(
				new Status(IStatus.WARNING, CompareDiagramIDEUIPapyrusPlugin.PLUGIN_ID,
						"Could not initialize ElementTypeSetConfigurationRegistry before comparison", e)); //$NON-NLS-1$
	}
}
