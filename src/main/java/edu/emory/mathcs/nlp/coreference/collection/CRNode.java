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
package edu.emory.mathcs.nlp.coreference.collection;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.component.template.node.AbstractNLPNode;
import edu.emory.mathcs.nlp.component.template.node.FeatMap;
import edu.emory.mathcs.nlp.coreference.util.ENGrammarUtils;
import edu.emory.mathcs.nlp.coreference.util.SalienceFactor;
import edu.emory.mathcs.nlp.coreference.util.type.GNumber;
import edu.emory.mathcs.nlp.coreference.util.type.GPerson;
import edu.emory.mathcs.nlp.coreference.util.type.Gender;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CRNode extends AbstractNLPNode<CRNode>
{
	private static final long serialVersionUID = -7214014769243897298L;
	
	private Gender           gender;
    private GNumber          number;
    private GPerson          person;
    private int              scene_id;
    private int              utterance_id;
    private int              sentence_id;
    private double           static_salient_weight;
    private double           dynamic_salience_weight;
    private SalienceFactor   salience_factor;
    private Equivalence equivalence_class;

//	============================== Constructors ==============================
    
    public CRNode()
    {
        super();
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(Gender gender, GNumber number, GPerson person)
    {
        super();
        init(gender, number, person);
    }

    public CRNode(int id, String form)
    {
        super(id, form);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(int id, String form, String posTag)
    {
        super(id, form, posTag);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(int id, String form, String lemma, String posTag, FeatMap feats)
    {
        super(id, form, lemma, posTag, feats);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(int id, String form, String lemma, String posTag, String namentTag, FeatMap feats)
    {
        super(id, form, lemma, posTag, namentTag, feats);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(int id, String form, String lemma, String posTag, FeatMap feats, CRNode dhead, String deprel)
    {
        super(id, form, lemma, posTag, feats, dhead, deprel);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(int id, String form, String lemma, String posTag, String namentTag, FeatMap feats, CRNode dhead, String deprel)
    {
        super(id, form, lemma, posTag, feats, dhead, deprel);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(int id, String form, String lemma, String posTag, String namentTag, String answerTag, FeatMap feats, CRNode dhead, String deprel)
    {
        super(id, form, lemma, posTag, namentTag, feats, dhead, deprel);
        init(Gender.NEUTER, GNumber.SINGULAR, GPerson.THIRD);
    }

    public CRNode(Gender gender, GNumber number, GPerson person, int id, String form, String lemma, String posTag, String namentTag, String answerTag, FeatMap feats, CRNode dhead, String deprel)
    {
        super(id, form, lemma, posTag, namentTag, feats, dhead, deprel);
        init(gender, number, person);
    }

    public void init(Gender gender, GNumber number, GPerson person)
    {
    	setGender(gender);
    	setNumber(number);
    	setPerson(person);
    	setSceneID(-1);
    	setUtteranceID(-1);
    	setSentenceID(-1);
    	setStaticSalienceWeight(0);
    	setDynamicSalienceWeight(0);
    	setSalienceFactor(new SalienceFactor());
    	setEquivalenceClass(null);
    }

//	============================== Setters ==============================

    public void setStaticSalienceWeight(double weight)
    {
       static_salient_weight = weight;
    }

    public void setDynamicSalienceWeight(double weight)
    {
        dynamic_salience_weight = weight;
    }
    
    // TODO: make sure if static is necessary
    public void setSalienceWeight(double weight)
    {
    	setStaticSalienceWeight(weight);
    	setDynamicSalienceWeight(weight);
    }

    // TODO: make sure if static is necessary
    public void increaseSalienceWeight(double weight)
    {
        static_salient_weight   += weight;
        dynamic_salience_weight += weight;
    }
    
    public void multipleDynamicSalienceWeight(double d)
    {
    	dynamic_salience_weight *= d;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public void setNumber(GNumber number)
    {
        this.number = number;
    }

    public void setPerson(GPerson person)
    {
        this.person = person;
    }

    public void setSceneID(int id)
    {
        scene_id = id;
    }

    public void setUtteranceID(int id)
    {
        utterance_id = id;
    }

    public void setSentenceID(int id)
    {
        sentence_id = id;
    }

    public void setEquivalenceClass(Equivalence eclass)
    {
        equivalence_class = eclass;
    }
    
    public void setSalienceFactor(SalienceFactor sf)
    {
    	salience_factor = sf;
    }

//	============================== Getters ==============================

    public double getStaticSalienceWeight()
    {
        return static_salient_weight;
    }

    public double getDynamicSalienceWeight()
    {
        return dynamic_salience_weight;
    }

    public Gender getGender()
    {
        return gender;
    }

    public GNumber getNumber()
    {
        return number;
    }

    public GPerson getPerson()
    {
        return person;
    }
    
    public int getSceneID()
    {
        return scene_id;
    }
    
    public int getUtternaceID()
    {
        return utterance_id;
    }

    public int getSentenceID()
    {
        return sentence_id;
    }

    public Equivalence getEquivalenceClass()
    {
        return equivalence_class;
    }

    public SalienceFactor getSalienceFactor()
    {
        return salience_factor;
    }

    public boolean isCompound() {
        return ENGrammarUtils.containsCompound(this);
    }

    public List<CRNode> getCompounds() {
        return this.getDependentList().stream().filter(dep -> dep.getDependencyLabel().equals("compound")).collect(Collectors.toList());
    }
    
//	============================== Helpers ==============================

    public boolean agree(CRNode node)
    {
    	return gender == node.gender && number == node.number && person == node.person;
    }
    
    @Override
    public CRNode self()
    {
        return this;
    }

    @Override
    public int compareTo(CRNode node)
    {
    	int scene_diff = scene_id - node.scene_id;
    	
    	if (scene_diff == 0)
    	{
    		int utt_diff = utterance_id - node.utterance_id;
    		
    		if (utt_diff == 0)
    		{
    			int sen_diff = sentence_id - node.sentence_id;
    			return sen_diff == 0 ? super.compareTo(node) : sen_diff;
    		}
    		
    		return utt_diff;
    	}
    	
    	return scene_diff;
    }

    @Override
    public String toString()
    {
    	StringJoiner join = new StringJoiner("\t");
    	
    	join.add(Integer.toString(scene_id));
    	join.add(Integer.toString(utterance_id));
    	join.add(Integer.toString(sentence_id));
        join.add(gender.toString());
        join.add(person.toString());
        join.add(number.toString());
    	join.add(super.toString());
    	join.add(Double.toString(static_salient_weight));

    	return join.toString();
    }
}
