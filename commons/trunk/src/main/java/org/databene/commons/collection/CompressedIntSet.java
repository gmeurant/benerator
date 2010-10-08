/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.commons.collection;

import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Collects int values in a compressed way.<br/><br/>
 * Created: 05.10.2010 19:17:30
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class CompressedIntSet {
	
	protected TreeMap<Integer, IntRange> numbers = new TreeMap<Integer, IntRange>();
	
	public void clear() {
		numbers.clear();
    }

	public void add(int i) {
		if (numbers.isEmpty()) {
			// if the set is empty, insert the number
			insertNumber(i);
		} else {
			// search the highest entry which is less or equals to i
			Entry<Integer, IntRange> floorEntry = numbers.floorEntry(i);
			IntRange rangeBelow;
			if (floorEntry == null)
				extendHigherRangeOrInsertNumber(i); // no range below found, check above
			else {
				// check found range
				rangeBelow = floorEntry.getValue();
				if (rangeBelow.contains(i))
					return;
				if (rangeBelow.getMax() + 1 == i) {
					// extend found range if applicable
					rangeBelow.setMax(i);
					// check if two adjacent ranges can be merged
				    IntRange rangeAbove = numbers.get(i + 1);
				    if (rangeAbove != null) {
				    	numbers.remove(i + 1);
				    	rangeBelow.setMax(rangeAbove.getMax());
				    }
				} else
					extendHigherRangeOrInsertNumber(i);
			}
		}
    }

	private void insertNumber(int i) {
	    numbers.put(i, new IntRange(i, i));
    }

	private void extendHigherRangeOrInsertNumber(int i) {
	    IntRange rangeAbove = numbers.get(i + 1);
	    if (rangeAbove != null) {
	    	numbers.remove(i + 1);
	    	rangeAbove.setMin(i);
	    	numbers.put(i, rangeAbove);
	    } else
	        insertNumber(i);
    }

	public boolean contains(int i) {
		Entry<Integer, IntRange> floorEntry = numbers.floorEntry(i);
		return (floorEntry != null && floorEntry.getValue().contains(i));
    }
	
/* TODO implement remove()
	public boolean remove(int i) {
	    return numbers.remove(i);
    }
*/
	public boolean isEmpty() {
	    return numbers.isEmpty();
    }

	@Override
	public String toString() {
	    return numbers.values().toString();
	}
	
	// TODO equals() and hashCode()
}
