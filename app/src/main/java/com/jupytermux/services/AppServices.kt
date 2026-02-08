package com.jupytermux.services

import android.content.Context
import com.jupytermux.database.AppDatabase
import com.jupytermux.data.SettingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsService(context: Context) {
    private val db = AppDatabase.getInstance(context)
    
    private val _theme = MutableStateFlow("material3")
    val theme: StateFlow<String> = _theme

    private val _fontSize = MutableStateFlow(14)
    val fontSize: StateFlow<Int> = _fontSize

    private val _darkMode = MutableStateFlow(true)
    val darkMode: StateFlow<Boolean> = _darkMode

    private val _autoSave = MutableStateFlow(true)
    val autoSave: StateFlow<Boolean> = _autoSave

    private val _autoSync = MutableStateFlow(true)
    val autoSync: StateFlow<Boolean> = _autoSync

    suspend fun setSetting(key: String, value: String) {
        db.settingDao().insert(SettingEntity(key, value))
        
        when (key) {
            "theme" -> _theme.emit(value)
            "fontSize" -> _fontSize.emit(value.toIntOrNull() ?: 14)
            "darkMode" -> _darkMode.emit(value.toBoolean())
            "autoSave" -> _autoSave.emit(value.toBoolean())
            "autoSync" -> _autoSync.emit(value.toBoolean())
        }
    }

    suspend fun getSetting(key: String): String? = db.settingDao().getValue(key)

    suspend fun loadAllSettings() {
        db.settingDao().getAllSettings().forEach { setting ->
            setSetting(setting.key, setting.value)
        }
    }
}

class SearchService(context: Context) {
    private val db = AppDatabase.getInstance(context)

    fun searchNotebooks(query: String) = db.notebookDao().searchByName("%$query%")

    fun searchHistory(query: String) = db.terminalHistoryDao().searchHistory("%$query%")

    suspend fun globalSearch(query: String): Map<String, Any> {
        return mapOf(
            "notebooks" to (db.notebookDao().searchByName("%$query%")).toString(),
            "commands" to (db.terminalHistoryDao().searchHistory("%$query%")).toString()
        )
    }
}

class ExtensionService(context: Context) {
    private val extensions = mutableListOf<JupyterExtension>()

    fun loadExtension(manifest: ExtensionManifest): Boolean {
        return try {
            // TODO: Load Jupyter extensions
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getLoadedExtensions(): List<JupyterExtension> = extensions

    suspend fun installExtension(url: String): Boolean {
        return try {
            // TODO: Download and install extension
            true
        } catch (e: Exception) {
            false
        }
    }
}

data class JupyterExtension(
    val name: String,
    val version: String,
    val path: String
)

data class ExtensionManifest(
    val name: String,
    val version: String,
    val main: String
)
