package io.ak1.pix.ui

import android.os.Bundle
import io.ak1.pix.R
import io.ak1.pix.databinding.ActivityMediaPreviewBinding
import io.ak1.pix.models.Img

class MediaPreviewActivity : BasePreviewActivity<ActivityMediaPreviewBinding>(R.layout.activity_media_preview) {

    val EXTRA_ALBUM = "extra_album"
    val EXTRA_ITEM = "extra_item"
    override val binding: ActivityMediaPreviewBinding by lazy { ActivityMediaPreviewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        mCollection.onCreate(this, this)
//        val album: Album =
//            intent.getParcelableExtra(com.zhihu.matisse.internal.ui.AlbumPreviewActivity.EXTRA_ALBUM)
//        mCollection.load(album)

        val img: Img? =
            intent.getParcelableExtra(EXTRA_ITEM)
//        if (mSpec.countable) {
//            mCheckView.setCheckedNum(mSelectedCollection.checkedNumOf(item))
//        } else {
//            mCheckView.setChecked(mSelectedCollection.isSelected(item))
//        }
//        updateSize(item)
    }


    companion object {
        const val EXTRA_ALBUM = "extra_album"
        const val EXTRA_ITEM = "extra_item"
    }
}