SELECT '++++++++++++++++++ Test_SP_POS_RetrieveN.sql ++++++++++++++++++++';

SELECT '-------------------- Case1: ����POS_SN���� -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPOS_SN = '832';
SET @iShopID = -1;
SET @iStatus = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iReturnSalt = 1;

CALL SP_POS_RetrieveN(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @iStatus, @iReturnSalt, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_POS_SN = @sPOS_SN AND F_ShopID = @iShopID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';

SELECT '-------------------- Case2: ����ShopID���� -------------------------' AS 'Case2';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPOS_SN = '';
SET @iShopID = 1;
SET @iStatus = -1;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iReturnSalt = 1;

CALL SP_POS_RetrieveN(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @iStatus, @iReturnSalt, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_POS_SN = @sPOS_SN AND F_ShopID = @iShopID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';

SELECT '-------------------- Case3: ����Status���� -------------------------' AS 'Case3';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @sPOS_SN = '';
SET @iShopID = -1;
SET @iStatus = 0;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;
SET @iReturnSalt = 1;

CALL SP_POS_RetrieveN(@iErrorCode, @sErrorMsg, @sPOS_SN, @iShopID, @iStatus, @iReturnSalt, @iPageIndex, @iPageSize, @iTotalRecord);
SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_POS WHERE F_POS_SN = @sPOS_SN AND F_ShopID = @iShopID AND F_Status = @iStatus;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case3 Testing Result';