DROP PROCEDURE IF EXISTS `SP_BonusConsumeHistory_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_BonusConsumeHistory_RetrieveN`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iVipID INT,
	IN sVipMobile VARCHAR(11),
	IN sVipName VARCHAR(32), 
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
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
		SELECT F_Value INTO INVALID_ID FROM t_nbrconstant WHERE F_Key = 'INVALID_ID'; 
		
		SET iPageIndex = iPageIndex - 1;
		SET recordIndex = iPageIndex * iPageSize;
		
		IF iVipID <> -1 AND NOT EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '会员ID不存在';
		ELSE
			SELECT F_ID, F_VipID, F_StaffID, F_Bonus, F_AddedBonus, F_Remark, F_CreateDatetime,
			(SELECT F_Name FROM t_vip WHERE F_ID = F_VipID) AS F_VipName,
			(CASE WHEN (SELECT F_Name FROM t_staff WHERE F_ID = F_StaffID) IS NULL THEN '系统' ELSE (SELECT F_Name FROM t_staff WHERE F_ID = F_StaffID) END) AS F_StaffName,
			(SELECT F_Mobile FROM t_vip WHERE F_ID = F_VipID) AS F_VipMobile
			FROM t_bonusconsumehistory 
			WHERE 1=1
			AND (CASE iVipID WHEN INVALID_ID THEN 1=1 ELSE F_VipID = iVipID END)
			AND (CASE sVipMobile WHEN '' THEN 1=1 ELSE F_VipID IN (SELECT F_ID FROM t_vip WHERE F_Mobile = sVipMobile) END)
			AND (CASE sVipName WHEN '' THEN 1=1 ELSE F_VipID IN (SELECT F_ID FROM t_vip WHERE F_Name LIKE concat('%', sVipName, '%')) END)
			ORDER BY F_ID DESC
			LIMIT recordIndex, iPageSize;
			
			SELECT count(1) into iTotalRecord FROM t_bonusconsumehistory 
			WHERE 1=1
			AND (CASE iVipID WHEN INVALID_ID THEN 1=1 ELSE F_VipID = iVipID END)
			AND (CASE sVipMobile WHEN '' THEN 1=1 ELSE F_VipID IN (SELECT F_ID FROM t_vip WHERE F_Mobile = sVipMobile) END)
			AND (CASE sVipName WHEN '' THEN 1=1 ELSE F_VipID IN (SELECT F_ID FROM t_vip WHERE F_Name LIKE concat('%', sVipName, '%')) END);
		END IF;
	
	COMMIT;
END;