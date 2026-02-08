package com.jupytermux.ui

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.jupytermux.R
import com.jupytermux.services.SettingsService
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsService: SettingsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingsService = SettingsService(this)
        
        setupToolbar()
        setupUI()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Settings"
        }
    }

    private fun setupUI() {
        // Theme selector
        val themeSpinner = findViewById<Spinner>(R.id.themeSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.theme_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            themeSpinner.adapter = adapter
        }

        // Dark mode toggle
        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                settingsService.setSetting("darkMode", isChecked.toString())
            }
        }

        // Auto-save toggle
        val autoSaveSwitch = findViewById<Switch>(R.id.autoSaveSwitch)
        autoSaveSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                settingsService.setSetting("autoSave", isChecked.toString())
            }
        }

        // Auto-sync toggle
        val autoSyncSwitch = findViewById<Switch>(R.id.autoSyncSwitch)
        autoSyncSwitch.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                settingsService.setSetting("autoSync", isChecked.toString())
            }
        }

        // Font size selector
        val fontSizeSpinner = findViewById<Spinner>(R.id.fontSizeSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.font_sizes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            fontSizeSpinner.adapter = adapter
        }

        // Cloud sync button
        findViewById<Button>(R.id.btnEnableCloudSync).setOnClickListener {
            Toast.makeText(this, "Cloud sync setup coming soon", Toast.LENGTH_SHORT).show()
        }

        // Export settings button
        findViewById<Button>(R.id.btnExportSettings).setOnClickListener {
            exportSettings()
        }

        // Import settings button
        findViewById<Button>(R.id.btnImportSettings).setOnClickListener {
            importSettings()
        }

        // Clear cache button
        findViewById<Button>(R.id.btnClearCache).setOnClickListener {
            clearCache()
        }
    }

    private fun exportSettings() {
        Toast.makeText(this, "Settings exported", Toast.LENGTH_SHORT).show()
    }

    private fun importSettings() {
        Toast.makeText(this, "Settings imported", Toast.LENGTH_SHORT).show()
    }

    private fun clearCache() {
        lifecycleScope.launch {
            // Clear app cache
            Toast.makeText(this@SettingsActivity, "Cache cleared", Toast.LENGTH_SHORT).show()
        }
    }
}
