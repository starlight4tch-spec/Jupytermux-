package com.jupytermux.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.UUID

@Entity(tableName = "notebooks")
data class NotebookEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val path: String,
    val content: String,
    @ColumnInfo(name = "kernel_type")
    val kernelType: String = "python3",
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    @ColumnInfo(name = "cloud_id")
    val cloudId: String? = null
)

@Entity(tableName = "cells")
data class CellEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "notebook_id")
    val notebookId: String,
    @ColumnInfo(name = "cell_index")
    val cellIndex: Int,
    @ColumnInfo(name = "cell_type")
    val cellType: String,
    val source: String,
    val output: String? = null,
    @ColumnInfo(name = "execution_count")
    val executionCount: Int? = null,
    @ColumnInfo(name = "is_executing")
    val isExecuting: Boolean = false
)

@Entity(tableName = "terminal_history")
data class TerminalHistoryEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val command: String,
    val output: String?,
    @ColumnInfo(name = "exit_code")
    val exitCode: Int,
    @ColumnInfo(name = "execution_time")
    val executionTime: Long,
    @ColumnInfo(name = "executed_at")
    val executedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "kernel_type")
    val kernelType: String
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey
    val key: String,
    val value: String
)

@Entity(tableName = "file_metadata")
data class FileMetadataEntity(
    @PrimaryKey
    val path: String,
    @ColumnInfo(name = "last_synced")
    val lastSynced: Long? = null,
    @ColumnInfo(name = "is_cached")
    val isCached: Boolean = false,
    @ColumnInfo(name = "local_path")
    val localPath: String? = null
)
