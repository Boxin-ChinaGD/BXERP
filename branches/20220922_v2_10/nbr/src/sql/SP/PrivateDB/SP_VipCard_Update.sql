DROP PROCEDURE IF EXISTS `SP_VipCard_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipCard_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
    IN iID INT,
    IN sTitle VARCHAR(9),
	IN sBackgroundColor VARCHAR(23),
	IN iClearBonusDay INT, -- ��������,�������<1����,˵��ʹ����������dtClearBonusDatetime
	IN dtClearBonusDatetime DATETIME -- ��������
)
BEGIN	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	
	START TRANSACTION;
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		   
        IF NOT EXISTS(SELECT 1 FROM t_vipcard WHERE F_ID = iID) THEN
        	SET iErrorCode := 2;
			SET sErrorMsg := '��Ա��������';
		ELSE		
			UPDATE t_vipcard SET
				F_Title = sTitle,
				F_BackgroundColor = sBackgroundColor,
				F_ClearBonusDay = IF(iClearBonusDay < 1, NULL, iClearBonusDay),
				F_ClearBonusDatetime = IF(iClearBonusDay < 1, dtClearBonusDatetime, NULL)
			WHERE F_ID = iID;

			SELECT F_ID, F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime
   	   		FROM t_vipcard WHERE F_ID = iID;
		END IF;

	COMMIT;
END;