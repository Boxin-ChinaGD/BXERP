SELECT '++++++++++++++++++ Test_SP_Commodity_UpdatePrice.sql ++++++++++++++++++++';
-- ��F_PricePurchase�ֶ���ȥ�������޸����
SELECT '-------------------- Case1:�޸�ƽ�������� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iINT2 = 3;
SET @iPriceRetail = 0; -- ���ۼ۸�
-- SET @iPricePurchase = 2; -- ����۸�
SET @iLatestPricePurchase = 9; -- ���������
SET @iShopID = 2;

-- CALL SP_Commodity_UpdatePrice(@iErrorCode, @sErrorMsg, @iID, @iPriceRetail, @iPricePurchase, @iLatestPricePurchase);
CALL SP_Commodity_UpdatePrice(@iErrorCode, @sErrorMsg, @iID, @iINT2, @iPriceRetail, @iLatestPricePurchase, @iShopID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
-- SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_PriceRetail = @iPriceRetail AND F_PricePurchase = @iPricePurchase AND F_LatestPricePurchase = @iLatestPricePurchase;
SELECT 1 FROM t_commodityshopinfo WHERE F_CommodityID = @iID AND F_ShopID = @iShopID AND F_PriceRetail = @iPriceRetail AND F_LatestPricePurchase = @iLatestPricePurchase;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:�޸����ۼ� -------------------------' AS 'Case2';

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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:���벻���ڵľ�����ID -------------------------' AS 'Case3';

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
SELECT IF(@iErrorCode = 4, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';