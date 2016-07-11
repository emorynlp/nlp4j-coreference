package utils;

import java.io.*;
import java.util.*;

import nodes.CRNode;
import nodes.CRNodePair;

/**
 * Created by ethzh_000 on 7/1/2016.
 */
public class CRUtils {

    public static class Counter {
        private int index = 0;

        public Counter() { }
        public void reset() {
            index = 0;
        }
        public int increment() {
            return index++;
        }
    }

    public static boolean compare(CRNode np1, CRNode np2) {
        return np1.compareTo(np2) == 1;
    }

    public static CRNodePair max(List<CRNodePair> pairs) {
        return Collections.max(pairs, new CRNodePairComparator());
    }

    public static boolean featuresMatch(CRNode np1, CRNode np2) {
        if (np1.getPerson() != np2.getPerson()) return false;
        if (!np1.getGender().equals(np2.getGender())) return false;
        if (!np1.getNumber().equals(np2.getNumber())) return false;

        return true;
    }

    public static boolean isContainedIn(CRNode np1, CRNode np2) {
        if (Domain.inArgumentDomain(np1, np2)) return true;
        if (Domain.inAdjunctDomain(np1, np2)) return true;

        return false;
    }

    public static Set<String> determinerTagSet() {
        Set<String> dset = new HashSet<>();
        dset.add("PRP$");
        dset.add("WP$");
        dset.add("WDT");
        dset.add("PDT");

        return dset;
    }

    // inefficient - needs to be fixed
    public static boolean isDeterminer(CRNode np) {
        Set<String> det_set = determinerTagSet();
        String pos_tag = np.getPartOfSpeechTag();
        if (det_set.contains(pos_tag)) return true;

        List<CRNode> dlist = np.getDependentList();
        boolean a_match = dlist.stream().anyMatch(ditem -> ditem.getPartOfSpeechTag().equals("POS"));

        return a_match;
    }

    public static boolean isDeterminer(CRNode np, CRNode np_pred) {
        return isDeterminer(np) && np.isDependentOf(np_pred);
    }

    // unable to filter "other" as in "each [other]" - needs to be fixed
    public static boolean isLexical(CRNode np) {
        String lemma = np.getLemma();
        int n = lemma.length();

        if (n > 4 && lemma.substring(n - 4, n).equals("self")) return true;
        else if (n > 6 && lemma.substring(n - 6, n).equals("selves")) return true;

        return false;
    }

    public static boolean isPronoun(CRNode noun) {
        String pos_tag = noun.getPartOfSpeechTag();
        if (pos_tag.length() >= 3) {
            if (pos_tag.substring(0, 3).equals("PRP")) return true;
        } else if (pos_tag.length() >= 2) {
            if (pos_tag.substring(0, 2).equals("WP")) return true;
        }

        return false;
    }

    public static boolean isPlural(CRNode noun) {
        if (isPronoun(noun)) {
            if (Constants.sprp_set.contains(noun.getLemma())) return false;
            else if (Constants.pprp_set.contains(noun.getLemma())) return true;
        } else {
            String pos_tag = noun.getPartOfSpeechTag();
            switch (pos_tag) {
                case "NNS": return true;
                case "NNPS": return true;
            }
        }

        return false;
    }

    // inefficient - needs to be fixed
    public static boolean isDefinite(CRNode nn_dep) throws IOException {
        if (nn_dep.getDependencyLabel().equals("poss")) return true;
        String pos_tag = nn_dep.getPartOfSpeechTag();
        if (determinerTagSet().contains(pos_tag)) return true;
        if (definiteDeterminers().contains(nn_dep.getLemma())) return true;

        return false;
    }

    // inefficient - needs to be fixed
    public static boolean isIndefinite(CRNode nn_dep) throws IOException {
        if (nn_dep.getDependencyLabel().equals("predet")) return true;
        String pos_tag = nn_dep.getPartOfSpeechTag();
        if (pos_tag.equals("CD")) return true;
        if (pos_tag.equals("PDT")) return true;
        if (indefiniteDeterminers().contains(nn_dep.getLemma())) return true;

        return false;
    }

    public static boolean isCataphoric(CRNodePair pair) {
        return isCataphoric(pair.getFirstNode(), pair.getSecondNode());
    }

    public static boolean isCataphoric(CRNode ante, CRNode post) {
        return ante.getID() > post.getID();
    }

