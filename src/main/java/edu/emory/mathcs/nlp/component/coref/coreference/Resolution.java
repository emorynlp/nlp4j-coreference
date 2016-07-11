package coreference;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Collectors;

import collections.Equivalence;
import readers.CRReader;
import utils.CRUtils;
import nodes.CRNode;
import nodes.CRNodePair;
import collections.Document;
import utils.Constants;

/**
 * Created by ethzh_000 on 7/5/2016.
 */
public class Resolution {

    private CRReader reader;
    private List<CRNode> np_list;
    // private List<CRNode> prp_list;
    // private List<CRNode> lexana_list;
    // private List<CRNodePair> noncoref_list;
    private List<CRNodePair> nonlex_candidate_pairs;
    private List<CRNodePair> lexana_candidate_pairs;
    private Set<CRNode> discourse_referents;
    private Map<String, CRNodePair> all_coreferents;

    public Resolution() {
        reader = new CRReader(1, 2, 3, 4, 5, 6, 7, 8);
        np_list = new ArrayList<>();
        // prp_list = new ArrayList<>();
        // lexana_list = new ArrayList<>();
        // noncoref_list = new ArrayList<>();
        nonlex_candidate_pairs = new ArrayList<>();
        lexana_candidate_pairs = new ArrayList<>();
        discourse_referents = new HashSet<>();
        all_coreferents = new HashMap<>();
    }

    public List<CRNode[]> parse(String filepath) throws Exception {
        reader.open(new FileInputStream(new File(filepath)));
        List<CRNode[]> documents = reader.readDocument();
        reader.close();

        return documents;
    }

    // needs to be changed to accommodate salience weighting and equivalence classing
    /* public void readAllDocuments(List<Document> documents) throws IOException {
        documents.stream().forEach(document -> readDocument(document));
        CRUtils.initFeatures(np_list);
        prp_list.addAll(CRUtils.filterPronouns(np_list));
        lexana_list.addAll(CRUtils.filterLexicalAnaphors(np_list));
    } */

    public void readDocument(Document document) throws IOException {
        List<CRNode> curr_np = CRUtils.filterNouns(document.getDocument());
        CRUtils.initFeatures(curr_np);
        SalienceWeighting.measureNewSalience(curr_np);
        if (!np_list.isEmpty()) SalienceWeighting.measureOldSalience(np_list);

        List<CRNode> curr_prp = CRUtils.filterPronouns(curr_np);
        List<CRNode> curr_lexana = CRUtils.filterLexicalAnaphors(curr_np);
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

        printFeatures(nonlex_coreferents);

        Map<String, CRNodePair> nonlex_map = nonlex_coreferents.stream().collect(Collectors.toMap(pair -> pair.getFirstNode().getWordForm(), CRNodePair::self));
        Map<String, CRNodePair> lexana_map = lexana_coreferents.stream().collect(Collectors.toMap(pair -> pair.getFirstNode().getWordForm(), CRNodePair::self));
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
        List<CRNode[]> documents = new ArrayList<>();
        if (args[0] != null) documents = r.parse(args[0]);
        // else documents = r.parse("C:/Users/ethzh_000/IdeaProjects/Coref_Resolution/src/readers/test6.txt.nlp");
        List<Document> docs = new ArrayList<>();
        CRUtils.Counter counter = new CRUtils.Counter();
        documents.stream().forEach(doc -> docs.add(new Document(doc, counter.increment())));
        for (Document doc : docs) r.readDocument(doc);
        CRUtils.writeMapToFile(Constants.proj_path + "utils/coreference_output.txt", r.all_coreferents);
        // r.readAllDocuments(docs);

        // r.findNonLexicalCandidatePairs();
        // System.out.println(r.prp_list);
        // System.out.println(r.np_list);
        // System.out.println(r.lexana_list);
        // System.out.println(r.noncoref_list);
        // System.out.println(r.nonlex_candidate_pairs);

        // List<CRNodePair> binding_pairs = r.bindLexicalAnaphorPairs();
        // System.out.println(binding_pairs);
        // System.out.println(r.discourse_referents);
        // SalienceWeighting.measureNewSalience(r.np_list);
        // System.out.println(r.np_list);
        // System.out.println(r.noncoref_list);
        // System.out.println(CRUtils.max(r.noncoref_list));
        // printSalienceFactors(r.np_list);
        // r.equivalence_classes = r.discourse_referents.stream().map(r::createEquivalenceClass).collect(Collectors.toList());
        // System.out.println(r.equivalence_classes.stream().map(e -> e.getAntecedent()).collect(Collectors.toList()));

        // Resolution.printFeatures(new CRNodePair(r.np_list.get(0), r.np_list.get(1)));
        // System.out.println(Coreferential.isNonCoreferential(r.np_list.get(0), r.np_list.get(2)));
    }

    // Debugging
    // -----------------------------------------------------------------------------------------------------------------
    public static void printFeatures(CRNodePair pair) {
        CRNode fnode = pair.getFirstNode();
        CRNode snode = pair.getSecondNode();

        System.out.println(fnode.getWordForm() + " : " + snode.getWordForm());
        System.out.println(fnode.getPerson() + " : " + snode.getPerson());
        System.out.println(fnode.getGender() + " : " + snode.getGender());
        System.out.println(fnode.getNumber() + " : " + snode.getNumber());
    }

    public static void printFeatures(List<CRNodePair> pairs) {
        pairs.stream().forEach(Resolution::printFeatures);
    }

    public static void printSalienceFactors(List<CRNode> nodes) {
        for (CRNode node : nodes) {
            System.out.println(node.getWordForm());

            Map<String, Integer> sfactors = node.getSalienceFactors();
            for (String key : sfactors.keySet())
                System.out.println(key + " : " + sfactors.get(key));
            System.out.println();
        }
    }
}