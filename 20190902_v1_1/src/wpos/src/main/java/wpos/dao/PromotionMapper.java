package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.promotion.Promotion;

@Repository
public interface PromotionMapper extends JpaRepository<Promotion, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_SN VARCHAR(20) NOT NULL," +
            "F_Name VARCHAR(32) NOT NULL," +
            "F_Status INT  NOT NULL," +
            "F_Type INT NOT NULL," +
            "F_DatetimeStart DATETIME NOT NULL," +
            "F_DatetimeEnd DATETIME NOT NULL," +
            "F_ExcecutionThreshold DECIMAL(20, 6) NOT NULL," +
            "F_ExcecutionAmount DECIMAL(20, 6) NOT NULL," +
            "F_ExcecutionDiscount DECIMAL(20, 6) NOT NULL," +
            "F_Scope INT NOT NULL," +
            "F_Staff INT NOT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_SyncType VARCHAR(1)," +
            "F_SyncDatetime DATETIME" +
            ");", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO T_Promotion (F_ID,F_SN,F_Name,F_Status,F_Type,F_DatetimeStart,F_DatetimeEnd,F_ExcecutionThreshold," +
            "F_ExcecutionAmount,F_ExcecutionDiscount,F_Scope,F_Staff,F_CreateDatetime,F_SyncType,F_SyncDatetime) VALUES (" +
            ":#{#p.ID},:#{#p.sn},:#{#p.name},:#{#p.status},:#{#p.type},:#{#p.datetimeStart},:#{#p.datetimeEnd},:#{#p.excecutionThreshold}," +
            ":#{#p.excecutionAmount},:#{#p.excecutionDiscount},:#{#p.scope},:#{#p.staff},:#{#p.createDatetime},:#{#p.syncType},:#{#p.syncDatetime});", nativeQuery = true)
    void create(@Param("p") Promotion promotion);
}
