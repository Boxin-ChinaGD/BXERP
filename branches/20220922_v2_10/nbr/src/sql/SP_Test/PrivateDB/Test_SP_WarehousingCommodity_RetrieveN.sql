-- case1:������ѯ
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 6;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_WarehousingCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iWarehousingID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_WarehousingID = @iWarehousingID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case2:WarehousingIDΪ�ղ�ѯ
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = '';
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_WarehousingCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iWarehousingID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_WarehousingID = @iWarehousingID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';

-- case3:WarehousingID������
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iWarehousingID = 9999;
SET @iPageIndex = 1;
SET @iPageSize = 10;
SET @iTotalRecord = 0;

CALL SP_WarehousingCommodity_RetrieveN(@iErrorCode, @sErrorMsg, @iWarehousingID, @iPageIndex, @iPageSize, @iTotalRecord);

SELECT @iTotalRecord;
SELECT @iErrorCode;
SELECT @sErrorMsg;
SELECT 1 FROM t_WarehousingCommodity WHERE F_WarehousingID = @iWarehousingID;
SELECT IF(found_rows() = 0 AND @iErrorCode = 0, '���Գɹ�', '����ʧ��') AS 'Testing Result';