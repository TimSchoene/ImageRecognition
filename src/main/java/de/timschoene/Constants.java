package de.timschoene;

/**
 * This is the constants class. It is the control center of the program.
 * Here, values can be adjusted by the user to adjust the network, the
 * statistics, and the learning curve to their specific needs.
 * The bounds and reasons for the values are given in the comments.
 * Values that state "fixed" must not be adjusted, as they are
 * responsible for image reading and size, which cannot be changed.
 */

public class Constants {

    //IMAGE IMPORT
    public static final String TRAINING_IMAGE_FILE_PATH = "images\\train-images.idx3-ubyte";
    public static final String TRAINING_CHECK_FILE_PATH = "images\\train-labels.idx1-ubyte";
    public static final int NUM_TRAINING_IMAGES = 60000; //AMOUNT OF IMAGES IN THE FILE (Fixed)
    public static final int STARTING_IMAGE = 0; //Where to start learning in the file
    public static final int IMAGE_WIDTH = 28; //Width of the Image (Fixed)
    public static final int IMAGE_HEIGHT = 28; //Height of the Image (Fixed)

    //TRAINING
    public static final int AMOUNT_TRAINING = 60000; //How many Images the Network trains (Should be at most NUM_TRAINING_IMAGES, otherwise Images repeat)
    public static final int MINI_BATCH_SIZE = 5; //How often the training deltas are added to the weights and biases

    //STATISTICS
    public static final int STATISTIC_START_BATCH = 59000; //The first iteration that is considered for Statistics (must be smaller than AMOUNT_TRAINING and greater than 0)

    //LEARNING RATE
    public static final double WEIGHT_LEARNING_RATE = 1; //How quickly the weights learn (Sweet Spot = 1)
    public static final double BIAS_LEARNING_RATE = 1; //How quickly the biases learn (Sweet Spot = 2)

    //NETWORK STRUCTURE
    public static final int INPUT_NEURON_LAYER_SIZE = IMAGE_WIDTH * IMAGE_HEIGHT; //Number of Pixels in the Image (Fixed)
    public static final int NEURON_LAYER_SIZE = 28; //Number of Neurons per layer (Sweet Spot of Learning quickly and achieving a high score = 28)
    public static final int NEURON_LAYERS = 1; //Number of hidden Layers in the Network (Sweet Spot = 2)
    public static final int OUTPUT_NEURON_LAYER_SIZE = 10; //There is 10 output numbers (Fixed)


}
