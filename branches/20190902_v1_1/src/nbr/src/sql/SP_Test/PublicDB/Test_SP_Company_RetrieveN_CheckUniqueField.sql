SELECT '++++++++++++++++++ Test_SP_Company_RetrieveN_CheckUniqueField.sql ++++++++++++++++++++';

SELECT '--------------------- case1: 查询一个不存在的公司名称------------------------------' AS 'case1';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = '茕茕孑立';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case1 Testing Result';


SELECT '--------------------- case2: 查询一个已存在的公司名称------------------------------' AS 'case2';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = 'BX一号分公司';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '该公司名称已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case2 Testing Result';

SELECT '--------------------- case3: 查询一个不存在的营业执照号------------------------------' AS 'case3';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 2;
SET @sUniqueField = 'ZX987654321';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case3 Testing Result';


SELECT '--------------------- case4: 查询一个已存在的营业执照号------------------------------' AS 'case4';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 2;
SET @sUniqueField = '111111111111111';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '该营业执照号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case4 Testing Result';

SELECT '--------------------- case9: 查询一个不存在的公司的DB名称------------------------------' AS 'case9';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 5;
SET @sUniqueField = 'zxr';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case9 Testing Result';


SELECT '--------------------- case10: 查询一个已存在的公司的DB名称------------------------------' AS 'case10';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 5;
SET @sUniqueField = 'nbr';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '该DB名称已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case10 Testing Result';

SELECT '--------------------- case11: 查询一个已存在的公司名称,传入的ID与已存在的公司名称对应的ID相同------------------------------' AS 'case11';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 1;
SET @sUniqueField = 'BX一号分公司';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case11 Testing Result';


SELECT '--------------------- case12: 查询一个已存在的营业执照号，但传入的ID与已存在的营业执照号对应的ID相同------------------------------' AS 'case12';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 2;
SET @sUniqueField = '111111111111111';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case12 Testing Result';


SELECT '--------------------- case15: 查询一个已存在的公司DB名称，但传入的ID与已存在的公司DB名称对应的ID相同------------------------------' AS 'case15';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 1;
SET @iFieldToCheckUnique = 5;
SET @sUniqueField = 'nbr';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case15 Testing Result';


SELECT '--------------------- case16: 查询一个不存在的子商户号------------------------------' AS 'case16';
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 6;
SET @sUniqueField = '6666666666';

CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '' AND @iErrorCode = 0,'测试成功','测试失败') AS 'Case16 Testing Result';


SELECT '--------------------- case17: 查询一个已存在的子商户号------------------------------' AS 'case17';
-- 
SET @sSubmchid = 'test17subm';
INSERT INTO nbr_bx.t_company (F_Name, F_SN, F_BusinessLicenseSN, F_BusinessLicensePicture, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_Key, F_DBUserName, F_DBUserPassword, F_Status,F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX三号分公司', '668869', '77777777777777', '/p/123.jpg', '老板1号', '13123615881', '000000', 'a13123615881', 'nbr11', '12345678901234567890123456789099', 'nbr11', 'WEF#EGEHEH$$^*DI', 0, @sSubmchid, '娃哈哈', '2030-12-02 01:01:01');
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg = '';
SET @iID = 0;
SET @iFieldToCheckUnique = 6;
SET @sUniqueField = @sSubmchid;
SELECT F_Submchid FROM nbr_bx.t_company WHERE F_ID = 1;
CALL SP_Company_RetrieveN_CheckUniqueField(@iErrorCode, @sErrorMsg,@iID, @iFieldToCheckUnique, @sUniqueField);

SELECT IF(@sErrorMsg = '该子商户号已存在' AND @iErrorCode = 1,'测试成功','测试失败') AS 'Case17 Testing Result';
DELETE FROM nbr_bx.t_company WHERE F_ID = @iID;