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
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	SET iErrorCode = 0; 
    SET sErrorMsg := '';
	
	START TRANSACTION;
	
		SELECT Func_CheckCommodityDependency(iID, sErrorMsg) INTO sCsheckDependency;
		SELECT F_Status, F_PackageUnitID INTO iStatus, oldPackageUnitID FROM t_commodity WHERE F_ID = iID;
		
		-- 判断是否是单品
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_TYPE <> 0) THEN
		    SET iErrorCode := 7;
		    SET sErrorMsg := '不是单品，删除失败';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID) THEN
			SET sErrorMsg := '商品不存在';
			SET iErrorCode := 2;
		ELSEIF Func_ValidateStateChange(10, iStatus, 2) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该商品已删除，不能重复删除';
		-- 判断删除的单品和多包装商品是否有过销存等记录。
		ELSEIF sCsheckDependency = '' THEN
			CALL SP_Barcodes_DeleteBySimpleCommodityID(iErrorCode, sErrorMsg, iID, iStaffID); -- 删除单品和对应的多包装商品的条形码,同时更新条形码的普存和同存
			IF iErrorCode = 0 THEN 
				-- 删除多包装商品
--				UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_Status != 2 AND F_RefCommodityID = iID;
				-- 删除单品
				UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_ID = iID;
				-- 包装单位新增历史记录	
				SELECT Func_CreateCommodityHistory(iID, '$', '$', '$', oldPackageUnitID, -1, -100000000, -100000000, iStaffID, '$','') INTO iFuncReturnCode;
				-- 供应商
				DELETE FROM t_providercommodity WHERE F_CommodityID = iID;
		   		-- 删除单品同步块缓存
		   		DELETE FROM t_commoditysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_commoditysynccache WHERE F_SyncData_ID = iID);
	   			DELETE FROM t_commoditysynccache WHERE F_SyncData_ID = iID;
			ELSEIF iErrorCode = 3 THEN
				SET sErrorMsg := '删除相关的条形码时出现数据库异常';
				ROLLBACK;
			END IF ;
		ELSE	-- 单品或多包装有相应的使用记录
			SET iErrorCode := 7;
			SET sErrorMsg := sCsheckDependency;
		END IF;
	
	COMMIT;
END;