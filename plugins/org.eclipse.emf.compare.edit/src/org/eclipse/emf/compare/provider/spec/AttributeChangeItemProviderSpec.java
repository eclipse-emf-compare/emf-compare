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
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.provider.AttributeChangeItemProvider;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class AttributeChangeItemProviderSpec extends AttributeChangeItemProvider {

	/**
	 * @param adapterFactory
	 */
	public AttributeChangeItemProviderSpec(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.MatchItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		AttributeChange attributeChange = (AttributeChange)object;
		Object ret = CompareItemProviderAdapterFactorySpec.getImage(getRootAdapterFactory(), attributeChange
				.getValue());

		if (ret == null) {
			ret = super.getImage(object);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.AttributeChangeItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final AttributeChange attChange = (AttributeChange)object;

		final String valueText = getValueText(attChange);
		final String attributeText = getAttributeText(attChange);

		String remotely = "";
		if (attChange.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely ";
		}

		String ret = "";
		switch (attChange.getKind()) {
			case ADD:
				ret = valueText + " has been " + remotely + "added to " + attributeText;
				break;
			case DELETE:
				ret = valueText + " has been " + remotely + "deleted from '" + attributeText;
				break;
			case CHANGE:
				ret = attributeText + " " + valueText + " has been " + remotely + "changed";
				break;
			case MOVE:
				ret = valueText + " has been " + remotely + "moved in '" + attributeText;
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + attChange.getKind());
		}

		return ret;
	}

	protected String getAttributeText(final AttributeChange attChange) {
		return attChange.getAttribute().getName();
	}

	protected String getValueText(final AttributeChange attChange) {
		String value = EcoreUtil.convertToString(attChange.getAttribute().getEAttributeType(), attChange
				.getValue());
		if (value == null) {
			value = "<null>";
		} else {
			value = Strings.elide(value, 20, "...");
		}
		return value;
	}
}
