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
package org.eclipse.emf.compare.uml2.tests.edit.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.uml2.internal.provider.decorator.ForwardingItemProviderAdapterDecorator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Util class for testing the
 * {@link org.eclipse.emf.compare.uml2.internal.provider.decorator.UMLProfileItemProviderAdapterFactoryDecorator}
 * .
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class StereotypedElementItemProviderTestUtil {

	/**
	 * Checks that every stereotyped element have a correct label and a correct icon.
	 * 
	 * @param stereotypedElementItemProviderDecorator
	 * @param expectedStaticIcons
	 * @param resource
	 */
	public static void checkIconAndLabel(AdapterFactory stereotypedElementItemProviderDecorator,
			HashMap<String, String> expectedStaticIcons, Resource resource) {
		TreeIterator<EObject> resourceContentIterator = resource.getAllContents();
		while (resourceContentIterator.hasNext()) {
			EObject eObject = resourceContentIterator.next();
			IItemLabelProvider itemLabelProvider = (IItemLabelProvider)stereotypedElementItemProviderDecorator
					.adapt(eObject, IItemLabelProvider.class);
			if (eObject instanceof Element) {
				if (!((Element)eObject).getAppliedStereotypes().isEmpty()) {
					Element element = (Element)eObject;
					// Checks label
					EList<Stereotype> appliedStereotypes = element.getAppliedStereotypes();
					final String expectedLabel = buildExpectedLabelOnStereotypedElement(appliedStereotypes,
							element);
					assertEquals(expectedLabel, itemLabelProvider.getText(eObject));
					// Checks icon
					List<String> actualIcons = getIconsLocation(itemLabelProvider.getImage(eObject));
					assertEquals(1, actualIcons.size());
					assertEquals(
							"Wrong icon for stereotypes :" + generateExpectedIconKey(appliedStereotypes), //$NON-NLS-1$
							getExpectedIcon(appliedStereotypes, expectedStaticIcons), actualIcons.get(0));
				} else {
					assertNotNull(itemLabelProvider);
					assertTrue("Wrong item provider for object " + itemLabelProvider.getText(eObject), //$NON-NLS-1$
							itemLabelProvider instanceof ForwardingItemProviderAdapterDecorator);
				}
			}
		}
	}

	private static String getExpectedIcon(List<Stereotype> stereotypes, Map<String, String> expectedIconsMap) {
		return expectedIconsMap.get(generateExpectedIconKey(stereotypes));
	}

	/**
	 * Gets the key used to retrieve the expected icon.
	 * 
	 * @param stereotypes
	 * @return
	 */
	private static String generateExpectedIconKey(List<Stereotype> stereotypes) {
		return Joiner.on(',').join(Iterables.transform(stereotypes, TO_NAME_FUNCTION));
	}

	/**
	 * Retrieves the name of each icon from the object returned by an item provider.
	 * <p>
	 * The name of an icon is only the last segment of its full path location
	 * </p>
	 * 
	 * @param icon
	 *            Object returned by the {@link org.eclipse.emf.edit.provider.ItemProvider#getImage()} method.
	 * @return A list of name of icon.
	 */
	public static List<String> getIconsLocation(Object icon) {
		final List<String> result = Lists.newArrayList();
		if (icon instanceof ComposedImage) {
			for (Object image : ((ComposedImage)icon).getImages()) {
				result.addAll(getIconsLocation(image));
			}
		} else if (icon instanceof URL) {

			String file = ((URL)icon).getFile();
			// Only stores the file name to avoid problems between relative and absolute path. The name of the
			// file is discriminant enough for our test.
			String[] segment = file.split("/"); //$NON-NLS-1$
			result.add(segment[segment.length - 1]);
		}
		return result;
	}

	private static final Function<Stereotype, String> TO_NAME_FUNCTION = new Function<Stereotype, String>() {

		public String apply(Stereotype input) {
			return input.getName();
		}
	};

	private static String buildExpectedLabelOnStereotypedElement(List<Stereotype> stereotypes, Element elem) {
		StringBuilder labelBuilder = new StringBuilder();
		String stereotypesLabel = Joiner.on(',').join(Iterables.transform(stereotypes, TO_NAME_FUNCTION));
		labelBuilder.append('<').append(stereotypesLabel).append('>');
		if (elem instanceof NamedElement) {
			labelBuilder.append(' ').append(((NamedElement)elem).getName());
		}
		return labelBuilder.toString();
	}

}
