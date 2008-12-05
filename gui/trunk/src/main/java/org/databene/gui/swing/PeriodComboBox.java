/*
 * (c) Copyright 2007-2008 by Volker Bergmann. All rights reserved.
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

package org.databene.gui.swing;

import org.databene.commons.Period;

import javax.swing.*;
import java.util.List;

/**
 * Component to choose a {@link Period} value.<br/>
 * <br/>
 * Created: 11.05.2007 06:50:25
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class PeriodComboBox extends JComboBox {

	private static final long serialVersionUID = 3990005784359468592L;

	public PeriodComboBox() {
        this(Period.minInstance(), Period.maxInstance());
    }

    public PeriodComboBox(Period minPeriod, Period maxPeriod) {
        super(periods(minPeriod, maxPeriod));
    }

    private static Period[] periods(Period minPeriod, Period maxPeriod) {
        List<Period> instances = Period.getInstances();
        int minIndex = instances.indexOf(minPeriod);
        int maxIndex = instances.indexOf(maxPeriod);
        Period[] periods = new Period[maxIndex - minIndex + 1];
        for (int i = minIndex; i <= maxIndex; i++)
            periods[i - minIndex] = instances.get(i);
        return periods;
    }


}
