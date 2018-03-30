package at.swingolf.appinit.importplayers;

import at.swingolf.appinit.ImportRegistry;
import at.swingolf.appinit.neo4jconverter.Club;
import at.swingolf.appinit.neo4jconverter.Location;
import at.swingolf.appinit.neo4jconverter.Person;
import at.swingolf.appinit.neo4jconverter.Tournament;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

public class PlayerFromCsv {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private final List<String> entries;
    private final ImportRegistry importRegistry;
    private final int year;

    public PlayerFromCsv(List<String> entries, int year, ImportRegistry importRegistry) {
        this.entries = entries;
        this.importRegistry=importRegistry;
        this.year=year;
    }

    public void toNeo4J() {
        entries.stream().filter(s -> !s.matches("^[,]+$")).forEach(s -> createPerson(s));
    }

    private void createPerson(String string) {
        String[] person = string.split(",");
        String club = person[0];
        String name = person[1];
        String license = person[2];
        String category= person[4];
        String handicap= person[5];
        Validate.notEmpty(license);
        String firstname = person[47];

        int dateIndex = 7;
        int resultIndex= 53;
//        int resultIndex = 7 // 53
        String date = null;
        while (!StringUtils.isEmpty(date = person[dateIndex])) {
            String result = person[resultIndex];
            dateIndex = dateIndex+2;
            resultIndex = resultIndex+2;
            Date parsedDate = null;
            try {
                parsedDate = sdf.parse(StringUtils.substringBeforeLast(date, ".")+"."+year);
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
            Optional<Location> location = Location.fromShortcut(StringUtils.substringAfterLast(date,"."));
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(parsedDate);
            if (!location.isPresent()) {
                throw new RuntimeException("location couldn't be parsed "+date);
            }
            Tournament tournament = importRegistry.createOrGetTournament(calendar, location.get());
        }

        Club c = importRegistry.createOrGetClub(club);
        Person p = importRegistry.createOrGetPerson(license);
        p.addClubForYear(c,year);
        p.setName(name);
        p.setFirstname(firstname);
        p.setCategory(category);
        p.setHandicap(Double.parseDouble(handicap));
    }
}
