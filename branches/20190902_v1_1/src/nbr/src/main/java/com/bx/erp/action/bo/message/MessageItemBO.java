package com.bx.erp.action.bo.message;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.dao.message.MessageItemMapper;
import com.bx.erp.model.BaseModel;

@Component("messageItemBO")
@Scope("prototype")
public class MessageItemBO extends BaseBO{
	
	protected final String SP_MessageItem_Create = "SP_MessageItem_Create";
	protected final String SP_MessageItem_RetrieveN = "SP_MessageItem_RetrieveN";
	
	@Resource
	private MessageItemMapper messageItemMapper;
	
	@Override
	protected void setMapper() {
		this.mapper = messageItemMapper;
	}
	
	@Override
	protected List<?> doRetrieveN(int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return super.doRetrieveN(iUseCaseID, s);
		}
	}
	
	@Override
	protected boolean checkRetrieveNPermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageItem_RetrieveN);
		}
	}
	
	@Override
	protected boolean checkCreatePermission(int staffID, int iUseCaseID, BaseModel s) {
		switch (iUseCaseID) {
		default:
			return checkStaffPermission(staffID, SP_MessageItem_Create);
		}
	}
}
