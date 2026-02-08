package com.jupytermux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jupytermux.R
import com.jupytermux.database.AppDatabase
import com.jupytermux.data.CellEntity
import com.jupytermux.data.NotebookEntity
import com.jupytermux.kernel.ExecutionRequest
import com.jupytermux.kernel.KernelType
import com.jupytermux.kernel.LocalKernelExecutor
import com.jupytermux.ui.adapters.NotebookCellAdapter
import kotlinx.coroutines.launch

class NotebookEditorActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: NotebookCellAdapter
    private lateinit var cellRecyclerView: RecyclerView
    private lateinit var kernelExecutor: LocalKernelExecutor
    
    private var currentNotebookId: String? = null
    private var currentKernelType: KernelType = KernelType.PYTHON3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notebook_editor)

        db = AppDatabase.getInstance(this)
        kernelExecutor = LocalKernelExecutor(currentKernelType)
        
        setupToolbar()
        setupUI()
        loadNotebookFromIntent()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Notebook Editor"
        }
    }

    private fun setupUI() {
        cellRecyclerView = findViewById(R.id.cellRecyclerView)
        adapter = NotebookCellAdapter { position, cell ->
            // Execute cell
            executeCellAtPosition(position)
        }
        cellRecyclerView.adapter = adapter
        cellRecyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAddCellCode).setOnClickListener {
            addNewCell("code")
        }

        findViewById<Button>(R.id.btnAddCellMarkdown).setOnClickListener {
            addNewCell("markdown")
        }

        findViewById<Button>(R.id.btnSaveNotebook).setOnClickListener {
            saveCurrentNotebook()
        }

        findViewById<Button>(R.id.btnExecuteAll).setOnClickListener {
            executeAllCells()
        }
    }

    private fun loadNotebookFromIntent() {
        currentNotebookId = intent.getStringExtra("NOTEBOOK_ID")
        if (currentNotebookId != null) {
            lifecycleScope.launch {
                val notebook = db.notebookDao().getById(currentNotebookId!!)
                if (notebook != null) {
                    currentKernelType = KernelType.valueOf(notebook.kernelType)
                    val cells = db.cellDao().getCellsByNotebookId(notebook.id)
                    adapter.submitList(cells)
                }
            }
        }
    }

    private fun addNewCell(type: String) {
        lifecycleScope.launch {
            currentNotebookId?.let { notebookId ->
                val cellCount = db.cellDao().getCellsByNotebookId(notebookId).size
                db.cellDao().addCell(notebookId, cellCount, type, "")
                loadNotebookFromIntent()
            }
        }
    }

    private fun executeCellAtPosition(position: Int) {
        lifecycleScope.launch {
            val cell = adapter.currentList.getOrNull(position) ?: return@launch
            if (cell.cellType != "code") return@launch

            val request = ExecutionRequest(
                code = cell.source,
                kernelType = currentKernelType,
                timeout = 30000
            )

            val result = kernelExecutor.execute(request)
            
            // Update cell with execution result
            val updatedCell = cell.copy(
                output = result.outputs.toString(),
                executionCount = result.executionCount,
                isExecuting = false
            )
            db.cellDao().update(updatedCell)
            adapter.notifyItemChanged(position)
        }
    }

    private fun executeAllCells() {
        lifecycleScope.launch {
            adapter.currentList.forEachIndexed { index, cell ->
                if (cell.cellType == "code") {
                    executeCellAtPosition(index)
                }
            }
        }
    }

    private fun saveCurrentNotebook() {
        lifecycleScope.launch {
            currentNotebookId?.let { notebookId ->
                val notebook = db.notebookDao().getById(notebookId) ?: return@launch
                db.notebookDao().update(notebook.copy(updatedAt = System.currentTimeMillis()))
                Toast.makeText(this@NotebookEditorActivity, "Notebook saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        kernelExecutor.shutdown()
    }
}
