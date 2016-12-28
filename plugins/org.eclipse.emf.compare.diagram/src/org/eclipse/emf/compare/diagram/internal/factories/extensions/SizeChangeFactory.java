/*******************************************************************************
 * Copyright (c) 2016 EclipseSource Services GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Philip Langer - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.internal.factories.extensions;

import static com.google.common.collect.Collections2.filter;
import static org.eclipse.emf.compare.DifferenceKind.CHANGE;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.fromSide;

import com.google.common.base.Predicate;

import java.util.Collection;

import org.eclipse.emf.compare.AttributeChange;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.diagram.internal.extensions.DiagramDiff;
import org.eclipse.emf.compare.diagram.internal.extensions.ExtensionsFactory;
import org.eclipse.emf.compare.diagram.internal.extensions.SizeChange;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * Factory of size changes.
 * 
 * @author Philip Langer <planger@eclipsesource.com>
 */
public class SizeChangeFactory extends NodeChangeFactory {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#getExtensionKind()
	 */
	@Override
	public Class<? extends Diff> getExtensionKind() {
		return SizeChange.class;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#createExtension()
	 */
	@Override
	public DiagramDiff createExtension() {
		return ExtensionsFactory.eINSTANCE.createSizeChange();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory#setRefiningChanges(org.eclipse.emf.compare.Diff,
	 *      org.eclipse.emf.compare.DifferenceKind, org.eclipse.emf.compare.Diff)
	 */
	@Override
	public void setRefiningChanges(Diff extension, DifferenceKind extensionKind, Diff refiningDiff) {
		if (extensionKind == CHANGE) {
			extension.getRefinedBy().addAll(
					filter(getAllDifferencesForChange(refiningDiff), fromSide(extension.getSource())));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diagram.internal.factories.extensions.NodeChangeFactory#getAllDifferencesForChange(org.eclipse.emf.compare.Diff)
	 */
	@Override
	protected Collection<Diff> getAllDifferencesForChange(final Diff input) {
		final Collection<Diff> diffs = super.getAllDifferencesForChange(input);
		return filter(diffs, new Predicate<Diff>() {
			public boolean apply(Diff diff) {
				return diff instanceof AttributeChange && isSizeChange((AttributeChange)diff)
						&& fromSide(input.getSource()).apply(diff);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.internal.postprocessor.factories.AbstractChangeFactory#isRelatedToAnExtensionMove(org.eclipse.emf.compare.AttributeChange)
	 */
	@Override
	protected boolean isRelatedToAnExtensionChange(AttributeChange input) {
		return isSizeChange(input) && filter(input.getRefines(), isExtensionKind(CHANGE)).isEmpty();
	}

	/**
	 * It checks that the given attribute change concerns a change of coordinates.
	 * 
	 * @param input
	 *            The difference.
	 * @return True if it is a change of coordinates.
	 */
	private static boolean isSizeChange(AttributeChange input) {
		return input.getAttribute() == NotationPackage.Literals.SIZE__HEIGHT
				|| input.getAttribute() == NotationPackage.Literals.SIZE__WIDTH;
	}

}
