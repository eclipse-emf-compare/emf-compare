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
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemElementChange;
import org.eclipse.emf.compare.mpatch.IndepAddRemReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.IndepMoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveElementChange;
import org.eclipse.emf.compare.mpatch.IndepRemoveReferenceChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateAttributeChange;
import org.eclipse.emf.compare.mpatch.IndepUpdateReferenceChange;
import org.eclipse.emf.compare.mpatch.MPatchModel;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchResolver;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.binding.AddElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AddReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.AttributeChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.BindingFactory;
import org.eclipse.emf.compare.mpatch.binding.ChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.ElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.MoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.RemoveElementChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.RemoveReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.binding.SubModelBinding;
import org.eclipse.emf.compare.mpatch.binding.UpdateReferenceChangeBinding;
import org.eclipse.emf.compare.mpatch.common.util.MPatchConstants;
import org.eclipse.emf.compare.mpatch.extension.IMPatchApplication;
import org.eclipse.emf.compare.mpatch.extension.IMPatchResolution;
import org.eclipse.emf.compare.mpatch.extension.MPatchApplicationResult;
import org.eclipse.emf.compare.mpatch.extension.MPatchApplicationResult.ApplicationStatus;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.provider.MPatchItemProviderAdapterFactory;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;

/**
 * The default implementation of the mpatch application interface.
 * 
 * This implementation changes the target model according to previously resolved symbolic references (
 * {@link ResolvedSymbolicReferences}, created by {@link MPatchResolver} and possibly refined by implementations of
 * {@link IMPatchResolution}).
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 * 
 */
public class GenericMPatchApplier implements IMPatchApplication {

