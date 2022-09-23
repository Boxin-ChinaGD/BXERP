DROP PROCEDURE IF EXISTS `SP_RetailTradePromotingFlow_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTradePromotingFlow_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iRetailTradePromotingID INT,
   	IN iPromotionID INT,
   	IN sProcessFlow VARCHAR(2048)
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		IF iPromotionID IS NOT NULL AND NOT EXISTS (SELECT 1 FROM t_promotion WHERE F_ID = iPromotionID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '促销ID不存在';
		ELSEIF NOT EXISTS (SELECT 1 FROM t_RetailTradePromoting WHERE F_ID = iRetailTradePromotingID) THEN
			SET iErrorCode := 7;
			SET sErrorMsg := '主表ID不存在';
		ELSE 
			INSERT INTO t_retailtradepromotingflow (
			    F_RetailTradePromotingID, 
			    F_PromotionID,
			    F_ProcessFlow, 
			    F_CreateDatetime
			    )
		    VALUES (
		        iRetailTradePromotingID, 
		        iPromotionID, 
		        sProcessFlow, 
		        now());
		        
		    SELECT 
		        F_ID, 
		        F_RetailTradePromotingID, 
		        F_PromotionID, 
		        F_ProcessFlow, 
		        F_CreateDatetime
		    FROM t_retailtradepromotingflow
		    WHERE F_ID = LAST_INSERT_ID();
		    
		    SET iErrorCode := 0;
		    SET sErrorMsg := '';
		END IF;
    
    COMMIT;
END;