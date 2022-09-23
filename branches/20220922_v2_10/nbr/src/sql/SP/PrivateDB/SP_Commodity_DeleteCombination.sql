DROP PROCEDURE IF EXISTS `SP_Commodity_DeleteCombination`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_DeleteCombination`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStaffID INT
	)
BEGIN
 	DECLARE sCheckDependency VARCHAR(32);
 	DECLARE iFuncReturnCode INT;
 	DECLARE iStatus INT;
 	DECLARE oldPackageUnitID INT;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode = 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	SET iErrorCode = 0; 
	SET sErrorMsg := '';
	
	START TRANSACTION;
	
		SELECT Func_CheckCommodityDependency(iID, sErrorMsg) INTO sCheckDependency;
		SELECT F_Status, F_PackageUnitID INTO iStatus, oldPackageUnitID FROM t_commodity WHERE F_ID = iID;
			
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_TYPE <> 1) THEN
		    SET iErrorCode := 7;
		    SET sErrorMsg := '�������Ʒ';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID) THEN
			SET sErrorMsg := '��Ʒ������';
			SET iErrorCode := 2;
		ELSEIF Func_ValidateStateChange(10, iStatus, 2) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����Ʒ��ɾ���������ظ�ɾ��';
		-- �����������Ʒ �Ƿ����۹�����������Щ����ϱ���漰����⣿�ɹ��ƻ�����)
		ELSEIF sCheckDependency <> '' THEN 
	      -- ����������м�¼���ش����� 7
	    	SET iErrorCode := 7;
	    	SET sErrorMsg := sCheckDependency;
	    ELSE 
	      -- û�м�¼ɾ�������Ʒ������,ͬʱ������������մ��ͬ�棨��Ʒ�Ͷ��װ����RefCommodityID ָ�� һ�������Ʒ�ɣ�����ֱ���޸������Ʒ״̬
	      -- ɾ��������
	        CALL SP_Barcodes_DeleteByCombinationCommodityID(iErrorCode, sErrorMsg, iID, iStaffID);
	        IF iErrorCode = 0 THEN 
	         -- ɾ����Ϲ�ϵ��Ĺ�ϵ��t_subcommodity�����ɾ����
	           -- DELETE FROM t_subcommodity WHERE F_CommodityID = iID;
	            
	         -- ɾ��������ɹ� �������Ʒ��״̬����Ϊɾ��״̬
	         	UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_Status != 2 AND F_Id= iID;
				-- ��װ��λ������ʷ��¼	
				SELECT Func_CreateCommodityHistory(iID, '$', '$', '$', oldPackageUnitID, -1, -100000000, -100000000, iStaffID, '$', '') INTO iFuncReturnCode;
				-- ��Ӧ��
				DELETE FROM t_providercommodity WHERE F_CommodityID = iID;
	         -- ɾ����ص�ͬ�����沢������Ӧ��D��ͬ���飨Java���������
	         	DELETE FROM t_commoditysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_commoditysynccache WHERE F_SyncData_ID = iID);
	   			DELETE FROM t_commoditysynccache WHERE F_SyncData_ID = iID;
	     	ELSEIF iErrorCode = 3 THEN
		      	SET sErrorMsg := 'ɾ����ص�������ʱ�������ݿ��쳣';
				ROLLBACK;
	        END IF ;
	   END IF ;
	   
	COMMIT;
END;