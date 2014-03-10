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
package org.eclipse.emf.compare.rcp.ui.internal.preferences.impl;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Interactive content used for {@link IDifferenceGroupProvider} preferences.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class GroupsInteractiveContent {

	/**
	 * Listener to update description text
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private final class DescriptionListener implements ISelectionChangedListener {

		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structSelection = (IStructuredSelection)selection;
				Object selected = structSelection.getFirstElement();
				if (selected instanceof IItemDescriptor<?>) {
					IItemDescriptor<?> desc = (IItemDescriptor<?>)selected;
					String description = desc.getDescription();
					descriptionText.setText(description);
				}
			}

		}
	}

	/** Down icon picture. */
	private static final String ENABLE_DOWN_IMG = "icons/full/pref16/down.gif"; //$NON-NLS-1$

	/** Up icon picture. */
	private static final String ENABLE_UP_IMG = "icons/full/pref16/up.gif"; //$NON-NLS-1$

	/** Text that shall be updated with the description of the viewer. */
	private final Text descriptionText;

	private ArrayList<IItemDescriptor<IDifferenceGroupProvider.Descriptor>> descriptors;

	private Button downButton;

	private Button upButton;

	/** Viewer of {@link IItemDescriptor}. */
	private ListViewer viewer;

	/** Composite holding the viewer. */
	private final Composite viewerCompsite;

	public GroupsInteractiveContent(Composite parent) {
		super();
		Composite containerComposite = new Composite(parent, SWT.NONE);
		containerComposite.setLayout(new GridLayout(1, false));
		containerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite interactiveComposite = new Composite(containerComposite, SWT.BORDER);
		interactiveComposite.setLayout(new GridLayout(2, false));
		interactiveComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Engine chooser composite
		this.viewerCompsite = new Composite(interactiveComposite, SWT.NONE);
		viewerCompsite.setLayout(new GridLayout(1, true));
		viewerCompsite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		// Descriptor engine Text
		createButtonComposite(interactiveComposite);
		this.descriptionText = createDescriptionComposite(interactiveComposite);
	}

	/**
	 * Creates the composite for up and down button.
	 * 
	 * @param parent
	 */
	private void createButtonComposite(Composite parent) {
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridData layoutData = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		buttonComposite.setLayoutData(layoutData);
		buttonComposite.setLayout(new RowLayout(SWT.VERTICAL));
		upButton = new Button(buttonComposite, SWT.NONE);
		upButton.setImage(EMFCompareRCPUIPlugin.getImage(ENABLE_UP_IMG));
		upButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				if (upButton.equals(e.getSource())) {
					IItemDescriptor<Descriptor> selection = getCurrentSelection();
					int index = descriptors.indexOf(selection);
					descriptors.remove(selection);
					int newIndex = index - 1;
					if (newIndex <= descriptors.size()) {
						descriptors.add(newIndex, selection);
						setViewerInput(descriptors);
						select(selection);
					}
				}
			}
		});
		downButton = new Button(buttonComposite, SWT.NONE);
		downButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				if (downButton.equals(e.getSource())) {
					IItemDescriptor<Descriptor> selection = getCurrentSelection();
					int index = descriptors.indexOf(selection);
					descriptors.remove(selection);
					int newIndex = index + 1;
					if (newIndex <= descriptors.size()) {
						descriptors.add(newIndex, selection);
						setViewerInput(descriptors);
						select(selection);
					}
				}
			}
		});

		downButton.setImage(EMFCompareRCPUIPlugin.getImage(ENABLE_DOWN_IMG));

	}

	/**
	 * Composite for description. This composite holds the text widget that will update with the current
	 * selection
	 * 
	 * @param composite
	 * @return
	 */
	private Text createDescriptionComposite(Composite composite) {
		Group descriptionComposite = new Group(composite, SWT.BORDER);
		descriptionComposite.setText(EMFCompareRCPUIMessages
				.getString("InteractiveUIContent.DESCRIPTION_COMPOSITE_LABEL")); //$NON-NLS-1$
		descriptionComposite.setLayout(new GridLayout(1, false));
		descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		Text engineDescriptionText = new Text(descriptionComposite, SWT.WRAP | SWT.MULTI);
		engineDescriptionText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		layoutData.heightHint = 50;
		engineDescriptionText.setLayoutData(layoutData);
		engineDescriptionText.setEditable(false);
		return engineDescriptionText;
	}

	@SuppressWarnings("unchecked")
	private IItemDescriptor<IDifferenceGroupProvider.Descriptor> getCurrentSelection() {
		final ISelection selection = viewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structSelection = (IStructuredSelection)selection;
			final Object first = structSelection.getFirstElement();
			if (first instanceof IItemDescriptor<?>) {
				// secure due to Object structure.
				return (IItemDescriptor<IDifferenceGroupProvider.Descriptor>)first;
			}
		}
		return null;
	}

	/**
	 * @return The ordered list of {@link IItemDescriptor}.
	 */
	public List<IItemDescriptor<IDifferenceGroupProvider.Descriptor>> getItems() {
		return descriptors;
	}

	/**
	 * @return The composite holding the viewer.
	 */
	public Composite getViewerComposite() {
		return viewerCompsite;
	}

	private boolean isFirst(Object o) {
		if (descriptors != null && !descriptors.isEmpty()) {
			return descriptors.get(0).equals(o);
		}
		return false;
	}

	private boolean isLast(Object o) {
		if (descriptors != null && !descriptors.isEmpty()) {
			return descriptors.get(descriptors.size() - 1).equals(o);
		}
		return false;
	}

	/**
	 * Handles a selection in the viewer. Updates related components.
	 * 
	 * @param descriptor
	 */
	public void select(IItemDescriptor<IDifferenceGroupProvider.Descriptor> descriptor) {
		// Update viewer
		viewer.setSelection(new StructuredSelection(descriptor), true);
		updateLinkedElements(descriptor);
	}

	/**
	 * Sets the input for interactive content.
	 * 
	 * @param input
	 */
	public void setViewerInput(List<IItemDescriptor<IDifferenceGroupProvider.Descriptor>> input) {
		if (viewer != null) {
			this.descriptors = Lists.newArrayList(input);
			viewer.setInput(descriptors);
		} else {
			EMFCompareRCPUIPlugin.getDefault().log(IStatus.ERROR,
					"Please initialize the viewer before setting input"); //$NON-NLS-1$
		}
	}

	/**
	 * @param viewer
	 *            A {@link StructuredViewer} of {@link IItemDescriptor}
	 */
	public void setViewer(ListViewer inputViewer) {
		this.viewer = inputViewer;
		viewer.addSelectionChangedListener(new DescriptionListener());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IItemDescriptor<?> selection = getCurrentSelection();
				if (selection == null) {
					upButton.setEnabled(false);
					downButton.setEnabled(false);
				} else {
					upButton.setEnabled(!isFirst(selection));
					downButton.setEnabled(!isLast(selection));
				}
			}

		});
	}

	/**
	 * Updates linked element in
	 * 
	 * @param descriptor
	 */
	private void updateLinkedElements(IItemDescriptor<IDifferenceGroupProvider.Descriptor> descriptor) {
		// Update description
		descriptionText.setText(descriptor.getDescription());
	}
}
