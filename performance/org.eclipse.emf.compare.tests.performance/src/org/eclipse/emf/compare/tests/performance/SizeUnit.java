/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.performance;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 *
 */
public enum SizeUnit {

	/**
	 * Prefixes binaires (prefixes CEI)
Nom	Symb.	Facteur
kibi	Ki	2^10 = 1 024
mebi	Mi	2^20 = 1 048 576
gibi	Gi	2^30 = 1 073 741 824
tebi	Ti	2^40 = 1 099 511 627 776
pebi	Pi	2^50 = 1 125 899 906 842 624
exbi	Ei	2^60 = 1 152 921 504 606 846 976
zebi	Zi	2^70 = 1 180 591 620 717 411 303 424
yobi	Yi	2^80 = 1 208 925 819 614 629 174 706 176
Prefixes decimaux (prefixes SI)
Nom	Symb.	Facteur	Err.
kilo	k	10^3 = 1 000	2 %
mega	M	10^6 = 1 000 000	5 %
giga	G	10^9 = 1 000 000 000	7 %
tera	T	10^12 = 1 000 000 000 000	10 %
peta	P	10^15 = 1 000 000 000 000 000	13 %
exa	E	10^18 = 1 000 000 000 000 000 000	15 %
zetta	Z	10^21 = 1 000 000 000 000 000 000 000	18 %
yotta	Y	10^24 = 1 000 000 000 000 000 000 000 000	21 %

	 */
	
	KIBI(1024), 
	MEBI(KIBI.fFactor * 1024), 
	GIBI(MEBI.fFactor * 1024), 
	TEBI(GIBI.fFactor * 1024), 
	PEBI(TEBI.fFactor * 1024),
	EXBI(PEBI.fFactor * 1024),
	ZEBI(EXBI.fFactor * 1024),
	YOBI(ZEBI.fFactor * 1024);
	
	private final long fFactor;
	
	private SizeUnit(long factor) {
		fFactor = factor;
	}
	
	public long convert(long value) {
		return (value / fFactor);
	}
	
	public double convert(double value) {
		return (value / fFactor);
	}
}
