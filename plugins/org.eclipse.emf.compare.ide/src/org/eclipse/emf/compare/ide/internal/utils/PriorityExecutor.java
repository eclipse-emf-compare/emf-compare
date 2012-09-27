// NOTE This class is _not_ source compatible with the JDK 6. It should only be compiled with the JDK 5, though the compiled ".class" will be binary compatible with 6 since the issues lies in type erasure.
/*******************************************************************************
 * Copyright (c) 2011, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.internal.utils;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This implementation of an Executor service delegate to a fixed thread pool executor, but prioritizes its
 * tasks.
 * <p>
 * Any threads submitted to this pool through the standard {@link #submit(Callable)},
 * {@link #submit(Runnable)} or {@link #submit(Runnable, Object)} methods will be considered
 * {@link Priority#NORMAL normal} priority tasks. Clients need to use the specialized
 * {@link #submit(Callable, Priority)} or {@link #submit(Runnable, Priority)} methods in order to specify
 * tasks of either {@link Priority#LOW low} or {@link Priority#HIGH high} priorities.
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public final class PriorityExecutor implements Executor {
	/** The thread pool to which we'll delegate. */
	private final ExecutorService pool;

	/**
	 * Constructs our executor service.
	 * 
	 * @param poolName
	 *            Name of this thread pool. We'll use this to name the worker threads.
	 */
	public PriorityExecutor(String poolName) {
		final int threadCount = Runtime.getRuntime().availableProcessors() * 2;
		final int initialCapacity = 16;
		final String actualName;
		if (poolName == null || poolName.length() == 0) {
			actualName = "PrioritizedPool"; //$NON-NLS-1$
		} else {
			actualName = poolName;
		}
		final ThreadFactory factory = new NamedPoolThreadFactory(actualName);
		pool = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
				new PriorityBlockingQueue<Runnable>(initialCapacity, new PriorityTaskComparable()), factory);
	}

	/**
	 * Wraps the given callable inside of a prioritized future, and submits it for execution.
	 * 
	 * @param <T>
	 *            Type of the result expected from this task's execution.
	 * @param task
	 *            The task we are to queue for execution.
	 * @param priority
	 *            The priority to associate with this task.
	 * @return The wrapped task.
	 */
	public <T> Future<T> submit(Runnable task, Priority priority) {
		if (task == null) {
			throw new NullPointerException();
		}
		FutureTask<T> ftask = new PriorityFutureTask<T>(Executors.callable(task, (T)null), priority);
		execute(ftask);
		return ftask;
	}

	/**
	 * Wraps the given callable inside of a prioritized future, and submits it for execution.
	 * 
	 * @param <T>
	 *            Type of the result expected from this task's execution.
	 * @param task
	 *            The task we are to queue for execution.
	 * @param priority
	 *            The priority to associate with this task.
	 * @return The wrapped task.
	 */
	public <T> Future<T> submit(Callable<T> task, Priority priority) {
		if (task == null) {
			throw new NullPointerException();
		}
		FutureTask<T> ftask = new PriorityFutureTask<T>(task, priority);
		execute(ftask);
		return ftask;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	public void execute(Runnable command) {
		if (command instanceof PriorityFutureTask<?>) {
			pool.execute(command);
		} else {
			pool.execute(new PriorityFutureTask<Object>(Executors.callable(command), Priority.NORMAL));
		}
	}

	/**
	 * The set of priorities accepted by this executor.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	public static enum Priority {
		/** Low priority tasks will be executed after any other. */
		LOW(2),

		/**
		 * The "normal" priority, tasks will be executed after high priority ones, but before low priority
		 * ones.
		 */
		NORMAL(1),

		/** High priority tasks will be executed as soon as possible. */
		HIGH(0);

		/** Value of this priority. */
		private int value;

		/**
		 * Constructs our enum literal.
		 * 
		 * @param value
		 *            The value associated with this literal.
		 */
		private Priority(int value) {
			this.value = value;
		}

		/**
		 * Returns an integer value associated with this literal.
		 * 
		 * @return The integer value associated with this literal.
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * A prioritized implementation of the {@link FutureTask}.
	 * 
	 * @param <V>
	 *            The type of result expected from this task.
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class PriorityFutureTask<V> extends FutureTask<V> {
		/** Priority associated with this future. */
		private Priority priority;

		/**
		 * Constructs our future task.
		 * 
		 * @param callable
		 *            The callable that is to be executed from this future.
		 * @param priority
		 *            The priority associated with this task.
		 */
		public PriorityFutureTask(Callable<V> callable, Priority priority) {
			super(callable);
			this.priority = priority;
		}

		/**
		 * Returns the priority associated with this task.
		 * 
		 * @return The priority associated with this task.
		 */
		public Priority getPriority() {
			return priority;
		}
	}

	/**
	 * This comparator will be used to order our {@link PriorityFutureTask} inside of the thread pool's
	 * blocking queue.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class PriorityTaskComparable implements Comparator<Runnable> {
		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Runnable o1, Runnable o2) {
			if (o1 instanceof PriorityFutureTask<?> && o2 instanceof PriorityFutureTask<?>) {
				return ((PriorityFutureTask<?>)o1).getPriority().getValue()
						- ((PriorityFutureTask<?>)o2).getPriority().getValue();
			}
			return 0;
		}
	}

	/**
	 * This pool will use this factory to create its worker threads with an intelligible name.
	 * 
	 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
	 */
	private static class NamedPoolThreadFactory implements ThreadFactory {
		/** Name of this pool. We'll use this to name our threads. */
		private final String poolName;

		/** All of our threads will be in this same group. */
		private final ThreadGroup group;

		/** We'll number the threads starting from '1'. */
		private final AtomicInteger threadNumber = new AtomicInteger(1);

		/**
		 * Constructs this pool's thread factory given the pool name.
		 * 
		 * @param poolName
		 *            Name of this thread pool.
		 */
		public NamedPoolThreadFactory(String poolName) {
			this.poolName = poolName;
			final SecurityManager manager = System.getSecurityManager();
			if (manager == null) {
				this.group = Thread.currentThread().getThreadGroup();
			} else {
				this.group = manager.getThreadGroup();
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		public Thread newThread(Runnable r) {
			final String prefix = poolName + "-thread-"; //$NON-NLS-1$
			final Thread thread = new Thread(group, r, prefix + threadNumber.getAndIncrement(), 0);
			if (thread.isDaemon()) {
				thread.setDaemon(false);
			}
			if (thread.getPriority() != Thread.NORM_PRIORITY) {
				thread.setPriority(Thread.NORM_PRIORITY);
			}
			return thread;
		}
	}
}
