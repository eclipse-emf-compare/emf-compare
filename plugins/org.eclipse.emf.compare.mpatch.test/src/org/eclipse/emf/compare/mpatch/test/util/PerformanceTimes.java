/*******************************************************************************
 * Copyright (c) 2010, 2011 Technical University of Denmark.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors:
 *    Patrick Koenemann, DTU Informatics - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.mpatch.test.util;

/**
 * Data class for collecting runtime of individual tasks in the framework.
 * 
 * @author Patrick Koenemann (pk@imm.dtu.dk)
 *
 */
public class PerformanceTimes {

	private long built;
	private long emfdiff;
	private long mpatch;
	private long groups;
	private long deps;
	private long resolve;
	private long validate;
	private long copy;
	private long apply;
	private long changes;
	private long check;
	private long refs;

	private long lastTime = System.currentTimeMillis();

	public final void tick() {
		lastTime = System.currentTimeMillis();
	}

	public final long getBuilt() {
		return built;
	}

	public final long getEmfdiff() {
		return emfdiff;
	}

	public final long getMPatch() {
		return mpatch;
	}

	public final long getGroups() {
		return groups;
	}

	public final long getDeps() {
		return deps;
	}

	public final long getRefs() {
		return refs;
	}

	public final long getResolve() {
		return resolve;
	}

	public final long getValidate() {
		return validate;
	}

	public final long getCopy() {
		return copy;
	}

	public final long getApply() {
		return apply;
	}

	public final long getChanges() {
		return changes;
	}

	public final long getCheck() {
		return check;
	}

	public final long getTotal() {
		return built + emfdiff + mpatch + groups + deps + resolve + validate + copy + apply + changes + check;
	}

	private final long getTime() {
		final long currentTime = System.currentTimeMillis();
		final long time = currentTime - lastTime;
		lastTime = currentTime;
		return time;
	}

	public final void setBuilt() {
		this.built = getTime();
	}

	public final void setEmfdiff() {
		this.emfdiff = getTime();
	}

	public final void setMPatch() {
		this.mpatch = getTime();
	}

	public final void setGroups() {
		this.groups = getTime();
	}

	public final void setDeps() {
		this.deps = getTime();
	}

	public final void setResolve() {
		this.resolve = getTime();
	}

	public final void setValidate() {
		this.validate = getTime();
	}

	public final void setCopy() {
		this.copy = getTime();
	}

	public final void setApply() {
		this.apply = getTime();
	}

	public final void setChanges() {
		this.changes = getTime();
	}

	public final void setCheck() {
		this.check = getTime();
	}

	public void setRefs() {
		this.refs = getTime();
	}

}
