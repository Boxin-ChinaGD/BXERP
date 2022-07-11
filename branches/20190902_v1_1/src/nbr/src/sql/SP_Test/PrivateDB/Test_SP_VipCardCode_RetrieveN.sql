SELECT '++++++++++++++++++ Test_SP_VipCardCode_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:查询所有 -------------------------' AS 'Case1';
INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

INSERT INTO t_vipcardcode (F_VipID, F_VipCardID, F_SN, F_CreateDatetime)
VALUES (1, @iVipCardID, '6688667890123456', now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iVipID = -1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_VipCardCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT IF(@iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_vipcardcode WHERE F_ID = @iID;
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;

SELECT '-------------------- Case2:查询某个会员的会员卡 -------------------------' AS 'Case2';
INSERT INTO T_VIP (F_SN,F_ICID,F_CardID,F_Name,F_Email,F_ConsumeTimes,F_ConsumeAmount,F_District,F_Category,F_Birthday,F_Bonus,F_LastConsumeDatetime,F_Mobile)
VALUES ('VIP999999'/*F_SN*/,'1234256'/*F_ICID*/,1/*F_CardID*/,'giggs'/*F_Name*/,'1234526@bx.vip'/*F_Email*/,5/*F_ConsumeTimes*/,99.8/*F_ConsumeAmount*/,'广州'/*F_District*/,
		1/*F_Category*/,'2017-08-06'/*F_Birthday*/,44.9/*F_Bonus*/,'2017-08-08 23:59:10'/*F_LastConsumeDatetime*/,'12313212332'/*F_Mobile*/);
SET @iVipID = last_insert_id();

INSERT INTO t_vipcard (F_Title, F_BackgroundColor, F_ClearBonusDay, F_ClearBonusDatetime, F_CreateDatetime)
VALUES ('会员卡', '255,255,255;255,255,255', 3650, NULL, now());
SET @iVipCardID = last_insert_id();

INSERT INTO t_vipcardcode (F_VipID, F_VipCardID, F_SN, F_CreateDatetime)
VALUES (@iVipID, @iVipCardID, '6688667890123456', now());
SET @iID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_VipCardCode_RetrieveN(@iErrorCode, @sErrorMsg, @iVipID, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_vipcardcode WHERE F_ID = @iID;
DELETE FROM t_vipcard WHERE F_ID = @iVipCardID;
DELETE FROM t_vip WHERE F_ID = @iVipID;