package com.example.assignment_2.servingsizecalculator;

/**
 * Store information about a single pot
 */

public class Pot {
    private String PotName;
    private int PotWeight;

    // Set member data based on parameters.
    public Pot(String name, int weightInG) {
        this.PotName = name;
        this.PotWeight = weightInG;
    }

    // Return the weight
    public int getWeightInG() {
        return PotWeight;
    }

    // Set the weight. Throws IllegalArgumentException if weight is less than 0.
    public void setWeightInG(int weightInG) {
        if (weightInG < 0){
            throw new IllegalArgumentException();
        }
        this.PotWeight = weightInG;
    }

    // Return the name.
    public String getName() {
        return PotName;
    }

    // Set the name. Throws IllegalArgumentException if name is an empty string (length 0),
    // or if name is a null-reference.
    public void setName(String name) {
        if (name == null || name.length() == 0){
            throw new IllegalArgumentException();
        }
        this.PotName = name;
    }
}