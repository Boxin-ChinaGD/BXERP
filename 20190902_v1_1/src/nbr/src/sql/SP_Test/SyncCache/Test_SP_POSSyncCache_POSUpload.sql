SELECT '++++++++++++++++++ .sql ++++++++++++++++++++';

SELECT '-------------------- Case1: 正常添加 -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 5;
SET @cSyncType = "C";
SET @iPOSID = 1;

CALL SP_POSSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);

SELECT @sErrorMsg;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: 重复添加U型数据(只存在一个U型) -------------------------' AS 'Case2';

INSERT INTO t_possynccache (F_SyncData_ID, F_SyncType, F_SyncSequence) VALUES (4,'U',0);

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = 4;
SET @cSyncType = "U";
SET @iPOSID = 1;
CALL SP_POSSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @iPOSID);
SELECT @sErrorMsg;
SELECT count(1) FROM t_possynccache WHERE F_SyncData_ID = @iSyncData_ID AND F_SyncType = @cSyncType;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case2 Testing Result';


SELECT '-------------------- Case3: pos1修改pos1 只会生成一条调度表 -------------------------' AS 'Case2';
INSERT INTO T_POS (F_POS_SN,F_ShopID,F_Salt,F_Status) VALUES ('SN214792s1',5,'B1AFC07474C37C5AEC4199ED28E09705',1);
SET @POSID = last_insert_id();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iSyncData_ID = @POSID;
SET @cSyncType = "U";

CALL SP_POSSyncCache_POSUpload(@iErrorCode, @sErrorMsg, @iSyncData_ID, 1, @cSyncType, @POSID);

SET @num = 0;
SELECT @sErrorMsg;
SELECT count(1) INTO @num FROM t_possynccachedispatcher WHERE F_POS_ID = @POSID;
SELECT IF(@num = 1 AND @iErrorCode = 0, '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_possynccachedispatcher;
DELETE FROM t_possynccache;
DELETE FROM t_pos WHERE F_ID = @POSID;