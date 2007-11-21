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

package org.databene.platform.db.model;

import org.databene.commons.OrderedMap;

import java.util.List;

/**
 * Created: 06.01.2007 18:34:20
 */
public class Database {

    private String name;
    private OrderedMap<String, DBCatalog> catalogs;
    private OrderedMap<String, DBSchema> schemas;

    // constructors ----------------------------------------------------------------------------------------------------

    public Database() {
        this(null, null);
    }

    public Database(String name, OrderedMap<String, DBCatalog> catalogs) {
        this.name = name;
        this.catalogs = new OrderedMap<String, DBCatalog>();
        if (catalogs != null)
            this.catalogs.putAll(catalogs);
        this.schemas = new OrderedMap<String, DBSchema>();
    }

    // properties ------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    // catalog operations ----------------------------------------------------------------------------------------------

    public List<DBCatalog> getCatalogs() {
        return catalogs.values();
    }

    public DBCatalog getCatalog(String catalogName) {
        return catalogs.get(catalogName);
    }

    public void addCatalog(DBCatalog catalog) {
        catalog.setDatabase(this);
        catalogs.put(catalog.getName(), catalog);
    }

    public void removeCatalog(DBCatalog catalog) {
        catalogs.remove(catalog.getName());
        catalog.setDatabase(null);
    }

    // schema operations -----------------------------------------------------------------------------------------------

    public List<DBSchema> getSchemas() {
        return schemas.values();
    }

    public DBSchema getSchema(String schemaName) {
        return schemas.get(schemaName);
    }

    public void addSchema(DBSchema schema) {
        schema.setDatabase(this);
        schemas.put(schema.getName(), schema);
    }

    public void removeSchema(DBSchema schema) {
        schemas.remove(schema.getName());
        schema.setDatabase(null);
    }

    // java.lang.Object overrides --------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Database that = (Database) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
