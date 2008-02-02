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

package org.databene.commons;

import junit.framework.TestCase;

import java.util.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.PrintWriter;

/**
 * Created: 05.04.2007 07:51:00
 */
public class BeanUtilTest extends TestCase {

    // type info tests -------------------------------------------------------------------------------------------------

    public void testIsSimpleTypeByName() {
        assertTrue(BeanUtil.isSimpleType("int"));
        assertTrue(BeanUtil.isSimpleType("java.lang.Integer"));
        assertTrue(BeanUtil.isSimpleType("java.lang.String"));
        assertFalse(BeanUtil.isSimpleType("java.lang.StringBuffer"));
        assertFalse(BeanUtil.isSimpleType("org.databene.commons.BeanUtil"));
    }

    public void testIsPrimitive() {
        assertTrue(BeanUtil.isPrimitive(int.class.getName()));
        assertFalse(BeanUtil.isPrimitive(Integer.class.getName()));
        assertTrue(BeanUtil.isPrimitive(char.class.getName()));
        assertFalse(BeanUtil.isPrimitive(Character.class.getName()));
        assertFalse(BeanUtil.isPrimitive(String.class.getName()));
        assertFalse(BeanUtil.isPrimitive(StringBuffer.class.getName()));
        assertFalse(BeanUtil.isPrimitive(BeanUtil.class.getName()));
    }

    public void testGetWrapper() {
        assertEquals(Integer.class, BeanUtil.getWrapper(int.class.getName()));
        assertEquals(Character.class, BeanUtil.getWrapper(char.class.getName()));
    }

    public void testIsCollectionType() {
        assertTrue(BeanUtil.isCollectionType(Collection.class));
        assertTrue(BeanUtil.isCollectionType(List.class));
        assertTrue(BeanUtil.isCollectionType(ArrayList.class));
        assertTrue(BeanUtil.isCollectionType(Set.class));
        assertTrue(BeanUtil.isCollectionType(HashSet.class));
        assertFalse(BeanUtil.isCollectionType(Map.class));
    }

    // field tests -----------------------------------------------------------------------------------------------------

    public void testGetAttributeValueByName() {
        B b = new B();
        assertEquals(1, BeanUtil.getAttributeValue(b, "val"));
    }

    public void testGetStaticAttributeValueByName() {
        assertEquals("x", BeanUtil.getStaticAttributeValue(B.class, "stat"));
    }

    public void testSetAttributeByName() {
        B b = new B();
        BeanUtil.setAttributeValue(b, "val", 2);
        assertEquals(2, b.val);
    }

    public void testSetStaticAttributeByName() {
        BeanUtil.setStaticAttributeValue(B.class, "stat", "y");
        assertEquals("y", B.stat);
    }

    public void testGetGenericTypes() throws NoSuchFieldException {
        Object o = new Object() {
            public List<Integer> list;
        };
        Class c = o.getClass();
        Field f = c.getField("list");
        assertTrue("Test for generic type failed", 
                Arrays.deepEquals(new Class[] { Integer.class }, BeanUtil.getGenericTypes(f)));
    }

    // instantiation tests ---------------------------------------------------------------------------------------------

    public void testNewInstance() {
        B b = BeanUtil.newInstance(B.class);
        assertEquals(1, b.val);
        b = BeanUtil.newInstance(B.class, 2);
        assertEquals(2, b.val);
    }

    public void testForName() {
        Class type = BeanUtil.forName("org.databene.commons.BeanUtilTest$B");
        assertEquals(B.class, type);
    }

    // method tests ----------------------------------------------------------------------------------------------------

    public void testGetMethod() throws IllegalAccessException, InvocationTargetException {
        Method method = BeanUtil.getMethod(B.class, "getVal");
        B b = new B();
        assertEquals(1, method.invoke(b));
        method = BeanUtil.getMethod(B.class, "setVal", Integer.class);
        method.invoke(b, 2);
        assertEquals(2, b.val);
    }

