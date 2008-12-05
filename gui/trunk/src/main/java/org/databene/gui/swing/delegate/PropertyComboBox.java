/*
 * (c) Copyright 2008 by Volker Bergmann. All rights reserved.
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

package org.databene.gui.swing.delegate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.databene.commons.BeanUtil;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.bean.ObservableBean;

/**
 * {@link JComboBox} implementation that serves as delegate of a property of a JavaBean object.<br/>
 * <br/>
 * Created at 01.12.2008 07:44:32
 * @since 0.1.6
 * @author Volker Bergmann
 */

public class PropertyComboBox extends JComboBox {

	private static final long serialVersionUID = -5039037135360506124L;
	
	private Object bean;
	private String propertyName;
	boolean locked;

	public PropertyComboBox(Object bean, String propertyName, Object ... values) {
		super(values);
		this.bean = bean;
		this.propertyName = propertyName;
		this.locked = true;
		Listener listener = new Listener();
		if (bean instanceof ObservableBean)
			((ObservableBean) bean).addPropertyChangeListener(propertyName, listener);
		this.getModel().addListDataListener(listener);
		this.locked = false;
		refresh();
	}

	/**
	 * reads the current property value and writes it to the text field.
	 */
	void refresh() {
		if (!locked) {
			locked = true;
			Object propertyValue = BeanUtil.getPropertyValue(bean, propertyName);
			Object selectedItem = getSelectedItem();
			if (!NullSafeComparator.equals(selectedItem, propertyValue))
				setSelectedItem(propertyValue);
			locked = false;
		}
	}
	
	/**
	 * writes the current text field content to the property.
	 */
	void update() {
		if (!locked) {
			locked = true;
			Object propertyValue = BeanUtil.getPropertyValue(bean, propertyName);
			Object selectedItem = getSelectedItem();
			if (!NullSafeComparator.equals(selectedItem, propertyValue))
				BeanUtil.setPropertyValue(bean, propertyName, selectedItem);
			locked = false;
		}
	}
	
	class Listener implements PropertyChangeListener, ListDataListener {

		public void propertyChange(PropertyChangeEvent evt) {
			refresh();
		}

		public void contentsChanged(ListDataEvent evt) {
			update();
		}

		public void intervalAdded(ListDataEvent evt) {
			update();
		}

		public void intervalRemoved(ListDataEvent evt) {
			update();
		}
		
	}
}
