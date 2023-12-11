package io.fluent.dailycases.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveRedundantTest {
    RemoveRedundant rr = new RemoveRedundant();
    @Test
    public void testRemoveRedundantString(){
        rr.removeRedundantString();
    }

    @Test
    public void testRemoveRedundantObject(){
        rr.removeRedundantObject();
    }

    @Test
    public void testFilterToReduceRedundant(){
        rr.filterToReduceRedundant();
    }

}