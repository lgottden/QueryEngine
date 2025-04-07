import java.util.ArrayList;
import java.util.List;

/**
 * A table has a name, a schema and a list of tuples
 */
public class Table implements ITable {
    private String name;
    private List<ITuple> tuples;
    private ISchema schema;

    /**
     * constructor
     * @param name
     * @param schema
     */
    public Table(String name, ISchema schema) {

    }

    /**
     * Returns the table name
     * @return
     */
    @Override
    public String getName() {

    }

    /**
     * adds a tuple to the table
     * @param tuple
     */
    @Override
    public void addTuple(ITuple tuple) {

    }

    /**
     * Returns the list of tuples
     * @return
     */
    @Override
    public List<ITuple> getTuples() {

    }

    /**
     * Returns the table schema
     * @return
     */
    @Override
    public ISchema getSchema() {

    }

}
