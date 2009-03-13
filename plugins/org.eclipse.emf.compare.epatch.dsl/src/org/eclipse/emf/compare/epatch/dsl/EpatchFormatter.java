/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.compare.epatch.dsl;

import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.BiListAssignmentElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.BiSingleAssignmentElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.EpatchElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.ImportElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.ListAssignmentValueElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.MonoListAssignmentElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.MonoSingleAssignmentElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.NamedResourceElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.ObjectCopyElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.ObjectNewElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.ObjectRefElements;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess.SingleAssignmentValueElements;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.parsetree.reconstr.impl.FormattingConfig;
import org.eclipse.xtext.parsetree.reconstr.impl.FormattingTokenSerializer;

import com.google.inject.Inject;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class EpatchFormatter extends FormattingTokenSerializer {
	@Inject
	protected EpatchFormatter(IGrammarAccess grammarAccess) {
		super(grammarAccess);
	}

	@Override
	protected void configureFormatting(FormattingConfig cfg) {
		EpatchGrammarAccess g = (EpatchGrammarAccess)getGrammarAccess();

		// epatch
		EpatchElements ep = g.getEpatchAccess();
		cfg.setLinewrap().after(ep.getLeftCurlyBracketKeyword_2());
		cfg.setLinewrap().before(ep.getRightCurlyBracketKeyword_6());
		cfg.setIndentation(ep.getLeftCurlyBracketKeyword_2(), ep.getRightCurlyBracketKeyword_6());

		// package
		ImportElements pr = g.getImportAccess();
		cfg.setLinewrap().after(pr.getAlternatives());

		// namedresource
		NamedResourceElements nr = g.getNamedResourceAccess();
		cfg.setLinewrap().after(nr.getLeftCurlyBracketKeyword_2());
		cfg.setLinewrap().before(nr.getRightCurlyBracketKeyword_9());
		cfg.setIndentation(nr.getLeftCurlyBracketKeyword_2(), nr.getRightCurlyBracketKeyword_9());
		cfg.setLinewrap().after(nr.getSemicolonKeyword_5());
		cfg.setLinewrap().after(nr.getSemicolonKeyword_8());
		cfg.setNoSpace().before(nr.getSemicolonKeyword_5());
		cfg.setNoSpace().before(nr.getSemicolonKeyword_8());
		cfg.setLinewrap(2).after(nr.getRightCurlyBracketKeyword_9());

		// object
		ObjectRefElements or = g.getObjectRefAccess();
		cfg.setLinewrap().after(or.getLeftCurlyBracketKeyword_3_0());
		cfg.setLinewrap().before(or.getRightCurlyBracketKeyword_3_2());
		cfg.setIndentation(or.getLeftCurlyBracketKeyword_3_0(), or.getRightCurlyBracketKeyword_3_2());
		cfg.setLinewrap(2).after(or.getGroup());
		cfg.setNoSpace().before(or.getLeftFragAssignment_2_0_1());
		cfg.setNoSpace().before(or.getLeftFragAssignment_2_1_2());
		cfg.setNoSpace().before(or.getRightFragAssignment_2_1_5());

		// MonoSingleAssignment
		MonoSingleAssignmentElements ms = g.getMonoSingleAssignmentAccess();
		cfg.setNoSpace().before(ms.getSemicolonKeyword_3());
		cfg.setLinewrap().after(ms.getSemicolonKeyword_3());

		// MonoListAssignment
		MonoListAssignmentElements ml = g.getMonoListAssignmentAccess();
		cfg.setNoSpace().before(ml.getSemicolonKeyword_5());
		cfg.setLinewrap().after(ml.getSemicolonKeyword_5());
		cfg.setNoSpace().before(ml.getCommaKeyword_3_1_0());

		// BiSingleAssignment
		BiSingleAssignmentElements bs = g.getBiSingleAssignmentAccess();
		cfg.setNoSpace().before(bs.getSemicolonKeyword_5());
		cfg.setLinewrap().after(bs.getSemicolonKeyword_5());

		// BiListAssignment
		BiListAssignmentElements bl = g.getBiListAssignmentAccess();
		cfg.setNoSpace().before(bl.getSemicolonKeyword_7());
		cfg.setLinewrap().after(bl.getSemicolonKeyword_7());
		cfg.setNoSpace().before(bl.getCommaKeyword_3_1_0());
		cfg.setNoSpace().before(bl.getCommaKeyword_5_1_0());

		// ListAssignmentValue
		ListAssignmentValueElements ls = g.getListAssignmentValueAccess();
		cfg.setNoSpace().around(ls.getColonKeyword_1());
		cfg.setNoSpace().after(ls.getLeftSquareBracketKeyword_2_2_1_2_0());
		cfg.setNoSpace().before(ls.getRightSquareBracketKeyword_2_2_1_2_2());
		cfg.setNoSpace().after(ls.getLeftSquareBracketKeyword_2_0_0());
		cfg.setNoSpace().before(ls.getRightSquareBracketKeyword_2_0_2());
		cfg.setNoSpace().before(ls.getImpFragAssignment_2_4_1());

		// SingleAssignmentValue
		SingleAssignmentValueElements si = g.getSingleAssignmentValueAccess();
		cfg.setNoSpace().before(si.getImpFragAssignment_4_1());

		// ObjectCopy
		ObjectCopyElements oc = g.getObjectCopyAccess();
		cfg.setNoSpace().before(oc.getFragmentAssignment_2());
		cfg.setLinewrap().after(oc.getLeftCurlyBracketKeyword_4_0());
		cfg.setLinewrap().before(oc.getRightCurlyBracketKeyword_4_2());
		cfg.setIndentation(oc.getLeftCurlyBracketKeyword_4_0(), oc.getRightCurlyBracketKeyword_4_2());

		// ObjectNew
		ObjectNewElements on = g.getObjectNewAccess();
		cfg.setNoSpace().before(on.getImpFragAssignment_2());
		cfg.setLinewrap().after(on.getLeftCurlyBracketKeyword_4_0());
		cfg.setLinewrap().before(on.getRightCurlyBracketKeyword_4_2());
		cfg.setIndentation(on.getLeftCurlyBracketKeyword_4_0(), on.getRightCurlyBracketKeyword_4_2());
	}
}
