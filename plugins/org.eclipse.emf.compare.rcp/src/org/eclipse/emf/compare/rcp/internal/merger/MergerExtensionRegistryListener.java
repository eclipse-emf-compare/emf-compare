/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.rcp.internal.merger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.compare.merge.IMerger;
import org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener;
import org.eclipse.emf.compare.rcp.internal.EMFCompareRCPMessages;

/**
 * Listener for contributions to the merger extension.
 */
public class MergerExtensionRegistryListener extends AbstractRegistryEventListener {

	/** TAG_MERGER. */
	private static final String TAG_MERGER = "merger"; //$NON-NLS-1$

	/** ATT_CLASS. */
	private static final String ATT_CLASS = "class"; //$NON-NLS-1$

	/** ATT_RANKING. */
	private static final String ATT_RANKING = "ranking"; //$NON-NLS-1$

	private final IMerger.Registry mergerRegistry;

	/**
	 * Constructor.
	 * 
	 * @param pluginID
	 *            The plugin id.
	 * @param extensionPointID
	 *            The extension point id.
	 * @param mergerRegistry
	 */
	public MergerExtensionRegistryListener(String pluginID, String extensionPointID, ILog log,
			IMerger.Registry mergerRegistry) {
		super(pluginID, extensionPointID, log);
		this.mergerRegistry = mergerRegistry;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.ide.utils.AbstractRegistryEventListener#readElement(org.eclipse.core.runtime.IConfigurationElement,
	 *      org.eclipse.emf.compare.ide.utils.AbstractRegistryEventListener.Action)
	 */
	@Override
	protected boolean readElement(IConfigurationElement element, Action b) {
		if (element.getName().equals(TAG_MERGER)) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
			} else if (element.getAttribute(ATT_RANKING) == null) {
				String rankingStr = element.getAttribute(ATT_RANKING);
				try {
					Integer.parseInt(rankingStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element, EMFCompareRCPMessages.getString(
							"malformed.extension.attribute", //$NON-NLS-1$
							ATT_RANKING));
				}
				logMissingAttribute(element, ATT_RANKING);
			} else {
				switch (b) {
					case ADD:
						try {
							IMerger merger = (IMerger)element.createExecutableExtension(ATT_CLASS);
							merger.setRanking(Integer.parseInt(element.getAttribute(ATT_RANKING)));
							IMerger previous = mergerRegistry.add(merger);
							if (previous != null) {
								log(IStatus.WARNING, element, EMFCompareRCPMessages.getString(
										"duplicate.merger", merger.getClass().getName())); //$NON-NLS-1$
							}
						} catch (CoreException e) {
							log(IStatus.ERROR, element, e.getMessage());
						}
						break;
					case REMOVE:
						mergerRegistry.remove(element.getAttribute(ATT_CLASS));
						break;
					default:
						break;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#validateExtensionElement(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean validateExtensionElement(IConfigurationElement element) {
		final boolean ret;
		if (TAG_MERGER.equals(element.getName())) {
			if (element.getAttribute(ATT_CLASS) == null) {
				logMissingAttribute(element, ATT_CLASS);
				ret = false;
			} else if (element.getAttribute(ATT_RANKING) == null) {
				String rankingStr = element.getAttribute(ATT_RANKING);
				try {
					Integer.parseInt(rankingStr);
				} catch (NumberFormatException nfe) {
					log(IStatus.ERROR, element, EMFCompareRCPMessages.getString(
							"malformed.extension.attribute", //$NON-NLS-1$
							ATT_RANKING));
				}
				logMissingAttribute(element, ATT_RANKING);
				ret = false;
			} else {
				ret = true;
			}
		} else {
			ret = false;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#addedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean addedValid(IConfigurationElement element) {
		try {
			IMerger merger = (IMerger)element.createExecutableExtension(ATT_CLASS);
			merger.setRanking(Integer.parseInt(element.getAttribute(ATT_RANKING)));
			IMerger previous = mergerRegistry.add(merger);
			if (previous != null) {
				log(IStatus.WARNING, element, EMFCompareRCPMessages.getString(
						"duplicate.extension", merger.getClass().getName())); //$NON-NLS-1$
			}
		} catch (CoreException e) {
			log(IStatus.ERROR, element, e.getMessage());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.extension.AbstractRegistryEventListener#removedValid(org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	protected boolean removedValid(IConfigurationElement element) {
		mergerRegistry.remove(element.getAttribute(ATT_CLASS));
		return true;
	}

}
