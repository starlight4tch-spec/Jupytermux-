package com.jupytermux.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jupytermux.R
import com.jupytermux.services.*
import com.jupytermux.utils.KeyboardShortcuts

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_full)

        setupButtons()
    }

    private fun setupButtons() {
        // Notebook Viewer
        findViewById<Button>(R.id.btn_notebook_viewer).setOnClickListener {
            startActivity(Intent(this, NotebookBrowserActivity::class.java))
        }

        // Notebook Editor
        findViewById<Button>(R.id.btn_notebook_editor).setOnClickListener {
            startActivity(Intent(this, NotebookBrowserActivity::class.java))
        }

        // Terminal
        findViewById<Button>(R.id.btn_terminal).setOnClickListener {
            startActivity(Intent(this, EnhancedTerminalActivity::class.java))
        }

        // Settings
        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // About
        findViewById<Button>(R.id.btn_about).setOnClickListener {
            showAboutDialog()
        }

        // Keyboard Shortcuts
        findViewById<Button>(R.id.btn_shortcuts).setOnClickListener {
            showShortcutsDialog()
        }
    }

    private fun showAboutDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("About Jupytermux")
            .setMessage("""
                Jupytermux v1.0.0
                
                A powerful Jupyter Notebook & Terminal client for Android
                
                Features:
                • Multi-language kernel support
                • Cloud sync
                • Git integration
                • SSH access
                • Advanced editing
                
                © 2025 Jupytermux Team
            """.trimIndent())
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showShortcutsDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Keyboard Shortcuts")
            .setMessage(KeyboardShortcuts.getShortcutDescription())
            .setPositiveButton("OK", null)
            .show()
    }
}
