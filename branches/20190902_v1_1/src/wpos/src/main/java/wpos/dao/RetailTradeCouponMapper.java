package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RetailTradeCoupon;

@Repository
public interface RetailTradeCouponMapper extends JpaRepository<RetailTradeCoupon, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_RetailTradeID INT NOT NULL," +
            "F_CouponCodeID INT NOT NULL," +
            "F_SyncDatetime DATETIME" +
            ");", nativeQuery = true)
    void createTable();
}
