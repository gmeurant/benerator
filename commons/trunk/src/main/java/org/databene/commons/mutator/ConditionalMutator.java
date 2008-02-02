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

package org.databene.commons.mutator;

import org.databene.commons.Accessor;
import org.databene.commons.Mutator;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.ComparableComparator;
import org.databene.commons.StringUtil;
import org.databene.commons.UpdateFailedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Comparator;

/**
 * Mutator that is only applied if a condition is true.<br/>
 * <br/>
 * Created: 08.05.2005 06:47:17
 */
public class ConditionalMutator extends MutatorProxy {

    public static final int ASSERT_EQUALS    = 0;
    public static final int OVERWRITE        = 1;
    public static final int SET_IF_UNDEFINED = 2;
    public static final int SET_IF_GREATER   = 3;

    protected int mode;
    private Comparator comparator;

	private Accessor accessor;

    private static Log logger = LogFactory.getLog(ConditionalMutator.class);

    public ConditionalMutator(Mutator realUpdater, Accessor accessor, int mode) {
        super(realUpdater);
        this.accessor = accessor;
        this.mode = mode;
        comparator = new ComparableComparator();
    }

    public void setValue(Object target, Object value) throws UpdateFailedException {
        Object oldValue = accessor.getValue(target);
        switch (mode) {
            case OVERWRITE:
                realMutator.setValue(target, value);
                break;
            case ASSERT_EQUALS:
                if (isEmpty(oldValue)) {
                    realMutator.setValue(target, value);
                } else if (!NullSafeComparator.equals(oldValue, value))
                    throw new RuntimeException("Mutator " + realMutator + " expected '" + oldValue + "', "
                            + "but found '" + value + "'");
                else
                    logger.debug("no update needed by " + realMutator);
                break;
            case SET_IF_UNDEFINED:
                if (isEmpty(oldValue))
                    realMutator.setValue(target, value);
                else
                    logger.debug("no update needed by " + realMutator);
                break;
            case SET_IF_GREATER:
                if (isEmpty(oldValue))
                    realMutator.setValue(target, value);
                else if (comparator.compare(oldValue, value) == -1)
                    realMutator.setValue(target, value);
                else
                    logger.debug("no update needed by " + realMutator);
                break;
            default:
                throw new RuntimeException("Illegal mode");
        }

    }

    // private helpers -------------------------------------------------------------------------------------------------

    private static boolean isEmpty(Object value) {
        return (value instanceof String ? StringUtil.isEmpty((String)value) : value == null);
    }
}
