-- case1:�����޸�
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID =1;
SET @iCommodityID = 1;
SET @iNO = 300;
SET @iPrice = 20;
SET @iAmount = 6000;

CALL SP_WarehousingCommodity_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iPrice, @iNO, @iAmount);
SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_CommodityID = @iCommodityID AND F_Price = @iPrice AND F_NO = @iNO AND F_Amount = @iAmount;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case2������һ��WarehousingID�����ڵ�WarehousingCommodity�����ش�����2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID =99999;
SET @iCommodityID = 1;
SET @iNO = 300;
SET @iPrice = 20;
SET @iAmount = 6000;

CALL SP_WarehousingCommodity_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iPrice, @iNO, @iAmount);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_CommodityID = @iCommodityID AND F_Price = @iPrice AND F_NO = @iNO AND F_Amount = @iAmount;
SELECT IF(found_rows() = 1 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case3:����һ��CommodityID�����ڵ�WarehousingCommodity�����ش�����2
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID =1;
SET @iCommodityID = 9999;
SET @iNO = 300;
SET @iPrice = 20;
SET @iAmount = 6000;

CALL SP_WarehousingCommodity_Update(@iErrorCode, @sErrorMsg, @iWarehousingID, @iCommodityID, @iPrice, @iNO, @iAmount);

SELECT @sErrorMsg;
SELECT @iErrorCode;
SELECT 1 FROM t_WarehousingCommodity WHERE F_CommodityID = @iCommodityID AND F_Price = @iPrice AND F_NO = @iNO AND F_Amount = @iAmount;
SELECT IF(found_rows() = 0 AND @iErrorCode = 2, '���Գɹ�', '����ʧ��') AS 'Testing Result';