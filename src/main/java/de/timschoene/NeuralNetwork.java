package de.timschoene;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.StatUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * This is the Neural Network class. All the logic behind deep learning is implemented here.
 * The data structure is based on arrays. The neurons represent values used in a single iteration,
 * whereas the weights and biases are the brains of the network.
 * In one iteration, the methods are executed chronologically. The "oneTimeSetup" method is only executed once.
 * During the "forwardPropagate" method, weights and biases are untouched, only the neurons get calculated.
 * The learning takes place in the "backPropagate" method, where weights and biases are changed.
 * This is done by propagating the average error, calculated in the "Calc" class, backwards to calculate
 * errors for every single neuron. Once this is achieved, the weights leading up to that neuron,
 * as well as its bias, can be adjusted. The "CONSTANTS.WEIGHT_LEARNING_RATE", and "CONSTANTS.BIAS_LEARNING_RATE"
 * are responsible for the impact of the adjustment. These, as well as several other values, can be controlled
 * in the constants class.
 */

public class NeuralNetwork {

    private final int totalLayerSize = Constants.NEURON_LAYERS + 2;

    private final double[][] neurons = new double[totalLayerSize][Constants.INPUT_NEURON_LAYER_SIZE]; //[gen][pos]
    private final double[][] biases = new double[totalLayerSize][Constants.INPUT_NEURON_LAYER_SIZE]; //[gen][pos]
    private final double[][][] weights = new double[totalLayerSize][Constants.INPUT_NEURON_LAYER_SIZE][Constants.NEURON_LAYER_SIZE]; //[startGen][startPos][endPos]
    private final double[] cost = new double[Constants.OUTPUT_NEURON_LAYER_SIZE];
    public int gottenRight = 0; //Tracking how many images the network has identified correctly

    public NeuralNetwork() {

    }

    public void oneTimeSetup() {

        Random random = new Random();

        for (int i = 0; i < totalLayerSize; i++) {

            for (int j = 0; j < Constants.INPUT_NEURON_LAYER_SIZE; j++) {

                if (i > 0 && j > Constants.NEURON_LAYER_SIZE) {
                    break;
                }

                if (i > totalLayerSize - 2 && j > Constants.OUTPUT_NEURON_LAYER_SIZE - 1) {
                    break;
                }

                if(i > 0) {
                    biases[i][j] = random.nextDouble(-1,1);
                    //System.out.println("Bias initialized. Generation: " + i + ", Position: " + j);
                }

                for (int k = 0; k < Constants.NEURON_LAYER_SIZE; k++) {

                    if (i > totalLayerSize - 3 && k > Constants.OUTPUT_NEURON_LAYER_SIZE - 1) {
                        break;
                    }

                    if (i > totalLayerSize - 2) {
                        break;
                    }

                    weights[i][j][k] = random.nextDouble(-1,1);
                    //System.out.println("Weight initialized. StartGen: " + i + ", StartPos: " + j + ", EndPos: " + k);
                }
            }
        }
    }

    public void inputImage(double[] pixels) {

        for (int i = 0; i < totalLayerSize; i++) {
            Arrays.fill(neurons[i], 0);
        }

        //System.out.println("Reset Neurons: " + Arrays.deepToString(neurons));

        System.arraycopy(pixels, 0, neurons[0], 0, Constants.INPUT_NEURON_LAYER_SIZE);

        //System.out.println("Input Neurons: " + (Arrays.toString(neurons[0])));
    }

    public void forwardPropagate() {

        for (int i = 0; i < totalLayerSize - 1; i++) {

            for (int j = 0; j < Constants.NEURON_LAYER_SIZE; j++) {

                if (i > totalLayerSize - 3 && j > Constants.OUTPUT_NEURON_LAYER_SIZE - 1) {
                    break;
                }

                for (int k = 0; k < Constants.INPUT_NEURON_LAYER_SIZE; k++) {

                    if (i > 0 && k > Constants.NEURON_LAYER_SIZE) {
                        break;
                    }

                    if(neurons[i][k] == 0) {
                        continue;
                    }

                    neurons[i+1][j] += neurons[i][k] * weights[i][k][j];
                }
                neurons[i+1][j] += biases[i+1][j];

                neurons[i+1][j] = Calc.sigmoid(neurons[i+1][j]);
            }
        }

        /* OUTPUT BUGFIX TEST PRINTS
        System.out.print("Output Neuron");
        for(int i = 0; i < Constants.OUTPUT_NEURON_LAYER_SIZE; i++) { System.out.print(" " + i +": " + neurons[totalLayerSize-1][i] + ", "); }
        System.out.print("\n");
        outputNumber = ArrayUtils.indexOf(neurons[totalLayerSize-1], StatUtils.max(neurons[totalLayerSize-1]));
        System.out.println("Single Number Output: " + outputNumber);
        */
    }

    public void resetCost() { Arrays.fill(cost, 0); }

