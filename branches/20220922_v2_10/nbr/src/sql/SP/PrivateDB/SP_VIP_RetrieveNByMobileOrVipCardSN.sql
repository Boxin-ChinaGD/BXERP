DROP PROCEDURE IF EXISTS `SP_VIP_RetrieveNByMobileOrVipCardSN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VIP_RetrieveNByMobileOrVipCardSN`(  -- ...应该是R1
OUT iErrorCode INT,
OUT sErrorMsg VARCHAR(64),
IN sVipCardSN VARCHAR(16),
IN sMobile VARCHAR(11),
IN iPageIndex INT,
IN iPageSize INT,
OUT iTotalRecord INT
)
BEGIN
DECLARE recordIndex INT;
DECLARE EXIT HANDLER FOR SQLEXCEPTION 
BEGIN
	SET iErrorCode := 3;
	SET sErrorMsg := '数据库错误';
	ROLLBACK;
END;

START TRANSACTION;

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
		F_Remark, 
		F_CreateDatetime, 
		F_UpdateDatetime
	FROM t_vip			
	WHERE  -- ... 状态需要判断嘛？会员好像不支持删除
	(CASE sMobile WHEN '' THEN 1=1 ELSE length(sMobile) > 5 AND F_Mobile LIKE CONCAT('%', sMobile, '%') END)  -- ...需要精确查找，不能使用like
	AND (CASE sVipCardSN WHEN '' THEN 1=1 ELSE F_ID = (SELECT F_VipID FROM T_VipCardCode WHERE F_SN LIKE CONCAT('%', sVipCardSN, '%')) END)
	ORDER BY F_ID ASC
	LIMIT recordIndex, iPageSize;
	
	SELECT count(1) into iTotalRecord
	FROM t_vip WHERE
	(CASE sMobile WHEN '' THEN 1=1 ELSE length(sMobile) > 5 AND F_Mobile LIKE CONCAT('%', sMobile, '%') END)
	AND (CASE sVipCardSN WHEN '' THEN 1=1 ELSE F_ID = (SELECT F_VipID FROM T_VipCardCode WHERE F_SN LIKE CONCAT('%', sVipCardSN, '%')) END);

	SET iErrorCode=0;
	SET sErrorMsg := '';
	
COMMIT;
END;