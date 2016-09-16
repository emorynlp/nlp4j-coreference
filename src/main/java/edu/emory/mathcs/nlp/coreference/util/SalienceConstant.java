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

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SalienceConstant extends SalienceFactor
{
	private static final long serialVersionUID = 3008989399098762853L;
	private double sentence_recency;
	private double parallel;
	private double cataphora;
	private double degrade;

	public SalienceConstant()
	{
		this(100, 35, 175, 80, 70, 50, 40, 40, 80, 50, 0.5);
	}
	
	public SalienceConstant(double sentenceRecency, double parallel, double cataphora, double subject, double existential, double accusative, double dative, double oblique, double headNoun, double nonAdverbial, double degrade)
	{
		super(subject, existential, accusative, dative, oblique, headNoun, nonAdverbial);
		setSentenceRecency(sentenceRecency);
		setParallel(parallel);
		setCataphora(cataphora);
		setDegrade(degrade);
	}
	
	public double getSentenceRecency()
	{
		return sentence_recency;
	}

	public double getParallel()
	{
		return parallel;
	}

	public double getCataphora()
	{
		return cataphora;
	}
	
	public double getDegrade()
	{
		return degrade;
	}
	
	public void setSentenceRecency(double sentenceRecency)
	{
		this.sentence_recency = sentenceRecency;
	}

	public void setParallel(double parallel)
	{
		this.parallel = parallel;
	}

	public void setCataphora(double cataphora)
	{
		this.cataphora = cataphora;
	}

	public void setDegrade(double degrade)
	{
		this.degrade = degrade;
	}
}
