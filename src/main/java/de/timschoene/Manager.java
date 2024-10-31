package de.timschoene;

/**
 * This is the Manager Class. It acts as the backbone of the program,
 * taking control of all the major activities of the network.
 * The main use of the manager class is getting a clear overview of the
 * process, as well as having a single instance governing over other processes.
 */

public class Manager {

    private static Manager instance;
    private static NeuralNetwork network;
    private static Image image;
    private int currentImage = Constants.STARTING_IMAGE;
    private int currentIteration = 0;

    public Manager() {
        instance = this;
        network = new NeuralNetwork();
        image = new Image(Constants.TRAINING_IMAGE_FILE_PATH, Constants.TRAINING_CHECK_FILE_PATH, Constants.IMAGE_WIDTH, Constants.IMAGE_HEIGHT, Constants.NUM_TRAINING_IMAGES);

        if(Constants.USE_XML && NetworkTranscript.fileExists())
            network.loadFromXML();
        else
            network.oneTimeSetup();

        for(int i = 0; i < Constants.AMOUNT_TRAINING; i++) {
            network.resetCost();
            if(currentImage >= Constants.NUM_TRAINING_IMAGES) { currentImage = 0; }

            System.out.println("Iteration " + (currentIteration) + ": ");
            loadNextImageIntoNetwork();
            network.forwardPropagate();
            addCostToDeltas();
            currentIteration++;

            network.backPropagate();
            if(i % Constants.MINI_BATCH_SIZE == 0) {
                network.addDeltas();
                network.resetDeltas();
            }
        }
        printStats();

        if(Constants.USE_XML)
            network.saveToXML();
    }


    private void addCostToDeltas() {
        double[] expected;
        expected = image.getResult(currentImage);
        network.addCost(expected);
    }

    private void loadNextImageIntoNetwork() {
        double[] pixels;
        //System.out.println("New image ID: " + currentImage);
        pixels = image.loadNewImage(currentImage);
        currentImage++;
        //Image.outputImage(pixels);
        getNeuralNetwork().inputImage(pixels);
    }

    public static Manager getInstance() {
        return instance;
    }

    public NeuralNetwork getNeuralNetwork() {
        return network;
    }

    public int getCurrentIteration() { return currentIteration; }

    private void printStats() {
        double correctPercentage = (double)network.gottenRight / (Constants.AMOUNT_TRAINING - Constants.STATISTIC_START_BATCH) * 100;
        for (int i = 0; i < Constants.OUTPUT_NEURON_LAYER_SIZE; i++) {
            network.correctPerNumber[i] /= network.totalPerNumber[i];
            System.out.println("The Network got " + network.correctPerNumber[i]*100 + "% of " + i + "s correctly.");
            System.out.println("The Network guessed a " + i + " " + network.totalPerNumber[i]*100 / (Constants.AMOUNT_TRAINING - Constants.STATISTIC_START_BATCH) + "% of times.");
        }
        System.out.println("The Network identified " + network.gottenRight + " of " + (Constants.AMOUNT_TRAINING - Constants.STATISTIC_START_BATCH) + " images correctly");
        System.out.println("The Network identified " + correctPercentage + "% of images correctly.");
    }
}
