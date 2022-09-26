package io.ak1.pix.adapters

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
        if (items != null) {
            img = items?.list?.get(position)
        }
        return PageFragment.newInstance(img, "2")
    }

    override fun getItemCount(): Int {
        if (items == null) {
            return 0
        }
        return items?.list?.size!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
//    override fun containsItem(itemId: Long): Boolean = items?.list?.contains(itemId.toString())
}