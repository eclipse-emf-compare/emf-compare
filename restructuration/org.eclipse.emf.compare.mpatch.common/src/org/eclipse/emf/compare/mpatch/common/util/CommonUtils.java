/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.compare.diff.metamodel.AttributeOrderChange;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffGroup;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ReferenceOrderChange;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.MatchOptions;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchElement;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Helper class for miscellanous functions.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 */
public class CommonUtils {

	/** See {@link CommonUtils#analyzeDiff(ComparisonSnapshot, int)} for documentation. */
	public static final int DIFF_EMPTY = 1;

	/** See {@link CommonUtils#analyzeDiff(ComparisonSnapshot, int)} for documentation. */
	public static final int DIFF_ORDERINGS = 2;

	/**
	 * Create an {@link IFile} pointing to the given <code>fileName</code> in the same location as the
	 * <code>referenceURI</code>.
	 * 
	 * @param referenceURI
	 *            An arbitrary {@link URI}.
	 * @param fileName
	 *            A fileName.
	 * @return An {@link IFile} having the name <code>fileName</code> located in the location of
	 *         <code>referenceURI</code>.
	 */
	public static IFile createNewIFile(final URI referenceURI, final String fileName) {
		final IPath referencePath = new Path(referenceURI.toPlatformString(true));
		final IPath path = referencePath.removeLastSegments(1);
		final IPath newPath = path.append(fileName);
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(newPath);
		return file;
	}

	/**
	 * Create an {@link IFile} pointing to the given <code>fileName</code> in the same location as the
	 * <code>referenceFile</code>.
	 * 
	 * @param referenceFile
	 *            An arbitrary {@link IFile}.
	 * @param fileName
	 *            A fileName.
	 * @return An {@link IFile} having the name <code>fileName</code> located in the location of
	 *         <code>referenceFile</code>.
	 */
	public static IFile createNewIFile(IFile referenceFile, String fileName) {
		final IPath path = referenceFile.getFullPath().removeLastSegments(1);
		final IPath newPath = path.append(fileName);
		final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(newPath);
		return file;
	}

	/**
	 * This extracts the referenced model from an emfdiff {@link ComparisonSnapshot} - to be more precise from
	 * the (single) {@link DiffModel}. If <code>leftModel</code> is <code>true</code>, then the left
	 * (modified) version of the model is returned; if <code>false</code>, then the right (unmodified) version
	 * is returned. Note: This extraction fails if the comparison contains more than just one original model.
	 * 
	 * @param emfdiff
	 *            An emfdiff {@link ComparisonSnapshot}.
	 * @param leftModel
	 *            Indicates whether the left (changed) or right (unchanged) version of the model should be
	 *            returned.
	 * @return A reference to the original model.
	 */
	public static EObject getModelFromEmfdiff(ComparisonSnapshot emfdiff, boolean leftModel) {
		if (emfdiff instanceof ComparisonResourceSnapshot) {
			final ComparisonResourceSnapshot resourceSnapshot = (ComparisonResourceSnapshot)emfdiff;
			final EList<EObject> roots = leftModel ? resourceSnapshot.getMatch().getLeftRoots()
					: resourceSnapshot.getMatch().getRightRoots();
			if (roots.size() > 1) {
				throw new UnsupportedOperationException("This is not supported yet. Please implement it!");
			} else if (roots.size() == 0) {
				return null;
			} else {
				return roots.get(0);
			}
		} else if (emfdiff instanceof ComparisonResourceSetSnapshot) {
			final ComparisonResourceSetSnapshot resourceSetSnapshot = (ComparisonResourceSetSnapshot)emfdiff;
			if (resourceSetSnapshot.getMatchResourceSet().getMatchModels().size() > 1) {
				throw new UnsupportedOperationException("This is not supported yet. Please implement it!");
			} else {
				final EList<EObject> roots = leftModel ? resourceSetSnapshot.getMatchResourceSet()
						.getMatchModels().get(0).getLeftRoots() : resourceSetSnapshot.getMatchResourceSet()
						.getMatchModels().get(0).getRightRoots();
				if (roots.size() > 1) {
					throw new UnsupportedOperationException("This is not supported yet. Please implement it!");
				} else if (roots.size() == 0) {
					return null;
				} else {
					return roots.get(0);
				}
			}
		} else {
			throw new UnsupportedOperationException("Invalid emfdiff type detected!");
		}
	}

