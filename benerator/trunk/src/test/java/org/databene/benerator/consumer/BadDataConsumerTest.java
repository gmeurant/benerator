/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator.consumer;

import static org.junit.Assert.*;

import org.databene.model.consumer.AbstractConsumer;
import org.databene.model.consumer.Consumer;
import org.junit.Test;

/**
 * TODO Document class.<br/><br/>
 * Created: 23.01.2011 08:15:24
 * @since TODO version
 * @author Volker Bergmann
 */
public class BadDataConsumerTest {
	
	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		// the real consumer throws an exception on every second invocation
		Consumer<Integer> realTarget = new AbstractConsumer<Integer>() {
			int count = 0;
			public void startConsuming(Integer object) {
				if (count++ % 2 == 1)
					throw new RuntimeException();
			}
		};
		
		// the bad data consumer stores error data in a list
		ListConsumer badTarget = new ListConsumer();
		
		BadDataConsumer consumer = new BadDataConsumer(badTarget, realTarget);

		for (int i = 1; i <= 5; i++) {
			consumer.startConsuming(i);
			consumer.finishConsuming(i);
		}
		consumer.close();
		
		assertEquals(2, badTarget.getConsumedData().size());
		assertEquals(2, badTarget.getConsumedData().get(0));
		assertEquals(4, badTarget.getConsumedData().get(1));
	}

}
