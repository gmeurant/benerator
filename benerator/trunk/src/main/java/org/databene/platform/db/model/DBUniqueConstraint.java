/*
 * (c) Copyright 2006-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.platform.db.model;

import java.util.List;
import java.util.Arrays;

/**
 * Represents a unique constraint on one or the combination of several columns of one table.<br/>
 * <br/>
 * Created: 06.01.2007 09:00:37
 * @author Volker Bergmann
 */
public class DBUniqueConstraint extends DBConstraint {

    private List<DBColumn> columns;

    /**
     * @param name the constraint name - it may be null
     * @param columns the DBColumns to which the constraint is applied
     */
    public DBUniqueConstraint(String name, DBColumn ... columns) {
        super(name);
        this.columns = Arrays.asList(columns);
    }

    public DBTable getOwner() {
        return columns.get(0).getTable();
    }

    public DBColumn[] getColumns() {
        DBColumn[] array = new DBColumn[columns.size()];
        return columns.toArray(array);
    }

    public void addColumn(DBColumn column) {
        columns.add(column);
    }

    public void removeColumn(DBColumn column) {
        columns.remove(column);
    }
}
