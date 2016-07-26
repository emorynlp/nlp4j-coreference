package utils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import nodes.CRNode;
import nodes.CRNodePair;

/**
 * Created by ethzh_000 on 7/13/2016.
 */
public class GrammarUtils {

    public static boolean featuresMatch(CRNode np1, CRNode np2) {
        if (np1.getPerson() != np2.getPerson()) return false;
        if (!np1.getGender().equals(np2.getGender())) return false;
        if (!np1.getNumber().equals(np2.getNumber())) return false;

        return true;
    }

    public static boolean isContainedIn(CRNode np1, CRNode np2) {
        if (Domain.inArgumentDomain(np1, np2)) return true;
        if (Domain.inAdjunctDomain(np1, np2)) return true;

        return false;
    }

    // inefficient - needs to be fixed
    public static boolean isDeterminer(CRNode np) {
        Set<String> det_set = CRUtils.determinerTagSet();
        String pos_tag = np.getPartOfSpeechTag();
        if (det_set.contains(pos_tag)) return true;

        List<CRNode> dlist = np.getDependentList();
        boolean a_match = dlist.stream().anyMatch(ditem -> ditem.getPartOfSpeechTag().equals("POS"));

        return a_match;
    }

    public static boolean isDeterminer(CRNode np, CRNode np_pred) {
        return isDeterminer(np) && np.isDependentOf(np_pred);
    }

    // unable to filter "other" as in "each [other]" - needs to be fixed
    public static boolean isLexical(CRNode np) {
        String lemma = np.getLemma();
        int n = lemma.length();

        if (n > 4 && lemma.substring(n - 4, n).equals("self")) return true;
        else if (n > 6 && lemma.substring(n - 6, n).equals("selves")) return true;

        return false;
    }

    public static boolean areParallel(CRNode np1, CRNode np2) {
        return Constants.arg_slots.get(np1.getDependencyLabel()) == Constants.arg_slots.get(np2.getDependencyLabel());
    }

    public static boolean isNominal(CRNode word) {
        String pos_tag = word.getPartOfSpeechTag();
        int pos_len = pos_tag.length();

        if (pos_len < 2) return false;
        else if (pos_tag.length() >= 3) {
            if (pos_tag.substring(0, 3).equals("PRP")) return true;
            else if (pos_tag.substring(0, 2).equals("NN")) return true;
            else if (pos_tag.substring(0, 2).equals("WP")) return true;
        } else {
            if (pos_tag.substring(0, 2).equals("NN")) return true;
            else if (pos_tag.substring(0, 2).equals("WP")) return true;
        }

        return false;
    }

    public static boolean isPronoun(CRNode noun) {
        String pos_tag = noun.getPartOfSpeechTag();
        if (pos_tag.length() >= 3) {
            if (pos_tag.substring(0, 3).equals("PRP")) return true;
        } else if (pos_tag.length() >= 2) {
            if (pos_tag.substring(0, 2).equals("WP")) return true;
        }

        return false;
    }

    public static boolean isPlural(CRNode noun) {
        if (isPronoun(noun)) {
            if (Constants.sprp_set.contains(noun.getLemma())) return false;
            else if (Constants.pprp_set.contains(noun.getLemma())) return true;
        } else {
            String pos_tag = noun.getPartOfSpeechTag();
            switch (pos_tag) {
                case "NNS":
                    return true;
                case "NNPS":
                    return true;
            }
        }

        return false;
    }

    // inefficient - needs to be fixed
    public static boolean isDefinite(CRNode nn_dep) throws IOException {
        if (nn_dep.getDependencyLabel().equals("poss")) return true;
        String pos_tag = nn_dep.getPartOfSpeechTag();
        if (CRUtils.determinerTagSet().contains(pos_tag)) return true;
        if (CRUtils.definiteDeterminers().contains(nn_dep.getLemma())) return true;

        return false;
    }

    // inefficient - needs to be fixed
    public static boolean isIndefinite(CRNode nn_dep) throws IOException {
        if (nn_dep.getDependencyLabel().equals("predet")) return true;
        String pos_tag = nn_dep.getPartOfSpeechTag();
        if (pos_tag.equals("CD")) return true;
        if (pos_tag.equals("PDT")) return true;
        if (CRUtils.indefiniteDeterminers().contains(nn_dep.getLemma())) return true;

        return false;
    }

    public static boolean isCataphoric(CRNodePair pair) {
        return isCataphoric(pair.getFirstNode(), pair.getSecondNode());
    }

    public static boolean isCataphoric(CRNode ante, CRNode post) {
        return ante.getID() > post.getID();
    }
}
