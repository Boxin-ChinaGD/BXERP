DROP PROCEDURE IF EXISTS `SP_WarehousingCommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_WarehousingCommodity_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iWarehousingID INT,
	IN iCommodityID INT,
	IN iNO INT,
	IN iBarcodeID INT,
	IN iPrice Decimal(20,6),
	IN iAmount Decimal(20,6),
	IN iShelfLife INT
)
BEGIN
	DECLARE iType INT;
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT F_Type, F_Status INTO iType, iStatus FROM t_commodity WHERE F_ID = iCommodityID;	
		-- �����ƷҪ�����������1����Ʒ���ͱ����ǵ�Ʒ����װ��Ʒ��2����Ʒ״̬�����Ǳ�ɾ������Ʒ��
		IF EXISTS (SELECT 1 FROM t_warehousingcommodity WHERE F_WarehousingID = iWarehousingID AND F_CommodityID = iCommodityID) THEN 
			SET iErrorCode := 1;
			SET sErrorMsg := 'һ����ⵥ�²�������ظ���Ʒ';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_ID = iBarcodeID) THEN 
		   	SET iErrorCode = 2;
			SET sErrorMsg := '��ⵥ��Ʒ�������벻����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_warehousing WHERE F_ID = iWarehousingID) THEN
			SET iErrorCode = 2;
			SET sErrorMsg := '����ⵥ������';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode = 2;
			SET sErrorMsg := '�������һ�������ڵ���Ʒ';
		ELSEIF iStatus = 2 THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '���������ɾ������Ʒ����ⵥ';
		ELSEIF iType <> 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '������ӷǵ�Ʒ����Ʒ����ⵥ';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_barcodes WHERE F_CommodityID = iCommodityID AND F_ID = iBarcodeID) THEN
		   		SET iErrorCode := 7;
		   		SET sErrorMsg := '������ID����Ʒʵ��������ID����Ӧ';
		ELSE   
		   	INSERT INTO t_warehousingcommodity (
				F_WarehousingID, 
				F_CommodityID, 
				F_NO, 
				F_PackageUnitID,
				F_CommodityName,
				F_BarcodeID,
				F_Price, 
				F_Amount, 
				F_ProductionDatetime, 
				F_ShelfLife, 
				F_ExpireDatetime,
				F_NOSalable
			) VALUES (
				iWarehousingID, 
				iCommodityID, 
				iNO, 
				(SELECT F_PackageUnitID FROM t_commodity WHERE F_ID = iCommodityID),
				(SELECT F_Name FROM t_commodity WHERE F_ID = iCommodityID),
				iBarcodeID, 
				iPrice, 
				iAmount, 
				now(),
				iShelfLife, 
				now(),
				iNO
			);
	
		SELECT 
			F_ID, 
			F_WarehousingID, 
			F_CommodityID, 
			F_NO, 
			F_PackageUnitID,
			F_CommodityName,
			F_BarcodeID,
			F_Price, 
			F_Amount, 
			F_ProductionDatetime, 
			F_ShelfLife, 
			F_ExpireDatetime,
			F_CreateDatetime,
			F_UpdateDatetime,
			F_NOSalable
		FROM t_warehousingcommodity WHERE F_ID = last_insert_id();
		
		   SET iErrorCode := 0;
		   SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;