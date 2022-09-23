DROP PROCEDURE IF EXISTS `SP_CouponCode_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponCode_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN iCouponID INT,
	IN iStatus INT,
	IN iPageIndex INT,
	IN iPageSize INT,
 	OUT iTotalRecord INT
)
BEGIN
	DECLARE INVALID_ID INT;
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
		
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		
		SET iPageIndex = iPageIndex - 1;
		SET recordIndex = iPageIndex * iPageSize;

	    SELECT F_ID, F_VipID, F_CouponID, F_Status, F_SN, F_CreateDatetime, F_UsedDatetime
		FROM t_couponcode WHERE 1=1
		AND (CASE iVipID WHEN INVALID_ID THEN 1=1 ELSE F_VipID = iVipID END)
		AND (CASE iCouponID WHEN INVALID_ID THEN 1=1 ELSE F_CouponID = iCouponID END)
		AND (CASE iStatus WHEN INVALID_ID THEN 1 = 1 ELSE F_Status = iStatus END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord FROM t_couponcode;
		
	COMMIT;
END;