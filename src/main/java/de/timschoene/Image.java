package de.timschoene;

import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * This is the image class. It is responsible for the
 * loading and checking of images. It communicates with the
 * training image file, as well as the training labels file
 * to load a new image into the network pixel by pixel,
 * as well as the associated number in the check file.
 */

public class Image {
    private final String trainingImageFilePath;
    private final String trainingCheckFilePath;
    private final int numImages;
    private final int imageSize;

    public Image(String trainingImageFilePath, String trainingCheckFilePath, int imageWidth, int imageHeight, int numImages) {
        this.trainingImageFilePath = trainingImageFilePath;
        this.trainingCheckFilePath = trainingCheckFilePath;
        this.numImages = numImages;
        this.imageSize = imageWidth * imageHeight;
    }

    public double[] loadNewImage(int index) {
        if (index < 0 || index >= numImages) {
            throw new IllegalArgumentException("Invalid image index.");
        }

        System.out.println(index + " = index");

        double[] image = new double[imageSize];
        try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(trainingImageFilePath)))) {
            dis.readInt(); // magic number
            dis.readInt(); // number of images
            dis.readInt(); // number of rows
            dis.readInt(); // number of columns

            // Skip to the selected image
            dis.skipBytes(index * imageSize);

            for (int j = 0; j < imageSize; j++) {
                image[j] = (double) (dis.readUnsignedByte()) / 255.0;
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }

    public double[] getResult(int currentImage) {
        double[] result = new double[Constants.OUTPUT_NEURON_LAYER_SIZE];
        Arrays.fill(result, 0);

        try (DataInputStream dis = new DataInputStream(new FileInputStream(trainingCheckFilePath))) {

            dis.readInt(); // magic number

            int maxNumber = dis.readInt(); // number of numbers
            if (currentImage < 0 || currentImage > maxNumber) {
                throw new IllegalArgumentException("Invalid number index.");
            }

            dis.skipBytes(currentImage - 1);

            int resultSpace = dis.readByte();
            //System.out.println("Expected Number: " + resultSpace);
            result[resultSpace] = 1;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void outputImage(double[] pixels) {
        System.out.println(Arrays.toString(pixels));
        System.out.println("\n\nImage:");
        for(int i = 0; i < Constants.IMAGE_WIDTH * Constants.IMAGE_HEIGHT; i++) {
            if(i % Constants.IMAGE_WIDTH == 0) {
                System.out.print("\n");
            }
            int num;
            num = (int) Math.ceil(pixels[i]);
            if(num == 0) {
                System.out.print("   ");
            } else {
                System.out.print("  " + num);
            }
        }
        System.out.println("\n\n");
    }
}

