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

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SalienceFactor implements Serializable
{
	private static final long serialVersionUID = 5026617494358742371L;

	public static final int IDX_SUBJECT       = 0;
	public static final int IDX_EXISTENTIAL   = 1;
	public static final int IDX_ACCUSATIVE    = 2;
	public static final int IDX_DATIVE        = 3;
	public static final int IDX_OBLIQUE       = 4;
	public static final int IDX_HEAD_NOUN     = 5;
	public static final int IDX_NON_ADVERBIAL = 6;
	
	private double[] weights;

	public SalienceFactor()
	{
		weights = new double[7];
	}
	
	public SalienceFactor(double subject, double existential, double accusative, double dative, double oblique, double headNoun, double nonAdverbial)
	{
		init(subject, existential, accusative, dative, oblique, headNoun, nonAdverbial);
	}
	
	public void init(double subject, double existential, double accusative, double dative, double oblique, double headNoun, double nonAdverbial)
	{
		weights = new double[7];
		setSubject(subject);
		setExistential(existential);
		setAccusative(accusative);
		setDative(dative);
		setOblique(oblique);
		setHeadNoun(headNoun);
		setNonAdverbial(nonAdverbial);
	}
	
//	============================== Getters ==============================
	
	public double getSubject()
	{
		return weights[IDX_SUBJECT];
	}
	
	public double getExistential()
	{
		return weights[IDX_EXISTENTIAL];
	}
	
	public double getAccusative()
	{
		return weights[IDX_ACCUSATIVE];
	}
	
	public double getDative()
	{
		return weights[IDX_DATIVE];
	}
	
	public double getOblique()
	{
		return weights[IDX_OBLIQUE];
	}
	
	public double getHeadNoun()
	{
		return weights[IDX_HEAD_NOUN];
	}
	
	public double getNonAdverbial()
	{
		return weights[IDX_NON_ADVERBIAL];
	}
	
//	============================== Setters ==============================
	
	public void setSubject(double subject)
	{
		weights[IDX_SUBJECT] = subject;
	}
	
	public void setExistential(double existential)
	{
		weights[IDX_EXISTENTIAL] = existential;
	}
	
	public void setAccusative(double accusative)
	{
		weights[IDX_ACCUSATIVE] = accusative;
	}
	
	public void setDative(double dative)
	{
		weights[IDX_DATIVE] = dative;
	}
	
	public void setOblique(double oblique)
	{
		weights[IDX_OBLIQUE] = oblique;
	}
	
	public void setHeadNoun(double headNoun)
	{
		weights[IDX_HEAD_NOUN] = headNoun;
	}
	
	public void setNonAdverbial(double nonAdverbial)
	{
		weights[IDX_NON_ADVERBIAL] = nonAdverbial;
	}
	
	public void set(SalienceFactor sf)
	{
		System.arraycopy(sf.weights, 0, weights, 0, weights.length);
	}

	public void union(SalienceFactor sf) {
		for (int i = 0; i < weights.length; i++)
			if (weights[i] == 0.0) weights[i] = sf.weights[i];
	}
	
//	============================== Helpers ==============================

	public double getTotalWeight()
	{
		return Arrays.stream(weights).sum();
	}
}
