DROP PROCEDURE IF EXISTS `SP_CouponScope_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CouponScope_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCouponID INT,
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
		
		SELECT F_ID, F_CouponID, F_CommodityID, F_CommodityName
		FROM t_couponscope WHERE 1=1
		AND (CASE iCouponID WHEN INVALID_ID THEN 1=1 ELSE F_CouponID = iCouponID END)
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord 
		FROM t_couponscope WHERE 1=1
		AND (CASE iCouponID WHEN INVALID_ID THEN 1=1 ELSE F_CouponID = iCouponID END);
		
	COMMIT;
END;