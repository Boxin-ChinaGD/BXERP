package com.bx.erp.action.commodity;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.commodity.BranchBO;

@Controller
@RequestMapping("/branch")
@Scope("prototype")
public class BranchAction extends BaseAction {
	@Resource
	private BranchBO branchBO;
}
