package com.jupytermux.utils

import java.io.File

object FileUtils {
    fun getNotebooksDirectory(): File {
        return File("/sdcard/Jupytermux/notebooks").apply {
            if (!exists()) mkdirs()
        }
    }

    fun getDownloadsDirectory(): File {
        return File("/sdcard/Download").apply {
            if (!exists()) mkdirs()
        }
    }

    fun listNotebooks(): List<File> {
        val dir = getNotebooksDirectory()
        return if (dir.exists()) {
            dir.listFiles { file -> file.extension == "ipynb" }?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }

    fun copyFile(source: String, destination: String): Boolean {
        return try {
            File(source).copyTo(File(destination), overwrite = true)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun createNotebook(name: String): Boolean {
        return try {
            val file = File(getNotebooksDirectory(), "$name.ipynb")
            val template = """
                {
                    "cells": [],
                    "metadata": {},
                    "nbformat": 4,
                    "nbformat_minor": 4
                }
            """.trimIndent()
            file.writeText(template)
            true
        } catch (e: Exception) {
            false
        }
    }
}
