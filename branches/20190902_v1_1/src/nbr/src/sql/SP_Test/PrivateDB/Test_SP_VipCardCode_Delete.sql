SELECT '++++++++++++++++++ Test_SP_VipCardCode_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:正常删除 -------------------------' AS 'Case1';

INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

INSERT INTO nbr.t_vipcardcode (F_VipID, F_VipCardID, F_SN, F_CreateDatetime)
VALUES (1, @iVipCardID, '6688667890123456', now());
SET @iID = last_insert_id();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_VipCardCode_Delete(@iErrorCode, @sErrorMsg, @iID);
-- 
SELECT @sErrorMsg;
SELECT 1 FROM t_vipcardcode WHERE F_ID = @iID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;