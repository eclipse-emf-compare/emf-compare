package org.eclipse.emf.compare.ide.ui.tests.logical.resolver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.compare.ide.ui.internal.logical.SimilarityComputer;
import org.junit.Test;

public class SimilarityComputerTest {

	private static final String LIPSUM1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In imperdiet risus eu hendrerit facilisis. Nullam at blandit elit."; //$NON-NLS-1$

	private static final String LIPSUM2 = "Aenean fermentum turpis sit amet auctor pretium. Maecenas dignissim nisi mi, eu fermentum eros lobortis et. Quisque rutrum neque nec."; //$NON-NLS-1$

	public static final String PREFIX;

	public static final String PREFIX2;

	static {
		StringBuilder prefixBuilder = new StringBuilder();
		while (prefixBuilder.length() < SimilarityComputer.MINIMUM_LENGTH) {
			prefixBuilder.append(LIPSUM1);
			prefixBuilder.append('\n');
		}
		PREFIX = prefixBuilder.toString();

		prefixBuilder = new StringBuilder();
		while (prefixBuilder.length() < SimilarityComputer.MINIMUM_LENGTH) {
			prefixBuilder.append(LIPSUM2);
			prefixBuilder.append('\n');
		}
		PREFIX2 = prefixBuilder.toString();
	}

	@Test
	public void testIdentical() throws IOException {
		String a = PREFIX;
		String b = PREFIX;

		assertTrue(SimilarityComputer.isSimilar(stream(a), stream(b)));
	}

	@Test
	public void testMinorAddition() throws IOException {
		String a = PREFIX;
		String b = PREFIX + "another line\n"; //$NON-NLS-1$

		assertTrue(SimilarityComputer.isSimilar(stream(a), stream(b)));
	}

	@Test
	public void testMinorDeletion() throws IOException {
		String a = PREFIX + "old line\n"; //$NON-NLS-1$
		String b = PREFIX;

		assertTrue(SimilarityComputer.isSimilar(stream(a), stream(b)));
	}

	@Test
	public void testMinorChange() throws IOException {
		String a = PREFIX + "old line\n"; //$NON-NLS-1$
		String b = PREFIX + "new line\n"; //$NON-NLS-1$

		assertTrue(SimilarityComputer.isSimilar(stream(a), stream(b)));
	}

	@Test
	public void testMajorChange() throws IOException {
		String a = PREFIX + "old line\n"; //$NON-NLS-1$
		String b;

		StringBuilder bBuilder = new StringBuilder();
		while (bBuilder.length() < a.length()) {
			bBuilder.append(LIPSUM2);
			bBuilder.append('\n');
		}
		b = bBuilder.toString();

		assertFalse(SimilarityComputer.isSimilar(stream(a), stream(b)));
	}

	@Test
	public void testTooShort() throws IOException {
		String a = LIPSUM1 + '\n' + LIPSUM2;
		String b = a;

		assertFalse(SimilarityComputer.isSimilar(stream(a), stream(b)));
	}

	private InputStream stream(String string) throws IOException {
		return new ByteArrayInputStream(string.getBytes("UTF-8")); //$NON-NLS-1$
	}

}
