package org.eclipse.emf.compare.provider;

import org.eclipse.jface.viewers.StyledString;

/**
 * @author <a href="mailto:axel.richard@obeo.fr">Axel Richard</a>
 */
public interface IItemStyledLabelProvider {

	/**
	 * Returns the styled text label for the given object.
	 * 
	 * @param object
	 *            the object to evaluate the styled string for.
	 * @return the styled string.
	 */
	public StyledString getStyledText(Object object);
}
