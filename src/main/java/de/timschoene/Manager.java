package de.timschoene;

public class Manager {

    private static Manager instance;
    private final JsonFileWriter jsonFileWriter;

    public Manager() {
        instance = this;
        jsonFileWriter = new JsonFileWriter("C:\\Users\\tim_s\\IdeaProjects\\ImageRecognition4\\json\\Data.json");
        System.out.println("check1");
        oneTimeSetup(); //Comment Later
    }

    private void oneTimeSetup() {
        jsonFileWriter.clearAll(); // DELETE LATER
        final int inputNeuronLayerSize = 784;
        final int neuronLayerSize = 16;
        final int neuronLayers = 2;
        final int outputNeuronLayerSize = 10;

        for (int i = 0; i < inputNeuronLayerSize; i++) {
            Neuron neuron = new Neuron( 0, i);
            System.out.println("check2");
        }

        for(int i = 1; i < neuronLayers + 1; i++) {
            for (int j = 0; j < neuronLayerSize; j++) {
                Neuron neuron = new Neuron(i, j);
            }
        }

        for (int i = neuronLayers + 1; i < outputNeuronLayerSize; i++) {
            Neuron neuron = new Neuron(3, i);
        }
    }

    public static Manager getInstance() {
        return instance;
    }

    public JsonFileWriter getJsonFileWriter() {
        return this.jsonFileWriter;
    }
}
