package utils;

import nodes.CRNode;
import nodes.CREntity;

/**
 * Created by ethzh_000 on 7/1/2016.
 */
public class Domain {

    public static boolean inArgumentDomain(CRNode np, CRNode np_pred) {
        CRNode head = np.getDependencyHead();
        CRNode phead = np_pred.getDependencyHead();
        return CRUtils.compare(head, phead);
    }

    public static boolean inAdjunctDomain(CRNode np, CRNode np_pred) {
        CRNode head = np.getDependencyHead();
        // String pos_tag = head.getPartOfSpeechTag();
        // if (pos_tag.equals("TO")) return false;
        // if (pos_tag.equals("IN")) return false;

        while (!head.getWordForm().equals(Constants.ROOT_TAG)) {
            if (CRUtils.compare(head, np_pred.getDependencyHead())) return true;
            head = head.getDependencyHead();
        }

        return false;
    }

    public static boolean inNPDomain(CRNode np, CRNode np_pred) {
        CRNode phead = np_pred.getDependencyHead();
        while (!phead.getWordForm().equals(Constants.ROOT_TAG)) {
            String pos_tag = phead.getPartOfSpeechTag();
            if (pos_tag.length() > 1 && pos_tag.substring(0, 2).equals("NN")) break;
            phead = phead.getDependencyHead();
        }

        if (phead.getWordForm().equals(Constants.ROOT_TAG)) return false;
        if (!GrammarUtils.isDeterminer(np, phead)) return false;

        if (inArgumentDomain(np, phead)) return true;
        else if (inAdjunctDomain(np, phead)) return true;

        return false;
    }
}
