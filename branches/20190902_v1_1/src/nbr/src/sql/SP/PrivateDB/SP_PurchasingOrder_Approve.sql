DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Approve`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Approve`(
	OUT iErrorCode INT, 
	OUT sErrorMsg VARCHAR(64), 
	IN iID INT,
	IN iApproverID INT
	)
BEGIN
	DECLARE iStatus INT;
	DECLARE iProviderID INT;
	DECLARE iCommodityID INT;
	DECLARE pocID INT;	
	DECLARE done INT DEFAULT FALSE;
	DECLARE list CURSOR FOR (SELECT 
		F_CommodityID AS iCommodityID, 
		F_ID AS pocID
	FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  -- 指定游标循环结束时返回值
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- 0：未审核、1：已审核、2：部分入库、3：全部入库、4：已删除
		SELECT F_Status, F_ProviderID INTO iStatus, iProviderID FROM T_PurchasingOrder WHERE F_ID = iID;
	
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;	
			SET sErrorMsg := '采购订单不存在';	
		ELSEIF NOT EXISTS(SELECT F_Name FROM t_staff WHERE F_ID = iApproverID AND F_Status = 0) THEN	-- 检查员工ID：是否不存在或是否为离职员工
			SET iErrorCode := 7;	
			SET sErrorMsg := '当前帐户不允许审核';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID) THEN
			SET iErrorCode := 7;	
			SET sErrorMsg := '该采购订单没有采购商品';
		ELSE
			IF Func_ValidateStateChange(2, iStatus, 1) = 1 THEN
				Update T_PurchasingOrder SET 
					F_Status = 1,
					F_ProviderName = (SELECT F_Name FROM t_provider WHERE F_ID = iProviderID),
					F_ApproveDatetime = now(),
					F_UpdateDatetime = now(),
					F_ApproverID = iApproverID
				WHERE F_ID = iID;
					
				SELECT F_ID, F_ShopID, F_SN, F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_ApproverID, F_Remark, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime, F_UpdateDatetime
				FROM T_PurchasingOrder
				WHERE F_ID = iID;
				
				OPEN list;
				read_loop: LOOP
					FETCH list INTO iCommodityID,pocID;
					IF done THEN
				  		LEAVE read_loop;
					END IF;
					
					UPDATE t_purchasingordercommodity SET
						F_CommodityName =  (SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID)
					WHERE F_ID = pocID;
						
		   		END LOOP read_loop;
			
		   		CLOSE list;
						
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET iErrorCode := 7; -- 不符合商业逻辑
				SET sErrorMsg := '该采购单已审核，请勿重复操作';
			END IF;
		END IF;
		
	COMMIT;
END;