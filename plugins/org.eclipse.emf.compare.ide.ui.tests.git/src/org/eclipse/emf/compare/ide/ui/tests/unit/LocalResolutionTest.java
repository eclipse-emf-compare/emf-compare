package org.eclipse.emf.compare.ide.ui.tests.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.mapping.ModelProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.EMFModelProvider;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.tests.workspace.TestProject;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is the super-class of parameterized unit tests that check the validity of logical model resolution in
 * each possible scope.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public abstract class LocalResolutionTest extends LogicalModelGraphTest {

	protected static final String PROJECT2_NAME = "Project-2"; //$NON-NLS-1$

	protected TestProject project2;

	protected IFile iFile1;

	protected IFile iFile2;

	private CrossReferenceResolutionScope originalResolutionScope;

	protected final CrossReferenceResolutionScope scopeToUse;

	protected final Set<? extends Set<URI>> expectedGraph;

	protected final Set<String> expectedTraversal;

	/**
	 * This parameterized test receives the scope to use and the associated expected graph and traversal.
	 * 
	 * @param scopeToUse
	 *            The resolution scope to run the test with
	 * @param uriSets
	 *            The expected graph, as a set of URI sets
	 * @param traversalPaths
	 *            The expected traversal, as a set of paths
	 */
	public LocalResolutionTest(CrossReferenceResolutionScope scopeToUse, Set<? extends Set<URI>> uriSets,
			Set<String> traversalPaths) {
		this.scopeToUse = scopeToUse;
		this.expectedGraph = uriSets;
		this.expectedTraversal = traversalPaths;
	}

	/**
	 * Implement this method in sub-classes to create models in relevant projects.
	 * 
	 * @throws Exception
	 */
	protected abstract void setUpModel() throws Exception;

	@Test
	public void test() throws Exception {
		ResolvingResult result = resolveTraversalOf(iFile1);

		Set<Set<URI>> subGraphs = result.getSubGraphs();
		assertEquals(expectedGraph.size(), subGraphs.size());
		assertTrue(subGraphs.containsAll(expectedGraph));

		StorageTraversal traversal = result.getTraversal();
		assertEquals(expectedTraversal.size(), traversal.getStorages().size());
		for (IStorage storage : traversal.getStorages()) {
			assertTrue(expectedTraversal.contains(storage.getFullPath().toString()));
		}
	}

	@Override
	@Before
	public void setUp() throws Exception {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		final String stringValue = store.getString(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE);
		originalResolutionScope = CrossReferenceResolutionScope.valueOf(stringValue);

		project2 = new TestProject(PROJECT2_NAME);

		super.setUp();

		setUpModel();
		setResolutionScope(scopeToUse);
	}

	@Override
	@After
	public void tearDown() throws Exception {
		final EMFModelProvider emfModelProvider = (EMFModelProvider)ModelProvider
				.getModelProviderDescriptor(EMFModelProvider.PROVIDER_ID).getModelProvider();
		emfModelProvider.clear();
		setResolutionScope(originalResolutionScope);
		iFile1 = null;
		iFile2 = null;
		project2.dispose();
		super.tearDown();
	}
}
