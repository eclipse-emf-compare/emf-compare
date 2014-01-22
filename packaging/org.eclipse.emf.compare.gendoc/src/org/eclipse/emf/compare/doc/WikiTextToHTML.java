/*******************************************************************************
 * Copyright (c) 2013 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.emf.compare.doc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.DescribeCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.DefaultSplittingStrategy;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.NoSplittingStrategy;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplitOutlineItem;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplittingHtmlDocumentBuilder;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplittingOutlineParser;
import org.eclipse.mylyn.internal.wikitext.core.parser.builder.SplittingStrategy;
import org.eclipse.mylyn.internal.wikitext.core.validation.StandaloneMarkupValidator;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.mylyn.wikitext.core.parser.outline.OutlineItem;
import org.eclipse.mylyn.wikitext.core.parser.util.MarkupToEclipseToc;
import org.eclipse.mylyn.wikitext.core.util.XmlStreamWriter;
import org.eclipse.mylyn.wikitext.core.validation.ValidationProblem;
import org.eclipse.mylyn.wikitext.core.validation.ValidationProblem.Severity;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 * 
 */
public class WikiTextToHTML {

	private static final String TOC = "__TOC__\n\n";

	private static final FileSystem DEFAULT_FS = FileSystems.getDefault();

	private static final boolean FORMAT_OUTPUT = true;

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	private static final String MEDIA_WIKI = "MediaWiki";
	
	private MarkupLanguage markupLanguage;

	private boolean navigationImages = false;

	private String title;

	private boolean emitDoctype = false;

	private boolean multipleOutputFiles;

	private String copyrightNotice;

	private boolean xhtmlStrict;

	private String prependImagePrefix;

	private String defaultAbsoluteLinkTarget;

	private String linkRel;

	private boolean suppressBuiltInCssStyles;

	private boolean useInlineCssStyles;

	private String htmlDoctype;

	private List<Stylesheet> stylesheets;
	private List<Stylesheet> helpStylesheets = new ArrayList<>();
	private List<Stylesheet> websiteStylesheets = new ArrayList<>();
	
	private PrimaryTOCWriter primaryTOCWriter = new PrimaryTOCWriter();

	private Path sourceFolder;

	private List<Path> foldersToCopy;

	private Path targetRootFolder;

	private Path targetWebsiteFolder;

	private Path targetHelpFolder;

	private boolean genEclipseHelp;

	private boolean genWebsite;
	
	private static java.util.Date NOW = Calendar.getInstance().getTime();
	
	public static void main(String[] args) throws Exception {
		WikiTextToHTML wikiTextToHTML = new WikiTextToHTML();
		wikiTextToHTML.run(args);
	}
	
