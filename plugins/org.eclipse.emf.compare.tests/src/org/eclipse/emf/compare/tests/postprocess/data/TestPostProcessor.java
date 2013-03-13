/*******************************************************************************
 * Copyright (c) 2012, 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.postprocess.data;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.compare.CompareFactory;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.postprocessor.IPostProcessor;

/**
 * This provides a post processor to add a copy of the first scanned match at the same level of the
 * comparison.
 * 
 * @author <a href="cedric.notot@obeo.fr">Cedric Notot</a>
 */
public class TestPostProcessor implements IPostProcessor {

	public void postMatch(Comparison comparison, Monitor monitor) {
		final List<Match> matches = comparison.getMatches();
		final Match lastMatch = matches.get(matches.size() - 1);
		final Match copyLastMatch = CompareFactory.eINSTANCE.createMatch();
		copyLastMatch.setLeft(lastMatch.getLeft());
		copyLastMatch.setRight(lastMatch.getRight());
		copyLastMatch.setOrigin(lastMatch.getOrigin());
		matches.add(copyLastMatch);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postConflicts(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postConflicts(Comparison comparison, Monitor monitor) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postEquivalences(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postEquivalences(Comparison comparison, Monitor monitor) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postRequirements(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postRequirements(Comparison comparison, Monitor monitor) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postDiff(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postDiff(Comparison comparison, Monitor monitor) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor#postComparison(org.eclipse.emf.compare.Comparison,
	 *      org.eclipse.emf.common.util.Monitor)
	 */
	public void postComparison(Comparison comparison, Monitor monitor) {
		// TODO Auto-generated method stub

	}

	public static class TestPostProcessorDescriptor implements IPostProcessor.Descriptor {

		private final Pattern nsURI;

		private final Pattern resourceURI;

		private final IPostProcessor postProcessor;

		private int ordinal;

		public TestPostProcessorDescriptor(Pattern nsURI, Pattern resourceURI, IPostProcessor postProcessor,
				int ordinal) {
			this.nsURI = nsURI;
			this.resourceURI = resourceURI;
			this.postProcessor = postProcessor;
			this.ordinal = ordinal;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getPostProcessor()
		 */
		public IPostProcessor getPostProcessor() {
			return postProcessor;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getNsURI()
		 */
		public Pattern getNsURI() {
			return nsURI;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getResourceURI()
		 */
		public Pattern getResourceURI() {
			return resourceURI;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getInstanceClassName()
		 */
		public String getInstanceClassName() {
			return postProcessor.getClass().getName();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#getOrdinal()
		 */
		public int getOrdinal() {
			return ordinal;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see org.eclipse.emf.compare.postprocessor.IPostProcessor.Descriptor#setOrdinal(int)
		 */
		public void setOrdinal(int parseInt) {
			ordinal = parseInt;
		}

	}

}
