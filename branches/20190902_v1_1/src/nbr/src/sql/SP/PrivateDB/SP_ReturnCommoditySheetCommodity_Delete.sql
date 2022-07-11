DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheetCommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheetCommodity_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iReturnCommoditySheetID INT,
   	IN iCommodityID INT
	)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_Status = 1 AND F_ID = iReturnCommoditySheetID) THEN 
			
			SET iErrorCode := 7;
			SET sErrorMsg := '不能删除已审核的退货单中的数据';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_ID = iReturnCommoditySheetID) THEN 
			
			SET iErrorCode := 7;
			SET sErrorMsg := '此退货单不存在';
				
		-- iCommodityID>0代表删除一行退货单从表的数据,iCommodityID<=0代表删除此退货单ID在从表的所有数据
		ELSEIF (iCommodityID > 0) AND NOT EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_Status = 1 AND F_ID = iReturnCommoditySheetID) THEN
		   
			DELETE FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID AND F_ReturnCommoditySheetID = iReturnCommoditySheetID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
		   
			DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iReturnCommoditySheetID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END