package collections;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by ethzh_000 on 7/25/2016.
 */
public class Utterance {

    private List<Document> documents;
    private int utterance_number;
    private String speaker;

    public Utterance() {
        utterance_number = 0;
        documents = new ArrayList<>();
    }

    public Utterance(List<Document> documents) {
        utterance_number = 0;
        this.documents = documents;
    }

    public Utterance(int utterance_number, String speaker, List<Document> documents) {
        this.utterance_number = utterance_number;
        this.speaker = speaker;
        this.documents = documents;
    }

    public void addDocument(Document doc) {
        documents.add(doc);
    }

    public void setUtteranceNumber(int utter_num) {
        utterance_number = utter_num;
    }

    public void setSpeaker(String spkr) {
        speaker = spkr;
    }

    public List<Document> getAllDocuments() {
        return documents;
    }

    public Document getDocument(int doc_num) {
        return documents.get(doc_num);
    }

    public String getSpeaker() {
        return speaker;
    }
}