    public void testInvoke() {
        B b = new B();
        assertEquals(1, BeanUtil.invoke(b, "getVal"));
        BeanUtil.invoke(b, "setVal", 2);
        assertEquals(2, b.val);
    }

    public void testTypesMatch() {
        assertTrue(BeanUtil.typesMatch(new Class[] {  }, new Class[] {  }));
        assertFalse(BeanUtil.typesMatch(new Class[] { String.class }, new Class[] {  }));
        assertTrue(BeanUtil.typesMatch(new Class[] { String.class }, new Class[] { String.class }));
        assertTrue(BeanUtil.typesMatch(new Class[] { C.class }, new Class[] { B.class }));
        assertFalse(BeanUtil.typesMatch(new Class[] { B.class }, new Class[] { C.class }));
        assertTrue(BeanUtil.typesMatch(new Class[] { A.class }, new Class[] { I.class }));
        assertTrue(BeanUtil.typesMatch(new Class[] { int.class }, new Class[] { Integer.class }));
        assertTrue(BeanUtil.typesMatch(new Class[] { Integer.class }, new Class[] { int.class }));
    }

    // property tests --------------------------------------------------------------------------------------------------

    public void testGetPropertyDescriptor() throws IllegalAccessException, InvocationTargetException {
        PropertyDescriptor desc = BeanUtil.getPropertyDescriptor(B.class, "val");
        assertEquals("val", desc.getName());
        B b = new B();
        desc.getWriteMethod().invoke(b, 2);
        assertEquals(2, b.val);
    }

    public void testReadMethodName() {
        assertEquals("getVal", BeanUtil.readMethodName("val", int.class));
        assertEquals("isValid", BeanUtil.readMethodName("valid", boolean.class));
        assertEquals("isValid", BeanUtil.readMethodName("valid", Boolean.class));
    }

    public void testWriteMethodName() {
        assertEquals("setVal", BeanUtil.writeMethodName("val"));
        assertEquals("setValid", BeanUtil.writeMethodName("valid"));
    }
/*
    public void testNullsafeClassName() {
        assertEquals(BeanUtilTest.class.getName(), BeanUtil.nullsafeClassName(BeanUtilTest.class));
        assertEquals(null, BeanUtil.nullsafeClassName(null));
    }
*/
    public void testGetPropertyDescriptors() {
        PropertyDescriptor[] descriptors = BeanUtil.getPropertyDescriptors(B.class);
        assertEquals(2, descriptors.length);
    }

    public void testGetPropertyValue() {
        B b = new B();
        assertEquals(b.getVal(), BeanUtil.getPropertyValue(b, "val"));
    }

    public void testSetPropertyValue() {
        B b = new B();
        BeanUtil.setPropertyValue(b, "val", 2);
        assertEquals(2, b.getVal());
    }

    // class tests -----------------------------------------------------------------------------------------------------

    public void testPrintClassInfo() {
        BeanUtil.printClassInfo(B.class, new PrintWriter(System.out));
    }

    public void testCheckJavaBean() {
        BeanUtil.checkJavaBean(B.class);
    }
    
    public void testDeprecated() {
        assertFalse(BeanUtil.deprecated(Object.class));
        assertTrue(BeanUtil.deprecated(Dep.class));
    }

    // Test classes ----------------------------------------------------------------------------------------------------

    public static interface I {
    }

    public static class A implements I {
        B b = new B();

        public B getB() {
            return b;
        }

    }

    public static class B<E extends Collection> {

        public int val;
        public static String stat = "x";
        public E gen;

        public B() {
            this(1);
        }

        public B(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public static String getStat() {
            return stat;
        }

        public static void setStat(String stat) {
            B.stat = stat;
        }
    }

    public static class C extends B {
    }
    
    @Deprecated
    public static class Dep {
        
    }
}
