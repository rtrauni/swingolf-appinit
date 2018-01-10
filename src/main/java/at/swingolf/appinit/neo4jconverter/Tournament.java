package at.swingolf.appinit.neo4jconverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Tournament extends Neo4jBaseDto{
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private final Calendar date;
    private final Location location;
    private final List<Game> games = new LinkedList<>();
    private String name;

    public Tournament(Calendar date, Location location) {
        this.date=date;
        this.location=location;
    }

    public Calendar getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public String toNeo4j() {
        StringBuffer sb =createStringBuffer();
        sb.append("CREATE ("+getKey()+":Tournament {name: '"+getName()+"'})\n" +
                "CREATE (duration"+getKey()+":Duration {from: "+sdf.format(getDate().getTime())+", to: "+sdf.format(getDate().getTime())+"})\n" +
                "CREATE ("+getKey()+")-[:HAS_DATE]->(duration"+getKey()+")\n");

        sb.append(games.stream().map(game -> game.toNeo4j()).collect(Collectors.joining("\n")));
        sb.append("\n");

        sb.append(games.stream().map(game -> "CREATE ("+getKey()+")-[:HAS_GAME]->("+game.getKey()+")\n").collect(Collectors.joining("\n")));
        sb.append("\n");

        return sb.toString();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    @Override
    Object[] getKeyInternal() {
        String dateString = sdf.format(date.getTime());
        return new Object[]{dateString,location};
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
