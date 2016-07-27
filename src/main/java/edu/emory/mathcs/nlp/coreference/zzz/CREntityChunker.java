package edu.emory.mathcs.nlp.coreference.zzz;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.nlp.coreference.collection.CRNode;

/**
 * Created by ethzh_000 on 7/13/2016.
 */
public class CREntityChunker {

    public CREntityChunker() {

    }

//    public List<CREntity> getEntityChunks(Sentence document) {
//        return getEntityChunks(CRUtils.filterNouns(document.getNodes()));
//    }

    public List<CREntity> getEntityChunks(List<CRNode> nouns) {
        List<CREntity> entities = new ArrayList<>();
        CREntity entity = null;
        List<CRNode> compounds = new ArrayList<>();

        for (CRNode noun : nouns) {
            char bilou = noun.getNamedEntityTag().charAt(0);
            switch (bilou) {
                case 'B':
                    entity = new CREntity(noun);
                    entities.add(entity);
                    break;
                case 'I':
                    if (entity != null) entity.addEntityDependent(noun);
                    continue;
                case 'L':
                    if (entity != null) entity.addEntityDependent(noun);
                    continue;
                case 'O':
                    String dep_label = noun.getDependencyLabel();
                    if (dep_label.equals("compound")) compounds.add(noun);
                    else {
                        entity = new CREntity(noun);
                        if (compounds.size() > 0) {
                            entity.addAllDependents(compounds);
                            compounds = new ArrayList<>();
                        }
                        entities.add(entity);
                    }
                    break;
                case 'U':
                    entity = new CREntity(noun);
                    entities.add(entity);
                    break;
            }
        }

        return entities;
    }
}
