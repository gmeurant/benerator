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

package org.databene.dataset;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.databene.commons.ArrayFormat;
import org.databene.commons.ArrayUtil;

import junit.framework.TestCase;

/**
 * Tests the DataSet features.<br/><br/>
 * Created: 21.03.2008 14:20:59
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class DataSetTest extends TestCase {
    
    private static Log logger = LogFactory.getLog(DataSetTest.class);
    
    private static final String REGION = "org/databene/dataset/region";
    public static final String TYPE = "test";
    
    public void testAtomicSet() {
        DataSet set = DataSetFactory.getDataSet(REGION, "DE");
        assertEquals("DE", set.getName());
    }
    
    public void testNestedSet() {
        DataSet eu = DataSetFactory.getDataSet(REGION, "europe");
        assertNotNull(eu);
        DataSet centralEurope = DataSetFactory.getDataSet(REGION, "central_europe");
        assertTrue(eu.getSubSets().contains(centralEurope));
        Set<DataSet> atomicSubSets = eu.getAtomicSubSets();
        assertTrue(atomicSubSets.contains(DataSetFactory.getDataSet(REGION, "DE")));
        assertTrue(atomicSubSets.contains(DataSetFactory.getDataSet(REGION, "CH")));
        String[] dataFiles = DataSetFactory.getDataFiles(REGION, "europe", "org/databene/domain/person/familyName", ".csv");
        logger.debug(ArrayFormat.format(dataFiles));
        assertTrue(ArrayUtil.contains(dataFiles, "org/databene/domain/person/familyName_DE.csv"));
    }
}
