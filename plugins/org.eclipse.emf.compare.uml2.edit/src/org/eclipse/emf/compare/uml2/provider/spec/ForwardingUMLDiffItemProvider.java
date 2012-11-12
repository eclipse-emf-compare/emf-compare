/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.uml2.provider.spec;

import com.google.common.base.Preconditions;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.DifferenceSource;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.provider.ForwardingItemProvider;
import org.eclipse.emf.compare.provider.spec.Strings;
import org.eclipse.emf.compare.uml2.StereotypeApplicationChange;
import org.eclipse.emf.compare.uml2.UMLDiff;
import org.eclipse.emf.compare.uml2.diff.internal.util.UMLCompareUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.util.UMLUtil;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class ForwardingUMLDiffItemProvider extends ForwardingItemProvider {

	/**
	 * @param delegate
	 */
	public ForwardingUMLDiffItemProvider(ItemProviderAdapter delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.utils.ForwardingItemProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Collection<?> getChildren(Object object) {
		final Collection<?> ret;
		UMLDiff umlDiff = (UMLDiff)object;
		if (umlDiff.getKind() == DifferenceKind.CHANGE) {
			ret = umlDiff.getRefinedBy();
		} else {
			ret = super.getChildren(object);
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.utils.ForwardingItemProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object object) {
		return !getChildren(object).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.uml2.internal.utils.ForwardingItemProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;

		String remotely = "";
		if (umlDiff.getSource() == DifferenceSource.RIGHT) {
			remotely = "remotely ";
		}

		// FIXME extract this to its own ItemProvider. instanceof in getReferenceText can be deleted as well.
		if (umlDiff instanceof StereotypeApplicationChange) {
			Stereotype stereotype = ((StereotypeApplicationChange)umlDiff).getStereotype();
			if (stereotype == null) {
				stereotype = UMLUtil.getStereotype(umlDiff.getDiscriminant());
			}
			final String stereotypeName;
			if (stereotype != null) {
				stereotypeName = stereotype.getName() + ' ';
			} else if (umlDiff.getDiscriminant() instanceof NamedElement) {
				stereotypeName = ((NamedElement)umlDiff.getDiscriminant()).getName() + ' ';
			} else {
				// Can't really do more
				stereotypeName = "";
			}

			final Match targetMatch = umlDiff.getMatch();
			final EObject target = findNonNullSide(targetMatch);
			String targetLabel = null;

			final String action;
			switch (umlDiff.getKind()) {
				case ADD:
					action = "applied";
					targetLabel = " to ";
					break;
				case DELETE:
					action = "removed";
					targetLabel = " from ";
					break;
				case CHANGE:
					action = "changed";
					targetLabel = " on ";
					break;
				case MOVE:
					action = "moved";
					targetLabel = " to ";
					break;
				default:
					throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
							+ " value: " + umlDiff.getKind());
			}

			if (target != null) {
				targetLabel += getText(getRootAdapterFactory(), target) + '.';
			} else {
				targetLabel = ".";
			}

			return "Stereotype " + stereotypeName + "has been " + remotely + action + targetLabel;
		}

		final String valueText = getValueText(umlDiff);
		final String referenceText = getReferenceText(umlDiff);

		String ret = "";
		switch (umlDiff.getKind()) {
			case ADD:
				ret = valueText + " has been " + remotely + "added to " + referenceText;
				break;
			case DELETE:
				ret = valueText + " has been " + remotely + "deleted from " + referenceText;
				break;
			case CHANGE:
				ret = valueText + " has been " + remotely + "changed";
				break;
			case MOVE:
				ret = valueText + " has been " + remotely + "moved in " + referenceText;
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + umlDiff.getKind());
		}

		return ret;
	}

	/**
	 * Tries and find a non-<code>null</code> side for the given match. Still returns <code>null</code> if all
	 * three sides are <code>null</code> though.
	 * 
	 * @param match
	 *            The match for which we need a non-<code>null</code> side.
	 * @return The first side (in order : left, right or origin) that was not <code>null</code>.
	 */
	private EObject findNonNullSide(Match match) {
		final EObject side;
		if (match.getLeft() != null) {
			side = match.getLeft();
		} else if (match.getRight() != null) {
			side = match.getRight();
		} else {
			side = match.getOrigin();
		}
		return side;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.provider.ReferenceChangeItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		final UMLDiff umlDiff = (UMLDiff)object;
		Object image = getImage(getRootAdapterFactory(), umlDiff.getDiscriminant());
		return image;
	}

	private String getValueText(final UMLDiff umlDiff) {
		String value = getText(getRootAdapterFactory(), umlDiff.getDiscriminant());
		if (value == null) {
			value = "<null>";
		} else {
			value = Strings.elide(value, 50, "...");
		}
		return value;
	}

	private String getReferenceText(final UMLDiff umlDiff) {
		String ret = "";
		switch (umlDiff.getKind()) {
			case ADD:
			case DELETE:
			case MOVE:
				EObject discriminant = umlDiff.getDiscriminant();
				// FIXME create a specific item provider for each diff ... this is not maintainable.
				if (umlDiff instanceof StereotypeApplicationChange) {
					ret = UMLCompareUtil.getBaseElement(discriminant).eContainingFeature().getName();
				} else {
					ret = discriminant.eContainingFeature().getName();
				}
				break;
			case CHANGE:
				break;
			default:
				throw new IllegalStateException("Unsupported " + DifferenceKind.class.getSimpleName()
						+ " value: " + umlDiff.getKind());
		}
		return ret;
	}

	/**
	 * Returns the text of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getText(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a text
	 * @return the text, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	private static String getText(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			Object adapter = adapterFactory.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getText(object);
			}
		}
		return null;
	}

	/**
	 * Returns the image of the given <code>object</code> by adapting it to {@link IItemLabelProvider} and
	 * asking for its {@link IItemLabelProvider#getImage(Object) text}. Returns null if <code>object</code> is
	 * null.
	 * 
	 * @param adapterFactory
	 *            the adapter factory to adapt from
	 * @param object
	 *            the object from which we want a image
	 * @return the image, or null if object is null.
	 * @throws NullPointerException
	 *             if <code>adapterFactory</code> is null.
	 */
	private static Object getImage(AdapterFactory adapterFactory, Object object) {
		Preconditions.checkNotNull(adapterFactory);
		if (object != null) {
			Object adapter = adapterFactory.adapt(object, IItemLabelProvider.class);
			if (adapter instanceof IItemLabelProvider) {
				return ((IItemLabelProvider)adapter).getImage(object);
			}
		}
		return null;
	}
}
