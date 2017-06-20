package forTests

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine

open class EmptyContinuation(override val context: CoroutineContext = EmptyCoroutineContext) : Continuation<Any?> {
    companion object : EmptyContinuation()
    override fun resume(data: Any?) {}
    override fun resumeWithException(exception: Throwable) { throw exception }
}

fun builder(c: suspend () -> Unit) {
    c.startCoroutine(EmptyContinuation)
}

class WaitFinish(val sleepTime: Long = 10, val maxAttempts: Int = 500) {
    private @Volatile var finished = false

    fun finish() {
        finished = true
    }

    fun waitEnd() {
        var attempts = 0
        while (!finished) {
            if (attempts > maxAttempts) {
                throw java.lang.IllegalStateException("Too long wait!")
            }
            attempts++
            Thread.sleep(sleepTime)
        }
    }
}