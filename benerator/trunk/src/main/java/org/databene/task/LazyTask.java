/*
 * (c) Copyright 2009 by Volker Bergmann. All rights reserved.
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

package org.databene.task;

import org.databene.commons.Context;
import org.databene.commons.Expression;
import org.databene.commons.IOUtil;

/**
 * {@link Task} implementation that evaluates an {@link Expression} 
 * which returns a Task and executes the returned Task.<br/>
 * <br/>
 * Created at 25.07.2009 16:48:52
 * @since 0.6.0
 * @author Volker Bergmann
 */

public class LazyTask implements Task {
	
	private Expression<Task> targetExpression;
	private Task target;

    public LazyTask(Expression<Task> targetExpression) {
	    this.targetExpression = targetExpression;
	    this.target = null;
    }

	public Expression<Task> getTargetExpression() {
	    return targetExpression;
    }

    public boolean available() { // TODO should we add a context param? It might be necessary to decide if the task is available
    	if (target == null)
    		return true; // we cannot resolve the Task here without a context, so let's assume the task would be available
	    return target.available();
    }

    public String getTaskName() {
	    return (target == null ? "LazyTask(" + targetExpression + ")" : target.getTaskName());
    }

    public void run(Context context) {
	    if (target == null)
	    	target = targetExpression.evaluate(context);
	    target.run(context);
    }

    public void close() {
	    IOUtil.close(target);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + target + ']';
    }
    
}
