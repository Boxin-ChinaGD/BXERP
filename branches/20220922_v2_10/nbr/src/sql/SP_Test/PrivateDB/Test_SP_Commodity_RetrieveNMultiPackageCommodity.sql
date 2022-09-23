SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveNMultiPackageCommodity.sql ++++++++++++++++++++';

SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRefCommodityID = 47;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Commodity_RetrieveNMultiPackageCommodity(@iErrorCode, @sErrorMsg, @iRefCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT @iTotalRecord;

SELECT 1 FROM t_commodity 
WHERE F_RefCommodityID = @iRefCommodityID;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE1 Testing Result';

-- case2:����һ�������ڵ�iRefCommodityID
SELECT '++++++++++++++++++ Test_SP_Commodity_RetrieveNMultiPackageCommodity.sql case2:����һ�������ڵ�iRefCommodityID ++++++++++++++++++++';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iRefCommodityID = -2222;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_Commodity_RetrieveNMultiPackageCommodity(@iErrorCode, @sErrorMsg, @iRefCommodityID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @sErrorMsg;

SELECT 1 FROM t_commodity 
WHERE F_RefCommodityID = @iRefCommodityID;
SELECT IF(@iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'CASE2 Testing Result';