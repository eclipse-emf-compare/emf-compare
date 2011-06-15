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
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.engine.DefaultMatchScopeProvider;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.compare.match.engine.IMatchEngine;
import org.eclipse.emf.compare.match.engine.IMatchScope;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * A specific {@link IMatchEngine} that ignores {@link EObject}s created to store UML stereotypes properties
 * (a.k.a. tagged values).
 * <p>
 * However, those properties are taken into account during the match phase of their base element.
 * 
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikaël Barbero</a>
 */
public class UML2MatchEngine extends GenericMatchEngine {

	@Override
	public MatchModel resourceMatch(Resource leftResource, Resource rightResource,
			Map<String, Object> optionMap) throws InterruptedException {
		optionMap.put(MatchOptions.OPTION_MATCH_SCOPE_PROVIDER, new UML2MatchScopeProvider(leftResource,
				rightResource));
		return super.resourceMatch(leftResource, rightResource, optionMap);
	}

	@Override
	protected List<EObject> getScopeInternalContents(EObject eObject, IMatchScope scope) {
		List<EObject> result = new ArrayList<EObject>(super.getScopeInternalContents(eObject, scope));
		result.addAll(getStereotypeApplications(eObject));
		return result;
	}

	private List<EObject> getStereotypeApplications(EObject eObject) {
		if (eObject instanceof Element) {
			return ((Element)eObject).getStereotypeApplications();
		} else {
			return Collections.emptyList();
		}
	}

	private static class UML2MatchScopeProvider extends DefaultMatchScopeProvider {

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

	private static class UML2MatchScope implements IMatchScope {

		private final IMatchScope fParentScope;

		private UML2MatchScope(IMatchScope scope) {
			fParentScope = scope;
		}

		public boolean isInScope(EObject eObject) {
			return fParentScope.isInScope(eObject) && UMLUtil.getStereotype(eObject) == null;
		}

		public boolean isInScope(Resource resource) {
			return fParentScope.isInScope(resource);
		}

	}

}
