SELECT '++++++++++++++++++Test_SP_VIP_RetrieveNByMobileOrVipCardSN.sql+++++++++++++++++++++++';

SELECT '-----------------CASE1: 输入5位数以上的手机号码精确搜索vip------------------' AS 'CASE1';
-- case1:输入5位数以上的手机号码精确搜索vip
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @sMobile = '135456';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-----------------CASE2: 输入5位数以下的手机号码精确搜索vip,没有数据------------------' AS 'CASE2';
-- case2:输入5位数以下的手机号码精确搜索vip
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @sMobile = '13545';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-----------------CASE3: 不输入值返回所有会员信息------------------' AS 'CASE3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '';
SET @sVipCardSN = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;

CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

SELECT '-------------------- CASE5:传递手机号码，查找此手机号码的会员 -------------------------' AS 'Case5';
SET @sMobile = '15022587951';
INSERT INTO T_VIP (F_ICID, F_CardID, F_SN, F_Name, F_Mobile, F_Email, F_ConsumeTimes, F_ConsumeAmount,F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime)
VALUES ('123222456'/*F_ICID*/, 1/*F_CardID*/, '123455677'/*F_SN*/,'gig1gs'/*F_Name*/, @sMobile/*F_Mobile*/, '12324536@bx.vip'/*F_Email*/, 5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/, '广州'/*F_District*/, 1/*F_Category*/,
'2017-08-06'/*F_Birthday*/, 0/*F_Bonus*/, '2017-08-08 23:59:10'/*F_LastConsumeDatetime*/);
SET @VIPID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = @VIPID;

SELECT '-------------------- CASE6:传递不存在的手机号码，查找数据为空 -------------------------' AS 'Case6';
SET @sMobile = '15022587951';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sVipCardSN = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';

SELECT '-------------------- CASE7:传递会员卡号号码，查找此卡号号码的会员 -------------------------' AS 'Case7';
INSERT INTO T_VIP (F_ICID, F_CardID, F_SN, F_Name, F_Mobile, F_Email, F_ConsumeTimes, F_ConsumeAmount,F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime)
VALUES ('1231222456'/*F_ICID*/, 1/*F_CardID*/, '12346516789'/*F_SN*/,'gi7ggs'/*F_Name*/, '1520710487645'/*F_Mobile*/, '123712456@bx.vip'/*F_Email*/, 5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/, '广州'/*F_District*/, 1/*F_Category*/,
'2017-08-06'/*F_Birthday*/, 0/*F_Bonus*/, '2017-08-08 23:59:10'/*F_LastConsumeDatetime*/);
SET @iVipID = last_insert_id();
-- 
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();
-- 
SET @sVipCardSN = '12342567879';
INSERT INTO T_VipCardCode (F_VipID, F_VipCardID, F_SN) VALUES (@iVipID, @iVipCardID, @sVipCardSN);
SET @VipCardCodeID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';
-- 

DELETE FROM T_VipCardCode WHERE F_ID = @VipCardCodeID;
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;
DELETE FROM t_vip WHERE F_ID = @iVipID;

SELECT '-------------------- CASE8:传递不存在的会员卡卡号号码，查找数据为空 -------------------------' AS 'Case8';
SET @sVipCardSN = '12342567879';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sMobile = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
-- 
CALL SP_VIP_RetrieveNByMobileOrVipCardSN(@iErrorCode, @sErrorMsg, @sVipCardSN, @sMobile,@iPageIndex,@iPageSize,@iTotalRecord);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Testing Result';