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
package org.eclipse.emf.compare.diagram.provider.spec;

import com.google.common.base.Preconditions;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.diagram.DiagramDiff;
import org.eclipse.emf.compare.provider.ForwardingItemProvider;
import org.eclipse.emf.compare.provider.spec.Strings;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ForwardingDiagramDiffItemProvider extends ForwardingItemProvider {

	public ForwardingDiagramDiffItemProvider(ItemProviderAdapter delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ForwardingItemProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		final Collection<?> ret;
		DiagramDiff diagramDiff = (DiagramDiff)object;
		if (diagramDiff.getKind() == DifferenceKind.CHANGE) {
			ret = diagramDiff.getRefinedBy();
		} else {
			ret = super.getChildren(object);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ForwardingItemProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		return !getChildren(object).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ForwardingItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		DiagramDiff diagramDiff = (DiagramDiff)object;

		final String valueText = getValueText(diagramDiff);

		String remotely = "";
		if (diagramDiff.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely ";
		}

		String ret = "";
		switch (diagramDiff.getKind()) {
			case ADD:
				ret = valueText + " has been " + remotely + "added";
				break;
			case DELETE:
				ret = valueText + " has been " + remotely + "deleted";
				break;
			case CHANGE:
				ret = valueText + " has been " + remotely + "changed";
				break;
			case MOVE:
				ret = valueText + " has been " + remotely + "moved";
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + diagramDiff.getKind());
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ForwardingItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final DiagramDiff diagramDiff = (DiagramDiff)object;
		Object image = null;
		if (diagramDiff.getView() instanceof View) {
			image = getImage(getRootAdapterFactory(), ((View)diagramDiff.getView()).getElement());
		} else {
			super.getImage(object);
		}
		return image;
	}

	/**
	 * Returns the image of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getImage(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a image
	 * @return the image, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	private static Object getImage(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			Object adapter = adapterFactory.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getImage(object);
			}
		}
		return null;
	}

	private String getValueText(final DiagramDiff diagramDiff) {
		String value = "<null>";
		if (diagramDiff.getView() instanceof View) {
			value = diagramDiff.getView().eClass().getName() + " "
					+ getText(getRootAdapterFactory(), ((View)diagramDiff.getView()).getElement());
		}
		value = Strings.elide(value, 50, "...");
		return value;
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
	private static String getText(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			Object adapter = adapterFactory.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getText(object);
			}
		}
		return null;
	}

}
