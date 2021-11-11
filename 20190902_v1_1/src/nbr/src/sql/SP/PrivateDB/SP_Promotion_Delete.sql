DROP PROCEDURE IF EXISTS `SP_Promotion_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Promotion_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
--	DECLARE iCheckDependency VARCHAR(32);
	DECLARE iStatus INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
--		SELECT Func_CheckPromotionDependency(iID, sErrorMsg) INTO iCheckDependency; -- 需求变动,可随时终止促销活动,不管有无依赖
   		SELECT F_Status INTO iStatus FROM t_promotion WHERE F_ID = iID;
   		
--   		IF iCheckDependency <> '' THEN
--			SET iErrorCode := 7;
--			SET sErrorMsg := iCheckDependency;
--		ELSE
		IF Func_ValidateStateChange(11, iStatus, 1) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '该促销已删除，不能重复删除';
		ELSE
--			DELETE FROM t_promotionscope WHERE F_PromotionID = iID;		-- 不能删除从表信息
		  	UPDATE t_promotion SET F_Status = 1 WHERE F_ID = iID;
		  	DELETE FROM t_promotionsynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_promotionsynccache WHERE F_SyncData_ID = iID);
			DELETE FROM t_promotionsynccache WHERE F_SyncData_ID = iID;

 			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	COMMIT;
END;