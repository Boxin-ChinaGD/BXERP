SELECT '++++++++++++++++++ Test_SP_POS_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常修改 -------------------------' AS 'Case1';

INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAst_insert_id();
SET @iShopID = 2;
SET @iReturnSalt = 1;

CALL SP_POS_Update(@iErrorCode, @sErrorMsg, @iID, @iShopID, @iReturnSalt);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_ID = @iID AND F_ShopID = @iShopID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2: 用不存在的shopID创建 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = LAst_insert_id();
SET @iShopID = -99;
SET @iReturnSalt = 1;

CALL SP_POS_Update(@iErrorCode, @sErrorMsg, @iID, @iShopID, @iReturnSalt);

SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';