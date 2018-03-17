package at.swingolf.appinit;

import at.swingolf.appinit.importplayers.PlayerFromCsvReader;
import at.swingolf.appinit.importresults.TournamentFromExcelReader;
import at.swingolf.appinit.importresults.TournamentFromHttpReader;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class HelloController {
    @Autowired
    PlayerFromCsvReader playerFromCsvReader;
    @Autowired
    TournamentFromExcelReader tournamentFromExcelReader;
    @Autowired
    TournamentFromHttpReader tournamentFromHttpReader;
    @Autowired
    ImportRegistry importRegistry;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/testtournamentfromhttp")
    public String index1() {
        tournamentFromHttpReader.importTournaments();
        importRegistry.getTournaments().forEach(tournament -> System.out.println(tournament));
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/import")
    public synchronized String importer() throws IOException {
        importRegistry.clear();

        System.out.println("importing new tournaments");
        tournamentFromHttpReader.importTournaments();
        System.out.println("importing Results");
        playerFromCsvReader.importTournaments();
        System.out.println("importing Tournaments");
        tournamentFromExcelReader.importTournaments();
        System.out.println("importing done");
        System.out.println("#### NEO4J ####");
        String persons = importRegistry.getPersons().stream().map(person -> person.toNeo4j()).collect(Collectors.joining("\n"));
        String clubs = importRegistry.getClubs().stream().map(club -> club.toNeo4j()).collect(Collectors.joining("\n"));
        String tournaments = importRegistry.getTournaments().stream().map(tournament -> tournament.toNeo4j()).collect(Collectors.joining("\n"));

        delete();
        String s = (persons + clubs);
        write(s, 1);

        AtomicInteger counter = new AtomicInteger(0);
        importRegistry.getTournaments().stream().map(tournament -> tournament.toNeo4j()).forEach(str -> write(persons + str,counter.getAndIncrement()));


        FileUtils.writeStringToFile(new File("output.txt"), s);
        return s.replaceAll("\n", "<br>");
    }

    private void delete() {
        Neo4jBridge bridge = new Neo4jBridge("bolt://neo4j:7687", "neo4j", "test");
//        System.out.println("about to write "+cypher.length()+" long cypher to neo4j db");
        String delete = "MATCH (n) DETACH DELETE n\n";
        bridge.execute(delete);
    }

    private void write(String cypher, int andIncrement) {
        System.out.println(andIncrement + "/"+importRegistry.getTournaments().size());
        Neo4jBridge bridge = new Neo4jBridge("bolt://localhost:7687", "neo4j", "test");
//        System.out.println("about to write "+cypher.length()+" long cypher to neo4j db");
        bridge.execute(cypher);
//        System.out.println("wrote "+cypher.length()+" long cypher to neo4j db");
    }


}