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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Splitter;
import edu.emory.mathcs.nlp.component.template.node.AbstractNLPNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.util.ENGrammarUtils;
import edu.emory.mathcs.nlp.coreference.util.type.GNumber;
import edu.emory.mathcs.nlp.coreference.util.type.GPerson;
import edu.emory.mathcs.nlp.coreference.util.type.Gender;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MentionDetector implements Serializable
{
	private static final long serialVersionUID = 7472382832644112810L;
	private Set<String> m_nouns;
	private Set<String> f_nouns;
	private Set<String> m_names;
	private Set<String> f_names;
	
	public MentionDetector()
	{
		m_nouns = DSUtils.createStringHashSet(IOUtils.createFileInputStream("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/main/resources/edu/emory/mathcs/nlp/coreference/masculine_nouns.txt"));
		f_nouns = DSUtils.createStringHashSet(IOUtils.createFileInputStream("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/main/resources/edu/emory/mathcs/nlp/coreference/feminine_nouns.txt"));
		
		try
		{
			m_names = readNameFileSet("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/main/resources/edu/emory/mathcs/nlp/coreference/male_names.txt");
			f_names = readNameFileSet("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/main/resources/edu/emory/mathcs/nlp/coreference/female_names.txt");
		}
		catch (IOException e) {e.printStackTrace();}
	}

	public List<CRNode> getMentions(CRNode[] nodes) {
		List<CRNode> mentions = Arrays.stream(nodes).filter(ENGrammarUtils::isMention).collect(Collectors.toList());
		init(mentions);
		return mentions;
	}
	
	public Set<String> readNameFileSet(String filename) throws IOException
	{
	    BufferedReader reader = IOUtils.createBufferedReader(filename);
	    Set<String> set = new HashSet<>();
	    String line;
	    
	    while ((line = reader.readLine()) != null)
	    {
	        String[] str_num_split = Splitter.splitTabs(line);
	        set.add(str_num_split[0]);
	    }
	
	    reader.close();
	    return set;
	}

	public void init(List<CRNode> nodes)
	{
	    for (CRNode node : nodes)
	    {
	        String lemma = node.getLemma();
	        if (ENGrammarUtils.isPlural(node)) node.setNumber(GNumber.PLURAL);
	        
	        if      (isMasculine(node))   node.setGender(Gender.MALE);
	        else if (isFeminine(node)) node.setGender(Gender.FEMALE);
	
	        if      (ENGrammarUtils.FIRST_PERSON_PRONOUN_SET.contains(lemma))  node.setPerson(GPerson.FIRST);
	        else if (ENGrammarUtils.SECOND_PERSON_PRONOUN_SET.contains(lemma)) node.setPerson(GPerson.SECOND);
	        else if (ENGrammarUtils.THIRD_PERSON_PRONOUN_SET.contains(lemma))  node.setPerson(GPerson.THIRD);
	    }
	}

	public <Node extends AbstractNLPNode<Node>>boolean isMasculine(Node node) {
		return isMale(node) || node.getDependentList().stream().filter(n -> n.getDependencyLabel().equals("compound") && n.getDependencyHead() == node).anyMatch(dep -> isMale(dep));
	}

	public <Node extends AbstractNLPNode<Node>>boolean isFeminine(Node node) {
		return isFemale(node) || node.getDependentList().stream().filter(n -> n.getDependencyLabel().equals("compound") && n.getDependencyHead() == node).anyMatch(dep -> isFemale(dep));
	}
	
	public <Node extends AbstractNLPNode<Node>>boolean isMale(Node node)
    {
    	return m_nouns.contains(node.getLemma()) || m_names.contains(node.getWordForm());
    }
	
	public <Node extends AbstractNLPNode<Node>>boolean isFemale(Node node)
    {
    	return f_nouns.contains(node.getLemma()) || f_names.contains(node.getWordForm());	
    }
}
