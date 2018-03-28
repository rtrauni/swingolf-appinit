package at.swingolf.appinit.neo4jconverter;

import java.util.Arrays;
import java.util.Optional;

public enum Location {
    LINZ("LI","Linz"),ISERLOY("IS","Iserloy"),WESTENHOLZ("WE","Westenholz"),RA("RA","RA"),ES("ES","Essen"),HZ("HZ","Harz"),SW("SW","Schwansen"),BO("BO","BO"),PA("PA","Paulushofen"),AL("AL","Allgäu"),HO("HO","Horbach"),RE("RE","Renningen"),MA("MA","MA"),
    GUTVEHR("GV","Gut Vehr"), WA("WA","WA"), CHAMBRAYLETOURS("ChambraylesTours","ChambraylesTours"), WESTERODE("Westerode","Westerode"), MUE("MUE","MUE");
    private String name;
    private String shortcut;
    Location(String shortcut,String name) {
        this.shortcut= shortcut;
        this.name=name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public static Optional<Location> fromShortcut(String shortcut) {
        return Arrays.stream(Location.values()).filter(value -> value.getShortcut().equalsIgnoreCase(shortcut)).findFirst();
    }

    public String getName() {
        return name;
    }
}
