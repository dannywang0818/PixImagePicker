package io.ak1.pix.adapters

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.ak1.pix.models.Img
import io.ak1.pix.models.ModelList
import io.ak1.pix.ui.fragments.PageFragment

class PreviewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
//    override fun getItemCount(): Int = 10
//
//    override fun createFragment(position: Int): Fragment = PreviewPagerFragment()


    private var items: ModelList? = null

    fun setItems(imageItems: ModelList?) {
        items = imageItems
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): PageFragment {
//        val itemId = items.itemId(position)
//        val itemText = items.getItemById(itemId)

        var img: Img? = null
        if (items != null && items?.list != null) {

            var i = 0
            for (item in items?.list!!) {
                if (item.contentUrl != Uri.EMPTY) {
                    if (i == position) {
                        img = item
                        break
                    } else {
                        i++
                    }
                }
            }
        }
        return PageFragment.newInstance(img, "2")
    }

    override fun getItemCount(): Int {
//        return 100
        if (items == null) {
            return 0
        }
        if (items != null && items?.list != null) {

            var i = 0
            for (item in items?.list!!) {
                if (item.contentUrl != Uri.EMPTY) {
                        i++
                }
            }

            return i
        }
        return 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

//    override fun containsItem(itemId: Long): Boolean {
//        return true
//    }
//= items?.list?.contains(itemId.toString())
}