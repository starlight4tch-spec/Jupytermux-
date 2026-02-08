package com.jupytermux.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import android.widget.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jupytermux.R
import com.jupytermux.utils.NotebookParser
import java.io.File

class NotebookViewerActivity : AppCompatActivity() {
    private lateinit var webViewNotebook: WebView
    private var currentNotebookPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notebook_viewer)

        webViewNotebook = findViewById(R.id.webViewNotebook)
        
        setupWebView()
        setupToolbar()
        loadNotebookFromIntent()
    }

    private fun setupWebView() {
        webViewNotebook.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.notebook_viewer)
        }
    }

    private fun loadNotebookFromIntent() {
        val notebookPath = intent.getStringExtra("NOTEBOOK_PATH")
        if (notebookPath != null && File(notebookPath).exists()) {
            loadNotebook(notebookPath)
        } else {
            Toast.makeText(this, "Notebook not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNotebook(path: String) {
        currentNotebookPath = path
        try {
            val jsonContent = File(path).readText()
            val htmlContent = NotebookParser.parseNotebookToHtml(jsonContent)
            webViewNotebook.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading notebook: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_refresh -> {
                currentNotebookPath?.let { loadNotebook(it) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notebook_viewer, menu)
        return true
    }
}
