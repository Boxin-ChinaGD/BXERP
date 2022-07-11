DROP PROCEDURE IF EXISTS `SP_PurchasingOrder_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PurchasingOrder_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	-- ... 0：未审核、1：已审核、2：部分入库、3：全部入库、4：已删除	
	DECLARE status INT;
	DECLARE iCheckDependency VARCHAR(32);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
  		SELECT Func_CheckPurchasingOrderDependency(iID, sErrorMsg) INTO iCheckDependency;
   		
		IF NOT EXISTS(SELECT 1 FROM t_purchasingorder WHERE F_ID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '不能删除不存在的采购单';
		ELSEIF iCheckDependency <> '' THEN
			SET iErrorCode := 7;
			SET sErrorMsg := iCheckDependency; 
		ELSE 
			SELECT F_Status INTO status FROM t_purchasingorder WHERE F_ID = iID;
			IF Func_ValidateStateChange(2, status, 4) = 1 THEN
				UPDATE t_purchasingorder SET F_Status = 4, F_ProviderID = NULL WHERE F_ID = iID;
				-- 采购订单变为已删除状态，那么被该采购订单采购的商品可以被删除，删除商品需要删除所有的条形码，单条形码与采购订单商品有外键约束，所以这里需要删除采购订单商品
			    DELETE FROM t_purchasingordercommodity WHERE F_PurchasingOrderID = iID;
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET iErrorCode := 7;
				SET sErrorMsg := '该采购订单已审核，不允许删除';
			END IF;
		END IF;
	
	COMMIT;
END;