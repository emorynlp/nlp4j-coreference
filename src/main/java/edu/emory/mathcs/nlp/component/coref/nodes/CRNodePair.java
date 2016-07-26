package nodes;

import utils.CRUtils;
import utils.Constants;
import utils.GrammarUtils;

/**
 * Created by ethzh_000 on 7/5/2016.
 */
public class CRNodePair implements Comparable<CRNodePair> {

    private CRNode node1;
    private CRNode node2;

    public CRNodePair(CRNode node1, CRNode node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    // Setters
    // -----------------------------------------------------------------------------------------------------------------
    public void setFirstNode(CRNode node) {
        node1 = node;
    }

    public void setSecondNode(CRNode node) {
        node2 = node;
    }

    // Getters
    // -----------------------------------------------------------------------------------------------------------------
    public CRNode getFirstNode() {
        return node1;
    }

    public CRNode getSecondNode() {
        return node2;
    }

    public int getPairSalience() {
        int ghost_sal = node2.getGhostSalience();
        if (GrammarUtils.isCataphoric(this)) ghost_sal -= Constants.cat_emph;
        if (GrammarUtils.areParallel(node1, node2)) ghost_sal += Constants.parallel_emph;
        return ghost_sal;
    }

    // Helpers
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(node1.toString());
        sb.append(" <------> ");
        sb.append(node2.toString());

        return sb.toString();
    }

    public int compareTo(CRNodePair pair) {
        CRNode np1 = pair.getFirstNode();
        CRNode np2 = pair.getFirstNode();

        if (CRUtils.compare(node1, np1) && CRUtils.compare(node2, np2)) return 0;
        if (CRUtils.compare(node1, np2) && CRUtils.compare(node2, np1)) return 0;

        return 1;
    }

    public CRNodePair self() {
        return this;
    }
}
