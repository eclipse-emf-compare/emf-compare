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
package org.eclipse.emf.compare;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.utils.IEqualityHelper;

/**
 * Object used to configure the engines of EMFCompare (match, diff, requirement, equivalence, merge).
 * <p>
 * The default configuration as used by EMF Compare can be obtained through
 * {@link EMFCompare#createDefaultConfiguration()}.
 * </p>
 * <p>
 * Note that configuration instances can be automatically created through the use and customization of
 * {@link EMFCompare#newComparator(org.eclipse.emf.compare.scope.IComparisonScope)}.
 * </p>
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
@Deprecated
public class EMFCompareConfiguration extends AdapterImpl {
	/** The monitor to report progress during the math/diff operation. */
	private final Monitor fMonitor;

	/**
	 * Constructs a configuration to be used by EMF Compare.
	 * 
	 * @param monitor
	 *            The progress monitor that should be used to report comparison progress to the user.
	 * @param helper
	 *            The equality helper that should ne used by the comparison engine.
	 */
	@Deprecated
	public EMFCompareConfiguration(Monitor monitor, IEqualityHelper helper) {
		this.fMonitor = checkNotNull(monitor);
	}

	/**
	 * Returns the {@link Monitor} to report progress.
	 * 
	 * @return the Monitor (never null).
	 */
	@Deprecated
	public Monitor getMonitor() {
		return fMonitor;
	}

	/**
	 * Returns the {@link IEqualityHelper}.
	 * 
	 * @return the EqualityHelper (never null).
	 * @see Comparison#getEqualityHelper()
	 */
	@Deprecated
	public IEqualityHelper getEqualityHelper() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.notify.impl.AdapterImpl#isAdapterForType(java.lang.Object)
	 */
	@Override
	public boolean isAdapterForType(Object type) {
		return type == EMFCompareConfiguration.class;
	}
}
