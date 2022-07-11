SELECT '++++++++++++++++++ Test_SP_VipSource_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常添加 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 2;
SET @iSourceCode = 0;
SET @sID1 = '1111111111111';
SET @sID2 = '2222222222222';
SET @sID3 = '3333333333333';
-- 
CALL SP_VipSource_Create(@iErrorCode, @sErrorMsg, @iVipID, @iSourceCode, @sID1, @sID2, @sID3);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipsource 
WHERE F_VipID = @iVipID 
AND F_SourceCode = @iSourceCode
AND F_ID1 = @sID1
AND F_ID2 = @sID2
AND F_ID3 = @sID3;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
--
DELETE FROM t_vipsource WHERE F_ID = LAST_INSERT_ID();

SELECT '-------------------- Case2:会员ID不存在 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = -1;
SET @iSourceCode = 0;
SET @sID1 = '1111111111111';
SET @sID2 = '2222222222222';
SET @sID3 = '3333333333333';
-- 
CALL SP_VipSource_Create(@iErrorCode, @sErrorMsg, @iVipID, @iSourceCode, @sID1, @sID2, @sID3);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipsource 
WHERE F_VipID = @iVipID 
AND F_SourceCode = @iSourceCode
AND F_ID1 = @sID1
AND F_ID2 = @sID2
AND F_ID3 = @sID3;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2 AND @sErrorMsg = '该会员不存在', '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:重复添加,错误码0，返回数据库对象 -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = 2;
SET @iSourceCode = 0;
SET @sID1 = '1111111111111';
SET @sID2 = '2222222222222';
SET @sID3 = '3333333333333';
-- 
CALL SP_VipSource_Create(@iErrorCode, @sErrorMsg, @iVipID, @iSourceCode, @sID1, @sID2, @sID3);
-- 重复添加
CALL SP_VipSource_Create(@iErrorCode, @sErrorMsg, @iVipID, @iSourceCode, @sID1, @sID2, @sID3);
SELECT @sErrorMsg;
SELECT 1 FROM t_vipsource 
WHERE F_VipID = @iVipID 
AND F_SourceCode = @iSourceCode
AND F_ID1 = @sID1
AND F_ID2 = @sID2
AND F_ID3 = @sID3;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';
--
DELETE FROM t_vipsource WHERE F_ID = LAST_INSERT_ID();



SELECT '-------------------- Case4:第一次创建会员来源,ID1、ID2、ID3都传空串，第二次创建ID1、ID2、ID3不传空串(模拟在nbr创建会员，然后在小程序登录) -------------------------' AS 'Case4';
-- 
INSERT INTO t_vip (F_SN, F_CardID, F_Mobile, F_LocalPosSN, F_Sex, F_Logo, F_ICID, F_Name, F_Email, F_ConsumeTimes, F_ConsumeAmount, F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime, F_Remark, F_CreateDatetime, F_UpdateDatetime)
VALUES ('VIP800014'/*F_SN*/, 1/*F_CardID*/, '18213665954'/*F_Mobile*/, '', 0, '123456123456', '380803199707015954'/*F_ICID*/, 'Tomqhjrjz', '3866082@bx.vip'/*F_Email*/, 0, 0, '广州', 1/*F_Category*/, '2020-03-26', 0, '2020-03-26 17:07:46', NULL, '2020-03-26 17:07:46', '2020-03-26 17:07:46');
SET @vipID = last_insert_id();

-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = @vipID;
SET @iSourceCode = 0;
SET @sID1 = '';
SET @sID2 = '';
SET @sID3 = '';
-- 
CALL SP_VipSource_Create(@iErrorCode, @sErrorMsg, @iVipID, @iSourceCode, @sID1, @sID2, @sID3);
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
SET @ID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iVipID = @vipID;
SET @iSourceCode = 0;
SET @sID1 = '1111111111111';
SET @sID2 = '2222222222222';
SET @sID3 = '3333333333333';
-- 
CALL SP_VipSource_Create(@iErrorCode, @sErrorMsg, @iVipID, @iSourceCode, @sID1, @sID2, @sID3);
-- 验证是否是update
SET @number = 0;
SELECT count(1) INTO @number FROM t_vipsource WHERE F_VipID = @iVipID;
-- 
SELECT IF(@number = 1 and @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';
DELETE FROM t_vipsource WHERE F_ID = @ID;
DELETE FROM t_vip WHERE F_ID = @vipID;