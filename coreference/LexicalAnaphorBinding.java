package coreference;

import nodes.CRNode;
import utils.Constants;
import utils.Domain;
import utils.GrammarUtils;

/**
 * Created by ethzh_000 on 7/6/2016.
 */
public class LexicalAnaphorBinding {

    public static boolean base(CRNode lex_ana, CRNode np) {
        return GrammarUtils.featuresMatch(lex_ana, np);
    }

    public static boolean Rule1(CRNode lex_ana, CRNode np) {
        if (!Domain.inArgumentDomain(lex_ana, np)) return false;

        Integer a_slot = Constants.arg_slots.get(lex_ana.getDependencyLabel());
        Integer n_slot = Constants.arg_slots.get(np.getDependencyLabel());

        int ana_slot = (a_slot == null) ? 0 : a_slot;
        int np_slot = (n_slot == null) ? 0 : n_slot;

        return ana_slot > np_slot;
    }

    public static boolean Rule2(CRNode lex_ana, CRNode np) {
        return Domain.inAdjunctDomain(lex_ana, np);
    }

    public static boolean Rule3(CRNode lex_ana, CRNode np) {
        return Domain.inNPDomain(lex_ana, np);
    }

    // inefficient - needs to be fixed
    public static boolean Rule4(CRNode lex_ana, CRNode np) {
        CRNode np_head = np.getDependencyHead();
        String np_pos_tag = np_head.getPartOfSpeechTag();
        if ( np_pos_tag.length() >= 2 && !np_pos_tag.substring(0, 2).equals("VB")) return false;

        CRNode ana_head = lex_ana.getDependencyHead();
        while (!ana_head.getWordForm().equals(Constants.ROOT_TAG)) {
            String ana_pos_tag = ana_head.getPartOfSpeechTag();
            if (ana_pos_tag.length() >= 2 && ana_pos_tag.substring(0, 2).equals("NN")) break;
            ana_head = ana_head.getDependencyHead();
        }

        if (ana_head.getWordForm().equals(Constants.ROOT_TAG)) return false;

        for (CRNode ditem : ana_head.getDependentList())
            if (GrammarUtils.isDeterminer(ditem)) return false;

        if (Domain.inArgumentDomain(ana_head, np)) return true;
        if (Domain.inAdjunctDomain(ana_head, np)) return true;

        return false;
    }

    public static boolean Rule5(CRNode lex_ana, CRNode np) {
        if (!GrammarUtils.isDeterminer(lex_ana)) return false;
        CRNode ana_head = lex_ana.getDependencyHead();

        if (Rule1(ana_head, np)) return true;
        if (Domain.inAdjunctDomain(ana_head, np)) return true;

        return false;
    }

    public static boolean isBinding(CRNode lex_ana, CRNode np) {
        if (!base(lex_ana, np)) return false;
        if (Rule1(lex_ana, np)) return true;
        if (Rule2(lex_ana, np)) return true;
        if (Rule3(lex_ana, np)) return true;
        if (Rule4(lex_ana, np)) return true;
        if (Rule5(lex_ana, np)) return true;

        return false;
    }
}
