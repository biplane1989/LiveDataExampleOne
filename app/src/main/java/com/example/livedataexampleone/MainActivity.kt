package com.example.livedataexampleone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    val TAG = "TAG"
    var liveDataA = MutableLiveData<Int>()
    val liveDataB = MutableLiveData<Int>()
    val mediatorLiveData = MediatorLiveData<Int>()

    val liveDataAObserver = Observer<Int> {
        Log.d(TAG, " data A : " + it)
    }
    val liveDataA1Observer = Observer<String> {
        Log.d(TAG, " data A1 : " + it)
    }

    val liveDataBObserver = Observer<Int> {
        Log.d(TAG, "data B : " + it)
    }

    val mediatorLiveDataObserver = Observer<Int> {
        Log.d(TAG, "mediator : " + it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            var count = 0
            while (true) {
                delay(1000)
                liveDataA.value = count
                count++
                liveDataB.value = count + 1
            }
        }

        val data = Transformations.map(liveDataA) { it ->
            convertDataA(it)
        }
        val data1 = Transformations.map(liveDataA) { it ->
            convertDataA1(it)
        }

        val data2 = Transformations.switchMap(liveDataA) {
            converLiveData(it)
        }
//        data.observe(this, liveDataAObserver)
//        data1.observe(this, liveDataA1Observer)
//
//        data2.observe(this, liveDataBObserver)


        mediatorLiveData.addSource(liveDataA) {
            mediatorLiveData.value = it
        }

        mediatorLiveData.addSource(liveDataB) {
            mediatorLiveData.value = it
        }

        mediatorLiveData.observe(this, mediatorLiveDataObserver)
    }

    private fun convertDataA(data: Int): Int {
        return data + 3
    }

    private fun convertDataA1(data: Int): String {
        return data.toString() + " orange"
    }

    fun converLiveData(data: Int): LiveData<Int> {
        liveDataB.value = data
        return liveDataB
    }
}