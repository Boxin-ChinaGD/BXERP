package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Coupon;

@Repository
public interface CouponMapper extends JpaRepository<Coupon, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Status INT NOT NULL," +
            "F_Type INT NOT NULL," +
            "F_Bonus INT NOT NULL," +
            "F_LeastAmount Decimal(20, 6) NOT NULL DEFAULT 0," +
            "F_ReduceAmount Decimal(20, 6) NOT NULL," +
            "F_Discount Decimal(20, 6) NOT NULL," +
            "F_Title VARCHAR(9) NOT NULL," +
            "F_Color VARCHAR(16) NOT NULL," +
            "F_Description VARCHAR(1024) NOT NULL," +
            "F_PersonalLimit INT NOT NULL," +
            "F_WeekDayAvailable INT NULL," +
            "F_BeginTime VARCHAR(8)," +
            "F_EndTime VARCHAR(8)," +
            "F_BeginDateTime DATETIME," +
            "F_EndDateTime DATETIME," +
            "F_Quantity INT NOT NULL," +
            "F_RemainingQuantity INT NOT NULL," +
            "F_Scope INT NOT NULL" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Coupon (F_ID,F_Status,F_Type,F_Bonus,F_LeastAmount,F_ReduceAmount,F_Discount,F_Title,F_Color,F_Description," +
            "F_PersonalLimit,F_WeekDayAvailable,F_BeginTime,F_EndTime,F_BeginDateTime,F_EndDateTime,F_Quantity,F_RemainingQuantity,F_Scope) VALUES (" +
            ":#{#c.ID},:#{#c.status},:#{#c.type},:#{#c.bonus},:#{#c.leastAmount},:#{#c.reduceAmount},:#{#c.discount},:#{#c.title},:#{#c.color},:#{#c.description}," +
            ":#{#c.personalLimit},:#{#c.weekDayAvailable},:#{#c.beginTime},:#{#c.endTime},:#{#c.beginDateTime},:#{#c.endDateTime},:#{#c.quantity},:#{#c.remainingQuantity},:#{#c.scope});", nativeQuery = true)
    void create(@Param("c") Coupon coupon);
}

