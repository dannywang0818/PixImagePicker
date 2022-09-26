package io.ak1.pix.models

import androidx.lifecycle.*
import io.ak1.pix.helpers.LocalResourceManager
import io.ak1.pix.interfaces.PixLifecycle
import io.ak1.pix.livedata.MediaLiveData
import io.ak1.pix.utility.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created By Akshay Sharma on 17,June,2021
 * https://ak1.io
 */


internal class PixViewModel : ViewModel(), PixLifecycle {

    val longSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    val selectionList by lazy { MutableLiveData<MutableSet<Img>>(HashSet()) }
    private val allImagesList by lazy { MediaLiveData.get(MediaLiveData.TAG) }
    val imageList: LiveData<ModelList> = allImagesList
    val callResults by lazy { MutableLiveData<Event<MutableSet<Img>>>() }
    val longSelectionValue: Boolean
        get() {
            return longSelection.value ?: false
        }
    val selectionListSize: Int
        get() {
            return selectionList.value?.size ?: 0
        }

    private lateinit var options: Options
    fun retrieveImages(localResourceManager: LocalResourceManager) {
        viewModelScope.launch(Dispatchers.IO) {

            val sizeInitial = 100
            selectionList.value?.clear()
            allImagesList.postValue(
                localResourceManager.retrieveMedia(
                    limit = sizeInitial,
                    mode = options.mode
                )
            )
            val modelList = localResourceManager.retrieveMedia(
//                start = sizeInitial + 1,
                mode = options.mode
            )
            if (modelList.list.isNotEmpty()) {
                allImagesList.postValue(modelList)
            }
        }
    }


    override fun onImageSelected(element: Img?, position: Int, callback: (Boolean) -> Boolean) {
        if (longSelectionValue) {
            selectionList.value?.apply {
                if (contains(element)) {
                    remove(element)
                    callback(false)
                } else if (callback(true)) {
                    element!!.position = (position)
                    add(element)
                }
            }
            selectionList.postValue(selectionList.value)
        } else {
            element!!.position = position
            selectionList.value?.add(element)
            returnObjects()
        }

    }

    override fun onImageLongSelected(element: Img?, position: Int, callback: (Boolean) -> Boolean) {
        if (options.count > 1) {
            // Utility.Companion.vibe(this@Pix, 50)
            longSelection.postValue(true)
            selectionList.value?.apply {
                if (contains(element)) {
                    remove(element)
                    callback(false)
                } else if (callback(true)) {
                    element!!.position = (position)
                    add(element)
                }
            }
            selectionList.postValue(selectionList.value)
        }
    }

    fun returnObjects() = callResults.postValue(Event(selectionList.value ?: HashSet()))

    fun setOptions(options: Options) {
        this.options = options
    }

    companion object{

    }
}

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandledOrReturnNull(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
