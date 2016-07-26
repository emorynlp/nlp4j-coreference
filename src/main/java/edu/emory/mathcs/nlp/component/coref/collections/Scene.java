package collections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethzh_000 on 7/25/2016.
 */
public class Scene {

    private List<Utterance> utterances;
    private String scene_title;

    public Scene() {
        utterances = new ArrayList<>();
        scene_title = "";
    }

    public Scene(List<Utterance> utterances) {
        this.utterances = utterances;
        scene_title = "";
    }

    public Scene(String scene_title, List<Utterance> utterances) {
        this.utterances = utterances;
        this.scene_title = scene_title;
    }

    public void addUtterance(Utterance utter) {
        utterances.add(utter);
    }

    public void setUtterances(List<Utterance> utters) {
        utterances = utters;
    }

    public void setSceneTitle(String title) {
        scene_title = title;
    }

    public List<Utterance> getUtterances() {
        return utterances;
    }

    public String getSceneTitle() {
        return scene_title;
    }
}
