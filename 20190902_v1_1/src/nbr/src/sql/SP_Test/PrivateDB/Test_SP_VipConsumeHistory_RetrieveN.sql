SELECT '++++++++++++++++++ SP_VipConsumeHistory_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 用VipID查询零售单,大于起始值 -------------------------' AS 'Case1';

INSERT INTO t_vip (F_CardID, F_Mobile, F_ICID, F_Name, F_Email, F_ConsumeTimes, F_ConsumeAmount, F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime, F_CreateDatetime, F_UpdateDatetime)
VALUES (1, '123456789456', '3208031997070152153', 'ada', '627025169@qq.com', 5, 110, '广州', 1, now(), 500, now(), now(), now());
SET @iVipID = LAST_INSERT_ID();

INSERT INTO T_RetailTrade (F_VipID, F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_ShopID)
VALUES (@iVipID, 'LS2019090412300300031220', 11,2,'url=ashasouuuuunalskd','2017-8-10',5,0,'A123460',1,'双击777',2);
SET @rtID = Last_insert_id();

INSERT INTO T_RetailTrade (F_VipID, F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_ShopID)
VALUES (@iVipID, 'LS2019090412300300031221', 12,2,'url=ashasouuuuunalskd','2017-8-10',5,0,'A123460',1,'双击777',2);
SET @rtID2 = Last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @bQuerySmallerThanStartID = 0;
SET @iStartRetailTradeIDInSQLite = 0;

CALL SP_VipConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iStartRetailTradeIDInSQLite, @bQuerySmallerThanStartID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 2 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 用VipID查询零售单,小于起始值 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iTotalRecord = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @bQuerySmallerThanStartID = 1;
SET @iStartRetailTradeIDInSQLite = @rtID2;

CALL SP_VipConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iStartRetailTradeIDInSQLite, @bQuerySmallerThanStartID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iTotalRecord = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_retailtrade WHERE F_ID = @rtID;
DELETE FROM t_retailtrade WHERE F_ID = @rtID2;
DELETE FROM t_vip WHERE F_ID =  @iVipID;

SELECT '-------------------- Case3: 错误的VipID查询 -------------------------' AS 'Case3';
SET @iVipID = -2;
SET @sErrorMsg = '';
SET @iErrorCode = 0;
SET @iTotalRecord = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @bQuerySmallerThanStartID = 1;
SET @iStartRetailTradeIDInSQLite = 0;

CALL SP_VipConsumeHistory_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iStartRetailTradeIDInSQLite, @bQuerySmallerThanStartID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 2, '测试成功', '测试失败') AS 'Case3 Testing Result';