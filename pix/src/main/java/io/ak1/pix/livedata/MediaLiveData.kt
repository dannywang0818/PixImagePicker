package io.ak1.pix.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import io.ak1.pix.models.ModelList

class MediaLiveData(symbol: String) : MutableLiveData<ModelList>() {

//    private val listener = { price: String ->
//        postValue(ModelList())
//    }

    companion object {
        private lateinit var sInstance: MediaLiveData

        @MainThread
        fun get(symbol: String): MediaLiveData {
            sInstance = if (::sInstance.isInitialized) sInstance else MediaLiveData(symbol)
            return sInstance
        }

        const val TAG = "MediaLiveData"
    }
}