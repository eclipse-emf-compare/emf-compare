/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.diff;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateReference;
import org.eclipse.emf.compare.epatch.AbstractEpatchBuilder;
import org.eclipse.emf.compare.epatch.AssignmentValue;
import org.eclipse.emf.compare.epatch.CreatedObject;
import org.eclipse.emf.compare.epatch.Epatch;
import org.eclipse.emf.compare.epatch.ListAssignment;
import org.eclipse.emf.compare.epatch.NamedObject;
import org.eclipse.emf.compare.epatch.NamedResource;
import org.eclipse.emf.compare.epatch.ObjectRef;
import org.eclipse.emf.compare.epatch.SingleAssignment;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class DiffEpatchService extends AbstractEpatchBuilder {
	public static Epatch createEpatch(MatchModel match, DiffModel diff, String name) {
		DiffEpatchService dt = new DiffEpatchService(match, diff, name);
		return dt.convert();
	}

	protected DiffModel diff;

	protected MatchModel match;

	protected Map<EObject, Match2Elements> matchMap = new HashMap<EObject, Match2Elements>();

	protected NamedResource res;

	protected Set<EObject> unmatches = new HashSet<EObject>();

	protected DiffEpatchService(MatchModel match, DiffModel diff, String name) {
		this.match = match;
		this.diff = diff;
		this.epatch = createEpatch(name);
	}

	protected Epatch convert() {
		epatch.getResources().add(res = createResource());
		fillMatchMap(match.getMatchedElements());
		fillUnmatchSet(match.getUnmatchedElements());
		for (TreeIterator<EObject> i = diff.eAllContents(); i.hasNext();) {
			EObject o = i.next();

			// model elements
			if (o instanceof ModelElementChangeLeftTarget)
				handleEleChangeLeft((ModelElementChangeLeftTarget)o);
			else if (o instanceof ModelElementChangeRightTarget)
				handleEleChangeRight((ModelElementChangeRightTarget)o);
			else if (o instanceof MoveModelElement)
				handleEleMove((MoveModelElement)o);

			// attributes
			else if (o instanceof AttributeChangeLeftTarget)
				handleAttrChangeLeft((AttributeChangeLeftTarget)o);
			else if (o instanceof AttributeChangeRightTarget)
				handleAttrChangeRight((AttributeChangeRightTarget)o);
			else if (o instanceof UpdateAttribute)
				handleAttrUpdate((UpdateAttribute)o);

			// references
			else if (o instanceof ReferenceChangeRightTarget)
				handleRefChangeRight((ReferenceChangeRightTarget)o);
			else if (o instanceof ReferenceChangeLeftTarget)
				handleRefChangeLeft((ReferenceChangeLeftTarget)o);
			else if (o instanceof UpdateReference)
				handleRefUpdate((UpdateReference)o);

			// other
			else if (o instanceof DiffGroup)
				; // do nothing
			else
				throw new RuntimeException("Warning: Didn't handle " + o.eClass().getName() + ": " + o);

			// TODO: support moving of references... and more?
		}
		generateNames();
		sortLists();
		return epatch;
	}

	protected ObjectRef createObjectRef(EObject left, EObject right) {
		ObjectRef r = fc.createObjectRef();
		objMap.put(left, r);
		String lfrag = getFragment(left);
		String rfrag = getFragment(right);
		if (!lfrag.equals(rfrag)) {
			r.setRightFrag(rfrag);
			r.setRightRes(res);
		}
		r.setLeftRes(res);
		r.setLeftFrag(lfrag);
		epatch.getObjects().add(r);
		return r;
	}

	protected NamedResource createResource() {
		NamedResource r = fc.createNamedResource();
		// TODO: support multiple roots
		r.setLeftUri(diff.getLeftRoots().get(0).eResource().getURI().lastSegment());
		r.setRightUri(diff.getRightRoots().get(0).eResource().getURI().lastSegment());
		r.setName("res0");
		return r;
	}

	protected void doAdd(EObject left, EObject right, EStructuralFeature feat, Object val) {
		if (feat.isMany()) {
			if (ignoreFeature(feat))
				return;
			NamedObject o = getNamedObject(left, right);
			ListAssignment ass = getListAssignment(o, feat);
			EList<?> list = (EList<?>)right.eGet(feat);
			int index = list.indexOf(val);
			ass.getRightValues().add(getListAssignmentValue(feat, val, index));
		} else
			doSet(left, right, feat);
	}

	protected void doRemove(EObject left, EObject right, EStructuralFeature feat, Object value) {
		if (feat.isMany()) {
			if (ignoreFeature(feat))
				return;
			NamedObject o = getNamedObject(left, right);
			ListAssignment ass = getListAssignment(o, feat);
			EList<?> list = (EList<?>)left.eGet(feat);
			int index = list.indexOf(value);
			ass.getLeftValues().add(getListAssignmentValue(feat, value, index));
		} else
			doSet(left, right, feat);
	}

	protected void doSet(EObject left, EObject right, EStructuralFeature feat) {
		if (ignoreFeature(feat))
			return;
		NamedObject o = getNamedObject(left, right);
		SingleAssignment ass = getSingleAssignment(o, feat);

		Object lobj = left.eGet(feat);
		Object robj = right.eGet(feat);

		ass.setLeftValue(getAssignmentValue(feat, lobj));
		ass.setRightValue(getAssignmentValue(feat, robj));
	}

	protected void fillMatchMap(EList<MatchElement> elements) {
		for (MatchElement e : elements) {
			if (e instanceof Match2Elements) {
				Match2Elements e2 = (Match2Elements)e;
				matchMap.put(e2.getLeftElement(), e2);
				matchMap.put(e2.getRightElement(), e2);
			}
			fillMatchMap(e.getSubMatchElements());
		}
	}

	protected void fillUnmatchSet(EList<UnmatchElement> elements) {
		for (UnmatchElement e : elements) {
			unmatches.add(e.getElement());
			for (TreeIterator<EObject> i = e.getElement().eAllContents(); i.hasNext();)
				unmatches.add(i.next());
		}
	}

	@Override
	protected AssignmentValue getAssignmentValueEObject(EReference ref, EObject eobj) {
		AssignmentValue ass = fc.createAssignmentValue();

		// if (eobj.eIsProxy())
		// eobj = EcoreUtil.resolve(eobj, ref);
		// if (eobj.eIsProxy()) // FIXME: is there a better resource set available?
		// eobj = EcoreUtil.resolve(eobj, diff.getRightRoots().get(0));

		NamedObject no = getNamedObject(eobj);
		if (no != null) {
			if (ref.isContainment()) {
				if (no instanceof CreatedObject)
					ass.setNewObject((CreatedObject)no);
				else
					ass.setRefObject(no);
			} else {
				ass.setRefObject(no);
			}
		} else {
			ass.setImport(getImportRef(eobj));
			ass.setImpFrag(getFragment(eobj));
		}
		return ass;
	}

	protected NamedObject getNamedObject(EObject object) {
		NamedObject o = objMap.get(object);
		if (o != null)
			return o;

		Match2Elements m = matchMap.get(object);
		if (m != null) {
			if (object != m.getLeftElement() && (o = objMap.get(m.getLeftElement())) != null)
				return o;
			return createObjectRef(m.getLeftElement(), m.getRightElement());
		} else if (unmatches.contains(object))
			return createObjectNew(object);
		return o;
	}

	protected NamedObject getNamedObject(EObject left, EObject right) {
		NamedObject o = objMap.get(left);
		if (o == null)
			return createObjectRef(left, right);
		return o;
	}

	protected void handleAttrChangeLeft(AttributeChangeLeftTarget ele) {
		doRemove(ele.getLeftElement(), ele.getRightElement(), ele.getAttribute(), ele.getLeftTarget());
	}

	protected void handleAttrChangeRight(AttributeChangeRightTarget ele) {
		doAdd(ele.getLeftElement(), ele.getRightElement(), ele.getAttribute(), ele.getRightTarget());
	}

	protected void handleAttrUpdate(UpdateAttribute ele) {
		doSet(ele.getLeftElement(), ele.getRightElement(), ele.getAttribute());
	}

	protected void handleEleChangeLeft(ModelElementChangeLeftTarget ele) {
		EObject leftParent = ele.getLeftElement().eContainer();
		EReference ref = ele.getLeftElement().eContainmentFeature();
		doRemove(leftParent, ele.getRightParent(), ref, ele.getLeftElement());
	}

	protected void handleEleChangeRight(ModelElementChangeRightTarget ele) {
		EObject rightParent = ele.getRightElement().eContainer();
		EReference ref = ele.getRightElement().eContainmentFeature();
		doAdd(ele.getLeftParent(), rightParent, ref, ele.getRightElement());
	}

	protected void handleEleMove(MoveModelElement ele) {
		EReference remRef = ele.getRightElement().eContainmentFeature();
		EObject remLeft = matchMap.get(ele.getRightTarget()).getLeftElement();
		EObject remRight = ele.getRightTarget();
		doRemove(remLeft, remRight, remRef, ele.getLeftElement());

		EReference addRef = ele.getLeftElement().eContainmentFeature();
		EObject addLeft = ele.getLeftTarget();
		EObject addRight = matchMap.get(ele.getLeftTarget()).getRightElement();
		doAdd(addLeft, addRight, addRef, ele.getRightElement());
	}

	protected void handleRefChangeLeft(ReferenceChangeLeftTarget ele) {
		doRemove(ele.getLeftElement(), ele.getRightElement(), ele.getReference(), ele.getLeftTarget());
	}

	protected void handleRefChangeRight(ReferenceChangeRightTarget ele) {
		doAdd(ele.getLeftElement(), ele.getRightElement(), ele.getReference(), ele.getRightTarget());
	}

	protected void handleRefUpdate(UpdateReference ele) {
		doSet(ele.getLeftElement(), ele.getRightElement(), ele.getReference());
	}

}
