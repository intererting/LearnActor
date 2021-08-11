package com.yly.learnactor

/**
 * @author    yiliyang
 * @date      2021/8/11 下午1:56
 * @version   1.0
 * @since     1.0
 */
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.system.*

// Message types for counterStateMachine
sealed interface CounterMessage
object IncrementCounter : CounterMessage // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMessage // a request with reply

fun CoroutineScope.counterStateMachine() = actor<CounterMessage> {
    var counter = 0 // actor state
    channel.consumeEach { message ->
        when (message) {
            is IncrementCounter -> counter++
            is GetCounter -> message.response.complete(counter)
        }
    }
}

fun main() = runBlocking<Unit> {
    val counterStateMachine = counterStateMachine() // create the counterStateMachine actor
    withContext(Dispatchers.Default) {
        massiveRun {
            counterStateMachine.send(IncrementCounter)
        }
    }
    // send a message to get the counter value from the counterStateMachine actor
    val response = CompletableDeferred<Int>()
    counterStateMachine.send(GetCounter(response))
    val count = response.await()
    println("Counter = ${count}")
    counterStateMachine.close() // shutdown the counterStateMachine actor
}

//Helper function to simulate a massive concurrent input of messages
suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        coroutineScope { // scope for coroutines
            repeat(n) {
                launch {
                    repeat(k) { action() }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}