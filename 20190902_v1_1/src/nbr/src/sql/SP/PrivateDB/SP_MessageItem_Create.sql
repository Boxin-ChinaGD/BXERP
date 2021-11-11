DROP PROCEDURE IF EXISTS `SP_MessageItem_Create`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_MessageItem_Create`(
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iMessageID INT,
	IN iMessageCategoryID INT,
	IN iCommodityID INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
		
		IF NOT EXISTS (SELECT 1 FROM t_message WHERE F_ID = iMessageID) THEN
			SET iErrorCode = 7;
			SET sErrorMsg = '传入的消息ID不正确，不存在该消息';
			
		ELSEIF NOT EXISTS (SELECT 1 FROM t_messagecategory WHERE F_ID = iMessageCategoryID) THEN
			SET iErrorCode = 7;
			SET sErrorMsg = '传入的消息分类ID不正确，不存在该消息分类';
		
		ELSEIF NOT EXISTS (SELECT 1 FROM t_commodity WHERE F_ID = iCommodityID) THEN
			SET iErrorCode = 7;
			SET sErrorMsg = '传入的商品ID不正确，不存在该商品';
		ELSE
			INSERT INTO t_messageitem(F_MessageID, F_MessageCategoryID, F_CommodityID) VALUES (iMessageID, iMessageCategoryID, iCommodityID);
			
			SELECT F_ID, F_MessageID, F_MessageCategoryID, F_CommodityID, F_CreateDatetime, F_UpdateDatetime FROM t_messageitem WHERE F_ID = LAST_INSERT_ID();
			
			SET iErrorCode = 0;
			SET sErrorMsg = '';
		END IF;
			
	COMMIT;
END;