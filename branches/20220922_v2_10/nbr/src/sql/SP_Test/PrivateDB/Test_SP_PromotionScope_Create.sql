SELECT '++++++++++++++++++ Test_SP_PromotionScope_Create.sql ++++++++++++++++++++';
SELECT '------------------- Case1 �������� ------------------' as 'Case 1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_promotionScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case2 �ò����ڵ�CommodityID���� ------------------' as 'Case 2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = -999;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_PromotionID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '����ʹ�ò����ڵ�CommodityID���д���', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case3 �ò����ڵ�PromotionID���� ------------------' as 'Case 3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
SET @iPromotionID = -969;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

SELECT '------------------- Case4 ������Ʒ������� ------------------' as 'Case 4';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��ʼ��ȫ����10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',3);
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_ID = last_insert_id(); 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------- Case5:����һ��������Χ,�������� ------------------' as 'Case 5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-2';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_promotionScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case6:����һ��������Χ������ɾ�� ------------------' as 'Case 6';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-2';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

DELETE FROM t_promotion WHERE F_ID = @iID;

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionScope WHERE F_ID = last_insert_id();
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '------------------- Case7:����һ��������Χ,�������� ------------------' as 'Case 7';
SET @iErrorCode = 0;
SET @iPromotionID = 99999;
SET @iCommodityID = 1;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';


SELECT '------------------- Case8:����һ��������Χ,��Ʒ����Ϊ���װ��Ʒ ------------------' as 'Case 8';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��ʼ��ȫ����10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',2/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '------------------- Case9:����һ��������Χ,��Ʒ����Ϊ�����Ʒ ------------------' as 'Case 9';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��ʼ��ȫ����10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',1/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';
-- 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;

SELECT '------------------- Case10:����һ������,��ָ����Ʒ�а����ظ�����Ʒ ------------------' as 'Case 10';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��ʼ��ȫ����10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'֩����1��aaa','��Ƭ','��',1,'��',3,1,'SP',1,
11.8,11,1,1,'url=116843434834',
3,'15',now(),'20',0,0,'.................',0/*F_Type*/);
SET @iCommodityID = last_insert_id();
-- 
INSERT INTO t_promotionscope (F_PromotionID, F_CommodityID, F_CommodityName)
VALUES (@iPromotionID, @iCommodityID, '֩����1��aaa');
SET @iPromotionscopeID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iCommodityID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';
-- 
DELETE FROM t_promotionscope WHERE F_ID = @iPromotionscopeID; 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;