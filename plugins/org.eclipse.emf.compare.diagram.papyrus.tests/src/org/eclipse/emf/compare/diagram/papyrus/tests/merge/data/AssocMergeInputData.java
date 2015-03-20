/**
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.diagram.papyrus.tests.merge.data;

import java.io.IOException;

import org.eclipse.emf.compare.diagram.papyrus.tests.DiagramInputData;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * Provides input models to the unit tests of the matching by id.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("nls")
public class AssocMergeInputData extends DiagramInputData {

	public Resource get2WayA1Left() throws IOException {
		return loadFromClassLoader("assocs/twoway/a1/left.notation");
	}

	public Resource get2WayA1Right() throws IOException {
		return loadFromClassLoader("assocs/twoway/a1/right.notation");
	}

	public Resource get2WayA2Left() throws IOException {
		return loadFromClassLoader("assocs/twoway/a2/left.notation");
	}

	public Resource get2WayA2Right() throws IOException {
		return loadFromClassLoader("assocs/twoway/a2/right.notation");
	}

	public Resource get2WayT1Left() throws IOException {
		return loadFromClassLoader("assocs/twoway/t1/left.notation");
	}

	public Resource get2WayT1Right() throws IOException {
		return loadFromClassLoader("assocs/twoway/t1/right.notation");
	}

	public Resource get3WayC1Ancestor() throws IOException {
		return loadFromClassLoader("assocs/threeway/c1/ancestor.notation");
	}

	public Resource get3WayC1Left() throws IOException {
		return loadFromClassLoader("assocs/threeway/c1/left.notation");
	}

	public Resource get3WayC1Right() throws IOException {
		return loadFromClassLoader("assocs/threeway/c1/right.notation");
	}

	public Resource get3WayT1Ancestor() throws IOException {
		return loadFromClassLoader("assocs/threeway/t1/ancestor.notation");
	}

	public Resource get3WayT1Left() throws IOException {
		return loadFromClassLoader("assocs/threeway/t1/left.notation");
	}

	public Resource get3WayT1Right() throws IOException {
		return loadFromClassLoader("assocs/threeway/t1/right.notation");
	}

	public Resource getConflictC1Ancestor() throws IOException {
		return loadFromClassLoader("assocs/conflict/c1/ancestor.notation");
	}

	public Resource getConflictC1Left() throws IOException {
		return loadFromClassLoader("assocs/conflict/c1/left.notation");
	}

	public Resource getConflictC1Right() throws IOException {
		return loadFromClassLoader("assocs/conflict/c1/right.notation");
	}

	public Resource getConflictT1Ancestor() throws IOException {
		return loadFromClassLoader("assocs/conflict/t1/ancestor.notation");
	}

	public Resource getConflictT1Left() throws IOException {
		return loadFromClassLoader("assocs/conflict/t1/left.notation");
	}

	public Resource getConflictT1Right() throws IOException {
		return loadFromClassLoader("assocs/conflict/t1/right.notation");
	}
}
