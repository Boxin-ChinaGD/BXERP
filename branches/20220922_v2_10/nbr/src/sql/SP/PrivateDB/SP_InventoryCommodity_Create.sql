DROP PROCEDURE IF EXISTS `SP_InventoryCommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_InventoryCommodity_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iInventorySheetID INT, 
	IN iCommodityID INT,
	IN iNOReal INT,
	IN iBarcodeID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END; 
	
	START TRANSACTION;
				
		IF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_InventorySheetID = iInventorySheetID AND F_CommodityID = iCommodityID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '���������ͬ����Ʒ��ͬһ���̵㵥��';
		ELSEIF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_Type <> 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := 'ֻ�ܴ�����ͨ��Ʒ���̵㵥';
		ELSE
			INSERT INTO t_inventorycommodity (
				F_InventorySheetID, 
		   		F_CommodityID,
		   		F_CommodityName, 
				F_Specification, 
				F_BarcodeID, 
				F_PackageUnitID, 
				F_NOReal,
				F_NOSystem
				)
	   		VALUES (
		  		iInventorySheetID, 
		   		iCommodityID, 
		   		(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID),
		   		(SELECT F_Specification FROM t_commodity WHERE F_ID = iCommodityID),
		   		iBarcodeID,
		   		(SELECT F_PackageUnitID FROM t_commodity WHERE F_ID = iCommodityID),
		   		iNOReal,
		   		-10000000  -- ֵΪ-10000000����ʾ���ֶθ��û���
	   			);	
	   			
			SELECT 
				F_ID, 
				F_InventorySheetID, 
				F_CommodityID, 
				F_CommodityName, 
				F_Specification, 
				F_BarcodeID, 
				F_PackageUnitID, 
				F_NOReal, 
				F_NOSystem, 
				F_CreateDatetime, 
				F_UpdateDatetime
			FROM t_inventorycommodity WHERE F_ID = LAST_INSERT_ID();
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;