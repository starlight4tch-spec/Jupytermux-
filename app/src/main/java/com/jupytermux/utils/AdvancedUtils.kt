package com.jupytermux.utils

import android.graphics.Color
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.langs.Language
import io.github.rosemoe.sora.text.Span

object SyntaxHighlighter {
    fun highlightPython(code: String): String {
        val keywords = setOf("def", "class", "if", "else", "elif", "for", "while", "return", "import", "from", "as", "try", "except", "finally", "with", "lambda", "yield", "pass", "break", "continue")
        var highlighted = code
        keywords.forEach { keyword ->
            highlighted = highlighted.replace(
                Regex("\\b$keyword\\b"),
                "<span style='color:#6750a4;font-weight:bold'>$keyword</span>"
            )
        }
        return highlighted
    }

    fun highlightJavaScript(code: String): String {
        val keywords = setOf("const", "let", "var", "function", "async", "await", "return", "if", "else", "for", "while", "switch", "case", "try", "catch", "throw", "class", "extends", "new")
        var highlighted = code
        keywords.forEach { keyword ->
            highlighted = highlighted.replace(
                Regex("\\b$keyword\\b"),
                "<span style='color:#d81b60;font-weight:bold'>$keyword</span>"
            )
        }
        return highlighted
    }

    fun highlightCode(code: String, language: String): String {
        return when (language.lowercase()) {
            "python", "py" -> highlightPython(code)
            "javascript", "js" -> highlightJavaScript(code)
            else -> code
        }
    }
}

object PlotRenderer {
    fun renderPlotFromJson(plotJson: String): String {
        // Use MPAndroidChart to render plots from JSON description
        return "<div id='plot' style='width: 100%; height: 300px;'></div>"
    }

    fun renderMatplotlibOutput(base64Image: String): String {
        return """<img src="data:image/png;base64,$base64Image" style="max-width: 100%;" />"""
    }
}

object Debugger {
    data class BreakPoint(val line: Int, val enabled: Boolean = true)
    
    private val breakpoints = mutableListOf<BreakPoint>()

    fun toggleBreakpoint(line: Int) {
        val existing = breakpoints.find { it.line == line }
        if (existing != null) {
            breakpoints.remove(existing)
        } else {
            breakpoints.add(BreakPoint(line))
        }
    }

    fun getBreakpoints(): List<BreakPoint> = breakpoints.toList()

    fun clearAllBreakpoints() {
        breakpoints.clear()
    }
}
