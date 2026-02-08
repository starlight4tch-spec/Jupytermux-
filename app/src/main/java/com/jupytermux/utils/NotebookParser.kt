package com.jupytermux.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

object NotebookParser {
    private val gson = Gson()

    fun parseNotebookToHtml(jsonContent: String): String {
        return try {
            val notebook = JsonParser.parseString(jsonContent).asJsonObject
            val cells = notebook.getAsJsonArray("cells")
            
            val htmlBuilder = StringBuilder()
            htmlBuilder.append("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; padding: 20px; }
                        .cell { margin-bottom: 20px; border-left: 3px solid #6750a4; padding-left: 15px; }
                        .cell-type { color: #666; font-size: 0.85em; }
                        .code-cell { background: #f5f5f5; padding: 10px; border-radius: 4px; font-family: monospace; overflow-x: auto; }
                        .markdown-cell { line-height: 1.6; }
                        .output { background: #f0f0f0; padding: 10px; margin-top: 10px; border-radius: 4px; }
                    </style>
                </head>
                <body>
            """.trimIndent())

            cells?.forEach { cellElement ->
                val cell = cellElement.asJsonObject
                val cellType = cell.get("cell_type").asString
                
                when (cellType) {
                    "markdown" -> {
                        val content = extractContent(cell.getAsJsonArray("source"))
                        htmlBuilder.append("""<div class="cell markdown-cell">$content</div>""")
                    }
                    "code" -> {
                        val source = extractContent(cell.getAsJsonArray("source"))
                        htmlBuilder.append("""
                            <div class="cell">
                            <div class="cell-type">Code</div>
                            <div class="code-cell">$source</div>
                        """.trimIndent())
                        
                        val outputs = cell.getAsJsonArray("outputs")
                        if (outputs != null && outputs.size() > 0) {
                            htmlBuilder.append("""<div class="output">""")
                            outputs.forEach { output ->
                                val outputContent = extractOutputContent(output.asJsonObject)
                                htmlBuilder.append(outputContent)
                            }
                            htmlBuilder.append("</div>")
                        }
                        htmlBuilder.append("</div>")
                    }
                }
            }

            htmlBuilder.append("</body></html>")
            htmlBuilder.toString()
        } catch (e: Exception) {
            "<html><body><h2>Error parsing notebook</h2><p>${e.message}</p></body></html>"
        }
    }

    private fun extractContent(sourceArray: com.google.gson.JsonArray?): String {
        if (sourceArray == null) return ""
        return sourceArray.joinToString("") { element ->
            element.asString.escapeHtml()
        }
    }

    private fun extractOutputContent(output: JsonObject): String {
        return when (output.get("output_type").asString) {
            "stream" -> {
                val text = output.getAsJsonArray("text")?.joinToString("") { it.asString } ?: ""
                "<pre>${text.escapeHtml()}</pre>"
            }
            "execute_result", "display_data" -> {
                val data = output.getAsJsonObject("data")
                when {
                    data?.has("text/plain") == true -> {
                        "<pre>${data.get("text/plain").asString.escapeHtml()}</pre>"
                    }
                    data?.has("image/png") == true -> {
                        val imageData = data.get("image/png").asString
                        """<img src="data:image/png;base64,$imageData" style="max-width:100%;">"""
                    }
                    else -> ""
                }
            }
            else -> ""
        }
    }

    private fun String.escapeHtml(): String {
        return this.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
