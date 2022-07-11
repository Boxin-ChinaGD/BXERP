-- 删除Staff时，应该检查的依赖。
-- 返回值：
-- ''，可以删除Staff。
-- 其它errorMsg，不可以删除Staff
DROP FUNCTION IF EXISTS Func_CheckStaffDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckStaffDependency`(
	iStaffID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF (SELECT F_RoleID FROM t_staffrole WHERE F_StaffID = iStaffID) = 6 THEN -- 角色ID6为售前人员
		SET sErrorMsg := '';
	 	RETURN sErrorMsg;
	END IF;
	
--	一个门店的老板无论在什么情况下，都是可以删除一个员工的，所以不需要检查依赖
--	IF EXISTS (SELECT 1 FROM T_Warehouse WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有仓库依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_PurchasingOrder WHERE F_ApproverID = iStaffID OR F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有采购依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_Warehousing WHERE F_ApproverID = iStaffID OR F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有入库依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_ReturnCommoditySheet WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有仓管退货依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_InventorySheet WHERE F_ApproverID = iStaffID OR F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有盘点依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_RetailTrade WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有零售单依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_RetailTradeAggregation WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有服务器的收银汇总依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_Promotion WHERE F_Staff = iStaffID) THEN
--		SET sErrorMsg := '该员工有促销依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_CommodityHistory WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有商品历史依赖，不能删除';
--	ELSEIF EXISTS(SELECT 1 FROM T_RetailTradeDailyReportByStaff WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '该员工有Staff零售报表依赖，不能删除';
--	ELSE 
		SET sErrorMsg := '';
--	END IF;	
	RETURN sErrorMsg;
END;