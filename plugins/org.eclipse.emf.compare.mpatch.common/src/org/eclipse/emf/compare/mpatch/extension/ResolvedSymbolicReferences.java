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
package org.eclipse.emf.compare.mpatch.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.IndepReferenceChange;
import org.eclipse.emf.compare.mpatch.binding.MPatchModelBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * This class is used to store the resolved binding for applying an mpatch to a particular model.<br>
 * <br>
 * It provides the raw results, i.e. a map from symbolic references to a set of resolved elements, as well as a grouped
 * result, i.e. a map from {@link IndepChange}s to the actual required symbolic references depending on the application
 * direction. For example, if a {@link IndepReferenceChange} is resolved for the left side (
 * {@link ResolvedSymbolicReferences#RESOLVE_LEFT}), then only the references <i>correspondingElement</i> and
 * <i>oldReference</i> are resolved, but not <i>newReference</i>.<br>
 * <br>
 * An instance of this should only be created by implementations of {@link IMPatchApplication}.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public final class ResolvedSymbolicReferences {

	private final MPatchModel mpatch;
	private final EObject model;
	private final int direction;
	private final Map<IndepChange, Map<IElementReference, List<EObject>>> resolution;
	private final Map<IndepChange, ValidationResult> validation;
	private final Map<IElementReference, List<EObject>> resolutionRaw;
	private final Map<IElementReference, List<IElementReference>> equallyResolvingReferences;
	private final MPatchModelBinding binding;

	/** A constant defining the resolution of elements in the unchanged model of an mpatch. */
	public static final int RESOLVE_UNCHANGED = 1;

	/** A constant defining the resolution of elements in the changed model of an mpatch. */
	public static final int RESOLVE_CHANGED = 2;

	/**
	 * Classification of validation result (
	 * {@link MPatchValidator#validateElementState(IndepChange, Map, boolean, boolean)}).<br>
	 * <br>
	 * 
	 * {@link ValidationResult#STATE_BEFORE} - State before the change found (=successful).<br>
	 * {@link ValidationResult#STATE_AFTER} - State after the change found (=applied).<br>
	 * {@link ValidationResult#STATE_INVALID} - Model elements resolved but neither state (before, after) could be
	 * found.<br> {@link ValidationResult#REFERENCE} - Wrong number of resolved references.<br>
	 * {@link ValidationResult#UNKNOWN_CHANGE} - The change is unknown.
	 */
	public static enum ValidationResult {
		/** State before the change found (=successful). */
		STATE_BEFORE,
		/** State after the change found (=applied). */
		STATE_AFTER,
		/** Model elements resolved but neither state (before, after) could be found. */
		STATE_INVALID,
		/** Wrong number of resolved references. */
		REFERENCE,
		/** the change is unknown. */
		UNKNOWN_CHANGE,
	}

	/**
	 * A mapping from {@link ValidationResult} to a human-readable String.
	 */
	public static final Map<ValidationResult, String> VALIDATION_RESULTS;

	static {
		final Map<ValidationResult, String> messageMap = new HashMap<ValidationResult, String>();
		messageMap.put(ValidationResult.STATE_BEFORE, "ok");
		messageMap.put(ValidationResult.STATE_AFTER, "applied");
		messageMap.put(ValidationResult.STATE_INVALID, "invalid state in model");
		messageMap.put(ValidationResult.REFERENCE, "check reference resolutions");
		messageMap.put(ValidationResult.UNKNOWN_CHANGE, "unknown change");
		VALIDATION_RESULTS = Collections.unmodifiableMap(messageMap);
	}

	/**
	 * Create a wrapper object for the resolution of symbolic references. See {@link IResolvedSymbolicReferences} for
	 * details.
	 */
	public ResolvedSymbolicReferences(MPatchModel mpatch, EObject model, int direction,
			Map<IndepChange, Map<IElementReference, List<EObject>>> resolution,
			Map<IElementReference, List<EObject>> resolutionRaw,
			Map<IElementReference, List<IElementReference>> equallyResolvingReferences,
			Map<IndepChange, ValidationResult> validation) {
		this.mpatch = mpatch;
		this.direction = direction;
		this.model = model;
		this.resolution = resolution;
		this.resolutionRaw = resolutionRaw;
		this.equallyResolvingReferences = equallyResolvingReferences;
		this.validation = validation;
		binding = BindingFactory.eINSTANCE.createMPatchModelBinding();
		binding.setMPatchModel(mpatch);
		binding.setModel(model);
	}

	/**
	 * The differences for which the symbolic references were resolved.
	 */
	public MPatchModel getMPatchModel() {
		return mpatch;
	}

	/**
	 * The direction for which the symbolic reference resolution was performed. Either
	 * {@link ResolvedSymbolicReferences#RESOLVE_CHANGED} or {@link ResolvedSymbolicReferences#RESOLVE_UNCHANGED}.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * A serializable form of the resolution of symbolic references for traceability.
	 */
	public MPatchModelBinding getMPatchModelBinding() {
		return binding;
	}

	/**
	 * The model for which the symbolic references were resolved.
	 */
	public EObject getModel() {
		return model;
	}

	/**
	 * A map containing all symbolic references which resolve to the same elements. More specifically speaking, groups
	 * of symbolic references for which {@link IElementReference#resolvesEqual(IElementReference)} returns
	 * <code>true</code>.
	 */
	public Map<IElementReference, List<IElementReference>> getEquallyResolvingReferences() {
		return equallyResolvingReferences;
	}

	/**
	 * @param reference
	 *            A symbolic reference.
	 * @return A list of all equally resolving references excluding <code>reference</code>.
	 * @throws IllegalArgumentException
	 *             If the reference is not listed in {@link ResolvedSymbolicReferences#getEquallyResolvingReferences()}.
	 */
	public List<IElementReference> getEquallyResolvingReferences(IElementReference reference) {
		if (!getEquallyResolvingReferences().containsKey(reference))
			throw new IllegalArgumentException(
					"Reference not found in equally resolved references. Make sure that it belongs to the differences! "
							+ reference);
		final List<IElementReference> list = new ArrayList<IElementReference>(getEquallyResolvingReferences().get(
				reference));
		list.remove(reference);
		return list;
	}

	/**
	 * This returns the raw resolution of symbolic references.<br>
	 * This means that all {@link IndepChange}s from the {@link MPatchModel} are mapped to a set of {@link EObject} for
	 * which the resolution was performed.
	 */
	public Map<IElementReference, List<EObject>> getRawResolution() {
		return resolutionRaw;
	}

	/**
	 * The result contains a direction-sensitive mapping from {@link IndepChange}s to all relevant symbolic references,
	 * depending on the direction of resolution. For example, if a {@link IndepReferenceChange} is resolved for the left
	 * side ( {@link ResolvedSymbolicReferences#RESOLVE_LEFT}), then only the references <i>correspondingElement</i> and
	 * <i>oldReference</i> are resolved, but not <i>newReference</i>.
	 * 
	 * The {@link IndepChange}s contained in this map might be a subset of those in
	 * {@link ResolvedSymbolicReferences#getMPatchModel()}; e.g. for difference application, only these changes are
	 * applied.
	 */
	public Map<IndepChange, Map<IElementReference, List<EObject>>> getResolutionByChange() {
		return resolution;
	}

	/**
	 * The result contains the states of all changes. It is the responsibility of the validator to update this map
	 * accordingly after each validation.
	 */
	public Map<IndepChange, ValidationResult> getValidation() {
		return validation;
	}
}
