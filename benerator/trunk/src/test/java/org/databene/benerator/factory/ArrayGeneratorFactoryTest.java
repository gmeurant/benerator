/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.benerator.factory;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

import org.databene.benerator.Generator;
import org.databene.benerator.SequenceTestGenerator;
import org.databene.benerator.test.GeneratorTest;
import org.databene.benerator.test.PersonIterable;
import org.databene.benerator.wrapper.ConvertingGenerator;
import org.databene.commons.ArrayFormat;
import org.databene.commons.ConfigurationError;
import org.databene.commons.converter.ArrayElementExtractor;
import org.databene.commons.converter.ConverterManager;
import org.databene.jdbacl.hsql.HSQLUtil;
import org.databene.model.data.ArrayElementDescriptor;
import org.databene.model.data.ArrayTypeDescriptor;
import org.databene.model.data.DataModel;
import org.databene.model.data.DefaultDescriptorProvider;
import org.databene.model.data.InstanceDescriptor;
import org.databene.model.data.SimpleTypeDescriptor;
import org.databene.model.data.Uniqueness;
import org.databene.platform.db.DBSystem;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link ArrayGeneratorFactory}.<br/><br/>
 * Created: 01.05.2010 08:03:12
 * @since 0.6.1
 * @author Volker Bergmann
 */
public class ArrayGeneratorFactoryTest extends GeneratorTest {
	
	static final Object[] ALICE = new Object[] { "Alice", 23 };
	static final Object[] BOB   = new Object[] { "Bob",   34 };
	static final Object[] OTTO  = new Object[] { "Otto",  89 };
	
	static final Object[] INT13 = new Object[] { 1, 3 };
	static final Object[] INT14 = new Object[] { 1, 4 };
	static final Object[] INT23 = new Object[] { 2, 3 };
	static final Object[] INT24 = new Object[] { 2, 4 };

	@Before
	public void setup() {
		ConverterManager.getInstance().reset();
	    DefaultDescriptorProvider provider = new DefaultDescriptorProvider(ArrayGeneratorFactoryTest.class.getName());
		provider.addDescriptor(createPersonDescriptor());
		DataModel dataModel = DataModel.getDefaultInstance();
		dataModel.clear();
		dataModel.addDescriptorProvider(provider);
	}

