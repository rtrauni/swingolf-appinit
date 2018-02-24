package at.swingolf.appinit;

import org.apache.commons.collections4.ListUtils;
import org.neo4j.driver.v1.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Neo4jBridge implements AutoCloseable {
    private final Driver driver;

    public Neo4jBridge(String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    public void execute(String query) {
        List<String> split = Arrays.asList(query.split("\n"));

        List<List<String>> partition = ListUtils.partition(split, 1000000);
        int n = 0;
        for (List<String> part : partition) {;
            n++;
            //            System.out.println(n+ " / "+partition.size());
        try ( Session session = driver.session() )
        {
String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    StatementResult result = tx.run(part.stream().collect(Collectors.joining("\n")));
                    return result.toString();
                }
            } );
//            System.out.println( greeting );
        }
    }}

    @Override
    public void close() throws Exception {

    }
}
