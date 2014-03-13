/*******************************************************************************
 * Copyright (c) 2013, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.match.eobject.internal;

/**
 * A class responsible for tracking statistics about a given comparison process.
 * 
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class ProximityMatchStats {
	/**
	 * number of comparison dones looking for identic objects.
	 */
	private int nbIndenticComparison;

	/**
	 * number of comparison dones looking for similar objects.
	 */
	private int nbMaxDistComparison;

	/**
	 * number of match not found.
	 */
	private int nbNoMatch;

	/**
	 * number of match found using the identic comparison.
	 */
	private int nbSuccessIdenticComparison;

	/**
	 * number of match found using the similarity comparison.
	 */
	private int nbSuccessMaxComparison;

	/**
	 * number of backtracks we had to do.
	 */
	private int nbBacktrack;

	/**
	 * number of double check done.
	 */
	private int nbDoubleCheck;

	/**
	 * number of double check which failed.
	 */
	private int nbFailedDoubleCheck;

	/**
	 * A backtrack has been done.
	 */
	public void backtrack() {
		nbBacktrack++;
	}

	/**
	 * We successfully matched two objects while trying to find identic objects.
	 */
	public void identicSuccess() {
		nbSuccessIdenticComparison++;
	}

	/**
	 * We compared two objects looking for identic objects.
	 */
	public void identicCompare() {
		nbIndenticComparison++;
	}

	/**
	 * We compared two objects by their similarity.
	 */
	public void similarityCompare() {
		nbMaxDistComparison++;
	}

	/**
	 * Double checked a candidate pair of match.
	 */
	public void doubleCheck() {
		nbDoubleCheck++;

	}

	/**
	 * We achieved a match using similarity.
	 */
	public void similaritySuccess() {
		nbSuccessMaxComparison++;

	}

	/**
	 * A double check has failed.
	 */
	public void failedDoubleCheck() {
		nbFailedDoubleCheck++;
	}

	/**
	 * We found no match for an Object.
	 */
	public void noMatch() {
		nbNoMatch++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("nls")
	public String toString() {
		return "ProximityMatchStats [nbIndenticComparison=" + nbIndenticComparison + ", nbMaxDistComparison="
				+ nbMaxDistComparison + ", nbnoMatch=" + nbNoMatch + ", nbSuccessIdenticComparison="
				+ nbSuccessIdenticComparison + ", nbSuccessMaxComparison=" + nbSuccessMaxComparison
				+ ", nbBacktrack=" + nbBacktrack + ", nbDoubleCheck=" + nbDoubleCheck
				+ ", nbFailedDoubleCheck=" + nbFailedDoubleCheck + "]";
	}

}
