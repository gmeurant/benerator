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
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Component that allows for selecting a time scale.
 * <br/>
 * Created: 09.05.2007 19:20:02
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class TimescaleSelector extends JPanel {

	private static final long serialVersionUID = 4851141632147396863L;
	
	Period scale;
    private Period minScale;
    private Period maxScale;

    private JComponent component;
    private Listener listener;

    public TimescaleSelector(Period scale) {
        this(scale, scale, scale);
    }

    public TimescaleSelector(Period scale, Period minScale, Period maxScale) {
        super(new BorderLayout());
        listener = new Listener();
        setScale(scale);
        setScaleRange(minScale, maxScale);
    }

    public Period getScale() {
        return scale;
    }

    public void setScale(Period scale) {
        this.scale = scale;
        if (component instanceof PeriodComboBox)
            ((PeriodComboBox)component).setSelectedItem(scale);
    }

    public Period getMinScale() {
        return minScale;
    }

    public Period getMaxScale() {
        return maxScale;
    }

    public void setScaleRange(Period minScale, Period maxScale) {
        removeAll();
        if (component instanceof PeriodComboBox)
            ((PeriodComboBox)component).removeActionListener(listener);
        this.minScale = minScale;
        this.maxScale = maxScale;
        if (minScale == maxScale)
            component = new JLabel(minScale.getName());
        else {
            PeriodComboBox comboBox = new PeriodComboBox(minScale, maxScale);
            comboBox.addActionListener(listener);
            component = comboBox;
        }
        add(component, BorderLayout.CENTER);
    }

    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Period selectedPeriod = (Period) ((JComboBox)e.getSource()).getSelectedItem();
            scale = selectedPeriod;
        }
    }
}
