import java.util.Objects;

public class Main {

    public static void main(String[] args) {

    }

    /**
     * Runs the given query on the database
     *
     * Implements the following algorithm
     *
     * Determine the type of query (from select, insert or delete)
     * If select query
     *   Select data
     *   Print results
     * Else if insert query
     *   Insert data
     * Else if delete is given
     *   Delete data
     *
     * @param query the given query.
     * @param db the given database.
     */
    public static void runQuery(String query, Database db) {
        try {
            String command = query.substring(0, query.indexOf(' '));
            if (command.equals("SELECT"))
                System.out.println(db.selectData(query));
            else if (command.equals("INSERT"))
                db.insertData(query);
            else
                db.deleteData(query);
        } catch (InvalidQueryException ex) { throw new RuntimeException(ex); }
    }
}
