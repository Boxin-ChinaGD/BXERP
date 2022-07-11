SELECT '++++++++++++++++++ Test_SP_Vip_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常修改 -------------------------' AS 'Case1';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,
F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999','1234256',1,'giggs','1234526@bx.vip',5,99.8,'广州',1,
'2017-08-06',44.9,'2017-08-08 23:59:10','12313212332');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @sDistrict = '广州';
SET @iCategory = 1;
SET @dBirthday = '2020/02/11';


CALL SP_VIP_Update(@iErrorCode, @sErrorMsg, @iID, @sDistrict, @iCategory, @dBirthday);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_District = @sDistrict AND F_Category = @iCategory AND F_Birthday = @dBirthday;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;

SELECT '-------------------- Case2:输入一个不存在的Id进行修改 -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -22;
SET @sDistrict = "广州";
SET @iCategory = 1;
SET @dBirthday = '2020/02/11';

CALL SP_VIP_Update(@iErrorCode, @sErrorMsg, @iID, @sDistrict, @iCategory, @dBirthday);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:对生日进行修改 -------------------------' AS 'Case3';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999'/*F_SN*/,'1234256'/*F_ICID*/,1/*F_CardID*/,'giggs'/*F_Name*/,'1234526@bx.vip'/*F_Email*/,5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/,'广州'/*F_District*/,
		1/*F_Category*/,'2017-08-06'/*F_Birthday*/,44.9/*F_Bonus*/,'2017-08-08 23:59:10'/*F_LastConsumeDatetime*/,'12313212332'/*F_Mobile*/);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();
SET @sDistrict = '广州';
SET @iCategory = 1;
SET @dBirthday = '2020/02/11';


CALL SP_VIP_Update(@iErrorCode, @sErrorMsg, @iID, @sDistrict, @iCategory, @dBirthday);

SELECT @sErrorMsg;
SELECT 1 FROM t_VIP WHERE F_ID = @iID AND F_District = @sDistrict AND F_Category = @iCategory AND F_Birthday = @dBirthday;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_vip WHERE F_ID = @iID;
