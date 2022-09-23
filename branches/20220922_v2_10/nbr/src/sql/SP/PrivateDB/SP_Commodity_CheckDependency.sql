DROP PROCEDURE IF EXISTS `SP_Commodity_CheckDependency`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_CheckDependency`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN 
	DECLARE NO INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
--		SELECT F_NO INTO NO FROM t_commodity WHERE F_ID = iID;
	
		IF EXISTS(SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = iID AND F_NO > 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '��Ʒ������Ϊ0����ɾ��';
	  	ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID = iID) THEN-- ... �ɹ��ƻ���
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '��Ʒ��RetailtradeCommodity������';
	  	ELSEIF  EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID = iID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '��Ʒ��PurchasingOrderCommodity������';
	  	ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID = iID) THEN
	  		SET iErrorCode := 7;
	  		SET sErrorMsg := '��Ʒ��InventoryCommodity������';
	  	ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID = iID) THEN
	  		SET iErrorCode := 7; -- ...������÷���7
	  		SET sErrorMsg := '��Ʒ��WarehousingCommodity������';
	  	ELSE
	  		SET iErrorCode := 0;
	  		SET sErrorMsg := '';
	  	END IF;		
	  	
	  	-- ... ����һ����������ǿ��>0����<0ʱ��������Ȼ���ڡ�����7��
 	COMMIT;
END;