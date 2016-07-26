package utils;

import java.util.Comparator;

import nodes.CRNodePair;

/**
 * Created by ethzh_000 on 7/8/2016.
 */
public class CRNodePairComparator implements Comparator<CRNodePair> {

    public CRNodePairComparator() {

    }

    public int compare(CRNodePair pair1, CRNodePair pair2) {
        return pair1.getPairSalience() - pair2.getPairSalience();
    }
}
