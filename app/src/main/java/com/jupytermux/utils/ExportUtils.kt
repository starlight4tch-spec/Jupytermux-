package com.jupytermux.utils

import android.content.Context
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File

object ExportService {
    fun exportToPDF(content: String, outputPath: String): Boolean {
        return try {
            val document = Document()
            PdfWriter.getInstance(document, java.io.FileOutputStream(outputPath))
            document.open()
            document.add(Paragraph(content))
            document.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun exportToHTML(title: String, content: String, outputPath: String): Boolean {
        return try {
            val html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>$title</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 20px; }
                        .code { background: #f5f5f5; padding: 10px; border-radius: 4px; }
                    </style>
                </head>
                <body>
                    <h1>$title</h1>
                    $content
                </body>
                </html>
            """.trimIndent()
            File(outputPath).writeText(html)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun exportToMarkdown(content: String, outputPath: String): Boolean {
        return try {
            File(outputPath).writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun exportToJSON(content: String, outputPath: String): Boolean {
        return try {
            val json = com.google.gson.Gson().toJson(content)
            File(outputPath).writeText(json)
            true
        } catch (e: Exception) {
            false
        }
    }
}

object MarkdownPreviewService {
    fun parseMarkdown(markdown: String): String {
        return try {
            val markwon = io.noties.markwon.Markwon.createDefault(android.app.Application())
            // Convert markdown to HTML
            markdown.replace("\n", "<br>")
                .replace("**(.+?)**".toRegex(), "<b>$1</b>")
                .replace("_(.+?)_".toRegex(), "<i>$1</i>")
        } catch (e: Exception) {
            markdown
        }
    }
}

object KeyboardShortcuts {
    data class Shortcut(val key: String, val action: String)

    val shortcuts = listOf(
        Shortcut("Ctrl+S", "Save Notebook"),
        Shortcut("Ctrl+Enter", "Execute Cell"),
        Shortcut("Shift+Enter", "Execute & Move Down"),
        Shortcut("Alt+Enter", "Execute & Insert Below"),
        Shortcut("Ctrl+/", "Toggle Comment"),
        Shortcut("Ctrl+F", "Find & Replace"),
        Shortcut("Tab", "Indent"),
        Shortcut("Shift+Tab", "Dedent"),
        Shortcut("Ctrl+Z", "Undo"),
        Shortcut("Ctrl+Y", "Redo"),
        Shortcut("Ctrl+D", "Delete Cell"),
        Shortcut("Ctrl+M", "Edit Mode"),
        Shortcut("Escape", "Command Mode")
    )

    fun getShortcutDescription(): String {
        return shortcuts.joinToString("\n") { "${it.key} - ${it.action}" }
    }
}
