/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diff.merge.internal;

import java.util.Map;

import org.eclipse.emf.compare.diff.merge.api.IMerger;
import org.eclipse.emf.compare.diff.merge.api.IMergerProvider;
import org.eclipse.emf.compare.diff.merge.internal.impl.AttributeChangeLeftTargetMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.AttributeChangeRightTargetMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.ModelElementChangeLeftTargetMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.ModelElementChangeRightTargetMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.MoveModelElementMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.ReferenceChangeLeftTargetMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.ReferenceChangeRightTargetMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.UpdateAttributeMerger;
import org.eclipse.emf.compare.diff.merge.internal.impl.UpdateUniqueReferenceValueMerger;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.AttributeChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.MoveModelElement;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.UpdateAttribute;
import org.eclipse.emf.compare.diff.metamodel.UpdateUniqueReferenceValue;
import org.eclipse.emf.compare.util.EMFCompareMap;

/**
 * This will associate all of the basic {@link DiffElement}s with generic merger implementations.
 * 
 * @author Laurent Goubet <a href="mailto:laurent.goubet@obeo.fr">laurent.goubet@obeo.fr</a>
 */
public class DefaultMergerProvider implements IMergerProvider {
	/**
	 * This map keeps a bridge between a given {@link DiffElement}'s class and the most accurate merger's
	 * class for that particular {@link DiffElement}.
	 */
	private Map<Class<? extends DiffElement>, Class<? extends IMerger>> mergerTypes;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.IMergerProvider#getMergers()
	 */
	public Map<Class<? extends DiffElement>, Class<? extends IMerger>> getMergers() {
		if (mergerTypes == null) {
			mergerTypes = new EMFCompareMap<Class<? extends DiffElement>, Class<? extends IMerger>>();
			mergerTypes.put(ModelElementChangeRightTarget.class, ModelElementChangeRightTargetMerger.class);
			mergerTypes.put(ModelElementChangeLeftTarget.class, ModelElementChangeLeftTargetMerger.class);
			mergerTypes.put(MoveModelElement.class, MoveModelElementMerger.class);
			mergerTypes.put(ReferenceChangeRightTarget.class, ReferenceChangeRightTargetMerger.class);
			mergerTypes.put(ReferenceChangeLeftTarget.class, ReferenceChangeLeftTargetMerger.class);
			mergerTypes.put(UpdateUniqueReferenceValue.class, UpdateUniqueReferenceValueMerger.class);
			mergerTypes.put(AttributeChangeRightTarget.class, AttributeChangeRightTargetMerger.class);
			mergerTypes.put(AttributeChangeLeftTarget.class, AttributeChangeLeftTargetMerger.class);
			mergerTypes.put(UpdateAttribute.class, UpdateAttributeMerger.class);
		}
		return mergerTypes;
	}
}
