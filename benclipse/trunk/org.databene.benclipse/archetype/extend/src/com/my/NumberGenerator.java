package com.my;

import org.databene.benerator.IllegalGeneratorStateException;
import org.databene.benerator.util.LightweightGenerator;

public class NumberGenerator extends LightweightGenerator<Long>{
	
	private long n;

	public NumberGenerator(long initialValue) {
		super(Long.class);
		n = initialValue;
	}

	public Long generate() throws IllegalGeneratorStateException {
		return n++;
	}

}
