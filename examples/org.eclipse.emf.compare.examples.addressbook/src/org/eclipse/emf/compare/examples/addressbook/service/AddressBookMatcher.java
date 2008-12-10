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
package org.eclipse.emf.compare.examples.addressbook.service;

import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressBook;
import org.eclipse.emf.compare.examples.addressbook.addressbook.AddressbookPackage;
import org.eclipse.emf.compare.examples.addressbook.addressbook.Contact;
import org.eclipse.emf.compare.examples.addressbook.addressbook.People;
import org.eclipse.emf.compare.match.api.IMatchEngine;
import org.eclipse.emf.compare.match.engine.GenericMatchEngine;
import org.eclipse.emf.ecore.EObject;

/**
 * This Match engine is specific for {@link AddressbookPackage} models. It match elements from a given model
 * to another similar model and may then be used to produce a {@link DiffModel}. People are matched only using
 * their name similarity, contacts are matched using the enclosing people and anything else is matched
 * re-using the generic EMF Compare match engine ( {@link GenericMatchEngine} ) .
 * 
 * @author Cedric Brun <cedric.brun@obeo.fr>
 */
public class AddressBookMatcher extends GenericMatchEngine implements IMatchEngine {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isSimilar(EObject obj1, EObject obj2) throws FactoryException {
		/*
		 * If the instance metaclasses are not the same, then we don't want to match them.
		 */
		if (obj1.eClass() != obj2.eClass())
			return false;
		/*
		 * If we've got a People, only check the name similarity.
		 */
		if (obj1 instanceof People || obj2 instanceof People)
			return nameSimilarity(obj1, obj2) > 0.8;
		/*
		 * A Contact doesn't have interesting information, but contacts should be matched thanks to the people
		 * containing them. FIXME : many contacts inside the same people may be badly matched and then
		 * differences may be detected.
		 */
		if (obj1 instanceof Contact) {
			if (obj1.eContainer() instanceof People && obj2.eContainer() instanceof People)
				return isSimilar(obj1.eContainer(), obj2.eContainer());
		}
		/*
		 * Let's just say that two instances of AdressBook are always the same.
		 */
		if (obj1 instanceof AddressBook && obj2 instanceof AddressBook)
			return true;
		/*
		 * If it's something we don't know about, then use the generic behavior.
		 */
		return super.isSimilar(obj1, obj2);
	}
}
