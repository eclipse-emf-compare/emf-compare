package org.eclipse.emf.compare.ide.ui.tests.unit.outgoing;

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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This checks the logical model in the following configuration:
 * 
 * <pre>
 * /project-2/file1.ecore -> n-ary EReference non-containment to file2.ecore
 * /project-2/file2.ecore
 * </pre>
 * 
 * The file for which the logical model is computed is "file1.ecore".
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@RunWith(Parameterized.class)
public class OutgoingSingleEReferenceTest extends LocalResolutionTest {

	protected static final String FILE1_NAME = "file1.ecore"; //$NON-NLS-1$

	protected static final String FILE2_NAME = "file2.ecore"; //$NON-NLS-1$

	public OutgoingSingleEReferenceTest(CrossReferenceResolutionScope scopeToUse,
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
						{OUTGOING, of(of(resourceURI(file1()), resourceURI(file2()))), of(file1(), file2()) },
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
		final ResourceSet resourceSet = new ResourceSetImpl();

		final File file1 = project.getOrCreateFile(iProject, FILE1_NAME);
		final File file2 = project.getOrCreateFile(iProject, FILE2_NAME);
		iFile1 = project.getIFile(iProject, file1);
		iFile2 = project.getIFile(iProject, file2);

		final Resource resource1 = connectResource(iFile1, resourceSet);
		final Resource resource2 = connectResource(iFile2, resourceSet);

		EPackage root1 = createPackage(null, "root1"); //$NON-NLS-1$
		resource1.getContents().add(root1);
		EPackage root2 = createPackage(null, "root2"); //$NON-NLS-1$
		resource2.getContents().add(root2);
		EClass c1 = createClass(root1, "C1"); //$NON-NLS-1$
		EClass c2 = createClass(root2, "C2"); //$NON-NLS-1$

		// Here we set the 1-ary EReference from file1 to file2
		EReference ref = EcoreFactory.eINSTANCE.createEReference();
		ref.setName("refToC2"); //$NON-NLS-1$
		ref.setEType(c2);
		c1.getEStructuralFeatures().add(ref);

		save(resourceSet);
	}
}
