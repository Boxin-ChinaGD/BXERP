package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Brand;
import wpos.model.Staff;

@Repository
public interface StaffMapper extends JpaRepository<Staff, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR(12) NOT NULL," +
            "F_Phone VARCHAR(32) NOT NULL," +
            "F_ICID VARCHAR(20) NULL," +
            "F_WeChat VARCHAR(20) NULL," +
            "F_OpenID VARCHAR(100)  NULL," +
            "F_PwdEncrypted VARCHAR(0) NULL," +
            "F_Salt VARCHAR(32)," +
            "F_PasswordExpireDate DATETIME NULL," +
            "F_IsFirstTimeLogin INT DEFAULT 1," +
            "F_ShopID INT," +
            "F_DepartmentID INT," +
            "F_Status INT NOT NULL," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)," +
            "F_RoleID INT," +
            "F_PasswordInPOS VARCHAR(32)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Staff (F_ID,F_Name,F_Phone,F_ICID, F_WeChat, F_OpenID, F_PwdEncrypted, F_Salt, F_PasswordExpireDate, F_IsFirstTimeLogin" +
            ", F_ShopID, F_DepartmentID, F_Status, F_SyncDatetime, F_SyncType, F_RoleID, F_PasswordInPOS) VALUES (" +
            ":#{#staff.ID}, :#{#staff.name}, :#{#staff.phone}, :#{#staff.ICID}, :#{#staff.weChat}, :#{#staff.openID}, :#{#staff.pwdEncrypted}, :#{#staff.salt}" +
            ", :#{#staff.passwordExpireDate}, :#{#staff.isFirstTimeLogin}, :#{#staff.shopID}, :#{#staff.departmentID}, :#{#staff.status}, :#{#staff.syncDatetime}, " +
            ":#{#staff.syncType}, :#{#staff.roleID}, :#{#staff.passwordInPOS}" +
            ");", nativeQuery = true)
    void create(@Param("staff") Staff staff);
}
