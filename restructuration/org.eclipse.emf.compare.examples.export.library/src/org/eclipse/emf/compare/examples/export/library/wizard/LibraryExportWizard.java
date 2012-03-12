/*******************************************************************************
 * Copyright (c) 2008, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.export.library.wizard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSnapshot;
import org.eclipse.emf.compare.diff.metamodel.ComparisonSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeLeftTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.examples.export.library.Book;
import org.eclipse.emf.compare.examples.export.library.Library;
import org.eclipse.emf.compare.examples.export.library.LibraryPackage;
import org.eclipse.emf.compare.examples.export.library.Member;
import org.eclipse.emf.compare.examples.export.library.provider.LibraryEditPlugin;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

/**
 * Wizard used by the "library report" action. It will export the result of a comparison as a human readable
 * report of the modification that have been made into the library.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class LibraryExportWizard extends BasicNewResourceWizard {
	/** Match of the comparison this wizard is meant to export. */
	private MatchModel match;

	/** Diff of the comparison this wizard is meant to export. */
	private DiffModel diff;

	/** References the page displayed by this wizard. */
	private LibraryExportWizardPage page;

	/**
	 * Initializes this wizard given the active workbench and the snapshot that will be exported.
	 * 
	 * @param workbench
	 *            Active workbench.
	 * @param inputSnapshot
	 *            The {@link ModelInputSnapshot} that is to be exported by this wizard.
	 */
	public void init(IWorkbench workbench, ComparisonSnapshot inputSnapshot) {
		super.init(workbench, new StructuredSelection());
		setWindowTitle("New File");
		setNeedsProgressMonitor(true);
		// ensures no modification will be made to the input
		if (inputSnapshot instanceof ComparisonResourceSnapshot) {
			match = (MatchModel)EcoreUtil.copy(((ComparisonResourceSnapshot)inputSnapshot).getMatch());
			diff = (DiffModel)EcoreUtil.copy(((ComparisonResourceSnapshot)inputSnapshot).getDiff());
		} else {
			match = (MatchModel)EcoreUtil.copy(((ComparisonResourceSetSnapshot)inputSnapshot)
					.getMatchResourceSet().getMatchModels().get(0));
			diff = (DiffModel)EcoreUtil.copy(((ComparisonResourceSetSnapshot)inputSnapshot)
					.getDiffResourceSet().getDiffModels().get(0));
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.wizards.newresource.BasicNewResourceWizard#initializeDefaultPageImageDescriptor()
	 */
	@Override
	protected void initializeDefaultPageImageDescriptor() {
		final URL imageURL = LibraryEditPlugin.getPlugin().getBundle().getEntry("icons/newfile_wiz.gif"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(ImageDescriptor.createFromURL(imageURL));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		boolean result = false;

		final IFile createdFile = page.createNewFile();
		if (createdFile != null) {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("<html><head><title>"); //$NON-NLS-1$
			// Report title
			buffer.append("Library report"); //$NON-NLS-1$
			buffer.append("</title>"); //$NON-NLS-1$
			// Adds a basic CSS style
			buffer.append(generateCSS());
			buffer.append("</head><body><h1>Library "); //$NON-NLS-1$
			// Will retrieve the "Library" object to get its name
			final Match2Elements matchElem = (Match2Elements)match.getMatchedElements().get(0);
			buffer.append(((Library)matchElem.getLeftElement()).getName());
			buffer.append("</h1>"); //$NON-NLS-1$

			// Handles the member changes
			final String newMembersTable = generateNewMemberTable();
			final String removedMembersTable = generateRemovedMemberTable();
			if (newMembersTable.length() > 0 || removedMembersTable.length() > 0) {
				buffer.append("<div class=\"box\" name=\"members\">"); //$NON-NLS-1$
				buffer.append("<h2>Member changes</h2>"); //$NON-NLS-1$
				buffer.append(newMembersTable);
				buffer.append(removedMembersTable);
				buffer.append("</div>"); //$NON-NLS-1$
			}

			// Handles the books changes
			final String newBooksTable = generateNewBookTable();
			final String removedBooksTable = generateRemovedBookTable();
			if (newBooksTable.length() > 0 || removedBooksTable.length() > 0) {
				buffer.append("<div class=\"box\" name=\"catalogue\"><br/>"); //$NON-NLS-1$
				buffer.append("<h2>Catalogue Changes</h2>"); //$NON-NLS-1$
				buffer.append(newBooksTable);
				buffer.append(removedBooksTable);
				buffer.append("</div>"); //$NON-NLS-1$
			}

			// Handles the borrowed books changes
			final String borrowedTable = generateBorrowedTable();
			final String returnedTable = generateReturnedTable();
			if (borrowedTable.length() > 0 || returnedTable.length() > 0) {
				buffer.append("<div class=\"box\" name=\"borrowed\"><br/>"); //$NON-NLS-1$
				buffer.append("<h2>Borrowed Books</h2>"); //$NON-NLS-1$
				buffer.append(borrowedTable);
				buffer.append(returnedTable);
				buffer.append("</div>"); //$NON-NLS-1$
			}

			buffer.append("</body></html>"); //$NON-NLS-1$

			try {
				final BufferedWriter writer = new BufferedWriter(new FileWriter(createdFile.getLocation()
						.toFile()));
				writer.write(buffer.toString());
				writer.flush();
				writer.close();
				result = true;
			} catch (final IOException e) {
				final IStatus status = new Status(IStatus.ERROR,
						"org.eclipse.emf.compare.examples.export.library", //$NON-NLS-1$
						"Couldn't create file " + createdFile.getFullPath(), e);
				LibraryEditPlugin.getPlugin().getLog().log(status);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		page = new LibraryExportWizardPage("libraryReport", getSelection()); //$NON-NLS-1$
		page.setTitle("Library");
		page.setDescription("Creates a new report on library comparison.");
		page.setFileName("result.html"); //$NON-NLS-1$
		addPage(page);
	}

	/**
	 * This will return an html table for the members that have been added to the library.
	 * 
	 * @return An html table for the added members, an empty String if there are none.
	 */
	private String generateNewMemberTable() {
		String newMembers = "";
		final TreeIterator<EObject> iterator = diff.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			if (next instanceof ModelElementChangeLeftTarget) {
				final EObject addedElement = ((ModelElementChangeLeftTarget)next).getLeftElement();
				if (addedElement instanceof Member) {
					// We need to create the table headers
					if (newMembers.length() == 0) {
						newMembers += "<table class=\"table_left\"><tr>"; //$NON-NLS-1$
						newMembers += "<td class=\"header\" colspan=\"2\">New Members</td></tr>"; //$NON-NLS-1$
						newMembers += "<tr><td class=\"header\">Name</td><td class=\"header\">ID</td></tr>"; //$NON-NLS-1$
					}
					newMembers += "<tr><td>"; //$NON-NLS-1$
					newMembers += ((Member)addedElement).getName();
					newMembers += "</td><td>"; //$NON-NLS-1$
					newMembers += ((Member)addedElement).getId();
					newMembers += "</td></tr>"; //$NON-NLS-1$
				}
			}
		}
		// Closes the table if we found added members
		if (newMembers.length() > 0) {
			newMembers += "</table>"; //$NON-NLS-1$
		}
		return newMembers;
	}

	/**
	 * This will return an html table for the books that have been added to the library.
	 * 
	 * @return An html table for the added books, an empty String if there are none.
	 */
	private String generateNewBookTable() {
		String newBooks = "";
		final TreeIterator<EObject> iterator = diff.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			if (next instanceof ModelElementChangeLeftTarget) {
				final EObject addedElement = ((ModelElementChangeLeftTarget)next).getLeftElement();
				if (addedElement instanceof Book) {
					// We need to create the table headers
					if (newBooks.length() == 0) {
						newBooks += "<table class=\"table_left\"><tr>"; //$NON-NLS-1$
						newBooks += "<td class=\"header\" colspan=\"2\">New Books</td></tr>"; //$NON-NLS-1$
						newBooks += "<tr><td class=\"header\">Title</td><td class=\"header\">Author</td></tr>"; //$NON-NLS-1$
					}
					newBooks += "<tr><td>"; //$NON-NLS-1$
					newBooks += ((Book)addedElement).getTitle();
					newBooks += "</td><td>"; //$NON-NLS-1$
					newBooks += ((Book)addedElement).getAuthor().getName();
					newBooks += "</td></tr>"; //$NON-NLS-1$
				}
			}
		}
		// Closes the table if we found added books
		if (newBooks.length() > 0) {
			newBooks += "</table>"; //$NON-NLS-1$
		}
		return newBooks;
	}

	/**
	 * This will return an html table for all changes corresponding to a newly borrowed book.
	 * 
	 * @return An html table for the borrowed books, an empty String if there are none.
	 */
	private String generateBorrowedTable() {
		String borrowedBooks = "";
		final TreeIterator<EObject> iterator = diff.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			if (next instanceof ReferenceChangeLeftTarget) {
				final EReference target = ((ReferenceChangeLeftTarget)next).getReference();
				if (target.getFeatureID() == LibraryPackage.MEMBER__BORROWED_BOOKS) {
					// We need to create the table headers
					if (borrowedBooks.length() == 0) {
						borrowedBooks += "<table class=\"table_left\"><tr>"; //$NON-NLS-1$
						borrowedBooks += "<td class=\"header\" colspan=\"2\">Borrowed Books</td></tr>"; //$NON-NLS-1$
						borrowedBooks += "<tr><td class=\"header\">Title</td><td class=\"header\">Member</td></tr>"; //$NON-NLS-1$
					}
					final Book borrowed = (Book)((ReferenceChangeLeftTarget)next).getLeftTarget();
					final Member member = (Member)((ReferenceChangeLeftTarget)next).getRightElement();
					borrowedBooks += "<tr><td>"; //$NON-NLS-1$
					borrowedBooks += borrowed.getTitle();
					borrowedBooks += "</td><td>"; //$NON-NLS-1$
					borrowedBooks += member.getName();
					borrowedBooks += "</td></tr>"; //$NON-NLS-1$
				}
			}
		}
		// Closes the table if we found borrowals
		if (borrowedBooks.length() > 0) {
			borrowedBooks += "</table>"; //$NON-NLS-1$
		}
		return borrowedBooks;
	}

	/**
	 * This will return an html table for all changes corresponding to a returned book.
	 * 
	 * @return An html table for the returned books, an empty String if there are none.
	 */
	private String generateReturnedTable() {
		String returnedBooks = "";
		final TreeIterator<EObject> iterator = diff.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			if (next instanceof ReferenceChangeRightTarget) {
				final EReference target = ((ReferenceChangeRightTarget)next).getReference();
				if (target.getFeatureID() == LibraryPackage.MEMBER__BORROWED_BOOKS) {
					// We need to create the table headers
					if (returnedBooks.length() == 0) {
						returnedBooks += "<table><tr>"; //$NON-NLS-1$
						returnedBooks += "<td class=\"header\" colspan=\"2\">Returned Books</td></tr>"; //$NON-NLS-1$
						returnedBooks += "<tr><td class=\"header\">Title</td><td class=\"header\">Member</td></tr>"; //$NON-NLS-1$
					}
					final Book returned = (Book)((ReferenceChangeRightTarget)next).getLeftTarget();
					final Member member = (Member)((ReferenceChangeRightTarget)next).getRightElement();
					returnedBooks += "<tr><td>"; //$NON-NLS-1$
					returnedBooks += returned.getTitle();
					returnedBooks += "</td><td>"; //$NON-NLS-1$
					returnedBooks += member.getName();
					returnedBooks += "</td></tr>"; //$NON-NLS-1$
				}
			}
		}
		// Closes the table if we found returned books
		if (returnedBooks.length() > 0) {
			returnedBooks += "</table>"; //$NON-NLS-1$
		}
		return returnedBooks;
	}

	/**
	 * This will return an html table for the members that have been removed from the library.
	 * 
	 * @return An html table for the removed members, an empty String if there are none.
	 */
	private String generateRemovedMemberTable() {
		String removedMembers = "";
		final TreeIterator<EObject> iterator = diff.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			if (next instanceof ModelElementChangeRightTarget) {
				final EObject removedElement = ((ModelElementChangeRightTarget)next).getRightElement();
				if (removedElement instanceof Member) {
					// We need to create the table headers
					if (removedMembers.length() == 0) {
						removedMembers += "<table><tr>"; //$NON-NLS-1$
						removedMembers += "<td class=\"header\" colspan=\"2\">Removed Members</td></tr>"; //$NON-NLS-1$
						removedMembers += "<tr><td class=\"header\">Name</td><td class=\"header\">ID</td></tr>"; //$NON-NLS-1$
					}
					removedMembers += "<tr><td>"; //$NON-NLS-1$
					removedMembers += ((Member)removedElement).getName();
					removedMembers += "</td><td>"; //$NON-NLS-1$
					removedMembers += ((Member)removedElement).getId();
					removedMembers += "</td></tr>"; //$NON-NLS-1$
				}
			}
		}
		// Closes the table if we found removed members
		if (removedMembers.length() > 0) {
			removedMembers += "</table>"; //$NON-NLS-1$
		}
		return removedMembers;
	}

	/**
	 * This will return an html table for the books that have been removed from the library.
	 * 
	 * @return An html table for the removed books, an empty String if there are none.
	 */
	private String generateRemovedBookTable() {
		String removedBooks = "";
		final TreeIterator<EObject> iterator = diff.eAllContents();
		while (iterator.hasNext()) {
			final EObject next = iterator.next();
			if (next instanceof ModelElementChangeRightTarget) {
				final EObject removedElement = ((ModelElementChangeRightTarget)next).getRightElement();
				if (removedElement instanceof Book) {
					// We need to create the table headers
					if (removedBooks.length() == 0) {
						removedBooks += "<table><tr>"; //$NON-NLS-1$
						removedBooks += "<td class=\"header\" colspan=\"2\">Removed Books</td></tr>"; //$NON-NLS-1$
						removedBooks += "<tr><td class=\"header\">Title</td><td class=\"header\">Author</td></tr>"; //$NON-NLS-1$
					}
					removedBooks += "<tr><td>"; //$NON-NLS-1$
					removedBooks += ((Book)removedElement).getTitle();
					removedBooks += "</td><td>"; //$NON-NLS-1$
					removedBooks += ((Book)removedElement).getAuthor().getName();
					removedBooks += "</td></tr>"; //$NON-NLS-1$
				}
			}
		}
		// Closes the table if we found removed members
		if (removedBooks.length() > 0) {
			removedBooks += "</table>"; //$NON-NLS-1$
		}
		return removedBooks;
	}

	/**
	 * This will generate a basic CSS style for the result html page.
	 * 
	 * @return Basic CSS style.
	 */
	private String generateCSS() {
		String css = "<style type=\"text/css\">"; //$NON-NLS-1$
		css += "h1 {color:#000099; text-align:center;}"; //$NON-NLS-1$
		css += "h2 {color:#0000BB; text-align:center;}"; //$NON-NLS-1$
		css += "table {border:4px groove #000077; border-collapse:collapse;}"; //$NON-NLS-1$
		css += "td {border:2px groove #000077; text-align:center;}"; //$NON-NLS-1$
		css += ".header {font-weight:bold;}"; //$NON-NLS-1$
		css += ".table_left {float:left; margin-right:10px;}"; //$NON-NLS-1$
		css += ".box {clear:both}"; //$NON-NLS-1$
		css += "</style>"; //$NON-NLS-1$
		return css;
	}
}
