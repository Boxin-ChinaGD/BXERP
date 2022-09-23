SELECT '++++++++++++++++++ Test_SP_Promotion_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ��ѯ���еĻ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:��ѯ δ��ʼ �Ļ -------------------------' AS 'Case2';
SET @name = 'asd';
SET @status = 0;
SET @type = 0;
SET @datetimestart = DATE_ADD(now(),INTERVAL 1 DAY) ;
SET @datetimeend = DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050006',@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 10;
SET @iStatus = 0;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case3:��ѯ ��ѯ������ �Ļ -------------------------' AS 'Case3';
SET @name = 'asd';
SET @status = 0;
SET @type = 0;
SET @datetimestart = '2008-10-4';
SET @datetimeend =DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050007',@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 11;
SET @iStatus = 0;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord = 0;

CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case4:��ѯ �Ѿ����� �Ļ -------------------------' AS 'Case4';
SET @name = 'asd';
SET @status = 0;
SET @type = 0;
SET @datetimestart = '2008-8-8';
SET @datetimeend ='2009-5-4';
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050008',@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 12;
SET @iStatus = 0;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '-------------------- Case5 :����δ����� @iInt1-------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 99;
SET @iStatus = 0;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord = 0;

CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';

SELECT '-------------------- Case6:��ѯ ��ѯ�����л��н�Ҫ���еĻ -------------------------' AS 'Case6';
SET @name = 'asd1';
SET @status = 0;
SET @type = 0;
SET @datetimestart = '2008-10-4';
SET @datetimeend =DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050009',@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 13;
SET @iStatus = 0;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord = 0;

CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case6 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '----------------------------------------case 7: ��ѯ������ɾ���Ĵ���� ----------------------------------------' AS 'Case7';
SET @name = '��ѯ��ɾ�������';
SET @status = 1;
SET @type = 0;
SET @datetimestart = '2008-10-4';
SET @datetimeend =DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050010',@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = 1;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;

CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case7 Testing Result';
DELETE FROM t_promotion WHERE F_ID=@iID;

SELECT '----------------------------------------case 8: ��ѯ����δɾ���Ĵ���� ----------------------------------------' AS 'Case8';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = 0;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;

CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case8 Testing Result';

SELECT '------- Case9:��֤����F_staffName�ֶν������ȷ��,����1��promotion��һ��retailtradepromotingflow��F_Staff=2ʱ��staffNameӦ���ǵ�Ա2��,����RetailTradeNO����1 ---------' AS 'Case9';

INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050011','���Ե�ȫ����10-1', 0, 0, now(),  DATE_ADD(now(),INTERVAL 1 YEAR), 10, 1, 8, 0, 2, now(), now());

SET @promotionID = last_insert_id();

INSERT INTO t_retailtradepromotingflow (F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime)
VALUES (2, @promotionID, '��������', now());

SET @retailtradepromotingflowID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = '';
SET @iPageIndex = 1;
SET @iPageSize = 5;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;

SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case9 Testing Result';


DELETE FROM t_retailtradepromotingflow WHERE F_ID = @retailtradepromotingflowID;
DELETE FROM t_promotion WHERE F_ID = @promotionID;

SELECT '-------------------- Case10: ��ѯ���еĻ,����Щ��н��а������iName����ģ��������iName����Ϊ��ʼ�������ѯ�л�������п�ʼ�����ֵĻ -------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = '��ʼ';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case10 Testing Result';


SELECT '---------------------case 11: ��ѯ���еĻ������Щ��а��ջ���ƽ���ģ����ѯ.sName����Ϊ�����ڵġ� ----------------------------------------' AS 'Case11';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = '-999999';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case11 Testing Result';

SELECT '---------------------case 12: ������С���ȵĴ������Ų�ѯ����(����10λ) ----------------------------------------' AS 'Case12';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = 'CX20190605';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord > 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case12 Testing Result';

SELECT '---------------------case 13: С����С���ȵĴ������Ų�ѯ����,�������ǲ�ѯ��������(С��10λ) ----------------------------------------' AS 'Case13';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = 'CX2019060';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case13 Testing Result';

SELECT '---------------------case 14: ������󳤶ȵĴ������Ų�ѯ����,�������ǲ�ѯ��������(����20λ) ----------------------------------------' AS 'Case14';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = 'CX20190605123451234512345';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';


SELECT '---------------------case 14: ������󳤶ȵĴ������Ų�ѯ����(����20λ) ----------------------------------------' AS 'Case14';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = 'CX201906051234512345';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case14 Testing Result';