/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Fleck - initial API and implementation
 *     Stefan Dirix - bug 498583
 *     Laurent Delaigue - bug 498583
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.papyrus.internal.hook.migration;

import com.google.common.base.Function;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.compare.uml2.papyrus.internal.UMLPapyrusCompareMessages;
import org.eclipse.emf.compare.uml2.papyrus.internal.UMLPapyrusComparePlugin;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.services.labelprovider.service.LabelProviderService;
import org.eclipse.papyrus.uml.modelrepair.internal.stereotypes.StereotypeApplicationRepairSnippet;
import org.eclipse.papyrus.uml.modelrepair.internal.stereotypes.ZombieStereotypesDescriptor;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Analyzer to retrieve zombie and orphan stereotype applications. Zombies are stereotype applications for
 * which the defining {@link EPackage} could not be found. Orphans are stereotype applications for which the
 * referenced base element is missing. The implementation of this class is based on the Papyrus' model repair
 * capabilities.
 * 
 * @author Martin Fleck <mfleck@eclipsesource.com>
 */
@SuppressWarnings("restriction")
public class StereotypeApplicationRepair extends StereotypeApplicationRepairSnippet {

	/**
	 * The resource under repair.
	 */
	private Resource resource;

	/**
	 * Creates a new repair analyzer for zombie and orphan stereotype applications for the given resource.
	 * 
	 * @param resource
	 *            the resource under repair
	 */
	public StereotypeApplicationRepair(Resource resource) {
		// new constructor to provide our own profile supplier
		super();
		this.resource = resource;
		setLabelProviderService(createLabelProviderService());
		setProfileSupplier(createProfileSupplier());
	}

	@Override
	public void dispose(ModelSet modelsManager) {
		try {
			LabelProviderService s = (LabelProviderService)getSuperField("labelProviderService"); //$NON-NLS-1$
			if (s != null) {
				s.disposeService();
			}
		} catch (ServiceException ex) {
			UMLPapyrusComparePlugin.getDefault().getLog().log(new Status(IStatus.WARNING,
					UMLPapyrusComparePlugin.PLUGIN_ID, "Unable to dispose Label Provider Service", //$NON-NLS-1$
					ex));
		}
		super.dispose(modelsManager);
	}

