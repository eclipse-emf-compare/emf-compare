/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.internal.provider.decorator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.provider.ExtendedItemProviderDecorator;
import org.eclipse.emf.compare.uml2.internal.provider.UMLCompareEditPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Item provider decorator for stereotyped {@link Element}s.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypedElementItemProviderDecorator extends ExtendedItemProviderDecorator implements IEditingDomainItemProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider, IItemFontProvider {

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            {@link ComposeableAdapterFactory}.
	 */
	public StereotypedElementItemProviderDecorator(ComposeableAdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public String getText(Object object) {
		if (object instanceof Element) {
			Element elem = (Element)object;
			EList<Stereotype> appliedStereotypes = elem.getAppliedStereotypes();
			if (!appliedStereotypes.isEmpty()) {
				return getStereotypedElementLabel((Element)object, appliedStereotypes);
			}
		}
		return super.getText(object);
	}

	/**
	 * Gets a label for a stereotyped {@link Element}.
	 * 
	 * @param element
	 *            stereotyped {@link Element}.
	 * @param appliedStereotypes
	 *            List of stereotypes to display.
	 * @return the label of the stereotyped element.
	 */
	private String getStereotypedElementLabel(Element element, EList<Stereotype> appliedStereotypes) {
		StringBuilder labelBuilder = new StringBuilder();
		String stereotypes = Joiner.on(',').join(
				Iterables.transform(appliedStereotypes, new Function<Stereotype, String>() {

					public String apply(Stereotype input) {
						return input.getName();
					}
				}));

		labelBuilder.append('<').append(stereotypes).append("> "); //$NON-NLS-1$
		if (element instanceof NamedElement) {
			NamedElement namedElement = (NamedElement)element;
			labelBuilder.append(namedElement.getName());
		}
		return labelBuilder.toString();
	}

	@Override
	public Object getImage(Object object) {
		if (object instanceof Element && !((Element)object).getAppliedStereotypes().isEmpty()) {
			return new ComposedImage(getStereotypeIcons((Element)object, super.getImage(object)));
		}
		return super.getImage(object);
	}

	/**
	 * Gets all icons from the stereotypes applied on the element.
	 * <p>
	 * The icons for a stereotype are computed this way:
	 * </p>
	 * <ol>
	 * <li>IF:There is {@link org.eclipse.uml2.uml.Image} in the profile model then computes the icon from its
	 * location. (warning: currently the feature "content" of a {@link org.eclipse.uml2.uml.Image} is not used
	 * to get a icon)</li>
	 * <li>ELSEIF: There is ItemProvider registered into the platform for the stereotype application use it.</li>
	 * <li>ELSE: Uses the base element icon.</li>
	 * </ol>
	 * <p>
	 * This method has been inspired from the implementation of
	 * {@link org.eclipse.uml2.uml.edit.providers.ElementItemProvider#overlayImage(Object object, Object image)}
	 * </p>
	 * 
	 * @param element
	 *            base {@link Element}.
	 * @param baseElementIcon
	 *            Base element icon.
	 * @return List of icon to use
	 */
	protected List<Object> getStereotypeIcons(Element element, Object baseElementIcon) {
		List<Object> images = null;
		Iterator<Stereotype> stereotypeIterator = element.getAppliedStereotypes().iterator();
		while (stereotypeIterator.hasNext() && images == null) {
			Stereotype appliedStereotype = stereotypeIterator.next();
			if (!appliedStereotype.getIcons().isEmpty()) {
				images = getStereotypeIconsFromProfile(appliedStereotype);
			} else {
				Object img = getStereotypeIconFromItemProvider(appliedStereotype, element);
				if (img != null) {
					images = Collections.singletonList(img);
				}
			}
		}
		if (images == null) {
			// If there is no icon for any stereotypes then uses the base element icon.
			images = Collections.singletonList(baseElementIcon);
		}

		return images;
	}

	/**
	 * Get the icon of a stereotype using registered item providers.
	 * 
	 * @param appliedStereotype
	 *            Applied stereotype from which you want to retrieve an icon.
	 * @param element
	 *            Base UML element.
	 * @return The icon of the stereotype or <code>null</code> if none.
	 */
	private Object getStereotypeIconFromItemProvider(Stereotype appliedStereotype, Element element) {
		EObject steretotypeApplication = element.getStereotypeApplication(appliedStereotype);
		return getStereotypeIconFromItemProvider(steretotypeApplication);
	}

	/**
	 * Gets the icons for a stereotype using the UML profile model.
	 * 
	 * @param appliedStereotype
	 *            Applied stereotyped from which you want to retrieve icons.
	 * @return List of icons to use for this stereotype or <code>null</code> if no icon has been found.
	 * @see Stereotype#getIcons()
	 */
	protected List<Object> getStereotypeIconsFromProfile(Stereotype appliedStereotype) {
		List<Object> images = new ArrayList<Object>();

		for (org.eclipse.uml2.uml.Image icon : appliedStereotype.getIcons()) {
			String location = icon.getLocation();

			if (!UML2Util.isEmpty(location)) {
				Object img = getIconFromLocation(icon.eResource(), location);
				if (img != null) {
					images.add(img);
				}
			} else {
				// TODO handle icons defined by the "content" feature...
			}
		}
		if (images.isEmpty()) {
			images = null;
		}
		return images;
	}

	/**
	 * Gets an icon from its location.
	 * 
	 * @param eResource
	 *            Resource holding the {@link org.eclipse.uml2.uml.Image} element.
	 * @param location
	 *            Location of the icon
	 * @return an icon or <code>null</code> otherwise.
	 */
	protected Object getIconFromLocation(Resource eResource, String location) {
		Object img = null;
		if (eResource != null) {
			ResourceSet resourceSet = eResource.getResourceSet();

			if (resourceSet != null) {
				URIConverter uriConverter = resourceSet.getURIConverter();
				URI normalizedURI = uriConverter.normalize(eResource.getURI());

				URI uri = URI.createURI(location).resolve(normalizedURI);

				URL url;
				try {
					url = new URL(uriConverter.normalize(uri).toString());
					url.openStream().close();
					img = url;
				} catch (MalformedURLException e) {
					UMLCompareEditPlugin.getPlugin().getLog().log(
							new Status(IStatus.WARNING, "org.eclipse.emf.compare.uml2.edit", //$NON-NLS-1$
									UMLCompareEditPlugin.INSTANCE.getString(
											"Unable_To_Retreive_Icon_Error_Message", //$NON-NLS-1$
											new Object[] {location }), e));
				} catch (IOException e) {
					UMLCompareEditPlugin.getPlugin().getLog().log(
							new Status(IStatus.WARNING, "org.eclipse.emf.compare.uml2.edit", //$NON-NLS-1$
									UMLCompareEditPlugin.INSTANCE.getString(
											"Unable_To_Retreive_Icon_Error_Message", //$NON-NLS-1$
											new Object[] {location }), e));
				}
			}
		}
		return img;
	}

	/**
	 * Retrieves the icon from registered item providers.
	 * <p>
	 * This implementation remove the {@link ReflectiveItemProvider} to prevent getting the basic EMF icon.
	 * </p>
	 * </p>
	 * 
	 * @param object
	 *            Input.
	 * @return The icon of the input object or <code>null</code> if no icon has been found.
	 */
	public Object getStereotypeIconFromItemProvider(Object object) {
		/*
		 * Use the root adapter factory to retrieve the adapter factory of the static profiles. This only
		 * works if the EMF.edit code has been generated.
		 */
		ComposeableAdapterFactory rootAdapterFactory = ((ComposeableAdapterFactory)adapterFactory)
				.getRootAdapterFactory();
		IItemLabelProvider itemLabelProvider = (IItemLabelProvider)rootAdapterFactory.adapt(object,
				IItemLabelProvider.class);
		if (itemLabelProvider instanceof IItemProviderDecorator) {
			IChangeNotifier cNotifier = ((IItemProviderDecorator)itemLabelProvider)
					.getDecoratedItemProvider();
			if (cNotifier instanceof IItemLabelProvider) {
				itemLabelProvider = (IItemLabelProvider)cNotifier;
			}

		}
		if (itemLabelProvider == null || itemLabelProvider instanceof ReflectiveItemProvider) {
			return null;
		} else {
			return itemLabelProvider.getImage(object);
		}

	}

	public ResourceLocator getResourceLocator() {
		return ((IChildCreationExtender)adapterFactory).getResourceLocator();
	}

}
