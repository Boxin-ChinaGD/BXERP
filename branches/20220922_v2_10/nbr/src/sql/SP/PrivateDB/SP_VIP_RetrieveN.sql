DROP PROCEDURE IF EXISTS `SP_VIP_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIP_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN sDistrict VARCHAR(30),
	IN iCategory INT,
	IN sMobile VARCHAR(11),
	IN sICID VARCHAR(30),
	IN sEmail VARCHAR(30),
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
)
BEGIN	
	DECLARE recordIndex INT;
	DECLARE INVALID_ID INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		
		SET iPageIndex = iPageIndex -1;
	  	SET recordIndex = iPageIndex * iPageSize;
	  	
			SELECT 
				F_ID, 
				F_SN, 
				F_CardID,
				F_Mobile, 
				F_LocalPosSN, 
				F_Sex, 
				F_Logo,  
				F_ICID, 
				F_Name, 
				F_Email, 
				F_ConsumeTimes, 
				F_ConsumeAmount, 
				F_District, 
				F_Category, 
				F_Birthday, 
				F_Bonus, 
				F_LastConsumeDatetime,
				F_Remark
			FROM t_vip WHERE 1 = 1
			AND (CASE sDistrict WHEN '' THEN 1 = 1 ELSE F_District = sDistrict END)
			AND (CASE iCategory WHEN INVALID_ID THEN 1 = 1 ELSE F_Category = iCategory END)
		   	AND (CASE sICID WHEN '' THEN 1=1 ELSE F_ICID = sICID END)
		   	AND (CASE sEmail WHEN '' THEN 1 = 1 ELSE F_Email = sEmail END)
			AND (CASE sMobile WHEN '' THEN 1 = 1 ELSE F_Mobile = sMobile END)
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
			SELECT count(1) INTO iTotalRecord 
			FROM t_vip WHERE 1 = 1
			AND (CASE sDistrict WHEN '' THEN 1 = 1 ELSE F_District = sDistrict END)
			AND (CASE iCategory WHEN INVALID_ID THEN 1 = 1 ELSE F_Category = iCategory END)
			AND (CASE sICID WHEN '' THEN 1=1 ELSE F_ICID = sICID END)
			AND (CASE sEmail WHEN '' THEN 1 = 1 ELSE F_Email = sEmail END)
			AND (CASE sMobile WHEN '' THEN 1 = 1 ELSE F_Mobile = sMobile END);
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
			
	COMMIT;
END;