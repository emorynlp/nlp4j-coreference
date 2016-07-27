package edu.emory.mathcs.nlp.coreference.util.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Splitter;
import edu.emory.mathcs.nlp.component.template.reader.TSVReader;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.collection.Sentence;
import edu.emory.mathcs.nlp.coreference.zzz.Scene;
import edu.emory.mathcs.nlp.coreference.zzz.Utterance;

/**
 * Created by ethzh_000 on 7/25/2016.
 */
public class CRTranscripter extends TSVReader<CRNode> {

	public int utterance = -1;
    public int document = -1;
    public int form = -1;
    public int lemma = -1;
    public int pos = -1;
    public int feats = -1;
    public int dhead = -1;
    public int deprel = -1;
    public int sheads = -1;
    public int nament = -1;
    public int referent = -1;
    private BufferedReader dep_reader;

    public CRTranscripter(int utterance, int document, int form, int lemma, int pos, int feats, int dhead, int deprel, int sheads, int nament, int referent) {
        super(form, lemma, pos, feats, dhead, deprel, sheads, nament);
        init(utterance, document, form, lemma, pos, feats, dhead, deprel, sheads, nament, referent);
    }

    protected void init(int utterance, int document, int form, int lemma, int pos, int feats, int dhead, int deprel, int sheads, int nament, int referent) {
        this.utterance = utterance;
        this.document = document;
        this.form = form;
        this.lemma = lemma;
        this.pos = pos;
        this.feats = feats;
        this.dhead = dhead;
        this.deprel = deprel;
        this.sheads = sheads;
        this.nament = nament;
        this.referent = referent;
    }

    public static CRTranscripter DEFAULT() {
        return new CRTranscripter(0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    }

    protected void initBufferedReader(String filename) throws IOException {
        dep_reader = IOUtils.createBufferedReader(filename);
    }

    public Scene readScene(String filename) throws IOException {
        initBufferedReader(filename);
        List<Utterance> utters = new ArrayList<>();

        Utterance utterance;
        while ((utterance = readUtterance()) != null) utters.add(utterance);

        dep_reader.close();
        return (utters.isEmpty()) ? null : new Scene(filename, utters);
    }

    public Utterance readUtterance() throws IOException {
        List<String[]> values_list = new ArrayList<>();
        int utter_num = 0;
        String speaker = "";

        String line;
        while ((line = dep_reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) break;
            String[] node_values = Splitter.splitTabs(line);
            values_list.add(node_values);
            utter_num = Integer.parseInt(node_values[utterance]);

            List<String> features = Splitter.splitIncludingMatches(Pattern.compile("[|,]"), node_values[feats]);
            if (features.isEmpty()) continue;
            Pattern p = Pattern.compile("speaker=(.+)");
            for (String feature : features) {
                Matcher m = p.matcher(feature);
                if (m.find()) speaker = m.group(1);
            }
        }

        return (values_list.isEmpty()) ? null : new Utterance(utter_num, speaker, toDocumentList(values_list));
    }

    public List<Sentence> toDocumentList(List<String[]> nodes) {
        CRNode[] utter_primitive;
        int utter_size = nodes.size();

        List<Sentence> documents = new ArrayList<>();
        List<String[]> values_list = new ArrayList<>();
        int doc_count = 0;

        for (int i = 0; i < utter_size; i++) {
            values_list.add(nodes.get(i));
            if (nodes.get(i)[lemma].equals(".")) {
                utter_primitive = toNodeList(values_list);
                documents.add(new Sentence(doc_count++, utter_primitive));
                values_list = new ArrayList<>();
            }
        }

        return documents;
    }

    public CRNode create() {
        return new CRNode();
    }
}
