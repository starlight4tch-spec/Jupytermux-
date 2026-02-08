package com.jupytermux.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import android.view.LayoutInflater
import com.jupytermux.R
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
    }

    private fun setupUI() {
        findViewById<MaterialButton>(R.id.btn_notebook_viewer).setOnClickListener {
            startActivity(Intent(this, NotebookViewerActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btn_terminal).setOnClickListener {
            startActivity(Intent(this, TerminalActivity::class.java))
        }

        lifecycleScope.launch {
            // Load user's recent notebooks and terminal sessions
            loadRecentItems()
        }
    }

    private suspend fun loadRecentItems() {
        // Load recent notebooks and sessions from local storage
        // This will be implemented with Room database
    }
}
