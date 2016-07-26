package utils;

import java.util.List;
import java.util.Map;

import nodes.CRNode;
import nodes.CRNodePair;

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

    public static void printSalienceFactors(List<CRNode> nodes) {
        for (CRNode node : nodes) {
            System.out.println(node.getWordForm());

            Map<String, Integer> sfactors = node.getSalienceFactors();
            for (String key : sfactors.keySet())
                System.out.println(key + " : " + sfactors.get(key));
            System.out.println();
        }
    }
}
