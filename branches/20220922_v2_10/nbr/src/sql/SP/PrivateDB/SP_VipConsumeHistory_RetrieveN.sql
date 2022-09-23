DROP PROCEDURE IF EXISTS `SP_VipConsumeHistory_RetrieveN`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_VipConsumeHistory_RetrieveN` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT,
	IN iStartRetailTradeIDInSQLite INT, -- 查询会员消费记录的零售单ID起始值
	IN bQuerySmallerThanStartID INT, -- 如果为0,查询大于起始值的零售单ID,如果为1,则查询小于起始值的零售单ID
	IN iPageIndex INT,
	IN iPageSize INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
		IF EXISTS(SELECT 1 FROM t_vip WHERE F_ID = iID) THEN
		    SELECT 
			   F_ID, 
			   F_VipID, 
			   F_SN, 
			   F_LocalSN, 
			   F_POS_ID, 
			   F_Logo, 
			   F_SaleDatetime, 
			   F_StaffID, 
			   F_PaymentType, 
			   F_PaymentAccount, 
			   F_Status, 
			   F_Remark, 
			   F_SourceID, 
			   F_SyncDatetime, 
			   F_Amount, 
			   F_AmountCash, 
			   F_AmountAlipay, 
			   F_AmountWeChat, 
			   F_Amount1, 
			   F_Amount2, 
			   F_Amount3, 
			   F_Amount4, 
			   F_Amount5, 
			   F_SmallSheetID,
			   F_AliPayOrderSN,
			   F_WxOrderSN,
			   F_WxTradeNO,
			   F_WxRefundNO,
			   F_WxRefundDesc,
			   F_WxRefundSubMchID
			 FROM t_retailtrade WHERE F_VipID = iID
			 AND (CASE bQuerySmallerThanStartID WHEN 0 THEN F_ID > iStartRetailTradeIDInSQLite ELSE 1=1 END)
			 AND (CASE bQuerySmallerThanStartID WHEN 1 THEN F_ID < iStartRetailTradeIDInSQLite ELSE 1=1 END)
			 ORDER BY F_ID DESC
			 LIMIT recordIndex, iPageSize;
			 
			 SELECT found_rows() into iTotalRecord;
			 
			 SELECT 
				 F_ID, 
				 F_TradeID, 
				 F_CommodityID, 
				 F_BarcodeID, 
				 F_NO, 
				 F_PriceOriginal, 
				 F_NOCanReturn, 
				 F_PriceReturn, 
				 F_PriceSpecialOffer, 
				 F_PriceVIPOriginal
			FROM t_retailtradecommodity 
			WHERE F_TradeID IN (
				SELECT F_ID FROM t_retailtrade 
				WHERE F_VipID = iID
				AND (CASE bQuerySmallerThanStartID WHEN 0 THEN F_ID > iStartRetailTradeIDInSQLite ELSE 1=1 END)
				AND (CASE bQuerySmallerThanStartID WHEN 1 THEN F_ID < iStartRetailTradeIDInSQLite ELSE 1=1 END)
			);
	
			 SET iErrorCode := 0;
			 SET sErrorMsg := '';
		ELSE
	   		SET iTotalRecord := 0; 
			SET iErrorCode := 2;
			SET sErrorMsg := '不能使用错误的VipID查询';
		END IF;
	
	COMMIT;
END;