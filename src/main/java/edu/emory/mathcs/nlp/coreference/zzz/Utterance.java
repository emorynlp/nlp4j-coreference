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
package edu.emory.mathcs.nlp.coreference.zzz;

import java.io.Serializable;
import java.util.List;

import edu.emory.mathcs.nlp.coreference.collection.Sentence;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Utterance implements Serializable
{
	private static final long serialVersionUID = 6256371941357111341L;
	private List<Sentence> sentences;
	private String speaker;
    private int id;

    public Utterance() {}

    public Utterance(int id, String speaker, List<Sentence> sentences)
    {
    	setID(id);
    	setSpeaker(speaker);
    	setSentences(sentences);
    }

//	============================== Setters ==============================
    
    public void setID(int id)
    {
        this.id = id;
    }

    public void setSpeaker(String speaker)
    {
        this.speaker = speaker;
    }
    
    public void setSentences(List<Sentence> sentences)
    {
    	this.sentences = sentences;
    }
    
    public void addSentence(Sentence sentence)
    {
        sentences.add(sentence);
    }

//	============================== Getters ==============================

    public int getID()
    {
    	return id;
    }
    
    public String getSpeaker()
    {
        return speaker;
    }
    
    public List<Sentence> getSentences()
    {
        return sentences;
    }

    public Sentence getSentence(int id)
    {
        return sentences.get(id);
    }
}
