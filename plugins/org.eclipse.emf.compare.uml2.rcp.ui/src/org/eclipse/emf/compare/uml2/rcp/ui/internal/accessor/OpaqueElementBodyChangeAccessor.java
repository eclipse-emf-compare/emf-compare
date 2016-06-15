/*******************************************************************************
 * Copyright (c) 2014 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.rcp.ui.internal.accessor;

import com.google.common.base.Optional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.IStreamContentAccessor;
import org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.impl.AbstractTypedElementAdapter;
import org.eclipse.emf.compare.rcp.ui.internal.contentmergeviewer.TypeConstants;
import org.eclipse.emf.compare.rcp.ui.internal.util.MergeViewerUtil;
import org.eclipse.emf.compare.rcp.ui.mergeviewer.IMergeViewer.MergeViewerSide;
import org.eclipse.emf.compare.uml2.internal.OpaqueElementBodyChange;
import org.eclipse.emf.compare.utils.ReferenceUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.uml2.uml.OpaqueAction;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * An accessor for {@link OpaqueElementBodyChange opaque element body changes} that represent <i>additions</i>
 * , <i>deletions</i>, and <i>changes</i> (that is, everything except for moves) of body values of
 * {@link OpaqueAction}, {@link OpaqueBehavior}, and {@link OpaqueExpression}. For such changes of body
 * values, we show the added/deleted/changed body value of the left-hand side and the right-hand side in a
 * textual diff viewer.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class OpaqueElementBodyChangeAccessor extends AbstractTypedElementAdapter implements IStreamContentAccessor {

	/** The change to be accessed by this accessor. */
	private OpaqueElementBodyChange bodyChange;

	/** The current side's object affected by {@link #bodyChange}. */
	private final EObject eObject;

	/**
	 * Creates a new accessor for {@link OpaqueElementBodyChange opaque element body changes} that represent
	 * additions, deletions, and changes of body values.
	 * 
	 * @param adapterFactory
	 *            the adapater factory used to create the accessor.
	 * @param bodyChange
	 *            The change to be accessed by this accessor.
	 * @param side
	 *            The side of this accessor.
	 */
	public OpaqueElementBodyChangeAccessor(AdapterFactory adapterFactory, OpaqueElementBodyChange bodyChange,
			MergeViewerSide side) {
		super(adapterFactory);
		this.bodyChange = bodyChange;
		this.eObject = MergeViewerUtil.getEObject(bodyChange.getMatch(), side);
		if (DifferenceKind.MOVE.equals(bodyChange.getKind())) {
			throw new IllegalArgumentException(
					"This accessor handles only additions, deletions, and changes of body values."); //$NON-NLS-1$
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.IStreamContentAccessor#getContents()
	 */
	public InputStream getContents() throws CoreException {
		final String language = bodyChange.getLanguage();
		final byte[] contents = getBodyAsByte(language).or(new byte[0]);
		return new ByteArrayInputStream(contents);
	}

	/**
	 * Returns the optional body value of {@link #eObject} for the given {@code language} as a byte array.
	 *
	 * @param language
	 *            The language to get the body value for.
	 * @return The optional body value of {@link #eObject} for {@code language}.
	 */
	private Optional<byte[]> getBodyAsByte(String language) {
		final Optional<byte[]> body;
		final List<String> languages = getLanguageValues();
		final List<String> bodies = getBodyValues();
		if (languages.contains(language) && bodies.size() > languages.indexOf(language)) {
			body = Optional.of(bodies.get(languages.indexOf(language)).getBytes());
		} else {
			body = Optional.absent();
		}
		return body;
	}

	/**
	 * Returns the values of the {@link #getLanguageFeature() language feature} of {@link #eObject}.
	 * 
	 * @return The values of the language feature of {@link #eObject}.
	 */
	@SuppressWarnings("unchecked")
	private List<String> getLanguageValues() {
		return (List<String>)ReferenceUtil.safeEGet(eObject, getLanguageFeature());
	}

	/**
	 * Returns the values of the {@link #getBodyFeature() body feature} of {@link #eObject}.
	 * 
	 * @return The values of the body feature of {@link #eObject}.
	 */
	@SuppressWarnings("unchecked")
	private List<String> getBodyValues() {
		return (List<String>)ReferenceUtil.safeEGet(eObject, getBodyFeature());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getName()
	 */
	public String getName() {
		return this.getClass().getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getImage()
	 */
	public Image getImage() {
		return ExtendedImageRegistry.INSTANCE.getImage(getItemDelegator().getImage(getBodyFeature()));
	}

	/**
	 * Returns the body feature of {@link #eObject} depending on whether it is an {@link OpaqueAction},
	 * {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * <p>
	 * If {@link #eObject} is not any of those types, an {@link IllegalArgumentException} is thrown, since
	 * this must never happen and something beforehand went horribly wrong.
	 * </p>
	 * 
	 * @return The body feature of {@link #eObject}.
	 */
	private EStructuralFeature getBodyFeature() {
		final EStructuralFeature bodyFeature;
		if (eObject instanceof OpaqueAction) {
			bodyFeature = UMLPackage.eINSTANCE.getOpaqueAction_Body();
		} else if (eObject instanceof OpaqueBehavior) {
			bodyFeature = UMLPackage.eINSTANCE.getOpaqueBehavior_Body();
		} else if (eObject instanceof OpaqueExpression) {
			bodyFeature = UMLPackage.eINSTANCE.getOpaqueExpression_Body();
		} else {
			throw new IllegalArgumentException("Cannot get body feature of the class " //$NON-NLS-1$
					+ eObject.eClass().getName());
		}
		return bodyFeature;
	}

	/**
	 * Returns the language feature of {@link #eObject} depending on whether it is an {@link OpaqueAction},
	 * {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * <p>
	 * If {@link #eObject} is not any of those types, an {@link IllegalArgumentException} is thrown, since
	 * this must never happen and something beforehand went horribly wrong.
	 * </p>
	 * 
	 * @return The language feature of {@link #eObject}.
	 */
	private EStructuralFeature getLanguageFeature() {
		return getLanguageFeature(eObject);
	}

	/**
	 * Returns the language feature of the given {@code object} depending on whether it is an
	 * {@link OpaqueAction}, {@link OpaqueBehavior}, or {@link OpaqueExpression}.
	 * <p>
	 * If {@code object} is not any of those types, an {@link IllegalArgumentException} is thrown, since this
	 * must never happen and something beforehand went horribly wrong.
	 * </p>
	 * 
	 * @param object
	 *            The instance of {@link OpaqueAction}, {@link OpaqueBehavior}, or {@link OpaqueExpression} to
	 *            get the language feature for.
	 * @return The language feature of {@link #eObject}.
	 */
	private EStructuralFeature getLanguageFeature(final EObject object) {
		final EStructuralFeature languageFeature;
		if (object instanceof OpaqueAction) {
			languageFeature = UMLPackage.eINSTANCE.getOpaqueAction_Language();
		} else if (object instanceof OpaqueBehavior) {
			languageFeature = UMLPackage.eINSTANCE.getOpaqueBehavior_Language();
		} else if (object instanceof OpaqueExpression) {
			languageFeature = UMLPackage.eINSTANCE.getOpaqueExpression_Language();
		} else {
			throw new IllegalArgumentException("Cannot get language feature of the class " //$NON-NLS-1$
					+ object.eClass().getName());
		}
		return languageFeature;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.rcp.ui.contentmergeviewer.accessor.legacy.ITypedElement#getType()
	 */
	public String getType() {
		return TypeConstants.TYPE_ETEXT_DIFF;
	}

}
