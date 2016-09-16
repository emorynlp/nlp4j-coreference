package edu.emory.mathcs.nlp.coreference;

import org.junit.Test;
import static org.junit.Assert.*;

import edu.emory.mathcs.nlp.coreference.collection.CRNode;
import edu.emory.mathcs.nlp.coreference.collection.Equivalence;

/**
 * Created by ethzh_000 on 8/29/2016.
 */
public class CoreferenceResolutionTest {

    @Test
    public static void test() throws Exception {
        CoreferenceResolution coref_res = new CoreferenceResolution();
        coref_res.readDocument("C:/Users/ethzh_000/IdeaProjects/nlp4j-coreference/src/test/resources/parses/test2.txt.nlp");
        for (CRNode referent : coref_res.getDiscourseReferents()) {
            Equivalence equi = referent.getEquivalenceClass();
            System.out.println(referent);
            System.out.println("------------------------------------------");
            equi.getReferentList().stream().forEach(System.out::println);
            System.out.println();
        }
    }
}