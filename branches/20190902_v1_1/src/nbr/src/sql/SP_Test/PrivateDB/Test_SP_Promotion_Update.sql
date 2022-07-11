SELECT '+++++++++++++++++++++++++++++++ Test_SP_Promotion_Update  +++++++++++++++++++++++++++++++++';

SELECT '------------------------------- CASE1:正常更新 ---------------------------------------' AS 'CASE1';
SET @name = 'asd';
SET @sErrorMsg = '';
SET @status = 0;
SET @type = 0;
SET @datetimestart = '2008-10-4';
SET @datetimeend =DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES (@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff, now(), now());

SET @iErrorCode = 0;
SET @iID = last_insert_id();
SET @name = 'asd2';
SET @status = 0;
SET @type = 1;
SET @datetimestart = '2008-10-4';
SET @datetimeend =DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = 1 ;
CALL SP_Promotion_Update(@iErrorCode, @sErrorMsg, @iID,@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff);

SELECT @sErrorMsg;
SELECT IF(found_rows()= 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE2:用不存在的staffID修改 ---------------------------------------' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 7;
SET @name = 'asd2';
SET @status = 0;
SET @type = 1;
SET @datetimestart = '2008-10-4';
SET @datetimeend =DATE_ADD(now(),INTERVAL 1 YEAR) ;
SET @excecutionthreshold = 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 1;
SET @scope = 0;
SET @staff = -99 ;
CALL SP_Promotion_Update(@iErrorCode, @sErrorMsg, @iID,@name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @staff);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case2 Testing Result';