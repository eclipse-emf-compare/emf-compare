/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent2;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class Echo extends AbstractWorkflowComponent2 {
	protected final Logger log = Logger.getLogger(getClass());

	@Override
	protected void checkConfigurationInternal(Issues issues) {
	}

	protected String fileName = null;

	public void setFile(String fn) {
		fileName = fn;
	}

	public void addLine(String line) {
		lines.add(line);
	}

	protected ArrayList<String> lines = new ArrayList<String>();

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor, Issues issues) {
		if (fileName != null) {
			try {
				PrintStream ps = new PrintStream(new FileOutputStream(fileName));
				for (String line : lines)
					ps.println(line);
				ps.flush();
				ps.close();
			} catch (IOException e) {
				issues.addError("Error creating output stream for " + fileName, e);
			}
		} else
			for (String line : lines)
				log.info(line);
	}

	@Override
	public String getLogMessage() {
		return fileName != null ? "Writing " + fileName : null;
	}
}
