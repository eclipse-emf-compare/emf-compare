/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.extensions.provider.spec;

import com.google.common.base.Preconditions;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.provider.AdapterFactoryUtil;
import org.eclipse.emf.compare.provider.ForwardingItemProvider;
import org.eclipse.emf.compare.provider.spec.Strings;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Unique item provider for every diagram differences.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class ForwardingDiagramDiffItemProvider extends ForwardingItemProvider {

	/** Max size allowed for the display of label. */
	private static final int CHAR_SIZE_LIMIT = 50;

	/** Used to describe the change. */
	private static final String HAS_BEEN = " has been "; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            Default item provider adapter.
	 */
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

		final String remotely = getRemoteText(diagramDiff);

		String ret = "";
		switch (diagramDiff.getKind()) {
			case ADD:
				ret = valueText + HAS_BEEN + remotely + "added"; //$NON-NLS-1$
				break;
			case DELETE:
				ret = valueText + HAS_BEEN + remotely + "deleted"; //$NON-NLS-1$
				break;
			case CHANGE:
				ret = valueText + HAS_BEEN + remotely + "changed"; //$NON-NLS-1$
				break;
			case MOVE:
				ret = valueText + HAS_BEEN + remotely + "moved"; //$NON-NLS-1$
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName() //$NON-NLS-1$
						+ " value: " + diagramDiff.getKind()); //$NON-NLS-1$
		}

		return ret;
	}

	/**
	 * It is used to get the word to describe that the change is in the remote side.
	 * 
	 * @param diagramDiff
	 *            the change.
	 * @return The word.
	 */
	protected String getRemoteText(DiagramDiff diagramDiff) {
		String remotely = ""; //$NON-NLS-1$
		if (diagramDiff.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely "; //$NON-NLS-1$
		}
		return remotely;
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

	/**
	 * It builds the label of the item related to the given difference.
	 * 
	 * @param diagramDiff
	 *            the difference.
	 * @return the label.
	 */
	protected String getValueText(final DiagramDiff diagramDiff) {
		String value = "<null>";
		if (diagramDiff.getView() instanceof View) {
			value = diagramDiff.getView().eClass().getName()
					+ " "
					+ AdapterFactoryUtil.getText(getRootAdapterFactory(), ((View)diagramDiff.getView())
							.getElement());
		}
		value = Strings.elide(value, CHAR_SIZE_LIMIT, "..."); //$NON-NLS-1$
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.ItemProviderAdapter#getForeground(java.lang.Object)
	 */
	@Override
	public Object getForeground(Object object) {
		DiagramDiff referenceChange = (DiagramDiff)object;
		switch (referenceChange.getState()) {
			case MERGED:
			case DISCARDED:
				return URI.createURI("color://rgb/156/156/156"); //$NON-NLS-1$
			default:
				return super.getForeground(object);
		}
	}
}
