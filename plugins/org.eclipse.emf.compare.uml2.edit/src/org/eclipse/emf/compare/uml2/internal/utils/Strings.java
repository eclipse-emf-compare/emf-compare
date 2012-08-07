package org.eclipse.emf.compare.uml2.internal.utils;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class Strings {

	private Strings() {
	}

	public final static String elide(String original, int max, String suffix) {
		if (original.length() > max) {
			String elided = original.substring(0, original.length() - suffix.length());
			return elided + suffix;
		}
		return original;
	}
}
