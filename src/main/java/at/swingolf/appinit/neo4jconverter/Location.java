package at.swingolf.appinit.neo4jconverter;

import java.util.Arrays;
import java.util.Optional;

public enum Location {
    LINZ("LI"),ISERLOY("IS"),WESTENHOLZ("WE"),RA("RA"),ES("ES"),HZ("HZ"),SW("SW"),BO("BO"),PA("PA"),AL("AL"),HO("HO"),RE("RE"),MA("MA"),GUTVEHR("GV"), WA("WA"), CHAMBRAYLETOURS("ChambraylesTours"), WESTERODE("Westerode"), MÜ("MÜ");
    private String shortcut;
    Location(String shortcut) {
        this.shortcut= shortcut;
    }

    public String getShortcut() {
        return shortcut;
    }

    public static Optional<Location> fromShortcut(String shortcut) {
        return Arrays.stream(Location.values()).filter(value -> value.getShortcut().equalsIgnoreCase(shortcut)).findFirst();
    }
}
