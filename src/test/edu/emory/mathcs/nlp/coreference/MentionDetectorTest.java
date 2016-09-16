package edu.emory.mathcs.nlp.coreference;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.util.reader.CRReader;
import edu.emory.mathcs.nlp.coreference.util.ENGrammarUtils;

/**
 * Created by ethzh_000 on 8/9/2016.
 */
public class MentionDetectorTest {

    @Test
    public void getMentionsTest() throws Exception {
        MentionDetector md = new MentionDetector();
        CRReader reader = CRReader.DEFAULT();
        List<CRNode> test = extract(md, reader, "C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/mentions/test_mentions.txt.nlp", true);
        String gold_file = "C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/mentions/gold_mentions.txt.nlp";
        assertTrue(match(test, gold_file));
    }

    protected static boolean match(List<CRNode> test, String gold) throws IOException {
        BufferedReader buf_reader = IOUtils.createBufferedReader(gold);
        String line;
        for (CRNode tnode : test) {
            if ((line = buf_reader.readLine()) == null) return false;
            String[] ids = line.split("\t");
            if (!(Integer.parseInt(ids[0]) == tnode.getSentenceID()) || !(Integer.parseInt(ids[1]) == tnode.getID()) || !(ids[2].equals(tnode.getWordForm()))) return false;
        }

        return true;
    }

    protected static List<CRNode> extract(MentionDetector md, CRReader reader, String filename, boolean test) throws Exception {
        reader.open(IOUtils.createFileInputStream(filename));
        List<CRNode> mentions = new ArrayList<>();
        for (CRNode[] nodes : reader.readDocument()) mentions.addAll(md.getMentions(nodes));
        reader.close();
        return mentions;
    }
}