package at.swingolf.appinit.importresults;

import at.swingolf.appinit.ImportRegistry;
import at.swingolf.appinit.ReadSGDDDates;
import at.swingolf.appinit.neo4jconverter.Location;
import at.swingolf.appinit.neo4jconverter.Tournament;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

public class TournamentFromHttp {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private final ReadSGDDDates.Entry entry;
    private final ImportRegistry importRegistry;

    public TournamentFromHttp(ImportRegistry importRegistry, ReadSGDDDates.Entry entry) {
        this.entry = entry;
        this.importRegistry = importRegistry;


    }

    public void toNeo4J() {
        Date parsedDate = null;
        try {
            String text = entry.getDate();
            if (entry.getDate().contains("-")) {
                text = StringUtils.substringAfterLast(entry.getDate(), "-");
            }
            if (text.equals("Sep. 2018")) {
                parsedDate = new Date(2018,10,1);
            } else {
                parsedDate = sdf.parse(text);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        Location location = null;
        String where = entry.getWhere().toLowerCase();
        if (where.contains("westenholz")) {
            location = Location.WESTENHOLZ;
        } else if (where.contains("linz")) {
            location = Location.LINZ;
        } else if (where.contains("renningen")) {
            location = Location.RE;
        } else if (where.contains("essen")) {
            location = Location.ES;
        } else if (where.contains("iserloy")) {
            location = Location.ISERLOY;
        } else if (where.contains("rastede")) {
            location = Location.RA;
        } else if (where.contains("horbach")) {
            location = Location.HO;
        } else if (where.contains("alling")) {
            location = Location.AL;
        } else if (where.contains("waabs")) {
            location = Location.WA;
        } else if (where.contains("markdorf")) {
            location = Location.MA;
        } else if (where.contains("chambray")) {
            location = Location.ChambraylèsTours;
        } else if (where.contains("westerode")) {
            location = Location.WESTERODE;
        } else if (where.contains("paulushofen")) {
            location = Location.PA;
        } else if (where.contains("münsterland")) {
            location = Location.MÜ;
        } else if (where.contains("göttingen")) {
        } else
         {
            throw new RuntimeException("not supported");
        }
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);

        if (location != null) {

        Tournament tournament = importRegistry.createOrGetTournament(calendar, location);
        tournament.setName(entry.getWhat());
        }

    }
}
