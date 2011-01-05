/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.tests.testdata;

import org.eclipse.emf.compare.epatch.tests.util.DynEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public enum ListChanges implements Change {
	ADD_INT {
		public void apply(ResourceSet rs) {
			inst1(rs).eAdd("intlist", 1);
		}

		public String getL2RDocu() {
			return "Sets the value of attribute 'intlist' to '1' at index 5. "
					+ "Assuming that the list only had five items before, "
					+ "this means adding one to the list's end.";
		}

		public String getR2LDocu() {
			return "The value at index 5 is removed from the attribute 'intlist'.";
		}

		protected String patch() {
			return "intlist = [ | 5:'1' ];";
		}

	},
	ADD_REMOVE {
		public void apply(ResourceSet rs) {
			DynEObject o = inst1(rs);
			o.eAdd("intlist", 1);
			o.eRemove("intlist", 2);
		}

		public String getL2RDocu() {
			return "From the attribute 'intlist', first " + "the value at index 2 is removed "
					+ "and then value '1' is inserted at index 4";
		}

		public String getR2LDocu() {
			return "From the attribute 'intlist', first " + "the value at index 4 is removed "
					+ "and then value '6' is inserted at index 2";
		}

		protected String patch() {
			return "intlist = [ 2:'6' | 4:'1' ];";
		}
	},
	LIST_SET {
		public void apply(ResourceSet rs) {
			inst1(rs).eSet("intlist", 2, 23);
		}

		public String getL2RDocu() {
			return "Sets the value of attribute 'intlist' to 23 at index 2";
		}

		public String getR2LDocu() {
			return "Sets the value of attribute 'intlist' to 6 at index 2";
		}

		protected String patch() {
			return "intlist = [ 2:'6' | 2:'23' ];";
		}
	},
	MOVE_INT {
		public void apply(ResourceSet rs) {
			inst1(rs).eMove("intlist", 4, 1);
		}

		public String getL2RDocu() {
			return "For the attribute 'intlist', the element at index 1 is "
					+ "removed from the list and then inserted again at index 4. "
					+ "This effectively moves the element from index 1 to index 4.";
		}

		public String getR2LDocu() {
			return "For the attribute 'intlist', the element at index 4 is "
					+ "removed from the list and then inserted again at index 1. "
					+ "This effectively moves the element from index 4 to index 1.";
		}

		protected String patch() {
			return "intlist = [ 1:[4] | 4:[1] ];";
		}
	},
	MULTIPLE_ADD {
		public void apply(ResourceSet rs) {
			DynEObject o = inst1(rs);
			o.eAdd("intlist", 2, 14);
			o.eAdd("intlist", 2, 13);
			o.eAdd("intlist", 2, 12);
			o.eAdd("intlist", 2, 11);
		}

		public String getL2RDocu() {
			return "For the attribute 'intlist', the value 11 is inserted at index 2, "
					+ "then value 12 is inserted at index 3, and so on. "
					+ "The values are inserted into the list in exactly this "
					+ "order to ensure that after the adding process each "
					+ "value is at the index desctibed for it in the Epatch";
		}

		public String getR2LDocu() {
			return "For attribute 'intlist', the values at the indices 5, 4, 3, "
					+ "and 2 are removed, starting with the element at the highest index. "
					+ "This order ensures that the removal of one element has no "
					+ "sideeffefts on the indices of elements sthat sill have to be removed.";
		}

		protected String patch() {
			return "intlist = [ | 2:'11', 3:'12', 4:'13', 5:'14' ];";
		}
	},
	MULTIPLE_REMOVE {
		public void apply(ResourceSet rs) {
			DynEObject o = inst1(rs);
			o.eRemove("intlist", 1);
			o.eRemove("intlist", 1);
			o.eRemove("intlist", 1);
			o.eRemove("intlist", 1);
		}

		public String getL2RDocu() {
			return null;
		}

		public String getR2LDocu() {
			return null;
		}

		protected String patch() {
			return "intlist = [ 4:'10', 3:'8', 2:'6', 1:'4' | ];";
		}
	},
	REMOVE_INT {
		public void apply(ResourceSet rs) {
			inst1(rs).eRemove("intlist", 2);
		}

		public String getL2RDocu() {
			return "The value at index 2 from attribute 'intlist' is removed.";
		}

		public String getR2LDocu() {
			return "The value '6' is inserted into the attribute 'intlist' at index 2";
		}

		protected String patch() {
			return "intlist = [ 2:'6' | ];";
		}
	}/*
	 * , SWAP_INT { public void apply(ResourceSet rs) { DynEObject o = inst1(rs); o.eMove("intlist", 1, 3);
	 * o.eMove("intlist", 3, 2); } public String getL2RDocu() { return
	 * "The values at index 1 and 3 are swapped."; } public String getR2LDocu() { return
	 * "The values at index 1 and 3 are swapped."; } protected String patch() { return
	 * "intlist = [ 3:[1], 1:[3] | 1:[3], 3:[1] ];"; } }
	 */;

	private static final String inst1 = "SimpleMM1Instance1.xmi";

	public String asPatch() {
		return "epatch " + name() + " {\n resource res0 {\n" + "  left uri 'SimpleMM1Instance1.xmi';\n"
				+ " right uri 'SimpleMM1Instance11.xmi';\n}\n" + "object res0#/ {\n" + patch() + " } }\n";
	}

	public ResourceSet getResourceSet() {
		return DynEObject.eCreateResourceSet(getClass(), inst1);
	};

	protected DynEObject inst1(ResourceSet rs) {
		return DynEObject.eGetResource(rs, inst1);
	}

	protected abstract String patch();

}
