package org.eclipse.emf.compare.epatch.dsl.parser.antlr.internal; 

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.xtext.parsetree.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.emf.compare.epatch.dsl.services.EpatchGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class InternalEpatchParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_FRAGMENT", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'epatch'", "'{'", "'}'", "'import'", "'uri'", "'ns'", "'java'", "'.'", "'extension'", "'::'", "'resource'", "'left'", "';'", "'right'", "'object'", "'='", "'|'", "'['", "','", "']'", "':'", "'null'", "'new'", "'copy'"
    };
    public static final int RULE_ML_COMMENT=8;
    public static final int RULE_ID=4;
    public static final int RULE_WS=10;
    public static final int EOF=-1;
    public static final int RULE_INT=7;
    public static final int RULE_STRING=5;
    public static final int RULE_ANY_OTHER=11;
    public static final int RULE_SL_COMMENT=9;
    public static final int RULE_FRAGMENT=6;

        public InternalEpatchParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g"; }


     
     	private EpatchGrammarAccess grammarAccess;
     	
        public InternalEpatchParser(TokenStream input, IAstFactory factory, EpatchGrammarAccess grammarAccess) {
            super(input, factory, grammarAccess.getGrammar());
            this.grammarAccess = grammarAccess;
        }
        
        @Override
        protected InputStream getTokenFile() {
        	ClassLoader classLoader = getClass().getClassLoader();
        	return classLoader.getResourceAsStream("org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.tokens");
        }
        
        @Override
        protected String getFirstRuleName() {
        	return "Epatch";	
       	} 



    // $ANTLR start entryRuleEpatch
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:69:1: entryRuleEpatch returns [EObject current=null] : iv_ruleEpatch= ruleEpatch EOF ;
    public final EObject entryRuleEpatch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEpatch = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:69:48: (iv_ruleEpatch= ruleEpatch EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:70:2: iv_ruleEpatch= ruleEpatch EOF
            {
             currentNode = createCompositeNode(grammarAccess.getEpatchRule(), currentNode); 
            pushFollow(FOLLOW_ruleEpatch_in_entryRuleEpatch71);
            iv_ruleEpatch=ruleEpatch();
            _fsp--;

             current =iv_ruleEpatch; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEpatch81); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleEpatch


    // $ANTLR start ruleEpatch
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:77:1: ruleEpatch returns [EObject current=null] : ( 'epatch' (lv_name_1= RULE_ID ) '{' (lv_imports_3= ruleImport )* (lv_resources_4= ruleNamedResource )* (lv_objects_5= ruleObjectRef )* '}' ) ;
    public final EObject ruleEpatch() throws RecognitionException {
        EObject current = null;

        Token lv_name_1=null;
        EObject lv_imports_3 = null;

        EObject lv_resources_4 = null;

        EObject lv_objects_5 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:82:6: ( ( 'epatch' (lv_name_1= RULE_ID ) '{' (lv_imports_3= ruleImport )* (lv_resources_4= ruleNamedResource )* (lv_objects_5= ruleObjectRef )* '}' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:83:1: ( 'epatch' (lv_name_1= RULE_ID ) '{' (lv_imports_3= ruleImport )* (lv_resources_4= ruleNamedResource )* (lv_objects_5= ruleObjectRef )* '}' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:83:1: ( 'epatch' (lv_name_1= RULE_ID ) '{' (lv_imports_3= ruleImport )* (lv_resources_4= ruleNamedResource )* (lv_objects_5= ruleObjectRef )* '}' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:83:2: 'epatch' (lv_name_1= RULE_ID ) '{' (lv_imports_3= ruleImport )* (lv_resources_4= ruleNamedResource )* (lv_objects_5= ruleObjectRef )* '}'
            {
            match(input,12,FOLLOW_12_in_ruleEpatch115); 

                    createLeafNode(grammarAccess.getEpatchAccess().getEpatchKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:87:1: (lv_name_1= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:89:6: lv_name_1= RULE_ID
            {
            lv_name_1=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleEpatch137); 

            		createLeafNode(grammarAccess.getEpatchAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getEpatchRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "name", lv_name_1, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,13,FOLLOW_13_in_ruleEpatch154); 

                    createLeafNode(grammarAccess.getEpatchAccess().getLeftCurlyBracketKeyword_2(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:111:1: (lv_imports_3= ruleImport )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==15) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:114:6: lv_imports_3= ruleImport
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getEpatchAccess().getImportsImportParserRuleCall_3_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleImport_in_ruleEpatch188);
            	    lv_imports_3=ruleImport();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getEpatchRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        
            	    	        try {
            	    	       		add(current, "imports", lv_imports_3, "Import", currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:132:3: (lv_resources_4= ruleNamedResource )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==22) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:135:6: lv_resources_4= ruleNamedResource
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getEpatchAccess().getResourcesNamedResourceParserRuleCall_4_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleNamedResource_in_ruleEpatch227);
            	    lv_resources_4=ruleNamedResource();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getEpatchRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        
            	    	        try {
            	    	       		add(current, "resources", lv_resources_4, "NamedResource", currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:153:3: (lv_objects_5= ruleObjectRef )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==26) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:156:6: lv_objects_5= ruleObjectRef
            	    {
            	     
            	    	        currentNode=createCompositeNode(grammarAccess.getEpatchAccess().getObjectsObjectRefParserRuleCall_5_0(), currentNode); 
            	    	    
            	    pushFollow(FOLLOW_ruleObjectRef_in_ruleEpatch266);
            	    lv_objects_5=ruleObjectRef();
            	    _fsp--;


            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getEpatchRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode.getParent(), current);
            	    	        }
            	    	        
            	    	        try {
            	    	       		add(current, "objects", lv_objects_5, "ObjectRef", currentNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	        currentNode = currentNode.getParent();
            	    	    

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            match(input,14,FOLLOW_14_in_ruleEpatch280); 

                    createLeafNode(grammarAccess.getEpatchAccess().getRightCurlyBracketKeyword_6(), null); 
                

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleEpatch


    // $ANTLR start entryRuleImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:185:1: entryRuleImport returns [EObject current=null] : iv_ruleImport= ruleImport EOF ;
    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImport = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:185:48: (iv_ruleImport= ruleImport EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:186:2: iv_ruleImport= ruleImport EOF
            {
             currentNode = createCompositeNode(grammarAccess.getImportRule(), currentNode); 
            pushFollow(FOLLOW_ruleImport_in_entryRuleImport313);
            iv_ruleImport=ruleImport();
            _fsp--;

             current =iv_ruleImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleImport323); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleImport


    // $ANTLR start ruleImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:193:1: ruleImport returns [EObject current=null] : (this_ModelImport_0= ruleModelImport | this_JavaImport_1= ruleJavaImport | this_ExtensionImport_2= ruleExtensionImport ) ;
    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;

        EObject this_ModelImport_0 = null;

        EObject this_JavaImport_1 = null;

        EObject this_ExtensionImport_2 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:198:6: ( (this_ModelImport_0= ruleModelImport | this_JavaImport_1= ruleJavaImport | this_ExtensionImport_2= ruleExtensionImport ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:199:1: (this_ModelImport_0= ruleModelImport | this_JavaImport_1= ruleJavaImport | this_ExtensionImport_2= ruleExtensionImport )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:199:1: (this_ModelImport_0= ruleModelImport | this_JavaImport_1= ruleJavaImport | this_ExtensionImport_2= ruleExtensionImport )
            int alt4=3;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==15) ) {
                switch ( input.LA(2) ) {
                case RULE_ID:
                    {
                    alt4=1;
                    }
                    break;
                case 20:
                    {
                    alt4=3;
                    }
                    break;
                case 18:
                    {
                    alt4=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("199:1: (this_ModelImport_0= ruleModelImport | this_JavaImport_1= ruleJavaImport | this_ExtensionImport_2= ruleExtensionImport )", 4, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("199:1: (this_ModelImport_0= ruleModelImport | this_JavaImport_1= ruleJavaImport | this_ExtensionImport_2= ruleExtensionImport )", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:200:5: this_ModelImport_0= ruleModelImport
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getImportAccess().getModelImportParserRuleCall_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleModelImport_in_ruleImport370);
                    this_ModelImport_0=ruleModelImport();
                    _fsp--;

                     
                            current = this_ModelImport_0; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getImportAccess().getModelImportParserRuleCall_0(), null); 
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:214:5: this_JavaImport_1= ruleJavaImport
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getImportAccess().getJavaImportParserRuleCall_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleJavaImport_in_ruleImport404);
                    this_JavaImport_1=ruleJavaImport();
                    _fsp--;

                     
                            current = this_JavaImport_1; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getImportAccess().getJavaImportParserRuleCall_1(), null); 
                        

                    }
                    break;
                case 3 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:228:5: this_ExtensionImport_2= ruleExtensionImport
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getImportAccess().getExtensionImportParserRuleCall_2(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleExtensionImport_in_ruleImport438);
                    this_ExtensionImport_2=ruleExtensionImport();
                    _fsp--;

                     
                            current = this_ExtensionImport_2; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getImportAccess().getExtensionImportParserRuleCall_2(), null); 
                        

                    }
                    break;

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleImport


    // $ANTLR start entryRuleModelImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:247:1: entryRuleModelImport returns [EObject current=null] : iv_ruleModelImport= ruleModelImport EOF ;
    public final EObject entryRuleModelImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModelImport = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:247:53: (iv_ruleModelImport= ruleModelImport EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:248:2: iv_ruleModelImport= ruleModelImport EOF
            {
             currentNode = createCompositeNode(grammarAccess.getModelImportRule(), currentNode); 
            pushFollow(FOLLOW_ruleModelImport_in_entryRuleModelImport477);
            iv_ruleModelImport=ruleModelImport();
            _fsp--;

             current =iv_ruleModelImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleModelImport487); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleModelImport


    // $ANTLR start ruleModelImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:255:1: ruleModelImport returns [EObject current=null] : (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport ) ;
    public final EObject ruleModelImport() throws RecognitionException {
        EObject current = null;

        EObject this_ResourceImport_0 = null;

        EObject this_EPackageImport_1 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:260:6: ( (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:261:1: (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:261:1: (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==15) ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1==RULE_ID) ) {
                    int LA5_2 = input.LA(3);

                    if ( (LA5_2==16) ) {
                        alt5=1;
                    }
                    else if ( (LA5_2==17) ) {
                        alt5=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("261:1: (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport )", 5, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("261:1: (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport )", 5, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("261:1: (this_ResourceImport_0= ruleResourceImport | this_EPackageImport_1= ruleEPackageImport )", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:262:5: this_ResourceImport_0= ruleResourceImport
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getModelImportAccess().getResourceImportParserRuleCall_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleResourceImport_in_ruleModelImport534);
                    this_ResourceImport_0=ruleResourceImport();
                    _fsp--;

                     
                            current = this_ResourceImport_0; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getModelImportAccess().getResourceImportParserRuleCall_0(), null); 
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:276:5: this_EPackageImport_1= ruleEPackageImport
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getModelImportAccess().getEPackageImportParserRuleCall_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleEPackageImport_in_ruleModelImport568);
                    this_EPackageImport_1=ruleEPackageImport();
                    _fsp--;

                     
                            current = this_EPackageImport_1; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getModelImportAccess().getEPackageImportParserRuleCall_1(), null); 
                        

                    }
                    break;

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleModelImport


    // $ANTLR start entryRuleResourceImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:295:1: entryRuleResourceImport returns [EObject current=null] : iv_ruleResourceImport= ruleResourceImport EOF ;
    public final EObject entryRuleResourceImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleResourceImport = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:295:56: (iv_ruleResourceImport= ruleResourceImport EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:296:2: iv_ruleResourceImport= ruleResourceImport EOF
            {
             currentNode = createCompositeNode(grammarAccess.getResourceImportRule(), currentNode); 
            pushFollow(FOLLOW_ruleResourceImport_in_entryRuleResourceImport607);
            iv_ruleResourceImport=ruleResourceImport();
            _fsp--;

             current =iv_ruleResourceImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleResourceImport617); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleResourceImport


    // $ANTLR start ruleResourceImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:303:1: ruleResourceImport returns [EObject current=null] : ( 'import' (lv_name_1= RULE_ID ) 'uri' (lv_uri_3= RULE_STRING ) ) ;
    public final EObject ruleResourceImport() throws RecognitionException {
        EObject current = null;

        Token lv_name_1=null;
        Token lv_uri_3=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:308:6: ( ( 'import' (lv_name_1= RULE_ID ) 'uri' (lv_uri_3= RULE_STRING ) ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:309:1: ( 'import' (lv_name_1= RULE_ID ) 'uri' (lv_uri_3= RULE_STRING ) )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:309:1: ( 'import' (lv_name_1= RULE_ID ) 'uri' (lv_uri_3= RULE_STRING ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:309:2: 'import' (lv_name_1= RULE_ID ) 'uri' (lv_uri_3= RULE_STRING )
            {
            match(input,15,FOLLOW_15_in_ruleResourceImport651); 

                    createLeafNode(grammarAccess.getResourceImportAccess().getImportKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:313:1: (lv_name_1= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:315:6: lv_name_1= RULE_ID
            {
            lv_name_1=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleResourceImport673); 

            		createLeafNode(grammarAccess.getResourceImportAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getResourceImportRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "name", lv_name_1, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,16,FOLLOW_16_in_ruleResourceImport690); 

                    createLeafNode(grammarAccess.getResourceImportAccess().getUriKeyword_2(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:337:1: (lv_uri_3= RULE_STRING )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:339:6: lv_uri_3= RULE_STRING
            {
            lv_uri_3=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleResourceImport712); 

            		createLeafNode(grammarAccess.getResourceImportAccess().getUriSTRINGTerminalRuleCall_3_0(), "uri"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getResourceImportRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "uri", lv_uri_3, "STRING", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleResourceImport


    // $ANTLR start entryRuleEPackageImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:364:1: entryRuleEPackageImport returns [EObject current=null] : iv_ruleEPackageImport= ruleEPackageImport EOF ;
    public final EObject entryRuleEPackageImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEPackageImport = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:364:56: (iv_ruleEPackageImport= ruleEPackageImport EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:365:2: iv_ruleEPackageImport= ruleEPackageImport EOF
            {
             currentNode = createCompositeNode(grammarAccess.getEPackageImportRule(), currentNode); 
            pushFollow(FOLLOW_ruleEPackageImport_in_entryRuleEPackageImport753);
            iv_ruleEPackageImport=ruleEPackageImport();
            _fsp--;

             current =iv_ruleEPackageImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleEPackageImport763); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleEPackageImport


    // $ANTLR start ruleEPackageImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:372:1: ruleEPackageImport returns [EObject current=null] : ( 'import' (lv_name_1= RULE_ID ) 'ns' (lv_nsURI_3= RULE_STRING ) ) ;
    public final EObject ruleEPackageImport() throws RecognitionException {
        EObject current = null;

        Token lv_name_1=null;
        Token lv_nsURI_3=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:377:6: ( ( 'import' (lv_name_1= RULE_ID ) 'ns' (lv_nsURI_3= RULE_STRING ) ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:378:1: ( 'import' (lv_name_1= RULE_ID ) 'ns' (lv_nsURI_3= RULE_STRING ) )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:378:1: ( 'import' (lv_name_1= RULE_ID ) 'ns' (lv_nsURI_3= RULE_STRING ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:378:2: 'import' (lv_name_1= RULE_ID ) 'ns' (lv_nsURI_3= RULE_STRING )
            {
            match(input,15,FOLLOW_15_in_ruleEPackageImport797); 

                    createLeafNode(grammarAccess.getEPackageImportAccess().getImportKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:382:1: (lv_name_1= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:384:6: lv_name_1= RULE_ID
            {
            lv_name_1=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleEPackageImport819); 

            		createLeafNode(grammarAccess.getEPackageImportAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getEPackageImportRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "name", lv_name_1, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,17,FOLLOW_17_in_ruleEPackageImport836); 

                    createLeafNode(grammarAccess.getEPackageImportAccess().getNsKeyword_2(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:406:1: (lv_nsURI_3= RULE_STRING )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:408:6: lv_nsURI_3= RULE_STRING
            {
            lv_nsURI_3=(Token)input.LT(1);
            match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleEPackageImport858); 

            		createLeafNode(grammarAccess.getEPackageImportAccess().getNsURISTRINGTerminalRuleCall_3_0(), "nsURI"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getEPackageImportRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "nsURI", lv_nsURI_3, "STRING", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleEPackageImport


    // $ANTLR start entryRuleJavaImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:433:1: entryRuleJavaImport returns [EObject current=null] : iv_ruleJavaImport= ruleJavaImport EOF ;
    public final EObject entryRuleJavaImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleJavaImport = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:433:52: (iv_ruleJavaImport= ruleJavaImport EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:434:2: iv_ruleJavaImport= ruleJavaImport EOF
            {
             currentNode = createCompositeNode(grammarAccess.getJavaImportRule(), currentNode); 
            pushFollow(FOLLOW_ruleJavaImport_in_entryRuleJavaImport899);
            iv_ruleJavaImport=ruleJavaImport();
            _fsp--;

             current =iv_ruleJavaImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleJavaImport909); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleJavaImport


    // $ANTLR start ruleJavaImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:441:1: ruleJavaImport returns [EObject current=null] : ( 'import' 'java' (lv_path_2= RULE_ID ) ( '.' (lv_path_4= RULE_ID ) )* ) ;
    public final EObject ruleJavaImport() throws RecognitionException {
        EObject current = null;

        Token lv_path_2=null;
        Token lv_path_4=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:446:6: ( ( 'import' 'java' (lv_path_2= RULE_ID ) ( '.' (lv_path_4= RULE_ID ) )* ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:447:1: ( 'import' 'java' (lv_path_2= RULE_ID ) ( '.' (lv_path_4= RULE_ID ) )* )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:447:1: ( 'import' 'java' (lv_path_2= RULE_ID ) ( '.' (lv_path_4= RULE_ID ) )* )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:447:2: 'import' 'java' (lv_path_2= RULE_ID ) ( '.' (lv_path_4= RULE_ID ) )*
            {
            match(input,15,FOLLOW_15_in_ruleJavaImport943); 

                    createLeafNode(grammarAccess.getJavaImportAccess().getImportKeyword_0(), null); 
                
            match(input,18,FOLLOW_18_in_ruleJavaImport952); 

                    createLeafNode(grammarAccess.getJavaImportAccess().getJavaKeyword_1(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:455:1: (lv_path_2= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:457:6: lv_path_2= RULE_ID
            {
            lv_path_2=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleJavaImport974); 

            		createLeafNode(grammarAccess.getJavaImportAccess().getPathIDTerminalRuleCall_2_0(), "path"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getJavaImportRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		add(current, "path", lv_path_2, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:475:2: ( '.' (lv_path_4= RULE_ID ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==19) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:475:3: '.' (lv_path_4= RULE_ID )
            	    {
            	    match(input,19,FOLLOW_19_in_ruleJavaImport992); 

            	            createLeafNode(grammarAccess.getJavaImportAccess().getFullStopKeyword_3_0(), null); 
            	        
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:479:1: (lv_path_4= RULE_ID )
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:481:6: lv_path_4= RULE_ID
            	    {
            	    lv_path_4=(Token)input.LT(1);
            	    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleJavaImport1014); 

            	    		createLeafNode(grammarAccess.getJavaImportAccess().getPathIDTerminalRuleCall_3_1_0(), "path"); 
            	    	

            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getJavaImportRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode, current);
            	    	        }
            	    	        
            	    	        try {
            	    	       		add(current, "path", lv_path_4, "ID", lastConsumedNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleJavaImport


    // $ANTLR start entryRuleExtensionImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:506:1: entryRuleExtensionImport returns [EObject current=null] : iv_ruleExtensionImport= ruleExtensionImport EOF ;
    public final EObject entryRuleExtensionImport() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExtensionImport = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:506:57: (iv_ruleExtensionImport= ruleExtensionImport EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:507:2: iv_ruleExtensionImport= ruleExtensionImport EOF
            {
             currentNode = createCompositeNode(grammarAccess.getExtensionImportRule(), currentNode); 
            pushFollow(FOLLOW_ruleExtensionImport_in_entryRuleExtensionImport1057);
            iv_ruleExtensionImport=ruleExtensionImport();
            _fsp--;

             current =iv_ruleExtensionImport; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleExtensionImport1067); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleExtensionImport


    // $ANTLR start ruleExtensionImport
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:514:1: ruleExtensionImport returns [EObject current=null] : ( 'import' 'extension' (lv_path_2= RULE_ID ) ( '::' (lv_path_4= RULE_ID ) )* ) ;
    public final EObject ruleExtensionImport() throws RecognitionException {
        EObject current = null;

        Token lv_path_2=null;
        Token lv_path_4=null;

         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:519:6: ( ( 'import' 'extension' (lv_path_2= RULE_ID ) ( '::' (lv_path_4= RULE_ID ) )* ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:520:1: ( 'import' 'extension' (lv_path_2= RULE_ID ) ( '::' (lv_path_4= RULE_ID ) )* )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:520:1: ( 'import' 'extension' (lv_path_2= RULE_ID ) ( '::' (lv_path_4= RULE_ID ) )* )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:520:2: 'import' 'extension' (lv_path_2= RULE_ID ) ( '::' (lv_path_4= RULE_ID ) )*
            {
            match(input,15,FOLLOW_15_in_ruleExtensionImport1101); 

                    createLeafNode(grammarAccess.getExtensionImportAccess().getImportKeyword_0(), null); 
                
            match(input,20,FOLLOW_20_in_ruleExtensionImport1110); 

                    createLeafNode(grammarAccess.getExtensionImportAccess().getExtensionKeyword_1(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:528:1: (lv_path_2= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:530:6: lv_path_2= RULE_ID
            {
            lv_path_2=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleExtensionImport1132); 

            		createLeafNode(grammarAccess.getExtensionImportAccess().getPathIDTerminalRuleCall_2_0(), "path"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getExtensionImportRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		add(current, "path", lv_path_2, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:548:2: ( '::' (lv_path_4= RULE_ID ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==21) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:548:3: '::' (lv_path_4= RULE_ID )
            	    {
            	    match(input,21,FOLLOW_21_in_ruleExtensionImport1150); 

            	            createLeafNode(grammarAccess.getExtensionImportAccess().getColonColonKeyword_3_0(), null); 
            	        
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:552:1: (lv_path_4= RULE_ID )
            	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:554:6: lv_path_4= RULE_ID
            	    {
            	    lv_path_4=(Token)input.LT(1);
            	    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleExtensionImport1172); 

            	    		createLeafNode(grammarAccess.getExtensionImportAccess().getPathIDTerminalRuleCall_3_1_0(), "path"); 
            	    	

            	    	        if (current==null) {
            	    	            current = factory.create(grammarAccess.getExtensionImportRule().getType().getClassifier());
            	    	            associateNodeWithAstElement(currentNode, current);
            	    	        }
            	    	        
            	    	        try {
            	    	       		add(current, "path", lv_path_4, "ID", lastConsumedNode);
            	    	        } catch (ValueConverterException vce) {
            	    				handleValueConverterException(vce);
            	    	        }
            	    	    

            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleExtensionImport


    // $ANTLR start entryRuleNamedResource
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:579:1: entryRuleNamedResource returns [EObject current=null] : iv_ruleNamedResource= ruleNamedResource EOF ;
    public final EObject entryRuleNamedResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNamedResource = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:579:55: (iv_ruleNamedResource= ruleNamedResource EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:580:2: iv_ruleNamedResource= ruleNamedResource EOF
            {
             currentNode = createCompositeNode(grammarAccess.getNamedResourceRule(), currentNode); 
            pushFollow(FOLLOW_ruleNamedResource_in_entryRuleNamedResource1215);
            iv_ruleNamedResource=ruleNamedResource();
            _fsp--;

             current =iv_ruleNamedResource; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleNamedResource1225); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleNamedResource


    // $ANTLR start ruleNamedResource
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:587:1: ruleNamedResource returns [EObject current=null] : ( 'resource' (lv_name_1= RULE_ID ) '{' 'left' ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) ) ';' 'right' ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) ) ';' '}' ) ;
    public final EObject ruleNamedResource() throws RecognitionException {
        EObject current = null;

        Token lv_name_1=null;
        Token lv_leftUri_5=null;
        Token lv_rightUri_10=null;
        EObject lv_leftRoot_6 = null;

        EObject lv_rightRoot_11 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:592:6: ( ( 'resource' (lv_name_1= RULE_ID ) '{' 'left' ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) ) ';' 'right' ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) ) ';' '}' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:593:1: ( 'resource' (lv_name_1= RULE_ID ) '{' 'left' ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) ) ';' 'right' ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) ) ';' '}' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:593:1: ( 'resource' (lv_name_1= RULE_ID ) '{' 'left' ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) ) ';' 'right' ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) ) ';' '}' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:593:2: 'resource' (lv_name_1= RULE_ID ) '{' 'left' ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) ) ';' 'right' ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) ) ';' '}'
            {
            match(input,22,FOLLOW_22_in_ruleNamedResource1259); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getResourceKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:597:1: (lv_name_1= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:599:6: lv_name_1= RULE_ID
            {
            lv_name_1=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleNamedResource1281); 

            		createLeafNode(grammarAccess.getNamedResourceAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getNamedResourceRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "name", lv_name_1, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,13,FOLLOW_13_in_ruleNamedResource1298); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getLeftCurlyBracketKeyword_2(), null); 
                
            match(input,23,FOLLOW_23_in_ruleNamedResource1307); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getLeftKeyword_3(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:625:1: ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==16) ) {
                alt8=1;
            }
            else if ( ((LA8_0>=34 && LA8_0<=35)) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("625:1: ( ( 'uri' (lv_leftUri_5= RULE_STRING ) ) | (lv_leftRoot_6= ruleCreatedObject ) )", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:625:2: ( 'uri' (lv_leftUri_5= RULE_STRING ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:625:2: ( 'uri' (lv_leftUri_5= RULE_STRING ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:625:3: 'uri' (lv_leftUri_5= RULE_STRING )
                    {
                    match(input,16,FOLLOW_16_in_ruleNamedResource1318); 

                            createLeafNode(grammarAccess.getNamedResourceAccess().getUriKeyword_4_0_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:629:1: (lv_leftUri_5= RULE_STRING )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:631:6: lv_leftUri_5= RULE_STRING
                    {
                    lv_leftUri_5=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleNamedResource1340); 

                    		createLeafNode(grammarAccess.getNamedResourceAccess().getLeftUriSTRINGTerminalRuleCall_4_0_1_0(), "leftUri"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNamedResourceRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "leftUri", lv_leftUri_5, "STRING", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:650:6: (lv_leftRoot_6= ruleCreatedObject )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:650:6: (lv_leftRoot_6= ruleCreatedObject )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:653:6: lv_leftRoot_6= ruleCreatedObject
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getNamedResourceAccess().getLeftRootCreatedObjectParserRuleCall_4_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleCreatedObject_in_ruleNamedResource1389);
                    lv_leftRoot_6=ruleCreatedObject();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNamedResourceRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "leftRoot", lv_leftRoot_6, "CreatedObject", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }
                    break;

            }

            match(input,24,FOLLOW_24_in_ruleNamedResource1403); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getSemicolonKeyword_5(), null); 
                
            match(input,25,FOLLOW_25_in_ruleNamedResource1412); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getRightKeyword_6(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:679:1: ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==16) ) {
                alt9=1;
            }
            else if ( ((LA9_0>=34 && LA9_0<=35)) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("679:1: ( ( 'uri' (lv_rightUri_10= RULE_STRING ) ) | (lv_rightRoot_11= ruleCreatedObject ) )", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:679:2: ( 'uri' (lv_rightUri_10= RULE_STRING ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:679:2: ( 'uri' (lv_rightUri_10= RULE_STRING ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:679:3: 'uri' (lv_rightUri_10= RULE_STRING )
                    {
                    match(input,16,FOLLOW_16_in_ruleNamedResource1423); 

                            createLeafNode(grammarAccess.getNamedResourceAccess().getUriKeyword_7_0_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:683:1: (lv_rightUri_10= RULE_STRING )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:685:6: lv_rightUri_10= RULE_STRING
                    {
                    lv_rightUri_10=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleNamedResource1445); 

                    		createLeafNode(grammarAccess.getNamedResourceAccess().getRightUriSTRINGTerminalRuleCall_7_0_1_0(), "rightUri"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNamedResourceRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "rightUri", lv_rightUri_10, "STRING", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:704:6: (lv_rightRoot_11= ruleCreatedObject )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:704:6: (lv_rightRoot_11= ruleCreatedObject )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:707:6: lv_rightRoot_11= ruleCreatedObject
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getNamedResourceAccess().getRightRootCreatedObjectParserRuleCall_7_1_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleCreatedObject_in_ruleNamedResource1494);
                    lv_rightRoot_11=ruleCreatedObject();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getNamedResourceRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "rightRoot", lv_rightRoot_11, "CreatedObject", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }
                    break;

            }

            match(input,24,FOLLOW_24_in_ruleNamedResource1508); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getSemicolonKeyword_8(), null); 
                
            match(input,14,FOLLOW_14_in_ruleNamedResource1517); 

                    createLeafNode(grammarAccess.getNamedResourceAccess().getRightCurlyBracketKeyword_9(), null); 
                

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleNamedResource


    // $ANTLR start entryRuleObjectRef
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:742:1: entryRuleObjectRef returns [EObject current=null] : iv_ruleObjectRef= ruleObjectRef EOF ;
    public final EObject entryRuleObjectRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectRef = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:742:51: (iv_ruleObjectRef= ruleObjectRef EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:743:2: iv_ruleObjectRef= ruleObjectRef EOF
            {
             currentNode = createCompositeNode(grammarAccess.getObjectRefRule(), currentNode); 
            pushFollow(FOLLOW_ruleObjectRef_in_entryRuleObjectRef1552);
            iv_ruleObjectRef=ruleObjectRef();
            _fsp--;

             current =iv_ruleObjectRef; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleObjectRef1562); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleObjectRef


    // $ANTLR start ruleObjectRef
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:750:1: ruleObjectRef returns [EObject current=null] : ( 'object' (lv_name_1= RULE_ID )? ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) ) ( '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}' )? ) ;
    public final EObject ruleObjectRef() throws RecognitionException {
        EObject current = null;

        Token lv_name_1=null;
        Token lv_leftFrag_3=null;
        Token lv_leftFrag_6=null;
        Token lv_rightFrag_9=null;
        EObject lv_assignments_11 = null;

        EObject lv_assignments_12 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:755:6: ( ( 'object' (lv_name_1= RULE_ID )? ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) ) ( '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}' )? ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:756:1: ( 'object' (lv_name_1= RULE_ID )? ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) ) ( '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}' )? )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:756:1: ( 'object' (lv_name_1= RULE_ID )? ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) ) ( '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}' )? )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:756:2: 'object' (lv_name_1= RULE_ID )? ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) ) ( '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}' )?
            {
            match(input,26,FOLLOW_26_in_ruleObjectRef1596); 

                    createLeafNode(grammarAccess.getObjectRefAccess().getObjectKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:760:1: (lv_name_1= RULE_ID )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==RULE_ID) ) {
                int LA10_1 = input.LA(2);

                if ( (LA10_1==RULE_ID||LA10_1==23) ) {
                    alt10=1;
                }
            }
            switch (alt10) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:762:6: lv_name_1= RULE_ID
                    {
                    lv_name_1=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectRef1618); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getNameIDTerminalRuleCall_1_0(), "name"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "name", lv_name_1, "ID", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:780:3: ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==RULE_ID) ) {
                alt11=1;
            }
            else if ( (LA11_0==23) ) {
                alt11=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("780:3: ( ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) ) | ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) ) )", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:780:4: ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:780:4: ( ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:780:5: ( RULE_ID ) (lv_leftFrag_3= RULE_FRAGMENT )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:780:5: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:783:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectRef1651); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getLeftResNamedResourceCrossReference_2_0_0_0(), "leftRes"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:796:2: (lv_leftFrag_3= RULE_FRAGMENT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:798:6: lv_leftFrag_3= RULE_FRAGMENT
                    {
                    lv_leftFrag_3=(Token)input.LT(1);
                    match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleObjectRef1676); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getLeftFragFRAGMENTTerminalRuleCall_2_0_1_0(), "leftFrag"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "leftFrag", lv_leftFrag_3, "FRAGMENT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:817:6: ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:817:6: ( 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:817:7: 'left' ( RULE_ID ) (lv_leftFrag_6= RULE_FRAGMENT ) 'right' ( RULE_ID ) (lv_rightFrag_9= RULE_FRAGMENT )
                    {
                    match(input,23,FOLLOW_23_in_ruleObjectRef1701); 

                            createLeafNode(grammarAccess.getObjectRefAccess().getLeftKeyword_2_1_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:821:1: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:824:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectRef1723); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getLeftResNamedResourceCrossReference_2_1_1_0(), "leftRes"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:837:2: (lv_leftFrag_6= RULE_FRAGMENT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:839:6: lv_leftFrag_6= RULE_FRAGMENT
                    {
                    lv_leftFrag_6=(Token)input.LT(1);
                    match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleObjectRef1748); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getLeftFragFRAGMENTTerminalRuleCall_2_1_2_0(), "leftFrag"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "leftFrag", lv_leftFrag_6, "FRAGMENT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }

                    match(input,25,FOLLOW_25_in_ruleObjectRef1765); 

                            createLeafNode(grammarAccess.getObjectRefAccess().getRightKeyword_2_1_3(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:861:1: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:864:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectRef1787); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getRightResNamedResourceCrossReference_2_1_4_0(), "rightRes"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:877:2: (lv_rightFrag_9= RULE_FRAGMENT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:879:6: lv_rightFrag_9= RULE_FRAGMENT
                    {
                    lv_rightFrag_9=(Token)input.LT(1);
                    match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleObjectRef1812); 

                    		createLeafNode(grammarAccess.getObjectRefAccess().getRightFragFRAGMENTTerminalRuleCall_2_1_5_0(), "rightFrag"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "rightFrag", lv_rightFrag_9, "FRAGMENT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:897:4: ( '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}' )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==13) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:897:5: '{' ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )* '}'
                    {
                    match(input,13,FOLLOW_13_in_ruleObjectRef1832); 

                            createLeafNode(grammarAccess.getObjectRefAccess().getLeftCurlyBracketKeyword_3_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:901:1: ( (lv_assignments_11= ruleBiSingleAssignment ) | (lv_assignments_12= ruleBiListAssignment ) )*
                    loop12:
                    do {
                        int alt12=3;
                        int LA12_0 = input.LA(1);

                        if ( (LA12_0==RULE_ID) ) {
                            int LA12_2 = input.LA(2);

                            if ( (LA12_2==27) ) {
                                int LA12_3 = input.LA(3);

                                if ( (LA12_3==29) ) {
                                    alt12=2;
                                }
                                else if ( ((LA12_3>=RULE_ID && LA12_3<=RULE_STRING)||(LA12_3>=33 && LA12_3<=35)) ) {
                                    alt12=1;
                                }


                            }


                        }


                        switch (alt12) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:901:2: (lv_assignments_11= ruleBiSingleAssignment )
                    	    {
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:901:2: (lv_assignments_11= ruleBiSingleAssignment )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:904:6: lv_assignments_11= ruleBiSingleAssignment
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getObjectRefAccess().getAssignmentsBiSingleAssignmentParserRuleCall_3_1_0_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleBiSingleAssignment_in_ruleObjectRef1867);
                    	    lv_assignments_11=ruleBiSingleAssignment();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "assignments", lv_assignments_11, "BiSingleAssignment", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:923:6: (lv_assignments_12= ruleBiListAssignment )
                    	    {
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:923:6: (lv_assignments_12= ruleBiListAssignment )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:926:6: lv_assignments_12= ruleBiListAssignment
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getObjectRefAccess().getAssignmentsBiListAssignmentParserRuleCall_3_1_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleBiListAssignment_in_ruleObjectRef1911);
                    	    lv_assignments_12=ruleBiListAssignment();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getObjectRefRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "assignments", lv_assignments_12, "BiListAssignment", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    match(input,14,FOLLOW_14_in_ruleObjectRef1926); 

                            createLeafNode(grammarAccess.getObjectRefAccess().getRightCurlyBracketKeyword_3_2(), null); 
                        

                    }
                    break;

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleObjectRef


    // $ANTLR start entryRuleCreatedObject
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:955:1: entryRuleCreatedObject returns [EObject current=null] : iv_ruleCreatedObject= ruleCreatedObject EOF ;
    public final EObject entryRuleCreatedObject() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCreatedObject = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:955:55: (iv_ruleCreatedObject= ruleCreatedObject EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:956:2: iv_ruleCreatedObject= ruleCreatedObject EOF
            {
             currentNode = createCompositeNode(grammarAccess.getCreatedObjectRule(), currentNode); 
            pushFollow(FOLLOW_ruleCreatedObject_in_entryRuleCreatedObject1961);
            iv_ruleCreatedObject=ruleCreatedObject();
            _fsp--;

             current =iv_ruleCreatedObject; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleCreatedObject1971); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleCreatedObject


    // $ANTLR start ruleCreatedObject
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:963:1: ruleCreatedObject returns [EObject current=null] : (this_ObjectNew_0= ruleObjectNew | this_ObjectCopy_1= ruleObjectCopy ) ;
    public final EObject ruleCreatedObject() throws RecognitionException {
        EObject current = null;

        EObject this_ObjectNew_0 = null;

        EObject this_ObjectCopy_1 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:968:6: ( (this_ObjectNew_0= ruleObjectNew | this_ObjectCopy_1= ruleObjectCopy ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:969:1: (this_ObjectNew_0= ruleObjectNew | this_ObjectCopy_1= ruleObjectCopy )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:969:1: (this_ObjectNew_0= ruleObjectNew | this_ObjectCopy_1= ruleObjectCopy )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==34) ) {
                alt14=1;
            }
            else if ( (LA14_0==35) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("969:1: (this_ObjectNew_0= ruleObjectNew | this_ObjectCopy_1= ruleObjectCopy )", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:970:5: this_ObjectNew_0= ruleObjectNew
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getCreatedObjectAccess().getObjectNewParserRuleCall_0(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleObjectNew_in_ruleCreatedObject2018);
                    this_ObjectNew_0=ruleObjectNew();
                    _fsp--;

                     
                            current = this_ObjectNew_0; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getCreatedObjectAccess().getObjectNewParserRuleCall_0(), null); 
                        

                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:984:5: this_ObjectCopy_1= ruleObjectCopy
                    {
                     
                            currentNode=createCompositeNode(grammarAccess.getCreatedObjectAccess().getObjectCopyParserRuleCall_1(), currentNode); 
                        
                    pushFollow(FOLLOW_ruleObjectCopy_in_ruleCreatedObject2052);
                    this_ObjectCopy_1=ruleObjectCopy();
                    _fsp--;

                     
                            current = this_ObjectCopy_1; 
                            currentNode = currentNode.getParent();
                        
                     
                        createLeafNode(grammarAccess.getCreatedObjectAccess().getObjectCopyParserRuleCall_1(), null); 
                        

                    }
                    break;

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleCreatedObject


    // $ANTLR start entryRuleBiSingleAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1005:1: entryRuleBiSingleAssignment returns [EObject current=null] : iv_ruleBiSingleAssignment= ruleBiSingleAssignment EOF ;
    public final EObject entryRuleBiSingleAssignment() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBiSingleAssignment = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1005:60: (iv_ruleBiSingleAssignment= ruleBiSingleAssignment EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1006:2: iv_ruleBiSingleAssignment= ruleBiSingleAssignment EOF
            {
             currentNode = createCompositeNode(grammarAccess.getBiSingleAssignmentRule(), currentNode); 
            pushFollow(FOLLOW_ruleBiSingleAssignment_in_entryRuleBiSingleAssignment2093);
            iv_ruleBiSingleAssignment=ruleBiSingleAssignment();
            _fsp--;

             current =iv_ruleBiSingleAssignment; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBiSingleAssignment2103); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleBiSingleAssignment


    // $ANTLR start ruleBiSingleAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1013:1: ruleBiSingleAssignment returns [EObject current=null] : ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) '|' (lv_rightValue_4= ruleSingleAssignmentValue ) ';' ) ;
    public final EObject ruleBiSingleAssignment() throws RecognitionException {
        EObject current = null;

        Token lv_feature_0=null;
        EObject lv_leftValue_2 = null;

        EObject lv_rightValue_4 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1018:6: ( ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) '|' (lv_rightValue_4= ruleSingleAssignmentValue ) ';' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1019:1: ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) '|' (lv_rightValue_4= ruleSingleAssignmentValue ) ';' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1019:1: ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) '|' (lv_rightValue_4= ruleSingleAssignmentValue ) ';' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1019:2: (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) '|' (lv_rightValue_4= ruleSingleAssignmentValue ) ';'
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1019:2: (lv_feature_0= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1021:6: lv_feature_0= RULE_ID
            {
            lv_feature_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleBiSingleAssignment2150); 

            		createLeafNode(grammarAccess.getBiSingleAssignmentAccess().getFeatureIDTerminalRuleCall_0_0(), "feature"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getBiSingleAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "feature", lv_feature_0, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,27,FOLLOW_27_in_ruleBiSingleAssignment2167); 

                    createLeafNode(grammarAccess.getBiSingleAssignmentAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1043:1: (lv_leftValue_2= ruleSingleAssignmentValue )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1046:6: lv_leftValue_2= ruleSingleAssignmentValue
            {
             
            	        currentNode=createCompositeNode(grammarAccess.getBiSingleAssignmentAccess().getLeftValueSingleAssignmentValueParserRuleCall_2_0(), currentNode); 
            	    
            pushFollow(FOLLOW_ruleSingleAssignmentValue_in_ruleBiSingleAssignment2201);
            lv_leftValue_2=ruleSingleAssignmentValue();
            _fsp--;


            	        if (current==null) {
            	            current = factory.create(grammarAccess.getBiSingleAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode.getParent(), current);
            	        }
            	        
            	        try {
            	       		set(current, "leftValue", lv_leftValue_2, "SingleAssignmentValue", currentNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	        currentNode = currentNode.getParent();
            	    

            }

            match(input,28,FOLLOW_28_in_ruleBiSingleAssignment2214); 

                    createLeafNode(grammarAccess.getBiSingleAssignmentAccess().getVerticalLineKeyword_3(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1068:1: (lv_rightValue_4= ruleSingleAssignmentValue )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1071:6: lv_rightValue_4= ruleSingleAssignmentValue
            {
             
            	        currentNode=createCompositeNode(grammarAccess.getBiSingleAssignmentAccess().getRightValueSingleAssignmentValueParserRuleCall_4_0(), currentNode); 
            	    
            pushFollow(FOLLOW_ruleSingleAssignmentValue_in_ruleBiSingleAssignment2248);
            lv_rightValue_4=ruleSingleAssignmentValue();
            _fsp--;


            	        if (current==null) {
            	            current = factory.create(grammarAccess.getBiSingleAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode.getParent(), current);
            	        }
            	        
            	        try {
            	       		set(current, "rightValue", lv_rightValue_4, "SingleAssignmentValue", currentNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	        currentNode = currentNode.getParent();
            	    

            }

            match(input,24,FOLLOW_24_in_ruleBiSingleAssignment2261); 

                    createLeafNode(grammarAccess.getBiSingleAssignmentAccess().getSemicolonKeyword_5(), null); 
                

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleBiSingleAssignment


    // $ANTLR start entryRuleBiListAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1100:1: entryRuleBiListAssignment returns [EObject current=null] : iv_ruleBiListAssignment= ruleBiListAssignment EOF ;
    public final EObject entryRuleBiListAssignment() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBiListAssignment = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1100:58: (iv_ruleBiListAssignment= ruleBiListAssignment EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1101:2: iv_ruleBiListAssignment= ruleBiListAssignment EOF
            {
             currentNode = createCompositeNode(grammarAccess.getBiListAssignmentRule(), currentNode); 
            pushFollow(FOLLOW_ruleBiListAssignment_in_entryRuleBiListAssignment2294);
            iv_ruleBiListAssignment=ruleBiListAssignment();
            _fsp--;

             current =iv_ruleBiListAssignment; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleBiListAssignment2304); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleBiListAssignment


    // $ANTLR start ruleBiListAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1108:1: ruleBiListAssignment returns [EObject current=null] : ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )* )? '|' ( (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )* )? ']' ';' ) ;
    public final EObject ruleBiListAssignment() throws RecognitionException {
        EObject current = null;

        Token lv_feature_0=null;
        EObject lv_leftValues_3 = null;

        EObject lv_leftValues_5 = null;

        EObject lv_rightValues_7 = null;

        EObject lv_rightValues_9 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1113:6: ( ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )* )? '|' ( (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )* )? ']' ';' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1114:1: ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )* )? '|' ( (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )* )? ']' ';' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1114:1: ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )* )? '|' ( (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )* )? ']' ';' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1114:2: (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )* )? '|' ( (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )* )? ']' ';'
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1114:2: (lv_feature_0= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1116:6: lv_feature_0= RULE_ID
            {
            lv_feature_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleBiListAssignment2351); 

            		createLeafNode(grammarAccess.getBiListAssignmentAccess().getFeatureIDTerminalRuleCall_0_0(), "feature"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getBiListAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "feature", lv_feature_0, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,27,FOLLOW_27_in_ruleBiListAssignment2368); 

                    createLeafNode(grammarAccess.getBiListAssignmentAccess().getEqualsSignKeyword_1(), null); 
                
            match(input,29,FOLLOW_29_in_ruleBiListAssignment2377); 

                    createLeafNode(grammarAccess.getBiListAssignmentAccess().getLeftSquareBracketKeyword_2(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1142:1: ( (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )* )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==RULE_INT) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1142:2: (lv_leftValues_3= ruleListAssignmentValue ) ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )*
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1142:2: (lv_leftValues_3= ruleListAssignmentValue )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1145:6: lv_leftValues_3= ruleListAssignmentValue
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getBiListAssignmentAccess().getLeftValuesListAssignmentValueParserRuleCall_3_0_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2412);
                    lv_leftValues_3=ruleListAssignmentValue();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getBiListAssignmentRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		add(current, "leftValues", lv_leftValues_3, "ListAssignmentValue", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1163:2: ( ',' (lv_leftValues_5= ruleListAssignmentValue ) )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( (LA15_0==30) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1163:3: ',' (lv_leftValues_5= ruleListAssignmentValue )
                    	    {
                    	    match(input,30,FOLLOW_30_in_ruleBiListAssignment2426); 

                    	            createLeafNode(grammarAccess.getBiListAssignmentAccess().getCommaKeyword_3_1_0(), null); 
                    	        
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1167:1: (lv_leftValues_5= ruleListAssignmentValue )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1170:6: lv_leftValues_5= ruleListAssignmentValue
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getBiListAssignmentAccess().getLeftValuesListAssignmentValueParserRuleCall_3_1_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2460);
                    	    lv_leftValues_5=ruleListAssignmentValue();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getBiListAssignmentRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "leftValues", lv_leftValues_5, "ListAssignmentValue", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,28,FOLLOW_28_in_ruleBiListAssignment2477); 

                    createLeafNode(grammarAccess.getBiListAssignmentAccess().getVerticalLineKeyword_4(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1192:1: ( (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )* )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==RULE_INT) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1192:2: (lv_rightValues_7= ruleListAssignmentValue ) ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )*
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1192:2: (lv_rightValues_7= ruleListAssignmentValue )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1195:6: lv_rightValues_7= ruleListAssignmentValue
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getBiListAssignmentAccess().getRightValuesListAssignmentValueParserRuleCall_5_0_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2512);
                    lv_rightValues_7=ruleListAssignmentValue();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getBiListAssignmentRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		add(current, "rightValues", lv_rightValues_7, "ListAssignmentValue", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1213:2: ( ',' (lv_rightValues_9= ruleListAssignmentValue ) )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0==30) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1213:3: ',' (lv_rightValues_9= ruleListAssignmentValue )
                    	    {
                    	    match(input,30,FOLLOW_30_in_ruleBiListAssignment2526); 

                    	            createLeafNode(grammarAccess.getBiListAssignmentAccess().getCommaKeyword_5_1_0(), null); 
                    	        
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1217:1: (lv_rightValues_9= ruleListAssignmentValue )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1220:6: lv_rightValues_9= ruleListAssignmentValue
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getBiListAssignmentAccess().getRightValuesListAssignmentValueParserRuleCall_5_1_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2560);
                    	    lv_rightValues_9=ruleListAssignmentValue();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getBiListAssignmentRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "rightValues", lv_rightValues_9, "ListAssignmentValue", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,31,FOLLOW_31_in_ruleBiListAssignment2577); 

                    createLeafNode(grammarAccess.getBiListAssignmentAccess().getRightSquareBracketKeyword_6(), null); 
                
            match(input,24,FOLLOW_24_in_ruleBiListAssignment2586); 

                    createLeafNode(grammarAccess.getBiListAssignmentAccess().getSemicolonKeyword_7(), null); 
                

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleBiListAssignment


    // $ANTLR start entryRuleMonoSingleAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1253:1: entryRuleMonoSingleAssignment returns [EObject current=null] : iv_ruleMonoSingleAssignment= ruleMonoSingleAssignment EOF ;
    public final EObject entryRuleMonoSingleAssignment() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMonoSingleAssignment = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1253:62: (iv_ruleMonoSingleAssignment= ruleMonoSingleAssignment EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1254:2: iv_ruleMonoSingleAssignment= ruleMonoSingleAssignment EOF
            {
             currentNode = createCompositeNode(grammarAccess.getMonoSingleAssignmentRule(), currentNode); 
            pushFollow(FOLLOW_ruleMonoSingleAssignment_in_entryRuleMonoSingleAssignment2619);
            iv_ruleMonoSingleAssignment=ruleMonoSingleAssignment();
            _fsp--;

             current =iv_ruleMonoSingleAssignment; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMonoSingleAssignment2629); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleMonoSingleAssignment


    // $ANTLR start ruleMonoSingleAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1261:1: ruleMonoSingleAssignment returns [EObject current=null] : ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) ';' ) ;
    public final EObject ruleMonoSingleAssignment() throws RecognitionException {
        EObject current = null;

        Token lv_feature_0=null;
        EObject lv_leftValue_2 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1266:6: ( ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) ';' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1267:1: ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) ';' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1267:1: ( (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) ';' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1267:2: (lv_feature_0= RULE_ID ) '=' (lv_leftValue_2= ruleSingleAssignmentValue ) ';'
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1267:2: (lv_feature_0= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1269:6: lv_feature_0= RULE_ID
            {
            lv_feature_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMonoSingleAssignment2676); 

            		createLeafNode(grammarAccess.getMonoSingleAssignmentAccess().getFeatureIDTerminalRuleCall_0_0(), "feature"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getMonoSingleAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "feature", lv_feature_0, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,27,FOLLOW_27_in_ruleMonoSingleAssignment2693); 

                    createLeafNode(grammarAccess.getMonoSingleAssignmentAccess().getEqualsSignKeyword_1(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1291:1: (lv_leftValue_2= ruleSingleAssignmentValue )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1294:6: lv_leftValue_2= ruleSingleAssignmentValue
            {
             
            	        currentNode=createCompositeNode(grammarAccess.getMonoSingleAssignmentAccess().getLeftValueSingleAssignmentValueParserRuleCall_2_0(), currentNode); 
            	    
            pushFollow(FOLLOW_ruleSingleAssignmentValue_in_ruleMonoSingleAssignment2727);
            lv_leftValue_2=ruleSingleAssignmentValue();
            _fsp--;


            	        if (current==null) {
            	            current = factory.create(grammarAccess.getMonoSingleAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode.getParent(), current);
            	        }
            	        
            	        try {
            	       		set(current, "leftValue", lv_leftValue_2, "SingleAssignmentValue", currentNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	        currentNode = currentNode.getParent();
            	    

            }

            match(input,24,FOLLOW_24_in_ruleMonoSingleAssignment2740); 

                    createLeafNode(grammarAccess.getMonoSingleAssignmentAccess().getSemicolonKeyword_3(), null); 
                

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleMonoSingleAssignment


    // $ANTLR start entryRuleMonoListAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1323:1: entryRuleMonoListAssignment returns [EObject current=null] : iv_ruleMonoListAssignment= ruleMonoListAssignment EOF ;
    public final EObject entryRuleMonoListAssignment() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMonoListAssignment = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1323:60: (iv_ruleMonoListAssignment= ruleMonoListAssignment EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1324:2: iv_ruleMonoListAssignment= ruleMonoListAssignment EOF
            {
             currentNode = createCompositeNode(grammarAccess.getMonoListAssignmentRule(), currentNode); 
            pushFollow(FOLLOW_ruleMonoListAssignment_in_entryRuleMonoListAssignment2773);
            iv_ruleMonoListAssignment=ruleMonoListAssignment();
            _fsp--;

             current =iv_ruleMonoListAssignment; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleMonoListAssignment2783); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleMonoListAssignment


    // $ANTLR start ruleMonoListAssignment
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1331:1: ruleMonoListAssignment returns [EObject current=null] : ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )* )? ']' ';' ) ;
    public final EObject ruleMonoListAssignment() throws RecognitionException {
        EObject current = null;

        Token lv_feature_0=null;
        EObject lv_leftValues_3 = null;

        EObject lv_leftValues_5 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1336:6: ( ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )* )? ']' ';' ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1337:1: ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )* )? ']' ';' )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1337:1: ( (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )* )? ']' ';' )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1337:2: (lv_feature_0= RULE_ID ) '=' '[' ( (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )* )? ']' ';'
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1337:2: (lv_feature_0= RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1339:6: lv_feature_0= RULE_ID
            {
            lv_feature_0=(Token)input.LT(1);
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleMonoListAssignment2830); 

            		createLeafNode(grammarAccess.getMonoListAssignmentAccess().getFeatureIDTerminalRuleCall_0_0(), "feature"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getMonoListAssignmentRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "feature", lv_feature_0, "ID", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,27,FOLLOW_27_in_ruleMonoListAssignment2847); 

                    createLeafNode(grammarAccess.getMonoListAssignmentAccess().getEqualsSignKeyword_1(), null); 
                
            match(input,29,FOLLOW_29_in_ruleMonoListAssignment2856); 

                    createLeafNode(grammarAccess.getMonoListAssignmentAccess().getLeftSquareBracketKeyword_2(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1365:1: ( (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )* )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( ((LA20_0>=RULE_ID && LA20_0<=RULE_STRING)||(LA20_0>=34 && LA20_0<=35)) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1365:2: (lv_leftValues_3= ruleAssignmentValue ) ( ',' (lv_leftValues_5= ruleAssignmentValue ) )*
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1365:2: (lv_leftValues_3= ruleAssignmentValue )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1368:6: lv_leftValues_3= ruleAssignmentValue
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getMonoListAssignmentAccess().getLeftValuesAssignmentValueParserRuleCall_3_0_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleAssignmentValue_in_ruleMonoListAssignment2891);
                    lv_leftValues_3=ruleAssignmentValue();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getMonoListAssignmentRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		add(current, "leftValues", lv_leftValues_3, "AssignmentValue", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1386:2: ( ',' (lv_leftValues_5= ruleAssignmentValue ) )*
                    loop19:
                    do {
                        int alt19=2;
                        int LA19_0 = input.LA(1);

                        if ( (LA19_0==30) ) {
                            alt19=1;
                        }


                        switch (alt19) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1386:3: ',' (lv_leftValues_5= ruleAssignmentValue )
                    	    {
                    	    match(input,30,FOLLOW_30_in_ruleMonoListAssignment2905); 

                    	            createLeafNode(grammarAccess.getMonoListAssignmentAccess().getCommaKeyword_3_1_0(), null); 
                    	        
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1390:1: (lv_leftValues_5= ruleAssignmentValue )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1393:6: lv_leftValues_5= ruleAssignmentValue
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getMonoListAssignmentAccess().getLeftValuesAssignmentValueParserRuleCall_3_1_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleAssignmentValue_in_ruleMonoListAssignment2939);
                    	    lv_leftValues_5=ruleAssignmentValue();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getMonoListAssignmentRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "leftValues", lv_leftValues_5, "AssignmentValue", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop19;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,31,FOLLOW_31_in_ruleMonoListAssignment2956); 

                    createLeafNode(grammarAccess.getMonoListAssignmentAccess().getRightSquareBracketKeyword_4(), null); 
                
            match(input,24,FOLLOW_24_in_ruleMonoListAssignment2965); 

                    createLeafNode(grammarAccess.getMonoListAssignmentAccess().getSemicolonKeyword_5(), null); 
                

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleMonoListAssignment


    // $ANTLR start entryRuleAssignmentValue
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1426:1: entryRuleAssignmentValue returns [EObject current=null] : iv_ruleAssignmentValue= ruleAssignmentValue EOF ;
    public final EObject entryRuleAssignmentValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAssignmentValue = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1426:57: (iv_ruleAssignmentValue= ruleAssignmentValue EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1427:2: iv_ruleAssignmentValue= ruleAssignmentValue EOF
            {
             currentNode = createCompositeNode(grammarAccess.getAssignmentValueRule(), currentNode); 
            pushFollow(FOLLOW_ruleAssignmentValue_in_entryRuleAssignmentValue2998);
            iv_ruleAssignmentValue=ruleAssignmentValue();
            _fsp--;

             current =iv_ruleAssignmentValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleAssignmentValue3008); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleAssignmentValue


    // $ANTLR start ruleAssignmentValue
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1434:1: ruleAssignmentValue returns [EObject current=null] : ( (lv_value_0= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? ) | (lv_newObject_7= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) ) ) ;
    public final EObject ruleAssignmentValue() throws RecognitionException {
        EObject current = null;

        Token lv_value_0=null;
        Token lv_refFeature_3=null;
        Token lv_refIndex_5=null;
        Token lv_impFrag_9=null;
        EObject lv_newObject_7 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1439:6: ( ( (lv_value_0= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? ) | (lv_newObject_7= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) ) ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1440:1: ( (lv_value_0= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? ) | (lv_newObject_7= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) ) )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1440:1: ( (lv_value_0= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? ) | (lv_newObject_7= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) ) )
            int alt23=4;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt23=1;
                }
                break;
            case RULE_ID:
                {
                int LA23_2 = input.LA(2);

                if ( (LA23_2==RULE_FRAGMENT) ) {
                    alt23=4;
                }
                else if ( (LA23_2==EOF||LA23_2==19||(LA23_2>=30 && LA23_2<=31)) ) {
                    alt23=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("1440:1: ( (lv_value_0= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? ) | (lv_newObject_7= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) ) )", 23, 2, input);

                    throw nvae;
                }
                }
                break;
            case 34:
            case 35:
                {
                alt23=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1440:1: ( (lv_value_0= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? ) | (lv_newObject_7= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) ) )", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1440:2: (lv_value_0= RULE_STRING )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1440:2: (lv_value_0= RULE_STRING )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1442:6: lv_value_0= RULE_STRING
                    {
                    lv_value_0=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleAssignmentValue3055); 

                    		createLeafNode(grammarAccess.getAssignmentValueAccess().getValueSTRINGTerminalRuleCall_0_0(), "value"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "value", lv_value_0, "STRING", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1461:6: ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1461:6: ( ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )? )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1461:7: ( RULE_ID ) ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )?
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1461:7: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1464:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAssignmentValue3092); 

                    		createLeafNode(grammarAccess.getAssignmentValueAccess().getRefObjectNamedObjectCrossReference_1_0_0(), "refObject"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1477:2: ( '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )? )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==19) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1477:3: '.' (lv_refFeature_3= RULE_ID ) ( '[' (lv_refIndex_5= RULE_INT ) ']' )?
                            {
                            match(input,19,FOLLOW_19_in_ruleAssignmentValue3105); 

                                    createLeafNode(grammarAccess.getAssignmentValueAccess().getFullStopKeyword_1_1_0(), null); 
                                
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1481:1: (lv_refFeature_3= RULE_ID )
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1483:6: lv_refFeature_3= RULE_ID
                            {
                            lv_refFeature_3=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAssignmentValue3127); 

                            		createLeafNode(grammarAccess.getAssignmentValueAccess().getRefFeatureIDTerminalRuleCall_1_1_1_0(), "refFeature"); 
                            	

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        
                            	        try {
                            	       		set(current, "refFeature", lv_refFeature_3, "ID", lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }

                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1501:2: ( '[' (lv_refIndex_5= RULE_INT ) ']' )?
                            int alt21=2;
                            int LA21_0 = input.LA(1);

                            if ( (LA21_0==29) ) {
                                alt21=1;
                            }
                            switch (alt21) {
                                case 1 :
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1501:3: '[' (lv_refIndex_5= RULE_INT ) ']'
                                    {
                                    match(input,29,FOLLOW_29_in_ruleAssignmentValue3145); 

                                            createLeafNode(grammarAccess.getAssignmentValueAccess().getLeftSquareBracketKeyword_1_1_2_0(), null); 
                                        
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1505:1: (lv_refIndex_5= RULE_INT )
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1507:6: lv_refIndex_5= RULE_INT
                                    {
                                    lv_refIndex_5=(Token)input.LT(1);
                                    match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleAssignmentValue3167); 

                                    		createLeafNode(grammarAccess.getAssignmentValueAccess().getRefIndexINTTerminalRuleCall_1_1_2_1_0(), "refIndex"); 
                                    	

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        
                                    	        try {
                                    	       		set(current, "refIndex", lv_refIndex_5, "INT", lastConsumedNode);
                                    	        } catch (ValueConverterException vce) {
                                    				handleValueConverterException(vce);
                                    	        }
                                    	    

                                    }

                                    match(input,31,FOLLOW_31_in_ruleAssignmentValue3184); 

                                            createLeafNode(grammarAccess.getAssignmentValueAccess().getRightSquareBracketKeyword_1_1_2_2(), null); 
                                        

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 3 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1530:6: (lv_newObject_7= ruleCreatedObject )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1530:6: (lv_newObject_7= ruleCreatedObject )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1533:6: lv_newObject_7= ruleCreatedObject
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getAssignmentValueAccess().getNewObjectCreatedObjectParserRuleCall_2_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleCreatedObject_in_ruleAssignmentValue3229);
                    lv_newObject_7=ruleCreatedObject();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "newObject", lv_newObject_7, "CreatedObject", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }
                    break;
                case 4 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1552:6: ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1552:6: ( ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1552:7: ( RULE_ID ) (lv_impFrag_9= RULE_FRAGMENT )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1552:7: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1555:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleAssignmentValue3262); 

                    		createLeafNode(grammarAccess.getAssignmentValueAccess().getImportImportCrossReference_3_0_0(), "import"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1568:2: (lv_impFrag_9= RULE_FRAGMENT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1570:6: lv_impFrag_9= RULE_FRAGMENT
                    {
                    lv_impFrag_9=(Token)input.LT(1);
                    match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleAssignmentValue3287); 

                    		createLeafNode(grammarAccess.getAssignmentValueAccess().getImpFragFRAGMENTTerminalRuleCall_3_1_0(), "impFrag"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "impFrag", lv_impFrag_9, "FRAGMENT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleAssignmentValue


    // $ANTLR start entryRuleListAssignmentValue
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1595:1: entryRuleListAssignmentValue returns [EObject current=null] : iv_ruleListAssignmentValue= ruleListAssignmentValue EOF ;
    public final EObject entryRuleListAssignmentValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleListAssignmentValue = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1595:61: (iv_ruleListAssignmentValue= ruleListAssignmentValue EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1596:2: iv_ruleListAssignmentValue= ruleListAssignmentValue EOF
            {
             currentNode = createCompositeNode(grammarAccess.getListAssignmentValueRule(), currentNode); 
            pushFollow(FOLLOW_ruleListAssignmentValue_in_entryRuleListAssignmentValue3329);
            iv_ruleListAssignmentValue=ruleListAssignmentValue();
            _fsp--;

             current =iv_ruleListAssignmentValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleListAssignmentValue3339); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleListAssignmentValue


    // $ANTLR start ruleListAssignmentValue
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1603:1: ruleListAssignmentValue returns [EObject current=null] : ( (lv_index_0= RULE_INT ) ':' ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) ) ) ;
    public final EObject ruleListAssignmentValue() throws RecognitionException {
        EObject current = null;

        Token lv_index_0=null;
        Token lv_refIndex_3=null;
        Token lv_value_5=null;
        Token lv_refFeature_8=null;
        Token lv_refIndex_10=null;
        Token lv_impFrag_14=null;
        EObject lv_newObject_12 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1608:6: ( ( (lv_index_0= RULE_INT ) ':' ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) ) ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1609:1: ( (lv_index_0= RULE_INT ) ':' ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) ) )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1609:1: ( (lv_index_0= RULE_INT ) ':' ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1609:2: (lv_index_0= RULE_INT ) ':' ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1609:2: (lv_index_0= RULE_INT )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1611:6: lv_index_0= RULE_INT
            {
            lv_index_0=(Token)input.LT(1);
            match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleListAssignmentValue3386); 

            		createLeafNode(grammarAccess.getListAssignmentValueAccess().getIndexINTTerminalRuleCall_0_0(), "index"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "index", lv_index_0, "INT", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            match(input,32,FOLLOW_32_in_ruleListAssignmentValue3403); 

                    createLeafNode(grammarAccess.getListAssignmentValueAccess().getColonKeyword_1(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1633:1: ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) )
            int alt26=5;
            switch ( input.LA(1) ) {
            case 29:
                {
                alt26=1;
                }
                break;
            case RULE_STRING:
                {
                alt26=2;
                }
                break;
            case RULE_ID:
                {
                int LA26_3 = input.LA(2);

                if ( (LA26_3==RULE_FRAGMENT) ) {
                    alt26=5;
                }
                else if ( (LA26_3==EOF||LA26_3==19||LA26_3==28||(LA26_3>=30 && LA26_3<=31)) ) {
                    alt26=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("1633:1: ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) )", 26, 3, input);

                    throw nvae;
                }
                }
                break;
            case 34:
            case 35:
                {
                alt26=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1633:1: ( ( '[' (lv_refIndex_3= RULE_INT ) ']' ) | (lv_value_5= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? ) | (lv_newObject_12= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) ) )", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1633:2: ( '[' (lv_refIndex_3= RULE_INT ) ']' )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1633:2: ( '[' (lv_refIndex_3= RULE_INT ) ']' )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1633:3: '[' (lv_refIndex_3= RULE_INT ) ']'
                    {
                    match(input,29,FOLLOW_29_in_ruleListAssignmentValue3414); 

                            createLeafNode(grammarAccess.getListAssignmentValueAccess().getLeftSquareBracketKeyword_2_0_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1637:1: (lv_refIndex_3= RULE_INT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1639:6: lv_refIndex_3= RULE_INT
                    {
                    lv_refIndex_3=(Token)input.LT(1);
                    match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleListAssignmentValue3436); 

                    		createLeafNode(grammarAccess.getListAssignmentValueAccess().getRefIndexINTTerminalRuleCall_2_0_1_0(), "refIndex"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "refIndex", lv_refIndex_3, "INT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }

                    match(input,31,FOLLOW_31_in_ruleListAssignmentValue3453); 

                            createLeafNode(grammarAccess.getListAssignmentValueAccess().getRightSquareBracketKeyword_2_0_2(), null); 
                        

                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1662:6: (lv_value_5= RULE_STRING )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1662:6: (lv_value_5= RULE_STRING )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1664:6: lv_value_5= RULE_STRING
                    {
                    lv_value_5=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleListAssignmentValue3482); 

                    		createLeafNode(grammarAccess.getListAssignmentValueAccess().getValueSTRINGTerminalRuleCall_2_1_0(), "value"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "value", lv_value_5, "STRING", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;
                case 3 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1683:6: ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1683:6: ( ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )? )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1683:7: ( RULE_ID ) ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )?
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1683:7: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1686:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleListAssignmentValue3519); 

                    		createLeafNode(grammarAccess.getListAssignmentValueAccess().getRefObjectNamedObjectCrossReference_2_2_0_0(), "refObject"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1699:2: ( '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )? )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==19) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1699:3: '.' (lv_refFeature_8= RULE_ID ) ( '[' (lv_refIndex_10= RULE_INT ) ']' )?
                            {
                            match(input,19,FOLLOW_19_in_ruleListAssignmentValue3532); 

                                    createLeafNode(grammarAccess.getListAssignmentValueAccess().getFullStopKeyword_2_2_1_0(), null); 
                                
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1703:1: (lv_refFeature_8= RULE_ID )
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1705:6: lv_refFeature_8= RULE_ID
                            {
                            lv_refFeature_8=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleListAssignmentValue3554); 

                            		createLeafNode(grammarAccess.getListAssignmentValueAccess().getRefFeatureIDTerminalRuleCall_2_2_1_1_0(), "refFeature"); 
                            	

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        
                            	        try {
                            	       		set(current, "refFeature", lv_refFeature_8, "ID", lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }

                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1723:2: ( '[' (lv_refIndex_10= RULE_INT ) ']' )?
                            int alt24=2;
                            int LA24_0 = input.LA(1);

                            if ( (LA24_0==29) ) {
                                alt24=1;
                            }
                            switch (alt24) {
                                case 1 :
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1723:3: '[' (lv_refIndex_10= RULE_INT ) ']'
                                    {
                                    match(input,29,FOLLOW_29_in_ruleListAssignmentValue3572); 

                                            createLeafNode(grammarAccess.getListAssignmentValueAccess().getLeftSquareBracketKeyword_2_2_1_2_0(), null); 
                                        
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1727:1: (lv_refIndex_10= RULE_INT )
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1729:6: lv_refIndex_10= RULE_INT
                                    {
                                    lv_refIndex_10=(Token)input.LT(1);
                                    match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleListAssignmentValue3594); 

                                    		createLeafNode(grammarAccess.getListAssignmentValueAccess().getRefIndexINTTerminalRuleCall_2_2_1_2_1_0(), "refIndex"); 
                                    	

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        
                                    	        try {
                                    	       		set(current, "refIndex", lv_refIndex_10, "INT", lastConsumedNode);
                                    	        } catch (ValueConverterException vce) {
                                    				handleValueConverterException(vce);
                                    	        }
                                    	    

                                    }

                                    match(input,31,FOLLOW_31_in_ruleListAssignmentValue3611); 

                                            createLeafNode(grammarAccess.getListAssignmentValueAccess().getRightSquareBracketKeyword_2_2_1_2_2(), null); 
                                        

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 4 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1752:6: (lv_newObject_12= ruleCreatedObject )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1752:6: (lv_newObject_12= ruleCreatedObject )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1755:6: lv_newObject_12= ruleCreatedObject
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getListAssignmentValueAccess().getNewObjectCreatedObjectParserRuleCall_2_3_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleCreatedObject_in_ruleListAssignmentValue3656);
                    lv_newObject_12=ruleCreatedObject();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "newObject", lv_newObject_12, "CreatedObject", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }
                    break;
                case 5 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1774:6: ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1774:6: ( ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1774:7: ( RULE_ID ) (lv_impFrag_14= RULE_FRAGMENT )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1774:7: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1777:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleListAssignmentValue3689); 

                    		createLeafNode(grammarAccess.getListAssignmentValueAccess().getImportImportCrossReference_2_4_0_0(), "import"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1790:2: (lv_impFrag_14= RULE_FRAGMENT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1792:6: lv_impFrag_14= RULE_FRAGMENT
                    {
                    lv_impFrag_14=(Token)input.LT(1);
                    match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleListAssignmentValue3714); 

                    		createLeafNode(grammarAccess.getListAssignmentValueAccess().getImpFragFRAGMENTTerminalRuleCall_2_4_1_0(), "impFrag"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getListAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "impFrag", lv_impFrag_14, "FRAGMENT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleListAssignmentValue


    // $ANTLR start entryRuleSingleAssignmentValue
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1817:1: entryRuleSingleAssignmentValue returns [EObject current=null] : iv_ruleSingleAssignmentValue= ruleSingleAssignmentValue EOF ;
    public final EObject entryRuleSingleAssignmentValue() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSingleAssignmentValue = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1817:63: (iv_ruleSingleAssignmentValue= ruleSingleAssignmentValue EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1818:2: iv_ruleSingleAssignmentValue= ruleSingleAssignmentValue EOF
            {
             currentNode = createCompositeNode(grammarAccess.getSingleAssignmentValueRule(), currentNode); 
            pushFollow(FOLLOW_ruleSingleAssignmentValue_in_entryRuleSingleAssignmentValue3757);
            iv_ruleSingleAssignmentValue=ruleSingleAssignmentValue();
            _fsp--;

             current =iv_ruleSingleAssignmentValue; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleSingleAssignmentValue3767); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleSingleAssignmentValue


    // $ANTLR start ruleSingleAssignmentValue
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1825:1: ruleSingleAssignmentValue returns [EObject current=null] : ( (lv_keyword_0= 'null' ) | (lv_value_1= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? ) | (lv_newObject_8= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) ) ) ;
    public final EObject ruleSingleAssignmentValue() throws RecognitionException {
        EObject current = null;

        Token lv_keyword_0=null;
        Token lv_value_1=null;
        Token lv_refFeature_4=null;
        Token lv_refIndex_6=null;
        Token lv_impFrag_10=null;
        EObject lv_newObject_8 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1830:6: ( ( (lv_keyword_0= 'null' ) | (lv_value_1= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? ) | (lv_newObject_8= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) ) ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1831:1: ( (lv_keyword_0= 'null' ) | (lv_value_1= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? ) | (lv_newObject_8= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) ) )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1831:1: ( (lv_keyword_0= 'null' ) | (lv_value_1= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? ) | (lv_newObject_8= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) ) )
            int alt29=5;
            switch ( input.LA(1) ) {
            case 33:
                {
                alt29=1;
                }
                break;
            case RULE_STRING:
                {
                alt29=2;
                }
                break;
            case RULE_ID:
                {
                int LA29_3 = input.LA(2);

                if ( (LA29_3==RULE_FRAGMENT) ) {
                    alt29=5;
                }
                else if ( (LA29_3==EOF||LA29_3==19||LA29_3==24||LA29_3==28) ) {
                    alt29=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("1831:1: ( (lv_keyword_0= 'null' ) | (lv_value_1= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? ) | (lv_newObject_8= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) ) )", 29, 3, input);

                    throw nvae;
                }
                }
                break;
            case 34:
            case 35:
                {
                alt29=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1831:1: ( (lv_keyword_0= 'null' ) | (lv_value_1= RULE_STRING ) | ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? ) | (lv_newObject_8= ruleCreatedObject ) | ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) ) )", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1831:2: (lv_keyword_0= 'null' )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1831:2: (lv_keyword_0= 'null' )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1833:6: lv_keyword_0= 'null'
                    {
                    lv_keyword_0=(Token)input.LT(1);
                    match(input,33,FOLLOW_33_in_ruleSingleAssignmentValue3813); 

                            createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getKeywordNullKeyword_0_0(), "keyword"); 
                        

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "keyword", /* lv_keyword_0 */ input.LT(-1), "null", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;
                case 2 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1853:6: (lv_value_1= RULE_STRING )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1853:6: (lv_value_1= RULE_STRING )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1855:6: lv_value_1= RULE_STRING
                    {
                    lv_value_1=(Token)input.LT(1);
                    match(input,RULE_STRING,FOLLOW_RULE_STRING_in_ruleSingleAssignmentValue3854); 

                    		createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getValueSTRINGTerminalRuleCall_1_0(), "value"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "value", lv_value_1, "STRING", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }
                    break;
                case 3 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1874:6: ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1874:6: ( ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )? )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1874:7: ( RULE_ID ) ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )?
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1874:7: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1877:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSingleAssignmentValue3891); 

                    		createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getRefObjectNamedObjectCrossReference_2_0_0(), "refObject"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1890:2: ( '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )? )?
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( (LA28_0==19) ) {
                        alt28=1;
                    }
                    switch (alt28) {
                        case 1 :
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1890:3: '.' (lv_refFeature_4= RULE_ID ) ( '[' (lv_refIndex_6= RULE_INT ) ']' )?
                            {
                            match(input,19,FOLLOW_19_in_ruleSingleAssignmentValue3904); 

                                    createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getFullStopKeyword_2_1_0(), null); 
                                
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1894:1: (lv_refFeature_4= RULE_ID )
                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1896:6: lv_refFeature_4= RULE_ID
                            {
                            lv_refFeature_4=(Token)input.LT(1);
                            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSingleAssignmentValue3926); 

                            		createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getRefFeatureIDTerminalRuleCall_2_1_1_0(), "refFeature"); 
                            	

                            	        if (current==null) {
                            	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                            	            associateNodeWithAstElement(currentNode, current);
                            	        }
                            	        
                            	        try {
                            	       		set(current, "refFeature", lv_refFeature_4, "ID", lastConsumedNode);
                            	        } catch (ValueConverterException vce) {
                            				handleValueConverterException(vce);
                            	        }
                            	    

                            }

                            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1914:2: ( '[' (lv_refIndex_6= RULE_INT ) ']' )?
                            int alt27=2;
                            int LA27_0 = input.LA(1);

                            if ( (LA27_0==29) ) {
                                alt27=1;
                            }
                            switch (alt27) {
                                case 1 :
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1914:3: '[' (lv_refIndex_6= RULE_INT ) ']'
                                    {
                                    match(input,29,FOLLOW_29_in_ruleSingleAssignmentValue3944); 

                                            createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getLeftSquareBracketKeyword_2_1_2_0(), null); 
                                        
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1918:1: (lv_refIndex_6= RULE_INT )
                                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1920:6: lv_refIndex_6= RULE_INT
                                    {
                                    lv_refIndex_6=(Token)input.LT(1);
                                    match(input,RULE_INT,FOLLOW_RULE_INT_in_ruleSingleAssignmentValue3966); 

                                    		createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getRefIndexINTTerminalRuleCall_2_1_2_1_0(), "refIndex"); 
                                    	

                                    	        if (current==null) {
                                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                                    	            associateNodeWithAstElement(currentNode, current);
                                    	        }
                                    	        
                                    	        try {
                                    	       		set(current, "refIndex", lv_refIndex_6, "INT", lastConsumedNode);
                                    	        } catch (ValueConverterException vce) {
                                    				handleValueConverterException(vce);
                                    	        }
                                    	    

                                    }

                                    match(input,31,FOLLOW_31_in_ruleSingleAssignmentValue3983); 

                                            createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getRightSquareBracketKeyword_2_1_2_2(), null); 
                                        

                                    }
                                    break;

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 4 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1943:6: (lv_newObject_8= ruleCreatedObject )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1943:6: (lv_newObject_8= ruleCreatedObject )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1946:6: lv_newObject_8= ruleCreatedObject
                    {
                     
                    	        currentNode=createCompositeNode(grammarAccess.getSingleAssignmentValueAccess().getNewObjectCreatedObjectParserRuleCall_3_0(), currentNode); 
                    	    
                    pushFollow(FOLLOW_ruleCreatedObject_in_ruleSingleAssignmentValue4028);
                    lv_newObject_8=ruleCreatedObject();
                    _fsp--;


                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "newObject", lv_newObject_8, "CreatedObject", currentNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	        currentNode = currentNode.getParent();
                    	    

                    }


                    }
                    break;
                case 5 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1965:6: ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1965:6: ( ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT ) )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1965:7: ( RULE_ID ) (lv_impFrag_10= RULE_FRAGMENT )
                    {
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1965:7: ( RULE_ID )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1968:3: RULE_ID
                    {

                    			if (current==null) {
                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                            
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleSingleAssignmentValue4061); 

                    		createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getImportImportCrossReference_4_0_0(), "import"); 
                    	

                    }

                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1981:2: (lv_impFrag_10= RULE_FRAGMENT )
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:1983:6: lv_impFrag_10= RULE_FRAGMENT
                    {
                    lv_impFrag_10=(Token)input.LT(1);
                    match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleSingleAssignmentValue4086); 

                    		createLeafNode(grammarAccess.getSingleAssignmentValueAccess().getImpFragFRAGMENTTerminalRuleCall_4_1_0(), "impFrag"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getSingleAssignmentValueRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "impFrag", lv_impFrag_10, "FRAGMENT", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }


                    }


                    }
                    break;

            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleSingleAssignmentValue


    // $ANTLR start entryRuleObjectNew
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2008:1: entryRuleObjectNew returns [EObject current=null] : iv_ruleObjectNew= ruleObjectNew EOF ;
    public final EObject entryRuleObjectNew() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectNew = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2008:51: (iv_ruleObjectNew= ruleObjectNew EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2009:2: iv_ruleObjectNew= ruleObjectNew EOF
            {
             currentNode = createCompositeNode(grammarAccess.getObjectNewRule(), currentNode); 
            pushFollow(FOLLOW_ruleObjectNew_in_entryRuleObjectNew4128);
            iv_ruleObjectNew=ruleObjectNew();
            _fsp--;

             current =iv_ruleObjectNew; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleObjectNew4138); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleObjectNew


    // $ANTLR start ruleObjectNew
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2016:1: ruleObjectNew returns [EObject current=null] : ( 'new' ( RULE_ID ) (lv_impFrag_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? ) ;
    public final EObject ruleObjectNew() throws RecognitionException {
        EObject current = null;

        Token lv_impFrag_2=null;
        Token lv_name_3=null;
        EObject lv_assignments_5 = null;

        EObject lv_assignments_6 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2021:6: ( ( 'new' ( RULE_ID ) (lv_impFrag_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2022:1: ( 'new' ( RULE_ID ) (lv_impFrag_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2022:1: ( 'new' ( RULE_ID ) (lv_impFrag_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2022:2: 'new' ( RULE_ID ) (lv_impFrag_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )?
            {
            match(input,34,FOLLOW_34_in_ruleObjectNew4172); 

                    createLeafNode(grammarAccess.getObjectNewAccess().getNewKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2026:1: ( RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2029:3: RULE_ID
            {

            			if (current==null) {
            	            current = factory.create(grammarAccess.getObjectNewRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
                    
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectNew4194); 

            		createLeafNode(grammarAccess.getObjectNewAccess().getImportImportCrossReference_1_0(), "import"); 
            	

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2042:2: (lv_impFrag_2= RULE_FRAGMENT )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2044:6: lv_impFrag_2= RULE_FRAGMENT
            {
            lv_impFrag_2=(Token)input.LT(1);
            match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleObjectNew4219); 

            		createLeafNode(grammarAccess.getObjectNewAccess().getImpFragFRAGMENTTerminalRuleCall_2_0(), "impFrag"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getObjectNewRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "impFrag", lv_impFrag_2, "FRAGMENT", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2062:2: (lv_name_3= RULE_ID )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==RULE_ID) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2064:6: lv_name_3= RULE_ID
                    {
                    lv_name_3=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectNew4249); 

                    		createLeafNode(grammarAccess.getObjectNewAccess().getNameIDTerminalRuleCall_3_0(), "name"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectNewRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "name", lv_name_3, "ID", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2082:3: ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )?
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==13) ) {
                alt32=1;
            }
            switch (alt32) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2082:4: '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}'
                    {
                    match(input,13,FOLLOW_13_in_ruleObjectNew4268); 

                            createLeafNode(grammarAccess.getObjectNewAccess().getLeftCurlyBracketKeyword_4_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2086:1: ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )*
                    loop31:
                    do {
                        int alt31=3;
                        int LA31_0 = input.LA(1);

                        if ( (LA31_0==RULE_ID) ) {
                            int LA31_2 = input.LA(2);

                            if ( (LA31_2==27) ) {
                                int LA31_3 = input.LA(3);

                                if ( (LA31_3==29) ) {
                                    alt31=2;
                                }
                                else if ( ((LA31_3>=RULE_ID && LA31_3<=RULE_STRING)||(LA31_3>=33 && LA31_3<=35)) ) {
                                    alt31=1;
                                }


                            }


                        }


                        switch (alt31) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2086:2: (lv_assignments_5= ruleMonoSingleAssignment )
                    	    {
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2086:2: (lv_assignments_5= ruleMonoSingleAssignment )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2089:6: lv_assignments_5= ruleMonoSingleAssignment
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getObjectNewAccess().getAssignmentsMonoSingleAssignmentParserRuleCall_4_1_0_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMonoSingleAssignment_in_ruleObjectNew4303);
                    	    lv_assignments_5=ruleMonoSingleAssignment();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getObjectNewRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "assignments", lv_assignments_5, "MonoSingleAssignment", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2108:6: (lv_assignments_6= ruleMonoListAssignment )
                    	    {
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2108:6: (lv_assignments_6= ruleMonoListAssignment )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2111:6: lv_assignments_6= ruleMonoListAssignment
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getObjectNewAccess().getAssignmentsMonoListAssignmentParserRuleCall_4_1_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMonoListAssignment_in_ruleObjectNew4347);
                    	    lv_assignments_6=ruleMonoListAssignment();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getObjectNewRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "assignments", lv_assignments_6, "MonoListAssignment", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop31;
                        }
                    } while (true);

                    match(input,14,FOLLOW_14_in_ruleObjectNew4362); 

                            createLeafNode(grammarAccess.getObjectNewAccess().getRightCurlyBracketKeyword_4_2(), null); 
                        

                    }
                    break;

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleObjectNew


    // $ANTLR start entryRuleObjectCopy
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2140:1: entryRuleObjectCopy returns [EObject current=null] : iv_ruleObjectCopy= ruleObjectCopy EOF ;
    public final EObject entryRuleObjectCopy() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleObjectCopy = null;


        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2140:52: (iv_ruleObjectCopy= ruleObjectCopy EOF )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2141:2: iv_ruleObjectCopy= ruleObjectCopy EOF
            {
             currentNode = createCompositeNode(grammarAccess.getObjectCopyRule(), currentNode); 
            pushFollow(FOLLOW_ruleObjectCopy_in_entryRuleObjectCopy4397);
            iv_ruleObjectCopy=ruleObjectCopy();
            _fsp--;

             current =iv_ruleObjectCopy; 
            match(input,EOF,FOLLOW_EOF_in_entryRuleObjectCopy4407); 

            }

        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end entryRuleObjectCopy


    // $ANTLR start ruleObjectCopy
    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2148:1: ruleObjectCopy returns [EObject current=null] : ( 'copy' ( RULE_ID ) (lv_fragment_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? ) ;
    public final EObject ruleObjectCopy() throws RecognitionException {
        EObject current = null;

        Token lv_fragment_2=null;
        Token lv_name_3=null;
        EObject lv_assignments_5 = null;

        EObject lv_assignments_6 = null;


         EObject temp=null; setCurrentLookahead(); resetLookahead(); 
            
        try {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2153:6: ( ( 'copy' ( RULE_ID ) (lv_fragment_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? ) )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2154:1: ( 'copy' ( RULE_ID ) (lv_fragment_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? )
            {
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2154:1: ( 'copy' ( RULE_ID ) (lv_fragment_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )? )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2154:2: 'copy' ( RULE_ID ) (lv_fragment_2= RULE_FRAGMENT ) (lv_name_3= RULE_ID )? ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )?
            {
            match(input,35,FOLLOW_35_in_ruleObjectCopy4441); 

                    createLeafNode(grammarAccess.getObjectCopyAccess().getCopyKeyword_0(), null); 
                
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2158:1: ( RULE_ID )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2161:3: RULE_ID
            {

            			if (current==null) {
            	            current = factory.create(grammarAccess.getObjectCopyRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
                    
            match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectCopy4463); 

            		createLeafNode(grammarAccess.getObjectCopyAccess().getResourceNamedResourceCrossReference_1_0(), "resource"); 
            	

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2174:2: (lv_fragment_2= RULE_FRAGMENT )
            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2176:6: lv_fragment_2= RULE_FRAGMENT
            {
            lv_fragment_2=(Token)input.LT(1);
            match(input,RULE_FRAGMENT,FOLLOW_RULE_FRAGMENT_in_ruleObjectCopy4488); 

            		createLeafNode(grammarAccess.getObjectCopyAccess().getFragmentFRAGMENTTerminalRuleCall_2_0(), "fragment"); 
            	

            	        if (current==null) {
            	            current = factory.create(grammarAccess.getObjectCopyRule().getType().getClassifier());
            	            associateNodeWithAstElement(currentNode, current);
            	        }
            	        
            	        try {
            	       		set(current, "fragment", lv_fragment_2, "FRAGMENT", lastConsumedNode);
            	        } catch (ValueConverterException vce) {
            				handleValueConverterException(vce);
            	        }
            	    

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2194:2: (lv_name_3= RULE_ID )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==RULE_ID) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2196:6: lv_name_3= RULE_ID
                    {
                    lv_name_3=(Token)input.LT(1);
                    match(input,RULE_ID,FOLLOW_RULE_ID_in_ruleObjectCopy4518); 

                    		createLeafNode(grammarAccess.getObjectCopyAccess().getNameIDTerminalRuleCall_3_0(), "name"); 
                    	

                    	        if (current==null) {
                    	            current = factory.create(grammarAccess.getObjectCopyRule().getType().getClassifier());
                    	            associateNodeWithAstElement(currentNode, current);
                    	        }
                    	        
                    	        try {
                    	       		set(current, "name", lv_name_3, "ID", lastConsumedNode);
                    	        } catch (ValueConverterException vce) {
                    				handleValueConverterException(vce);
                    	        }
                    	    

                    }
                    break;

            }

            // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2214:3: ( '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}' )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==13) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2214:4: '{' ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )* '}'
                    {
                    match(input,13,FOLLOW_13_in_ruleObjectCopy4537); 

                            createLeafNode(grammarAccess.getObjectCopyAccess().getLeftCurlyBracketKeyword_4_0(), null); 
                        
                    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2218:1: ( (lv_assignments_5= ruleMonoSingleAssignment ) | (lv_assignments_6= ruleMonoListAssignment ) )*
                    loop34:
                    do {
                        int alt34=3;
                        int LA34_0 = input.LA(1);

                        if ( (LA34_0==RULE_ID) ) {
                            int LA34_2 = input.LA(2);

                            if ( (LA34_2==27) ) {
                                int LA34_3 = input.LA(3);

                                if ( (LA34_3==29) ) {
                                    alt34=2;
                                }
                                else if ( ((LA34_3>=RULE_ID && LA34_3<=RULE_STRING)||(LA34_3>=33 && LA34_3<=35)) ) {
                                    alt34=1;
                                }


                            }


                        }


                        switch (alt34) {
                    	case 1 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2218:2: (lv_assignments_5= ruleMonoSingleAssignment )
                    	    {
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2218:2: (lv_assignments_5= ruleMonoSingleAssignment )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2221:6: lv_assignments_5= ruleMonoSingleAssignment
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getObjectCopyAccess().getAssignmentsMonoSingleAssignmentParserRuleCall_4_1_0_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMonoSingleAssignment_in_ruleObjectCopy4572);
                    	    lv_assignments_5=ruleMonoSingleAssignment();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getObjectCopyRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "assignments", lv_assignments_5, "MonoSingleAssignment", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;
                    	case 2 :
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2240:6: (lv_assignments_6= ruleMonoListAssignment )
                    	    {
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2240:6: (lv_assignments_6= ruleMonoListAssignment )
                    	    // ../org.eclipse.emf.compare.epatch.dsl/src-gen/org/eclipse/emf/compare/epatch/dsl/parser/antlr/internal/InternalEpatch.g:2243:6: lv_assignments_6= ruleMonoListAssignment
                    	    {
                    	     
                    	    	        currentNode=createCompositeNode(grammarAccess.getObjectCopyAccess().getAssignmentsMonoListAssignmentParserRuleCall_4_1_1_0(), currentNode); 
                    	    	    
                    	    pushFollow(FOLLOW_ruleMonoListAssignment_in_ruleObjectCopy4616);
                    	    lv_assignments_6=ruleMonoListAssignment();
                    	    _fsp--;


                    	    	        if (current==null) {
                    	    	            current = factory.create(grammarAccess.getObjectCopyRule().getType().getClassifier());
                    	    	            associateNodeWithAstElement(currentNode.getParent(), current);
                    	    	        }
                    	    	        
                    	    	        try {
                    	    	       		add(current, "assignments", lv_assignments_6, "MonoListAssignment", currentNode);
                    	    	        } catch (ValueConverterException vce) {
                    	    				handleValueConverterException(vce);
                    	    	        }
                    	    	        currentNode = currentNode.getParent();
                    	    	    

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop34;
                        }
                    } while (true);

                    match(input,14,FOLLOW_14_in_ruleObjectCopy4631); 

                            createLeafNode(grammarAccess.getObjectCopyAccess().getRightCurlyBracketKeyword_4_2(), null); 
                        

                    }
                    break;

            }


            }


            }

             resetLookahead(); 
                	lastConsumedNode = currentNode;
                
        }
         
            catch (RecognitionException re) { 
                recover(input,re); 
                appendSkippedTokens();
            } 
        finally {
        }
        return current;
    }
    // $ANTLR end ruleObjectCopy


 

    public static final BitSet FOLLOW_ruleEpatch_in_entryRuleEpatch71 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEpatch81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_12_in_ruleEpatch115 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleEpatch137 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleEpatch154 = new BitSet(new long[]{0x000000000440C000L});
    public static final BitSet FOLLOW_ruleImport_in_ruleEpatch188 = new BitSet(new long[]{0x000000000440C000L});
    public static final BitSet FOLLOW_ruleNamedResource_in_ruleEpatch227 = new BitSet(new long[]{0x0000000004404000L});
    public static final BitSet FOLLOW_ruleObjectRef_in_ruleEpatch266 = new BitSet(new long[]{0x0000000004004000L});
    public static final BitSet FOLLOW_14_in_ruleEpatch280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleImport_in_entryRuleImport313 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleImport323 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelImport_in_ruleImport370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaImport_in_ruleImport404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleExtensionImport_in_ruleImport438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleModelImport_in_entryRuleModelImport477 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleModelImport487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleResourceImport_in_ruleModelImport534 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEPackageImport_in_ruleModelImport568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleResourceImport_in_entryRuleResourceImport607 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleResourceImport617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_ruleResourceImport651 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleResourceImport673 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_ruleResourceImport690 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleResourceImport712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleEPackageImport_in_entryRuleEPackageImport753 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleEPackageImport763 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_ruleEPackageImport797 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleEPackageImport819 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_ruleEPackageImport836 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleEPackageImport858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleJavaImport_in_entryRuleJavaImport899 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleJavaImport909 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_ruleJavaImport943 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_ruleJavaImport952 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleJavaImport974 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleJavaImport992 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleJavaImport1014 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_ruleExtensionImport_in_entryRuleExtensionImport1057 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleExtensionImport1067 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_ruleExtensionImport1101 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_ruleExtensionImport1110 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleExtensionImport1132 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_21_in_ruleExtensionImport1150 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleExtensionImport1172 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_ruleNamedResource_in_entryRuleNamedResource1215 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleNamedResource1225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_ruleNamedResource1259 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleNamedResource1281 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_13_in_ruleNamedResource1298 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_ruleNamedResource1307 = new BitSet(new long[]{0x0000000C00010000L});
    public static final BitSet FOLLOW_16_in_ruleNamedResource1318 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleNamedResource1340 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_ruleCreatedObject_in_ruleNamedResource1389 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleNamedResource1403 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_ruleNamedResource1412 = new BitSet(new long[]{0x0000000C00010000L});
    public static final BitSet FOLLOW_16_in_ruleNamedResource1423 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleNamedResource1445 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_ruleCreatedObject_in_ruleNamedResource1494 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleNamedResource1508 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_ruleNamedResource1517 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectRef_in_entryRuleObjectRef1552 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleObjectRef1562 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_ruleObjectRef1596 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectRef1618 = new BitSet(new long[]{0x0000000000800010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectRef1651 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleObjectRef1676 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_23_in_ruleObjectRef1701 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectRef1723 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleObjectRef1748 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_ruleObjectRef1765 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectRef1787 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleObjectRef1812 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleObjectRef1832 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleBiSingleAssignment_in_ruleObjectRef1867 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleBiListAssignment_in_ruleObjectRef1911 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleObjectRef1926 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCreatedObject_in_entryRuleCreatedObject1961 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleCreatedObject1971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectNew_in_ruleCreatedObject2018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectCopy_in_ruleCreatedObject2052 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBiSingleAssignment_in_entryRuleBiSingleAssignment2093 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBiSingleAssignment2103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleBiSingleAssignment2150 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleBiSingleAssignment2167 = new BitSet(new long[]{0x0000000E00000030L});
    public static final BitSet FOLLOW_ruleSingleAssignmentValue_in_ruleBiSingleAssignment2201 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_ruleBiSingleAssignment2214 = new BitSet(new long[]{0x0000000E00000030L});
    public static final BitSet FOLLOW_ruleSingleAssignmentValue_in_ruleBiSingleAssignment2248 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleBiSingleAssignment2261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleBiListAssignment_in_entryRuleBiListAssignment2294 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleBiListAssignment2304 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleBiListAssignment2351 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleBiListAssignment2368 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_ruleBiListAssignment2377 = new BitSet(new long[]{0x0000000010000080L});
    public static final BitSet FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2412 = new BitSet(new long[]{0x0000000050000000L});
    public static final BitSet FOLLOW_30_in_ruleBiListAssignment2426 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2460 = new BitSet(new long[]{0x0000000050000000L});
    public static final BitSet FOLLOW_28_in_ruleBiListAssignment2477 = new BitSet(new long[]{0x0000000080000080L});
    public static final BitSet FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2512 = new BitSet(new long[]{0x00000000C0000000L});
    public static final BitSet FOLLOW_30_in_ruleBiListAssignment2526 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ruleListAssignmentValue_in_ruleBiListAssignment2560 = new BitSet(new long[]{0x00000000C0000000L});
    public static final BitSet FOLLOW_31_in_ruleBiListAssignment2577 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleBiListAssignment2586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMonoSingleAssignment_in_entryRuleMonoSingleAssignment2619 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMonoSingleAssignment2629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMonoSingleAssignment2676 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleMonoSingleAssignment2693 = new BitSet(new long[]{0x0000000E00000030L});
    public static final BitSet FOLLOW_ruleSingleAssignmentValue_in_ruleMonoSingleAssignment2727 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleMonoSingleAssignment2740 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleMonoListAssignment_in_entryRuleMonoListAssignment2773 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleMonoListAssignment2783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleMonoListAssignment2830 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_ruleMonoListAssignment2847 = new BitSet(new long[]{0x0000000020000000L});
    public static final BitSet FOLLOW_29_in_ruleMonoListAssignment2856 = new BitSet(new long[]{0x0000000C80000030L});
    public static final BitSet FOLLOW_ruleAssignmentValue_in_ruleMonoListAssignment2891 = new BitSet(new long[]{0x00000000C0000000L});
    public static final BitSet FOLLOW_30_in_ruleMonoListAssignment2905 = new BitSet(new long[]{0x0000000C00000030L});
    public static final BitSet FOLLOW_ruleAssignmentValue_in_ruleMonoListAssignment2939 = new BitSet(new long[]{0x00000000C0000000L});
    public static final BitSet FOLLOW_31_in_ruleMonoListAssignment2956 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_ruleMonoListAssignment2965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleAssignmentValue_in_entryRuleAssignmentValue2998 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleAssignmentValue3008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleAssignmentValue3055 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAssignmentValue3092 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleAssignmentValue3105 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAssignmentValue3127 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_ruleAssignmentValue3145 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleAssignmentValue3167 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleAssignmentValue3184 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCreatedObject_in_ruleAssignmentValue3229 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleAssignmentValue3262 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleAssignmentValue3287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleListAssignmentValue_in_entryRuleListAssignmentValue3329 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleListAssignmentValue3339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleListAssignmentValue3386 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_ruleListAssignmentValue3403 = new BitSet(new long[]{0x0000000C20000030L});
    public static final BitSet FOLLOW_29_in_ruleListAssignmentValue3414 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleListAssignmentValue3436 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleListAssignmentValue3453 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleListAssignmentValue3482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleListAssignmentValue3519 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleListAssignmentValue3532 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleListAssignmentValue3554 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_ruleListAssignmentValue3572 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleListAssignmentValue3594 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleListAssignmentValue3611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCreatedObject_in_ruleListAssignmentValue3656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleListAssignmentValue3689 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleListAssignmentValue3714 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleSingleAssignmentValue_in_entryRuleSingleAssignmentValue3757 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleSingleAssignmentValue3767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_ruleSingleAssignmentValue3813 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_STRING_in_ruleSingleAssignmentValue3854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSingleAssignmentValue3891 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_ruleSingleAssignmentValue3904 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSingleAssignmentValue3926 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_ruleSingleAssignmentValue3944 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_RULE_INT_in_ruleSingleAssignmentValue3966 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_ruleSingleAssignmentValue3983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleCreatedObject_in_ruleSingleAssignmentValue4028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleSingleAssignmentValue4061 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleSingleAssignmentValue4086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectNew_in_entryRuleObjectNew4128 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleObjectNew4138 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_ruleObjectNew4172 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectNew4194 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleObjectNew4219 = new BitSet(new long[]{0x0000000000002012L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectNew4249 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleObjectNew4268 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleMonoSingleAssignment_in_ruleObjectNew4303 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleMonoListAssignment_in_ruleObjectNew4347 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleObjectNew4362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ruleObjectCopy_in_entryRuleObjectCopy4397 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_entryRuleObjectCopy4407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_ruleObjectCopy4441 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectCopy4463 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RULE_FRAGMENT_in_ruleObjectCopy4488 = new BitSet(new long[]{0x0000000000002012L});
    public static final BitSet FOLLOW_RULE_ID_in_ruleObjectCopy4518 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_13_in_ruleObjectCopy4537 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleMonoSingleAssignment_in_ruleObjectCopy4572 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_ruleMonoListAssignment_in_ruleObjectCopy4616 = new BitSet(new long[]{0x0000000000004010L});
    public static final BitSet FOLLOW_14_in_ruleObjectCopy4631 = new BitSet(new long[]{0x0000000000000002L});

}