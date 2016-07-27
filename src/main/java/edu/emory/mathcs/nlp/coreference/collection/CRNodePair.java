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

import edu.emory.mathcs.nlp.coreference.util.LLUtils;
import edu.emory.mathcs.nlp.coreference.util.SalienceConstant;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CRNodePair
{
    private CRNode node1;
    private CRNode node2;

    public CRNodePair(CRNode node1, CRNode node2)
    {
    	setFirstNode (node1);
    	setSecondNode(node2);
    }

//	============================== Setters ==============================
    
    public void setFirstNode(CRNode node)
    {
        node1 = node;
    }

    public void setSecondNode(CRNode node)
    {
        node2 = node;
    }

//	============================== Getters ==============================
    
    public CRNode getFirstNode()
    {
        return node1;
    }

    public CRNode getSecondNode()
    {
        return node2;
    }

    public double getPairSalienceWeight(SalienceConstant c)
    {
        double weight = node2.getDynamicSalienceWeight();
        if (LLUtils.isCataphoric(node1, node2)) weight -= c.getCataphora();
        if (LLUtils.areParallel (node1, node2)) weight += c.getParallel();
        return weight;
    }

//	============================== Helpers ==============================
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(node1.toString());
        sb.append(" <---> ");
        sb.append(node2.toString());
        return sb.toString();
    }

//    public int compareTo(CRNodePair pair)
//    {
//        CRNode np1 = pair.getFirstNode();
//        CRNode np2 = pair.getSecondNode();
//
//        if (CRUtils.compare(node1, np1) && CRUtils.compare(node2, np2)) return 0;
//        if (CRUtils.compare(node1, np2) && CRUtils.compare(node2, np1)) return 0;
//
//        return 1;
//    }
}
