DROP PROCEDURE IF EXISTS `SP_VipSource_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipSource_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iPageIndex INT,
	IN iPageSize INT,
 	OUT iTotalRecord INT
)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		SET iPageIndex = iPageIndex - 1;
		SET recordIndex = iPageIndex * iPageSize;

	    SELECT F_ID, F_VipID, F_SourceCode, F_ID1, F_ID2, F_ID3
		FROM t_vipsource 
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord FROM t_vipsource;
		
	COMMIT;
END;