package io.ak1.pix.engine

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.MainThread
import com.squareup.picasso.Picasso
import io.ak1.pix.livedata.MediaLiveData

class PicassoEngine {

    fun loadThumbnail(
        context: Context?,
        resize: Int,
        placeholder: Drawable?,
        imageView: ImageView?,
        uri: Uri?
    ) {
        Picasso.get().load(uri).placeholder(placeholder!!)
            .resize(resize, resize)
            .centerCrop()
            .into(imageView)
    }

    fun loadGifThumbnail(
        context: Context?, resize: Int, placeholder: Drawable?, imageView: ImageView?,
        uri: Uri?
    ) {
        loadThumbnail(context, resize, placeholder, imageView, uri)
    }

    fun loadImage(context: Context?, resizeX: Int, resizeY: Int, imageView: ImageView?, uri: Uri?) {
        Picasso.get().load(uri).resize(resizeX, resizeY).priority(Picasso.Priority.HIGH)
            .centerInside().into(imageView)
    }

    fun loadGifImage(
        context: Context?,
        resizeX: Int,
        resizeY: Int,
        imageView: ImageView?,
        uri: Uri?
    ) {
        loadImage(context, resizeX, resizeY, imageView, uri)
    }

    fun supportAnimatedGif(): Boolean {
        return false
    }


    companion object {
        private lateinit var sInstance: PicassoEngine

        @MainThread
        fun get(): PicassoEngine {
            sInstance = if (::sInstance.isInitialized) sInstance else PicassoEngine()
            return sInstance
        }

        const val TAG = "PicassoEngine"
    }

}