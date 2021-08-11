package com.yly.learnactor

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.channelFlow

/**
 * @author    yiliyang
 * @date      2021/8/11 下午2:25
 * @version   1.0
 * @since     1.0
 */
fun main() = runBlocking {
//    testUnconfined()

//    testJobScope()

    testScheduler()
    return@runBlocking
}

/**
 * 协程的调用不能保证挂起后还在同一个线程中执行,如果要保证,可以使用start = CoroutineStart.UNDISPATCHED
 */
fun CoroutineScope.testScheduler() {
    launch(Dispatchers.IO) {
        println("1 ${Thread.currentThread()}")
        delay(10)
        println("1 ${Thread.currentThread()}")
    }
    launch(Dispatchers.IO) {
        println("2 ${Thread.currentThread()}")
        delay(20)
        println("2 ${Thread.currentThread()}")
    }
    launch(Dispatchers.IO) {
        println("3 ${Thread.currentThread()}")
        delay(30)
        println("3 ${Thread.currentThread()}")
    }
}

fun CoroutineScope.testJobScope() {
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        // it spawns two other jobs
        launch(Job()) {
            //由于Job的原因,这个地方不会被cancel
            //主要的原因在于Job的实现JobImpl的  init { initParentJob(parent) } 中parent为null
            //相当于开启了一个新的协程树,而直接launch的parent为上一个conroutineScope中的coroutineContext
            //那么当出了异常的时候,那么parent就会cancel掉所有的job,新的Job就不会被cancel
            repeat(10) {
                delay(1000)
                println("repeat job")
            }
        }
        // and the other inherits the parent context
        launch {
            repeat(10) {
                delay(1000)
                println("repeat without job")
            }
        }
    }
    launch {
        delay(3000)
        request.cancel() // cancel processing of the request
//        delay(5000) // delay a second to see what happens }
    }
}

/**
 *
 * Thread[DefaultDispatcher-worker-1,5,main]
 *Thread[DefaultDispatcher-worker-1,5,main]
 *Thread[DefaultDispatcher-worker-1,5,main]
 *Thread[kotlinx.coroutines.DefaultExecutor,5,main]
 *
 * Dispatchers.Unconfined 在协程第一次被挂起之前,是在调用线程开始执行的,但是一旦被挂起恢复,那么就会在其他线程执行
 * start = CoroutineStart.UNDISPATCHED 和 Dispatchers.Unconfined差不多,但是挂起恢复后还能保证在调用线程执行,
 * 根本原理是其他的协程都会使用ContinuationInterceptor切换状态机,但start = CoroutineStart.UNDISPATCHED不会使用,所以效率可能差一点
 *
 */
fun CoroutineScope.testUnconfined() {
    launch(Dispatchers.IO) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            println(Thread.currentThread())
            delay(100)
            println(Thread.currentThread())
        }

        launch(Dispatchers.Unconfined) {
            println(Thread.currentThread())
            delay(100)
            println(Thread.currentThread())
        }
    }
}
