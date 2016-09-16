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

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.MathUtils;
import edu.emory.mathcs.nlp.common.util.Splitter;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNodePair;
import edu.emory.mathcs.nlp.coreference.util.reader.CRReader;

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

    /* public static CRNodePair max(List<CRNodePair> pairs, SalienceConstant c)
    {
        return Collections.max(pairs, new Comparator<CRNodePair>()
		{
			@Override
			public int compare(CRNodePair pair1, CRNodePair pair2)
			{
				return MathUtils.signum(pair1.getPairSalienceWeight(c) - pair2.getPairSalienceWeight(c));
			}
		});
    } */

    public static CRNodePair max(List<CRNodePair> pairs, SalienceConstant c)
    {
        return Collections.max(pairs, (CRNodePair pair1, CRNodePair pair2) -> MathUtils.signum(pair1.getPairSalienceWeight(c) - pair2.getPairSalienceWeight(c)));
    }

    public static CRNode copy(CRNode node) {
        CRNode copy = new CRNode();
        copy.set(node.getID(), node.getWordForm(), node.getLemma(), node.getPartOfSpeechTag(), node.getNamedEntityTag(), node.getFeatMap(), node.getDependencyHead(), node.getDependencyLabel());
        copy.setSentenceID(node.getSentenceID());
        copy.init(node.getGender(), node.getNumber(), node.getPerson());
        copy.setSalienceFactor(node.getSalienceFactor());
        copy.setDynamicSalienceWeight(node.getDynamicSalienceWeight());
        copy.setStaticSalienceWeight(node.getStaticSalienceWeight());

        return copy;
    }

    public static int hash(CRNode node) {
        int hash = node.getSentenceID();
        hash = ((hash >>> 16) ^ hash) * 0x45d9f3b;
        hash = ((hash >>> 16) ^ hash) * 0x45d9f3b;
        hash = (hash >>> 16) ^ hash;
        return hash;
    }

    public static Set<String> determinerTagSet() {
        Set<String> dset = new HashSet<>();
        dset.add("PRP$");
        dset.add("WP$");
        dset.add("WDT");
        dset.add("PDT");

        return dset;
    }

    public static <K, V> void writeMapToFile(Map<K, V> map, String filename) throws IOException {
        File outputFile = new File(filename);
        if (!outputFile.exists()) outputFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Map.Entry<K, V> entry : map.entrySet())
            writer.write(entry.getKey().toString() + "\t:\t" + entry.getValue().toString() + "\n");

        writer.close();
    }

    public static Map<CRNode, CRNode> readFiletoMap(String filename) throws IOException {
        Map<CRNode, CRNode> map = new HashMap<>();
        BufferedReader reader = IOUtils.createBufferedReader(filename);
        List<String[]> node_primitives = new ArrayList<>();
        CRReader creader = new CRReader(3, 4, 5, 6, 7, 8, 9, 10);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] node_pair = line.split("\t:\t");
            String[] fnode_values = Splitter.splitTabs(node_pair[0]);
            String[] snode_values = Splitter.splitTabs(node_pair[1]);
            node_primitives.add(fnode_values);
            node_primitives.add(snode_values);
        }

        CRNode[] nodes = creader.toNodeList(node_primitives);
        for (int i = 0; i < nodes.length; i += 2) {
            map.put(nodes[i], nodes[i + 1]);
        }

        return map;
    }

    public static List<CRNode> filterNouns(List<CRNode> sentence) {
        return sentence.stream().filter(ENGrammarUtils::isNominal).collect(Collectors.toList());
    }

//    public static Set<String> definiteDeterminers() throws IOException {
//        return readTextFileSet(Constants.proj_path + "utils/files/def_det.txt");
//    }
//
//    public static Set<String> indefiniteDeterminers() throws IOException {
//        return readTextFileSet(Constants.proj_path + "utils/files/indef_det.txt");
//    }
//
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