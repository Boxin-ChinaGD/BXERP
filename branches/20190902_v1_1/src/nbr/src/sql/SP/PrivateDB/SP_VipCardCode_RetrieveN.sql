DROP PROCEDURE IF EXISTS `SP_VipCardCode_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipCardCode_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
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

	    SELECT F_ID, F_VipID, F_VipCardID, F_SN, F_CreateDatetime
		FROM t_vipcardcode
		WHERE 1 = 1
		AND (CASE iVipID WHEN -1 THEN 1 = 1 ELSE F_VipID = iVipID END) 
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord 
		FROM t_vipcardcode 
		WHERE 1 = 1
		AND (CASE iVipID WHEN -1 THEN 1 = 1 ELSE F_VipID = iVipID END);
		
	COMMIT;
END;