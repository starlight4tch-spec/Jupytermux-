package com.jupytermux.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jupytermux.R
import java.io.IOException

class TerminalActivity : AppCompatActivity() {
    private lateinit var terminalView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminal)

        terminalView = findViewById(R.id.terminalView)
        
        setupToolbar()
        initializeTerminal()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.terminal)
        }
    }

    private fun initializeTerminal() {
        // Initialize Termux terminal session
        // This will handle shell commands and Python execution
        try {
            startTerminalSession()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun startTerminalSession() {
        // TODO: Connect to Termux or start local shell
        terminalView.text = "$ Terminal ready\n"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup terminal session
    }
}
