SELECT '++++++++++++++++++Test_SP_CategoryParent_Delete.sql+++++++++++++++++++++++';

SELECT '-----------------Case1: ɾ���Ĵ��൱�е�С���й�����Ʒ������ɾ����������Ϊ7------------------' AS 'Case1';
-- 
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Դ���', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��2', @iID, now(), now());
SET @categoryID2 = LAST_INSERT_ID();
-- 
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'����','����','��',1,'��',3,@categoryID,'SP',1,
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
SELECT IF(found_rows() = 1 AND @iErrorCode = 7, '���Գɹ�', '����ʧ��') AS 'Case1 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_category WHERE F_ID = @categoryID;
DELETE FROM t_category WHERE F_ID = @categoryID2;
DELETE FROM t_categoryparent WHERE F_ID = @iID;
-- 
SELECT '-----------------Case2: ɾ���Ĵ��൱�е�С��û�й�����Ʒ����ɾ����������Ϊ0------------------' AS 'Case2';
-- 
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Դ���', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��2', @iID, now(), now());
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
			AND @iCategorySyncCacheID_TypeC2 = 0 AND @iCategorySyncCacheID_TypeU2 = 0 AND @iCategorySyncCacheID_TypeD2 > 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- ɾ�����Դ���������
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID;
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2;

SELECT '-----------------Case3: ���൱�е�С���й�����Ʒ��ɾ����������Ʒ��ɾ�����࣬������Ϊ0------------------' AS 'Case3';
-- ��������
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Դ���', now(), now());
SET @iID = LAST_INSERT_ID();
-- ����С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- ����С��
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��2', @iID, now(), now());
SET @categoryID2 = LAST_INSERT_ID();
-- ������Ʒ
INSERT INTO T_Commodity (F_Status,F_Name,F_ShortName,F_Specification,F_PackageUnitID,F_PurchasingUnit,F_BrandID,F_CategoryID,F_MnemonicCode,F_PricingType,
F_PriceVIP,F_PriceWholesale,F_CanChangePrice,F_RuleOfPoint,F_Picture,
F_ShelfLife,F_ReturnDays,F_CreateDate,F_PurchaseFlag,F_RefCommodityID,F_RefCommodityMultiple,F_Tag,F_Type)
VALUES (0,'����','����','��',1,'��',3,@categoryID,'SP',1,
11.8,11,1,1,'url=116843434834',
3,30,now(),'20',0,0,'.................', 0);
SET @commID = LAST_INSERT_ID();
-- �ֶ��Ѵ�������Ʒ����Ϊɾ��״̬
UPDATE t_commodity SET F_Status = 2, F_CategoryID = NULL WHERE F_ID = @commID;
-- �������ڽ����֤�ı���
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
			AND @iCategorySyncCacheID_TypeC2 = 0 AND @iCategorySyncCacheID_TypeU2 = 0 AND @iCategorySyncCacheID_TypeD2 > 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case2 Testing Result';
-- 
DELETE FROM t_commodity WHERE F_ID = @commID;
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID;
DELETE FROM t_categorysynccache WHERE F_SyncData_ID = @categoryID2;

SELECT '-----------------Case4: ɾ��û�й���С��Ĵ��࣬������Ϊ0------------------' AS 'Case4';
-- ��������
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Դ���', now(), now());
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
			AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case4 Testing Result';
			
SELECT '-----------------Case5: ɾ���Ĵ��൱�е�С��û�й�����Ʒ����ɾ������ֻ��һ̨��Ч��POSû������D��ͬ����,������Ϊ0------------------' AS 'Case5';
-- 
INSERT INTO t_categoryparent (F_Name, F_CreateDatetime, F_UpdateDatetime)
VALUES ('���Դ���', now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��', @iID, now(), now());
SET @categoryID = LAST_INSERT_ID();
-- 
INSERT INTO t_category (F_Name, F_ParentID, F_CreateDatetime, F_UpdateDatetime)
VALUES ('����С��2', @iID, now(), now());
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
-- �ֶ�����һ̨POSΪ��Ч״̬�������POS�޸�Ϊ��Ч״̬
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
			AND @iCategorySyncCacheID_TypeC2 = 0 AND @iCategorySyncCacheID_TypeU2 = 0 AND @iCategorySyncCacheID_TypeD2 = 0 AND @iErrorCode = 0 AND @sErrorMsg = '', '���Գɹ�', '����ʧ��') AS 'Case5 Testing Result';
-- ��ԭPOS����״̬
UPDATE t_pos SET F_Status = 0;
UPDATE t_pos SET F_Status = 1 WHERE F_POS_SN = 'SN2141819';