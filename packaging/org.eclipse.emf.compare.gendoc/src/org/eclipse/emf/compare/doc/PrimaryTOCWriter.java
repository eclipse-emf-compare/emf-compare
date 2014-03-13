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

import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;

import org.eclipse.mylyn.wikitext.core.util.DefaultXmlStreamWriter;
import org.eclipse.mylyn.wikitext.core.util.FormattingXMLStreamWriter;
import org.eclipse.mylyn.wikitext.core.util.XmlStreamWriter;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 *
 */
public class PrimaryTOCWriter {
	
	private StringWriter primaryTOCOut;
	private XmlStreamWriter primaryTOCWriter;
	
	private StringWriter pluginOut;
	private XmlStreamWriter pluginWriter;
	private Path baseDir;

	void startPrimaryTOC(Path indexHTMLFile, String title) {
		baseDir = indexHTMLFile.getParent();
		primaryTOCOut = new StringWriter(8096);
		primaryTOCWriter = createXmlStreamWriter(primaryTOCOut);
		
		pluginOut = new StringWriter(8096);
		pluginWriter = createXmlStreamWriter(pluginOut);

		primaryTOCWriter.writeStartDocument("UTF-8", "1.0");
		primaryTOCWriter.writeStartElement("toc"); 
		primaryTOCWriter.writeAttribute("topic", indexHTMLFile.toString()); 
		primaryTOCWriter.writeAttribute("label", title); 

		pluginWriter.writeStartDocument("UTF-8", "1.0");
		pluginWriter.writeLiteral("\n<?eclipse version=\"3.2\"?>\n");
		pluginWriter.writeStartElement("plugin");
		
		pluginWriter.writeStartElement("extension");
		pluginWriter.writeAttribute("point", "org.eclipse.help.toc");
		
		pluginWriter.writeEmptyElement("toc");
		pluginWriter.writeAttribute("file", "help/toc.xml");
		pluginWriter.writeAttribute("primary", "true");
		
		pluginWriter.writeEndElement();
		
		pluginWriter.writeStartElement("extension");
		pluginWriter.writeAttribute("point", "org.eclipse.help.toc");
		
	}
	
	void endPrimaryTOC() {
		primaryTOCWriter.writeEndElement();
		primaryTOCWriter.writeEndDocument();
		primaryTOCWriter.close();
		
		pluginWriter.writeEndElement();
		pluginWriter.writeEndElement();
		pluginWriter.writeEndDocument();
		pluginWriter.close();
	}
	
	void startTopic(String label, Path href) {
		primaryTOCWriter.writeStartElement("topic");
		primaryTOCWriter.writeAttribute("label", label);
		
		if (href != null) {
			primaryTOCWriter.writeAttribute("href", href.toString());
		}
	}
	
	void createLink(Path linkedTOC) {
		primaryTOCWriter.writeStartElement("link");
		primaryTOCWriter.writeAttribute("toc", baseDir.resolve(linkedTOC).toString());
		primaryTOCWriter.writeEndElement();
		
		pluginWriter.writeEmptyElement("toc");
		pluginWriter.writeAttribute("file", baseDir.resolve(linkedTOC).toString());
	}
	
	void endTopic() {
		primaryTOCWriter.writeEndElement();
	}
	
	String getPrimaryTOCContent() {
		return primaryTOCOut.toString();
	}
	
	String getPluginContent() {
		return pluginOut.toString();
	}
	
	protected XmlStreamWriter createXmlStreamWriter(Writer out) {
		XmlStreamWriter writer = new DefaultXmlStreamWriter(out);
		return new FormattingXMLStreamWriter(writer);
	}
}