	/** Timestamp format for default text in notes fields. */
	protected static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/** Adapter factory for binding notes. */
	protected static final ComposedAdapterFactory ADAPTER_FACTORY = new ComposedAdapterFactory(
			ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

	/** The label for this extension. */
	private static final String LABEL = "Generic Application Engine";

	/** internally used to classify the application of a single change. */
	private enum ApplicationResult {
		/** Successful application. */
		SUCCESSFUL,
		/** Only binding created. */
		BOUND,
		/** Application failed. */
		FAILED,
		/** Cross-reference restoring failed. */
		REFERENCES,
	}

	/** All differences in the order in which they must be applied. */
	protected List<IndepChange> orderedChanges;

	/** A container to collect all deletions (when adding elements to it, their respective parent will loose them. */
	protected final EObject deletionContainer = ExtEcoreUtils.wrapInGenericContainer(Collections.<EObject> emptyList());

	/** The particular collection in the deletionContainer. */
	@SuppressWarnings("unchecked")
	protected final EList<EObject> deletedSubmodels = (EList<EObject>) deletionContainer.eGet(deletionContainer
			.eClass().getEStructuralFeature("children"));

	/** Store a flattened collection of deletions for further symbolic reference resolution. */
	protected final Collection<EObject> flatDeletions = new ArrayList<EObject>();

	/** Flat (!) mapping from all added elements of submodels to their respective change for returning statistics. */
	protected final Map<EObject, IndepAddRemElementChange> addedElementToChangeMap = new LinkedHashMap<EObject, IndepAddRemElementChange>();

	/** Flat (!) mapping from symrefs of all added elements to their respective elements for dynamic symref resolution. */
	protected final Map<IElementReference, Set<EObject>> addedElementSymrefToElementsMap = new LinkedHashMap<IElementReference, Set<EObject>>();

	/** Flat (!) mapping from all added (sub)elements to their model descriptor for cross-reference restoring. */
	protected final Map<EObject, IModelDescriptor> addedElementToModelDescriptorMap = new LinkedHashMap<EObject, IModelDescriptor>();

	/** Flat mapping from all added (sub)elements to their submodelbindings for cross-reference restoring. */
	protected final Map<EObject, SubModelBinding> addedElementToSubModelBindingMap = new LinkedHashMap<EObject, SubModelBinding>();

	static {
		ADAPTER_FACTORY.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new MPatchItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		ADAPTER_FACTORY.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return LABEL;
	}

	/**
	 * Apply {@link MPatchModel} as resolved in {@link ResolvedSymbolicReferences}.
	 * 
	 * @return The application result.
	 */
	@Override
	public MPatchApplicationResult applyMPatch(final ResolvedSymbolicReferences mapping, final boolean storeBinding) {

		// 0. set up collections for return value
		final Collection<IndepChange> successfulChanges = new LinkedList<IndepChange>();
		final Collection<IndepChange> boundChanges = new LinkedList<IndepChange>();
		final Collection<IndepChange> failedReferenceChanges = new LinkedList<IndepChange>();
		final Collection<IndepChange> failedChanges = new LinkedList<IndepChange>();
		addedElementToChangeMap.clear();
		addedElementSymrefToElementsMap.clear();
		addedElementToModelDescriptorMap.clear();
		deletedSubmodels.clear();
		mapping.getMPatchModelBinding().getChangeBindings().clear();
		addedElementToSubModelBindingMap.clear();

		// 1. set up ordering of the differences!
		final boolean forward = mapping.getDirection() == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;
		orderedChanges = MPatchValidator.orderChanges(mapping.getResolutionByChange().keySet(), forward);

		// 2. apply the changes!
		for (final IndepChange indepChange : orderedChanges) {
			try {
				switch (applyChange(indepChange, mapping, forward, storeBinding)) {
				case SUCCESSFUL:
					successfulChanges.add(indepChange);
					break;
				case BOUND:
					boundChanges.add(indepChange);
					break;
				case REFERENCES:
					failedReferenceChanges.add(indepChange);
					break;
				case FAILED:
					failedChanges.add(indepChange);
					break;
				}
			} catch (final Exception e) {
				failedChanges.add(indepChange);
			}
		}

		// 3. restore references for all added elements
		restoreCrossReferences(successfulChanges, failedReferenceChanges, mapping);

		// prepare and return some useful information about the difference application
		final ApplicationStatus status = failedChanges.isEmpty() ? failedReferenceChanges.isEmpty() ? ApplicationStatus.SUCCESSFUL
				: ApplicationStatus.REFERENCES
				: ApplicationStatus.FAILURE;
		final MPatchApplicationResult result = new MPatchApplicationResult(status, successfulChanges, boundChanges,
				failedReferenceChanges, failedChanges);

		// 4. add notes to binding
		if (storeBinding) {
			addNotesToBinding(mapping, result);
		}

		return result;
	}

	/**
	 * Add notes.
	 */
	private void addNotesToBinding(ResolvedSymbolicReferences mapping, MPatchApplicationResult result) {
		// adapter factory for creating some nicely formatted notes
		final AdapterFactoryLabelProvider labels = new AdapterFactoryLabelProvider(ADAPTER_FACTORY);
		final BindingFactory factory = BindingFactory.eINSTANCE;

		// note for root element
		factory.createNote(mapping.getMPatchModelBinding(), "Binding was created on " + TIME_FORMAT.format(new Date())
				+ " by '" + LABEL + "'.\n" + "Application result:\n" + result.getMessage(ADAPTER_FACTORY));

		// iterator over all change bindings
		for (ChangeBinding changeBinding : mapping.getMPatchModelBinding().getChangeBindings()) {
			factory.createNote(changeBinding, "Binding for change: " + labels.getText(changeBinding.getChange()));
			
			// only continue if change was applied successfully
			if (!result.successful.contains(changeBinding.getChange()))
				continue;

			// check the validation result
			final String validation = result.successful.contains(changeBinding.getChange()) ? "applied to"
					: result.bound.contains(changeBinding.getChange()) ? "was already applied"
							: result.crossReferences.contains(changeBinding.getChange()) ? "applied but not all cross references could be restored"
									: "was not applied successfully";

			// notes for corresponding elements
			for (ElementChangeBinding elementChangeBinding : changeBinding.getCorrespondingElements()) {
				factory.createNote(elementChangeBinding,
						"Change " + validation + ": " + labels.getText(elementChangeBinding.getModelElement()) + "\n"
								+ "Resolved by: " + labels.getText(elementChangeBinding.getElementReference()));
			}

			// notes for add reference element change binding
			if (changeBinding instanceof AddReferenceChangeBinding) {
				final AddReferenceChangeBinding addReferenceChangeBinding = (AddReferenceChangeBinding) changeBinding;
				for (ElementChangeBinding elementChangeBinding : addReferenceChangeBinding.getChangedReference()) {
					factory.createNote(elementChangeBinding,
							"Reference added to: " + labels.getText(elementChangeBinding.getModelElement()) + "\n"
									+ "Resolved by: " + labels.getText(elementChangeBinding.getElementReference()));
				}
			}

			// notes for update reference element change binding
			if (changeBinding instanceof UpdateReferenceChangeBinding) {
				final UpdateReferenceChangeBinding updateReferenceChangeBinding = (UpdateReferenceChangeBinding) changeBinding;
				final ElementChangeBinding elementChangeBinding = updateReferenceChangeBinding.getNewReference();
				if (elementChangeBinding != null)
					factory.createNote(elementChangeBinding,
							"Reference added to: " + labels.getText(elementChangeBinding.getModelElement()) + "\n"
									+ "Resolved by: " + labels.getText(elementChangeBinding.getElementReference()));
			}

			// notes for moved element change binding
			if (changeBinding instanceof MoveElementChangeBinding) {
				final MoveElementChangeBinding moveElementChangeBinding = (MoveElementChangeBinding) changeBinding;
				final ElementChangeBinding elementChangeBinding = moveElementChangeBinding.getNewParent();
				factory.createNote(elementChangeBinding,
						"Moved to: " + labels.getText(elementChangeBinding.getModelElement()) + "\n" + "Resolved by: "
								+ labels.getText(elementChangeBinding.getElementReference()));
			}

			// notes for added sub models
			if (changeBinding instanceof AddElementChangeBinding) {
				final AddElementChangeBinding compoundBinding = (AddElementChangeBinding) changeBinding;
				addNotesToSubModelBinding(compoundBinding.getSubModelReferences(), labels);
			}
		}
	}

	/**
	 * Add notes.
	 */
	private void addNotesToSubModelBinding(EList<? extends ElementChangeBinding> subModelReferences,
			ILabelProvider labels) {
		for (ElementChangeBinding binding : subModelReferences) {
			if (binding instanceof SubModelBinding) {
				final SubModelBinding subModelBinding = (SubModelBinding) binding;
				BindingFactory.eINSTANCE.createNote(binding,
						"Added model element: " + labels.getText(subModelBinding.getSelfElement()) + "\n" + "Parent: "
								+ labels.getText(binding.getModelElement()));
				addNotesToSubModelBinding(subModelBinding.getSubModelReferences(), labels);
			} else {
				BindingFactory.eINSTANCE.createNote(binding,
						"Cross-reference restored to: " + labels.getText(binding.getModelElement()) + "\n"
								+ "Resolved by: " + labels.getText(binding.getElementReference()));
			}
		}
	}

	/**
	 * Cross references must only be restored for added (sub) elements.
	 * 
	 * @param successfulChanges
	 *            A collection of successful changes; if cross-references could be restored, the change is removed here.
	 * @param failedReferenceChanges
	 *            A collection of changes for which the cross-reference restoration failed; changes are thus added here
	 *            in such a case.
	 * @param binding
	 *            The binding.
	 */
	protected void restoreCrossReferences(final Collection<IndepChange> successfulChanges,
			final Collection<IndepChange> failedReferenceChanges, final ResolvedSymbolicReferences binding) {

		// 0. store elements for which not all symrefs could be restored
		final List<EObject> failedSymrefRestoredElements = new ArrayList<EObject>();
		int counter = 0;

		// 1. restore references for all added elements
		for (EObject addedElement : addedElementToModelDescriptorMap.keySet()) {

			// do we have cross references at all?
			final IModelDescriptor descriptor = addedElementToModelDescriptorMap.get(addedElement);
			final EList<IElementReference> crossReferences = descriptor.getCrossReferences();
			if (crossReferences.isEmpty())
				continue;

			// 2. resolve all cross references for that element :-)
			final EMap<IElementReference, EList<EObject>> crossReferenceResolutions = new BasicEMap<IElementReference, EList<EObject>>();
			for (IElementReference crossRef : crossReferences) {
				final Collection<EObject> resolution = resolveSymbolicReference(binding, flatDeletions, crossRef);
				crossReferenceResolutions.put(crossRef, new BasicEList<EObject>(resolution));
			}

			// 3. call applyCrossReferences
			final EList<IElementReference> failedReferences = descriptor.applyCrossReferences(addedElement,
					crossReferenceResolutions);
			if (failedReferences.size() > 0) {
				failedSymrefRestoredElements.add(addedElement);
			}
			counter += descriptor.getCrossReferences().size() - failedReferences.size();

			// store mapping for all cross references
			final SubModelBinding subModelBinding = addedElementToSubModelBindingMap.get(addedElement);
			if (subModelBinding != null) {
				for (IElementReference ref : crossReferenceResolutions.keySet()) {
					for (EObject obj : crossReferenceResolutions.get(ref)) {
						subModelBinding.getSubModelReferences().add(
								BindingFactory.eINSTANCE.createElementChangeBinding(ref, obj));
					}
				}
			}

		}

		// 4. now udpate the collections for the result statistics
		for (EObject failedElement : failedSymrefRestoredElements) {
			final IndepChange change = addedElementToChangeMap.get(failedElement);
			if (successfulChanges.remove(change)) {
				if (!failedReferenceChanges.add(change)) {
					throw new IllegalStateException(
							"For some reason, the following change cannot be moved to another collection: " + change);
				}
			} else if (!failedReferenceChanges.contains(change)) {
				throw new IllegalStateException("If the change is in not listed in any statistics collection: "
						+ change);
			}
		}
	}

	/**
	 * Apply the given atomic change to the given model, having the symbolic references resolved in the given binding.
	 * 
	 * @param indepChange
	 *            The change which should be applied.
	 * @param binding
	 *            A resolution of symbolic references.
	 * @param forward
	 *            The direction in which the changes should be applied (<code>true</code> = forward, <code>false</code>
	 *            = backward).
	 * @param storeBinding
	 *            If set, a {@link ChangeBinding} will be created and added to
	 *            {@link ResolvedSymbolicReferences#getMPatchModelBinding()}.
	 * @return <code>true</code> if the change was applied successfully, and <code>false</code> if it failed.
	 * @throws IllegalArgumentException
	 *             If the binding did not resolve correctly or is wrong in some sense.
	 */
	protected ApplicationResult applyChange(IndepChange indepChange, ResolvedSymbolicReferences binding,
			boolean forward, final boolean storeBinding) throws IllegalArgumentException {

		// unknown changes cannot be applied!
		if (indepChange instanceof UnknownChange) {
			return ApplicationResult.FAILED;
		}

		// for the time being, only forward application is supported!
		if (!forward) {
			throw new UnsupportedOperationException("Only forward application of " + MPatchConstants.MPATCH_SHORT_NAME
					+ " is supported so far! Please use the Reversal transformation of "
					+ MPatchConstants.MPATCH_SHORT_NAME + " for a reversing changes.");
			/*
			 * PK: my original plan was to make the mpatch declarative and applicable in both ways. However, it turned
			 * out o be easier to leave the application direction just one way and to offer an additional transformation
			 * that reverses the mpatch.
			 */
		}

		// get symbolic references, and unchanged and changed model elements (applies for all changes!)
		final Collection<EObject> correspondingElements = binding.getResolutionByChange().get(indepChange)
				.get(indepChange.getCorrespondingElement());

		// create diff-model-binding
		ChangeBinding changeBinding = null;
		if (storeBinding) {
			changeBinding = BindingFactory.eINSTANCE.createChangeBindingForChange(indepChange);
			binding.getMPatchModelBinding().getChangeBindings().add(changeBinding);
			// binding.resolutionApplied().put(indepChange.getCorrespondingElement(), correspondingElements);
		}

		// we need at least one corresponding element!
		if (correspondingElements.size() == 0) {
			return ApplicationResult.FAILED;
		}

		ApplicationResult result;

		// forward: ADD ELEMENT; backward: REMOVE ELEMENT
		if ((indepChange instanceof IndepAddElementChange && forward)
				|| (indepChange instanceof IndepRemoveElementChange && !forward)) {
			result = applyAddElementChange((IndepAddRemElementChange) indepChange, correspondingElements, binding,
					(AddElementChangeBinding) changeBinding);

			// forward: REMOVE ELEMENT; backward: ADD ELEMENT
		} else if ((indepChange instanceof IndepRemoveElementChange && forward)
				|| (indepChange instanceof IndepAddElementChange && !forward)) {
			result = applyRemoveElementChange((IndepAddRemElementChange) indepChange, correspondingElements, binding,
					(RemoveElementChangeBinding) changeBinding);

			// MOVE ELEMENT
		} else if ((indepChange instanceof IndepMoveElementChange)) {
			result = applyMoveElementChange((IndepMoveElementChange) indepChange, correspondingElements, binding,
					forward, (MoveElementChangeBinding) changeBinding);

			// forward: ADD ATTRIBUTE; backward: REMOVE ATTRIBUTE
		} else if ((indepChange instanceof IndepAddAttributeChange && forward)
				|| (indepChange instanceof IndepRemoveAttributeChange && !forward)) {
			result = applyAddAttributeChange((IndepAddRemAttributeChange) indepChange, correspondingElements,
					(AttributeChangeBinding) changeBinding);

			// forward: REMOVE ATTRIBUTE; backward: ADD ATTRIBUTE
		} else if ((indepChange instanceof IndepRemoveAttributeChange && forward)
				|| (indepChange instanceof IndepAddAttributeChange && !forward)) {
			result = applyRemoveAttributeChange((IndepAddRemAttributeChange) indepChange, correspondingElements,
					(AttributeChangeBinding) changeBinding);

			// CHANGE ATTRIBUTE
		} else if (indepChange instanceof IndepUpdateAttributeChange) {
			result = applyUpdateAttributeChange((IndepUpdateAttributeChange) indepChange, correspondingElements,
					forward, (AttributeChangeBinding) changeBinding);

			// forward: ADD REFERENCE; backward: REMOVE REFERENCE
		} else if ((indepChange instanceof IndepAddReferenceChange && forward)
				|| (indepChange instanceof IndepRemoveReferenceChange && !forward)) {
			result = applyAddReferenceChange((IndepAddRemReferenceChange) indepChange, correspondingElements, binding,
					(AddReferenceChangeBinding) changeBinding);

			// forward: REMOVE REFERENCE; backward: ADD REFERENCE
		} else if ((indepChange instanceof IndepRemoveReferenceChange && forward)
				|| (indepChange instanceof IndepAddReferenceChange && !forward)) {
			result = applyRemoveReferenceChange((IndepAddRemReferenceChange) indepChange, correspondingElements,
					binding, (RemoveReferenceChangeBinding) changeBinding);

			// CHANGE REFERENCE
		} else if (indepChange instanceof IndepUpdateReferenceChange) {
			result = applyUpdateReferenceChange((IndepUpdateReferenceChange) indepChange, correspondingElements,
					binding, forward, (UpdateReferenceChangeBinding) changeBinding);

		} else
			throw new UnsupportedOperationException("Unknown change type: " + indepChange);

		return result;
	}

	/**
	 * Update a reference of all corresponding elements.
	 * 
	 * @return <code>true</code>, if at least one reference was updated.
	 */
	protected ApplicationResult applyUpdateReferenceChange(final IndepUpdateReferenceChange referenceChange,
			final Collection<EObject> correspondingElements, final ResolvedSymbolicReferences binding,
			final boolean forward, final UpdateReferenceChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// since we know that a reference was _changed_ (and not added or removed), we have to deal with
		// a single reference
		final IElementReference objectReference = forward ? referenceChange.getNewReference() : referenceChange
				.getOldReference();

		// do the costly symbolic reference resolution before the iteration
		EObject referencedObject = null;
		if (objectReference != null) { // also null might be possible!

			// we need to find the model element first and then set it!
			final Collection<EObject> references = resolveSymbolicReference(binding, flatDeletions, objectReference);

			// we can expect exactly one model element! return false otherwise.
			if (references == null || references.size() != 1) {
				return result;
			}
			referencedObject = references.iterator().next();

			// add element binding, if not null
			if (changeBinding != null) {
				changeBinding.setNewReference(BindingFactory.eINSTANCE.createElementChangeBinding(objectReference,
						referencedObject));
			}
		}

		// 2. get right elements
		for (final EObject element : correspondingElements) {
			if (element != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									referenceChange.getCorrespondingElement(), element));
				}

				// 2.2 also null might be possible!
				if (objectReference == null) {
					
					// already null?
					if (element.eGet(referenceChange.getReference()) == null) {
						result = ApplicationResult.BOUND;
					}
					
					// apply!!!
					element.eSet(referenceChange.getReference(), null);
					if (element.eGet(referenceChange.getReference()) == null) {
						result = ApplicationResult.SUCCESSFUL;
					}
				} else {

					// already that reference?
					if (referencedObject.equals(element.eGet(referenceChange.getReference()))) {
						result = ApplicationResult.BOUND;
					}
					
					// 2.4 now lets set the reference!
					element.eSet(referenceChange.getReference(), referencedObject);
					if (referencedObject.equals(element.eGet(referenceChange.getReference()))) {
						result = ApplicationResult.SUCCESSFUL;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Remove a reference described by a symbolic reference from a collection of references from all corresponding
	 * elements.
	 * 
	 * @return <code>true</code>, if at least one reference was removed.
	 */
	protected ApplicationResult applyRemoveReferenceChange(IndepAddRemReferenceChange referenceChange,
			Collection<EObject> correspondingElements, ResolvedSymbolicReferences binding,
			RemoveReferenceChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// 2. get right elements and perform changes
		for (final EObject element : correspondingElements) {
			if (element != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									referenceChange.getCorrespondingElement(), element));
				}

				// 2.1 since we know that a reference was _added_ (and not changed), we have to deal with a list
				@SuppressWarnings("unchecked")
				final EList<?> list = (EList<EObject>) element.eGet(referenceChange.getReference());

				// 2.2 find the element(s) to add here
				final Collection<EObject> changedReferenceResolution = resolveSymbolicReference(binding, flatDeletions,
						referenceChange.getChangedReference());
				for (final EObject removedObject : changedReferenceResolution) {
					if (removedObject != null) {

						// 2.3 remove object from list
						if (list.remove(removedObject)) {
							result = ApplicationResult.SUCCESSFUL;
						} else if (!ApplicationResult.SUCCESSFUL.equals(result)) {
							// already removed...
							result = ApplicationResult.BOUND;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Add a reference to all corresponding elements, if possible.
	 * 
	 * @return <code>true</code>, if at least one reference was added successfully.
	 */
	protected ApplicationResult applyAddReferenceChange(final IndepAddRemReferenceChange referenceChange,
			final Collection<EObject> correspondingElements, final ResolvedSymbolicReferences binding,
			final AddReferenceChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// do the costly symbolic reference resolution before the iteration
		final Collection<EObject> addedObjects = resolveSymbolicReference(binding, flatDeletions,
				referenceChange.getChangedReference());

		// empty result fails this application..
		if (addedObjects == null) {
			return result;
		}

		// add element binding, if not null
		if (changeBinding != null) {
			for (EObject modelElement : addedObjects) {
				changeBinding.getChangedReference().add(
						BindingFactory.eINSTANCE.createElementChangeBinding(referenceChange.getChangedReference(),
								modelElement));
			}
		}

		// get right elements and perform changes
		for (final EObject element : correspondingElements) {
			if (element != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									referenceChange.getCorrespondingElement(), element));
				}

				// 2.1 since we know that a reference was _added_ (and not changed), we have to deal with a list
				@SuppressWarnings("unchecked")
				final EList<EObject> list = (EList<EObject>) element.eGet(referenceChange.getReference());

				for (final EObject addedObject : addedObjects) {
					if (addedObject != null) {

						if (list.contains(addedObject)) {
							// already applied
							if (!ApplicationResult.SUCCESSFUL.equals(result))
								result = ApplicationResult.BOUND;
						} else if (list.add(addedObject)) {

							// 2.3 add right object to list
							result = ApplicationResult.SUCCESSFUL;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Update the attribute value of all corresponding elements.
	 * 
	 * @return <code>true</code> if at least one attribute was changed successfully.
	 */
	protected ApplicationResult applyUpdateAttributeChange(IndepUpdateAttributeChange attributeChange,
			Collection<EObject> correspondingElements, boolean forward, AttributeChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// 2. get right elements and perform changes
		for (final EObject element : correspondingElements) {
			if (element != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									attributeChange.getCorrespondingElement(), element));
				}

				// since we know that an attribute was _changed_ (and not added or removed), we have to deal with a
				// single attribute
				final Object attributeValue = forward ? attributeChange.getNewValue() : attributeChange.getOldValue();
				
				// is it already the new value?
				if ((attributeValue == null && element.eGet(attributeChange.getChangedAttribute()) == null)
						|| (attributeValue != null && attributeValue.equals(element.eGet(attributeChange.getChangedAttribute())))) {
					if (!ApplicationResult.SUCCESSFUL.equals(result))
						result = ApplicationResult.BOUND;
				} else {
				
					// otherwise set new value!
					element.eSet(attributeChange.getChangedAttribute(), attributeValue);
					if ((attributeValue == null && element.eGet(attributeChange.getChangedAttribute()) == null)
							|| attributeValue.equals(element.eGet(attributeChange.getChangedAttribute()))) {
						result = ApplicationResult.SUCCESSFUL;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Remove the described attribute from a collection of attributes in all corresponding elements.
	 * 
	 * @return <code>true</code> if at least one attribute was removed.
	 */
	protected ApplicationResult applyRemoveAttributeChange(IndepAddRemAttributeChange attributeChange,
			Collection<EObject> correspondingElements, AttributeChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// 2. get right elements and perform changes
		for (final EObject element : correspondingElements) {
			if (element != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									attributeChange.getCorrespondingElement(), element));
				}

				// since we know that an attribute was _added_ (and not changed), we have to deal with a list
				final EList<?> list = (EList<?>) element.eGet(attributeChange.getChangedAttribute());
				
				// remove the value!
				if (list.remove(attributeChange.getValue())) {
					result = ApplicationResult.SUCCESSFUL;
				} else if (!ApplicationResult.SUCCESSFUL.equals(result)) {
					// already removed..
					result = ApplicationResult.BOUND;
				}
			}
		}
		return result;
	}

	/**
	 * Add an attribute to a collection of attributes.
	 * 
	 * @return <code>true</code>, if at least one attribute was added.
	 */
	protected ApplicationResult applyAddAttributeChange(IndepAddRemAttributeChange attributeChange,
			Collection<EObject> correspondingElements, AttributeChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// 2. get right elements and perform changes
		for (final EObject element : correspondingElements) {
			if (element != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									attributeChange.getCorrespondingElement(), element));
				}

				// since we know that an attribute was _added_ (and not changed), we have to deal with a list
				@SuppressWarnings("unchecked")
				final EList<Object> list = (EList<Object>) element.eGet(attributeChange.getChangedAttribute());
				
				// already applied?
				if (list.contains(attributeChange.getValue())) {
					if (!ApplicationResult.SUCCESSFUL.equals(result))
						result = ApplicationResult.BOUND;
				} else if (list.add(attributeChange.getValue())) {
					result = ApplicationResult.SUCCESSFUL;
				}
			}
		}
		return result;
	}

	/**
	 * Move all corresponding elements, if possible.
	 * 
	 * @return <code>true</code>, if at least one element was moved.
	 */
	protected ApplicationResult applyMoveElementChange(final IndepMoveElementChange elementChange,
			final Collection<EObject> correspondingElements, final ResolvedSymbolicReferences binding, boolean forward,
			final MoveElementChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// 2. get the new parent (we don't need the old one here)
		final IElementReference newParentReference = forward ? elementChange.getNewParent() : elementChange
				.getOldParent();
		final Collection<EObject> newParents = resolveSymbolicReference(binding, flatDeletions, newParentReference);
		if (newParents != null && newParents.size() == 1) {
			final EObject newParent = newParents.iterator().next();
			if (newParent != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.setNewParent(BindingFactory.eINSTANCE.createElementChangeBinding(newParentReference,
							newParent));
				}

				// 3. get containment
				final EReference containment = forward ? elementChange.getNewContainment() : elementChange
						.getOldContainment();

				// 4. perform the move(s)
				if (containment.isMany()) {
					@SuppressWarnings("unchecked")
					final EList<EObject> list = (EList<EObject>) newParent.eGet(containment);
					for (final EObject moveElement : correspondingElements) {

						// get elements to move
						if (moveElement != null) {
							// already moved?
							if (list.contains(moveElement)) {
								if (!ApplicationResult.SUCCESSFUL.equals(result))
									result = ApplicationResult.BOUND;
							} else if (list.add(moveElement)) {
								result = ApplicationResult.SUCCESSFUL;

								// add element binding, if not null
								if (changeBinding != null) {
									changeBinding.getCorrespondingElements().add(
											BindingFactory.eINSTANCE.createElementChangeBinding(
													elementChange.getCorrespondingElement(), moveElement));
								}
							}
						}
					}

					// we allow movement for single references only for one element and if the reference is 'free'
				} else if (correspondingElements.size() == 1 && !newParent.eIsSet(containment)) {
					final EObject moveElement = correspondingElements.iterator().next();

					// get right element to move
					if (moveElement != null) {

						// is it already moved?
						if (moveElement.equals(newParent.eGet(containment))) {
							if (!ApplicationResult.SUCCESSFUL.equals(result))
								result = ApplicationResult.BOUND;
							
							// add element binding, if not null
							if (changeBinding != null) {
								changeBinding.getCorrespondingElements().add(
										BindingFactory.eINSTANCE.createElementChangeBinding(
												elementChange.getCorrespondingElement(), moveElement));
							}
						} else {
							// lets move it!
							newParent.eSet(containment, moveElement);
							if (moveElement.equals(newParent.eGet(containment))) {
								result = ApplicationResult.SUCCESSFUL;
	
								// add element binding, if not null
								if (changeBinding != null) {
									changeBinding.getCorrespondingElements().add(
											BindingFactory.eINSTANCE.createElementChangeBinding(
													elementChange.getCorrespondingElement(), moveElement));
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Remove all elements from the corresponding elements which satisfy the symbolic reference of the sub model.
	 * 
	 * @param binding
	 * 
	 * @return <code>true</code>, if at least one element was removed.
	 */
	protected ApplicationResult applyRemoveElementChange(final IndepAddRemElementChange elementChange,
			final Collection<EObject> correspondingElements, ResolvedSymbolicReferences binding,
			RemoveElementChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;
		for (final EObject parent : correspondingElements) {
			if (parent != null) {

				// add element binding, if not null
				if (changeBinding != null) {
					changeBinding.getCorrespondingElements().add(
							BindingFactory.eINSTANCE.createElementChangeBinding(
									elementChange.getCorrespondingElement(), parent));
				}

				// 2.1 find the correct element for removal
				final Collection<EObject> resolvedElements = resolveSymbolicReference(binding, flatDeletions,
						elementChange.getSubModel().getSelfReference());

				// 2.2 only children of parent are relevant for us, so lets remove all others
				final List<EObject> elementsToRemove = new ArrayList<EObject>();
				for (EObject element : resolvedElements) {
					if (parent.equals(element.eContainer())) {
						elementsToRemove.add(element);
					}
				}

				// 2.3 now lets remove them!
				for (EObject element : elementsToRemove) {
					EcoreUtil.delete(element);
					if (deletedSubmodels.add(element)) {
						result = ApplicationResult.SUCCESSFUL;
					} else if (!ApplicationResult.SUCCESSFUL.equals(result)) {
						// does not exist..
						result = ApplicationResult.BOUND;
					}
				}
				// update data structures for symbolic reference resolutions
				flatDeletions.addAll(ExtEcoreUtils.flattenEObjects(elementsToRemove));
			}
		}
		return result;
	}

	/**
	 * Add elements by applying subModels to all corresponding elements.
	 * 
	 * @return <code>true</code>, if at least one element was added successfully.
	 */
	protected ApplicationResult applyAddElementChange(final IndepAddRemElementChange elementChange,
			final Collection<EObject> correspondingElements, ResolvedSymbolicReferences binding, final AddElementChangeBinding changeBinding) {
		ApplicationResult result = ApplicationResult.FAILED;

		// maybe the element already exists? then they should have been resolved!
		/*
		 * Fix: resolveSymbolicReferences includes raw resolutions, but this is exactly what we do not want here!
		 * So we get the set of resolved references directly from the binding. 
		 */
//		final Collection<EObject> maybeAddedElements = resolveSymbolicReference(binding, flatDeletions, elementChange.getSubModelReference());
		final Collection<EObject> maybeAddedElements = binding.getResolutionByChange().get(elementChange).get(elementChange.getSubModelReference());
		final Map<EObject, EMap<EObject, IModelDescriptor>> addedElements = new LinkedHashMap<EObject, EMap<EObject, IModelDescriptor>>();
		for (EObject added : maybeAddedElements) {
			final EMap<EObject, IModelDescriptor> descriptors = elementChange.getSubModel().isDescriptorFor(added, false);
			if (descriptors != null) {
				addedElements.put(added, descriptors);
			}
		}
		
		for (final EObject parent : correspondingElements) {
			if (parent != null) {

				// already existing to this parent?
				boolean found = false;
				for (EObject added : addedElements.keySet()) {
					if (parent.equals(added.eContainer())) {
						if (!ApplicationResult.SUCCESSFUL.equals(result))
							result = ApplicationResult.BOUND;

						final EMap<EObject, IModelDescriptor> submodelDescriptors = addedElements.get(added);
						for (EObject element : submodelDescriptors.keySet()) {
							final IElementReference selfReference = submodelDescriptors.get(element).getSelfReference();
							addElementToSetMap(selfReference, element, addedElementSymrefToElementsMap);
						}
						
						// add element binding, if not null
						if (changeBinding != null) {
							final SubModelBinding subBinding = BindingFactory.eINSTANCE.createSubModelBinding(
									submodelDescriptors, added, elementChange.getCorrespondingElement(), parent,
									addedElementToSubModelBindingMap);
							changeBinding.getSubModelReferences().add(subBinding);
						}
						
						found = true;
					}
				}

				if (!found) {
					// references are added later because there might be cyclic cross references with other added elements
					final EMap<EObject, IModelDescriptor> submodelDescriptors = elementChange.getSubModel().applyStructure(
							parent, elementChange.getContainment());
					if (submodelDescriptors != null && !submodelDescriptors.isEmpty()) {
						result = ApplicationResult.SUCCESSFUL; // return true if it was successfully applied at least once!
	
						for (EObject element : submodelDescriptors.keySet()) {
							final IElementReference selfReference = submodelDescriptors.get(element).getSelfReference();
							addElementToSetMap(selfReference, element, addedElementSymrefToElementsMap);
							addedElementToChangeMap.put(element, elementChange);
							addedElementToModelDescriptorMap.put(element, submodelDescriptors.get(element));
							if (selfReference.equals(elementChange.getSubModel().getSelfReference())) {
								// addedSubmodels.add(element); // obsolete because we introduced modeldescriptorreferences
	
								// add element binding, if not null
								if (changeBinding != null) {
									final SubModelBinding subBinding = BindingFactory.eINSTANCE.createSubModelBinding(
											submodelDescriptors, element, elementChange.getCorrespondingElement(), parent,
											addedElementToSubModelBindingMap);
									changeBinding.getSubModelReferences().add(subBinding);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/** Helper method to add an element to a map of set of elements. */
	protected static <T, S> void addElementToSetMap(T key, S element, Map<T, Set<S>> map) {
		Set<S> set = map.get(key);
		if (set == null) {
			set = new HashSet<S>();
			map.put(key, set);
		}
		set.add(element);
	}

	/**
	 * Resolve the given symbolic reference to concrete model elements. Take into account that other elements might have
	 * been added or deleted in the meantime.
	 * 
	 * @param binding
	 *            The initial resolution of all symbolic references.
	 * @param flatDeletions
	 *            A set of recently deleted elements.
	 * @param symref
	 *            The symbolic reference which should be resolved.
	 * @return A collection of all resolved model elements.
	 */
	protected Collection<EObject> resolveSymbolicReference(ResolvedSymbolicReferences binding,
			Collection<EObject> flatDeletions, IElementReference symref) {

		// if symbolic reference is a ModelDescriptorReference and the model descriptor has already been applied,
		// it is quite easy to resolve ;-) Otherwise we need to go on as before.
		if (symref instanceof ModelDescriptorReference) {
			final IModelDescriptor descriptor = ((ModelDescriptorReference) symref).getResolvesTo();
			final Set<EObject> elements = addedElementSymrefToElementsMap.get(descriptor.getSelfReference());
			if (elements != null)
				return elements;
		}

		// was there a refinement of this reference?
		final Map<IElementReference, List<EObject>> changeResolutions = binding.getResolutionByChange().get(
				MPatchUtil.getChangeFor(symref));
		if (changeResolutions != null && changeResolutions.containsKey(symref)
				&& !changeResolutions.get(symref).isEmpty()) {
			return changeResolutions.get(symref);
		}

		// get results from original binding
		final Collection<EObject> rawResult = binding.getRawResolution().get(symref);

		// prepare resulting data structure
		final Collection<EObject> result = new HashSet<EObject>();

		// only add to the result those elements that have not been deleted
		if (rawResult != null) {
			for (EObject obj : rawResult) {
				if (!flatDeletions.contains(obj)) {
					result.add(obj);
				}
			}
		}

		/*
		 * This is obsolete since we introduced ModelDescriptorReferences! Good: It eliminates an expensive resolve-call
		 * :-)
		 */
		// add to the result those matching elements that have been added earlier
		// for (EObject addedElement : addedSubmodels) {
		// result.addAll(symref.resolve(addedElement));
		// }

		// also add those which resolve to the same elements, useful e.g. for auto-generated ids:
		// for (IElementReference ref : addedElementSymrefToElementsMap.keySet()) {
		// if (ref.resolvesEqual(symref))
		// result.addAll(addedElementSymrefToElementsMap.get(ref));
		// }

		return result;
	}
}
