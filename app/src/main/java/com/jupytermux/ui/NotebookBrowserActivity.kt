package com.jupytermux.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jupytermux.R
import com.jupytermux.database.AppDatabase
import com.jupytermux.data.NotebookEntity
import com.jupytermux.ui.adapters.NotebookBrowserAdapter
import kotlinx.coroutines.launch

class NotebookBrowserActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: NotebookBrowserAdapter
    private lateinit var recyclerView: RecyclerView
    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notebook_browser)

        db = AppDatabase.getInstance(this)
        
        setupToolbar()
        setupUI()
        loadNotebooks()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "My Notebooks"
        }
    }

    private fun setupUI() {
        recyclerView = findViewById(R.id.notebookRecyclerView)
        adapter = NotebookBrowserAdapter(
            onNotebookClick = { notebook ->
                val intent = Intent(this, NotebookEditorActivity::class.java)
                intent.putExtra("NOTEBOOK_ID", notebook.id)
                startActivity(intent)
            },
            onDeleteClick = { notebook ->
                deleteNotebook(notebook)
            },
            onFavoriteClick = { notebook ->
                toggleFavorite(notebook)
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnCreateNotebook).setOnClickListener {
            showCreateNotebookDialog()
        }

        val searchBar = findViewById<EditText>(R.id.searchBar)
        searchBar.setOnTextChangedListener { query ->
            searchQuery = query
            if (query.isNotEmpty()) {
                searchNotebooks(query)
            } else {
                loadNotebooks()
            }
        }

        findViewById<Button>(R.id.btnShowFavorites).setOnClickListener {
            loadFavorites()
        }
    }

    private fun loadNotebooks() {
        lifecycleScope.launch {
            db.notebookDao().getAllNotebooks().collect { notebooks ->
                adapter.submitList(notebooks)
            }
        }
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            db.notebookDao().getFavorites().collect { notebooks ->
                adapter.submitList(notebooks)
            }
        }
    }

    private fun searchNotebooks(query: String) {
        lifecycleScope.launch {
            db.notebookDao().searchByName("%$query%").collect { notebooks ->
                adapter.submitList(notebooks)
            }
        }
    }

    private fun showCreateNotebookDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Create Notebook")
            .setMessage("Enter notebook name:")
            .setView(EditText(this).apply {
                hint = "My Notebook"
            })
            .setPositiveButton("Create") { _, _ ->
                // Create new notebook
                val nameEditText = (it as? android.app.AlertDialog)?.findViewById<EditText>(android.R.id.edit)
                val name = nameEditText?.text.toString().ifEmpty { "Untitled" }
                createNotebook(name)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createNotebook(name: String) {
        lifecycleScope.launch {
            val notebook = NotebookEntity(name = name, path = "", content = "")
            db.notebookDao().insert(notebook)
            Toast.makeText(this@NotebookBrowserActivity, "Notebook created", Toast.LENGTH_SHORT).show()
            loadNotebooks()
        }
    }

    private fun deleteNotebook(notebook: NotebookEntity) {
        lifecycleScope.launch {
            db.notebookDao().delete(notebook)
            Toast.makeText(this@NotebookBrowserActivity, "Notebook deleted", Toast.LENGTH_SHORT).show()
            loadNotebooks()
        }
    }

    private fun toggleFavorite(notebook: NotebookEntity) {
        lifecycleScope.launch {
            db.notebookDao().setFavorite(notebook.id, !notebook.isFavorite)
            loadNotebooks()
        }
    }
}

// Extension for EditText to listen to text changes
fun EditText.setOnTextChangedListener(callback: (String) -> Unit) {
    addTextChangedListener(object : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback(s.toString())
        }
        override fun afterTextChanged(s: android.text.Editable?) {}
    })
}
