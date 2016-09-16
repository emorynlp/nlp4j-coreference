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
import java.util.*;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.component.template.NLPComponent;
import edu.emory.mathcs.nlp.coreference.collection.*;
import edu.emory.mathcs.nlp.coreference.util.CRUtils;
import edu.emory.mathcs.nlp.coreference.util.ENGrammarUtils;
import edu.emory.mathcs.nlp.coreference.util.LLUtils;
import edu.emory.mathcs.nlp.coreference.util.SalienceConstant;
import edu.emory.mathcs.nlp.coreference.util.reader.CRReader;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CoreferenceResolution implements NLPComponent<CRNode>, Serializable
{
	private static final long serialVersionUID = 4385016199630276557L;
	private List<CRNode>       mention_list;
    private Set<CRNode>        discourse_referents;
    // private Map<CRNode,CRNode> all_coreferents;
    private Map<CRNodeIdentifier,CRNodeIdentifier> all_coreferents;
    private MentionDetector    mention_detector;
    private SalienceConstant   salience_constant;
    private CRReader           reader;

    public CoreferenceResolution()
    {
        init();
        mention_list = new ArrayList<>();
        discourse_referents = new HashSet<>();
        all_coreferents = new HashMap<>();
    }

    public void init() {
        initReader();
        initMentionDetector();
        initSalienceConstant();
    }

    protected void initReader() {
        reader = CRReader.DEFAULT();
    }

    protected void initMentionDetector() {
        mention_detector = new MentionDetector();
    }

    protected void initSalienceConstant() {
        salience_constant = new SalienceConstant();
    }

    public Map<CRNodeIdentifier, CRNodeIdentifier> getCoreferents() {
        return all_coreferents;
    }

    public Set<CRNode> getDiscourseReferents() {
        return discourse_referents;
    }

    public void readDocument(String filename) throws Exception {
        open(filename);
        List<CRNode[]> nodes = reader.readDocument();
        process(nodes);
        close();
    }

    public void open(String filename) {
        reader.open(IOUtils.createFileInputStream(filename));
    }

    public void close() {
        reader.close();
    }
    
    @Override
	public void process(List<CRNode[]> document)
	{
	    setSentenceIDs(document);
		for (CRNode[] nodes : document) process(nodes);
	}
    
    @Override
	public void process(CRNode[] nodes)
    {
    	List<CRNode> mentions = mention_detector.getMentions(nodes);
    	LLUtils.measureSalience(mentions, salience_constant);
        if (!mention_list.isEmpty()) LLUtils.degradeSalienceInPreviousNodes(mention_list, salience_constant);

        List<CRNode> curr_refl = mentions.stream().filter(ENGrammarUtils::isReflexive).collect(Collectors.toList());
        mentions.removeAll(curr_refl);
        List<CRNode> curr_prp = mentions.stream().filter(ENGrammarUtils::isPronoun).collect(Collectors.toList());
        mention_list.addAll(mentions);

        List<CRNodePair> nonrefl_coreferents = findCombinationPairs(curr_prp, mentions, false);
        List<CRNodePair> refl_coreferents    = findCombinationPairs(curr_refl, mentions, true);

//        System.out.println("Sentence Number: " + sentence.getID());
//        System.out.println("Current Noun Phrases: " + mentions);
//        System.out.println("All Noun Phrases: " + mention_list);
//        System.out.println("Current Pronouns: " + curr_prp);
//        System.out.println("Non-Lexical Coreferents: " + nonrefl_coreferents);
//        System.out.println("Current Lexical Anaphors: " + curr_refl);
//        System.out.println("Lexical Anaphor Coreferents: " + refl_coreferents);
//        // DebugUtils.printFeatures(nonlex_coreferents);
//        System.out.println();

        /* Map<CRNode, CRNode> nonlex_map = nonrefl_coreferents.stream().collect(Collectors.toMap(CRNodePair::getFirstNode, CRNodePair::getSecondNode));
        Map<CRNode, CRNode> lexana_map = refl_coreferents.stream().collect(Collectors.toMap(CRNodePair::getFirstNode, CRNodePair::getSecondNode));
        all_coreferents.putAll(nonlex_map);
        all_coreferents.putAll(lexana_map); */

        Map<CRNodeIdentifier, CRNodeIdentifier> nonref_map = new HashMap<>();
        Map<CRNodeIdentifier, CRNodeIdentifier> ref_map = new HashMap<>();
        for (CRNodePair node_pair : nonrefl_coreferents) nonref_map.put(new CRNodeIdentifier(node_pair.getFirstNode()), new CRNodeIdentifier(node_pair.getSecondNode()));
        for (CRNodePair node_pair : refl_coreferents) ref_map.put(new CRNodeIdentifier(node_pair.getFirstNode()), new CRNodeIdentifier(node_pair.getSecondNode()));

        all_coreferents.putAll(nonref_map);
        all_coreferents.putAll(ref_map);
    }

    public List<CRNodePair> findCombinationPairs(List<CRNode> pronouns, List<CRNode> mentions, boolean reflexive) {
        List<CRNodePair> coreferents = new ArrayList<>();

        for (CRNode pronoun : pronouns) {
            List<CRNodePair> possible_pairs = new ArrayList<>();

            for (CRNode mention : mentions) {
                if (pronoun.getEquivalenceClass() != null && mention.getEquivalenceClass() != null && pronoun.getEquivalenceClass() == mention.getEquivalenceClass()) continue;
                CRNodePair pair = (reflexive) ? checkReflexivePair(pronoun, mention) : checkNonReflexivePair(pronoun, mention);
                if (pair != null) possible_pairs.add(pair);
            }

            if (!possible_pairs.isEmpty()) {
                CRNodePair max_np = CRUtils.max(possible_pairs, salience_constant);
                if (ENGrammarUtils.isCataphoric(max_np.getFirstNode(), max_np.getSecondNode())) createEquivalenceClass(max_np.getSecondNode()).addReferent(max_np.getFirstNode());
                else createEquivalenceClass(max_np.getFirstNode()).addReferent(max_np.getSecondNode());
                coreferents.add(max_np);
            } else createEquivalenceClass(pronoun);
        }

        return coreferents;
    }

    public CRNodePair checkNonReflexivePair(CRNode noreflex, CRNode mention) {
        return (noreflex == mention) ? null : ((!LLUtils.isNonCoreferential(noreflex, mention)) ?  new CRNodePair(noreflex, mention) : null);
    }

    public CRNodePair checkReflexivePair(CRNode reflex, CRNode mention) {
        return (LLUtils.isBinding(reflex, mention)) ? new CRNodePair(reflex, mention) : null;
    }

    public Equivalence createEquivalenceClass(CRNode ante)
    {
        if (ante.getEquivalenceClass() != null) return ante.getEquivalenceClass();
        discourse_referents.add(ante);
        return new Equivalence(ante);
    }

    public void setSentenceIDs(List<CRNode[]> document) {
        for (int i = 0; i < document.size(); i++) setSentenceID(document.get(i), i);
    }

    public void setSentenceID(CRNode[] nodes, int sent_id) {
        Arrays.stream(nodes).forEach(n -> n.setSentenceID(sent_id));
    }
}