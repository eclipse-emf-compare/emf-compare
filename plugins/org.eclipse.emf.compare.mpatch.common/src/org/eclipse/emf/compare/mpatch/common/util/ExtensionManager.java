/*******************************************************************************
 * Copyright (c) 2010 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.common.CommonMPatchPlugin;
import org.eclipse.emf.compare.mpatch.common.preferences.MPatchPreferencesPage;
import org.eclipse.emf.compare.mpatch.extension.IMPatchApplication;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.IMPatchTransformation;
import org.eclipse.emf.compare.mpatch.extension.IModelDescriptorCreator;
import org.eclipse.emf.compare.mpatch.extension.ISymbolicReferenceCreator;

/**
 * The {@link ExtensionManager} loads and maintains all extensions of this framework.
 * 
 * <ul>
 * <li>The transformations after the creation of {@link MPatchModel}s: {@link IMPatchTransformation}
 * <li>The symbolic reference resolvers: {@link IMPatchResolution}
 * <li>The appliers for {@link MPatchModel}s: {@link IMPatchApplication}
 * <li>The symbolic reference creators: {@link ISymbolicReferenceCreator}
 * <li>The model descriptor creators: {@link IModelDescriptorCreator}
 * </ul>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public final class ExtensionManager {

	private static final String EXTENSION_NAME_DESCRIPTOR_CREATOR = "model_descriptor_creator";
	private static final String EXTENSION_NAME_SYMREF_CREATOR = "symbolic_reference_creator";
	private static final String EXTENSION_NAME_APPLIER = "diff_applier";
	private static final String EXTENSION_NAME_TRANSFORMATION = "diff_transformation";
	private static final String EXTENSION_NAME_RESOLUTION = "diff_resolver";

	private final Map<String, IMPatchResolution> allResultions = new HashMap<String, IMPatchResolution>();
	private final Map<String, IMPatchApplication> allApplications = new HashMap<String, IMPatchApplication>();
	private final Map<String, ISymbolicReferenceCreator> allSymrefCreators = new HashMap<String, ISymbolicReferenceCreator>();
	private final Map<String, IModelDescriptorCreator> allModelDescrCreators = new HashMap<String, IModelDescriptorCreator>();
	private final Map<String, IMPatchTransformation> allTransformations = new LinkedHashMap<String, IMPatchTransformation>();
	private final List<String> mandatoryTransformations = new ArrayList<String>();

	/** Singleton instance. */
	private static ExtensionManager _INSTANCE;

	static {
		_INSTANCE = new ExtensionManager(); // singleton instance
	}

	/** Don't allow external instantiations. */
	private ExtensionManager() {
	}

	/**
	 * @return All symbolic reference resolvers indexed by their label.
	 */
	public static Map<String, IMPatchResolution> getAllResolutions() {
		// check if we already did that
		if (_INSTANCE.allResultions.isEmpty()) {
			final Collection<IMPatchResolution> allExtensions = ExtensionLoader.<IMPatchResolution> getAllExtensions(
					IMPatchResolution.EXTENSION_ID, EXTENSION_NAME_RESOLUTION);
			for (IMPatchResolution mPatchResolution : allExtensions) {
				_INSTANCE.allResultions.put(mPatchResolution.getLabel(), mPatchResolution);
			}
		}
		return _INSTANCE.allResultions;
	}

	/**
	 * @return All transformations (optional and mandatory!) indexed by their label.
	 */
	public static Map<String, IMPatchTransformation> getAllTransformations() {
		// check if we already did that
		if (_INSTANCE.allTransformations.isEmpty()) {

			final Collection<IMPatchTransformation> allExtensions = ExtensionLoader
					.<IMPatchTransformation> getAllExtensions(IMPatchTransformation.EXTENSION_ID,
							EXTENSION_NAME_TRANSFORMATION);
			for (IMPatchTransformation mPatchTransformation : allExtensions) {
				// name clash?
				if (_INSTANCE.allTransformations.containsKey(mPatchTransformation.getLabel())) {
					CommonMPatchPlugin.getDefault().logWarning(
							"Name clash!!! Two transformations found with the same label and one of them will be ignored! Label: "
									+ mPatchTransformation.getLabel(), new RuntimeException());
					continue;
				}

				// sort it in!
				_INSTANCE.allTransformations.put(mPatchTransformation.getLabel(), mPatchTransformation);
				if (!mPatchTransformation.isOptional()) {
					int index = 0;
					while (_INSTANCE.mandatoryTransformations.size() > index) {
						final String other = _INSTANCE.mandatoryTransformations.get(index);
						if (_INSTANCE.allTransformations.get(other).getPriority() <= mPatchTransformation.getPriority())
							break;
						else
							index++;
					}
					_INSTANCE.mandatoryTransformations.add(index, mPatchTransformation.getLabel());
				}
			}
		}
		return _INSTANCE.allTransformations;
	}

	/**
	 * @return All difference appliers indexed by their label.
	 */
	public static Map<String, IMPatchApplication> getAllApplications() {
		// check if we already did that
		if (_INSTANCE.allApplications.isEmpty()) {
			final Collection<IMPatchApplication> allExtensions = ExtensionLoader.<IMPatchApplication> getAllExtensions(
					IMPatchApplication.EXTENSION_ID, EXTENSION_NAME_APPLIER);
			for (IMPatchApplication mPatchApplication : allExtensions) {
				_INSTANCE.allApplications.put(mPatchApplication.getLabel(), mPatchApplication);
			}
		}
		return _INSTANCE.allApplications;
	}

	/**
	 * @return All symbolic reference creators indexed by their label.
	 */
	public static Map<String, ISymbolicReferenceCreator> getAllSymbolicReferenceCreators() {
		// check if we already did that
		if (_INSTANCE.allSymrefCreators.isEmpty()) {
			final Collection<ISymbolicReferenceCreator> allExtensions = ExtensionLoader
					.<ISymbolicReferenceCreator> getAllExtensions(ISymbolicReferenceCreator.EXTENSION_ID,
							EXTENSION_NAME_SYMREF_CREATOR);
			for (ISymbolicReferenceCreator iSymrefCreator : allExtensions) {
				_INSTANCE.allSymrefCreators.put(iSymrefCreator.getLabel(), iSymrefCreator);
			}
		}
		return _INSTANCE.allSymrefCreators;
	}

	/**
	 * @return All model descriptor creators indexed by their label.
	 */
	public static Map<String, IModelDescriptorCreator> getAllModelDescriptorCreators() {
		// check if we already did that
		if (_INSTANCE.allModelDescrCreators.isEmpty()) {
			final Collection<IModelDescriptorCreator> allExtensions = ExtensionLoader
					.<IModelDescriptorCreator> getAllExtensions(IModelDescriptorCreator.EXTENSION_ID,
							EXTENSION_NAME_DESCRIPTOR_CREATOR);
			for (IModelDescriptorCreator iModelDescriptorCreator : allExtensions) {
				_INSTANCE.allModelDescrCreators.put(iModelDescriptorCreator.getLabel(), iModelDescriptorCreator);
			}
		}
		return _INSTANCE.allModelDescrCreators;
	}

	/**
	 * @return The default symbolic reference resolver.
	 */
	public static IMPatchResolution getSelectedResolution() {
		// get preferred resolution
		String preferredResolution = CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
				MPatchPreferencesPage.PREFERENCES_KEY_RESOLUTION);

		// check whether it is an existing extension
		final Map<String, IMPatchResolution> allResolutions = getAllResolutions();
		if (allResolutions.containsKey(preferredResolution)) {
			return allResolutions.get(preferredResolution);
		} else if (allResolutions.isEmpty()) {
			CommonMPatchPlugin.getDefault().logError(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Resolution extension found! Please check installation.");
			return null;
		} else {
			final IMPatchResolution mPatchResolution = allResolutions.values().iterator().next();
			CommonMPatchPlugin.getDefault().logWarning(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Resolver was selected! So we just take the first we find: "
							+ mPatchResolution.getLabel());
			return mPatchResolution;
		}
	}

	/**
	 * @return The default difference applier.
	 */
	public static IMPatchApplication getSelectedApplication() {
		// get preferred resolution
		String preferredApplication = CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
				MPatchPreferencesPage.PREFERENCES_KEY_APPLICATION);

		// check whether it is an existing extension
		final Map<String, IMPatchApplication> allApplications = getAllApplications();
		if (allApplications.containsKey(preferredApplication)) {
			return allApplications.get(preferredApplication);
		} else if (allApplications.isEmpty()) {
			CommonMPatchPlugin.getDefault().logError(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Application extension found! Please check installation.");
			return null;
		} else {
			final IMPatchApplication mPatchApplication = allApplications.values().iterator().next();
			CommonMPatchPlugin.getDefault().logWarning(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Application was selected! So we just take the first we find: "
							+ mPatchApplication.getLabel());
			return mPatchApplication;
		}
	}

	/**
	 * @return The default symbolic reference creator.
	 */
	public static ISymbolicReferenceCreator getSelectedSymbolicReferenceCreator() {
		// get preferred symref
		String preferredSymrefCreator = CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
				MPatchPreferencesPage.PREFERENCES_KEY_SYMBOLIC_REFERENCE);

		// check whether it is an existing extension
		final Map<String, ISymbolicReferenceCreator> allSymrefCreators = getAllSymbolicReferenceCreators();
		if (allSymrefCreators.containsKey(preferredSymrefCreator)) {
			return allSymrefCreators.get(preferredSymrefCreator);
		} else if (allSymrefCreators.isEmpty()) {
			CommonMPatchPlugin.getDefault().logError(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Symbolic Reference Creator extension found! Please check installation.");
			return null;
		} else {
			final ISymbolicReferenceCreator creator = allSymrefCreators.values().iterator().next();
			CommonMPatchPlugin.getDefault().logWarning(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Symbolic Reference Creator was selected! So we just take the first we find: "
							+ creator.getLabel());
			return creator;
		}
	}

	/**
	 * @return The model descriptor creator.
	 */
	public static IModelDescriptorCreator getSelectedModelDescriptorCreator() {
		// get preferred model descriptor creator
		String preferredModelDescriptorCreator = CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
				MPatchPreferencesPage.PREFERENCES_KEY_MODEL_DESCRIPTOR);

		// check whether it is an existing extension
		final Map<String, IModelDescriptorCreator> allModelDescriptorCreators = getAllModelDescriptorCreators();
		if (allModelDescriptorCreators.containsKey(preferredModelDescriptorCreator)) {
			return allModelDescriptorCreators.get(preferredModelDescriptorCreator);
		} else if (allModelDescriptorCreators.isEmpty()) {
			CommonMPatchPlugin.getDefault().logError(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Model Descirptor Creator extension found! Please check installation.");
			return null;
		} else {
			final IModelDescriptorCreator creator = allModelDescriptorCreators.values().iterator().next();
			CommonMPatchPlugin.getDefault().logWarning(
					"No " + MPatchConstants.MPATCH_SHORT_NAME
							+ " Model Descriptor Creator was selected! So we just take the first we find: "
							+ creator.getLabel());
			return creator;
		}
	}

	/**
	 * @return The (ordered) list of transformations which are selected in the preference page.
	 */
	public static List<String> getSelectedOptionalTransformations() {
		final List<String> result = new ArrayList<String>();

		// get ordered list from preferences
		final List<String> preferencesList = Arrays.asList(CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
				MPatchPreferencesPage.PREFERENCES_KEY_TRANSFORMATION_ORDER).split(
				MPatchPreferencesPage.PREFERENCES_TRANSFORMATION_SEPERATOR));

		// filter only the selected optional transformations
		final Map<String, IMPatchTransformation> allTransformations = getAllTransformations();
		for (String transformationLabel : preferencesList) {
			if (allTransformations.containsKey(transformationLabel)
					&& allTransformations.get(transformationLabel).isOptional()) {
				result.add(transformationLabel);
			}
		}
		return result;
	}
	
	/**
	 * @return All optional transformations that are not selected.
	 */
	public static List<String> getUnselectedOptionalTransformations() {
		final List<String> result = new ArrayList<String>();

		// get list from preferences
		final List<String> preferencesList = Arrays.asList(CommonMPatchPlugin.getDefault().getPreferenceStore().getString(
				MPatchPreferencesPage.PREFERENCES_KEY_TRANSFORMATION_ORDER).split(
				MPatchPreferencesPage.PREFERENCES_TRANSFORMATION_SEPERATOR));

		// filter only the unselected optional transformations
		final Map<String, IMPatchTransformation> allTransformations = getAllTransformations();
		for (String label : allTransformations.keySet()) {
			if (!preferencesList.contains(label)
					&& allTransformations.get(label).isOptional()) {
				result.add(label);
			}
		}
		return result;
	}

	/**
	 * @return The (ordered) list of mandatory transformations.
	 */
	public static List<String> getMandatoryTransformations() {
		getAllTransformations(); // in case it was not yet initialized - this one does it!
		return _INSTANCE.mandatoryTransformations;
	}
}
