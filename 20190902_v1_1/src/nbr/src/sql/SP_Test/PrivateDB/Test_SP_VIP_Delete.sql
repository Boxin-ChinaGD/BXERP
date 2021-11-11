SELECT '++++++++++++++++++ Test_SP_Vip_Delete ++++++++++++++++++++';

SELECT '-------------------- Case1:删除没有积分没有消费过的会员 -------------------------' AS 'Case1';
INSERT INTO T_VIP (F_SN,F_ICID, F_CardID, F_Name, F_Email, F_ConsumeTimes, F_ConsumeAmount,
F_District, F_Category, F_Birthday, F_Bonus, F_LastConsumeDatetime)
VALUES ('1111','12322456', 1, 'giggs', '1232456@bx.vip', 5,99.8, '广州', 1,
'2017-08-06', 44.9, '2017-08-08 23:59:10');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAST_INSERT_ID();

CALL SP_VIP_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT @iErrorCode;
SELECT @sErrorMsg;

SELECT 1 FROM t_vip WHERE F_ID = @iID;
SELECT IF(FOUND_ROWS() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'CASE1 Testing Result';