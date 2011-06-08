package org.eclipse.emf.compare.tests.merge.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

public class EcoreMergeInputData {

	public EObject getA() throws IOException {
		String path = "a.ecore";
		return loadFromClassloader(path);
	}

	public EObject getB() throws IOException {
		return loadFromClassloader("b.ecore");
	}

	public EObject getC() throws IOException {
		return loadFromClassloader("c.ecore");
	}

	public EObject getD() throws IOException {
		return loadFromClassloader("d.ecore");
	}

	public EObject getE() throws IOException {
		return loadFromClassloader("e.ecore");
	}

	public EObject getF() throws IOException {
		return loadFromClassloader("f_orderchange.ecore");
	}

	public EObject getG() throws IOException {
		return loadFromClassloader("g_move.ecore");
	}

	public EObject getH() throws IOException {
		return loadFromClassloader("h_delete.ecore");
	}

	public EObject getUML171() throws IOException {
		return loadFromClassloader("UML171.ecore");
	}

	public EObject getUML172() throws IOException {
		return loadFromClassloader("UML172.ecore");
	}

	public EObject getUML173() throws IOException {
		return loadFromClassloader("UML173.ecore");
	}

	private EObject loadFromClassloader(String string) throws IOException {
		InputStream str = this.getClass().getResourceAsStream(string);
		XMIResourceImpl res = new XMIResourceImpl(URI.createURI("http://" + string));
		res.load(str, Collections.EMPTY_MAP);
		return res.getContents().get(0);
	}
}
