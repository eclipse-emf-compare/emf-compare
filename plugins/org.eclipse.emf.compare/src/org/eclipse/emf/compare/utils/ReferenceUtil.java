package org.eclipse.emf.compare.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

/**
 * This utility class holds methods that will be used by the diff and merge processes. TODO: Maybe useless.
 * 
 * @author <a href="mailto:cedric.notot@obeo.fr">Cedric Notot</a>
 */
public final class ReferenceUtil {
	/**
	 * Utility classes don't need a default constructor.
	 */
	private ReferenceUtil() {
		// Hides default constructor
	}

	/**
	 * Initialize a cross referencer between business model objects and differences for a given
	 * <code>comparison</code>.
	 * 
	 * @param comparison
	 *            The comparison.
	 * @return The cross referencer.
	 */
	public static EcoreUtil.CrossReferencer initializeCrossReferencer(Comparison comparison) {
		EcoreUtil.CrossReferencer crossReferencer = new EcoreUtil.CrossReferencer(comparison) {
			/** Generic Serial ID. */
			private static final long serialVersionUID = 1L;

			{
				crossReference();
			}
		};
		return crossReferencer;
	}

	/**
	 * Get the objects which reference the given <code>referencedObject</code> through the given
	 * <code>feature</code> thanks to the given <code>crossreferencer</code>. The given <code>clazz</code>
	 * enables to specify the expected kind of objects.
	 * 
	 * @param crossReferencer
	 *            The cross referencer.
	 * @param referencedEObject
	 *            The concerned object.
	 * @param feature
	 *            The structural feature.
	 * @param clazz
	 *            The expected kind of objects.
	 * @param <T>
	 *            The expected kind.
	 * @return A set of referencing objects.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends EObject> Set<T> getCrossReferences(EcoreUtil.CrossReferencer crossReferencer,
			EObject referencedEObject, EStructuralFeature feature, Class<T> clazz) {
		final Set<T> result = new HashSet<T>();

		final Collection<Setting> settings = crossReferencer.get(referencedEObject);
		if (settings != null) {
			for (Setting setting : settings) {
				if (feature == null || setting.getEStructuralFeature().equals(feature)) {
					final EObject crossElt = setting.getEObject();
					if (clazz == null || clazz.isInstance(crossElt)) {
						result.add((T)crossElt);
					}
				}
			}
		}

		return result;
	}

	/**
	 * Get the reference changes matching the <code>referencedEObject</code> from the <code>feature</code>
	 * with the specified <code>kind</code>. Only reference changes where the nature of the reference is the
	 * one set by <code>isContainment</code> are kept.
	 * 
	 * @param crossReferencer
	 *            The cross referencer enables to request differences from the business model.
	 * @param referencedEObject
	 *            The given business object to find change candidates.
	 * @param feature
	 *            The structural feature to focus on requested changes.
	 * @param kind
	 *            The kind of changes to retrieve.
	 * @param isContainment
	 *            The nature of the reference to focus.
	 * @return The list of found changes.
	 */
	public static Set<ReferenceChange> getReferenceChanges(EcoreUtil.CrossReferencer crossReferencer,
			EObject referencedEObject, EStructuralFeature feature, DifferenceKind kind, Boolean isContainment) {
		final Set<ReferenceChange> result = new HashSet<ReferenceChange>();
		if (referencedEObject != null) {
			Iterator<ReferenceChange> crossReferences = getCrossReferences(crossReferencer,
					referencedEObject, feature, ReferenceChange.class).iterator();
			while (crossReferences.hasNext()) {
				ReferenceChange crossReference = crossReferences.next();
				if ((kind == null || crossReference.getKind().equals(kind))
						&& (isContainment == null || isContainment.booleanValue() == crossReference
								.getReference().isContainment())) {
					result.add(crossReference);
				}
			}
		}
		return result;
	}

