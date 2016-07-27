package edu.emory.mathcs.nlp.coreference.zzz;

import java.util.List;

import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.collection.CRNodePair;

/**
 * Created by ethzh_000 on 7/13/2016.
 */
public class DebugUtils {

    public static void printFeatures(CRNodePair pair) {
        CRNode fnode = pair.getFirstNode();
        CRNode snode = pair.getSecondNode();

        System.out.println(fnode.getWordForm() + " : " + snode.getWordForm());
        System.out.println(fnode.getPerson() + " : " + snode.getPerson());
        System.out.println(fnode.getGender() + " : " + snode.getGender());
        System.out.println(fnode.getNumber() + " : " + snode.getNumber());
    }

    public static void printFeatures(List<CRNodePair> pairs) {
        pairs.stream().forEach(DebugUtils::printFeatures);
    }

//    public static void printSalienceFactors(List<CRNode> nodes) {
//        for (CRNode node : nodes) {
//            System.out.println(node.getWordForm());
//
//            Map<String, Integer> sfactors = node.getSalienceFactor();
//            for (String key : sfactors.keySet())
//                System.out.println(key + " : " + sfactors.get(key));
//            System.out.println();
//        }
//    }
}
