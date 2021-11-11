DROP PROCEDURE IF EXISTS `SP_Subcommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Subcommodity_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iCommodityID INT,
   	IN iSubCommodityID INT
	)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN	
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		DELETE FROM t_subcommodity WHERE F_CommodityID = iCommodityID AND F_SubCommodityID = iSubCommodityID;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
	
	COMMIT;
END;