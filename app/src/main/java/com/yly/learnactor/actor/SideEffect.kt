package com.yly.learnactor.actor

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * @author    yiliyang
 * @date      2021/8/11 下午4:30
 * @version   1.0
 * @since     1.0
 */
class Repository {

    suspend fun update(count: Int): Boolean {
        TODO("Save to DB")
    }
}

class CounterSideEffect(
    private val repository: Repository,
    private val counterStateStore: CounterStateStore,
) {

    init {
        counterStateStore.messagesFlow
            .onEach(::handle)
            .launchIn(counterStateStore.scope)
    }

    private fun handle(message: CounterMessage) {
        when (message) {
            IncrementCounter -> {

                counterStateStore.scope.launch {
                    val currentState = counterStateStore.getState()
                    repository.update(currentState.count)
                }
            }
            DecrementCounter -> TODO()
        }

    }
}