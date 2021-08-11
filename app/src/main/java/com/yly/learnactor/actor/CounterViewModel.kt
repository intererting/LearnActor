package com.yly.learnactor.actor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

/**
 * @author    yiliyang
 * @date      2021/8/11 下午4:31
 * @version   1.0
 * @since     1.0
 */
class CounterViewModel(private val counterStateStore: CounterStateStore) : ViewModel() {

    val stateFlow: Flow<CounterState> = counterStateStore.stateFlow

    fun dispatch(message: CounterMessage) {
        counterStateStore.dispatch(message)
    }

    override fun onCleared() {
        super.onCleared()
        counterStateStore.scope.cancel()
    }

    companion object {

        fun getViewModel(): CounterViewModel {
            val scope = CoroutineScope(SupervisorJob())
            val counterStateStore = CounterStateStore(CounterState(), scope)
//            val repository = Repository()
//
//            CounterSideEffect(repository, counterStateStore)

            return CounterViewModel(counterStateStore)
        }
    }
}