SELECT '++++++++++++++++++ Test_SP_Commodity_UpdatePurchasingUnit.sql ++++++++++++++++++++';

SELECT '-------------------- Case1:�޸Ĳɹ���λ -------------------------' AS 'Case1';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @sPurchasingUnit = 'ǧ��';

CALL SP_Commodity_UpdatePurchasingUnit(@iErrorCode, @sErrorMsg, @iID, @sPurchasingUnit);
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_commodity WHERE F_ID = @iID AND F_PurchasingUnit = @sPurchasingUnit;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';