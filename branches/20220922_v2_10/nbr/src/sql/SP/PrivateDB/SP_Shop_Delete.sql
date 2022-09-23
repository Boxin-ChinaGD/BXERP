-- ɾ��һ���ŵ��һ����˾�µ������ŵ�
DROP PROCEDURE IF EXISTS `SP_Shop_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Shop_Delete`(
	OUT iErrorCode INT,  
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT, -- �����ߴ���-1��>0����������ῼ��<=0����� 
	IN iCompanyID INT -- �����ߴ���>0����������ῼ��<=0����� 
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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	SET sErrorMsg := ''; -- ���ں���Ĵ�����Ϣ������Ҫƴ�ӣ�������ǰ�����ó�ʼֵ

	IF NOT EXISTS (SELECT 1 from nbr_bx.T_Company WHERE F_ID = iCompanyID) THEN
	   SET iErrorCode := 7;
	   SET sErrorMsg := '�ù�˾������';
   	ELSEIF iShopID <= 0 THEN -- ɾ����˾�µ������ŵ���Ϣ
	   --	UPDATE t_shop SET F_Status = 3 WHERE F_CompanyID = iCompanyID;
	   	
	   	OPEN shopList;  
	    shopLoop: LOOP	  
	   		FETCH shopList INTO iID, iStatus;   
	   		IF done THEN  
	        	LEAVE shopLoop;   
	    	END IF; 
	    	
	    	IF Func_ValidateStateChange(3, iStatus, 3) <> 1 THEN
	   		    SET sErrorMsg := concat(sErrorMsg, 'ShopID=', iID,',ɾ��ʧ��,״̬Ϊ', iStatus, '����ɾ�� <br />');
	   		ELSE
	   			UPDATE t_shop SET F_Status = 3 WHERE F_ID = iID;
	    	
		    	-- ���¶�Ӧ��staff
		        UPDATE t_staff SET F_Status = 1 WHERE F_ShopID = iID AND F_Status = 0 AND Func_ValidateStateChange(7, 0, 1) = 1;
		        IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, 'ɾ��Ա���������� <br />');
		        END IF;
		        -- ... ����֧�ֶ��ʱ�Ŵ���ͬ����
		        -- ... ��Ҫ���staff������ѯ
		        
				-- ���¶�Ӧ��pos
				UPDATE t_pos SET F_Status = 1 WHERE F_ShopID = iID AND F_Status = 0 AND Func_ValidateStateChange(8, 0, 1) = 1;
				IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, 'ɾ��POS���������� <br />');
		        END IF;
				-- ... ����֧�ֶ��ʱ�Ŵ���ͬ����
				-- ... ��Ҫ���pos������ѯ
				
			END IF;
			
	 	END LOOP shopLoop;
		CLOSE shopList;
		
	   	SET iErrorCode := 0;
		SET sErrorMsg := '';
	ELSE -- ɾ����˾�µľ����ĸ��ŵ����Ϣ
		SELECT F_Status INTO iStatus FROM t_shop WHERE F_ID = iShopID AND F_CompanyID = iCompanyID;
		IF Func_ValidateStateChange(3, iStatus, 3) <> 1 THEN
	    	SET sErrorMsg := concat('����ɾ�����ŵ꣬���ŵ�״̬Ϊ', iStatus);
	    	SET iErrorCode := 7;
	    ELSEIF iShopID = 1 THEN
	    	SET sErrorMsg := 'Ĭ���ŵ겻�ܶ���';
	    	SET iErrorCode := 7;
	   	ELSE
		    UPDATE t_shop SET F_Status = 3 WHERE F_ID = iShopID AND F_CompanyID = iCompanyID;
			-- ����pos��staff��shop�����״̬
			UPDATE t_staff SET F_Status = 1 WHERE F_ShopID = iShopID AND F_Status = 0 AND Func_ValidateStateChange(7, 0, 1) = 1;
			IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, 'ɾ��Ա���������� <br />');
		    END IF;
			-- ... ����֧�ֶ��ʱ�Ŵ���ͬ����
	
		    UPDATE t_pos SET F_Status = 1 WHERE F_ShopID = iShopID AND F_Status = 0 AND Func_ValidateStateChange(8, 0, 1) = 1;
		    IF row_count() = 0 THEN
		           SET sErrorMsg := concat(sErrorMsg, 'ɾ��POS���������� <br />');
		    END IF;
			-- ... ����֧�ֶ��ʱ�Ŵ���ͬ����
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
	
			
			-- �������иո�����״̬��pos��staff
			SELECT F_ID FROM t_pos WHERE F_ShopID = iShopID AND F_Status = 1;
			SELECT F_ID FROM t_staff WHERE F_ShopID = iShopID AND F_Status = 1;
			
			SET iErrorCode := 0;
		END IF;
--			
--		-- ����pos��D��ͬ���黺��
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
--	    	-- ��ն�Ӧ�Ļ���
--	    	DELETE FROM t_possynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_possynccache WHERE F_SyncData_ID = iPosID);
--	    	DELETE FROM t_possynccache WHERE F_SyncData_ID = iPosID;
--	    	
--	  		SET iSyncSequence = iSyncSequence + 1;
--	  		SELECT iSyncSequence;
--			-- ����һ��D�͵�ͬ���黺��.
--			INSERT INTO t_possynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime) VALUES (iPosID, 'D', iSyncSequence, now());
--			
--	 	END LOOP posLoop;
--		CLOSE posList;
--		
--		SET done := false;
--	
--		-- ����staff��D��ͬ���黺��
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
--			-- ����һ��D�͵�ͬ���黺��.
--			INSERT INTO t_staffsynccache (F_SyncData_ID, F_SyncType, F_SyncSequence, F_CreateDatetime) VALUES (iStaffID, 'D', iSyncSequence, now());
--			
--	 	END LOOP staffLoop;
--		CLOSE staffList;	
	END IF;

END;