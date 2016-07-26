package utils;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import chunkers.CREntityChunker;
import nodes.CRNode;
import nodes.CRNodePair;
import nodes.CREntity;

/**
 * Created by ethzh_000 on 7/1/2016.
 */
public class CRUtils {

    public static class Counter {
        private int index = 0;

        public Counter() {
        }

        public void reset() {
            index = 0;
        }

        public int increment() {
            return index++;
        }
    }

    public static BufferedReader getBufferedReader(String file_path) throws IOException {
        return new BufferedReader(new FileReader(new File(file_path)));
    }

    public static String getFileExtension(String filename) {
        Pattern p = Pattern.compile(".+([.]\\w+)");
        Matcher m = p.matcher(filename);

        return (m.find()) ? m.group(1) : null;
    }

    public static boolean compare(CRNode np1, CRNode np2) {
        return np1.compareTo(np2) == 1;
    }

    public static CRNodePair max(List<CRNodePair> pairs) {
        return Collections.max(pairs, new CRNodePairComparator());
    }

    public static Set<String> determinerTagSet() {
        Set<String> dset = new HashSet<>();
        dset.add("PRP$");
        dset.add("WP$");
        dset.add("WDT");
        dset.add("PDT");

        return dset;
    }

    public static <K, V> void writeMapToFile(String filename, Map<K, V> map) throws IOException {
        File outputFile = new File(filename);
        if (!outputFile.exists()) outputFile.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (Map.Entry<K, V> entry : map.entrySet())
            writer.write(entry.getKey() + "\t--------->\t" + entry.getValue().toString() + "\n");

        writer.close();
    }

    public static Set<String> readTextFileSet(String filename) throws IOException {
        BufferedReader reader = getBufferedReader(filename);

        Set<String> set = new HashSet<>();
        String line;
        while ((line = reader.readLine()) != null) set.add(line);

        reader.close();
        return set;
    }

    public static Set<String> readNameFileSet(String filename) throws IOException {
        BufferedReader reader = getBufferedReader(filename);

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
        return readTextFileSet(Constants.proj_path + "utils/files/masculine_nouns.txt");
    }

    public static Set<String> feminineNouns() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/files/feminine_nouns.txt");
    }

    public static Set<String> maleNames() throws IOException {
        return readNameFileSet(Constants.proj_path + "utils/files/male_names.txt");
    }

    public static Set<String> femaleNames() throws IOException {
        return readNameFileSet(Constants.proj_path + "utils/files/female_names.txt");
    }

    public static Set<String> definiteDeterminers() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/files/def_det.txt");
    }

    public static Set<String> indefiniteDeterminers() throws IOException {
        return readTextFileSet(Constants.proj_path + "utils/files/indef_det.txt");
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

            if (GrammarUtils.isPlural(word)) word.setNumber("pl");

            if (m_nouns.contains(lemma) || m_names.contains(word_form)) word.setGender("m");
            else if (f_nouns.contains(lemma) || f_names.contains(word_form)) word.setGender("f");

            if (Constants.fp_set.contains(lemma)) word.setPerson(1);
            else if (Constants.sp_set.contains(lemma)) word.setPerson(2);
            else if (Constants.tp_set.contains(lemma)) word.setPerson(3);
        }
    }

    public static List<CRNode> filterNouns(List<CRNode> sentence) {
        return sentence.stream().filter(GrammarUtils::isNominal).collect(Collectors.toList());
    }

    public static List<CRNode> filterPronouns(List<CRNode> nouns) {
        return nouns.stream().filter(GrammarUtils::isPronoun).collect(Collectors.toList());
    }

    public static List<CRNode> filterLexicalAnaphors(List<CRNode> nouns) {
        List<CRNode> lexana_list = nouns.stream().filter(GrammarUtils::isLexical).collect(Collectors.toList());
        nouns.removeAll(lexana_list);

        return lexana_list;
    }

    public static List<CRNode> filterDefiniteNouns(List<CRNode> nouns) throws IOException {
        List<CRNode> def_list = new ArrayList<>();
        for (CRNode noun : nouns)
            if (GrammarUtils.isDefinite(noun)) def_list.add(noun);

        return def_list;
    }

    public static List<CRNode> filterIndefiniteNouns(List<CRNode> nouns) throws IOException {
        List<CRNode> indef_list = new ArrayList<>();
        for (CRNode noun : nouns)
            if (GrammarUtils.isIndefinite(noun)) indef_list.add(noun);

        return indef_list;
    }
}