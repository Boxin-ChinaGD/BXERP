SELECT '++++++++++++++++++ Test_SP_Vip_ResetBonus.sql ++++++++++++++++++++';

SET @dtClearBonusDatetime = NULL;
SET @iClearBonusDay = 3650;

SELECT '-------------------- Case1:根据固定天数进行积分清零，会员创建时间到今天未达到清零天数，不清零 -------------------------' AS 'Case1';
SET @dtCreateDatetime = now();
SET @iBonus = 50;
-- 
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
-- 
CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case2:根据固定天数进行积分清零，会员创建时间到今天等于清零天数，积分清零 -------------------------' AS 'Case2';
UPDATE t_vipcard SET 
	F_ClearBonusDatetime = NULL,
	F_ClearBonusDay = 3
WHERE F_ID = 1;
-- 
SET @dtCreateDatetime = date_sub(now(),interval 3 day); -- 往前推3天
SET @iBonus = 50;
-- 
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SELECT * FROM t_VIP WHERE F_ID = @iID ;
-- 
CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);
-- 
SELECT @sErrorMsg;

SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;
-- 还原积分清零规则
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case3:根据固定天数进行积分清零，会员创建时间到今天超过清零天数但不能被整除，不清零 -------------------------' AS 'Case3';
UPDATE t_vipcard SET 
	F_ClearBonusDatetime = NULL,
	F_ClearBonusDay = 3
WHERE F_ID = 1;
--
SET @dtCreateDatetime = now() - 07000000; -- 往前推7天
SET @iBonus = 50;
--
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
-- 
CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = @iBonus;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;
-- 还原积分清零规则
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case4:根据固定日期进行积分清零，会员当年创建，达到清零日期，积分清零 -------------------------' AS 'Case4';
-- 修改会员卡积分清零规则为根据固定日期清零
UPDATE t_vipcard
SET F_ClearBonusDatetime = now(),
F_ClearBonusDay = NULL
WHERE F_ID = 1;

SET @dtCreateDatetime = now();
SET @iBonus = 50;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

-- 还原积分清零规则
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case5:根据固定日期进行积分清零，会员上一年创建，达到清零日期，积分清零 -------------------------' AS 'Case5';
-- 修改会员卡积分清零规则为根据固定日期清零
UPDATE t_vipcard
SET F_ClearBonusDatetime = now() - 010000000000, -- 往前推1年
F_ClearBonusDay = NULL
WHERE F_ID = 1;

SET @dtCreateDatetime = now();
SET @iBonus = 50;
INSERT INTO T_VIP (F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime, F_Mobile, F_CreateDatetime)
VALUES ('1232456',1,'giggs','1232456@bx.vip',5,99.8,'广州',1,
'2017-08-06',@iBonus,'2017-08-08 23:59:10','12345612312', @dtCreateDatetime);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_Bonus = 0;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

-- 还原积分清零规则
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;

SELECT '-------------------- Case6:会员卡积分清零规则异常 -------------------------' AS 'Case6';
-- 修改会员卡积分清零规则为根据固定日期清零
UPDATE t_vipcard
SET F_ClearBonusDatetime = NULL,
F_ClearBonusDay = NULL
WHERE F_ID = 1;

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_Vip_ResetBonus(@iErrorCode, @sErrorMsg);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '会员卡积分清零规则异常', '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

-- 还原积分清零规则
UPDATE t_vipcard
SET F_ClearBonusDatetime = @dtClearBonusDatetime,
F_ClearBonusDay = @iClearBonusDay
WHERE F_ID = 1;