/*******************************************************************************
 * Copyright (c) 2015 EclipseSource Muenchen GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Dirix - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.diagram.papyrus.tests.util;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.papyrus.infra.core.resource.AbstractBaseModel;
import org.eclipse.papyrus.infra.core.resource.sasheditor.DiModel;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.papyrus.uml.tools.model.UmlModel;

/**
 * Util class to access the save parameters of Papyrus.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public final class PapyrusSaveParameterUtil {

	/**
	 * No public constructor for util classes.
	 */
	private PapyrusSaveParameterUtil() {
	}

	/**
	 * Returns the save parameter for UML model files used by Papyrus.
	 * 
	 * @return A collection of save parameters.
	 */
	public static Map<Object, Object> getUMLSaveParameter() {
		final UMLSaveParametersHelper helper = new UMLSaveParametersHelper();
		return helper.getSaveParametersForTest();
	}

	/**
	 * Returns the save parameter for DI model files used by Papyrus.
	 * 
	 * @return A collection of save parameters.
	 */
	public static Map<Object, Object> getDISaveParameter() {
		final DISaveParametersHelper helper = new DISaveParametersHelper();
		return helper.getSaveParametersForTest();
	}

	/**
	 * Returns the save parameter for Notation model files used by Papyrus.
	 * 
	 * @return A collection of save parameters.
	 */
	public static Map<Object, Object> getNotationSaveParameter() {
		final NotationSaveParametersHelper helper = new NotationSaveParametersHelper();
		return helper.getSaveParametersForTest();
	}

	/**
	 * Returns the default parameter for abstract model files offered by Papyrus.
	 * 
	 * @return A collection of save parameters.
	 */
	public static Map<Object, Object> getDefaultSaveParameter() {
		final DefaultSaveParametersHelper helper = new DefaultSaveParametersHelper();
		return helper.getSaveParametersForTest();
	}

	/**
	 * Helper class to retrieve the save parameters of Papyrus uml models.
	 */
	private static class UMLSaveParametersHelper extends UmlModel {
		public Map<Object, Object> getSaveParametersForTest() {
			return super.getSaveOptions();
		}
	}

	/**
	 * Helper class to retrieve the save parameters of Papyrus di models.
	 */
	private static class DISaveParametersHelper extends DiModel {
		public Map<Object, Object> getSaveParametersForTest() {
			return super.getSaveOptions();
		}
	}

	/**
	 * Helper class to retrieve the save parameters of Papyrus notation models.
	 */
	private static class NotationSaveParametersHelper extends NotationModel {
		public Map<Object, Object> getSaveParametersForTest() {
			return super.getSaveOptions();
		}
	}

	/**
	 * Helper class to retrieve the save parameters of Papyrus abstract base models.
	 */
	private static class DefaultSaveParametersHelper extends AbstractBaseModel {
		@Override
		protected String getModelFileExtension() {
			return null;
		}

		@Override
		public String getIdentifier() {
			return null;
		}

		public Map<Object, Object> getSaveParametersForTest() {
			return super.getSaveOptions();
		}

		// since Papyrus 2.0 we have to implement canPersist
		// we omit @Override on purpose to be backward compatible
		@SuppressWarnings("all")
		public boolean canPersist(EObject eObject) {
			return false;
		}

		// since Papyrus 2.0 we have to implement persist
		// we omit @Override on purpose to be backward compatible
		@SuppressWarnings("all")
		public void persist(EObject object) {
			// no implementation
		}
	}

	/**
	 * Tests if two save parameter maps are equal. Since some options use objects as values an ordinary
	 * {@link Map#equals(Object)} will always return {@code false}. These options are checked manually, the
	 * "normal" options are checked via {@link Map#equals(Object)}
	 * 
	 * @param saveParameters1
	 *            Save parameter map to compare.
	 * @param saveParameters2
	 *            Save parameter map to compare.
	 * @return {@code true} if the given parameter are equal, {@code false} otherwise.
	 */
	public static boolean isEqual(final Map<?, ?> saveParameters1, final Map<?, ?> saveParameters2) {
		// Check URI Handler manually since the value is a dynamic object
		if (saveParameters1.containsKey(XMLResource.OPTION_URI_HANDLER)) {
			final Object uriHandler1 = saveParameters1.get(XMLResource.OPTION_URI_HANDLER);
			final Object uriHandler2 = saveParameters2.get(XMLResource.OPTION_URI_HANDLER);

			if (uriHandler1 != null && uriHandler2 != null) {
				if (!uriHandler1.getClass().equals(uriHandler2.getClass())) {
					return false;
				}
				saveParameters1.remove(XMLResource.OPTION_URI_HANDLER);
				saveParameters2.remove(XMLResource.OPTION_URI_HANDLER);
			}
		}
		return saveParameters1.equals(saveParameters2);
	}
}
