SELECT '++++++++++++++++++ Test_SP_Promotion_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 查询所有的活动 -------------------------' AS 'Case1';

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
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2:查询 未开始 的活动 -------------------------' AS 'Case2';
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
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case3:查询 查询进行中 的活动 -------------------------' AS 'Case3';
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
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '-------------------- Case4:查询 已经结束 的活动 -------------------------' AS 'Case4';
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
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;


SELECT '-------------------- Case5 :输入未定义的 @iInt1-------------------------' AS 'Case5';
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
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

SELECT '-------------------- Case6:查询 查询进行中还有将要进行的活动 -------------------------' AS 'Case6';
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
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case6 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '----------------------------------------case 7: 查询所有已删除的促销活动 ----------------------------------------' AS 'Case7';
SET @name = '查询已删除活动测试';
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
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case7 Testing Result';
DELETE FROM t_promotion WHERE F_ID=@iID;

SELECT '----------------------------------------case 8: 查询所有未删除的促销活动 ----------------------------------------' AS 'Case8';

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
SELECT IF(@iTotalRecord >= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case8 Testing Result';

SELECT '------- Case9:验证增加F_staffName字段结果的正确性,创建1个promotion和一个retailtradepromotingflow，F_Staff=2时，staffName应该是店员2号,并且RetailTradeNO等于1 ---------' AS 'Case9';

INSERT INTO t_promotion (F_SN,F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('CX201906050011','测试的全场满10-1', 0, 0, now(),  DATE_ADD(now(),INTERVAL 1 YEAR), 10, 1, 8, 0, 2, now(), now());

SET @promotionID = last_insert_id();

INSERT INTO t_retailtradepromotingflow (F_RetailTradePromotingID, F_PromotionID, F_ProcessFlow, F_CreateDatetime)
VALUES (2, @promotionID, '测试数据', now());

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

SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case9 Testing Result';


DELETE FROM t_retailtradepromotingflow WHERE F_ID = @retailtradepromotingflowID;
DELETE FROM t_promotion WHERE F_ID = @promotionID;

SELECT '-------------------- Case10: 查询所有的活动,在这些活动中进行按活动名称iName进行模糊搜索，iName设置为开始，代表查询有活动名称中有开始两个字的活动 -------------------------' AS 'Case10';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iInt1 = 0;
SET @iStatus = -1;
SET @sString1 = '开始';
SET @iPageIndex = 1;
SET @iPageSize = 20;
SET @iTotalRecord = 0;


CALL SP_Promotion_RetrieveN(@iErrorCode, @sErrorMsg, @iInt1, @iStatus, @sString1, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case10 Testing Result';


SELECT '---------------------case 11: 查询所有的活动，在这些活动中按照活动名称进行模糊查询.sName设置为不存在的。 ----------------------------------------' AS 'Case11';

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
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case11 Testing Result';

SELECT '---------------------case 12: 等于最小长度的促销单号查询促销(等于10位) ----------------------------------------' AS 'Case12';

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
SELECT IF(@iTotalRecord > 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case12 Testing Result';

SELECT '---------------------case 13: 小于最小长度的促销单号查询促销,期望的是查询不到数据(小于10位) ----------------------------------------' AS 'Case13';

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
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case13 Testing Result';

SELECT '---------------------case 14: 大于最大长度的促销单号查询促销,期望的是查询不到数据(大于20位) ----------------------------------------' AS 'Case14';

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
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';


SELECT '---------------------case 14: 等于最大长度的促销单号查询促销(等于20位) ----------------------------------------' AS 'Case14';

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
SELECT IF(@iTotalRecord = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case14 Testing Result';