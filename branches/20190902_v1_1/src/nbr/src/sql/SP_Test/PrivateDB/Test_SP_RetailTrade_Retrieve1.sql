SELECT '++++++++++++++++++ Test_SP_RetailTrade_Retrieve1.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 用ID来查询零售单 -------------------------' AS 'Case1';

INSERT INTO T_RetailTrade (F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,
F_Status,F_Remark, F_ShopID)
VALUES ('LS2019090412300300031220','PS2424468',2,'url=ashasouuuuunalskd','2017-8-10',5,0,'A123460',1,'双击777',2);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = last_insert_id();

CALL SP_RetailTrade_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTrade WHERE F_ID = @iID AND F_LocalSN = @iLocal AND F_POS_ID = @iPOS_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iID;

SELECT '-------------------- Case2: 用不存在的ID来查询零售单 返回错误码为0 但查询不到数据 -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = -1;

CALL SP_RetailTrade_Retrieve1(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT 1 FROM t_RetailTrade WHERE F_ID = @iID AND F_LocalSN = @iLocal AND F_POS_ID = @iPOS_ID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';
DELETE FROM t_retailtrade WHERE F_ID = @iID;