	@Test
	public void testGenerator() {
		ArrayTypeDescriptor descriptor = new ArrayTypeDescriptor("");
		descriptor.setGenerator(PersonAttrArrayGenerator.class.getName());
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.NONE, context);
		generator.init(context);
		for (int i = 0; i < 10; i++)
			assertEqualArrays(PersonAttrArrayGenerator.ALICE, generator.generate());
	}
	
	@Test
	public void testXlsSource() {
		ArrayTypeDescriptor descriptor = createPersonDescriptor();
		descriptor.setSource("org/databene/benerator/factory/person.ent.xls");
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.NONE, context);
		generator.init(context);
		assertEqualArrays(ALICE, generator.generate());
		assertEqualArrays(OTTO, generator.generate());
		assertNull(generator.generate());
	}
	
    @Test
	public void testXlsDataset() {
		ArrayTypeDescriptor descriptor = createPersonDescriptor();
		descriptor.setSource("org/databene/benerator/factory/dataset_{0}.xls");
		descriptor.setNesting("org/databene/benerator/factory/testnesting");
		descriptor.setDataset("DACH");
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.SIMPLE, context);
		Generator<String> g = new ConvertingGenerator<Object[], String>(generator, new ArrayElementExtractor<String>(String.class, 0));
		generator.init(context);
		expectGeneratedSet(g, "de", "at", "ch");
		assertNull(generator.generate());
	}
	
	@Test
	public void testCsvSource() {
		ArrayTypeDescriptor descriptor = createPersonDescriptor();
		descriptor.setSource("org/databene/benerator/factory/person.ent.csv");
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.NONE, context);
		generator.init(context);
		assertEqualArrays(ALICE, generator.generate());
		assertEqualArrays(OTTO, generator.generate());
		assertNull(generator.generate());
	}
	
    @Test
	public void testCsvDataset() {
		ArrayTypeDescriptor descriptor = createPersonDescriptor();
		descriptor.setSource("org/databene/benerator/factory/dataset_{0}.csv");
		descriptor.setNesting("org/databene/benerator/factory/testnesting");
		descriptor.setDataset("DACH");
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.SIMPLE, context);
		Generator<String> g = new ConvertingGenerator<Object[], String>(generator, new ArrayElementExtractor<String>(String.class, 0));
		generator.init(context);
		expectGeneratedSet(g, "de", "at", "ch");
		assertNull(generator.generate());
	}
	
	@Test
	public void testDatabaseSource() throws Exception {
		// prepare DB
		DBSystem db = new DBSystem("db", HSQLUtil.getInMemoryURL("benerator"), HSQLUtil.DRIVER, "sa", null);
		context.set("db", db);
		try {
			db.execute(
					"create table agft_person (" +
					"  id   int         NOT NULL," +
					"  name varchar(30) NOT NULL," +
					"  age  int         NOT NULL" +
					")");
			db.execute("insert into agft_person (id, name, age) values (1, 'Alice', 23)");
			db.execute("insert into agft_person (id, name, age) values (2, 'Otto', 89)");
			
			// prepare descriptor
			ArrayTypeDescriptor descriptor = createPersonDescriptor();
			descriptor.setSource("db");
			descriptor.setSelector("select name, age from agft_person");
			Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.NONE, context);
			generator.init(context);
			
			// verify results
	        assertEqualArrays(ALICE, generator.generate());
	        Object[] p2 = generator.generate();
	        assertEqualArrays(OTTO,  p2);
	        assertNull(generator.generate());
			
		} finally {
			db.execute("drop table agft_person if exists");
			db.close();
		}
	}
	
	@Test
	public void testEntitySource() {
		ArrayTypeDescriptor descriptor = new ArrayTypeDescriptor("testEntitySourceType");
		descriptor.setSource(PersonIterable.class.getName());
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("testEntitySource", descriptor, Uniqueness.NONE, context);
		generator.init(context);
		for (int i = 0; i < 2; i++) {
	        Object[] product = generator.generate();
	        assertTrue("Found: " + ArrayFormat.format(product), Arrays.equals(ALICE, product) || Arrays.equals(BOB, product));
        }
		assertNull(generator.generate());
	}
	
	@Test
	public void testGeneratorSource() {
		context.set("myGen", new PersonAttrArrayGenerator());
		ArrayTypeDescriptor descriptor = createPersonDescriptor();
		descriptor.setSource("myGen");
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.NONE, context);
		generator.init(context);
		for (int i = 0; i < 10; i++)
			assertEqualArrays(ALICE, generator.generate());
	}
	
	@Test(expected = ConfigurationError.class)
	public void testIllegalSourceType() {
		ArrayTypeDescriptor descriptor = new ArrayTypeDescriptor("");
		descriptor.setSource("illegalSource");
		context.set("illegalSource", new File("txt.txt"));
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", descriptor, Uniqueness.NONE, context);
		generator.init(context);
	}

	@Test
	public void testSyntheticGeneration() {
		ArrayTypeDescriptor arrayDescriptor = new ArrayTypeDescriptor("");
		ArrayElementDescriptor e1 = new ArrayElementDescriptor(0, "string");
		((SimpleTypeDescriptor) e1.getLocalType(false)).setValues("'Alice', 'Bob'");
		arrayDescriptor.addElement(e1);
		ArrayElementDescriptor e2 = new ArrayElementDescriptor(1, "int");
		((SimpleTypeDescriptor) e2.getLocalType(false)).setValues("23,34");
		arrayDescriptor.addElement(e2);
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("", arrayDescriptor, Uniqueness.NONE, context);
		generator.init(context);
		for (int i = 0; i < 10; i++) {
			Object[] product = generator.generate();
			assertNotNull(product);
			assertTrue("Expected 'Alice' or 'Bob', but was: " + product[0], 
					"Alice".equals(product[0]) || "Bob".equals(product[0]));
			assertTrue((Integer) product[1] == 23 || (Integer) product[1] == 34);
		}
	}

	/** TODO v0.6.x implement array mutation
	@Test
	public void testMutatingGeneration() {
		Object[] MUTATED_ALICE = new Object[] { "Alice", 24 };
		
		// define descriptor
		ArrayTypeDescriptor descriptor = createPersonDescriptor();
		descriptor.setGenerator(PersonAttrArrayGenerator.class.getName());
		descriptor.getElement(1).getLocalType(false).setScript("p[1] + 1");
		
		// create generator
		Generator<Object[]> generator = ArrayGeneratorFactory.createArrayGenerator("p", descriptor, Uniqueness.NONE, context);
		generator.init(context);
		
		// validate
		for (int i = 0; i < 10; i++)
			assertEqualArrays(MUTATED_ALICE, generator.generate());
	}
	*/

	@Test
	@SuppressWarnings("unchecked")
    public void testUniqueArrayGeneration() {
		ArrayTypeDescriptor arrayTypeDescriptor = new ArrayTypeDescriptor("MyArray");
		
		// create descriptor
		context.set("gen0", new SequenceTestGenerator<Integer>(1, 2));
		ArrayElementDescriptor e0 = new ArrayElementDescriptor(0, "int");
		((SimpleTypeDescriptor) e0.getLocalType(false)).setGenerator("gen0");
		arrayTypeDescriptor.addElement(e0);
		
		context.set("gen1", new SequenceTestGenerator<Integer>(3, 4));
		ArrayElementDescriptor e1 = new ArrayElementDescriptor(1, "int");
		((SimpleTypeDescriptor) e1.getLocalType(false)).setGenerator("gen1");
		arrayTypeDescriptor.addElement(e1);
		
		InstanceDescriptor arrayInstDescriptor = new InstanceDescriptor("array", arrayTypeDescriptor);
		arrayInstDescriptor.setUnique(true);
		
		// create generator
		Generator<Object[]> generator = (Generator<Object[]>) InstanceGeneratorFactory.createSingleInstanceGenerator(arrayInstDescriptor, Uniqueness.NONE, context);
		generator.init(context);
		
		// test generator
		assertArray(INT13, generator.generate());
		assertArray(INT14, generator.generate());
		assertArray(INT23, generator.generate());
		assertArray(INT24, generator.generate());
		assertNull(generator.generate());
	}

	@Test
	@SuppressWarnings("unchecked")
    public void testUniqueValuesArrayGeneration() {
		ArrayElementDescriptor e0 = new ArrayElementDescriptor(0, "int");
		((SimpleTypeDescriptor) e0.getLocalType(false)).setValues("1,2");
		
		ArrayElementDescriptor e1 = new ArrayElementDescriptor(1, "int");
		((SimpleTypeDescriptor) e1.getLocalType(false)).setValues("3,4");
		
		ArrayTypeDescriptor arrayTypeDescriptor = new ArrayTypeDescriptor("MyArray");
		arrayTypeDescriptor.addElement(e0);
		arrayTypeDescriptor.addElement(e1);
		
		InstanceDescriptor arrayInstDescriptor = new InstanceDescriptor("array", arrayTypeDescriptor);
		arrayInstDescriptor.setUnique(true);
		
		Generator<Object[]> generator = (Generator<Object[]>) InstanceGeneratorFactory.createSingleInstanceGenerator(arrayInstDescriptor, Uniqueness.NONE, context);
		generator.init(context);
		for (int i = 0; i < 4; i++) {
	        Object[] product = generator.generate();
	        assertTrue(
        		Arrays.equals(INT13, product)
        		|| Arrays.equals(INT14, product)
        		|| Arrays.equals(INT23, product)
        		|| Arrays.equals(INT24, product)
        	);
        }
		assertUnavailable(generator);
	}
	
	// helpers ---------------------------------------------------------------------------------------------------------

	@SuppressWarnings("null")
    private void assertArray(Object[] expected, Object[] actual) {
	    if (expected == null) {
	    	assertNull(actual);
	    	return;
	    }
        String failureMessage = failureMessage(expected, actual);
	    if (actual == null)
	        fail(failureMessage);
	    assertEquals(failureMessage, expected.length, actual.length);
	    for (int i = 0; i < expected.length; i++)
	    	assertTrue(failureMessage, expected[i].equals(actual[i]));
    }

	private String failureMessage(Object[] expected, Object[] actual) {
		return "Expected " + Arrays.toString(expected) + ", found: " + Arrays.toString(actual);
    }

	private static ArrayTypeDescriptor createPersonDescriptor() {
		ArrayTypeDescriptor arrayDescriptor = new ArrayTypeDescriptor("personType");
		ArrayElementDescriptor e1 = new ArrayElementDescriptor(0, "string");
		arrayDescriptor.addElement(e1);
		ArrayElementDescriptor e2 = new ArrayElementDescriptor(1, "int");
		arrayDescriptor.addElement(e2);
		return arrayDescriptor;
	}

	private void assertEqualArrays(Object[] expected, Object[] actual) {
	    assertTrue(errMsg(expected, actual), Arrays.equals(expected, actual));
    }

	private String errMsg(Object[] expected, Object[] actual) {
	    return "Expected {" + ArrayFormat.format(expected) + "} but found {" + ArrayFormat.format(actual) + "}";
    }

}
