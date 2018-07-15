package com.example.assignment_2.servingsizecalculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class PotClassTest {
    private Pot myPot = new Pot("aPot", 100);
    @Test
    public void testGetWeight() throws Exception {
        assertEquals(100, myPot.getWeightInG());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("aPot", myPot.getName());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetWeightThrowException() throws Exception {
        myPot.setWeightInG(-1);
    }

    @Test
    public void testSetWeightWorking() throws Exception {
        myPot.setWeightInG(50);
        assertEquals(50, myPot.getWeightInG());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetNameThrowException_emptyString() throws Exception {
        myPot.setName("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetNameThrowException_nullReference() throws Exception {
        myPot.setName(null);
    }

    @Test
    public void testSetNameWorking() throws Exception {
        myPot.setName("newPot");
        assertEquals("newPot", myPot.getName());
    }
}