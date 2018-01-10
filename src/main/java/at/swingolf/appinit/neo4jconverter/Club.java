package at.swingolf.appinit.neo4jconverter;

public class Club extends Neo4jBaseDto{
    private final String id;
    private String name;

    public Club(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String toNeo4j() {
        StringBuffer sb = createStringBuffer();
        sb.append("CREATE ("+getKey()+ ":Club {name:'" + getName() + "'})\n");
        String durationKey = "duration2017toNowFor" + getKey();
        sb.append("CREATE (" + durationKey + ":Duration {from: 20170101, to: null})\n");
        sb.append("CREATE ("+getKey() + ")-[:IS_ACTIVE]->(" + durationKey + ")\n");
        return sb.toString();
    }

    @Override
    Object[] getKeyInternal() {
        return new Object[]{getId()};
    }

    public String getName() {
        return name;
    }
}
