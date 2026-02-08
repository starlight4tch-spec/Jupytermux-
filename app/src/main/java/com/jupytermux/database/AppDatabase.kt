package com.jupytermux.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jupytermux.data.*

@Database(
    entities = [
        NotebookEntity::class,
        CellEntity::class,
        TerminalHistoryEntity::class,
        SettingEntity::class,
        FileMetadataEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notebookDao(): NotebookDao
    abstract fun cellDao(): CellDao
    abstract fun terminalHistoryDao(): TerminalHistoryDao
    abstract fun settingDao(): SettingDao
    abstract fun fileMetadataDao(): FileMetadataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jupytermux_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
