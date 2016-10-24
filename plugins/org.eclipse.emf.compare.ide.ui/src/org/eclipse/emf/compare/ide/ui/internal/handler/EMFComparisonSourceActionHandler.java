/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.handler;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.EMFCompare.Builder;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.domain.impl.EMFCompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIMessages;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.ide.ui.internal.editor.ComparisonScopeEditorInput;
import org.eclipse.emf.compare.ide.ui.internal.logical.ComparisonScopeBuilder;
import org.eclipse.emf.compare.ide.ui.logical.SynchronizationModel;
import org.eclipse.emf.compare.ide.ui.source.IEMFComparisonSource;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.rcp.EMFCompareRCPPlugin;
import org.eclipse.emf.compare.rcp.internal.extension.impl.EMFCompareBuilderConfigurator;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Handles the comparison between selections adapting to {@link IEMFComparisonSource}s.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public class EMFComparisonSourceActionHandler extends AbstractHandler {

	/**
	 * {@inheritDoc}
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getCurrentSelection(event);

		final IEMFComparisonSource[] sources = adaptSelection(selection.toList());

		final AdapterFactory adapterFactory = new ComposedAdapterFactory(EMFCompareRCPPlugin.getDefault()
				.createFilteredAdapterFactoryRegistry(Maps.newLinkedHashMap()));

		final IEMFComparisonSource leftSource;
		final IEMFComparisonSource rightSource;
		final IEMFComparisonSource originSource;

		if (sources.length == 3) {
			final Shell shell = HandlerUtil.getActiveShell(event);
			final SelectAncestorDialog<IEMFComparisonSource> dialog = new SelectAncestorDialog<IEMFComparisonSource>(
					shell, adapterFactory, sources);
			if (dialog.open() == Window.CANCEL) {
				return null;
			} else {
				leftSource = dialog.leftElement;
				rightSource = dialog.rightElement;
				originSource = dialog.originElement;
			}
		} else if (sources.length == 2) {
			leftSource = sources[0];
			rightSource = sources[1];
			originSource = null;
		} else {
			return null;
		}

		final StorageTraversal left = getStorageTraversal(leftSource);
		final StorageTraversal right = getStorageTraversal(rightSource);
		final StorageTraversal origin = getStorageTraversal(originSource);

		if (left.getStorages().size() == 0 && right.getStorages().size() == 0
				&& origin.getStorages().size() == 0) {
			MessageDialog.openError(HandlerUtil.getActiveShell(event),
					EMFCompareIDEUIMessages
							.getString("EMFComparisonSourceActionHandler_NoResourcesFound_Title"), //$NON-NLS-1$
					EMFCompareIDEUIMessages
							.getString("EMFComparisonSourceActionHandler_NoResourcesFound_Message")); //$NON-NLS-1$
			return null;
		}

		final SynchronizationModel model = new SynchronizationModel(left, right, origin);
		final IComparisonScope scope = ComparisonScopeBuilder.create(model, new NullProgressMonitor());

		final IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		final CommandStack commandStack = getCommandStack(activePart);

		String inputTitle = MessageFormat.format(
				EMFCompareIDEUIMessages.getString("EMFComparisonSourceActionHandler_InputTitle_Base"), //$NON-NLS-1$
				leftSource.getName(), rightSource.getName());
		if (originSource != null) {
			inputTitle += MessageFormat.format(
					' ' + EMFCompareIDEUIMessages
							.getString("EMFComparisonSourceActionHandler_InputTitle_Origin"), //$NON-NLS-1$
					originSource.getName());
		}

		final CompareEditorInput input = createCompareEditorInput(commandStack, adapterFactory, scope,
				inputTitle);
		CompareUI.openCompareEditor(input);

		return null;
	}

	private IEMFComparisonSource[] adaptSelection(Collection<?> selection) {
		final IAdapterManager adapterManager = Platform.getAdapterManager();
		final Iterable<IEMFComparisonSource> sources = Iterables.transform(selection,
				new Function<Object, IEMFComparisonSource>() {
					public IEMFComparisonSource apply(Object input) {
						return (IEMFComparisonSource)adapterManager.loadAdapter(input,
								IEMFComparisonSource.class.getName());
					}
				});
		return Iterables.toArray(sources, IEMFComparisonSource.class);
	}

	/**
	 * Determines the {@link StorageTraversal} for the given {@code source}.
	 * 
	 * @param source
	 *            The {@link IEMFComparisonSource} which provides the {@link StorageTraversal}. Can be
	 *            {@code null}.
	 * @return The {@link StorageTraversal} for the given {@code source}.
	 */
	private StorageTraversal getStorageTraversal(IEMFComparisonSource source) {
		if (source == null) {
			return new StorageTraversal(new HashSet<IStorage>());
		}
		return source.getStorageTraversal();
	}

	/**
	 * Creates the comparison input.
	 * 
	 * @param commandStack
	 *            The {@link CommandStack} which is used within the input. Can be {@code} null.
	 * @param adapterFactory
	 *            The {@link AdapterFactory} which shall be used in the created input.
	 * @param scope
	 *            The {@IComparisonScope} which shall be used in the created input.
	 * @param inputTitle
	 *            The title for the created input.
	 * @return The created CompareEditorInput.
	 */
	private CompareEditorInput createCompareEditorInput(final CommandStack commandStack,
			final AdapterFactory adapterFactory, final IComparisonScope scope, final String inputTitle) {
		final ICompareEditingDomain editingDomain = createEMFCompareEditingDomain(commandStack,
				scope.getLeft(), scope.getRight(), scope.getOrigin());

		final EMFCompareConfiguration configuration = new EMFCompareConfiguration(new CompareConfiguration());

		final Builder builder = EMFCompare.builder();
		final IEclipsePreferences preferences = EMFCompareRCPPlugin.getDefault().getEMFComparePreferences();

		final EMFCompareBuilderConfigurator engineProvider;
		if (preferences != null) {
			engineProvider = new EMFCompareBuilderConfigurator(preferences,
					EMFCompareRCPPlugin.getDefault().getMatchEngineFactoryRegistry(),
					EMFCompareRCPPlugin.getDefault().getPostProcessorRegistry());
		} else {
			engineProvider = EMFCompareBuilderConfigurator.createDefault();
		}
		engineProvider.configure(builder);
		final EMFCompare comparator = builder.build();

		final CompareEditorInput input = new ComparisonScopeEditorInput(configuration, editingDomain,
				adapterFactory, comparator, scope) {
			/**
			 * {@inheritDoc}
			 * 
			 * @see org.eclipse.compare.CompareEditorInput#handleDispose()
			 */
			@Override
			protected void handleDispose() {
				super.handleDispose();
				if (editingDomain instanceof IDisposable) {
					((IDisposable)editingDomain).dispose();
				}
			}
		};

		input.setTitle(inputTitle);

		configuration.setContainer(input);
		return input;
	}

	/**
	 * Checks if the given {@code object} can provide an {@link EditingDomain} with a command stack.
	 * 
	 * @param object
	 *            The object which is checked for an {@link IEditingDomainProvider} which provides a command
	 *            stack.
	 * @return The provided {@link CommandStack} or {@code null} otherwise.
	 */
	private CommandStack getCommandStack(final Object object) {
		if (object instanceof IEditingDomainProvider) {
			final IEditingDomainProvider provider = (IEditingDomainProvider)object;
			final EditingDomain domain = provider.getEditingDomain();
			if (domain != null) {
				return domain.getCommandStack();
			}
		}
		return null;
	}

	/**
	 * Creates the {@link EMFCompareEditingDomain} for the given arguments.
	 * 
	 * @param delegatingCommandStack
	 *            Used in the newly created {@link EMFCompareEditingDomain}. Can be null.
	 * @param left
	 *            The "left" part of the comparison.
	 * @param right
	 *            The "right" part of the comparison.
	 * @param origin
	 *            The "origin" part of the comparison.
	 * @return The newly created {@link EMFCompareEditingDomain}.
	 */
	private ICompareEditingDomain createEMFCompareEditingDomain(final CommandStack delegatingCommandStack,
			final Notifier left, final Notifier right, Notifier origin) {
		if (delegatingCommandStack == null) {
			return EMFCompareEditingDomain.create(left, right, origin);
		} else {
			return EMFCompareEditingDomain.create(left, right, origin, delegatingCommandStack);
		}
	}

}
