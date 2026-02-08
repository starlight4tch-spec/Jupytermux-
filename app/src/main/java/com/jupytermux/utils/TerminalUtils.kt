package com.jupytermux.utils

import com.google.gson.JsonObject

object TerminalUtils {
    
    fun parseCommand(command: String): Map<String, String> {
        val parts = command.split(" ", limit = 2)
        return mapOf(
            "program" to (parts.getOrNull(0) ?: ""),
            "arguments" to (parts.getOrNull(1) ?: "")
        )
    }

    fun isPythonCommand(command: String): Boolean {
        return command.startsWith("python") || command.startsWith("python3")
    }

    fun executePythonCommand(code: String): String {
        return try {
            // This would connect to a Python interpreter via Termux
            "Executing: $code"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun formatOutput(output: String): String {
        return output.replace("\n", "<br>")
            .replace(" ", "&nbsp;")
    }
}
