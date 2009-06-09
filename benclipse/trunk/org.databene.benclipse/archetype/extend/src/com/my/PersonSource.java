package com.my;

import java.util.Arrays;
import java.util.List;

import org.databene.commons.HeavyweightIterator;
import org.databene.commons.iterator.HeavyweightIteratorAdapter;
import org.databene.model.data.AbstractEntitySource;
import org.databene.model.data.Entity;

public class PersonSource extends AbstractEntitySource {
	
	private List<Entity> list;
	
	public PersonSource() {
		list = Arrays.asList(
			new Entity("Person", "name", "Alice"),
			new Entity("Person", "name", "Bob")
		);
	}

	public HeavyweightIterator<Entity> iterator() {
		return new HeavyweightIteratorAdapter<Entity>(list.iterator());
	}

}
