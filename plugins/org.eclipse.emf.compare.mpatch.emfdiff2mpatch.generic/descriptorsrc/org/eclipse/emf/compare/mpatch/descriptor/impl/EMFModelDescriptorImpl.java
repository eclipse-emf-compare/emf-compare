/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *
 * $Id: EMFModelDescriptorImpl.java,v 1.2 2010/09/14 09:45:46 pkonemann Exp $
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.descriptor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.descriptor.DescriptorPackage;
import org.eclipse.emf.compare.mpatch.descriptor.EMFModelDescriptor;
import org.eclipse.emf.compare.mpatch.emfdiff2mpatch.generic.Activator;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EMF Model Descriptor</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getCrossReferences <em>
 * Cross References</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getAllCrossReferences <em>
 * All Cross References</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getSelfReference <em>Self
 * Reference</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getAllSelfReferences <em>
 * All Self References</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getSubModelDescriptors
 * <em>Sub Model Descriptors</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getDescriptorUris <em>
 * Descriptor Uris</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getAttributes <em>
 * Attributes</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getSubDescriptors <em>Sub
 * Descriptors</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getReferences <em>
 * References</em>}</li>
 * <li>{@link org.eclipse.emf.compare.mpatch.descriptor.impl.EMFModelDescriptorImpl#getDescriptorUri <em>
 * Descriptor Uri</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class EMFModelDescriptorImpl extends EObjectImpl implements EMFModelDescriptor {

	/**
	 * The cached value of the '{@link #getSelfReference() <em>Self Reference</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSelfReference()
	 * @generated
	 * @ordered
	 */
	protected IElementReference selfReference;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected EClass type;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' map. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EMap<EAttribute, EList<Object>> attributes;

	/**
	 * The cached value of the '{@link #getSubDescriptors() <em>Sub Descriptors</em>}' map. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getSubDescriptors()
	 * @generated
	 * @ordered
	 */
	protected EMap<EReference, EList<EMFModelDescriptor>> subDescriptors;

	/**
	 * The cached value of the '{@link #getReferences() <em>References</em>}' map. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getReferences()
	 * @generated
	 * @ordered
	 */
	protected EMap<EReference, EList<IElementReference>> references;

	/**
	 * The default value of the '{@link #getDescriptorUri() <em>Descriptor Uri</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescriptorUri()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTOR_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescriptorUri() <em>Descriptor Uri</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDescriptorUri()
	 * @generated
	 * @ordered
	 */
	protected String descriptorUri = DESCRIPTOR_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected EMFModelDescriptorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DescriptorPackage.Literals.EMF_MODEL_DESCRIPTOR;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<IElementReference> getCrossReferences() {
		final EList<IElementReference> references = new EObjectEList<IElementReference>(
				IElementReference.class, this, DescriptorPackage.EMF_MODEL_DESCRIPTOR__ALL_CROSS_REFERENCES);
		for (EList<IElementReference> list : getReferences().values()) {
			references.addAll(list);
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<IElementReference> getAllCrossReferences() {
		EList<IElementReference> references = getCrossReferences();
		for (EList<EMFModelDescriptor> list : getSubDescriptors().values()) {
			for (EMFModelDescriptor emfModelDescriptor : list) {
				references.addAll(emfModelDescriptor.getAllCrossReferences());
			}
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IElementReference getSelfReference() {
		return selfReference;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSelfReference(IElementReference newSelfReference, NotificationChain msgs) {
		IElementReference oldSelfReference = selfReference;
		selfReference = newSelfReference;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE, oldSelfReference,
					newSelfReference);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setSelfReference(IElementReference newSelfReference) {
		if (newSelfReference != selfReference) {
			NotificationChain msgs = null;
			if (selfReference != null) {
				msgs = ((InternalEObject)selfReference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
						- DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE, null, msgs);
			}
			if (newSelfReference != null) {
				msgs = ((InternalEObject)newSelfReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
						- DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE, null, msgs);
			}
			msgs = basicSetSelfReference(newSelfReference, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE, newSelfReference,
					newSelfReference));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<String> getDescriptorUris() {
		final EList<String> uris = new EDataTypeUniqueEList<String>(String.class, this,
				DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URIS);
		uris.add(getDescriptorUri());
		for (EList<EMFModelDescriptor> list : getSubDescriptors().values()) {
			for (EMFModelDescriptor emfModelDescriptor : list) {
				uris.addAll(emfModelDescriptor.getDescriptorUris());
			}
		}
		return uris;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<IElementReference> getAllSelfReferences() {
		final EList<IElementReference> selves = new EObjectEList<IElementReference>(IElementReference.class,
				this, DescriptorPackage.EMF_MODEL_DESCRIPTOR__ALL_SELF_REFERENCES);
		final Queue<EMFModelDescriptor> queue = new LinkedList<EMFModelDescriptor>();
		queue.add(this);
		while (!queue.isEmpty()) {
			final EMFModelDescriptor descriptor = queue.poll();
			selves.add(descriptor.getSelfReference());
			for (final Collection<EMFModelDescriptor> subModelDescriptors : descriptor.getSubDescriptors()
					.values()) {
				queue.addAll(subModelDescriptors);
			}
		}
		return selves;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<IModelDescriptor> getSubModelDescriptors() {
		final EList<IModelDescriptor> descriptors = new EObjectEList<IModelDescriptor>(
				IModelDescriptor.class, this, DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS);
		for (List<EMFModelDescriptor> list : getSubDescriptors().values()) {
			descriptors.addAll(list);
		}
		return descriptors;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (EClass)eResolveProxy(oldType);
			if (type != oldType) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE,
							DescriptorPackage.EMF_MODEL_DESCRIPTOR__TYPE, oldType, type));
				}
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EClass basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setType(EClass newType) {
		EClass oldType = type;
		type = newType;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					DescriptorPackage.EMF_MODEL_DESCRIPTOR__TYPE, oldType, type));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<EAttribute, EList<Object>> getAttributes() {
		if (attributes == null) {
			attributes = new EcoreEMap<EAttribute, EList<Object>>(
					DescriptorPackage.Literals.EATTRIBUTE_TO_OBJECT_MAP, EAttributeToObjectMapImpl.class,
					this, DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<EReference, EList<EMFModelDescriptor>> getSubDescriptors() {
		if (subDescriptors == null) {
			subDescriptors = new EcoreEMap<EReference, EList<EMFModelDescriptor>>(
					DescriptorPackage.Literals.EREFERENCE_TO_DESCRIPTOR_MAP,
					EReferenceToDescriptorMapImpl.class, this,
					DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS);
		}
		return subDescriptors;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<EReference, EList<IElementReference>> getReferences() {
		if (references == null) {
			references = new EcoreEMap<EReference, EList<IElementReference>>(
					DescriptorPackage.Literals.EREFERENCE_TO_ELEMENT_REFERENCE_MAP,
					EReferenceToElementReferenceMapImpl.class, this,
					DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES);
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDescriptorUri() {
		return descriptorUri;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDescriptorUri(String newDescriptorUri) {
		String oldDescriptorUri = descriptorUri;
		descriptorUri = newDescriptorUri;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET,
					DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI, oldDescriptorUri, descriptorUri));
		}
	}

	/**
	 * Internal operation to create the object this model descriptor describes. This is a recursive call over
	 * {@link EMFModelDescriptor#getSubDescriptors()}.
	 * 
	 * @param appliedSubDescriptors
	 *            Each call should add itself and its created object in this map.
	 * @param submodelElements
	 *            Each call should add itself and its created object in this map.
	 * @return The element this method has created.
	 */
	private EObject applyObject(final Map<EMFModelDescriptor, EObject> appliedSubDescriptors,
			final EMap<EObject, IModelDescriptor> submodelElements) {
		// create plain object
		final EObject object = getType().getEPackage().getEFactoryInstance().create(getType());
		appliedSubDescriptors.put(this, object);
		submodelElements.put(object, this);

		// do this recursively and add sub elements
		for (final EReference containment : getSubDescriptors().keySet()) {
			for (final EMFModelDescriptor subDescriptor : getSubDescriptors().get(containment)) {
				final EObject subElement = ((EMFModelDescriptorImpl)subDescriptor).applyObject(
						appliedSubDescriptors, submodelElements);
				if (!ExtEcoreUtils.setStructuralFeature(object, containment, subElement)) {
					throw new IllegalArgumentException(
							"Something is wrong here because we could not add a child as defined in the model descriptor!");
				}
			}
		}
		return object;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<IElementReference> applyCrossReferences(EObject createdObject,
			EMap<IElementReference, EList<EObject>> resolvedCrossReferences) {
		final EList<IElementReference> result = new BasicEList<IElementReference>();

		// restore all cross-references
		for (final EReference reference : getReferences().keySet()) {
			for (final IElementReference symRef : getReferences().get(reference)) {

				// try to resolve the symbolic reference
				try {
					// get the resolutions
					final EList<EObject> resolvedElements = resolvedCrossReferences.get(symRef);

					// set the object(s), if available
					if (resolvedElements == null || resolvedElements.isEmpty()) {
						result.add(symRef);
					} else if (!ExtEcoreUtils
							.setStructuralFeature(createdObject, reference, resolvedElements)) {
						result.add(symRef);
					}
				} catch (Exception e) {
					// some serious error handling in case of exception!
					Activator.getDefault().logError("Cross reference restoring failed!", e);
					result.add(symRef);
				}
			}
		}
		return result;
	}

	/**
	 * Restore all attributes of the given object.
	 * 
	 * @param object
	 *            An object which was created by this descriptor.
	 * @return A list of attributes which <b>failed</b> to restore.
	 */
	private List<EAttribute> applyAttributes(EObject object) {
		ArrayList<EAttribute> result = new ArrayList<EAttribute>();

		// restore all attribute values
		for (final EAttribute attribute : getAttributes().keySet()) {
			final Object value = getAttributes().get(attribute);

			// lets set this attribute
			if (!ExtEcoreUtils.setStructuralFeature(object, attribute, value)) {
				result.add(attribute);
			}
		}
		return result;
	}

	/**
	 * Recursively restore all attributes to the given objects.
	 * 
	 * @param object
	 *            The root object of a submodel for which we are a descriptor for.
	 * @param appliedSubDescriptors
	 *            A mapping of all sub-elements and their respective descriptors of the sub-model given in
	 *            <code>object</code>.
	 * @return A list of attributes which <b>failed</b> to restore.
	 */
	private List<EAttribute> applyAttributes(EObject object,
			Map<EMFModelDescriptor, EObject> appliedSubDescriptors) {
		List<EAttribute> result = applyAttributes(object);
		for (EMFModelDescriptor subDescriptor : appliedSubDescriptors.keySet()) {
			result.addAll(((EMFModelDescriptorImpl)subDescriptor).applyAttributes(appliedSubDescriptors
					.get(subDescriptor)));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EMap<EObject, IModelDescriptor> applyStructure(EObject parentModelElement, EReference containment) {
		// this is the critical part now ;-)

		// 1. instantiate the element we are a descriptor for and store structure
		final Map<EMFModelDescriptor, EObject> appliedSubDescriptors = new LinkedHashMap<EMFModelDescriptor, EObject>();
		final EMap<EObject, IModelDescriptor> submodelElements = new BasicEMap<EObject, IModelDescriptor>();
		final EObject createdElement = applyObject(appliedSubDescriptors, submodelElements);

		// 2. set all attributes
		if (applyAttributes(createdElement, appliedSubDescriptors).isEmpty()) {

			// 3. that was successful :-) lets add the element!
			if (ExtEcoreUtils.setStructuralFeature(parentModelElement, containment, createdElement)) {
				return submodelElements;
			}
		}

		// something went wrong here!
		System.out
				.println("EMFModelDescriptor.apply was not successful! We should really add more error handling here!");
		Activator
				.getDefault()
				.logError(
						"EMFModelDescriptor.apply was not successful! We should really add more error handling here!");
		return null;
	}

	/**
	 * <!-- begin-user-doc --> Check whether two model descriptors describe the same model element.
	 * Cross-references, sub-elements, and attributes will be checked. <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public boolean describesEqual(IModelDescriptor other) {

		// obvious checks..
		if (other == null || !(other instanceof EMFModelDescriptor)) {
			return false;
		}
		final EMFModelDescriptor other2 = (EMFModelDescriptor)other;
		if (getType() == null || !getType().equals(other2.getType())) {
			return false;
		}
		if (getReferences().size() != other2.getReferences().size()) {
			return false;
		}
		if (getAttributes().size() != other2.getAttributes().size()) {
			return false;
		}
		if (getSubDescriptors().size() != other2.getSubDescriptors().size()) {
			return false;
		}

		// check all cross-references
		for (EReference ref : getReferences().keySet()) {
			if (getReferences().get(ref).size() != other2.getReferences().get(ref).size()) {
				return false;
			}
			outer: for (IElementReference crossRef1 : getReferences().get(ref)) {
				for (IElementReference crossRef2 : other2.getReferences().get(ref)) {
					if (crossRef1 != null && crossRef1.resolvesEqual(crossRef2)) {
						continue outer;
					}
				}
				return false; // no counterpart for crossRef1 found!
			}
		}

		// check all attributes!
		for (EAttribute attr : getAttributes().keySet()) {
			if (getAttributes().get(attr).size() != other2.getAttributes().get(attr).size()) {
				return false;
			}
			outer: for (Object value1 : getAttributes().get(attr)) {
				for (Object value2 : other2.getAttributes().get(attr)) {
					if (value1 != null && value1.equals(value2)) {
						continue outer;
					}
				}
				return false; // no counterpart for crossRef1 found!
			}
		}

		// check all sub-model descriptors
		for (EReference ref : getSubDescriptors().keySet()) {
			if (getSubDescriptors().get(ref).size() != other2.getSubDescriptors().get(ref).size()) {
				return false;
			}
			outer: for (IModelDescriptor subMd1 : getSubDescriptors().get(ref)) {
				/*
				 * These are expensive checks! But what should we do?! there is no qualifier for model
				 * descriptors and, in fact, they are not sorted!
				 */
				for (IModelDescriptor subMd2 : other2.getSubDescriptors().get(ref)) {
					if (subMd1.describesEqual(subMd2)) {
						continue outer;
					}
				}
				return false; // no counterpart for subMd1 found!
			}
		}

		return true;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EMap<EObject, IModelDescriptor> isDescriptorFor(EObject element, boolean checkAttributes) {
		final EMap<EObject, IModelDescriptor> descriptorMap = new BasicEMap<EObject, IModelDescriptor>();
		if (isDescriptorFor(element, descriptorMap, checkAttributes)) {
			return descriptorMap;
		}
		return null;
	}

	private boolean isDescriptorFor(final EObject element,
			final EMap<EObject, IModelDescriptor> descriptorMap, boolean checkAttributes) {
		if (!element.eClass().equals(getType())) {
			return false;
		}
		if (checkAttributes && !checkAttributes(element)) {
			return false;
		}

		// check all sub descriptors!
		for (EReference subReference : getSubDescriptors().keySet()) {
			final List<?> children;
			if (subReference.isMany()) {
				children = (List<?>)element.eGet(subReference);
			} else {
				final Object child = element.eGet(subReference);
				children = child == null ? null : Collections.singletonList(child);
			}
			final List<EMFModelDescriptor> subDescriptors = new ArrayList<EMFModelDescriptor>(
					getSubDescriptors().get(subReference));

			// quick checks for emptyness
			if (subDescriptors.isEmpty() && (children == null || children.isEmpty())) {
				continue;
			}
			if (subDescriptors.isEmpty() ^ (children == null || children.isEmpty())) {
				return false;
			}
			if (children == null || children.size() != subDescriptors.size()) {
				return false;
			}

			// that gets ugly for many children! because we have to do n^2 check!!!
			children: for (Object child : children) {
				for (int i = 0; i < subDescriptors.size(); i++) {
					final EMFModelDescriptorImpl subDescriptor = (EMFModelDescriptorImpl)subDescriptors
							.get(i);

					// expensive recursive call
					if (subDescriptor.isDescriptorFor((EObject)child, descriptorMap, checkAttributes)) {
						subDescriptors.remove(i); // remove it to avoid false duplicate matches
						continue children;
					}
				}
				return false; // no descriptor found for the current child!
			}
		}
		descriptorMap.put(element, this);
		return true;
	}

	private boolean checkAttributes(final EObject element) {
		final Set<EAttribute> checkedAttributes = new HashSet<EAttribute>();
		for (EAttribute eAttribute : getAttributes().keySet()) {
			checkedAttributes.add(eAttribute);
			final EList<Object> describedValue = getAttributes().get(eAttribute);
			final Object actualValue = element.eGet(eAttribute);

			// common checks
			if (actualValue == null && (describedValue == null || describedValue.isEmpty())) {
				continue;
			}
			if (actualValue == null ^ (describedValue == null || describedValue.isEmpty())) {
				return false;
			}

			if (eAttribute.isMany()) {

				// check all values!
				final List<?> actualList = (List<?>)actualValue;
				if (actualList == null || describedValue == null
						|| actualList.size() != describedValue.size()) {
					return false;
				}
				for (Object object : actualList) {
					if (!describedValue.contains(object)) {
						return false;
					}
				}
			} else {

				// simple check of primitive values
				if (actualValue == null && (describedValue == null || describedValue.isEmpty())) {
					continue;
				}
				if (describedValue != null && actualValue != null && describedValue.size() == 1
						&& actualValue.equals(describedValue.get(0))) {
					continue;
				}
				return false;
			}
		}

		// all attributes covered?
		final List<EAttribute> toCheck = new ArrayList<EAttribute>(element.eClass().getEAllAttributes());
		toCheck.removeAll(checkedAttributes);
		for (EAttribute eAttribute : toCheck) {
			if (!MPatchUtil.isRelevantFeature(eAttribute)) {
				continue;
			}

			// all not yet checked attributes must the default value
			final Object value = element.eGet(eAttribute);
			final Object defaultValue = eAttribute.getDefaultValue();
			if (eAttribute.isMany()) {
				if (value == null || ((List<?>)value).size() == 0) { // both null or empty?
					if (defaultValue != null && defaultValue instanceof List<?>
							&& !((List<?>)defaultValue).isEmpty()) {
						return false;
					}
				} else if (defaultValue instanceof List<?>) { // actual comparison
					final List<?> defaultValues = (List<?>)defaultValue;
					final List<?> actualValues = (List<?>)value;
					if (defaultValues.size() != actualValues.size()) {
						return false;
					}
					// check for inclusion, both ways!
					for (Object object : actualValues) {
						if (!defaultValues.contains(object)) {
							return false;
						}
					}
					for (Object object : defaultValues) {
						if (!actualValues.contains(object)) {
							return false;
						}
					}
				}

			} else {
				if (value == null) { // default value must be null, too
					if (defaultValue != null) {
						return false;
					}
				} else if (!value.equals(defaultValue)) { // actual equality check
					return false;
				}
			}
		}

		return true; // all seems to be ok :-)
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE:
				return basicSetSelfReference(null, msgs);
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES:
				return ((InternalEList<?>)getAttributes()).basicRemove(otherEnd, msgs);
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS:
				return ((InternalEList<?>)getSubDescriptors()).basicRemove(otherEnd, msgs);
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__CROSS_REFERENCES:
				return getCrossReferences();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ALL_CROSS_REFERENCES:
				return getAllCrossReferences();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE:
				return getSelfReference();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ALL_SELF_REFERENCES:
				return getAllSelfReferences();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS:
				return getSubModelDescriptors();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URIS:
				return getDescriptorUris();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__TYPE:
				if (resolve) {
					return getType();
				}
				return basicGetType();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES:
				if (coreType) {
					return getAttributes();
				} else {
					return getAttributes().map();
				}
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS:
				if (coreType) {
					return getSubDescriptors();
				} else {
					return getSubDescriptors().map();
				}
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES:
				if (coreType) {
					return getReferences();
				} else {
					return getReferences().map();
				}
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI:
				return getDescriptorUri();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE:
				setSelfReference((IElementReference)newValue);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__TYPE:
				setType((EClass)newValue);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES:
				((EStructuralFeature.Setting)getAttributes()).set(newValue);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS:
				((EStructuralFeature.Setting)getSubDescriptors()).set(newValue);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES:
				((EStructuralFeature.Setting)getReferences()).set(newValue);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI:
				setDescriptorUri((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE:
				setSelfReference((IElementReference)null);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__TYPE:
				setType((EClass)null);
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES:
				getAttributes().clear();
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS:
				getSubDescriptors().clear();
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES:
				getReferences().clear();
				return;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI:
				setDescriptorUri(DESCRIPTOR_URI_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__CROSS_REFERENCES:
				return !getCrossReferences().isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ALL_CROSS_REFERENCES:
				return !getAllCrossReferences().isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SELF_REFERENCE:
				return selfReference != null;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ALL_SELF_REFERENCES:
				return !getAllSelfReferences().isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_MODEL_DESCRIPTORS:
				return !getSubModelDescriptors().isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URIS:
				return !getDescriptorUris().isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__TYPE:
				return type != null;
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__ATTRIBUTES:
				return attributes != null && !attributes.isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__SUB_DESCRIPTORS:
				return subDescriptors != null && !subDescriptors.isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__REFERENCES:
				return references != null && !references.isEmpty();
			case DescriptorPackage.EMF_MODEL_DESCRIPTOR__DESCRIPTOR_URI:
				return DESCRIPTOR_URI_EDEFAULT == null ? descriptorUri != null : !DESCRIPTOR_URI_EDEFAULT
						.equals(descriptorUri);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (descriptorUri: ");
		result.append(descriptorUri);
		result.append(')');
		return result.toString();
	}

} // EMFModelDescriptorImpl
