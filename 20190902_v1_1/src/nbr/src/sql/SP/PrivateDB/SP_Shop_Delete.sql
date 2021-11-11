-- 删除一间门店或一个公司下的所有门店
DROP PROCEDURE IF EXISTS `SP_Shop_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Shop_Delete`(
	OUT iErrorCode INT,  
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT, -- 调用者传递-1或>0，但本程序会考虑<=0的情况 
	IN iCompanyID INT -- 调用者传递>0，但本程序会考虑<=0的情况 
	)
BEGIN
--	DECLARE iSyncSequence INT;
--	DECLARE iPosID INT;
--	DECLARE iStaffID INT;
	DECLARE isDeleteID VARCHAR(32);
	DECLARE iStatus INT;
	DECLARE iID INT;
	DECLARE maxSyncSequence INT;
	DECLARE done INT DEFAULT false;
	DECLARE posList CURSOR FOR (SELECT F_ID AS iPosID FROM t_pos WHERE F_ShopID =iShopID); 
--	DECLARE staffList CURSOR FOR (SELECT F_ID FROM t_staff WHERE F_ShopID = iShopID);
	DECLARE shopList CURSOR FOR (SELECT F_ID, F_Status FROM t_shop WHERE F_CompanyID = iCompanyID);
    
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;   
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	SET sErrorMsg := ''; -- 由于后面的错误信息可能需要拼接，所以在前面设置初始值

	IF NOT EXISTS (SELECT 1 from nbr_bx.T_Company WHERE F_ID = iCompanyID) THEN
	   SET iErrorCode := 7;
	   SET sErrorMsg := '该公司不存在';
   	ELSEIF iShopID <= 0 THEN -- 删除公司下的所有门店信息
	   --	UPDATE t_shop SET F_Status = 3 WHERE F_CompanyID = iCompanyID;
	   	
	   	OPEN shopList;  
	    shopLoop: LOOP	  
	   		FETCH shopList INTO iID, iStatus;   
	   		IF done THEN  
	        	LEAVE shopLoop;   
	    	END IF; 
	    	
	    	IF Func_ValidateStateChange(3, iStatus, 3) <> 1 THEN
	   		    SET sErrorMsg := concat(sErrorMsg, 'ShopID=', iID,',删除失败,状态为', iStatus, '不能删除 <br />');
	   		ELSE
	   			UPDATE t_shop SET F_Status = 3 WHERE F_ID = iID;
	    	
		    	-- 更新对应的staff
		        UPDATE t_staff SET F_Status = 1 WHERE F_ShopID = iID AND F_Status = 0 AND Func_ValidateStateChange(7, 0, 1) = 1;
		        IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, '删除员工发生错误 <br />');
		        END IF;
		        -- ... 将来支持多店时才处理同步块
		        -- ... 需要添加staff依赖查询
		        
				-- 更新对应的pos
				UPDATE t_pos SET F_Status = 1 WHERE F_ShopID = iID AND F_Status = 0 AND Func_ValidateStateChange(8, 0, 1) = 1;
				IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, '删除POS机发生错误 <br />');
		        END IF;
				-- ... 将来支持多店时才处理同步块
				-- ... 需要添加pos依赖查询
				
			END IF;
			
	 	END LOOP shopLoop;
		CLOSE shopList;
		
	   	SET iErrorCode := 0;
		SET sErrorMsg := '';
	ELSE -- 删除公司下的具体哪个门店的信息
		SELECT F_Status INTO iStatus FROM t_shop WHERE F_ID = iShopID AND F_CompanyID = iCompanyID;
		IF Func_ValidateStateChange(3, iStatus, 3) <> 1 THEN
	    	SET sErrorMsg := concat('不能删除该门店，该门店状态为', iStatus);
	    	SET iErrorCode := 7;
	    ELSEIF iShopID = 1 THEN
	    	SET sErrorMsg := '默认门店不能冻结';
	    	SET iErrorCode := 7;
	   	ELSE
		    UPDATE t_shop SET F_Status = 3 WHERE F_ID = iShopID AND F_CompanyID = iCompanyID;
			-- 设置pos，staff，shop的相关状态
			UPDATE t_staff SET F_Status = 1 WHERE F_ShopID = iShopID AND F_Status = 0 AND Func_ValidateStateChange(7, 0, 1) = 1;
			IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, '删除员工发生错误 <br />');
		    END IF;
			-- ... 将来支持多店时才处理同步块
	
		    UPDATE t_pos SET F_Status = 1 WHERE F_ShopID = iShopID AND F_Status = 0 AND Func_ValidateStateChange(8, 0, 1) = 1;
		    IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, '删除POS机发生错误 <br />');
		    END IF;
			-- ... 将来支持多店时才处理同步块
			OPEN posList;  
		    posLoop: LOOP	  
		   		FETCH posList INTO iID;   
		   		IF done THEN  
		        	LEAVE posLoop;   
		    	END IF; 
		    		SET maxSyncSequence = (SELECT Max(F_SyncSequence) FROM t_possynccache);
		    		IF maxSyncSequence IS NOT NULL THEN
				  		SET maxSyncSequence = maxSyncSequence + 1;
				  	ELSE 
				  		SET maxSyncSequence = 1;
				  	END IF;
					INSERT INTO t_possynccache(F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime)
					VALUES(iID, 'D', maxSyncSequence, now());
				
		 	END LOOP posLoop;
			CLOSE posList;
	
			
			-- 返回所有刚刚设置状态的pos，staff
			SELECT F_ID FROM t_pos WHERE F_ShopID = iShopID AND F_Status = 1;
			SELECT F_ID FROM t_staff WHERE F_ShopID = iShopID AND F_Status = 1;
			
			SET iErrorCode := 0;
		END IF;
