package edu.emory.mathcs.nlp.bin;

import static org.junit.Assert.*;

import edu.emory.mathcs.nlp.coreference.util.CRUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.nlp.common.util.IOUtils;
import edu.emory.mathcs.nlp.common.util.Splitter;
import edu.emory.mathcs.nlp.coreference.collection.Sentence;
import edu.emory.mathcs.nlp.coreference.collection.CRNode;

/**
 * Created by ethzh_000 on 7/28/2016.
 */
public class CoreferenceResolutionDecoderTest {

    public static List<String[]> readGoldGoldStandard(BufferedReader reader) throws IOException {
        List<String[]> node_pairs = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) node_pairs.add(Splitter.splitTabs(line));

        return node_pairs;
    }

    @Test
    public static void test() throws Exception {
        CoreferenceResolutionDecoder decoder = new CoreferenceResolutionDecoder("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/decoder/test.txt.nlp");
        decoder.exe();
        Map<CRNode, CRNode> node_map = CRUtils.readFiletoMap("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/decoder/coreferents.txt");
        BufferedReader reader = IOUtils.createBufferedReader("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/decoder/gold.txt");
        List<String[]> node_pairs = readGoldGoldStandard(reader);
        for (String[] node_pair : node_pairs) {

        }
    }
}