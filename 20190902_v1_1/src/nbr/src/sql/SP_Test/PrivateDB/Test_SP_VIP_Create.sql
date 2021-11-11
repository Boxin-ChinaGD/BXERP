-- CASE1:正常添加iIDInPOS为-1
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @sMobile = "17352640123";
SET @iPOS_SN = NULL;
SET @sICID = "3208031949754261782";
SET @sName = "TOM4";
SET @sEmail = "627025a169@QQ.COM";
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = "广州";
SET @iCategory = 5;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = -1;

CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);
SELECT @iErrorCode;

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = LAST_INSERT_ID() ;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();


-- CASE2:ICID重复 返回7
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @iIDInPOS = -1;
SET @sMobile = '12345612345';
SET @iPOS_SN = NULL;
SET @sICID = '320803199707016031';
SET @sName = 'TOM';
SET @sEmail = '627025169@QQ.COM';
SET @sAccount = 'V1Q2124';
SET @Password = '123456';
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = '广州';
SET @iCategory = 1;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = -1;

CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = LAST_INSERT_ID();
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE2 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();



-- CASE5:Email重复 返回7
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @iIDInPOS = -1;
SET @iPOS_SN = NULL;
SET @sICID = '320803199754261782';
SET @sName = 'TOM';
SET @sEmail = '123456@bx.vip';
SET @sAccount = 'V1Q2124';
SET @Password = '123456';
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = '广州';
SET @iCategory = 1;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = -1;

CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = LAST_INSERT_ID() ;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'CASE5 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();


-- CASE8:当iIDInPOS不为-1的时候
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @iIDInPOS = 1;
SET @sMobile = '17352640123';
SET @iPOS_SN = NULL;
SET @sICID = '3208031949754261782';
SET @sName = 'TOM4';
SET @sEmail = '627025a169@QQ.COM';
SET @sAccount = 'V1Q2a124';
SET @Password = '123a456';
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = '广州';
SET @iCategory = 5;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = -1;

CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);
SELECT @iErrorCode AS 'CASE8 @iErrorCode';

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = LAST_INSERT_ID() ;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE8 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();

-- CASE9：用不存在的VipCategory创建
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @iIDInPOS = -1;
SET @sMobile = '17352640123';
SET @iPOS_SN = NULL;
SET @sICID = '3208031949754261782';
SET @sName = 'TOM4';
SET @sEmail = '627025a169@QQ.COM';
SET @sAccount = 'V1Q2a124';
SET @Password = '123a456';
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = '广州';
SET @iCategory = -999;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = -1;

CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'CASE9 Testing Result';


SELECT  '---------------- case 10 创建会员时，初始积分为积分规则的初始设置积分 ------------------------------------' AS 'Case10';
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @sMobile = "17352640123";
SET @iPOS_SN = NULL;
SET @sICID = "3208031949754261782";
SET @sName = "TOM4";
SET @sEmail = "627025a169@QQ.COM";
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = "广州";
SET @iCategory = 5;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = -1;
-- 
CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);
SET @ID = LAST_INSERT_ID();
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_vip WHERE F_ID = @ID AND F_Bonus = (SELECT F_InitIncreaseBonus FROM t_bonusrule LIMIT 0, 1);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE10 Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();

SELECT  '---------------- case 11 导入商家数据进行创建会员时，初始积分为导入的积分 ------------------------------------' AS 'Case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sSN = '';
SET @sCardID = 1;
SET @sMobile = "17352640123";
SET @iPOS_SN = NULL;
SET @sICID = "3208031949754261782";
SET @sName = "TOM4";
SET @sEmail = "627025a169@QQ.COM";
SET @iConsumeTimes = 6;
SET @fConsumeAmount = 45.6;
SET @sDistrict = "广州";
SET @iCategory = 5;
SET @dtBirthday = now();
SET @dtLastConsumeDatetime = now();
SET @iSex = 1;
SET @sLogo = '123123123123';
SET @dtCreateDatetime = now();
SET @iBonusImported = 19;
-- 
CALL SP_VIP_Create(@iErrorCode, @sErrorMsg, @sSN,@sCardID,@sMobile, @iPOS_SN, @iSex, @sLogo, @sICID, @sName, @sEmail, @iConsumeTimes, @fConsumeAmount,
@sDistrict, @iCategory, @dtBirthday, @dtLastConsumeDatetime, @dtCreateDatetime, @iBonusImported);
SET @ID = LAST_INSERT_ID();
SELECT 1 FROM t_vip WHERE F_ID = @ID AND F_Bonus = @iBonusImported;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE11 Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();