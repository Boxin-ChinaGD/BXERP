package com.bx.erp.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonValueProcessorOfDouble implements JsonValueProcessor  {
	@Override
	public Object processArrayValue(Object value, JsonConfig arg1) {
		if (value instanceof double[]) {
			String[] obj = {};
			double[] nums = (double[]) value;
			for (int i = 0; i < nums.length; i++) {
				obj[i] = roundHalfUp(nums[i], 6);
			}

			return obj;
		}

		return value;
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig arg2) {
		if (value instanceof Float) {
			return roundHalfUp((Float)value, 6);
		}
		return value;
	}
	
    public String roundHalfUp(double number, int frac) {
    	DecimalFormat fmt = (DecimalFormat) NumberFormat.getInstance();
    	fmt.setMaximumFractionDigits(frac);
    	
    	return fmt.format(number).replaceAll(",", "");
    }
}