    public void addCost(double[] expected) {
        double[] output = new double[Constants.OUTPUT_NEURON_LAYER_SIZE];
        double[] addToCost;
        double singlecost = 0;

        System.arraycopy(neurons[Constants.NEURON_LAYERS + 1], 0, output, 0, Constants.OUTPUT_NEURON_LAYER_SIZE);

        int outputNumber = ArrayUtils.indexOf(output, StatUtils.max(output));
        System.out.println("Output Number: " + outputNumber);

        int expectedNumber = ArrayUtils.indexOf(expected, StatUtils.max(expected));
        System.out.println("Expected Number: " + expectedNumber);

        if(outputNumber == expectedNumber && Manager.getInstance().getCurrentIteration() > Constants.STATISTIC_START_BATCH) {
            gottenRight++;
        }

        addToCost = Calc.cost(output, expected);

        //System.out.println("Expected output neurons: " + Arrays.toString(expected));

        for(int i = 0; i < Constants.OUTPUT_NEURON_LAYER_SIZE; i++) {
            cost[i] += addToCost[i];
            singlecost += Math.abs(addToCost[i]);
        }

        System.out.println("Delta Cost: " + singlecost);
    }

    public void backPropagate() {

        double[][] errors = new double[totalLayerSize][Math.max(Constants.NEURON_LAYER_SIZE,Constants.OUTPUT_NEURON_LAYER_SIZE)];
        System.arraycopy(cost, 0, errors[totalLayerSize - 1], 0, Constants.OUTPUT_NEURON_LAYER_SIZE);

        //calculating normalized hidden errors
        for (int i = totalLayerSize - 2; i >= 0; i--) {
            for (int j = 0; j < Constants.NEURON_LAYER_SIZE; j++) {
                double currentWeightedSum = 0;
                for (int k = 0; k < Constants.NEURON_LAYER_SIZE; k++) {
                    if (i > totalLayerSize - 3 && k > Constants.OUTPUT_NEURON_LAYER_SIZE - 1) { break; }
                    currentWeightedSum += weights[i][j][k] * errors[i+1][k];
                }
                errors[i][j] = currentWeightedSum / Constants.NEURON_LAYER_SIZE; //Normalizing the weighted Sum

                //System.out.println("errors[" + i + "][" + j + "]= " + errors[i][j]); //Error Test Print
            }
        }

        //adjusting weights and biases
        for (int i = totalLayerSize - 2; i >= 0; i--) {
            for (int j = 0; j < Constants.INPUT_NEURON_LAYER_SIZE; j++) {
                if (i > 0 && j > Constants.NEURON_LAYER_SIZE) { break; }
                for (int k = 0; k < Constants.NEURON_LAYER_SIZE; k++) {
                    if (i > totalLayerSize - 3 && k > Constants.OUTPUT_NEURON_LAYER_SIZE - 1) { break; }
                    weights[i][j][k] += Constants.WEIGHT_LEARNING_RATE * errors[i+1][k] * Calc.derivativeSigmoid(neurons[i+1][k]) * neurons[i][j];
                    //System.out.println("Weight "+i+", "+j+", "+k+" adjusted by: " + "Learning Rate: " + Constants.LEARNING_RATE + " * Errors: " + errors[i+1][k] + " * dSig(neurons[i=" + i+1 +"][k=" + k + "]): " +  Calc.derivativeSigmoid(neurons[i+1][k]) + " * neurons[i=" + i + "][j=" + j + "]: " +  neurons[i][j] + ", totalling: " + Constants.LEARNING_RATE * errors[i+1][k] * Calc.derivativeSigmoid(neurons[i+1][k]) * neurons[i][j]);
                }
                if(!(j > Constants.NEURON_LAYER_SIZE - 1)) {
                    biases[i + 1][j] += Constants.BIAS_LEARNING_RATE * errors[i + 1][j] * Calc.derivativeSigmoid(neurons[i + 1][j]);
                    //System.out.println("Bias "+(i+1)+", "+j+" adjusted by: Learning Rate: " + Constants.BIAS_LEARNING_RATE + " * Errors: " + (i+1) + ", " + j + ": " + errors[i + 1][j] + " * dSig(neurons[i=" + (i+1) +"][j=" + j + "]): " + Calc.derivativeSigmoid(neurons[i + 1][j]) + ", Totalling: " + Constants.BIAS_LEARNING_RATE * errors[i + 1][j] * Calc.derivativeSigmoid(neurons[i + 1][j]));
                }
            }
        }
    }

    /*
    public void saveToJson() {

        try {
            NetworkTranscript networkTranscript = new NetworkTranscript(biases, weights);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File file = new File("neural_network.json");
            if (!file.exists()) {
                file.createNewFile();
            }
            String json = gson.toJson(networkTranscript);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFromJson() {

    }
    */ //Unused Methods for saving and loading Networks (implemented soon)
}
