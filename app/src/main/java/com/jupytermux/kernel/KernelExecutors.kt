package com.jupytermux.kernel

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class LocalKernelExecutor(private val kernelType: KernelType) : KernelExecutor {
    private var process: Process? = null
    private var isRunning = false

    override suspend fun execute(request: ExecutionRequest): ExecutionResult {
        val startTime = System.currentTimeMillis()
        return try {
            val outputs = mutableListOf<ExecutionOutput>()
            
            val command = when (kernelType) {
                KernelType.PYTHON, KernelType.PYTHON3 -> listOf("python3", "-c", request.code)
                KernelType.JAVASCRIPT -> listOf("node", "-e", request.code)
                KernelType.JULIA -> listOf("julia", "-e", request.code)
                KernelType.R -> listOf("Rscript", "-e", request.code)
                KernelType.JAVA -> listOf("java", "-jar", "app.jar") // Requires compilation
                KernelType.KOTLIN -> listOf("kotlinc", "-script", "-") // stdin
                KernelType.RUBY -> listOf("ruby", "-e", request.code)
                KernelType.GO -> listOf("go", "run", "-") // stdin
                KernelType.RUST -> listOf("rustc", "-") // stdin
                KernelType.CPP -> listOf("g++", "-xc++", "-") // stdin
                KernelType.CSHARP -> listOf("csharp", "-e", request.code)
                else -> listOf("echo", "Unsupported kernel")
            }

            process = ProcessBuilder(command).redirectErrorStream(true).start()
            isRunning = true

            val completed = process!!.waitFor(request.timeout, TimeUnit.MILLISECONDS)
            
            if (!completed) {
                process?.destroy()
                isRunning = false
                return ExecutionResult(
                    executionCount = 0,
                    success = false,
                    outputs = listOf(ExecutionOutput.ErrorOutput(
                        "TimeoutError",
                        "Code execution timed out after ${request.timeout}ms",
                        emptyList()
                    )),
                    executionTime = System.currentTimeMillis() - startTime
                )
            }

            val reader = BufferedReader(InputStreamReader(process!!.inputStream))
            val output = reader.readText()
            isRunning = false

            if (output.isNotEmpty()) {
                outputs.add(ExecutionOutput.StreamOutput("stdout", output))
            }

            ExecutionResult(
                executionCount = 1,
                success = process!!.exitValue() == 0,
                outputs = outputs,
                executionTime = System.currentTimeMillis() - startTime
            )
        } catch (e: Exception) {
            Log.e("KernelExecutor", "Error executing code", e)
            ExecutionResult(
                executionCount = 0,
                success = false,
                outputs = listOf(ExecutionOutput.ErrorOutput(
                    "Exception",
                    e.message ?: "Unknown error",
                    e.stackTrace.map { it.toString() }
                )),
                executionTime = System.currentTimeMillis() - startTime
            )
        }
    }

    override suspend fun interrupt() {
        process?.destroy()
        isRunning = false
    }

    override fun shutdown() {
        process?.destroy()
        isRunning = false
    }

    override fun isAlive(): Boolean = isRunning
}

class RemoteKernelExecutor(
    private val kernelType: KernelType,
    private val remoteUrl: String,
    private val token: String
) : KernelExecutor {
    
    override suspend fun execute(request: ExecutionRequest): ExecutionResult {
        // Implement REST API calls to remote Jupyter kernel
        return ExecutionResult(
            executionCount = 0,
            success = false,
            outputs = listOf(ExecutionOutput.ErrorOutput("NotImplemented", "Remote kernel not yet implemented", emptyList())),
            executionTime = 0
        )
    }

    override suspend fun interrupt() {
        // Send interrupt to remote kernel
    }

    override fun shutdown() {
        // Close remote connection
    }

    override fun isAlive(): Boolean = true
}
