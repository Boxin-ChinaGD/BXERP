package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.RememberLoginStaff;

@Repository
public interface RememberLoginStaffMapper extends JpaRepository<RememberLoginStaff, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Phone VARCHAR(32) NOT NULL," +
            "F_Password VARCHAR(32) NOT NULL," +
            "F_Remembered int" +
            ");", nativeQuery = true)
    void createTable();
}