package de.timschoene;

/**
 * This is the Calculation class.
 * It takes care of the non-linearity used in
 * neural networks, here a sigmoid function.
 * It also calculates the derivative of the sigmoid
 * function for back-propagation purposes.
 * This class also calculates the cost function
 * of a single training example.
 */

public class Calc {
    public static double sigmoid(double num) {
        //System.out.println("Sigmoid: " + num + " -> " + 1 / (1 + Math.exp(-num)));
        return 1 / (1 + Math.exp(-num));
    }

    public static double derivativeSigmoid(double num) {
        return num * (1 - sigmoid(num));
    }

    public static double[] cost(double[] output, double[] expected) {
        for(int i = 0; i < Constants.OUTPUT_NEURON_LAYER_SIZE; i++) {
            if(output[i] < expected[i]) {
                output[i] = Math.pow(expected[i] - output[i] , 2);
            } else {
                output[i] = -Math.pow(expected[i] - output[i] , 2);
            }
        }
        return output;
    }

    public static int findMaxIndex(double[] output) {
        int maxIndex = 0;
        for (int i = 0; i < output.length; i++) {
            if(output[i]>output[maxIndex])
                maxIndex = i;
        }
        return maxIndex;
    }
}