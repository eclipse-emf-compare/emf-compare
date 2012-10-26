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

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Conflict;
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

		String remotely = ""; //$NON-NLS-1$
		if (attChange.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely "; //$NON-NLS-1$
		}

		String ret = ""; //$NON-NLS-1$
		switch (attChange.getKind()) {
			case ADD:
				ret = valueText + " has been " + remotely + "added to " + attributeText; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case DELETE:
				ret = valueText + " has been " + remotely + "deleted from " + attributeText; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case CHANGE:
				String changeText = ReferenceChangeItemProviderSpec.changeText(attChange, attChange
						.getAttribute());
				ret = attributeText + " " + valueText + " has been " + remotely + changeText; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case MOVE:
				ret = valueText + " has been " + remotely + "moved in '" + attributeText; //$NON-NLS-1$ //$NON-NLS-2$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + attChange.getKind()); //$NON-NLS-1$
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
			value = "<null>"; //$NON-NLS-1$
		} else {
			value = Strings.elide(value, 50, "..."); //$NON-NLS-1$
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		Collection<?> children = super.getChildren(object);
		List<? super Object> ret = newArrayList(children);
		AttributeChange attributeChange = (AttributeChange)object;
		Conflict conflict = attributeChange.getConflict();
		if (conflict != null) {
			// ret.add(conflict);
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		AttributeChange attributeChange = (AttributeChange)object;
		switch (attributeChange.getState()) {
			case MERGED:
			case DISCARDED:
				return URI.createURI("color://rgb/156/156/156"); //$NON-NLS-1$
			default:
				return super.getForeground(object);
		}
	}
}
