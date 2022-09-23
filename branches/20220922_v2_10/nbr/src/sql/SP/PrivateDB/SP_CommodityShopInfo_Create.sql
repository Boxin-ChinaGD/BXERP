DROP PROCEDURE IF EXISTS `SP_CommodityShopInfo_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_CommodityShopInfo_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN iShopID INT,
	IN dLatestPricePurchase Decimal(20,6),
	IN dPriceRetail Decimal(20,6),  
	IN iNOStart INT,
	IN dPurchasingPriceStart Decimal(20,6),
	IN iStaffID INT,
	IN warehousingID INT -- ������ڳ���Ʒ����Ҫ����ֵ���ID
)
BEGIN
	DECLARE iFuncReturnCode INT; -- ���պ����ķ���ֵ�����䲻Ҫ���ؽ�������ϲ�������ŵ������Ľ��������
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		IF EXISTS(SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '����Ʒ�ŵ���Ϣ�Ѵ���';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ʹ�ò����ڵ���Ʒ������Ʒ�ŵ���Ϣ';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����ʹ�ò����ڵ��ŵ괴����Ʒ�ŵ���Ϣ';
		ELSE
			INSERT INTO t_commodityshopinfo (F_CommodityID, F_ShopID, F_LatestPricePurchase, F_PriceRetail, F_NO, F_NOStart, F_PurchasingPriceStart) 
			VALUES (iCommodityID, iShopID, dLatestPricePurchase, dPriceRetail, 0, iNOStart, dPurchasingPriceStart);
			SET @ID = last_insert_id();
			
			
			-- ������Ʒ�޸���ʷ��
			IF NOT EXISTS (SELECT 1 FROM t_commodityhistory WHERE F_CommodityID = iCommodityID) THEN	-- ������Ʒ����һ����Ʒ�޸���ʷ�Ϳ�����
				SET @sBarcode = '';
				SELECT F_Barcode INTO @sBarcode FROM t_barcodes WHERE F_CommodityID = iCommodityID LIMIT 0,1;
				SELECT Func_CreateCommodityHistory(
				iCommodityID, 
				'$', 
				@sBarcode,
				'$', 
				0, 
				-1, 
				-100000000, 
				-100000000, 
				iStaffID,
				'',
				iShopID) INTO iFuncReturnCode; -- 0
		  	END IF;	 
			
			-- �ж��Ƿ񴴽��ڳ���Ʒ��ⵥ
			IF iNOStart >= 0 AND dPurchasingPriceStart >= 0 THEN
				
				UPDATE t_commodityshopinfo SET 
					F_NO = iNOStart, 
					F_CurrentWarehousingID = warehousingID,
					F_LatestPricePurchase = dPurchasingPriceStart
				WHERE F_ID = @ID;
				
				-- ������Ʒ�޸���ʷ��
				SELECT Func_CreateCommodityHistory(
				iCommodityID, 
				'$', 
				'$', 
				'$', 
				-1, 
				-1, 
				-100000000, 
				0,
				iStaffID, 
				'$',
				iShopID) INTO iFuncReturnCode; -- 0
				
			END IF;
			
			SELECT 
				F_ID, 
				F_CommodityID, 
				F_ShopID,
				F_LatestPricePurchase,
				F_PriceRetail,
				F_NO,
				F_NOStart,
				F_PurchasingPriceStart,
				F_CurrentWarehousingID
				FROM t_commodityshopinfo WHERE F_ID = @ID;
			
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;