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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.internal.wikitext.mediawiki.core.block.TableOfContentsBlock;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder.BlockType;
import org.eclipse.mylyn.wikitext.core.parser.outline.OutlineItem;
import org.eclipse.mylyn.wikitext.core.parser.outline.OutlineParser;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 */
public class CustomTableOfContentsBlock extends TableOfContentsBlock {

	static final Pattern startPattern = Pattern.compile("\\s*__TOC__\\s*(.*?)"); //$NON-NLS-1$

	private int blockLineNumber = 0;

	private Matcher matcher;

	protected void emitToc(OutlineItem item) {
		if (item.getChildren().isEmpty()) {
			return;
		}
		if ((item.getLevel() + 1) > maxLevel) {
			return;
		}
		Attributes nullAttributes = new Attributes();

		builder.beginBlock(BlockType.NUMERIC_LIST, new Attributes(null, null, "list-style: none", null)); //$NON-NLS-1$ //$NON-NLS-2$
		for (OutlineItem child : item.getChildren()) {
			builder.beginBlock(BlockType.LIST_ITEM, nullAttributes);
			builder.link('#' + child.getId(), child.getLabel());
			emitToc(child);
			builder.endBlock();
		}
		builder.endBlock();
	}

	@Override
	public int processLineContent(String line, int offset) {
		if (blockLineNumber++ > 0) {
			setClosed(true);
			return 0;
		}

		if (!getMarkupLanguage().isFilterGenerativeContents()) {
			OutlineParser outlineParser = new OutlineParser(new MediaWikiLanguage());
			OutlineItem rootItem = outlineParser.parse(state.getMarkupContent());

			builder.beginBlock(BlockType.DIV, new Attributes(null, "toc", null, null));
			builder.beginHeading(3, new Attributes(null, "toc-title", null, null));
			builder.characters("Table of Contents");
			builder.endHeading();

			if (rootItem.getChildren().size() == 1 && rootItem.getChildren().get(0).getLevel() == 1) {
				emitToc(rootItem.getChildren().get(0));
			} else {
				emitToc(rootItem);
			}

			builder.endBlock();
		}
		int start = matcher.start(1);
		if (start > 0) {
			setClosed(true);
		}
		return start;
	}

	@Override
	public boolean canStart(String line, int lineOffset) {
		if (lineOffset == 0 && !getMarkupLanguage().isFilterGenerativeContents()) {
			matcher = startPattern.matcher(line);
			blockLineNumber = 0;
			return matcher.matches();
		} else {
			matcher = null;
			return false;
		}
	}

	@Override
	public CustomTableOfContentsBlock clone() {
		return (CustomTableOfContentsBlock)super.clone();
	}
}
