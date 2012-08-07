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
package org.eclipse.emf.compare.uml2.edit.utils;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLDiffItemProvider extends ForwardingItemProvider {

	/**
	 * @param delegate
	 */
	public UMLDiffItemProvider(ItemProviderAdapter delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.edit.utils.ForwardingItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;

		final String valueText = getValueText(umlDiff);
		final String referenceText = getReferenceText(umlDiff);

		String remotely = "";
		if (umlDiff.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely ";
		}

		String ret = "";
		switch (umlDiff.getKind()) {
			case ADD:
				ret = valueText + " has been " + remotely + "added to " + referenceText;
				break;
			case DELETE:
				ret = valueText + " has been " + remotely + "deleted from " + referenceText;
				break;
			case CHANGE:
				ret = referenceText + " " + valueText + " has been " + remotely + "changed";
				break;
			case MOVE:
				ret = valueText + " has been " + remotely + "moved in " + referenceText;
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + umlDiff.getKind());
		}

		return ret;
	}

	private String getValueText(final UMLDiff umlDiff) {
		String value = getText(getRootAdapterFactory(), umlDiff.getDiscriminant());
		if (value == null) {
			value = "<null>";
		} else {
			value = Strings.elide(value, 20, "...");
		}
		return value;
	}

	private String getReferenceText(final UMLDiff umlDiff) {
		String ret = "";
		switch (umlDiff.getKind()) {
			case ADD:
			case DELETE:
			case MOVE:
				EObject discriminant = umlDiff.getDiscriminant();
				EList<Diff> refinedBy = umlDiff.getRefinedBy();
				for (Diff diff : refinedBy) {
					if (diff instanceof ReferenceChange) {
						if (((ReferenceChange)diff).getValue() == discriminant) {
							ret = ((ReferenceChange)diff).getReference().getName();
						}
					} else if (diff instanceof AttributeChange) {

					}
				}
				break;
			case CHANGE:
				ret = "CHANGE";
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + umlDiff.getKind());
		}
		return ret;
	}

	/**
	 * Returns the text of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getText(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a text
	 * @return the text, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	static String getText(AdapterFactory adapterFactory, Object object) {
		// Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			Object adapter = adapterFactory.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getText(object);
			}
		}
		return null;
	}

}
