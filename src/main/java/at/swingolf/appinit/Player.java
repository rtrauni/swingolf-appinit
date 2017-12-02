package at.swingolf.appinit;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rudolf.traunmueller on 02.12.2017.
 */
public class Player {
    private String name;

    private Map<Integer,Integer> scores = new TreeMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<Integer, Integer> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", scores=" + scores +
                '}';
    }
}
