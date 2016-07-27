/**
 * Copyright 2016, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.coreference.util;

import java.util.Set;

import edu.emory.mathcs.nlp.common.treebank.POSTagEn;
import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.component.template.node.AbstractNLPNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class ENGrammarUtils
{
	public static final Set<String> SINGULAR_PRONOUN_SET = DSUtils.toHashSet("I","me","my","mine","myself","you","your","yours","yourself","he","him","his","himself","she","her","hers","herself","it","its","itself");
    public static final Set<String> PLURAL_PRONOUN_SET   = DSUtils.toHashSet("we","us","our","ours","ourselves","yourselves","they","them","their","theirs","themselves");
    public static final Set<String> FIRST_PERSON_PRONOUN_SET  = DSUtils.toHashSet("I","me","my","mine","myself","we","us","our","ours","ourselves");
    public static final Set<String> SECOND_PERSON_PRONOUN_SET = DSUtils.toHashSet("you","your","yours","yourself","yourselves");
    public static final Set<String> THIRD_PERSON_PRONOUN_SET  = DSUtils.toHashSet("he","him","his","himself","she","her","hers","herself","it","its","itself","they","them","their","theirs","themselves");
    
    public static <Node extends AbstractNLPNode<Node>>boolean isPlural(Node node)
    {
    	// TODO: check "you"
    	if (isPronoun(node)) return PLURAL_PRONOUN_SET.contains(node.getLemma());
    	return node.isPartOfSpeechTag(POSTagEn.POS_NNS) || node.isPartOfSpeechTag(POSTagEn.POS_NNPS);
    }
    
    // TODO: unable to filter "other" as in "each [other]" - needs to be fixed
    public static boolean isReflexive(CRNode np)
    {
        String lemma = np.getLemma();
        return lemma.endsWith("self") || lemma.endsWith("selves");
    }

//    // inefficient - needs to be fixed
//    public static boolean isDefinite(CRNode nn_dep) throws IOException {
//        if (nn_dep.getDependencyLabel().equals("poss")) return true;
//        String pos_tag = nn_dep.getPartOfSpeechTag();
//        if (CRUtils.determinerTagSet().contains(pos_tag)) return true;
//        if (CRUtils.definiteDeterminers().contains(nn_dep.getLemma())) return true;
//
//        return false;
//    }
//
//    // inefficient - needs to be fixed
//    public static boolean isIndefinite(CRNode nn_dep) throws IOException {
//        if (nn_dep.getDependencyLabel().equals("predet")) return true;
//        String pos_tag = nn_dep.getPartOfSpeechTag();
//        if (pos_tag.equals("CD")) return true;
//        if (pos_tag.equals("PDT")) return true;
//        if (CRUtils.indefiniteDeterminers().contains(nn_dep.getLemma())) return true;
//
//        return false;
//    }

    public static <Node extends AbstractNLPNode<Node>>boolean isPronoun(Node node)
	{
		return node.getPartOfSpeechTag().startsWith(POSTagEn.POS_PRP) || node.getPartOfSpeechTag().startsWith(POSTagEn.POS_WP);
	}
	
	public static <Node extends AbstractNLPNode<Node>>boolean isNominal(Node node)
	{
		String posTag = node.getPartOfSpeechTag();
		return posTag.startsWith(POSTagEn.POS_NN) || posTag.startsWith(POSTagEn.POS_PRP) || posTag.startsWith(POSTagEn.POS_WP);
	}
}
