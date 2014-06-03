/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.ide.ui.internal.structuremergeviewer.CompareInputAdapter;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.edit.tree.TreeNode;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class TreeNodeCompareInput extends CompareInputAdapter implements INotifyChangedListener, IChangeNotifier {

	/**
	 * This is used to implement {@link IChangeNotifier}.
	 */
	protected IChangeNotifier changeNotifier;

	protected IChangeNotifier delegateItemProvider;

	/**
	 * Constructor.
	 * 
	 * @param adapterFactory
	 *            the given adapter factory.
	 */
	public TreeNodeCompareInput(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public void setTarget(Notifier target) {
		super.setTarget(target);
		if (target == null) {
			if (delegateItemProvider != null) {
				delegateItemProvider.removeListener(this);
				delegateItemProvider = null;
			}
		} else if (target instanceof TreeNode) {
			TreeNode treeNode = (TreeNode)target;
			delegateItemProvider = (IChangeNotifier)(getRootAdapterFactory().adapt(treeNode.getData(),
					IItemLabelProvider.class));
			delegateItemProvider.addListener(this);
		} else {
			throw new IllegalArgumentException(target + " should have been a TreeNode"); //$NON-NLS-1$
		}
	}

	/**
	 * This handles notification by calling {@link #fireNotifyChanged(Notification) fireNotifyChanged}. This
	 * will also be called by the {@link #delegateItemProvider} when it normally fires a notification to its
	 * adapter factory; the listener method is hooked up in {@link #setTarget setTarget}. Notifications are
	 * wrapped to look like they originate from the target.
	 */
	@Override
	public void notifyChanged(final Notification notification) {
		fireNotifyChanged(ViewerNotification.wrapNotification(notification, this));
	}

	/**
	 * This convenience method converts the arguments into an appropriate update call on the viewer. The event
	 * type is a value from the static constants in {@link org.eclipse.emf.common.notify.Notifier}.
	 */
	public void fireNotifyChanged(Notification notification) {
		if (changeNotifier != null) {
			changeNotifier.fireNotifyChanged(notification);
		}

		if (getAdapterFactory() instanceof IChangeNotifier) {
			IChangeNotifier localChangeNotifier = (IChangeNotifier)getAdapterFactory();
			localChangeNotifier.fireNotifyChanged(notification);
		}
	}

	public void addListener(INotifyChangedListener listener) {
		if (changeNotifier == null) {
			changeNotifier = new ChangeNotifier();
		}
		changeNotifier.addListener(listener);
	}

	public void removeListener(INotifyChangedListener listener) {
		if (changeNotifier != null) {
			changeNotifier.removeListener(listener);
		}
	}

	@Override
	public void dispose() {
		if (delegateItemProvider != null) {
			delegateItemProvider.removeListener(this);
		}
		super.dispose();
	}

}
