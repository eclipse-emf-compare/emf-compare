/*******************************************************************************
 * Copyright (c) 2015, 2017 Obeo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Christian W. Damus - bug 522017
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.IComputation;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler.ShutdownStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

@SuppressWarnings("nls")
public class ResourceComputationSchedulerTest {

	@Rule
	public final Timeout timeout = new Timeout(15, TimeUnit.SECONDS);

	protected ResourceComputationScheduler<String> scheduler;

	protected boolean flag;

	@Test
	public void testInitializeCanBeCalledSeveralTimes() {
		scheduler.initialize();
		scheduler.initialize();
	}

	@Test
	public void testIsInitializedBeforeInit() {
		assertFalse(scheduler.isInitialized());
	}

	@Test
	public void testIsInitializedAfterInit() {
		scheduler.initialize();
		assertTrue(scheduler.isInitialized());
	}

	@Test
	public void testIsInitializedAfterDispose() {
		scheduler.initialize();
		scheduler.dispose();
		assertFalse(scheduler.isInitialized());
	}

	@Test
	public void testBasicExecution() throws Exception {
		scheduler.initialize();
		flag = false;
		Integer result = scheduler.call(new Callable<Integer>() {
			public Integer call() throws Exception {
				return Integer.valueOf(42);
			}
		}, new Runnable() {
			public void run() {
				flag = true;
			}
		});
		assertEquals(Integer.valueOf(42), result);
		// Flag will be true only if the post-treatment has been called...
		assertTrue(flag);
	}

	@Test(expected = OperationCanceledException.class)
	public void testInterruptedExceptionInCallCausesOperationCanceledException() throws Exception {
		scheduler.initialize();
		scheduler.call(new Callable<String>() {
			public String call() throws Exception {
				throw new InterruptedException();
			}
		}, null);
	}

	@Test(expected = OperationCanceledException.class)
	public void testOperationCanceledExceptionCall() throws Exception {
		scheduler.initialize();
		scheduler.call(new Callable<String>() {
			public String call() throws Exception {
				throw new OperationCanceledException();
			}
		}, null);
	}

	@Test
	public void testPostTreatmentIsCalledWhenExceptionInTreatment() throws Exception {
		scheduler.initialize();
		flag = false;
		try {
			scheduler.call(new Callable<Integer>() {
				public Integer call() throws Exception {
					throw new Exception("For test");
				}
			}, new Runnable() {
				public void run() {
					flag = true;
				}
			});
		} catch (RuntimeException e) {
			assertEquals("For test", e.getCause().getMessage());
		}
		// Flag will be true only if the post-treatment has been called...
		assertTrue(flag);
	}

	@Test
	public void testPostTreatmentCanBeNull() throws Exception {
		scheduler.initialize();
		Integer result = scheduler.call(new Callable<Integer>() {
			public Integer call() throws Exception {
				return Integer.valueOf(42);
			}
		}, null);
		assertEquals(Integer.valueOf(42), result);
	}

	@Test(expected = NullPointerException.class)
	public void testCallableCannotBeNull() throws Exception {
		scheduler.initialize();
		scheduler.call(null, new Runnable() {
			public void run() {
				// Nothing
			}
		});
	}

	@Test
	public void testComputedElements() {
		scheduler.initialize();
		assertTrue(scheduler.getComputedElements().isEmpty());
		scheduler.setComputedElements(Arrays.asList("a", "b", "c"));
		assertEquals(ImmutableSet.of("a", "b", "c"), scheduler.getComputedElements());
		scheduler.clearComputedElements();
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test
	public void testComputeOneSuccess() throws Exception {
		scheduler.initialize();
		final CompStatus desc = new CompStatus();
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.computeAll(Arrays.asList(new TestSuccessfulComputation(desc, "comp1")));
				assertEquals(ImmutableSet.of("comp1"), scheduler.getComputedElements());
				return Integer.valueOf(42);
			}
		}, null);
		checkSuccess(desc);
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test
	public void testComputeSeveralSuccess() throws Exception {
		scheduler.initialize();
		final CompStatus[] statuses = new CompStatus[10];
		final List<TestSuccessfulComputation> computations = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			statuses[i] = new CompStatus();
			computations.add(new TestSuccessfulComputation(statuses[i], "comp" + i));
		}
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				for (int i = 0; i < 10; i++) {
					scheduler.computeAll(computations);
				}
				assertEquals(ImmutableSet.of("comp0", "comp1", "comp2", "comp3", "comp4", "comp5", "comp6",
						"comp7", "comp8", "comp9"), scheduler.getComputedElements());
				return Integer.valueOf(42);
			}
		}, null);
		for (int i = 0; i < 10; i++) {
			checkSuccess(statuses[i]);
		}
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test
	public void testPostTreatmentOnFailureIsCalledOnOneFailedComputation() throws Exception {
		scheduler.initialize();
		final CompStatus cs = new CompStatus();
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.computeAll(Arrays.asList(new TestFailedComputation(cs, "fail1")));
				assertEquals(ImmutableSet.of("fail1"), scheduler.getComputedElements());
				return Integer.valueOf(42);
			}
		}, null);
		checkFailure(cs);
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test
	public void testPostTreatmentOnFailureIsCalledOnAllFailingComputations() throws Exception {
		scheduler.initialize();
		final CompStatus[] statuses = new CompStatus[10];
		final List<TestFailedComputation> computations = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			statuses[i] = new CompStatus();
			computations.add(new TestFailedComputation(statuses[i], "fail" + i));
		}
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.computeAll(computations);
				assertEquals(ImmutableSet.of("fail0", "fail1", "fail2", "fail3", "fail4", "fail5", "fail6",
						"fail7", "fail8", "fail9"), scheduler.getComputedElements());
				return Integer.valueOf(42);
			}
		}, null);
		for (int i = 0; i < 10; i++) {
			checkFailure(statuses[i]);
		}
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test
	public void testRunOneSuccess() throws Exception {
		scheduler.initialize();
		final CompStatus desc = new CompStatus();
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.runAll(Arrays.asList(new UninterruptibleRunnable(desc)));
				assertTrue(scheduler.getComputedElements().isEmpty());
				return Integer.valueOf(42);
			}
		}, null);
		checkSuccess(desc);
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test
	public void testRunSeveralSuccess() throws Exception {
		scheduler.initialize();
		final CompStatus[] statuses = new CompStatus[10];
		final List<UninterruptibleRunnable> toBeRun = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			statuses[i] = new CompStatus();
			toBeRun.add(new UninterruptibleRunnable(statuses[i]));
		}
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.runAll(toBeRun);
				assertTrue(scheduler.getComputedElements().isEmpty());
				return Integer.valueOf(42);
			}
		}, null);
		for (int i = 0; i < 10; i++) {
			checkSuccess(statuses[i]);
		}
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void testScheduleComputationCannotRunOutsideCall() throws Exception {
		scheduler.initialize();
		final CompStatus desc = new CompStatus();
		scheduler.scheduleComputation(new TestSuccessfulComputation(desc, "comp"));
	}

	/**
	 * Verify that multiple concurrent invocations of {@link ResourceComputationScheduler#runAll(Iterable)}
	 * are all notified when the computation completes.
	 */
	@Test
	public void testAllThreadsNotifiedOfCompletion() {
		scheduler.initialize();
		final CompStatus[][] statuses = new CompStatus[10][10];
		final List<Runnable> toBeRun = Lists.newArrayList();

		for (int i = 0; i < 10; i++) {
			final String key = Character.toString((char)('A' + i));
			final CompStatus[] nestedStatuses = statuses[i];

			toBeRun.add(new Runnable() {

				public void run() {
					// Nested scheduling of tasks
					final List<IComputation<String>> nested = Lists.newArrayList();
					for (int j = 0; j < 10; j++) {
						final String nestedKey = key + j;
						final CompStatus status = new CompStatus();
						nestedStatuses[j] = status;
						nested.add(new IComputation<String>() {
							public String getKey() {
								return nestedKey;
							}

							public void run() {
								status.addCall();
								status.success("as expected");
							}

							public FutureCallback<Object> getPostTreatment() {
								return null;
							}
						});
					}

					scheduler.computeAll(nested);

					String[] expectedKeys = Iterables.toArray(Iterables.transform( //
							Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), //
							new Function<String, String>() {
								public String apply(String input) {
									return key + input;
								}
							}), String.class);
					assertThat(scheduler.getComputedElements(), hasItems(expectedKeys));
				}
			});
		}
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				ExecutorService exec = Executors.newFixedThreadPool(4);
				try {
					for (Runnable next : toBeRun) {
						exec.execute(next);
					}

					exec.shutdown();
					exec.awaitTermination(15, TimeUnit.SECONDS);

					Set<String> expectedKeys = ImmutableSet.copyOf(Iterables.transform(Lists.cartesianProduct( //
							Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"), //
							Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")), //
							new Function<List<String>, String>() {
								public String apply(List<String> input) {
									return input.get(0) + input.get(1);
								}
							}));
					assertThat(scheduler.getComputedElements(), is(expectedKeys));
				} finally {
					exec.shutdown();
				}

				return Integer.valueOf(42);
			}
		}, null);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				checkSuccess(statuses[i][j]);
			}
		}
		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());
	}

	/**
	 * Verify that concurrent invocation of {@code isScheduled()} seems to work.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void testIsScheduled() {
		testTemplate_concurrentAccessToComputedElements(new Function<String, Callable<String>>() {
			public java.util.concurrent.Callable<String> apply(final String key) {
				return new Callable<String>() {
					public String call() throws Exception {
						Thread.sleep(250L);
						assertThat(scheduler.isScheduled(key), is(true));
						Thread.sleep(250L);
						return key;
					}
				};
			}
		});
	}

	@SuppressWarnings("boxing")
	private void testTemplate_concurrentAccessToComputedElements(
			Function<String, Callable<String>> bodyFactory) {

		scheduler.initialize();
		final CompStatus[] statuses = new CompStatus[10];
		final List<TestComputation<String>> toBeComputed = Lists.newArrayList();

		for (int i = 0; i < 10; i++) {
			final String key = Character.toString((char)('A' + i));
			final CompStatus status = new CompStatus();
			statuses[i] = status;

			toBeComputed.add(new TestComputation<String>(status, key, bodyFactory.apply(key)));
		}
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.computeAll(toBeComputed);

				return 42;
			}
		}, null);
		for (int i = 0; i < 10; i++) {
			checkSuccess(statuses[i]);
		}
		assertThat(result, is(42));
		assertThat(scheduler.getComputedElements(), empty());
	}

	/**
	 * Verify that concurrent invocation of {@code getComputedElements()} seems to work.
	 */
	@Test
	public void testGetComputedElements() {
		testTemplate_concurrentAccessToComputedElements(new Function<String, Callable<String>>() {
			public java.util.concurrent.Callable<String> apply(final String key) {
				return new Callable<String>() {
					public String call() throws Exception {
						Thread.sleep(250L);
						assertThat(scheduler.getComputedElements(), hasItem(key));
						Thread.sleep(250L);
						return key;
					}
				};
			}
		});
	}

	/**
	 * Verify that an exception in a {@code runAll(...)} runnable doesn't cause a subsequent use of the
	 * scheduler to hang.
	 */
	@Test
	@SuppressWarnings("boxing")
	public void testFailureInRunnable() throws Exception {
		scheduler.initialize();
		final CompStatus[] statuses = new CompStatus[10];

		class MaybeThrow implements Runnable {
			private final CompStatus status;

			private final boolean doThrow;

			MaybeThrow(CompStatus status, boolean doThrow) {
				super();

				this.status = status;
				this.doThrow = doThrow;
			}

			public void run() {
				if (doThrow) {
					status.fail("throwing");
					throw new RuntimeException("as expected");
				}

				status.addCall();
				status.success("as expected");
			}
		}

		for (int i = 0; i < 10; i++) {
			final CompStatus status = new CompStatus();
			statuses[i] = status;
		}

		Future<Integer> task = new FutureTask<>(new Callable<Integer>() {
			public Integer call() {
				// This won't go so well, but at least it will return
				try {
					final List<Runnable> toBeRun = Lists.newArrayList();
					for (int i = 0; i < 10; i++) {
						toBeRun.add(new MaybeThrow(statuses[i], true));
					}

					Integer result = scheduler.call(new Callable<Integer>() {

						public Integer call() throws Exception {
							scheduler.runAll(toBeRun);

							return 42;
						}
					}, null);
					fail("scheduler.call(...) should have thrown");
					return result;
				} catch (RuntimeException e) {
					// Expected
					return null;
				}
			}
		});
		new Thread((Runnable)task).start();
		task.get(); // Wait for the task to finish

		// Next time, don't throw

		task = new FutureTask<>(new Callable<Integer>() {
			public Integer call() {
				// This should go fine
				return scheduler.call(new Callable<Integer>() {

					public Integer call() throws Exception {
						final List<Runnable> toBeRun = Lists.newArrayList();
						for (int i = 0; i < 10; i++) {
							toBeRun.add(new MaybeThrow(statuses[i], false));
						}

						scheduler.runAll(toBeRun);

						return 42;
					}
				}, null);
			}
		});
		new Thread((Runnable)task).start();
		Integer result = task.get(); // Wait for the task to finish

		for (int i = 0; i < 10; i++) {
			checkSuccess(statuses[i]);
		}
		assertThat(result, is(42));
		assertThat(scheduler.getComputedElements(), empty());
	}

	protected void checkSuccess(CompStatus state) {
		assertEquals(1, state.getCallCount());
		assertFalse(state.isInterrupted());
		if (!state.isSuccess() || state.isFailed()) {
			fail(state.getMessage());
		}
		assertEquals("as expected", state.getMessage());
	}

	protected void checkFailure(CompStatus state) {
		assertEquals(1, state.getCallCount());
		assertFalse(state.isInterrupted());
		if (state.isSuccess() || !state.isFailed()) {
			fail(state.getMessage());
		}
		assertEquals("as expected", state.getMessage());
	}

	protected void checkInterruptedAndSuccess(CompStatus state) {
		assertEquals(1, state.getCallCount());
		assertTrue(state.isInterrupted());
		if (!state.isSuccess() || state.isFailed()) {
			fail(state.getMessage());
		}
		assertEquals("as expected", state.getMessage());
	}

	protected void checkInterruptedAndFailure(CompStatus state) {
		assertEquals(1, state.getCallCount());
		assertTrue(state.isInterrupted());
		if (state.isSuccess() || !state.isFailed()) {
			fail(state.getMessage());
		}
		assertEquals("as expected", state.getMessage());
	}

	@Before
	public void setUp() {
		scheduler = new ResourceComputationScheduler<String>(100, TimeUnit.MILLISECONDS, null);
	}

	@After
	public void tearDown() {
		scheduler.dispose();
	}

	/**
	 * A test computation with pluggable behaviour that updates its {@link CompStatus} accordingly when its
	 * post-treatment is called.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 * @author Christian W. Damus
	 */
	private class TestComputation<T> implements IComputation<String>, Supplier<T> {
		private final Callable<? extends T> body;

		private final CompStatus cs;

		private final String name;

		private T result;

		private TestComputation(CompStatus cs, String name, Callable<? extends T> body) {
			this.cs = cs;
			this.name = name;
			this.body = body;
		}

		/**
		 * {@inheritDoc}
		 */
		public synchronized T get() {
			return result;
		}

		public void run() {
			cs.addCall();

			try {
				synchronized(this) {
					result = body.call();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				cs.interrupt();
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new WrappedException(e);
			}
		}

		public FutureCallback<Object> getPostTreatment() {
			return new FutureCallback<Object>() {
				public void onFailure(Throwable t) {
					Throwable toReport = t;
					if (t instanceof WrappedException) {
						// Unwrap it
						toReport = ((WrappedException)t).exception();
					}

					fail(String.format("%s: %s", toReport.getClass().getName(), toReport.getMessage()));
				}

				public void onSuccess(Object r) {
					success("as expected");
				}
			};
		}

		public String getKey() {
			return name;
		}

		void fail(String message) {
			cs.fail(message);
		}

		void success(String message) {
			cs.success(message);
		}
	}

	/**
	 * A test computation that is successful and updates it {@link CompStatus} accordingly when its
	 * post-treatment is called.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private final class TestSuccessfulComputation extends TestComputation<String> {

		private TestSuccessfulComputation(CompStatus cs, final String name) {
			super(cs, name, new Callable<String>() {
				public String call() throws Exception {
					Thread.sleep(1L);
					return name;
				}
			});
		}

		@Override
		public FutureCallback<Object> getPostTreatment() {
			return new FutureCallback<Object>() {
				public void onFailure(Throwable t) {
					fail("onFailure() called on computation " + getKey() + ", should have been onSuccess().");
				}

				public void onSuccess(Object r) {
					success("as expected");
				}
			};
		}
	}

	/**
	 * A test computation that systematically throws an exception when run, and updates its {@link CompStatus}
	 * accordingly if onFailure() is called on its post-treatment.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private final class TestFailedComputation extends TestComputation<String> {

		private TestFailedComputation(CompStatus desc, final String name) {
			super(desc, name, new Callable<String>() {
				public String call() throws Exception {
					throw new RuntimeException("Error for tests in computation " + name);
				}
			});
		}

		@Override
		public FutureCallback<Object> getPostTreatment() {
			return new FutureCallback<Object>() {
				public void onFailure(Throwable t) {
					fail("as expected");
				}

				public void onSuccess(Object r) {
					success("onSuccess() called on computation " + getKey()
							+ ", should have been onFailure().");
				}
			};
		}
	}

	/**
	 * A test computation that is successful and updates it {@link CompStatus} accordingly when its
	 * post-treatment is called.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private final class UninterruptibleRunnable implements Runnable {
		private final CompStatus cs;

		private UninterruptibleRunnable(CompStatus desc) {
			this.cs = desc;
		}

		public void run() {
			cs.addCall();
			cs.success("as expected");
		}
	}

	/**
	 * Computation Status.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	protected static class CompStatus {
		private boolean success = false;

		private boolean failed = false;

		private int callCount;

		private boolean interrupted;

		private ShutdownStatus shutdownStatus;

		private String message;

		public String getMessage() {
			return message;
		}

		public boolean isSuccess() {
			return success;
		}

		public synchronized void addCall() {
			callCount++;
		}

		public int getCallCount() {
			return callCount;
		}

		public void success(String msg) {
			this.success = true;
			this.failed = false;
			message = msg;
		}

		public boolean isFailed() {
			return failed;
		}

		public void fail(String msg) {
			this.failed = true;
			this.success = false;
			message = msg;
		}

		public void interrupt() {
			interrupted = true;
		}

		public boolean isInterrupted() {
			return interrupted;
		}

		public void setShutdownStatus(ShutdownStatus shutdownStatus) {
			this.shutdownStatus = shutdownStatus;
		}

		public ShutdownStatus getShutdownStatus() {
			return shutdownStatus;
		}
	}
}
