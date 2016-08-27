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
package edu.emory.mathcs.nlp.bin;

import java.util.Map;

import edu.emory.mathcs.nlp.coreference.CoreferenceResolution;
import edu.emory.mathcs.nlp.coreference.util.CRUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CoreferenceResolutionDecoder
{
    String filename;

    public CoreferenceResolutionDecoder(String filename) {
        this.filename = filename;
    }

    public void exe() throws Exception {
        CoreferenceResolution coref_res = new CoreferenceResolution();
        coref_res.readDocument(filename);
        CRUtils.writeMapToFile(coref_res.getCoreferents(), "C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/decoder/coreferents.txt");
    }

	public static void main(String[] args) throws Exception {
	    CoreferenceResolutionDecoder crd = new CoreferenceResolutionDecoder(args[0]);
        crd.exe();
    }
}
