package org.ronondex2009;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class which represents a set of quadgram frequencies and acts as an input-output machine for calculating word fitness.
 * calculates the fitness of a word. Uses quadgrams.
 * @author Ronan Kai Guin Adkins
 */

public class WordFitness {
    private final HashMap<String, Integer> quadgramAppearances;
    private int totalQuadgrams;

    /**
     * Default blank constructor. 
     * Users should call {@link #FromQuadgramFrequencies(Scanner)} or {@link #FromSample(Scanner)} builder methods from a newly initialized WordFitness.
     * Example: WordFitness wordFitness = new WordFitness().FromSample(new File("sampleTextFile.txt"));
     */
    public WordFitness() {
        quadgramAppearances = new HashMap<>();
        totalQuadgrams = 0;
    }

    /**
     * Builds WordBuilder from pre-calculated quadgram frequencies in a file. Much more reliable than using a sample file.
     * This program has a default file that can be used by calling {@link #FromQuadgramFrequencies()}.
     * If users can use this method instead of {@link #FromSample(Scanner)}, they should. Also more performant for quality.</br>
     * Format of frequencies file should be lines of:</br>
     * Quadgram Count</br>
     * TODO add more builder methods for various formats, like XML.
     * @see #FromQuadgramFrequencies()
     * @param quadgramFrequencyScanner
     * @return WordFitness, with quadgram frequencies set to those in quadgramFrequencyFile.
     * @throws FileNotFoundException
     */
    public WordFitness FromQuadgramFrequencies(Scanner quadgramFrequencyScanner) throws FileNotFoundException {
        throw(new RuntimeException("Not Implemented"));
    }
    
    /**
     * Builds WordBuilder from pre-calculated quadgram frequencies in the default frequencies file.
     * Most preferred way of constructing WordFitness. Example:
     * WordFitness myWordFitness = new WordFitness().FromQuadgramFrequencies();
     * @see #FromQuadgramFrequencies(Scanner)
     * @see #FromSample()
     * @return WordFitness, with quadgram frequencies set to those in the default frequency file. 
     * @throws FileNotFoundException
     */
    public WordFitness FromQuadgramFrequencies() throws FileNotFoundException {
        try (Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("defaultFrequencies.txt"))) { FromQuadgramFrequencies(sc); } 
        catch (FileNotFoundException e) { throw(new RuntimeException("Couldn't load defaultSampleText resource as stream. Are you running from a .jar build?")); }
        return this;    
    }

    /**
     * Builds WordFitness by harvesting the quadgrams from the sample text.
     * Less reliable than using a pre-calculated frequencies file.
     * Can be used to bridge the gap between languages if a frequency file is not available for a language,
     * by using a large enough corpus of text can act 'well enough' to filter random or garbled text from
     * actual text in the target language.
     * @see #FromQuadgramFrequencies(Scanner)
     * @param sampleScanner
     * @return WordFitness with quadgram frequencies harvested from the sample file.
     * @throws FileNotFoundException
     */
    public WordFitness FromSample(Scanner sampleScanner) throws FileNotFoundException {
        // rotating window of four characters for getting quadgrams
        char[] tokenBuffer = new char[4];
        Pattern originalDelimiter = sampleScanner.delimiter();
        sampleScanner.useDelimiter("");

        // populate buffer with four characters
        for (int i = 0; i < 4; i++) { tokenBuffer[i] = (char)getNextLetter(sampleScanner); }
        
        // next, continuously rotate this window into the text and increment our data.
        while (true) { 
            // increment our data
            String currentBufferAsString = String.valueOf(tokenBuffer);
            quadgramAppearances.putIfAbsent(currentBufferAsString, 0); // set if absent
            quadgramAppearances.compute(currentBufferAsString, (s, n) -> n + 1); // increment
            totalQuadgrams++;

            // now rotate the next character into the token buffer for next loop (and break at EOF)
            // TODO find a way to refactor this
            int nextLetter = getNextLetter(sampleScanner); if (nextLetter == -1) break;
            for (int i = 0; i < 3; i++) { tokenBuffer[i] = tokenBuffer[i + 1];}
            tokenBuffer[3] = (char) nextLetter;
        }
        
        sampleScanner.useDelimiter(originalDelimiter);
        return this;
    }

    /**
     * {@link #FromSample(Scanner)} with default sample text file. This method of construction should not be used.
     * Likely to be removed in the future.
     * @deprecated Do not use this function. Use {@link #FromSample()} or {@link #FromQuadgramFrequencies(Scanner)}
     * @return WordFitness with quadgram frequencies harvested from the sample file.
     */
    @Deprecated
    public WordFitness FromSample(){
        try (Scanner sc = new Scanner(getClass().getClassLoader().getResourceAsStream("defaultFrequencies.txt"))) { FromSample(sc); } 
        catch (FileNotFoundException e) { throw(new RuntimeException("Couldn't load defaultSampleText resource as stream. Are you running from a .jar build?")); }
        return this;
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
                if (quadgramAppearances.get(String.valueOf(tokenBuffer)) != null &&
                    quadgramAppearances.get(String.valueOf(tokenBuffer)) != 0) {
                    nonNormalizedFitness -= Math.log((double)quadgramAppearances.get(String.valueOf(tokenBuffer)) / totalQuadgrams);
                }
                else {
                    // The quadgram we have is either null or not found
                    nonNormalizedFitness -= Math.log((double)1/totalQuadgrams);
                }
                totalQuadgramsReadFromInput++;

                // now rotate the next character into the token buffer for next loop (and break at EOF)
                // TODO find a way to refactor this
                int nextLetter = getNextLetter(sc); if (nextLetter == -1) break;
                for (int i = 0; i < 3; i++) { tokenBuffer[i] = tokenBuffer[i + 1];}
                tokenBuffer[3] = (char) nextLetter;
            }
        }

        return nonNormalizedFitness / totalQuadgramsReadFromInput;
    }

    // get next lowercased non-newline non-whitespace character. Scanner should be constructed with useDelimiter("")
    private int getNextLetter(Scanner sc) {
        while (sc.hasNext()) { 
            char nextChar = sc.next().toLowerCase().charAt(0);
            if (Character.isLetter(nextChar)) return nextChar;
        }
        return -1; // EOF (probably)
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
