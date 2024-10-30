package de.timschoene;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;

public class NetworkTranscript {
    private double[][][] weights;
    private double[][] biases;
    static String filePath = "./data.json";

    public NetworkTranscript() {
        // No-args constructor for Jackson
    }

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

    public void saveToJson() {
        XmlMapper xmlMapper = new XmlMapper();
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                System.out.println("File does not exist");
                file.createNewFile();
            }
            String xml = xmlMapper.writeValueAsString(this);
            FileWriter writer = new FileWriter(file);
            writer.write(xml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static NetworkTranscript loadFromJson() {
        XmlMapper xmlMapper = new XmlMapper();
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader reader = new FileReader(file);
            String xml = reader.toString();
            return xmlMapper.readValue(file, NetworkTranscript.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
