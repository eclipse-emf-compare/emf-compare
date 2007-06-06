package org.eclipse.emf.compare.ui.legacy;

import org.eclipse.compare.ITypedElement;
import org.eclipse.emf.compare.diff.DiffElement;
import org.eclipse.emf.compare.match.MatchElement;
import org.eclipse.swt.graphics.Image;

/**
 * Basic wrapper for an {@link org.eclipse.compare.ITypedElement}.
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class TypedElementWrapper implements ITypedElement {
	private Object object;

	public Image getImage() {
		return null;
		// FIXMECBR
		// return DiffUtils.computeEObjectImage(object);
	}

	public String getName() {
		return "noname - FIXME";
		// FIXMECBR
		// return DiffUtils.computeEObjectName(object);
	}

	public String getType() {
		if (this.object instanceof MatchElement) {
			return DiffConstants.DELTA_TYPE;
		}
		if (this.object instanceof DiffElement) {
			return DiffConstants.DIFF_TYPE;
		}
		return null;
	}
	
	public Object getObject() {
		return object;
	}

	public TypedElementWrapper(final Object object) {
		super();
		this.object = object;
	}
}