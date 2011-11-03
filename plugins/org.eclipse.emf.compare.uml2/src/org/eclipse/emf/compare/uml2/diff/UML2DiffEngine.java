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
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.engine.GenericDiffEngine;
import org.eclipse.emf.compare.diff.engine.IMatchManager;
import org.eclipse.emf.compare.diff.engine.IMatchManager.MatchSide;
import org.eclipse.emf.compare.diff.engine.check.ReferencesCheck;
import org.eclipse.emf.compare.diff.metamodel.AbstractDiffExtension;
import org.eclipse.emf.compare.diff.metamodel.ConflictingDiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.uml2.diff.internal.extension.DiffExtensionFactoryRegistry;
import org.eclipse.emf.compare.uml2.diff.internal.extension.IDiffExtensionFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.CrossReferencer;

/**
 * A specific DiffEngine to compute differences in UML models.
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
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UML2DiffEngine extends GenericDiffEngine {

	/**
	 * UML2 extensions factories.
	 */
	private Set<IDiffExtensionFactory> uml2ExtensionFactories;

	@Override
	public DiffModel doDiff(MatchModel match, boolean threeWay) {
		final DiffModel ret = super.doDiff(match, threeWay);
		postProcess(ret);
		return ret;
	}

	@Override
	public DiffModel doDiffResourceSet(MatchModel match, boolean threeWay, CrossReferencer crossReferencer) {
		final DiffModel ret = super.doDiffResourceSet(match, threeWay, crossReferencer);
		postProcess(ret);
		return ret;
	}

	/**
	 * Executes a post-processing on the resulting {@link DiffModel} of the differences computing.
	 * 
	 * @param dg
	 *            The {@link DiffModel}
	 */
	void postProcess(DiffModel dg) {
		EcoreUtil.CrossReferencer diffModelCrossReferencer = new EcoreUtil.CrossReferencer(dg) {
			private static final long serialVersionUID = -7188045763674814697L;
			{
				crossReference(); // init map
			}
		};

		final Map<Class<? extends AbstractDiffExtension>, IDiffExtensionFactory> mapUml2ExtensionFactories = DiffExtensionFactoryRegistry
				.createExtensionFactories(this);
		uml2ExtensionFactories = new HashSet<IDiffExtensionFactory>(mapUml2ExtensionFactories.values());

		final TreeIterator<EObject> tit = dg.eAllContents();
		while (tit.hasNext()) {
			final EObject next = tit.next();
			if (next instanceof DiffElement) {
				applyManagedTypes((DiffElement)next, diffModelCrossReferencer);
			}
		}

		diffModelCrossReferencer = null;
		fillRequiredDifferences(dg, mapUml2ExtensionFactories);

	}

	/**
	 * Scan the {@link DiffModel} to fill the dependencies links ("requires") of {@link AbstractDiffExtension}
	 * s.
	 * 
	 * @param dg
	 *            The {@link DiffModel}.
	 * @param mapUml2ExtensionFactories
	 *            The map of the extensions factories.
	 */
	private void fillRequiredDifferences(DiffModel dg,
			final Map<Class<? extends AbstractDiffExtension>, IDiffExtensionFactory> mapUml2ExtensionFactories) {
		EcoreUtil.CrossReferencer diffModelCrossReferencer = new EcoreUtil.CrossReferencer(dg) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5034169206637698601L;

			{
				crossReference(); // init map
			}
		};
		final TreeIterator<EObject> tit = dg.eAllContents();
		while (tit.hasNext()) {
			final EObject next = tit.next();
			if (next instanceof AbstractDiffExtension) {
				fillRequiredDifferences(mapUml2ExtensionFactories, (AbstractDiffExtension)next,
						diffModelCrossReferencer);
			}
		}
	}

	/**
	 * Fill the dependency link ("requires") of the {@link AbstractDiffExtension}, delegating this treatment
	 * to the related factories.
	 * 
	 * @param mapUml2ExtensionFactories
	 *            The map of the extensions factories.
	 * @param diff
	 *            The difference extension to complete.
	 * @param crossReferencer
	 *            A Cross referencer.
	 */
	void fillRequiredDifferences(
			Map<Class<? extends AbstractDiffExtension>, IDiffExtensionFactory> mapUml2ExtensionFactories,
			AbstractDiffExtension diff, EcoreUtil.CrossReferencer crossReferencer) {
		final Class<?> classDiffElement = diff.eClass().getInstanceClass();
		final IDiffExtensionFactory diffFactory = mapUml2ExtensionFactories.get(classDiffElement);
		if (diffFactory != null) {
			diffFactory.fillRequiredDifferences(diff, crossReferencer);
		}
	}

	/**
	 * Creates the difference extensions in relation to the existing {@link DiffElement}s.
	 * 
	 * @param element
	 *            The input {@link DiffElement}.
	 * @param diffModelCrossReferencer
	 *            The cross referencer.
	 */
	void applyManagedTypes(DiffElement element, EcoreUtil.CrossReferencer diffModelCrossReferencer) {
		for (IDiffExtensionFactory factory : uml2ExtensionFactories) {
			if (factory.handles(element)) {
				final AbstractDiffExtension extension = factory.create(element, diffModelCrossReferencer);
				final DiffElement diffParent = factory.getParentDiff(element, diffModelCrossReferencer);
				if (element.isConflicting()) {
					ConflictingDiffElement conflictingDiffElement = null;
					if (element.eContainer() != null
							&& element.eContainer() instanceof ConflictingDiffElement) {
						conflictingDiffElement = (ConflictingDiffElement)element.eContainer();
					} else {
						conflictingDiffElement = DiffFactory.eINSTANCE.createConflictingDiffElement();
					}
					conflictingDiffElement.getSubDiffElements().add((DiffElement)extension);
					diffParent.getSubDiffElements().add((DiffElement)conflictingDiffElement);
				} else {
					diffParent.getSubDiffElements().add((DiffElement)extension);
				}
			}
		}
	}

	/**
	 * Get the model object from any other model object and the given and expected side.
	 * 
	 * @param from
	 *            The reference model object.
	 * @param side
	 *            The expected side.
	 * @return The model object from the specified side.
	 * @see use {@link UML2DiffEngine#getMatched(EObject, MatchSide)}
	 */
	@Deprecated
	public EObject getMatched(EObject from, int side) {
		return getMatchedEObject(from, side);

	}

	/**
	 * Get the model object from any other model object and the given and expected side.
	 * 
	 * @param from
	 *            The reference model object.
	 * @param side
	 *            The expected side.
	 * @return The model object from the specified side.
	 */
	public EObject getMatched(EObject from, MatchSide side) {
		return getMatchManager().getMatchedEObject(from, side);
	}

	/**
	 * Return the left or right matched EObject from the one given. More specifically, this will return the
	 * left matched element if the given {@link EObject} is the right one, or the right matched element if the
	 * given {@link EObject} is either the left or the origin one.
	 * 
	 * @see org.eclipse.emf.compare.diff.engine.IMatchManager#getMatchedEObject(EObject).
	 * @param from
	 *            The original {@link EObject}.
	 * @return The matched EObject.
	 */
	public EObject getMatched(EObject from) {
		return getMatchManager().getMatchedEObject(from);
	}

	@Deprecated
	public static int getRightSide() {
		return RIGHT_OBJECT;
	}

	@Deprecated
	public static int getLeftSide() {
		return LEFT_OBJECT;
	}

	@Deprecated
	public static int getAncestorSide() {
		return ANCESTOR_OBJECT;
	}

	@Override
	protected ReferencesCheck getReferencesChecker() {
		return new UML2ReferencesCheck(getMatchManager());
	}

	/**
	 * Extension of {@link ReferencesCheck} for UML2.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class UML2ReferencesCheck extends ReferencesCheck {

		/**
		 * Parameters.
		 */
		private static final String SUBSETS_OF_CONTAINMENT_PROPERTIES = "/org/eclipse/emf/compare/uml2/diff/internal/subsets.of.containment.properties"; //$NON-NLS-1$

		/**
		 * Properties for parameters.
		 */
		private static final Properties SUBSETS_OF_CONTAINMENT = new Properties();

		static {
			try {
				SUBSETS_OF_CONTAINMENT.load(UML2DiffEngine.class
						.getResourceAsStream(SUBSETS_OF_CONTAINMENT_PROPERTIES));
			} catch (IOException e) {
				// ignore
			}
		}

		/**
		 * Constructor.
		 * 
		 * @param matchManager
		 *            {@link IMatchManager}
		 */
		UML2ReferencesCheck(IMatchManager matchManager) {
			super(matchManager);
		}

		@Override
		protected boolean shouldBeIgnored(EReference reference) {
			final String fqn = fqn(reference);
			return super.shouldBeIgnored(reference) || SUBSETS_OF_CONTAINMENT.getProperty(fqn) != null;
		}

		/**
		 * Transforms the {@link EReference} to a string.
		 * 
		 * @param reference
		 *            The {@link EReference}
		 * @return The string.
		 */
		private String fqn(EReference reference) {
			final StringBuilder fqn = new StringBuilder(reference.getEContainingClass().getName());
			fqn.append('.').append(reference.getName());
			return fqn.toString();
		}
	}
}
