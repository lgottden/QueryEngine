import java.util.*;

/**
 * The main database class
 * Database as a list of tables, list of schemas and a folder name where the database is stored
 * Database is stored (on the disk) in the form of three csv files and schema text file
 */
class Database {

    private List<ITable> tables;
    private List<ISchema> schemas;
    private String folderName;

    /**
     * Constructor
     * Creates the empty tables and schema lists
     * Reads the schema file to add schemas to the database
     * Populates the database table (with the data read from the csv files)
     * @param folderName the name of the database folder.
     * @param schemaFileName the name of the file containing the database schemas.
     */
    public Database(String folderName, String schemaFileName) {
        this.tables = new ArrayList<>();
        this.schemas = new ArrayList<>();
        this.folderName = folderName;
        IO.readSchema(schemaFileName, this.folderName, this);
        this.populateDB();
    }

    /**
     * Adds a table to the database
     * @param table the given table.
     */
    public void addTable(ITable table) {
        this.tables.add(table);
    }

    /**
     * Adds a table schema to the database
     * @param schema the given schema.
     */
    public void addSchema(ISchema schema) {
        this.schemas.add(schema);
    }

    /**
     * Return the list of tables in the database
     * @return the list of tables in this database.
     */
    public List<ITable> getTables() {
        return this.tables;
    }

    /**
     * Returns the list of schemas in the database
     * @return the list of schemas in this database.
     */
    public List<ISchema> getSchemas() {
        return this.schemas;
    }

    /**
     * The list of tables in the database is initialized with empty tables in the constructor
     * An empty table has a name and an empty list of tuples
     * This method sets the empty table in the list to the one provided as a parameter
     * @param table the given table that the table in the list is set to.
     */
    public void updateTable(ITable table) {
        for (ITable emptyTable : this.tables) {
            if (emptyTable.getName().equals(table.getName())) {
                for (ITuple tuple : table.getTuples())
                    emptyTable.addTuple(tuple);
            }
        }
    }

    /**
     * Populates the database
     *
     * Implements the following algorithm
     *
     * For each table in the db (tables are initially empty)
     *   Get the table's data from the csv file (by calling the read table method)
     *   Update the table (by calling the update table method)
     */
    public void populateDB() {
        for (ITable table : this.tables)
            updateTable(IO.readTable(table.getName(), table.getSchema(), this.folderName));
    }

    /**
     * Insert data into a table based upon the insert query
     * If the query is invalid throws an InvalidQueryException
     *
     * Implements the following algorithm
     *
     * Parse the insert into clause to get the table name, attribute name(s) and value(s)
     * If the query in not valid
     *   Throw an invalid query exception
     *   Exit
     * Create a new tuple with the schema of the table
     * Set the tuple values to the values from the query
     * Open the file corresponding to the table name
     * Append the tuple values (as comma separated values) to the end of the file
     *
     * @param query the given query.
     * @throws InvalidQueryException the exception thrown when the given query is invalid.
     */
    public void insertData(String query) throws InvalidQueryException {

    }

