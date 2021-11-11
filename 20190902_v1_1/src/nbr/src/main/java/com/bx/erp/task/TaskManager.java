package com.bx.erp.task;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.bx.erp.model.TaskType.EnumTaskType;

@Component("taskManager")
public class TaskManager {
	private static HashMap<EnumTaskType, TaskThread> list = new HashMap<EnumTaskType, TaskThread>();
	/** 为TestNG测试而设的变量或函数，不能在非测试代码中调用！！！！！ */
	private static TaskScheduler taskScheduler;

	/** 为TestNG测试而设的变量或函数，不能在非测试代码中调用！！！！！ */
	public static TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	/** 为TestNG测试而设的变量或函数，不能在非测试代码中调用！！！！！ */
	public static void setTaskScheduler(TaskScheduler taskScheduler) {
		TaskManager.taskScheduler = taskScheduler;
	}

	public static void register(EnumTaskType ect, TaskThread bc) {
		list.put(ect, bc);
	}

	public static TaskThread getCache(EnumTaskType ect) {
		return list.get(ect);
	}

}
