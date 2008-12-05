/*
 * (c) Copyright 2006 by Volker Bergmann. All rights reserved.
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

package org.databene.gui.swing.table.edit;

import org.databene.commons.BeanUtil;

import javax.swing.*;
import java.util.Date;

/**
 * Created: 17.05.2007 18:27:41
 * @since 0.1.6
 * @author Volker Bergmann
 */
public class CellEditorManager {

    public static DefaultCellEditor createEditor(Class<?> targetType) {
        if (String.class.isAssignableFrom(targetType))
            return new DefaultCellEditor(new JTextField());
        else if (Number.class.isAssignableFrom(targetType) || BeanUtil.isPrimitiveNumber(targetType.getName()))
            return new NumberCellEditor(targetType);
        else if (Boolean.class.equals(targetType))
            return new CheckBoxTableCellEditor();
        else if (targetType.isAssignableFrom(Date.class))
            return new DateCellEditor();
        else
            throw new UnsupportedOperationException("Unsupported type: " + targetType);
    }
}
