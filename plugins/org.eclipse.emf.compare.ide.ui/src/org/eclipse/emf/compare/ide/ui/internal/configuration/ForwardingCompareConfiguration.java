/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.configuration;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.ICompareContainer;
import org.eclipse.compare.ICompareInputLabelProvider;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Image;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public abstract class ForwardingCompareConfiguration extends CompareConfiguration {

	private final ListenerList fListeners;

	private final IPropertyChangeListener delegateProperyChangeListener;

	public ForwardingCompareConfiguration() {
		fListeners = new ListenerList();

		// we have to listen to the delegate to avoid leaking the delegate through event.getSource in fired
		// events.
		delegateProperyChangeListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				fireChange(event.getProperty(), event.getOldValue(), event.getNewValue());
			}
		};
	}

	protected abstract CompareConfiguration delegate();

	@Override
	public IPreferenceStore getPreferenceStore() {
		return delegate().getPreferenceStore();
	}

	@Override
	public Image getImage(int kind) {
		return delegate().getImage(kind);
	}

	@Override
	public Image getImage(Image base, int kind) {
		return delegate().getImage(base, kind);
	}

	@Override
	public void dispose() {
		delegate().removePropertyChangeListener(delegateProperyChangeListener);
		delegate().dispose();
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		fListeners.add(listener);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		fListeners.remove(listener);
	}

	/**
	 * Fires a <code>PropertyChangeEvent</code> to registered listeners.
	 * 
	 * @param propertyName
	 *            the name of the property that has changed
	 * @param oldValue
	 *            the property's old value
	 * @param newValue
	 *            the property's new value
	 */
	protected final void fireChange(String propertyName, Object oldValue, Object newValue) {
		PropertyChangeEvent event = null;
		Object[] listeners = fListeners.getListeners();
		if (listeners != null) {
			for (int i = 0; i < listeners.length; i++) {
				IPropertyChangeListener l = (IPropertyChangeListener)listeners[i];
				if (event == null) {
					event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
				}
				l.propertyChange(event);
			}
		}
	}

	@Override
	public void setProperty(String key, Object newValue) {
		if (delegate() == null) {
			// ignore the set of properties from super <init> as we are always delegating to #delegate().
		} else {
			delegate().setProperty(key, newValue);
		}
	}

	@Override
	public Object getProperty(String key) {
		return delegate().getProperty(key);
	}

	@Override
	public void setAncestorLabel(String label) {
		delegate().setAncestorLabel(label);
	}

	@Override
	public String getAncestorLabel(Object element) {
		return delegate().getAncestorLabel(element);
	}

	@Override
	public void setAncestorImage(Image image) {
		delegate().setAncestorImage(image);
	}

	@Override
	public Image getAncestorImage(Object element) {
		return delegate().getAncestorImage(element);
	}

	@Override
	public void setLeftEditable(boolean editable) {
		delegate().setLeftEditable(editable);
	}

	@Override
	public boolean isLeftEditable() {
		return delegate().isLeftEditable();
	}

	@Override
	public void setLeftLabel(String label) {
		delegate().setLeftLabel(label);
	}

	@Override
	public String getLeftLabel(Object element) {
		return delegate().getLeftLabel(element);
	}

	@Override
	public void setLeftImage(Image image) {
		delegate().setLeftImage(image);
	}

	@Override
	public Image getLeftImage(Object element) {
		return delegate().getLeftImage(element);
	}

	@Override
	public void setRightEditable(boolean editable) {
		delegate().setRightEditable(editable);
	}

	@Override
	public boolean isRightEditable() {
		return delegate().isRightEditable();
	}

	@Override
	public void setRightLabel(String label) {
		delegate().setRightLabel(label);
	}

	@Override
	public String getRightLabel(Object element) {
		return delegate().getRightLabel(element);
	}

	@Override
	public void setRightImage(Image image) {
		delegate().setRightImage(image);
	}

	@Override
	public Image getRightImage(Object element) {
		return delegate().getRightImage(element);
	}

	@Override
	public ICompareContainer getContainer() {
		return delegate().getContainer();
	}

	@Override
	public void setContainer(ICompareContainer container) {
		delegate().setContainer(container);
	}

	@Override
	public ICompareInputLabelProvider getLabelProvider() {
		return delegate().getLabelProvider();
	}

	@Override
	public void setLabelProvider(ICompareInput input, ICompareInputLabelProvider labelProvider) {
		delegate().setLabelProvider(input, labelProvider);
	}

	@Override
	public void setDefaultLabelProvider(ICompareInputLabelProvider labelProvider) {
		delegate().setDefaultLabelProvider(labelProvider);
	}

	@Override
	public void setChangeIgnored(int kind, boolean ignored) {
		delegate().setChangeIgnored(kind, ignored);
	}

	@Override
	public boolean isChangeIgnored(int kind) {
		return delegate().isChangeIgnored(kind);
	}

}
