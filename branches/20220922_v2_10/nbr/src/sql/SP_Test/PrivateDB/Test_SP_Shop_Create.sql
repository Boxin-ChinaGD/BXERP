SELECT '++++++++++++++++++ Test_SP_Shop_Create.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:������� -------------------------' AS 'Case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����1';
SET @iCompanyID = 1;
SET @sAddress = '�ڶ�1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT 1 FROM t_shop WHERE F_Name = @sName AND F_Address = @sAddress AND F_Status = @sStatus AND F_Longitude = @fLongitude AND F_Key = @sKey;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0 AND @sErrorMsg = '','���Գɹ�','����ʧ��') AS 'Case1 Testing Result';

DELETE FROM t_shop WHERE F_ID = last_insert_id();

SELECT '-------------------- Case2:����ظ���˾���Ƶ��ظ������ŵ� -------------------------' AS 'Case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����';
SET @iCompanyID = 1;
SET @sAddress = '�ڶ�1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT 1 FROM t_shop WHERE F_Name = @sName;
SELECT IF(found_rows() = 1 AND @iErrorCode = 1 AND @sErrorMsg = '���ŵ������Ѵ���','���Գɹ�','����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3:��Ӳ����ڵĹ�˾���ŵ� -------------------------' AS 'Case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����';
SET @iCompanyID = -1;
SET @sAddress = '�ڶ�1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '���޸ù�˾','���Գɹ�','����ʧ��') AS 'Case3 Testing Result';

SELECT '-------------------- Case4:�ò����ڵ�ҵ������д��� -------------------------' AS 'Case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����';
SET @iCompanyID = 1;
SET @sAddress = '�ڶ�1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = -99999;
SET @iDistrictID = 1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '���޸�ҵ����','���Գɹ�','����ʧ��') AS 'Case4 Testing Result';

SELECT '-------------------- Case5:�ò����ڵ��ŵ�������д��� -------------------------' AS 'Case5';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sName = '���С����1';
SET @iCompanyID = 1;
SET @sAddress = '�ڶ�1';
SET @sStatus = 1;
SET @fLongitude = 12.12;
SET @fLatitude = 122.11;
SET @sKey = '123456';
SET @sRemark = '';
SET @iBxStaffID = 1;
SET @iDistrictID = -1;

CALL SP_Shop_Create(@iErrorCode, @sErrorMsg, @sName, @iCompanyID, @sAddress, @iDistrictID, @sStatus, @fLongitude, @fLatitude, @sKey, @sRemark, @iBxStaffID);

SELECT IF(@iErrorCode = 7 AND @sErrorMsg = '���޸��ŵ�����','���Գɹ�','����ʧ��') AS 'Case5 Testing Result';

