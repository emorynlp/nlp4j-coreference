package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ethzh_000 on 7/1/2016.
 */
public final class Constants {

    // project path
    public static final String proj_path = "C:/Users/ethzh_000/IdeaProjects/Coref_Resolution/src/";

    // root word form of AbstractNLPNode
    public static final String ROOT_TAG = "@#r$%";

    // personal pronouns split by person
    public static Set<String> firstPerson() {
        Set<String> fp_set = new HashSet<>();
        fp_set.add("I");
        fp_set.add("me");
        fp_set.add("my");
        fp_set.add("mine");
        fp_set.add("myself");
        fp_set.add("we");
        fp_set.add("us");
        fp_set.add("our");
        fp_set.add("ours");
        fp_set.add("ourselves");

        return fp_set;
    }

    public static Set<String> secondPerson() {
        Set<String> sp_set = new HashSet<>();
        sp_set.add("you");
        sp_set.add("your");
        sp_set.add("yours");
        sp_set.add("yourself");
        sp_set.add("yourselves");

        return sp_set;
    }

    public static Set<String> thirdPerson() {
        Set<String> tp_set = new HashSet<>();
        tp_set.add("he");
        tp_set.add("him");
        tp_set.add("his");
        tp_set.add("himself");
        tp_set.add("she");
        tp_set.add("her");
        tp_set.add("hers");
        tp_set.add("herself");
        tp_set.add("it");
        tp_set.add("its");
        tp_set.add("itself");
        tp_set.add("they");
        tp_set.add("them");
        tp_set.add("their");
        tp_set.add("theirs");
        tp_set.add("themselves");

        return tp_set;
    }

    public static final Set<String> fp_set = firstPerson();
    public static final Set<String> sp_set = secondPerson();
    public static final Set<String> tp_set = thirdPerson();

    public static Set<String> singularPronouns() {
        Set<String> sprp_set = new HashSet<>();
        sprp_set.add("I");
        sprp_set.add("me");
        sprp_set.add("my");
        sprp_set.add("mine");
        sprp_set.add("myself");
        sprp_set.add("you");
        sprp_set.add("your");
        sprp_set.add("yours");
        sprp_set.add("yourself");
        sprp_set.add("he");
        sprp_set.add("him");
        sprp_set.add("his");
        sprp_set.add("himself");
        sprp_set.add("she");
        sprp_set.add("her");
        sprp_set.add("hers");
        sprp_set.add("herself");
        sprp_set.add("it");
        sprp_set.add("its");
        sprp_set.add("itself");

        return sprp_set;
    }

    public static Set<String> pluralPronouns() {
        Set<String> pprp_set = new HashSet<>();
        pprp_set.add("we");
        pprp_set.add("us");
        pprp_set.add("our");
        pprp_set.add("ours");
        pprp_set.add("ourselves");
        pprp_set.add("yourselves");
        pprp_set.add("they");
        pprp_set.add("them");
        pprp_set.add("their");
        pprp_set.add("theirs");
        pprp_set.add("themselves");

        return pprp_set;
    }

    public static final Set<String> sprp_set = singularPronouns();
    public static final Set<String> pprp_set = pluralPronouns();

    // anaphor binding argument slot order
    // subj. > agent (deep subj.) > dobj. > (iobj. || pobj.)
    public static Map<String, Integer> getArgSlots() {
        Map<String, Integer> arg_slots = new HashMap<>();
        arg_slots.put("csubj", 3);
        arg_slots.put("csubjpass", 3);
        arg_slots.put("nsubj", 3);
        arg_slots.put("nsubjpass", 3);
        arg_slots.put("appos", 3);
        arg_slots.put("agent", 2);
        arg_slots.put("dobj", 1);
        arg_slots.put("pobj", 0);

        return arg_slots;
    }

    public static final Map<String, Integer> arg_slots = getArgSlots();

    // salience weighting values
    public static final int sentence_recency = 100;
    public static final int subj_emph = 80;
    public static final int exist_emph = 70;
    public static final int acc_emph = 50;
    public static final int dat_emph = 40;
    public static final int obliq_emph = 40;
    public static final int head_nn_emph = 80;
    public static final int non_adv_emph = 50;
    public static final int cat_emph = 175;
    public static final int parallel_emph = 35;

    // salience factors hash-map
    public static Map<String, Integer> newSalienceFactors() {
        Map<String, Integer> sal_factors = new HashMap<>();
        sal_factors.put("subj", 0);
        sal_factors.put("exist", 0);
        sal_factors.put("acc", 0);
        sal_factors.put("dat", 0);
        sal_factors.put("obliq", 0);
        sal_factors.put("head_nn", 0);
        sal_factors.put("non_adv", 0);

        return sal_factors;
    }

    // subject label set
    public static Set<String> subjSet() {
        Set<String> sset = new HashSet<>();
        sset.add("csubj");
        sset.add("csubjpass");
        sset.add("nsubj");
        sset.add("nsubjpass");

        return sset;
    }

    public static Set<String> subj_set = subjSet();
}
