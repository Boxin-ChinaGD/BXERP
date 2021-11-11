package com.bx.erp.action.bo.commodity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bx.erp.action.bo.BaseBO;

@Component("branchBO")
@Scope("prototype")
public class BranchBO extends BaseBO {

	@Override
	protected void setMapper() {
		throw new RuntimeException("尚未实现的方法setMapper()");
	}


}
