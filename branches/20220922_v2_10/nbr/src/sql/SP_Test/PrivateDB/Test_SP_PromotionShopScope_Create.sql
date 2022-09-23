SELECT '++++++++++++++++++ Test_SP_PromotionShopScope_Create.sql ++++++++++++++++++++';
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
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = 2;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_promotionShopScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case2 �ò����ڵ�ShopID���� ------------------' as 'Case 2';
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
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = -999;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_PromotionID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7 AND @sErrorMsg = '����ʹ�ò����ڵ�shopID���д���', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

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
SET @iShopID = 1;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';


SELECT '------------------- Case4:����һ��������Χ,�������� ------------------' as 'Case 4';
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
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = 1;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_ID = last_insert_id();
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';

DELETE FROM t_promotionShopScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------- Case5:����һ��������Χ������ɾ�� ------------------' as 'Case 5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '��10-2';
SET @status = 1;
SET @type = 1;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 1;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @shopScope = 1;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime, F_ShopScope)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now(), @shopScope);
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @iPromotionID = @iID;
SET @iShopID = 2;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_PromotionShopScope WHERE F_ID = last_insert_id();
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
DELETE FROM t_promotionShopScope WHERE F_ID = LAST_INSERT_ID();
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '------------------- Case6:����һ��������Χ,�������� ------------------' as 'Case 6';
SET @iErrorCode = 0;
SET @iPromotionID = 99999;
SET @iShopID = 1;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';



SELECT '------------------- Case7:����һ������,��ָ����Ʒ�а����ظ����ŵ� ------------------' as 'Case 10';
INSERT INTO t_promotion (F_Name, F_SN, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('��ʼ��ȫ����10-1x', 'CX201906050001x',0, 0, now(),  DATE_ADD(now(),INTERVAL 7 DAY), 10, 1, 8, 0, 2, now(), now());
SET @iPromotionID = last_insert_id();
SET @iShopID = 2;
-- 
INSERT INTO t_promotionShopScope (F_PromotionID, F_ShopID)
VALUES (@iPromotionID, @iShopID);
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
SET @shopScope = 1;
SET @staff = 1 ;
SET @iErrorCode = 0;
CALL SP_PromotionShopScope_Create(@iErrorCode, @sErrorMsg, @iPromotionID, @iShopID);
-- 
SELECT @sErrorMsg;
SELECT IF( @iErrorCode = 1, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
-- 
DELETE FROM t_promotionShopScope WHERE F_ID = @iPromotionscopeID; 
DELETE FROM t_promotion WHERE F_ID = @iPromotionID;