	/**
	 * Create an emfdiff ({@link ComparisonResourceSnapshot}) from two {@link EObject}s. This is done by first
	 * creating a {@link MatchModel} and then a {@link DiffModel}.
	 * 
	 * @param leftModel
	 *            The left model.
	 * @param rightModel
	 *            The right model.
	 * @param useIdsIfAvailable
	 *            Tell EMF Compare to use IDs if available.
	 * @return The emfdiff.
	 */
	public static ComparisonResourceSnapshot createEmfdiff(EObject leftModel, EObject rightModel,
			boolean useIdsIfAvailable) {
		MatchModel matchModel = null;
		try {
			final Map<String, Object> options = new HashMap<String, Object>();

			// use ids?
			options.put(MatchOptions.OPTION_IGNORE_ID, !useIdsIfAvailable);
			options.put(MatchOptions.OPTION_IGNORE_XMI_ID, !useIdsIfAvailable);

			// usually we operate on the same meta model
			options.put(MatchOptions.OPTION_DISTINCT_METAMODELS, false);

			matchModel = MatchService.doMatch(leftModel, rightModel, options);
		} catch (final InterruptedException e) {
			// Ignore
		}

		final DiffModel diffModel = DiffService.doDiff(matchModel, false);
		return wrapInComparisonSnapshot(matchModel, diffModel);
	}

	/**
	 * Create an emfdiff ({@link ComparisonResourceSnapshot}) from two {@link EObject}s. This is done by first
	 * creating a {@link MatchModel} and then a {@link DiffModel}.
	 * 
	 * @param leftModel
	 *            The left model.
	 * @param rightModel
	 *            The right model.
	 * @return The emfdiff.
	 */
	public static ComparisonResourceSnapshot createEmfdiff(EObject leftModel, EObject rightModel) {
		MatchModel matchModel = null;
		try {
			matchModel = MatchService.doMatch(leftModel, rightModel, Collections.<String, Object> emptyMap());
		} catch (final InterruptedException e) {
			// Ignore
		}

		final DiffModel diffModel = DiffService.doDiff(matchModel, false);
		return wrapInComparisonSnapshot(matchModel, diffModel);
	}

	private static ComparisonResourceSnapshot wrapInComparisonSnapshot(MatchModel matchModel,
			DiffModel diffModel) {
		final ComparisonResourceSnapshot comparison = DiffFactory.eINSTANCE
				.createComparisonResourceSnapshot();
		comparison.setDiff(diffModel);
		comparison.setMatch(matchModel);
		comparison.setDate(new Date());
		return comparison;
	}

