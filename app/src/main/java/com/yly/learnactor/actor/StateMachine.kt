package com.yly.learnactor.actor

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author    yiliyang
 * @date      2021/8/11 下午4:03
 * @version   1.0
 * @since     1.0
 */

fun CoroutineScope.counterStateMachine(
    initialState: CounterState,
    mutableStateFlow: MutableStateFlow<CounterState>,
    mutableMessages: MutableSharedFlow<CounterMessage>,
) = actor<CounterMessage> {
    var state: CounterState = initialState
    channel.consumeEach { message ->
        when (message) {
            is IncrementCounter -> {
                state = state.copy(count = state.count + 1)
                mutableStateFlow.emit(state)
                mutableMessages.emit(message)
            }
            is DecrementCounter -> {
                state = state.copy(count = state.count - 1)
                mutableStateFlow.emit(state)
                mutableMessages.emit(message)
            }
            is GetCounterState -> message.deferred.complete(state)
        }
    }
}


class CounterStateStore(initialState: CounterState, val scope: CoroutineScope) {

    private val mutableStateFlow = MutableStateFlow<CounterState>(initialState)
    private val mutableMessages = MutableSharedFlow<CounterMessage>()
    private val stateMachine =
        scope.counterStateMachine(initialState, mutableStateFlow, mutableMessages)

    val stateFlow: Flow<CounterState> = mutableStateFlow
    val messagesFlow: Flow<CounterMessage> = mutableMessages

    fun dispatch(message: CounterMessage) {
        stateMachine.trySend(message)
    }

    suspend fun getState(): CounterState {
        val completableDeferred = CompletableDeferred<CounterState>()
        dispatch(GetCounterState(completableDeferred))
        return completableDeferred.await()
    }

}