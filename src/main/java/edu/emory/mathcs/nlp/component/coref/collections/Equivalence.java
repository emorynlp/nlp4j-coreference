package collections;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import nodes.CRNode;
import utils.Constants;

/**
 * Created by ethzh_000 on 7/6/2016.
 */
public class Equivalence {

    private CRNode antecedent;
    private List<CRNode> referents;
    private Set<CRNode> refset;
    private Map<String, Integer> sfactors;

    public Equivalence() {
        antecedent = new CRNode();
        referents = new ArrayList<>();
        addReferent(antecedent);
        sfactors = Constants.newSalienceFactors();
    }

    public Equivalence(CRNode antecedent) {
        this.antecedent = antecedent;
        referents = new ArrayList<>();
        addReferent(antecedent);
        sfactors = Constants.newSalienceFactors();
    }

    public Equivalence(CRNode antecedent, List<CRNode> referents) {
        this.antecedent = antecedent;
        addReferent(antecedent);
        this.referents.addAll(referents);
        sfactors = Constants.newSalienceFactors();
        refset = toReferentSet();
    }

    // Setters/Functions
    // -----------------------------------------------------------------------------------------------------------------
    public void setAntecedent(CRNode ante) {
        antecedent = ante;
    }

    public void setReferents(List<CRNode> refs) {
        referents = refs;
    }

    public Set<CRNode> toReferentSet() {
        Set<CRNode> rset = new HashSet<>();
        referents.stream().forEach(rset::add);

        return rset;
    }

    public void addReferent(CRNode ref) {
        referents.add(ref);
    }

    public void setSubjFactor() {
        sfactors.put("subj", Constants.subj_emph);
    }

    public void setExistFactor() {
        sfactors.put("exist", Constants.exist_emph);
    }

    public void setAccFactor() {
        sfactors.put("acc", Constants.acc_emph);
    }

    public void setDatFactor() {
        sfactors.put("dat", Constants.dat_emph);
    }

    public void setObliqFactor() {
        sfactors.put("obliq", Constants.obliq_emph);
    }

    public void setHeadNounFactor() {
        sfactors.put("head_nn", Constants.head_nn_emph);
    }

    public void setNonAdvFactor() {
        sfactors.put("non_adv", Constants.non_adv_emph);
    }

    public void setAppropriateFactor(String factor) {
        switch (factor) {
            case "subj": setSubjFactor(); break;
            case "exist": setExistFactor(); break;
            case "acc": setAccFactor(); break;
            case "dat": setDatFactor(); break;
            case "obliq": setObliqFactor(); break;
            case "head_nn": setHeadNounFactor(); break;
            case "non_adv": setNonAdvFactor(); break;
        }
    }

    public void setSalienceFactors(CRNode node) {
        node.getSalienceFactors().keySet().stream().forEach(key -> setAppropriateFactor(key));
    }

    public void setAllSalienceFactors() {
        referents.stream().forEach(ref -> setSalienceFactors(ref));
    }

    public void setNodeEquivalence() {
        referents.stream().forEach(ref -> ref.setEquivalenceClass(this));
    }

    // Getters
    // -----------------------------------------------------------------------------------------------------------------
    public List<CRNode> getReferents() {
        return referents;
    }

    public Set<CRNode> getReferentSet() {
        return refset;
    }

    public CRNode getAntecedent() {
        return antecedent;
    }

    public int getSalientWeight() {
        return sfactors.values().stream().mapToInt(Integer::intValue).sum();
    }
}