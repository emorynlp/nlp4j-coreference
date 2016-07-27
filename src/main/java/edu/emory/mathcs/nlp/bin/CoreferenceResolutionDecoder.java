/**
 * Copyright 2016, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.nlp.bin;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class CoreferenceResolutionDecoder
{
	public static void main(String[] args) throws Exception {
//		private CRReader reader = new CRReader(1, 2, 3, 4, 5, 6, 7, 8);
//		
//		reader.open(new FileInputStream(new File(filename)));
//        List<CRNode[]> documents = reader.readDocument();
//        reader.close();
//		
//		
//        Resolution r = new Resolution();
//        /* List<CRNode[]> documents = r.parseNLP("C:/Users/ethzh_000/IdeaProjects/Coref_Resolution/src/readers/tests/test8.txt.nlp");
//        List<Document> docs = new ArrayList<>();
//        // CRUtils.Counter counter = new CRUtils.Counter();
//        // documents.forEach(doc -> docs.add(new Document(doc, counter.increment())));
//        // CREntityChunker chunker = new CREntityChunker();
//        // for (Document doc : docs) System.out.println(chunker.getEntityChunks(doc));
//        for (Document doc : docs) r.readDocument(doc); */
//
//        Scene scene = r.parseTranscript("C:/Users/ethzh_000/Documents/Emory/NLP/Big_Bang_Theory/merged/s1/010101.dep");
//        for (Utterance utterance : scene.getUtterances()) {
//            for (Sentence document : utterance.getSentences()) {
//                r.readDocument(document);
//                System.out.println(utterance.getSpeaker());
//
//                for (CRNode np : document.getNodes()) {
//                    if (np.getPerson() == 1) {
//                        System.out.print(np.toString() + " -------> " + utterance.getSpeaker() + ", ");
//                    }
//                }
//                System.out.println();
//                System.out.println();
//            }
//        }
//
//        // CRUtils.writeMapToFile(Constants.proj_path + "utils/coreference_output.txt", r.all_coreferents);
		
		
//		private CRTranscripter transcripter = CRTranscripter.DEFAULT();
//		transcripter.readScene(filename);
    }
	
//	 public boolean isTranscript(String filename) throws Exception {
//	        String ext = CRUtils.getFileExtension(filename);
//	        if (ext == null) throw new Exception("Not a valid file");
//	        else if (ext.equals(".dep")) return true;
//	        else return false;
//	    }
}
