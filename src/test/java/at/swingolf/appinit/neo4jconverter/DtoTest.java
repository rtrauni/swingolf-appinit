package at.swingolf.appinit.neo4jconverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

class DtoTest {

    Club sgcLinz = new Club("sgclinz");
    Person rudolf = new Person("007-0021");

    Calendar tournamentDate = new GregorianCalendar(2017,Calendar.JANUARY,1);
    Tournament tournament = new Tournament(tournamentDate,Location.LINZ);

    Game game = new Game(tournament,"Südliga Linz Tag 1",tournamentDate);

    Score score = new Score(rudolf,1,3,game);

    @BeforeEach
    void before(){
        rudolf.setFirstname("Rudolf");
        rudolf.setName("Traunmüller");
        rudolf.addClubForYear(sgcLinz,2017);

        game.addScore(score);
        tournament.addGame(game);
    }

    @Test
    void scoreToNeo4j() {
        System.out.println(score.toNeo4j());
    }

    @Test
    void personToNeo4j() {
        System.out.println(rudolf.toNeo4j());
    }

    @Test
    void clubToNeo4j() {
        System.out.println(sgcLinz.toNeo4j());
    }

    @Test
    void gameToNeo4j() {
        System.out.println(game.toNeo4j());
    }

    @Test
    void tournamentToNeo4j() {
        System.out.println(tournament.toNeo4j());
    }


}