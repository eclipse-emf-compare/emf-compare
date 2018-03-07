/*******************************************************************************
 * Copyright (c) 2018 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.ide.ui.sirius.internal;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.compare.diagram.ide.ui.internal.accessor.IDiagramNodeAccessor;
import org.eclipse.emf.compare.diagram.ide.ui.internal.contentmergeviewer.diagram.DiagramContentMergeViewer;
import org.eclipse.emf.compare.ide.ui.internal.configuration.EMFCompareConfiguration;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramDescriptionMappingsManager;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramDescriptionMappingsRegistry;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManager;
import org.eclipse.sirius.diagram.business.api.componentization.DiagramMappingsManagerRegistry;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.widgets.Composite;

public class SiriusContentMergeViewer extends DiagramContentMergeViewer {
	private Set<DiagramMappingsManager> siriusMappingManagers = new LinkedHashSet<>();

	private Set<DiagramDescriptionMappingsManager> siriusDescriptionMappingManagers = new LinkedHashSet<>();

	public SiriusContentMergeViewer(Composite parent, EMFCompareConfiguration config) {
		super(parent, config);
	}

	@Override
	protected void updateContent(Object ancestor, Object left, Object right) {
		super.updateContent(ancestor, left, right);
		getSiriusDiagram(ancestor).ifPresent(this::storeManagers);
		getSiriusDiagram(left).ifPresent(this::storeManagers);
		getSiriusDiagram(right).ifPresent(this::storeManagers);
	}

	@Override
	protected void handleDispose(DisposeEvent event) {
		super.handleDispose(event);
		for (DiagramMappingsManager manager : siriusMappingManagers) {
			DiagramMappingsManagerRegistry.INSTANCE.removeDiagramMappingsManagers(manager);
		}
		for (DiagramDescriptionMappingsManager manager : siriusDescriptionMappingManagers) {
			manager.dispose();
		}
		siriusMappingManagers.clear();
		siriusDescriptionMappingManagers.clear();
	}

	private Optional<DDiagram> getSiriusDiagram(Object accessor) {
		if (accessor instanceof IDiagramNodeAccessor) {
			Diagram gmfDiagram = ((IDiagramNodeAccessor)accessor).getOwnedDiagram();
			if (gmfDiagram != null) {
				return Optional.ofNullable((DDiagram)gmfDiagram.getElement());
			}
		}
		return Optional.empty();
	}

	private void storeManagers(DDiagram diagram) {
		siriusMappingManagers
				.add(DiagramMappingsManagerRegistry.INSTANCE.getDiagramMappingsManager(null, diagram));
		siriusDescriptionMappingManagers.add(DiagramDescriptionMappingsRegistry.INSTANCE
				.getDiagramDescriptionMappingsManager(null, diagram.getDescription()));
	}
}
