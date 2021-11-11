DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
	IN iStaffID INT,
	IN iProviderID INT,
	IN sRemark VARCHAR(128)
	)
BEGIN
	DECLARE icompanyID INT;
	DECLARE ipurchasingOrderID INT;
	DECLARE sSN VARCHAR(20);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该供应商不存在，请重新选择供应商';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该门店不存在，请重新选择门店';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID AND F_Status = 0) THEN	-- 检查员工ID：是否不存在或是否为离职员工
			SET iErrorCode := 7;
			SET sErrorMsg := '当前帐户不允许创建采购单';
		ELSE 
		
			SELECT F_SN INTO sSN FROM t_purchasingorder order by F_ID DESC limit 1;
			SELECT Func_GenerateSN('CG', sSN) INTO sSN;
			
			INSERT INTO T_PurchasingOrder(
				F_SN,
				F_ShopID,
				F_CreateDatetime, 
				F_StaffID, 
				F_ProviderID, 
				F_ProviderName, 
				F_Remark
	   		) 
			VALUES(
				sSN,
				iShopID,
				now(),
				iStaffID,
				iProviderID,
				(SELECT F_Name FROM t_provider WHERE F_ID = iProviderID),
				sRemark
			);
		   
			SET ipurchasingOrderID := last_insert_id();
			
			SELECT F_CompanyID INTO icompanyID FROM t_shop WHERE F_ID = (SELECT F_ShopID FROM t_staff WHERE F_ID = iStaffID );
			
	   		INSERT INTO t_message (F_CategoryID, F_IsRead, F_Parameter, F_CreateDatetime, F_SenderID, F_ReceiverID,F_CompanyID)
			VALUES (1, 0, '[{\"Link1\": \"www.xxxx.com\"}, {\"Link1_Tag\": \"有采购订单生成,待审核\"}]', now(), 1, 1, icompanyID);
			
			SELECT 
				F_ID,
				F_ShopID,
				F_SN, 
				F_Status, 
				F_StaffID, 
				F_ProviderID, 
				F_ProviderName, 
				F_ApproverID, 
				F_Remark, 
				F_CreateDatetime, 
				F_UpdateDatetime,
				(SELECT F_ID FROM t_message WHERE F_ID = last_insert_id()) AS messageID
			FROM t_purchasingorder WHERE F_ID = ipurchasingOrderID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;	
	
	COMMIT;
END;