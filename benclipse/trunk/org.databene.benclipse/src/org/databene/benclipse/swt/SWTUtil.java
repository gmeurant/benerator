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

package org.databene.benclipse.swt;

import org.databene.benclipse.core.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

/**
 * Provides SWT related utility methods, mainly for constructing GUI elements.<br/>
 * <br/>
 * Created at 15.02.2009 07:35:06
 * @since 0.5.9
 * @author Volker Bergmann
 */

public class SWTUtil {

    public static Group createGroup(Composite parent, String label, int columns, int hspan, int fill) {
        Group group = new Group(parent, 0);
        initGridLayout(group, columns, hspan, fill, true);
        group.setFont(parent.getFont());
        group.setText(label);
        return group;
    }

    public static Composite createComposite(Composite parent, int columns, int hspan, int fill) {
        return createComposite(parent, columns, hspan, fill, false);
    }

    public static Composite createComposite(Composite parent, int columns, int hspan, int fill, boolean margin) {
        Composite g = new Composite(parent, 0);
        initGridLayout(g, columns, hspan, fill, margin);
        g.setFont(parent.getFont());
        return g;
    }

    public static Button createButton(Composite parent, String label) {
	    Button button = new Button(parent, SWT.BUTTON1);
	    button.setText(label);
	    button.setLayoutData(new GridData());
		return button;
    }

    public static Button createCheckbox(
    		Composite parent, String label, SelectionListener selectionListener, String tooltipText, int hspan) {
	    Button button = new Button(parent, SWT.CHECK);
		button.setText(label);
		button.setToolTipText(tooltipText);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = hspan;
        button.setLayoutData(gd);
		if (selectionListener != null)
			button.addSelectionListener(selectionListener);
		return button;
    }

    public static Button createLocalCheckbox(
    		Composite parent, String labelCode, SelectionListener selectionListener, String tooltipText, int hspan) {
	    return createCheckbox(parent, Messages.getString(labelCode), selectionListener, tooltipText, hspan);
    }

    public static Button createRadioButton(
    		Composite parent, String label, SelectionListener selectionListener, String tooltipText, int hspan) {
	    Button button = new Button(parent, SWT.RADIO);
		button.setText(label);
		button.setToolTipText(tooltipText);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = hspan;
        button.setLayoutData(gd);
		if (selectionListener != null)
			button.addSelectionListener(selectionListener);
		return button;
    }

	public static Text createLabeledText(Composite parent, String labelText, int textHSpan) {
		createLabel(parent, labelText);
		return createText(parent, textHSpan);
	}

	public static Text createText(Composite parent, int hspan) {
	    Text text = new Text(parent, SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = hspan;
		text.setLayoutData(gd);
		return text;
    }

	public static Label createLabel(Composite parent, String text) {
	    Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText(text);
		return label;
    }

    public static Combo createLabeledCombo(Composite parent, String label, int hspan) {
    	createLabel(parent, label);
    	return createCombo(parent, hspan);
    }

    public static Combo createCombo(Composite parent, int hspan) {
    	Combo combo = new Combo(parent, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = hspan;
        combo.setLayoutData(gd);
        return combo;
    }
    
	public static Link createLink(Composite parent, String text, SelectionListener selectionListener) {
	    Link link = new Link(parent, SWT.NONE);
        link.setFont(parent.getFont());
        link.setText(text);
        link.setLayoutData(new GridData());
        if (selectionListener != null)
        	link.addSelectionListener(selectionListener);
        link.setEnabled(true);
        return link;
    }

	public static void initGridLayout(Composite component, int columns, int hspan, int fill, boolean margin) {
	    GridLayout layout = new GridLayout(columns, false);
        initGridLayoutMargins(layout, margin);
        component.setLayout(layout);
        GridData gd = new GridData(fill);
        gd.horizontalSpan = hspan;
        component.setLayoutData(gd);
    }
    
    // private helper methods ------------------------------------------------------------------------------------------
    
    private static GridLayout initGridLayoutMargins(GridLayout layout, boolean margins) {
		layout.horizontalSpacing = 6;
		layout.verticalSpacing = 6;
		if (margins) {
			layout.marginWidth = 10;
			layout.marginHeight = 10;
		} else {
			layout.marginWidth = 0;
			layout.marginHeight = 0;
		}
		return layout;
	}

}
