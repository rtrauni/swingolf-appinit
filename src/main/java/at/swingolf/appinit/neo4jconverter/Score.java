package at.swingolf.appinit.neo4jconverter;

public class Score extends Neo4jBaseDto{
    private Person person;
    private Integer hole;
    private Integer score;
    private Game game;


    public Score(Person person, Integer hole, Integer score, Game game) {
        this.person = person;
        this.hole = hole;
        this.score = score;
        this.game = game;
    }

    public String toNeo4j() {
        StringBuffer sb =createStringBuffer();
        if (getKey().contains("Score_Game_20170701_ES_003_0002")) {
            System.out.println("WARN Score_Game_20170701_ES_003_0002 -> duplicate");
        } else {
            sb.append("CREATE (" + getKey() + ":Score {score: '" + getScore() + "', hole: '" + hole + "'})\n");
            sb.append("CREATE (" + getKey() + ")-[:WAS_PLAYED_BY]->(" + person.getKey() + ")\n");
            sb.append("CREATE (" + getKey() + ")-[:WAS_PLAYED_IN_GAME]->(" + game.getKey() + ")\n");
        }
        return sb.toString();
    }


    public Integer getScore() {
        return score;
    }

    @Override
    Object[] getKeyInternal() {
        return new Object[]{game.getKey(),person.getId(),hole};
    }
}
