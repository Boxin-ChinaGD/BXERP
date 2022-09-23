SELECT '++++++++++++++++++ Test_SP_POS_Delete.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ɾ������ʹ���е� -------------------------' AS 'Case1';

--	INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('SN1561891231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
--	SET @iID = LAST_INSERT_ID();
--	
--	INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
--	VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
--	SET @iID2 = LAST_INSERT_ID();
--	
--	SET @iErrorCode = 0;
--	SET @sErrorMsg = '';
--	
--	CALL SP_POS_Delete(@iErrorCode, @sErrorMsg, @iID);
--	
--	SELECT @iErrorCode;
--	SELECT @sErrorMsg;
--	SELECT IF(@iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
--	
--	DELETE FROM t_staff WHERE F_ID = @iID2;
--	DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case2: ɾ��û��ʹ�õ� -------------------------' AS 'Case2';

INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1546451231', 1, 'DFSDGSDKJG546S8FDH', 0, now(), now());
SET @iID = last_insert_id();
SET @iErrorCode = 0;
SET @sErrorMsg = '';

CALL SP_POS_Delete(@iErrorCode, @sErrorMsg, @iID);
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT @sErrorMsg;
DELETE FROM t_pos WHERE F_ID = @iID;

SELECT '-------------------- Case3: POS�쳣״̬������ɾ��-------------------------' AS 'Case3';

INSERT INTO t_pos (F_POS_SN, F_ShopID, F_Salt, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('SN1546451231', 1, 'DFSDGSDKJG546S8FDH', -1, now(), now());
SET @iID = LAST_INSERT_ID();

SET @iErrorCode = 0;
SET @sErrorMsg = '';
CALL SP_POS_Delete(@iErrorCode, @sErrorMsg, @iID);

SELECT @sErrorMsg;
SELECT @iErrorCode;

SELECT IF(@sErrorMsg = '��POS����ɾ���������ظ�ɾ��' AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';

DELETE FROM t_pos WHERE F_ID = @iID;