DROP PROCEDURE IF EXISTS `SP_ReturnCommoditySheetCommodity_Delete`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_ReturnCommoditySheetCommodity_Delete`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
   	IN iReturnCommoditySheetID INT,
   	IN iCommodityID INT
	)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		IF EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_Status = 1 AND F_ID = iReturnCommoditySheetID) THEN 
			
			SET iErrorCode := 7;
			SET sErrorMsg := '����ɾ������˵��˻����е�����';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_ID = iReturnCommoditySheetID) THEN 
			
			SET iErrorCode := 7;
			SET sErrorMsg := '���˻���������';
				
		-- iCommodityID>0����ɾ��һ���˻����ӱ������,iCommodityID<=0����ɾ�����˻���ID�ڴӱ����������
		ELSEIF (iCommodityID > 0) AND NOT EXISTS (SELECT 1 FROM t_returncommoditysheet WHERE F_Status = 1 AND F_ID = iReturnCommoditySheetID) THEN
		   
			DELETE FROM t_returncommoditysheetcommodity WHERE F_CommodityID = iCommodityID AND F_ReturnCommoditySheetID = iReturnCommoditySheetID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		ELSE
		   
			DELETE FROM t_returncommoditysheetcommodity WHERE F_ReturnCommoditySheetID = iReturnCommoditySheetID;
			
			SET iErrorCode := 0;
			SET sErrorMsg := '';
		END IF;
	
	COMMIT;
END