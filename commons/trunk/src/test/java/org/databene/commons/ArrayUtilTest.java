/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.commons;

import junit.framework.TestCase;

import java.util.*;

/**
 * Created: 20.06.2007 18:03:10
 */
public class ArrayUtilTest extends TestCase {

    private Integer[] ONE_TWO      = { 1, 2       };
    private Integer[] TWO_ONE      = { 2, 1       };
    private Integer[] ONE_TO_THREE = { 1, 2, 3    };
    private Integer[] THREE_TO_ONE = { 3, 2, 1    };
    private Integer[] ONE_TO_FOUR  = { 1, 2, 3, 4 };
    private Integer[] FOUR_TO_ONE  = { 4, 3, 2, 1 };

    public void testAsList() {
        List<Integer> expectedList = new ArrayList<Integer>();
        expectedList.add(1);
        expectedList.add(2);
        expectedList.add(3);
        assertEquals(expectedList, ArrayUtil.toList(1, 2, 3));
    }

    public void testAsSet() {
        Set<Integer> expectedSet = new HashSet<Integer>();
        expectedSet.add(1);
        expectedSet.add(2);
        expectedSet.add(3);
        assertEquals(expectedSet, ArrayUtil.toSet(1, 2, 3));
    }

    public void testContains() {
        assertTrue(ArrayUtil.contains(1, ONE_TO_THREE));
        assertTrue(ArrayUtil.contains(2, ONE_TO_THREE));
        assertTrue(ArrayUtil.contains(3, ONE_TO_THREE));
        assertFalse(ArrayUtil.contains(0, ONE_TO_THREE));
        assertFalse(ArrayUtil.contains(4, ONE_TO_THREE));
    }

    public void testEndsWithSequence() {
        assertTrue(ArrayUtil.endsWithSequence(ONE_TO_THREE, new Integer[] { 1, 2, 3 }));
        assertTrue(ArrayUtil.endsWithSequence(ONE_TO_THREE, new Integer[] { 2, 3 }));
        assertTrue(ArrayUtil.endsWithSequence(ONE_TO_THREE, new Integer[] { 3 }));
        assertTrue(ArrayUtil.endsWithSequence(ONE_TO_THREE, new Integer[] { }));
        assertFalse(ArrayUtil.endsWithSequence(ONE_TO_THREE, new Integer[] { 0, 1, 2, 3 }));
        assertFalse(ArrayUtil.endsWithSequence(ONE_TO_THREE, new Integer[] { 2, 2 }));
    }

    public void testCommonElements() {
        List<Integer> expectedList = new ArrayList<Integer>();
        expectedList.add(1);
        expectedList.add(2);
        expectedList.add(3);
        assertEquals(expectedList, ArrayUtil.commonElements(ONE_TO_THREE, ONE_TO_THREE));
        expectedList.clear();
        expectedList.add(1);
        assertEquals(expectedList, ArrayUtil.commonElements(new Integer[] {0, 1}, ONE_TO_THREE));
        expectedList.clear();
        assertEquals(expectedList, ArrayUtil.commonElements(new Integer[] {8}, ONE_TO_THREE));
        assertEquals(expectedList, ArrayUtil.commonElements(new Integer[0], ONE_TO_THREE));
    }

    public void testEqualsIgnoreOrder() {
        assertTrue(ArrayUtil.equalsIgnoreOrder(ONE_TO_THREE, ONE_TO_THREE));
        assertTrue(ArrayUtil.equalsIgnoreOrder(new Integer[] { 3, 2, 1 }, ONE_TO_THREE));
        assertFalse(ArrayUtil.equalsIgnoreOrder(new Integer[] {2, 3, 4}, ONE_TO_THREE));
    }

    public void testIndexOf() {
        assertEquals(-1, ArrayUtil.indexOf(0, ONE_TO_THREE));
        assertEquals( 0, ArrayUtil.indexOf(1, ONE_TO_THREE));
        assertEquals( 1, ArrayUtil.indexOf(2, ONE_TO_THREE));
        assertEquals( 2, ArrayUtil.indexOf(3, ONE_TO_THREE));
        assertEquals(-1, ArrayUtil.indexOf(4, ONE_TO_THREE));
    }

    public void testRemove() {
        assertTrue(Arrays.equals(new Integer[] { 2, 3 }, ArrayUtil.remove(ONE_TO_THREE, 0)));
        assertTrue(Arrays.equals(new Integer[] { 1, 3 }, ArrayUtil.remove(ONE_TO_THREE, 1)));
        assertTrue(Arrays.equals(new Integer[] { 1, 2 }, ArrayUtil.remove(ONE_TO_THREE, 2)));
    }

    public void testRevert() {
        assertTrue(Arrays.equals(TWO_ONE, ArrayUtil.revert(ONE_TWO)));
        assertTrue(Arrays.equals(THREE_TO_ONE, ArrayUtil.revert(ONE_TO_THREE)));
        assertTrue(Arrays.equals(FOUR_TO_ONE, ArrayUtil.revert(ONE_TO_FOUR)));
    }
}
