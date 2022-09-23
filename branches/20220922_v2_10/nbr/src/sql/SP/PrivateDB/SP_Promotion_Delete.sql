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
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
--		SELECT Func_CheckPromotionDependency(iID, sErrorMsg) INTO iCheckDependency; -- ����䶯,����ʱ��ֹ�����,������������
   		SELECT F_Status INTO iStatus FROM t_promotion WHERE F_ID = iID;
   		
--   		IF iCheckDependency <> '' THEN
--			SET iErrorCode := 7;
--			SET sErrorMsg := iCheckDependency;
--		ELSE
		IF Func_ValidateStateChange(11, iStatus, 1) <> 1 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '�ô�����ɾ���������ظ�ɾ��';
		ELSE
--			DELETE FROM t_promotionscope WHERE F_PromotionID = iID;		-- ����ɾ���ӱ���Ϣ
		  	UPDATE t_promotion SET F_Status = 1 WHERE F_ID = iID;
		  	DELETE FROM t_promotionsynccachedispatcher WHERE F_SyncCacheID IN (SELECT F_ID FROM t_promotionsynccache WHERE F_SyncData_ID = iID);
			DELETE FROM t_promotionsynccache WHERE F_SyncData_ID = iID;

 			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	COMMIT;
END;