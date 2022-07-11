DROP PROCEDURE IF EXISTS `SP_Staff_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN queryKeyword VARCHAR(12),
	IN iStatus INT,
	IN iOperator INT, -- 1, OP在操作。0，Boss在操作
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
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID';
		
		SELECT 
			ts.F_ID, 
			ts.F_Name,
			ts.F_Phone, 
			ts.F_ICID, 
			ts.F_WeChat, 
			ts.F_OpenID, 
			ts.F_Unionid,
			ts.F_PasswordExpireDate, 
			ts.F_IsFirstTimeLogin, 
			ts.F_ShopID, 
			ts.F_DepartmentID, 
			ts.F_Status,
			ts.F_CreateDatetime,
			ts.F_UpdateDatetime,
	        (SELECT F_ID FROM t_role WHERE F_ID IN (SELECT F_RoleID FROM T_StaffRole WHERE F_StaffID = ts.F_ID)) AS roleID,
	  	    (SELECT F_Name from t_role WHERE F_ID IN (SELECT F_RoleID FROM T_StaffRole WHERE F_StaffID = ts.F_ID))  AS roleName,
	  	    (SELECT F_CompanyID FROM t_shop WHERE F_ID = ts.F_ShopID) AS companyID
		FROM t_staff ts 
		WHERE 1 = 1 
	   	AND (CASE ifnull(queryKeyword,'') WHEN '' THEN 1=1 ELSE ts.F_Name LIKE CONCAT('%', queryKeyword ,'%') OR (length(queryKeyword) >= 6 AND ts.F_Phone LIKE CONCAT(queryKeyword ,'%')) END )
	   	AND (CASE iStatus WHEN INVALID_ID THEN 1 = 1 ELSE ts.F_Status = iStatus END)
	   	AND (CASE iOperator WHEN 1 THEN 1 = 1 ELSE ts.F_Phone != '13888888888' END) -- OP才能查询出售前账号
	   	GROUP BY ts.F_ID
		ORDER BY ts.F_ID DESC
		LIMIT recordIndex,iPageSize;
		
		SELECT count(1) into iTotalRecord
		FROM t_staff ts 
		WHERE 1 = 1 
	   	AND (CASE ifnull(queryKeyword,'') WHEN '' THEN 1=1 ELSE ts.F_Name LIKE CONCAT('%', queryKeyword ,'%') OR (length(queryKeyword) >= 6 AND ts.F_Phone LIKE CONCAT(queryKeyword ,'%')) END )
	   	AND (CASE iStatus WHEN INVALID_ID THEN 1 = 1 ELSE ts.F_Status = iStatus END)
		AND (CASE iOperator WHEN 1 THEN 1 = 1 ELSE ts.F_Phone != '13888888888' END);
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;