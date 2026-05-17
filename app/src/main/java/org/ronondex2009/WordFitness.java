package org.ronondex2009;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class which represents a sample corpus of text and
 * calculates the fitness of a word. Uses quadgrams.
 * @author Ronan Kai Guin Adkins
 */

public class WordFitness {
    private final HashMap<String, Integer> quadgramAppearances;
    private int totalQuadgrams;

    // get next lowercased non-newline non-whitespace character. Scanner should be constructed with useDelimiter("")
    private int getNextLetter(Scanner sc) {
        while (sc.hasNext()) { 
            char nextChar = sc.next().toLowerCase().charAt(0);
            if (Character.isLetter(nextChar)) return nextChar;
        }
        return -1; // EOF (probably)
    }

    public WordFitness(String sampleTextPath) throws FileNotFoundException {
        // initialize data
        quadgramAppearances = new HashMap<>();
        totalQuadgrams = 0;
        char[] tokenBuffer = new char[4];

        // Open a scanner on sampleTextPath, or load defaultSampleText.txt as a resource in the jar file if empty.
        try (Scanner sampleTextScanner = (sampleTextPath.equals("")) ? 
                new Scanner(getClass().getClassLoader().getResourceAsStream("defaultSampleText.txt")).useDelimiter(""): 
                new Scanner(new File(sampleTextPath)).useDelimiter("")) {
            // populate buffer with four characters
            for (int i = 0; i < 4; i++) { tokenBuffer[i] = (char)getNextLetter(sampleTextScanner); }
            
            // next, continuously rotate this window into the text and increment our data.
            while (true) { 
                // increment our data
                String currentBufferAsString = String.valueOf(tokenBuffer);
                quadgramAppearances.putIfAbsent(currentBufferAsString, 0); // set if absent
                quadgramAppearances.compute(currentBufferAsString, (s, n) -> n + 1); // increment
                totalQuadgrams++;

                // now rotate the next character into the token buffer for next loop (and break at EOF)
                // TODO find a way to refactor this
                int nextLetter = getNextLetter(sampleTextScanner); if (nextLetter == -1) break;
                for (int i = 0; i < 3; i++) { tokenBuffer[i] = tokenBuffer[i + 1];}
                tokenBuffer[3] = (char) nextLetter;
            }
            System.out.println(quadgramAppearances);
        }
    }

    public double getFitness(String input) {
        double nonNormalizedFitness;
        int totalQuadgramsReadFromInput;
        try (Scanner sc = new Scanner(input).useDelimiter("")) {
            nonNormalizedFitness = 0;
            totalQuadgramsReadFromInput = 0;
            // initialize buffer
            char[] tokenBuffer = new char[4];
            for (int i = 0; i < 4; i++) { tokenBuffer[i] = (char)getNextLetter(sc); }
            // accumulate fitness from quadgrams
            while (true) {
                // add total appearances (gets divided and normalized later)
                totalQuadgramsReadFromInput++;
                if (quadgramAppearances.get(String.valueOf(tokenBuffer)) == 0) continue;
                if (quadgramAppearances.get(String.valueOf(tokenBuffer)) == null) continue;
                nonNormalizedFitness -= Math.log((double)quadgramAppearances.get(String.valueOf(tokenBuffer)) / totalQuadgrams);
                
                // now rotate the next character into the token buffer for next loop (and break at EOF)
                // TODO find a way to refactor this
                int nextLetter = getNextLetter(sc); if (nextLetter == -1) break;
                for (int i = 0; i < 3; i++) { tokenBuffer[i] = tokenBuffer[i + 1];}
                tokenBuffer[3] = (char) nextLetter;
            }
        }

        return nonNormalizedFitness / totalQuadgramsReadFromInput;
    }

    @Override
    public String toString() {
        return "WordFitness [quadgramAppearances=" + quadgramAppearances + ", totalQuadgrams=" + totalQuadgrams + "]";
    }

    public HashMap<String, Integer> getQuadgramAppearances() {
        return quadgramAppearances;
    }

    public int getTotalQuadgrams() {
        return totalQuadgrams;
    }
}
