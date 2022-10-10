package io.ak1.pix.utility

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class PhotoMetadataUtils {

    private fun PhotoMetadataUtils() {
        throw AssertionError("oops! the utility class is about to be instantiated...")
    }



    fun getSizeInMB(sizeInBytes: Long): Float {
        val df = NumberFormat.getNumberInstance(Locale.US) as DecimalFormat
        df.applyPattern("0.0")
        var result = df.format((sizeInBytes.toFloat() / 1024 / 1024).toDouble())
        Log.e(TAG, "getSizeInMB: $result")
        result = result.replace(",".toRegex(), ".") // in some case , 0.0 will be 0,0
        return java.lang.Float.valueOf(result)
    }
    companion object{

        private val TAG = PhotoMetadataUtils::class.java.simpleName
        private val MAX_WIDTH = 1600
        private val SCHEME_CONTENT = "content"

        fun getPixelsCount(resolver: ContentResolver, uri: Uri?): Int {
            val size = getBitmapBound(resolver, uri)
            return size.x * size.y
        }


        fun getBitmapBound(resolver: ContentResolver, uri: Uri?): Point {
            var `is`: InputStream? = null
            return try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                `is` = resolver.openInputStream(uri!!)
                BitmapFactory.decodeStream(`is`, null, options)
                val width = options.outWidth
                val height = options.outHeight
                Point(width, height)
            } catch (e: FileNotFoundException) {
                Point(0, 0)
            } finally {
                if (`is` != null) {
                    try {
                        `is`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        fun getBitmapSize(uri: Uri?, activity: Activity): Point? {
            val resolver = activity.contentResolver
            val imageSize = getBitmapBound(resolver, uri)
            var w = imageSize.x
            var h = imageSize.y
            if (PhotoMetadataUtils.shouldRotate(resolver, uri)) {
                w = imageSize.y
                h = imageSize.x
            }
            if (h == 0) return Point(MAX_WIDTH, MAX_WIDTH)
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            val screenWidth = metrics.widthPixels.toFloat()
            val screenHeight = metrics.heightPixels.toFloat()
            val widthScale = screenWidth / w
            val heightScale = screenHeight / h
            return if (widthScale > heightScale) {
                Point((w * widthScale).toInt(), (h * heightScale).toInt())
            } else Point((w * widthScale).toInt(), (h * heightScale).toInt())
        }

        fun getPath(resolver: ContentResolver, uri: Uri?): String? {
            if (uri == null) {
                return null
            }
            if (SCHEME_CONTENT == uri.scheme) {
                var cursor: Cursor? = null
                return try {
                    cursor = resolver.query(
                        uri, arrayOf(MediaStore.Images.ImageColumns.DATA),
                        null, null, null
                    )
                    if (cursor == null || !cursor.moveToFirst()) {
                        null
                    } else cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
                } finally {
                    cursor?.close()
                }
            }
            return uri.path
        }

        private fun shouldRotate(resolver: ContentResolver, uri: Uri?): Boolean {
            val exif: ExifInterface
            exif = try {
                ExifInterfaceCompat.newInstance(getPath(resolver, uri))
            } catch (e: IOException) {
                Log.e(TAG, "could not read exif info of the image: $uri")
                return false
            }
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            return (orientation == ExifInterface.ORIENTATION_ROTATE_90
                    || orientation == ExifInterface.ORIENTATION_ROTATE_270)
        }
    }
}