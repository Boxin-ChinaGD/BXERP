DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheet_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheet_Create`(
 	OUT iErrorCode INT,
 	OUT sErrorMsg VARCHAR(64),
 	IN iStaffID INT,
	IN iProviderID INT,
	IN iShopID INT
	)
BEGIN
	DECLARE sSN VARCHAR(20);
	DECLARE oldNO INT;
	DECLARE iFuncReturnCode INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;

	START TRANSACTION;
	
		IF NOT EXISTS(SELECT 1 FROM t_staff WHERE F_ID = iStaffID) THEN
			SET iErrorCode := 7;
	  		SET sErrorMsg := '��ǰ�ʻ����������˻���';
	  	ELSEIF NOT EXISTS(SELECT 1 FROM t_provider WHERE F_ID = iProviderID) THEN
			SET iErrorCode := 7;
	  		SET sErrorMsg := '�ù�Ӧ�̲����ڣ�������ѡ��Ӧ��';
	  	ELSEIF NOT EXISTS(SELECT 1 FROM t_shop WHERE F_ID = iShopID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '���ŵ겻���ڣ�������ѡ���ŵ�';
		ELSE
			SELECT F_SN INTO sSN FROM t_returncommoditysheet ORDER BY F_ID DESC LIMIT 1;
			SELECT Func_GenerateSN('TH', sSN) INTO sSN;
		
			INSERT INTO t_returncommoditysheet (
				F_SN,
				F_StaffID, 
				F_ProviderID,
				F_ShopID
			)
			VALUES (
				sSN,
				iStaffID,
				iProviderID,
				iShopID
			 );	
			SELECT F_ID, F_SN, F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime, F_ShopID
			FROM t_returncommoditysheet
			WHERE F_ID = LAST_INSERT_ID();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;