DROP PROCEDURE IF EXISTS `SP_PromotionSyncCache_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PromotionSyncCache_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_ID, F_SyncSequence, F_SyncData_ID, F_SyncType FROM t_promotionsynccache ORDER BY F_ID DESC;
		SET iTotalRecord := found_rows();
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;