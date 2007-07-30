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

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ModelInputSnapshot;
import org.eclipse.emf.compare.ui.AbstractCompareAction;
import org.eclipse.emf.compare.ui.contentmergeviewer.ModelContentMergeViewer;
import org.eclipse.emf.compare.ui.wizard.SaveDeltaWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

/**
 * This implementation of {@link AbstractCompareAction} is used to create the structure viewer's "export as" action.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class ExportMenu extends AbstractCompareAction implements IMenuCreator {
	/** Name of the extension point to parse for actions. */
	private static final String EXPORT_ACTIONS_EXTENSION_POINT = "org.eclipse.emf.compare.ui.export"; //$NON-NLS-1$

	/** Bundle where the messages and icons can be found. */
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(ModelContentMergeViewer.BUNDLE_NAME);

	/** Keeps track of the {@link ActionContributionItem actions} displayed by the menu. */
	private static final ArrayList<ExportActionDescriptor> CACHED_ACTIONS = new ArrayList<ExportActionDescriptor>();
	
	/** Control under which the menu must be created. */
	protected final Control parentControl;
	
	/** Viewer currently displaying this menu. */
	protected final ModelStructureMergeViewer parentViewer;
	
	/** Default action of the displayed menu. */
	private Action saveAction;

	/** Menu displayed for this action. */
	private Menu menu;

	static {
		parseExtensionMetaData();
	}

	/**
	 * This default constructor will instantiate an action given the {@link #BUNDLE bundle} resources prefixed by &quot;action.save&quot;.
	 * 
	 * @param parent
	 *            {@link Control} under which this {@link Action}'s menu must be created.
	 * @param owner
	 *            The viewer currently displaying this menu.
	 */
	public ExportMenu(Control parent, ModelStructureMergeViewer owner) {
		super(BUNDLE, "action.export.menu."); //$NON-NLS-1$
		setMenuCreator(this);
		parentControl = parent;
		parentViewer = owner;
		
		initializeMenu();
	}
	
	/**
	 * Creates the menu and adds the save ("export as emfdiff") to it.
	 */
	private void initializeMenu() {
		final ResourceBundle bundle = ResourceBundle.getBundle(ModelContentMergeViewer.BUNDLE_NAME);
		saveAction = new AbstractCompareAction(bundle, "action.export.emfdiff.") { //$NON-NLS-1$
			@Override
			public void run() {
				final SaveDeltaWizard wizard = new SaveDeltaWizard(bundle.getString("UI_SaveDeltaWizard_FileExtension")); //$NON-NLS-1$
				wizard.init(PlatformUI.getWorkbench(), (ModelInputSnapshot)parentViewer.getInput());
				final WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
				dialog.open();
			}
		};
		final ActionContributionItem contribution = new ActionContributionItem(saveAction);
		contribution.fill(getMenu(parentControl), 0);
	}

	/**
	 * This will add the given action to this action's menu.
	 * 
	 * @param action
	 *            {@link Action} to add to this action's menu.
	 */
	public void addActionToMenu(Action action) {
		final ActionContributionItem contribution = new ActionContributionItem(action);
		contribution.fill(getMenu(parentControl), -1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
	 */
	public Menu getMenu(Control parent) {
		if (menu == null) {
			menu = new Menu(parent);
			for (ExportActionDescriptor descriptor : CACHED_ACTIONS)
				addActionToMenu(descriptor.getActionInstance());
		}
		return menu;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
	 */
	public Menu getMenu(Menu parent) {
		if (menu != null)
			return menu;
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.action.IMenuCreator#dispose()
	 */
	public void dispose() {
		if (menu != null)
			menu.dispose();
	}
	
	/**
	 * This will switch the enable state of {@link #saveAction}.
	 * 
	 * @param enabled
	 *            <code>True</code> if the action must be enabled, <code>False</code> otherwise
	 */
	public void enableSave(boolean enabled) {
		saveAction.setEnabled(enabled);
	}

	/**
	 * If this action is triggered, it'll run the action with id {@link EMFCompareConstants#ACTION_ID_SAVE} if it is contained within its menu.
	 * 
	 * @see org.eclipse.emf.compare.ui.AbstractCompareAction#run()
	 */
	@Override
	public void run() {
		saveAction.run();
	}

	/**
	 * This will parse {@link #EXPORT_ACTIONS_EXTENSION_POINT} for actions to display.
	 */
	private static void parseExtensionMetaData() {
		final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(EXPORT_ACTIONS_EXTENSION_POINT).getExtensions();
		for (IExtension extension : extensions) {
			for (IConfigurationElement configElement : extension.getConfigurationElements()) {
				final ExportActionDescriptor descriptor = new ExportActionDescriptor(configElement);
				CACHED_ACTIONS.add(descriptor);
			}
		}
	}
}

/**
 * Describes an action registered from a plug-in's extension point for the "export diff as..." menu.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
final class ExportActionDescriptor {
	/** Name of the extension point attribute corresponding to the action's class. */
	private static final String EXPORT_ACTION_CLASS = "class"; //$NON-NLS-1$

	/** class of the action. */
	private final String actionClass;

	/** Keeps a reference to the configuration element that describes the {@link Action}. */
	private final IConfigurationElement element;

	/** This descriptor's wrapped {@link Action}. */
	private Action action;

	/**
	 * Constructs a new descriptor from an IConfigurationElement.
	 * 
	 * @param configuration
	 *            Configuration of the {@link Action}.
	 */
	public ExportActionDescriptor(IConfigurationElement configuration) {
		element = configuration;
		actionClass = element.getAttribute(EXPORT_ACTION_CLASS);
	}

	/**
	 * Returns the wrapped action's class name.
	 * 
	 * @return The wrapped action's class name.
	 */
	public String getActionClass() {
		return actionClass;
	}

	/**
	 * Returns an instance of the described {@link Action}.
	 * 
	 * @return Instance of the described {@link Action}.
	 */
	public Action getActionInstance() {
		if (action == null) {
			try {
				action = (Action)element.createExecutableExtension(EXPORT_ACTION_CLASS);
			} catch (CoreException e) {
				EMFComparePlugin.log(e, true);
			}
		}
		return action;
	}
}
