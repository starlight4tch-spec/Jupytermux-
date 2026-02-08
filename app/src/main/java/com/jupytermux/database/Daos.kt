package com.jupytermux.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.jupytermux.data.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotebookDao {
    @Insert
    suspend fun insert(notebook: NotebookEntity): Long

    @Update
    suspend fun update(notebook: NotebookEntity)

    @Delete
    suspend fun delete(notebook: NotebookEntity)

    @Query("SELECT * FROM notebooks WHERE id = :id")
    suspend fun getById(id: String): NotebookEntity?

    @Query("SELECT * FROM notebooks ORDER BY updated_at DESC")
    fun getAllNotebooks(): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks WHERE is_favorite = 1 ORDER BY updated_at DESC")
    fun getFavorites(): Flow<List<NotebookEntity>>

    @Query("SELECT * FROM notebooks WHERE name LIKE :query ORDER BY updated_at DESC")
    fun searchByName(query: String): Flow<List<NotebookEntity>>

    @Query("UPDATE notebooks SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)
}

@Dao
interface CellDao {
    @Insert
    suspend fun insert(cell: CellEntity): Long

    @Update
    suspend fun update(cell: CellEntity)

    @Delete
    suspend fun delete(cell: CellEntity)

    @Query("SELECT * FROM cells WHERE notebook_id = :notebookId ORDER BY cell_index ASC")
    suspend fun getCellsByNotebookId(notebookId: String): List<CellEntity>

    @Query("INSERT INTO cells (notebook_id, cell_index, cell_type, source) VALUES (:notebookId, :index, :type, :source)")
    suspend fun addCell(notebookId: String, index: Int, type: String, source: String)
}

@Dao
interface TerminalHistoryDao {
    @Insert
    suspend fun insert(history: TerminalHistoryEntity): Long

    @Query("SELECT * FROM terminal_history ORDER BY executed_at DESC LIMIT :limit")
    fun getRecent(limit: Int = 100): Flow<List<TerminalHistoryEntity>>

    @Query("SELECT * FROM terminal_history WHERE command LIKE :query ORDER BY executed_at DESC")
    fun searchHistory(query: String): Flow<List<TerminalHistoryEntity>>

    @Query("DELETE FROM terminal_history WHERE executed_at < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
}

@Dao
interface SettingDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(setting: SettingEntity)

    @Query("SELECT value FROM settings WHERE key = :key")
    suspend fun getValue(key: String): String?

    @Query("SELECT * FROM settings")
    suspend fun getAllSettings(): List<SettingEntity>
}

@Dao
interface FileMetadataDao {
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(metadata: FileMetadataEntity)

    @Query("SELECT * FROM file_metadata WHERE path = :path")
    suspend fun getByPath(path: String): FileMetadataEntity?

    @Query("SELECT * FROM file_metadata WHERE is_cached = 1")
    fun getCachedFiles(): Flow<List<FileMetadataEntity>>
}
