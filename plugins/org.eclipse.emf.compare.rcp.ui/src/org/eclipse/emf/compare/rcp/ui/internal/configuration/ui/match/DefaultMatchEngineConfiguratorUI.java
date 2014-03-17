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
package org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.match;

import com.google.common.base.Throwables;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.emf.compare.rcp.internal.match.DefaultRCPMatchEngineFactory;
import org.eclipse.emf.compare.rcp.ui.internal.EMFCompareRCPUIMessages;
import org.eclipse.emf.compare.rcp.ui.internal.configuration.ui.AbstractConfigurationUI;
import org.eclipse.emf.compare.utils.UseIdentifiers;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.prefs.Preferences;

/**
 * {@link AbstractConfigurationUI} for {@link DefaultRCPMatchEngineFactory}.
 * 
 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
 */
public class DefaultMatchEngineConfiguratorUI extends AbstractConfigurationUI {

	private Button whenAvailableButton;

	private Button neverButton;

	private Button onlyButton;

	private DataHolder dataHolder;

	public DefaultMatchEngineConfiguratorUI(Composite parent, int style, Preferences pref) {
		super(parent, style, pref);
	}

	@Override
	public void createContent() {
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		this.setLayout(rowLayout);
		Label text = new Label(this, SWT.NONE);
		text.setText(EMFCompareRCPUIMessages
				.getString("DefaultMatchEngineConfiguratorUI.useIdentifier.label")); //$NON-NLS-1$
		whenAvailableButton = new Button(this, SWT.RADIO);
		whenAvailableButton.setText(EMFCompareRCPUIMessages
				.getString("DefaultMatchEngineConfiguratorUI.whenAvailable.label")); //$NON-NLS-1$

		onlyButton = new Button(this, SWT.RADIO);
		onlyButton.setText(EMFCompareRCPUIMessages.getString("DefaultMatchEngineConfiguratorUI.only.label")); //$NON-NLS-1$

		neverButton = new Button(this, SWT.RADIO);
		neverButton
				.setText(EMFCompareRCPUIMessages.getString("DefaultMatchEngineConfiguratorUI.never.label")); //$NON-NLS-1$

		UseIdentifiers defaultValue = DefaultRCPMatchEngineFactory.getUseIdentifierValue(getPreference());
		dataHolder = new DataHolder(defaultValue);

		bindData();

	}

	/**
	 * Bind radio button with data object
	 */
	private void bindData() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableValue whenAvailableBtnSelection = SWTObservables.observeSelection(whenAvailableButton);
		IObservableValue onlyButtonSelection = SWTObservables.observeSelection(onlyButton);
		IObservableValue neverButtonSelection = SWTObservables.observeSelection(neverButton);
		SelectObservableValue featureRepoPolicyObservable = new SelectObservableValue(DataHolder.class);
		featureRepoPolicyObservable.addOption(UseIdentifiers.WHEN_AVAILABLE, whenAvailableBtnSelection);
		featureRepoPolicyObservable.addOption(UseIdentifiers.ONLY, onlyButtonSelection);
		featureRepoPolicyObservable.addOption(UseIdentifiers.NEVER, neverButtonSelection);
		ctx.bindValue(featureRepoPolicyObservable, PojoObservables.observeValue(dataHolder, "value")); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeConfiguration() {
		UseIdentifiers value = dataHolder.getValue();
		if (value != DefaultRCPMatchEngineFactory.DEFAULT_USE_IDENTIFIER_ATRIBUTE) {
			getPreference().put(DefaultRCPMatchEngineFactory.USE_IDENTIFIER_ATTR, value.toString());
		} else {
			getPreference().remove(DefaultRCPMatchEngineFactory.USE_IDENTIFIER_ATTR);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetDefault() {
		// Set default
		if (whenAvailableButton != null && !whenAvailableButton.isDisposed()) {
			whenAvailableButton
					.setSelection(DefaultRCPMatchEngineFactory.DEFAULT_USE_IDENTIFIER_ATRIBUTE == UseIdentifiers.WHEN_AVAILABLE);
		}
		if (onlyButton != null && !onlyButton.isDisposed()) {
			onlyButton
					.setSelection(DefaultRCPMatchEngineFactory.DEFAULT_USE_IDENTIFIER_ATRIBUTE == UseIdentifiers.ONLY);
		}
		if (neverButton != null && !neverButton.isDisposed()) {
			neverButton
					.setSelection(DefaultRCPMatchEngineFactory.DEFAULT_USE_IDENTIFIER_ATRIBUTE == UseIdentifiers.NEVER);
		}
		dataHolder.setValue(DefaultRCPMatchEngineFactory.DEFAULT_USE_IDENTIFIER_ATRIBUTE);
		try {
			getPreference().remove(DefaultRCPMatchEngineFactory.USE_IDENTIFIER_ATTR);
		} catch (IllegalStateException e) {
			Throwables.propagate(e);
		}
	}

	/**
	 * POJO holding data.
	 * 
	 * @author <a href="mailto:arthur.daussy@obeo.fr">Arthur Daussy</a>
	 */
	private static final class DataHolder {

		private UseIdentifiers value;

		public DataHolder(UseIdentifiers value) {
			super();
			this.value = value;
		}

		public UseIdentifiers getValue() {
			return value;
		}

		public void setValue(UseIdentifiers value) {
			this.value = value;
		}

	}

}
