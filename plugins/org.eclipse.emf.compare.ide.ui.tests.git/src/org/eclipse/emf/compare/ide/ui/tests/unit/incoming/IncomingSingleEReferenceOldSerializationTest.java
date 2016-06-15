package org.eclipse.emf.compare.ide.ui.tests.unit.incoming;

import static com.google.common.collect.ImmutableSet.of;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.CONTAINER;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.OUTGOING;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.PROJECT;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.SELF;
import static org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope.WORKSPACE;

import java.io.File;
import java.util.Arrays;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.tests.unit.LocalResolutionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This checks the logical model in the following configuration:
 * 
 * <pre>
 * /project-2/types.ecore
 * /project-2/model.ecore -> 1-ary EReference non-containment to types.ecore
 * </pre>
 * 
 * The file for which the logical model is computed is "types.ecore". NOTE: This test differs from
 * {@link IncomingSingleEReferenceTest} because the files read are serialized with an old version of EMF, with
 * a format that is valid, but apparently not used anymore.
 * 
 * @see Bugzilla 473997
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@RunWith(Parameterized.class)
public class IncomingSingleEReferenceOldSerializationTest extends LocalResolutionTest {

	protected static final String FILE1_NAME = "types.ecore"; //$NON-NLS-1$

	protected static final String FILE2_NAME = "model.ecore"; //$NON-NLS-1$

	public IncomingSingleEReferenceOldSerializationTest(CrossReferenceResolutionScope scopeToUse,
			Set<? extends Set<URI>> uriSets, Set<String> traversalPaths) {
		super(scopeToUse, uriSets, traversalPaths);
	}

	@Parameters(name = "{index}: scope {0} -> graph {1}, traversal {2}")
	public static Iterable<Object[]> data1() {
		return Arrays
				.asList(new Object[][] {
						{WORKSPACE, of(of(resourceURI(file1()), resourceURI(file2()))),
								of(file1(), file2()) },
						{PROJECT, of(of(resourceURI(file1()), resourceURI(file2()))), of(file1(), file2()) },
						{CONTAINER, of(of(resourceURI(file1()), resourceURI(file2()))),
								of(file1(), file2()) },
						{OUTGOING, of(of(resourceURI(file1()))), of(file1()) },
						{SELF, of(of(resourceURI(file1()))), of(file1()) }, });
	}

	private static String file1() {
		return "/" + PROJECT2_NAME + "/" + FILE1_NAME; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String file2() {
		return "/" + PROJECT2_NAME + "/" + FILE2_NAME; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void setUpModel() throws Exception {
		final IProject iProject = project2.getProject();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		iFile1.setContents(this.getClass().getResourceAsStream(FILE1_NAME), true, false, null);
		iFile2.setContents(this.getClass().getResourceAsStream(FILE2_NAME), true, false, null);

	}
}
