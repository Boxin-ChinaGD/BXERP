SELECT '++++++++++++++++++ Test_SP_WarehousingCommodity_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: �������״̬�µ���ͨ��Ʒ����ⵥ��Ʒ -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeID = last_insert_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iNO = 300;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'case1 Testing Result';

SELECT @iErrorCode;
SELECT @sErrorMsg;

DELETE FROM t_warehousingcommodity WHERE F_ID = last_insert_id();
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- case2:����һ��commodityID�����ڵ������Ʒ -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = 999999999;
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@sErrorMsg = '�������һ�������ڵ���Ʒ' AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'case2 Testing Result';

SELECT '-------------------- case3:����һ��warehousing�в����ڵ�WarehousingID�����Ʒ-------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 999999999;
SET @iCommodityID = 2;
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);

SELECT IF(@sErrorMsg = '����ⵥ������' AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'case3 Testing Result';

SELECT '-------------------- case4:����һ��iBarcodeID�����ڵ������Ʒ-------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = 6;
SET @iNO = 300;
SET @iBarcodeID = 999999999;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '��ⵥ��Ʒ�������벻����', '���Գɹ�', '����ʧ��') AS 'case4 Testing Result';

SELECT '-------------------- case5:�������Ʒ������һ����ⵥ��Ʒ-------------------------' AS 'Case5';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '��Ը����ζ6670', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111',1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '������ӷǵ�Ʒ����Ʒ����ⵥ', '���Գɹ�', '����ʧ��') AS 'case5 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '��Ը����ζ6670';

SELECT '-------------------- case6:����ɾ���������Ʒ������һ����ⵥ��Ʒ-------------------------' AS 'Case6';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '��Ը����ζ6671', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 1);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '���������ɾ������Ʒ����ⵥ', '���Գɹ�', '����ʧ��') AS 'case6 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '��Ը����ζ6671';

SELECT '-------------------- case7:����ɾ������Ʒ������һ����ⵥ��Ʒ-------------------------' AS 'Case7';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '��Ը����ζ6672', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '���������ɾ������Ʒ����ⵥ', '���Գɹ�', '����ʧ��') AS 'case7 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '��Ը����ζ6672';

SELECT '-------------------- case8:����ɾ���Ķ��װ��Ʒ������һ����ⵥ��Ʒ-------------------------' AS 'Case8';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (2, '��Ը����ζ6673', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 2 AND @sErrorMsg = '���������ɾ������Ʒ����ⵥ', '���Գɹ�', '����ʧ��') AS 'case8 Testing Result';

DELETE FROM t_commodity WHERE F_Name = '��Ը����ζ6673';

SELECT '-------------------- case9:�ö��װ��Ʒ������һ����ⵥ��Ʒ-------------------------' AS 'Case9';
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, 
F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, 
F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_Type)
VALUES (0, '��Ը����ζ6675', '��Ƭ', '��', 1, '��', 3, 3, 'SP', 1, 
11.8, 11, 1, 1, 'url=116843434834', 
3, 30, '2018-04-14', 20, 0, 0, '1111111', 2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = last_insert_id();
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;
	
CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '������ӷǵ�Ʒ����Ʒ����ⵥ' , '���Գɹ�', '����ʧ��') AS 'case9 Testing Result';

DELETE FROM t_WarehousingCommodity WHERE F_CommodityID = @iCommodityID AND F_WarehousingID = @iWarehousingID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '-------------------- case10:����һ����ⵥ��Ʒ����������ͬ����Ʒ-------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iCommodityID = 2;
SET @iNO = 300;
SET @iBarcodeID = 1;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);


SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'case10 Testing Result';


SELECT '-------------------- Case11: �������ID����Ʒ��ID����Ӧ -------------------------' AS 'Case1';

INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag)
VALUES (1,'����','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
'3��','15',now(),'20',0,0,'.................');
SET @iCommodityID = last_insert_id();

INSERT INTO t_barcodes (F_CommodityID, F_Barcode)
VALUES (@iCommodityID, '123456789');
SET @iBarcodeIDs = last_insert_ID();

SET @iBarcodeID = 1;

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 1;
SET @iNO = 300;
SET @iPrice = 10;
SET @iAmount = 3000;
SET @iShelfLife = 22;

CALL SP_WarehousingCommodity_Create(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iNO, @iBarcodeID, @iPrice, @iAmount, @iShelfLife);
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '������ID����Ʒʵ��������ID����Ӧ', '���Գɹ�', '����ʧ��') AS 'case11 Testing Result';

SELECT @iErrorCode;
SELECT @sErrorMsg;

DELETE FROM t_warehousingcommodity WHERE F_ID = last_insert_id();
DELETE FROM t_barcodes WHERE F_ID = @iBarcodeIDs;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;