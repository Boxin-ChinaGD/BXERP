SELECT '++++++++++++++++++ Test_SP_WxUser_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- CASE1:����һ����ȷ��oppenid-------------------------' AS 'Case1'; 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @subscribe = '1';
SET	@oppenId = '23ERWE5CVWER413543';
SET	@nickname = '����һ�����?';
SET @sex = '2';
SET @iLangauge = 'zh_CN';
SET @city = '����';
SET @province = '�㶫ʡ';
SET @country = '�й�';
SET @headImgUrl = 'https://sdqwc/weqasd/qweqasd/asd1231231';
SET @subscribeTime = '2019-1-11';
SET @unionId = '12dqwe235213';
SET @remark ='������ӵ��û�';
SET @groupId ='12354';
SET	@tagIdList = '413dqweq123';
SET @subscribeScene ='ADD_SCENE_QR_CODE';
SET @qrScene ='135';
SET @qrSceneStr = '3123';
-- SET @privilege = "324524";

CALL SP_WxUser_Create(@iErrorCode, @sErrorMsg, @subscribe,@oppenId,@nickname,@sex,@iLangauge,@city,@province,@country,@headImgUrl,
@subscribeTime,@unionid,@remark,@groupId,@tagIdList,@subscribeScene,@qrScene,@qrSceneStr);

CALL SP_WxUser_Delete(@iErrorCode, @sErrorMsg, @oppenId);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_wxuser WHERE F_OpenId = @oppenId;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- case2:����һ���յ�oppenid-------------------------' AS 'Case2'; 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @subscribe = "1";
SET	@oppenId = "";
CALL SP_WxUser_Create(@iErrorCode, @sErrorMsg, @subscribe,@oppenId,@nickname,@sex,@iLangauge,@city,@province,@country,@headImgUrl,
@subscribeTime,@unionid,@remark,@groupId,@tagIdList,@subscribeScene,@qrScene,@qrSceneStr);

CALL SP_WxUser_Delete(@iErrorCode, @sErrorMsg, @oppenId);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_wxuser WHERE F_OpenId = @oppenId;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- case3:����һ�������ڵ�oppenid-------------------------' AS 'Case3'; 

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET	@oppenId = "123eqweZzc123";

CALL SP_WxUser_Delete(@iErrorCode, @sErrorMsg, @oppenId);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_wxuser WHERE F_OpenId = @oppenId;
SELECT IF(found_rows() = 0 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';