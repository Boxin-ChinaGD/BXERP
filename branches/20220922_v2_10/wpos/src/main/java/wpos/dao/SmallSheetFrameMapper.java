package wpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wpos.model.Brand;
import wpos.model.SmallSheetFrame;

@Repository
public interface SmallSheetFrameMapper extends JpaRepository<SmallSheetFrame, Integer> {
    @Transactional
    @Modifying
    @Query(value = "CREATE TABLE T_#{#entityName}(" +
            "F_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "F_Logo MEDIUMTEXT NULL," +
            "F_CreateDatetime DATETIME," +
            "F_UpdateDatetime DATETIME," +
            "F_CountOfBlankLineAtBottom INT," +
            "F_DelimiterToRepeat varchar(1) default '-'," +
            "F_SyncDatetime DATETIME," +
            "F_SyncType varchar(1), " +
            "F_SlaveCreated INT default 0" +
            ");", nativeQuery = true)
    void createTable();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO T_SmallSheetFrame (F_ID, F_Logo, F_CreateDatetime, F_UpdateDatetime, F_CountOfBlankLineAtBottom, F_DelimiterToRepeat, " +
            "F_SyncDatetime, F_SyncType) VALUES (:#{#smallSheetFrame.ID}, :#{#smallSheetFrame.logo}, :#{#smallSheetFrame.createDatetime}, " +
            ":#{#smallSheetFrame.updateDatetime}, :#{#smallSheetFrame.countOfBlankLineAtBottom}, :#{#smallSheetFrame.delimiterToRepeat}, " +
            ":#{#smallSheetFrame.syncDatetime}, :#{#smallSheetFrame.syncType});", nativeQuery = true)
    void create(@Param("smallSheetFrame") SmallSheetFrame smallSheetFrame);
}
