package at.swingolf.appinit.importresults;

import at.swingolf.appinit.ImportRegistry;
import at.swingolf.appinit.neo4jconverter.*;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rudolf.traunmueller on 02.12.2017.
 */
public class TournamentFromExcel {
    private final ImportRegistry importRegistry;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private String place;
    private String sheetName;
    private String date;
    private String name;
    private String referee;
    private String best15;
    private String best20;
    private Collection<PlayerFromExcel> playerFromExcels = new LinkedList<>();
    private Integer year;

    public TournamentFromExcel(ImportRegistry importRegistry) {
        this.importRegistry = importRegistry;
    }

    public void toNeo4J() {
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(StringUtils.substringBeforeLast(date, ".") + "." + year);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        Optional<Location> location = Location.fromShortcut(StringUtils.substringAfterLast(date, "."));
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(parsedDate);
        if (!location.isPresent()) {
            throw new RuntimeException("location couldn't be parsed " + date + "; " + sheetName + "; " + name);
        }
        Tournament tournament = importRegistry.createOrGetTournament(calendar, location.get());
        tournament.setName(getName());
        Integer correcture = null;
        if (!StringUtils.isEmpty(best15)) {
            correcture = Integer.valueOf(best15);
            tournament.setCorrecture(correcture);
        }
        if (!StringUtils.isEmpty(best20)) {
            Integer best20Int = Integer.valueOf(best20);
            if (best20Int < correcture) {
                tournament.setCorrecture(best20Int);
            }
        }
        Game game = new Game(tournament, tournament.getKey(), calendar);
        tournament.addGame(game);
        playerFromExcels.stream().forEach(playerFromExcel -> {
            Person person = importRegistry.createOrGetPerson(playerFromExcel.getLicense());
            person.addHandicap(game.getDate(),playerFromExcel.getOldHandicap());
            playerFromExcel.getScores().forEach((hole, score) -> {
                Score s = new Score(person, hole, score, game);
                game.addScore(s);
            });
//            tournament.addPlayer(person);
        });
    }

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

    public void setYear(Integer year) {
        this.year = year;
    }

    public Collection<PlayerFromExcel> getPlayerFromExcels() {
        return playerFromExcels;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "place='" + place + '\'' +
                ", sheetName='" + sheetName + '\'' +
                ", year='"+year + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", referee='" + referee + '\'' +
                ", best15='" + best15 + '\'' +
                ", best20='" + best20 + '\'' +
                '}';
    }


}
