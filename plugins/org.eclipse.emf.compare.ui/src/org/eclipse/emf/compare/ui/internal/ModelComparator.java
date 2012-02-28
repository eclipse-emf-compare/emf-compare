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
package org.eclipse.emf.compare.ui.internal;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.IStreamContentAccessor;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.mapping.IModelProviderDescriptor;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.EMFCompareException;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.engine.DefaultMatchScopeProvider;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.ui.EMFCompareUIMessages;
import org.eclipse.emf.compare.ui.EMFCompareUIPlugin;
import org.eclipse.emf.compare.ui.ICompareInputDetailsProvider;
import org.eclipse.emf.compare.ui.ModelCompareInput;
import org.eclipse.emf.compare.ui.team.AbstractTeamHandler;
import org.eclipse.emf.compare.ui.util.EMFCompareConstants;
import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.compare.util.EclipseModelUtils;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.mapping.ISynchronizationContext;
import org.eclipse.team.ui.mapping.ISynchronizationCompareAdapter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * This class will allow model comparison given a CompareConfiguration while allowing specific handling of
 * resource loading when team providers require it.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class ModelComparator implements ICompareInputDetailsProvider {
	/**
	 * This will be initialized to <code>false</code> in eclipse 3.5M6 and above, <code>true</code> otherwise.
	 */
	protected static final boolean IS_LESS_GANYMEDE;

	/**
	 * This will contain instances of comparators associated to given CompareConfiguration.
	 */
	protected static final Map<CompareConfiguration, ModelComparator> INSTANCES = new HashMap<CompareConfiguration, ModelComparator>();

	/** Keeps track of the team handlers declared for the extension point. */
	private static final Set<TeamHandlerDescriptor> CACHED_HANDLERS = new LinkedHashSet<TeamHandlerDescriptor>();

	/** Name of the extension point to parse for team handlers. */
	private static final String TEAM_HANDLERS_EXTENSION_POINT = "org.eclipse.emf.compare.ui.team.handler"; //$NON-NLS-1$

	/** This will hold the result of these resources' comparison. */
	protected ComparisonSnapshot comparisonResult;

	/** Keeps a reference to the last "ancestor" element of the input. */
	private ITypedElement ancestorElement;

	/** Resource of the ancestor model used in this comparison. */
	private Resource ancestorResource;

	/** Keeps a reference to the root CompareConfiguration. */
	private final CompareConfiguration compareConfiguration;

	/** This will keep track of the handler used by this comparison. */
	private AbstractTeamHandler comparisonHandler;

	/** Keeps a reference to the last "left" element of the input. */
	private ITypedElement leftElement;

	/**
	 * Indicates that the left compared model is remote and shouldn't be modified.
	 */
	private boolean leftIsRemote;

	/** Resource of the left model used in this comparison. */
	private Resource leftResource;

	/** Keeps a reference to the last "right" element of the input. */
	private ITypedElement rightElement;

	/**
	 * Indicates that the right compared model is remote and shouldn't be modified. This will only happen if
	 * we couldn't load a local resource when comparing with repository.
	 */
	private boolean rightIsRemote;

	/** Resource of the right model used in this comparison. */
	private Resource rightResource;

	/** The compare input we were initially fed. */
	private final ICompareInput compareInput;

	/**
	 * This will be set to <code>true</code> if we managed to load the resources, <code>false</code> if an
	 * error occured.
	 */
	private boolean loadingSucceeded;

	static {
		parseExtensionMetaData();
		boolean temp = false;
		try {
			Class.forName("org.eclipse.compare.internal.ICompareAsText"); //$NON-NLS-1$
			temp = true;
		} catch (ClassNotFoundException e) {
			// Will be thrown in Eclipse 3.5M6 or above
		}
		IS_LESS_GANYMEDE = temp;
		if (PlatformUI.getWorkbench() != null && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
			final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			if (page != null) {
				page.addPartListener(new CompareEditorlistener());
			}
		}
	}

	/**
	 * Instantiates a ModelComparator given its root CompareConfiguration.
	 * 
	 * @param configuration
	 *            CompareConfiguration of this comparator.
	 * @param input
	 *            The previous compare input.
	 */
	private ModelComparator(CompareConfiguration configuration, ICompareInput input) {
		compareConfiguration = configuration;
		compareInput = input;
	}

	/**
	 * This will return the ModelComparator associated to the given CompareConfiguration.
	 * 
	 * @param configuration
	 *            CompareConfiguration of this comparator.
	 * @param input
	 *            The previous compare input.
	 * @return The comparator for this configuration.
	 */
	public static ModelComparator getComparator(CompareConfiguration configuration, ICompareInput input) {
		if (!INSTANCES.containsKey(configuration)) {
			INSTANCES.put(configuration, new ModelComparator(configuration, input));
		}
		return INSTANCES.get(configuration);
	}

	/**
	 * This will return the ModelComparator associated to the given CompareConfiguration.
	 * 
	 * @param configuration
	 *            CompareConfiguration of this comparator.
	 * @return The comparator for this configuration.
	 */
	public static ModelComparator getComparator(CompareConfiguration configuration) {
		return INSTANCES.get(configuration);
	}

	/**
	 * Removes the comparator corresponding to a no longer used configuration.
	 * 
	 * @param configuration
	 *            CompareConfiguration which comparator should be removed.
	 */
	public static void removeComparator(CompareConfiguration configuration) {
		// In eclipse 3.5M6 and above, viewers can be switched. We cannot remove the comparator then
		if (IS_LESS_GANYMEDE) {
			INSTANCES.remove(configuration);
		}
	}

	/**
	 * This will parse {@link #TEAM_HANDLERS_EXTENSION_POINT} for team handlers.
	 */
	private static void parseExtensionMetaData() {
		final IExtension[] extensions = Platform.getExtensionRegistry()
				.getExtensionPoint(TEAM_HANDLERS_EXTENSION_POINT).getExtensions();
		// The RevisionComparisonHandler will be added last in the registry to allow clients override
		TeamHandlerDescriptor revisionHandler = null;
		for (final IExtension extension : extensions) {
			for (final IConfigurationElement configElement : extension.getConfigurationElements()) {
				final TeamHandlerDescriptor descriptor = new TeamHandlerDescriptor(configElement);
				if (RevisionComparisonHandler.class.getName().equals(descriptor.getHandlerClass())) {
					revisionHandler = descriptor;
				} else {
					CACHED_HANDLERS.add(descriptor);
				}
			}
		}
		CACHED_HANDLERS.add(revisionHandler);
	}

	/**
	 * This will run the comparison process and return the resulting {@link ComparisonSnapshot snapshot}.
	 * 
	 * @param configuration
	 *            Compared configuration of this comparison. Properties will be set on this to hold comparison
	 *            data.
	 * @return Result of the comparison of the loaded resources.
	 */
	public ComparisonSnapshot compare(CompareConfiguration configuration) {
		if (!loadingSucceeded) {
			// We couldn't load the resource. It's useless to carry on.
			comparisonResult = DiffFactory.eINSTANCE.createComparisonResourceSetSnapshot();
		}

		if (comparisonResult == null) {
			final Date start = Calendar.getInstance().getTime();

			MatchService.setMatchEngineSelector(new VisualEngineSelector());
			DiffService.setDiffEngineSelector(new VisualEngineSelector());

			// show prompt to select the match scope
			final MessageDialog queryMatchScopeDialog = new MessageDialog(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					EMFCompareUIMessages.getString("ModelComparator.MatchModeSelectionDialogTitle"), null, EMFCompareUIMessages.getString("ModelComparator.MatchModeSelectionDialogMessage"), MessageDialog.NONE, new String[] {EMFCompareUIMessages.getString("ModelComparator.MatchModelSelectionDialogResourceOption"), EMFCompareUIMessages.getString("ModelComparator.MatchModelSelectionDialogResourceSetOption") }, 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			final boolean doResourceMatchOnly = queryMatchScopeDialog.open() == 0;

			if (doResourceMatchOnly) {
				comparisonResult = doResourceCompare();
			} else {
				comparisonResult = doResourceSetCompare();
			}

			// set date of comparison
			final Date end = Calendar.getInstance().getTime();
			comparisonResult.setDate(end);

			configuration.setLeftEditable(configuration.isLeftEditable() && !isLeftRemote());
			configuration.setRightEditable(configuration.isRightEditable() && !isRightRemote());
			configuration.setProperty(EMFCompareConstants.PROPERTY_COMPARISON_TIME,
					end.getTime() - start.getTime());
			if (isLeftRemote()) {
				if (doResourceMatchOnly) {
					configuration.setLeftLabel(EMFCompareUIMessages.getString(EMFCompareUIMessages
							.getString("ModelComparator.remoteResourceCompareLabel"))); //$NON-NLS-1$
					configuration.setRightLabel(EMFCompareUIMessages.getString(EMFCompareUIMessages
							.getString("ModelComparator.localResourceCompareLabel"))); //$NON-NLS-1$
				} else {

					configuration.setLeftLabel(EMFCompareUIMessages.getString(EMFCompareUIMessages
							.getString("ModelComparator.remoteResourceSetCompareLabel"))); //$NON-NLS-1$
					configuration.setRightLabel(EMFCompareUIMessages.getString(EMFCompareUIMessages
							.getString("ModelComparator.localResourceSetCompareLabel"))); //$NON-NLS-1$

				}
			}
		}
		return comparisonResult;
	}

	/**
	 * This will run the comparison process and return the resulting {@link ComparisonSnapshot snapshot},
	 * silently, in a thread UI.
	 * 
	 * @param configuration
	 *            Compared configuration of this comparison. Properties will be set on this to hold comparison
	 *            data.
	 * @return Result of the comparison of the loaded resources.
	 */
	public ComparisonSnapshot compareSilentlyInThreadUI(CompareConfiguration configuration) {
		if (!loadingSucceeded) {
			// We couldn't load the resource. It's useless to carry on.
			comparisonResult = DiffFactory.eINSTANCE.createComparisonResourceSetSnapshot();
		}

		if (comparisonResult == null) {
			final Date start = Calendar.getInstance().getTime();

			MatchService.setMatchEngineSelector(new VisualEngineSelector());
			DiffService.setDiffEngineSelector(new VisualEngineSelector());

			try {
				comparisonResult = doResourceSetCompareInThreadUI(new NullProgressMonitor());
			} catch (InterruptedException e) {
				EMFComparePlugin.log(e, false);
			}

			// set date of comparison
			final Date end = Calendar.getInstance().getTime();
			comparisonResult.setDate(end);

			configuration.setLeftEditable(configuration.isLeftEditable() && !isLeftRemote());
			configuration.setRightEditable(configuration.isRightEditable() && !isRightRemote());
			configuration.setProperty(EMFCompareConstants.PROPERTY_COMPARISON_TIME,
					end.getTime() - start.getTime());
			if (isLeftRemote()) {
				configuration.setLeftLabel(EMFCompareUIMessages.getString(EMFCompareUIMessages
						.getString("ModelComparator.remoteResourceSetCompareLabel"))); //$NON-NLS-1$
				configuration.setRightLabel(EMFCompareUIMessages.getString(EMFCompareUIMessages
						.getString("ModelComparator.localResourceSetCompareLabel"))); //$NON-NLS-1$
			}
		}
		return comparisonResult;
	}

	/**
	 * Disposes of this comparator and the resources it loaded.
	 */
	public void dispose() {
		final Thread longRunningDispose = new Thread() {
			@Override
			public void run() {
				final ComparisonSnapshot snapshot = getComparisonResult();
				if (snapshot.eResource() != null) {
					disposeOfResource(snapshot.eResource());
				}
				disposeOfResource(getAncestorResource());
				disposeOfResource(getLeftResource());
				disposeOfResource(getRightResource());
			}
		};
		longRunningDispose.start();
	}

	/**
	 * Disposes of the given resource.
	 * 
	 * @param res
	 *            The resource to dispose.
	 */
	protected void disposeOfResource(Resource res) {
		if (res != null) {
			if (res.getResourceSet() != null) {
				for (Resource sibling : res.getResourceSet().getResources()) {
					sibling.unload();
				}
				res.getResourceSet().getResources().clear();
			} else {
				res.unload();
			}
		}
	}

	/**
	 * Perform a comparison of the left and right (and if specified ancestor resource).
	 * 
	 * @return the {@link ComparisonResourceSnapshot} that contains the comparison result.
	 */
	protected ComparisonResourceSnapshot doResourceCompare() {
		final RunnableResourceCompare runnable = new RunnableResourceCompare();
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
		} catch (final InterruptedException e) {
			EMFComparePlugin.log(e, false);
		} catch (final EMFCompareException e) {
			EMFComparePlugin.log(e, false);
		} catch (final InvocationTargetException e) {
			EMFComparePlugin.log(e, true);
		}
		return runnable.getComparisonResourceSnapshot();

	}

	/**
	 * Runnable to execute the resource comparison.
	 * 
	 * @author cnotot
	 */
	public class RunnableResourceCompare implements IRunnableWithProgress {

		/**
		 * The result of the resource comparison.
		 */
		private ComparisonResourceSnapshot snapshot;

		/**
		 * {@inheritDoc}.
		 * 
		 * @param monitor
		 * @throws InvocationTargetException
		 * @throws InterruptedException
		 */
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			snapshot = doResourceCompareInThreadUI(monitor);
		}

		/**
		 * Get the result of the resource set comparison.
		 * 
		 * @return ComparisonResourceSetSnapshot
		 */
		public ComparisonResourceSnapshot getComparisonResourceSnapshot() {
			return snapshot;
		}

	}

	/**
	 * Perform a comparison of the left and right (and if specified ancestor resource), in a thread UI.
	 * 
	 * @param monitor
	 *            IProgressMonitor
	 * @return the {@link ComparisonResourceSnapshot} that contains the comparison result.
	 * @throws InterruptedException
	 *             exception
	 */
	protected ComparisonResourceSnapshot doResourceCompareInThreadUI(IProgressMonitor monitor)
			throws InterruptedException {
		// create snapshot
		final ComparisonResourceSnapshot snapshot = DiffFactory.eINSTANCE.createComparisonResourceSnapshot();
		snapshot.setDiff(DiffFactory.eINSTANCE.createDiffModel());
		snapshot.setMatch(MatchFactory.eINSTANCE.createMatchModel());

		final Map<String, Object> options = new EMFCompareMap<String, Object>();
		options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);

		// do comparison
		final MatchModel match;
		if (getAncestorResource() == null) {
			options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new DefaultMatchScopeProvider(
					getLeftResource(), getRightResource()));
			match = MatchService.doResourceMatch(getLeftResource(), getRightResource(), options);
		} else {
			options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new DefaultMatchScopeProvider(
					getLeftResource(), getRightResource(), getAncestorResource()));
			match = MatchService.doResourceMatch(getLeftResource(), getRightResource(),
					getAncestorResource(), options);
		}
		final DiffModel diff = DiffService.doDiff(match, getAncestorResource() != null);
		snapshot.setDiff(diff);
		snapshot.setMatch(match);

		return snapshot;
	}

	/**
	 * /** Perform a comparison of the left and right (and if specified ancestor resource), as well as all
	 * other resources, included in their respective resource sets.
	 * 
	 * @return the {@link ComparisonResourceSetSnapshot} that contains the comparison result.
	 */
	protected ComparisonResourceSetSnapshot doResourceSetCompare() {
		final RunnableResourceSetCompare runnable = new RunnableResourceSetCompare();
		try {
			PlatformUI.getWorkbench().getProgressService().busyCursorWhile(runnable);
		} catch (final InterruptedException e) {
			EMFComparePlugin.log(e, false);
		} catch (final EMFCompareException e) {
			EMFComparePlugin.log(e, false);
		} catch (final InvocationTargetException e) {
			EMFComparePlugin.log(e, true);
		}
		return runnable.getComparisonResourceSetSnapshot();
	}

	/**
	 * Runnable to execute the resource set comparison.
	 * 
	 * @author cnotot
	 */
	public class RunnableResourceSetCompare implements IRunnableWithProgress {

		/**
		 * The result of the resource set comparison.
		 */
		private ComparisonResourceSetSnapshot snapshot;

		/**
		 * {@inheritDoc}.
		 * 
		 * @param monitor
		 * @throws InvocationTargetException
		 * @throws InterruptedException
		 */
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			snapshot = doResourceSetCompareInThreadUI(monitor);
		}

		/**
		 * Get the result of the resource set comparison.
		 * 
		 * @return ComparisonResourceSetSnapshot
		 */
		public ComparisonResourceSetSnapshot getComparisonResourceSetSnapshot() {
			return snapshot;
		}

	}

	/**
	 * /** Perform a comparison of the left and right (and if specified ancestor resource), as well as all
	 * other resources, included in their respective resource sets, in a thread UI.
	 * 
	 * @param monitor
	 *            IProgressMonitor
	 * @return the {@link ComparisonResourceSetSnapshot} that contains the comparison result.
	 * @throws InterruptedException
	 *             exception
	 */
	protected ComparisonResourceSetSnapshot doResourceSetCompareInThreadUI(IProgressMonitor monitor)
			throws InterruptedException {
		final ComparisonResourceSetSnapshot snapshot = DiffFactory.eINSTANCE
				.createComparisonResourceSetSnapshot();
		snapshot.setDiffResourceSet(DiffFactory.eINSTANCE.createDiffResourceSet());
		snapshot.setMatchResourceSet(MatchFactory.eINSTANCE.createMatchResourceSet());

		final Map<String, Object> options = new EMFCompareMap<String, Object>();
		options.put(MatchOptions.OPTION_PROGRESS_MONITOR, monitor);

		// do comparison
		final MatchResourceSet match;
		if (getAncestorResource() == null) {
			options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new DefaultMatchScopeProvider(
					getLeftResource().getResourceSet(), getRightResource().getResourceSet()));
			match = MatchService.doResourceSetMatch(getLeftResource().getResourceSet(), getRightResource()
					.getResourceSet(), options);
		} else {
			options.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new DefaultMatchScopeProvider(
					getLeftResource().getResourceSet(), getRightResource().getResourceSet(),
					getAncestorResource().getResourceSet()));
			match = MatchService.doResourceSetMatch(getLeftResource().getResourceSet(), getRightResource()
					.getResourceSet(), getAncestorResource().getResourceSet(), options);
		}
		final DiffResourceSet diff = DiffService.doDiff(match, getAncestorResource() != null);
		snapshot.setDiffResourceSet(diff);
		snapshot.setMatchResourceSet(match);
		return snapshot;
	}

	/**
	 * Returns the compared resources ancestor.
	 * 
	 * @return The compared resources ancestor.
	 */
	public Resource getAncestorResource() {
		if (comparisonHandler != null)
			return comparisonHandler.getAncestorResource();
		return ancestorResource;
	}

	/**
	 * This will return the comparison result.
	 * 
	 * @return The comparison result. <code>null</code> if no comparison has been done since last loading
	 *         resources.
	 */
	public ComparisonSnapshot getComparisonResult() {
		return comparisonResult;
	}

	/**
	 * Returns the left compared resource.
	 * 
	 * @return The left compared resource.
	 */
	public Resource getLeftResource() {
		if (comparisonHandler != null)
			return comparisonHandler.getLeftResource();
		return leftResource;
	}

	/**
	 * Returns the right compared resource.
	 * 
	 * @return The right compared resource.
	 */
	public Resource getRightResource() {
		if (comparisonHandler != null)
			return comparisonHandler.getRightResource();
		return rightResource;
	}

	/**
	 * Indicates that the left resource is remote and shouldn't be modified.
	 * 
	 * @return <code>true</code> if the left compared resource is remote, <code>false</code> otherwise.
	 */
	public boolean isLeftRemote() {
		if (comparisonHandler != null)
			return comparisonHandler.isLeftRemote();
		return leftIsRemote;
	}

	/**
	 * Indicates that the right resource is remote and shouldn't be modified. Note that this will never return
	 * <code>true</code> unless we failed to load a local resource for comparison.
	 * 
	 * @return <code>true</code> if the right compared resource is remote, <code>false</code> otherwise.
	 */
	public boolean isRightRemote() {
		if (comparisonHandler != null)
			return comparisonHandler.isRightRemote();
		return rightIsRemote;
	}

	/**
	 * Returns the compare input we've initially been fed.
	 * 
	 * @return The initial compare input.
	 */
	public ICompareInput getCompareInput() {
		return compareInput;
	}

	/**
	 * This will load the resources held by <code>input</code>.
	 * 
	 * @param input
	 *            CompareInput which holds the resources to be loaded.
	 * @return <code>true</code> if the given models have been successfully loaded, <code>false</code>
	 *         otherwise.
	 */
	public boolean loadResources(ICompareInput input) {
		if (ancestorElement != input.getAncestor() || leftElement != input.getLeft()
				|| rightElement != input.getRight()) {
			clear();
			leftElement = input.getLeft();
			rightElement = input.getRight();
			ancestorElement = input.getAncestor();

			// check whether this comparison hasn't already been played (workaround for #345415)
			if (handleResourceMapping(input)) {
				loadingSucceeded = true;
				return loadingSucceeded;
			}

			try {
				// This will be sufficient when comparing local resources
				loadingSucceeded = handleLocalResources(leftElement, rightElement, ancestorElement);
				// If resources weren't local, iterates through the registry to find
				// a proper team handler
				if (!loadingSucceeded) {
					final Iterator<TeamHandlerDescriptor> handlerDescriptorIterator = CACHED_HANDLERS
							.iterator();
					while (handlerDescriptorIterator.hasNext()) {
						final AbstractTeamHandler handler = handlerDescriptorIterator.next()
								.getHandlerInstance();
						loadingSucceeded |= handler.loadResources(input);
						if (loadingSucceeded) {
							comparisonHandler = handler;
							break;
						}
					}
				}
				// We didn't find a proper handler, use a generic one
				if (!loadingSucceeded) {
					loadingSucceeded |= handleGenericResources(leftElement, rightElement, ancestorElement);
				}
				/*
				 * The generic handler should work for any EMF resources. If we're here, no exception has been
				 * thrown and loading ended accurately
				 */
				loadingSucceeded = true;
			} catch (final IOException e) {
				final String dialogMessage;
				if (IS_LESS_GANYMEDE) {
					dialogMessage = EMFCompareUIMessages.getString("ModelComparator.ResourceLoadingFailure"); //$NON-NLS-1$
				} else {
					dialogMessage = EMFCompareUIMessages
							.getString("ModelComparator.ResourceLoadingFailureGanymede"); //$NON-NLS-1$
				}
				final Dialog dialog = new CompareErrorDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(), "Comparison failed", dialogMessage, //$NON-NLS-1$
						new Status(IStatus.ERROR, EMFCompareUIPlugin.PLUGIN_ID, e.getMessage(), e));
				final int buttonPressed = dialog.open();
				if (buttonPressed == CompareErrorDialog.COMPARE_AS_TEXT_ID) {
					final Set<Object> set = new HashSet<Object>();
					final CompareEditorInput editorInput = (CompareEditorInput)compareConfiguration
							.getContainer();
					// value of the 3.3 and 3.4 "ICompareAsText.PROP_TEXT_INPUT"
					compareConfiguration.setProperty("org.eclipse.compare.TextInputs", set); //$NON-NLS-1$
					set.add(editorInput);
					set.add(editorInput.getCompareResult());
					CompareUI.openCompareEditorOnPage(editorInput, null);
				}
			} catch (final CoreException e) {
				EMFComparePlugin.log(e.getStatus());
			}
		} else {
			// input was the same as the last
		}
		return loadingSucceeded;
	}

	/**
	 * This is a workaround for bug 345415 and needs to be removed ASAP when this bug is fixed. Basically,
	 * Platform/Compare does not call our ModelProvider when using the action
	 * "right-click > Open In Compare Editor". We then do the work ourselves here... but with insufficient
	 * information. We'll need to access non-API fields along the way.
	 * 
	 * @param input
	 *            The input we've been fed by Platform/Compare.
	 * @return <code>true</code> if we managed to find a ResourceMapping with a {@link ModelCompareInput}
	 *         corresponding to <em>input</em>, <code>false</code> otherwise.
	 */
	private boolean handleResourceMapping(ICompareInput input) {
		final IResourceProvider resourceProvider = (IResourceProvider)Platform.getAdapterManager()
				.getAdapter(leftElement, IResourceProvider.class);
		if (resourceProvider != null) {
			final IResource localResource = resourceProvider.getResource();
			final IModelProviderDescriptor[] descriptors = ModelProvider.getModelProviderDescriptors();
			for (int i = 0; i < descriptors.length; i++) {
				final IModelProviderDescriptor descriptor = descriptors[i];
				try {
					final IResource[] resources = descriptor
							.getMatchingResources(new IResource[] {localResource, });
					if (resources.length > 0) {
						final ModelProvider modelProvider = descriptor.getModelProvider();
						final ISynchronizationCompareAdapter compareAdapter = (ISynchronizationCompareAdapter)Platform
								.getAdapterManager().getAdapter(modelProvider,
										ISynchronizationCompareAdapter.class);
						// FIXME until 345415 is fixed, we need to find the proper model provider from here...
						// ... which requires access to non-API things.
						final Field contextField = input.getClass().getDeclaredField("context"); //$NON-NLS-1$
						contextField.setAccessible(true);
						final ISynchronizationContext context = (ISynchronizationContext)contextField
								.get(input);
						final ICompareInput actualInput = compareAdapter.asCompareInput(context,
								localResource);

						if (actualInput instanceof ModelCompareInput) {
							comparisonResult = ((ModelCompareInput)actualInput).getComparisonSnapshot();
							return true;
						}
					}
				} catch (CoreException e) {
					// FIXME log
				} catch (SecurityException e) {
					// FIXME remove when 345415 is fixed
				} catch (NoSuchFieldException e) {
					// FIXME remove when 345415 is fixed
				} catch (IllegalArgumentException e) {
					// FIXME remove when 345415 is fixed
				} catch (IllegalAccessException e) {
					// FIXME remove when 345415 is fixed
				}
			}
		}

		return false;
	}

	/**
	 * We might want to create an editor for an already compared configuration. This enables us to do so.
	 * 
	 * @param snapshot
	 *            The expected result of this comparison.
	 */
	public void setComparisonResult(ComparisonSnapshot snapshot) {
		if (snapshot instanceof ComparisonResourceSnapshot) {
			comparisonResult = DiffFactory.eINSTANCE.createComparisonResourceSetSnapshot();
			comparisonResult.setDate(snapshot.getDate());
			final DiffResourceSet diffRS = DiffFactory.eINSTANCE.createDiffResourceSet();
			diffRS.getDiffModels().add(((ComparisonResourceSnapshot)snapshot).getDiff());
			((ComparisonResourceSetSnapshot)comparisonResult).setDiffResourceSet(diffRS);
			final MatchResourceSet matchRS = MatchFactory.eINSTANCE.createMatchResourceSet();
			matchRS.getMatchModels().add(((ComparisonResourceSnapshot)snapshot).getMatch());
			((ComparisonResourceSetSnapshot)comparisonResult).setMatchResourceSet(matchRS);
		} else {
			comparisonResult = snapshot;
		}
	}

	/**
	 * Clears all loaded resources from the resource set.
	 */
	private void clear() {
		clearResourceSet(leftResource, rightResource, ancestorResource);
		leftResource = null;
		rightResource = null;
		ancestorResource = null;
		comparisonResult = null;
		comparisonHandler = null;
	}

	/**
	 * This will empty the resourceSet of the given <tt>resource</tt>.
	 * 
	 * @param resource
	 *            Resource that is to be cleared.
	 */
	private void clearResourceSet(Resource... resource) {
		for (int i = 0; i < resource.length; i++) {
			if (resource[i] == null) {
				continue;
			}
			final ResourceSet resourceSet = resource[i].getResourceSet();
			final Iterator<Resource> resourcesIterator = resourceSet.getResources().iterator();
			while (resourcesIterator.hasNext()) {
				resourcesIterator.next().unload();
			}
			resourceSet.getResources().clear();
		}
	}

	/**
	 * This generic handler should be able to load resources passed by any team plug-in. Using this handler
	 * instead of the subversive specific handler in plugin org.eclipse.emf.compare.team.subversive will
	 * result in unsaveable merge operations for example. Users of specific team plugins should write their
	 * own handler to avoid going through this method in order for their files to be saveable (thus
	 * mergeable).
	 * 
	 * @param left
	 *            Handler of the left compared model.
	 * @param right
	 *            Handler of the right compared model.
	 * @param ancestor
	 *            Handler of these two models' common ancestor.
	 * @return <code>true</code> If all resources have been loaded by this handler, <code>false</code>
	 *         otherwise.
	 * @throws IOException
	 *             Thrown if the right resource cannot be loaded.
	 * @throws CoreException
	 *             Thrown if exceptions occur when loading the remote resources (left and ancestor).
	 */
	private boolean handleGenericResources(ITypedElement left, ITypedElement right, ITypedElement ancestor)
			throws IOException, CoreException {
		if (left instanceof ResourceNode && right instanceof IStreamContentAccessor) {
			if (((ResourceNode)left).getResource().isAccessible()) {
				leftResource = EclipseModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
						new ResourceSetImpl()).eResource();
			} else {
				leftResource = ModelUtils.createResource(URI.createPlatformResourceURI(((ResourceNode)left)
						.getResource().getFullPath().toOSString(), true));
				// resource has been deleted. We set it as "remote" to disable merge facilities
				leftIsRemote = true;
			}
			try {
				rightResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(),
						right.getName(), new ResourceSetImpl()).eResource();
			} catch (final IOException e) {
				// We couldn't load the remote resource. Considers it has been added to the repository
				rightResource = ModelUtils.createResource(URI.createURI(right.getName()));
				// Set the left as remote to disable merge facilities
				leftIsRemote = true;
			}
			rightIsRemote = true;
			if (ancestor != null) {
				try {
					ancestorResource = ModelUtils.load(((IStreamContentAccessor)ancestor).getContents(),
							ancestor.getName(), new ResourceSetImpl()).eResource();
				} catch (final IOException e) {
					// Couldn't load ancestor resource, create an empty one
					ancestorResource = ModelUtils.createResource(URI.createURI(ancestor.getName()));
				}
			}
			return true;
		}
		/*
		 * We should never be here. There always is a local resource when comparing with CVS. this code will
		 * be executed if we couldn't manage to handle this local resource as such. Though the resource will
		 * be loaded thanks to this generic handler, note that it will not be saveable.
		 */
		boolean result = false;
		if (left instanceof IStreamContentAccessor && right instanceof IStreamContentAccessor) {
			leftResource = ModelUtils.load(((IStreamContentAccessor)left).getContents(), left.getName(),
					new ResourceSetImpl()).eResource();
			rightResource = ModelUtils.load(((IStreamContentAccessor)right).getContents(), right.getName(),
					new ResourceSetImpl()).eResource();
			rightIsRemote = true;
			leftIsRemote = true;
			if (ancestor != null) {
				ancestorResource = ModelUtils.load(((IStreamContentAccessor)ancestor).getContents(),
						ancestor.getName(), new ResourceSetImpl()).eResource();
			}
			result = true;
		}
		return result;
	}

	/**
	 * This will try and load the given element as being local resources.
	 * 
	 * @param left
	 *            Handler of the left compared model.
	 * @param right
	 *            Handler of the right compared model.
	 * @param ancestor
	 *            Handler of these two models' common ancestor.
	 * @return <code>true</code> If all resources have been loaded by this handler, <code>false</code>
	 *         otherwise.
	 * @throws IOException
	 *             Thrown if resources cannot be loaded.
	 */
	private boolean handleLocalResources(ITypedElement left, ITypedElement right, ITypedElement ancestor)
			throws IOException {
		if (left instanceof ResourceNode && right instanceof ResourceNode) {
			leftResource = EclipseModelUtils.load(((ResourceNode)left).getResource().getFullPath(),
					new ResourceSetImpl()).eResource();
			rightResource = EclipseModelUtils.load(((ResourceNode)right).getResource().getFullPath(),
					new ResourceSetImpl()).eResource();
			if (ancestor != null) {
				ancestorResource = EclipseModelUtils.load(
						((ResourceNode)ancestor).getResource().getFullPath(), new ResourceSetImpl())
						.eResource();
			}
			return true;
		}
		return false;
	}

	/**
	 * This will be used to listen to the changes on compare editors in the workspace : we need to know when
	 * they are closed in order to dispose of our comparators.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	public static final class CompareEditorlistener implements IPartListener {
		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partClosed(IWorkbenchPart part) {
			if (part instanceof IEditorPart
					&& ((IEditorPart)part).getEditorInput() instanceof CompareEditorInput) {
				final CompareEditorInput editorInput = (CompareEditorInput)((IEditorPart)part)
						.getEditorInput();
				final CompareConfiguration configuration = editorInput.getCompareConfiguration();
				final ModelComparator comparator = getComparator(configuration);
				if (comparator != null) {
					comparator.dispose();
					INSTANCES.remove(configuration);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partActivated(IWorkbenchPart part) {
			// Do nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partBroughtToTop(IWorkbenchPart part) {
			// Do nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partDeactivated(IWorkbenchPart part) {
			// Do nothing
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
		 */
		public void partOpened(IWorkbenchPart part) {
			// Do nothing
		}
	}

	/**
	 * Describes a team handler registered from a plug-in's extension point.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static final class TeamHandlerDescriptor {
		/**
		 * Name of the extension point attribute corresponding to the handler's class.
		 */
		private static final String ATTRIBUTE_HANDLER_CLASS = "class"; //$NON-NLS-1$

		/**
		 * Keeps a reference to the configuration element that describes this handler.
		 */
		private final IConfigurationElement element;

		/** This descriptor's wrapped {@link AbstractTeamHandler handler}. */
		private AbstractTeamHandler handler;

		/**
		 * Constructs a new descriptor from an IConfigurationElement.
		 * 
		 * @param configuration
		 *            Configuration of the team handler.
		 */
		public TeamHandlerDescriptor(IConfigurationElement configuration) {
			element = configuration;
		}

		/**
		 * Returns an instance of the described handler.
		 * 
		 * @return Instance of the handler.
		 */
		public AbstractTeamHandler getHandlerInstance() {
			if (handler == null) {
				try {
					handler = (AbstractTeamHandler)element.createExecutableExtension(ATTRIBUTE_HANDLER_CLASS);
				} catch (final CoreException e) {
					EMFComparePlugin.log(e, true);
				}
			}
			return handler;
		}

		/**
		 * Returns the class of the wrapped AbstractTeamHandler. This is only used internally to set the
		 * RevisionComparisonHandler last.
		 * 
		 * @return The class of the wrapped AbstractTeamHandler.
		 */
		String getHandlerClass() {
			return element.getAttribute(ATTRIBUTE_HANDLER_CLASS);
		}
	}

	/**
	 * This implementation of an ErrorDialog allows us to display comparison failures to the user along with a
	 * way for them to force a text comparison of the offending files.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	final class CompareErrorDialog extends ErrorDialog {
		/** ID of the "Compare As Text" button. Not intended to be either subclassed or accessed externally. */
		static final int COMPARE_AS_TEXT_ID = IDialogConstants.CLIENT_ID + 1;

		/**
		 * Delegates to the super constructor.
		 * 
		 * @param parentShell
		 *            Parent Shell of this viewer.
		 * @param dialogTitle
		 *            Title of this error dialog.
		 * @param dialogMessage
		 *            Message to display to the user.
		 * @param status
		 *            The error to show to the user.
		 */
		public CompareErrorDialog(Shell parentShell, String dialogTitle, String dialogMessage, IStatus status) {
			super(parentShell, dialogTitle, dialogMessage, status, IStatus.OK | IStatus.INFO
					| IStatus.WARNING | IStatus.ERROR);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.ErrorDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			/*
			 * Using the CANCEL_ID for an "OK" button in order to inherit the cancel behavior but be coherent
			 * with the error message.
			 */
			createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.OK_LABEL, true);
			if (IS_LESS_GANYMEDE) {
				createButton(parent, COMPARE_AS_TEXT_ID, "Compare As Text", false); //$NON-NLS-1$
			}
			createDetailsButton(parent);
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.jface.dialogs.ErrorDialog#buttonPressed(int)
		 */
		@Override
		protected void buttonPressed(int id) {
			if (id == COMPARE_AS_TEXT_ID) {
				setReturnCode(COMPARE_AS_TEXT_ID);
				close();
			} else {
				super.buttonPressed(id);
			}
		}
	}
}
