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
package org.eclipse.emf.compare.diagram.internal.matchs.provider.spec;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;
import org.eclipse.gmf.runtime.notation.provider.ViewItemProvider;
import org.eclipse.gmf.runtime.notation.util.NotationSwitch;

/**
 * This is the factory that is used to provide the interfaces needed to support Diagram compare merge viewer,
 * on matches.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramCompareItemProviderAdapterFactorySpec extends NotationItemProviderAdapterFactory implements RankedAdapterFactory {

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.gmf.runtime.notation.View}
	 * instances.
	 */
	protected ViewItemProvider viewItemProviderSpec;

	/**
	 * This keeps track of the one adapter used for all {@link org.eclipse.gmf.runtime.notation.Diagram}
	 * instances.
	 */
	protected ViewItemProvider diagramItemProviderSpec;

	/** The Specific switch to create adapters for ALL views and diagrams. */
	// CHECKSTYLE:OFF
	protected NotationSwitch modelSwitchSpec = new NotationSwitch() {
		// CHECKSTYLE:ON

		@Override
		public Object caseView(View object) {
			// For all instances of View
			if (viewItemProviderSpec == null) {
				viewItemProviderSpec = new ViewItemProviderSpec(
						DiagramCompareItemProviderAdapterFactorySpec.this);
			}
			return viewItemProviderSpec;
		}

		@Override
		public Object caseDiagram(Diagram object) {
			// For all instances of Diagram
			if (diagramItemProviderSpec == null) {
				diagramItemProviderSpec = new DiagramItemProviderSpec(
						DiagramCompareItemProviderAdapterFactorySpec.this);
			}
			return diagramItemProviderSpec;
		}

		@Override
		public Object defaultCase(EObject object) {
			// delegate to the default notational switch
			return modelSwitch.doSwitch(object);
		}
	};

	/** ranking of the factory. */
	private int ranking;

	/**
	 * Constructor calling super {@link #CompareItemProviderAdapterFactory()}.
	 */
	public DiagramCompareItemProviderAdapterFactorySpec() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.gmf.runtime.notation.util.NotationAdapterFactory#createAdapter(org.eclipse.emf.common.notify.Notifier)
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitchSpec.doSwitch((EObject)target);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactory#getRanking()
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.adapterfactory.RankedAdapterFactory#setRanking(int)
	 */
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

}
