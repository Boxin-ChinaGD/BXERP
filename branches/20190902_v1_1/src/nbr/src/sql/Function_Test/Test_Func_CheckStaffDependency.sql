SELECT '++++++++++++++++++++++++++++++++++Test_Func_CheckStaffDependency.sql+++++++++++++++++++++++++++++++++++++++++';

SELECT '-------------------- Case1:没有依赖关系，可以删除-------------------------' AS 'Case1';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();


SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case1 Testing Result';

DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case2:该员工有仓库依赖，可以删除-------------------------' AS 'Case2';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID) 
VALUES ('仓库2233', '植物园', 0, @iID);
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case2 Testing Result';

DELETE FROM t_warehouse WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case3:该员工有采购依赖，可以删除-------------------------' AS 'Case3';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_PurchasingOrder(F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(0, @iID, 1, '默认供应商', now(), now(), now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case3 Testing Result';

DELETE FROM T_PurchasingOrder WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case4:该员工有入库依赖，可以删除-------------------------' AS 'Case4';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (0, 3, 1, @iID, now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case4 Testing Result';

DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case5:该员工有仓管退货依赖，可以删除-------------------------' AS 'Case5';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime, F_ShopID)
VALUES (@iID, 1, 1, NOW(), NOW(),2);
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case5 Testing Result';

DELETE FROM T_ReturnCommoditySheet WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case6:该员工有盘点依赖，可以删除-------------------------' AS 'Case6';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_InventorySheet (F_ShopID, F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,@iID,'2017-12-06','...........................zz');
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case6 Testing Result';

DELETE FROM T_InventorySheet WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case7:该员工有零售单依赖，可以删除-------------------------' AS 'Case7';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_RetailTrade (F_ShopID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark)
VALUES (2,'LS2019090414230000011234','PS2424468',2,'url=ashasouuuuunalskd','2017-8-10',@iID,0,'A123460',1,'双击777');
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case7 Testing Result';

DELETE FROM T_RetailTrade WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case8:该员工有服务器的收银汇总依赖，可以删除-------------------------' AS 'Case8';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO T_RetailTradeAggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iID, 1, now(), now(), 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case8 Testing Result';

DELETE FROM T_RetailTradeAggregation WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case9:该员工有促销依赖，可以删除-------------------------' AS 'Case9';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('a', 0, 1, now(), now(), 10, 1, 8, 0, @iID, now(), now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case9 Testing Result';

