package coreference;

import nodes.CRNode;
import utils.CRUtils;
import utils.Domain;

/**
 * Created by ethzh_000 on 7/5/2016.
 */
public class Coreferential {

    public static boolean Rule1(CRNode prp, CRNode np) {
        return !CRUtils.featuresMatch(prp, np);
    }

    public static boolean Rule2(CRNode prp, CRNode np) {
        return Domain.inArgumentDomain(prp, np);
    }

    public static boolean Rule3(CRNode prp, CRNode np) {
        return Domain.inAdjunctDomain(prp, np);
    }

    public static boolean Rule4(CRNode prp, CRNode np) {
        String pos_tag = np.getPartOfSpeechTag();
        if (pos_tag.equals("PRP") || pos_tag.equals("PRP$")) return false;

        CRNode head = prp.getDependencyHead();
        if (CRUtils.isContainedIn(np, head)) return true;

        return false;
    }

    public static boolean Rule5(CRNode prp, CRNode np) {
        return Domain.inNPDomain(prp, np);
    }

    public static boolean Rule6(CRNode prp, CRNode np) {
        if (!CRUtils.isDeterminer(prp)) return false;
        if (!CRUtils.isContainedIn(np, prp.getDependencyHead())) return false;

        return true;
    }

    public static boolean isNonCoreferential(CRNode prp, CRNode np) {
        if (Rule1(prp, np)) return true;
        if (Rule2(prp, np)) return true;
        if (Rule3(prp, np)) return true;
        if (Rule4(prp, np)) return true;
        if (Rule5(prp, np)) return true;
        if (Rule6(prp, np)) return true;

        return false;
    }
}