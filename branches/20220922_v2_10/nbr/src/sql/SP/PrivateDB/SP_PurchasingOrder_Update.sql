DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iProviderID INT,
	IN sRemark VARCHAR(128)
	)
BEGIN
	-- 0：未审核、1：已审核、2：部分入库、3：全部入库、4：已删除
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Status INTO iStatus FROM T_PurchasingOrder WHERE F_ID = iID;
		
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '采购订单不存在';	
		ELSEIF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '该采购订单没有采购商品';	
		ELSEIF NOT EXISTS(SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '供应商不存在，请重新选择供应商';
		ELSEIF iStatus = 0 THEN 
			Update T_PurchasingOrder SET
				F_UpdateDatetime = now(),
				F_ProviderID = iProviderID,
				F_ProviderName = (SELECT F_Name FROM t_provider WHERE F_ID = iProviderID),
				F_Remark = sRemark
				WHERE F_ID = iID;
			
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
				F_UpdateDatetime 
			FROM t_purchasingorder -- ...
			WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE 
			SET iErrorCode := 7;
			SET sErrorMsg := '该采购订单已审核，不允许修改';
		END IF;
	
	COMMIT;
END;