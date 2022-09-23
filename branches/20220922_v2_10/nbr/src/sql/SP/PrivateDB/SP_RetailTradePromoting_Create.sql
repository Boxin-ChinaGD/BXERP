DROP PROCEDURE IF EXISTS `SP_RetailTradePromoting_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradePromoting_Create` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iTradeID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '���ݿ����';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		IF EXISTS(SELECT 1 FROM t_retailtrade WHERE F_ID = iTradeID) THEN
			-- ������۵�ID�Ѿ�����ɾ���ڽ��в���
			IF EXISTS(SELECT 1 FROM t_retailtradepromoting WHERE F_TradeID = iTradeID) THEN 
		  		DELETE FROM t_retailtradepromotingflow WHERE F_RetailTradePromotingID = (SELECT F_ID FROM t_retailtradepromoting WHERE F_TradeID = iTradeID);
		   		DELETE FROM t_retailtradepromoting WHERE F_TradeID = iTradeID;   
	   		END IF ;
		
			INSERT INTO t_retailtradepromoting (F_TradeID) VALUES (iTradeID);
		
	   		SELECT F_ID, F_TradeID,F_CreateDateTime FROM t_retailtradepromoting WHERE F_ID = LAST_INSERT_ID();
	
	   		SET iErrorCode := 0;
	  		SET sErrorMsg := '';
	  	ELSE 
	  		SET iErrorCode := 2;
	  		SET sErrorMsg := 'û�д����۵�';
		END IF;
	COMMIT;
END