import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MethodTests {
    Schema s;
    Schema s2;
    Tuple tup;
    Tuple tup2;
    Tuple tup3;
    Tuple tup4;
    Tuple tup5;
    Tuple tup6;
    ITable tab;
    ITable tab2;
    ITable tab3;
    Database db;

    @BeforeEach
    void initialize() {
        s = new Schema(Map.of(0, "sid:String", 1, "sname:String", 2, "major:String", 3, "byear:Integer"));
        s2 = new Schema(Map.of(0, "sid:String", 1, "cno:String"));
        tup = new Tuple(s);
        tup2 = new Tuple(s);
        tup3 = new Tuple(s);
        tup4 = new Tuple(s);
        tup5 = new Tuple(s2);
        tup6 = new Tuple(s2);
        tab = new Table("student", s);
        tab2 = IO.readTable("student", s, "db");
        tab3 = new Table("enroll", s2);
        db = new Database("db", "schema");
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
                () -> IO.writeTable(tab2, "db"),
                () -> IO.printTable(tab, s),
                () -> IO.writeTuple("test", tup2.getValues(), "db")
        );
    }

    @Test
    void testDatabase() {
        assertAll(
                () -> assertEquals(List.of(Map.of(0, "sid:String", 1, "sname:String", 2, "major:String", 3, "byear:Integer"),
                                           Map.of(0, "cno:String", 1, "cname:String", 2, "dept:String"),
                                           Map.of(0, "sid:String", 1, "cno:String", 2, "grade:String")),
                        db.getSchemas().stream().map(ISchema::getAttributes).toList()),
                () -> assertEquals(List.of("student", "course", "enroll"), db.getTables().stream().map(ITable::getName).toList()),
                () -> db.getTables().forEach(table -> IO.printTable(table, table.getSchema())),
                () -> tup5.setValues(new Object[]{"s1", "c1"}),
                () -> tup6.setValues(new Object[]{"s2", "c3"}),
                () -> tab3.addTuple(tup5),
                () -> tab3.addTuple(tup6),
                () -> assertEquals(tab3, db.selectData("SELECT sid, cno\nFROM enroll\nWHERE grade = b"))
        );
    }
}