	/**
	 * Reflectively sets the field with the given name in the super class to the specified fieldValue.
	 * 
	 * @param fieldName
	 *            name of the field in the super class
	 * @param fieldValue
	 *            new value of the field in the super class
	 */
	protected void setSuperField(String fieldName, Object fieldValue) {
		try {
			final Field superField = getClass().getSuperclass().getDeclaredField(fieldName);
			superField.setAccessible(true);
			superField.set(this, fieldValue);
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reflectively returns the value of field with the given name from the super class. If no such field can
	 * be found or an exception is thrown, null is returned.
	 * 
	 * @param fieldName
	 *            name of the field in the super class
	 * @return field value or null
	 */
	protected Object getSuperField(String fieldName) {
		try {
			final Field superField = getClass().getSuperclass().getDeclaredField(fieldName);
			superField.setAccessible(true);
			return superField.get(this);
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reflectively sets the adapter for this repair snippet. This is needed for Eclipse Luna as it expects a
	 * ModelSet as resource set for the migrated resource.
	 * 
	 * @param resourceSet
	 *            resource set containing the resource under repair
	 */
	private void setAdapter(ModelSet resourceSet) {
		// adapter needed to provide EPackage.Registry via the adapters resourceSet
		final Object adapterObject = getSuperField("adapter"); //$NON-NLS-1$
		if (adapterObject instanceof Adapter.Internal) {
			((Adapter.Internal)adapterObject).setTarget(resourceSet);
		}
	}

	/**
	 * Reflectively sets the labelProviderService for this repair snippet. This label provider service is not
	 * used during the comparison, but necessary for Papyrus which displays a user dialog during the
	 * migration. The Papyrus label provider needs the Workbench to be initialized, therefore we should use a
	 * simpler label provider to avoid this requirement.
	 * 
	 * @param labelProviderService
	 *            label provider service
	 */
	private void setLabelProviderService(LabelProviderService labelProviderService) {
		setSuperField("labelProviderService", labelProviderService); //$NON-NLS-1$
	}

	/**
	 * Reflectively sets the profileSupplier for this repair snippet.
	 * 
	 * @param profileSupplier
	 *            supplier of profiles for missing packages.
	 */
	protected void setProfileSupplier(Function<EPackage, Profile> profileSupplier) {
		setSuperField("dynamicProfileSupplier", profileSupplier); //$NON-NLS-1$
	}

	/**
	 * Creates a new label provider service that is used during the migration. In automatic migration, this
	 * label provider service is not used.
	 * 
	 * @return newly created label provider service
	 */
	protected LabelProviderService createLabelProviderService() {
		// we use a label provider service that does not need any special UI capabilities
		UMLLabelProviderService umlLabelProviderService = new UMLLabelProviderService();
		try {
			umlLabelProviderService.startService();
		} catch (ServiceException ex) {
			UMLPapyrusComparePlugin.getDefault().getLog().log(new Status(IStatus.WARNING,
					UMLPapyrusComparePlugin.PLUGIN_ID, "Unable to start UML Label Provider Service", //$NON-NLS-1$
					ex));
		}
		return umlLabelProviderService;
	}

	/***
	 * Creates a new profile supplier that is called if a package is missing and we need to find a profile
	 * that defines such a package.
	 * 
	 * @return newly created profile supplier
	 */
	protected Function<EPackage, Profile> createProfileSupplier() {
		return new MissingProfileSupplier(getRootElement(resource));
	}

	/**
	 * Returns the resource under analysis.
	 * 
	 * @return resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * Creates a {@link ModelSet} wrapper around the given resource set to be used for profile migration
	 * within Eclipse Luna.
	 * 
	 * @param resourceSet
	 *            resource set containing the resource under repair
	 * @return newly created model set wrapper
	 */
	protected ModelSet createModelSetWrapper(ResourceSet resourceSet) {
		final ModelSetWrapper modelSet = new ModelSetWrapper(resourceSet);
		// avoid read-only for our resource
		modelSet.setReadOnly(resource, Boolean.FALSE);
		return modelSet;
	}

	/**
	 * Analyzes the stereotype applications of the given resources root element and returns a descriptor
	 * containing zombie and orphan stereotype applications. For zombies, the defining package could not be
	 * found and for orphans the base element could not be found. The descriptor also already suggests repair
	 * actions, i.e., migrating the missing package for zombies if possible and deleting the stereotype
	 * application for orphans.
	 * 
	 * @return descriptor of zombie and orphan stereotypes
	 */
	public ZombieStereotypesDescriptor repair() {
		try {
			final ResourceSet resourceSet = resource.getResourceSet();
			final ModelSet modelSet = createModelSetWrapper(resourceSet);
			setAdapter(modelSet);
			modelSet.getResources().add(resource);
			final ZombieStereotypesDescriptor stereotypesDescriptor = getZombieStereotypes(resource);
			resourceSet.getResources().add(resource);
			return stereotypesDescriptor;
			// CHECKSTYLE:OFF
		} catch (Exception e) {
			// CHECKSTYLE:ON
			resource.getErrors().add(new ProfileMigrationDiagnostic(UMLPapyrusCompareMessages.getString(
					"profile.migration.exception", e, resource))); //$NON-NLS-1$
			UMLPapyrusComparePlugin.getDefault().getLog().log(new Status(IStatus.ERROR,
					UMLPapyrusComparePlugin.PLUGIN_ID, "Exception occurred during profile migration", //$NON-NLS-1$
					e)); // The exception stack trace will appear in the error log
		}
		return null;
	}

	/**
	 * Returns the root {@link Element element} in the given resource. If multiple elements are present the
	 * first one is returned.
	 * 
	 * @param resource
	 *            resource to check
	 * @return The first root element or null if no element is found
	 */
	protected static Element getRootElement(Resource resource) {
		return (Element)EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.ELEMENT);
	}
}
