package com.my;

import org.databene.model.function.AbstractWeightFunction;

public class ReciprocalWeight extends AbstractWeightFunction{

	public double value(double x) {
		return 1 / x;
	}

}
