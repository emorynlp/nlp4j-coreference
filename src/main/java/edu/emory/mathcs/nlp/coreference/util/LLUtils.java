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
 
import java.util.List;
import java.util.Set;

import edu.emory.mathcs.nlp.common.treebank.DEPTagEn;
import edu.emory.mathcs.nlp.common.treebank.POSLibEn;
import edu.emory.mathcs.nlp.common.util.DSUtils;
import edu.emory.mathcs.nlp.component.template.node.AbstractNLPNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;

/**
 * Logics from Lappin and Leass, 1994.
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LLUtils
{	
//	============================== Definitions ==============================

    /**
     * @param P a nominal.
     * @param N a nominal.
     * @return {@code true} if {@code P} is in an argument domain of {@code N}.
     */
    public static <Node extends AbstractNLPNode<Node>>boolean isArgumentDomainOf(Node P, Node N)
    {
    	Node H = P.getDependencyHead();
    	
    	while (H != null && H.isDependencyLabel(DEPTagEn.DEP_XCOMP))
    		H = H.getDependencyHead();
    	
    	return N.isDependentOf(H);
    }

    /**
     * @param P a nominal whose head is a preposition.
     * @param N a nominal.
     * @return {@code true} if {@code P} is in an adjunct domain of {@code N}.
     */
    public static <Node extends AbstractNLPNode<Node>>boolean isAdjunctDomainOf(Node P, Node N)
    {
    	Node PREP = getHighestPREP(P);
    	return PREP != null && isArgumentDomainOf(PREP, N);
    }
    
    /**
     * @param P a nominal.
     * @param N a nominal that is the head of a determiner.
     * @return {@code true} if {@code P} is in an NP domain of {@code N}.
     */
    public static <Node extends AbstractNLPNode<Node>>boolean isNPDomainOf(Node P, Node N)
    {
    	return isDeterminer(N) && (isArgumentDomainOf(P, N) || isAdjunctDomainOf(P, N));
    }

    /**
     * @param P a nominal.
     * @param Q a nominal.
     * @return {@code true} if {@code P} is contained in {@code Q}.
     */
	public static <Node extends AbstractNLPNode<Node>>boolean isContainedIn(Node P, Node Q)
	{
		return isContainedInAux(P, Q);
	}
		
	private static <Node extends AbstractNLPNode<Node>>boolean isContainedInAux(Node P, Node Q)
	{
		for (Node N : Q.getDependentList())
		{
			if (N.isDependencyLabel(DEPTagEn.DEP_CONJ) && (N == P || isContainedInAux(P, N)))
				return true;
		}
		
		return false;
	}
	
//	============================== Syntactic Filter Rules ==============================

	public static boolean isNonCoreferential(CRNode P, CRNode N)
	{
		return isNonCoreferential1(P, N) || 
			   isNonCoreferential2(P, N) ||
			   isNonCoreferential3(P, N) || 
			   isNonCoreferential4(P, N) ||
			   isNonCoreferential5(P, N) ||
			   isNonCoreferential6(P, N);
	}
	
	private static boolean isNonCoreferential1(CRNode P, CRNode N)
	{
	    return P.agree(N);
	}

	private static boolean isNonCoreferential2(CRNode P, CRNode N)
	{
	    return isArgumentDomainOf(P, N);
	}

	private static boolean isNonCoreferential3(CRNode P, CRNode N)
	{
	    return isAdjunctDomainOf(P, N);
	}

	private static boolean isNonCoreferential4(CRNode P, CRNode N)
	{
		return ENGrammarUtils.isPronoun(N) ? false : isContainedIn(N, P.getDependencyHead());
	}

	private static boolean isNonCoreferential5(CRNode P, CRNode N)
	{
	    return isNPDomainOf(P, N);
	}

	private static boolean isNonCoreferential6(CRNode P, CRNode N)
	{
		return isDeterminer(P) ? isContainedIn(N, P.getDependencyHead()) : false;
	}

//	============================== Anaphor Binding Algorithm ==============================

	public static boolean isBinding(CRNode A, CRNode N)
	{
		return A.agree(N) && (isBinding1(A,N) || isBinding2(A,N) || isBinding3(A,N) || isBinding4(A,N) || isBinding5(A,N));
	}
	
	public static boolean isBinding1(CRNode A, CRNode N)
	{
		return isArgumentDomainOf(A, N) ? getArgSlot(A.getDependencyLabel()) < getArgSlot(N.getDependencyLabel()) : false;
	}

	public static boolean isBinding2(CRNode A, CRNode N)
	{
	    return isAdjunctDomainOf(A, N);
	}

	public static boolean isBinding3(CRNode A, CRNode N)
	{
	    return isNPDomainOf(A, N);
	}

	public static boolean isBinding4(CRNode A, CRNode N)
	{
		CRNode V = N.getDependencyHead();
		
		if (V != null && POSLibEn.isVerb(V.getPartOfSpeechTag()))
		{
			CRNode PREP = getHighestPREP(A);
			if (PREP != null) A = PREP;
			CRNode Q = A.getDependencyHead();
			
			if (!Q.containsDependent(null, (node,label) -> isDeterminer(node)))
				return isArgumentDomainOf(Q, N) || isAdjunctDomainOf(Q, N);
		}
		
		return false;
	}

	public static boolean isBinding5(CRNode A, CRNode N)
	{
		CRNode Q = A.getDependencyHead();
		return isDeterminer(A) && (isBinding1(Q, N) || isAdjunctDomainOf(Q, N));
	}

