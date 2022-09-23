SELECT '++++++++++++++++++ Test_SP_Coupon_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iStatus = 0;
SET @iType = 0;
SET @iBonus = 0;
SET @dLeastAmount = 0;
SET @dReduceAmount = 10;
SET @dDiscount = 1;
SET @sTitle = '�ֽ�ȯ';
SET @sColor = 'xxxxxxxxxxxx';
SET @sDescription = 'ʹ��˵��';
SET @iPersonalLimit = 1;
SET @iWeekDayAvailable = 0;
SET @sBeginTime = '00:00:00';
SET @sEndTime = '23:59:59';
SET @dtBeginDateTime = '1970/01/01';
SET @dtEndDateTime = '2030/01/01';
SET @iQuantity = 1000;
SET @iRemainingQuantity = 1000;
SET @iScope = 0;
-- 
CALL SP_Coupon_Create(@iErrorCode, @sErrorMsg, @iStatus, @iType, @iBonus, @dLeastAmount, @dReduceAmount, @dDiscount, @sTitle, @sColor, @sDescription, @iPersonalLimit, 
@iWeekDayAvailable, @sBeginTime, @sEndTime, @dtBeginDateTime, @dtEndDateTime, @iQuantity, @iRemainingQuantity, @iScope);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_coupon WHERE F_Title = @sTitle AND F_Color = @sColor;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_coupon WHERE F_ID = LAST_INSERT_ID();