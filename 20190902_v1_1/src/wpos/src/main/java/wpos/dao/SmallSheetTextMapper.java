package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.SmallSheetFrame;
import wpos.model.SmallSheetText;

@Repository
public interface SmallSheetTextMapper extends JpaRepository<SmallSheetText, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Content VARCHAR(100) NOT NULL," +
            "F_Size Decimal(20,6) NOT NULL," +
            "F_Bold INT NOT NULL," +
            "F_Gravity INT NOT NULL," +
            "F_FrameID INT NOT NULL," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType VARCHAR(1)" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_SmallSheetText (F_ID, F_Content, F_Size, F_Bold, F_Gravity, F_FrameID, F_SyncDatetime, F_SyncType) VALUES " +
            "(:#{#smallSheetText.ID}, :#{#smallSheetText.content}, :#{#smallSheetText.size}, " +
            ":#{#smallSheetText.bold}, :#{#smallSheetText.gravity}, :#{#smallSheetText.frameId}, " +
            ":#{#smallSheetText.syncDatetime}, :#{#smallSheetText.syncType});", nativeQuery = true)
    void create(@Param("smallSheetText") SmallSheetText smallSheetText);
}
