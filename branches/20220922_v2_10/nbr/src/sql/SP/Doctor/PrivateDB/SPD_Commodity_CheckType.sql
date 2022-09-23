DROP PROCEDURE IF EXISTS `SPD_Commodity_CheckType`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SPD_Commodity_CheckType`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64)
)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE iType INT;
	DECLARE done INT DEFAULT FALSE;
	-- 
	-- ��ʱģ�壬����list��Ҫ��������仯
	DECLARE list CURSOR FOR (SELECT F_ID AS iCommodityID, F_Type AS iType FROM t_commodity WHERE F_Status <> 2);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;  
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		SET iErrorCode := 3, sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		OPEN list;
		read_loop: LOOP
			FETCH list INTO iCommodityID, iType;
			IF done THEN
				LEAVE read_loop;
			END IF;
			
			-- ���������Ʒ�����ͣ���Ʒ����ֻ��Ϊ0,1,2,3
			IF iType = 0 OR iType = 1 OR iType = 3 THEN
			-- ��Ʒ����Ϊ1�����ڲ�����ƷID�Ͳ�����Ʒ������ҪΪ0
			-- ��Ʒ����Ϊ0�����ڲ�����ƷID�Ͳ�����Ʒ������ҪΪ0
			-- ��Ʒ����Ϊ3�����ڲ�����ƷID�Ͳ�����Ʒ������ҪΪ0
				IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_RefCommodityID = 0 AND F_RefCommodityMultiple = 0) THEN
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_RefCommodityID = 0) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '�Ĳ�����ƷID����ȷ����Ʒ�������Ʒ�Ĳ�����ƷIDΪ0');
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID AND F_RefCommodityMultiple = 0) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '�Ĳ�����Ʒ��������ȷ����Ʒ�������Ʒ�Ĳ�����Ʒ����Ϊ0');
				END IF;
			-- ��Ʒ����Ϊ2�����ڲ�����ƷID�ǵ�Ʒ�Ͳ�����Ʒ��������1
			ELSEIF iType = 2 THEN 
				IF EXISTS(SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_RefCommodityID FROM t_commodity WHERE F_ID = iCommodityID) AND F_Type = 0) 
				AND EXISTS(SELECT 1 FROM t_commodity WHERE F_RefCommodityMultiple > 1 AND F_ID = iCommodityID) THEN 
					SET iErrorCode := 0;
					SET sErrorMsg := '';
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID IN (SELECT F_RefCommodityID FROM t_commodity WHERE F_ID = iCommodityID) AND F_Type = 0) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '�Ĳ�����ƷID����ȷ�����װ��Ʒ�Ĳ�����ƷIDΪ���ڵĵ�Ʒ');
				ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_RefCommodityMultiple > 1 AND F_ID = iCommodityID) THEN
					SET done := TRUE;
					SET iErrorCode := 7;
					SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '�Ĳ�����Ʒ��������ȷ�����װ��Ʒ�Ĳ�����Ʒ��������1');
				END IF;
			ELSE 
				SET done := TRUE;
				SET iErrorCode := 7;
				SET sErrorMsg := CONCAT('��Ʒ', iCommodityID, '����Ʒ����Type����ȷ����ֻ��Ϊ0��1��2��3'); 
			END IF;
		END LOOP read_loop;
		CLOSE list;
	
	COMMIT;
END;