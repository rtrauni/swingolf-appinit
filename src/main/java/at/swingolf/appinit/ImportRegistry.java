package at.swingolf.appinit;

import at.swingolf.appinit.neo4jconverter.Club;
import at.swingolf.appinit.neo4jconverter.Location;
import at.swingolf.appinit.neo4jconverter.Person;
import at.swingolf.appinit.neo4jconverter.Tournament;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImportRegistry {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

    Map<String, Club> clubs = new HashMap<>();
    Map<String, Person> persons = new HashMap<>();
    Map<String, Tournament> tournaments = new HashMap<>();

    public Club createOrGetClub(String clubId) {
        if (clubs.containsKey(clubId)) {
            return clubs.get(clubId);
        }
        Club club = new Club(clubId);
        clubs.put(clubId, club);
        return club;
    }

    public Person createOrGetPerson(String personId) {
        if (persons.containsKey(personId)) {
            return persons.get(personId);
        }
        Person person = new Person(personId);
        persons.put(personId, person);
        return person;
    }

    public Collection<Person> getPersons() {
        return persons.values();
    }

    public Collection<Club> getClubs() {
        return clubs.values();
    }

    public Tournament createOrGetTournament(Calendar date, Location location) {
        String dateString = sdf.format(date.getTime());
        String mapkey = dateString + "_" + location.toString();
        if (!tournaments.containsKey(mapkey)) {
            tournaments.put(mapkey, new Tournament(date, location));
        }
        return tournaments.get(mapkey);
    }

    public Collection<Tournament> getTournaments() {
        return tournaments.values();
    }

    public void clear() {
        clubs.clear();
        persons.clear();
        tournaments.clear();

    }
}
