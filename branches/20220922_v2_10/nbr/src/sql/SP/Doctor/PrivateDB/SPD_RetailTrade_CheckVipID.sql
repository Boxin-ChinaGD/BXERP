DROP PROCEDURE IF EXISTS `SPD_RetailTrade_CheckVipID`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_RetailTrade_CheckVipID`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	-- ...��ʱģ�壬�����ֵ��Ҫ��������仯
	DECLARE iID INT;
	DECLARE iVipID INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iID, F_VipID AS iVipID FROM t_retailtrade);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iID, iVipID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ������е����۵���VipID����Ϊ�գ��������vip����
			-- ������ǣ���ô����Ϊ���ݲ�����
			IF iVipID IS NULL THEN -- VipIDΪ��
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSEIF EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iVipID) THEN -- VipID��Ϊ�ղ��Ҵ���vip��
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE -- VipID��Ϊ�ղ��Ҳ�����vip��
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('IDΪ', iID, '�����۵���VipID������vip����');
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;