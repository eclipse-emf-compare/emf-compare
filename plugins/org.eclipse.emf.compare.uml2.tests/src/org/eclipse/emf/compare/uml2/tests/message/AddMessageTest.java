package org.eclipse.emf.compare.uml2.tests.message;

import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.added;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.addedToReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.changedReference;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.ofKind;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removed;
import static org.eclipse.emf.compare.utils.EMFComparePredicates.removedFromReference;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Diff;
import org.eclipse.emf.compare.DifferenceKind;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.tests.framework.AbstractInputData;
import org.eclipse.emf.compare.uml2.MessageChange;
import org.eclipse.emf.compare.uml2.tests.AbstractTest;
import org.eclipse.emf.compare.uml2.tests.message.data.MessageInputData;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

@SuppressWarnings("nls")
public class AddMessageTest extends AbstractTest {

	private MessageInputData input = new MessageInputData();

	@Test
	public void testA10UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA10UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA11UseCase3way() throws IOException {
		final Resource left = input.getA1Left();
		final Resource right = input.getA1Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA20UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testA21UseCase() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testA20UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.ADD, comparison);
	}

	@Test
	public void testA21UseCase3way() throws IOException {
		final Resource left = input.getA2Left();
		final Resource right = input.getA2Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB2(TestKind.DELETE, comparison);
	}

	@Test
	public void testA30UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testA31UseCase() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testA30UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.ADD, comparison);
	}

	@Test
	public void testA31UseCase3way() throws IOException {
		final Resource left = input.getA3Left();
		final Resource right = input.getA3Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB3(TestKind.DELETE, comparison);
	}

	@Test
	public void testA40UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA41UseCase() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	@Test
	public void testA40UseCase3way() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, right);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.ADD, comparison);
	}

	@Test
	public void testA41UseCase3way() throws IOException {
		final Resource left = input.getA4Left();
		final Resource right = input.getA4Right();

		final IComparisonScope scope = EMFCompare.createDefaultScope(left, right, left);
		final Comparison comparison = getCompare().compare(scope);
		testAB1(TestKind.DELETE, comparison);
	}

	private void testAB1(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 12 differences
		assertSame(Integer.valueOf(12), Integer.valueOf(differences.size()));

		Predicate<? super Diff> addMessageDescription = null;
		Predicate<? super Diff> addCoveredInMessage0Send0Description = null;
		Predicate<? super Diff> addMessageInMessage0Send0Description = null;
		Predicate<? super Diff> addCoveredInMessage0Recv0Description = null;
		Predicate<? super Diff> addMessageInMessage0Recv0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline1Description = null;
		Predicate<? super Diff> addReceiveEventInMessageDescription = null;
		Predicate<? super Diff> addSendEventInMessageDescription = null;
		Predicate<? super Diff> addMessageSendDescription = null;
		Predicate<? super Diff> addMessageRecvDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addMessageDescription = removed("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageSendDescription = removed("model.interaction0.Message0Send0"); //$NON-NLS-1$
			addMessageRecvDescription = removed("model.interaction0.Message0Recv0"); //$NON-NLS-1$
			addCoveredInMessage0Send0Description = removedFromReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Send0Description = changedReference("model.interaction0.Message0Send0",
					"message", "model.interaction0.Message0", null);
			addCoveredInMessage0Recv0Description = removedFromReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", "model.interaction0.Message0", null);
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", "model.interaction0.Message0Recv0", null);
			addSendEventInMessageDescription = changedReference("model.interaction0.Message0", "sendEvent",
					"model.interaction0.Message0Send0", null);
			addCoveredByInLifeline0Description = removedFromReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Send0");
			addCoveredByInLifeline1Description = removedFromReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Recv0");
		} else {
			addMessageDescription = added("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageSendDescription = added("model.interaction0.Message0Send0"); //$NON-NLS-1$
			addMessageRecvDescription = added("model.interaction0.Message0Recv0");
			addCoveredInMessage0Send0Description = addedToReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Send0Description = changedReference("model.interaction0.Message0Send0",
					"message", null, "model.interaction0.Message0");
			addCoveredInMessage0Recv0Description = addedToReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", null, "model.interaction0.Message0");
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", null, "model.interaction0.Message0Recv0");
			addSendEventInMessageDescription = changedReference("model.interaction0.Message0", "sendEvent",
					null, "model.interaction0.Message0Send0");
			addCoveredByInLifeline0Description = addedToReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Send0");
			addCoveredByInLifeline1Description = addedToReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Recv0");
		}

		final Diff addMessage = Iterators.find(differences.iterator(), addMessageDescription);
		final Diff addCoveredInMessage0Send0 = Iterators.find(differences.iterator(),
				addCoveredInMessage0Send0Description);
		final Diff addMessageInMessage0Send0 = Iterators.find(differences.iterator(),
				addMessageInMessage0Send0Description);
		final Diff addCoveredInMessage0Recv0 = Iterators.find(differences.iterator(),
				addCoveredInMessage0Recv0Description);
		final Diff addMessageInMessage0Recv0 = Iterators.find(differences.iterator(),
				addMessageInMessage0Recv0Description);
		final Diff addReceiveEventInMessage = Iterators.find(differences.iterator(),
				addReceiveEventInMessageDescription);
		final Diff addSendEventInMessage = Iterators.find(differences.iterator(),
				addSendEventInMessageDescription);
		final Diff addCoveredByInLifeline0 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline0Description);
		final Diff addCoveredByInLifeline1 = Iterators.find(differences.iterator(),
				addCoveredByInLifeline1Description);
		final Diff addMessageSend = Iterators.find(differences.iterator(), addMessageSendDescription);
		final Diff addMessageRecv = Iterators.find(differences.iterator(), addMessageRecvDescription);

		assertNotNull(addMessage);
		assertNotNull(addCoveredInMessage0Send0);
		assertNotNull(addMessageInMessage0Send0);
		assertNotNull(addCoveredInMessage0Recv0);
		assertNotNull(addMessageInMessage0Recv0);
		assertNotNull(addReceiveEventInMessage);
		assertNotNull(addSendEventInMessage);
		assertNotNull(addCoveredByInLifeline0);
		assertNotNull(addCoveredByInLifeline1);
		assertNotNull(addMessageSend);
		assertNotNull(addMessageRecv);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(differences, instanceOf(MessageChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(differences.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(4), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addReceiveEventInMessage));
			assertTrue(addUMLMessage.getRefinedBy().contains(addSendEventInMessage));
			assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Recv0));
			assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Send0));
		} else {
			addUMLMessage = Iterators.find(differences.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessage.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredInMessage0Send0.getRequires().size()));
			assertTrue(addCoveredInMessage0Send0.getRequires().contains(addMessageSend));

			assertSame(Integer.valueOf(2), Integer.valueOf(addMessageInMessage0Send0.getRequires().size()));
			assertTrue(addMessageInMessage0Send0.getRequires().contains(addMessageSend));
			assertTrue(addMessageInMessage0Send0.getRequires().contains(addMessage));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredInMessage0Recv0.getRequires().size()));
			assertTrue(addCoveredInMessage0Recv0.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(2), Integer.valueOf(addMessageInMessage0Recv0.getRequires().size()));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessageRecv));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessage));

			assertSame(Integer.valueOf(2), Integer.valueOf(addReceiveEventInMessage.getRequires().size()));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessage));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(2), Integer.valueOf(addSendEventInMessage.getRequires().size()));
			assertTrue(addSendEventInMessage.getRequires().contains(addMessage));
			assertTrue(addSendEventInMessage.getRequires().contains(addMessageSend));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredByInLifeline0.getRequires().size()));
			assertTrue(addCoveredByInLifeline0.getRequires().contains(addMessageSend));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredByInLifeline1.getRequires().size()));
			assertTrue(addCoveredByInLifeline1.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageSend.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageRecv.getRequires().size()));

		} else {
			assertSame(Integer.valueOf(4), Integer.valueOf(addMessage.getRequires().size()));
			assertTrue(addMessage.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessage.getRequires().contains(addSendEventInMessage));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Recv0));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Send0));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredInMessage0Send0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageInMessage0Send0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredInMessage0Recv0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageInMessage0Recv0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addReceiveEventInMessage.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addSendEventInMessage.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredByInLifeline0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredByInLifeline1.getRequires().size()));

			assertSame(Integer.valueOf(4), Integer.valueOf(addMessageSend.getRequires().size()));
			assertTrue(addMessageSend.getRequires().contains(addSendEventInMessage));
			assertTrue(addMessageSend.getRequires().contains(addCoveredByInLifeline0));
			assertTrue(addMessageSend.getRequires().contains(addCoveredInMessage0Send0));
			assertTrue(addMessageSend.getRequires().contains(addMessageInMessage0Send0));

			assertSame(Integer.valueOf(4), Integer.valueOf(addMessageRecv.getRequires().size()));
			assertTrue(addMessageRecv.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredByInLifeline1));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addMessageInMessage0Recv0));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(2), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addCoveredInMessage0Send0.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addCoveredInMessage0Send0.getEquivalence()
				.getDifferences().size()));
		assertTrue(addCoveredInMessage0Send0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Send0));
		assertTrue(addCoveredInMessage0Send0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline0));

		assertNotNull(addCoveredInMessage0Recv0.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addCoveredInMessage0Recv0.getEquivalence()
				.getDifferences().size()));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Recv0));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline1));

	}

	private void testAB2(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		final Iterator<Diff> itUseFullDiffs = Iterators.filter(differences.iterator(),
				not(ofKind(DifferenceKind.MOVE)));
		final List<Diff> useFullDiffs = new ArrayList<Diff>();
		while (itUseFullDiffs.hasNext()) {
			Diff diff = (Diff)itUseFullDiffs.next();
			useFullDiffs.add(diff);
		}

		assertSame(Integer.valueOf(7), Integer.valueOf(useFullDiffs.size()));

		Predicate<? super Diff> addMessageDescription = null;
		Predicate<? super Diff> addCoveredInMessage0Recv0Description = null;
		Predicate<? super Diff> addMessageInMessage0Recv0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline0Description = null;
		Predicate<? super Diff> addReceiveEventInMessageDescription = null;
		Predicate<? super Diff> addMessageRecvDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addMessageDescription = removed("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = removed("model.interaction0.Message0Recv0"); //$NON-NLS-1$
			addCoveredInMessage0Recv0Description = removedFromReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", "model.interaction0.Message0", null);
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", "model.interaction0.Message0Recv0", null);
			addCoveredByInLifeline0Description = removedFromReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Recv0");
		} else {
			addMessageDescription = added("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = added("model.interaction0.Message0Recv0");
			addCoveredInMessage0Recv0Description = addedToReference("model.interaction0.Message0Recv0",
					"covered", "model.interaction0.Lifeline0");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Recv0",
					"message", null, "model.interaction0.Message0");
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"receiveEvent", null, "model.interaction0.Message0Recv0");
			addCoveredByInLifeline0Description = addedToReference("model.interaction0.Lifeline0",
					"coveredBy", "model.interaction0.Message0Recv0");
		}

		final Diff addMessage = Iterators.find(useFullDiffs.iterator(), addMessageDescription);
		final Diff addCoveredInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredInMessage0Recv0Description);
		final Diff addMessageInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addMessageInMessage0Recv0Description);
		final Diff addReceiveEventInMessage = Iterators.find(useFullDiffs.iterator(),
				addReceiveEventInMessageDescription);
		final Diff addCoveredByInLifeline0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredByInLifeline0Description);
		final Diff addMessageRecv = Iterators.find(useFullDiffs.iterator(), addMessageRecvDescription);

		assertNotNull(addMessage);
		assertNotNull(addCoveredInMessage0Recv0);
		assertNotNull(addMessageInMessage0Recv0);
		assertNotNull(addReceiveEventInMessage);
		assertNotNull(addCoveredByInLifeline0);
		assertNotNull(addMessageRecv);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(useFullDiffs, instanceOf(MessageChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addReceiveEventInMessage));
			assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Recv0));
		} else {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessage.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredInMessage0Recv0.getRequires().size()));
			assertTrue(addCoveredInMessage0Recv0.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(2), Integer.valueOf(addMessageInMessage0Recv0.getRequires().size()));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessageRecv));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessage));

			assertSame(Integer.valueOf(2), Integer.valueOf(addReceiveEventInMessage.getRequires().size()));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessage));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredByInLifeline0.getRequires().size()));
			assertTrue(addCoveredByInLifeline0.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageRecv.getRequires().size()));

		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(addMessage.getRequires().size()));
			assertTrue(addMessage.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Recv0));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredInMessage0Recv0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageInMessage0Recv0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addReceiveEventInMessage.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredByInLifeline0.getRequires().size()));

			assertSame(Integer.valueOf(4), Integer.valueOf(addMessageRecv.getRequires().size()));
			assertTrue(addMessageRecv.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addMessageInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredByInLifeline0));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addCoveredInMessage0Recv0.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addCoveredInMessage0Recv0.getEquivalence()
				.getDifferences().size()));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Recv0));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline0));

		// FIXME
		assertFalse("No move expected", Iterators.filter(differences.iterator(), ofKind(DifferenceKind.MOVE))
				.hasNext());

	}

	private void testAB3(TestKind kind, final Comparison comparison) {
		final List<Diff> differences = comparison.getDifferences();

		// We should have no less and no more than 7 differences
		final Iterator<Diff> itUseFullDiffs = Iterators.filter(differences.iterator(),
				not(ofKind(DifferenceKind.MOVE)));
		final List<Diff> useFullDiffs = new ArrayList<Diff>();
		while (itUseFullDiffs.hasNext()) {
			Diff diff = (Diff)itUseFullDiffs.next();
			useFullDiffs.add(diff);
		}

		assertSame(Integer.valueOf(7), Integer.valueOf(useFullDiffs.size()));

		Predicate<? super Diff> addMessageDescription = null;
		Predicate<? super Diff> addCoveredInMessage0Recv0Description = null;
		Predicate<? super Diff> addMessageInMessage0Recv0Description = null;
		Predicate<? super Diff> addCoveredByInLifeline0Description = null;
		Predicate<? super Diff> addReceiveEventInMessageDescription = null;
		Predicate<? super Diff> addMessageRecvDescription = null;

		if (kind.equals(TestKind.DELETE)) {
			addMessageDescription = removed("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = removed("model.interaction0.Message0Send0"); //$NON-NLS-1$
			addCoveredInMessage0Recv0Description = removedFromReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Send0",
					"message", "model.interaction0.Message0", null);
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"sendEvent", "model.interaction0.Message0Send0", null);
			addCoveredByInLifeline0Description = removedFromReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Send0");
		} else {
			addMessageDescription = added("model.interaction0.Message0"); //$NON-NLS-1$
			addMessageRecvDescription = added("model.interaction0.Message0Send0");
			addCoveredInMessage0Recv0Description = addedToReference("model.interaction0.Message0Send0",
					"covered", "model.interaction0.Lifeline1");
			addMessageInMessage0Recv0Description = changedReference("model.interaction0.Message0Send0",
					"message", null, "model.interaction0.Message0");
			addReceiveEventInMessageDescription = changedReference("model.interaction0.Message0",
					"sendEvent", null, "model.interaction0.Message0Send0");
			addCoveredByInLifeline0Description = addedToReference("model.interaction0.Lifeline1",
					"coveredBy", "model.interaction0.Message0Send0");
		}

		final Diff addMessage = Iterators.find(useFullDiffs.iterator(), addMessageDescription);
		final Diff addCoveredInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredInMessage0Recv0Description);
		final Diff addMessageInMessage0Recv0 = Iterators.find(useFullDiffs.iterator(),
				addMessageInMessage0Recv0Description);
		final Diff addReceiveEventInMessage = Iterators.find(useFullDiffs.iterator(),
				addReceiveEventInMessageDescription);
		final Diff addCoveredByInLifeline0 = Iterators.find(useFullDiffs.iterator(),
				addCoveredByInLifeline0Description);
		final Diff addMessageRecv = Iterators.find(useFullDiffs.iterator(), addMessageRecvDescription);

		assertNotNull(addMessage);
		assertNotNull(addCoveredInMessage0Recv0);
		assertNotNull(addMessageInMessage0Recv0);
		assertNotNull(addReceiveEventInMessage);
		assertNotNull(addCoveredByInLifeline0);
		assertNotNull(addMessageRecv);

		// CHECK EXTENSION
		assertSame(Integer.valueOf(1), count(useFullDiffs, instanceOf(MessageChange.class)));
		Diff addUMLMessage = null;
		if (kind.equals(TestKind.ADD)) {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.ADD)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(2), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addReceiveEventInMessage));
			assertTrue(addUMLMessage.getRefinedBy().contains(addCoveredInMessage0Recv0));
		} else {
			addUMLMessage = Iterators.find(useFullDiffs.iterator(), and(instanceOf(MessageChange.class),
					ofKind(DifferenceKind.DELETE)));
			assertNotNull(addUMLMessage);
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
			assertSame(Integer.valueOf(1), Integer.valueOf(addUMLMessage.getRefinedBy().size()));
			assertTrue(addUMLMessage.getRefinedBy().contains(addMessage));
		}

		// CHECK REQUIREMENT
		if (kind.equals(TestKind.ADD)) {

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessage.getRequires().size()));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredInMessage0Recv0.getRequires().size()));
			assertTrue(addCoveredInMessage0Recv0.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(2), Integer.valueOf(addMessageInMessage0Recv0.getRequires().size()));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessageRecv));
			assertTrue(addMessageInMessage0Recv0.getRequires().contains(addMessage));

			assertSame(Integer.valueOf(2), Integer.valueOf(addReceiveEventInMessage.getRequires().size()));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessage));
			assertTrue(addReceiveEventInMessage.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(1), Integer.valueOf(addCoveredByInLifeline0.getRequires().size()));
			assertTrue(addCoveredByInLifeline0.getRequires().contains(addMessageRecv));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageRecv.getRequires().size()));

		} else {
			assertSame(Integer.valueOf(2), Integer.valueOf(addMessage.getRequires().size()));
			assertTrue(addMessage.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessage.getRequires().contains(addMessageInMessage0Recv0));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredInMessage0Recv0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addMessageInMessage0Recv0.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addReceiveEventInMessage.getRequires().size()));

			assertSame(Integer.valueOf(0), Integer.valueOf(addCoveredByInLifeline0.getRequires().size()));

			assertSame(Integer.valueOf(4), Integer.valueOf(addMessageRecv.getRequires().size()));
			assertTrue(addMessageRecv.getRequires().contains(addReceiveEventInMessage));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addMessageInMessage0Recv0));
			assertTrue(addMessageRecv.getRequires().contains(addCoveredByInLifeline0));
		}

		// CHECK EQUIVALENCE
		assertSame(Integer.valueOf(1), Integer.valueOf(comparison.getEquivalences().size()));

		assertNotNull(addCoveredInMessage0Recv0.getEquivalence());
		assertSame(Integer.valueOf(2), Integer.valueOf(addCoveredInMessage0Recv0.getEquivalence()
				.getDifferences().size()));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredInMessage0Recv0));
		assertTrue(addCoveredInMessage0Recv0.getEquivalence().getDifferences().contains(
				addCoveredByInLifeline0));

		// FIXME
		assertFalse("No move expected", Iterators.filter(differences.iterator(), ofKind(DifferenceKind.MOVE))
				.hasNext());

	}

	@Override
	protected AbstractInputData getInput() {
		return input;
	}

}
