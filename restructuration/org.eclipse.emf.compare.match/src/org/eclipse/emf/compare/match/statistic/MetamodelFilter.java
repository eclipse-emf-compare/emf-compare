/*******************************************************************************
 * Copyright (c) 2006, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.statistic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.util.EMFCompareMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class determines the unused features in a metamodel using models.<br/>
 * <p>
 * A feature is considered &quot;unused&quot; if its value is never changed throughout all the model's
 * classes.
 * </p>
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class MetamodelFilter {
	/** Keeps track of all the informations of the features. */
	protected final Map<EStructuralFeature, FeatureInformation> featuresToInformation = new EMFCompareMap<EStructuralFeature, FeatureInformation>();

	/** List of the unused features' informations. */
	protected List<EStructuralFeature> unusedFeatures;

	/**
	 * This map will keep track of all the used {@link EStructuralFeature features} for a given {@link EClass
	 * class}.
	 */
	private final Map<EClass, List<EStructuralFeature>> eClassToFilteredFeaturesList = new EMFCompareMap<EClass, List<EStructuralFeature>>();

	/**
	 * Analyses a model and changes the stats using this model.
	 * 
	 * @param root
	 *            Model to analyze.
	 */
	public void analyseModel(EObject root) {
		processEObject(root);
		final Iterator<EObject> it = root.eAllContents();
		while (it.hasNext()) {
			final EObject eObj = it.next();
			processEObject(eObj);
		}
		unusedFeatures = null;
		eClassToFilteredFeaturesList.clear();
	}

	/**
	 * Clears all recorded information.
	 */
	public void clear() {
		featuresToInformation.clear();
		unusedFeatures = null;
		eClassToFilteredFeaturesList.clear();
	}

	/**
	 * Returns a list of the pertinent features for this {@link EObject}.
	 * 
	 * @param eObj
	 *            {@link EObject} from which we seek the features.
	 * @return A list of the pertinent features for this {@link EObject}.
	 */
	public List<EStructuralFeature> getFilteredFeatures(EObject eObj) {
		// cache the filtered features for a type
		if (eClassToFilteredFeaturesList.containsKey(eObj.eClass()))
			return eClassToFilteredFeaturesList.get(eObj.eClass());
		// end of memorize cache

		final List<EStructuralFeature> result = new ArrayList<EStructuralFeature>();
		final Collection<EStructuralFeature> unused = getUnusedFeatures();
		final Iterator<EStructuralFeature> it = eObj.eClass().getEAllStructuralFeatures().iterator();
		while (it.hasNext()) {
			final EStructuralFeature feat = it.next();
			if (!unused.contains(feat))
				result.add(feat);
		}
		eClassToFilteredFeaturesList.put(eObj.eClass(), result);
		return result;
	}

	/**
	 * This will iterate through all the features stored via {@link #processEObject(EObject)} and populates
	 * the {@link #unusedFeatures unused features list}.
	 */
	private void buildUnusedFeatures() {
		unusedFeatures = new ArrayList<EStructuralFeature>();
		final Iterator<EStructuralFeature> it = featuresToInformation.keySet().iterator();
		while (it.hasNext()) {
			final EStructuralFeature feat = it.next();
			if (featuresToInformation.get(feat).hasUniqueValue())
				unusedFeatures.add(featuresToInformation.get(feat).getFeature());
		}
	}

	/**
	 * Returns all the unused features of the {@link EObject} that's been parsed through
	 * {@link #processEObject(EObject)}.
	 * 
	 * @return All the unused features of the {@link EObject} that's been parsed through
	 *         {@link #processEObject(EObject)}.
	 */
	private Collection<EStructuralFeature> getUnusedFeatures() {
		if (unusedFeatures == null)
			buildUnusedFeatures();
		return unusedFeatures;
	}

	/**
	 * Iterates through all the {@link EStructuralFeature features} of a given {@link EObject} and populates
	 * the {@link #featuresToInformation known features list} for later use.
	 * 
	 * @param eObj
	 *            {@link EObject} we need to parse for feature information.
	 */
	private void processEObject(EObject eObj) {
		final Iterator<EStructuralFeature> featIt = eObj.eClass().getEAllStructuralFeatures().iterator();
		while (featIt.hasNext()) {
			final EStructuralFeature feat = featIt.next();
			if (!feat.isDerived()) {
				FeatureInformation featureInformation = featuresToInformation.get(feat);
				if (featureInformation == null) {
					featureInformation = new FeatureInformation(feat);
					featuresToInformation.put(feat, featureInformation);
				}
				final Object value = eObj.eGet(feat);
				if (feat instanceof EReference) {
					if (feat.isMany()) {
						final Collection<EObject> values = (Collection)value;
						if (values.size() == 0) {
							featureInformation.processValue("0"); //$NON-NLS-1$
						}
					} else {
						if (value == null) {
							featureInformation.processValue("0"); //$NON-NLS-1$
						}
					}
				} else {
					if (value != null) {
						featureInformation.processValue(value.toString());
					} else {
						featureInformation.processValue("null"); //$NON-NLS-1$
					}
				}
			}
		}
	}
}

/**
 * Describes a feature.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class FeatureInformation {
	/** Structure which information is computed here. */
	private final EStructuralFeature feature;

	/** Checks wether this feature's value is always the same. */
	private boolean hasUniqueValue = true;

	/** Counts the number of times this feature's information is accessed. */
	private int timesUsed;

	/** Value of this feature if it is never altered. */
	private String uniqueValue;

	/**
	 * Creates a {@link FeatureInformation} from a feature.
	 * 
	 * @param feat
	 *            The {@link EStructuralFeature feature} we want described.
	 */
	public FeatureInformation(EStructuralFeature feat) {
		feature = feat;
	}

	/**
	 * Returns the feature described by this {@link FeatureInformation}.
	 * 
	 * @return The feature described by this {@link FeatureInformation}.
	 */
	public EStructuralFeature getFeature() {
		return feature;
	}

	/**
	 * Indicates that this features always has the same value.
	 * 
	 * @return <code>True</code> if this feature always has the same value, <code>False</code> otherwise.
	 */
	public boolean hasUniqueValue() {
		return hasUniqueValue;
	}

	/**
	 * Adds this value in the calculus model.
	 * 
	 * @param value
	 *            The value to add.
	 */
	public void processValue(String value) {
		timesUsed += 1;
		if (uniqueValue != null && !uniqueValue.equals(value)) {
			hasUniqueValue = false;
		} else if (uniqueValue == null) {
			uniqueValue = value;
		}
	}
}
