-- CASE1:�������iIDInPOSΪ-1
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
SET @sDistrict = "����";
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();


-- CASE2:ICID�ظ� ����7
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
SET @sDistrict = '����';
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
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();



-- CASE5:Email�ظ� ����7
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
SET @sDistrict = '����';
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
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE5 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();


-- CASE8:��iIDInPOS��Ϊ-1��ʱ��
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
SET @sDistrict = '����';
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE8 Testing Result';

DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();

-- CASE9���ò����ڵ�VipCategory����
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
SET @sDistrict = '����';
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
SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'CASE9 Testing Result';


SELECT  '---------------- case 10 ������Աʱ����ʼ����Ϊ���ֹ���ĳ�ʼ���û��� ------------------------------------' AS 'Case10';
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
SET @sDistrict = "����";
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE10 Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();

SELECT  '---------------- case 11 �����̼����ݽ��д�����Աʱ����ʼ����Ϊ����Ļ��� ------------------------------------' AS 'Case11';
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
SET @sDistrict = "����";
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE11 Testing Result';
-- 
DELETE FROM t_vip WHERE F_ID = LAST_INSERT_ID();