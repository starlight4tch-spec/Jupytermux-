package com.jupytermux.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.jupytermux.R
import com.jupytermux.database.AppDatabase
import com.jupytermux.data.TerminalHistoryEntity
import com.jupytermux.kernel.KernelType
import com.jupytermux.kernel.LocalKernelExecutor
import com.jupytermux.kernel.ExecutionRequest
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class EnhancedTerminalActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var terminal: TextView
    private lateinit var commandInput: EditText
    private lateinit var kernelSpinner: Spinner
    
    private var commandHistory = mutableListOf<String>()
    private var historyIndex = -1
    private var currentKernel = KernelType.PYTHON3
    
    private lateinit var executor: LocalKernelExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enhanced_terminal)

        db = AppDatabase.getInstance(this)
        executor = LocalKernelExecutor(currentKernel)
        
        setupToolbar()
        setupUI()
        loadCommandHistory()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Terminal"
        }
    }

    private fun setupUI() {
        terminal = findViewById(R.id.terminalOutput)
        commandInput = findViewById(R.id.commandInput)
        kernelSpinner = findViewById(R.id.kernelSpinner)

        // Setup kernel spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.kernel_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            kernelSpinner.adapter = adapter
        }

        kernelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val kernelName = (parent.getItemAtPosition(pos) as String).uppercase()
                currentKernel = KernelType.valueOf(kernelName.replace(" ", "_"))
                executor = LocalKernelExecutor(currentKernel)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Execute button
        findViewById<Button>(R.id.btnExecute).setOnClickListener {
            executeCommand()
        }

        // Clear button
        findViewById<Button>(R.id.btnClearTerminal).setOnClickListener {
            terminal.text = ""
        }

        // History up/down buttons
        findViewById<Button>(R.id.btnHistoryUp).setOnClickListener {
            showPreviousCommand()
        }

        findViewById<Button>(R.id.btnHistoryDown).setOnClickListener {
            showNextCommand()
        }

        // Copy output button
        findViewById<Button>(R.id.btnCopyOutput).setOnClickListener {
            copyToClipboard(terminal.text.toString())
        }

        // Long press for context menu
        terminal.setOnLongClickListener {
            showTerminalContextMenu()
            true
        }

        terminal.append("$ Jupytermux Terminal\n$ Type your commands below\n\n")
    }

    private fun executeCommand() {
        val command = commandInput.text.toString().trim()
        if (command.isEmpty()) return

        commandInput.text.clear()
        terminal.append("$ $command\n")
        commandHistory.add(command)
        historyIndex = commandHistory.size

        lifecycleScope.launch {
            try {
                val request = ExecutionRequest(
                    code = command,
                    kernelType = currentKernel,
                    timeout = 30000
                )
                val result = executor.execute(request)
                
                result.outputs.forEach { output ->
                    terminal.append("${output}\n")
                }

                // Save to history
                val history = TerminalHistoryEntity(
                    command = command,
                    output = result.outputs.toString(),
                    exitCode = if (result.success) 0 else 1,
                    executionTime = result.executionTime,
                    kernelType = currentKernel.name
                )
                db.terminalHistoryDao().insert(history)
            } catch (e: Exception) {
                terminal.append("Error: ${e.message}\n")
            }
        }
    }

    private fun loadCommandHistory() {
        lifecycleScope.launch {
            db.terminalHistoryDao().getRecent(100).collect { history ->
                commandHistory.clear()
                commandHistory.addAll(history.map { it.command })
            }
        }
    }

    private fun showPreviousCommand() {
        if (historyIndex > 0) {
            historyIndex--
            commandInput.setText(commandHistory[historyIndex])
            commandInput.setSelection(commandHistory[historyIndex].length)
        }
    }

    private fun showNextCommand() {
        if (historyIndex < commandHistory.size - 1) {
            historyIndex++
            commandInput.setText(commandHistory[historyIndex])
            commandInput.setSelection(commandHistory[historyIndex].length)
        } else if (historyIndex == commandHistory.size - 1) {
            historyIndex = commandHistory.size
            commandInput.setText("")
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Terminal Output", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun showTerminalContextMenu() {
        val options = arrayOf("Copy Output", "Clear All", "Search History", "Export")
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> copyToClipboard(terminal.text.toString())
                    1 -> terminal.text = ""
                    2 -> showSearchHistoryDialog()
                    3 -> exportTerminalLog()
                }
            }
            .show()
    }

    private fun showSearchHistoryDialog() {
        val searchInput = EditText(this)
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Search History")
            .setView(searchInput)
            .setPositiveButton("Search") { _, _ ->
                lifecycleScope.launch {
                    db.terminalHistoryDao().searchHistory("%${searchInput.text}%").collect { results ->
                        terminal.append("\n--- Search Results ---\n")
                        results.forEach {
                            terminal.append("${it.command}\n")
                        }
                    }
                }
            }
            .show()
    }

    private fun exportTerminalLog() {
        // Export terminal output to file
        Toast.makeText(this, "Export functionality coming soon", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.shutdown()
    }
}
