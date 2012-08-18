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
package org.eclipse.emf.compare.ide.ui.tests.automation;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.eclipse.finder.waits.Conditions;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPartReference;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CompareBot {

	private SWTWorkbenchBot bot;

	public CompareBot() {
		bot = new SWTWorkbenchBot();
	}

	public CompareBot closeWelcome() {
		Matcher<IWorkbenchPartReference> matcher = WidgetMatcherFactory.withPartName("Welcome");
		List<SWTBotView> views = bot.views(matcher);
		for (SWTBotView swtBotView : views) {
			swtBotView.close();
		}

		return this;
	}

	public CompareBot importTestProjecstInWorkspace(String dataSetName) {

		bot.menu("File").menu("New").menu("Example...").click();

		bot.waitUntil(Conditions.shellIsActive("New Example"));

		bot.tree().setFocus();
		bot.tree().getTreeItem(dataSetName);
		bot.button("Next >").click();
		bot.button("Finish").click();
		return this;
	}

	public void compareWithEachOther(String projectName, String v1, String v2) {

		/*
		 * open the project explorer view
		 */
		SWTBotView projectExp = openProjectExplorer();
		/*
		 * 
		 */
		SWTBotTree tree = projectExp.bot().tree();
		tree.expandNode(projectName).select(v1, v2).getNode(v2);
		ContextMenuHelper.clickContextMenu(tree, "Compare With", "Each Other");

		// closeMatchModeSelection();
		// yes, this is indeed a bug, we have to close the match mode selection two times.
		// closeMatchModeSelection();

		waitForProgressInformationToDisappear();

		waitForProgressInformationToDisappear();

		bot.waitUntilWidgetAppears(Conditions.waitForEditor(new BaseMatcher<IEditorReference>() {

			public boolean matches(Object item) {
				if (item instanceof IEditorReference) {
					return "org.eclipse.compare.CompareEditor".equals(((IEditorReference)item).getId());
				}
				return false;
			}

			public void describeTo(Description description) {
				// TODO Auto-generated method stub

			}

		}));

	}

	protected void waitForProgressInformationToDisappear() {
		bot.waitUntil(Conditions.shellIsActive("Progress Information"));
		final SWTBotShell showViewShell = bot.shell("Progress Information");
		showViewShell.activate();
		showViewShell.close();
	}

	protected void closeMatchModeSelection() {
		bot.waitUntil(Conditions.shellIsActive("Match Mode Selection"));
		final SWTBotShell showViewShell = bot.shell("Match Mode Selection");
		showViewShell.activate();
		showViewShell.close();
	}

	protected SWTBotView openProjectExplorer() {
		bot.menu("Window").menu("Show View").menu("Other...").click();
		bot.waitUntilWidgetAppears(Conditions.shellIsActive("Show View"));
		final SWTBotShell showViewShell = bot.shell("Show View");
		showViewShell.activate();

		bot.tree().expandNode("General").getNode("Project Explorer").select();
		bot.button("OK").click();
		SWTBotView viewByTitle = bot.viewByTitle("Project Explorer");
		viewByTitle.setFocus();
		return viewByTitle;
	}

	public CompareBot createGITRepository(String project) {
		/*
		 * open the project explorer view
		 */
		SWTBotView projectExp = openProjectExplorer();
		SWTBotTree tree = projectExp.bot().tree();
		tree.select(project);
		ContextMenuHelper.clickContextMenu(tree, "Team", "Share Project...");
		bot.waitUntilWidgetAppears(Conditions.shellIsActive("Share Project"));
		final SWTBotShell shareShell = bot.shell("Share Project");
		shareShell.activate();
		shareShell.bot().table().getTableItem("Git").select().click();
		bot.button("Next >").click();
		bot.checkBox().select();
		bot.tree().getAllItems()[0].click().select();
		bot.button("Create Repository").click();
		bot.button("Finish").click();
		return this;

	}

	public void commit(String projectName, String... filenames) {
		SWTBotView projectExp = openProjectExplorer();
		SWTBotTree tree = projectExp.bot().tree();
		for (SWTBotTreeItem item : tree.getAllItems()) {
			if (item.getText().startsWith(projectName)) {
				item.expand();
			}
		}
		tree.select(filenames);
		ContextMenuHelper.clickContextMenu(tree, "Team", "Add to Index");

		// tree.select(projectName);
		ContextMenuHelper.clickContextMenu(tree, "Team", "Commit...");
		bot.waitUntilWidgetAppears(Conditions.shellIsActive("Commit Changes"));
		final SWTBotShell shareShell = bot.shell("Commit Changes");
		shareShell.activate();

		bot.styledText().setFocus();
		bot.styledText().setText("My Commit");

		bot.button("Commit").click();

	}

}
