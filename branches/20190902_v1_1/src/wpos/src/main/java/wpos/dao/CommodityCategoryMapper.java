package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.CommodityCategory;

@Repository
public interface CommodityCategoryMapper extends JpaRepository<CommodityCategory, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Name VARCHAR(10) NOT NULL," +
            "F_ParentID INT NOT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_UpdateDatetime DATETIME," +
            "F_SyncType VARCHAR(1)," +
            "F_SyncDatetime DATETIME" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_CommodityCategory (F_ID,F_Name,F_ParentID,F_CreateDatetime,F_UpdateDatetime,F_SyncType,F_SyncDatetime) VALUES (" +
            ":#{#category.ID}, :#{#category.name}, :#{#category.parentID}, :#{#category.createDatetime}, :#{#category.updateDatetime}, :#{#category.syncType}, :#{#category.syncDatetime}" +
            ");", nativeQuery = true)
    void create(@Param("category") CommodityCategory commodityCategory);
}
