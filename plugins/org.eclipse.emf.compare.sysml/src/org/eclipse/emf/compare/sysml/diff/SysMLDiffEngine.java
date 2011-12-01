/*****************************************************************************
 * Copyright (c) 2011 Atos Origin.
 *
 *    
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Atos Origin - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.emf.compare.sysml.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.uml2.diff.UML2DiffEngine;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Diff engine for SysML specific engine.
 * 
 * @author Arthur Daussy <a href="mailto:arthur.daussy@atos.net">arthur.daussy@atos.net</a>
 * @since 1.3
 */
public class SysMLDiffEngine extends UML2DiffEngine {
	/**
	 * A set of all SysML Extension Factories.
	 */
	protected Set<IDiffExtensionFactory> sysMLExtensionFactories;

	/**
	 * Add SysML process {@inheritDoc}.
	 * 
	 * @see org.eclipse.emf.compare.uml2.diff.UML2DiffEngine#doDiff(org.eclipse.emf.compare.match.metamodel.MatchModel,
	 *      boolean)
	 */
	@Override
	public DiffModel doDiff(MatchModel match, boolean threeWay) {
		final DiffModel ret = super.doDiff(match, threeWay);
		postProcessSysML(ret);
		return ret;

	}

	/**
	 * Process specific action for SysML.
	 * 
	 * @param dg
	 *            {@link DiffModel}
	 */
	protected void postProcessSysML(DiffModel dg) {
		EcoreUtil.CrossReferencer diffModelCrossReferencerSysML = new EcoreUtil.CrossReferencer(dg) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 5249665166472289714L;

			{
				crossReference(); // init map
			}
		};
		sysMLExtensionFactories = DiffSysMLExtensionFactoryRegistry.createExtensionFactories(this,
				diffModelCrossReferencerSysML);

		final List<DiffElement> toBrowse = new ArrayList<DiffElement>();
		for (final TreeIterator<EObject> tit = dg.eAllContents(); tit.hasNext(); ) {
			final EObject next = tit.next();
			if (next instanceof DiffElement) {
				toBrowse.add((DiffElement)next);
			}
		}

		for (DiffElement diffElement : toBrowse) {
			applyManagedTypesSysML(diffElement, diffModelCrossReferencerSysML);
		}
	}

	/**
	 * {@link org.eclipse.emf.compare.uml2.diff.UML2DiffEngine.applyManagedTypes(DiffElement)}.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 * @param crossReferencer
	 *            The cross referencer.
	 */
	void applyManagedTypesSysML(DiffElement element, EcoreUtil.CrossReferencer crossReferencer) {
		for (IDiffExtensionFactory factory : sysMLExtensionFactories) {
			if (factory.handles(element)) {
				final AbstractDiffExtension extension = factory.create(element, crossReferencer);
				final DiffElement diffParent = factory.getParentDiff(element, crossReferencer);
				diffParent.getSubDiffElements().add((DiffElement)extension);
			}
		}
	}

}