	public void run(String[] args) throws Exception {
		processCommandLineArgs(args);
		
		if (targetRootFolder == null) {
			System.err.println("Error: unable to find -location argument");
			usage();
			System.exit(1);
		}
		if (!genEclipseHelp && !genWebsite) {
			System.err.println("Error: you must at least provide a -eclipsehelp or a -website option");
			usage();
			System.exit(1);
		}
		
		markupLanguage = new CustomMediaWikiLanguage();
		markupLanguage.setInternalLinkPattern("{0}");

		Stylesheet ss1 = new Stylesheet();
		ss1.setUrl("/help/topic/org.eclipse.emf.compare.doc/help/resources/bootstrap.css");
		helpStylesheets.add(ss1);
		Stylesheet ss2 = new Stylesheet();
		ss2.setUrl("/help/topic/org.eclipse.emf.compare.doc/help/resources/custom.css");
		helpStylesheets.add(ss2);
		
		ss1 = new Stylesheet();
		ss1.setUrl("resources/bootstrap.css");
		websiteStylesheets.add(ss1);
		ss2 = new Stylesheet();
		ss2.setUrl("resources/custom.css");
		websiteStylesheets.add(ss2);
		
		sourceFolder = DEFAULT_FS.getPath("src");
		foldersToCopy = new ArrayList<>();
		foldersToCopy.add(targetRootFolder.resolve(sourceFolder).resolve("images"));
		foldersToCopy.add(targetRootFolder.resolve(sourceFolder).resolve("resources"));
		
		targetWebsiteFolder = DEFAULT_FS.getPath("target", "website").resolve(gitDescribe());
		targetHelpFolder = DEFAULT_FS.getPath("help");
		final Path resolvedTargetHelpFolder = targetRootFolder.resolve(targetHelpFolder);

		if (genEclipseHelp) {
			if (Files.exists(resolvedTargetHelpFolder)) {
				System.out.println("Deleting "+ resolvedTargetHelpFolder + " before regenerating Eclipse help");
				removeRecursiveContent(resolvedTargetHelpFolder);
			}
			primaryTOCWriter.startPrimaryTOC(targetHelpFolder.resolve("index.html"), "EMF Compare Documentation");
		}
		
		final PathMatcher mediawikiPattern = DEFAULT_FS.getPathMatcher("glob:**/*.mediawiki");
		
		Files.walkFileTree(targetRootFolder.resolve(sourceFolder), new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult visitFile(Path markupPath, BasicFileAttributes attrs) throws IOException {
	            if (mediawikiPattern.matches(markupPath)) {
	            	processFile(sourceFolder, targetWebsiteFolder, targetHelpFolder, markupPath);
	            }
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
	        	System.err.println("Failed to visit " + file);
	        	exc.printStackTrace();
	            return FileVisitResult.CONTINUE;
	        }
	        
	        /** 
	         * {@inheritDoc}
	         * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
	         */
	        @Override
	        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
	        	if (genEclipseHelp && !dir.equals(targetRootFolder.resolve(sourceFolder)) && !foldersToCopy.contains(dir)) {
					if (dir.resolve("index.mediawiki").toFile().exists()) {
	        			primaryTOCWriter.startTopic(getTitle(dir), dir.resolve("index.html"));
	        		} else {
	        			primaryTOCWriter.startTopic(getTitle(dir), null);
	        		}
	        	}
	        	return FileVisitResult.CONTINUE;
	        }
	        
	        /** 
	         * {@inheritDoc}
	         * @see java.nio.file.SimpleFileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
	         */
	        @Override
	        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
	        	if (genEclipseHelp && !dir.equals(targetRootFolder.resolve(sourceFolder)) && !foldersToCopy.contains(dir)) {
	        		primaryTOCWriter.endTopic();
	        	}
	        	return FileVisitResult.CONTINUE;
	        }
	    });
		
		if (genEclipseHelp) {
			primaryTOCWriter.endPrimaryTOC();
			writeStringToFile(primaryTOCWriter.getPrimaryTOCContent(), resolvedTargetHelpFolder.resolve("toc.xml"));
			writeStringToFile(primaryTOCWriter.getPluginContent(), targetRootFolder.resolve("plugin.xml"));
		}
		
		for (Path folder : foldersToCopy) {
			if (genWebsite)
				copy(folder, targetRootFolder.resolve(targetWebsiteFolder).resolve(folder.getFileName()), "glob:**/*");
			if (genEclipseHelp)
				copy(folder, resolvedTargetHelpFolder.resolve(folder.getFileName()), "glob:**/*");
		}
	}

	/**
	 * 
	 */
	private void usage() {
		System.out.println("Usage: wikiTextToHTML -location path [-eclipsehelp] [-website]");
	}

	private void processCommandLineArgs(String[] args) throws Exception {
		if (args == null)
			throw new Exception("No argument provided");
		for (int i = 0; i < args.length; i++) {
			String option = args[i];
			String arg = "";
			if (i == args.length - 1 || args[i + 1].startsWith("-")) {//$NON-NLS-1$
				// do nothgin
			} else {
				arg = args[++i];
			}

			if (option.equalsIgnoreCase("-location")) { //$NON-NLS-1$
				targetRootFolder = DEFAULT_FS.getPath(arg);
			}

			if (option.equalsIgnoreCase("-eclipsehelp")) { //$NON-NLS-1$
				genEclipseHelp = true;
			}

			if (option.equalsIgnoreCase("-website")) { //$NON-NLS-1$
				genWebsite = true;
			}
		}
	}
	
	private String getTitle(Path path) {
		String filename = path.getFileName().toString();
		int lastIndexOf = filename.lastIndexOf('.');
		if (lastIndexOf >= 0) {
			filename = filename.substring(0, lastIndexOf);
		}
		String[] split = filename.split("-");
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
			String str = split[i].trim();
			if (str.length() > 0) {
				if (i == 0) {
					char firstChar = str.charAt(0);
					sb.append(Character.toUpperCase(firstChar));
					sb.append(str.substring(1));
				} else {
					sb.append(str);
				}
				sb.append(' ');
			}
		}
		return sb.toString().trim();
	}
	
	private void processFile(final Path sourceFolder,
			final Path targetWebsiteFolder,
			final Path targetHelpFolder, Path markupPath)
			throws IOException, FileNotFoundException,
			UnsupportedEncodingException {
		System.out.println("Processing " + markupPath);
		
		Path relativeMarkupPath = targetRootFolder.resolve(sourceFolder).relativize(markupPath);
		
		Path targetHTML = targetWebsiteFolder.resolve(changeFilename(relativeMarkupPath, ".html"));
		
		Path relativeTOCPath = changeFilename(relativeMarkupPath, "toc-", ".xml");
		Path targetTOC = targetHelpFolder.resolve(relativeTOCPath);
		Path targetHelp = targetHelpFolder.resolve(changeFilename(relativeMarkupPath, ".html"));

		if (genWebsite) {
			mkdirs(targetRootFolder.resolve(targetHTML));
		} 
		if (genEclipseHelp) {
			mkdirs(targetRootFolder.resolve(targetTOC));
			mkdirs(targetRootFolder.resolve(targetHelp));
		}
		
		String markupContent = new String(Files.readAllBytes(markupPath), UTF_8);
				
		String markupContentWithTOC = markupContent.replaceFirst("=(.*)=", "=EMF Compare — $1=\n\nVersion " + gitDescribe() +"\n\n__TOC__\n\n") + 
				"\n\nPart of [index.html EMF Compare Documentation]" +
				"\n\nVersion " + gitDescribe() +
				"\n\nLast updated " + NOW;
				

		if (performValidation(markupPath, markupContent)) {
			// for website
			if (genWebsite) {
				stylesheets = websiteStylesheets;
				genHTML(getTitle(targetHTML), markupContentWithTOC, targetRootFolder.resolve(targetHTML));
			}
			
			// for eclipse help
			if (genEclipseHelp) {
				stylesheets = helpStylesheets;
				genHTML(getTitle(targetHTML), markupContent, targetRootFolder.resolve(targetHelp));
			
			
				final PathMatcher indexPattern = DEFAULT_FS.getPathMatcher("glob:**/index.mediawiki");
				if (!indexPattern.matches(markupPath)) {
					genTOC(getTitle(targetHelp), markupContent, targetRootFolder.resolve(targetTOC), targetHelp);
					primaryTOCWriter.startTopic(getTitle(targetHelp), targetHelp);
					primaryTOCWriter.createLink(relativeTOCPath);
					primaryTOCWriter.endTopic();
				}
			}
		}
	}

	
	private void mkdirs(Path file) {
		File parentFile = file.getParent().toFile();
		if (!parentFile.exists()) {
			file.getParent().toFile().mkdirs();
		}
	}
	
	private static void removeRecursiveContent(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				// try to delete the file anyway, even if its attributes
				// could not be read, since delete-only access is
				// theoretically possible
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				if (exc == null) {
					if (!dir.equals(path)) {
						Files.delete(dir);
					}
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed; propagate exception
					throw exc;
				}
			}
		});
	}

	private Path changeFilename(Path source, String newFileExtension) {
		return changeFilename(source, "", newFileExtension);
	}
	
	private Path changeFilename(Path source, String prefix, String newFileExtension) {
		String filename = source.getFileName().toString();
		int lastIndexOf = filename.lastIndexOf(".");
		String newFileName = prefix + filename.substring(0, lastIndexOf) + newFileExtension;
		return source.resolveSibling(newFileName);
	}

	private void genTOC(String name, String markupContent, Path targetToc, Path targetHTML) throws IOException, UnsupportedEncodingException, FileNotFoundException {
		MarkupToEclipseToc toEclipseToc = new MarkupToEclipseToc() {
			public String createToc(OutlineItem root) {
				StringWriter out = new StringWriter(8096);

				XmlStreamWriter writer = createXmlStreamWriter(out);

				writer.writeStartDocument("utf-8", "1.0"); //$NON-NLS-1$ //$NON-NLS-2$

				if (copyrightNotice != null) {
					writer.writeComment(copyrightNotice);
				}
				
				Method method = null;
				try {
					method = MarkupToEclipseToc.class.getDeclaredMethod("emitToc", XmlStreamWriter.class, List.class);
					method.setAccessible(true);
				} catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
					throw new RuntimeException(e);
				}

				if (root.getChildren().size() == 1 && root.getChildren().get(0).getLevel() == 1) {
					OutlineItem innerRoot = root.getChildren().get(0);
					writer.writeStartElement("toc"); //$NON-NLS-1$
					writer.writeAttribute("topic", getHtmlFile() + "#" + innerRoot.getId()); //$NON-NLS-1$
					writer.writeAttribute("label", innerRoot.getLabel()); //$NON-NLS-1$
					try {
						method.invoke(this, writer, innerRoot.getChildren());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				} else {
					writer.writeStartElement("toc"); //$NON-NLS-1$
					writer.writeAttribute("topic", getHtmlFile()); //$NON-NLS-1$
					writer.writeAttribute("label", root.getLabel()); //$NON-NLS-1$
					try {
						method.invoke(this, writer, root.getChildren());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}

				writer.writeEndElement(); // toc

				writer.writeEndDocument();
				writer.close();

				return out.toString();
			}
		};
		toEclipseToc.setMarkupLanguage(markupLanguage.clone());
		toEclipseToc.setHtmlFile(targetHTML.toString());
		toEclipseToc.setBookTitle(name);
		String tocContents = toEclipseToc.parse(markupContent);
		
		writeStringToFile(tocContents, targetToc);
	}
	
	private void writeStringToFile(String content, Path path) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		try(Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(path.toFile())), "UTF-8")) {
			writer.write(content);
		}
	}

	private void genHTML(String name, String markupContent, Path htmlOutputFile) throws IOException, FileNotFoundException {
		try (Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(htmlOutputFile.toFile())), UTF_8)) {
			HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer, FORMAT_OUTPUT);
			for (Stylesheet stylesheet : stylesheets) {
				HtmlDocumentBuilder.Stylesheet builderStylesheet;

				if (stylesheet.url != null) {
					if (stylesheets == websiteStylesheets) {
						Path stylesheetPath = DEFAULT_FS.getPath(stylesheet.url);
						Path targetStylesheetPath = targetRootFolder.resolve(targetWebsiteFolder.resolve(stylesheetPath));
						Path relativeStylesheetPath = htmlOutputFile.getParent().relativize(targetStylesheetPath);
						builderStylesheet = new HtmlDocumentBuilder.Stylesheet(relativeStylesheetPath.toString());
					} else {
						builderStylesheet = new HtmlDocumentBuilder.Stylesheet(stylesheet.url);
					}
				} else {
					builderStylesheet = new HtmlDocumentBuilder.Stylesheet(stylesheet.file);
				}
				builder.addCssStylesheet(builderStylesheet);

				if (!stylesheet.attributes.isEmpty()) {
					for (Map.Entry<String, String> attr : stylesheet.attributes.entrySet()) {
						builderStylesheet.getAttributes().put(attr.getKey(), attr.getValue());
					}
				}
			}

			builder.setTitle(title == null ? name.toString() : title);
			builder.setEmitDtd(emitDoctype);
			if (emitDoctype && htmlDoctype != null) {
				builder.setHtmlDtd(htmlDoctype);
			}
			builder.setUseInlineStyles(useInlineCssStyles);
			builder.setSuppressBuiltInStyles(suppressBuiltInCssStyles);
			builder.setLinkRel(linkRel);
			builder.setDefaultAbsoluteLinkTarget(defaultAbsoluteLinkTarget);
			builder.setPrependImagePrefix(prependImagePrefix);
			builder.setXhtmlStrict(xhtmlStrict);
			builder.setCopyrightNotice(copyrightNotice);

			SplittingStrategy splittingStrategy = multipleOutputFiles
					? new DefaultSplittingStrategy()
					: new NoSplittingStrategy();
			SplittingOutlineParser outlineParser = new SplittingOutlineParser();
			outlineParser.setMarkupLanguage(markupLanguage.clone());
			outlineParser.setSplittingStrategy(splittingStrategy);
			SplitOutlineItem item = outlineParser.parse(markupContent);
			item.setSplitTarget(htmlOutputFile.toFile().getName());
			SplittingHtmlDocumentBuilder splittingBuilder = new SplittingHtmlDocumentBuilder();
			splittingBuilder.setRootBuilder(builder);
			splittingBuilder.setOutline(item);
			splittingBuilder.setRootFile(htmlOutputFile.toFile());
			splittingBuilder.setNavigationImages(navigationImages);
			splittingBuilder.setFormatting(FORMAT_OUTPUT);

			MarkupParser parser = new MarkupParser();
			parser.setMarkupLanguage(markupLanguage);
			parser.setBuilder(splittingBuilder);
			parser.parse(markupContent);
		}
	}

	private String gitDescribe() {
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try {
			Repository repo = builder.setWorkTree(new File("."))
					.readEnvironment() // scan environment GIT_* variables
					.findGitDir() // scan up the file system tree
					.build();
			Git git = new Git(repo);
			DescribeCommand command = git.describe();
			return command.call();
		} catch (IOException e) {
			new RuntimeException(e);
		} catch (GitAPIException e) {
			new RuntimeException(e);
		}
		return "";
	}

	private void copy(final Path sourceFolder,
			final Path targetFolder, String pattern) throws IOException {
		final PathMatcher imageMatcher = DEFAULT_FS.getPathMatcher(pattern);
		Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path sourcePath, BasicFileAttributes attrs) throws IOException {
				if (imageMatcher.matches(sourcePath)) {
					String targetFile = sourcePath.toString().replace(sourceFolder.toString(), targetFolder.toString());
					Path targetPath = DEFAULT_FS.getPath(targetFile);
					targetPath.getParent().toFile().mkdirs();
					Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	/**
	 * Returns true if valid (may have warning).
	 * @param source
	 * @param markupContent
	 * @return
	 */
	private boolean performValidation(Path source, String markupContent) {
		StandaloneMarkupValidator markupValidator = StandaloneMarkupValidator.getValidator(MEDIA_WIKI);

		List<ValidationProblem> problems = markupValidator.validate(markupContent);

		int errorCount = 0;
		for (ValidationProblem problem : problems) {
			String messageLevel = problem.getSeverity().name();
			if (problem.getSeverity() == Severity.ERROR) {
				errorCount++;
			}
			System.out.println(String.format("%s: %s:%s %s", messageLevel, source.toString(), problem.getOffset(), problem.getMessage())); //$NON-NLS-1$
		}

		return errorCount == 0;
	}
	
	public static class Stylesheet {
		private File file;

		private String url;

		private final Map<String, String> attributes = new HashMap<>();

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void addConfiguredAttribute(Attribute attribute) {
			attributes.put(attribute.getName(), attribute.getValue());
		}
	}
	
	public static class Attribute {
		private String name;

		private String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
