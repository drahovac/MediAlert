package com.bobmitchigan.medialert.viewModel

import com.squareup.sqldelight.db.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

actual open class BaseViewModel {
    private var hasCleared = false

    actual val scope: CoroutineScope by lazy {
        val result = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        if (hasCleared)
            closeWithRuntimeException(result)

        return@lazy result
    }

    protected open fun onCleared() {}

    /**
     * Closes the [viewModelScope] and cancels all its coroutines.
     * Should be called from main thread.
     */
    fun clear() {
        hasCleared = true
        closeWithRuntimeException(scope)
        onCleared()
    }

    companion object {
        private fun closeWithRuntimeException(obj: Any?) {
            if (obj is Closeable) {
                try {
                    obj.close()
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}

class CloseableCoroutineScope(context: CoroutineContext) : CoroutineScope, Closeable {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}
