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
	DECLARE iFuncReturnCode INT; -- 接收函数的返回值，令其不要返回结果集给上层以免干扰到正常的结果集次序
 	DECLARE iStatus INT;
	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode = 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
    SET iErrorCode = 0; 
    SET sErrorMsg := '';
	    
  	START TRANSACTION;
  		SELECT Func_CheckCommodityDependency(iID, iErrorCode) INTO sCsheckDependency;
		SELECT F_Status,F_PackageUnitID INTO iStatus,oldPackageUnitID FROM t_commodity WHERE F_ID = iID;
		-- 判断是否是多包装商品
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_TYPE <> 3) THEN
		    SET iErrorCode := 7;
		    SET sErrorMsg := '非服务商品';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID) THEN
			SET sErrorMsg := '商品不存在';
			SET iErrorCode := 2;
		ELSEIF Func_ValidateStateChange(10, iStatus, 2) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该商品已删除，不能重复删除';
		-- 判断删除的多包装商品是否有过销存等记录。
		ELSEIF sCsheckDependency = '' THEN
			CALL SP_Barcodes_DeleteByServiceCommodityID(iErrorCode, sErrorMsg, iID, iStaffID); -- 删除多包装商品的条形码,同时更新条形码的普存和同存
			IF iErrorCode = 0 THEN 
				-- 删除自己
				UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_ID = iID;
				-- 包装单位新增历史记录	
	 			SELECT Func_CreateCommodityHistory(iID, '$', '$', '$', oldPackageUnitID, -1, -100000000, -100000000, iStaffID, '$','') INTO iFuncReturnCode;
		   		-- 删除相关同步块缓存
		   		DELETE FROM t_commoditysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_commoditysynccache WHERE F_SyncData_ID = iID);
	   			DELETE FROM t_commoditysynccache WHERE F_SyncData_ID = iID;
			ELSEIF iErrorCode = 3 THEN
				SET sErrorMsg := '删除相关的条形码时出现数据库异常';
  				ROLLBACK;	
			END IF;
		ELSE
			SET iErrorCode := 7;
			SET sErrorMsg := sCsheckDependency;
		END IF;
		
 	COMMIT;
END;