	/**
	 * Returns the list of references that should be taken into account when copying an EObject : EReferences
	 * that are neither {@link EReference#isContainer() container} nor {@link EReference#isContainment()}; or
	 * that contain feature maps.
	 * 
	 * @param eObject
	 *            The EObject for which we seek the "copy-able" references.
	 * @return The EReferences that should be copied from the given EObject.
	 */
	public static List<EStructuralFeature> getCopiableReferences(EObject eObject) {
		final EClass eClass = eObject.eClass();
		final List<EStructuralFeature> result = new ArrayList<EStructuralFeature>();
		for (int j = 0; j < eClass.getFeatureCount(); ++j) {
			final EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(j);
			if (eStructuralFeature.isChangeable() && !eStructuralFeature.isDerived()) {
				if (isSimpleReference(eStructuralFeature) || isFeatureMap(eStructuralFeature)) {
					result.add(eStructuralFeature);
				}
			}
		}
		return result;
	}

	/**
	 * Checks if the specified feature is an EReference that is neither {@link EReference#isContainer()
	 * container} nor {@link EReference#isContainment()} reference.
	 * 
	 * @param feature
	 *            Feature to be tested.
	 * @return <code>true</code> if the feature is a simple reference, <code>false</code> otherwise.
	 */
	public static boolean isSimpleReference(EStructuralFeature feature) {
		return feature instanceof EReference && !((EReference)feature).isContainment()
				&& !((EReference)feature).isContainer();
	}

	/**
	 * Checks if the specified feature's type is a feature map.
	 * 
	 * @param feature
	 *            Feature to be tested.
	 * @return <code>true</code> if it the feature's type is a feature map, <code>false</code> otherwise.
	 */
	public static boolean isFeatureMap(EStructuralFeature feature) {
		return FeatureMapUtil.isFeatureMap(feature);
	}

	/**
	 * Returns the value of the given feature for the given EObject. Whatever the type of the feature, its
	 * value(s) will be returned as a Set.
	 * 
	 * @param eObject
	 *            The EObject from which to retrieve a feature's values.
	 * @param feature
	 *            The feature which values we seek.
	 * @param resolveProxies
	 *            <code>true</code> if the proxies have to be resolved.
	 * @return The Set of values for the given EReference of the given EObject
	 */
	@SuppressWarnings("unchecked")
	private static Set<EObject> getReferencedEObject(EObject eObject, EStructuralFeature feature,
			boolean resolveProxies) {
		final Set<EObject> result = new LinkedHashSet<EObject>();
		if (isSimpleReference(feature)) {
			final Object obj = eObject.eGet(feature, resolveProxies);
			if (obj instanceof EObject) {
				result.add((EObject)obj);
			} else if (obj instanceof Collection<?>) {
				result.addAll((Collection<EObject>)obj);
			}
		} else if (isFeatureMap(feature)) {
			final FeatureMap featureMap = (FeatureMap)eObject.eGet(feature);
			for (int k = 0; k < featureMap.size(); ++k) {
				final EStructuralFeature f = featureMap.getEStructuralFeature(k);
				if (f instanceof EReference) {
					final Object referencedEObject = featureMap.getValue(k);
					if (referencedEObject instanceof EObject) {
						result.add((EObject)referencedEObject);
					} else if (referencedEObject instanceof Collection<?>) {
						result.addAll((Collection<EObject>)referencedEObject);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Get all the EObjects referenced by the specified eObject.
	 * 
	 * @param eObject
	 *            The EObject.
	 * @param resolveProxies
	 *            <code>true</code> if the proxies have to be resolved.
	 * @return The set of all EObjects referenced from the given EObject.
	 */
	public static Set<EObject> getReferencedEObjects(EObject eObject, boolean resolveProxies) {
		final Set<EObject> result = new LinkedHashSet<EObject>();
		final Iterator<EStructuralFeature> references = getCopiableReferences(eObject).iterator();
		while (references.hasNext()) {
			final EStructuralFeature feature = references.next();
			result.addAll(getReferencedEObject(eObject, feature, resolveProxies));
		}
		// final Iterator<EObject> children = eObject.eAllContents();
		// while (children.hasNext()) {
		// final EObject child = children.next();
		// result.addAll(getReferencedEObjects(child, resolveProxies));
		// }
		return result;
	}
}
