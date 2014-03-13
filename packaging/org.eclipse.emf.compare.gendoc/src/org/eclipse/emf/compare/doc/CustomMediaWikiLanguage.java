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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.mylyn.internal.wikitext.mediawiki.core.block.TableOfContentsBlock;
import org.eclipse.mylyn.wikitext.core.parser.markup.Block;
import org.eclipse.mylyn.wikitext.core.parser.markup.MarkupLanguage;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;

/**
 * @author <a href="mailto:mikael.barbero@obeo.fr">Mikael Barbero</a>
 *
 */
public class CustomMediaWikiLanguage extends MediaWikiLanguage {
	
	public CustomMediaWikiLanguage() {
		super();
		setName("CustomMediaWikiLanguage");
	}
	
	@Override
	protected void addStandardBlocks(List<Block> blocks,
			List<Block> paragraphBreakingBlocks) {
		super.addStandardBlocks(blocks, paragraphBreakingBlocks);
		CustomTableOfContentsBlock customTOCBlock = new CustomTableOfContentsBlock();
		replaceTOCBlock(blocks, customTOCBlock);
		replaceTOCBlock(paragraphBreakingBlocks, customTOCBlock);
	}

	private void replaceTOCBlock(List<Block> blocksList, CustomTableOfContentsBlock customTOCBlock) {
		for (Block block : new ArrayList<>(blocksList)) {
			if (block instanceof TableOfContentsBlock) {
				 
				blocksList.set(blocksList.indexOf(block), customTOCBlock);
			}
		}
	}
	
	@Override
	public MarkupLanguage clone() {
		return (CustomMediaWikiLanguage) super.clone();
	}
}