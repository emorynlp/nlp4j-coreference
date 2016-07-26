package collections;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import nodes.CRNode;

/**
 * Created by ethzh_000 on 7/2/2016.
 */
public class Document {

    private int document_number = 0;
    private List<CRNode> sentence;

    public Document() {
    }

    public Document(List<CRNode> sentence) {
        this.sentence = sentence;
    }

    public Document(CRNode[] sentence) {
        this.sentence = new ArrayList<>(Arrays.asList(sentence));
    }

    public Document(int document_number, List<CRNode> sentence) {
        this.sentence = sentence;
        this.document_number = document_number;
        setNodeDocumentNumber();
    }

    public Document(CRNode[] sentence, int document_number) {
        this.sentence = new ArrayList<>(Arrays.asList(sentence));
        this.document_number = document_number;
        setNodeDocumentNumber();
    }

    // Setters/Functions
    // -----------------------------------------------------------------------------------------------------------------
    public void setDocument(List<CRNode> doc) {
        sentence = doc;
    }

    public void setDocumentNumber(int doc_num) {
        document_number = doc_num;
    }

    public void setNodeDocumentNumber() {
        sentence.stream().forEach(node -> node.setDocumentNumber(document_number));
    }

    // Getters
    // -----------------------------------------------------------------------------------------------------------------
    public List<CRNode> getDocument() {
        return sentence;
    }

    public int getDocumentNumber() {
        return document_number;
    }

    public Map<Integer, CRNode> getIDtoNodeMap() {
        Map<Integer, CRNode> iton = new HashMap<>();
        for (CRNode node : sentence)
            iton.put(node.getID(), node);

        return iton;
    }

    public CRNode getRoot() {
        return sentence.get(0);
    }
}
