package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.promotion.PromotionScope;

@Repository
public interface PromotionScopeMapper extends JpaRepository<PromotionScope, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_PromotionID INT," +
            "F_CommodityID INT DEFAULT NULL," +
            "F_CommodityName VARCHAR (32) NOT NULL," +
            "F_SyncType VARCHAR(1)," +
            "F_SyncDatetime DATETIME" +
            ");", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO T_PromotionScope (F_ID,F_PromotionID,F_CommodityID,F_CommodityName,F_SyncType,F_SyncDatetime) VALUES (" +
            ":#{#p.ID},:#{#p.promotionID},:#{#p.commodityID},:#{#p.commodityName},:#{#p.syncType},:#{#p.syncDatetime});", nativeQuery = true)
    void create(@Param("p") PromotionScope promotionScope);
}
