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
public enum SingleChanges implements Change {

	CHANGE_INT {
		public void apply(ResourceSet rs) {
			inst1(rs).eSet("intval", 1);
		}

		public String getL2RDocu() {
			return "The value of attribute 'intval' is set to '1'";
		}

		public String getR2LDocu() {
			return "The value of attribute 'intval' is set to '10'";
		}

		protected String patch() {
			return "intval = '10' | '1';";
		}

	},
	CHANGE_STRING {
		public void apply(ResourceSet rs) {
			inst1(rs).eSet("strval", "MyNewStringValue");
		}

		public String getL2RDocu() {
			return "The value of attribute 'strval' is set to 'MyNewStringValue'";
		}

		public String getR2LDocu() {
			return "The value of attribute 'strval' is set to 'MyStringValue'";
		}

		protected String patch() {
			return "strval = 'MyStringValue' | 'MyNewStringValue';";
		}

	},
	SET_INT {
		public void apply(ResourceSet rs) {
			inst1(rs).eSet("intvalunset", 1);
		}

		protected String patch() {
			return "intvalunset = '0' | '1';";
		}
	},
	SET_STRING {
		public void apply(ResourceSet rs) {
			inst1(rs).eSet("strvalunset", "StrVal");
		}

		public String getL2RDocu() {
			return "The value of attribute 'strvalunset' is set to 'StrVal'";
		}

		public String getR2LDocu() {
			return "The value of attribute 'strval' is unset. "
					+ "This is the same as calling eobject.eUnset() for the attribute or "
					+ "as assignng the default value";
		}

		protected String patch() {
			return "strvalunset = null | 'StrVal';";
		}
	},
	UNSET_INT {
		public void apply(ResourceSet rs) {
			inst1(rs).eUnset("intval");
		}

		protected String patch() {
			return "intval = '10' | '0';";
		}

	},
	UNSET_STRING {
		public void apply(ResourceSet rs) {
			inst1(rs).eUnset("strval");
		}

		protected String patch() {
			return "strval = 'MyStringValue' | null;";
		}
	};

	private static final String inst1 = "SimpleMM1Instance1.xmi";

	public String asPatch() {
		return "epatch " + name() + " {\n resource res0 {\n" + "  left uri 'SimpleMM1Instance1.xmi';\n"
				+ " right uri 'SimpleMM1Instance11.xmi';\n}\n" + "object res0#/ {\n" + patch() + " } }\n";
	}

	public String getL2RDocu() {
		return null;
	}

	public String getR2LDocu() {
		return null;
	}

	public ResourceSet getResourceSet() {
		return DynEObject.eCreateResourceSet(getClass(), inst1);
	}

	protected DynEObject inst1(ResourceSet rs) {
		return DynEObject.eGetResource(rs, inst1);
	};

	protected abstract String patch();

}
