/*
 * (c) Copyright 2005-2008 by Volker Bergmann. All rights reserved.
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

package org.databene.gui.swing.table.item;

import org.databene.commons.BeanUtil;
import org.databene.commons.ComparableComparator;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.bean.BeanComparator;
import org.databene.commons.bean.PropertyAccessorFactory;
import org.databene.commons.bean.PropertyMutatorFactory;
import org.databene.commons.comparator.BooleanComparator;
import org.databene.commons.comparator.ComparatorFactory;
import org.databene.gui.swing.table.edit.CellEditorManager;
import org.databene.gui.swing.table.edit.IntCellEditor;
import org.databene.gui.swing.table.render.CheckBoxTableCellRenderer;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.util.Comparator;
import java.beans.PropertyDescriptor;

/**
 * Created: 06.01.2005 18:41:01
 * @since 0.1.6
 * @author Volker Bergmann
 * */
public class PropertyConnector extends FieldConnector {

    // constructors ----------------------------------------------------------------------------------------------------

    public PropertyConnector(Class<?> beanClass, String propertyName) {
        this(beanClass, propertyName, false);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PropertyConnector(Class<?> beanClass, String propertyName, boolean editable) {
        super(getDisplayName(beanClass, propertyName),
                PropertyAccessorFactory.getAccessor(beanClass, propertyName),
                new DefaultTableCellRenderer(),
                (editable ? PropertyMutatorFactory.getPropertyMutator(beanClass, propertyName) : null),
                (editable ? CellEditorManager.createEditor(
                        BeanUtil.getPropertyDescriptor(beanClass, propertyName).getPropertyType()) : null),
                new NullSafeComparator(new BeanComparator(beanClass, propertyName))
        );
    }

    private static String getDisplayName(Class<?> beanClass, String propertyName) {
        PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(beanClass, propertyName);
        if (propertyDescriptor == null)
            return "???";
        return propertyDescriptor.getDisplayName();
    }


    public PropertyConnector(String title, String propertyName, Class<?> propertyType) {
        this(title, propertyName, propertyType, (TableCellRenderer)null);
    }

    public PropertyConnector(String title, String propertyName, Class<?> propertyType, TableCellRenderer renderer) {
        this(title, propertyName, propertyType, renderer, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PropertyConnector(String title, String propertyName, Class<?> propertyType,
                             TableCellRenderer renderer, TableCellEditor editor) {
        this(title, propertyName, propertyType, renderer, editor,
            new NullSafeComparator(new BeanComparator(propertyName)));
    }

    @SuppressWarnings("unchecked")
    public PropertyConnector(String title, String propertyName, Class<?> propertyType,
                             TableCellRenderer renderer, TableCellEditor editor, Comparator<?> comparator) {
        super(
            title,
            PropertyAccessorFactory.getAccessor(propertyType, propertyName),
            (renderer != null ? renderer : new DefaultTableCellRenderer()),
            PropertyMutatorFactory.getPropertyMutator(propertyName),
            editor,
            comparator);
    }

    public PropertyConnector(String title, String propertyName, Class<?> propertyType, Class<?> itemClass) {
        this(title, propertyName, propertyType, itemClass,
                getDefaultRenderer(propertyType),
            getDefaultEditor(propertyType),
            new BeanComparator(itemClass, propertyName, getDefaultComparator(propertyType))
        );
    }

    public PropertyConnector(String title, String propertyName, Class<?> propertyType, Class<?> itemClass, TableCellRenderer renderer, TableCellEditor editor) {
        this(title, propertyName, propertyType,
                renderer, editor, new BeanComparator(itemClass, propertyName, getDefaultComparator(propertyType))
        );
    }

    public PropertyConnector(String title, String propertyName, Class<?> propertyType, Class<?> itemClass, 
    		TableCellRenderer renderer, TableCellEditor editor, boolean comparable) {
        this(title, propertyName, propertyType, itemClass, renderer, editor,
            (comparable ?
                    new BeanComparator(itemClass, propertyName, ComparatorFactory.getComparator(propertyType)) :
                    null)
        );
    }

    @SuppressWarnings({ "unchecked" })
    public PropertyConnector(String title, String propertyName, Class<?> propertyType, Class<?> itemClass,
                             TableCellRenderer renderer, TableCellEditor editor, Comparator<?> comparator) {
        super(
            title,
            PropertyAccessorFactory.getAccessor(itemClass, propertyName, true),
            (renderer != null ? renderer : new DefaultTableCellRenderer()),
            PropertyMutatorFactory.getPropertyMutator(propertyName),
            editor,
            comparator);
    }

    // implementation --------------------------------------------------------------------------------------------------

    private static TableCellEditor getDefaultEditor(Class<?> type) {
        if (boolean.class.equals(type) || Boolean.class.equals(type))
            return new DefaultCellEditor(new JCheckBox());
        else if (int.class.equals(type) || Integer.class.equals(type))
            return new IntCellEditor();
        else
            return new DefaultCellEditor(new JTextField());
    }

    private static TableCellRenderer getDefaultRenderer(Class<?> type) {
        if (boolean.class.equals(type) || Boolean.class.equals(type))
            return new CheckBoxTableCellRenderer();
        else
            return new DefaultTableCellRenderer();
    }

    private static NullSafeComparator<?> getDefaultComparator(Class<?> type) {
        if (boolean.class.equals(type) || Boolean.class.equals(type))
            return new NullSafeComparator<Boolean>(new BooleanComparator());
        else
            return new NullSafeComparator<Comparable<?>>(new ComparableComparator<Comparable<?>>());
    }
/*
    private static final class MyPropertyAccessor implements Accessor {
        private String accessorName;

        public MyPropertyAccessor(String propertyName, Class propertyType) {
            this.accessorName = BeanUtil.accessorName(propertyName, propertyType);
        }

        public Object getValue(Object item) {
            if (item == null)
                return null;
            try {
                Method accessor = item.getClass().getMethod(accessorName, null);
                return accessor.invoke(item, null);
            } catch (NoSuchMethodException e) {
                // that's alright
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
*/
}
