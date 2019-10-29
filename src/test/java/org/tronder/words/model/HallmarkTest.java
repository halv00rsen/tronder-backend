package org.tronder.words.model;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class HallmarkTest {

    @Test
    public void testHallmarkEqual() {
        Hallmark one = new Hallmark("equal");
        Hallmark two = new Hallmark("equal");
        assertTrue(one.equals(two));
        assertTrue(Objects.equals(one, two));
    }
}
