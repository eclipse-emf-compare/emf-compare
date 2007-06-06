package org.eclipse.emf.compare.tests.unit;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.compare.diff.DiffModel;
import org.eclipse.emf.compare.diff.ModelInputSnapshot;
import org.eclipse.emf.compare.diff.generic.DiffMaker;
import org.eclipse.emf.compare.match.MatchModel;
import org.eclipse.emf.compare.match.service.MatchService;
import org.eclipse.emf.compare.tests.util.EMFCompareTestCase;
import org.eclipse.emf.compare.tests.util.FileUtils;
import org.eclipse.emf.compare.util.FactoryException;
import org.eclipse.emf.compare.util.ModelUtils;
import org.eclipse.emf.ecore.EObject;

/**
 * Test the results models are still the same as expected.
 * 
 * @author Cedric Brun <a href="mailto:cedric.brun@obeo.fr">cedric.brun@obeo.fr</a>
 */
public class TestNonRegressionModels extends EMFCompareTestCase {
	private String inputDirectory;

	protected void setUp() throws Exception {
		setInputDirectory("/inputs");
	}

	/**
	 * Compare the expected models (in the expected directories) with the result
	 * of the comparison services.
	 * 
	 * @throws FactoryException
	 *             Thrown if a needed feature doesn't exist in its expected
	 *             class.
	 * @throws IOException
	 *             Thrown if an I/O operation has failed or been interrupted.
	 * @throws InterruptedException
	 *             If one of the threads is interrupted.
	 */
	public void testNonRegressionModels() throws FactoryException, IOException,
			InterruptedException {
		final File inputDir = new File(FileLocator.toFileURL(
				getPlugin().getBundle().getEntry(getInputDirectory()))
				.getFile());

		final File[] directories = FileUtils.listDirectories(inputDir);

		final Date start = Calendar.getInstance().getTime();
		if (directories != null) {
			for (int i = 0; i < directories.length; i++) {
				compareSnapshots(directories[i]);
			}
		}
		final Date end = Calendar.getInstance().getTime();
		System.out.println("non-regression models evaluated in "
				+ (end.getTime() - start.getTime()) / 1000 + "s");
	}

	/**
	 * Compares the snapshot of a given folder with its expected folder
	 * snapshot.
	 * 
	 * @param directory
	 *            Input directory containing the models.
	 * @throws IOException
	 *             Thrown if an I/O operation has failed or been interrupted.
	 * @throws InterruptedException
	 *             If one of the threads is interrupted.
	 */
	private void compareSnapshots(File directory) throws IOException,
			InterruptedException {
		if (FileUtils.listDirectories(directory).length != 0) {
			for (int i = 0; i < FileUtils.listDirectories(directory).length; i++) {
				compareSnapshots(FileUtils.listDirectories(directory)[i]);
			}
		} else {
			final File expectedDir = new File(directory.getPath().replace(
					File.separator + "inputs",
					File.separator + "expected" + File.separator + "inputs"));

			final String testedDir = directory.getName().toUpperCase();
			System.out.println(testedDir + "\n===============");
			final List<EObject> inputModels = ModelUtils
					.getModelsFrom(directory);
			final List<EObject> expectedSnapshot = ModelUtils
					.getModelsFrom(expectedDir);

			if (inputModels.size() == 2 && expectedSnapshot.size() == 1) {
				final MatchModel inputMatch = new MatchService().doMatch(
						inputModels.get(0), inputModels.get(1),
						new NullProgressMonitor());
				final DiffModel inputDiff = new DiffMaker().doDiff(inputMatch);

				// Serializes current and expected match and diff as Strings
				final String currentMatch = ModelUtils.serialize(inputMatch);
				final String currentDiff = ModelUtils.serialize(inputDiff);
				final String expectedMatch = ModelUtils
						.serialize(((ModelInputSnapshot) expectedSnapshot
								.get(0)).getMatch());
				final String expectedDiff = ModelUtils
						.serialize(((ModelInputSnapshot) expectedSnapshot
								.get(0)).getDiff());

				assertEquals(testedDir + ", match doesn't match",
						suppressPathReferences(currentMatch),
						suppressPathReferences(expectedMatch));
				assertEquals(testedDir + ", diff doesn't match",
						suppressPathReferences(currentDiff),
						suppressPathReferences(expectedDiff));
			}
		}
	}

	/**
	 * Searches a {@link java.lang.String String} representing the contents of a
	 * model for all references to "a href='path#element'" and supresses the
	 * path.
	 * 
	 * @param aFile
	 *            The file to parse.
	 */
	private String suppressPathReferences(String aFile) {
		final String[] fragments = aFile.split("\n");
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < fragments.length; i++) {
			buffer.append(fragments[i].replaceAll("href=\".*#", "href=\"#"));
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @return the input directory
	 */
	public String getInputDirectory() {
		return inputDirectory;
	}

	/**
	 * Set the directory to use for model inputs.
	 * 
	 * @param inputDir the input directory
	 */
	public void setInputDirectory(String inputDir) {
		inputDirectory = inputDir;
	}
}
