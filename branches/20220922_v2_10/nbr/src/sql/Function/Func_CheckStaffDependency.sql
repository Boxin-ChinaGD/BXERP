-- ɾ��Staffʱ��Ӧ�ü���������
-- ����ֵ��
-- ''������ɾ��Staff��
-- ����errorMsg��������ɾ��Staff
DROP FUNCTION IF EXISTS Func_CheckStaffDependency;
CREATE DEFINER=`root`@`localhost` FUNCTION `Func_CheckStaffDependency`(
	iStaffID INT,
	sErrorMsg VARCHAR(32)
) RETURNS VARCHAR(32)
BEGIN
	IF (SELECT F_RoleID FROM t_staffrole WHERE F_StaffID = iStaffID) = 6 THEN -- ��ɫID6Ϊ��ǰ��Ա
		SET sErrorMsg := '';
	 	RETURN sErrorMsg;
	END IF;
	
--	һ���ŵ���ϰ�������ʲô����£����ǿ���ɾ��һ��Ա���ģ����Բ���Ҫ�������
--	IF EXISTS (SELECT 1 FROM T_Warehouse WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա���вֿ�����������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_PurchasingOrder WHERE F_ApproverID = iStaffID OR F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա���вɹ�����������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_Warehousing WHERE F_ApproverID = iStaffID OR F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա�����������������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_ReturnCommoditySheet WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա���вֹ��˻�����������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_InventorySheet WHERE F_ApproverID = iStaffID OR F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա�����̵�����������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_RetailTrade WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա�������۵�����������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_RetailTradeAggregation WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա���з�������������������������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_Promotion WHERE F_Staff = iStaffID) THEN
--		SET sErrorMsg := '��Ա���д�������������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_CommodityHistory WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա������Ʒ��ʷ����������ɾ��';
--	ELSEIF EXISTS(SELECT 1 FROM T_RetailTradeDailyReportByStaff WHERE F_StaffID = iStaffID) THEN
--		SET sErrorMsg := '��Ա����Staff���۱�������������ɾ��';
--	ELSE 
		SET sErrorMsg := '';
--	END IF;	
	RETURN sErrorMsg;
END;