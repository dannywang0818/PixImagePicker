package io.ak1.pix.models

import androidx.lifecycle.*
import io.ak1.pix.helpers.LocalResourceManager
import io.ak1.pix.interfaces.PixLifecycle
import io.ak1.pix.livedata.MediaLiveData
import io.ak1.pix.utility.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.RuntimeException

/**
 * Created By Akshay Sharma on 17,June,2021
 * https://ak1.io
 */


internal class PixViewModel : ViewModel(), PixLifecycle {

    val longSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    val selectionList by lazy { MutableLiveData<MutableSet<Img>>(HashSet()) }
    private val allImagesList by lazy { MediaLiveData.get(MediaLiveData.TAG) }
    val imageList: LiveData<ModelList> = allImagesList
    val callResults by lazy { MutableLiveData<Event<ArrayList<Img>>>() }
    val longSelectionValue: Boolean
        get() {
            return longSelection.value ?: false
        }
    val selectionListSize: Int
        get() {
//            return selectionList.value?.size ?: 0
            return imageList.value!!.selection.size
        }

    private lateinit var options: Options
    fun retrieveImages(localResourceManager: LocalResourceManager) {

        // load image only once
        if (imageList.value != null && imageList.value!!.list != null && imageList.value!!.list!!.size > 0) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val sizeInitial = 100
//            selectionList.value?.clear()
            val modelListPageFirst = localResourceManager.retrieveMedia(
                limit = sizeInitial,
                mode = options.mode
            )
            allImagesList.postValue(
                modelListPageFirst
            )
            val modelList = localResourceManager.retrieveMedia(
                start = sizeInitial + 1,
                mode = options.mode
            )
            if (modelList.list.isNotEmpty()) {
                modelList.addBefore(modelListPageFirst)
                allImagesList.postValue(modelList)
            }
        }
    }


    override fun onImageSelected(element: Img?, position: Int, callback: (Boolean) -> Boolean) {
        if (longSelectionValue) {
            throw RuntimeException("Toto exception: delete selectionList!!!!!!")
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



//            selectionList.value?.apply {
//                if (contains(element)) {
//                    remove(element)
//                    callback(false)
//                } else if (callback(true)) {
//                    element!!.position = (position)
//                    add(element)
//                }
//            }
//            selectionList.postValue(selectionList.value)


            val imgCollection = allImagesList.value

            imgCollection!!.triggerSelection(element, position, callback)
//            allImagesList.postValue(imgCollection!!)
        }
    }

    fun returnObjects() = callResults.postValue(Event(imageList.value!!.selection))

    fun setOptions(options: Options) {
        this.options = options
    }

    fun anySelectedImage(): Boolean {
        val imgCollection = allImagesList.value
        return imgCollection!!.anySelectedImage()
    }

    fun clearImageSelection() {
        val imgCollection = allImagesList.value
        imgCollection!!.clearImageSelection()
    }

    fun selectedImages(): List<Img> {

        val imgCollection = allImagesList.value
        return imgCollection!!.selection
    }

    fun addSelectedImgAtFirst(img: Img) {
        val imgCollection = allImagesList.value
        imgCollection!!.addSelectedImgAtFirst(img)
        allImagesList.postValue(imgCollection!!)
    }

    companion object {

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
