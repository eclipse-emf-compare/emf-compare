/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Martin Fleck - bug 512677
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.internal.logical.resolver;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;

/**
 * Class providing facilities to schedule computations and hide as much as possible the multi-threaded
 * complexity. Each computation is identified by a key. Each computation is supposed to possibly load
 * resources, and the scheduler also provides a facility to unload resources that are no longer needed.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 * @param <T>
 *            type of keys used to keep track of currently ongoing computations.
 */
public class ResourceComputationScheduler<T> {

	/**
	 * Keeps track of the keys which we are currently computing (or which are queued for computation).
	 * <p>
	 * This along with {@link #computedKeys} will prevent multiple "duplicate" computation threads to be
	 * queued. We assume that this will be sufficient to prevent duplicates, and the computation threads
	 * themselves won't check whether their target has already been computed before starting.
	 * </p>
	 */
	private final Set<T> currentlyComputing;

	/**
	 * We'll keep track of what's already been computed to avoid duplicate jobs.
	 */
	private volatile Set<T> computedKeys;

	/** Thread pool for our resolving threads. */
	private ListeningExecutorService computingPool;

	/** Thread pool for our unloading threads. */
	private ListeningExecutorService unloadingPool;

	/**
	 * An executor service will be used to shut down the {@link #unloadingPool} and the {@link #computingPool}
	 * .
	 */
	private ListeningExecutorService terminator;

	/** Tracks if shutdown of {@link #unloadingPool} and {@link #computingPool} is currently in progress. */
	private final AtomicBoolean shutdownInProgress;

	/**
	 * This will lock will prevent concurrent modifications of this class's fields. Most notably,
	 * {@link #currentlyComputing} and {@link #computedKeys} must not be accessed concurrently by two threads
	 * at once.
	 */
	private final ReentrantLock lock;

	/** Condition to await for all current task threads to terminate. */
	private final Condition endOfTasks;

	/** How long to wait for task completion when shutting down the pools. */
	private final int shutdownWaitDuration;

	/** Unit of the above duration. */
	private final TimeUnit shutdownWaitUnit;

	/** Event bus used to send state change events */
	private final EventBus eventBus;

	/**
	 * Constructor, configured to wait for tasks completion for 5 seconds (will wait at most 10 seconds).
	 */
	public ResourceComputationScheduler() {
		this(5, TimeUnit.SECONDS);
	}

