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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.Disposable;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.provider.NotationItemProviderAdapterFactory;
import org.eclipse.gmf.runtime.notation.util.NotationSwitch;

/**
 * This is the factory that is used to provide the interfaces needed to support Diagram compare merge viewer,
 * on matches.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class DiagramCompareItemProviderAdapterFactorySpec extends NotationItemProviderAdapterFactory {

	/**
	 * This keeps track of all the item providers created, so that they can be {@link #dispose disposed}.
	 */
	protected Disposable disposable = new Disposable();

	/** The Specific switch to create adapters for ALL views and diagrams. */
	// CHECKSTYLE:OFF
	protected NotationSwitch modelSwitchSpec = new NotationSwitch() {
		// CHECKSTYLE:ON

		@Override
		public Object caseView(View object) {
			return new ViewItemProviderSpec(DiagramCompareItemProviderAdapterFactorySpec.this);
		}

		@Override
		public Object caseDiagram(Diagram object) {
			return new DiagramItemProviderSpec(DiagramCompareItemProviderAdapterFactorySpec.this);
		}

		@Override
		public Object defaultCase(EObject object) {
			if (object instanceof Style) {
				return new StyleItemProviderSpec(DiagramCompareItemProviderAdapterFactorySpec.this);
			}

			// delegate to the default notational switch
			return modelSwitch.doSwitch(object);
		}
	};

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
	 * @see NotationItemProviderAdapterFactory#dispose()
	 */
	@Override
	public void dispose() {
		disposable.dispose();
		super.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see NotationItemProviderAdapterFactory#associate(Adapter adapter, Notifier target)
	 */
	@Override
	protected void associate(Adapter adapter, Notifier target) {
		super.associate(adapter, target);
		if (adapter != null) {
			disposable.add(adapter);
		}
	}
}
