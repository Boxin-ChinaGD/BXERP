DROP PROCEDURE IF EXISTS `SP_RetailTrade_RetrieveOldTrade`;
CREATE DEFINER=`root`@`localhost` PROCEDURE `SP_RetailTrade_RetrieveOldTrade` (
	OUT iErrorCode INT,
	OUT sErrorMsg VARCHAR(64),
	IN iShopID INT,
	IN iVipID INT,
	IN bRequestFromApp INT, -- bRequestFromApp=1,根据sSN查询。bRequestFromApp=0，表明是nbr端，根据queryKeyword查询
	IN queryKeyword VARCHAR(32),
	IN sLocalSN INT,	
	IN iPOS_ID INT,
	IN dStartDate DATETIME,
	IN dEndDate DATETIME,
	IN iPaymentType INT,
	IN iStaffID INT,
	IN iPageIndex INT,
	IN iPageSize INT,
	IN iExcludeReturned INT,
	OUT iTotalRecord INT
	)
BEGIN
	DECLARE recordIndex INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		SET iErrorCode := 3, sErrorMsg = '数据库错误';
		ROLLBACK;
	END;
	
	START TRANSACTION;
	
		SET sErrorMsg = '';
		SET iPageIndex = iPageIndex - 1;
		
		SET recordIndex = iPageIndex * iPageSize;
		
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
		FROM (

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
				FROM t_retailtrade 
				WHERE 
					(CASE queryKeyword WHEN '' THEN 1=1 ELSE length(queryKeyword) BETWEEN 10 AND 26 AND F_SN LIKE CONCAT('%',replace(queryKeyword, '_', '\_'),'%') END)
				UNION 
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
				FROM t_retailtrade 
				WHERE  (CASE bRequestFromApp 
					WHEN 0 THEN 
						(CASE IFNULL(queryKeyword, '') WHEN '' THEN 1=1 ELSE F_ID IN (
							SELECT F_TradeID FROM t_retailtradecommodity WHERE F_CommodityName LIKE CONCAT('%',replace(queryKeyword, '_', '\_'), '%')
						) END) 
					ELSE
						1=0
					END) 
		)AS TMP
		WHERE 1 = 1
			AND (CASE iShopID WHEN 0 THEN 1=1 ELSE F_ShopID = iShopID END) 
			AND (CASE sLocalSN WHEN '' THEN 1=1 ELSE F_LocalSN = sLocalSN END) 
			AND (CASE iPOS_ID WHEN 0 THEN 1=1 ELSE F_POS_ID = iPOS_ID END) 
		  	AND (CASE ifnull(dStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dStartDate END) 
		  	AND (CASE ifnull(dEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dEndDate END) 
		  	AND (CASE iVipID WHEN 0 THEN 1=1 ELSE F_VipID = iVipID END) 
		  	AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (F_PaymentType & iPaymentType) = iPaymentType END) 
		  	AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE F_StaffID = iStaffID END) 
		  	AND (CASE iExcludeReturned WHEN 0 THEN 1=1 ELSE F_SourceID = -1 END) -- iExcludeReturned为0包含退货单，为1不包含退货单
		  	
		ORDER BY F_ID DESC
		LIMIT recordIndex, iPageSize;
		
		SELECT count(1) into iTotalRecord 
		FROM (

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
				FROM t_retailtrade 
				WHERE 
					(CASE queryKeyword WHEN '' THEN 1=1 ELSE length(queryKeyword) >= 10 AND F_SN LIKE CONCAT('%',replace(queryKeyword, '_', '\_'),'%') END)
				UNION 
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
				FROM t_retailtrade 
				WHERE  (CASE bRequestFromApp 
					WHEN 0 THEN 
						(CASE IFNULL(queryKeyword, '') WHEN '' THEN 1=1 ELSE F_ID IN (
							SELECT F_TradeID FROM t_retailtradecommodity WHERE F_CommodityName LIKE CONCAT('%',replace(queryKeyword, '_', '\_'), '%')
						) END) 
					ELSE
						1=0
					END) 
		)AS TMP
		WHERE 1 = 1
			AND (CASE iShopID WHEN 0 THEN 1=1 ELSE F_ShopID = iShopID END) 
			AND (CASE sLocalSN WHEN '' THEN 1=1 ELSE F_LocalSN = sLocalSN END) 
			AND (CASE iPOS_ID WHEN 0 THEN 1=1 ELSE F_POS_ID = iPOS_ID END) 
		  	AND (CASE ifnull(dStartDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime >= dStartDate END) 
		  	AND (CASE ifnull(dEndDate, 0) WHEN 0 THEN 1=1 ELSE F_SaleDatetime <= dEndDate END) 
		  	AND (CASE iVipID WHEN 0 THEN 1=1 ELSE F_VipID = iVipID END) 
		  	AND (CASE iPaymentType WHEN 0 THEN 1=1 ELSE (F_PaymentType & iPaymentType) = iPaymentType END) 
		  	AND (CASE iStaffID WHEN 0 THEN 1=1 ELSE F_StaffID = iStaffID END)
	  		AND (CASE iExcludeReturned WHEN 0 THEN 1=1 ELSE F_SourceID = -1 END); -- iExcludeReturned为0包含退货单，为1不包含退货单
	  		
		SET iErrorCode := 0;
   		SET sErrorMsg := '';
	
	COMMIT;
END;