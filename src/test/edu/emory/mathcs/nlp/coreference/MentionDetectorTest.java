package edu.emory.mathcs.nlp.coreference;

import static org.junit.Assert.*;

import org.junit.Test;

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
        List<CRNode> gold = extract(md, reader, "C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/mentions/gold_mentions.txt.nlp", false);
        assertTrue(equals(test, gold));
    }

    protected static boolean equals(List<CRNode> array1, List<CRNode> array2) {
        return array1.toString().equals(array2.toString());
    }

    protected static List<CRNode> extract(MentionDetector md, CRReader reader, String filename, boolean test) throws Exception {
        reader.open(IOUtils.createFileInputStream(filename));
        List<CRNode> mentions = null;
        for (CRNode[] nodes : reader.readDocument()) {
            mentions = (test) ? md.getMentions(nodes) : Arrays.asList(nodes).stream().filter(ENGrammarUtils::isNominal).collect(Collectors.toList());
        }
        reader.close();
        return mentions;
    }
}