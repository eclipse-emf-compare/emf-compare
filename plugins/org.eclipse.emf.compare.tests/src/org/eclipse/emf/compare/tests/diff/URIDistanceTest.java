/**
 * Copyright (c) 2012, 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.emf.compare.tests.diff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;

import org.eclipse.emf.compare.match.eobject.URIDistance;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.junit.Test;

@SuppressWarnings("nls")
public class URIDistanceTest {

	private URIDistance meter = new URIDistance();

	@Test
	public void moreOrLessTheSame() throws Exception {
		String origin = "/root/a/b";
		String closest = "/root/a/c";
		String farthest = "/root/d/c";
		assertTrue(proximity(origin, closest) < proximity(origin, farthest));
	}

	@Test
	public void moreOrLessTheSame2() throws Exception {
		String origin = "/root/a/b";
		String closest = "/root/a/b/c/d";
		String farthest = "/root/d/c/d";
		assertTrue(proximity(origin, closest) < proximity(origin, farthest));
	}

	@Test
	public void moreOrLessTheSame3() throws Exception {
		String origin = "/root/a/b";
		String closest = "/root/";
		String farthest = "/";
		assertTrue(proximity(origin, closest) < proximity(origin, farthest));
	}

	@Test
	public void moreOrLessTheSame4() throws Exception {
		String origin = "/root/a/b";
		String closest = "/root/";
		String farthest = "/otherRoot/";
		assertTrue(proximity(origin, closest) < proximity(origin, farthest));
	}

	@Test
	public void sameNumberOfFragments() throws Exception {
		assertEquals(1, proximity("/root/a/b", "/root/a/b/c"));
		assertEquals(1, proximity("/root/a/b/", "/root/a/b/c/"));
		assertEquals(2, proximity("/root/a/b/", "/root/a/b/c/d/"));
		assertEquals(0, proximity("/root/a/b", "/root/a/b"));
		assertEquals(7, proximity("/root/a/a2/a3", "/root/b/b2/a3"));
		assertEquals(7, proximity("/root/a/a2/a3", "/root/b/a2/b3"));
	}

	@Test
	public void identics() throws Exception {
		assertEquals(0, proximity("/root/a/b/", "/root/a/b/"));
		assertEquals(0, proximity("/root/", "/root/"));
		assertEquals(0, proximity("", ""));
	}

	@Test
	public void limitCases() throws Exception {
		assertEquals(0, proximity("", ""));
		assertEquals(10,
				proximity("/", "/a/very/long/path/just/to/check/we/wont/ends/up/with/a/weird/thing"));
		assertEquals(10,
				proximity("/a/very/long/path/just/to/check/we/wont/ends/up/with/a/weird/thing", "/"));
	}

	@Test
	public void completelyDifferent() throws Exception {
		assertEquals(10, proximity("/c/d/e/", "/root/a/b/"));
		assertEquals(10, proximity("/c/", "/root/a/b/"));
		assertEquals(10, proximity("/c/d/e", "/root/"));
		assertEquals(10, proximity("/c/d/e/f", "/a/b/e/f"));
		assertEquals(10, proximity("/a", "/b"));
	}

	@Test
	public void orderMatters() throws Exception {
		assertEquals(10, proximity("/c/d/e/f", "/f/d/c/e"));
	}

	@Test
	public void idLikeURIs() throws Exception {
		assertEquals(10, proximity("#131233", "#azeazezae"));
		assertEquals(2, proximity("/c/d/e/f", "/c/d/e/f?#azeaze"));
	}

	@Test
	public void traillingSlashes() throws Exception {
		assertEquals(1, proximity("/root/a/b/", "/root/a/b/c/"));
		assertEquals(1, proximity("root/a/b/", "/root/a/b/c/"));
		assertEquals(1, proximity("/root/a/b/", "/root/a/b/c"));
		assertEquals(10, proximity("///root/a/b/", "/root/a/b/c"));
	}

	@Test
	public void nullDistanceForSameModel() throws Exception {
		Iterator<? extends EObject> it = EcorePackage.eINSTANCE.eAllContents();
		Iterator<? extends EObject> it2 = EcorePackage.eINSTANCE.eAllContents();
		while (it.hasNext() && it2.hasNext()) {
			EObject a = it.next();
			EObject b = it2.next();
			// System.out.println(meter.apply(a));
			assertEquals(0, meter.proximity(a, b));
		}
	}

	/**
	 * Return a metric result URI similarities. It compares 2 strings splitting those by "/" and return an int
	 * representing the level of similarity. 0 - they are exactly the same to 10 - they are completely
	 * different. "adding a fragment", "removing a fragment".
	 * 
	 * @param aPath
	 *            First of the two {@link String}s to compare.
	 * @param bPath
	 *            Second of the two {@link String}s to compare.
	 * @return The number of changes to transform one uri to another one.
	 */
	public int proximity(String aPath, String bPath) {
		if (aPath.equals(bPath)) {
			return 0;
		} else {
			CharMatcher slash = CharMatcher.is('/');
			Splitter splitter = Splitter.on('/');
			String actualAPath = aPath;
			String actualBPath = bPath;
			if (slash.indexIn(actualAPath) == 0) {
				actualAPath = aPath.substring(1);
			}
			if (slash.indexIn(actualBPath) == 0) {
				actualBPath = bPath.substring(1);
			}
			Iterable<String> aString = splitter.split(slash.trimTrailingFrom(actualAPath));
			Iterable<String> bString = splitter.split(slash.trimTrailingFrom(actualBPath));
			return meter.proximity(ImmutableList.copyOf(aString), ImmutableList.copyOf(bString));
		}
	}
}
