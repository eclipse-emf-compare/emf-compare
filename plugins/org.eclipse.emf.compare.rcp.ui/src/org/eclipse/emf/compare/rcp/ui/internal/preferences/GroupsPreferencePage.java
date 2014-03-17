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

import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.compare.rcp.internal.extension.IItemDescriptor;
import org.eclipse.emf.compare.rcp.ui.EMFCompareRCPUIPlugin;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.preferences.impl.GroupsInteractiveContent;
import org.eclipse.emf.compare.rcp.ui.internal.preferences.impl.ItemDescriptorLabelProvider;
import org.eclipse.emf.compare.rcp.ui.internal.structuremergeviewer.groups.impl.DifferenceGroupManager;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider;
import org.eclipse.emf.compare.rcp.ui.structuremergeviewer.groups.IDifferenceGroupProvider.Descriptor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Preference page for group providers.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class GroupsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	/** UI content for two way comparison tab. */
	private GroupsInteractiveContent twoWayComparisonContent;

	/** UI content for three way comparison tab. */
	private GroupsInteractiveContent threeWayComparisonContent;

	/** {@link DifferenceGroupManager}. */
	private DifferenceGroupManager groupManager = new DifferenceGroupManager(EMFCompareRCPUIPlugin
			.getDefault().getEMFCompareUIPreferences(), EMFCompareRCPUIPlugin.getDefault()
			.getItemDifferenceGroupProviderRegistry());

	/**
	 * {@inheritDoc}
	 */
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

		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		fillTwoWayComparisonTab(tabFolder);
		fillThreeWayComparisonTab(tabFolder);
		return container;
	}

	/**
	 * Fills the tab for two way comparison.
	 * 
	 * @param tabFolder
	 *            Holding tab folder.
	 */
	private void fillTwoWayComparisonTab(TabFolder tabFolder) {
		Composite tabSkeletonComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("GroupsPreferencePage.twoWayComparisonTab.label"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("GroupsPreferencePage.viewerDescription.label")); //$NON-NLS-1$

		List<IItemDescriptor<Descriptor>> currentGroupRanking = groupManager.getCurrentGroupRanking(false);

		twoWayComparisonContent = createInteractiveContent(tabSkeletonComposite, currentGroupRanking,
				currentGroupRanking.get(0));
	}

	/**
	 * Fills the tab for three way comparison.
	 * 
	 * @param tabFolder
	 *            Holding tab folder.
	 */
	private void fillThreeWayComparisonTab(TabFolder tabFolder) {
		Composite tabSkeletonComposite = createTabSkeleton(tabFolder, EMFCompareRCPUIMessages
				.getString("GroupsPreferencePage.threeWayComparisonTab.label"), //$NON-NLS-1$
				EMFCompareRCPUIMessages.getString("GroupsPreferencePage.viewerDescription.label")); //$NON-NLS-1$

		List<IItemDescriptor<Descriptor>> currentGroupRanking = groupManager.getCurrentGroupRanking(true);
		threeWayComparisonContent = createInteractiveContent(tabSkeletonComposite, currentGroupRanking,
				currentGroupRanking.get(0));
	}

	/**
	 * Creates an interactive content.
	 * <p>
	 * Interactive content aims at handling all ui that is modified with user interaction.
	 * </p>
	 * 
	 * @param parent
	 *            Parent Composite.
	 * @param input
	 *            Input of the viewer.
	 * @param defaultSelection
	 *            Default element that need to be selected.
	 * @return A {@link GroupsInteractiveContent}
	 */
	private GroupsInteractiveContent createInteractiveContent(Composite parent,
			List<IItemDescriptor<IDifferenceGroupProvider.Descriptor>> input,
			IItemDescriptor<IDifferenceGroupProvider.Descriptor> defaultSelection) {

		final GroupsInteractiveContent interactiveUI = new GroupsInteractiveContent(parent);
		createViewer(interactiveUI);
		interactiveUI.setViewerInput(input);
		interactiveUI.select(defaultSelection);

		return interactiveUI;
	}

	/**
	 * Creates skeleton of a tab.
	 * 
	 * @param tabFolder
	 *            Holding tab folder.
	 * @param tabLabel
	 *            Label of the tab.
	 * @param introText
	 *            Text use as description a tab
	 * @return Main composite of the tab
	 */
	private Composite createTabSkeleton(TabFolder tabFolder, String tabLabel, String introText) {
		TabItem tbtmMain = new TabItem(tabFolder, SWT.NONE);
		tbtmMain.setText(tabLabel);
		Composite tabComposite = new Composite(tabFolder, SWT.NONE);
		tbtmMain.setControl(tabComposite);
		tabComposite.setLayout(new GridLayout(1, true));
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		tabComposite.setLayoutData(layoutData);
		// Description text
		Label introductionText = new Label(tabComposite, SWT.WRAP);
		introductionText.setText(introText);
		return tabComposite;
	}

	/**
	 * Creates the list viewer holding items.
	 * 
	 * @param interactiveUI
	 *            {@link GroupsInteractiveContent} in which the viewer need to be set up.
	 */
	private void createViewer(final GroupsInteractiveContent interactiveUI) {
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE;
		ListViewer descriptorViewer = new ListViewer(interactiveUI.getViewerComposite(), style);
		interactiveUI.setViewer(descriptorViewer);
		descriptorViewer.setContentProvider(ArrayContentProvider.getInstance());
		descriptorViewer.setLabelProvider(new ItemDescriptorLabelProvider());
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		descriptorViewer.getControl().setLayoutData(gd);
	}

	@Override
	public boolean performOk() {
		groupManager.setCurrentGroupRanking(twoWayComparisonContent.getItems(), false);
		groupManager.setCurrentGroupRanking(threeWayComparisonContent.getItems(), true);
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		twoWayComparisonContent.setViewerInput(groupManager.getDefaultRankingConfiguration(false));
		threeWayComparisonContent.setViewerInput(groupManager.getDefaultRankingConfiguration(true));
		super.performDefaults();
	}

}
