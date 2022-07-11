DROP PROCEDURE IF EXISTS `SP_Staff_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Staff_Retrieve1`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN sPhone VARCHAR(32),
	IN iInvolvedResigned INT,
	IN iReturnSalt INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
	
		SELECT 
			st.F_ID, 
			st.F_Name,
			F_Phone, 
			F_ICID, 
			F_WeChat, 
			F_OpenID, 
			F_Unionid,
			F_pwdEncrypted,
			IF(iReturnSalt = 0, NULL, F_Salt) AS F_Salt,
			F_PasswordExpireDate, 
			F_IsFirstTimeLogin, 
			F_ShopID, 
			F_DepartmentID, 
			st.F_Status,
			st.F_CreateDatetime,
			st.F_UpdateDatetime,
			sh.F_Name AS shopName
		FROM t_staff st, t_shop sh 
		WHERE st.F_ShopID = sh.F_ID
		AND (CASE iID WHEN 0 THEN 1 = 1 ELSE st.F_ID = iID END)
		AND (CASE sPhone WHEN '' THEN 1 = 1 ELSE st.F_Phone = sPhone END)
		AND (CASE iInvolvedResigned WHEN 1 THEN 1 = 1 ELSE st.F_Status = 0 END) -- iInvolvedResigned为1是包含离职
		ORDER BY st.F_ID DESC
		LIMIT 1; -- 在职员工的手机号码不能相同，但可以和离职员工的相同，所以这里结果集可能超过一个，需要限制为1个
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;