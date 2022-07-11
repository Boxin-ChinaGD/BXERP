package com.bx.erp.test.robot.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.bx.erp.model.BaseModel;
import com.bx.erp.test.robot.model.ProgramAttendee;
import com.bx.erp.test.robot.protocol.BaseBuffer;

/** 活动单元类：在一次机器人测试中，一种活动可能要做多次，每次被定义为一个单元，即活动单元。 <br />
 * 一个单元，包含什么时间做、操作时需要什么参数等2个元素 */
public class ProgramUnit extends BaseBuffer implements Cloneable {
	protected ProgramAttendee attendee; 
	public ProgramAttendee getAttendee() {
		return attendee;
	}

	public void setAttendee(ProgramAttendee attendee) {
		this.attendee = attendee;
	}

	/** 活动单元的序号 */
	protected int no;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

//	protected Date datetimeStart;
//
//	public Date getDatetimeStart() {
//		return datetimeStart;
//	}
//
//	public void setDatetimeStart(Date datetimeStart) {
//		this.datetimeStart = datetimeStart;
//	}

	protected int operationType;

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	
	// 以下4个参数为活动的输入
	protected BaseModel baseModelIn1;
	protected BaseModel baseModelIn2;
	protected List<BaseModel> listBaseModelIn1;
	protected List<BaseModel> listBaseModelIn2;

	// 以下4个参数为活动的输出
	protected BaseModel baseModelOut1;
	protected BaseModel baseModelOut2;
	protected List<BaseModel> listBaseModelOut1;
	protected List<BaseModel> listBaseModelOut2;

	public BaseModel getBaseModelIn1() {
		return baseModelIn1;
	}

	public void setBaseModelIn1(BaseModel baseModelIn1) {
		this.baseModelIn1 = baseModelIn1;
	}

	public BaseModel getBaseModelIn2() {
		return baseModelIn2;
	}

	public void setBaseModelIn2(BaseModel baseModelIn2) {
		this.baseModelIn2 = baseModelIn2;
	}

	public List<BaseModel> getListBaseModelIn1() {
		return listBaseModelIn1;
	}

	public void setListBaseModelIn1(List<BaseModel> listBaseModelIn1) {
		this.listBaseModelIn1 = listBaseModelIn1;
	}

	public List<BaseModel> getListBaseModelIn2() {
		return listBaseModelIn2;
	}

	public void setListBaseModelIn2(List<BaseModel> listBaseModelIn2) {
		this.listBaseModelIn2 = listBaseModelIn2;
	}

	public BaseModel getBaseModelOut1() {
		return baseModelOut1;
	}

	public void setBaseModelOut1(BaseModel baseModelOut1) {
		this.baseModelOut1 = baseModelOut1;
	}

	public BaseModel getBaseModelOut2() {
		return baseModelOut2;
	}

	public void setBaseModelOut2(BaseModel baseModelOut2) {
		this.baseModelOut2 = baseModelOut2;
	}

	public List<BaseModel> getListBaseModelOut1() {
		return listBaseModelOut1;
	}

	public void setListBaseModelOut1(List<BaseModel> listBaseModelOut1) {
		this.listBaseModelOut1 = listBaseModelOut1;
	}

	public List<BaseModel> getListBaseModelOut2() {
		return listBaseModelOut2;
	}

	public void setListBaseModelOut2(List<BaseModel> listBaseModelOut2) {
		this.listBaseModelOut2 = listBaseModelOut2;
	}

	@Override
	public ProgramUnit clone() {
		ProgramUnit pu = new ProgramUnit();
		try {
			if (baseModelIn1 != null) {
				pu.setBaseModelIn1(baseModelIn1.clone());
			}
			if (baseModelIn2 != null) {
				pu.setBaseModelIn2(baseModelIn2.clone());
			}
			if (listBaseModelIn1 != null) {
				List<BaseModel> listBaseModelIn1Clone = new ArrayList<BaseModel>();
				Collections.copy(listBaseModelIn1Clone, listBaseModelIn1);
				pu.setListBaseModelIn1(listBaseModelIn1Clone);
			}
			if (listBaseModelIn2 != null) {
				List<BaseModel> listBaseModelIn2Clone = new ArrayList<BaseModel>();
				Collections.copy(listBaseModelIn2Clone, listBaseModelIn2);
				pu.setListBaseModelIn2(listBaseModelIn2Clone);
			}
			if (baseModelOut1 != null) {
				pu.setBaseModelOut1(baseModelOut1.clone());
			}
			if (baseModelOut2 != null) {
				pu.setBaseModelOut2(baseModelOut2.clone());
			}
			if (listBaseModelOut1 != null) {
				List<BaseModel> listBaseModelOut1Clone = new ArrayList<BaseModel>();
				Collections.copy(listBaseModelOut1Clone, listBaseModelOut1);
				pu.setListBaseModelOut1(listBaseModelOut1Clone);
			}
			if (listBaseModelOut2 != null) {
				List<BaseModel> listBaseModelOut2Clone = new ArrayList<BaseModel>();
				Collections.copy(listBaseModelOut2Clone, listBaseModelOut2);
				pu.setListBaseModelOut2(listBaseModelOut2Clone);
			}
			pu.setNo(no);
			pu.setOperationType(operationType);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return pu;
	}
	
	
	public String toJson(ProgramUnit pu) {
		String str = JSON.toJSONString(pu);
		return str;
	}

	public BaseBuffer fromJson(String json) {
		ProgramUnit pu = JSON.parseObject(json, ProgramUnit.class);
		return pu;
	}
}
