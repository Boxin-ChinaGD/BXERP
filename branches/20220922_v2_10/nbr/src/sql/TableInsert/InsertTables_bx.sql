
-- 博昕客户信息
delete from nbr_bx.T_Company;
INSERT INTO nbr_bx.t_company (F_Name,F_SN,F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_DBUserName, F_DBUserPassword, F_Key, F_Status, F_Submchid, F_BrandName, F_ExpireDatetime)
VALUES ('BX一号分公司','668866','111111111111111','/p/common_db/license/nbr.jpg', '/p/common_db/logo/nbr.jpg', '老板一号','13123615881','000000','a13123615881','nbr','nbr','WEF#EGEHEH$$^*DI', '12345678901234567890123456789012', 0, '1574431111', '博昕虚拟门店', '2030-12-02 1:01:01');
INSERT INTO nbr_bx.T_Company (F_Name,F_SN,F_BusinessLicenseSN, F_BusinessLicensePicture, F_Logo, F_BossName, F_BossPhone, F_BossPassword, F_BossWechat, F_DBName, F_DBUserName, F_DBUserPassword, F_Key, F_Status, F_BrandName, F_ExpireDatetime)
VALUES ('BX二号分公司','668867','222222222222222','/p/common_db/license/nbr_bx.jpg', '/p/common_db/logo/nbr_bx.jpg', '老板二号','13133565881','000000','a12341513215','nbr_bx','nbr_bx','WEF#EGEHEH$$^*DI','12345678901234567890123456789013', 0, '博昕客户公共DB', '2030-12-02 1:01:01');

-- 博昕内部员工表
delete from nbr_bx.T_BXStaff;
INSERT INTO nbr_bx.T_BXStaff (F_Mobile,F_Salt,F_RoleID,F_DepartmentID,F_Name,F_Sex,F_ICID)
VALUES ('13185246281','B1AFC07474C37C5AEC4199ED28E09705',1,1,'a1',1,'440883198411111222');
INSERT INTO nbr_bx.T_BXStaff (F_Mobile,F_Salt,F_RoleID,F_DepartmentID,F_Name,F_Sex,F_ICID)
VALUES ('13462346281','B1AFC07474C37C5AEC4199ED28E09705',1,1,'a2',0,'440883198411111333');
INSERT INTO nbr_bx.T_BXStaff (F_Mobile,F_Salt,F_RoleID,F_DepartmentID,F_Name,F_Sex,F_ICID)
VALUES ('13125231281','B1AFC07474C37C5AEC4199ED28E09705',1,1,'a3',0,'440883198411111444');
INSERT INTO nbr_bx.T_BXStaff (F_Mobile,F_Salt,F_RoleID,F_DepartmentID,F_Name,F_Sex,F_ICID)
VALUES ('13128246281','B1AFC07474C37C5AEC4199ED28E09705',1,1,'a4',1,'440883198411111555');
INSERT INTO nbr_bx.T_BXStaff (F_Mobile,F_Salt,F_RoleID,F_DepartmentID,F_Name,F_Sex,F_ICID)
VALUES ('13163949281','B1AFC07474C37C5AEC4199ED28E09705',1,1,'a5',1,'440883198411111666');