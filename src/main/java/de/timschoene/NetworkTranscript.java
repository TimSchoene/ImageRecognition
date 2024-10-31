package de.timschoene;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;

public class NetworkTranscript {
    private double[][][] weights;
    private double[][] biases;
    static String filePath = "./data.xml";

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

    public double[][][] getWeights() {
        return weights;
    }

    public static boolean fileExists() {
        File file = new File(filePath);
        return file.exists();
    }

    public void saveToXML() {
        XmlMapper xmlMapper = new XmlMapper();
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                System.out.println("File does not exist");
                if(file.createNewFile())
                    System.out.println("File created");
            }
            String xml = xmlMapper.writeValueAsString(this);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(xml);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static NetworkTranscript loadFromXML() {
        XmlMapper xmlMapper = new XmlMapper();
        //xmlMapper.something
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return xmlMapper.readValue(file, NetworkTranscript.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
