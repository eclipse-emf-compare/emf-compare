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
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.utils.EqualityHelper;

/**
 * Object used to configure the engines of EMFCompare (match, diff, requirement, equivalence, merge).
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class EMFCompareConfiguration extends AdapterImpl {

	/**
	 * The monitor to report progress during the math/diff operation.
	 */
	private/* final */Monitor fMonitor;

	/**
	 * The equality helper used to compare object during match & merge process.
	 */
	private/* final */EqualityHelper fEqualityHelper;

	/**
	 * The strategy used for matching, using IDs or not.
	 */
	private USE_IDS shouldUseIDs = USE_IDS.WHEN_AVAILABLE;

	/**
	 * Default visibility constructor (only called by {@link Builder#build()}).
	 */
	/* package */EMFCompareConfiguration() {
		// no instantiation outside of this package
	}

	/**
	 * Configuration values for the match strategy.
	 */
	public enum USE_IDS {
		/**
		 * Only use id, do not fall back on content based matching if no id is found. This means any element
		 * not having an ID will not be matched.
		 */
		ONLY,
		/**
		 * Use ID when available on an element, if not available use the content matching strategy.
		 */
		WHEN_AVAILABLE,
		/**
		 * Never use IDs, always use the content matching strategy. That's useful for instance when you want
		 * to compare two results of a transformation which have arbitrary IDs.
		 */
		NEVER
	}

	/**
	 * Returns the {@link Monitor} to report progress.
	 * 
	 * @return the Monitor (never null).
	 */
	public Monitor getMonitor() {
		return fMonitor;
	}

	/**
	 * Returns the {@link EqualityHelper}.
	 * 
	 * @return the EqualityHelper (never null).
	 */
	public EqualityHelper getEqualityHelper() {
		return fEqualityHelper;
	}

	/**
	 * return the matching strategy to use regarding IDs.
	 * 
	 * @return the matching strategy to use regarding IDs.
	 */
	public USE_IDS matchByID() {
		return shouldUseIDs;
	}

	/**
	 * Returns a new Builder to construct an EMFCompareConfiguration.
	 * 
	 * @return the new Builder instance.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A builder object following the builder pattern.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	public static class Builder {

		/**
		 * The monitor to report progress.
		 */
		private Monitor fMonitor;

		/**
		 * The equality helper.
		 */
		private EqualityHelper fEqualityHelper;

		/**
		 * Matching strategy kept by the configuration builder.
		 */
		private USE_IDS shouldUSEID;

		/**
		 * Package private constructor only called by {@link EMFCompareConfiguration#builder()}.
		 */
		/* package */Builder() {

		}

		/**
		 * Configure the configuration object to use the given monitor.
		 * 
		 * @param monitor
		 *            The monitor.
		 * @return This same builder to allow chained call.
		 * @throws NullPointerException
		 *             if monitor is null.
		 */
		public Builder setMonitor(Monitor monitor) {
			checkNotNull(monitor);
			this.fMonitor = monitor;
			return this;
		}

		/**
		 * Configure the configuration object to use the given equalityHelper.
		 * 
		 * @param equalityHelper
		 *            the EqualityHelper.
		 * @return This same builder to allow chained call.
		 * @throws NullPointerException
		 *             if monitor is null.
		 */
		public Builder setEqualityHelper(EqualityHelper equalityHelper) {
			checkNotNull(equalityHelper);
			this.fEqualityHelper = equalityHelper;
			return this;
		}

		/**
		 * Specify the matching strategy regarding IDs.
		 * 
		 * @param strategy
		 *            the strategy to use.
		 * @return This same builder to allow chained call.
		 */
		public Builder shouldUseID(USE_IDS strategy) {
			checkNotNull(strategy);
			this.shouldUSEID = strategy;
			return this;
		}

		/**
		 * Compile all set options to create a new {@link EMFCompareConfiguration}.
		 * 
		 * @return a new configuration object.
		 */
		public EMFCompareConfiguration build() {
			EMFCompareConfiguration configuration = new EMFCompareConfiguration();

			if (fMonitor == null) {
				fMonitor = new BasicMonitor();
			}
			configuration.fMonitor = fMonitor;

			if (fEqualityHelper == null) {
				fEqualityHelper = new EqualityHelper();
			}
			configuration.fEqualityHelper = fEqualityHelper;
			if (shouldUSEID != null) {
				configuration.shouldUseIDs = shouldUSEID;
			}
			return configuration;
		}
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
