DROP PROCEDURE IF EXISTS `SP_Barcodes_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Barcodes_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStaffID INT
)
BEGIN
	DECLARE commID INT;
	DECLARE oldBarcode VARCHAR(128);
	DECLARE iFuncReturnCode INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN 
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_barcodes WHERE F_ID = iID) THEN
			SET iErrorCode := 2;
			SET sErrorMsg := '需要删除的条码不存在';
		ELSEIF EXISTS(SELECT 1 FROM t_retailtradecommodity WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的条形码在零售商品中有依赖';
		ELSEIF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的条形码在采购订单商品中有依赖';
		ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的条形码在入库单商品中有依赖';
		ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的条形码在盘点单商品中有依赖';
		ELSEIF EXISTS(SELECT 1 FROM t_returncommoditysheetcommodity WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的条形码在退货单商品中有依赖';
		ELSEIF EXISTS(SELECT F_PromotionID FROM t_promotionscope WHERE F_CommodityID IN (SELECT F_CommodityID FROM t_barcodes WHERE F_ID = iID)) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '删除的条形码在指定促销范围中有依赖';
		ELSE
			
			SELECT F_Barcode, F_CommodityID INTO oldBarcode, commID from t_barcodes WHERE F_ID = iID; 
					
			DELETE FROM t_barcodes WHERE F_ID = iID;
			DELETE FROM t_barcodessynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_barcodessynccache WHERE F_SyncData_ID = iID);
			DELETE FROM t_barcodessynccache WHERE F_SyncData_ID =  iID;
		
			select Func_CreateCommodityHistory(
			commID, 
			'$', 
			'', 
			'$', 
			-1, 
			-1, 
			-100000000, 
			-100000000, 
			iStaffID,
			oldBarcode,
			'') INTO iFuncReturnCode; -- 0
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		  
		END IF;
	
	COMMIT;
END;