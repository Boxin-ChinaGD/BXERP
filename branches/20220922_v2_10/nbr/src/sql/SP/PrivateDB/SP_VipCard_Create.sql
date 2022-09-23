-- ��Ĭ�ϻ�Ա���������Ÿýӿڸ�Action
DROP PROCEDURE IF EXISTS `SP_VipCard_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipCard_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN sTitle VARCHAR(9),
	IN sBackgroundColor VARCHAR(23),
	IN iClearBonusDay INT, -- ��������,������븺��,˵��ʹ����������
	IN dtClearBonusDatetime DATETIME -- ��������
)
BEGIN
	DECLARE sSN VARCHAR(15);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
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