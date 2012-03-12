/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.applier;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public enum ApplyStrategy {
	LEFT_TO_RIGHT {
		public ApplyStrategy inverse() {
			return RIGHT_TO_LEFT;
		}
	},
	RIGHT_TO_LEFT {
		public ApplyStrategy inverse() {
			return LEFT_TO_RIGHT;
		}
	};
	public abstract ApplyStrategy inverse();
}
