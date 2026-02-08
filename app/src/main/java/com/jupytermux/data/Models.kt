package com.jupytermux.data

import java.io.File

data class Notebook(
    val id: String,
    val name: String,
    val path: String,
    val lastModified: Long,
    val createdAt: Long
)

data class TerminalSession(
    val id: String,
    val name: String,
    val createdAt: Long
)

class NotebookRepository {
    fun getAllNotebooks(baseDir: File): List<Notebook> {
        val notebooks = mutableListOf<Notebook>()
        baseDir.walkTopDown().forEach { file ->
            if (file.extension == "ipynb") {
                notebooks.add(
                    Notebook(
                        id = file.absolutePath,
                        name = file.nameWithoutExtension,
                        path = file.absolutePath,
                        lastModified = file.lastModified(),
                        createdAt = file.lastModified()
                    )
                )
            }
        }
        return notebooks.sortedByDescending { it.lastModified }
    }

    fun deleteNotebook(path: String): Boolean {
        return File(path).delete()
    }

    fun renameNotebook(oldPath: String, newName: String): Boolean {
        val oldFile = File(oldPath)
        val newFile = File(oldFile.parent, "$newName.ipynb")
        return oldFile.renameTo(newFile)
    }
}
