DROP PROCEDURE IF EXISTS `SP_Commodity_UpdateWarehousing`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_UpdateWarehousing` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN iNO INT,
	IN iPrice Decimal(20,6),
	IN iStaffID INT,
	IN iShopID INT 
	)
BEGIN
	DECLARE iNum INT; 		-- ��ǰ�ۼ�����
--	DECLARE iCost Decimal(20,6);	-- ��ǰ��ƽ��������  
	DECLARE iCommodityMultiple INT; -- ��Ʒ����
	DECLARE iRefID INT; -- ������ƷID
	DECLARE iFuncReturnCode INT; 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		
		
		SELECT 
	--		F_NOAccumulated, 
	--		F_PricePurchase, 
			F_RefCommodityMultiple, 
			F_RefCommodityID 
		INTO 
	--		iNum, 
	--		iCost, 
			iCommodityMultiple, 
			iRefID 
		FROM t_commodity WHERE F_ID = iCommodityID;
		
	--	SET @total = iNum + iNO * cm;											-- ��˳ɹ�����Ʒ���ۼ�����
	--	SET @imean = (iPrice * iNO * cm + iCost * iNum)/@total; 					-- ��˳ɹ�����Ʒ��ƽ��������
		
   		UPDATE t_commodityshopinfo SET 
--			F_PricePurchase = @imean,
			F_LatestPricePurchase = iPrice, 
			-- F_NOAccumulated = @total,
			F_NO = F_NO + iNO	
   		WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID;
   		
   		UPDATE t_commodity SET F_UpdateDatetime = now()	WHERE F_ID = iCommodityID;
   		
   		-- ������Ʒ�޸���ʷ��
		select Func_CreateCommodityHistory(iCommodityID, '$', '$', '$', -1, -1, -100000000, (SELECT F_NO FROM t_commodityshopinfo WHERE F_CommodityID = iCommodityID AND F_ShopID = iShopID) - iNO, iStaffID, '$', iShopID) INTO iFuncReturnCode;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;