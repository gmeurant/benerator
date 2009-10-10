/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.benerator;

import java.util.List;

import org.databene.benerator.test.GeneratorClassTest;
import org.databene.benerator.util.TreePathGenerator;
import org.databene.commons.tree.DefaultTreeModel;
import org.databene.commons.tree.DefaultTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the TreePathGenerator.<br/>
 * <br/>
 * Created: 30.07.2007 19:08:22
 * @author Volker Bergmann
 */
public class TreePathGeneratorTest extends GeneratorClassTest {

    private static final Logger logger = LoggerFactory.getLogger(TreePathGeneratorTest.class);

    public TreePathGeneratorTest() {
        super(TreePathGenerator.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        DefaultTreeNode<String> root = new DefaultTreeNode<String>("root");
        DefaultTreeNode<String> a = new DefaultTreeNode<String>("a");
        root.addChild(a);
        DefaultTreeNode<String> a1 = new DefaultTreeNode<String>("a1");
        a.addChild(a1);
        DefaultTreeNode<String> a2 = new DefaultTreeNode<String>("a2");
        a.addChild(a2);

        DefaultTreeNode<String> b = new DefaultTreeNode<String>("b");
        root.addChild(b);
        DefaultTreeNode<String> b1 = new DefaultTreeNode<String>("b1");
        b.addChild(b1);
        DefaultTreeNode<String> b2 = new DefaultTreeNode<String>("b2");
        b.addChild(b2);

        DefaultTreeNode<String> c = new DefaultTreeNode<String>("c");
        root.addChild(c);

        DefaultTreeModel<String> model = new DefaultTreeModel<String>(root);
        TreePathGenerator generator = new TreePathGenerator(model);
        for (int i = 0; i < 10; i++) {
            List<String> path = generator.generate();
        	assertNotNull(path);
			logger.debug(path.toString());
        }
    }

}
