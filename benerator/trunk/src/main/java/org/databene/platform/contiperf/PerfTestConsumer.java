/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.platform.contiperf;

import java.io.IOException;

import org.databene.contiperf.ExecutionLogger;
import org.databene.contiperf.Invoker;
import org.databene.contiperf.Percentile;
import org.databene.contiperf.PercentileRequirement;
import org.databene.contiperf.PerfTestController;
import org.databene.contiperf.PerformanceRequirement;
import org.databene.contiperf.log.FileExecutionLogger;
import org.databene.model.consumer.Consumer;
import org.databene.model.consumer.ConsumerProxy;

/**
 * TODO Document class.<br/><br/>
 * Created: 22.10.2009 16:17:14
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class PerfTestConsumer extends ConsumerProxy<Object> {
	
	private PerformanceRequirement requirement;
	private PerfTestController controller;

	public PerfTestConsumer() {
	    this(null);
    }
	
	public PerfTestConsumer(Consumer<Object> target) {
	    super(target);
	    this.requirement = new PerformanceRequirement();
    }
	
	public void setTarget(Consumer<Object> target) {
		this.target = target;
	}
	
	public void setMax(int max) {
		requirement.setMax(max);
	}

	public void setPercentiles(PercentileRequirement[] percentiles) {
		requirement.setPercentiles(percentiles);
	}
	
	@Override
    public void startConsuming(Object object) {
	    try {
	        getController().invoke(new Object[] { object });
        } catch (Exception e) {
	        throw new RuntimeException(e);
        }
    }
	
	@Override
	public void close() throws IOException {
	    super.close();
	    controller.stop();
	}
	
	protected PerfTestController getController() {
		if (controller == null) {
			Invoker invoker = new ConsumerInvoker("enrollCustomer", target); // TODO
			ExecutionLogger logger = new FileExecutionLogger();
			controller = new PerfTestController(invoker, requirement, logger);
		}
		return controller;
	}

}
