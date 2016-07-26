package coreference;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

import chunkers.CREntityChunker;
import collections.*;
import nodes.CRNode;
import nodes.CRNodePair;
import nodes.CREntity;
import readers.CRReader;
import readers.CRTranscripter;
import utils.CRUtils;
import utils.Constants;
import utils.DebugUtils;

import edu.emory.mathcs.nlp.decode.NLPDecoder;

/**
 * Created by ethzh_000 on 7/5/2016.
 */
public class Resolution {

    private CRReader reader;
    private CRTranscripter transcripter;
    private List<CRNode> np_list;
    private List<CRNodePair> nonlex_candidate_pairs;
    private List<CRNodePair> lexana_candidate_pairs;
    private Set<CRNode> discourse_referents;
    private Map<CRNode, CRNode> all_coreferents;

    public Resolution() {
        reader = new CRReader(1, 2, 3, 4, 5, 6, 7, 8);
        transcripter = CRTranscripter.DEFAULT();
        np_list = new ArrayList<>();
        nonlex_candidate_pairs = new ArrayList<>();
        lexana_candidate_pairs = new ArrayList<>();
        discourse_referents = new HashSet<>();
        all_coreferents = new HashMap<>();
    }

    public boolean isTranscript(String filename) throws Exception {
        String ext = CRUtils.getFileExtension(filename);
        if (ext == null) throw new Exception("Not a valid file");
        else if (ext.equals(".dep")) return true;
        else return false;
    }

    public Scene parseTranscript(String filename) throws Exception {
        return transcripter.readScene(filename);
    }

    public List<CRNode[]> parseNLP(String filename) throws Exception {
        reader.open(new FileInputStream(new File(filename)));
        List<CRNode[]> documents = reader.readDocument();
        reader.close();

        return documents;
    }

    public void readDocument(Document document) throws IOException {
        List<CRNode> curr_np = CRUtils.filterNouns(document.getDocument());
        CRUtils.initFeatures(curr_np);
        SalienceWeighting.measureNewSalience(curr_np);
        if (!np_list.isEmpty()) SalienceWeighting.measureOldSalience(np_list);

        List<CRNode> curr_lexana = CRUtils.filterLexicalAnaphors(curr_np);
        List<CRNode> curr_prp = CRUtils.filterPronouns(curr_np);
        np_list.addAll(curr_np);

        List<CRNodePair> nonlex_coreferents = findNonLexicalCandidatePairs(curr_prp);
        List<CRNodePair> lexana_coreferents = bindLexicalAnaphorPairs(curr_lexana);

        System.out.println("Document Number: " + document.getDocumentNumber());
        System.out.println("Current Noun Phrases: " + curr_np);
        System.out.println("All Noun Phrases: " + np_list);
        System.out.println("Current Pronouns: " + curr_prp);
        System.out.println("Non-Lexical Coreferents: " + nonlex_coreferents);
        System.out.println("Current Lexical Anaphors: " + curr_lexana);
        System.out.println("Lexical Anaphor Coreferents: " + lexana_coreferents);
        // DebugUtils.printFeatures(nonlex_coreferents);
        System.out.println();

        Map<CRNode, CRNode> nonlex_map = nonlex_coreferents.stream().collect(Collectors.toMap(CRNodePair::getFirstNode, CRNodePair::getSecondNode));
        Map<CRNode, CRNode> lexana_map = lexana_coreferents.stream().collect(Collectors.toMap(CRNodePair::getFirstNode, CRNodePair::getSecondNode));
        all_coreferents.putAll(nonlex_map);
        all_coreferents.putAll(lexana_map);
    }

    // inefficient - needs to be fixed
    public List<CRNodePair> findNonLexicalCandidatePairs(List<CRNode> curr_prp) {
        List<CRNodePair> coreferents = new ArrayList<>();

        for (CRNode prp : curr_prp) {
            List<CRNodePair> possible_pairs = new ArrayList<>();

            for (CRNode np : np_list) {
                if (CRUtils.compare(prp, np)) continue;

                if (Coreferential.isNonCoreferential(prp, np)) {
                    createEquivalenceClass(np);
                    createEquivalenceClass(prp);
                } else {
                    possible_pairs.add(new CRNodePair(prp, np));
                    createEquivalenceClass(np);
                    np.getEquivalenceClass().addReferent(prp);
                }
            }

            if (!possible_pairs.isEmpty()) {
                CRNodePair coreferent = CRUtils.max(possible_pairs);
                coreferents.add(coreferent);
            }
        }

        return coreferents;
    }

    public List<CRNodePair> bindLexicalAnaphorPairs(List<CRNode> curr_lexana) {
        List<CRNodePair> coreferents = new ArrayList<>();

        for (CRNode lex_ana : curr_lexana) {
            List<CRNodePair> possible_pairs = new ArrayList<>();

            for (CRNode np : np_list)
                if (LexicalAnaphorBinding.isBinding(lex_ana, np)) possible_pairs.add(new CRNodePair(lex_ana, np));

            if (!possible_pairs.isEmpty()) {
                CRNodePair coreferent = CRUtils.max(possible_pairs);
                coreferents.add(coreferent);
            }
        }

        return coreferents;
    }

    public Equivalence createEquivalenceClass(CRNode ante) {
        if (ante.getEquivalenceClass() != null) return ante.getEquivalenceClass();

        discourse_referents.add(ante);
        Equivalence newEquiClass = new Equivalence(ante);
        newEquiClass.setAllSalienceFactors();
        newEquiClass.setNodeEquivalence();

        return newEquiClass;
    }

    public static void main(String[] args) throws Exception {
        Resolution r = new Resolution();
        /* List<CRNode[]> documents = r.parseNLP("C:/Users/ethzh_000/IdeaProjects/Coref_Resolution/src/readers/tests/test8.txt.nlp");
        List<Document> docs = new ArrayList<>();
        // CRUtils.Counter counter = new CRUtils.Counter();
        // documents.forEach(doc -> docs.add(new Document(doc, counter.increment())));
        // CREntityChunker chunker = new CREntityChunker();
        // for (Document doc : docs) System.out.println(chunker.getEntityChunks(doc));
        for (Document doc : docs) r.readDocument(doc); */

        Scene scene = r.parseTranscript("C:/Users/ethzh_000/Documents/Emory/NLP/Big_Bang_Theory/merged/s1/010101.dep");
        for (Utterance utterance : scene.getUtterances()) {
            for (Document document : utterance.getAllDocuments()) {
                r.readDocument(document);
                System.out.println(utterance.getSpeaker());

                for (CRNode np : document.getDocument()) {
                    if (np.getPerson() == 1) {
                        System.out.print(np.toString() + " -------> " + utterance.getSpeaker() + ", ");
                    }
                }
                System.out.println();
                System.out.println();
            }
        }

        // CRUtils.writeMapToFile(Constants.proj_path + "utils/coreference_output.txt", r.all_coreferents);
    }
}