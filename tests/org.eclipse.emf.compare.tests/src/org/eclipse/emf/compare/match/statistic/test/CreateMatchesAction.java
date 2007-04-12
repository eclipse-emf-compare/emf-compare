package org.eclipse.emf.compare.match.statistic.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.generic.DiffMaker;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.match.statistic.test.util.EMFCompareTestCase;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Initialize an "expected" directory from an input one browsing the
 * subdirectories. If a directory contains 2 models, then a 2way comparison is
 * done, if there's 3 a 3Way comparison is done with the first model as ancestor
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 * 
 */
public class CreateMatchesAction extends EMFCompareTestCase implements
		IObjectActionDelegate {

	private final String MATCH_DIR = "expected/"; //$NON-NLS-1$

	/**
	 * return true if the folder contains 2 models we could compare.
	 * 
	 * @return true if the folder contains 2 models we could compare.
	 * @throws CoreException
	 */
	private boolean is2WayComparable(final IFolder folder) throws CoreException {
		int count = 0;

		for (int i = 0; i < folder.members().length; i++) {
			if (folder.members()[i] instanceof IFile) {
				count += 1;
			}
		}
		return count == 2;
	}

	private void log(final String line) {
		System.out.println(line);

	}

	/**
	 * Create match models in the match_dir from models found in input_dir
	 * 
	 * @param input_dir
	 * @param match_dir
	 * @throws CoreException
	 * @throws FactoryException
	 * @throws InterruptedException 
	 */
	public void doCreateMatches(final IFolder input_dir, final IFolder match_dir)
			throws CoreException, FactoryException, InterruptedException {
		if (is2WayComparable(input_dir)) {
			final List models = new ArrayList();

			for (int i = 0; i < input_dir.members().length; i++) {
				if (input_dir.members()[i] instanceof IFile
						&& !((IFile) input_dir.members()[i]).isDerived()
						&& !((IFile) input_dir.members()[i]).getName()
								.startsWith(".")) { //$NON-NLS-1$
					models.add(load((IFile) input_dir.members()[i]));
				}
			}
			if (models.size() != 2) {
				System.err
						.println("ERROR : missing model or unable to load it - size : " //$NON-NLS-1$
								+ models.size());
			} else {
				// System.out.println("Creating match in " +
				// match_dir.toString()
				// + " from " + input_dir.toString() + " model."
				// + "models =" + models);
				final Date start = Calendar.getInstance().getTime();

				final MatchModel match = new MatchService().doMatch(
						(EObject) models.get(0), (EObject) models.get(1),new NullProgressMonitor());
				final String matchFile = match_dir.getFullPath().toString()
						+ "/result.match"; //$NON-NLS-1$
				final String diffFile = match_dir.getFullPath().toString()
						+ "/result.diff"; //$NON-NLS-1$
				final DiffMaker diffM = new DiffMaker();
				final DiffModel diff = diffM.doDiff(match);
				final Date end = Calendar.getInstance().getTime();
				log(getSize((EObject) models.get(0)) + ";" //$NON-NLS-1$
						+ (end.getTime() - start.getTime()));
				try {
					save(match, matchFile);
					save(diff, diffFile);
				} catch (final IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

	private int getSize(final EObject object) {
		final TreeIterator it = object.eAllContents();
		int result = 0;
		while (it.hasNext()) {
			it.next();
			result += 1;
		}
		return result;
	}

	/**
	 * Create the matches and diff browsing the rootInput
	 * 
	 * @param rootInput
	 */
	public void createMatches(final IFolder rootInput) {

		try {
			final IFolder rootMatch = rootInput.getProject().getFolder(
					this.MATCH_DIR);
			if (!rootMatch.exists()) {
				rootMatch.create(true, true, null);
			}
			rootInput.accept(new IResourceVisitor() {

				public boolean visit(final IResource resource) {
					if (resource.getName().startsWith(".")) {
						return false;
					}
					if (resource instanceof IFolder) {
						try {
							doCreateMatches((IFolder) resource,
									getMatchDirFromInput((IFolder) resource));
						} catch (final CoreException e) {
							e.printStackTrace();
						} catch (final FactoryException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return true;
					}
					return false;
				}
			});
		} catch (final CoreException e) {
			e.printStackTrace();
		}
	}

	protected IFolder getMatchDirFromInput(final IFolder folder)
			throws CoreException {
		final IPath relativePath = folder.getProjectRelativePath();
		final IFolder result = folder.getProject().getFolder(
				this.MATCH_DIR + relativePath.toString());
		if (!result.exists()) {
			result.create(true, true, null);
		}
		return result;

	}

	public void setActivePart(final IAction action,
			final IWorkbenchPart targetPart) {
	}

	public void run(final IAction action) {
		log("Performances measure done the " //$NON-NLS-1$
				+ Calendar.getInstance().getTime().toLocaleString());
		log("Number of elements;Time"); //$NON-NLS-1$
		createMatches(((IFolder) this.selection.getFirstElement()));

	}

	private IStructuredSelection selection;

	public void selectionChanged(final IAction action,
			final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
		}

	}

}
