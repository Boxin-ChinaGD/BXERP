SELECT '++++++++++++++++++ Test_SP_Promotion_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:û������������ɾ��-------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'asd';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

CALL SP_Promotion_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_promotion WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '-------------------- Case2:��������Ҳ��ɾ��,�������������ʱ��ֹ-------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'asd';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

INSERT INTO t_retailtradepromotingflow (F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime)
VALUES (1, @iID, 'aa', now());
SET @iID2 = LAST_INSERT_ID();

CALL SP_Promotion_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_promotion WHERE F_ID = @iID AND F_Status = 1;
SELECT IF(@iErrorCode = 0 AND FOUND_ROWS() = 1, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

DELETE FROM t_retailtradepromotingflow WHERE F_ID = @iID2;
DELETE FROM t_promotion WHERE F_ID = @iID;

--	SELECT '-------------------- Case3:û������������ɾ������֤�ӱ��Ƿ�ɾ���ɹ�-------------------------' AS 'Case3';
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	SET @name = 'asd';
--	SET @status = 0;
--	SET @type = 0;
--	SET @datetimestart = now();
--	SET @datetimeend = now();
--	SET @excecutionthreshold = 10;
--	SET @excecutionamount = 3;
--	SET @excecutiondiscount = 1;
--	SET @scope = 0;
--	SET @staff = 1 ;
--	-- ��������
--	INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
--	VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
--	SET @iPromotionID = last_insert_id();
--	-- �����ӱ�
--	INSERT INTO nbr.t_promotionscope (F_PromotionID, F_CommodityID)
--	VALUES (@iPromotionID, 1);
--	-- 
--	CALL SP_Promotion_Delete(@iErrorCode, @sErrorMsg, @iPromotionID);
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	-- ������֤������û��ɾ���ɹ�
--	SELECT 1 FROM t_promotion WHERE F_ID = @iPromotionID AND F_Status = 1;
--	SET @Promotion = FOUND_ROWS();
--	-- ������֤������Χ��û��ɾ���ɹ�
--	SELECT 1 FROM t_promotionscope WHERE F_PromotionID = @iPromotionID;
--	SET @PromotionScope = FOUND_ROWS();
--	-- 
--	SELECT IF(@iErrorCode = 0 AND @Promotion = 1 AND @PromotionScope = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
--	DELETE FROM t_promotion WHERE F_ID = @iPromotionID;

SELECT '-------------------- Case4:�ظ�ɾ��-------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = 'asd';
SET @status = 1;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

CALL SP_Promotion_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�ô�����ɾ���������ظ�ɾ��', '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;