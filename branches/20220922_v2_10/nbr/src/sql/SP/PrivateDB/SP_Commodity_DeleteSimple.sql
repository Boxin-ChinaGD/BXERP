DROP PROCEDURE IF EXISTS `SP_Commodity_DeleteSimple`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_DeleteSimple`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStaffID INT
	)
BEGIN
	DECLARE iCommodityID INT;
 	DECLARE sCsheckDependency VARCHAR(32);
 	DECLARE iStatus INT;
 	DECLARE iFuncReturnCode INT;
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
	
		SELECT Func_CheckCommodityDependency(iID, sErrorMsg) INTO sCsheckDependency;
		SELECT F_Status, F_PackageUnitID INTO iStatus, oldPackageUnitID FROM t_commodity WHERE F_ID = iID;
		
		-- �ж��Ƿ��ǵ�Ʒ
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_TYPE <> 0) THEN
		    SET iErrorCode := 7;
		    SET sErrorMsg := '���ǵ�Ʒ��ɾ��ʧ��';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID) THEN
			SET sErrorMsg := '��Ʒ������';
			SET iErrorCode := 2;
		ELSEIF Func_ValidateStateChange(10, iStatus, 2) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����Ʒ��ɾ���������ظ�ɾ��';
		-- �ж�ɾ���ĵ�Ʒ�Ͷ��װ��Ʒ�Ƿ��й�����ȼ�¼��
		ELSEIF sCsheckDependency = '' THEN
			CALL SP_Barcodes_DeleteBySimpleCommodityID(iErrorCode, sErrorMsg, iID, iStaffID); -- ɾ����Ʒ�Ͷ�Ӧ�Ķ��װ��Ʒ��������,ͬʱ������������մ��ͬ��
			IF iErrorCode = 0 THEN 
				-- ɾ�����װ��Ʒ
--				UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_Status != 2 AND F_RefCommodityID = iID;
				-- ɾ����Ʒ
				UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_ID = iID;
				-- ��װ��λ������ʷ��¼	
				SELECT Func_CreateCommodityHistory(iID, '$', '$', '$', oldPackageUnitID, -1, -100000000, -100000000, iStaffID, '$','') INTO iFuncReturnCode;
				-- ��Ӧ��
				DELETE FROM t_providercommodity WHERE F_CommodityID = iID;
		   		-- ɾ����Ʒͬ���黺��
		   		DELETE FROM t_commoditysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_commoditysynccache WHERE F_SyncData_ID = iID);
	   			DELETE FROM t_commoditysynccache WHERE F_SyncData_ID = iID;
			ELSEIF iErrorCode = 3 THEN
				SET sErrorMsg := 'ɾ����ص�������ʱ�������ݿ��쳣';
				ROLLBACK;
			END IF ;
		ELSE	-- ��Ʒ����װ����Ӧ��ʹ�ü�¼
			SET iErrorCode := 7;
			SET sErrorMsg := sCsheckDependency;
		END IF;
	
	COMMIT;
END;