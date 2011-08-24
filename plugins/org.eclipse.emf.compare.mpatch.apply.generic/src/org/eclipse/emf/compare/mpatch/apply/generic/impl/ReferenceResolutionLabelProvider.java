/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.apply.generic.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.mpatch.ChangeGroup;
import org.eclipse.emf.compare.mpatch.IElementReference;
import org.eclipse.emf.compare.mpatch.IModelDescriptor;
import org.eclipse.emf.compare.mpatch.IndepAddElementChange;
import org.eclipse.emf.compare.mpatch.IndepChange;
import org.eclipse.emf.compare.mpatch.MPatchPackage;
import org.eclipse.emf.compare.mpatch.ModelDescriptorReference;
import org.eclipse.emf.compare.mpatch.UnknownChange;
import org.eclipse.emf.compare.mpatch.apply.generic.GenericApplyActivator;
import org.eclipse.emf.compare.mpatch.apply.util.MPatchValidator;
import org.eclipse.emf.compare.mpatch.common.util.OverlayImageDescriptor;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences;
import org.eclipse.emf.compare.mpatch.extension.ResolvedSymbolicReferences.ValidationResult;
import org.eclipse.emf.compare.mpatch.util.ExtEcoreUtils;
import org.eclipse.emf.compare.mpatch.util.MPatchUtil;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for viewer for the refinement of symbolic references.
 * <ol>
 * <li>column: default text and image.
 * <li>column: indicating image and number of successfully resolved references.
 * <li>column: detailed information about reference resolution.
 * </ol>
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class ReferenceResolutionLabelProvider extends AdapterFactoryLabelProvider {

	/** Overlay image for successfully resolved references. */
	public static final ImageDescriptor GREEN = ImageDescriptor.createFromURL(GenericApplyActivator.getDefault()
			.getBundle().getEntry("icons/green.png"));

	/** Overlay image for not successfully resolved references. */
	public static final ImageDescriptor BLUE = ImageDescriptor.createFromURL(GenericApplyActivator.getDefault()
			.getBundle().getEntry("icons/blue.png"));

	/** Overlay image for illegal states of the resolved model elements. */
	public static final ImageDescriptor RED = ImageDescriptor.createFromURL(GenericApplyActivator.getDefault()
			.getBundle().getEntry("icons/red.png"));

	/** Overlay image for not selected symbolic references. */
	public static final ImageDescriptor SHADED = ImageDescriptor.createFromURL(GenericApplyActivator.getDefault()
			.getBundle().getEntry("icons/shaded.png"));

	/** The symbolic reference resolution. */
	final private ResolvedSymbolicReferences mapping;

	private final boolean respectApplied;
	
	/**
	 * Default constructor.
	 * 
	 * @see ReferenceResolutionLabelProvider
	 */
	public ReferenceResolutionLabelProvider(ResolvedSymbolicReferences mapping, AdapterFactory adapterFactory, boolean respectApplied) {
		super(adapterFactory);
		this.mapping = mapping;
		this.respectApplied = respectApplied;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0)
			return firstColumnImage(element);

		if (element instanceof ChangeGroup)
			return null; // no image here

		if (element instanceof UnknownChange)
			return null; // no image here

		if (columnIndex == 1) {
			final boolean forward = mapping.getDirection() == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;

			if (element instanceof ModelDescriptorReference)
				return secondColumnDescriptorImage((ModelDescriptorReference) element);

			if (element instanceof IElementReference)
				return secondColumnReferenceImage((IElementReference) element, forward);

			if (element instanceof IndepChange)
				return secondColumnChangeImage((IndepChange) element, forward);
		}
		return null;
	}

	private Image secondColumnChangeImage(IndepChange change, final boolean forward) {
		ImageDescriptor img = null;

		// for changes
		if (mapping.getResolutionByChange().containsKey(change)) {
			for (final IElementReference ref : mapping.getResolutionByChange().get(change).keySet()) {
				/*
				 * Like in MPatchValidator, we skip the self reference of IndepAddRemElementChanges.
				 */
				if (ref.eContainer() instanceof IModelDescriptor)
					continue;
				
				if (!MPatchValidator.validateResolution(ref, mapping.getResolutionByChange().get(change).get(ref))) {
					img = BLUE; // not successfully resolved
					break;
				}
			}
			// resolution is valid! check state next.
			if (img == null) {
				final ValidationResult actualState = mapping.getValidation().get(change);
				
				if (ValidationResult.STATE_BEFORE.equals(actualState)) {
					img = GREEN; // everything ok
				} else if (ValidationResult.STATE_AFTER.equals(actualState) && respectApplied) {
					// TODO: now we got more states.. hence we need more images here to represent them!
					img = GREEN;
				} else {
					img = RED; // state not fulfilled, but resolved
				}
			}
		} else {
			img = SHADED; // not selected
		}
		return img.createImage();
	}

	private Image secondColumnReferenceImage(IElementReference ref, final boolean forward) {
		final ImageDescriptor img;

		// for symbolic references
		final IndepChange change = MPatchUtil.getChangeFor(ref);
		if (!mapping.getResolutionByChange().containsKey(change))
			return null; // change is ignored

		if (mapping.getResolutionByChange().containsKey(change)) {
			if (MPatchValidator.validateResolution(ref, mapping.getResolutionByChange().get(change).get(ref))) {
				/*
				 * PK: The state is checked in secondColumnChangeImage! This is why only resolution should be checked
				 * here!
				 */
				// final ValidationResult state = MPatchValidator.validateElementState(change, mapping
				// .getResolutionByChange().get(change), false, forward);
				// if (ValidationResult.SUCCESSFUL.equals(state)) {
				img = GREEN; // resolution and state ok
				// } else {
				// img = red; // state not fulfilled, but resolved
				// }
			} else {
				img = BLUE; // not successfully resolved
			}
		} else {
			img = SHADED; // not selected
		}

		return img.createImage();
	}

	private Image secondColumnDescriptorImage(ModelDescriptorReference ref) {
		final IndepChange change = MPatchUtil.getChangeFor(ref);
		if (!mapping.getResolutionByChange().containsKey(change))
			return null; // change is ignored

		final IModelDescriptor descriptor = ref.getResolvesTo();
		final IndepChange otherChange = MPatchUtil.getChangeFor(descriptor.getSelfReference());
		if (mapping.getResolutionByChange().containsKey(otherChange))
			return GREEN.createImage(); // depending change is not ignored
		return RED.createImage(); // depending change is ignored!
	}

	private Image firstColumnImage(Object element) {
		// shaded or default?
		final IndepChange change;
		if (element instanceof ChangeGroup)
			return super.getImage(element);
		else if (element instanceof IndepChange)
			change = (IndepChange) element;
		else if (element instanceof IElementReference)
			change = MPatchUtil.getChangeFor((IElementReference) element);
		else
			return super.getImage(element);
		if (!mapping.getResolutionByChange().containsKey(change))
			return createOverlayImage((EObject) element, SHADED, adapterFactory);
		return super.getImage(element);
	}

	protected static Image createOverlayImage(EObject obj, ImageDescriptor imageDescriptor,
			AdapterFactory adapterFactory) {
		final IItemLabelProvider lp = (IItemLabelProvider) adapterFactory.adapt(obj, IItemLabelProvider.class);
		final ImageDescriptor imgDescr = ImageDescriptor.createFromImage(ExtendedImageRegistry.getInstance().getImage(
				lp.getImage(obj)));
		return new OverlayImageDescriptor(imgDescr.createImage(), imageDescriptor).createImage();
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		// the first column shows the default value from the adapter factory
		if (columnIndex == 0)
			return super.getColumnText(element, columnIndex);

		if (columnIndex == 1)
			return secondColumnText(element);

		if (columnIndex == 2) {
			final boolean forward = mapping.getDirection() == ResolvedSymbolicReferences.RESOLVE_UNCHANGED;

			// a summary for groups
			if (element instanceof ChangeGroup)
				return thirdColumnGroupText(element, forward);

			// a summary for changes
			if (element instanceof IndepChange)
				return thirdColumnChangeText(element, forward);

			// for references, give a list of resolved elements
			if (element instanceof ModelDescriptorReference)
				return thirdColumnDescriptorText(element);

			if (element instanceof IElementReference)
				return thirdColumnReferenceText(element);
		}

		return "";
	}

	private String thirdColumnReferenceText(Object element) {
		final IElementReference ref = (IElementReference) element;
		final IndepChange change = MPatchUtil.getChangeFor(ref);
		if (!mapping.getResolutionByChange().containsKey(change))
			return ""; // change is ignored

		// compute list from mapping
		if (mapping.getResolutionByChange().containsKey(change)) {
			String list = "";
			if (mapping.getResolutionByChange().get(change).get(ref) != null) {
				for (final EObject obj : mapping.getResolutionByChange().get(change).get(ref)) {
					list += (list.length() == 0 ? "" : ", ") + getText(obj);
				}
			}
			return "[" + list + "]";
		} else
			return "";
	}

	private String thirdColumnDescriptorText(Object element) {
		final ModelDescriptorReference ref = (ModelDescriptorReference) element;
		final IndepChange change = MPatchUtil.getChangeFor(ref);
		if (!mapping.getResolutionByChange().containsKey(change))
			return ""; // change is ignored

		final IModelDescriptor descriptor = ref.getResolvesTo();
		final IndepChange otherChange = MPatchUtil.getChangeFor(descriptor.getSelfReference());
		if (mapping.getResolutionByChange().containsKey(otherChange))
			return "ok"; // depending change is not ignored
		return "depending change missing"; // depending change is ignored!
	}

	private String thirdColumnChangeText(Object element, final boolean forward) {
		if (mapping.getResolutionByChange().containsKey(element)) {
			final ValidationResult state = mapping.getValidation().get(element);

			// we have a special role for added elements here!
			if (element instanceof IndepAddElementChange) {
				final IndepAddElementChange change = (IndepAddElementChange) element;
				final List<EObject> selfRef = mapping.getResolutionByChange().get(change).get(change.getSubModelReference());
				if (selfRef == null || selfRef.size() == 0) {
					return ResolvedSymbolicReferences.VALIDATION_RESULTS.get(state);
					
				} else { 
					String list = "";
					for (EObject obj : selfRef) {
						list += (list.length() == 0 ? "" : ", ") + getText(obj);
					}
					return "bound: [" + list + "]";
				}
			} else {
				return ResolvedSymbolicReferences.VALIDATION_RESULTS.get(state);
			}
		} else {
			return "ignored";
		}
	}

	private String thirdColumnGroupText(Object element, final boolean forward) {
		final ChangeGroup group = (ChangeGroup) element;
		int resolved = 0;
		int ignored = 0;
		int failed = 0;
		final Set<EClass> types = Collections.singleton(MPatchPackage.eINSTANCE.getIndepChange());
		final List<EObject> changes = ExtEcoreUtils.collectTypedElements(group.getSubChanges(), types, true);

		// collect statistics
		for (EObject change : changes) {
			final ValidationResult actualState = mapping.getValidation().get(change);
			if (!mapping.getResolutionByChange().containsKey(change))
				ignored++;
			else if (!ValidationResult.STATE_BEFORE.equals(actualState) && !ValidationResult.STATE_AFTER.equals(actualState))
				failed++;
			else
				resolved++;
		}
		return "resolved: " + resolved + "; ignored: " + ignored + "; failed: " + failed;
	}

	private String secondColumnText(Object element) {
		// here we give some numbers but only for symbolic references!
		if (element instanceof IElementReference && !(element instanceof ModelDescriptorReference)) {
			final IElementReference ref = (IElementReference) element;
			final IndepChange change = MPatchUtil.getChangeFor(ref);

			// get the number of elements which are resolved
			if (mapping.getResolutionByChange().containsKey(change)) {
				if (mapping.getResolutionByChange().get(change).get(ref) != null) {
					return String.valueOf(mapping.getResolutionByChange().get(change).get(ref).size());
				} else
					return "0";
			} else
				return "";
		} else
			return "";
	}
}