//	============================== Helpers ==============================

	public static <Node extends AbstractNLPNode<Node>>boolean isDeterminer(Node node)
	{
		return node.isDependencyLabel(DEPTagEn.DEP_POSS);
	}
	
	 public static <Node extends AbstractNLPNode<Node>>Node getHighestPREP(Node node)
	    {
	    	if (node.isDependencyLabel(DEPTagEn.DEP_POBJ))
	    	{
	    		Node PREP = node.getDependencyHead();
	    		Node G = PREP.getDependencyHead();
	    		
	    		while (G != null && G.isDependencyLabel(DEPTagEn.DEP_POBJ))
	    		{
	    			PREP = G.getDependencyHead();
	    			G = PREP.getDependencyHead();
	    		}
	    		
	    		return PREP;
	    	}
	    	
	    	return null;
	    }
	
	public static int getArgSlot(String label)
	{
		switch (label)
		{
		case DEPTagEn.DEP_APPOS:
		case DEPTagEn.DEP_NSUBJ:
		case DEPTagEn.DEP_NSUBJPASS: return 3;
		case DEPTagEn.DEP_AGENT:     return 2;
		case DEPTagEn.DEP_ATTR:
		case DEPTagEn.DEP_DOBJ:      return 1;
		case DEPTagEn.DEP_POBJ:      return 0;
		default: return -1;      
		}
	}
	
	public static boolean areParallel(CRNode np1, CRNode np2)
    {
        return getArgSlot(np1.getDependencyLabel()) == getArgSlot(np2.getDependencyLabel());
    }
	
	public static Set<String> NOUN_MODIFIER_SET = DSUtils.toHashSet(DEPTagEn.DEP_ACL, DEPTagEn.DEP_COMPOUND, DEPTagEn.DEP_POBJ, DEPTagEn.DEP_PREP, DEPTagEn.DEP_QUANTMOD, DEPTagEn.DEP_RELCL);
	
//	============================== Salience Weighting ==============================

	public static boolean isSubject(CRNode node)
	{
		return getArgSlot(node.getDependencyLabel()) == 3;
	}

	public static boolean isExistential(CRNode node)
	{
		return node.isDependencyLabel(DEPTagEn.DEP_EXPL);
	}

	public static boolean isAccusative(CRNode node)
	{
		return node.isDependencyLabel(DEPTagEn.DEP_DOBJ);
	}

	public static boolean isDative(CRNode node)
	{
		if (node.isDependencyLabel(DEPTagEn.DEP_DATIVE)) return true;
		CRNode G = node.getGrandDependencyHead();
		return G != null && G.isDependencyLabel(DEPTagEn.DEP_DATIVE);
	}

	public static boolean isObliqueComplement(CRNode node)
	{
		if (node.isDependencyLabel(DEPTagEn.DEP_POBJ))
		{
			CRNode G = node.getGrandDependencyHead();
			return G != null && POSLibEn.isVerb(G.getPartOfSpeechTag());
		}

		return false;
	}

	public static boolean isHeadNoun(CRNode node)
	{
		while (node.hasDependencyHead() && (NOUN_MODIFIER_SET.contains(node.getDependencyLabel())))
		{
			node = node.getDependencyHead();
			
			if (POSLibEn.isNoun(node.getPartOfSpeechTag()))
				return false;
		}
		
		return true;
	}

	public static boolean isAdverbial(CRNode node)
	{
		CRNode PREP = getHighestPREP(node);
		
		if (PREP != null)
		{
			CRNode V = PREP.getDependencyHead();
			return V != null && PREP.getID() < V.getID() && POSLibEn.isVerb(V.getPartOfSpeechTag());
		}
		
		return false;
	}

	public static void measureSalience(List<CRNode> mentions, SalienceConstant c)
	{
		if (mentions.isEmpty()) return;
	
	    for (CRNode node : mentions)
	    {
	        double inc = c.getSentenceRecency();
	
	        if (isSubject(node))
	        {
	            inc += c.getSubject();
	            node.getSalienceFactor().setSubject(c.getSubject());
	        }
	        else if (isExistential(node))
	        {
	            inc += c.getExistential();
	            node.getSalienceFactor().setExistential(c.getExistential());
	        }
	        else if (isAccusative(node))
	        {
	            inc += c.getAccusative();
	            node.getSalienceFactor().setAccusative(c.getAccusative());
	        }
	        else if (isDative(node) || isObliqueComplement(node))
	        {
	            inc += c.getDative();
	            node.getSalienceFactor().setDative(c.getDative());
	        }

	        if (isHeadNoun(node))
	        {
	            inc += c.getHeadNoun();
	            node.getSalienceFactor().setHeadNoun(c.getHeadNoun());
	        }
	        
	        if (!isAdverbial(node))
	        {
	        	inc += c.getNonAdverbial();
	            node.getSalienceFactor().setNonAdverbial(c.getNonAdverbial());
	        }
	
//	        Equivalence ec = node.getEquivalenceClass();
//	        if (ec != null) inc += ec.getSalienceTotalWeight();
	        node.setSalienceWeight(inc);
	    }
	}

	public static void degradeSalienceInPreviousNodes(List<CRNode> mentions, SalienceConstant c)
	{
		for (CRNode node : mentions) node.multipleDynamicSalienceWeight(c.getDegrade());
	}

	public static <Node extends AbstractNLPNode<Node>>boolean isCataphoric(Node ante, Node post)
	{
	    return ante.getID() > post.getID();
	}
}
