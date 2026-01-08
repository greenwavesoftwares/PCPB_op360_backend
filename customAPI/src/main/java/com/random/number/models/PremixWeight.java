package com.random.number.models;

public class PremixWeight {
    private double maxWeight;
    private double minWeight;
    private double weight;
    private String premixName;

    // Constructor
    public PremixWeight(double maxWeight, double minWeight, double weight, String premixName) {
        this.maxWeight = maxWeight;
        this.minWeight = minWeight;
        this.weight = weight;
        this.premixName = premixName;
    }

    // Getters and Setters
    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(double minWeight) {
        this.minWeight = minWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPremixName() {
        return premixName;
    }

    public void setPremixName(String premixName) {
        this.premixName = premixName;
    }
}
