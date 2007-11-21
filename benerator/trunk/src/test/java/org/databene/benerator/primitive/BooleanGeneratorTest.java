package org.databene.benerator.primitive;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.benerator.IllegalGeneratorStateException;
import org.databene.benerator.GeneratorClassTest;

/**
 * (c) Copyright 2006 by Volker Bergmann
 * Created: 09.06.2006 20:07:56
 */
public class BooleanGeneratorTest extends GeneratorClassTest {

    private static Log logger = LogFactory.getLog(BooleanGeneratorTest.class);

    public BooleanGeneratorTest() {
        super(BooleanGenerator.class);
    }

    public void testDefaultConstructor() {
        new BooleanGenerator();
    }

    public void testDistribution() throws IllegalGeneratorStateException {
        checkDistribution(0.5, 1000);
        checkDistribution(0.0, 1000);
        checkDistribution(0.1, 1000);
        checkDistribution(0.9, 1000);
        checkDistribution(1.0, 1000);
    }

    private void checkDistribution(double trueProbability, int n) throws IllegalGeneratorStateException {
        BooleanGenerator generator = new BooleanGenerator((float)trueProbability);
        int[] count = new int[2];
        for (int i = 0; i < n; i++) {
            if (generator.generate())
                count[1]++;
            else
                count[0]++;
        }
        logger.debug("prob=" + trueProbability + ", n=" + n + ", falseCount=" + count[0] + ", trueCount=" + count[1]);
        float ratio = (float)count[1] / n;
        assertEquals(trueProbability, ratio, 0.1);
    }

}
