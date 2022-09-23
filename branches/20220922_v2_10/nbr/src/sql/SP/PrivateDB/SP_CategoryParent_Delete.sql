DROP PROCEDURE IF EXISTS `SP_CategoryParent_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CategoryParent_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE iCategoryID INT;
	DECLARE iValid_Pos INT;
	DECLARE done INT DEFAULT false;
-- 
	DECLARE list CURSOR FOR (
		SELECT F_ID AS iCategoryID 
		FROM t_category 
		WHERE F_ParentID = iID
	);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
   START TRANSACTION;
			
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_CategoryID IN (SELECT F_ID FROM t_category WHERE  F_ParentID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := 'ɾ���Ĵ��൱�е�С���й�����Ʒ';
		ELSE 
--          ������ʹ���α꣬�ҳ�������ص�С�࣬����D��ͬ���飬Ȼ��ɾ��С�ࡣ��������������ܽϸ��ӡ�			
			OPEN list;
				read_loop: LOOP
				FETCH list INTO iCategoryID;
				IF done THEN
					LEAVE read_loop;
				END IF;
				
				-- ɾ�����С��
				DELETE FROM t_category WHERE F_ID = iCategoryID;
				-- ɾ�����С���ͬ����
				DELETE FROM t_categorysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_categorysynccache WHERE F_SyncData_ID = iCategoryID);
				DELETE FROM t_categorysynccache WHERE F_SyncData_ID = iCategoryID;	
				-- �ж��Ƿ���һ̨������Ч��POS��
				SELECT COUNT(1) INTO iValid_Pos FROM t_pos WHERE F_Status = 0;
				IF iValid_Pos > 1 THEN 
					-- �������С���D��ͬ����
					-- POS��ͬ��ʱ��ͬ��D��ͬ���������ִ�е�(ͬ��˳���ֶ��������ظ���)
					-- ���Դ���С���D��ͬ����ʱ����ͬ��˳���ֶ�F_SyncSequenceд��Ϊ10000
					INSERT INTO t_categorysynccache (F_SyncData_ID, F_SyncSequence, F_SyncType) VALUES (iCategoryID, 10000, 'D');
				END IF;
				
				END LOOP read_loop;
			CLOSE list;
	
			DELETE FROM t_categoryparent WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
			
	COMMIT;
END;