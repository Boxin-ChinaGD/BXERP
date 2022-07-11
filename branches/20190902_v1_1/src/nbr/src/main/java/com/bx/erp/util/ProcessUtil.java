package com.bx.erp.util;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class ProcessUtil {
	private static Log logger = LogFactory.getLog(ProcessUtil.class);
	
	public static String readSharedMemory(FileChannel fc) throws Exception {
		int size = (int) fc.size();
		MappedByteBuffer buffer = fc.map(MapMode.READ_WRITE, 0, size);
		byte[] bytes = new byte[size];
		buffer.get(bytes);
		String msg = new String(bytes, 0, size).trim();
		
		if (!"".equals(msg) && null != msg) {
			logger.info("读取进程消息：" + msg);
			logger.info("读取进程消息完毕，清除消息...");
			for (int i = 0; i < size; i++) {
				buffer.put(i, (byte) 0);
			}
		}
		
		return msg;
	}
	
	public static void writeSharedMemory(FileChannel fc, String message) throws Exception {
		logger.info("发送消息之前清除共享内存，防止干扰...");
		int size = (int) fc.size();
		MappedByteBuffer buffer = fc.map(MapMode.READ_WRITE, 0, size);

		for (int i = 0; i < size; i++) {
			buffer.put(i, (byte) 0);
		}
		
		logger.info("发送进程消息：" + message);
		byte[] bytes = message.getBytes();
		buffer = fc.map(MapMode.READ_WRITE, 0, bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			buffer.put(i,bytes[i]);
		}
	}
	
	public static String readJSON(String json, String key) {
		if (json == null || "".equals(json)) {
			return "";
		} else {
			JSONObject jsonObject = JSONObject.fromObject(json);
			String value = JsonPath.read(jsonObject, "$." + key);
			return value;
		}
	}
}
