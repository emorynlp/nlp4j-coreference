package nodes;

import java.util.Map;

import utils.Constants;
import collections.Equivalence;

import edu.emory.mathcs.nlp.component.template.node.AbstractNLPNode;
import edu.emory.mathcs.nlp.component.template.node.FeatMap;

/**
 * Created by ethzh_000 on 7/1/2016.
 */
public class CRNode extends AbstractNLPNode<CRNode> {

    private String gender;
    private String number;
    private int person;
    private int document;
    private int salient_weight;
    private int ghost_salience;
    private Map<String, Integer> sfactors;
    private Equivalence equivalence_class;

    public CRNode() {
        super();
        init();
    }

    public CRNode(String gender, String number, int person, int document) {
        super();
        init(gender, number, person, document);
    }

    public CRNode(int id, String form) {
        super(id, form);
        init();
    }

    public CRNode(int id, String form, String posTag) {
        super(id, form, posTag);
        init();
    }

    public CRNode(int id, String form, String lemma, String posTag, FeatMap feats) {
        super(id, form, lemma, posTag, feats);
        init();
    }

    public CRNode(int id, String form, String lemma, String posTag, String namentTag, FeatMap feats) {
        super(id, form, lemma, posTag, namentTag, feats);
        init();
    }

    public CRNode(int id, String form, String lemma, String posTag, FeatMap feats, CRNode dhead, String deprel) {
        super(id, form, lemma, posTag, feats, dhead, deprel);
        init();
    }

    public CRNode(int id, String form, String lemma, String posTag, String namentTag, FeatMap feats, CRNode dhead, String deprel) {
        super(id, form, lemma, posTag, feats, dhead, deprel);
        init();
    }

    public CRNode(int id, String form, String lemma, String posTag, String namentTag, String answerTag, FeatMap feats, CRNode dhead, String deprel) {
        super(id, form, lemma, posTag, namentTag, feats, dhead, deprel);
        init();
    }

    public void init() {
        gender = "n";
        number = "sg";
        person = 3;
        document = -1;
        salient_weight = 0;
        ghost_salience = 0;
        sfactors = Constants.newSalienceFactors();
        equivalence_class = null;
    }

    public void init(String gender, String number, int person, int document) {
        this.gender = gender;
        this.number = number;
        this.person = person;
        this.document = document;
        salient_weight = 0;
        ghost_salience = 0;
        sfactors = Constants.newSalienceFactors();
        equivalence_class = null;
    }

    // Setters
    // -----------------------------------------------------------------------------------------------------------------

    public void setSalience(int salience) {
        salient_weight = salience;
        ghost_salience = salient_weight;
    }

    public void increaseSalience(int amount) {
        salient_weight += amount;
        ghost_salience += amount;
    }

    public void decreaseSalience(int amount) {
        ghost_salience -= amount;
    }

    public void degradeSalience() {
        ghost_salience /= 2;
    }

    public void setGender(String g) {
        gender = g;
    }

    public void setNumber(String num) {
        number = num;
    }

    public void setPerson(int p) {
        person = p;
    }

    public void setDocumentNumber(int doc_num) {
        document = doc_num;
    }

    public void setEquivalenceClass(Equivalence equi_class) {
        equivalence_class = equi_class;
    }

    public void setAsSubject() {
        sfactors.put("subj", 1);
    }

    public void setAsExistential() {
        sfactors.put("exist", 1);
    }

    public void setAsAccusative() {
        sfactors.put("acc", 1);
    }

    public void setAsDative() {
        sfactors.put("dat", 1);
    }

    public void setAsObliqueComp() {
        sfactors.put("obliq", 1);
    }

    public void setAsHeadNoun() {
        sfactors.put("head_nn", 1);
    }

    public void setAsNonAdverbial() {
        sfactors.put("non_adv", 1);
    }

    // Getters
    // -----------------------------------------------------------------------------------------------------------------

    public int getSalience() {
        return salient_weight;
    }

    public int getGhostSalience() {
        return ghost_salience;
    }

    public String getGender() {
        return gender;
    }

    public String getNumber() {
        return number;
    }

    public int getPerson() {
        return person;
    }

    public int getDocumentNumber() {
        return document;
    }

    public Equivalence getEquivalenceClass() {
        return equivalence_class;
    }

    public Map<String, Integer> getSalienceFactors() {
        return sfactors;
    }

    @Override
    public CRNode self() {
        return this;
    }

    // Helpers
    // -----------------------------------------------------------------------------------------------------------------

    public int compareTo(CRNode node) {
        int doc_diff = document - node.getDocumentNumber();
        if (doc_diff == 0) {
            int id_diff = id - node.getID();
            if (id_diff == 0) return 1;
            else return 0;
        } else return 0;
    }

    @Override
    public String toString() {
        return Integer.toString(document) + "\t" + super.toString() + "\t" + Integer.toString(salient_weight);
    }
}