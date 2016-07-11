package coreference;

import java.util.ArrayList;
import java.util.List;

import nodes.CRNode;
import collections.*;
import utils.Constants;

/**
 * Created by ethzh_000 on 7/6/2016.
 */
public class SalienceWeighting {

    public static boolean inDocument(CRNode word, Document doc) {
        return word.getDocumentNumber() == doc.getDocumentNumber();
    }

    public static boolean isSubject(CRNode word) {
        return Constants.subj_set.contains(word.getDependencyLabel());
    }

    public static boolean isExistential(CRNode word) {
        return word.getPartOfSpeechTag().equals("EX");
    }

    public static boolean isAccusative(CRNode word) {
        return word.getDependencyLabel().equals("dobj");
    }

    public static boolean isDative(CRNode word) {
        if (word.getDependencyLabel().equals("pobj")) {
            CRNode prep_head = word.getDependencyHead();
            if (prep_head.getDependencyLabel().equals("dative")) return true;
        }

        return false;
    }

    // may need to differentiate complement from adjunct
    public static boolean isObliqueComplement(CRNode word) {
        if (word.getDependencyLabel().equals("pobj")) {
            CRNode prep_head = word.getDependencyHead();
            if (prep_head.getDependencyLabel().equals("prep")) return true;
        }

        return false;
    }

    public static boolean isHeadNoun(CRNode word) {
        CRNode head = word.getDependencyHead();
        while (!head.getWordForm().equals(Constants.ROOT_TAG)) {
            if (head.getPartOfSpeechTag().substring(0, 2).equals("NN")) return false;
            head = head.getDependencyHead();
        }

        return true;
    }

    public static void findAllNonAdverbials(List<CRNode> nouns) {
        List<CRNode> nadv_list = new ArrayList<>();
        boolean adv_phr = true;

        for (int i = 0; i < nouns.size(); i++) {
            CRNode ditem = nouns.get(i);
            String label = ditem.getDependencyLabel();
            if (Constants.subj_set.contains(label)) adv_phr = false;
            else if (ditem.getLemma().equals(",")) adv_phr = true;

            if (adv_phr) {
                if (!label.equals("pobj")) {
                    ditem.increaseSalience(Constants.non_adv_emph);
                    ditem.setAsNonAdverbial();
                }
            } else {
                ditem.increaseSalience(Constants.non_adv_emph);
                ditem.setAsNonAdverbial();
            }
        }
    }

    public static void measureNewSalience(List<CRNode> curr_nouns) {
        findAllNonAdverbials(curr_nouns);

        for (CRNode noun : curr_nouns) {
            int inc_weight = Constants.sentence_recency;

            if (isSubject(noun)) {
                inc_weight += Constants.subj_emph;
                noun.setAsSubject();
            } else if (isExistential(noun)) {
                inc_weight += Constants.exist_emph;
                noun.setAsExistential();
            } else if (isAccusative(noun)) {
                inc_weight += Constants.acc_emph;
                noun.setAsAccusative();
            } else if (isDative(noun)) {
                inc_weight += Constants.dat_emph;
                noun.setAsDative();
            } else if (isObliqueComplement(noun)) {
                inc_weight += Constants.obliq_emph;
                noun.setAsObliqueComp();
            }

            if (isHeadNoun(noun)) {
                inc_weight += Constants.head_nn_emph;
                noun.setAsHeadNoun();
            }

            Equivalence equi_class = noun.getEquivalenceClass();
            if (equi_class != null) inc_weight += equi_class.getSalientWeight();

            noun.setSalience(inc_weight);
        }
    }

    public static void measureOldSalience(List<CRNode> prev_nouns) {
        prev_nouns.stream().forEach(CRNode::degradeSalience);
    }
}
