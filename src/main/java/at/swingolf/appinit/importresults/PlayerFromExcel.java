package at.swingolf.appinit.importresults;

import org.springframework.util.Assert;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by rudolf.traunmueller on 02.12.2017.
 */
public class PlayerFromExcel {
    private String name;

    private String license;

    private Double oldHandicap;

    private Integer result;

    private Integer resultAfterCorrecture;

    private Map<Integer,Integer> scores = new TreeMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        Assert.hasText(license, "the license for player " + name + " mustn't be empty");
        this.license = license;
    }

    public Double getOldHandicap() {
        return oldHandicap;
    }

    public void setOldHandicap(Double oldHandicap) {
        this.oldHandicap = oldHandicap;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getResultAfterCorrecture() {
        return resultAfterCorrecture;
    }

    public void setResultAfterCorrecture(Integer resultAfterCorrecture) {
        this.resultAfterCorrecture = resultAfterCorrecture;
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
                ", license='" + license + '\'' +
                ", oldHandicap=" + oldHandicap +
                ", result=" + result +
                ", resultAfterCorrecture=" + resultAfterCorrecture +
                ", scores=" + scores +
                '}';
    }
}
