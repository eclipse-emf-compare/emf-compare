/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.IComputation;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler.CallStatus;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler.ComputationState;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler.ShutdownState;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ResourceComputationScheduler.ShutdownStatus;
import org.junit.Before;
import org.junit.Test;

/**
 * These tests are the same as in the extend class, except they use a {@link ResourceComputationScheduler}
 * initialized with a non-null {@link EventBus}. It also adds tests that can only be performed when an
 * EventBus is set on the scheduler.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("nls")
public class ResourceComputationSchedulerWithEventBusTest extends ResourceComputationSchedulerTest {

	private EventBus bus;

	@Test
	public void testEventsLaunchedInStandardCase() throws Exception {
		scheduler.initialize();
		final List<CallStatus> receivedEvents = new ArrayList<CallStatus>();
		bus.register(new Object() {
			@Subscribe
			public void callStatusChanged(CallStatus cs) {
				receivedEvents.add(cs);
			}
		});
		scheduler.call(new Callable<String>() {
			public String call() throws Exception {
				return "";
			}
		}, null);
		assertEquals(4, receivedEvents.size());
		assertEquals(ComputationState.SETTING_UP, receivedEvents.get(0).getState());
		assertEquals(ComputationState.SCHEDULED, receivedEvents.get(1).getState());
		assertEquals(ComputationState.FINISHING, receivedEvents.get(2).getState());
		assertEquals(ComputationState.FINISHED, receivedEvents.get(3).getState());
	}

	@Test
	public void testEventsLaunchedWhenCallThrowsException() throws Exception {
		scheduler.initialize();
		final List<CallStatus> receivedEvents = new ArrayList<CallStatus>();
		bus.register(new Object() {
			@Subscribe
			public void callStatusChanged(CallStatus cs) {
				receivedEvents.add(cs);
			}
		});
		Exception exceptionReceived = null;
		try {
			scheduler.call(new Callable<String>() {
				public String call() throws Exception {
					throw new Exception("Test");
				}
			}, null);
		} catch (Exception e) {
			exceptionReceived = e;
		}
		assertEquals(5, receivedEvents.size());
		assertEquals(ComputationState.SETTING_UP, receivedEvents.get(0).getState());
		assertEquals(ComputationState.SCHEDULED, receivedEvents.get(1).getState());
		assertEquals(ComputationState.FAILED, receivedEvents.get(2).getState());
		// Check the root exception has been well passed in the event too
		assertNotNull(exceptionReceived);
		assertSame(exceptionReceived.getCause(), receivedEvents.get(2).getCause());
		assertEquals(ComputationState.FINISHING, receivedEvents.get(3).getState());
		assertEquals(ComputationState.FINISHED, receivedEvents.get(4).getState());
	}

	@Test
	public void testEventsLaunchedWhenPostTreamentThrowsException() throws Exception {
		scheduler.initialize();
		final List<CallStatus> receivedEvents = new ArrayList<CallStatus>();
		bus.register(new Object() {
			@Subscribe
			public void callStatusChanged(CallStatus cs) {
				receivedEvents.add(cs);
			}
		});
		try {
			scheduler.call(new Callable<String>() {
				public String call() throws Exception {
					return "";
				}
			}, new Runnable() {
				public void run() {
					throw new RuntimeException();
				}
			});
			fail("There should have been a RuntimeException");
		} catch (RuntimeException e) {
			// As expected
		}
		// Flag will only be true if the CallStatus "FINISH event has been received
		assertEquals(4, receivedEvents.size());
		assertEquals(ComputationState.SETTING_UP, receivedEvents.get(0).getState());
		assertEquals(ComputationState.SCHEDULED, receivedEvents.get(1).getState());
		assertEquals(ComputationState.FINISHING, receivedEvents.get(2).getState());
		assertEquals(ComputationState.FINISHED, receivedEvents.get(3).getState());
	}

	/**
	 * This test checks that when a scheduler executes a long-runnning task that does not handle interrupts
	 * "gracefully", that is by taking care to stop its treatment ASAP, then the scheduler can nevertheless
	 * shutdown correctly.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDemandShutdownWithLongRunningTaskThatInterruptsImproperly() throws Exception {
		scheduler.initialize();
		final CompStatus cs = new CompStatus();
		final AtomicBoolean readyForFinalChecks = new AtomicBoolean(false);

		// The following computation does not handle properly interruption
		// so we expect its treatment to terminate properly with succeeded instead of failed
		// The computation used here will set cs to "interrupted" when it is interrupted, but it will go on
		// until cs.trigger() is called, which is done after all the checks are made in the test.
		final TriggerableComputation tc = new TriggerableComputation(cs, "long1", false) {
			@Override
			protected void succeeded(Object r) {
				cs.success("As expected");
			}

			@Override
			protected void failed(Throwable t) {
				cs.fail("failed() called, should have been succeeded().");
			}
		};

		bus.register(new Object() {
			@Subscribe
			public void check(ShutdownStatus status) {
				synchronized(scheduler) {
					switch (status.getState()) {
						case FINISH_SUCCESS:
							cs.setShutdownStatus(status);
							readyForFinalChecks.compareAndSet(false, true);
							// This will trigger the execution of the final tests
							scheduler.notifyAll();
							break;
						case FINISH_FAILED:
							cs.setShutdownStatus(status);
							readyForFinalChecks.compareAndSet(false, true);
							scheduler.notifyAll();
					}
				}
			}
		});
		try {
			Integer result = scheduler.call(new Callable<Integer>() {

				public Integer call() throws Exception {
					scheduler.scheduleComputation(tc);
					// We ask for shutdown before the task can complete
					// The scheduler is configured to wait only 100ms
					scheduler.demandShutdown();

					return Integer.valueOf(42);
				}
			}, null);
			assertEquals(Integer.valueOf(42), result);
			assertTrue(scheduler.getComputedElements().isEmpty());

			// This will wait for the shutdown to be over
			// But the computation is still running
			synchronized(scheduler) {
				while (!readyForFinalChecks.get()) {
					scheduler.wait();
				}
				assertEquals(1, cs.getCallCount());
				assertTrue(cs.isInterrupted());
				assertFalse(cs.isFailed());
				// 2 next lines make sure the computation is still running
				assertFalse(cs.isSuccess());
				assertNull(cs.getMessage());
				// Make sure the shutdown is ok
				assertEquals(ShutdownState.FINISH_SUCCESS, cs.getShutdownStatus().getState());
			}
		} finally {
			// We finally allow the tested computation, that is still running, to terminate
			tc.trigger();
		}
	}

	@Test
	public void testDemandShutdownWithLongRunningTaskThatInterruptsGracefully() throws Exception {
		scheduler.initialize();
		final CompStatus cs = new CompStatus();
		final AtomicBoolean readyForFinalChecks = new AtomicBoolean(false);

		// The following computation handles interruption properly
		// so we expect its treatment to terminate with failed
		final TriggerableComputation tc = new TriggerableComputation(cs, "long1", true) {
			@Override
			protected void failed(Throwable t) {
				cs.fail("As expected");
			}

			@Override
			protected void succeeded(Object r) {
				cs.success("Computation ends successfully when it should have failed.");
			}
		};
		bus.register(new Object() {
			@Subscribe
			public void check(ShutdownStatus status) {
				synchronized(scheduler) {
					switch (status.getState()) {
						case FINISH_SUCCESS:
							cs.setShutdownStatus(status);
							readyForFinalChecks.compareAndSet(false, true);
							// This will trigger the execution of the final tests
							scheduler.notifyAll();
							break;
						case FINISH_FAILED:
							cs.setShutdownStatus(status);
							readyForFinalChecks.compareAndSet(false, true);
							scheduler.notifyAll();
					}
				}
			}
		});
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.scheduleComputation(tc);
				// We ask for shutdown before the task can complete
				// The scheduler is configured to wait only 100ms
				scheduler.demandShutdown();

				return Integer.valueOf(42);
			}
		}, null);

		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());

		synchronized(scheduler) {
			// This will wait until the computation has finished to perform the final tests
			while (!readyForFinalChecks.get()) {
				scheduler.wait();
			}
			assertEquals(1, cs.getCallCount());
			assertTrue(cs.getMessage(), cs.isInterrupted());
			assertTrue(cs.getMessage(), cs.isFailed());
			assertFalse(cs.getMessage(), cs.isSuccess());
			assertEquals("As expected", cs.getMessage());
		}
	}

	@Test
	public void testDemandShutdownWithRunningTaskThatTerminatesGracefully() throws Exception {
		scheduler = new ResourceComputationScheduler<String>(1, TimeUnit.SECONDS, bus);
		scheduler.initialize();
		final CompStatus cs = new CompStatus();
		final AtomicBoolean readyForFinalChecks = new AtomicBoolean(false);

		// The following computation does not handle interruption properly
		// but it has the time to terminate before the pools shut down
		// so we expect to have it succeed
		final TriggerableComputation tc = new TriggerableComputation(cs, "long1", false) {
			@Override
			protected void succeeded(Object r) {
				getStatus().success("As expected");
			}

			@Override
			protected void failed(Throwable t) {
				getStatus().fail("failed() should not have been called.");
			}
		};
		bus.register(new Object() {
			@Subscribe
			public void check(ShutdownStatus status) {
				synchronized(scheduler) {
					switch (status.getState()) {
						case FINISH_SUCCESS:
							cs.setShutdownStatus(status);
							readyForFinalChecks.compareAndSet(false, true);
							// This will trigger the execution of the final tests
							scheduler.notifyAll();
							break;
						case FINISH_FAILED:
							cs.setShutdownStatus(status);
							readyForFinalChecks.compareAndSet(false, true);
							scheduler.notifyAll();
					}
				}
			}
		});
		Integer result = scheduler.call(new Callable<Integer>() {

			public Integer call() throws Exception {
				scheduler.scheduleComputation(tc);
				// We ask for shutdown before the task can complete
				// The scheduler is configured to wait 100ms
				scheduler.demandShutdown();
				// This allows the test to make sure that the shutdown has been demanded before
				// the treatment is over
				tc.trigger();
				return Integer.valueOf(42);
			}
		}, null);

		assertEquals(Integer.valueOf(42), result);
		assertTrue(scheduler.getComputedElements().isEmpty());

		synchronized(scheduler) {
			// This will wait until the computation has finished to perform the final tests
			while (!readyForFinalChecks.get()) {
				scheduler.wait();
			}
			assertEquals(1, cs.getCallCount());
			assertFalse(cs.getMessage(), cs.isInterrupted());
			assertTrue(cs.getMessage(), cs.isSuccess());
			assertFalse(cs.getMessage(), cs.isFailed());
			assertEquals("As expected", cs.getMessage());
			assertEquals(ShutdownState.FINISH_SUCCESS, cs.getShutdownStatus().getState());
		}
	}

	@Override
	@Before
	public void setUp() {
		bus = new EventBus();
		scheduler = new ResourceComputationScheduler<String>(10, TimeUnit.MILLISECONDS, bus);
	}

	/**
	 * A test computation that sleeps until it's either triggered by a call to {@link #trigger()} or
	 * interrupted.
	 * 
	 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
	 */
	private static class TriggerableComputation implements IComputation<String> {
		private volatile boolean start = false;

		private final CompStatus cs;

		private final String name;

		private final boolean throwOnInterrupt;

		public TriggerableComputation(CompStatus cs, String name, boolean throwOnInterrupt) {
			this.cs = cs;
			this.name = name;
			this.throwOnInterrupt = throwOnInterrupt;
		}

		public void trigger() {
			start = true;
		}

		public void run() {
			cs.addCall();
			while (!start) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					cs.interrupt();
					if (throwOnInterrupt) {
						throw new RuntimeException("Interrupted");
					}
				}
			}
		}

		public FutureCallback<Object> getPostTreatment() {
			return new FutureCallback<Object>() {
				public void onFailure(Throwable t) {
					failed(t);
				}

				public void onSuccess(Object r) {
					succeeded(r);
				}
			};
		}

		public CompStatus getStatus() {
			return cs;
		}

		public String getKey() {
			return name;
		}

		/**
		 * Override this method to specify specific expected behaviour in tests. Update variable cs to convey
		 * information about succes or failure, since using junit methods (fail, assertTrue, etc.) here will
		 * not cause a test to fail.
		 * 
		 * @param t
		 */
		protected void failed(Throwable t) {
			cs.fail("As expected");
		}

		/**
		 * Override this method to specify specific expected behaviour in tests. Update variable cs to convey
		 * information about succes or failure, since using junit methods (fail, assertTrue, etc.) here will
		 * not cause a test to fail.
		 * 
		 * @param t
		 */
		protected void succeeded(@SuppressWarnings("unused") Object r) {
			cs.success("As expected");
		}
	}
}
