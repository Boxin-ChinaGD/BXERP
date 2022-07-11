package com.bx.erp.util;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import net.sf.json.JsonConfig;

@Component("jsonUtil")
public class JsonUtil {
	private Log logger = LogFactory.getLog(JsonUtil.class);
	
 	public static JsonConfig jsonConfig;

	@PostConstruct 
	public void init() {
		jsonConfig = new JsonConfig();
		JsonValueProcessorOfDate jsonValueProcessorOfDate  = new JsonValueProcessorOfDate();
		jsonConfig.registerJsonValueProcessor(Date.class, jsonValueProcessorOfDate);
		
		logger.info("JsonConfig初始化成功。采用的日期格式为：" + jsonValueProcessorOfDate.getDatePattern());
	}
}
