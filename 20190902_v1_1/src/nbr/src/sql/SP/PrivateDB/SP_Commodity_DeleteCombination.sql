DROP PROCEDURE IF EXISTS `SP_Commodity_DeleteCombination`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Commodity_DeleteCombination`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStaffID INT
	)
BEGIN
 	DECLARE sCheckDependency VARCHAR(32);
 	DECLARE iFuncReturnCode INT;
 	DECLARE iStatus INT;
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
	
		SELECT Func_CheckCommodityDependency(iID, sErrorMsg) INTO sCheckDependency;
		SELECT F_Status, F_PackageUnitID INTO iStatus, oldPackageUnitID FROM t_commodity WHERE F_ID = iID;
			
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID AND F_TYPE <> 1) THEN
		    SET iErrorCode := 7;
		    SET sErrorMsg := '非组合商品';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iID) THEN
			SET sErrorMsg := '商品不存在';
			SET iErrorCode := 2;
		ELSEIF Func_ValidateStateChange(10, iStatus, 2) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该商品已删除，不能重复删除';
		-- 检查这个组合商品 是否被销售过（具体是哪些表？组合表会涉及到入库？采购计划表吗？)
		ELSEIF sCheckDependency <> '' THEN 
	      -- 如果其他表有记录返回错误码 7
	    	SET iErrorCode := 7;
	    	SET sErrorMsg := sCheckDependency;
	    ELSE 
	      -- 没有记录删除组合商品条形码,同时更新条形码的普存和同存（单品和多包装不能RefCommodityID 指向 一个组合商品吧）所以直接修改组合商品状态
	      -- 删除条形码
	        CALL SP_Barcodes_DeleteByCombinationCommodityID(iErrorCode, sErrorMsg, iID, iStaffID);
	        IF iErrorCode = 0 THEN 
	         -- 删除组合关系表的关系（t_subcommodity）真的删除？
	           -- DELETE FROM t_subcommodity WHERE F_CommodityID = iID;
	            
	         -- 删除条形码成功 将组合商品的状态设置为删除状态
	         	UPDATE t_commodity SET F_Status = 2, F_PackageUnitID = NULL, F_CategoryID = NULL, F_BrandID = null,F_UpdateDatetime = now() WHERE F_Status != 2 AND F_Id= iID;
				-- 包装单位新增历史记录	
				SELECT Func_CreateCommodityHistory(iID, '$', '$', '$', oldPackageUnitID, -1, -100000000, -100000000, iStaffID, '$', '') INTO iFuncReturnCode;
				-- 供应商
				DELETE FROM t_providercommodity WHERE F_CommodityID = iID;
	         -- 删除相关的同步缓存并创建相应的D型同步块（Java层会作处理）
	         	DELETE FROM t_commoditysynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_commoditysynccache WHERE F_SyncData_ID = iID);
	   			DELETE FROM t_commoditysynccache WHERE F_SyncData_ID = iID;
	     	ELSEIF iErrorCode = 3 THEN
		      	SET sErrorMsg := '删除相关的条形码时出现数据库异常';
				ROLLBACK;
	        END IF ;
	   END IF ;
	   
	COMMIT;
END;