package com.bx.erp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bx.erp.action.BaseAction;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonValueProcessorOfDate implements JsonValueProcessor {
	private Log logger = LogFactory.getLog(JsonValueProcessorOfDate.class);
	
	private String datePattern = BaseAction.DATETIME_FORMAT_Default1; // 默认的日期格式

	public JsonValueProcessorOfDate() {
		super();
	}

	public JsonValueProcessorOfDate(String format) {
		super();
		this.datePattern = format;
	}

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		try {
			if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.CHINA);
				return sdf.format((Date) value);
			}
			return (value == null ? "" : value.toString());
		} catch (Exception e) {
			logger.info("JsonValueProcessorOfDate.process()：" + e.getMessage());
			return "";
		}
	}

	public String getDatePattern() {
		return datePattern;
	}

	// public void setDatePattern(String datePaterns) {
	// this.datePattern = datePaterns;
	// }
}
