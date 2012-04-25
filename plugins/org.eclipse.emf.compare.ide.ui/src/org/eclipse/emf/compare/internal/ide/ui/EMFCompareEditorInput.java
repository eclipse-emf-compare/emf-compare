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
package org.eclipse.emf.compare.internal.ide.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * A two-way or three-way compare for arbitrary EMF Resources.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareEditorInput extends CompareEditorInput {

	/**
	 * The adapter factory to use to adapt EMF objects.
	 */
	private final AdapterFactory fAdapterFactory;

	/**
	 * Does the comparison is three way. True if the ancestor resource is null.
	 */
	private final boolean fThreeWay;

	/**
	 * The adapted ancestor, always null if comparison is two-ways only.
	 */
	private final IStructureComparator fAncestor;

	/**
	 * The adapted left resource, never null after a call to
	 * {@link #setSelection(Resource, Resource, Resource, Shell)}.
	 */
	private final IStructureComparator fLeft;

	/**
	 * The adapted right resource, never null after a call to
	 * {@link #setSelection(Resource, Resource, Resource, Shell)}.
	 */
	private final IStructureComparator fRight;

	/**
	 * The left notifier.
	 */
	private final Notifier fLeftNotifier;

	/**
	 * The right notifier.
	 */
	private final Notifier fRightNotifier;

	/**
	 * The ancestor notifier, always null if the comparison is two-ways only.
	 */
	private final Notifier fAncestorNotifier;

	public EMFCompareEditorInput(CompareConfiguration configuration, AdapterFactory adapterFactory,
			Notifier leftNotifier, Notifier rightNotifier) {
		this(configuration, adapterFactory, leftNotifier, rightNotifier, null);
	}

	/**
	 * Construct a new compare input for EMF {@link Resource}s.
	 * 
	 * @param configuration
	 *            the configuration required by the super class.
	 * @param adapterFactory
	 *            the {@link AdapterFactory} that will be use to adapt EObject.
	 */
	public EMFCompareEditorInput(CompareConfiguration configuration, AdapterFactory adapterFactory,
			Notifier leftNotifier, Notifier rightNotifier, Notifier ancestorNotifier) {
		super(configuration);
		checkNotNull(leftNotifier);
		checkNotNull(rightNotifier);
		checkNotNull(adapterFactory);

		this.fAdapterFactory = adapterFactory;
		this.fThreeWay = (ancestorNotifier != null);

		this.fLeftNotifier = leftNotifier;
		this.fLeft = getStructure(this.fLeftNotifier);

		this.fRightNotifier = rightNotifier;
		this.fRight = getStructure(this.fRightNotifier);

		if (fThreeWay) {
			this.fAncestorNotifier = ancestorNotifier;
			this.fAncestor = getStructure(this.fAncestorNotifier);
		} else {
			this.fAncestorNotifier = null;
			this.fAncestor = null;
		}

		initializeCompareConfiguration();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#prepareInput(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		return EMFCompare.compare(fLeftNotifier, fRightNotifier, fAncestorNotifier);
	}

	/**
	 * Initializes the images in the compare configuration.
	 */
	private void initializeCompareConfiguration() {
		CompareConfiguration cc = getCompareConfiguration();
		IItemLabelProvider itemLabelProvider = null;

		itemLabelProvider = (IItemLabelProvider)fAdapterFactory
				.adapt(fLeftNotifier, IItemLabelProvider.class);
		cc.setLeftLabel(buildLabel(fLeftNotifier, itemLabelProvider));
		cc.setLeftImage(buildImage(fLeftNotifier, itemLabelProvider));

		itemLabelProvider = (IItemLabelProvider)fAdapterFactory.adapt(fRightNotifier,
				IItemLabelProvider.class);
		cc.setRightLabel(buildLabel(fRightNotifier, itemLabelProvider));
		cc.setRightImage(buildImage(fRightNotifier, itemLabelProvider));

		if (fThreeWay) {
			itemLabelProvider = (IItemLabelProvider)fAdapterFactory.adapt(fAncestorNotifier,
					IItemLabelProvider.class);
			cc.setAncestorLabel(buildLabel(fAncestorNotifier, itemLabelProvider));
			cc.setAncestorImage(buildImage(fAncestorNotifier, itemLabelProvider));
		}
	}

	/**
	 * Returns the proper label of the given {@link Notifier}.
	 * 
	 * @param notifier
	 *            the notifier.
	 * @param itemLabelProvider
	 *            the IItemLabelProvider used to retrieve the label from.
	 * @return the label of the given notifier, an empty string if null and the default {@link #toString()}
	 *         value in all other cases.
	 */
	private static String buildLabel(Notifier notifier, IItemLabelProvider itemLabelProvider) {
		String ret = null;
		if (itemLabelProvider != null) {
			ret = itemLabelProvider.getText(notifier);
		} else if (notifier == null) {
			ret = ""; //$NON-NLS-1$
		} else {
			ret = notifier.toString();
		}
		return ret;
	}

	/**
	 * Returns the proper image of the given {@link Notifier}.
	 * 
	 * @param notifier
	 *            the Notifier.
	 * @param itemLabelProvider
	 *            the IItemLabelProvider used to retrieve the image from.
	 * @return the label of the given Notifier, an default image if null and the default {@link #toString()}
	 *         value in all other cases.
	 */
	private static Image buildImage(Notifier notifier, IItemLabelProvider itemLabelProvider) {
		Image ret = null;

		if (itemLabelProvider != null) {
			ret = ExtendedImageRegistry.getInstance().getImage(itemLabelProvider.getImage(notifier));
		}

		return ret;
	}

	/**
	 * Creates a <code>IStructureComparator</code> for the given input. Returns <code>null</code> if no
	 * <code>IStructureComparator</code> can be found for the <code>IResource</code>.
	 * 
	 * @param input
	 *            the resource to return the {@link IStructureComparator} from.
	 * @return the proper {@link IStructureComparator}.
	 */
	private IStructureComparator getStructure(Notifier input) {
		IItemLabelProvider adapt = (IItemLabelProvider)fAdapterFactory.adapt(input, IItemLabelProvider.class);
		if (input instanceof IStructureComparator) {
			return (IStructureComparator)adapt;
		}

		return null;
	}

}