	/**
	 * Analyze an EMF Compare snapshot for some criteria:
	 * <ol>
	 * <li>{@link CommonUtils#DIFF_EMPTY}: The given diff does not contain any {@link DiffElement} except for
	 * {@link DiffGroup}s.
	 * <li>{@link CommonUtils#DIFF_ORDERINGS}: The given diff does only contain ordering changes multi-valued
	 * references (not yet implemented for attributes in EMF Compare).
	 * </ol>
	 * 
	 * @param snapshot
	 *            An EMF Compare snapshot.
	 * @param criteria
	 *            The criteria which should be checked.
	 * @return A collection of {@link DiffElement}s which <b>do not</b> satisfy the given criteria, an empty
	 *         collection otherwise.
	 */
	public static Collection<DiffElement> analyzeDiff(ComparisonSnapshot snapshot, int criteria) {
		final Collection<DiffElement> violations = new ArrayList<DiffElement>();

		// maybe a bit inefficient, but the easiest implementation!
		for (final TreeIterator<EObject> i = snapshot.eAllContents(); i.hasNext();) {
			final EObject obj = i.next();
			if (obj instanceof DiffElement) {

				// we break if our criteria is violated!
				switch (criteria) {
					case DIFF_EMPTY:
						if (!(obj instanceof DiffGroup)) {
							violations.add((DiffElement)obj);
						}
						break;
					case DIFF_ORDERINGS:
						if (!(obj instanceof DiffGroup) && !(obj instanceof ReferenceOrderChange)
								&& !(obj instanceof AttributeOrderChange)) {
							violations.add((DiffElement)obj);
						}
						break;
					default:
				}
			}
		}
		return violations;

		// probably more efficient but more implementation effort

		// if (snapshot instanceof ComparisonResourceSetSnapshot) {
		// throw new
		// UnsupportedOperationException("Support for ComparisonResourceSetSnapshot not yet implemented!");
		// } else if (snapshot instanceof ComparisonResourceSnapshot) {
		// final ComparisonResourceSnapshot resourceSnapshop = (ComparisonResourceSnapshot) snapshot;
		// if (resourceSnapshop.getDiff() != null && resourceSnapshop.getDiff().getOwnedElements().size() > 0)
		// {
		//
		// // check if there exist any changes at all
		// if (criteria == DIFF_EMPTY) {
		// for (final DiffElement diff : resourceSnapshop.getDiff().getOwnedElements()) {
		// if (diff instanceof DiffGroup) {
		// if (((DiffGroup)diff).getSubchanges() > 0)
		// return false;
		// } else {
		// return true;
		// }
		// }
		// return true;
		//
		// // check if the only diffelements are about ordering
		// } else if (criteria == DIFF_ORDERINGS) {
		// for (final TreeIterator<EObject> i = resourceSnapshop.getDiff().eAllContents(); i.hasNext();) {
		// final EObject obj = i.next();
		// // TODO
		// }
		//
		// // unknown criteria!
		// } else {
		// throw new IllegalArgumentException("Unknown criteria: " + criteria);
		// }
		//
		// } else {
		// return true;
		// }
		// } else {
		// throw new UnsupportedOperationException("Unknown snapshot type: " + snapshot);
		// }
	}

