package readers;

import nodes.CRNode;

import edu.emory.mathcs.nlp.component.template.reader.TSVReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

/**
 * Created by ethzh_000 on 7/4/2016.
 */
public class CRReader extends TSVReader<CRNode> {

    public CRReader() {
        super();
    }

    public CRReader(Object2IntMap<String> map) {
        super(map);
    }

    public CRReader(int form, int lemma, int pos, int feats, int dhead, int deprel, int sheads, int nament) {
        super(form, lemma, pos, feats, dhead, deprel, sheads, nament);
    }

    public CRNode create() {
        return new CRNode();
    }
}