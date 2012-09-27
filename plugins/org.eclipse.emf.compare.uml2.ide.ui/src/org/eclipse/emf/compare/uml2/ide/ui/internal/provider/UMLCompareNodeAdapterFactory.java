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
package org.eclipse.emf.compare.uml2.ide.ui.internal.provider;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;

import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.compare.structuremergeviewer.IDiffContainer;
import org.eclipse.compare.structuremergeviewer.IDiffElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UMLCompareNodeAdapterFactory extends UMLCompareAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 */
	protected final IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 */
	protected final Collection<Object> supportedTypes = newArrayList();

	/**
	 * Creates an {@link ComposeableAdapterFactory} with the following supported types:
	 * <ul>
	 * <li>{@link IDiffElement}</li>,
	 * <li>{@link IDiffContainer}</li>,
	 * <li>{@link ITypedElement}</li>,
	 * <li>{@link ICompareInput}</li>.
	 * </ul>
	 * 
	 * @param grouper
	 *            This will be used by the comparison adapter to group differences together.
	 */
	public UMLCompareNodeAdapterFactory() {
		supportedTypes.add(IDiffElement.class);
		supportedTypes.add(IDiffContainer.class);
		supportedTypes.add(ITypedElement.class);
		supportedTypes.add(ICompareInput.class);
	}

	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * 
	 * @param notifier
	 *            the notifier to adapt
	 * @param type
	 *            unused
	 * @return a previously existing associated adapter, a new associated adapter if possible, or
	 *         <code>null</code> otherwise.
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterFactoryImpl#adapt(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * 
	 * @return the root adapter factory that contains this factory.
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		ComposeableAdapterFactory ret = this;
		if (parentAdapterFactory != null) {
			ret = parentAdapterFactory.getRootAdapterFactory();
		}
		return ret;
	}

	/**
	 * This sets the direct parent adapter factory into which this factory is composed.
	 * 
	 * @param parentAdapterFactory
	 *            the direct parent adapter factory into which this factory is composed
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createAssociationChangeAdapter()
	 */
	@Override
	public Adapter createAssociationChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createExecutionSpecificationChangeAdapter()
	 */
	@Override
	public Adapter createExecutionSpecificationChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createProfileApplicationChangeAdapter()
	 */
	@Override
	public Adapter createProfileApplicationChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createDependencyChangeAdapter()
	 */
	@Override
	public Adapter createDependencyChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createInterfaceRealizationChangeAdapter()
	 */
	@Override
	public Adapter createInterfaceRealizationChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createSubstitutionChangeAdapter()
	 */
	@Override
	public Adapter createSubstitutionChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createExtendChangeAdapter()
	 */
	@Override
	public Adapter createExtendChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createIncludeChangeAdapter()
	 */
	@Override
	public Adapter createIncludeChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createGeneralizationSetChangeAdapter()
	 */
	@Override
	public Adapter createGeneralizationSetChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createIntervalConstraintChangeAdapter()
	 */
	@Override
	public Adapter createIntervalConstraintChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createMessageChangeAdapter()
	 */
	@Override
	public Adapter createMessageChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createStereotypePropertyChangeAdapter()
	 */
	@Override
	public Adapter createStereotypePropertyChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createStereotypeApplicationChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeApplicationChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createStereotypeReferenceChangeAdapter()
	 */
	@Override
	public Adapter createStereotypeReferenceChangeAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.util.UMLCompareAdapterFactory#createUMLDiffAdapter()
	 */
	@Override
	public Adapter createUMLDiffAdapter() {
		return new UMLDiffNode(getRootAdapterFactory());
	}

	/**
	 * This adds a listener.
	 * 
	 * @param notifyChangedListener
	 *            the listener to add.
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * 
	 * @param notifyChangedListener
	 *            the listener to remove.
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * 
	 * @param notification
	 *            the notification to fire.
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.provider.IDisposable#dispose()
	 */
	public void dispose() {
	}
}
