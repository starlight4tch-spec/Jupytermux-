package com.jupytermux.kernel

enum class KernelType {
    PYTHON, PYTHON3, JAVASCRIPT, JULIA, R, JAVA, KOTLIN, RUBY, GO, RUST, CPP, CSHARP
}

data class ExecutionRequest(
    val code: String,
    val kernelType: KernelType,
    val timeout: Long = 30000
)

sealed class ExecutionOutput {
    data class StreamOutput(val streamType: String, val text: String) : ExecutionOutput()
    data class DisplayData(val mimeType: String, val data: String) : ExecutionOutput()
    data class ExecuteResult(val data: Map<String, String>) : ExecutionOutput()
    data class ErrorOutput(val ename: String, val evalue: String, val traceback: List<String>) : ExecutionOutput()
}

data class ExecutionResult(
    val executionCount: Int,
    val success: Boolean,
    val outputs: List<ExecutionOutput>,
    val executionTime: Long
)

interface KernelExecutor {
    suspend fun execute(request: ExecutionRequest): ExecutionResult
    suspend fun interrupt()
    fun shutdown()
    fun isAlive(): Boolean
}
