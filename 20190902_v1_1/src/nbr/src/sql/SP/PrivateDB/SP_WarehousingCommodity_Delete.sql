DROP PROCEDURE IF EXISTS `SP_WarehousingCommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WarehousingCommodity_Delete` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iWarehousingID INT,
   	IN iCommodityID INT
)
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF iCommodityID > 0 THEN
			DELETE FROM t_WarehousingCommodity WHERE F_CommodityID = iCommodityID AND F_WarehousingID = iWarehousingID;
		ELSE
			DELETE FROM t_WarehousingCommodity WHERE F_WarehousingID = iWarehousingID;
		END IF;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;