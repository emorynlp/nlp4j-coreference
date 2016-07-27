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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNodePair;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CRUtils {

    public static class Counter {
        private int index = 0;

        public Counter() {
        }

        public void reset() {
            index = 0;
        }

        public int increment() {
            return index++;
        }
    }

    public static CRNodePair max(List<CRNodePair> pairs, SalienceConstant c)
    {
        return Collections.max(pairs, new Comparator<CRNodePair>()
		{
			@Override
			public int compare(CRNodePair pair1, CRNodePair pair2)
			{
				return MathUtils.signum(pair1.getPairSalienceWeight(c) - pair2.getPairSalienceWeight(c));
			}
		});
    }

    public static Set<String> determinerTagSet() {
        Set<String> dset = new HashSet<>();
        dset.add("PRP$");
        dset.add("WP$");
        dset.add("WDT");
        dset.add("PDT");

        return dset;
    }

    public static <K, V> void writeMapToFile(String filename, Map<K, V> map) throws IOException {
        File outputFile = new File(filename);
        if (!outputFile.exists()) outputFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Map.Entry<K, V> entry : map.entrySet())
            writer.write(entry.getKey() + "\t--------->\t" + entry.getValue().toString() + "\n");

        writer.close();
    }


//    public static Set<String> definiteDeterminers() throws IOException {
//        return readTextFileSet(Constants.proj_path + "utils/files/def_det.txt");
//    }
//
//    public static Set<String> indefiniteDeterminers() throws IOException {
//        return readTextFileSet(Constants.proj_path + "utils/files/indef_det.txt");
//    }

    public static List<CRNode> filterNouns(List<CRNode> sentence) {
        return sentence.stream().filter(ENGrammarUtils::isNominal).collect(Collectors.toList());
    }

//    public static List<CRNode> filterDefiniteNouns(List<CRNode> nouns) throws IOException {
//        List<CRNode> def_list = new ArrayList<>();
//        for (CRNode noun : nouns)
//            if (ENGrammarUtils.isDefinite(noun)) def_list.add(noun);
//
//        return def_list;
//    }
//
//    public static List<CRNode> filterIndefiniteNouns(List<CRNode> nouns) throws IOException {
//        List<CRNode> indef_list = new ArrayList<>();
//        for (CRNode noun : nouns)
//            if (ENGrammarUtils.isIndefinite(noun)) indef_list.add(noun);
//
//        return indef_list;
//    }
}