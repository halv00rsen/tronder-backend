package org.tronder.words.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class DialectTest {

    private Dialect dialect;
    private Set<Hallmark> hallmarks;

    @Before
    public void init() {
        dialect = new Dialect();
        hallmarks = new HashSet<>();
        hallmarks.add(new Hallmark("hallmarkOne"));
        hallmarks.add(new Hallmark("hallmarkTwo"));
        hallmarks.add(new Hallmark("hallmarkEqual"));

    }

    @Test
    public void testSerializeHallmarks() {
        dialect.setHallmarks(hallmarks);
        List<String> hallmarksSerialized = dialect.getHallmarks();
        assertThat(hallmarksSerialized.size(), is(3));
        assertTrue(hallmarksSerialized.contains("hallmarkOne"));
        assertTrue(hallmarksSerialized.contains("hallmarkEqual"));
        assertTrue(hallmarksSerialized.contains("hallmarkTwo"));
    }

    @Test
    public void testAddingDuplicateValues() {
        dialect.setHallmarks(hallmarks);
        assertThat(dialect.getHallmarksSet().size(), is(3));
        Hallmark equalHallmark = new Hallmark("hallmarkEqual");
        assertTrue(dialect.getHallmarksSet().contains(equalHallmark));
        assertFalse(dialect.getHallmarksSet().add(equalHallmark));
        assertThat(dialect.getHallmarksSet().size(), is(3));
    }
}
