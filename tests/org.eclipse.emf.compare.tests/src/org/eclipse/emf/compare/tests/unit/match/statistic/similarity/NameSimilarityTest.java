/*******************************************************************************
 * Copyright (c) 2006, 2007, 2008 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.tests.unit.match.statistic.similarity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.match.statistic.MetamodelFilter;
import org.eclipse.emf.compare.match.statistic.similarity.NameSimilarity;
import org.eclipse.emf.compare.tests.EMFCompareTestPlugin;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Tests the methods used to compute name similarity.
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
@SuppressWarnings("nls")
public class NameSimilarityTest extends TestCase {
	/** Full path to the model containing this test's input. */
	private static final String INPUT_MODEL_PATH = "/data/testInput.ecore";

	/** String displayed for a <code>null</code> result where it shouldn't be. */
	private static final String MESSAGE_NULL_RESULT = "returned a null result.";

	/** Filter that will be used to detect the relevant features of an {@link EObject}. */
	private MetamodelFilter filter;

	/** Model that contains the test's input. */
	private EObject inputModel;

	/** Name of the currently tested method for the error messages. */
	private String testedMethod;

	/**
	 * Tests {@link NameSimilarity#contentValue(EObject, MetamodelFilter)}.
	 * <p>
	 * Expected (assumed from the model data/testInput) :
	 * <ul>
	 * <li>Class1 =&gt; "false "</li>
	 * <li>Class1 and Class1Clone =&gt; equal result</li>
	 * <li>Class1 and Class1.clone() =&gt; equal result</li>
	 * <li>Class1 and Class1Altered =&gt; distinct result</li>
	 * </ul>
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the similarity couldn't be computed. Considered a failed test.
	 */
	public void testFilteredContentValue() throws FactoryException {
		testedMethod = "NameSimilarity#contentValue(EObject, MetaModelFilter)";

		// inputModel.eContents().get(0) returns reference to Class1, which solely has an attribute.
		// expected value is the result of filter.getFilteredFeatures() toString concatenation
		final EObject class1 = inputModel.eContents().get(0);
		final EObject class1ClonedFromModel = inputModel.eContents().get(1);
		final EObject class1ClonedFromCode = EcoreUtil.copy(class1);
		final EObject class1Altered = inputModel.eContents().get(2);

		final String class1Content = NameSimilarity.contentValue(class1, filter);
		final String class1ClonedFromModelContent = NameSimilarity
				.contentValue(class1ClonedFromModel, filter);
		final String class1ClonedFromCodeContent = NameSimilarity.contentValue(class1ClonedFromCode, filter);
		final String class1AlteredContent = NameSimilarity.contentValue(class1Altered, filter);

		assertNotNull(testedMethod + ' ' + MESSAGE_NULL_RESULT, class1Content);

		// the expected "false " comes from the "interface" attribute of EClass
		assertEquals(testedMethod + ' ' + "didn't return the expected result.", new String("false "),
				class1Content);
		assertEquals(testedMethod + ' ' + "returned a distinct result for two identical objects.",
				class1Content, class1ClonedFromModelContent);
		assertEquals(testedMethod + ' ' + "returned a distinct result for two clones.", class1Content,
				class1ClonedFromCodeContent);

		// Class1 and Class1Altered are distinct in that Class1Altered has its "interface" flag set as "true"
		assertFalse(testedMethod + ' ' + "returned an equal result for two different objects.", class1Content
				.equals(class1AlteredContent));
	}

	/**
	 * Tests {@link NameSimilarity#findName(EObject)}.
	 * <p>
	 * Expected results (assumed from the model data/testInput) :
	 * <ul>
	 * <li>Class1 (EClass) =&gt; same result as {@link EClass#getName()}</li>
	 * <li>EString (EDataType) =&gt; same result as {@link EDataType#getName()}</li>
	 * <li>Visibility (EEnum) =&gt; same result as {@link EEnum#getName()}</li>
	 * <li>Package1 (EPackage) =&gt; same result as {@link EPackage#getName()}</li>
	 * <li>Object with no "name" attribute =&gt; name of the object's class.</li>
	 * <li>Unnamed object =&gt; name of the object's class.</li>
	 * </ul>
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if an object's name couldn't be retrieved. Considered a failed test.
	 */
	public void testFindName() throws FactoryException {
		testedMethod = "NameSimilarity#findName(EObject)";

		// Will test findName() on class, datatype, enum and package.
		final EObject clazz = inputModel.eContents().get(0);
		final EObject datatype = inputModel.eContents().get(3);
		final EObject enumeration = inputModel.eContents().get(4);
		final EObject packaje = inputModel.eContents().get(6);
		final EObject noNameObject = EcoreFactory.eINSTANCE.createEObject();
		final EObject unnamedClass = inputModel.eContents().get(5);

		final String className = NameSimilarity.findName(clazz);
		final String datatypeName = NameSimilarity.findName(datatype);
		final String enumName = NameSimilarity.findName(enumeration);
		final String packageName = NameSimilarity.findName(packaje);
		final String noName = NameSimilarity.findName(noNameObject);
		final String unnamedClassName = NameSimilarity.findName(unnamedClass);

		assertNotNull(testedMethod + ' ' + MESSAGE_NULL_RESULT, className);

		assertEquals(testedMethod + ' ' + "didn't return the accurate result for a class.", ((EClass)clazz)
				.getName(), className);
		assertEquals(testedMethod + ' ' + "didn't return the accurate result for a data type.",
				((EDataType)datatype).getName(), datatypeName);
		assertEquals(testedMethod + ' ' + "didn't return the accurate result for an enumeration.",
				((EEnum)enumeration).getName(), enumName);
		assertEquals(testedMethod + ' ' + "didn't return the accurate result for a package.",
				((EPackage)packaje).getName(), packageName);
		assertEquals(testedMethod + ' ' + "returned an unexpected result for a object without name.",
				noNameObject.eClass().getName(), noName);
		assertEquals(testedMethod + ' ' + "returned an unexpected result for an unnamed object.",
				unnamedClass.eClass().getName(), unnamedClassName);
	}

	/**
	 * Tests {@link NameSimilarity#nameSimilarityMetric(String, String)}.
	 * <p>
	 * Expected results : <table>
	 * <tr>
	 * <td>arg1</td>
	 * <td>arg2</td>
	 * <td>result</td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td><code>null</code></td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>null</code></td>
	 * <td>&quot;string&quot;</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;string&quot;</td>
	 * <td><code>null</code></td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;ceString&quot;</td>
	 * <td>&quot;ceString&quot;</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;classe&quot;</td>
	 * <td>&quot;Classe&quot;</td>
	 * <td><code>0.999999</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;Classe&quot;</td>
	 * <td>&quot;UneClasse&quot;</td>
	 * <td><code>10/13</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;package&quot;</td>
	 * <td>&quot;packagedeux&quot;</td>
	 * <td><code>12/16</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;&quot;</td>
	 * <td>&quot;MaClasse&quot;</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;package&quot;</td>
	 * <td>&quot;packageASupprimer&quot;</td>
	 * <td><code>12/22</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;attribut&quot;</td>
	 * <td>&quot;reference&quot;</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td>&quot;aa&quot;</td>
	 * <td>&quot;aaaa&quot;</td>
	 * <td><code>2/4</code></td>
	 * </tr>
	 * </table>
	 * </p>
	 */
	public void testNameSimilarityMetric() {
		final String[] data = new String[] {null, null, null, "string", "string", null, "ceString",
				"ceString", "classe", "Classe", "Classe", "UneClasse", "package", "packagedeux", "",
				"MaClasse", "package", "packageASupprimer", "attribut", "reference", "aa", "aaaa", };
		final double[] similarities = new double[] {0d, 0d, 0d, 1d, 0.999999d, 10d / 13d, 3d / 4d, 0d,
				6d / 11d, 0d, 1d / 2d, };
		for (int i = 0; i < data.length; i += 2) {
			assertEquals("Unexpected result of nameSimilarityMetric for str1 = " + data[i] + " and str2 = "
					+ data[i + 1], similarities[i / 2], NameSimilarity.nameSimilarityMetric(data[i],
					data[i + 1]));
		}
	}

	/**
	 * Tests {@link NameSimilarity#pairs(String)}.
	 * <p>
	 * <ul>
	 * Assertions :
	 * <li>Result isn't <code>null</code>.</li>
	 * <li>Result is instance of {@link ArrayList}.</li>
	 * <li>Result's size is equal to source's length - 1 if length &gt; 1, <code>0</code> otherwise.</li>
	 * <li>All {@link String}s contained within the result are 2-character long.</li>
	 * </ul>
	 * <ul>
	 * Expected results :
	 * <li><code>null</code> =&gt; empty {@link ArrayList}</li>
	 * <li>&quot;anEvenSizeString&quot; =&gt; [an, nE, Ev, ve, en, nS, Si, iz, ze, eS, St, tr, ri, in, ng]</li>
	 * <li>&quot;anOddSizeString&quot; =&gt; [an, nO, Od, dd, dS, Si, iz, ze, eS, St, tr, ri, in, ng]</li>
	 * <li>&quot;&quot; =&gt; empty {@link ArrayList}</li>
	 * <li>&quot;!&quot; =&gt; empty {@link ArrayList}</li>
	 * <li>&quot;-&amp;;+&quot; =&gt; [-&amp;, &amp;;, ;+]</li>
	 * </ul>
	 * </p>
	 */
	public void testPairs() {
		final String[] data = new String[] {null, "anEvenSizeString", "anOddSizeString", "", "!", "-&;+",
				Calendar.getInstance().getTime().toString(), };
		for (int i = 0; i < data.length; i++) {
			final List<String> result = NameSimilarity.pairs(data[i]);
			assertNotNull("Method pairs() returned null result.", result);
			assertTrue("Method pairs()'s result isn't an instance of ArrayList.", result instanceof ArrayList);

			// Special case for null and 1-char long input
			if (data[i] == null || data[i].length() < 2) {
				assertEquals("Unexpected size of pair()'s result.", 0, result.size());
			} else {
				assertEquals("Unexpected size of pair()'s result.", data[i].length() - 1, result.size());

				for (int j = 0; j < result.size(); j++) {
					assertEquals("Unexpected pair size for " + '"' + result.get(j) + '"', 2, result.get(j)
							.length());
					assertEquals("Unexpected pair for source " + '"' + data[i] + '"', data[i].substring(j,
							j + 2), result.get(j));
				}
			}
		}
	}

	/**
	 * Tests {@link NameSimilarity#contentValue(EObject, MetamodelFilter)}.
	 * <p>
	 * Expected results (assumed from the model data/testInput) :
	 * <ul>
	 * <li>Class1 =&gt; concatenation of all attributes</li>
	 * <li>Class1 and Class1Clone =&gt; equal result</li>
	 * <li>Class1 and Class1.clone() =&gt; equal result</li>
	 * <li>Class1 and Class1Altered =&gt; distinct result</li>
	 * </ul>
	 * </p>
	 * 
	 * @throws FactoryException
	 *             Thrown if the similarity couldn't be computed. Considered a failed test.
	 */
	public void testUnFilteredContentValue() throws FactoryException {
		testedMethod = "NameSimilarity#contentValue(EObject, MetaModelFilter)";

		final EObject class1 = inputModel.eContents().get(0);
		final EObject class1ClonedFromModel = inputModel.eContents().get(1);
		final EObject class1ClonedFromCode = EcoreUtil.copy(class1);
		final EObject class1Altered = inputModel.eContents().get(2);

		// Computes the expected result for class1
		final StringBuilder buffer = new StringBuilder();
		final List<EAttribute> classAttrib = new ArrayList<EAttribute>(class1.eClass().getEAllAttributes());
		classAttrib.remove(NameSimilarity.findNameFeature(class1));
		for (EAttribute attribute : classAttrib) {
			if (attribute != null && EFactory.eGet(class1, attribute.getName()) != null) {
				buffer.append(EFactory.eGetAsString(class1, attribute.getName())).append(" ");
			}
		}

		final String expectedClass1Content = buffer.toString();
		final String class1Content = NameSimilarity.contentValue(class1, null);
		final String class1ClonedFromModelContent = NameSimilarity.contentValue(class1ClonedFromModel, null);
		final String class1ClonedFromCodeContent = NameSimilarity.contentValue(class1ClonedFromCode, null);
		final String class1AlteredContent = NameSimilarity.contentValue(class1Altered, null);

		assertNotNull(testedMethod + ' ' + MESSAGE_NULL_RESULT, class1Content);

		assertEquals(testedMethod + ' ' + "didn't return the expected result.", expectedClass1Content,
				class1Content);
		assertEquals(testedMethod + ' ' + "returned a distinct result for two identical objects.",
				class1Content, class1ClonedFromModelContent);
		assertEquals(testedMethod + ' ' + "returned a distinct result for two clones.", class1Content,
				class1ClonedFromCodeContent);

		// Class1 and Class1Altered are distinct in that Class1Altered has its "interface" flag set as "true"
		assertFalse(testedMethod + ' ' + "returned an equal result for two different objects.", class1Content
				.equals(class1AlteredContent));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		final File modelFile = new File(FileLocator.toFileURL(
				EMFCompareTestPlugin.getDefault().getBundle().getEntry(INPUT_MODEL_PATH)).getFile());
		inputModel = ModelUtils.load(modelFile, new ResourceSetImpl());
		filter = new MetamodelFilter();
		filter.analyseModel(inputModel);
	}
}
