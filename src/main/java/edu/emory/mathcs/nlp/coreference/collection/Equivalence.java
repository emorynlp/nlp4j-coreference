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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.nlp.coreference.util.SalienceFactor;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Equivalence implements Serializable
{
	private static final long serialVersionUID = -4912304894785387050L;
	
	private CRNode         antecedent;
    private List<CRNode>   referent_list;
    private SalienceFactor salience_factor;

    public Equivalence()
    {
    	this(new CRNode());
    }

    public Equivalence(CRNode antecedent)
    {
    	this(antecedent, new ArrayList<>());
    }

    public Equivalence(CRNode antecedent, List<CRNode> referents)
    {
        setSalienceFactor(new SalienceFactor());
    	setAntecedent(antecedent);
    	setReferentList(referents);
        addReferent(antecedent);
    }

//	============================== Setters ==============================
    
    public void setAntecedent(CRNode antecedent)
    {
        this.antecedent = antecedent;
    }

    public void setReferentList(List<CRNode> refs)
    {
        referent_list = refs;
    }
    
    public void setSalienceFactor(SalienceFactor sf)
    {
    	salience_factor = sf;
    }

    public void addReferent(CRNode ref)
    {
        referent_list.add(ref);
        ref.setEquivalenceClass(this);
        salience_factor.union(ref.getSalienceFactor());
    }

//	============================== Getters ==============================
    
    public CRNode getAntecedent()
    {
        return antecedent;
    }
    
    public List<CRNode> getReferentList()
    {
        return referent_list;
    }
    
    public SalienceFactor getSalienceFactor()
    {
    	return salience_factor;
    }

    public double getSalienceTotalWeight()
    {
        return salience_factor.getTotalWeight();
    }
}