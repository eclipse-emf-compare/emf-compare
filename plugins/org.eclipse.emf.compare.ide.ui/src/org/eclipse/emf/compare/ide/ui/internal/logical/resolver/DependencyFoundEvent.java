/*******************************************************************************
 * Copyright (c) 2015 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Philip Langer - refactorings
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import com.google.common.base.Optional;

/**
 * Event indicating a dependency between two resources has been found.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <T>
 *            The type of the key used to identify the resources (will be URI in our case).
 */
public class DependencyFoundEvent<T> {

	/** Source of the dependency. */
	private final T from;

	/** Target of the dependency. */
	private final T to;

	/** Key of the object that owns this dependency */
	private final Optional<T> parent;

	/**
	 * Constructor.
	 * 
	 * @param from
	 *            The source
	 * @param to
	 *            The target
	 * @param parent
	 *            The key of the object at the source causing the dependency
	 */
	public DependencyFoundEvent(T from, T to, Optional<T> parent) {
		this.from = from;
		this.to = to;
		this.parent = parent;
	}

	/**
	 * Constructor.
	 * 
	 * @param from
	 *            The source
	 * @param to
	 *            The target
	 */
	public DependencyFoundEvent(T from, T to) {
		this.from = from;
		this.to = to;
		this.parent = Optional.absent();
	}

	public T getFrom() {
		return from;
	}

	public T getTo() {
		return to;
	}

	public Optional<T> getParent() {
		return parent;
	}

	public boolean hasParent() {
		return parent.isPresent();
	}
}
