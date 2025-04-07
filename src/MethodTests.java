import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MethodTests {
    Schema s;
    Tuple tup;
    Table tab;

    @BeforeEach
    void initialize() {
        s = new Schema(Map.of(0, "Sid:String", 1, "Sname:String", 2, "Major:String", 3, "Byear:Integer"));
        tup = new Tuple(s);
        tab = new Table("Student", s);
    }

    @Test
    void testSchema() {
        assertAll(
                () -> assertEquals("Sname", s.getName(1)),
                () -> assertEquals("Byear", s.getName(3)),
                () -> assertEquals("Integer", s.getType(3)),
                () -> assertEquals("String", s.getType(0))
        );
    }

    @Test
    void testTuple() {
        assertAll(
                () -> tup.setValue(1, "John"),
                () -> assertEquals("John", tup.getValue(1)),
                () -> assertInstanceOf(String.class, tup.getValue(1)),
                () -> tup.setValues(new Object[]{"s2", "Ellen", "Math", 1995}),
                () -> assertArrayEquals(new Object[]{"s2", "Ellen", "Math", 1995}, tup.getValues()),
                () -> assertInstanceOf(Integer.class, tup.getValue(3))
        );
    }

    @Test
    void testTable() {
        assertAll(
                () -> assertEquals("Student", tab.getName()),
                () -> assertEquals(s, tab.getSchema()),
                () -> tup.setValues(new Object[]{"s1", "John", "CS", 1990}),
                () -> tab.addTuple(tup),
                () -> assertEquals(List.of(tup), tab.getTuples())
        );
    }
}