	/**
	 * Constructor.
	 * 
	 * @param shutdownWaitDuration
	 *            Time to wait for current tasks completion when shutting down the pools (will wait at most
	 *            twice this amount of time).
	 * @param shutdownWaitUnit
	 *            Unit to use to interpret the other parameter.
	 */
	public ResourceComputationScheduler(int shutdownWaitDuration, TimeUnit shutdownWaitUnit) {
		this(shutdownWaitDuration, shutdownWaitUnit, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param shutdownWaitDuration
	 *            Time to wait for current tasks completion when shutting down the pools (will wait at most
	 *            twice this amount of time).
	 * @param shutdownWaitUnit
	 *            Unit to use to interpret the other parameter.
	 * @param eventBus
	 *            The {@link EventBus} used to post events (shutdown events), can be {@code null}
	 */
	public ResourceComputationScheduler(int shutdownWaitDuration, TimeUnit shutdownWaitUnit,
			EventBus eventBus) {
		this.lock = new ReentrantLock(true);
		this.endOfTasks = lock.newCondition();
		this.currentlyComputing = new HashSet<T>();
		this.shutdownInProgress = new AtomicBoolean(false);
		this.shutdownWaitDuration = shutdownWaitDuration;
		this.shutdownWaitUnit = shutdownWaitUnit;
		this.eventBus = eventBus;
	}

	/**
	 * Creates the thread pools of this resolver and instantiates the computedElements Set. We cannot keep
	 * pools between resolving calls because in case of cancellation, we have to shutdown the pool to exit
	 * early.
	 * <p>
	 * <b>Pre-conditions:</b>
	 * <ul>
	 * <li>{@link #initialize()} has been called</li>
	 * <li>A lock has been acquired before on this instance's {@link #lock}</li>
	 * <li>{@link #dispose()} has not been called</li>
	 * </ul>
	 * <b>Post-conditions:</b>
	 * <ul>
	 * <li>{@link #computingPool} is not null and is ready to be used</li>
	 * <li>{@link #unloadingPool} is not null and is ready to be used</li>
	 * <li>{@link #computedKeys} is not null</li>
	 * </p>
	 */
	private void setUpComputation() {
		final int availableProcessors = Runtime.getRuntime().availableProcessors();
		ThreadFactory computingThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat("EMFCompare-ResolvingThread-%d") //$NON-NLS-1$
				.build();
		this.computingPool = MoreExecutors.listeningDecorator(
				Executors.newFixedThreadPool(availableProcessors, computingThreadFactory));
		ThreadFactory unloadingThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat("EMFCompare-UnloadingThread-%d") //$NON-NLS-1$
				.build();
		this.unloadingPool = MoreExecutors.listeningDecorator(
				Executors.newFixedThreadPool(availableProcessors, unloadingThreadFactory));
		computedKeys = new LinkedHashSet<T>();
	}

	/**
	 * Shutdown the pools and delete {@link #computedKeys}.
	 * <p>
	 * <b>Post-conditions:</b>
	 * <ul>
	 * <li>{@link #computingPool} is null and is ready to be used</li>
	 * <li>{@link #unloadingPool} is null and is ready to be used</li>
	 * <li>{@link #computedKeys} is null</li>
	 * </p>
	 */
	private void tearDownComputation() {
		if (!shutdownInProgress.get()) {
			shutdownPools();
		}
		computedKeys = null;
	}

	/**
	 * If {@link #shutdownInProgress shutdown has not been requested before}, it submits a new task to
	 * {@link #shutdownPools() shut down} {@link #computingPool} and {@link #unloadingPool}. Do nothing if
	 * current thread already is interrupted. If a shutdown is actually started, events will be posted to the
	 * scheduler's eventBus if there is one. The events will be:
	 * <ol>
	 * <li>STARTED</li>
	 * <li>SUCCESS if shutdown has succeeded or FAILURE if shutdown has failed</li>
	 * </ol>
	 * <b>Note</b> that these events will be sent in the calling Thread.
	 */
	public void demandShutdown() {
		if (!Thread.currentThread().isInterrupted()) {
			if (shutdownInProgress.compareAndSet(false, true)) {
				if (eventBus != null) {
					eventBus.post(ShutdownStatus.STARTED);
				}
				Runnable runnable = new Runnable() {
					public void run() {
						shutdownPools();
					}
				};

				ListenableFuture<?> listenableFuture = terminator.submit(runnable);
				Futures.addCallback(listenableFuture, new FutureCallback<Object>() {
					public void onSuccess(Object result) {
						shutdownInProgress.set(false);
						if (eventBus != null) {
							eventBus.post(ShutdownStatus.SUCCESS);
						}
					}

					public void onFailure(Throwable t) {
						shutdownInProgress.set(false);
						if (eventBus != null) {
							eventBus.post(new ShutdownStatus(t));
						}
						EMFCompareIDEUIPlugin.getDefault().log(t);
					}
				});
			}
		}
	}

	/**
	 * Shutdown {@link #computingPool} and {@link #unloadingPool} and set these two fields to null.
	 */
	private synchronized void shutdownPools() {
		try {
			// we don't do the closing in one sentence to make sure we call both shutdown methods
			if (computingPool != null) {
				shutdownAndAwaitTermination(computingPool);
			}
			if (unloadingPool != null) {
				shutdownAndAwaitTermination(unloadingPool);
			}
		} finally {
			computingPool = null;
			unloadingPool = null;
		}
	}

	/**
	 * Initializes this scheduler, which instantiates its {@link #terminator}. Can be called several times
	 * with no problem.
	 */
	public synchronized void initialize() {
		if (!isInitialized()) {
			this.terminator = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(
					new ThreadFactoryBuilder().setNameFormat("EMFCompare-ThreadPoolShutdowner-%d") //$NON-NLS-1$
							.setPriority(Thread.MAX_PRIORITY).build()));
		}
	}

	/**
	 * Indicates whether this scheduler is initialized, i.e. it can run computations.
	 * 
	 * @return {@code true} if and only if the scheduler is initialized.
	 */
	public boolean isInitialized() {
		return terminator != null;
	}

	/**
	 * Disposes this scheduler, which shuts down its {@link #terminator}.
	 */
	public synchronized void dispose() {
		if (isInitialized()) {
			terminator.shutdown();
			terminator = null;
		}
	}

	/**
	 * Executes the given callable as soon as possible. Whatever happens, the given callback is run before
	 * returning (in a "finally" clause) and then the "notComputing" condition will be signalled and the lock
	 * released.
	 * <p>
	 * <b>Pre-conditions:</b>
	 * <ul>
	 * <li>{@link #initialize()} has been called</li>
	 * <li>{@link #dispose()} has not been called</li>
	 * </ul>
	 * </p>
	 * If the scheduler has an eventBus, it will post the following events:
	 * <ol>
	 * <li>SETTING_UP</li>
	 * <li>SCHEDULED as soon as the set-up is finished</li>
	 * <li>FAILURE if and only if the given callable throws an exception</li>
	 * <li>FINISHING as soon as the given callable has finished running (successfully or not)</li>
	 * <li>FINISHED as soon as the tear-down is finished</li>
	 * </ol>
	 * <b>Note</b> that these events will be sent in the Thread that ran the computation, NOT in the calling
	 * Thread.
	 * 
	 * @param callable
	 *            will be executed as soon as this instance is no longer computing anything. Must not be
	 *            {@code null}.
	 * @param postTreatment
	 *            will be called in a finally clause, whatever the outcome of the computation. Can be
	 *            {@code null}.
	 * @param <U>
	 *            the type of the return value.
	 * @return The result returned by the given callable execution.
	 */
	public synchronized <U> U call(Callable<U> callable, Runnable postTreatment) {
		checkNotNull(callable);
		try {
			if (eventBus != null) {
				eventBus.post(CallStatus.SETTING_UP);
			}
			setUpComputation();
			if (eventBus != null) {
				eventBus.post(CallStatus.SCHEDULED);
			}
			return callable.call();
		} catch (Exception e) {
			if (eventBus != null) {
				eventBus.post(new CallStatus(e));
			}
			if (e instanceof InterruptedException) {
				throw new OperationCanceledException();
			}
			if (e instanceof OperationCanceledException) {
				throw (OperationCanceledException)e;
			}
			throw new RuntimeException(e);
		} finally {
			if (eventBus != null) {
				eventBus.post(CallStatus.FINISHING);
			}
			try {
				tearDownComputation();
				if (postTreatment != null) {
					postTreatment.run();
				}
			} finally {
				if (eventBus != null) {
					eventBus.post(CallStatus.FINISHED);
				}
			}
		}
	}

	/**
	 * Schedules all the given computations, which will only be run if no computation for the same key is in
	 * the {@link #computedKeys} variable. It is up to the caller to make sure that the semantics of
	 * computations previously run is the same as thos they are submitting, otherwise computations completely
	 * unrelated to what is being submitted may have marked a key as already computed. Returns after all the
	 * currently running plus submitted computations have finished.
	 * 
	 * @param computations
	 *            An iterable over the computations to schedule. {@code null} entries are silently ignored.
	 */
	public void computeAll(Iterable<? extends IComputation<T>> computations) {
		checkNotNull(computations);
		lock.lock();
		for (IComputation<T> comp : computations) {
			if (comp != null) {
				scheduleComputation(comp);
			}
		}
		waitForEndOfTasks();
	}

	/**
	 * Schedules a given computation to be performed as soon as possible, if its key is not present in the
	 * {@link #computedKeys} or in the {@link #currentlyComputing} keys, in which case the computation is
	 * ignored. It is up to the caller to make sure that they submit homogeneous computations, in order for
	 * the filtering of computations by key to be meaningful.
	 * <p>
	 * <b>WARNING!</b> In a multi-threaded execution, this method may return before the computation is run. It
	 * is up to callers to make sure they only invoke that inside of a more general call to
	 * {@link #call(Callable, Runnable)}, {@link #computeAll(Iterable)}, or {@link #runAll(Iterable)}
	 * </p>
	 * 
	 * @param computation
	 *            The computation to run. Cannot be {@code null}.
	 * @return {@code true} if and only if the given key is not already among either the computed elements or
	 *         the currently computing elements.
	 */
	public boolean scheduleComputation(final IComputation<T> computation) {
		checkNotNull(computation);
		lock.lock();
		try {
			if (computedKeys.add(computation.getKey()) && currentlyComputing.add(computation.getKey())) {
				ListenableFuture<?> future = computingPool.submit(new Runnable() {
					public void run() {
						computation.run();
					}
				});
				// even if post-treatment is null, we need this callback for proper finalization
				Futures.addCallback(future, new ComputingFutureCallback<T>(this, computation.getKey(),
						computation.getPostTreatment()));
				return true;
			}
			return false;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Executes all the runnables in the given iterable, and returns when all computations possibly running or
	 * launched by the given runnables are finished. This must be used when some treatment will possibly
	 * schedule computations but the exact list of computations cannot be computed beforehand.
	 * 
	 * @param runnables
	 *            An iterable over the runnables to execute, must not be {@code null}. {@code null} entries
	 *            are silently ignored.
	 */
	public void runAll(Iterable<? extends Runnable> runnables) {
		checkNotNull(runnables);
		lock.lock();
		for (Runnable runnable : runnables) {
			if (runnable != null) {
				runnable.run();
			}
		}
		waitForEndOfTasks();
	}

	/**
	 * Schedule a job that is suppoed to unload resource(s) that are no longer needed. This implementation
	 * uses a dedicated thread pool to perform these unloads.
	 * 
	 * @param runnable
	 *            Runnable to run, must not be {@code null}
	 * @param callback
	 *            Callback to call upon completion, can be {@code null}
	 */
	public void scheduleUnload(Runnable runnable, FutureCallback<Object> callback) {
		ListenableFuture<?> future = unloadingPool.submit(runnable);
		if (callback != null) {
			Futures.addCallback(future, callback);
		}
	}

	/**
	 * Provides the set of keys of all the computations that have been run or are still running since its set
	 * of keys {@link #computedKeys} was last set.
	 * 
	 * @return The set of keys of all the computations that have been run or are still running since its set
	 *         of keys {@link #computedKeys} was last set.
	 */
	public ImmutableSet<T> getComputedElements() {
		if (computedKeys == null) {
			return ImmutableSet.of();
		}
		return ImmutableSet.copyOf(computedKeys);
	}

	/**
	 * Evaluates whether a computation with the given key has been run or is still running since its set of
	 * keys {@link #computedKeys} was last set.
	 * 
	 * @param key
	 *            The key of the computation to check.
	 * @return true, if a computation of the given key has been run or is still running.
	 */
	public boolean isScheduled(T key) {
		return computedKeys != null && computedKeys.contains(key);
	}

	/**
	 * Clears the set of computed keys.
	 */
	public void clearComputedElements() {
		computedKeys.clear();
	}

	/**
	 * Sets the computed keys with all the values in the given iterable.
	 * 
	 * @param elements
	 *            An iterable over the elements to set as computed, must not be {@code null} but can be empty.
	 */
	public void setComputedElements(Iterable<T> elements) {
		computedKeys = Sets.newLinkedHashSet(elements);
	}

	/**
	 * Shuts down an {@link ExecutorService} in two phases, first by calling {@link ExecutorService#shutdown()
	 * shutdown} to reject incoming tasks, and then calling {@link ExecutorService#shutdownNow() shutdownNow},
	 * if necessary, to cancel any lingering tasks. Returns true if the pool has been properly shutdown, false
	 * otherwise.
	 * <p>
	 * Copy/pasted from {@link ExecutorService} javadoc.
	 * 
	 * @param pool
	 *            the pool to shutdown, must not be {@code null}
	 * @return true if the pool has been properly shutdown, false otherwise.
	 */
	private boolean shutdownAndAwaitTermination(ExecutorService pool) {
		boolean ret = true;
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(shutdownWaitDuration, shutdownWaitUnit)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being canceled
				if (!pool.awaitTermination(shutdownWaitDuration, shutdownWaitUnit)) {
					ret = false;
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
			ret = false;
		}
		return ret;
	}

	/**
	 * Wait until all tasks have finished executing, or the current thread is interrupted, in which case an
	 * OperationCanceledException is thrown.
	 */
	private void waitForEndOfTasks() {
		try {
			// TODO Is this test really necessary?
			// TODO Is this test dangerous (infinite wait)
			while (!currentlyComputing.isEmpty()) {
				endOfTasks.await();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			// FIXME?
			throw new OperationCanceledException();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * This will remove the given uri from the {@link #currentlyComputing} set and signal to
	 * {@link #endOfTasks} if the set is empty afterward. This method must be call by every callback of
	 * resolving tasks.
	 * 
	 * @param key
	 *            the key to remove.
	 */
	private void finalizeTask(T key) {
		lock.lock();
		try {
			currentlyComputing.remove(key);
			if (currentlyComputing.isEmpty()) {
				endOfTasks.signal();
			}
		} finally {
			lock.unlock();
		}
	}

	// TODO Change javadoc
	/**
	 * The callback for tasks. It will report progress, log errors and finalize the resolving and as such,
	 * possibly signaling the end of the resolution.
	 * 
	 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
	 */
	private static final class ComputingFutureCallback<T> implements FutureCallback<Object> {
		/** The scheduler. */
		private final ResourceComputationScheduler<T> scheduler;

		/** The key. */
		private final T key;

		/** The wrapped callback. */
		private final FutureCallback<Object> wrappedCallback;

		/**
		 * Constructor.
		 * 
		 * @param scheduler
		 *            The scheduler
		 * @param key
		 *            The key
		 * @param callback
		 *            The callback, can be {@code null}
		 */
		private ComputingFutureCallback(ResourceComputationScheduler<T> scheduler, T key,
				FutureCallback<Object> callback) {
			this.scheduler = checkNotNull(scheduler);
			this.key = checkNotNull(key);
			this.wrappedCallback = callback;
		}

		/**
		 * {@inheritDoc}
		 */
		public void onSuccess(Object result) {
			try {
				if (wrappedCallback != null) {
					wrappedCallback.onSuccess(result);
				}
			} finally {
				scheduler.finalizeTask(key);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void onFailure(Throwable t) {
			try {
				if (wrappedCallback != null) {
					wrappedCallback.onFailure(t);
				}
			} finally {
				scheduler.finalizeTask(key);
			}
		}
	}

	public static enum ComputationState {
		/** Computation is setting-up (preparing pools and so on). */
		SETTING_UP,
		/** Computation is set-up and scheduled. */
		SCHEDULED,
		/** Computation is over, tear-down and post-treatments are starting. */
		FINISHING,
		/** Computation is over and has failed. */
		FAILED,
		/** Computation is over and tear-down + post-treatments are finished. */
		FINISHED;
	}

	public static class CallStatus {
		public static final CallStatus SETTING_UP = new CallStatus(ComputationState.SETTING_UP);

		public static final CallStatus SCHEDULED = new CallStatus(ComputationState.SCHEDULED);

		public static final CallStatus FINISHING = new CallStatus(ComputationState.FINISHING);

		public static final CallStatus FINISHED = new CallStatus(ComputationState.FINISHED);

		private final Throwable cause;

		private final ComputationState state;

		private CallStatus(ComputationState state) {
			this.state = state;
			this.cause = null;
		}

		private CallStatus(Throwable cause) {
			this.state = ComputationState.FAILED;
			this.cause = cause;
		}

		public Throwable getCause() {
			return cause;
		}

		public ComputationState getState() {
			return state;
		}
	}

	public static enum ShutdownState {
		STARTED, FINISH_FAILED, FINISH_SUCCESS;
	}

	public static class ShutdownStatus {
		public static final ShutdownStatus STARTED = new ShutdownStatus(ShutdownState.STARTED);

		public static final ShutdownStatus SUCCESS = new ShutdownStatus(ShutdownState.FINISH_SUCCESS);

		private final Throwable cause;

		private final ShutdownState state;

		private ShutdownStatus(ShutdownState state) {
			this.state = state;
			this.cause = null;
		}

		private ShutdownStatus(Throwable cause) {
			this.state = ShutdownState.FINISH_FAILED;
			this.cause = cause;
		}

		public Throwable getCause() {
			return cause;
		}

		public ShutdownState getState() {
			return state;
		}
	}
}
