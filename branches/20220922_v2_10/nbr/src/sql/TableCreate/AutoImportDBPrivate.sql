SELECT 'Steps to go below: ';
SELECT '$ Import private DB';

select 'Create Private Database nbr_test_641dcc';
SELECT 'Creare_Database'$$;
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/TableCreate/Database.sql;


use {nbr_xxx};
select 'Importing tables...';

SELECT 'Creare_Table 1'$$;
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/TableCreate/CreateTables.sql;
SELECT 'Creare_Table 2'$$;
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/TableConfig/PrivateDBConfig.sql;


select 'Importing SP & functions...';

delimiter $$
SELECT '--------------------------------------- PrivateDB SP -------------------------------------------------------'$$
SELECT 'Private_SP 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_Create.sql
SELECT 'Private_SP 2'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_Delete.sql
SELECT 'Private_SP 3'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_DeleteByCombinationCommodityID.sql
SELECT 'Private_SP 4'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_DeleteByMultiPackagingCommodityID.sql
SELECT 'Private_SP 5'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_DeleteByServiceCommodityID.sql
SELECT 'Private_SP 6'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_DeleteBySimpleCommodityID.sql
SELECT 'Private_SP 7'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_Retrieve1.sql
SELECT 'Private_SP 8'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_RetrieveN.sql
SELECT 'Private_SP 9'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Barcodes_Update.sql
SELECT 'Private_SP 10'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusConsumeHistory_Create.sql
SELECT 'Private_SP 11'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusConsumeHistory_Delete.sql
SELECT 'Private_SP 12'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusConsumeHistory_Retrieve1.sql
SELECT 'Private_SP 13'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusConsumeHistory_RetrieveN.sql
SELECT 'Private_SP 14'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusRule_Create.sql
SELECT 'Private_SP 15'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusRule_Delete.sql
SELECT 'Private_SP 16'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusRule_Retrieve1.sql
SELECT 'Private_SP 17'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_BonusRule_Update.sql
SELECT 'Private_SP 18'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Brand_Create.sql
SELECT 'Private_SP 19'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Brand_Delete.sql
SELECT 'Private_SP 20'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Brand_Retrieve1.sql
SELECT 'Private_SP 21'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Brand_RetrieveN.sql
SELECT 'Private_SP 22'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Brand_Update.sql
SELECT 'Private_SP 23'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CategoryParent_Create.sql
SELECT 'Private_SP 24'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CategoryParent_Delete.sql
SELECT 'Private_SP 25'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CategoryParent_Retrieve1.sql
SELECT 'Private_SP 26'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CategoryParent_RetrieveN.sql
SELECT 'Private_SP 27'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CategoryParent_Update.sql
SELECT 'Private_SP 28'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_Create.sql
SELECT 'Private_SP 29'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_Delete.sql
SELECT 'Private_SP 30'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_Retrieve1.sql
SELECT 'Private_SP 31'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_RetrieveN.sql
SELECT 'Private_SP 32'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_RetrieveNByParent.sql
SELECT 'Private_SP 33'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_RetrieveN_CheckUniqueField.sql
SELECT 'Private_SP 34'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Category_Update.sql
SELECT 'Private_SP 35'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CommodityHistory_RetrieveN.sql
SELECT 'Private_SP 36'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CommodityProperty_Retrieve1.sql
SELECT 'Private_SP 37'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CommodityProperty_Update.sql
SELECT 'Private_SP 38'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CommodityShopInfo_Create.sql
SELECT 'Private_SP 39'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CommodityShopInfo_RetrieveN.sql
SELECT 'Private_SP 40'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_CheckDependency.sql
SELECT 'Private_SP 41'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_CreateCombination.sql
SELECT 'Private_SP 42'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_CreateMultiPackaging.sql
SELECT 'Private_SP 43'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_CreateService.sql
SELECT 'Private_SP 44'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_CreateSimple.sql
SELECT 'Private_SP 45'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_DeleteCombination.sql
SELECT 'Private_SP 46'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_DeleteMultiPackaging.sql
SELECT 'Private_SP 47'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_DeleteService.sql
SELECT 'Private_SP 48'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_DeleteSimple.sql
SELECT 'Private_SP 49'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_Retrieve1.sql
SELECT 'Private_SP 50'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_RetrieveInventory.sql
SELECT 'Private_SP 51'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_RetrieveN.sql
SELECT 'Private_SP 52'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_RetrieveNMultiPackageCommodity.sql
SELECT 'Private_SP 53'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_RetrieveN_CheckUniqueField.sql
SELECT 'Private_SP 54'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_Update.sql
SELECT 'Private_SP 55'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_UpdatePrice.sql
SELECT 'Private_SP 56'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_UpdatePurchasingUnit.sql
SELECT 'Private_SP 57'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Commodity_UpdateWarehousing.sql
SELECT 'Private_SP 58'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ConfigCacheSize_Retrieve1.sql
SELECT 'Private_SP 59'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ConfigCacheSize_RetrieveN.sql
SELECT 'Private_SP 60'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ConfigCacheSize_Update.sql
SELECT 'Private_SP 61'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ConfigGeneral_Retrieve1.sql
SELECT 'Private_SP 62'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ConfigGeneral_RetrieveN.sql
SELECT 'Private_SP 63'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ConfigGeneral_Update.sql
SELECT 'Private_SP 64'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_Consume.sql
SELECT 'Private_SP 65'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_Create.sql
SELECT 'Private_SP 66'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_Delete.sql
SELECT 'Private_SP 67'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_Retrieve1.sql
SELECT 'Private_SP 68'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_RetrieveN.sql
SELECT 'Private_SP 69'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_RetrieveNByVipID.sql
SELECT 'Private_SP 70'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponCode_RetrieveNTotalByVipID.sql
SELECT 'Private_SP 71'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponScope_Create.sql
SELECT 'Private_SP 72'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponScope_Delete.sql
SELECT 'Private_SP 73'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponScope_Retrieve1.sql
SELECT 'Private_SP 74'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_CouponScope_RetrieveN.sql
SELECT 'Private_SP 75'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Coupon_Create.sql
SELECT 'Private_SP 76'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Coupon_Delete.sql
SELECT 'Private_SP 77'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Coupon_Retrieve1.sql
SELECT 'Private_SP 78'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Coupon_RetrieveN.sql
SELECT 'Private_SP 79'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Department_RetrieveN.sql
SELECT 'Private_SP 80'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_InventoryCommodity_Create.sql
SELECT 'Private_SP 81'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_InventoryCommodity_Delete.sql
SELECT 'Private_SP 82'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_InventoryCommodity_RetrieveN.sql
SELECT 'Private_SP 83'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_InventoryCommodity_UpdateNoReal.sql
SELECT 'Private_SP 84'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_Approve.sql
SELECT 'Private_SP 85'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_Create.sql
SELECT 'Private_SP 86'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_Delete.sql
SELECT 'Private_SP 87'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_Retrieve1.sql
SELECT 'Private_SP 88'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_RetrieveN.sql
SELECT 'Private_SP 89'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_RetrieveNByFields.sql
SELECT 'Private_SP 90'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_Submit.sql
SELECT 'Private_SP 91'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_UpdateCommodity.sql
SELECT 'Private_SP 92'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Inventory_UpdateSheet.sql
SELECT 'Private_SP 93'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageCategory_RetrieveN.sql
SELECT 'Private_SP 94'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageHandlerSetting_Create.sql
SELECT 'Private_SP 95'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageHandlerSetting_Delete.sql
SELECT 'Private_SP 96'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageHandlerSetting_Retrieve1.sql
SELECT 'Private_SP 97'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageHandlerSetting_RetrieveN.sql
SELECT 'Private_SP 98'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageHandlerSetting_Update.sql
SELECT 'Private_SP 99'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageItem_Create.sql
SELECT 'Private_SP 100'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_MessageItem_RetrieveN.sql
SELECT 'Private_SP 101'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Message_Create.sql
SELECT 'Private_SP 102'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Message_Retrieve1.sql
SELECT 'Private_SP 103'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Message_RetrieveN.sql
SELECT 'Private_SP 104'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Message_RetrieveNForWx.sql
SELECT 'Private_SP 105'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Message_Update.sql
SELECT 'Private_SP 106'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Message_UpdateStatus.sql
SELECT 'Private_SP 107'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PackageUnit_Create.sql
SELECT 'Private_SP 108'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PackageUnit_Delete.sql
SELECT 'Private_SP 109'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PackageUnit_Retrieve1.sql
SELECT 'Private_SP 110'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PackageUnit_RetrieveN.sql
SELECT 'Private_SP 111'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PackageUnit_Update.sql
SELECT 'Private_SP 112'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Permission_Create.sql
SELECT 'Private_SP 113'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Permission_Delete.sql
SELECT 'Private_SP 114'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Permission_RetrieveAlsoRoleStaff.sql
SELECT 'Private_SP 115'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Permission_RetrieveN.sql
SELECT 'Private_SP 116'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_Create.sql
SELECT 'Private_SP 117'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_Delete.sql
SELECT 'Private_SP 118'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_RecycleApp.sql
SELECT 'Private_SP 119'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_Reset.sql
SELECT 'Private_SP 120'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_Retrieve1.sql
SELECT 'Private_SP 121'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_Retrieve1BySN.sql
SELECT 'Private_SP 122'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_RetrieveN.sql
SELECT 'Private_SP 123'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_POS_Update.sql
SELECT 'Private_SP 124'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PromotionScope_Create.sql
SELECT 'Private_SP 125'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PromotionScope_RetrieveN.sql
SELECT 'Private_SP 126'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PromotionShopScope_Create.sql
SELECT 'Private_SP 127'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PromotionShopScope_RetrieveN.sql
SELECT 'Private_SP 128'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Promotion_Create.sql
SELECT 'Private_SP 129'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Promotion_Delete.sql
SELECT 'Private_SP 130'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Promotion_Retrieve1.sql
SELECT 'Private_SP 131'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Promotion_RetrieveN.sql
SELECT 'Private_SP 132'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Promotion_Update.sql
SELECT 'Private_SP 133'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderCommodity_Create.sql
SELECT 'Private_SP 134'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderCommodity_Delete.sql
SELECT 'Private_SP 135'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderCommodity_RetrieveN.sql
SELECT 'Private_SP 136'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderDistrict_Create.sql
SELECT 'Private_SP 137'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderDistrict_Delete.sql
SELECT 'Private_SP 138'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderDistrict_Retrieve1.sql
SELECT 'Private_SP 139'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderDistrict_RetrieveN.sql
SELECT 'Private_SP 140'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ProviderDistrict_Update.sql
SELECT 'Private_SP 141'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_Create.sql
SELECT 'Private_SP 142'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_Delete.sql
SELECT 'Private_SP 143'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_Retrieve1.sql
SELECT 'Private_SP 144'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_RetrieveN.sql
SELECT 'Private_SP 145'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_RetrieveNByFields.sql
SELECT 'Private_SP 146'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_RetrieveN_CheckUniqueField.sql
SELECT 'Private_SP 147'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Provider_Update.sql
SELECT 'Private_SP 148'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrderCommodity_Create.sql
SELECT 'Private_SP 149'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrderCommodity_Delete.sql
SELECT 'Private_SP 150'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrderCommodity_RetrieveN.sql
SELECT 'Private_SP 151'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrderCommodity_RetrieveNWarehousing.sql
SELECT 'Private_SP 152'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_Approve.sql
SELECT 'Private_SP 153'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_Create.sql
SELECT 'Private_SP 154'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_Delete.sql
SELECT 'Private_SP 155'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_Retrieve1.sql
SELECT 'Private_SP 156'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_RetrieveN.sql
SELECT 'Private_SP 157'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_RetrieveNByFields.sql
SELECT 'Private_SP 158'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_Update.sql
SELECT 'Private_SP 159'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_PurchasingOrder_UpdateStatus.sql
SELECT 'Private_SP 160'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Report_Warehousing.sql
SELECT 'Private_SP 161'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeAggregation_Create.sql
SELECT 'Private_SP 162'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeAggregation_Retrieve1.sql
SELECT 'Private_SP 163'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCommoditySource_RetrieveN.sql
SELECT 'Private_SP 164'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCommodity_delete.sql
SELECT 'Private_SP 165'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCommodity_RetrieveNTradeCommodity.sql
SELECT 'Private_SP 166'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCommodity_UploadTrade_CreateCommodity.sql
SELECT 'Private_SP 167'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCommodity_UploadTrade_CreateReturnCommodity.sql
SELECT 'Private_SP 168'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCommodity_UploadTrade_CreateSaleCommodity.sql
SELECT 'Private_SP 169'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCoupon_Create.sql
SELECT 'Private_SP 170'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeCoupon_RetrieveN.sql
SELECT 'Private_SP 171'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportByCategoryParent_Create.sql
SELECT 'Private_SP 172'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportByCategoryParent_RetrieveN.sql
SELECT 'Private_SP 173'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportByCommodity_RetrieveN.sql
SELECT 'Private_SP 174'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportByStaff_Create.sql
SELECT 'Private_SP 175'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportByStaff_RetrieveN.sql
SELECT 'Private_SP 176'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportSummary_Retrieve1.sql
SELECT 'Private_SP 177'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReportSummary_RetrieveNForChart.sql
SELECT 'Private_SP 178'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeDailyReport_Create.sql
SELECT 'Private_SP 179'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeMonthlyReportSummary_Create.sql
SELECT 'Private_SP 180'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeMonthlyReportSummary_RetrieveN.sql
SELECT 'Private_SP 181'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradePromotingFlow_Create.sql
SELECT 'Private_SP 182'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradePromotingFlow_RetrieveN.sql
SELECT 'Private_SP 183'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradePromoting_Create.sql
SELECT 'Private_SP 184'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradePromoting_Retrieve1.sql
SELECT 'Private_SP 185'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradePromoting_RetrieveN.sql
SELECT 'Private_SP 186'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTradeReportByCommodity_RetrieveN.sql
SELECT 'Private_SP 187'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTrade_Retrieve1.sql
SELECT 'Private_SP 188'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTrade_RetrieveNByCommodityNameInTimeRange.sql
SELECT 'Private_SP 189'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTrade_RetrieveOldTrade.sql
SELECT 'Private_SP 190'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RetailTrade_UploadTrade.sql
SELECT 'Private_SP 191'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheetCommodity_Create.sql
SELECT 'Private_SP 192'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheetCommodity_Delete.sql
SELECT 'Private_SP 193'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheetCommodity_RetrieveN.sql
SELECT 'Private_SP 194'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheet_Approve.sql
SELECT 'Private_SP 195'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheet_Create.sql
SELECT 'Private_SP 196'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheet_Retrieve1.sql
SELECT 'Private_SP 197'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheet_RetrieveN.sql
SELECT 'Private_SP 198'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnCommoditySheet_Update.sql
SELECT 'Private_SP 199'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ReturnRetailTradeCommodityDestination_RetrieveN.sql
SELECT 'Private_SP 200'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_RolePermission_Delete.sql
SELECT 'Private_SP 201'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Role_Create.sql
SELECT 'Private_SP 202'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Role_Delete.sql
SELECT 'Private_SP 203'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Role_RetrieveAlsoStaff.sql
SELECT 'Private_SP 204'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Role_RetrieveN.sql
SELECT 'Private_SP 205'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Role_Update.sql
SELECT 'Private_SP 206'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ShopDistrict_Create.sql
SELECT 'Private_SP 207'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ShopDistrict_Retrieve1.sql
SELECT 'Private_SP 208'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_ShopDistrict_RetrieveN.sql
SELECT 'Private_SP 209'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_Create.sql
SELECT 'Private_SP 210'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_Delete.sql
SELECT 'Private_SP 211'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_Retrieve1.sql
SELECT 'Private_SP 212'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_RetrieveN.sql
SELECT 'Private_SP 213'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_RetrieveNByFields.sql
SELECT 'Private_SP 214'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_RetrieveN_CheckUniqueField.sql
SELECT 'Private_SP 215'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Shop_Update.sql
SELECT 'Private_SP 216'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetFrame_Create.sql
SELECT 'Private_SP 217'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetFrame_Delete.sql
SELECT 'Private_SP 218'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetFrame_Retrieve1.sql
SELECT 'Private_SP 219'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetFrame_RetrieveN.sql
SELECT 'Private_SP 220'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetFrame_Update.sql
SELECT 'Private_SP 221'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetText_Create.sql
SELECT 'Private_SP 222'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetText_Delete.sql
SELECT 'Private_SP 223'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetText_RetrieveN.sql
SELECT 'Private_SP 224'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_SmallSheetText_Update.sql
SELECT 'Private_SP 225'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_StaffBelonging_RetrieveN.sql
SELECT 'Private_SP 226'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_StaffRole_Retrieve1.sql
SELECT 'Private_SP 227'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_StaffRole_RetrieveN.sql
SELECT 'Private_SP 228'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_Create.sql
SELECT 'Private_SP 229'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_Delete.sql
SELECT 'Private_SP 230'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_ResetPassword.sql
SELECT 'Private_SP 231'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_Retrieve1.sql
SELECT 'Private_SP 232'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_RetrieveN.sql
SELECT 'Private_SP 233'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_RetrieveNPermission.sql
SELECT 'Private_SP 234'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_RetrieveN_CheckUniqueField.sql
SELECT 'Private_SP 235'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_Update.sql
SELECT 'Private_SP 236'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_Update_OpenidAndUnionid.sql
SELECT 'Private_SP 237'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Staff_Update_Unsubscribe.sql
SELECT 'Private_SP 238'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_StateMachine_RetrieveN.sql
SELECT 'Private_SP 239'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Subcommodity_Create.sql
SELECT 'Private_SP 240'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Subcommodity_Delete.sql
SELECT 'Private_SP 241'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Subcommodity_RetrieveN.sql
SELECT 'Private_SP 242'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_UnsalableCommodity_RetrieveN.sql
SELECT 'Private_SP 243'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipBelonging_RetrieveN.sql
SELECT 'Private_SP 244'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCardCode_Create.sql
SELECT 'Private_SP 245'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCardCode_Delete.sql
SELECT 'Private_SP 246'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCardCode_Retrieve1.sql
SELECT 'Private_SP 247'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCardCode_RetrieveN.sql
SELECT 'Private_SP 248'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCard_Create.sql
SELECT 'Private_SP 249'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCard_Delete.sql
SELECT 'Private_SP 250'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCard_Retrieve1.sql
SELECT 'Private_SP 251'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCard_RetrieveN.sql
SELECT 'Private_SP 252'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipCard_Update.sql
SELECT 'Private_SP 253'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIPCategory_Create.sql
SELECT 'Private_SP 254'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIPCategory_Delete.sql
SELECT 'Private_SP 255'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIPCategory_Retrieve1.sql
SELECT 'Private_SP 256'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIPCategory_RetrieveN.sql
SELECT 'Private_SP 257'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIPCategory_Update.sql
SELECT 'Private_SP 258'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipConsumeHistory_RetrieveN.sql
SELECT 'Private_SP 259'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipSource_Create.sql
SELECT 'Private_SP 260'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipSource_Delete.sql
SELECT 'Private_SP 261'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipSource_Retrieve1.sql
SELECT 'Private_SP 262'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VipSource_RetrieveN.sql
SELECT 'Private_SP 263'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_Create.sql
SELECT 'Private_SP 264'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_Delete.sql
SELECT 'Private_SP 265'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Vip_ResetBonus.sql
SELECT 'Private_SP 266'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_Retrieve1.sql
SELECT 'Private_SP 267'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_RetrieveN.sql
SELECT 'Private_SP 268'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_RetrieveNByMobileOrCardCode.sql
SELECT 'Private_SP 269'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_RetrieveNByMobileOrVipCardSN.sql
SELECT 'Private_SP 270'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Vip_RetrieveN_CheckUniqueField.sql
SELECT 'Private_SP 271'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_VIP_Update.sql
SELECT 'Private_SP 272'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Vip_UpdateBonus.sql
SELECT 'Private_SP 273'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehouse_Create.sql
SELECT 'Private_SP 274'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehouse_Delete.sql
SELECT 'Private_SP 275'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehouse_Retrieve1.sql
SELECT 'Private_SP 276'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehouse_RetrieveInventory.sql
SELECT 'Private_SP 277'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehouse_RetrieveN.sql
SELECT 'Private_SP 278'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehouse_Update.sql
SELECT 'Private_SP 279'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_WarehousingCommodity_Create.sql
SELECT 'Private_SP 280'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_WarehousingCommodity_Delete.sql
SELECT 'Private_SP 281'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_WarehousingCommodity_RetrieveN.sql
SELECT 'Private_SP 282'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_WarehousingCommodity_Update.sql
SELECT 'Private_SP 283'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_Approve.sql
SELECT 'Private_SP 284'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_Create.sql
SELECT 'Private_SP 285'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_Delete.sql
SELECT 'Private_SP 286'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_Retrieve1.sql
SELECT 'Private_SP 287'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_Retrieve1OrderID.sql
SELECT 'Private_SP 288'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_RetrieveN.sql
SELECT 'Private_SP 289'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_RetrieveNByFields.sql
SELECT 'Private_SP 290'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_Warehousing_Update.sql
SELECT 'Private_SP 291'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_WxUser_Create.sql
SELECT 'Private_SP 292'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/PrivateDB/SP_WxUser_Delete.sql





