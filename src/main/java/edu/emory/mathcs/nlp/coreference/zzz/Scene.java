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
package edu.emory.mathcs.nlp.coreference.zzz;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ethan Zhou, Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Scene
{
    private List<Utterance> utterances;
    private String title;

    public Scene() {
        utterances = new ArrayList<>();
        title = "";
    }

    public Scene(List<Utterance> utterances) {
        this.utterances = utterances;
        title = "";
    }

    public Scene(String scene_title, List<Utterance> utterances) {
        this.utterances = utterances;
        this.title = scene_title;
    }

    public void addUtterance(Utterance utter) {
        utterances.add(utter);
    }

    public void setUtterances(List<Utterance> utters) {
        utterances = utters;
    }

    public void setSceneTitle(String title) {
        this.title = title;
    }

    public List<Utterance> getUtterances() {
        return utterances;
    }

    public String getSceneTitle() {
        return title;
    }
}