--			
--		-- 增加pos的D型同步块缓存
--		SELECT max(F_SyncSequence) INTO iSyncSequence from t_possynccache;
--		
--		SET iSyncSequence = if(iSyncSequence IS NULL, 0, iSyncSequence);
--	  		
--		OPEN posList;  
--	    posLoop: LOOP	  
--	   		FETCH posList INTO iPosID;   
--	   		IF done THEN  
--	        	LEAVE posLoop;   
--	    	END IF; 
--	    	
--	    	-- 清空对应的缓存
--	    	DELETE FROM t_possynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_possynccache WHERE F_SyncData_ID = iPosID);
--	    	DELETE FROM t_possynccache WHERE F_SyncData_ID = iPosID;
--	    	
--	  		SET iSyncSequence = iSyncSequence + 1;
--	  		SELECT iSyncSequence;
--			-- 创建一个D型的同步块缓存.
--			INSERT INTO t_possynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime) VALUES (iPosID, 'D', iSyncSequence, now());
--			
--	 	END LOOP posLoop;
--		CLOSE posList;
--		
--		SET done := false;
--	
--		-- 增加staff的D型同步块缓存
--		SELECT max(F_SyncSequence) INTO iSyncSequence from t_staffsynccache;
--		SET iSyncSequence = if(iSyncSequence IS NULL, 0, iSyncSequence);
--	  	
--		OPEN staffList;  
--	    staffLoop: LOOP	  
--	   		FETCH staffList INTO iStaffID;   
--	   		IF done THEN 
--	        	LEAVE staffLoop;   
--	    	END IF; 
--		 
--	    	DELETE FROM t_staffsynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_staffsynccache WHERE F_SyncData_ID = iStaffID);
--	    	DELETE FROM t_staffsynccache WHERE F_SyncData_ID = iStaffID;
--	  	   
--	  		SET iSyncSequence = iSyncSequence + 1;
--	  		
--			-- 创建一个D型的同步块缓存.
--			INSERT INTO t_staffsynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime) VALUES (iStaffID, 'D', iSyncSequence, now());
--			
--	 	END LOOP staffLoop;
--		CLOSE staffList;	
	END IF;

END;