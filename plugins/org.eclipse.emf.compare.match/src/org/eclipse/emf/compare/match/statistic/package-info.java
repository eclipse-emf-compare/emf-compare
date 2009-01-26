/*
 * (non-javadoc)
 * Copyright (c) 2006, 2009 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
/**
 * This package provides the logic behind EMF Compare's matching process.
 * <p>
 * The metrics and similarity computation features are all provided by the classes
 * within these packages. {@link MetamodelFilter} allows us to filter unused 
 * attributes/references of the models, 
 * {@link org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity} is in
 * charge of computing String similarities and 
 * {@link org.eclipse.emf.compare.match.statistic.similarity.StructureSimilarity} is
 * used to compute structural similarities on the EObjects.
 * </p>
 */
package org.eclipse.emf.compare.match.statistic;