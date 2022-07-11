SELECT '++++++++++++++++++ Test_SP_Commodity_UpdatePrice.sql ++++++++++++++++++++';
-- 因F_PricePurchase字段已去除，故修改相关
SELECT '-------------------- Case1:修改平均进货价 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iINT2 = 3;
SET @iPriceRetail = 0; -- 零售价格
-- SET @iPricePurchase = 2; -- 购买价格
SET @iLatestPricePurchase = 9; -- 最近进货价
SET @iShopID = 2;

-- CALL SP_Commodity_UpdatePrice(@iErrorCode, @sErrorMsg, @iID, @iPriceRetail, @iPricePurchase, @iLatestPricePurchase);
CALL SP_Commodity_UpdatePrice(@iErrorCode, @sErrorMsg, @iID, @iINT2, @iPriceRetail, @iLatestPricePurchase, @iShopID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_PriceRetail = @iPriceRetail AND F_PricePurchase = @iPricePurchase AND F_LatestPricePurchase = @iLatestPricePurchase;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iID AND F_ShopID = @iShopID AND F_PriceRetail = @iPriceRetail AND F_LatestPricePurchase = @iLatestPricePurchase;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:修改零售价 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iINT2 = 3;
SET @iPriceRetail = 10;
-- SET @iPricePurchase = 2;
SET @iLatestPricePurchase = 3;
SET @iShopID = 2;

CALL SP_Commodity_UpdatePrice(@iErrorCode, @sErrorMsg, @iID, @iINT2, @iPriceRetail, @iLatestPricePurchase, @iShopID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iID AND F_PriceRetail = @iPriceRetail AND F_LatestPricePurchase = @iLatestPricePurchase AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:传入不存在的经办人ID -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iINT2 = -10;
SET @iPriceRetail = 10;
-- SET @iPricePurchase = 2;
SET @iLatestPricePurchase = 3;
SET @iShopID = 2;

CALL SP_Commodity_UpdatePrice(@iErrorCode, @sErrorMsg, @iID, @iINT2, @iPriceRetail, @iLatestPricePurchase, @iShopID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 4, '测试成功', '测试失败') AS 'Case3 Testing Result';