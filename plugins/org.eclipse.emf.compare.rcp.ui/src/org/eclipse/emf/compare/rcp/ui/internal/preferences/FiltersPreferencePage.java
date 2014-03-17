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
package org.eclipse.emf.compare.rcp.ui.internal.preferences;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.filters.impl.DifferenceFilterManager;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.filters.IDifferenceFilter;
import org.eclipse.jface.databinding.viewers.IViewerObservableSet;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Preference page for {@link IDifferenceFilter}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class FiltersPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** Filter manager. Used to retrieve current and default configuration. */
	private DifferenceFilterManager filterManager;

	/** Interactive content holding UI components. */
	private InteractiveFilterUIContent filterInteractiveContent;

	public void init(IWorkbench workbench) {
		// Do not use InstanceScope.Instance to be compatible with Helios.
		@SuppressWarnings("deprecation")
		ScopedPreferenceStore store = new ScopedPreferenceStore(new InstanceScope(),
				EMFCompareRCPUIPlugin.PLUGIN_ID);
		setPreferenceStore(store);

	}

	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(1, true));
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout(1, true));

		// Description text
		Label introductionText = new Label(container, SWT.WRAP);
		introductionText.setText(EMFCompareRCPUIMessages.getString("FiltersPreferencePage.INTRO_TEXT")); //$NON-NLS-1$
		filterManager = EMFCompareRCPUIPlugin.getDefault().getDifferenceFilterManager();

		Collection<IDifferenceFilter> allFilters = filterManager.getAllFilters();
		final IDifferenceFilter defaultSelection;
		if (!allFilters.isEmpty()) {
			defaultSelection = allFilters.iterator().next();
		} else {
			defaultSelection = null;
		}

		filterInteractiveContent = new InteractiveFilterUIContent(container, filterManager.getAllFilters(),
				defaultSelection, filterManager.getCurrentByDefaultFilters());
		return container;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performOk() {
		filterManager.setCurrentByDefaultFilters(filterInteractiveContent.getCheckedFilter());
		return super.performOk();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void performDefaults() {
		filterInteractiveContent.checkElements(filterManager.getInitialByDefaultFilters());
		super.performDefaults();
	}

	/**
	 * Interactive UI for filter preference page.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	public static class InteractiveFilterUIContent {

		/** Text that will be updated with the description of the viewer. */
		private final Text descriptionText;

		/** Viewer of {@link IDifferenceFilter}. */
		private CheckboxTableViewer viewer;

		/** DataHolder for {@link IDifferenceFilter}. */
		private FilterDataHolder dataHolder = new FilterDataHolder();

		private InteractiveFilterUIContent(Composite parent, Collection<IDifferenceFilter> filters,
				IDifferenceFilter defaultSelection, Collection<IDifferenceFilter> defaultCheck) {
			super();
			Composite contentComposite = new Composite(parent, SWT.NONE);
			contentComposite.setLayout(new GridLayout(1, true));
			contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			// Engine chooser composite
			Composite viewerComposite = new Composite(contentComposite, SWT.NONE);
			viewerComposite.setLayout(new GridLayout(1, true));
			viewerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			viewer = createViewer(viewerComposite);
			// Descriptor engine Text
			this.descriptionText = createDescriptionComposite(parent);
			setViewerInput(Lists.newArrayList(filters));
			bindAndInit(defaultSelection, Sets.newLinkedHashSet(defaultCheck));
		}

		/**
		 * @return All checked {@link IDifferenceFilter}.
		 */
		public Set<IDifferenceFilter> getCheckedFilter() {
			return dataHolder.getFilters();
		}

		private void setViewerInput(List<IDifferenceFilter> filters) {
			Collections.sort(filters, new Comparator<IDifferenceFilter>() {

				public int compare(IDifferenceFilter o1, IDifferenceFilter o2) {
					if (o1 == o2) {
						return 0;
					} else if (o1 == null || o1.getLabel() == null) {
						return -1;
					} else if (o2 == null || o2.getLabel() == null) {
						return 1;
					}
					return o1.getLabel().compareTo(o2.getLabel());
				}
			});
			viewer.setInput(filters);
		}

		private CheckboxTableViewer createViewer(Composite viewerCompsite) {
			CheckboxTableViewer descriptorViewer = CheckboxTableViewer.newCheckList(viewerCompsite,
					SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
			descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
			descriptorViewer.setLabelProvider(new FilterLabelProvider());
			GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			descriptorViewer.getControl().setLayoutData(gd);
			setViewer(descriptorViewer);
			return descriptorViewer;
		}

		private void bindAndInit(IDifferenceFilter defaultSelection, Set<IDifferenceFilter> defaultCheck) {
			if (defaultSelection != null) {
				select(defaultSelection);
			}
			if (dataHolder != null) {
				if (defaultCheck != null) {
					dataHolder.setFilters(defaultCheck);
				}
				// Bind data
				bindMultipleData(viewer, dataHolder);
			}
		}

		/**
		 * Bind UI to data object.
		 * 
		 * @param engineBindingProperty
		 * @param descriptorViewer
		 * @param dataObject
		 */
		private void bindMultipleData(CheckboxTableViewer descriptorViewer, FilterDataHolder dataObject) {
			DataBindingContext ctx = new DataBindingContext();
			// Bind the button with the corresponding field in data
			IViewerObservableSet target = ViewersObservables.observeCheckedElements(descriptorViewer,
					IDifferenceFilter.class);
			IObservableSet model = PojoProperties.set(FilterDataHolder.class, FilterDataHolder.FIELD_NAME)
					.observe(dataObject);

			ctx.bindSet(target, model);
		}

		/**
		 * Check element in the viewer.
		 * 
		 * @param checkedFilter
		 *            Element to check.
		 */
		public void checkElements(Set<IDifferenceFilter> checkedFilter) {
			viewer.setCheckedElements(checkedFilter.toArray());
			dataHolder.setFilters(checkedFilter);
		}

		/**
		 * Composite for description. This composite hold the text widget that will update with the current
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
			descriptionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			Text engineDescriptionText = new Text(descriptionComposite, SWT.WRAP | SWT.MULTI);
			engineDescriptionText.setBackground(Display.getCurrent().getSystemColor(
					SWT.COLOR_WIDGET_BACKGROUND));
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
			layoutData.heightHint = 50;
			engineDescriptionText.setLayoutData(layoutData);
			engineDescriptionText.setEditable(false);
			return engineDescriptionText;
		}

		/**
		 * Handle a selection in the viewer. Update related components.
		 * 
		 * @param descriptor
		 */
		public void select(IDifferenceFilter descriptor) {
			// Update viewer
			viewer.setSelection(new StructuredSelection(descriptor), true);
			updateLinkedElements(descriptor);
		}

		/**
		 * @param viewer
		 *            A {@link StructuredViewer} of {@link IItemDescriptor}
		 */
		public void setViewer(CheckboxTableViewer inputViewer) {
			this.viewer = inputViewer;
			viewer.addSelectionChangedListener(new DescriptionListener());
		}

		/**
		 * Update linked element in
		 * 
		 * @param descriptor
		 */
		private void updateLinkedElements(IDifferenceFilter descriptor) {
			descriptionText.setText(descriptor.getDescription());
		}

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
					if (selected instanceof IDifferenceFilter) {
						IDifferenceFilter desc = (IDifferenceFilter)selected;
						String description = desc.getDescription();
						if (description != null) {
							descriptionText.setText(description);
						} else {
							descriptionText.setText(""); //$NON-NLS-1$
						}
					}
				}

			}
		}

		/**
		 * Label provider for {@link IDifferenceFilter}.
		 * 
		 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
		 */
		private static final class FilterLabelProvider extends LabelProvider {
			/**
			 * {@inheritDoc}
			 */
			@Override
			public String getText(Object element) {
				if (element instanceof IDifferenceFilter) {
					return ((IDifferenceFilter)element).getLabel();
				}
				return super.getText(element);
			}
		}

		/**
		 * Data holder for checked {@link IDifferenceFilter}.
		 * 
		 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
		 */
		private static final class FilterDataHolder {

			private static String FIELD_NAME = "filters"; //$NON-NLS-1$

			private Set<IDifferenceFilter> filters;

			public Set<IDifferenceFilter> getFilters() {
				return filters;
			}

			public void setFilters(Set<IDifferenceFilter> filters) {
				this.filters = filters;
			}

		}
	}

}
