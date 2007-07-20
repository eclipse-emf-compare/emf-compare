/*******************************************************************************
 * Copyright (c) 2006, 2007 Obeo.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class determines the unused features in a metamodel using models.<br/>
 * <p>
 * A feature is considered &quot;unused&quot; if its value is never changed throughout all the model's classes.
 * </p>
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class MetamodelFilter {
	/** Keeps track of all the informations of the features. */
	protected final Map<EStructuralFeature, FeatureInformation> featuresToInformation = new HashMap<EStructuralFeature, FeatureInformation>();

	/** List of the unused features' informations. */
	protected List<FeatureInformation> unusedFeatures;

	/** This {@link HashMap} will keep track of all the used {@link EStructuralFeature features} for a given {@link EClass class}. */
	private final Map<EClass, List<EStructuralFeature>> eClassToFeaturesList = new HashMap<EClass, List<EStructuralFeature>>();

	/**
	 * Returns a list of the pertinent features for this {@link EObject}.
	 * 
	 * @param eObj
	 *            {@link EObject} from which we seek the features.
	 * @return A list of the pertinent features for this {@link EObject}.
	 */
	public List getFilteredFeatures(EObject eObj) {
		// cache the filtered features for a type
		if (eClassToFeaturesList.containsKey(eObj.eClass()))
			return eClassToFeaturesList.get(eObj.eClass());
		// end of memorize cache

		final List<EStructuralFeature> result = new ArrayList<EStructuralFeature>();
		final Collection unused = getUnusedFeatures();
		final Iterator it = eObj.eClass().getEAllStructuralFeatures().iterator();
		while (it.hasNext()) {
			final EStructuralFeature feat = (EStructuralFeature)it.next();
			if (!unused.contains(feat))
				result.add(feat);
		}
		eClassToFeaturesList.put(eObj.eClass(), result);
		return result;
	}

	/**
	 * Returns all the unused features of the {@link EObject} that's been parsed through {@link #processEObject(EObject)}.
	 * 
	 * @return All the unused features of the {@link EObject} that's been parsed through {@link #processEObject(EObject)}.
	 */
	private Collection getUnusedFeatures() {
		if (unusedFeatures == null)
			buildUnusedFeatures();
		return unusedFeatures;
	}

	/**
	 * Analyses a model and changes the stats using this model.
	 * 
	 * @param root
	 *            Model to analyze.
	 */
	public void analyseModel(EObject root) {
		processEObject(root);
		final Iterator it = root.eAllContents();
		while (it.hasNext()) {
			final EObject eObj = (EObject)it.next();
			processEObject(eObj);
		}
		unusedFeatures = null;
		eClassToFeaturesList.clear();
	}

	/**
	 * This will iterate through all the features stored via {@link #processEObject(EObject)} and populates the
	 * {@link #unusedFeatures unused features list}.
	 */
	private void buildUnusedFeatures() {
		unusedFeatures = new ArrayList<FeatureInformation>();
		final Iterator<EStructuralFeature> it = featuresToInformation.keySet().iterator();
		while (it.hasNext()) {
			final EStructuralFeature feat = it.next();
			if (featuresToInformation.get(feat).hasUniqueValue())
				unusedFeatures.add(featuresToInformation.get(feat));
		}
	}

	/**
	 * Iterates through all the {@link EStructuralFeature features} of a given {@link EObject} and populates the
	 * {@link #featuresToInformation known features list} for later use.
	 * 
	 * @param eObj
	 *            {@link EObject} we need to parse for feature information.
	 */
	private void processEObject(EObject eObj) {
		final Iterator featIt = eObj.eClass().getEAllStructuralFeatures().iterator();
		while (featIt.hasNext()) {
			final EStructuralFeature feat = (EStructuralFeature)featIt.next();
			if (!featuresToInformation.containsKey(feat))
				featuresToInformation.put(feat, new FeatureInformation(feat));
			try {
				if (EFactory.eGet(eObj, feat.getName()) != null) {
					featuresToInformation.get(feat).processValue(eObj.eGet(feat).toString());
				} else {
					featuresToInformation.get(feat).processValue("null"); //$NON-NLS-1$
				}
			} catch (FactoryException e) {
				EMFComparePlugin.log(e.getMessage(), false);
			}
		}
	}
}

/**
 * Describes a feature.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
class FeatureInformation {
	/** Structure which information is computed here. */
	private final EStructuralFeature feature;

	/** Checks wether this feature's value is always the same. */
	private boolean hasUniqueValue = true;

	/** Value of this feature if it is never altered. */
	private String uniqueValue;

	/** Counts the number of times this feature's information is accessed. */
	private int timesUsed;

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
	 * Returns the number of time this feature has been used.
	 * 
	 * @return The number of time this feature has been used.
	 */
	public int getTimesUsed() {
		return timesUsed;
	}

	/**
	 * Returns the feature unique value.
	 * 
	 * @return The feature unique value.
	 */
	public String getUniqueValue() {
		return uniqueValue;
	}
}
