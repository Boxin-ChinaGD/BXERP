DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheet_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheet_Update`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
 	IN iID INT,
	IN iProviderID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '该退货单没有退货商品';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该供应商不存在，请重新选择供应商';
		ELSEIF (SELECT F_Status FROM t_returncommoditysheet WHERE F_ID = iID) = 0 THEN
			UPDATE t_returncommoditysheet SET 
				F_ProviderID = iProviderID,
				F_UpdateDatetime = now()
			WHERE F_ID = iID;
			
			SELECT F_ID, F_SN, F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime, F_ShopID
			FROM t_returncommoditysheet
			WHERE F_ID = iID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := '该退货单已审核,不允许修改';
		END IF;
	
	COMMIT;
END;