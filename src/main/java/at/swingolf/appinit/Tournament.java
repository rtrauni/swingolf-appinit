package at.swingolf.appinit;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by rudolf.traunmueller on 02.12.2017.
 */
public class Tournament {
    private String place;
    private String sheetName;
    private String date;
    private String name;
    private String referee;
    private String best15;
    private String best20;
    private Collection<Player> players = new LinkedList<>();

    public void setPlace(String place) {
        this.place = place;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getBest15() {
        return best15;
    }

    public void setBest15(String best15) {
        this.best15 = best15;
    }

    public String getBest20() {
        return best20;
    }

    public void setBest20(String best20) {
        this.best20 = best20;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "place='" + place + '\'' +
                ", sheetName='" + sheetName + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", referee='" + referee + '\'' +
                ", best15='" + best15 + '\'' +
                ", best20='" + best20 + '\'' +
                '}';
    }


}
