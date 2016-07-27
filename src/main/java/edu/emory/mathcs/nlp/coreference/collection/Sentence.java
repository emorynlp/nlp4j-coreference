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

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Sentence implements Serializable
{
	private static final long serialVersionUID = -3222545013624017050L;
	private CRNode[] nodes;
    private int id;

    public Sentence() {}

    public Sentence(int id, CRNode[] nodes)
    {
    	setID(id);
    	setNodes(nodes);
    }

//	============================== Setters ==============================

    public void setNodes(CRNode[] nodes)
    {
        this.nodes = nodes;
    }
    
    public void setID(int id)
    {
    	this.id = id;
    }

//	============================== Getters ==============================

    public CRNode[] getNodes()
    {
        return nodes;
    }
    
    public CRNode getNode(int id)
    {
        return nodes[id];
    }

    public int getID()
    {
        return id;
    }
}
