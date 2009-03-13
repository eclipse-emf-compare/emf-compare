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
public enum ObjectChanges implements Change {
	ADD_OBJECT {
		public void apply(ResourceSet rs) {
			inst1(rs).eGetObj("tree").eAddNew("children", "CompositeNode")
					.eSet("name", "MyNewCompositeNode");
		}

		public String getL2RDocu() {
			return "A new object of type '//CompositeNode' is instantiated "
					+ "and it's EAttribute 'name' is set to 'MyNeCompositeNode'. "
					+ "Then, this object is stored at index 3 of EReference "
					+ "'childen' of the object with the fragmentURI '//@tree'";
		}

		public String getR2LDocu() {
			return "The entry at index 3 from EReference 'children' is removed.";
		}

		protected String patch() {
			return "object res0#//@tree {\n"
					+ "children = [ | 3:new mm#//CompositeNode {\n"
					+ "name = 'MyNewCompositeNode'; } ]; }";
		}
	},
	ADD_OBJECT_WITH_LIST {
		public void apply(ResourceSet rs) {
			DynEObject cn = inst1(rs).eGetObj("tree").eAddNew("children",
					"CompositeNode").eSet("name", "MyRoot");
			cn.eAddNew("children", "CompositeNode").eSet("name", "MyComp1");
			cn.eAddNew("children", "ChildNode").eSet("name", "MyLeaf1");
			cn.eAddNew("children", "ChildNode").eSet("name", "MyLeaf2");
		}

		public String getL2RDocu() {
			return "Two objects of type CompositeNode and two of type "
					+ "ChildNode are instantiated. Their 'name'-EAttribute is set. "
					+ "The first CompositeNode EReference 'children' is "
					+ "filled with the other three objects "
					+ "and the object itself gets stored at index 3 of "
					+ "EReference 'children' of the object with the fragmentURI '//@tree'. "
					+ "The 'children'-EReferences are containment-references.";
		}

		public String getR2LDocu() {
			return "The entry at index 3 from EReference 'children' is removed.";
		}

		protected String patch() {
			return "object res0#//@tree {\n"
					+ "children = [ | 3:new mm#//CompositeNode {\n"
					+ "children = [ new mm#//CompositeNode {\n"
					+ "name = 'MyComp1'; }, new mm#//ChildNode {\n"
					+ "name = 'MyLeaf1'; }, new mm#//ChildNode {\n"
					+ "name = 'MyLeaf2'; } ]; name = 'MyRoot'; } ];\n" + "}";
		}
	},
	ADD_REFERENCE {
		public void apply(ResourceSet rs) {
			DynEObject o = inst1(rs).eGetObj("tree").eGetObj("children",
					"name", "ChildWithRef");
			inst1(rs).eAdd("reflist", o.eObj());
		}

		public String getL2RDocu() {
			return "The object with the alias 'ChildWithRef' is added "
					+ "to non-containment-EReference 'reflist' at index 2"
					+ "The alias 'ChildWithRef' points to the object "
					+ "with fragmentURI '//@tree/@children.1'.";
		}

		public String getR2LDocu() {
			return "The entry at index 2 from EReference 'reflist' is removed.";
		}

		protected String patch() {
			return "object res0#/ {\n"
					+ "    reflist = [ | 2:ChildWithRef ];\n" + "  }\n"
					+ "  object ChildWithRef res0#//@tree/@children.1 { }";
		}
	},
	MOVE_OBJECT_FROM_LIST_TO_LIST {
		public void apply(ResourceSet rs) {
			DynEObject tree = inst1(rs).eGetObj("tree");
			tree.eGetObj("children", "name", "CompositeNode1").eAdd("children",
					tree.eGetObj("children", "name", "ChildWithRef").eObj());
		}

		public String getL2RDocu() {
			return "The object with alias 'ChildWithRef' is removed "
					+ "from the 'children'-EReference of object '//@tree' and "
					+ "added to the 'children'-EREference of object '//@tree/@children.0'. "
					+ "Since ChildWithRef changes it's location within "
					+ "the Resource, it's fragmentURI changes. "
					+ "'//@tree/@children.1' identifies the object in the left "
					+ "model, and '//@tree/@children.0/@children.1' in the right model.";
		}

		public String getR2LDocu() {
			return " The object with alias 'ChildWithRef' is removed "
					+ "from the 'children'-EReference of object '//@tree/@children.0' and "
					+ "added to the 'children'-EREference of object '//@tree'.";
		}

		protected String patch() {
			return "object res0#//@tree {\n"
					+ "children = [ 1:ChildWithRef | ];\n"
					+ "}\n"
					+ "object res0#//@tree/@children.0 {\n"
					+ "children = [ | 1:ChildWithRef ];\n"
					+ "}\n"
					+ "object ChildWithRef left res0#//@tree/@children.1 right res0#//@tree/@children.0/@children.1 { }";
		}
	},
	MOVE_OBJECT_FROM_SINGLE_TO_LIST {
		public void apply(ResourceSet rs) {
			DynEObject oldtree = inst1(rs).eGetObj("tree");
			inst1(rs).eUnset("tree");
			DynEObject newtree = inst1(rs).eSetNew("tree", "CompositeNode");
			newtree.eSet("name", "newTreeRoot");
			newtree.eAdd("children", oldtree.eObj());
		}

		protected String patch() {
			return "object res0#/ {\n"
					+ "    tree = RootNode | new mm#//CompositeNode {\n"
					+ "      children = [ RootNode ];\n"
					+ "      name = 'newTreeRoot';\n"
					+ "    };\n"
					+ "  }\n"
					+ "  object RootNode left res0#//@tree right res0#//@tree/@children.0 { }";
		}
	},
	REMOVE_REFERENCE {
		public void apply(ResourceSet rs) {
			inst1(rs).eRemove("reflist", "name", "Child11");
		}

		protected String patch() {
			return "object res0#/ {\n" + "    reflist = [ 1:Child11 | ];\n"
					+ "  }\n"
					+ "  object Child11 res0#//@tree/@children.0/@children.0 { }";
		}
	},
	UNSET_OBJECT_COMPLEX {
		public void apply(ResourceSet rs) {
			inst1(rs).eUnset("tree");
			inst1(rs).eRemove("reflist", "name", "Child11");
			inst1(rs).eRemove("reflist", "name", "RootNode");
		}

		protected String patch() {
			return "object res0#/ {\n"
					+ "    reflist = [ 1:Child11, 0:RootNode | ];\n"
					+ "    tree = new mm#//CompositeNode RootNode {\n"
					+ "      children = [ new mm#//CompositeNode CompositeNode1 {\n"
					+ "        children = [ new mm#//ChildNode Child11 {\n"
					+ "          name = 'Child11';\n" + "        } ];\n"
					+ "        name = 'CompositeNode1';\n"
					+ "      }, new mm#//ChildNode {\n"
					+ "        friend = CompositeNode1;\n"
					+ "        name = 'ChildWithRef';\n"
					+ "      }, new mm#//ChildNode {\n"
					+ "        name = 'ChildWithoutRef';\n" + "      } ];\n"
					+ "      name = 'RootNode';\n" + "    } | null;\n" + "  }";
		}
	},
	UNSET_OBJECT_COMPOSITE {
		public void apply(ResourceSet rs) {
			inst1(rs).eGetObj("tree").eRemove("children", "name",
					"CompositeNode1");
			inst1(rs).eGetObj("tree").eGetObj("children", "name",
					"ChildWithRef").eUnset("friend");
			inst1(rs).eRemove("reflist", "name", "Child11");
		}

		protected String patch() {
			return "object res0#/ {\n"
					+ "    reflist = [ 1:Child11 | ];\n"
					+ "  }\n"
					+ "\n"
					+ "  object res0#//@tree {\n"
					+ "    children = [ 0:new mm#//CompositeNode CompositeNode1 {\n"
					+ "      children = [ new mm#//ChildNode Child11 {\n"
					+ "        name = 'Child11';\n"
					+ "      } ];\n"
					+ "      name = 'CompositeNode1';\n"
					+ "    } | ];\n"
					+ "  }\n"
					+ "\n"
					+ "  object left res0#//@tree/@children.1 right res0#//@tree/@children.0 {\n"
					+ "    friend = CompositeNode1 | null;\n" + "  }";
		}
	},
	UNSET_OBJECT_SIMPLE {
		public void apply(ResourceSet rs) {
			inst1(rs).eGetObj("tree").eRemove("children", "name",
					"ChildWithoutRef");
		}

		protected String patch() {
			return "object res0#//@tree {\n"
					+ "children = [ 2:new mm#//ChildNode {\n"
					+ "name = 'ChildWithoutRef'; } | ];\n" + "}";
		}
	},
	UNSET_OBJECT_WITH_REF {
		public void apply(ResourceSet rs) {
			inst1(rs).eGetObj("tree").eRemove("children", "name",
					"ChildWithRef");
		}

		protected String patch() {
			return "object res0#//@tree {\n"
					+ "children = [ 1:new mm#//ChildNode {\n"
					+ "friend = CompositeNode1; name = 'ChildWithRef'; } | ];\n }\n"
					+ "object CompositeNode1 res0#//@tree/@children.0 { }";
		}
	};

	private static final String inst1 = "SimpleMM1Instance1.xmi";

	public String asPatch() {
		String p = patch();
		String m = p.contains("mm#") ? "import mm ns 'http://www.itemis.de/emf/epatch/testmm1'\n"
				: "";
		return "epatch " + name() + " {\n" + m + "resource res0 {\n"
				+ "left uri 'SimpleMM1Instance1.xmi';\n"
				+ "right uri 'SimpleMM1Instance11.xmi';\n" + "}\n" + p + " }";
	}

	public String getL2RDocu() {
		return null;
	}

	public String getR2LDocu() {
		return null;
	}

	public ResourceSet getResourceSet() {
		return DynEObject.eCreateResourceSet(getClass(), inst1);
	};

	protected DynEObject inst1(ResourceSet rs) {
		return DynEObject.eGetResource(rs, inst1);
	}

	protected abstract String patch();

}
