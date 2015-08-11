package org.eclipse.emf.compare.ide.ui.tests.unit;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.graph.IGraphView;
import org.eclipse.emf.compare.graph.PruningIterator;
import org.eclipse.emf.compare.ide.ui.internal.EMFCompareIDEUIPlugin;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.CrossReferenceResolutionScope;
import org.eclipse.emf.compare.ide.ui.internal.logical.resolver.ThreadedModelResolver;
import org.eclipse.emf.compare.ide.ui.internal.preferences.EMFCompareUIPreferences;
import org.eclipse.emf.compare.ide.ui.tests.egit.CompareGitTestCase;
import org.eclipse.emf.compare.ide.utils.ResourceUtil;
import org.eclipse.emf.compare.ide.utils.StorageTraversal;
import org.eclipse.emf.compare.internal.utils.Graph;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This captures useful behaviours to facilitate the writing of test cases.
 * 
 * @author <a href="mailto:laurent.delaigue@obeo.fr">Laurent Delaigue</a>
 */
@SuppressWarnings("restriction")
public class LogicalModelGraphTest extends CompareGitTestCase {

	protected ResolvingResult resolveTraversalOf(IFile file) throws Exception {
		ThreadedModelResolver resolver = new ThreadedModelResolver();
		resolver.setGraph(new Graph<URI>());
		resolver.initialize();
		StorageTraversal traversal = resolver.resolveLocalModel(file, new NullProgressMonitor());
		Set<Set<URI>> subGraphs = getSubGraphs(resolver.getGraphView());
		return new ResolvingResult(subGraphs, traversal);
	}

	protected void setResolutionScope(CrossReferenceResolutionScope scope) {
		final IPreferenceStore store = EMFCompareIDEUIPlugin.getDefault().getPreferenceStore();
		store.setValue(EMFCompareUIPreferences.RESOLUTION_SCOPE_PREFERENCE, scope.name());
	}

	protected Set<Set<URI>> getSubGraphs(IGraphView<URI> graph) {
		PruningIterator<URI> iterator = graph.breadthFirstIterator();
		Set<URI> roots = new LinkedHashSet<URI>();
		while (iterator.hasNext()) {
			roots.add(iterator.next());
			iterator.prune();
		}

		Set<Set<URI>> subgraphs = new LinkedHashSet<Set<URI>>();
		Set<URI> knownURIs = new HashSet<URI>();
		for (URI root : roots) {
			if (!knownURIs.contains(root)) {
				Set<URI> subgraph = graph.getSubgraphContaining(root);
				knownURIs.addAll(subgraph);
				subgraphs.add(subgraph);
			}
		}
		return subgraphs;
	}

	public static class ExpectedResult {
		private final Set<? extends Set<URI>> subGraphs;

		private final int diagnosticSeverity;

		private final Set<? extends IStorage> storagesInModel;

		public ExpectedResult(Set<? extends Set<URI>> subGraphs, Set<? extends IStorage> storagesInModel,
				int diagnosticSeverity) {
			this.subGraphs = subGraphs;
			this.storagesInModel = storagesInModel;
			this.diagnosticSeverity = diagnosticSeverity;
		}

		public Set<? extends Set<URI>> getSubGraphs() {
			return subGraphs;
		}

		public int getDiagnosticSeverity() {
			return diagnosticSeverity;
		}

		public Set<? extends IStorage> getStoragesInModel() {
			return storagesInModel;
		}
	}

	protected static URI resourceURI(String path) {
		return URI.createPlatformResourceURI(path, true);
	}

	protected static Set<URI> uriSet(IStorage... storages) {
		return ImmutableSet.copyOf(Iterables.transform(Arrays.asList(storages), ResourceUtil.asURI()));
	}

	protected static Set<IStorage> storageSet(IStorage... storages) {
		return Sets.<IStorage> newLinkedHashSet(Arrays.asList(storages));
	}

	protected static ExpectedResult expectError(Set<? extends Set<URI>> expectedGraph,
			Set<? extends IStorage> expectedTraversal) {
		return new ExpectedResult(expectedGraph, expectedTraversal, Diagnostic.ERROR);
	}

	protected static ExpectedResult expectOk(Set<? extends Set<URI>> expectedGraph,
			Set<? extends IStorage> expectedTraversal) {
		return new ExpectedResult(expectedGraph, expectedTraversal, Diagnostic.OK);
	}

	public static class ResolvingResult {
		private final Set<Set<URI>> subGraphs;

		private final StorageTraversal traversal;

		public ResolvingResult(Set<Set<URI>> subGraphs, StorageTraversal traversal) {
			this.subGraphs = subGraphs;
			this.traversal = traversal;
		}

		public Set<Set<URI>> getSubGraphs() {
			return subGraphs;
		}

		public StorageTraversal getTraversal() {
			return traversal;
		}
	}
}
