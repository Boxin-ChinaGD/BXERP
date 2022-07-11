DROP PROCEDURE IF EXISTS `SP_PackageUnit_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_PackageUnit_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS(SELECT 1 FROM t_purchasingordercommodity WHERE F_PackageUnitID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该包装单位已被采购订单商品使用，不能删除';
		ELSEIF EXISTS(SELECT 1 FROM t_warehousingcommodity WHERE F_PackageUnitID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该包装单位已被入库单商品使用，不能删除';
		ELSEIF EXISTS(SELECT 1 FROM t_inventorycommodity WHERE F_PackageUnitID = iID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该包装单位已被盘点单商品使用，不能删除';
		ELSEIF EXISTS (SELECT 1 FROM t_commodity WHERE F_PackageUnitID = iID ) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该包装单位已被商品使用，不能删除';
		ELSE
			DELETE FROM t_packageunit WHERE F_ID = iID;
			SET iErrorCode := 0;
			SET sErrorMsg := '';
			
		END IF;
	
	COMMIT;
END;