    public static <K, V> void writeMapToFile(String filename, Map<K, V> map) throws IOException {
        File outputFile = new File(filename);
        if (!outputFile.exists()) outputFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Map.Entry<K, V> entry : map.entrySet())
            writer.write(entry.getKey() + "\t--------->\t" + entry.getValue().toString() + "\n");

        writer.close();
    }

    public static Set<String> readTextFileSet(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        Set<String> set = new HashSet<>();
        String line;
        while ((line = reader.readLine()) != null) set.add(line);

        reader.close();
        return set;
    }

    public static Set<String> readNameFileSet(String filename) throws IOException {
        BufferedReader reader = new BufferedReader((new FileReader((new File(filename)))));

        Set<String> set = new HashSet<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] str_num_split = line.split("\\w");
            set.add(str_num_split[0]);
        }

        reader.close();
        return set;
    }

    public static Set<String> masculineNouns() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/masculine_nouns.txt");
    }

    public static Set<String> feminineNouns() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/feminine_nouns.txt");
    }

    public static Set<String> maleNames() throws IOException {
        return readNameFileSet(Constants.proj_path + "utils/male_names.txt");
    }

    public static Set<String> femaleNames() throws IOException {
        return readNameFileSet(Constants.proj_path + "utils/female_names.txt");
    }

    public static Set<String> definiteDeterminers() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/def_det.txt");
    }

    public static Set<String> indefiniteDeterminers() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/indef_det.txt");
    }

    public static void initFeatures(List<CRNode> nouns) throws IOException {
        // expensive operations - need to fix
        Set<String> m_nouns = CRUtils.masculineNouns();
        Set<String> f_nouns = CRUtils.feminineNouns();
        Set<String> m_names = CRUtils.maleNames();
        Set<String> f_names = CRUtils.femaleNames();

        for (CRNode word : nouns) {
            String lemma = word.getLemma();
            String word_form = word.getWordForm();

            if (isPlural(word)) word.setNumber("pl");

            if (m_nouns.contains(lemma) || m_names.contains(word_form)) word.setGender("m");
            else if (f_nouns.contains(lemma) || f_names.contains(word_form)) word.setGender("f");

            if (Constants.fp_set.contains(lemma)) word.setPerson(1);
            else if (Constants.sp_set.contains(lemma)) word.setPerson(2);
            else if (Constants.tp_set.contains(lemma)) word.setPerson(3);
        }
    }

    public static List<CRNode> filterNouns(List<CRNode> sentence) {
        List<CRNode> nn_list = new ArrayList<>();

        for (CRNode word : sentence) {
            String pos_tag = word.getPartOfSpeechTag();
            int pos_len = pos_tag.length();

            if (pos_len < 2) ;
            else if (pos_tag.length() >= 3) {
                if (pos_tag.substring(0, 3).equals("PRP")) nn_list.add(word);
                else if (pos_tag.substring(0, 2).equals("NN")) nn_list.add(word);
                else if (pos_tag.substring(0, 2).equals("WP")) nn_list.add(word);
            } else {
                if (pos_tag.substring(0, 2).equals("NN")) nn_list.add(word);
                else if (pos_tag.substring(0, 2).equals("WP")) nn_list.add(word);
            }
        }

        return nn_list;
    }

    public static List<CRNode> filterPronouns(List<CRNode> nouns) {
        List<CRNode> prp_list = new ArrayList<>();

        for (CRNode noun : nouns) {
            if (isLexical(noun)) continue;
            if (isPronoun(noun)) prp_list.add(noun);
        }

        return prp_list;
    }

    public static List<CRNode> filterLexicalAnaphors(List<CRNode> nouns) {
        List<CRNode> lexana_list = new ArrayList<>();

        for (CRNode noun : nouns)
            if (isLexical(noun))
                lexana_list.add(noun);

        nouns.removeAll(lexana_list);

        return lexana_list;
    }

    public static List<CRNode> filterDefiniteNouns(List<CRNode> nouns) throws IOException {
        List<CRNode> def_list = new ArrayList<>();
        for (CRNode noun : nouns)
            if (isDefinite(noun)) def_list.add(noun);

        return def_list;
    }

    public static List<CRNode> filterIndefiniteNouns(List<CRNode> nouns) throws IOException {
        List<CRNode> indef_list = new ArrayList<>();
        for (CRNode noun : nouns)
            if (isIndefinite(noun)) indef_list.add(noun);

        return indef_list;
    }
}