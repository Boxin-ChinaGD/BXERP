use nbr;

SELECT '----------------------------------------------------------------------------------------------------------------------------';
select 'nbr Running SPD Test...';

delimiter $$

SELECT 'PrivateDB_SPD_Test 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckBrandID.sql
SELECT 'PrivateDB_SPD_Test 2'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckCategoryID.sql
SELECT 'PrivateDB_SPD_Test 3'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckCouponScope.sql
SELECT 'PrivateDB_SPD_Test 4'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckInventory.sql
SELECT 'PrivateDB_SPD_Test 5'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckNO.sql
SELECT 'PrivateDB_SPD_Test 6'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckProvider.sql
SELECT 'PrivateDB_SPD_Test 7'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckPurchasingOrder.sql
SELECT 'PrivateDB_SPD_Test 8'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckReturnCommoditySheet.sql
SELECT 'PrivateDB_SPD_Test 9'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 10'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckSubCommodity.sql
SELECT 'PrivateDB_SPD_Test 11'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckType.sql
SELECT 'PrivateDB_SPD_Test 12'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Commodity_CheckWarehousing.sql
SELECT 'PrivateDB_SPD_Test 13'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_InventoryCommodity_CheckInventorySheetID.sql
SELECT 'PrivateDB_SPD_Test 14'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_InventoryCommodity_CheckNOReal.sql
SELECT 'PrivateDB_SPD_Test 15'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_InventoryCommodtiy_CheckCommodity.sql
SELECT 'PrivateDB_SPD_Test 16'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Inventory_CheckInventoryCommodtiy.sql
SELECT 'PrivateDB_SPD_Test 17'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Inventory_CheckStaffID.sql
SELECT 'PrivateDB_SPD_Test 18'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Inventory_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 19'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Inventory_CheckWarehouseID.sql
SELECT 'PrivateDB_SPD_Test 20'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Pos_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 21'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Promotion_CheckDatetime.sql
SELECT 'PrivateDB_SPD_Test 22'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Promotion_CheckScope.sql
SELECT 'PrivateDB_SPD_Test 23'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Promotion_CheckSN.sql
SELECT 'PrivateDB_SPD_Test 24'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Promotion_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 25'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Promotion_CheckType.sql
SELECT 'PrivateDB_SPD_Test 26'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_PurchasingOrderCommodity_CheckBarcodesID.sql
SELECT 'PrivateDB_SPD_Test 27'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_PurchasingOrderCommodity_CheckCommodity.sql
SELECT 'PrivateDB_SPD_Test 28'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_PurchasingOrderCommodity_CheckPackageUnitID.sql
SELECT 'PrivateDB_SPD_Test 29'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_PurchasingOrder_CheckPurchasingOrderCommodity.sql
SELECT 'PrivateDB_SPD_Test 30'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_PurchasingOrder_CheckStaffID.sql
SELECT 'PrivateDB_SPD_Test 31'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_PurchasingOrder_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 32'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTradeCommoditySource_CheckNO.sql
SELECT 'PrivateDB_SPD_Test 33'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTradeCommodity_CheckBarcodeID.sql
SELECT 'PrivateDB_SPD_Test 34'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTradeCommodity_CheckCommodity.sql
SELECT 'PrivateDB_SPD_Test 35'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTrade_CheckAmount.sql
SELECT 'PrivateDB_SPD_Test 36'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTrade_CheckPaymentType.sql
SELECT 'PrivateDB_SPD_Test 37'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTrade_CheckRetailTradeCommodity.sql
SELECT 'PrivateDB_SPD_Test 38'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTrade_CheckSmallSheetID.sql
SELECT 'PrivateDB_SPD_Test 39'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTrade_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 40'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_RetailTrade_CheckVipID.sql
SELECT 'PrivateDB_SPD_Test 41'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheetCommodity_CheckBarcodeID.sql
SELECT 'PrivateDB_SPD_Test 42'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheetCommodity_CheckCommodity.sql
SELECT 'PrivateDB_SPD_Test 43'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheetCommodity_CheckReturnCommoditySheetID.sql
SELECT 'PrivateDB_SPD_Test 44'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheet_CheckProviderID.sql
SELECT 'PrivateDB_SPD_Test 45'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheet_CheckReturnCommoditySheetCommodity.sql
SELECT 'PrivateDB_SPD_Test 46'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheet_CheckStaffID.sql
SELECT 'PrivateDB_SPD_Test 47'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_ReturnCommoditySheet_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 48'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Returnretailtradecommoditydestination_CheckNO.sql
SELECT 'PrivateDB_SPD_Test 49'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Returnretailtradecommoditydestination_CheckWarehousingID.sql
SELECT 'PrivateDB_SPD_Test 50'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Shop_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 51'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckICID.sql
SELECT 'PrivateDB_SPD_Test 52'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckIsFirstTimeLogin.sql
SELECT 'PrivateDB_SPD_Test 53'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckName.sql
SELECT 'PrivateDB_SPD_Test 54'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckOpenID.sql
SELECT 'PrivateDB_SPD_Test 55'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckPhone.sql
SELECT 'PrivateDB_SPD_Test 56'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 57'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckUnionid.sql
SELECT 'PrivateDB_SPD_Test 58'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Staff_CheckWeChat.sql
SELECT 'PrivateDB_SPD_Test 59'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Vip_CheckBonus.sql
SELECT 'PrivateDB_SPD_Test 60'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Vip_CheckName.sql
SELECT 'PrivateDB_SPD_Test 61'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Vip_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 62'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Warehouse_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 63'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_WarehousingCommodity_CheckBarcodesID.sql
SELECT 'PrivateDB_SPD_Test 64'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_WarehousingCommodity_CheckCommodity.sql
SELECT 'PrivateDB_SPD_Test 65'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_WarehousingCommodity_CheckPackageUnitID.sql
SELECT 'PrivateDB_SPD_Test 66'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_WarehousingCommodity_CheckSalableNO.sql
SELECT 'PrivateDB_SPD_Test 67'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_WarehousingCommodity_CheckWarehousingID.sql
SELECT 'PrivateDB_SPD_Test 68'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Warehousing_CheckProviderID.sql
SELECT 'PrivateDB_SPD_Test 69'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Warehousing_CheckStaffID.sql
SELECT 'PrivateDB_SPD_Test 70'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Warehousing_CheckStatus.sql
SELECT 'PrivateDB_SPD_Test 71'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Warehousing_CheckWarehouseID.sql
SELECT 'PrivateDB_SPD_Test 72'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/Doctor/PrivateDB/SPD_Warehousing_CheckWarehousingCommodity.sql


-- 检查私有DB nbr的Test_SPD数目有无变更
SET @var = (SELECT COUNT(1) FROM information_schema.routines  WHERE routine_schema = 'nbr' AND ROUTINE_NAME LIKE 'SPD\_%')$$
SELECT IF(@var = 72,'nbr的Test_SPD数目一致，检查成功！',concat('检查不成功！nbr的Test_SPD数目应该是', @var, '！请检查最近新增了哪些Test_SPD！！')) AS TestSPDNO$$

delimiter ;