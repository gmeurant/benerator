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

package org.databene.commons.tree;

import junit.framework.TestCase;

import org.databene.commons.TreeModel;
import org.databene.commons.tree.DefaultTreeNode;
import org.databene.commons.tree.TreeIterator;

/**
 * Created: 08.05.2007 19:03:28
 */
public class BidirectionalTreeIteratorTest extends TestCase {

    public void testNext() {
        TreeModel<DefaultTreeNode<String>> model = TreeCreator.createTreeModel();
        TreeIterator<DefaultTreeNode<String>> iterator = new TreeIterator<DefaultTreeNode<String>>(model);
        assertTrue(iterator.hasNext());
        assertEquals("root", iterator.next().getObject());
        assertTrue(iterator.hasNext());
        assertEquals("a1l", iterator.next().getObject());
        assertTrue(iterator.hasNext());
        assertEquals("a2f", iterator.next().getObject());
        assertTrue(iterator.hasNext());
        assertEquals("b1f", iterator.next().getObject());
        assertTrue(iterator.hasNext());
        assertEquals("c1l", iterator.next().getObject());
        assertTrue(iterator.hasNext());
        assertEquals("a3l", iterator.next().getObject());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

}
