/*******************************************************************************
 * Copyright (c) 2006, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.viewer.structure;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.export.ExportMenu;
import org.eclipse.emf.compare.ui.internal.ModelComparator;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.util.AdapterUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Compare and merge viewer with an area showing diffs as a structured tree.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ModelStructureMergeViewer extends TreeViewer {
	/** Configuration element of the underlying comparison. */
	protected CompareConfiguration configuration;

	/** This is the action displaying the "export diff as..." menu. */
	protected ExportMenu exportMenu;

	/**
	 * Allows us to ignore a selection event in the content viewer if it is one caused by a selection event in
	 * the structure viewer.
	 */
	/* package */boolean ignoreContentSelection;

	/**
	 * Creates a new model structure merge viewer and intializes it.
	 * 
	 * @param parent
	 *            Parent composite for this viewer.
	 * @param compareConfiguration
	 *            The configuration object.
	 */
	public ModelStructureMergeViewer(Composite parent, CompareConfiguration compareConfiguration) {
		super(parent);
		initialize(compareConfiguration);
		createToolItems();
	}

	/**
	 * Returns the compare configuration of this viewer, or <code>null</code> if this viewer does not yet have
	 * a configuration.
	 * 
	 * @return the compare configuration, or <code>null</code> if none
	 */
	public CompareConfiguration getCompareConfiguration() {
		return configuration;
	}

	/**
	 * Returns the viewer's title.
	 * 
	 * @return The viewer's title.
	 * @see CompareUI#COMPARE_VIEWER_TITLE
	 */
	public String getTitle() {
		return EMFCompareUIMessages.getString("ModelStructureMergeViewer.viewerTitle"); //$NON-NLS-1$
	}

	/**
	 * Returns the widget representing the given element.
	 * 
	 * @param element
	 *            Element we seek the {@link TreeItem} for.
	 * @return The widget representing the given element.
	 */
	/* package */Widget find(Object element) {
		final Widget widget = super.findItem(element);
		return widget;
	}

	/**
	 * Creates this viewer's content provider.
	 * 
	 * @param compareConfiguration
	 *            Compare configuration that's been fed this viewer.
	 * @return This viewer's content provider.
	 * @since 1.1
	 */
	protected ModelStructureContentProvider createContentProvider(CompareConfiguration compareConfiguration) {
		return new ModelStructureContentProvider(compareConfiguration);
	}

	/**
	 * Creates this viewer's label provider.
	 * 
	 * @param compareConfiguration
	 *            Compare configuration that's been fed this viewer.
	 * @return This viewer's label provider.
	 * @since 1.1
	 */
	protected ModelStructureLabelProvider createLabelProvider(
			@SuppressWarnings("unused") CompareConfiguration compareConfiguration) {
		return new ModelStructureLabelProvider();
	}

	/**
	 * This will initialize the "save as emfdiff" action and put its icon in the {@link CompareViewerPane}
	 * toolbar.
	 */
	protected void createToolItems() {
		final ToolBarManager tbm = CompareViewerPane.getToolBarManager(getControl().getParent());
		if (exportMenu == null) {
			exportMenu = new ExportMenu(tbm.getControl(), this);
		}
		tbm.add(new Separator("IO")); //$NON-NLS-1$
		tbm.appendToGroup("IO", exportMenu); //$NON-NLS-1$
		tbm.update(true);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#fireOpen(org.eclipse.jface.viewers.OpenEvent)
	 */
	@Override
	protected void fireOpen(OpenEvent event) {
		// DIRTY
		// cancels open events that could be fired to avoid "NullViewer"
		// opening.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#fireSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	protected void fireSelectionChanged(SelectionChangedEvent event) {
		// DIRTY
		// cancels selection changed events that could be fired to avoid
		// "NullViewer" opening.
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.ContentViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
	 */
	@Override
	protected void handleDispose(DisposeEvent event) {
		super.handleDispose(event);
		exportMenu.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.AbstractTreeViewer#inputChanged(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		final TreePath[] expandedPaths = getExpandedTreePaths();

		super.inputChanged(input, oldInput);
		if (input != null) {
			if (!(input instanceof ComparisonSnapshot) && input != oldInput) {
				final ComparisonSnapshot snapshot = ModelComparator.getComparator(configuration)
						.getComparisonResult();
				Object match = null;
				// check whether a resource or resource set comparison was performed
				if (snapshot instanceof ComparisonResourceSnapshot) {
					match = ((ComparisonResourceSnapshot)snapshot).getMatch();
				} else {
					match = ((ComparisonResourceSetSnapshot)snapshot).getMatchResourceSet();
				}
				if (match != null) {
					setInput(snapshot);
				} else {
					setInput(null);
				}
			}
			updateToolItems();

			setExpandedTreePaths(expandedPaths);
		} else {
			hideStructurePane();
		}
	}

	/**
	 * Updates the Structure viewer's tool items. This will modify the actions of the "export diff as..."
	 * menu.
	 */
	protected void updateToolItems() {
		final ModelComparator comparator = ModelComparator.getComparator(configuration);
		if (comparator != null) {
			exportMenu.enableSave(!comparator.isLeftRemote() && !comparator.isRightRemote());
		} else {
			exportMenu.enableSave(false);
		}
		CompareViewerPane.getToolBarManager(getControl().getParent()).update(true);
	}

	/**
	 * This will be called when the input of this viewer is set to <code>null</code> and hide the whole
	 * viewer.
	 */
	private void hideStructurePane() {
		getControl().getParent().getParent().setVisible(false);
	}

	/**
	 * initializer of this structure merge viewer. It sets the {@link LabelProvider label} and content
	 * provider for the tree and creates the different needed listeners.
	 * 
	 * @param compareConfiguration
	 *            Configuration of the underlying comparison.
	 */
	private void initialize(CompareConfiguration compareConfiguration) {
		configuration = compareConfiguration;
		setLabelProvider(createLabelProvider(compareConfiguration));
		setUseHashlookup(true);
		setContentProvider(createContentProvider(compareConfiguration));

		final Tree tree = getTree();
		tree.setData(CompareUI.COMPARE_VIEWER_TITLE, getTitle());
		tree.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing to do here
			}

			public void widgetSelected(SelectionEvent e) {
				final List<DiffElement> selectedElements = new ArrayList<DiffElement>(getTree()
						.getSelection().length);
				for (final TreeItem item : getTree().getSelection())
					if (item.getData() instanceof DiffElement) {
						selectedElements.add((DiffElement)item.getData());
					}
				ignoreContentSelection = true;
				configuration.setProperty(EMFCompareConstants.PROPERTY_STRUCTURE_SELECTION, selectedElements);
			}
		});

		configuration.addPropertyChangeListener(new ConfigurationPropertyListener());
		final IWorkbenchPart part = configuration.getContainer().getWorkbenchPart();
		if (part != null) {
			part.getSite().setSelectionProvider(this);
		}
	}

	/**
	 * Listens to property change events on the compare configuration. Typically responds to selection change
	 * events in the content viewer and preferences change.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class ConfigurationPropertyListener implements IPropertyChangeListener {
		/**
		 * Default constructor. Implemented to increase its visibility.
		 */
		public ConfigurationPropertyListener() {
			// no action is to be taken here
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty().equals(EMFCompareConstants.PROPERTY_CONTENT_SELECTION)) {
				if (ignoreContentSelection) {
					ignoreContentSelection = false;
				} else {
					TreeItem item = (TreeItem)find(event.getNewValue());
					/*
					 * if we could not find the item, we need to expand the whole tree to try and get it. This
					 * is due to the lazy loading of the tree content.
					 */
					if (item == null) {
						final Object[] expandedElements = getExpandedElements();
						expandAll();
						item = (TreeItem)find(event.getNewValue());
						setExpandedElements(expandedElements);
					}
					if (item != null) {
						setSelection(new StructuredSelection(item.getData()), true);
						expandToLevel(item.getData(), 0);
					}
				}
			} else if (event.getProperty().equals(EMFCompareConstants.PROPERTY_CONTENT_INPUT_CHANGED)) {
				setInput(event.getNewValue());
			}
		}
	}

	/**
	 * {@link LabelProvider} of this viewer.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private class ModelStructureLabelProvider extends LabelProvider {
		/**
		 * We use this generic label provider, but we want to customize some aspects that's why we choose to
		 * aggregate it.
		 */
		/* package */AdapterFactoryLabelProvider adapterProvider;

		/**
		 * Default constructor.
		 */
		public ModelStructureLabelProvider() {
			adapterProvider = new AdapterFactoryLabelProvider(AdapterUtils.getAdapterFactory());

		}

		/**
		 * Returns the platform icon for a given {@link IFile}. If not an {@link IFile}, delegates to the
		 * {@link AdapterFactoryLabelProvider} to get the {@link Image}.
		 * 
		 * @param object
		 *            Object to get the {@link Image} for.
		 * @return The platform icon for the given object.
		 * @see AdapterFactoryLabelProvider#getImage(Object)
		 */
		@Override
		public Image getImage(Object object) {
			Image image = null;
			if (object instanceof AbstractDiffExtension) {
				image = (Image)((AbstractDiffExtension)object).getImage();
			}
			if (object instanceof IFile) {
				image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			} else {
				if (image == null) {
					image = adapterProvider.getImage(object);
				}
			}
			return image;
		}

		/**
		 * Returns the name of the given {@link IFile}, delegates to
		 * {@link AdapterFactoryLabelProvider#getText(Object)} if not an {@link IFile}.
		 * 
		 * @param object
		 *            Object we seek the name for.
		 * @return The name of the given object.
		 * @see AdapterFactoryLabelProvider#getText(Object)
		 */
		@Override
		public String getText(Object object) {
			String text = null;
			if (object instanceof AbstractDiffExtension) {
				text = ((AbstractDiffExtension)object).getText();
			} else {
				if (object instanceof IFile) {
					text = ((IFile)object).getName();
				} else if (object instanceof Resource) {
					text = ((Resource)object).getURI().lastSegment();
				} else {
					text = adapterProvider.getText(object);
				}
			}
			return text;
		}
	}
}
