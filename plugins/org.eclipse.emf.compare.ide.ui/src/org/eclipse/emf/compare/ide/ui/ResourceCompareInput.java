/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.internal.CompareMessages;
import org.eclipse.compare.structuremergeviewer.IStructureComparator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * A two-way or three-way compare for arbitrary EMF Resources.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ResourceCompareInput extends CompareEditorInput {

	/**
	 * Does the comparison is three way. Is valued from
	 * {@link #setSelection(Resource, Resource, Resource, Shell)} if the third resource is null.
	 */
	private boolean fThreeWay;

	/**
	 * The ancestor resource, always null if the comparison is two-ways only.
	 */
	private Resource fAncestorResource;

	/**
	 * The left resource, never null after a call to
	 * {@link #setSelection(Resource, Resource, Resource, Shell)}.
	 */
	private Resource fLeftResource;

	/**
	 * The right resource, never null after a call to
	 * {@link #setSelection(Resource, Resource, Resource, Shell)}.
	 */
	private Resource fRightResource;

	/**
	 * The adapted ancestor, always null if comparison is two-ways only.
	 */
	private IStructureComparator fAncestor;

	/**
	 * The adapted left resource, never null after a call to
	 * {@link #setSelection(Resource, Resource, Resource, Shell)}.
	 */
	private IStructureComparator fLeft;

	/**
	 * The adapted right resource, never null after a call to
	 * {@link #setSelection(Resource, Resource, Resource, Shell)}.
	 */
	private IStructureComparator fRight;

	/**
	 * The adapter factory to use to adapt EMF objects.
	 */
	private final AdapterFactory fAdapterFactory;

	/**
	 * The compare configuration to use to configure the comparison.
	 */
	private CompareConfiguration fCompareConfiguration;

	/**
	 * Construct a new compare input for EMF {@link Resource}s.
	 * 
	 * @param configuration
	 *            the configuration required by the super class.
	 * @param adapterFactory
	 *            the {@link AdapterFactory} that will be use to adapt EObject.
	 */
	ResourceCompareInput(CompareConfiguration configuration, AdapterFactory adapterFactory) {
		super(configuration);

		this.fAdapterFactory = adapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.compare.CompareEditorInput#prepareInput(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * A simple message dialog to let the user specify which {@link Resource} is the ancestor.
	 */
	class SelectAncestorDialog extends MessageDialog {
		/**
		 * The chosen ancestor.
		 */
		Resource ancestorResource;

		/**
		 * The chosen left Resource.
		 */
		Resource leftResource;

		/**
		 * The chosen right Resource.
		 */
		Resource rightResource;

		/**
		 * The array of resource to choose from.
		 */
		private Resource[] theResources;

		/**
		 * The array of button corresponding to the array of resources.
		 */
		private Button[] buttons;

		/**
		 * The object listening to the change of the selection in the array of button and triggering the
		 * {@link #pickAncestor(int)} method on event catching.
		 */
		private SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button selectedButton = (Button)e.widget;
				if (!selectedButton.getSelection()) {
					return;
				}
				for (int i = 0; i < 3; i++) {
					if (selectedButton == buttons[i]) {
						pickAncestor(i);
					}
				}
			}
		};

		/**
		 * Constructor.
		 * 
		 * @param parentShell
		 *            the parent shell to be used to display the dialog.
		 * @param theResources
		 *            the array of resource to choose from.
		 */
		public SelectAncestorDialog(Shell parentShell, Resource[] theResources) {
			super(parentShell, CompareMessages.SelectAncestorDialog_title, null,
					CompareMessages.SelectAncestorDialog_message, MessageDialog.QUESTION, new String[] {
							IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL, }, 0);
			this.theResources = theResources;
		}

		@Override
		protected Control createCustomArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout());
			buttons = new Button[3];
			for (int i = 0; i < 3; i++) {
				buttons[i] = new Button(composite, SWT.RADIO);
				buttons[i].addSelectionListener(selectionListener);
				buttons[i].setText(NLS.bind(CompareMessages.SelectAncestorDialog_option, theResources[i]
						.getURI().toPlatformString(true)));
				buttons[i].setFont(parent.getFont());
				// set initial state
				buttons[i].setSelection(i == 0);
			}
			pickAncestor(0);
			return composite;
		}

		/**
		 * Set the resource fields to the proper value regarding the given index. The left and the right
		 * resources are set as followed:
		 * <ul>
		 * <li>If the ancestor is the first element in the resources array, the left is the second and right
		 * is the <third.</li>
		 * <li>If the ancestor is the second element in the resources array, the left is the first and right
		 * is the third.</li>
		 * <li>If the ancestor is the third element in the resources array, the left is the first, and right
		 * is the second.</li>
		 * </ul>
		 * 
		 * @param i
		 *            the index of the ancestor.
		 */
		private void pickAncestor(int i) {
			ancestorResource = theResources[i];
			if (i == 0) {
				leftResource = theResources[1];
			} else {
				leftResource = theResources[0];
			}

			if (i == 2) {
				rightResource = theResources[1];
			} else {
				rightResource = theResources[2];
			}
		}
	}

	/**
	 * If the compare is three-way, this method asks the user which resource to use as the ancestor. Depending
	 * on the value of showSelectAncestorDialog flag it uses different dialogs to get the feedback from the
	 * user. Returns false if the user cancels the prompt, true otherwise.
	 * 
	 * @param first
	 *            the first resource.
	 * @param second
	 *            the second resource.
	 * @param third
	 *            the third resource (can be null if no ancestor).
	 * @param shell
	 *            the shell to use as parent to open a popup.
	 * @return Returns false if the user cancels the prompt, true otherwise.
	 */
	boolean setSelection(Resource first, Resource second, Resource third, Shell shell) {
		fThreeWay = third != null;

		if (fThreeWay) {
			SelectAncestorDialog dialog = new SelectAncestorDialog(shell, new Resource[] {first, second,
					third, });
			int code = dialog.open();
			if (code != Window.OK) {
				return false;
			}

			fAncestorResource = dialog.ancestorResource;
			fAncestor = getStructure(fAncestorResource);
			fLeftResource = dialog.leftResource;
			fRightResource = dialog.rightResource;
		} else {
			fAncestorResource = null;
			fAncestor = null;
			fLeftResource = first;
			fRightResource = second;
		}
		fLeft = getStructure(fLeftResource);
		fRight = getStructure(fRightResource);
		return true;
	}

	/**
	 * Initializes the images in the compare configuration.
	 */
	void initializeCompareConfiguration() {
		CompareConfiguration cc = getCompareConfiguration();
		if (fLeftResource != null) {
			IItemLabelProvider itemLabelProvider = (IItemLabelProvider)fAdapterFactory.adapt(fLeftResource,
					IItemLabelProvider.class);
			cc.setLeftLabel(buildLabel(fLeftResource, itemLabelProvider));
			cc.setLeftImage(buildImage(fLeftResource, itemLabelProvider));
		}
		if (fRightResource != null) {
			IItemLabelProvider itemLabelProvider = (IItemLabelProvider)fAdapterFactory.adapt(fRightResource,
					IItemLabelProvider.class);
			cc.setRightLabel(buildLabel(fRightResource, itemLabelProvider));
			cc.setRightImage(buildImage(fRightResource, itemLabelProvider));
		}
		if (fThreeWay && fAncestorResource != null) {
			IItemLabelProvider itemLabelProvider = (IItemLabelProvider)fAdapterFactory.adapt(
					fAncestorResource, IItemLabelProvider.class);
			cc.setAncestorLabel(buildLabel(fAncestorResource, itemLabelProvider));
			cc.setAncestorImage(buildImage(fAncestorResource, itemLabelProvider));
		}
	}

	/**
	 * Returns the proper label of the given {@link Resource}.
	 * 
	 * @param resource
	 *            the resource.
	 * @param itemLabelProvider
	 *            the IItemLabelProvider used to retrieve the label from.
	 * @return the label of the given resource, an empty string if null and the default {@link #toString()}
	 *         value in all other cases.
	 */
	private String buildLabel(Resource resource, IItemLabelProvider itemLabelProvider) {
		String ret = null;
		if (itemLabelProvider != null) {
			ret = itemLabelProvider.getText(fLeftResource);
		} else if (fLeftResource == null) {
			ret = ""; //$NON-NLS-1$
		} else {
			ret = fLeftResource.toString();
		}
		return ret;
	}

	/**
	 * Returns the proper image of the given {@link Resource}.
	 * 
	 * @param resource
	 *            the resource.
	 * @param itemLabelProvider
	 *            the IItemLabelProvider used to retrieve the image from.
	 * @return the label of the given resource, an default image if null and the default {@link #toString()}
	 *         value in all other cases.
	 */
	private Image buildImage(Resource resource, IItemLabelProvider itemLabelProvider) {
		Image ret = null;

		if (itemLabelProvider != null) {
			ret = ExtendedImageRegistry.getInstance().getImage(itemLabelProvider.getImage(fLeftResource));
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
	private IStructureComparator getStructure(Resource input) {
		IItemLabelProvider adapt = (IItemLabelProvider)fAdapterFactory.adapt(input, IItemLabelProvider.class);
		if (input instanceof IStructureComparator) {
			return (IStructureComparator)adapt;
		}

		return null;
	}

}
