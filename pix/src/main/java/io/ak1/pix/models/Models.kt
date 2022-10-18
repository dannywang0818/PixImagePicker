package io.ak1.pix.models

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel

import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Created By Akshay Sharma on 17,June,2021
 * https://ak1.io
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Img(
    var headerDate: String = "",
    var contentUrl: Uri = Uri.EMPTY,
    var scrollerDate: String = "",
    var mediaType: Int = 1,
    var position: Int = 0
) : Parcelable {
    @IgnoredOnParcel
    var selected = false

}

@SuppressLint("ParcelCreator")
@Parcelize
class Options : Parcelable {
    var ratio = Ratio.RATIO_AUTO
    var count = 1
    var spanCount = 4
    var path = "Pix/Camera"
    var isFrontFacing = false
    var mode = Mode.All
    var flash = Flash.Auto
    var preSelectedUrls = ArrayList<Uri>()
    var videoOptions: VideoOptions = VideoOptions()
}

@Parcelize
enum class Mode : Parcelable {
    All, Picture, Video
}

@SuppressLint("ParcelCreator")
@Parcelize
class VideoOptions : Parcelable {
    var videoBitrate: Int? = null
    var audioBitrate: Int? = null
    var videoFrameRate: Int? = null
    var videoDurationLimitInSeconds = 10
}

@Parcelize
enum class Flash : Parcelable {
    Disabled, On, Off, Auto
}

@Parcelize
enum class Ratio : Parcelable {
    RATIO_4_3, RATIO_16_9, RATIO_AUTO
}

class ModelList(
    var list: ArrayList<Img> = ArrayList(),
    var selection: ArrayList<Img> = ArrayList()
) {
    fun triggerSelection(element: Img?, position: Int, callback: (Boolean) -> Boolean) {

        selection.apply {
            if (contains(element)) {
                remove(element)
                callback(false)
                list[position].selected = false
            } else if (callback(true)) {
                element!!.position = (position)
                add(element)
                list[position].selected = true
            }
        }

    }

    fun calculateGridPositionFromPagerPosition(pagerPosition: Int): Int {
        var gridPosition = pagerPosition
        for ((i, inItem) in list.withIndex()) {
            if (i == gridPosition) {
                break
            }
            if (inItem.contentUrl == Uri.EMPTY) {
                gridPosition++
            }
        }

        return gridPosition
    }

    fun getImgIndex(img: Img): Int {

        var headerCount = 0
        for ((i, inItem) in list.withIndex()) {
            if (inItem.contentUrl == Uri.EMPTY) {
                headerCount++
            } else if (inItem.contentUrl == img.contentUrl) {
                return i - headerCount
            }
        }
        return -1
    }

}