    /**
     * Selects data from a table (and returns it in the form of a results table)
     * If the query in not valid, throws an InvalidQueryException
     *
     * A query is valid if
     *
     * 1.	It has a select clause (select keyword followed by at least one attribute name)
     * 2.	It has a from clause (from keyword followed by a table name)
     * 3.	All the attribute names in the select clause are in the schema
     * 4.	The table name in the from clause is in the schema
     * 5.	All the attribute names in the where clause (if present) are in the schema
     * 6.	The attribute name in the order by clause (if present) is in the schema
     *
     * Implements the following algorithm
     *
     * Parse the query to get the select, from, where and order by clauses and the attribute and table names and condition
     * If the query is not valid
     *   Throw an invalid query exception
     *   Exit
     * Create a new results schema based with the attributes from the select clause
     * Create a new result table
     * For each tuple in the table
     *   If the tuple matches the where clause condition(s)
     *     Create a new results tuple using the result schema
     *     Set the results tuple values to the current tuple corresponding values
     *     Add the results tuple to the result table
     * Return results table
     *
     * @param query the given query.
     * @return a results table containing the data requested by the given query.
     * @throws InvalidQueryException the exception thrown when the given query is invalid.
     */
    public ITable selectData(String query) throws InvalidQueryException {
        if (!(query.contains("SELECT") && query.contains("FROM")))
            throw new InvalidQueryException(query);

        String select = query.substring(query.indexOf(' ') + 1, query.indexOf('\n'));
        query = query.substring(query.indexOf('\n') + 1);

        String from;
        String where = "";
        boolean hasWhere = query.contains("WHERE");
        if (hasWhere) {
            from = query.substring(query.indexOf(' ') + 1, query.indexOf('\n'));
            query = query.substring(query.indexOf('\n') + 1);
            where = query.substring(query.indexOf(' ') + 1);
        } else
            from = query.substring(query.indexOf(' ') + 1);

        String[] attributes = select.split(", ");
        for (String attribute : attributes) {
            if (this.schemas.stream().noneMatch(schema -> schema.toString().contains(attribute)))
                throw new InvalidQueryException(query);
        }

        if (this.tables.stream().noneMatch(table -> table.getName().equals(from)))
            throw new InvalidQueryException(query);

        String whereAttribute;
        String comparator = "";
        String constant = "";
        if (hasWhere) {
            String[] pieces = where.split(" ");
            whereAttribute = pieces[0];
            comparator = pieces[1];
            constant = pieces[2];
            if (this.schemas.stream().noneMatch(schema -> schema.toString().contains(whereAttribute)))
                throw new InvalidQueryException(query);
        } else
            whereAttribute = "";

        Map<Integer, String> tempList = new HashMap<>();
        for (ITable table : this.tables) {
            if (table.getName().equals(from))
                tempList = table.getSchema().getAttributes();
        }
        Map<Integer, String> resultList = new HashMap<>();
        int i = 0;
        for (String str : tempList.values()) {
            if (Arrays.stream(attributes).anyMatch(str::contains)) {
                resultList.put(i, str);
                i++;
            }
        }
        ISchema resultSchema = new Schema(resultList);

        ITable originalTable = this.tables.stream()
                                          .filter(table -> table.getName().equals(from))
                                          .toList()
                                          .getFirst();
        ITable resultTable = new Table(from, resultSchema);
        for (ITuple tuple : originalTable.getTuples()) {
            if (hasWhere && originalTable.getSchema().getAttributes().values().stream().anyMatch(value -> value.contains(whereAttribute))) {
                int index = 0;
                for (Map.Entry<Integer, String> attribute : originalTable.getSchema().getAttributes().entrySet())
                    if (attribute.getValue().contains(whereAttribute)) { index = attribute.getKey(); }
                String tupleVal = tuple.getValue(index);
                boolean passesComparator = true;
                switch (comparator) {
                    case "=":
                        if (!tupleVal.equals(constant)) { passesComparator = false; }
                        break;
                    case "!=":
                        if (tupleVal.equals(constant)) { passesComparator = false; }
                        break;
                    case ">":
                        if (tupleVal.compareTo(constant) <= 0) { passesComparator = false; }
                        break;
                    case ">=":
                        if (tupleVal.compareTo(constant) < 0) { passesComparator = false; }
                        break;
                    case "<":
                        if (tupleVal.compareTo(constant) >= 0) { passesComparator = false; }
                        break;
                    case "<=":
                        if (tupleVal.compareTo(constant) > 0) { passesComparator = false; }
                }
                if (!passesComparator) { continue; }
            }
            Object[] tupleArray = new Object[resultSchema.getAttributes().size()];
            int arrIndex = 0;
            for (int j = 0; j < tuple.getValues().length; j++) {
                if (resultTable.getSchema().getAttributes().containsValue(originalTable.getSchema().getAttributes().get(j))) {
                    tupleArray[arrIndex] = tuple.getValue(j);
                    arrIndex++;
                }
            }
            ITuple newTuple = new Tuple(resultSchema);
            newTuple.setValues(tupleArray);
            resultTable.addTuple(newTuple);
        }
        return resultTable;
    }

    /**
     * Delete data from a table
     * If the query in not valid, throws an InvalidQueryException
     *
     * Implements the following algorithm
     *
     * Parse the query to get the from and where clauses
     * Parse the from clause to get the table name
     * If the query in not valid
     *   Throw an invalid query exception
     *   Exit
     * If where clause is not empty
     *   Parse the where clause to get the condition
     *   For each tuple in the table
     *     If the where clause condition is true
     *       Remove the tuple from the table
     * Else
     *   For each tuple in the table
     *     Remove the tuple from the table
     * Write the table to the file
     *
     * @param query
     * @throws InvalidQueryException
     */
    public void deleteData(String query) throws InvalidQueryException {

    }
}
