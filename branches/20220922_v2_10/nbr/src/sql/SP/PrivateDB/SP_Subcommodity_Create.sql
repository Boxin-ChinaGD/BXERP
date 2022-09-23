DROP PROCEDURE IF EXISTS `SP_Subcommodity_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_Subcommodity_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iCommodityID INT,
	IN iSubCommodityID INT,
	IN iSubCommodityNO INT,
	IN iPrice DECIMAL(20, 6)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		-- �ڴ��������Ʒ��ҳ���У����ܳ��ֳ��˵�Ʒ�������������Ʒ
		
		IF EXISTS (SELECT 1 FROM t_commodity WHERE F_Status <> 2 AND F_ID = iCommodityID AND F_Type <> 1) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := 'ֻ�������Ʒ�ܲ�������Ʒ';
		ELSEIF iPrice < 0 THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '����Ʒ�۸���Ϊ����';
	   	ELSEIF EXISTS (SELECT 1 FROM t_subcommodity WHERE F_CommodityID = iCommodityID AND F_SubCommodityID = iSubCommodityID) THEN
			SET iErrorCode := 1;
			SET sErrorMsg := '���������ͬ����Ʒ��ͬһ�����Ʒ��';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) OR NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iSubCommodityID) THEN 
			SET iErrorCode := 7;
			SET sErrorMsg := '������Ӳ����ڵ���Ʒ';
		ELSEIF NOT EXISTS(SELECT 1 FROM t_commodity WHERE F_ID = iSubCommodityID AND F_Type = 0) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '������ϳ��˵�Ʒ�������������Ʒ';
		ELSE
			INSERT INTO t_subcommodity (F_CommodityID, F_SubCommodityID, F_SubCommodityNO, F_Price) VALUES (iCommodityID, iSubCommodityID, iSubCommodityNO, iPrice);
			
			SELECT 
				F_ID, 
				F_CommodityID, 
				F_SubCommodityID,
				F_SubCommodityNO,
				F_Price,
				F_CreateDatetime,
				F_UpdateDatetime 
			FROM t_subcommodity 
			WHERE F_ID = last_insert_id();
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END;