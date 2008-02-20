package org.databene.commons;

import java.util.Arrays;

import junit.framework.TestCase;

public class ArrayBuilderTest extends TestCase {

    public void test() {
        check(new String[] {});
        check(new String[] { "0" }, "0");
        check(new String[] { "0", "1" }, "0", "1");
    }
    
    private void check(String[] expected, String ... items) {
        ArrayBuilder<String> builder = new ArrayBuilder<String>(String.class);
        for (String item : items)
            builder.append(item);
        assertTrue(Arrays.deepEquals(expected, builder.toArray()));
    }
}
