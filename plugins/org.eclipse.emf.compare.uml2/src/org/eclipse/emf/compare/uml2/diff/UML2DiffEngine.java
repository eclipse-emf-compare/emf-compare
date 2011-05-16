/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.diff;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.engine.GenericDiffEngine;
import org.eclipse.emf.compare.diff.engine.IDiffEngine;
import org.eclipse.emf.compare.diff.engine.check.ReferencesCheck;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.uml2.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

/**
 * A specific {@link IDiffEngine} to compute differences in UML models.
 * <p>
 * In addition to references ignored in the {@link GenericDiffEngine}, it ignores subsets of containment
 * references. The list is stored in the property files <code>
 * /org.eclipse.emf.compare.uml2/src/org/eclipse/emf/compare/uml2/diff/internal/subsets.of.containment
 * .properties
 * </code>
 * <p>
 * It also apply a postprocessing after the diff of the {@link GenericDiffEngine}. This post processing
 * browses a set of extension factory and call them on element it can handle in order to create a specific
 * diff model.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikaël Barbero</a>
 */
public class UML2DiffEngine extends GenericDiffEngine {

	private Set<IDiffExtensionFactory> uml2ExtensionFactories;

	@Override
	protected DiffGroup doDiffThreeWay(MatchModel match) {
		DiffGroup ret = super.doDiffThreeWay(match);
		postProcess(ret);
		return ret;
	}

	@Override
	protected DiffGroup doDiffTwoWay(MatchModel match) {
		DiffGroup ret = super.doDiffTwoWay(match);
		postProcess(ret);
		return ret;
	}

	void postProcess(DiffGroup dg) {
		uml2ExtensionFactories = DiffExtensionFactoryRegistry.createExtensionFactories(this);

		for (TreeIterator<EObject> tit = dg.eAllContents(); tit.hasNext();) {
			EObject next = tit.next();
			if (next instanceof DiffElement) {
				applyManagedTypes((DiffElement)next);
			}
		}
	}

	void applyManagedTypes(DiffElement element) {
		for (IDiffExtensionFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				AbstractDiffExtension extension = factory.create(element);
				DiffElement diffParent = factory.getParentDiff(element);
				diffParent.getSubDiffElements().add((DiffElement)extension);
			}
		}
	}

	public EObject getMatched(EObject from, int side) {
		return getMatchedEObject(from, side);

	}

	public static int getRightSide() {
		return RIGHT_OBJECT;
	}

	public static int getLeftSide() {
		return LEFT_OBJECT;
	}

	public static int getAncestorSide() {
		return ANCESTOR_OBJECT;
	}

	@Override
	protected ReferencesCheck getReferencesChecker() {
		return new UML2ReferencesCheck(matchCrossReferencer);
	}

	private static final class UML2ReferencesCheck extends ReferencesCheck {

		private static final String SUBSETS_OF_CONTAINMENT_PROPERTIES = "/org/eclipse/emf/compare/uml2/diff/internal/subsets.of.containment.properties"; //$NON-NLS-1$

		private static final Properties subsetsOfContainment = new Properties();

		static {
			try {
				subsetsOfContainment.load(UML2DiffEngine.class
						.getResourceAsStream(SUBSETS_OF_CONTAINMENT_PROPERTIES));
			} catch (IOException e) {
			}
		}

		private UML2ReferencesCheck(CrossReferencer referencer) {
			super(referencer);
		}

		@Override
		protected boolean shouldBeIgnored(EReference reference) {
			String fqn = fqn(reference);
			return super.shouldBeIgnored(reference) || subsetsOfContainment.getProperty(fqn) != null;
		}

		private String fqn(EReference reference) {
			StringBuilder fqn = new StringBuilder(reference.getEContainingClass().getName());
			fqn.append('.').append(reference.getName());
			return fqn.toString();
		}
	}
}
