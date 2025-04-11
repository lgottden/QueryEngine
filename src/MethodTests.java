import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MethodTests {
    Schema s;
    Tuple tup;
    Tuple tup2;
    Tuple tup3;
    Tuple tup4;
    ITable tab;
    ITable tab2;

    @BeforeEach
    void initialize() {
        s = new Schema(Map.of(0, "sid:String", 1, "sname:String", 2, "major:String", 3, "byear:Integer"));
        tup = new Tuple(s);
        tup2 = new Tuple(s);
        tup3 = new Tuple(s);
        tup4 = new Tuple(s);
        tab = new Table("student", s);
        tab2 = IO.readTable("student", s, "db");
    }

    // Condition tests omitted, as they are essentially trivial.

    @Test
    void testSchema() {
        assertAll(
                () -> assertEquals("sname", s.getName(1)),
                () -> assertEquals("byear", s.getName(3)),
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
                () -> assertEquals("student", tab.getName()),
                () -> assertEquals(s, tab.getSchema()),
                () -> tup.setValues(new Object[]{"s1", "John", "CS", 1990}),
                () -> tab.addTuple(tup),
                () -> assertEquals(List.of(tup), tab.getTuples())
        );
    }

    @Test
    void testIO() {
        assertAll(
                () -> tup.setValues(new Object[]{"s1", "John", "CS", 1990}),
                () -> tab.addTuple(tup),
                () -> tup2.setValues(new Object[]{"s2", "Ellen", "Math", 1995}),
                () -> tab.addTuple(tup2),
                () -> tup3.setValues(new Object[]{"s3", "Eric", "CS", 1990}),
                () -> tab.addTuple(tup3),
                () -> tup4.setValues(new Object[]{"s4", "Ann", "Biology", 2001}),
                () -> tab.addTuple(tup4),
                () -> assertEquals(tab, tab2),
                () -> tab2 = new Table("test", s),
                () -> tab2.addTuple(tup),
                () -> tab2.addTuple(tup4),
                () -> IO.writeTable(tab2, "db")
        );
    }
}
