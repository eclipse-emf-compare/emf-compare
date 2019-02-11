/*******************************************************************************
 * Copyright (c) 2014, 2018 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - bug 522422, 514079
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.contentmergeviewer.label;

import com.google.common.eventbus.Subscribe;

import java.util.EventObject;

import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerPane;
import org.eclipse.compare.IPropertyChangeNotifier;
import org.eclipse.compare.internal.Utilities;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.compare.command.ICompareCommandStack;
import org.eclipse.emf.compare.domain.ICompareEditingDomain;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ICompareEditingDomainChange;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * A dummy viewer that displays a single label.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class LabelContentViewer extends Viewer implements IPropertyChangeNotifier, CommandStackListener {

	/** The Control as returned by {@link #getControl()}. */
	private final Composite control;

	@SuppressWarnings("rawtypes") // Doesn't use generic type for back-ward compatibility
	private final ListenerList listenerList;

	private Object input;

	private final EMFCompareConfiguration configuration;

	private boolean dirty;

	/**
	 * Creates a new viewer and its controls.
	 * 
	 * @param parent
	 *            the parent of the {@link #getControl() control} of this viewer.
	 */
	@SuppressWarnings("rawtypes")
	public LabelContentViewer(Composite parent, String title, String message,
			EMFCompareConfiguration configuration) {
		this.configuration = configuration;
		control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		control.setLayout(layout);
		Label label = new Label(control, SWT.NONE);
		label.setText(message);
		label.setFont(JFaceResources.getBannerFont());
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		control.setData(CompareUI.COMPARE_VIEWER_TITLE, title);

		ToolBarManager toolBarManager = CompareViewerPane.getToolBarManager(parent);
		if (toolBarManager != null) {
			toolBarManager.removeAll();
		}

		listenerList = new ListenerList();

		editingDomainChange(null, configuration.getEditingDomain());

		configuration.getEventBus().register(this);

		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				handleDisposed();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setSelection(org.eclipse.jface.viewers.ISelection, boolean)
	 */
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#setInput(java.lang.Object)
	 */
	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#refresh()
	 */
	@Override
	public void refresh() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return StructuredSelection.EMPTY;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getInput()
	 */
	@Override
	public Object getInput() {
		return input;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	@Override
	public Control getControl() {
		return control;
	}

	@SuppressWarnings("unchecked")
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.add(listener);
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.remove(listener);
	}

	/**
	 * @param oldValue
	 * @param newValue
	 */
	@Subscribe
	public void editingDomainChange(ICompareEditingDomainChange event) {
		ICompareEditingDomain oldValue = event.getOldValue();
		ICompareEditingDomain newValue = event.getNewValue();
		editingDomainChange(oldValue, newValue);
	}

	public void editingDomainChange(ICompareEditingDomain oldValue, ICompareEditingDomain newValue) {
		if (oldValue != null) {
			ICompareCommandStack commandStack = oldValue.getCommandStack();
			commandStack.removeCommandStackListener(this);
		}
		if (newValue != oldValue) {
			if (newValue != null) {
				ICompareCommandStack commandStack = newValue.getCommandStack();
				commandStack.addCommandStackListener(this);
				setDirty(commandStack.isLeftSaveNeeded() || commandStack.isRightSaveNeeded());
			}
		}
	}

	protected void setDirty(boolean dirty) {
		if (this.dirty != dirty) {
			this.dirty = dirty;
			Utilities.firePropertyChange(listenerList, this, CompareEditorInput.DIRTY_STATE, null,
					Boolean.valueOf(dirty));
		}
	}

	public void commandStackChanged(EventObject event) {
		if (configuration.getEditingDomain() != null) {
			ICompareCommandStack commandStack = configuration.getEditingDomain().getCommandStack();
			setDirty(commandStack.isLeftSaveNeeded() || commandStack.isRightSaveNeeded());
		}
	}

	protected void handleDisposed() {
		configuration.getEventBus().unregister(this);
		configuration.disposeListeners();
		editingDomainChange(configuration.getEditingDomain(), null);
	}
}