SELECT '--------------------------------------- PrivateDB SyncCache -------------------------------------------------------'$$
SELECT 'Priavtae_Sync_SP 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BarcodesSyncCacheDispatcher_RetriveN.sql
SELECT 'Priavtae_Sync_SP 2'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BarcodesSyncCacheDispatcher_UpdatePOSStatus.sql
SELECT 'Priavtae_Sync_SP 3'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BarcodesSyncCache_Delete.sql
SELECT 'Priavtae_Sync_SP 4'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BarcodesSyncCache_DeleteAll.sql
SELECT 'Priavtae_Sync_SP 5'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BarcodesSyncCache_POSUpload.sql
SELECT 'Priavtae_Sync_SP 6'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BarcodesSyncCache_RetrieveN.sql
SELECT 'Priavtae_Sync_SP 7'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BrandSyncCacheDispatcher_RetriveN.sql
SELECT 'Priavtae_Sync_SP 8'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BrandSyncCacheDispatcher_UpdatePOSStatus.sql
SELECT 'Priavtae_Sync_SP 9'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BrandSyncCache_Delete.sql
SELECT 'Priavtae_Sync_SP 10'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BrandSyncCache_DeleteAll.sql
SELECT 'Priavtae_Sync_SP 11'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BrandSyncCache_POSUpload.sql
SELECT 'Priavtae_Sync_SP 12'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_BrandSyncCache_RetrieveN.sql
SELECT 'Priavtae_Sync_SP 13'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CategorySyncCacheDispatcher_RetriveN.sql
SELECT 'Priavtae_Sync_SP 14'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CategorySyncCacheDispatcher_UpdatePOSStatus.sql
SELECT 'Priavtae_Sync_SP 15'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CategorySyncCache_Delete.sql
SELECT 'Priavtae_Sync_SP 16'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CategorySyncCache_DeleteAll.sql
SELECT 'Priavtae_Sync_SP 17'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CategorySyncCache_POSUpload.sql
SELECT 'Priavtae_Sync_SP 18'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CategorySyncCache_RetrieveN.sql
SELECT 'Priavtae_Sync_SP 19'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CommoditySyncCacheDispatcher_RetriveN.sql
SELECT 'Priavtae_Sync_SP 20'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CommoditySyncCacheDispatcher_UpdatePOSStatus.sql
SELECT 'Priavtae_Sync_SP 21'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CommoditySyncCache_Delete.sql
SELECT 'Priavtae_Sync_SP 22'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CommoditySyncCache_DeleteAll.sql
SELECT 'Priavtae_Sync_SP 23'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CommoditySyncCache_POSUpload.sql
SELECT 'Priavtae_Sync_SP 24'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_CommoditySyncCache_RetrieveN.sql
SELECT 'Priavtae_Sync_SP 25'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_POSSyncCacheDispatcher_RetriveN.sql
SELECT 'Priavtae_Sync_SP 26'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_POSSyncCacheDispatcher_UpdatePOSStatus.sql
SELECT 'Priavtae_Sync_SP 27'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_POSSyncCache_Delete.sql
SELECT 'Priavtae_Sync_SP 28'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_POSSyncCache_DeleteAll.sql
SELECT 'Priavtae_Sync_SP 29'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_POSSyncCache_POSUpload.sql
SELECT 'Priavtae_Sync_SP 30'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_POSSyncCache_RetrieveN.sql
SELECT 'Priavtae_Sync_SP 31'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_PromotionSyncCacheDispatcher_RetriveN.sql
SELECT 'Priavtae_Sync_SP 32'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_PromotionSyncCacheDispatcher_UpdatePOSStatus.sql
SELECT 'Priavtae_Sync_SP 33'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_PromotionSyncCache_Delete.sql
SELECT 'Priavtae_Sync_SP 34'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_PromotionSyncCache_DeleteAll.sql
SELECT 'Priavtae_Sync_SP 35'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_PromotionSyncCache_POSUpload.sql
SELECT 'Priavtae_Sync_SP 36'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/SP/SyncCache/SP_PromotionSyncCache_RetrieveN.sql


SELECT '--------------------------------------- PrivateDB Functions -------------------------------------------------------'$$
SELECT 'Function 1'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckCommodityDependency.sql
SELECT 'Function 2'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckPosDependency.sql
SELECT 'Function 3'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckPromotionDependency.sql
SELECT 'Function 4'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckProviderDependency.sql
SELECT 'Function 5'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckPurchasingOrderDependency.sql
SELECT 'Function 6'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckStaffDependency.sql
SELECT 'Function 7'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckVipDependency.sql
SELECT 'Function 8'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CheckWarehouseDependency.sql
SELECT 'Function 9'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CreateCommodityHistory.sql
SELECT 'Function 10'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_CreateRetailTradeCommoditySource.sql
SELECT 'Function 11'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_DeleteRetailTradeCommoditySource.sql
SELECT 'Function 12'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_DeleteWarehousingForReturnCommoditySheet.sql
SELECT 'Function 13'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_GenerateCouponSN.sql
SELECT 'Function 14'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_GenerateSN.sql
SELECT 'Function 15'$$
source D:/BXERP/branches/20220922_v2_10/nbr/src/sql/Function/Func_ValidateStateChange.sql


delimiter ;

select 'Stopped importing DB: nbr';