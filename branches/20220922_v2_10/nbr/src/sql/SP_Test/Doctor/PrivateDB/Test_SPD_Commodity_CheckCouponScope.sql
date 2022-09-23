SELECT '++++++++++++++++++ Test_SPD_Commodity_CheckCouponScope.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�������� -------------------------' AS 'Case1';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

-- 
SELECT '-------------------- Case2:�����Ʒ���Ż�ȯ��Χ������SPD���� -------------------------' AS 'Case2';
-- �½���ͨ��Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,1);
SET @iCommodityID = LAST_INSERT_ID();
-- 
-- �����Ż�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, 'δ���õ��Ż�ȯ', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- �����Ż�ȯ��Χ
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '�ǰͿ�AB123');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID ,'���ǵ�Ʒ�����ܲ����Ż�ȯ��Χ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case3:���װ��Ʒ���Ż�ȯ��Χ������SPD���� -------------------------' AS 'Case3';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'�ǰͿ�AB123','����123','��',4,'֧',4,4,'SP',1,
5,38,5.8,5.5,1,1,null,
3,30,'2019-01-14','20',0,0,'�ǰͿ�AB',0,2);
SET @iCommodityID = LAST_INSERT_ID();
-- 
-- �����Ż�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, 'δ���õ��Ż�ȯ', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- �����Ż�ȯ��Χ
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '�ǰͿ�AB123');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID ,'���ǵ�Ʒ�����ܲ����Ż�ȯ��Χ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;



SELECT '-------------------- Case4:������Ʒ���Ż�ȯ��Χ������SPD���� -------------------------' AS 'Case4';
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_LatestPricePurchase,F_PriceRetail,F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag, F_NO,F_Type)
VALUES (0,'��ͨ','���','��',4,NULL,4,4,'ZT',1,
0,38,5.8,5.5,1,1,null,
0,30,'2019-01-14','0',0,0,'��ͨ���',0,3);
SET @iCommodityID = LAST_INSERT_ID();
-- 
-- �����Ż�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, 'δ���õ��Ż�ȯ', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- �����Ż�ȯ��Χ
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '�ǰͿ�AB123');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID ,'���ǵ�Ʒ�����ܲ����Ż�ȯ��Χ') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;


SELECT '-------------------- Case5:�����Ż�ȯ��Χ����ͨ��Ʒ����ɾ��״̬�ģ�SPD���� -------------------------' AS 'Case5';
-- 
-- �½���ͨ��Ʒ
INSERT INTO t_commodity (F_Status, F_Name, F_ShortName, F_Specification, F_PackageUnitID, F_PurchasingUnit, F_BrandID, F_CategoryID, F_MnemonicCode, F_PricingType, F_LatestPricePurchase, F_PriceRetail, F_PriceVIP, F_PriceWholesale, F_CanChangePrice, F_RuleOfPoint, F_Picture, F_ShelfLife, F_ReturnDays, F_CreateDate, F_PurchaseFlag, F_RefCommodityID, F_RefCommodityMultiple, F_Tag, F_NO, F_Type, F_NOStart, F_PurchasingPriceStart, F_StartValueRemark, F_CreateDatetime, F_UpdateDatetime, F_PropertyValue1, F_PropertyValue2, F_PropertyValue3, F_PropertyValue4, F_CurrentWarehousingID)
VALUES (2/*F_Status*/, '�Ż�ȯ��ͨ��Ʒ', '��Ƭ', '��', 1, '��', 3, 1, 'SP', 1, 8, 12, 11.8, 11, 1, 1, NULL, 3, 30, '2018-04-14 01:00:01', 20, 0, 0, '1111111', 0, 0, -1, -1, NULL, '2020-04-20 08:57:14', '2020-04-20 08:57:14', NULL, NULL, NULL, NULL, NULL);
SET @iCommodityID = last_insert_id();
-- 
-- �����Ż�ȯ
INSERT INTO t_coupon (F_Status, F_Type, F_Bonus, F_LeastAmount, F_ReduceAmount, F_Discount, F_Title, F_Color, F_Description, F_PersonalLimit, F_WeekDayAvailable, F_BeginTime, F_EndTime, F_BeginDateTime, F_EndDateTime, F_Quantity, F_RemainingQuantity, F_Scope)
VALUES (0, 0, 0, 1, 1, 1, 'δ���õ��Ż�ȯ', 'Color010', '', 1, 0, '9:00:00', '12:00:00', '3333-03-03 15:15:15', '3333-03-20 15:15:15', 1000, 1000, 0);
SET @couponID = last_insert_id();
-- 
-- �����Ż�ȯ��Χ
INSERT INTO t_couponscope (F_CouponID, F_CommodityID, F_CommodityName)
VALUES (@couponID, @iCommodityID, '�Ż�ȯ��ͨ��Ʒ');
SET @couponscopeID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
-- 
CALL SPD_Commodity_CheckCouponScope(@iErrorCode, @sErrorMsg);
SELECT @iErrorCode, @sErrorMsg;
-- 
SELECT IF(@sErrorMsg = CONCAT('��Ʒ', @iCommodityID ,'�������Ż�ȯ��Χ, ��������ɾ��״̬(���ܱ�ɾ��)') AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- 
-- 
DELETE FROM t_couponscope WHERE F_ID = @couponscopeID;
DELETE FROM t_coupon WHERE F_ID = @couponID;
DELETE FROM t_commodity WHERE F_ID = @iCommodityID;