/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.examples.addressbook.addressbook.spec;

import java.util.Iterator;

import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookFactory;
import org.eclipse.emf.compare.examples.addressbook.addressbook.BookVersion;
import org.eclipse.emf.compare.examples.addressbook.addressbook.impl.RepositoryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author Cedric Brun <cedric.brun@obeo.fr>
 */
public class RepositorySpec extends RepositoryImpl {

	@Override
	public AddressBook checkout(int id) {
		if (id == -1)
			return checkout(getHead());
		final Iterator<BookVersion> it = getHistory().iterator();
		while (it.hasNext()) {
			final BookVersion cur = it.next();
			if (cur.getId() == id)
				return checkout(cur.getBook());
		}
		return null;
	}

	private AddressBook checkout(AddressBook book) {
		return (AddressBook)EcoreUtil.copy(book);
	}

	@Override
	public void checkin() {
		final int maxversion = getMaxVersion();
		final BookVersion newVersion = AddressbookFactory.eINSTANCE.createBookVersion();
		newVersion.setId(maxversion + 1);
		newVersion.setBook((AddressBook)EcoreUtil.copy(getHead()));
	}

	private int getMaxVersion() {
		int max = -1;
		final Iterator<BookVersion> it = getHistory().iterator();
		while (it.hasNext()) {
			final BookVersion version = it.next();
			if (version.getId() > max) {
				max = version.getId();
			}
		}
		return max;
	}

}
