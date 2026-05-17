package org.ronondex2009;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main entry point. Command-Line interface, and driver.
 * @author Ronan Kai Guin Adkins
 */
public class App {
    /*private void helpMessage() {
        System.out.println("Usage: fitnessFilter "); // TODO complete
    }*/

    public static void main(String[] args) {
        // parse input arguments
        // TODO this later
        double fitnessThreshold = 2;

        WordFitness wordFitness;
        try {
            wordFitness = new WordFitness("");
        } catch (FileNotFoundException e) { System.err.println("Sample file not found. " + e.getMessage()); return; }

        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNext()) {
                String nextLine = sc.nextLine();
                if (wordFitness.getFitness(nextLine) < fitnessThreshold) {
                    System.out.println(nextLine);
                }
            }
        }
    }
}
