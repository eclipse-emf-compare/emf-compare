/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import static com.google.common.collect.Iterables.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
import org.eclipse.emf.compare.ResourceAttachmentChange;
import org.eclipse.emf.compare.provider.IItemDescriptionProvider;
import org.eclipse.emf.compare.provider.IItemStyledLabelProvider;
import org.eclipse.emf.compare.provider.MatchResourceItemProvider;
import org.eclipse.emf.compare.provider.utils.ComposedStyledString;
import org.eclipse.emf.compare.provider.utils.IStyledString;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

/**
 * Specialized {@link MatchResourceItemProvider} returning nice output for {@link #getText(Object)} and
 * {@link #getImage(Object)}.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class MatchResourceItemProviderSpec extends MatchResourceItemProvider implements IItemStyledLabelProvider, IItemDescriptionProvider {

	/**
	 * Constructor calling super {@link #MatchResourceItemProviderSpec(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchResourceItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public Collection<?> getChildren(Object object) {
		Collection<Object> children = new ArrayList<Object>();
		MatchResource matchResource = (MatchResource)object;
		Comparison comparison = matchResource.getComparison();
		for (ResourceAttachmentChange rac : filter(comparison.getDifferences(),
				ResourceAttachmentChange.class)) {
			final String diffResourceURI = rac.getResourceURI();
			if (diffResourceURI != null
					&& (diffResourceURI.equals(matchResource.getLeftURI())
							|| diffResourceURI.equals(matchResource.getRightURI()) || diffResourceURI
								.equals(matchResource.getOriginURI()))) {
				children.add(rac);
			}
		}
		return children;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchResourceItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final MatchResource matchResource = (MatchResource)object;
		final String leftURI = matchResource.getLeftURI();
		final String rightURI = matchResource.getRightURI();

		final String commonBase = getCommonBase(leftURI, rightURI);

		String text = ""; //$NON-NLS-1$
		if (leftURI != null) {
			text += leftURI.substring(commonBase.length());
		}
		text += " <-> "; //$NON-NLS-1$
		if (rightURI != null) {
			text += rightURI.substring(commonBase.length());
		}
		if (matchResource.eContainer() instanceof Comparison
				&& ((Comparison)matchResource.eContainer()).isThreeWay()) {
			final String originURI = matchResource.getOriginURI();
			text += " (" + originURI.substring(commonBase.length()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return text;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchResourceItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final MatchResource matchResource = (MatchResource)object;
		Resource resource = matchResource.getLeft();
		Object image = null;
		if (resource == null) {
			resource = matchResource.getRight();
			if (resource == null) {
				resource = matchResource.getOrigin();
			}
		}

		if (resource != null) {
			IItemLabelProvider itemLabelProvider = (IItemLabelProvider)getRootAdapterFactory().adapt(
					resource, IItemLabelProvider.class);

			image = itemLabelProvider.getImage(resource);
			if (image == null) {
				image = super.getImage(object);
			}
		} else {
			image = super.getImage(object);
		}
		return image;
	}

	/**
	 * Returns the longest common starting substring of the two given strings.
	 * 
	 * @param left
	 *            First of the two strings for which we need the common starting substring.
	 * @param right
	 *            Second of the two strings for which we need the common starting substring.
	 * @return The longest common starting substring of the two given strings.
	 */
	public String getCommonBase(String left, String right) {
		if (left == null || right == null) {
			return ""; //$NON-NLS-1$
		}

		final char[] leftChars = left.toCharArray();
		final char[] rightChars = right.toCharArray();

		final StringBuilder buffer = new StringBuilder();
		StringBuilder fragmentBuffer = new StringBuilder();
		for (int i = 0; i < Math.min(leftChars.length, rightChars.length); i++) {
			if (leftChars[i] == rightChars[i]) {
				fragmentBuffer.append(leftChars[i]);

				if (leftChars[i] == '\\' || leftChars[i] == '/') {
					buffer.append(fragmentBuffer);
					fragmentBuffer = new StringBuilder();
				}
			} else {
				break;
			}
		}

		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemStyledLabelProvider#getStyledText(java.lang.Object)
	 */
	public IStyledString.IComposedStyledString getStyledText(Object object) {
		return new ComposedStyledString(getText(object));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.IItemDescriptionProvider#getDescription(java.lang.Object)
	 */
	public String getDescription(Object object) {
		return getText(object);
	}
}
