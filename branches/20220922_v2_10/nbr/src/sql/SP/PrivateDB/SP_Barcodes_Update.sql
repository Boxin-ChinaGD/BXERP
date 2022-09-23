DROP PROCEDURE IF EXISTS `SP_Barcodes_Update`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_Update`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iCommodityID INT,
	IN sBarcode VARCHAR(64),
	IN iStaffID INT 
)
BEGIN
	DECLARE commID INT;
	DECLARE oldBarcode VARCHAR(128);
	DECLARE newBarcode VARCHAR(128);
	DECLARE iFuncReturnCode INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	-- ��������
	START TRANSACTION;
	
	IF NOT EXISTS(SELECT 1 FROM t_barcodes WHERE F_ID = iID) THEN
		SET iErrorCode := 2;
		SET sErrorMsg := '��Ҫ���µ����벻����';
	ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN 
		SET iErrorCode := 7;
		SET sErrorMsg := '��Ʒ������';
	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_BarcodeID = iID) THEN
		SET iErrorCode := 7;
		SET sErrorMsg := '�޸ĵ������������۵���Ʒ��������';
	ELSEIF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_BarcodeID = iID) THEN
		SET iErrorCode := 7;
		SET sErrorMsg := '�޸ĵ��������ڲɹ�������Ʒ��������';
	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_BarcodeID = iID) THEN
		SET iErrorCode := 7;
		SET sErrorMsg := '�޸ĵ�����������ⵥ��Ʒ��������';
	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_BarcodeID = iID) THEN
		SET iErrorCode := 7;
		SET sErrorMsg := '�޸ĵ����������̵㵥��Ʒ��������';
	ELSEIF EXISTS(SELECT 1 FROM t_barcodes WHERE F_CommodityID = iCommodityID AND F_Barcode = sBarcode) THEN
		SET iErrorCode := 7;
		SET sErrorMsg := '��Ʒ�����Ѵ��ڣ��������޸�';
	ELSEIF EXISTS(SELECT F_PromotionID FROM t_promotionscope WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
		SET iErrorCode := 7;
		SET sErrorMsg := '�޸ĵ���������ָ��������Χ��������';
	ELSE
		
		SELECT group_concat(F_Barcode), F_CommodityID INTO oldBarcode, commID from t_barcodes WHERE F_CommodityID = iCommodityID; 
	   	
		DELETE FROM t_barcodes WHERE F_ID = iID;
		DELETE FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_barcodessynccache WHERE F_SyncData_ID = iID);
		DELETE FROM t_barcodessynccache WHERE F_SyncData_ID =  iID;
		
		INSERT INTO t_barcodes (F_CommodityID, F_Barcode, F_CreateDatetime, F_UpdateDatetime) VALUES (iCommodityID, sBarcode, now(), now());
		
		SELECT F_ID, F_CommodityID, F_Barcode , F_CreateDatetime, F_UpdateDatetime FROM t_barcodes WHERE F_ID = Last_insert_id();
		
		SELECT group_concat(F_Barcode) INTO newBarcode FROM t_barcodes WHERE F_CommodityID = iCommodityID;
			
		select Func_CreateCommodityHistory(
		commID, 
		'$', 
		newBarcode, 
		'$', 
		-1, 
		-1, 
		-100000000, 
		-100000000, 
		iStaffID,
		oldBarcode,
		''
		) INTO iFuncReturnCode; -- 0
		
		SET iErrorCode := 0;
		SET sErrorMsg := '';

	END IF;
	
	IF(iErrorCode <> 0) THEN
		ROLLBACK;
	ELSE
		COMMIT;
	END IF;
END;