DELETE FROM T_Promotion WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case10:该员工有商品历史依赖，可以删除-------------------------' AS 'Case10';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime)
VALUES (1, 'a', 1, 2, @iID, 1, now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case10 Testing Result';

DELETE FROM T_CommodityHistory WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case10:该员工有Staff零售报表依赖，可以删除-------------------------' AS 'Case10';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aa', '12345678911', '123456789123456', '123456', 'asdefggsdjfasgyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();

INSERT INTO t_retailtradedailyreportbystaff (F_ShopID, F_Datetime, F_StaffID, F_NO, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime)
VALUES (2, now(), @iID, 1, 1, 1, now(), now());
SET @iID2 = LAST_INSERT_ID();

SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case10 Testing Result';

DELETE FROM T_RetailTradeDailyReportByStaff WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case11:该员工为售前账号，删除成功-------------------------' AS 'Case11';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case12:该员工为售前账号,有仓库依赖。删除成功-------------------------' AS 'Case12';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO t_warehouse (F_Name, F_Address, F_Status, F_StaffID) 
VALUES ('仓库2233', '植物园', 0, @iID);
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_warehouse WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case13:该员工为售前账号,有采购依赖。删除成功-------------------------' AS 'Case13';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO T_PurchasingOrder(F_Status, F_StaffID, F_ProviderID, F_ProviderName, F_CreateDatetime, F_ApproveDatetime, F_EndDatetime) 
VALUES(0, @iID, 1, '默认供应商', now(), now(), now());
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_purchasingorder WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case14:该员工为售前账号,有入库依赖。删除成功-------------------------' AS 'Case14';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO T_Warehousing (F_Status, F_ProviderID, F_WarehouseID, F_StaffID, F_CreateDatetime)
VALUES (0, 3, 1, @iID, now());
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM T_Warehousing WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case15:该员工为售前账号,有仓管退货依赖。删除成功-------------------------' AS 'Case15';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO t_returncommoditysheet (F_StaffID, F_ProviderID, F_Status, F_CreateDatetime, F_UpdateDatetime, F_ShopID)
VALUES (@iID, 1, 1, NOW(), NOW(),2);
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_returncommoditysheet WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case16:该员工为售前账号,有盘点依赖。删除成功-------------------------' AS 'Case16';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO T_InventorySheet (F_ShopID,F_WarehouseID,F_Scope,F_Status,F_StaffID,F_CreateDatetime,F_Remark)
VALUES (2,1,200,0,@iID,'2017-12-06','...........................zz');
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM T_InventorySheet WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case17:该员工为售前账号,有零售单依赖。删除成功-------------------------' AS 'Case17';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO T_RetailTrade (F_ShopID,F_SN,F_LocalSN,F_POS_ID,F_Logo,F_SaleDatetime,F_StaffID,F_PaymentType,F_PaymentAccount,F_Status,F_Remark)
VALUES (2,'LS2019090414230000011235','PS2424468',2,'url=ashasouuuuunalskd','2017-8-10',@iID,0,'A123460',1,'双击777');
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM T_RetailTrade WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case18:该员工为售前账号,有收银汇总依赖。删除成功-------------------------' AS 'Case18';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID(); 
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO T_RetailTradeAggregation (F_StaffID, F_PosID, F_WorkTimeStart, F_WorkTimeEnd, F_TradeNO, F_Amount, F_ReserveAmount, F_CashAmount, F_WechatAmount, F_AlipayAmount, F_Amount1, F_Amount2, F_Amount3, F_Amount4, F_Amount5, F_UploadDateTime)
VALUES (@iID, 1, now(), now(), 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, now());
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM T_RetailTradeAggregation WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case19:该员工为售前账号,有促销依赖。删除成功-------------------------' AS 'Case19';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO t_promotion (F_Name, F_Status, F_Type, F_DatetimeStart, F_DatetimeEnd, F_ExcecutionThreshold, F_ExcecutionAmount, F_ExcecutionDiscount, F_Scope, F_Staff, F_CreateDatetime, F_UpdateDatetime)
VALUES ('a', 0, 1, now(), now(), 10, 1, 8, 0, @iID, now(), now());
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_promotion WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case20:该员工为售前账号,有商品历史依赖。删除成功-------------------------' AS 'Case20';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO t_commodityhistory (F_CommodityID, F_FieldName, F_OldValue, F_NewValue, F_StaffID, F_BySystem, F_Datetime)
VALUES (1, 'a', 1, 2, @iID, 1, now());
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_commodityhistory WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;

SELECT '-------------------- Case21:该员工为售前账号,有零售报表依赖。删除成功-------------------------' AS 'Case21';
INSERT INTO t_staff (F_Name, F_Phone, F_ICID, F_WeChat, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin, F_ShopID, F_DepartmentID, F_Status, F_CreateDatetime, F_UpdateDatetime)
VALUES ('aax', '1234567897', '123456789123456f', '123456f', 'asdefggsdjfasgfyf', now(), 1, 1, 1, 0, now(), now());
SET @iID = LAST_INSERT_ID();
-- 
INSERT INTO t_staffrole (F_StaffID, F_RoleID) VALUES (@iID, 3);
-- 
INSERT INTO t_retailtradedailyreportbystaff (F_ShopID, F_Datetime, F_StaffID, F_NO, F_TotalAmount, F_GrossMargin, F_CreateDatetime, F_UpdateDatetime)
VALUES (2, now(), @iID, 1, 1, 1, now(), now());
SET @iID2 = LAST_INSERT_ID();
-- 
SET @sErrorMsg = '';
SELECT Func_CheckStaffDependency(@iID, @sErrorMsg) INTO @sErrorMsg;
SELECT IF(@sErrorMsg = '', '测试成功', '测试失败') AS 'Case11 Testing Result';
-- 
DELETE FROM t_staffrole WHERE F_StaffID = @iID;
DELETE FROM t_retailtradedailyreportbystaff WHERE F_ID = @iID2;
DELETE FROM t_staff WHERE F_ID = @iID;