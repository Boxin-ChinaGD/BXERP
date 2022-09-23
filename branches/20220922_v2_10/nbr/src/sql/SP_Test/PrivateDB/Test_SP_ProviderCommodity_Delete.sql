SELECT '++++++++++++++++++ Test_SP_ProviderCommodity_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ���빩Ӧ��ID����ƷID��ʱ��ɾ����Ӧ�Ĺ�Ӧ����Ʒ -------------------------' AS 'Case1';

INSERT INTO nbr.t_provider (F_Name, F_DistrictID, F_Address, F_ContactName, F_Mobile)
VALUES ('̨���ŷ�ʳƷ���޹�˾333', 3, '̨��33', 'angel33', '13129355772');
SET @iProviderID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 1;


INSERT INTO t_providercommodity (F_CommodityID, F_ProviderID)
VALUES (@iCommodityID, @iProviderID);

CALL SP_ProviderCommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

SELECT '-------------------- Case2: ֻ������ƷID��ɾ�����������Ʒ��صĹ�Ӧ����Ʒ -------------------------' AS 'Case2';
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0);
SET @iCommodityID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = @iCommodityID;
SET @iProviderID = 0;

CALL SP_ProviderCommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- Case3:��������ƷID�͹�Ӧ��ID����ɾ��-------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iCommodityID = 0;
SET @iProviderID = 0;

CALL SP_ProviderCommodity_Delete(@iErrorCode, @sErrorMsg, @iCommodityID, @iProviderID);

SELECT @sErrorMsg;
SELECT 1 FROM t_providercommodity WHERE F_CommodityID = @iCommodityID AND F_ProviderID = @iProviderID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE3 Testing Result';