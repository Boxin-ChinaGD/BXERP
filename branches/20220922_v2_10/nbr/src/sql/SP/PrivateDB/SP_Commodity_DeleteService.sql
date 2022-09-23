DROP PROCEDURE IF EXISTS `SP_Commodity_DeleteService`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_DeleteService`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStaffID INT
	)
BEGIN
	DECLARE iCommodityID INT;
	DECLARE oldPackageUnitID INT;
	DECLARE sCsheckDependency VARCHAR(32);
	DECLARE iFuncReturnCode INT; -- ���պ����ķ���ֵ�����䲻Ҫ���ؽ�������ϲ�������ŵ������Ľ��������
 	DECLARE iStatus INT;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode = 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
    SET iErrorCode = 0; 
    SET sErrorMsg := '';
	    
  	START TRANSACTION;
  		SELECT Func_CheckCommodityDependency(iID, iErrorCode) INTO sCsheckDependency;
		SELECT F_Status,F_PackageUnitID INTO iStatus,oldPackageUnitID FROM t_commodity WHERE F_ID = iID;
		-- �ж��Ƿ��Ƕ��װ��Ʒ
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_TYPE <> 3) THEN
		    SET iErrorCode := 7;
		    SET sErrorMsg := '�Ƿ�����Ʒ';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID) THEN
			SET sErrorMsg := '��Ʒ������';
			SET iErrorCode := 2;
		ELSEIF Func_ValidateStateChange(10, iStatus, 2) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����Ʒ��ɾ���������ظ�ɾ��';
		-- �ж�ɾ���Ķ��װ��Ʒ�Ƿ��й�����ȼ�¼��
		ELSEIF sCsheckDependency = '' THEN
			CALL SP_Barcodes_DeleteByServiceCommodityID(iErrorCode, sErrorMsg, iID, iStaffID); -- ɾ�����װ��Ʒ��������,ͬʱ������������մ��ͬ��
			IF iErrorCode = 0 THEN 
				-- ɾ���Լ�
				UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_ID = iID;
				-- ��װ��λ������ʷ��¼	
	 			SELECT Func_CreateCommodityHistory(iID, '$', '$', '$', oldPackageUnitID, -1, -100000000, -100000000, iStaffID, '$','') INTO iFuncReturnCode;
		   		-- ɾ�����ͬ���黺��
		   		DELETE FROM t_commoditysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_commoditysynccache WHERE F_SyncData_ID = iID);
	   			DELETE FROM t_commoditysynccache WHERE F_SyncData_ID = iID;
			ELSEIF iErrorCode = 3 THEN
				SET sErrorMsg := 'ɾ����ص�������ʱ�������ݿ��쳣';
  				ROLLBACK;	
			END IF;
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := sCsheckDependency;
		END IF;
		
 	COMMIT;
END;