package com.yly.learnactor.actor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author    yiliyang
 * @date      2021/8/11 下午4:32
 * @version   1.0
 * @since     1.0
 */
class CounterActivity : AppCompatActivity() {

//    private val viewModel by viewModels<CounterViewModel>()
//
//    private lateinit var binding: ActivityCounterBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCounterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.buttonDecrement.setOnClickListener {
//            viewModel.dispatch(DecrementCounter)
//        }
//
//        binding.buttonIncrement.setOnClickListener {
//            viewModel.dispatch(IncrementCounter)
//        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.stateFlow.collect(::setupViews)
//        }
//    }
//
//    private fun setupViews(state: CounterState) {
//        binding.textCount.text = state.count.toString()
//    }
}