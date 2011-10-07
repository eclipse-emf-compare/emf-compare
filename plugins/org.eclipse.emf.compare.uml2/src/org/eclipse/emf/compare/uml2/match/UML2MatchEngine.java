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
package org.eclipse.emf.compare.uml2.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.match.engine.GenericMatchScopeProvider;
import org.eclipse.emf.compare.match.engine.IMatchScope;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.uml2.uml.Element;

/**
 * A specific IMatchEngine that ignores {@link EObject}s created to store UML stereotypes properties (a.k.a.
 * tagged values).
 * <p>
 * However, those properties are taken into account during the match phase of their base element.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class UML2MatchEngine extends GenericMatchEngine {

	@Override
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> optionMap) throws InterruptedException {
		final Map<String, Object> uml2OptionsMap = new HashMap<String, Object>(optionMap);
		uml2OptionsMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftResource,
				rightResource));
		return super.resourceMatch(leftResource, rightResource, uml2OptionsMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchEngine#contentMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public MatchModel contentMatch(EObject leftObject, EObject rightObject, EObject ancestor,
			Map<String, Object> optionMap) {
		final Map<String, Object> uml2OptionsMap = new HashMap<String, Object>(optionMap);
		uml2OptionsMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftObject,
				rightObject, ancestor));
		return super.contentMatch(leftObject, rightObject, ancestor, uml2OptionsMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchEngine#contentMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public MatchModel contentMatch(EObject leftObject, EObject rightObject, Map<String, Object> optionMap) {
		final Map<String, Object> uml2OptionsMap = new HashMap<String, Object>(optionMap);
		uml2OptionsMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftObject,
				rightObject));
		return super.contentMatch(leftObject, rightObject, uml2OptionsMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, EObject ancestor,
			Map<String, Object> optionMap) throws InterruptedException {
		final Map<String, Object> uml2OptionsMap = new HashMap<String, Object>(optionMap);
		uml2OptionsMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftRoot,
				rightRoot));
		return super.modelMatch(leftRoot, rightRoot, ancestor, uml2OptionsMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchEngine#modelMatch(org.eclipse.emf.ecore.EObject,
	 *      org.eclipse.emf.ecore.EObject, java.util.Map)
	 */
	@Override
	public MatchModel modelMatch(EObject leftRoot, EObject rightRoot, Map<String, Object> optionMap)
			throws InterruptedException {
		final Map<String, Object> uml2OptionsMap = new HashMap<String, Object>(optionMap);
		uml2OptionsMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftRoot,
				rightRoot));
		return super.modelMatch(leftRoot, rightRoot, uml2OptionsMap);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.match.engine.GenericMatchEngine#resourceMatch(org.eclipse.emf.ecore.resource.Resource,
	 *      org.eclipse.emf.ecore.resource.Resource, org.eclipse.emf.ecore.resource.Resource, java.util.Map)
	 */
	@Override
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource, Resource ancestorResource,
			Map<String, Object> optionMap) throws InterruptedException {
		final Map<String, Object> uml2OptionsMap = new HashMap<String, Object>(optionMap);
		uml2OptionsMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftResource,
				rightResource, ancestorResource));
		return super.resourceMatch(leftResource, rightResource, ancestorResource, uml2OptionsMap);
	}

	@Override
	protected List<EObject> getScopeInternalContents(EObject eObject, IMatchScope scope) {
		final List<EObject> result = new ArrayList<EObject>(super.getScopeInternalContents(eObject, scope));
		// All is in the scope. See Bug 351593.
		// result.addAll(getStereotypeApplications(eObject));
		return result;
	}

	/**
	 * Get stereotype applications on the given model object.
	 * 
	 * @param eObject
	 *            The model object.
	 * @return List of stereotype applications.
	 */
	private List<EObject> getStereotypeApplications(EObject eObject) {
		if (eObject instanceof Element) {
			return ((Element)eObject).getStereotypeApplications();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * UML2 match scope provider.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static class UML2MatchScopeProvider extends GenericMatchScopeProvider {

		/**
		 * Constructor.
		 * 
		 * @param leftObject
		 *            The left model object.
		 * @param rightObject
		 *            The right model object.
		 * @param ancestorObject
		 *            The ancestor model object.
		 */
		public UML2MatchScopeProvider(EObject leftObject, EObject rightObject, EObject ancestorObject) {
			super(leftObject, rightObject, ancestorObject);
		}

		/**
		 * Constructor.
		 * 
		 * @param leftObject
		 *            The left model object.
		 * @param rightObject
		 *            The right model object.
		 */
		public UML2MatchScopeProvider(EObject leftObject, EObject rightObject) {
			super(leftObject, rightObject);
		}

		/**
		 * Constructor.
		 * 
		 * @param leftResource
		 *            The left resource.
		 * @param rightResource
		 *            The right resource.
		 * @param ancestorResource
		 *            The ancestor resource.
		 */
		public UML2MatchScopeProvider(Resource leftResource, Resource rightResource, Resource ancestorResource) {
			super(leftResource, rightResource, ancestorResource);
		}

		/**
		 * Constructor.
		 * 
		 * @param leftResourceSet
		 *            The left resource set.
		 * @param rightResourceSet
		 *            The right resource set.
		 * @param ancestorResourceSet
		 *            The ancestor resource set.
		 */
		public UML2MatchScopeProvider(ResourceSet leftResourceSet, ResourceSet rightResourceSet,
				ResourceSet ancestorResourceSet) {
			super(leftResourceSet, rightResourceSet, ancestorResourceSet);
		}

		/**
		 * Constructor.
		 * 
		 * @param leftResourceSet
		 *            The left resource set.
		 * @param rightResourceSet
		 *            The right resource set.
		 */
		public UML2MatchScopeProvider(ResourceSet leftResourceSet, ResourceSet rightResourceSet) {
			super(leftResourceSet, rightResourceSet);
		}

		/**
		 * Constructor.
		 * 
		 * @param leftResource
		 *            The left resource.
		 * @param rightResource
		 *            The right resource.
		 */
		public UML2MatchScopeProvider(Resource leftResource, Resource rightResource) {
			super(leftResource, rightResource);
		}

		@Override
		public IMatchScope getLeftScope() {
			return new UML2MatchScope(super.getLeftScope());
		}

		@Override
		public IMatchScope getRightScope() {
			return new UML2MatchScope(super.getRightScope());
		}

		@Override
		public IMatchScope getAncestorScope() {
			return new UML2MatchScope(super.getAncestorScope());
		}

	}

	/**
	 * The UML2 match scope.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class UML2MatchScope implements IMatchScope {

		/**
		 * parent match scope.
		 */
		private final IMatchScope fParentScope;

		/**
		 * Constructor.
		 * 
		 * @param scope
		 *            The match scope.
		 */
		private UML2MatchScope(IMatchScope scope) {
			fParentScope = scope;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.match.engine.IMatchScope#isInScope(org.eclipse.emf.ecore.EObject)
		 */
		public boolean isInScope(EObject eObject) {
			// All is in the scope. See Bug 351593.
			// boolean isStereotypeApplication = UMLUtil.getStereotype(eObject) != null;
			return fParentScope.isInScope(eObject)/* && !isStereotypeApplication */;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.match.engine.IMatchScope#isInScope(org.eclipse.emf.ecore.resource.Resource)
		 */
		public boolean isInScope(Resource resource) {
			return fParentScope.isInScope(resource);
		}

	}

}
