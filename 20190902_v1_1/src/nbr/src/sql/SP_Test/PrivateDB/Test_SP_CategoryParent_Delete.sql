SELECT '++++++++++++++++++Test_SP_CategoryParent_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: 删除的大类当中的小类有关联商品，不可删除，错误码为7------------------' AS 'Case1';
-- 
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试大类', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类2', @iID, now(), now());
SET @categoryID2 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'卫龙','辣条','克',1,'箱',3,@categoryID,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @commID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg  := '';
-- 
CALL SP_CategoryParent_Delete(@iErrorCode, @sErrorMsg , @iID);
-- 
SELECT @sErrorMsg;
-- 
SELECT 1 FROM t_categoryparent WHERE F_ID = @iID;
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '测试成功', '测试失败') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_category WHERE F_ID = @categoryID;
DELETE FROM t_category WHERE F_ID = @categoryID2;
DELETE FROM t_categoryparent WHERE F_ID = @iID;
-- 
SELECT '-----------------Case2: 删除的大类当中的小类没有关联商品，可删除，错误码为0------------------' AS 'Case2';
-- 
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试大类', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类2', @iID, now(), now());
SET @categoryID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg  := '';
SET @iCategoryID = 0;
SET @iCategoryID2 = 0;
SET @iCategoryParentID = 0;
SET @iCategorySyncCacheID_TypeC = 0;
SET @iCategorySyncCacheID_TypeU = 0;
SET @iCategorySyncCacheID_TypeD = 0;
SET @iCategorySyncCacheID_TypeC2 = 0;
SET @iCategorySyncCacheID_TypeU2 = 0;
SET @iCategorySyncCacheID_TypeD2 = 0;
-- 
CALL SP_CategoryParent_Delete(@iErrorCode, @sErrorMsg , @iID);
-- 
SELECT F_ID INTO @iCategoryID FROM t_category WHERE F_ID = @categoryID;
SELECT F_ID INTO @iCategoryID2 FROM t_category WHERE F_ID = @categoryID2;
SELECT F_ID INTO @iCategoryParentID FROM t_categoryparent WHERE F_ID = @iID;
SELECT F_ID INTO @iCategorySyncCacheID_TypeC FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'C';
SELECT F_ID INTO @iCategorySyncCacheID_TypeU FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'U';
SELECT F_ID INTO @iCategorySyncCacheID_TypeD FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'D';
SELECT F_ID INTO @iCategorySyncCacheID_TypeC2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'C';
SELECT F_ID INTO @iCategorySyncCacheID_TypeU2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'U';
SELECT F_ID INTO @iCategorySyncCacheID_TypeD2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'D';
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT @iCategoryParentID, @iCategoryID, @iCategoryID2;
SELECT @iCategorySyncCacheID_TypeC, @iCategorySyncCacheID_TypeU, @iCategorySyncCacheID_TypeD;
SELECT @iCategorySyncCacheID_TypeC2, @iCategorySyncCacheID_TypeU2, @iCategorySyncCacheID_TypeD2;
-- 
SELECT IF(@iCategoryID = 0 AND @iCategoryID2 = 0 AND @iCategoryParentID = 0 AND @iCategorySyncCacheID_TypeC = 0 AND @iCategorySyncCacheID_TypeU = 0 AND @iCategorySyncCacheID_TypeD > 0 -- 
			AND @iCategorySyncCacheID_TypeC2 = 0 AND @iCategorySyncCacheID_TypeU2 = 0 AND @iCategorySyncCacheID_TypeD2 > 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 删除测试创建的数据
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID;
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2;

SELECT '-----------------Case3: 大类当中的小类有关联商品，删除关联的商品后，删除大类，错误码为0------------------' AS 'Case3';
-- 创建大类
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试大类', now(), now());
SET @iID = LAST_INSERT_ID();
-- 创建小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 创建小类
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类2', @iID, now(), now());
SET @categoryID2 = LAST_INSERT_ID();
-- 创建商品
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'卫龙','辣条','克',1,'箱',3,@categoryID,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @commID = LAST_INSERT_ID();
-- 手动把创建的商品设置为删除状态
UPDATE t_commodity SET F_Status = 2, F_CategoryID = NULL WHERE F_ID = @commID;
-- 声明用于结果验证的变量
SET @iErrorCode = 0;
SET @sErrorMsg  := '';
SET @iCategoryID = 0;
SET @iCategoryID2 = 0;
SET @iCategoryParentID = 0;
SET @iCategorySyncCacheID_TypeC = 0;
SET @iCategorySyncCacheID_TypeU = 0;
SET @iCategorySyncCacheID_TypeD = 0;
SET @iCategorySyncCacheID_TypeC2 = 0;
SET @iCategorySyncCacheID_TypeU2 = 0;
SET @iCategorySyncCacheID_TypeD2 = 0;
-- 
CALL SP_CategoryParent_Delete(@iErrorCode, @sErrorMsg , @iID);
-- 
SELECT @iID, @categoryID;
SELECT F_ID INTO @iCategoryID FROM t_category WHERE F_ID = @categoryID;
SELECT F_ID INTO @iCategoryID2 FROM t_category WHERE F_ID = @categoryID2;
SELECT F_ID INTO @iCategoryParentID FROM t_categoryparent WHERE F_ID = @iID;
SELECT F_ID INTO @iCategorySyncCacheID_TypeC FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'C';
SELECT F_ID INTO @iCategorySyncCacheID_TypeU FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'U';
SELECT F_ID INTO @iCategorySyncCacheID_TypeD FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'D';
SELECT F_ID INTO @iCategorySyncCacheID_TypeC2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'C';
SELECT F_ID INTO @iCategorySyncCacheID_TypeU2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'U';
SELECT F_ID INTO @iCategorySyncCacheID_TypeD2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'D';
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT @iCategoryParentID, @iCategoryID, @iCategoryID2;
SELECT @iCategorySyncCacheID_TypeC, @iCategorySyncCacheID_TypeU, @iCategorySyncCacheID_TypeD;
SELECT @iCategorySyncCacheID_TypeC2, @iCategorySyncCacheID_TypeU2, @iCategorySyncCacheID_TypeD2;
-- 
SELECT IF(@iCategoryID = 0 AND @iCategoryID2 = 0 AND @iCategoryParentID = 0 AND @iCategorySyncCacheID_TypeC = 0 AND @iCategorySyncCacheID_TypeU = 0 AND @iCategorySyncCacheID_TypeD > 0 -- 
			AND @iCategorySyncCacheID_TypeC2 = 0 AND @iCategorySyncCacheID_TypeU2 = 0 AND @iCategorySyncCacheID_TypeD2 > 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID;
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2;

SELECT '-----------------Case4: 删除没有关联小类的大类，错误码为0------------------' AS 'Case4';
-- 创建大类
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试大类', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg  := '';
SET @iCategoryID = 0;
SET @iCategoryParentID = 0;
SET @iCategorySyncCacheID_TypeD = 0;
-- 
CALL SP_CategoryParent_Delete(@iErrorCode, @sErrorMsg , @iID);
-- 
SELECT F_ID INTO @iCategoryID FROM t_category WHERE F_ParentID = @iID;
SELECT F_ID INTO @iCategoryParentID FROM t_categoryparent WHERE F_ID = @iID;
SELECT F_ID INTO @iCategorySyncCacheID_TypeD FROM t_categorysynccache WHERE F_SyncData_ID IN (SELECT F_ID FROM t_category WHERE F_ParentID = @iID) AND F_SyncType = 'D';
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT @iCategoryID, @iCategoryParentID, @iCategorySyncCacheID_TypeD;
-- 
SELECT IF(@iCategoryID = 0 AND @iCategoryParentID = 0 AND @iCategorySyncCacheID_TypeD = 0 -- 
			AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case4 Testing Result';
			
SELECT '-----------------Case5: 删除的大类当中的小类没有关联商品，可删除，但只有一台有效的POS没有生成D型同步块,错误码为0------------------' AS 'Case5';
-- 
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试大类', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('测试小类2', @iID, now(), now());
SET @categoryID2 = LAST_INSERT_ID();
-- 
SET @iErrorCode = 0;
SET @sErrorMsg  := '';
SET @iCategoryID = 0;
SET @iCategoryID2 = 0;
SET @iCategoryParentID = 0;
SET @iCategorySyncCacheID_TypeC = 0;
SET @iCategorySyncCacheID_TypeU = 0;
SET @iCategorySyncCacheID_TypeD = 0;
SET @iCategorySyncCacheID_TypeC2 = 0;
SET @iCategorySyncCacheID_TypeU2 = 0;
SET @iCategorySyncCacheID_TypeD2 = 0;
-- 手动设置一台POS为有效状态，其余的POS修改为无效状态
UPDATE t_pos SET F_Status = 1;
UPDATE t_pos SET F_Status = 0 WHERE F_ID = 1;
-- 
CALL SP_CategoryParent_Delete(@iErrorCode, @sErrorMsg , @iID);
-- 
SELECT F_ID INTO @iCategoryID FROM t_category WHERE F_ID = @categoryID;
SELECT F_ID INTO @iCategoryID2 FROM t_category WHERE F_ID = @categoryID2;
SELECT F_ID INTO @iCategoryParentID FROM t_categoryparent WHERE F_ID = @iID;
SELECT F_ID INTO @iCategorySyncCacheID_TypeC FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'C';
SELECT F_ID INTO @iCategorySyncCacheID_TypeU FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'U';
SELECT F_ID INTO @iCategorySyncCacheID_TypeD FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID AND F_SyncType = 'D';
SELECT F_ID INTO @iCategorySyncCacheID_TypeC2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'C';
SELECT F_ID INTO @iCategorySyncCacheID_TypeU2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'U';
SELECT F_ID INTO @iCategorySyncCacheID_TypeD2 FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2 AND F_SyncType = 'D';
-- 
SELECT @iErrorCode, @sErrorMsg;
SELECT @iCategoryParentID, @iCategoryID, @iCategoryID2;
SELECT @iCategorySyncCacheID_TypeC, @iCategorySyncCacheID_TypeU, @iCategorySyncCacheID_TypeD;
SELECT @iCategorySyncCacheID_TypeC2, @iCategorySyncCacheID_TypeU2, @iCategorySyncCacheID_TypeD2;
-- 
SELECT IF(@iCategoryID = 0 AND @iCategoryID2 = 0 AND @iCategoryParentID = 0 AND @iCategorySyncCacheID_TypeC = 0 AND @iCategorySyncCacheID_TypeU = 0 AND @iCategorySyncCacheID_TypeD = 0 -- 
			AND @iCategorySyncCacheID_TypeC2 = 0 AND @iCategorySyncCacheID_TypeU2 = 0 AND @iCategorySyncCacheID_TypeD2 = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '测试成功', '测试失败') AS 'Case5 Testing Result';
-- 还原POS机的状态
UPDATE t_pos SET F_Status = 0;
UPDATE t_pos SET F_Status = 1 WHERE F_POS_SN = 'SN2141819';