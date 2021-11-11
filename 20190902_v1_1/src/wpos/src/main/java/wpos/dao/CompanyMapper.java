package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Company;

@Component("companyMapper")
public interface CompanyMapper extends JpaRepository<Company, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR(32) NOT NULL," +
            "F_SN VARCHAR(8) NOT NULL," +
            "F_BusinessLicenseSN VARCHAR(30)," +
            "F_BusinessLicensePicture VARCHAR(128) NULL," +
            "F_BossName VARCHAR(12) NOT NULL," +
            "F_BossPhone VARCHAR(32) NOT NULL," +
            "F_BossPassword VARCHAR(16) NOT NULL," +
            "F_BossWechat VARCHAR(20) NOT NULL," +
            "F_DBName Varchar(20) NOT NULL," +
            "F_Key VARCHAR(32) NOT NULL," +
            "F_DBUserName VARCHAR(20)," +
            "F_DBUserPassword VARCHAR(16)," +
            "F_Status INT NOT NULL," +
            "F_Submchid VARCHAR(10) NULL," +
            "F_BrandName VARCHAR(20)," +
            "F_CreateDatetime DATETIME," +
            "F_UpdateDatetime DATETIME," +
            "F_ExpireDatetime DATETIME," +
//            "F_ShowVipSystemTip INT NOT NULL DEFAULT 1," +
            "F_Logo VARCHAR(128) NULL" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Company (F_ID,F_Name,F_SN,F_BusinessLicenseSN,F_BusinessLicensePicture,F_BossName,F_BossPhone,F_BossPassword," +
            "F_BossWechat,F_DBName,F_Key,F_DBUserName,F_DBUserPassword,F_Status,F_Submchid,F_BrandName,F_CreateDatetime,F_UpdateDatetime," +
            "F_ExpireDatetime,F_Logo) VALUES (" +
            ":#{#c.ID},:#{#c.name},:#{#c.SN},:#{#c.businessLicenseSN},:#{#c.businessLicensePicture},:#{#c.bossName},:#{#c.bossPhone},:#{#c.bossPassword}," +
            ":#{#c.bossWechat},:#{#c.dbName},:#{#c.Key},:#{#c.dbUserName},:#{#c.dbUserPassword},:#{#c.status},:#{#c.submchid},:#{#c.brandName},:#{#c.createDatetime},:#{#c.updateDatetime}," +
            ":#{#c.expireDatetime},:#{#c.logo});", nativeQuery = true)
    void create(@Param("c") Company company);
}
