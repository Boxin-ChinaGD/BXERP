package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.promotion.PromotionScope;
import wpos.model.promotion.PromotionShopScope;

@Repository
public interface PromotionShopScopeMapper extends JpaRepository<PromotionShopScope, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_PromotionID INT," +
            "F_ShopID INT DEFAULT NULL," +
            "F_ShopName VARCHAR (32) NOT NULL," +
            "F_SyncType VARCHAR(1)," +
            "F_SyncDatetime DATETIME" +
            ");", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO T_PromotionShopScope (F_ID,F_PromotionID,F_ShopID,F_ShopName,F_SyncType,F_SyncDatetime) VALUES (" +
            ":#{#p.ID},:#{#p.promotionID},:#{#p.shopID},:#{#p.shopName},:#{#p.syncType},:#{#p.syncDatetime});", nativeQuery = true)
    void create(@Param("p") PromotionShopScope promotionShopScope);
}
