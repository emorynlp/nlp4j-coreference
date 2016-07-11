package utils;

import nodes.CRNodePair;

import java.util.Comparator;

/**
 * Created by ethzh_000 on 7/8/2016.
 */
public class CRNodePairComparator implements Comparator<CRNodePair> {

    public CRNodePairComparator() {

    }

    public int compare(CRNodePair pair1, CRNodePair pair2) {
        return pair1.getSecondNode().getSalience() - pair2.getSecondNode().getSalience();
    }
}
