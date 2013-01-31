/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.provider.spec;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.MatchResource;
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
public class MatchResourceItemProviderSpec extends MatchResourceItemProvider implements IItemStyledLabelProvider {

	/**
	 * Constructor calling super {@link #MatchResourceItemProviderSpec(AdapterFactory)}.
	 * 
	 * @param adapterFactory
	 *            the adapter factory
	 */
	public MatchResourceItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
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
		if (resource == null) {
			resource = matchResource.getRight();
			if (resource == null) {
				resource = matchResource.getOrigin();
			}
		}

		if (resource != null) {
			IItemLabelProvider itemLabelProvider = (IItemLabelProvider)getRootAdapterFactory().adapt(
					resource, IItemLabelProvider.class);

			Object image = itemLabelProvider.getImage(resource);
			if (image != null) {
				return image;
			} else {
				return super.getImage(object);
			}
		} else {
			return super.getImage(object);
		}
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
}
