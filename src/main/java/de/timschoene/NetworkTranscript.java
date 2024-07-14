package de.timschoene;

public class NetworkTranscript {
    private double[][][] weights;
    private double[][] biases;

    public NetworkTranscript(double[][] biases, double[][][] weights) {
        this.biases = biases;
        this.weights = weights;
    }

    public double[][] getBiases() {
        return biases;
    }

    public void setBiases(double[][] biases) {
        this.biases = biases;
    }

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }
}
