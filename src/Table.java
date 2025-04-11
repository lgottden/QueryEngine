import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A table has a name, a schema and a list of tuples
 */
public class Table implements ITable {

    private String name;
    private List<ITuple> tuples;
    private ISchema schema;

    /**
     * constructor
     * @param name the given table name.
     * @param schema the given table schema.
     */
    public Table(String name, ISchema schema) {
        this.name = name;
        this.schema = schema;
        this.tuples = new ArrayList<>();
    }

    /**
     * Returns the table name
     * @return the table name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * adds a tuple to the table
     * @param tuple the given tuple.
     */
    @Override
    public void addTuple(ITuple tuple) {
        this.tuples.add(tuple);
    }

    /**
     * Returns the list of tuples
     * @return this table's associated list of tuples.
     */
    @Override
    public List<ITuple> getTuples() {
        return this.tuples;
    }

    /**
     * Returns the table schema
     * @return this table's associated schema.
     */
    @Override
    public ISchema getSchema() {
        return this.schema;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Table))
            return false;
        else {
            Table othTable = (Table) o;
            return this.tuples.equals(othTable.tuples) &&
                   this.schema.equals(othTable.schema) &&
                   this.name.equals(othTable.name);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.schema, this.tuples);
    }

    @Override
    public String toString() {
        String acc = this.name + "\n";
        for (ITuple tup : this.tuples)
            acc += tup.toString() + "\n";
        return acc.substring(0, acc.length() - 1);
    }
}
