package org.eclipse.emf.compare.diagram.ide.ui.papyrus.contentmergeviewer.facet;

import com.google.common.base.Function;

import org.eclipse.papyrus.emf.facet.custom.metamodel.v0_2_0.internal.treeproxy.EObjectTreeElement;

/**
 * Util class for handling Papyrus Facet.
 * 
 * @author Stefan Dirix <sdirix@eclipsesource.com>
 */
public final class FacetUtil {

	/**
	 * Un-Wraps the Objects returned by the Papyrus Facet mechanism.
	 */
	public static final Function<Object, Object> UN_WRAP = new Function<Object, Object>() {
		public Object apply(Object input) {
			if (EObjectTreeElement.class.isInstance(input)) {
				return EObjectTreeElement.class.cast(input).getEObject();
			}
			return input;
		}

	};

	/**
	 * Private Constructor for Util classes.
	 */
	private FacetUtil() {

	}
}
