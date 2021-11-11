package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.BaseModel;
import wpos.model.Vip;

import java.util.List;

@Repository
public interface VipMapper extends JpaRepository<Vip, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_ICID VARCHAR(30) NULL," +
            "F_Mobile VARCHAR(11) NOT NULL," +
            "F_Name VARCHAR(32) NOT NULL," +
            "F_Email VARCHAR(30) NULL," +
            "F_ConsumeTimes INT NOT NULL," +
            "F_ConsumeAmount Decimal(20,6) NOT NULL," +
            "F_District VARCHAR(30) NOT NULL," +
            "F_Category INT NOT NULL," +
            "F_Birthday DATETIME NULL," +
            "F_Bonus INT NOT NULL," +
            "F_LastConsumeDatetime DATETIME NULL," +
            "F_LocalPosSN VARCHAR(32)," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)," +
            "F_CardID INT NOT NULL" +
            ");", nativeQuery = true)
    void createTable();
}
