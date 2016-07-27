package edu.emory.mathcs.nlp.coreference.zzz;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import edu.emory.mathcs.nlp.common.constant.StringConst;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;

/**
 * Created by ethzh_000 on 7/13/2016.
 */
public class CREntity extends CRNode {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2555672262010620272L;
	private List<CRNode> entity_dependents;
    private boolean named = false;

    public CREntity() {
        super();
        entity_dependents = new ArrayList<>();
    }

    public CREntity(CRNode root) {
        super(root.getID(), root.getWordForm(), root.getLemma(), root.getPartOfSpeechTag(), root.getNamedEntityTag(), root.getFeatMap(), root.getDependencyHead(), root.getDependencyLabel());
//        init();
        entity_dependents = new ArrayList<>();
    }

    // Setters
    // -----------------------------------------------------------------------------------------------------------------
    public void setToNamedEntity() {
        named = true;
    }

    public void addEntityDependent(CRNode node) {
        entity_dependents.add(node);
    }

    public void addAllDependents(List<CRNode> dependents) {
        entity_dependents.addAll(dependents);
    }

    // Getters
    // -----------------------------------------------------------------------------------------------------------------
    public boolean isNamedEntity() {
        return named;
    }

    public boolean isCommonEntity() {
        return !named;
    }

    public boolean isUnitEntity() {
        return entity_dependents.size() == 1;
    }

    public boolean isCompoundEntity() {
        return entity_dependents.size() > 1;
    }

    // Helpers
    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        StringJoiner printable = new StringJoiner(StringConst.TAB);
        printable.add(super.toString());

        StringJoiner entity_form = new StringJoiner(StringConst.SPACE);
        entity_dependents.forEach(dep -> entity_form.add(dep.getWordForm()));
        entity_form.add(word_form);

        printable.add(entity_form.toString());
        return printable.toString();
    }
}
