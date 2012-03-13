/*******************************************************************************
 * Copyright (c) 2008, 2012 Dimitrios Kolovos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ui.internal;

/**
 * This runnable implementation allows us to keep a reference to the execution result.
 * 
 * @author <a href="dkolovos@cs.york.ac.uk">Dimitrios Kolovos</a>
 * @since 1.0
 */
public abstract class AbstractReturningRunnable implements Runnable {
	/** Result of the execution. */
	private Object result;

	/**
	 * Returns the result of the runnable's execution.
	 * 
	 * @return Result of the execution.
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		result = runImpl();
	}

	/**
	 * Clients should override this insteas of {@link #run()} to implement this runnable's logic.
	 * 
	 * @return The execution result.
	 */
	public abstract Object runImpl();
}
