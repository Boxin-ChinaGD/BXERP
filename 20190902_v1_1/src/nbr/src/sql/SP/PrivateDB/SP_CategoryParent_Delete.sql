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
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
   START TRANSACTION;
			
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_CategoryID IN (SELECT F_ID FROM t_category WHERE  F_ParentID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的大类当中的小类有关联商品';
		ELSE 
--          在这里使用游标，找出所有相关的小类，创建D型同步块，然后删除小类。这里测试用例可能较复杂。			
			OPEN list;
				read_loop: LOOP
				FETCH list INTO iCategoryID;
				IF done THEN
					LEAVE read_loop;
				END IF;
				
				-- 删除相关小类
				DELETE FROM t_category WHERE F_ID = iCategoryID;
				-- 删除相关小类的同步块
				DELETE FROM t_categorysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_categorysynccache WHERE F_SyncData_ID = iCategoryID);
				DELETE FROM t_categorysynccache WHERE F_SyncData_ID = iCategoryID;	
				-- 判断是否有一台以上有效的POS机
				SELECT COUNT(1) INTO iValid_Pos FROM t_pos WHERE F_Status = 0;
				IF iValid_Pos > 1 THEN 
					-- 创建相关小类的D型同步块
					-- POS机同步时，同步D型同步块是最后执行的(同步顺序字段是允许重复的)
					-- 所以创建小类的D型同步块时，把同步顺序字段F_SyncSequence写死为10000
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