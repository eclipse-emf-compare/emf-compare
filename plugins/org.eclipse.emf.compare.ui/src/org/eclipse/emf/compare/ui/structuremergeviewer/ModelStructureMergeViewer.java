/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.structuremergeviewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.ui.Messages;
import org.eclipse.emf.compare.ui.contentprovider.ModelStructureContentProvider;
import org.eclipse.emf.compare.ui.export.ExportMenu;
import org.eclipse.emf.compare.ui.util.EMFAdapterFactoryProvider;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Compare and merge viewer with an area showing diffs as a structured tree.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ModelStructureMergeViewer extends TreeViewer {
	/** Configuration element of the underlying comparison. */
	protected CompareConfiguration configuration;

	/** This is the action displaying the "export diff as..." menu. */
	protected ExportMenu exportMenu;

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
	 * Returns the compare configuration of this viewer, or <code>null</code> if this viewer does not yet
	 * have a configuration.
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
		return Messages.getString("ModelStructureMergeViewer.viewerTitle"); //$NON-NLS-1$
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
		setLabelProvider(new ModelStructureLabelProvider());
		setUseHashlookup(true);
		setContentProvider(new ModelStructureContentProvider(compareConfiguration));

		final Control tree = getControl();
		tree.setData(CompareUI.COMPARE_VIEWER_TITLE, getTitle());
		addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				configuration.setProperty(EMFCompareConstants.PROPERTY_STRUCTURE_SELECTION, event
						.getSelection());
			}
		});

		configuration.addPropertyChangeListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(EMFCompareConstants.PROPERTY_CONTENT_SELECTION)) {
					expandAll();
					final TreeItem item = (TreeItem)find(event.getNewValue());
					collapseAll();
					if (item != null) {
						setSelection(new StructuredSelection(item.getData()), true);
						expandToLevel(item.getData(), 0);
					}
				} else if (event.getProperty().equals(EMFCompareConstants.PROPERTY_CONTENT_INPUT_CHANGED)) {
					setInput(event.getNewValue());
				}
			}
		});
	}

	/**
	 * This will initialize the "save as emfdiff" action and put its icon in the {@link CompareViewerPane}
	 * toolbar.
	 */
	protected void createToolItems() {
		final ToolBarManager tbm = CompareViewerPane.getToolBarManager(getControl().getParent());
		if (exportMenu == null)
			exportMenu = new ExportMenu(tbm.getControl(), this);
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
		super.inputChanged(input, oldInput);
		if (!(input instanceof ModelInputSnapshot) && input != oldInput) {
			setInput(((ModelStructureContentProvider)getContentProvider()).getSnapshot());
			configuration.setProperty(EMFCompareConstants.PROPERTY_COMPARISON_RESULT,
					((ModelStructureContentProvider)getContentProvider()).getSnapshot());
		}
		updateToolItems();
	}

	/**
	 * Updates the Structure viewer's tool items. This will modify the actions of the "export diff as..."
	 * menu.
	 */
	protected void updateToolItems() {
		Boolean enableSave = (Boolean)configuration.getProperty(EMFCompareConstants.PROPERTY_LEFT_IS_REMOTE);
		if (enableSave == null)
			enableSave = false;

		exportMenu.enableSave(!enableSave);
		CompareViewerPane.getToolBarManager(getControl().getParent()).update(true);
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
	 * {@link LabelProvider} of this viewer.
	 */
	private class ModelStructureLabelProvider extends LabelProvider {
		/**
		 * We uses this generic label provider, but we want to customize some aspects that's why we choose to
		 * aggregate it.
		 */
		/*package*/AdapterFactoryLabelProvider adapterProvider;

		/**
		 * Default constructor.
		 */
		public ModelStructureLabelProvider() {
			adapterProvider = new AdapterFactoryLabelProvider(EMFAdapterFactoryProvider.getAdapterFactory());

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
					if (image == null && object instanceof EObject) {
						if (EMFAdapterFactoryProvider.addAdapterFactoryFor((EObject)object)) {
							adapterProvider.setAdapterFactory(EMFAdapterFactoryProvider.getAdapterFactory());
							image = adapterProvider.getImage(object);
						}
					}
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
				} else {
					text = adapterProvider.getText(object);
					if (text == null && object instanceof EObject) {
						if (EMFAdapterFactoryProvider.addAdapterFactoryFor((EObject)object)) {
							adapterProvider.setAdapterFactory(EMFAdapterFactoryProvider.getAdapterFactory());
							text = adapterProvider.getText(object);
						}
					}
				}
			}
			return text;
		}
	}
}
