package com.yly.learnactor.actor

import kotlinx.coroutines.CompletableDeferred

/**
 * @author    yiliyang
 * @date      2021/8/11 下午4:03
 * @version   1.0
 * @since     1.0
 */
sealed interface CounterMessage
object IncrementCounter : CounterMessage
object DecrementCounter : CounterMessage
class GetCounterState(val deferred: CompletableDeferred<CounterState>) : CounterMessage
