package at.swingolf.appinit.neo4jconverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends Neo4jBaseDto {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private Tournament tournament;
    private String name;
    private Calendar date;

    public Game(Tournament tournament, String name, Calendar date) {
        this.tournament = tournament;
        this.name = name;
        this.date = date;
    }

    private List<Score> scores = new LinkedList<>();

    public String getName() {
        return name;
    }

    public Calendar getDate() {
        return date;
    }

    public void addScore(Score score) {
        scores.add(score);
    }

    public String toNeo4j() {
        StringBuffer sb =createStringBuffer();
            sb.append("CREATE ("+getKey()+":Game {name: '"+getName()+"'})\n");

            sb.append(scores.stream().map(score -> score.toNeo4j()).collect(Collectors.joining("\n")));

            sb.append("\n");
        System.out.println(sb.toString());
            return sb.toString();
    }

    @Override
    Object[] getKeyInternal() {
        String dateString = sdf.format(date.getTime());
        return new Object[]{dateString,tournament.getLocation()};
    }
}
