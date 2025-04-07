import java.util.HashMap;
import java.util.Map;

/**
 * the schema is stored as a map of (index, name:type) pairs
 */
public class Schema implements ISchema {

    private Map<Integer, String> attributes;

    /**
     * constructor
     * @param attributes the given map of indices and names/types.
     */
    public Schema(Map<Integer, String> attributes) {
        this.attributes = new HashMap<>();
        this.attributes.putAll(attributes);
    }

    /**
     * getter
     * @return this schema's map of attributes.
     */
    @Override
    public Map<Integer, String> getAttributes() {
        return this.attributes;
    }

    /**
     * splits the name:type to return the attribute name
     * @param index the given index.
     * @return the attribute name associated with the given index.
     */
    @Override
    public String getName(int index) {
        String val = this.attributes.get(index);
        return val.substring(0, val.indexOf(':'));
    }

    /**
     * splits the name:type to return the attribute type
     * @param index the given index.
     * @return the attribute type associated with the given index.
     */
    @Override
    public String getType(int index) {
        String val = this.attributes.get(index);
        return val.substring(val.indexOf(':') + 1);
    }
}
