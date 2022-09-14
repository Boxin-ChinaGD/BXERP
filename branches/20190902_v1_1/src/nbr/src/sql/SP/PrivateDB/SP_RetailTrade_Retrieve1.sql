DROP PROCEDURE IF EXISTS `SP_RetailTrade_Retrieve1`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTrade_Retrieve1` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iID INT
	)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3;
		SET sErrorMsg := 'Êý¾Ý¿â´íÎó';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SELECT 
			F_ID, 
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
			F_AmountPaidIn, 
			F_AmountChange, 
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
			F_WxRefundSubMchID,
			F_CouponAmount, 
			F_ConsumerOpenID,
			F_ShopID
		FROM t_retailtrade WHERE F_ID = iID;
	
		SET iErrorCode := 0;
		SET sErrorMsg := '';
		
	COMMIT;
END;