package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.CouponScope;

@Repository
public interface CouponScopeMapper extends JpaRepository<CouponScope, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_CouponID INT NOT NULL," +
            "F_CommodityID INT NOT NULL," +
            "F_CommodityName VARCHAR(32) NOT NULL" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_CouponScope (F_ID,F_CouponID,F_CommodityID,F_CommodityName) VALUES (" +
            ":#{#c.ID},:#{#c.couponID},:#{#c.commodityID},:#{#c.commodityName});", nativeQuery = true)
    void create(@Param("c") CouponScope couponScope);
}
