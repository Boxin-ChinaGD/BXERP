-- 有默认会员卡，不开放该接口给Action
DROP PROCEDURE IF EXISTS `SP_VipCard_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipCard_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN sTitle VARCHAR(9),
	IN sBackgroundColor VARCHAR(23),
	IN iClearBonusDay INT, -- 清零天数,如果传入负数,说明使用清零日期
	IN dtClearBonusDatetime DATETIME -- 清零日期
)
BEGIN
	DECLARE sSN VARCHAR(15);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	
	START TRANSACTION;
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';
        
        INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
		VALUES (sTitle, sBackgroundColor, iClearBonusDay, dtClearBonusDatetime, now());
		
		SELECT F_ID, F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime
   	   	FROM t_vipcard WHERE F_ID = LAST_INSERT_ID();

	COMMIT;
END;