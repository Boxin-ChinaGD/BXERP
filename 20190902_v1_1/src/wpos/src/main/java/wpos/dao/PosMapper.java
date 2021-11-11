package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Pos;

@Repository
public interface PosMapper extends JpaRepository<Pos, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_POS_SN VARCHAR(32) NOT NULL," +
            "F_ShopID INT NOT NULL," +
            "F_pwdEncrypted VARCHAR(0) NULL," +
            "F_Salt VARCHAR(32)," +
            "F_PasswordInPOS VARCHAR(16) NULL," +
            "F_Status INT NOT NULL," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_Pos (F_ID,F_POS_SN,F_ShopID,F_pwdEncrypted,F_Salt,F_PasswordInPOS,F_Status,F_SyncDatetime,F_SyncType) VALUES (" +
            ":#{#p.ID},:#{#p.pos_SN},:#{#p.shopID},:#{#p.pwdEncrypted},:#{#p.salt},:#{#p.passwordInPOS},:#{#p.status},:#{#p.syncDatetime},:#{#p.syncType});", nativeQuery = true)
    void create(@Param("p") Pos pos);
}
