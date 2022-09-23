SELECT '+++++++++++++++++++++++++++++++ Test_SP_Promotion_Create +++++++++++++++++++++++++++++++++';

SELECT '------------------------------- CASE1:添加促销活动 只传入满减金额 ---------------------------------------' AS 'CASE1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '全场满10减3';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = -1;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();	
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE2:添加促销活动 只传入满减折扣 ---------------------------------------' AS 'CASE2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '全场满10打7折';
SET @status = 0;
SET @type = 1;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = -1;
SET @excecutiondiscount = 7;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE3:添加满减金额的促销活动 传入满减金额和满减折扣 ---------------------------------------' AS 'CASE3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '全场满10减3';
SET @status = 0;
SET @type = 0;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 7;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();	
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;

SELECT '------------------------------- CASE4:添加满折金额促销活动 传入满减金额和满减折扣 ----------------------------------------' AS 'CASE4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @name = '全场满10打6折';
SET @status = 0;
SET @type = 1;
SET @datetimestart = now();
SET @datetimeend = now();
SET @excecutionthreshold= 10;
SET @excecutionamount = 3;
SET @excecutiondiscount = 6;
SET @scope = 0;
SET @shopScope = 0;
SET @staffId = 1;

CALL SP_Promotion_Create(@iErrorCode, @sErrorMsg, @name, @status, @type, @datetimestart, @datetimeend, @excecutionthreshold, @excecutionamount, @excecutiondiscount, @scope, @shopScope, @staffId);
SET @iID = last_insert_id();
SELECT 1 FROM t_Promotion WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM t_Promotion WHERE F_ID = @iID;