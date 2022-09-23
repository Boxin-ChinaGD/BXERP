SELECT '++++++++++++++++++ Test_SP_Shop_Update.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�����޸� -------------------------' AS 'Case1';
INSERT INTO t_shop (F_Name, F_BxStaffID, F_CompanyID, F_Address, F_DistrictID, F_Status, F_Longitude, F_Latitude, F_Key)
VALUES ('���С����33', 1, 1 ,'�ڶ�1', 1, 1, 12.12, 122.11, '123456');

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����656556';
SET @sAddress = '�ڶ�2';
SET @iDistrictID = 2;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
-- SET @sKey = '123456';
SET @sRemark = '���ŵ�Ϊ��һ�ŵ�';
SET @iID = LAST_INSERT_ID();

CALL SP_Shop_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iDistrictID, @fLongitude, @fLatitude, @sRemark);

SELECT @iErrorCode;
SELECT 1 FROM t_shop WHERE F_Name = @sName AND F_Address = @sAddress AND F_Longitude = @fLongitude AND F_DistrictID = @iDistrictID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '','���Գɹ�','����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_shop WHERE F_ID = @iID;

SELECT '-------------------- Case2:�޸ĳ��ظ����ŵ����� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '��꿼��õ�';
SET @sAddress = '�ڶ�1';
SET @iDistrictID = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sRemark = '���ŵ�Ϊ�ڶ��ŵ�';
-- SET @sKey = '123456';
SET @iID = 3;

CALL SP_Shop_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iDistrictID, @fLongitude, @fLatitude, @sRemark);

SELECT IF(@iErrorCode = 1 AND @sErrorMsg = '�ŵ������ظ�','���Գɹ�','����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:�޸ĳɲ����ڵ��ŵ����� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����1';
SET @sAddress = '�ڶ�1';
SET @iDistrictID = -1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sRemark = '���ŵ�Ϊ�ڶ��ŵ�';
-- SET @sKey = '123456';
SET @iID = 3;

CALL SP_Shop_Update(@iErrorCode, @sErrorMsg, @iID, @sName, @sAddress, @iDistrictID, @fLongitude, @fLatitude, @sRemark);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '�����޸ĳɲ����ڵ��ŵ�����','���Գɹ�','����ʧ��') AS 'Case3 Testing Result'; 