	/**
	 * Get the input of the current editor, if there is one.
	 * 
	 * @return The URI of the currently selected file or <code>null</code>, if there is none.
	 */
	public static URI getCurrentEditorFileInputUri() {
		final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (editor != null) {
			try {
				final IEditorInput editorInput = editor.getEditorInput();
				if (editorInput instanceof FileEditorInput) {
					final IFile file = ((FileEditorInput)editorInput).getFile();
					if (file != null) {
						final URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);

						// lets check if we got an EMF model here
						final ResourceSet resourceSet = new ResourceSetImpl();
						final Resource resource = resourceSet.getResource(uri, true);
						if (resource != null && !resource.getContents().isEmpty()) {
							final EObject root = resource.getContents().get(0);

							// maybe we got a GMF diagram here?
							// since we don't want a dependency to GMF, use reflection to check if root is a
							// diagram.

							final EObject element = getDiagramElement(root);
							if (element != null && element.eResource() != null) {
								final URI uri2 = element.eResource().getURI();
								return uri2;
							}

							// return the uri of the editor input otherwise
							return uri;
						}
					}
				} else {
					// maybe GMF is loaded and it is a gmf diagram input?
					try {
						final URI uri = CommonGmfUtils.getUriFromEditorInput(editorInput);
						if (uri != null) {
							return uri;
						}
					} catch (NoClassDefFoundError e) {
						// do nothing..
					}
				}
			} catch (Exception e) {
				// do nothing... just return null
			}
		}
		return null;
	}

	/**
	 * Match all elements in the contents of the two given EObjects. This method makes use of EMF Compare's
	 * {@link MatchService} to find matches.
	 * 
	 * @param obj1
	 *            One EObject.
	 * @param obj2
	 *            Another EObject.
	 * @return A map containing all matched objects in both content trees.
	 */
	public static Map<EObject, EObject> getMatchingObjects(EObject obj1, EObject obj2) {
		if (obj1 == null || obj2 == null) {
			throw new IllegalArgumentException("Parameters must not be null!");
		}

		// ignore ids; we operate on the same meta model
		final Map<String, Object> options = new HashMap<String, Object>();
		options.put(MatchOptions.OPTION_IGNORE_ID, true);
		options.put(MatchOptions.OPTION_IGNORE_XMI_ID, true);
		options.put(MatchOptions.OPTION_DISTINCT_METAMODELS, false);
		final MatchModel match;
		try {
			match = MatchService.doContentMatch(obj1, obj2, options);
		} catch (InterruptedException e) {
			throw new RuntimeException("Comparing two EObjects failed!", e);
		}

		// built result map
		final Map<EObject, EObject> result = new HashMap<EObject, EObject>();
		final Queue<MatchElement> queue = new LinkedList<MatchElement>();
		queue.addAll(match.getMatchedElements());
		while (!queue.isEmpty()) {
			final MatchElement matchElement = queue.poll();
			final Match2Elements match2Elements = (Match2Elements)matchElement;
			result.put(match2Elements.getLeftElement(), match2Elements.getRightElement());
			result.put(match2Elements.getRightElement(), match2Elements.getLeftElement());
			queue.addAll(matchElement.getSubMatchElements());
		}
		return result;
	}

	/**
	 * Extract the element of a GMF diagram using EMF reflection.
	 * 
	 * @param diagram
	 *            The diagram.
	 * @return The element if diagram is really a GMF diagram.
	 */
	private static EObject getDiagramElement(EObject diagram) {
		final EClass eClass = diagram.eClass();
		if (eClass == null) {
			return null;
		}
		if ("org.eclipse.gmf.runtime.notation.Diagram".equals(eClass.getInstanceClassName())) {
			final EStructuralFeature feature = eClass.getEStructuralFeature("element");
			if (feature != null) {
				final Object object = diagram.eGet(feature);
				if (object instanceof EObject) {
					return (EObject)object;
				}
			}
		}
		return null;
	}

	/**
	 * Opposite of {@link String#split(String)}.
	 */
	public static <T extends Appendable> T join(String[] src, CharSequence pattern, T dst) throws IOException {
		for (int i = 0; i < src.length; i++) {
			if (i > 0) {
				dst.append(pattern);
			}
			dst.append(src[i]);
		}
		return dst;
	}

	/**
	 * Opposite of {@link String#split(String)}.
	 */
	public static String join(String[] src, CharSequence pattern) {
		try {
			return join(src, pattern, new StringBuilder()).toString();
		} catch (IOException e) {
			throw new Error("StringBuilder should not throw IOExceptions!");
		}
	}

	/**
	 * Opposite of {@link String#split(String)}.
	 */
	public static String join(List<? extends String> src, CharSequence pattern) {
		try {
			return join(src.toArray(new String[src.size()]), pattern, new StringBuilder()).toString();
		} catch (IOException e) {
			throw new Error("StringBuilder should not throw IOExceptions!");
		}
	}

	/**
	 * Filter map elements by their value. The values are compared using {@link Object#equals(Object)}.
	 * 
	 * @param map
	 *            A map.
	 * @param value
	 *            The value to filter.
	 * @return A list of elements (subset of keys of <code>map</code>) which have the given <code>value</code>
	 *         .
	 */
	public static <K, V> List<K> filterByValue(Map<K, V> map, V value) {
		final ArrayList<K> result = new ArrayList<K>();
		if (value != null) {
			for (K key : map.keySet()) {
				if (value.equals(map.get(key))) {
					result.add(key);
				}
			}
		}
		return result;
	}
}
