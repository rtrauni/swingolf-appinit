package at.swingolf.appinit.neo4jconverter;

import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Person extends Neo4jBaseDto {
    private final String id;
    Map<Integer, Club> clubPerYear = new HashMap<>();
    Map<Calendar, Double> oldHandicaps = new TreeMap<>();
    private String name;
    private String firstname;
    private String category;
    private double handicap;

    public Person(String personId) {
        Assert.notNull(personId, "The personId mustn't be null.");
        this.id = personId;

    }

    public void addClubForYear(Club c, int year) {
        clubPerYear.put(year, c);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String toNeo4j() {
        StringBuffer sb = createStringBuffer();
        sb.append("CREATE (" + getKey() + ":User {firstname:'" + getFirstname() + "', lastname:'" + getName() + "', category:'" + getCategory() + "', handicap:'" + getHandicap() + "'})\n");
        sb.append("CREATE (license" + getKey() + ":License {license:'" + getId() + "'})\n");
        sb.append("CREATE (duration2017toNow" + getKey() + ":Duration {from: 201700101, to: null})\n");
        sb.append("CREATE (license" + getKey() + ")-[:IS_ACTIVE]->(duration2017toNow" + getKey() + ")\n");
        sb.append("CREATE (" + getKey() + ")-[:HAS_LICENSE]->(license" + getKey() + ")\n");
        return sb.toString();
    }

    @Override
    Object[] getKeyInternal() {
        return new Object[]{getId()};
    }

    public void addHandicap(Calendar date, Double oldHandicap) {
        oldHandicaps.put(date, oldHandicap);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setHandicap(double handicap) {
        this.handicap = handicap;
    }

    public double getHandicap() {
        return handicap;
    }
}
