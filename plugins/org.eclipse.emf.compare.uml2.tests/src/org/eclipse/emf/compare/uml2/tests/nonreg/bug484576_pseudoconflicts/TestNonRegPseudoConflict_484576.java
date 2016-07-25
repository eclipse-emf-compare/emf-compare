package org.eclipse.emf.compare.uml2.tests.nonreg.bug484576_pseudoconflicts;

import static com.google.common.collect.Collections2.filter;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Predicate;

import java.io.IOException;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Conflict;
import org.eclipse.emf.compare.ConflictKind;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLInputData;
import org.eclipse.emf.compare.uml2.tests.AbstractUMLTest;
import org.eclipse.emf.compare.uml2.tests.nonreg.bug484576_pseudoconflicts.data.NonReg484576Data;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;

/**
 * This test makes sure that bug 484576 is fixed. It means checking that pseudo-conflicts contain only
 * equivalent diffs on each side and don't aggregate several unrelated diffs.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
public class TestNonRegPseudoConflict_484576 extends AbstractUMLTest {

	private NonReg484576Data input = new NonReg484576Data();

	@Test
	public void testOnePseudoConflictPerDiff() throws IOException {
		final Resource ancestor = input.getAncestor();
		final Resource left = input.getLeft();
		final Resource right = input.getRight();

		final Comparison comparison = compare(left, right, ancestor);

		assertEquals(7, comparison.getConflicts().size());
		assertEquals(6, filter(comparison.getConflicts(), new Predicate<Conflict>() {
			public boolean apply(Conflict conflict) {
				return conflict.getKind() == ConflictKind.PSEUDO;
			}
		}).size());
	}

	@Override
	protected AbstractUMLInputData getInput() {
		return input;
	}
}
