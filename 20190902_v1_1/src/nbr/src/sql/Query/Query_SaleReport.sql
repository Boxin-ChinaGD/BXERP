SELECT F_ID, F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime
FROM nbr.t_retailtradeaggregation
ORDER BY F_ID DESC

SELECT F_ID, F_VipID, F_SN, F_LocalSN, F_POS_ID, F_Logo, F_SaleDatetime, F_StaffID, F_PaymentType, F_PaymentAccount, F_Status, F_Remark, F_SourceID, F_SyncDatetime, F_Amount, F_AmountCash, F_AmountAlipay, F_AmountWeChat, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_SmallSheetID, F_AliPayOrderSN, F_WxOrderSN, F_WxTradeNO, F_WxRefundNO, F_WxRefundDesc, F_WxRefundSubMchID
FROM nbr.t_retailtrade
ORDER BY F_ID DESC

SELECT F_ID, F_Datetime, F_TotalAmount, F_TotalGross, F_CreateDatetime, F_UpdateDatetime
FROM nbr.t_retailtrademonthlyreportsummary
ORDER BY F_ID DESC

SELECT F_ID, F_Datetime, F_TotalNO, F_PricePurchase, F_TotalAmount, F_AverageAmountOfCustomer, F_TotalGross, F_RatioGrossMargin, F_TopSaleCommodityID, F_TopSaleCommodityNO, F_TopSaleCommodityAmount, F_TopPurchaseCustomerName, F_CreateDatetime, F_UpdateDatetime
FROM nbr.t_retailtradedailyreportsummary
ORDER BY F_ID DESC

SELECT F_ID, F_Datetime, F_StaffID, F_NO, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime
FROM nbr.t_retailtradedailyreportbystaff
ORDER BY F_ID DESC

SELECT F_ID, F_Datetime, F_CommodityID, F_NO, F_TotalPurchasingAmount, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime
FROM nbr.t_retailtradedailyreportbycommodity
ORDER BY F_ID DESC

SELECT F_ID, F_Datetime, F_CategoryParentID, F_TotalAmount, F_CreateDatetime, F_UpdateDatetime
FROM nbr.t_retailtradedailyreportbycategoryparent
ORDER BY F_ID DESC