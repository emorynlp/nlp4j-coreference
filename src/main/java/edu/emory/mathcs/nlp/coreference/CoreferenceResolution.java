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
package edu.emory.mathcs.nlp.coreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.component.template.NLPComponent;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNodePair;
import edu.emory.mathcs.nlp.coreference.collection.Equivalence;
import edu.emory.mathcs.nlp.coreference.util.CRUtils;
import edu.emory.mathcs.nlp.coreference.util.ENGrammarUtils;
import edu.emory.mathcs.nlp.coreference.util.LLUtils;
import edu.emory.mathcs.nlp.coreference.util.SalienceConstant;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CoreferenceResolution implements NLPComponent<CRNode>, Serializable
{
	private static final long serialVersionUID = 4385016199630276557L;
	private List<CRNode>       mention_list;
    private Set<CRNode>        discourse_referents;
    private Map<CRNode,CRNode> all_coreferents;
    private MentionDetector    mention_detector;
    private SalienceConstant   salience_constant;

    public CoreferenceResolution()
    {
        mention_list = new ArrayList<>();
        discourse_referents = new HashSet<>();
        all_coreferents = new HashMap<>();
        mention_detector = new MentionDetector();
        salience_constant = new SalienceConstant();
    }
    
    @Override
	public void process(List<CRNode[]> document)
	{
		for (CRNode[] nodes : document)
			process(nodes);
	}
    
    @Override
	public void process(CRNode[] nodes)
    {
    	List<CRNode> curr_np = mention_detector.getMentions(nodes);
    	LLUtils.measureSalience(curr_np, salience_constant);
        if (!mention_list.isEmpty()) LLUtils.degradeSalienceInPreviousNodes(mention_list, salience_constant);

        List<CRNode> curr_refl = curr_np.stream().filter(ENGrammarUtils::isReflexive).collect(Collectors.toList());
        curr_np.removeAll(curr_refl);
        List<CRNode> curr_prp = curr_np.stream().filter(ENGrammarUtils::isPronoun).collect(Collectors.toList());
        mention_list.addAll(curr_np);

        List<CRNodePair> nonrefl_coreferents = findReferentialPairs(curr_prp);
        List<CRNodePair> refl_coreferents    = bindReflexivePairs(curr_refl);

//        System.out.println("Document Number: " + sentence.getID());
//        System.out.println("Current Noun Phrases: " + curr_np);
//        System.out.println("All Noun Phrases: " + mention_list);
//        System.out.println("Current Pronouns: " + curr_prp);
//        System.out.println("Non-Lexical Coreferents: " + nonrefl_coreferents);
//        System.out.println("Current Lexical Anaphors: " + curr_refl);
//        System.out.println("Lexical Anaphor Coreferents: " + refl_coreferents);
//        // DebugUtils.printFeatures(nonlex_coreferents);
//        System.out.println();

        Map<CRNode, CRNode> nonlex_map = nonrefl_coreferents.stream().collect(Collectors.toMap(CRNodePair::getFirstNode, CRNodePair::getSecondNode));
        Map<CRNode, CRNode> lexana_map = refl_coreferents.stream().collect(Collectors.toMap(CRNodePair::getFirstNode, CRNodePair::getSecondNode));
        all_coreferents.putAll(nonlex_map);
        all_coreferents.putAll(lexana_map);
    }
    
    // inefficient - needs to be fixed
    public List<CRNodePair> findReferentialPairs(List<CRNode> curr_prp)
    {
        List<CRNodePair> coreferents = new ArrayList<>();

        for (CRNode prp : curr_prp)
        {
            List<CRNodePair> possible_pairs = new ArrayList<>();

            for (CRNode np : mention_list)
            {
                if (prp == np) continue;

                if (LLUtils.isNonCoreferential(prp, np))
                {
                    createEquivalenceClass(np);
                    createEquivalenceClass(prp);
                }
                else
                {
                    possible_pairs.add(new CRNodePair(prp, np));
                    createEquivalenceClass(np).addReferent(prp);
                }
            }

            if (!possible_pairs.isEmpty())
            {
                CRNodePair coreferent = CRUtils.max(possible_pairs, salience_constant);
                coreferents.add(coreferent);
            }
        }

        return coreferents;
    }

    public List<CRNodePair> bindReflexivePairs(List<CRNode> curr_lexana)
    {
        List<CRNodePair> coreferents = new ArrayList<>();

        for (CRNode lex_ana : curr_lexana)
        {
            List<CRNodePair> possible_pairs = new ArrayList<>();

            for (CRNode np : mention_list)
                if (LLUtils.isBinding(lex_ana, np)) possible_pairs.add(new CRNodePair(lex_ana, np));

            if (!possible_pairs.isEmpty())
            {
                CRNodePair coreferent = CRUtils.max(possible_pairs, salience_constant);
                coreferents.add(coreferent);
            }
        }

        return coreferents;
    }

    public Equivalence createEquivalenceClass(CRNode ante)
    {
        if (ante.getEquivalenceClass() != null) return ante.getEquivalenceClass();
        discourse_referents.add(ante);
        return new Equivalence(ante);
    }
}