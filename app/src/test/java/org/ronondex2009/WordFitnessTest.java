package org.ronondex2009;

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

public class WordFitnessTest {
    @Test
    public void testGetQuadgramAppearances() throws FileNotFoundException {
        WordFitness wordFitness = new WordFitness("src/test/resources/testSampleText.txt");
        Assert.assertEquals(17, wordFitness.getTotalQuadgrams()); 
    }

    @Test
    public void testGetTotalQuadgrams() throws FileNotFoundException {
        WordFitness wordFitness = new WordFitness("src/test/resources/testSampleText.txt");
        Assert.assertEquals(2.833213344, wordFitness.getFitness("sample"), 0.1); 
    }

    @Test
    public void testToString() {

    }
}
