package edu.emory.mathcs.nlp.coreference.collection;

/**
 * Created by ethzh_000 on 8/28/2016.
 */
public class CRNodeIdentifier {

    int sentence_id = 0;
    int node_id = 0;

    public CRNodeIdentifier(CRNode node) {
        sentence_id = node.getSentenceID();
        node_id = node.getID();
    }

    public String toString() {
        return Integer.toString(sentence_id) + "\t" + Integer.toString(node_id);
    }
}
