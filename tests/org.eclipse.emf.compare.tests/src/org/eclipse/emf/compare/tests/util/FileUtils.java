package org.eclipse.emf.compare.tests.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.manipulation.ConvertLineDelimitersOperation;
import org.eclipse.core.filebuffers.manipulation.FileBufferOperationRunner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * File utilities for Unit tests getting expected results from the JUnit
 * project.
 * 
 */
public class FileUtils {

	/**
	 * 
	 * 
	 */
	private FileUtils() {
		// prevent instantiation.
	}

	/**
	 * Resolve a URL to a location string (file system absolute path).
	 * 
	 * @param urlString
	 *            a URL string, possibly using the platform:/ format.
	 * @return an absolute file system path corresponding to
	 *         <code>urlString</code>
	 * @throws IOException
	 *             if an IO error occurs
	 * @throws MalformedURLException
	 *             if urlString is an invalid URL
	 */
	public static String resolveURLToLocationString(final String urlString)
			throws IOException, MalformedURLException {
		final String fileName = Platform.resolve(new URL(urlString)).getFile();
		return fileName;
	}

	/**
	 * Return the file contents as a string. This method also converts the
	 * file's line separator to the current system's line separator if
	 * <code>convertNewLines</code> is <code>true</code>.
	 * 
	 * @param urlString
	 *            a URL string, possibly using the platform:/ format.
	 * @param convertNewLines
	 *            Convert the file line separators to the system line separator.
	 * @return the file contents
	 * @throws IOException
	 *             if an IO error occurs
	 * @throws MalformedURLException
	 *             if urlString is an invalid URL
	 * @throws CoreException
	 *             if an error occurs reading the file.
	 */
	public static String getFileContents(final String urlString,
			final boolean convertNewLines) throws MalformedURLException, IOException,
			CoreException {

		final ITextFileBufferManager textBufferMgr = FileBuffers
				.getTextFileBufferManager();
		final String fileName = resolveURLToLocationString(urlString);

		final IPath path = new Path(fileName);
		textBufferMgr.connect(path, new NullProgressMonitor());
		final ITextFileBuffer textFileBuffer = textBufferMgr
				.getTextFileBuffer(path);

		if (convertNewLines) {
			new FileBufferOperationRunner(textBufferMgr, null).execute(
					new IPath[] { path }, new ConvertLineDelimitersOperation(
							System.getProperty("line.separator")),
					new NullProgressMonitor());
		}

		final String result = textFileBuffer.getDocument().get();
		textBufferMgr.disconnect(path, new NullProgressMonitor());
		return result;
	}

}
