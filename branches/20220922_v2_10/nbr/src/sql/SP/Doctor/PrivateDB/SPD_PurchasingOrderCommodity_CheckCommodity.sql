DROP PROCEDURE IF EXISTS `SPD_PurchasingOrderCommodity_CheckCommodity`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_PurchasingOrderCommodity_CheckCommodity`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iPurchasingOrderCommodityID INT;
	DECLARE iStatus INT;
	DECLARE iType INT;
	DECLARE iNO INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
    DECLARE list CURSOR FOR (SELECT F_ID AS iPurchasingOrderCommodityID FROM t_purchasingordercommodity);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
		
			FETCH list INTO iPurchasingOrderCommodityID;
			IF done THEN
				LEAVE read_loop;
			END IF;
			-- 
			-- ������вɹ���������Ʒ����Ʒ��������Ʒ����
			-- ��������ڣ�����Ϊ���ݲ�����
			SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			-- 
			IF found_rows() > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, 'û�ж�Ӧ����ƷID');
			   	LEAVE read_loop;
			END IF;
			-- 	
			-- ������вɹ������е���Ʒ����Ϊ���װ��Ʒ�������Ʒ����ɾ����Ʒ�ͷ�����Ʒ
			-- ���Ϊ��ɾ������Ʒ����ô����Ϊ���ݲ�����
			SELECT F_Status INTO iStatus FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			-- 
			IF iStatus = 2 THEN 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ��ɾ����Ʒ');
			  	LEAVE read_loop;
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			-- 
		  	SELECT F_Type INTO iType FROM t_commodity WHERE F_ID IN (SELECT F_CommodityID FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID);
			IF iType = 2 THEN
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ���װ��Ʒ');
			   	LEAVE read_loop;
		  	ELSEIF iType = 1 THEN
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ�������Ʒ');
			 	LEAVE read_loop;
			ELSEIF iType = 3 THEN
				SET done = TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, '��Ӧ����Ʒ����Ϊ��������Ʒ');
			 	LEAVE read_loop;
			ELSE 
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			END IF;
			-- ������вɹ���Ʒ���е���Ʒ����������ȫ������0
			-- ���������0������Ϊ���ݲ�����
			SELECT F_CommodityNO INTO iNO FROM t_purchasingordercommodity WHERE F_ID = iPurchasingOrderCommodityID;
			IF iNO > 0 THEN
				SET iErrorCode := 0;
				SET sErrorMsg := '';
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('�ɹ�������Ʒ', iPurchasingOrderCommodityID, '����Ʒ�����������0');
			    LEAVE read_loop;
			END IF;

		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;